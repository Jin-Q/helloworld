package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtsiglmt;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class SaveLmtSubAppOp extends CMISOperation {
	
	private final String modelId = "LmtSubAppList";
	private final String modelIdApp = "LmtSubApp";

	@Override
	//保存授信分项信息
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			
			IndexedCollection iColl = null;
			KeyedCollection kColl = null;
			connection = this.getConnection(context);
			try {
				iColl = (IndexedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(iColl == null || iColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			
			TableModelDAO dao = this.getTableModelDAO(context);
			for(int i=0; i<iColl.size(); i++){
				kColl = (KeyedCollection)iColl.get(i);				
				kColl.remove("displayid");	
				kColl.setName(modelIdApp);
				String serno = (String)kColl.getDataValue("serno");
				String variet_no = (String)kColl.getDataValue("variet_no");
				String condition ="where serno='"+serno+"' and variet_no='"+variet_no+"'";
				IndexedCollection iColl_sub = dao.queryList(modelIdApp, condition, connection);
				if(iColl_sub.size()>0){//已有数据，修改额度
					dao.update(kColl, connection);
				}else{//没有分项授信，进行授信
					dao.insert(kColl, connection);
				}			
			}
			context.addDataField("flag", "success");
		}catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}	

}
