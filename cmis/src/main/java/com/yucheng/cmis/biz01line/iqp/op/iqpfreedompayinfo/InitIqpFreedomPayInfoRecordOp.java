package com.yucheng.cmis.biz01line.iqp.op.iqpfreedompayinfo;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.TimeUtil;

public class InitIqpFreedomPayInfoRecordOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "IqpFreedomPayInfo";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno = (String) context.getDataValue("serno");
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			String modiflg ="";
			String modify_rel_serno ="";
			if(context.containsKey("modiflg")){
				modiflg = (String) context.getDataValue("modiflg");
			}
			if(context.containsKey("modify_rel_serno")){
				modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
			}
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
				try {
					SqlClient.executeUpd("deletePayPlanTmp", modify_rel_serno, null, null, connection);//删除
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}else{
				//先删除
				SqlClient.executeUpd("deletePayPlan", serno, null, null, connection);
			}
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
			
			//初始化还款计划
			String apply_date="";
			String interest_term="";
			String repay_date="";
			String termType="";
			String term="";
			
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
				KeyedCollection ctltkc = dao.queryDetail("CtrLoanContTmp", modify_rel_serno, connection);
				KeyedCollection ctlstkc = dao.queryDetail("CtrLoanContSubTmp", modify_rel_serno, connection);
				apply_date = (String) ctltkc.getDataValue("cont_start_date");
			    interest_term = (String) ctlstkc.getDataValue("interest_term");
				repay_date = (String) ctlstkc.getDataValue("repay_date");
				termType = (String) ctlstkc.getDataValue("term_type");
				term = (String) ctlstkc.getDataValue("cont_term");
				
			}else{
				KeyedCollection iqpkc = dao.queryDetail("IqpLoanApp", serno, connection);
				KeyedCollection subkc = dao.queryDetail("IqpLoanAppSub", serno, connection);
				apply_date = (String) iqpkc.getDataValue("apply_date");
			    interest_term = (String) subkc.getDataValue("interest_term");
				repay_date = (String) subkc.getDataValue("repay_date");
				termType = (String) subkc.getDataValue("term_type");
				term = (String) subkc.getDataValue("apply_term");
			}
			
			
			
			
			
			/**计算合同到期日*/
		    String type = "";
		    if("001".equals(termType)){
		    	type = "Y";
		    }else if("002".equals(termType)){
		    	type = "M";
		    }else if("003".equals(termType)){
		    	type = "D";
		    }
			String contract_expiry_date = DateUtils.getAddDate(type, apply_date, Integer.parseInt(term));//合同到期日
			Map payPlanMap=new HashMap();
			payPlanMap = this.getPayPlan(apply_date, contract_expiry_date, interest_term, repay_date);
			
			for(int i=1;i<=payPlanMap.size();i++){
				KeyedCollection kColl = new KeyedCollection(modelId);
				/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
				if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
					kColl.setName("IqpFreedomPayInfoTmp");
					kColl.addDataField("modify_rel_serno", modify_rel_serno);
				}
				/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
				kColl.addDataField("serno", serno);
				kColl.addDataField("dateno", i);
				kColl.addDataField("suppose_pay_cap", "0");
				kColl.addDataField("pay_date", payPlanMap.get(i));
				
				dao.insert(kColl, connection);
			}
			
          	context.addDataField("flag", "success");
			context.addDataField("msg", "");
		}catch (EMPException ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "初始化失败！失败原因："+ee.getMessage());
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
	
	private Map getPayPlan(String startDate,String endDate,String interest_term,String day) throws Exception{
		if(interest_term==null||"".equals(interest_term)){
			throw new Exception("获取不到计息周期！");
		}
		if(startDate==null||"".equals(startDate)||startDate.length()!=10){
			throw new Exception("获取不到开始日期或者日期格式错误！");
		}
		if(endDate==null||"".equals(endDate)||endDate.length()!=10){
			throw new Exception("获取不到结束日期或者日期格式错误！");
		}
		if(day==null||"".equals(day)||day.length()!=2){
			throw new Exception("获取不到还款日或者还款日格式错误！");
		}
		
		if(TimeUtil.compareDate(startDate, endDate)>=0){
			throw new Exception("开始日期大于结束日期！");
		}
		
		int startday_int = Integer.parseInt(startDate.substring(8, 10));
		if(startday_int>27){
			startDate = startDate.substring(0,8)+"27";
		}
		int endday_int = Integer.parseInt(endDate.substring(8, 10));
		if(endday_int>27){
			endDate = endDate.substring(0, 8)+"27";
		}
		
		Map payPlanMap=new HashMap();
		int n = 1;
		String repay_date = "";
		//计息周期：0-按年、1-按季、2-按月
		if("0".equals(interest_term)){
			repay_date = startDate.substring(0, 8) + day;
			if(TimeUtil.compareDate(startDate, repay_date)>=0){
				//payPlanMap.put(n, startDate);
				repay_date = TimeUtil.ADD_YEAR(repay_date, 1);
				//n++;
			}else{
				//payPlanMap.put(n, startDate);
				payPlanMap.put(n, repay_date);
				repay_date = TimeUtil.ADD_YEAR(repay_date, 1);
				n++;
			}
			while(TimeUtil.compareDate(repay_date, endDate)<0){
				payPlanMap.put(n, repay_date);
				repay_date = TimeUtil.ADD_YEAR(repay_date, 1);
				n++;
			}
			payPlanMap.put(n, endDate);
		}else if("1".equals(interest_term)){
			String mon = startDate.substring(5, 7);
			int mon_int = Integer.parseInt(mon);
			if(mon_int<=3){
				repay_date = startDate.substring(0, 4)+"-03-"+day;
			}else if(mon_int>3&&mon_int<=6){
				repay_date = startDate.substring(0, 4)+"-06-"+day;
			}else if(mon_int>6&&mon_int<=9){
				repay_date = startDate.substring(0, 4)+"-09-"+day;
			}else{
				repay_date = startDate.substring(0, 4)+"-12-"+day;
			}
			
			if(TimeUtil.compareDate(startDate, repay_date)>=0){
				//payPlanMap.put(n, startDate);
				repay_date = TimeUtil.ADD_MONTH(repay_date, 3);
				//n++;
			}else{
				//payPlanMap.put(n, startDate);
				payPlanMap.put(n, repay_date);
				repay_date = TimeUtil.ADD_MONTH(repay_date, 3);
				n++;
			}
			while(TimeUtil.compareDate(repay_date, endDate)<0){
				payPlanMap.put(n, repay_date);
				repay_date = TimeUtil.ADD_MONTH(repay_date, 3);
				n++;
			}
			payPlanMap.put(n, endDate);
		}else if("2".equals(interest_term)){
			repay_date = startDate.substring(0, 8) + day;
			if(TimeUtil.compareDate(startDate, repay_date)>=0){
				//payPlanMap.put(n, startDate);
				repay_date = TimeUtil.ADD_MONTH(repay_date, 1);
				//n++;
			}else{
				//payPlanMap.put(n, startDate);
				payPlanMap.put(n, repay_date);
				repay_date = TimeUtil.ADD_MONTH(repay_date, 1);
				n++;
			}
			while(TimeUtil.compareDate(repay_date, endDate)<0){
				payPlanMap.put(n, repay_date);
				repay_date = TimeUtil.ADD_MONTH(repay_date, 1);
				n++;
			}
			payPlanMap.put(n, endDate);
		}
		
		return payPlanMap;
	}
}
