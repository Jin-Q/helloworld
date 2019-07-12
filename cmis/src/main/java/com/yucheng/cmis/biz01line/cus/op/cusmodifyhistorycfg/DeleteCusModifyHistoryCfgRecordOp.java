package com.yucheng.cmis.biz01line.cus.op.cusmodifyhistorycfg;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.cuscom.component.ModifyHistoryComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeleteCusModifyHistoryCfgRecordOp extends CMISOperation {

	private final String modelId = "CusModifyHistoryCfg";
	
	private final String model_id_name = "model_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String model_id_value = null;
			try {
				model_id_value = (String)context.getDataValue(model_id_name);
			} catch (Exception e) {}
			if(model_id_value == null || model_id_value.length() == 0){
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "The value of pk["+model_id_name+"] cannot be null!");
				throw new EMPJDBCException("The value of pk["+model_id_name+"] cannot be null!");
			}
				
			//删除配置表中信息
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, model_id_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			//删除字段字典配置表中信息
			ModifyHistoryComponent mhComponent = (ModifyHistoryComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance("ModifyHistory", context,connection);
			mhComponent.deleteModifyCfgByModelId(model_id_value);
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
