package com.yucheng.cmis.biz01line.cus.op.cuscommanager;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.cus.cusindiv.component.CusIndivComponent;
import com.yucheng.cmis.biz01line.cus.cusindiv.domain.CusIndiv;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class CusIndiv4managerOp extends CMISOperation {
	
	private final String modelId = "CusIndiv";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			CusIndiv cusIndiv=new CusIndiv();
			String cus_id_rel="";
			
			CusIndivComponent cusIndivComponent = (CusIndivComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(
					PUBConstant.CUSINDIV,context,connection);
			ComponentHelper componetHelper = new ComponentHelper();
			
			KeyedCollection kColl=new KeyedCollection(modelId);
			cus_id_rel = context.getDataValue("cus_id_rel").toString();
			cusIndiv = cusIndivComponent.getCusIndiv(cus_id_rel);
			kColl=componetHelper.domain2kcol(cusIndiv, modelId);
			
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
