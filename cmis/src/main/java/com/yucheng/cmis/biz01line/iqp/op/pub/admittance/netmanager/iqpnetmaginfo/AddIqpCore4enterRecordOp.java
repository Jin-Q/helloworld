package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpnetmaginfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddIqpCore4enterRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpNetMagInfo";
	
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
		    String net_agr_no=(String)kColl.getDataValue("net_agr_no");
		    String app_type=(String)kColl.getDataValue("app_type");
		    String condition="where net_agr_no='"+net_agr_no+"' and app_type='"+app_type+"'";
		    String serno=CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
		    //验证是否已存在一笔入网申请
		    IndexedCollection iColl=dao.queryList("IqpCoreConNet", condition, connection);
		    if(iColl.isEmpty())
		    {
		    	kColl.setDataValue("serno", serno);
		    	kColl.setDataValue("app_type", app_type);
		    	kColl.addDataField("approve_status", "000");
		    	kColl.remove("status");
		    	kColl.setName("IqpCoreConNet");
		    	dao.insert(kColl, connection);
		    }else
		    {
		    	KeyedCollection kColl2=(KeyedCollection)iColl.get(0);
		    	String serno_value=(String)kColl2.getDataValue("serno");
		    	kColl.setDataValue("serno", serno_value);
		    	kColl.addDataField("approve_status", "000");
		    	kColl.setDataValue("app_type", app_type);
		    	kColl.setName("IqpCoreConNet");
		    	kColl.remove("status");
		    	dao.update(kColl, connection);
		    }			
		   context.addDataField("flag", "success");	
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
