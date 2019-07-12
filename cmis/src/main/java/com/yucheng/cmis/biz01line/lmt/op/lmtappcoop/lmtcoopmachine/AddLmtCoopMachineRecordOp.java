package com.yucheng.cmis.biz01line.lmt.op.lmtappcoop.lmtcoopmachine;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddLmtCoopMachineRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtCoopMachine";
	
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
			
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			
			//得到拟按揭设备信息
			IndexedCollection iColl = (IndexedCollection)context.getDataElement("LmtSchedEquipList");
			if(null != iColl && iColl.size()>0){
				for (Iterator<KeyedCollection> iterator = iColl.iterator(); iterator.hasNext();) {
					KeyedCollection kColl_equip = (KeyedCollection) iterator.next();
					kColl_equip.setName("LmtSchedEquip");
					dao.insert(kColl_equip, connection);
				}
			}
			
			dao.deleteAllByPk(modelId, "", connection);
			
			context.addDataField("msg", "Y");
			context.addDataField("flag", "Y");
		}catch(Exception e){
			context.addDataField("msg", e.getMessage());
			context.addDataField("flag","N");
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
