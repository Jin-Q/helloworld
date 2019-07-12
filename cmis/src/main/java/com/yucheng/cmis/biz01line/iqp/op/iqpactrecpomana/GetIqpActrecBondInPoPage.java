package com.yucheng.cmis.biz01line.iqp.op.iqpactrecpomana;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class GetIqpActrecBondInPoPage extends CMISOperation {

	private final String modelId = "IqpActrecbondDetail";

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection conn = null;
		conn = this.getConnection(context);
		String poNo = context.getDataValue("po_no").toString();
		String conditionStr = "where po_no = '" + poNo + "'";
		IndexedCollection iCollIn = null;
		IndexedCollection iCollOut = null;
		TableModelDAO dao = this.getTableModelDAO(context);
		iCollIn = dao.queryList(modelId, conditionStr + " and status ='2'",
				conn);
		iCollOut = dao.queryList(modelId, conditionStr
				+ " and status in('1','3','5')", conn);
		iCollIn.setName("IqpActrecBondInPoList");
		iCollOut.setName("IqpActrecBondOutPoList");
		this.putDataElement2Context(iCollIn, context);
		this.putDataElement2Context(iCollOut, context);
		return "0";
	}

}
