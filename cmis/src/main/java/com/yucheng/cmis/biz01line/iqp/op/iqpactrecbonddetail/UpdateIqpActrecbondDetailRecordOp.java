package com.yucheng.cmis.biz01line.iqp.op.iqpactrecbonddetail;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.component.IqpActrecBondComponent;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateIqpActrecbondDetailRecordOp extends CMISOperation {

	private final String modelId = "IqpActrecbondDetail";
	private final String modelIdMana = "IqpActrecpoMana";

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
				context.addDataField("flag", "fail");
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			context.addDataField("flag", "success");

			KeyedCollection kCollMana = null;
			String poNo = kColl.getDataValue("po_no").toString();
			kCollMana = dao.queryFirst(modelIdMana, null, " where po_no = '"
					+ poNo + "'", connection);
			IqpActrecBondComponent component = new IqpActrecBondComponent();
			String sAmt = component.getAllInvcAndBondAmt(poNo, connection);
			kCollMana.setDataValue("invc_quant", Integer.parseInt(sAmt
					.split("@")[0]));
			kCollMana.setDataValue("invc_amt", Double.parseDouble(sAmt
					.split("@")[1]));
			kCollMana.setDataValue("crd_rgtchg_amt", Double.parseDouble(sAmt
					.split("@")[2]));
			dao.update(kCollMana, connection);

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
