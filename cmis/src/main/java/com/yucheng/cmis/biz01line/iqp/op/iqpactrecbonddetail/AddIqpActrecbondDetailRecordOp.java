package com.yucheng.cmis.biz01line.iqp.op.iqpactrecbonddetail;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.component.IqpActrecBondComponent;
import com.yucheng.cmis.operation.CMISOperation;

public class AddIqpActrecbondDetailRecordOp extends CMISOperation {

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
			String poNo = kColl.getDataValue("po_no").toString();
			String cont_no = kColl.getDataValue("cont_no").toString();
			String invcNo = kColl.getDataValue("invc_no").toString();
			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("cont_no", cont_no);
			pkMap.put("invc_no", invcNo);
			KeyedCollection kCollQuery = dao.queryDetail(modelId, pkMap,
					connection);
			if (kCollQuery == null || kCollQuery.getDataValue("invc_no") == null) {
				kColl.setDataValue("status", "1");
				dao.insert(kColl, connection);
				context.addDataField("flag", "success");
			} else {
				/*kCollQuery.setDataValue("buy_cus_name", kColl.getDataValue(
						"buy_cus_name").toString());
				kCollQuery.setDataValue("sel_cus_name", kColl.getDataValue(
						"sel_cus_name").toString());
				kCollQuery.setDataValue("bond_mode", kColl.getDataValue(
						"bond_mode").toString());
				kCollQuery.setDataValue("invc_amt", kColl.getDataValue(
						"invc_amt").toString());
				kCollQuery.setDataValue("cont_no", kColl
						.getDataValue("cont_no").toString());
				kCollQuery.setDataValue("bond_amt", kColl.getDataValue(
						"bond_amt").toString());
				kCollQuery.setDataValue("invc_date", kColl.getDataValue(
						"invc_date").toString());
				kCollQuery.setDataValue("bond_pay_date", kColl.getDataValue(
						"bond_pay_date").toString());
				dao.update(kCollQuery, connection);*/
				context.addDataField("flag", "exist");
			}

			KeyedCollection kCollMana = null;
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
