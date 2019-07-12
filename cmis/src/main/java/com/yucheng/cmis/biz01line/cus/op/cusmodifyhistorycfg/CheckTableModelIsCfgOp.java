package com.yucheng.cmis.biz01line.cus.op.cusmodifyhistorycfg;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckTableModelIsCfgOp extends CMISOperation {
	
	private final String modelId = "CusModifyHistoryCfg";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String model_id = "";
			String infoFlag = "";
			try {
				model_id = (String)context.getDataValue("model_id");
			} catch (Exception e) {}
			if(model_id == null || "".equals(model_id)){
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "字段model_id是空值!");
				throw new EMPJDBCException("字段model_id是空值!");
			}
			//查询该表模型是否进行过配置
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = " where model_id='"+model_id+"'";
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			if(iColl.size()>0){
				infoFlag = "exist";
			}else{
				infoFlag = "notExist";
			}
			
			context.addDataField("flag", infoFlag);
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
