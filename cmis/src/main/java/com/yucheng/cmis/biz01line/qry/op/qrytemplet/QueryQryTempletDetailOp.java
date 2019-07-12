package com.yucheng.cmis.biz01line.qry.op.qrytemplet;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryQryTempletDetailOp extends CMISOperation {

	private final String modelId = "QryTemplet";

	private final String temp_no_name = "temp_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String condition="";

			String temp_no_value = null;
			try {
				temp_no_value = (String)context.getDataValue(temp_no_name);
			} catch (Exception e) {}
			if(temp_no_value == null || temp_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+temp_no_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryAllDetail(modelId, temp_no_value, connection);
			
			if(null!=kColl.getDataValue("organlevel") && !"".equals(kColl.getDataValue("organlevel"))){
				//翻译机构
				SInfoUtils.addPrdPopName("SOrg",kColl, "organlevel", "organno", "organname", ",", connection, dao);  //翻译适用机构
			}
			
			this.putDataElement2Context(kColl, context);

			condition=" where temp_no='"+temp_no_value+"'";
			IndexedCollection iColl_QryParam = dao.queryList("QryParam",condition, connection);
			this.putDataElement2Context(iColl_QryParam, context);
			
			condition=" where temp_no='"+temp_no_value+"'";
			IndexedCollection iColl_QryResult = dao.queryList("QryResult",condition, connection);
			
			this.putDataElement2Context(iColl_QryResult, context);
			
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
