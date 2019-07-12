package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class GetHLByCurrOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String currType = "";
			if(context.containsKey("currType")){
				currType = (String)context.getDataValue("currType");
			}
			if(currType == ""){
				context.addDataField("flag", "failed");
				context.addDataField("msg", "请选择申请币种！");
				context.addDataField("sld", "");
			}else {
				/** 通过币种获取汇率表CRD_HLWH中币种汇率 */
				IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
					.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
				KeyedCollection ic = cmisComponent.getHLByCurrType(currType);
				context.addDataField("flag", ic.getDataValue("flag"));
				context.addDataField("msg", ic.getDataValue("msg"));
				context.addDataField("sld", ic.getDataValue("sld"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			this.releaseConnection(context, connection);
		}
		return "0";
	}

}
