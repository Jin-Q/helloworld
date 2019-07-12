package com.yucheng.cmis.biz01line.cont.op.ctrassetstrsfcont;

import java.sql.Connection;


import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DestroyCtrAssetstrsfContOp extends CMISOperation {
	private final String modelId = "CtrAssetstrsfCont";
	private final String modelIdPvp = "PvpLoanApp";
	private final String modelIdAcc = "AccView";
	private final String modelIdAuthorize = "PvpAuthorize";
	private final String modelIdAsset= "IqpAsset";

	private final String cont_no_name = "cont_no";
	
    
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String cont_no_value = null;
			String accStatus = null;
			String PvpAuthStatus = null;
			String bill_no = "";
			String bill_no_authorize = "";
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
	        	 if(!"10".equals(status) && !"9".equals(status)){
	        		accStatus = "error";
	        		String bill = (String)kColl.getDataValue("bill_no");
	        		if(i < iCollAcc.size()-1){
	        			 bill_no += "'"+bill+"',";
	 				}else{
	 					bill_no += "'"+bill+"'";
	 				}
	        	 }
	         }
	         //判断授权是否未发送
	         //'00':'未授权', '01':'授权失败', '02':'已授权', '03':'授权已撤销', '04':'授权已确认', '05':'等待通知'
	         IndexedCollection iCollAuthorize = dao.queryList(modelIdAuthorize, conditionAccView, connection);
	         for(int i=0;i<iCollAuthorize.size();i++){
	        	 KeyedCollection kColl = (KeyedCollection)iCollAuthorize.get(i);
	        	 String status = (String)kColl.getDataValue("status");
	        	 if(!"03".equals(status) && !"04".equals(status)){
	        		PvpAuthStatus = "error";
	        		String bill = (String)kColl.getDataValue("bill_no");
	        		if(i < iCollAuthorize.size()-1){
	        			 bill_no_authorize += "'"+bill+"',";
	 				}else{
	 					bill_no_authorize += "'"+bill+"'";
	 				}
	        	 }
	         }
	         //判断条件1.如果存在在途的出账,不让注销
	         if(iCollPvp.size()>0 && iCollPvp != null){
	        	 context.put("flag", "Pvperror");
	        	 context.put("billNo", bill_no);
	        	 return "0";
	         }
	         //判断条件2.如果存在有效的台账不让注销
	         if(iCollAcc.size()>0 && iCollAcc != null && "error".equals(accStatus)){
	        	 context.put("flag", "accStatusError");
	        	 context.put("billNo", bill_no);
	        	 return "0";
	         }
	         //判断条件3.如果存在授权未发送
	         if("error".equals(PvpAuthStatus)){
	        	 context.put("flag", "pvpAuthorizeStatusError");
	        	 context.put("billNo", bill_no_authorize);
	        	 return "0";
	         }
	         /**-----------------------------------------------------------------------------*/
				//跟新合同状态
				KeyedCollection kCollCont = dao.queryDetail(modelId, cont_no_value, connection);
				kCollCont.put("cont_status", "600");//撤销操作把合同状态改为注销
				context.put("flag", "success");
				dao.update(kCollCont, connection);
				
				//跟新资产包状态
				String addet_no = (String)kCollCont.getDataValue("asset_no");//资产包编号
				if(addet_no != null && !"".equals(addet_no)){
					KeyedCollection kCollAsset = dao.queryDetail(modelIdAsset, addet_no, connection);
					kCollAsset.put("status", "03");
					dao.update(kCollAsset, connection);
					//'01':'登记', '02':'已引用', '03':'已办结','04作废'
				}
				/**-----------------------------------------------------------------------------*/
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
