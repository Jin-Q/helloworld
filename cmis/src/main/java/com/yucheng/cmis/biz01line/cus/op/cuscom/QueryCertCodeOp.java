package com.yucheng.cmis.biz01line.cus.op.cuscom;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

/**
 * 
 * @author zhoujf
 * @since 2009-08-08
 * @see 根据证件类型和证件号码检查其在系统中是否已经存在
 */
public class QueryCertCodeOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String certType = "";
		String certCode = "";
		String cusType = "";
		String cusNo = "";
		String flag = "n";
		try {
			connection = this.getConnection(context);
			certType = (String) context.getDataValue("certType");
			certCode = (String) context.getDataValue("certCode");
			cusType = (String) context.getDataValue("cusType");
			cusNo = (String) context.getDataValue("cusNo");
			CusComComponent cusComComponent = (CusComComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(
							PUBConstant.CUSCOM, context, connection);
			flag = cusComComponent.checkCertCodeExist(certCode, certType, cusType,cusNo);
			context.addDataField("certCodeFlag", flag);
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
