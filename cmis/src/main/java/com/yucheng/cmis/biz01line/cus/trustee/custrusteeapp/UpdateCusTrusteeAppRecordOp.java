package com.yucheng.cmis.biz01line.cus.trustee.custrusteeapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class UpdateCusTrusteeAppRecordOp extends CMISOperation {
	

	private final String modelId = "CusTrusteeApp";
	private final String modelId1 = "CusTrustee";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = "success";
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			String deal_flag = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			try {
				deal_flag = (String)context.getDataValue("deal_flag");
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection KColl_his = dao.queryFirst(modelId1, null, " where consignor_id = '"+kColl.getDataValue("consignor_id")+"' and trustee_id = '"+kColl.getDataValue("trustee_id")+"'", connection);
			KeyedCollection KColl_app = dao.queryFirst(modelId, null, " where consignor_id = '"+kColl.getDataValue("consignor_id")+"' and trustee_id = '"+kColl.getDataValue("trustee_id")+"' and approve_status in ('000','111','992','993') ", connection);
			
			if("add".equals(deal_flag)){
				if((KColl_his!=null&&KColl_his.size()>0&&KColl_his.getDataValue("consignor_id")!=null&&!"".equals(KColl_his.getDataValue("consignor_id")))||(KColl_app!=null&&KColl_app.size()>0&&KColl_app.getDataValue("consignor_id")!=null&&!"".equals(KColl_app.getDataValue("consignor_id")))){
					flag = "error1";
				}else{
					dao.insert(kColl, connection);
				}
			}else{
				dao.update(kColl, connection);
			}
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", "error");
			throw new EMPException(e);
		} finally {
			context.addDataField("flag", flag);
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
