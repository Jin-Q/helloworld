package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpoverseeagr;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryAccMortvalueRemindDetailOp  extends CMISOperation {
	
	private final String modelId = "AccMortvalueRemind";
	
	private final String cargo_id_name = "cargo_id";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String cargo_id = null;
			try {
				cargo_id = (String)context.getDataValue(cargo_id_name);
			} catch (Exception e) {}
			if(cargo_id == null || cargo_id.length() == 0)
				throw new EMPJDBCException("The value of pk["+cargo_id_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, cargo_id, connection);
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id"});
			//翻译目录
			SInfoUtils.addPrdPopName("IqpMortCatalogMana", kColl, "catalog_no", "catalog_no", "catalog_name", "->", connection, dao);  //翻译目录路径			
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
