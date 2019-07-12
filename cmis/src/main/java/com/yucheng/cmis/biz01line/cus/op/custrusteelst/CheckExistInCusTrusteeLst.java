package com.yucheng.cmis.biz01line.cus.op.custrusteelst;

import java.sql.Connection;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusTrusteeLstComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.PUBConstant;

public class CheckExistInCusTrusteeLst extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		// TODO Auto-generated method stub
		Connection connection=this.getConnection(context);
		try{
		String serno=(String) context.getDataValue("serno");
		CusTrusteeLstComponent chac = 
			(CusTrusteeLstComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(PUBConstant.CUSTRUSTEELST, context, connection);
		List<CMISDomain> list = chac.findCusTrusteeLstListBySerno(serno);
		
		if(list.size()>0){
			context.addDataField("flag", "true");
		}else{
			context.addDataField("flag", "false");
		}
	}catch (EMPException ee) {
		throw ee;
	} catch(Exception e){
		throw new EMPException(e);
	} finally {
		if (connection != null)
			this.releaseConnection(context, connection);
	}
	return "0";
		
	}

}
