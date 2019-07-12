package com.yucheng.cmis.biz01line.cus.op.cusindiv;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class TempUpdateCusIndivRecordOp extends CMISOperation {
	private final String modelId = "CusIndiv";
	private final String modelIdBase = "CusBase";

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = "";
		try {
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			KeyedCollection kCollBase = null;
			try {
				kColl = (KeyedCollection) context.getDataElement(modelId);
				kCollBase = (KeyedCollection) context.getDataElement(modelIdBase);
			} catch (Exception e) {
			}
			if (kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update[" + modelId
						+ "] cannot be empty!");

			// kColl.setDataValue("cus_status", "00");
//			kColl.setDataValue("last_upd_id", context.getDataValue("currentUserId"));
//			kColl.setDataValue("last_upd_date", context.getDataValue("OPENDAY"));

			TableModelDAO dao = this.getTableModelDAO(context);

			String cusId = (String) kCollBase.getDataValue("cus_id");
			kColl.addDataField("cus_id", cusId);
			kCollBase.addDataField("last_update_date", context.getDataValue("OPENDAY"));
//			KeyedCollection oldKColl = dao.queryAllDetail(modelId, cusId,connection);

//			ModifyHistoryComponent historyComponent = (ModifyHistoryComponent) CMISComponentFactory
//					.getComponentFactoryInstance().getComponentInstance(
//							PUBConstant.MODIFY_HISTORY_COMPONENT,
//							context, connection);

//			HttpServletRequest request = (HttpServletRequest) context.getDataValue(EMPConstance.SERVLET_REQUEST);
//			String userIP = request.getRemoteAddr();
//			historyComponent.recordHistoryModify((KeyedCollection) oldKColl.clone(), (KeyedCollection) kColl.clone(), modelId, userIP);

			int count = dao.update(kColl, connection);
			if (count != 1) {
				throw new EMPException("更新对私客户信息失败！");
			}
			count = dao.update(kCollBase, connection);
			if (count != 1) {
				throw new EMPException("更新对私客户信息失败！");
			}
			flag = "success";
			context.addDataField("flag", flag);

			context.clear();
			String cus_id_value = String.valueOf(kColl.getDataValue("cus_id"));
			KeyedCollection kCollNew = dao.queryDetail(modelIdBase,cus_id_value, connection);
			SInfoUtils.addSOrgName(kCollNew, new String[] { "main_br_id","input_br_id" });
			SInfoUtils.addUSerName(kCollNew, new String[] { "cust_mgr","input_id", "last_upd_id" });
			this.putDataElement2Context(kCollNew, context);

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
