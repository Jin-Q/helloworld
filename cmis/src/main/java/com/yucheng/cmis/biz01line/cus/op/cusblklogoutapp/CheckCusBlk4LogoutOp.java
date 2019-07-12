package com.yucheng.cmis.biz01line.cus.op.cusblklogoutapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class CheckCusBlk4LogoutOp extends CMISOperation {
	private final String modelId = "CusBlkLogoutapp";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection =this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			
			String cus_id =(String)context.getDataValue("cus_id");
				
			IndexedCollection iColl = dao.queryList(modelId, "where cus_id='"+cus_id+"' and approve_status not in ('997','998')", connection);
	 			
			if(iColl.size()>0){
				context.addDataField("flag", PUBConstant.SUCCESS); 
			}else{
				context.addDataField("flag", PUBConstant.FAIL);
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