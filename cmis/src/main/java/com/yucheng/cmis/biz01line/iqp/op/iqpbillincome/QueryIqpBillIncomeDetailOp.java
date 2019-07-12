package com.yucheng.cmis.biz01line.iqp.op.iqpbillincome;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class QueryIqpBillIncomeDetailOp  extends CMISOperation {
	private final String modelId = "IqpBillIncome";
	private final String porder_no_name = "porder_no";
	private boolean updateCheck = false;
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String porder_no_value = null;
			String batchno = null;
			String is_ebill = "";
			try {
				porder_no_value = (String)context.getDataValue(porder_no_name);
				batchno = (String)context.getDataValue("batch_no");
			} catch (Exception e) {}
			if(porder_no_value == null || porder_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+porder_no_name+"] cannot be null!");
		    
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> incomeMap = new HashMap<String,String>();
			incomeMap.put("batch_no",batchno);
			incomeMap.put("porder_no",porder_no_value);
			KeyedCollection kColl = dao.queryDetail(modelId, incomeMap, connection);
			/** 通过汇票号码、批次号码查询业务类型以及票面金额 */
			String porderno = (String)kColl.getDataValue("porder_no");
			KeyedCollection detKColl = dao.queryDetail("IqpBillDetail", porder_no_value, connection);
			if(detKColl!=null&&detKColl.getDataValue("is_ebill")!=null&&!"".equals(detKColl.getDataValue("is_ebill"))){
				is_ebill = detKColl.getDataValue("is_ebill").toString();
			}
			context.put("is_ebill", is_ebill);
			if(porderno != null &&  !"".equals(porderno)){
			}else {
				
				String endDate = "";
				String drftAmt = "";
				String porder_addr = "";
				if(detKColl != null && detKColl.size() > 0){
					drftAmt = (String)detKColl.getDataValue("drft_amt");
					endDate = (String)detKColl.getDataValue("porder_end_date");
					porder_addr = (String)detKColl.getDataValue("porder_addr");//汇票签发地
					kColl.setDataValue("drft_amt", drftAmt);
				}
				
				
				KeyedCollection batKColl = dao.queryDetail("IqpBatchMng", batchno, connection);
				if(batKColl != null && batKColl.size() > 0){
					IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
					.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
					DataSource dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
					
					String bizType = (String)batKColl.getDataValue("biz_type");
					String discDate = (String)batKColl.getDataValue("fore_disc_date");
					String discRate = (String)batKColl.getDataValue("rate");
					String rebuyDate = (String)batKColl.getDataValue("rebuy_date");
					String adjDays = "";
					if("2".equals(porder_addr)){//1:本地/2:异地
						adjDays = "3";//调整天数
					}else{
						adjDays = "0";//调整天数
					}
					kColl.put("adj_days", adjDays);
					if(discRate == null){
						discRate = "0";
					}
					kColl.setDataValue("fore_disc_date", discDate);
					/** 计算贴现天数、回购天数 */
					//计算下一个工作日
					endDate = cmisComponent.getNextWorkDate(endDate, dataSource);
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date discDateHelp = sdf.parse(discDate);
					Date endDateHelp = sdf.parse(endDate);
					/** 计算贴现利息，贴现天数 */
					long discNum = (endDateHelp.getTime()-discDateHelp.getTime())/(24L*60L*60L*1000L);
					if(discNum <= 0){
						discNum = 0;
					}
					double discInt = Double.parseDouble(drftAmt)*(Double.parseDouble(String.valueOf(discNum))+Double.parseDouble(adjDays))*Double.parseDouble(discRate)/360;
					kColl.setDataValue("disc_days", String.valueOf(discNum));
					kColl.setDataValue("int", discInt);
					/** 计算回购利息，回购天数 */
					if("04".equals(bizType)||"02".equals(bizType)){
						if(rebuyDate == null){
							rebuyDate = "";
						}
						kColl.setDataValue("fore_rebuy_date", rebuyDate);
						//计算下一个工作日
						rebuyDate = cmisComponent.getNextWorkDate(rebuyDate, dataSource);
						Date rebuyDateHelp = sdf.parse(rebuyDate);
						long rebuyNum = (rebuyDateHelp.getTime()-discDateHelp.getTime())/(24L*60L*60L*1000L);
						if(rebuyNum <= 0){
							rebuyNum = 0;
						}
						double rebuyInt = Double.parseDouble(drftAmt)*(Double.parseDouble(String.valueOf(rebuyNum))+Double.parseDouble(adjDays))*Double.parseDouble(discRate)/360;
						kColl.put("disc_days", String.valueOf(rebuyNum));
						kColl.put("rebuy_days", String.valueOf(rebuyNum));
						kColl.put("int", rebuyInt);
						kColl.put("rebuy_int", rebuyInt);
					}
					if(!"07".equals(bizType)){
						kColl.setDataValue("disc_rate", discRate);
					}
					kColl.setDataValue("biz_type", bizType);
				}
			}
			
			this.putDataElement2Context(kColl, context);
			
			/**查询付息明细*/
			IndexedCollection PIntIColl = null;
			String condition = " where batch_no = '"+batchno+"' and porder_no = '"+porder_no_value+"'";
			PIntIColl = dao.queryList("IqpBillPintDetail", condition, connection);
			PIntIColl.setName("IqpBillPintDetailList");
			this.putDataElement2Context(PIntIColl, context);
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
