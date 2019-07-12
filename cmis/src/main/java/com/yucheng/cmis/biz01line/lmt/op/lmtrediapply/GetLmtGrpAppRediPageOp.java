package com.yucheng.cmis.biz01line.lmt.op.lmtrediapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class GetLmtGrpAppRediPageOp  extends CMISOperation {
	
	private final String modelId = "LmtAppGrpRedi";
	
	private final String serno_name = "serno";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			//RecordRestrict recordRestrict = this.getRecordRestrict(context);
			//recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			String serno_value = null;
			String isShow = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			try {
				isShow = (String)context.getDataValue("isShow");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);  //查询复议表是否存在
			if("N".equals(isShow)){
				if(null!=kColl && null!=kColl.getDataValue("serno") && !"".equals(kColl.getDataValue("serno")) && "000".equals(kColl.getDataValue("approve_status"))){
					EMPLog.log("MESSAGE", EMPLog.INFO, 0, "-------复议申请表中已经存在待发起申请，直接转发-------", null);
				}else{
					kColl = dao.queryDetail("LmtAppGrp", serno_value, connection);  //先查询出授信申请信息
						
					//将授信申请直接复制为复议申请
					kColl.setName(modelId);
					if("01".equals(kColl.getDataValue("app_type"))){  //新增授信申请
						kColl.setDataValue("app_type", "05");  //设置为复议
					}else if("02".equals(kColl.getDataValue("app_type"))){  //变更授信申请
						kColl.setDataValue("app_type", "06");  //设置为变更复议
					}
					kColl.setDataValue("approve_status", "000");
					dao.insert(kColl, connection);
					
					/**集团授信发起复议时将集团下的成员申请也置为待发起状态 add by tangzf 2014.04.10**/
					SqlClient.executeUpd("updateLmtGrpApproveStatus", serno_value, "000", null, connection);
				}
			}
			
			
			String[] args=new String[] { "grp_no" };
			String[] modelIds=new String[]{"CusGrpInfo"};
			String[] modelForeign=new String[]{"grp_no"};
			String[] fieldName=new String[]{"grp_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id" });

			
			this.putDataElement2Context(kColl, context);
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
