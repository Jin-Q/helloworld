package com.yucheng.cmis.biz01line.lmt.op.lmtquotaadjust;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddLmtQuotaAdjustRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtQuotaAdjustApp";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement("LmtQuotaAdjust");
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			TableModelDAO dao = this.getTableModelDAO(context);
			String agr_no = "";
			if(kColl.containsKey("fin_agr_no")&&kColl.getDataValue("fin_agr_no")!=null&&!"".equals(kColl.getDataValue("fin_agr_no"))){
				agr_no = (String)kColl.getDataValue("fin_agr_no");
			}
			String conditionStr = "where fin_agr_no='"+agr_no+"' and approve_status in ('000','111','992','993') and serno is not null order by inure_date asc,end_date asc";
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,connection);
			if(iColl!=null&&iColl.size()>0){
				KeyedCollection kColl1 = (KeyedCollection) iColl.get(0);
				String fin_serno = (String) kColl1.getDataValue("fin_serno");
				kColl.put("fin_serno", fin_serno);
			}
			//add a record
			
			kColl.setName(modelId);
			dao.insert(kColl, connection);
			context.addDataField("flag", "success");
			
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
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
