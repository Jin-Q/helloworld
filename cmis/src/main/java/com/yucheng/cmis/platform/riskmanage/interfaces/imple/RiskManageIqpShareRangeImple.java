package com.yucheng.cmis.platform.riskmanage.interfaces.imple;

import java.util.HashMap;
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
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class RiskManageIqpShareRangeImple implements RiskManageInterface{

	private final String modelId = "LmtAgrJointCoop";//授信联保合作方协议表
	private final String modelIdAgr = "LmtIndusAgr";//行业授信协议表
	
	/*合作方/行业的授信额度在不在"所属机构范围"
	 */
	public Map<String, String> getResultMap(String tableName, String serno,Context context,Connection connection) throws Exception {
		
		//分开事务，自己创建连接
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection conn = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		String returnFlag = "";
		String returnInfo = "";
		
		try {
			conn = this.getConnection(context, dataSource);
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			int res = 0;
			//1.取授信额度使用标志 为4,5,6
			KeyedCollection kCollIqp = dao.queryDetail(tableName, serno, connection);
			String limit_ind = (String)kCollIqp.getDataValue("limit_ind");
			String manager_br_id = (String)kCollIqp.getDataValue("manager_br_id");
			String prd_id = (String)kCollIqp.getDataValue("prd_id");
			if("4".equals(limit_ind) || "5".equals(limit_ind)|| "6".equals(limit_ind)){
				String condition = "where share_range='2' and coop_type<>'010' and agr_no in(select a.agr_no from R_Bus_Lmtcredit_Info a where a.serno='"+serno+"')";
			    IndexedCollection iCollLmt = dao.queryList(modelId, condition, connection);
			    String conditonStr = "where shared_scope='2' and agr_no in(select a.agr_no from R_Bus_Lmtcredit_Info a where a.serno='"+serno+"')";
		    	IndexedCollection iCollLmtAgr = dao.queryList(modelIdAgr, conditonStr, connection);
			    if(iCollLmt.size()==0){
			    	 if(iCollLmtAgr.size()==0){
						 returnMap.put("OUT_是否通过", "通过");
						 returnMap.put("OUT_提示信息", "不适合此项检查");
						 return returnMap;
			    	 }else{
			    		 for(int i=0;i<iCollLmtAgr.size();i++){
						    KeyedCollection kColl = (KeyedCollection)iCollLmtAgr.get(i);
						    String shared_scope = (String)kColl.getDataValue("belg_org");
						    res += shared_scope.indexOf(manager_br_id);
						 }
						 if(res >= 0){
						    returnFlag = "通过";
							returnInfo = "管理机构在该合作方/行业授信所属机构范围";
						  }else{
						    returnFlag = "不通过";
							returnInfo = "管理机构不在该行业授信所属机构范围";
						  }
			    	 }
			    }else{
			    	for(int i=0;i<iCollLmt.size();i++){
				    	KeyedCollection kColl = (KeyedCollection)iCollLmt.get(i);
				    	String belg_org = (String)kColl.getDataValue("belg_org");
				    	res += belg_org.indexOf(manager_br_id);
				    }
				    if(res >= 0){
				    	for(int i=0;i<iCollLmtAgr.size();i++){
						    KeyedCollection kColl = (KeyedCollection)iCollLmtAgr.get(i);
						    String shared_scope = (String)kColl.getDataValue("belg_org");
						    res += shared_scope.indexOf(manager_br_id);
						 }
						 if(res >= 0){
						    returnFlag = "通过";
							returnInfo = "管理机构在该合作方/行业授信所属机构范围";
						  }else{
						    returnFlag = "不通过";
							returnInfo = "管理机构不在该合作方/行业授信所属机构范围";
						  }
				    }else{
				    	returnFlag = "不通过";
						returnInfo = "管理机构不在该合作方的授信所属机构范围";
				    }
			    }
			}else{
				returnFlag = "通过";
				returnInfo = "不适合此项检查";
			}
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
	private Connection getConnection(Context context, DataSource dataSource)
			throws EMPJDBCException, SessionException {
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
