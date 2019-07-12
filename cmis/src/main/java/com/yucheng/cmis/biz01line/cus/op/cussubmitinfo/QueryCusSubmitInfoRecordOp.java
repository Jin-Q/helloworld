package com.yucheng.cmis.biz01line.cus.op.cussubmitinfo;

import java.sql.Connection;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryCusSubmitInfoRecordOp extends CMISOperation {

	private final String modelId = "CusSubmitInfo";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String cus_id = "";
		String condition = "";
		String back = "";
		String checkRestrict = "";
		try{
			context.addDataField("flag","");
			connection = this.getConnection(context);
			try {
				cus_id = (String)context.getDataValue("cus_id");
				if(context.containsKey("checkRestrict")){
					checkRestrict = (String)context.getDataValue("checkRestrict");
				}
				back = (String)context.getDataValue("back");
			} catch (Exception e) {}
			if(cus_id == null || cus_id.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id+"] cannot be null!");
				
			String currentUserId = (String)context.getDataValue("currentUserId");//当前登录人
			//查询是否有未完成的记录，有表示正在进行信息补录或影像扫描
			if( !"".equals( back )){
				//打回
				condition = " where cus_id='" + cus_id + "' and end_flag='1' and opr_type ='2'";
				String condition2 = " where cus_id='" + cus_id + "' and end_flag='1' and opr_type in('1','3') and rcv_id='"+currentUserId+"'";
				TableModelDAO dao = this.getTableModelDAO(context);
				IndexedCollection iColl = dao.queryList(modelId, condition, connection);
				IndexedCollection iColl2 = dao.queryList(modelId, condition2, connection);
				
				if(iColl.size()>0){
					context.setDataValue("flag", "0");	//已经打回
				}else if(iColl2.size() == 1 && iColl.size() == 0){
					context.setDataValue("flag", "1");  //可以打回
				}else if( iColl.size() == 0 && iColl2.size() == 0){
					context.setDataValue("flag", "2");  //请先提交
				}
			}else if(!"".equals(checkRestrict)){//20140820 Edited by FCL 
				condition = " where cus_id='" + cus_id + "' and end_flag ='1'";
				TableModelDAO dao = this.getTableModelDAO(context);
				IndexedCollection iColl = dao.queryList(modelId, condition, connection);
				if(iColl.size()==1){
					for (Iterator iterator = iColl.iterator(); iterator.hasNext();) {
						KeyedCollection object = (KeyedCollection) iterator.next();
						String rcdId = (String)object.getDataValue("rcv_id");
						if(!currentUserId.equals(rcdId)){
							context.setDataValue("flag", "0");//接收人与当前用户不一致
						}else{
							context.setDataValue("flag", "1");//接收人与当前用户一致
						}
					}
				}
			}else{
															//未完成 		操作类型(1.提交 2.打回 3.移交)
				condition = " where cus_id='"+cus_id+"' and end_flag='1' and opr_type='1'";
				//需要判断是否是第一次提交
				String condition2 = " where cus_id='" + cus_id + "' and end_flag='1' and opr_type='2'";
				TableModelDAO dao = this.getTableModelDAO(context);
				IndexedCollection iColl = dao.queryList(modelId, condition, connection);
				IndexedCollection iColl2 = dao.queryList(modelId, condition2, connection);
				if(iColl.size()>0){
					context.setDataValue("flag", "0");	//已经存在
				}else if( iColl.size() == 0 && iColl2.size() >0){
					context.setDataValue("flag", "1"); //不是首次提交
				}else {
					context.setDataValue("flag", "2" );
				}
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
