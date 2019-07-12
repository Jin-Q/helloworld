package com.yucheng.cmis.biz01line.lmt.op.rlmtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddLmtZGEContForLmtOp  extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			if(context.containsKey("type")){
				if("00".equalsIgnoreCase((String)context.getDataValue("type"))){   //一般担保合同
					context.put("grt_type", "YB");
				}else{  //最高额担保合同
					context.put("grt_type", "ZGE");
				}
			}
			connection = this.getConnection(context); 
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
