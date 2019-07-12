package com.yucheng.cmis.biz01line.grt.op.grtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.grt.component.GrtGuarContComponet;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeleteGrtGuarantyReOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);


			String guaranty_no = (String)context.getDataValue("guaranty_no");

			GrtGuarContComponet ggc = (GrtGuarContComponet) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("GrtGuarCont", context, connection);
			int con = ggc.deleteGrtGuarantyReGuar(guaranty_no);
			if(con!=1){
				throw new EMPException("Remove Failed! Records :"+con);
			}
			context.addDataField("flag", "success");
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
