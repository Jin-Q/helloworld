package com.yucheng.cmis.biz01line.lmt.op.jointguar.pubbailinfo_joint;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class AddPubBailInfo_jointDataOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "PubBailInfo";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String cus_id = null;
		String serno = null;
		String cont_no = null;
		try{
			connection = this.getConnection(context);
			try{
				serno = (String) context.getDataValue("serno");
				context.setDataValue("serno", serno);
			}catch(Exception e){}
				
			try{
				cus_id = (String) context.getDataValue("cus_id");
				context.setDataValue("cus_id", cus_id);
			}catch(Exception e){}
			
			try{
				cont_no = (String) context.getDataValue("cont_no");
			}catch(Exception e){}

			KeyedCollection kColl = new KeyedCollection(modelId);
			kColl.put("serno", serno);
			kColl.put("cus_id", cus_id);
			kColl.put("cont_no", cont_no);
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			this.putDataElement2Context(kColl, context);
			
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
