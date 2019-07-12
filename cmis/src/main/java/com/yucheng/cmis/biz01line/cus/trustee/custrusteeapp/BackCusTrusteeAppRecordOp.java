package com.yucheng.cmis.biz01line.cus.trustee.custrusteeapp;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class BackCusTrusteeAppRecordOp extends CMISOperation {

	private final String modelId = "CusTrusteeInfo";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = "suc";
		try{
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			connection = this.getConnection(context);
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict("CusTrusteeInfo", context, connection);
			
			TableModelDAO dao = getTableModelDAO(context);
			String serno = (String) context.getDataValue("serno");
			KeyedCollection kColl = dao.queryFirst(modelId, null, " where serno = '" + serno + "'", connection);
			String consignor_id = (String)kColl.getDataValue("consignor_id");
			String trustee_id = (String)kColl.getDataValue("trustee_id");
			
			//删除关联表中的记录
			CusBaseComponent comp = (CusBaseComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(
					PUBConstant.CUSBASE, context, connection);
			comp.delCusTrusteeRecord(consignor_id, trustee_id);
			//设置为回收状态
			kColl.setDataValue("trustee_status", "2");
			kColl.setDataValue("retract_date", context.getDataValue(PUBConstant.OPENDAY));
			dao.update(kColl, connection);
			context.put("flag", flag);
			context.put("msg", "success");
		}catch(Exception e){
			context.put("flag", "fail");
			context.put("msg", "操作失败，原因："+e.getMessage());			
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
