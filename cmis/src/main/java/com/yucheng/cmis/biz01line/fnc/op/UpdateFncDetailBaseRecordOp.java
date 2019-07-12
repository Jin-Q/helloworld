package com.yucheng.cmis.biz01line.fnc.op;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateFncDetailBaseRecordOp extends CMISOperation {
	 
	private final String modelId = "FncDetailBase";

 
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String cus_id = "";
		String year = "";
		String pk = "";
		String flg = "";
		
		try{
			connection = this.getConnection(context);
			
			String condition="";
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				
				cus_id = (String) kColl.getDataValue("cus_id");
				year = (String) kColl.getDataValue("fnc_ym");
				
				String sql = "Select pk From fnc_detail_base a Where trim(a.cus_id)='"+cus_id+"' And trim(fnc_ym)='"+year+"'";
			//	System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+sql);
				PreparedStatement s = connection.prepareStatement(sql);
				ResultSet rs = s.executeQuery();
				if(rs.next()){
					flg = "update";//如果存在该项目编号，则要警告客户已经存在
					context.addDataField("flag", flg);
				}else{//否则的话,则让他修改
					if(kColl == null || kColl.size() == 0)
						throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
					 
					TableModelDAO dao = this.getTableModelDAO(context);
					int count=dao.update(kColl, connection);
					if(count!=1){
						throw new EMPException("Remove Failed! Record Count: " + count);
					}
					flg = "done";
					context.addDataField("flag", flg);
				}	
				
			} catch (Exception e) {e.printStackTrace();}
			

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
