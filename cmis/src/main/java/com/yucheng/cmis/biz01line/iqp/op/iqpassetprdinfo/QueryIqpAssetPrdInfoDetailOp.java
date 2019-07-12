package com.yucheng.cmis.biz01line.iqp.op.iqpassetprdinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryIqpAssetPrdInfoDetailOp  extends CMISOperation {
	
	/**添加项目金额的值，将其传入到后台并用于返回页面     2014-08-05 邓亚辉*/
	private final String modelId = "IqpAssetPrdInfo";
	private final String iqpassetproappModel = "IqpAssetProApp";
	private final String prd_id_name = "prd_id";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = "";
		String pro_amt = "";
		try{
			connection = this.getConnection(context);
			
			
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String prd_id_value = null;
			try {
				prd_id_value = (String)context.getDataValue(prd_id_name);
			} catch (Exception e) {}
			if(prd_id_value == null || prd_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+prd_id_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, prd_id_value, connection);
			serno = (String)kColl.getDataValue("serno");
			//将项目金额查出
		    if(serno!=null && serno != ""){
		    	IndexedCollection iapaIcoll = dao.queryList(iqpassetproappModel,  " where serno='"+serno+"'", connection);
		    	if(iapaIcoll != null && iapaIcoll.size() > 0){
		    		KeyedCollection kc = (KeyedCollection) iapaIcoll.get(0);
		    		pro_amt=(String) kc.getDataValue("pro_amt");
		    		context.addDataField("pro_amt", pro_amt);
		    	}
		    }
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
