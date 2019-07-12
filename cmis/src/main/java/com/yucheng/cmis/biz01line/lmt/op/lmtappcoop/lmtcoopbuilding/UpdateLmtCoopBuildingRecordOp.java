package com.yucheng.cmis.biz01line.lmt.op.lmtappcoop.lmtcoopbuilding;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateLmtCoopBuildingRecordOp extends CMISOperation {
	
	private final String modelId = "LmtCoopBuilding";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0){
				context.addDataField("msg", "未获取到合作方楼盘项目信息！");
				context.addDataField("flag","N");
				throw new Exception("未获取到合作方楼盘项目信息！");
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			if(count!=1){
				context.addDataField("msg", "合作方楼盘项目信息更新失败！");
				context.addDataField("flag","N");
				throw new Exception("合作方楼盘项目信息更新失败！");
			}
			context.addDataField("msg", "更新成功！");
			context.addDataField("flag","Y");
			
		}catch(Exception e){
			context.addDataField("msg", e.getMessage());
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
