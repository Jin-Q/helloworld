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
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
/**
 * @author yangzy
 * @time 2014年11月26日
 * @description 需求编号：XD140812040, 银行承兑税票改造
 * @version v1.0
 */


public class RiskManageAccpCheckActpStateImple implements RiskManageInterface{
	/**
	 * 检查该客户在75天内是否提供足额的税票，没有则不能续办银承业务。
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
			returnFlag = "通过";
			returnInfo = "否";
			
		    String openDay = (String)context.getDataValue("OPENDAY");
		    String conditionStr="where serno= '"+serno+"'";
		    KeyedCollection kCollIqpLoanApp = dao.queryFirst("IqpLoanApp", null, conditionStr, connection);  
		    String prd_id = (String)kCollIqpLoanApp.getDataValue("prd_id");
		    if(!"200024".equals(prd_id)){
		      returnMap.put("OUT_是否通过", returnFlag);
		      returnMap.put("OUT_提示信息", returnInfo);
		      return returnMap;
		    }
		    String daorg_cusid = (String)kCollIqpLoanApp.getDataValue("cus_id");  
		    String condition = "where to_date('"+openDay+"', 'yyyy-mm-dd') - to_date(acc_accp.isse_date, 'yyyy-mm-dd') > 75 and exists (select 1 from ctr_loan_cont_sub t1  where acc_accp.cont_no = t1.cont_no and (t1.actp_status <>'2' or t1.actp_status is null)) and acc_accp.daorg_cusid = '"+daorg_cusid+"' and acc_accp.accp_status in ('1','6','7') ";

		    IndexedCollection icoll = dao.queryList("AccAccp", condition, connection);
		    
		    if(icoll!=null&&icoll.size()>0){
		    	returnMap.put("OUT_是否通过", "不通过");
		    	returnMap.put("OUT_提示信息", "该客户75天内未提供足额的税票，不能续办开具银承业务。");
		    }else{
		    	returnMap.put("OUT_是否通过", "通过");
		    	returnMap.put("OUT_提示信息", "该客户可以续办银承业务。");
		    }
	    	
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
