package com.yucheng.cmis.biz01line.prd.op.prdcatalog;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.prd.prdtools.PRDConstant;
import com.yucheng.cmis.biz01line.prd.prdtools.tree.CMISProductTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;

public class DeletePrdCatalogRecordOp extends CMISOperation {

	private final String modelId = "PrdCatalog";
	

	private final String catalogid_name = "catalogid";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);


			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String catalogid_value = null;
			try {
				catalogid_value = (String)context.getDataValue(catalogid_name);
			} catch (Exception e) {}
			if(catalogid_value == null || catalogid_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+catalogid_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, catalogid_value, connection);
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
