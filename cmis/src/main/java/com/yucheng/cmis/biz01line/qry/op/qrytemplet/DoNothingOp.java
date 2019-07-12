package com.yucheng.cmis.biz01line.qry.op.qrytemplet;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.QryPubConstant;
import com.yucheng.cmis.biz01line.qry.component.QryComponent;
import com.yucheng.cmis.biz01line.qry.component.QryGenPageComponent;

public class DoNothingOp extends CMISOperation {
	public String doExecute(Context context) throws EMPException {
		return "0";
	}
}
