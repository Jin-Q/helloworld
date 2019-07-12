package com.yucheng.cmis.biz01line.cus.op.cuscommanager;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class CheckComMrgTypOp extends CMISOperation{
	
	private final String modelId = "CusComManager";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String cus_id = null;
		try {
			connection = this.getConnection(context);
			try {
				cus_id = (String)context.getDataValue("cus_id");
			} catch (Exception e) {
				e.printStackTrace();
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, " where cus_id ='"+cus_id+"' and com_mrg_typ ='01'", connection);
			if(iColl.size()>0){
				context.addDataField("flag", PUBConstant.FAIL);
			}else{
				context.addDataField("flag", PUBConstant.SUCCESS);
			}
		} catch (EMPException ee) {
			throw ee;
		}catch (Exception  e) {
			throw new EMPException(e);
		}finally{
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}

