package com.yucheng.cmis.biz01line.prd.op.prdsubtabactivity.prdsubtabaction;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckPrdSubTabActionOp  extends CMISOperation {
	
	private final String modelId = "PrdSubTabAction";
	private final String mainactid = "main_act_id";
	private final String mainid = "main_id";
	private final String subid = "sub_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String main_act_id="";
			String main_id="";
			String sub_id="";
			try {
				main_act_id = (String)context.getDataValue(mainactid);
				main_id = (String)context.getDataValue(mainid);
				sub_id = (String)context.getDataValue(subid);
			} catch (Exception e) {}
			if(main_act_id == null || main_act_id.length() == 0)
				throw new EMPJDBCException("The value of pk["+main_act_id+"] cannot be null!");
			
			String conditionStr = " where main_id = '"+main_id+"' and sub_id = '"+sub_id+"' and main_act_id = '"+main_act_id+"'";
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, conditionStr, connection);
			String sub_act_id_str = "";
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				String sub_act_id =(String)kColl.getDataValue("sub_act_id");
				sub_act_id_str = sub_act_id_str+sub_act_id+",";
			}
			
		    context.addDataField("subactid", sub_act_id_str); 
		    context.addDataField("flag", "succ");
			
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
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
