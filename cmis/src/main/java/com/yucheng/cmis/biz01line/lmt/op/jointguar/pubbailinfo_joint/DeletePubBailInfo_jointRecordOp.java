package com.yucheng.cmis.biz01line.lmt.op.jointguar.pubbailinfo_joint;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class DeletePubBailInfo_jointRecordOp extends CMISOperation {

	private final String modelId = "PubBailInfo";
	
	private final String serno_name = "serno";
	private final String bail_acct_no_name = "bail_acct_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno_value = null;
			String bail_acct_no_value = null;

			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
				
			try {
				bail_acct_no_value = (String)context.getDataValue(bail_acct_no_name);
			} catch (Exception e) {}
			if(bail_acct_no_value == null || bail_acct_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+bail_acct_no_name+"] cannot be null!");
				
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
	
			Map<String,String> pkMap = new HashMap<String,String>();
			if(modiflg!=null && !"".equals(modiflg) && "yes".equals(modiflg)){
				KeyedCollection delKColl = new KeyedCollection("delKColl");
				delKColl.addDataField("modify_rel_serno", modify_rel_serno);
				delKColl.addDataField("serno", serno_value);
				delKColl.addDataField("bail_acct_no", bail_acct_no_value);
				try {
					int count = SqlClient.delete("deletePubBailInfoTmpByMSB", delKColl, connection);
					if(count!=1){
						throw new EMPException("Remove Failed! Records :"+count);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}				
				context.addDataField("flag", "modifySuc");
			}else{
				pkMap.put("serno",serno_value);
				pkMap.put("bail_acct_no",bail_acct_no_value);
				int count=dao.deleteByPks(modelId, pkMap, connection);
				if(count!=1){
					throw new EMPException("Remove Failed! Records :"+count);
				}
				context.addDataField("flag", "suc");
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
