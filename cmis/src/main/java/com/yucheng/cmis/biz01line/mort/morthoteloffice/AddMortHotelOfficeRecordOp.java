package com.yucheng.cmis.biz01line.mort.morthoteloffice;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddMortHotelOfficeRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortHotelOffice";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			kColl = (KeyedCollection)context.getDataElement(modelId);
			TableModelDAO dao = this.getTableModelDAO(context);
			String hotel_office_id = (String) kColl.getDataValue("hotel_office_id");
			if(!"".equals(hotel_office_id)){
				//修改操作
				dao.update(kColl, connection);
				context.addDataField("flag", "success");
			}else{
				//新增操作
				hotel_office_id = CMISSequenceService4JXXD.querySequenceFromDB("MT", "fromDate", connection, context);
				try {
					kColl = (KeyedCollection)context.getDataElement(modelId);
					kColl.setDataValue("hotel_office_id", hotel_office_id);
				} catch (Exception e) {}
				if(kColl == null || kColl.size() == 0)
					throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
				dao.insert(kColl, connection);
				context.addDataField("flag", "success");
			}
			
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
