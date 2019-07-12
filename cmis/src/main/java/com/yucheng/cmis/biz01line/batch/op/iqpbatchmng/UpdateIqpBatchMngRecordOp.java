package com.yucheng.cmis.biz01line.batch.op.iqpbatchmng;

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
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class UpdateIqpBatchMngRecordOp extends CMISOperation {
	private final String modelId = "IqpBatchMng";
	private final String relModel = "IqpBatchBillRel";
	private final String detModel = "IqpBillDetail";
	private final String inModel = "IqpBillIncome";
	/**
	 * 保存修改的信息,如果贴现日期有变动要同步更新批次下所有票据的利息
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			DataSource dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
			
			/** 同步更新批次下所有票据日期 */
			String batchno = (String)kColl.getDataValue("batch_no");//批次号码
			String discDate = (String)kColl.getDataValue("fore_disc_date");//贴现日期
			String rebuyDate = (String)kColl.getDataValue("rebuy_date");//回购日期
			String bizType = (String)kColl.getDataValue("biz_type");//业务类型
			String rate = (String)kColl.getDataValue("rate");//转贴现利率
			
			IndexedCollection relIColl = dao.queryList(relModel, " where batch_no = '"+batchno+"'", connection);
			if(relIColl != null && relIColl.size() > 0){
				for(int i=0;i<relIColl.size();i++){
					KeyedCollection relKColl = (KeyedCollection)relIColl.get(i);
					String porderno = (String)relKColl.getDataValue("porder_no");
					/** 通过票据编号查询票据明细表中票据的到期日 */
					KeyedCollection detKColl = (KeyedCollection)dao.queryDetail(detModel, porderno, connection);
					String endDate = (String)detKColl.getDataValue("porder_end_date");//汇票到期日
					String drftAmt = (String)detKColl.getDataValue("drft_amt");//票面金额
					/** 票据编号检索票据收益表中票据收益信息,重新计算票据利率 */
					Map<String,String> incomeMap = new HashMap<String,String>();
					incomeMap.put("batch_no",batchno);
					incomeMap.put("porder_no",porderno);
					KeyedCollection inKColl = (KeyedCollection)dao.queryDetail(inModel, incomeMap, connection);
					/** 重新计算票据收益表,需要通过业务类型判断 */
					if(inKColl != null && ((String)inKColl.getDataValue("porder_no")) != null){
						inKColl.setDataValue("fore_disc_date", discDate);
						inKColl.setDataValue("fore_rebuy_date", rebuyDate);
					
						String discRate = (String)inKColl.getDataValue("disc_rate");//贴现利率
						String rebuyRate = (String)inKColl.getDataValue("rebuy_rate");//回购利率
						String adjDays = (String)inKColl.getDataValue("adj_days");//调整天数
						if(adjDays == null){
							adjDays = "0";
						}
						//计算下一个工作日
						endDate = cmisComponent.getNextWorkDate(endDate, dataSource);
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Date discDateHelp = sdf.parse(discDate);
						Date endDateHelp = sdf.parse(endDate);
						
                        if("04".equals(bizType)|| "02".equals(bizType)){//卖出回购,买入返售
                        	/** 计算回购天数、回购利息 */
    						if(rebuyDate == null || rebuyDate == ""){
    							inKColl.setDataValue("rebuy_days", "0");
    							inKColl.setDataValue("rebuy_int", "0");
    						}else {//卖出回购时，计息天数应该为：回购日期-转贴现日期
    							rebuyDate = cmisComponent.getNextWorkDate(rebuyDate, dataSource);//计算下一个工作日
    							Date rebuyDateHelp = sdf.parse(rebuyDate);
    							long rebuyNum = (rebuyDateHelp.getTime()-discDateHelp.getTime())/(24L*60L*60L*1000L);
    							if(rebuyNum <= 0){
    								rebuyNum = 0;
    							}
    							if(!rate.equals(rebuyRate)){//如果利息计算中的利率不是批次中的转贴现利率，以批次的利率为准
    								rebuyRate = rate;
    								inKColl.setDataValue("rebuy_rate", rate);//更新利率
    							}
    							double rebuyInt = Double.parseDouble(drftAmt)*(Double.parseDouble(String.valueOf(rebuyNum))+Double.parseDouble(adjDays))*Double.parseDouble(rebuyRate)/360;
    							inKColl.setDataValue("rebuy_days", String.valueOf(rebuyNum));
    							inKColl.setDataValue("disc_days", String.valueOf(rebuyNum));
    							inKColl.setDataValue("int", rebuyInt);
    							inKColl.setDataValue("rebuy_int", rebuyInt);
    						}
						}else{
							/** 计算贴现利息，贴现天数 */
							long discNum = (endDateHelp.getTime()-discDateHelp.getTime())/(24L*60L*60L*1000L);
							if(discNum <= 0){
								discNum = 0;
							}
							
							if(!"07".equals(bizType)){
								adjDays = "0";
								if(!rate.equals(discRate)){//如果利息计算中的利率不是批次中的转贴现利率，以批次的利率为准
									discRate = rate;
									inKColl.setDataValue("disc_rate", rate);//更新利率
								}
							}else{
								if("".equals(discRate) || discRate == null){
									discRate = "0";
								}
							}
							double discInt = Double.parseDouble(drftAmt)*(Double.parseDouble(String.valueOf(discNum))+Double.parseDouble(adjDays))*Double.parseDouble(discRate)/360;
							inKColl.setDataValue("disc_days", String.valueOf(discNum));
							inKColl.setDataValue("int", discInt);
						}
						
						int upcount = dao.update(inKColl, connection);
						if(upcount!=1){
							throw new EMPException("Update Failed! Record Count: " + upcount);
						}
					}
				}
				
				/** 通过批次号、票据编号重新统计票据批次表中信息 ，封装需要查询的票据信息SQL */
				String porderSQLHelp = " where porder_no in (";
				for(int i=0;i<relIColl.size();i++){
					KeyedCollection kc = (KeyedCollection)relIColl.get(i);
					String porderno = (String)kc.getDataValue("porder_no");
					porderSQLHelp = porderSQLHelp+"'"+porderno+"',";
				}
				porderSQLHelp = porderSQLHelp.substring(0, porderSQLHelp.length()-1)+") ";
				/** 计算票据总金额 */
				double billAmt = 0;
				IndexedCollection biIColl = dao.queryList(detModel, porderSQLHelp, connection);
				if(biIColl != null && biIColl.size() > 0){
					int billNum = biIColl.size();
					for(int i=0;i<biIColl.size();i++){
						KeyedCollection kc = (KeyedCollection)biIColl.get(i);
						String amt = (String)kc.getDataValue("drft_amt");
						billAmt += Double.parseDouble(amt);
					}
					kColl.setDataValue("bill_qnt", billNum);//票据数量
					kColl.setDataValue("bill_total_amt", billAmt);//票据总金额
				}
				
				/** 计算票据利息,回购利息 */
				double intAmt = 0;
				double rbuyAmt = 0;
				String incomestr = porderSQLHelp + " and batch_no = '"+batchno+"'";
				IndexedCollection inIColl = dao.queryList("IqpBillIncome", incomestr, connection);
				if(inIColl != null && inIColl.size() > 0){
					for(int i=0;i<inIColl.size();i++){
						KeyedCollection kc = (KeyedCollection)inIColl.get(i);
						String amt = (String)kc.getDataValue("int");
						String ramt = (String)kc.getDataValue("rebuy_int");
						if(ramt == null){
							ramt = "0";
						}
						intAmt += Double.parseDouble(amt);
						rbuyAmt += Double.parseDouble(ramt);
					}
					kColl.setDataValue("int_amt", intAmt);//票据利息
					kColl.setDataValue("rebuy_int", rbuyAmt);//回购利息
				}
				kColl.setDataValue("rpay_amt", billAmt-intAmt-rbuyAmt);//实付金额
				
				dao.update(kColl, connection);
			}else {
				/** 不做任何处理*/
			}
			context.addDataField("flag", "success");
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");
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
