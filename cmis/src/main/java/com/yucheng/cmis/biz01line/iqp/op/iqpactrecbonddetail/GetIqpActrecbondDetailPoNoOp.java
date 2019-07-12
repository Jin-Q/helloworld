package com.yucheng.cmis.biz01line.iqp.op.iqpactrecbonddetail;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class GetIqpActrecbondDetailPoNoOp extends CMISOperation {

	private final String modelId = "IqpActrecbondDetail";

	@Override
	public String doExecute(Context context) throws EMPException {
		if (!context.containsKey("po_no"))
			return "0";
		String po_no = context.getDataValue("po_no").toString();
		String invc_no = context.getDataValue("invc_no").toString();
		if (invc_no != null && !invc_no.equals("")) {
			Connection connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("po_no", po_no);
			pkMap.put("invc_no", invc_no);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);

			this.putDataElement2Context(kColl, context);
		}
		return "0";
	}

}
