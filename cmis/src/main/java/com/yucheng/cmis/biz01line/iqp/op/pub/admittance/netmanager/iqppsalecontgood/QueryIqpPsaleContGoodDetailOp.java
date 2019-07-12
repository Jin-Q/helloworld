package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqppsalecontgood;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryIqpPsaleContGoodDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpPsaleContGood";
	

	private final String psale_cont_name = "psale_cont";
	private final String commo_name_name = "commo_name";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String psale_cont_value = null;
			try {
				psale_cont_value = (String)context.getDataValue(psale_cont_name);
			} catch (Exception e) {}
			if(psale_cont_value == null || psale_cont_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+psale_cont_name+"] cannot be null!");

			String commo_name_value = null;
			try {
				commo_name_value = (String)context.getDataValue(commo_name_name);
			} catch (Exception e) {}
			if(commo_name_value == null || commo_name_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+commo_name_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("psale_cont",psale_cont_value);
			pkMap.put("commo_name",commo_name_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			SInfoUtils.addPrdPopName("IqpMortCatalogMana", kColl, "commo_name", "catalog_no", "catalog_name", "->", connection, dao); //翻译上级目录
			this.putDataElement2Context(kColl, context);
			
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
