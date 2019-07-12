package com.yucheng.cmis.biz01line.grt.op.grtguarantee;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.grt.component.GrtGuarContComponet;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeleteGrtGuaranteeRecordOp extends CMISOperation {

	private final String modelId = "GrtGuarantee";
	

	private final String guar_id_name = "guar_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String guar_id_value = null;
			try {
				guar_id_value = (String)context.getDataValue(guar_id_name);
			} catch (Exception e) {}
			if(guar_id_value == null || guar_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+guar_id_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			//删除保证人信息
			int count=dao.deleteByPk(modelId, guar_id_value, connection);
			GrtGuarContComponet ggc = (GrtGuarContComponet) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("GrtGuarCont", context, connection);
			//级联删除关系表信息
			int con = ggc.deleteGrtGuarantyReGuar(guar_id_value);
			if(con!=1&&count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.addDataField("flag","success");
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
