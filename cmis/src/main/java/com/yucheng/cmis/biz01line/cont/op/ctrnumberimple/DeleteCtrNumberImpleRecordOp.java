package com.yucheng.cmis.biz01line.cont.op.ctrnumberimple;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * @author lisj
 * @description 零售合同评分配置
 * @time 2014年11月19日
 * @verion v1.0
 */
public class DeleteCtrNumberImpleRecordOp extends CMISOperation {

	private final String modelId = "CtrNumberImple";
	private final String score_id_name = "score_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String score_id_value = null;
			try {
				score_id_value = (String)context.getDataValue(score_id_name);
			} catch (Exception e) {}
			if(score_id_value == null || score_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+score_id_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, score_id_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
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
