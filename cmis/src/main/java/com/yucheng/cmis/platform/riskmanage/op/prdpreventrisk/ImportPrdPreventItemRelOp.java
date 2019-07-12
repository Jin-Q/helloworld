package com.yucheng.cmis.platform.riskmanage.op.prdpreventrisk;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class ImportPrdPreventItemRelOp extends CMISOperation {
	private static final String relModel = "PrdPvRiskItemRel";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String item_id = (String)context.getDataValue("item_id");
			String prevent_id = (String)context.getDataValue("preventId");
			if(item_id == null || item_id.trim().length() == 0){
				throw new EMPException("导入拦截项获取拦截项ID失败！");
			}
			if(prevent_id == null || prevent_id.trim().length() == 0){
				throw new EMPException("导入拦截项获取拦截方案ID失败！");
			}
			
			KeyedCollection relKColl = new KeyedCollection();
			relKColl.addDataField("prevent_id", prevent_id);
			relKColl.addDataField("item_id", item_id);
			relKColl.setName(relModel);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(relKColl, connection);
			
			context.addDataField("flag", "success");
		} catch (Exception e) {
			context.addDataField("flag", "falied");
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
