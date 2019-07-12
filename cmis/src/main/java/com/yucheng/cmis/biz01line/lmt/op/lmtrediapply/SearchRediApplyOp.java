package com.yucheng.cmis.biz01line.lmt.op.lmtrediapply;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.TranslateDic;

public class SearchRediApplyOp  extends CMISOperation {
	
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
			
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0){
				context.addDataField("flag", "error");
				context.addDataField("msg", "查询复议信息错误，原因：传入业务编号为空！");
				return "0";
			}
		
			String modelId = "LmtRediApply";
			String modelId_app = "LmtApply";   //原有申请表模型
			if(context.containsKey("type") && "indiv".equalsIgnoreCase((String)context.getDataValue("type"))){
				modelId = "LmtAppIndivRedi";
				modelId_app = "LmtAppIndiv";
			}else if(context.containsKey("type") && "grp".equalsIgnoreCase((String)context.getDataValue("type"))){
				modelId = "LmtAppGrpRedi";
				modelId_app = "LmtAppGrp";
			}else if(context.containsKey("type") && "coop".equalsIgnoreCase((String)context.getDataValue("type"))){
				modelId = "LmtAppJointCoopRedi";
				modelId_app = "LmtAppJointCoop";
			}
			//复议时先判断是否对原申请有记录集权限
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(modelId_app, context, connection);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);  //查询复议表中是否有记录
			if(null!=kColl && null!=kColl.getDataValue("serno") && !"".equals(kColl.getDataValue("serno"))){
				if(!"000".equals(kColl.getDataValue("approve_status"))){
					SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id" });
					SInfoUtils.addUSerName(kColl, new String[] { "manager_id" ,"input_id" });
					
					KeyedCollection dictColl = (KeyedCollection)context.getDataElement(CMISConstance.ATTR_DICTDATANAME);
					TranslateDic trans = new TranslateDic();
					String cnName = trans.getCnnameByOpttypeAndEnname(dictColl, "WF_APP_STATUS",  kColl.getDataValue("approve_status").toString());
					String inputName = (String)kColl.getDataValue("input_id_displayname");
					String inputBrName = (String)kColl.getDataValue("input_br_id_displayname");
					if(context.containsKey("type") && !"coop".equalsIgnoreCase((String)context.getDataValue("type"))){
						String type = "05".equals(kColl.getDataValue("app_type"))?"复议":"变更复议";
						context.addDataField("msg", "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下已存在状态为["+cnName+"]的"+type+"申请，不能再次发起！");
					}else{
						context.addDataField("msg", "该客户在登记机构["+inputBrName+"]，登记人["+inputName+"]名下已存在状态为["+cnName+"]的复议申请，不能再次发起！");
					}				
					context.addDataField("flag", "error");
					
					return "0";
				}
			}
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "success");
		}catch(Exception e){
			context.addDataField("flag", "error");
			context.addDataField("msg", "查询失败，原因："+e.getMessage());			
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
