package com.yucheng.cmis.platform.workflow.op.wfilvcreditright;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.TimeUtil;
/**
 * 
*@author lisj
*@time 2015-3-16
*@description TODO 需求编号：【XD150407025】分支机构授信审批权限配置
*@version v1.0
*
 */
public class DeleteWfiLvCreditRightRecordOp extends CMISOperation {

	private final String modelId = "WfiLvCreditRight";
	private final String pk_id_name = "pk_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String pk_id_value = null;
			try {
				pk_id_value = (String)context.getDataValue(pk_id_name);
			} catch (Exception e) {}
			if(pk_id_value == null || pk_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_id_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl  = dao.queryDetail(modelId, pk_id_value, connection);
			int count=dao.deleteByPk(modelId, pk_id_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.put("flag", PUBConstant.SUCCESS);
			//写入操作记录表
			try {
				KeyedCollection  wfiLCRLog = new KeyedCollection();
				String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
				String op_time = TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss");
				wfiLCRLog.put("operation_id", pk_id_value);
				wfiLCRLog.put("operation_org_id", (String)context.getDataValue("organNo"));//当前机构码
				wfiLCRLog.put("operation_staff", currentUserId);
				wfiLCRLog.put("operation_date", op_time);
				wfiLCRLog.put("right_type", (String)kColl.getDataValue("right_type"));
				wfiLCRLog.put("op_type", "delete");//操作类型
				wfiLCRLog.setName("WfiLcrLog");
				dao.insert(wfiLCRLog, connection);
			} catch (Exception e) {
				context.put("flag", "writelogEx");
			}
		}catch (EMPException ee) {
			context.put("flag", PUBConstant.FAIL);
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
