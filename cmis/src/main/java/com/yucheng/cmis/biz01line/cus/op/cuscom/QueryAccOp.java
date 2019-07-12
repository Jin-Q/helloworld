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
 * 
 *根据帐号类型和帐号检查该帐号是否在系统
 */
public class QueryAccOp  extends CMISOperation{

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String accType = "";//帐号类型
		String accNo = "";//帐号
		String cusNo = "";//客户码
		String flag = "n";
		try {
			connection = this.getConnection(context);
			accType = (String) context.getDataValue("accType");
			accNo = (String) context.getDataValue("accNo");
			cusNo = (String) context.getDataValue("cusNo");
			CusComComponent cusComComponent = (CusComComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(
							PUBConstant.CUSCOM, context, connection);
			flag = cusComComponent.AccExist(accNo, accType,cusNo);
			context.addDataField("accNoFlag", flag);
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
