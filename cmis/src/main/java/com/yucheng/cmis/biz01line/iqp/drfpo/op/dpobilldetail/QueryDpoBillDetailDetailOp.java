package com.yucheng.cmis.biz01line.iqp.drfpo.op.dpobilldetail;

import java.net.URLDecoder;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryDpoBillDetailDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpBillDetailInfo";
	

	private final String porder_no_name = "porder_no";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String porder_no_value = null;
			try {
				porder_no_value = (String)context.getDataValue(porder_no_name);
				//中文转码
				porder_no_value = URLDecoder.decode(porder_no_value,"UTF-8");
			} catch (Exception e) {}
			if(porder_no_value == null || porder_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+porder_no_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, porder_no_value, connection);
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
