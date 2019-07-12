package com.yucheng.cmis.biz01line.iqp.op.iqpbatchmng;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class UpdateIqpBillIncomeIntOp extends CMISOperation {
	private final String relModel = "IqpBatchBillRel";
	private final String inModel = "IqpBillIncome";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			/** 同步更新批次下所有票据日期 */
			IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			TableModelDAO dao = this.getTableModelDAO(context);
			
			String batchno = (String)context.getDataValue("batch_no");
			String discDate = (String)context.getDataValue("fore_disc_date");
			IndexedCollection relIColl = dao.queryList(relModel, " where batch_no = '"+batchno+"'", connection);
			if(relIColl != null && relIColl.size() > 0){
				for(int i=0;i<relIColl.size();i++){
					KeyedCollection relKColl = (KeyedCollection)relIColl.get(i);
					String porderno = (String)relKColl.getDataValue("porder_no");
					/** 通过批次号、票据编号检索票据收益表中票据收益信息,重新计算票据利率 */
					Map<String,String> param = new HashMap();
					param.put("batch_no", batchno);
					param.put("porder_no", porderno);
					KeyedCollection inKColl = (KeyedCollection)dao.queryDetail(inModel, param, connection);
					/** 通过业务类型判断利率的计算方式（非卖出回购，一律计算贴现利息） */
					String bizType = "";
					if(context.containsKey("bizType")){
						bizType = (String)context.getDataValue("bizType");
					}
					if(bizType != null && bizType != ""){
						String billAmt = (String)inKColl.getDataValue("drft_amt");
						if("04".equals(bizType)){
							String discDays = (String)inKColl.getDataValue("disc_days");
							String adjDays = (String)inKColl.getDataValue("adj_days");
							String discRate = (String)inKColl.getDataValue("disc_rate");
							double discInt = Double.parseDouble(billAmt)*(Double.parseDouble(discDays)+Double.parseDouble(adjDays))*Double.parseDouble(discRate)/360;
							inKColl.setDataValue("int", discInt);
						}else {
							String rebuyDays = (String)inKColl.getDataValue("rebuy_days");
							String rebuyRate = (String)inKColl.getDataValue("rebuy_rate");
							double rebuyInt = Double.parseDouble(billAmt)*Double.parseDouble(rebuyDays)*Double.parseDouble(rebuyRate)/360;
							inKColl.setDataValue("rebuy_int", rebuyInt);
						}
						int upcount = dao.update(inKColl, connection);
						if(upcount!=1){
							throw new EMPException("Update Failed! Record Count: " + upcount);
						}
					}else {
						throw new Exception("获取票据业务类型出错！");
					}
					
					/** 通过批次号、票据编号更新票据收益表中票据收益信息 */
					//int result = cmisComponent.updateIqpBillIncomeRateByDate(batchno, porderno, discDate);
				}
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
		
		
		return null;
	}

}
