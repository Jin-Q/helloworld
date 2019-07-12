package com.yucheng.cmis.biz01line.cont.op.ctrlimitcont;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DestroyCtrLimitContOp extends CMISOperation {
	private final String modelId = "CtrLimitCont";
	private final String modelIdPvp = "PvpLoanApp";
	private final String modelIdAcc = "AccView";
	private final String modelIdIqp = "IqpLoanApp";
	private final String modelIdCtr = "CtrLoanCont";

	private final String cont_no_name = "cont_no";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		    
			String cont_no_value = null;
			String serno = "";
			String accStatus = null;
			String bill_no = "";
			String cont_no = "";
			try {
				cont_no_value = (String)context.getDataValue(cont_no_name);
			} catch (Exception e) {}
			if(cont_no_value == null || cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cont_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			//是否存在在途申请
			String conditionIqp = "where limit_cont_no='"+cont_no_value+"' and approve_status in('000','111','991','992')";
			IndexedCollection iCollIqp = dao.queryList(modelIdIqp, conditionIqp, connection);
			for(int i=0;i<iCollIqp.size();i++){
	        	 KeyedCollection kCollIqp = (KeyedCollection)iCollIqp.get(i);
	        	 serno = (String)kCollIqp.getDataValue("serno");
	        	 if(i < iCollIqp.size()-1){
	        		 bill_no += "'"+serno+"',";
	 			 }else{
	 				bill_no += "'"+serno+"'";
	 			 }
	        }
			if(iCollIqp.size()>0){
				context.addDataField("flag", "iqpError");
				context.addDataField("billNo", bill_no);
				return "0";
			}
			//是否存在未签订的合同
			String conditionCtr = "where limit_cont_no='"+cont_no_value+"' and cont_status='100'";
			IndexedCollection iCollCtr = dao.queryList(modelIdCtr, conditionCtr, connection);
			for(int i=0;i<iCollCtr.size();i++){
	        	 KeyedCollection kCollCtr = (KeyedCollection)iCollCtr.get(i);
	        	 cont_no = (String)kCollCtr.getDataValue("cont_no");
	        	 if(i < kCollCtr.size()-1){
	        		 bill_no += "'"+cont_no+"',";
	 			 }else{
	 				bill_no += "'"+cont_no+"'";
	 			 }
	        }
			if(iCollCtr.size()>0){
				context.addDataField("flag", "ctrError");
				context.addDataField("billNo", bill_no);
				return "0";
			}
			 //是否出账
			 String conditionStr = "where cont_no in(select cont_no from ctr_loan_cont where limit_cont_no='"+cont_no_value+"' and cont_status ='200') and approve_status in('000','111','991','992')";//待发起，审批中，重办，打回
	         IndexedCollection iCollPvp = dao.queryList(modelIdPvp, conditionStr, connection);
	         /**业务是否结清,查询台账视图,查询是否有在途出账
	          * 如果accStatus = "error" 则有业务未结清
	          * */
	         String conditionAccView = "where cont_no in(select cont_no from ctr_loan_cont where limit_cont_no='"+cont_no_value+"' and cont_status ='200')";
	         IndexedCollection iCollAcc = dao.queryList(modelIdAcc, conditionAccView, connection);
	         for(int i=0;i<iCollAcc.size();i++){
	        	 KeyedCollection kColl = (KeyedCollection)iCollAcc.get(i);
	        	 String status = (String)kColl.getDataValue("status");
	        	 if(status != "10" && status != "9"){
	        		accStatus = "error";
	        		String bill = (String)kColl.getDataValue("bill_no");
	        		if(i < iCollAcc.size()-1){
	        			 bill_no += "'"+bill+"',";
	 				}else{
	 					bill_no += "'"+bill+"'";
	 				}
	        	 }
	         }
	         if((iCollPvp.size()==0 || iCollPvp == null) && (iCollAcc.size()==0 || iCollAcc == null)){
	        	//跟新合同状态
					KeyedCollection kCollCont = dao.queryDetail(modelId, cont_no_value, connection);
					String end_date = (String)kCollCont.getDataValue("end_date");
					String open_day = (String)context.getDataValue("OPENDAY");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date contEndDate = sdf.parse(end_date);
					Date openDay = sdf.parse(open_day);
					if(contEndDate.after(openDay)){
						kCollCont.setDataValue("cont_status", "500");//撤销操作把合同状态改为中止
						context.addDataField("flag", "stopSuccess");
					}else{
						kCollCont.setDataValue("cont_status", "600");//撤销操作把合同状态改为注销
						context.addDataField("flag", "success");
					}
					dao.update(kCollCont, connection);
					/**-----------------------------------------------------------------------------*/
	         }else{
	        	 if( (iCollPvp.size()==0 || iCollPvp == null) && iCollAcc.size()>0 && iCollAcc != null && !"error".equals(accStatus)){
						//跟新合同状态
						KeyedCollection kCollCont = dao.queryDetail(modelId, cont_no_value, connection);
						String end_date = (String)kCollCont.getDataValue("end_date");
						String open_day = (String)context.getDataValue("OPENDAY");
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Date contEndDate = sdf.parse(end_date);
						Date openDay = sdf.parse(open_day);
						if(contEndDate.after(openDay)){
							kCollCont.setDataValue("cont_status", "500");//撤销操作把合同状态改为中止
							context.addDataField("flag", "stopSuccess");
						}else{
							kCollCont.setDataValue("cont_status", "600");//撤销操作把合同状态改为注销
							context.addDataField("flag", "success");
						}
						dao.update(kCollCont, connection);
						/**-----------------------------------------------------------------------------*/
						
		         }else if((iCollPvp.size()==0 || iCollPvp == null) && iCollAcc.size()>0 && iCollAcc != null && "error".equals(accStatus)){
					 context.addDataField("flag", "accStatusError");
		         }else {
		        	 context.addDataField("flag", "Pvperror");
		         }
	         }
	         context.addDataField("billNo", bill_no);
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
