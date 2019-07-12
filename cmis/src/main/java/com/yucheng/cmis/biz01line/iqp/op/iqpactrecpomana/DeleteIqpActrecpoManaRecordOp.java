package com.yucheng.cmis.biz01line.iqp.op.iqpactrecpomana;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.component.IqpActrecBondComponent;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpActrecpoManaRecordOp extends CMISOperation {

	private final String modelId = "IqpActrecpoMana";
	private final String modelIdDetail = "IqpActrecbondDetail";
	private final String modelIdBus = "IqpBuscontInfo";
	private final String modelIdExp = "IqpExpInfo";
	private final String po_no_name = "po_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String po_no_value = null;
			try {
				po_no_value = (String) context.getDataValue(po_no_name);
			} catch (Exception e) {
			}
			if (po_no_value == null || po_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk[" + po_no_name
						+ "] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			int count = dao.deleteByPk(modelId, po_no_value, connection);
			if (count != 1) {
				throw new EMPException("Remove Failed! Records :" + count);
			}
			IqpActrecBondComponent comp = new IqpActrecBondComponent();
			comp.deleteByNo("iqp_actrecbond_detail", "  po_no = '"
					+ po_no_value + "'", connection);
			comp.deleteByNo("iqp_buscont_info", "  po_no = '" + po_no_value
					+ "'", connection);
			comp.deleteByNo("iqp_exp_info", "  po_no = '" + po_no_value + "'",
					connection);
			/**add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造  begin**/
			//删除保理池关联的回款保证金明细
			comp.deleteByNo("iqp_bailacc_detail", "  po_no = '" + po_no_value + "'",
					connection);
			/**add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造  end**/
			context.addDataField("flag", "success");
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			context.addDataField("flag", "success");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
