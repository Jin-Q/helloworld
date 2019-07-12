package com.yucheng.cmis.biz01line.iqp.op.iqpactrecbonddetail;

import java.net.URLDecoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.component.IqpActrecBondComponent;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpActrecbondDetailRecordOp extends CMISOperation {

	private final String modelId = "IqpActrecbondDetail";
	private final String modelIdMana = "IqpActrecpoMana";
	private final String cont_no_name = "cont_no";
	private final String invc_no_name = "invc_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);

			String cont_no_value = null;
			try {
				cont_no_value = (String) context.getDataValue(cont_no_name);
			} catch (Exception e) {
			}
			if (cont_no_value == null || cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk[" + cont_no_name
						+ "] cannot be null!");

			String invc_no_value = null;
			try {
				invc_no_value = (String) context.getDataValue(invc_no_name);
			} catch (Exception e) {
			}
			if (invc_no_value == null || invc_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk[" + invc_no_name
						+ "] cannot be null!");

			//中文转码
			cont_no_value = URLDecoder.decode(cont_no_value,"UTF-8");
			invc_no_value = URLDecoder.decode(invc_no_value,"UTF-8");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("cont_no", cont_no_value);
			pkMap.put("invc_no", invc_no_value);
			int count = dao.deleteByPks(modelId, pkMap, connection);
			if (count != 1) {
				throw new EMPException("Remove Failed! Records :" + count);
			}
			
			String po_no_value = context.getDataValue("po_no").toString();
			KeyedCollection kCollMana = null;
			kCollMana = dao.queryFirst(modelIdMana, null, " where po_no = '"
					+ po_no_value + "'", connection);
			IqpActrecBondComponent component = new IqpActrecBondComponent();
			String sAmt = component.getAllInvcAndBondAmt(po_no_value, connection);
			kCollMana.setDataValue("invc_quant", Integer.parseInt(sAmt
					.split("@")[0]));
			kCollMana.setDataValue("invc_amt", Double.parseDouble(sAmt
					.split("@")[1]));
			kCollMana.setDataValue("crd_rgtchg_amt", Double.parseDouble(sAmt
					.split("@")[2]));
			dao.update(kCollMana, connection);
			context.setDataValue("po_no", po_no_value);
			context.addDataField("flag", "success");
		} catch (EMPException ee) {
			context.addDataField("flag", "fail");
			throw ee;
		} catch (Exception e) {
			context.addDataField("flag", "fail");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
