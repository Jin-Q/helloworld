package com.yucheng.cmis.biz01line.cus.op.cusbase;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class getCusBaseByCusId extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
	Connection connection = null;
	try{	
		connection =this.getConnection(context);
		String cusId = (String)context.getDataValue("cus_id");
		CusBaseComponent cbc = (CusBaseComponent)CMISComponentFactory.getComponentFactoryInstance()
		    .getComponentInstance(PUBConstant.CUSBASE, context,connection);
	    CusBase cb = cbc.getCusBase(cusId);
			if(cb!=null){
				context.addDataField("cusName", cb.getCusName());
			}else{
				throw new EMPException("根据客户编号【"+cusId+"】无法找到客户基本信息");
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
