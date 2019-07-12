package com.yucheng.cmis.biz01line.lmt.op.bizarea.lmtappnamelist;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteLmtAppNameListRecordOp extends CMISOperation {

	private final String modelId = "LmtAppNameList";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String serno_value = null;
			String cus_id = "";
			try {
				serno_value = (String)context.getDataValue("serno");
				cus_id = (String)context.getDataValue("cus_id");
			} catch (Exception e) {}
			if(serno_value == null || "".equals(serno_value))
				throw new EMPJDBCException("The value of pk[serno] cannot be null!");
			
			if(cus_id==null||"".equals(cus_id))
				throw new EMPJDBCException("The value of pk[cus_id] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String, String> pkMap = new HashMap<String, String>();
			pkMap.put("serno", serno_value);
			pkMap.put("cus_id", cus_id);
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			//删除成员额度信息
			Map<String,String> refFields = new HashMap<String,String>();
            refFields.put("serno", serno_value);
            refFields.put("cus_id", cus_id);
			LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
			lmtComponent.deleteByField("LmtAppDetails", refFields);
			
			context.addDataField("flag", PUBConstant.SUCCESS);
		}catch (EMPException ee) {
			context.addDataField("flag", PUBConstant.FAIL);
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", PUBConstant.FAIL);
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
