package com.yucheng.cmis.platform.workflow.approverconf.op;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class DeleteWfiApproverUserRecordOp extends CMISOperation {

	private final String modelId = "WfiApproverUser";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();   //加泛型
			pkMap.put("confid", (String)context.getDataValue("confid"));
			pkMap.put("actorno", (String)context.getDataValue("actorno"));
			
			/**根据配置类型（筛选类型 1：例外审批人  2：固定参与人  3：机构业务审批人员），操作人员计数表：
			 * 如果为[例外审批]删除例外审批人员时修改人员计数表对应记录状态为”生效“，同时更新审批计数为同类型下最小值。
			 * 如果为[固定参与人]删除固定参与人员时将其对应的审批人员计数状态改为”无效“。
			 * 如果为[机构业务审批]删除机构业务审批人员时将其对应的审批人员计数状态改为”无效“。
			 * 需求编号：XD140812045  2014-09-16  author：唐顺岩  **/
			String confid = (String)context.getDataValue("confid");  //得到配置ID
			KeyedCollection conf_kColl = (KeyedCollection)dao.queryFirst("WfiApproverConf", null, " WHERE CONFID='"+confid+"'", connection);
			
			String conf_type = (String)conf_kColl.getDataValue("conf_type");  //获取配置类型，根据配置类型操作人员计数表
			String appl_type = (String)conf_kColl.getDataValue("appl_type");  //获取流程类型
			String nodeid = (String)conf_kColl.getDataValue("nodeid");  //获取流程节点
			
			KeyedCollection kCollParam = new KeyedCollection();
			kCollParam.put("appl_type", appl_type); //获得申请类型
			kCollParam.put("nodeid", nodeid);  //获得配置的节点编号
			kCollParam.put("actorno", (String)context.getDataValue("actorno"));  //获得配置的用户
			
			SInfoUtils.addUSerName(kCollParam, new String[] {"actorno"});
			if("1".equals(conf_type)){   //如果为例外审批人
				//查询同申请类型、同节点下生效的审批人员最小审批数量
				String count = SqlClient.queryFirst("queryMinApproveQnt", kCollParam, null, connection)+"";   //直接将返回的BigDecimal转为字符串
				int approveQnt = Integer.parseInt(count);   //转换为int
				
				//更新数量与状态值对象
				KeyedCollection valueKcoll = new KeyedCollection();
				valueKcoll.addDataField("approve_qnt",approveQnt);
				valueKcoll.addDataField("status","1");
				
				int update_count = SqlClient.executeUpd("updateWfiApproveCount", kCollParam, valueKcoll, null, connection);
				if(update_count < 1){  //未更新到数据，说明该人员不属于随机岗位下，或者随机岗位审批人员未初始化
					context.put("flag", "用户["+kCollParam.getDataValue("actorno_displayname")+"]不属于随机岗位，或随机岗位审批人员未初始化，请确认！");
					EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "用户["+kCollParam.getDataValue("actorno_displayname")+"]不属于随机岗位，或随机岗位审批人员未初始化，请确认！", null);
					return "0";
				}
			}else if("2".equals(conf_type)){  //固定参与人 直接更新状态为”失效“
				int update_count = SqlClient.executeUpd("updateApproveStatus", kCollParam, "0", null, connection);
				if(update_count < 1){  //未更新到数据，说明该人员不属于随机岗位下，或者随机岗位审批人员未初始化
					context.put("flag", "用户["+kCollParam.getDataValue("actorno_displayname")+"]不属于固定参与人员，或随机岗位审批人员未初始化，请确认！");
					EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "用户["+kCollParam.getDataValue("actorno_displayname")+"]不属于固定参与人员，或随机岗位审批人员未初始化，请确认！", null);
					return "0";
				}
			}else{  //机构业务审批人员 直接更新状态为”失效“
				kCollParam.put("orgid", conf_kColl.getDataValue("orgid"));  
				int update_count = SqlClient.executeUpd("updateApproveStatusByOrgId", kCollParam, "0", null, connection);
				if(update_count < 1){  //未更新到数据，说明该人员不属于随机岗位下，或者随机岗位审批人员未初始化
					context.put("flag", "用户["+kCollParam.getDataValue("actorno_displayname")+"]不属于机构["+conf_kColl.getDataValue("orgid")+"]业务审批人员，或随机岗位审批人员未初始化，请确认！");
					EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "用户["+kCollParam.getDataValue("actorno_displayname")+"]不属于机构["+conf_kColl.getDataValue("orgid")+"]业务审批人员，或随机岗位审批人员未初始化，请确认！", null);
					return "0";
				}
			}
			int count=dao.deleteByPks(modelId, pkMap, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			
			context.put("flag", "success");
		}catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, e.getMessage(), null);
			context.put("flag", null==e.getCause()?e.getMessage():e.getCause().getMessage());
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
