package com.yucheng.cmis.biz01line.lmt.op.lmtappcoop;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeleteLmtAppJointCoopRecordOp extends CMISOperation {

	private final String modelId = "LmtAppJointCoop";

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			connection = this.getConnection(context);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);

			String serno_value = null;
			String coop_type_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
				coop_type_value = (String)context.getDataValue("coop_type");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
				
			TableModelDAO dao = this.getTableModelDAO(context);
			
			if("001".equals(coop_type_value) || "002".equals(coop_type_value) ||"003".equals(coop_type_value)){
				 Map<String,String> refFields = new HashMap<String,String>();
	             refFields.put("serno", serno_value);
	             
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
				if("001".equals(coop_type_value)){
					lmtComponent.deleteByField("LmtCoopBuilding", refFields);
				}else if("002".equals(coop_type_value)){
					lmtComponent.deleteByField("LmtCoopCar", refFields);
				}else if("003".equals(coop_type_value)){
					//根据合作方流水号删除原有拟按揭设备信息
					lmtComponent.deleteLmtSchedEquip(serno_value);
					
					lmtComponent.deleteByField("LmtCoopMachine", refFields);
				}
			}
			
			int count=dao.deleteByPk(modelId, serno_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			
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
