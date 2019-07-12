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
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
/**
 * 
*@author wangj
*@time 2015-5-28
*@description 需求编号：【XD141222087】法人账户透支需求变更
*@version v1.0
*
 */
public class RiskManageFreezeLmtAgrDetailsImple implements RiskManageInterface{

    private final String sqlId="queryApplingIqpLoanAppBySerno";//查询是否存在审核中的法人账户透支的申请
    private final String  appmodelId="LmtAppDetails";
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
				IndexedCollection iCollResult = SqlClient.queryList4IColl(sqlId,serno,connection);
				if (iCollResult != null&&iCollResult.size()>0 ) {
					returnFlag = "不通过";
					returnInfo = "存在审核中的法人账户透支的申请,请检查！";
				}else{
					returnFlag = "通过";
					returnInfo = "不属于法人账户透支额度冻结申请,检查通过！";
					String condition=" where serno ='" + serno+"' and limit_name ='100051' ";
					IndexedCollection iColl = dao.queryList(appmodelId, condition, connection);
					if(iColl!=null&&iColl.size()>0){
						returnFlag = "通过";
						returnInfo = "不存在审核中的法人账户透支的申请,检查通过！";
						//KeyedCollection temp = (KeyedCollection) iColl.get(0);
						//BigDecimal froze_amt = BigDecimalUtil.replaceNull((String) temp.getDataValue("froze_amt"));//冻结金额
						//if(froze_amt.compareTo(new BigDecimal(0)) <= 0){
						//	returnFlag = "不通过";
						//	returnInfo = "该冻结申请未录入【冻结金额】,请检查！";
						//}
					}
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
