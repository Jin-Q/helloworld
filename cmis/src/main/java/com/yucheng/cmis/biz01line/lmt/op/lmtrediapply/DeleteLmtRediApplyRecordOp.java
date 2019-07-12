package com.yucheng.cmis.biz01line.lmt.op.lmtrediapply;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class DeleteLmtRediApplyRecordOp extends CMISOperation {

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			String modelId =  "LmtRediApply";
			if(context.containsKey("lx") && "INDIV".equalsIgnoreCase((String)context.getDataValue("lx"))){
				modelId =  "LmtAppIndivRedi";
			}else if(context.containsKey("type") && "coop".equalsIgnoreCase((String)context.getDataValue("type"))){
				modelId =  "LmtAppJointCoopRedi";
			}else if(context.containsKey("type") && "grp".equalsIgnoreCase((String)context.getDataValue("type"))){
				modelId =  "LmtAppGrpRedi";
			}
			connection = this.getConnection(context);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(modelId, context, connection);
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, serno_value, connection);
			if(count!=1){
				context.addDataField("flag","field");
				context.addDataField("msg","删除授信复议申请信息失败，错误描述：未找到对应的申请记录！");
				return "0";
			}
			
			/**集团授信复议删除时将集团下的成员申请状态改回否决 add by tangzf 2014.04.10**/
			if("LmtAppGrpRedi".equals(modelId)){
				SqlClient.executeUpd("updateLmtGrpApproveStatus", serno_value, "998", null, connection);
			}
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "success");
		}catch(Exception e){
			context.addDataField("flag","field");
			context.addDataField("msg", e.getMessage());
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
