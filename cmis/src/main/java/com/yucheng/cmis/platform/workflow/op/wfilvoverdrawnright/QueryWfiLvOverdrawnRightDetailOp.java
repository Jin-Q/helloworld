package com.yucheng.cmis.platform.workflow.op.wfilvoverdrawnright;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
/**
 * 
*@author wangj
*@time 2015-5-14
*@description TODO 需求编号：【XD141222087】法人账户透支需求变更
*@version v1.0
*
 */
public class QueryWfiLvOverdrawnRightDetailOp  extends CMISOperation {
	
	private final String modelId = "WfiLvOverdrawnRight";
	

	private final String pk_id_name = "pk_id";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String pk_id_value = null;
			try {
				pk_id_value = (String)context.getDataValue(pk_id_name);
			} catch (Exception e) {}
			if(pk_id_value == null || pk_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_id_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, pk_id_value, connection);
			this.putDataElement2Context(kColl, context);
			SInfoUtils.addSOrgName(kColl, new String[]{"org_id"});
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
