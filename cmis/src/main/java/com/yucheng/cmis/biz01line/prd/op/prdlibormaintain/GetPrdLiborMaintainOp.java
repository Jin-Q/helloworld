package com.yucheng.cmis.biz01line.prd.op.prdlibormaintain;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.prd.msi.PrdServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class GetPrdLiborMaintainOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String currType = "";//币种
			String irType = "";//利率种类
			if(context.containsKey("curr")){
				currType = (String)context.getDataValue("curr");
				irType = (String)context.getDataValue("irType");
			}
			if(currType == ""){
				context.addDataField("flag", "failed");
				context.addDataField("msg", "请选择申请币种！");
				context.addDataField("rateValue", 0);
			}else {
//				CrdLiborComponent liborComponent = (CrdLiborComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(SyscfgConstant.CRDLIBORCOMPONENT, context, connection);
//				KeyedCollection ic = liborComponent.getLiborRate(currType,irType);
				
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				PrdServiceInterface service = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
				KeyedCollection ic = service.getLiborRate(currType, irType, context, connection);
				
				context.addDataField("flag", ic.getDataValue("flag"));
				context.addDataField("msg", ic.getDataValue("msg"));
				context.addDataField("rateValue", ic.getDataValue("rateValue"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			this.releaseConnection(context, connection);
		}
		return "0";
	}

}
