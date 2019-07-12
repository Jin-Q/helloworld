package com.yucheng.cmis.biz01line.iqp.op.iqpassetorgregi;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckIqpAssetOrgRegiAppOp extends CMISOperation {

	private final String modelId = "IqpAssetOrgRegi";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String serno = null;
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(serno == null || serno.length() == 0)
				throw new EMPJDBCException("The value serno cannot be null!");
			String asset_org_id = null;
			try {
				asset_org_id = (String)context.getDataValue("asset_org_id");
			} catch (Exception e) {}
			if(asset_org_id == null || asset_org_id.length() == 0)
				throw new EMPJDBCException("The value asset_org_id cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("serno",serno);
			pkMap.put("asset_org_id",asset_org_id);
			KeyedCollection kcoll = dao.queryDetail(modelId, pkMap, connection);
			
			if(null!=kcoll.getDataValue("serno")&&!"".equals(kcoll.getDataValue("serno"))){
				context.addDataField("flag", "error");
				context.addDataField("msg", "该参与机构信息已存在！");
			}else{
				context.addDataField("flag", "success");
				context.addDataField("msg", "");
			}
			
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
			context.addDataField("msg", "该参与机构信息已存在，失败原因："+ee.getMessage());
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
