package com.yucheng.cmis.platform.organization.sorg.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.initializer.OrganizationInitializer;

public class DeleteSOrgRecordOp extends CMISOperation {

	private final String modelId = "SOrg";
	

	private final String organno_name = "organno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String organno_value = null;
			try {
				organno_value = (String)context.getDataValue(organno_name);
			} catch (Exception e) {}
			if(organno_value == null || organno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+organno_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, organno_value, connection);
			if(count!=1){
				context.addDataField("flag", "failure");
				context.addDataField("msg", "Remove Failed! Records :"+count);
				return null;
			}
			OrganizationInitializer.removeOrgMapInfo(organno_value);
			//SInfoUtils.findAllOrgInfo();
			context.addDataField("flag", "success");
			context.addDataField("msg", "");
		}catch (EMPException ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "删除失败！失败原因："+ee.getMessage());
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
