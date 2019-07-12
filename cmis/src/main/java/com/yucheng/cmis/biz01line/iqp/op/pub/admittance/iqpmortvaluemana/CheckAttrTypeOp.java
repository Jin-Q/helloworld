package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpmortvaluemana;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.component.CatalogManaComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class CheckAttrTypeOp extends CMISOperation {
	
	private final String modelId = "IqpMortCatalogMana";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			String catalog_path = (String) context.getDataValue("catalog_path");
			String arr[] = catalog_path.split(",");
			String catalog_no = arr[arr.length-1];
			KeyedCollection kC = dao.queryDetail(modelId, catalog_no, connection);
			String attr_type = (String) kC.getDataValue("attr_type");
			context.addDataField("attr_type",attr_type);
		}catch(Exception e){
			context.addDataField("flag","N");
			try {
				connection.rollback();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
