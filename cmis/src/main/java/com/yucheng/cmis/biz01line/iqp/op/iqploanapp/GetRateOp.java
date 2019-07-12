package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class GetRateOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		/** 通过业务品种、币种、期限类型、期限获取利率信息 */
		Connection connection = null;
		try {
			connection = this.getConnection(context);
		
			String prdId = "";
			String currType = "";
			String termType = "";
			String term = "";
			if(context.containsKey("prdId")){
				prdId = (String)context.getDataValue("prdId");
			}
			if(context.containsKey("currType")){
				currType = (String)context.getDataValue("currType");
			}
			if(context.containsKey("termType")){
				termType = (String)context.getDataValue("termType");
			}
			if(context.containsKey("term")){
				term = (String)context.getDataValue("term");
			}
			
			IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			KeyedCollection ic = cmisComponent.getRate(prdId,currType,termType,term,context,connection);
			context.addDataField("flag", ic.getDataValue("flag"));
			context.addDataField("msg", ic.getDataValue("msg"));
			context.addDataField("rate", ic.getDataValue("rate"));
			context.addDataField("code", ic.getDataValue("code"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
