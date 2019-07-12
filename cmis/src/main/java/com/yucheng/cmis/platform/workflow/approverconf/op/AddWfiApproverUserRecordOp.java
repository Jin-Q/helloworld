package com.yucheng.cmis.platform.workflow.approverconf.op;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class AddWfiApproverUserRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "WfiApproverUser";
	
	/**
	 * bussiness logic operation
	 */
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
			
			/**根据配置类型（筛选类型 1：例外审批人  2：固定参与人  3：机构业务审批人员），操作人员计数表：
			 * 如果为[例外审批]修改人员计数表对应记录状态，新增固定参与人时将用户置为无效；
			 * 如果为[固定参与人]新增用户时判断用户在计数表中是否存在，如果不存在实时新增操作人员计数数据；如果存在则更新状态及计数数量
			 * 如果为[机构业务审批]新增用户时判断用户在计数表中是否存在，如果不存在实时新增操作人员计数数据；如果存在则更新状态及计数数量
			 * 需求编号：XD140812045  2014-09-16  author：唐顺岩  **/
			String confid = (String)kColl.getDataValue("confid");  //得到配置ID
			KeyedCollection conf_kColl = (KeyedCollection)dao.queryFirst("WfiApproverConf", null, " WHERE CONFID='"+confid+"'", connection);
			
			String conf_type = (String)conf_kColl.getDataValue("conf_type");  //获取配置类型，根据配置类型操作人员计数表
			String appl_type = (String)conf_kColl.getDataValue("appl_type");  //获取流程类型
			String nodeid = (String)conf_kColl.getDataValue("nodeid");  //获取流程节点
			
			KeyedCollection kCollParam = new KeyedCollection();
			kCollParam.put("appl_type", appl_type); //获得申请类型
			kCollParam.put("nodeid", nodeid);  //获得配置的节点编号
			kCollParam.put("actorno", kColl.getDataValue("actorno"));  //获得配置的用户
			if("1".equals(conf_type)){   //如果为例外审批人
				int update_count = SqlClient.executeUpd("updateApproveStatus", kCollParam, "0", null, connection);
				if(update_count < 1){  //未更新到数据，说明该人员不属于随机岗位下，或者随机岗位审批人员未初始化
					EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "用户["+kColl.getDataValue("actorname")+"]不属于随机岗位，或随机岗位审批人员未初始化，请确认！", null);
					context.put("flag", "用户["+kColl.getDataValue("actorname")+"]不属于随机岗位，或随机岗位审批人员未初始化，请确认！");
					return "0";
				}
			}else{  //固定参与人\机构业务审批人员
				//查询同流程同节点下生效用户的最小审批记录数    2014-09-16   唐顺岩
				String query_sql = "queryMinApproveQnt";   //查询默认为查询非机构下审批人员最小审批笔数。
				String update_sql = "updateWfiApproveCount";  //修改人员计数表状态及计数的命名SQL
				String select_exists_condition = " WHERE APPL_TYPE='"+ appl_type +"' AND NODEID='"+ nodeid +"' AND ACTORNO='"+kColl.getDataValue("actorno")+"'";
				if("3".equals(conf_type)){  //如果为机构业务审批人员，在参数中需要加入机构
					kCollParam.put("orgid", conf_kColl.getDataValue("orgid"));  //获得配置的机构编号
					query_sql = "queryMinApproveQntByOrgId";
					update_sql = "updateWfiApproveCountByOrgId";
					select_exists_condition += " AND ORGID='"+conf_kColl.getDataValue("orgid")+"'";
				}
				
				String count = SqlClient.queryFirst(query_sql, kCollParam, null, connection)+"";   //直接将返回的BigDecimal转为字符串
				int approveQnt = Integer.parseInt(count);   //转换为int
				
				//查询在审批计数表中是否已经存在
				KeyedCollection modelKcoll = new KeyedCollection();
				modelKcoll.setName("WfiApproveCount");
				
				modelKcoll.addDataField("appl_type",appl_type);
				modelKcoll.addDataField("nodeid",nodeid);
				modelKcoll.addDataField("actorno",kColl.getDataValue("actorno"));
				modelKcoll.addDataField("orgid", conf_kColl.getDataValue("orgid"));
				
				IndexedCollection exists_icoll = dao.queryList("WfiApproveCount", select_exists_condition, connection);
				if(null!=exists_icoll && exists_icoll.size()<1){  //如果存在则插入数据
					modelKcoll.addDataField("approve_qnt",approveQnt);
					modelKcoll.addDataField("status","1");
					
					modelKcoll.addDataField("approve_date",context.getDataValue("OPENDAY"));  //该日期放加入审批记录的当天，以后不再更改
					dao.insert(modelKcoll, connection);
				}else{
					//更新数量与状态值对象
					KeyedCollection valueKcoll = new KeyedCollection();
					valueKcoll.addDataField("approve_qnt",approveQnt);
					valueKcoll.addDataField("status","1");
					
					int update_count = SqlClient.executeUpd(update_sql, modelKcoll, valueKcoll, null, connection);
					if(update_count < 1){  //未更新到数据，说明该人员不属于随机岗位下，或者随机岗位审批人员未初始化
						context.put("flag", "用户["+kColl.getDataValue("actorname")+"]不属于随机岗位，或随机岗位审批人员未初始化，请确认！");
						EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "用户["+kColl.getDataValue("actorname")+"]不属于随机岗位，或随机岗位审批人员未初始化，请确认！", null);
						return "0";
					}
				}
			}
			/**END**/
			
			//add a record
			dao.insert(kColl, connection);
			connection.commit();
			context.put("flag", 1);
		}catch (EMPException ee) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ee.printStackTrace();
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, ee.getMessage(), null);
			context.put("flag", null==ee.getCause()?ee.getMessage():ee.getCause().getMessage());
		} catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException eee) {
				eee.printStackTrace();
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
