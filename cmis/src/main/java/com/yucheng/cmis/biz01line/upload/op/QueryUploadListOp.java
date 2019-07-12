package com.yucheng.cmis.biz01line.upload.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.util.TableModelUtil;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryUploadListOp extends CMISOperation {
	
	private final String modelId = "PubDocumentInfo";

	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String fileType = context.getDataValue("file_type").toString();//文档业务类型
			String serno = context.getDataValue("serno").toString();//业务流水
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = " where file_type = '"+fileType+"' and serno = '"+serno+"'";
			IndexedCollection iColl = dao.queryList(modelId, null,condition, pageInfo, connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
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
