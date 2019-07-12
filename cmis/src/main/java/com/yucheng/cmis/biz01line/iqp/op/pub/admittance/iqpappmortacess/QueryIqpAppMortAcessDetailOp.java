package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpappmortacess;

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
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpAppMortAcessDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpAppMortAcess";
	private final String modelId1 = "IqpAppMortDetail";

	private final String serno_name = "serno";
	
	
	private boolean updateCheck = true;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			IndexedCollection iColldetail = dao.queryList(modelId1, "where serno ='"+serno_value+"'", connection);
			iColldetail.setName("IqpAppMortDetailList");
			SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id"});
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id"});
			String[] args=new String[] { "sup_catalog_no" };
			String[] modelIds=new String[]{"IqpMortCatalogMana"};
			String[] modelForeign=new String[]{"catalog_no"};
			String[] fieldName=new String[]{"catalog_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColldetail, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			this.putDataElement2Context(kColl, context);
			this.putDataElement2Context(iColldetail, context);
			
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
