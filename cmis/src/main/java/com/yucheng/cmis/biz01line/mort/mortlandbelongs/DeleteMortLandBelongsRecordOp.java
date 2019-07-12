package com.yucheng.cmis.biz01line.mort.mortlandbelongs;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class DeleteMortLandBelongsRecordOp extends CMISOperation {

	private final String modelId = "MortLandBelongs";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			String land_id = (String)context.getDataValue("land_id");
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("land_id", land_id);
			String sql  = "DELETE FROM MORT_LAND_BELONGS WHERE land_id='"+land_id+"'";
			int count = SqlClient.deleteBySql(sql, connection);
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
