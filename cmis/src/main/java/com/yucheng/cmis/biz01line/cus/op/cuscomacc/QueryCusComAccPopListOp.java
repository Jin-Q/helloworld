package com.yucheng.cmis.biz01line.cus.op.cuscomacc;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryCusComAccPopListOp extends CMISOperation {
	
	private String modelId = "CusComAcc";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String cusId = null;
			TableModelDAO dao = this.getTableModelDAO(context);
			try {
				cusId = (String) context.getDataValue("cusId");
			} catch (Exception e) {
			}
			if (cusId == null || cusId.equals(""))
				throw new EMPJDBCException("客户码不可以为空！");

			

			String condition = " where cus_id='" + cusId + "'";
			IndexedCollection iColl = dao.queryList(modelId, condition,connection);
			iColl.setName(iColl.getName() + "List");
			this.putDataElement2Context(iColl, context);
			//SInfoUtils.addSOrgName(iColl, new String[]{"acc_open_org","acc_org"});

		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}

