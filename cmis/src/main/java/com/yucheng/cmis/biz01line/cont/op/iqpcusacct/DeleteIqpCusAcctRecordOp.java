package com.yucheng.cmis.biz01line.cont.op.iqpcusacct;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class DeleteIqpCusAcctRecordOp extends CMISOperation {

	private final String modelId = "IqpCusAcct";
	private final String pkid = "pk_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String pk_id = null;
			try {
				pk_id =(String)context.getDataValue(pkid);
			} catch (Exception e) {}
			if(pk_id == null || pk_id.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_id+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
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
				KeyedCollection delKColl = new KeyedCollection("delKColl");
				delKColl.addDataField("modify_rel_serno", modify_rel_serno);
				delKColl.addDataField("pk_id", pk_id);
				SqlClient.delete("deleteIqpCusAcctTmpByMP", delKColl, connection);
				context.addDataField("flag","success");
				
			}else{
				int count = dao.deleteByPk(modelId, pk_id, connection);
				
				if(count!=1){
					throw new EMPException("Remove Failed! Records :"+count);
				}else{
					context.addDataField("flag","success");
				}
			}
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/		
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
