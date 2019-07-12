package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpcoreconnet;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteIqpCoreConNetRecordOp extends CMISOperation {

	private final String modelId = "IqpCoreConNet";
	

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, serno_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			int res = cmisComponent.removeIqpAppMemByApp(serno_value, connection);		
			
			context.addDataField("flag", PUBConstant.SUCCESS);
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
