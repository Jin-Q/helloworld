package com.yucheng.cmis.biz01line.lmt.op.lmtapply;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddLmtApplyRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "LmtApply";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = "";
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			String manager_br_id = kColl.getDataValue("manager_br_id").toString();
			serno = CMISSequenceService4JXXD.querySequenceFromSQ("SQ", "all", manager_br_id, connection, context);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			kColl.setDataValue("serno", serno);
			dao.insert(kColl, connection);			
			
			//现不通过申请类型来区分是否需要复制台账信息  2013-12-12 唐顺岩
			//变更保存时将原有授信台账复制到申请分项及申请分项历史表
			//if(kColl.containsKey("app_type") && "02".equals(kColl.getDataValue("app_type"))){
			//}
			
			LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
			//复制数据到申请分项表
			lmtComponent.createLmtAppDetailsRecord((String)kColl.getDataValue("agr_no"), serno, (String)kColl.getDataValue("lrisk_type"));
			//复制数据到申请分项历史表
			lmtComponent.createLmtAppDetailsHisRecord((String)kColl.getDataValue("agr_no"), serno, (String)kColl.getDataValue("lrisk_type"));
			
			context.addDataField("flag", "success");
			context.addDataField("serno", serno);
			context.addDataField("message", "success");
		}catch(Exception e){
			context.addDataField("flag", "error");
			context.addDataField("serno", serno);
			context.addDataField("message", "失败原因："+e.getMessage());			
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
