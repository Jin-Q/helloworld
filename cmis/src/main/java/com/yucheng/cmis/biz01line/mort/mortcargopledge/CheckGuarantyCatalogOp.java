package com.yucheng.cmis.biz01line.mort.mortcargopledge;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckGuarantyCatalogOp extends CMISOperation {

	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String guaranty_no = null;
			String guaranty_catalog = null;
			try {
				guaranty_no = (String)context.getDataValue("guaranty_no");
				guaranty_catalog = (String)context.getDataValue("guaranty_catalog");
			} catch (Exception e) {}
			if(guaranty_catalog == null || guaranty_catalog.length() == 0)
				throw new EMPJDBCException("The value of pk["+guaranty_catalog+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kc = dao.queryDetail("MortGuarantyBaseInfo", guaranty_no, connection);
			String guaranty_type = (String) kc.getDataValue("guaranty_type");
			if(guaranty_catalog.indexOf(guaranty_type)==-1){//货物的押品所属目录是否处于抵质押物类型项下（否时）
				context.addDataField("flag","fail");
			}else{
				context.addDataField("flag","success");
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
