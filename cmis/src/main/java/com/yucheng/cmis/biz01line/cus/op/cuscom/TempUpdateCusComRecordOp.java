package com.yucheng.cmis.biz01line.cus.op.cuscom;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

/**
 * 
 * 更新记录：2010-01-11 将暂存时保存修改历史的方法去掉
 *
 */
public class TempUpdateCusComRecordOp extends CMISOperation {

	private final String modelId = "CusCom";
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
				throw new EMPJDBCException("The values to update[" + modelId + "] cannot be empty!");
			if (kCollBase == null || kCollBase.size() == 0)
				throw new EMPJDBCException("The values to update[" + modelIdBase + "] cannot be empty!");
			
			String cus_id = (String) kCollBase.getDataValue("cus_id");
			kColl.addDataField("cus_id", cus_id);
			kCollBase.addDataField("last_update_date", context.getDataValue("OPENDAY"));
			//将修改日期和修改人赋值给KCOLL
//			kColl.setDataValue("last_upd_id", context.getDataValue("currentUserId"));
//			kColl.setDataValue("last_upd_date", context.getDataValue("OPENDAY"));
//			String cus_type = (String) kColl.getDataValue("cus_type");

//			if ("211".equals(cus_type) || "212".equals(cus_type) || "260".equals(cus_type) || "280".equals(cus_type) || "290".equals(cus_type)) {
//				flagInfo = "com";
//			} else {
//				flagInfo = "ass";
//			}

			TableModelDAO dao = this.getTableModelDAO(context);
			dao = this.getTableModelDAO(context);
			int count = dao.update(kColl, connection);
			if (count != 1) {
				throw new EMPException("更新企业客户信息失败！");
			}
			count = dao.update(kCollBase, connection);
			if (count != 1) {
				throw new EMPException("更新企业客户信息失败！");
			}
			
			flag = "success";
			context.addDataField("flag", flag);

			context.clear();
			KeyedCollection kCollNew = dao.queryDetail(modelId,cus_id, connection);
			
			SInfoUtils.addSOrgName(kCollNew, new String[] { "main_br_id","input_br_id" });
			SInfoUtils.addUSerName(kCollNew, new String[] { "cust_mgr","input_id" });
			
			this.putDataElement2Context(kCollNew, context);

		} catch (EMPException ee) {
			ee.printStackTrace();
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
