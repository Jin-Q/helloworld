package com.yucheng.cmis.biz01line.cus.op.cusclirel;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class GetCusCliRelByCusIdOp extends CMISOperation {

	private final String modelId = "CusCliRel";
	
	@Override
	public String doExecute(Context context) throws EMPException {
		/** 通过客户ID获得客户所属业务线 */
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String cusId = "";
			cusId = (String)context.getDataValue("cusId");
			String condition = " where cus_id = '"+cusId+"'";
			TableModelDAO dao = this.getTableModelDAO(context);
			
			IndexedCollection detIColl = dao.queryList(modelId, condition, connection);
			if(detIColl.size()==0){
				context.addDataField("flag", "failed");
				context.addDataField("msg", "未关联客户信息,请录入！");
				return null;
			}
			context.addDataField("flag", "success");
			context.addDataField("msg", "");
			
		} catch (Exception e) {
			context.addDataField("flag", "failed");
			context.addDataField("msg", "查询客户关联信息错误，错误原因："+e.getMessage());			
			try {
				connection.rollback();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
 }


				

		
	
	