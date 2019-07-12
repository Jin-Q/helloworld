package com.yucheng.cmis.biz01line.cont.op.ctrrpddscntcont;

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

public class DestroyCtrRpddscntContOp extends CMISOperation {
	private final String modelId = "CtrRpddscntCont";
	private final String modelIdPvp = "PvpLoanApp";
	private final String modelIdAcc = "AccView";

	private final String cont_no_name = "cont_no";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String cont_no_value = null;
			String accStatus = null;
			String bill_no = "";
			try {
				cont_no_value = (String)context.getDataValue(cont_no_name);
			} catch (Exception e) {}
			if(cont_no_value == null || cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cont_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			 //是否出账
			 String conditionStr = "where cont_no='"+cont_no_value+"' and approve_status in('000','111','991','992')";//待发起，审批中，重办，打回
	         IndexedCollection iCollPvp = dao.queryList(modelIdPvp, conditionStr, connection);
	         /**业务是否结清,查询台账视图,查询是否有在途出账
	          * 如果accStatus = "error" 则有业务未结清
	          * */
	         String conditionAccView = "where cont_no='"+cont_no_value+"'";
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
	         if( (iCollPvp.size()==0 || iCollPvp == null) && iCollAcc.size()>0 && iCollAcc != null && !"error".equals(accStatus)){
					//跟新合同状态
					KeyedCollection kCollCont = dao.queryDetail(modelId, cont_no_value, connection);
					String rpddscnt_date = (String)kCollCont.getDataValue("rpddscnt_date");
					String open_day = (String)context.getDataValue("OPENDAY");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date contEndDate = sdf.parse(rpddscnt_date);
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
