package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtappnamelist;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class UpdateLmtAppNameListRecordOp extends CMISOperation {
	
	private final String modelId = "LmtAppNameList";

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
			//若是否设置额度置为否则删除额度分项信息
			String is_limit_set = (String)kColl.getDataValue("is_limit_set");
			if("2".equals(is_limit_set)){
				Map<String,String> refFields = new HashMap<String,String>();
				refFields.put("serno", (String)kColl.getDataValue("serno"));
				refFields.put("cus_id", (String)kColl.getDataValue("cus_id"));
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
				lmtComponent.deleteByField("LmtAppDetails", refFields);
			}
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
