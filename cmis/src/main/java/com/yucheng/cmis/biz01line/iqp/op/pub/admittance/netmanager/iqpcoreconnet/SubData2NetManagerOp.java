package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpcoreconnet;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
		/**
		 * 提交数据保存到网络管理中
		 * 
		 * */

public class SubData2NetManagerOp extends CMISOperation {
	private final String modelId = "IqpCoreConNet";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno_value = context.getDataValue("serno").toString();
			TableModelDAO dao= this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryAllDetail(modelId, serno_value, connection);
		    kColl.setDataValue("approve_status", "997");//修改状态为通过
		    dao.update(kColl, connection);//修改核心企业建网的审批状态
		    kColl.addDataField("status", "1");	
		    kColl.remove("app_type");
		    kColl.remove("approve_status");
		    kColl.setName("IqpNetMagInfo");
		    dao.insert(kColl, connection);
		    context.addDataField("flag", "success");
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
