package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtappbizareasupmk;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteLmtAppBizAreaSupmkRecordOp extends CMISOperation {

	private final String modelId = "LmtAppBizAreaSupmk";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String supmk_serno_value = null;
			String tempStr = "";
			try {
				supmk_serno_value = (String) context.getDataValue("schemecodeArr");
			} catch (Exception e) {}
				
			tempStr = supmk_serno_value.substring(0,supmk_serno_value.length() - 1);
			TableModelDAO dao = this.getTableModelDAO(context);
			for(int i=tempStr.indexOf(","); i != -1; i=tempStr.indexOf(",") ){
				supmk_serno_value = tempStr.substring(0, i);
				tempStr = tempStr.substring(i+1, tempStr.length());
				dao.deleteByPk(modelId, supmk_serno_value, connection);
			}
			if(!"".equals(tempStr)){
				dao.deleteByPk(modelId, tempStr, connection);
			}
			context.addDataField("flag", "suc");
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
