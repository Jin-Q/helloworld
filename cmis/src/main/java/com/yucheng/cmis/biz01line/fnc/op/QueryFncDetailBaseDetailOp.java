package com.yucheng.cmis.biz01line.fnc.op;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryFncDetailBaseDetailOp extends CMISOperation {

	private final String modelId = "FncDetailBase";

	private final String pk_name = "pk";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String condition="";

			String pk_value = null;
			try {
				pk_value = (String)context.getDataValue(pk_name);
			} catch (Exception e) {}
			if(pk_value == null || pk_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryAllDetail(modelId, pk_value, connection);
			this.putDataElement2Context(kColl, context);

			condition=" where pk='"+pk_value+"'";
			IndexedCollection iColl_FncAccPayable = dao.queryList("FncAccPayable",condition, connection);
			this.putDataElement2Context(iColl_FncAccPayable, context);
			
			condition=" where pk='"+pk_value+"'";
			IndexedCollection iColl_FncAccReceivable = dao.queryList("FncAccReceivable",condition, connection);
			this.putDataElement2Context(iColl_FncAccReceivable, context);
			
			condition=" where pk='"+pk_value+"'";
			IndexedCollection iColl_FncAssure = dao.queryList("FncAssure",condition, connection);
			this.putDataElement2Context(iColl_FncAssure, context);
			
			condition=" where pk='"+pk_value+"'";
			IndexedCollection iColl_FncFixedAsset = dao.queryList("FncFixedAsset",condition, connection);
			this.putDataElement2Context(iColl_FncFixedAsset, context);
			
			condition=" where pk='"+pk_value+"'";
			IndexedCollection iColl_FncInventory = dao.queryList("FncInventory",condition, connection);
			this.putDataElement2Context(iColl_FncInventory, context);
			
			condition=" where pk='"+pk_value+"'";
			IndexedCollection iColl_FncInvestment = dao.queryList("FncInvestment",condition, connection);
			this.putDataElement2Context(iColl_FncInvestment, context);
			
			condition=" where pk='"+pk_value+"'";
			IndexedCollection iColl_FncLoan = dao.queryList("FncLoan",condition, connection);
			this.putDataElement2Context(iColl_FncLoan, context);
			
			condition=" where pk='"+pk_value+"'";
			IndexedCollection iColl_FncOtherPayable = dao.queryList("FncOtherPayable",condition, connection);
			this.putDataElement2Context(iColl_FncOtherPayable, context);
			
			condition=" where pk='"+pk_value+"'";
			IndexedCollection iColl_FncOtherRecv = dao.queryList("FncOtherRecv",condition, connection);
			this.putDataElement2Context(iColl_FncOtherRecv, context);
			
			condition=" where pk='"+pk_value+"'";
			IndexedCollection iColl_FncProject = dao.queryList("FncProject",condition, connection);
			this.putDataElement2Context(iColl_FncProject, context);
			
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
