package com.yucheng.cmis.biz01line.mort.mortexportreturntax;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddMortExportReturnTaxRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortExportReturnTax";
	
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
			String return_tax_id = (String) kColl.getDataValue("return_tax_id");
			if(!"".equals(return_tax_id)){
				//修改操作
				dao.update(kColl, connection);
				context.addDataField("flag", "success");
			}else{
				//新增操作
				return_tax_id = CMISSequenceService4JXXD.querySequenceFromDB("MT", "fromDate", connection, context);
				try {
					kColl = (KeyedCollection)context.getDataElement(modelId);
					kColl.setDataValue("return_tax_id", return_tax_id);
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
