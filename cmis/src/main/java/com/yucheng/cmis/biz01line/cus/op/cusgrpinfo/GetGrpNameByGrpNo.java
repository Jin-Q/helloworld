package com.yucheng.cmis.biz01line.cus.op.cusgrpinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class GetGrpNameByGrpNo extends CMISOperation{

	public String doExecute(Context context) throws EMPException {
		Connection connection =null;
		String grp_no = null;
		String serno = null;  
		TableModelDAO dao = this.getTableModelDAO(context);
		try{
			connection = this.getConnection(context);	
			grp_no = (String)context.getDataValue("grp_no");  
			try
			{
				serno = (String)context.getDataValue("serno");
			}catch(EMPException e)
			{
				
			}
			if(serno == null)
				serno="";
			KeyedCollection kColl = null;
			KeyedCollection kCollLmtApply = null;
			kColl = dao.queryAllDetail("CusGrpInfo", grp_no, connection);			
			context.addDataField("grp_name", (String)kColl.getDataValue("grp_name"));
			if(!serno.trim().equals(""))
			{
				kCollLmtApply = dao.queryAllDetail("LmtApply", serno, connection);	
				context.addDataField("cus_name", (String)kCollLmtApply.getDataValue("cus_name"));
			}else
			{
				context.addDataField("cus_name", "");
			}
		} catch (EMPException ee) {
			
		}
		 return "0";
	}
}
