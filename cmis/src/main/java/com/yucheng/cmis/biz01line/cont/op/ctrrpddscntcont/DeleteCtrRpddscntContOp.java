package com.yucheng.cmis.biz01line.cont.op.ctrrpddscntcont;

import java.sql.Connection;
import java.util.Iterator;


import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class DeleteCtrRpddscntContOp extends CMISOperation {
	private final String modelId = "CtrRpddscntCont";
	private final String modelIdPvp = "PvpLoanApp";
	private final String modelIdAcc = "AccLoan";
	private final String modelIdAuthorize = "PvpAuthorize";

	private final String cont_no_name = "cont_no";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String cont_no_value = null;
			try {
				cont_no_value = (String)context.getDataValue(cont_no_name);
			} catch (Exception e) {}
			if(cont_no_value == null || cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cont_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			 //查询出账表
			//'000':'待发起', '111':'审批中', '990':'取消', '991':'重办', '992':'打回', '993':'追回', '997':'通过', '998':'否决'
			 String conditionPvp = "where cont_no='"+cont_no_value+"' and approve_status in('111','997')";
	         IndexedCollection iCollPvp = dao.queryList(modelIdPvp, conditionPvp, connection);
	         String conditionAcc = "where cont_no='"+cont_no_value+"'";
	         //查询台账表(贷款意向，信托贷款不需要出账)
	         IndexedCollection iCollAcc = dao.queryList(modelIdAcc, conditionAcc, connection);
	         if((iCollPvp.size()>0 && iCollPvp != null) || (iCollAcc.size()>0 && iCollAcc != null)){
	        	 String conditionPvpSpe = "where cont_no='"+cont_no_value+"' and approve_status='111'";
	        	 IndexedCollection iCollPvpSpe = dao.queryList(modelIdPvp, conditionPvpSpe, connection);
	        	 if(iCollPvpSpe.size()>0 && iCollPvpSpe != null){
	        		 context.put("flag", "error");
	        		 context.put("msg", "存在审批中出账记录!");
	        	 }else{
	        		 //再判断授权全部撤销,如果全部授权撤销则允许合同撤销
		        	 IndexedCollection iColl = dao.queryList(modelIdAuthorize, conditionAcc, connection);
		        	 int isDisfrock = 0;
		        	 for(Iterator iterator = iColl.iterator();iterator.hasNext();){
		        		 KeyedCollection kColl = (KeyedCollection)iterator.next();
		        		 String status = (String)kColl.getDataValue("status");
		        		 if(!"03".equals(status)){
		        			 isDisfrock +=1;
		        		 }
		        	 }
		        	 if(isDisfrock == 0){
		        		//跟新合同状态
		 	            KeyedCollection kColl = dao.queryDetail(modelId, cont_no_value, connection);
		 				kColl.setDataValue("cont_status", "700");//撤销操作把合同状态改为撤销
		 				dao.update(kColl, connection);
		 				
		 				String serno = (String) kColl.getDataValue("serno");
						KeyedCollection kCollForBatch = null;
						String conditionstr = " where serno='"+serno+"'";
						kCollForBatch=dao.queryFirst("IqpBatchMng", null, conditionstr, connection);
						if(kCollForBatch.getDataValue("batch_no")!=null&&!(kCollForBatch.getDataValue("batch_no")).equals("")){
							kCollForBatch.setDataValue("status", "04");//改回【作废】状态
							dao.update(kCollForBatch, connection);
						}
						//如果存在出账记录，前提是 没有 '111':'审批中'(前面已判断)
						//则把该出账记录改为 取消 (否决的不用改)
						String conditionStr = "where cont_no='"+cont_no_value+"' and approve_status!='998'";
				        IndexedCollection iCollPvpNeedChange = dao.queryList(modelIdPvp, conditionStr, connection);
				        for(int i=0;i<iCollPvpNeedChange.size();i++){
				        	KeyedCollection kCollPvpNeedChange = (KeyedCollection)iCollPvpNeedChange.get(i);
				        	kCollPvpNeedChange.setDataValue("approve_status", "990");
				        	dao.update(kCollPvpNeedChange, connection);
				        }
						context.put("flag", "success");
						context.put("msg", "合同撤销成功!");
		        	 }else{
		        		 context.put("flag", "error");
		        		 context.put("msg", "存在审批中、通过状态出账或台账记录!");
		        	 }
	        	 }
	         }else{
				//跟新合同状态
	            KeyedCollection kColl = dao.queryDetail(modelId, cont_no_value, connection);
				kColl.setDataValue("cont_status", "700");//撤销操作把合同状态改为撤销
				dao.update(kColl, connection);
				
				String serno = (String) kColl.getDataValue("serno");
				KeyedCollection kCollForBatch = null;
				String conditionstr = " where serno='"+serno+"'";
				kCollForBatch=dao.queryFirst("IqpBatchMng", null, conditionstr, connection);
				if(kCollForBatch.getDataValue("batch_no")!=null&&!(kCollForBatch.getDataValue("batch_no")).equals("")){
					kCollForBatch.setDataValue("status", "04");//改【作废】状态
					dao.update(kCollForBatch, connection);
				}
				
				//如果存在出账记录，前提是 没有 '111':'审批中', '997':'通过'(前面已判断)
				//则把该出账记录改为 取消 (否决的不用改)
				String conditionStr = "where cont_no='"+cont_no_value+"' and approve_status!='998'";
		        IndexedCollection iCollPvpNeedChange = dao.queryList(modelIdPvp, conditionStr, connection);
		        for(int i=0;i<iCollPvpNeedChange.size();i++){
		        	KeyedCollection kCollPvpNeedChange = (KeyedCollection)iCollPvpNeedChange.get(i);
		        	kCollPvpNeedChange.setDataValue("approve_status", "990");
		        	//同时更新流程节点状态，防止代办事项查询到  add by zhaozq 20140928 start
		        	String pvpserno = (String) kCollPvpNeedChange.getDataValue("serno");
		        	SqlClient.executeUpd("updateWfstatusForCont", pvpserno, "4", null , connection);
		        	//同时更新流程节点状态，防止代办事项查询到  add by zhaozq 20140928 end
		        	dao.update(kCollPvpNeedChange, connection);
		        }
				context.put("flag", "success");
				context.put("msg", "合同撤销成功!");
	         }
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
