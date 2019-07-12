package com.yucheng.cmis.platform.workflow.op.wfilvcreditright;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
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
public class AddWfiLvCreditRightRecordOp extends CMISOperation {
	
	private final String modelId = "WfiLvCreditRight";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String right_type = (String)kColl.getDataValue("right_type");//权限类型
			//新增权限配置前，校验配置表是否存在该项配置（根据客户条线、担保方式、权限类型）
			String belg_line = (String) kColl.getDataValue("belg_line");
			String assure_main = (String) kColl.getDataValue("assure_main");
			IndexedCollection existSetting = null;
			if(right_type.equals("03")){
				String cb_org_id = (String)kColl.getDataValue("org_id");
				existSetting = dao.queryList(modelId, "where belg_line='"+belg_line+"' and assure_main='"+assure_main+"' " 
											+"and right_type='"+right_type+"' and org_id='"+cb_org_id+"'", connection);
			}else{
				String org_lvl = (String) kColl.getDataValue("org_lvl");
				existSetting = dao.queryList(modelId, "where belg_line='"+belg_line+"' and assure_main='"+assure_main+"' " 
						+"and right_type='"+right_type+"' and org_lvl='"+org_lvl+"'", connection);
			}
			if(existSetting ==null || existSetting.size() <=0){
				dao.insert(kColl, connection);
				context.put("flag", PUBConstant.SUCCESS);
			}else{
				context.put("flag", "existSetting");
			}
			if("success".equals((String)context.getDataValue("flag"))){
				//社区支行审批权限新增
				if(right_type.equals("03")){
					String cb_org_id = (String)kColl.getDataValue("org_id");
					IndexedCollection temp = dao.queryList("WfiOrgLifeloanRel", "where org_id='"+cb_org_id+"'", connection);
					if(temp==null || temp.size()<=0){
						KeyedCollection lifeLoanKColl = new KeyedCollection("WfiOrgLifeloanRel");
						lifeLoanKColl.addDataField("org_id", cb_org_id);
						lifeLoanKColl.addDataField("is_life_loan", "2");
						dao.insert(lifeLoanKColl, connection);
					}
				}
				//写入操作记录表
				try {
					KeyedCollection  wfiLCRLog = new KeyedCollection();
					String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
					String op_time = TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss");
					wfiLCRLog.put("operation_id", (String)kColl.getDataValue("pk_id"));
					wfiLCRLog.put("operation_org_id", (String)context.getDataValue("organNo"));//当前机构码
					wfiLCRLog.put("operation_staff", currentUserId);
					wfiLCRLog.put("operation_date", op_time);
					wfiLCRLog.put("right_type", (String)kColl.getDataValue("right_type"));
					wfiLCRLog.put("op_type", "add");//操作类型
					wfiLCRLog.setName("WfiLcrLog");
					dao.insert(wfiLCRLog, connection);
				} catch (Exception e) {
					context.put("flag", "writelogEx");
				}
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
