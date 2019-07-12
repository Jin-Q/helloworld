package com.yucheng.cmis.ops.lmt.wfispnamelist;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class DeleteWfiSpNameListRecordOp extends CMISOperation {

	private final String modelId = "WfiSpNameList";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String pk_id =null;
			String cus_id = null;
			pk_id = (String)context.getDataValue("pk_id");
			cus_id = (String)context.getDataValue("cus_id");
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("pk_id", pk_id);
			pkMap.put("cus_id", cus_id);
			String sql  = "DELETE FROM wfi_sp_name_list WHERE pk_id='"+pk_id+"'";
			int count = SqlClient.deleteBySql(sql, connection);
			/*			=dao.deleteByPks(modelId, pkMap, connection);*/
			if(count!=1){
				context.addDataField("flag","failed");
				context.addDataField("msg","删除名单失败!！");
				return "0";
			}
			context.addDataField("flag","success");
			context.addDataField("msg","删除成功!！");
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
