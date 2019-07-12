package com.yucheng.cmis.platform.riskmanage.interfaces.imple;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

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
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
/**
 * 
*@author lisj
*@time 2015-6-1 
*@description TODO 需求编号：【XD150123005】小微自助循环贷款改造
*@version v1.0
*
 */
public class RiskManageMicroBizFreezeCheckImple implements RiskManageInterface{

	public Map<String, String> getResultMap(String tableName, String serno,Context context,Connection connection) throws Exception {
		
		//分开事务，自己创建连接
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Map<String, String> returnMap = new HashMap<String, String>();
		String returnFlag = "";
		String returnInfo = "";
		try {
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection lmtApp=dao.queryDetail("LmtApply",serno, connection);
			String applyType=TagUtil.replaceNull4String(lmtApp.getDataValue("app_type"));
			if("04".equals(applyType)){
				returnFlag = "通过";
				returnInfo = "该申请不属于额度冻结申请,无需检查！";
			}else{
				IndexedCollection iColl = dao.queryList("LmtAppDetails", " where serno ='"+serno+"' and limit_name ='100088'", connection);
				if(iColl!=null && iColl.size() >0){
					//KeyedCollection temp = (KeyedCollection) iColl.get(0);
					//BigDecimal froze_amt = BigDecimalUtil.replaceNull((String) temp.getDataValue("froze_amt"));//冻结金额
					//if(froze_amt.compareTo(new BigDecimal(0)) > 0){
						IndexedCollection iCollResult = SqlClient.queryList4IColl("checkApprovingLoanTranByLmtSerno",serno,connection);
						if (iCollResult != null&&iCollResult.size()>0) {
							returnFlag = "不通过";
							returnInfo = "该额度存在关联的在途业务申请信息,不允许做冻结操作！";
						}else{
							returnFlag = "通过";
							returnInfo = "该额度不存在关联的在途业务申请信息,检查通过！";
						}
					//}else{
					//	returnFlag = "不通过";
					//	returnInfo = "该冻结申请未录入【冻结金额】,请检查！";
					//}
				}else{
					returnFlag = "通过";
					returnInfo = "该额度不属于小微自助循环贷款额度,无需检查！";
				}
			}
			
			returnMap.put("OUT_是否通过", returnFlag);
			returnMap.put("OUT_提示信息", returnInfo);
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(dataSource, connection);
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
