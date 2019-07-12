package com.yucheng.cmis.biz01line.lmt.op.lmtappfinsubpay;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class AddLmtAppFinSubpayPageOp extends CMISOperation {

	// operation TableModel
	private final String modelId = "LmtAppFinSubpay";
	private final String modelIdList = "LmtSubpayList";

	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			TableModelDAO dao = getTableModelDAO(context);
			String serno = null;
			try {
				serno = (String) context.getDataValue("serno");
				context.setDataValue("serno", serno);
			} catch (Exception e) {
			}
			if (serno != null && !"".equals(serno)) {// 如果 有 传入 serno
				try {
					KeyedCollection kColl = new KeyedCollection(modelId);
					kColl = dao.queryFirst(modelId, null, "where serno ='"
							+ serno + "'", connection);
					kColl.setDataValue("serno", serno);

					SInfoUtils.addUSerName(kColl, new String[] { "input_id",
							"manager_id" });
					SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id",
							"manager_br_id" });

					String[] args = new String[] { "cus_id" };
					String[] modelIds = new String[] { "CusBase" };
					String[] modelForeign = new String[] { "cus_id" };
					String[] fieldName = new String[] { "cus_name" };
					SystemTransUtils.dealName(kColl, args,
							SystemTransUtils.ADD, context, modelIds,
							modelForeign, fieldName);
					this.putDataElement2Context(kColl, context);
					IndexedCollection iColl = new IndexedCollection(modelIdList);
					iColl = dao.queryList(modelIdList, "where serno = '"
							+ serno + "'", connection);
					this.putDataElement2Context(iColl, context);
				} catch (Exception e) {
				}
			} else {
				KeyedCollection kColl = new KeyedCollection(modelId);
				kColl.addDataField("input_id", context
						.getDataValue(PUBConstant.loginuserid));
				kColl.addDataField("input_br_id", context
						.getDataValue(PUBConstant.loginorgid));
				SInfoUtils.addUSerName(kColl, new String[] { "input_id" });
				SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id" });

				String[] args = new String[] { "cus_id" };
				String[] modelIds = new String[] { "CusBase" };
				String[] modelForeign = new String[] { "cus_id" };
				String[] fieldName = new String[] { "cus_name" };
				// 详细信息翻译时调用
				SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD,
						context, modelIds, modelForeign, fieldName);
				String main_br_id = (String) context.getDataValue("organNo");
				serno = CMISSequenceService4JXXD.querySequenceFromSQ("SQ",
						"all", main_br_id, connection, context);
				kColl.addDataField("serno", serno);
				this.putDataElement2Context(kColl, context);
			}
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
