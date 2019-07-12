package com.yucheng.cmis.platform.riskmanage.interfaces.imple;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.ConnectionManager;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.session.SessionException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;

public class RiskManagePspCheckTestImple implements RiskManageInterface{

	private final String modelIdCusManager = "CusManager";//主管客户经理表
	private final String modelIdPspCheckTask = "PspCheckTask";//贷后检查任务
	
	/*检查该主管客户经理下是否存在未完成的贷后检查任务
	 */
	public Map<String, String> getResultMap(String tableName, String serno,Context context,Connection connection) throws Exception {
		
		//分开事务，自己创建连接
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection conn = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		String returnFlag = "";
		String returnInfo = "";
		try {
			connection = this.getConnection(context, dataSource);
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			//查询本笔业务的主管客户经理
			//modified by yangzy 20150923 检查该主管客户经理下是否存在未完成的贷后检查任务改造，检查范围包括协办客户经理 start
			if("IqpLoanApp".equals(tableName)){
				String conditionCusBase = " where cus_id in (select cus_id from iqp_loan_app where serno = '"+serno+"') ";
				KeyedCollection kCollCusBase = dao.queryFirst("CusBase",null, conditionCusBase, connection);
				if(kCollCusBase!=null&&kCollCusBase.getDataValue("cust_mgr")!=null){
					String cust_mgr = (String)kCollCusBase.getDataValue("cust_mgr");
					if(cust_mgr != null && !"".equals(cust_mgr)){
						//查询该客户经理下是否有未完成的贷后检查任务
						String openDay = (String)context.getDataValue("OPENDAY");
						String conditionStr = "where task_huser='"+cust_mgr+"' and approve_status not in('997','998') and task_request_time<'"+openDay+"'";
						IndexedCollection iColl = dao.queryList(modelIdPspCheckTask, conditionStr, connection);
						if(iColl.size()>0){
							returnFlag = "不通过";
							returnInfo = "客户所属主管客户经理下存在["+iColl.size()+"]笔未完成贷后检查任务";
						}
					}
				}
			}
			if("".equals(returnFlag)){
				String condition = " where serno='"+serno+"' ";
				IndexedCollection iCollCusManager = dao.queryList(modelIdCusManager, condition, connection);
				if(iCollCusManager!=null&&iCollCusManager.size()>0){
					for (Iterator<KeyedCollection> iterator = iCollCusManager.iterator(); iterator.hasNext();) {
						KeyedCollection kCollCusManager = (KeyedCollection) iterator.next();
						String manager_id = (String)kCollCusManager.getDataValue("manager_id");
						if(manager_id != null && !"".equals(manager_id)){
							//查询该客户经理下是否有未完成的贷后检查任务
							String openDay = (String)context.getDataValue("OPENDAY");
							String conditionStr = "where task_huser='"+manager_id+"' and approve_status not in('997','998') and task_request_time<'"+openDay+"'";
							IndexedCollection iColl = dao.queryList(modelIdPspCheckTask, conditionStr, connection);
							if(iColl.size()>0){
								returnFlag = "不通过";
								returnInfo += "客户经理工号["+manager_id+"]下存在["+iColl.size()+"]笔未完成贷后任务！";
							}
						}
					}
					if("".equals(returnFlag)){
						returnFlag = "通过";
						returnInfo = "客户经理下贷后检查任务检查已全部完成";
					}
				}else{
					returnFlag = "不通过";
					returnInfo = "未录入主管客户经理";
				}
			}
			
//			String manager_id = (String)kCollCusManager.getDataValue("manager_id");
//			if(manager_id != null && !"".equals(manager_id)){
//				//查询该客户经理下是否有未完成的贷后检查任务
//				String openDay = (String)context.getDataValue("OPENDAY");
//				String conditionStr = "where task_huser='"+manager_id+"' and approve_status not in('997','998') and task_request_time<'"+openDay+"'";
//				IndexedCollection iColl = dao.queryList(modelIdPspCheckTask, conditionStr, connection);
//				if(iColl.size()==0){
//					returnFlag = "通过";
//					returnInfo = "该客户经理下贷后检查任务检查已全部完成";
//				}else{
//					returnFlag = "不通过";
//					returnInfo = "该主管客户经理下存在["+iColl.size()+"]笔未完成贷后检查任务";
//				}
//			}else{
//				returnFlag = "不通过";
//				returnInfo = "未录入主管客户经理";
//			}
			//modified by yangzy 20150923 检查该主管客户经理下是否存在未完成的贷后检查任务改造，检查范围包括协办客户经理 end
			returnMap.put("OUT_是否通过", returnFlag);
			returnMap.put("OUT_提示信息", returnInfo);
		}catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (conn != null)
				this.releaseConnection(dataSource, conn);
		}
		return returnMap;
	}
	
	/**
	 * 获取数据库连接
	 * 
	 * @param context
	 * @param dataSource
	 * @return
	 * @throws EMPJDBCException
	 * @throws SessionException 
	 */
	private Connection getConnection(Context context, DataSource dataSource) throws EMPJDBCException, SessionException {
		if (dataSource == null)
			throw new SessionException("登陆超时，请重新登陆或联系管理员 !"
					+ this.toString());
		Connection connection = null;
		connection = ConnectionManager.getConnection(dataSource);
		
		EMPLog.log( EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0, "Apply new connection from data source: "+dataSource+" success!");
		return connection;
	}
	
	/**
	 * 释放数据库连接
	 * 
	 * @param dataSource
	 * @param connection
	 * @throws EMPJDBCException
	 */
	private void releaseConnection(DataSource dataSource, Connection connection)
			throws EMPJDBCException {
		ConnectionManager.releaseConnection(dataSource, connection);
		EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0, "Do release the connection from data source: " + dataSource + " success!");
	}

}
