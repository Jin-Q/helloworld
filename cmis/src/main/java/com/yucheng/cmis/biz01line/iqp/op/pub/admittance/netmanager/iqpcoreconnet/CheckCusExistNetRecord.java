package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpcoreconnet;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckCusExistNetRecord extends CMISOperation {
	private final String modelId = "IqpCoreConNet";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection =this.getConnection(context);
			String cus_id =(String)context.getDataValue("cus_id");
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, "where cus_id='"+cus_id+"'", connection);
			if(iColl.size()>0)
			{
				context.addDataField("flag", "success");
			}else
			{
				context.addDataField("flag", "fail");
			}
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
