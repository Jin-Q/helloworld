package com.yucheng.cmis.biz01line.lmt.op.lmtappdetails;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class GetCusBelgLineOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtAppDetails";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cus_id = null;
			try {
				cus_id = (String) context.getDataValue("cus_id");
			} catch (Exception e) {}
			if(cus_id == null || "".equals(cus_id))
				throw new EMPJDBCException("The values to ["+modelId+"] cannot be empty!");
			
			CusBaseComponent cusBaseComponent = (CusBaseComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE,context, connection);
			
			CusBase cusBase = cusBaseComponent.getCusBase(cus_id);
			
			context.addDataField("flag",cusBase.getBelgLine());
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
