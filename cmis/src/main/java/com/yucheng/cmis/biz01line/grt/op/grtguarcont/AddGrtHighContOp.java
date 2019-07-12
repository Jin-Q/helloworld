package com.yucheng.cmis.biz01line.grt.op.grtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class AddGrtHighContOp  extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String cus_id="";
		KeyedCollection kColl = new KeyedCollection("GrtGuarCont");
		try{
			connection = this.getConnection(context);
			//获取客户编码，供新增时过滤使用
//			cus_id =(String)context.getDataValue("cus_id");
//			kColl.addDataField("cus_id",cus_id);
			kColl.addDataField("guar_model","00");  //担保模式  -默认为普通
			if(context.containsKey("rel") && "sxRel".equals(context.getDataValue("rel"))){
				kColl.addDataField("limit_code",context.getDataValue("limit_code"));  //授信额度编码
			}
//			String[] args=new String[] { "cus_id" };
//			String[] modelIds=new String[]{"CusBase"};
//			String[] modelForeign=new String[]{"cus_id"};
//			String[] fieldName=new String[]{"cus_name"};
//			//详细信息翻译时调用			
//			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
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
