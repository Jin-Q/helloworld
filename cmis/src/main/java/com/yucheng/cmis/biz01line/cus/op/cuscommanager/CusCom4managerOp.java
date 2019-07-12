package com.yucheng.cmis.biz01line.cus.op.cuscommanager;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class CusCom4managerOp extends CMISOperation {
	
	private final String modelId = "CusCom";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			CusCom cusCom=new CusCom();
			String cus_id_rel="";
			
			CusComComponent cusComComponent = (CusComComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(
					PUBConstant.CUSCOM,context,connection);
			ComponentHelper componetHelper = new ComponentHelper();
			
			KeyedCollection kColl=new KeyedCollection(modelId);
			cus_id_rel = context.getDataValue("cus_id_rel").toString();
			cusCom = cusComComponent.getCusCom(cus_id_rel);
			kColl=componetHelper.domain2kcol(cusCom, modelId);
			
			IndexedCollection iColl=new IndexedCollection("cusList"); 
			iColl.addDataElement(kColl);
			
			this.putDataElement2Context(iColl, context);
			
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
