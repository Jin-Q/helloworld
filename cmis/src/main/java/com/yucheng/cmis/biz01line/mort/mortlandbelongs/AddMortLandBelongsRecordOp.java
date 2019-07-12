package com.yucheng.cmis.biz01line.mort.mortlandbelongs;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

/**
 * @Description:XD150714054 土地承包经营权共有人情况
 * @author FChengLiang
 * @time:2015-8-19 上午08:35:09
 */
public class AddMortLandBelongsRecordOp extends CMISOperation {

	private final String modelId1 = "MortLandMgrRightDetail";
	private final String modelId = "MortLandBelongs";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String guaranty_no = "";
		try {
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection) context.getDataElement(modelId);
				guaranty_no = (String)kColl.getDataValue("guaranty_no");
				kColl.put("land_id", "" + System.currentTimeMillis());
			} catch (Exception e) {
			}
			if (kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert[" + modelId
						+ "] cannot be empty!");

			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);

			String conditionStr = "where guaranty_no ='" + guaranty_no + "'";
			IndexedCollection iColl = dao.queryList(modelId1, conditionStr,
					connection);
			KeyedCollection kColl1 = new KeyedCollection(modelId);
			if (iColl.size() != 0) {
				kColl1 = (KeyedCollection) iColl.get(0);
				IndexedCollection iColl1 = dao.queryList(modelId, null,
						conditionStr, connection);
				iColl1.setName(iColl1.getName() + "List");
				this.putDataElement2Context(iColl1, context);
			}
			this.putDataElement2Context(kColl1, context);
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
