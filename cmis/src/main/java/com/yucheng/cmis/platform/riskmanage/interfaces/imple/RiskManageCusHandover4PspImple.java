package com.yucheng.cmis.platform.riskmanage.interfaces.imple;

import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;
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
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class RiskManageCusHandover4PspImple implements RiskManageInterface{

	private final String modelId = "PspCheckTask";//贷后任务表
	
	public Map<String, String> getResultMap(String tableName, String serno,Context context,Connection connection) throws Exception {
		
		//分开事务，自己创建连接
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection conn = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		String returnFlag = "";
		String returnInfo = "";
		String cus_name = "";
		String flag = "succ";
		String err_cus = "";
		try {
			conn = this.getConnection(context, dataSource);
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			/* modified by yangzy 2015/04/08 风险拦截提示信息过长改造 start */
		    String condition = " where serno = '"+serno+"'                                     "
		    				  +"   and exists (select 1                                        "
		    				  +"          from psp_check_task                                  "
		    				  +"         where cus_handover_lst.cus_id = psp_check_task.cus_id "
		    				  +"           and psp_check_task.approve_status in                "
		    				  +"               ('000', '111', '991', '992', '993'))            ";
			
			IndexedCollection iColl = dao.queryList("CusHandoverLst", condition, connection);
			returnFlag = "通过";
			returnInfo = "移交客户未存在未完成的贷后任务。";
			
			if(iColl==null || iColl.size()<1){
				returnMap.put("OUT_是否通过", returnFlag);
				returnMap.put("OUT_提示信息", returnInfo);
				return returnMap;
			}
			int count = 10;
			if(iColl.size() <= count){
				count = iColl.size();
			}
	    	for(int i=0;i<count;i++){
	    		KeyedCollection kColl = (KeyedCollection)iColl.get(i);
	    		String cus_id = (String)kColl.getDataValue("cus_id");
	    		CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
				CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
				//KeyedCollection result = (KeyedCollection) SqlClient.queryFirst("checkPspCheckTaskByCusId", cus_id,null, connection);
				//BigDecimal counts = (BigDecimal) result.getDataValue("counts");
				//if(counts.compareTo(new BigDecimal("0"))>0){
					CusBase cusBase = csi.getCusBaseByCusId(cus_id,context,connection);
					cus_name = TagUtil.replaceNull4String(cusBase.getCusName());//客户名称
					flag = "err";
					err_cus+=cus_name+"，";
				//}
	    	}
	    	/* modified by yangzy 2015/04/08 风险拦截提示信息过长改造 end */
	    	if("err".equals(flag)){
	    		err_cus = err_cus.substring(0, err_cus.length()-1);
	    		returnFlag = "不通过";
				returnInfo = "待移交客户【"+err_cus+"】存在未完成的贷后任务，请按要求完成贷后任务再移交！";
				returnMap.put("OUT_是否通过", returnFlag);
				returnMap.put("OUT_提示信息", returnInfo);
				return returnMap;
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
