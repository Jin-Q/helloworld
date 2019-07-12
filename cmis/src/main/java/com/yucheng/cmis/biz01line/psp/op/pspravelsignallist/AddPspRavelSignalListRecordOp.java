package com.yucheng.cmis.biz01line.psp.op.pspravelsignallist;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class AddPspRavelSignalListRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "PspRavelSignalList";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			String serno = null;
			String pk_id = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			serno = (String)kColl.getDataValue("serno");
			pk_id = (String)kColl.getDataValue("pk_id");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> mapValue = new HashMap<String,String>();
			mapValue.put("serno", serno);
			mapValue.put("pk_id", pk_id);
			KeyedCollection kCollTmp = dao.queryDetail(modelId, mapValue, connection);
			if(kCollTmp!=null&&kCollTmp.getDataValue("serno")!=null&&!"".equals(kCollTmp.getDataValue("serno"))){//数据已存在
				context.put("flag", PUBConstant.EXISTS);
			}else{
				dao.insert(kColl, connection);
				context.put("flag", PUBConstant.SUCCESS);
			}
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
