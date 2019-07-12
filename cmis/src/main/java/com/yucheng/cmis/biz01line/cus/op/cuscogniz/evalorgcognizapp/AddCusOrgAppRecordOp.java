package com.yucheng.cmis.biz01line.cus.op.cuscogniz.evalorgcognizapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddCusOrgAppRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusOrgApp";
	private final String modelIdInfo = "CusOrgInfo";
	private final String modelIdInfoMng = "CusOrgInfoMng";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ","all", connection, context);
			String cus_id = (String)kColl.getDataValue("cus_id");
			if(cus_id==null||"".equals(cus_id)){//该机构已经存在
				cus_id = CMISSequenceService4JXXD.querySequenceFromDB("EVAL","all", connection, context);//产生评估机构客户码
				kColl.setDataValue("cus_id", cus_id);
			}else{
				String condition = " where cus_id='"+cus_id+"'";
				IndexedCollection iCollInfoMng = dao.queryList(modelIdInfoMng, condition, connection);
				for(int i=0;i<iCollInfoMng.size();i++){
					KeyedCollection kCollInfoMng = (KeyedCollection)iCollInfoMng.get(i);
					kCollInfoMng.setDataValue("cus_id", cus_id);
					kCollInfoMng.addDataField("app_serno", serno);
					kCollInfoMng.remove("serno");
					kCollInfoMng.setName(modelIdInfo);
					dao.insert(kCollInfoMng, connection);
				}
			}
			//add a record
			kColl.setDataValue("serno", serno);
			dao.insert(kColl, connection);
			context.addDataField("flag", serno);
			
		}catch (EMPException ee) {
			context.addDataField("flag", "false");
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", "false");
			throw new EMPException(e);			
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
