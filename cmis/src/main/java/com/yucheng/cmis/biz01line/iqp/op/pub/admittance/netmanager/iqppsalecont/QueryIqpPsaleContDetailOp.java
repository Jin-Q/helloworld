package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqppsalecont;

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

public class QueryIqpPsaleContDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpPsaleCont";	
	private final String psale_cont_name = "psale_cont";		
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
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, psale_cont_value, connection);
			String[] args=new String[] { "buyer_cus_id","barg_cus_id" };
		    String[] modelIds=new String[]{"CusBase","CusBase"};
		    String[] modelForeign=new String[]{"cus_id","cus_id"};
		    String[] fieldName=new String[]{"cus_name","cus_name"};
			//详细信息翻译时调用			
           SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
           SInfoUtils.addPrdPopName("IqpMortCatalogMana", kColl, "commo_name", "catalog_no", "catalog_name", "", connection, dao); //翻译上级目录
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
