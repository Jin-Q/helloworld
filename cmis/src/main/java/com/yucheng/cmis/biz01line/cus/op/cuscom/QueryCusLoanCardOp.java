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
 * @author zhoujf 检查贷款卡号是否在系统中存在
 * @since 2009.08.07
 */
public class QueryCusLoanCardOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String loanCardId = "";
		String cusNo = "";
		String flag = "n";
		try {
			connection = this.getConnection(context);
			loanCardId = (String) context.getDataValue("loanCardNo");
			cusNo = (String) context.getDataValue("cusNo");
			CusComComponent cusComComponent = (CusComComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(
							PUBConstant.CUSCOM, context, connection);
			flag = cusComComponent.checkLoanCardIdExist(loanCardId,cusNo);
			context.addDataField("loanCardIdFlag", flag);
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
