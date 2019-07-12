package com.yucheng.cmis.biz01line.lmt.op.lmtindiv.lmtappindiv;

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
import com.yucheng.cmis.pub.PUBConstant;

public class UpdateLmtAppIndivRecordOp extends CMISOperation {

	private final String modelId = "LmtAppIndiv";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}
			//如果是自助额度修改
			if(context.containsKey("self")){
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
				lmtComponent.updateLmtAppIndivAmt(kColl.getDataValue("serno").toString());//根据流水号更新个人授信申请基表数据
			}
			
			context.addDataField("flag", PUBConstant.SUCCESS);
			context.addDataField("serno", kColl.getDataValue("serno"));
			context.addDataField("msg", "Y");
		}catch(Exception e){
			context.addDataField("serno", "");
			context.addDataField("flag", PUBConstant.FAIL);
			context.addDataField("msg", "更新自助额度失败，失败原因："+e.getMessage());
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
		return null;
	}
}
