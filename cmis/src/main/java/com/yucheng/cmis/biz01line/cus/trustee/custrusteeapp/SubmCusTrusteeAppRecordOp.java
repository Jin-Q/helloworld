package com.yucheng.cmis.biz01line.cus.trustee.custrusteeapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class SubmCusTrusteeAppRecordOp extends CMISOperation {

	private final String modelId = "CusTrustee";
	private final String modelIdApp = "CusTrusteeApp";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = getTableModelDAO(context);
			String serno = (String) context.getDataValue("serno");
			String openDate = context.getDataValue("OPENDAY").toString();	
			//根据serno获得提交的数据
			KeyedCollection kColl_App = dao.queryFirst(modelIdApp, null, " where serno = '" + serno + "'", connection);
			kColl_App.put("approve_status", "997");
			dao.update(kColl_App, connection);
			KeyedCollection kColl_Info = new KeyedCollection();
			kColl_Info.put("serno", kColl_App.getDataValue("serno"));
			kColl_Info.put("consignor_type", kColl_App.getDataValue("consignor_type"));
			kColl_Info.put("is_provid_accredit", kColl_App.getDataValue("is_provid_accredit"));
			kColl_Info.put("consignor_id", kColl_App.getDataValue("consignor_id"));
			kColl_Info.put("consignor_br_id", kColl_App.getDataValue("consignor_br_id"));
			kColl_Info.put("trustee_id", kColl_App.getDataValue("trustee_id"));
			kColl_Info.put("trustee_br_id", kColl_App.getDataValue("trustee_br_id"));
			kColl_Info.put("trustee_detail", kColl_App.getDataValue("trustee_detail"));
			/* added by yangzy 2015/06/04 托管增加托管日期 start */
			kColl_Info.put("trustee_date", kColl_App.getDataValue("trustee_date"));
			/* added by yangzy 2015/06/04 托管增加托管日期 end */
			kColl_Info.put("supervise_date", kColl_App.getDataValue("supervise_date"));
			kColl_Info.put("trustee_status", "1");
			kColl_Info.put("input_id", kColl_App.getDataValue("input_id"));
			kColl_Info.put("input_br_id", kColl_App.getDataValue("input_br_id"));
			kColl_Info.put("input_date", kColl_App.getDataValue("input_date"));
			kColl_Info.setName("CusTrusteeInfo");
			dao.insert(kColl_Info, connection);
			
			KeyedCollection kColl_Detail = new KeyedCollection();
			kColl_Detail.put("consignor_id", kColl_App.getDataValue("consignor_id"));
			kColl_Detail.put("trustee_id", kColl_App.getDataValue("trustee_id"));
			/* added by yangzy 2015/06/04 托管增加托管日期 start */
			kColl_Detail.put("trustee_date", kColl_App.getDataValue("trustee_date"));
			/* added by yangzy 2015/06/04 托管增加托管日期 end */
			kColl_Detail.setName("CusTrustee");
			dao.insert(kColl_Detail, connection);	
			context.addDataField("flag", "suc");
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
