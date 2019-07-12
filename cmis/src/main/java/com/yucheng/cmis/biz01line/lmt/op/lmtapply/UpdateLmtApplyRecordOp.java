package com.yucheng.cmis.biz01line.lmt.op.lmtapply;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class UpdateLmtApplyRecordOp extends CMISOperation {

	private final String modelId = "LmtApply";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection) context.getDataElement(modelId);
			} catch (Exception e) {
			}
			if (kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update[" + modelId
						+ "] cannot be empty!");

			TableModelDAO dao = this.getTableModelDAO(context);
			int count = dao.update(kColl, connection);
			if (count != 1) {
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			if (kColl.getDataValue("grp_serno") != null
					&& !"".equals(kColl.getDataValue("grp_serno"))) {
				LmtPubComponent lmtComponent = (LmtPubComponent) CMISComponentFactory
						.getComponentFactoryInstance().getComponentInstance(
								"LmtPubComponent", context, connection);
				lmtComponent.updateLmtGrpAppAmt(kColl.getDataValue("grp_serno")
						.toString()); // 根据流水号更新集团授信申请基表数据
			}

			context.addDataField("message", "Y");
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
