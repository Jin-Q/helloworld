package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtintbankbatchlist.lmtbatchcorre;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryIntbankDetailOp  extends CMISOperation {
	
	private final String modelId = "CusSameOrg";
	

	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}

			String cus_id_value = null;
			try {
				cus_id_value = (String)context.getDataValue("cus_id");
			} catch (Exception e) {}
			if(cus_id_value == null || cus_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id_value+"] cannot be null!");

			
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = "where cus_id= '"+ cus_id_value+"'";

			IndexedCollection iColl =dao.queryList(modelId, condition, connection);
			KeyedCollection kColl = null;							
			kColl = (KeyedCollection)iColl.get(0);
			SInfoUtils.addSOrgName(kColl, new String[]{"main_br_id","input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[]{"main_mgr","input_id"});
			
			this.putDataElement2Context(kColl, context);
			
			//Map<String,String> map = new HashMap<String,String>();
			//map.put("address", "STD_GB_AREA_ALL");//行业名称
			//SInfoUtils.addPopName(kColl, map,connection);
			
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
