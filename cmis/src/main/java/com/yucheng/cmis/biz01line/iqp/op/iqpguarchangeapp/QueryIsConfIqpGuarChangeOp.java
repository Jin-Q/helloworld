package com.yucheng.cmis.biz01line.iqp.op.iqpguarchangeapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryIsConfIqpGuarChangeOp extends CMISOperation {
	private static final String MODEL = "IqpGuarChangeApp";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
	
		try {
			connection = this.getConnection(context);
			String cont_no = "";
			if(context.containsKey("cont_no")){
				cont_no = (String)context.getDataValue("cont_no");
			}
			String condition = " where cont_no = '"+cont_no+"' and approve_status not in ('997')";  
			/** 通过合同编号查询信用证修改是否有在途记录 */
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(MODEL, condition, connection); 
			if(iColl.size() > 0){
				context.addDataField("flag", "failed");
			}else {
				context.addDataField("flag", "success");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
