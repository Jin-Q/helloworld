package com.yucheng.cmis.biz01line.mort.mortreservecredit;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class AddMortReserveCreditLeadOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortReserveCredit";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String guaranty_no = "";
		String conditionStr = "";
		try {
			connection = this.getConnection(context);
			guaranty_no=(String) context.getDataValue("guaranty_no");
			TableModelDAO dao = this.getTableModelDAO(context);
			conditionStr = "where guaranty_no ='"+guaranty_no+"'";
			IndexedCollection iColl = dao.queryList(modelId, conditionStr, connection);
			KeyedCollection kColl = new KeyedCollection(modelId);
			if(iColl.size()!=0){
			   kColl = (KeyedCollection) iColl.get(0);
			}else{
			   kColl.addDataField("guaranty_no", guaranty_no);
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
