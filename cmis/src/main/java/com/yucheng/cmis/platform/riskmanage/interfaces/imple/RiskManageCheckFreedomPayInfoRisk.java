package com.yucheng.cmis.platform.riskmanage.interfaces.imple;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

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
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
/**
 * 
*@author lisj
*@time 2015-9-15 
*@description TODO 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求
*@version v1.0
*
 */
public class RiskManageCheckFreedomPayInfoRisk implements RiskManageInterface {
	//private final String modelId = "PvpBizModifyRel";
	private static final Logger logger = Logger.getLogger(RiskManageIqpComFinImple.class);
	
	public Map<String, String> getResultMap(String tableName, String serno, Context context, Connection connection) throws Exception {
		//分开事务，自己创建连接
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection conn = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		String returnFlag = "";
		String returnInfo = "";
		try {
			 conn = this.getConnection(context, dataSource);
			 TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			 KeyedCollection kColl4CLCST = dao.queryDetail("CtrLoanContSubTmp", serno, conn);
			 String repay_type = (String)kColl4CLCST.getDataValue("repay_type");
			 
			 if(!"".equals(repay_type) && "A001".equals(repay_type)){
				IndexedCollection iColl4IFPI = dao.queryList("IqpFreedomPayInfoTmp", "where modify_rel_serno='"+serno+"'",conn);
				if(iColl4IFPI!=null && iColl4IFPI.size()>0){
					KeyedCollection  riskOne = (KeyedCollection)SqlClient.queryFirst("compareContAmtWithFPAmt", serno, null, conn);
					BigDecimal c1 = BigDecimalUtil.replaceNull(riskOne.getDataValue("cc"));
					if(c1.compareTo(new BigDecimal(0))>0){
						 returnFlag = "不通过";
						 returnInfo = "该修改信息的合同金额不等于自由还款金额！";
					}else{
						KeyedCollection  riskTwo = (KeyedCollection)SqlClient.queryFirst("compareContDateWithFPDate", serno, null, conn);
						BigDecimal c2 = BigDecimalUtil.replaceNull(riskTwo.getDataValue("cc"));
						if(c2.compareTo(new BigDecimal(0))>0){
							returnFlag = "不通过";
							returnInfo = "该修改信息的自由还款末期日期不等于合同到期日！";
						}else{
							KeyedCollection  riskThree = (KeyedCollection)SqlClient.queryFirst("comparePayDateWithFPPayDate", serno, null, conn);
							BigDecimal c3 = new BigDecimal(1);
							if(riskThree!=null){
								 BigDecimalUtil.replaceNull(riskThree.getDataValue("cc"));
							}
							if(c3.compareTo(new BigDecimal(0))<=0){
								returnFlag = "不通过";
								returnInfo = "自由还款计划相同日期不能有两条！";
							}
						}	
					}
				}else{
					returnFlag = "不通过";
					returnInfo = "该修改信息未录入还款计划登记信息！";
				}
				
			 }else{
				 returnFlag = "通过";
				 returnInfo = "该修改信息的支付方式不为【自由还款法】，无需检查！";
			 }
			 
			 if("".equals(returnFlag)){
					 returnFlag = "通过";
					 returnInfo = "该打回修改业务申请信息检查通过！";	 
			 }
			 	returnMap.put("OUT_是否通过", returnFlag);
				returnMap.put("OUT_提示信息", returnInfo);
				
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "打回修改业务提交流程检查失败！"+e.getMessage(), null);
			logger.error("打回修改业务提交流程检查失败检查失败！"+e.getStackTrace());
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
