package com.yucheng.cmis.biz01line.mort.mortguarantyinsurinfo;

import java.sql.Connection;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckInsuaranceNoOp extends CMISOperation {
	
	//operation TableModel
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String insuarance_no = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			insuarance_no = (String) context.getDataValue("insuarance_no");
			try {
					KeyedCollection kc = dao.queryAllDetail("MortGuarantyInsurInfo", insuarance_no, this.getConnection(context));
					if(null==(String)kc.getDataValue("insuarance_no")){
						context.addDataField("check", "true");
					}else{
						context.addDataField("check", "false");
					}	
			} catch (Exception e) {}
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
