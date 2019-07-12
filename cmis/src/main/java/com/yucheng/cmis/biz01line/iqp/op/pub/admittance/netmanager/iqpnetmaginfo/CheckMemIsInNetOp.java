package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpnetmaginfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckMemIsInNetOp extends CMISOperation {
	private final String modelId = "IqpMemMana";

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection=null;
		try{
			connection=this.getConnection(context);
			String net_agr_no=(String)context.getDataValue("net_agr_no");
			TableModelDAO dao=this.getTableModelDAO(context);
			//查询是否有待入网或带退网的成员
			String condition = "where net_agr_no='"+net_agr_no+"' and status in ('1','3')";
			IndexedCollection iColl=dao.queryList(modelId, condition, connection);
			if(iColl.isEmpty()){
				context.addDataField("flag", "success");
			}else{
				context.addDataField("flag", "exist");
			}
		}catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
