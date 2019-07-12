package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtbatchlmt;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryLmtSigLmt4BatchRecordOp extends CMISOperation {

	private final String modelId = "LmtSigLmt";

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String cus_id_value = null;
		try{
			connection = this.getConnection(context);
			String condition="";
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			
			
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryAllDetail(modelId, serno_value, connection);
			cus_id_value = (String)kColl.getDataValue("cus_id");
			String condition1 = "where cus_id='"+cus_id_value+"'";
			KeyedCollection kColl_cus =dao.queryFirst("CusSameOrg", null, condition1, connection);
			
			String same_org_cnname =(String)kColl_cus.getDataValue("same_org_cnname");
			String same_org_type =(String)kColl_cus.getDataValue("same_org_type");
			String crd_grade =(String)kColl_cus.getDataValue("crd_grade");
			String assets =(String)kColl_cus.getDataValue("assets");
			
			kColl.addDataField("same_org_cnname", same_org_cnname);
			kColl.addDataField("same_org_type", same_org_type);
			kColl.addDataField("crd_grade", crd_grade);
			kColl.setDataValue("asserts", assets);
			
			//翻译成中文
			SInfoUtils.addSOrgName(kColl, new String []{"input_br_id"});
			SInfoUtils.addUSerName(kColl, new String []{"input_id"});
		
			this.putDataElement2Context(kColl, context);

			condition=" where serno='"+serno_value+"'";
			IndexedCollection iColl_LmtSubApp = dao.queryList("LmtSubApp",condition, connection);
			this.putDataElement2Context(iColl_LmtSubApp, context);
			
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
