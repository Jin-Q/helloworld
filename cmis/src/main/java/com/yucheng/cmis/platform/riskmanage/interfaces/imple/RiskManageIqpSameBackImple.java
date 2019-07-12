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
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class RiskManageIqpSameBackImple implements RiskManageInterface{

	private final String modelId = "IqpLoanApp";//业务申请表
	private final String modelIdLmtAcc = "LmtIntbankAcc";
	private final String modelIdRBusLmt = "RBusLmtcreditInfo";
	
	/*同业授信额度及限额检查
	 * 同业授信金额 <= 授信总额。
	 */
	public Map<String, String> getResultMap(String tableName, String serno,Context context,Connection connection) throws Exception {
		
		//分开事务，自己创建连接
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection conn = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		String returnFlag = "";
		String returnInfo = "";
		String limit_credit_no = "";
		try {
			conn = this.getConnection(context, dataSource);
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail(tableName, serno, connection);
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
	    	IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
	    	if(tableName =="IqpLoanApp"){//普通贷款
	    		KeyedCollection kCollRBusLmt = dao.queryFirst(modelIdRBusLmt,null, serno, connection);
    			limit_credit_no = (String)kCollRBusLmt.getDataValue("agr_no");//第三方授信编号
	    	}else if(tableName =="IqpRpddscnt" || tableName =="IqpAssetstrsf"){//转贴现 //资产转让
	    		String limit_ind = (String)kColl.getDataValue("limit_ind");//授信额度使用标志
	    		if(limit_ind =="2" || limit_ind =="3" || limit_ind =="4"){
	    			KeyedCollection kCollRBusLmt = dao.queryFirst(modelIdRBusLmt,null, serno, connection);
	    			limit_credit_no = (String)kCollRBusLmt.getDataValue("agr_no");
	    		}else{
	    			returnFlag = "通过";
					returnInfo = "未使用授信，不适合此项检查";
					returnMap.put("OUT_是否通过", returnFlag);
					returnMap.put("OUT_提示信息", returnInfo);
					return returnMap;
	    		}
	    	}
	    	//第三方授信编号如果为空，则不进行检查 searchLmtAgrAmtByAgrNO
	    	if(!"".equals(limit_credit_no) && limit_credit_no != null){
	    		//查询同业授信台账
	    		IndexedCollection iCollLmtAcc = dao.queryList(modelIdLmtAcc, "where agr_no='"+limit_credit_no+"'", connection);
	    		if(iCollLmtAcc.size()>0){
	    			KeyedCollection kCollRes = serviceIqp.getLmtAmtByArgNo(limit_credit_no, "02", connection, context);
					BigDecimal lmt_amt = BigDecimalUtil.replaceNull(kCollRes.getDataValue("lmt_amt"));
					//查询授信总金额
					LmtServiceInterface serviceLmt = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
					String lmtAll = (String)serviceLmt.searchLmtAgrAmtByAgrNO(limit_credit_no, "02", connection);
					BigDecimal lmt_total = BigDecimalUtil.replaceNull(lmtAll);
					
					if (lmt_total.compareTo(lmt_amt) >= 0) {
						returnFlag = "通过";
						returnInfo = "同业授信额度可以覆盖敞口金额";
					} else {
						returnFlag = "不通过";
						returnInfo = "同业授信额度不能覆盖敞口金额";
					}
	    		}else{
	    			returnFlag = "通过";
					returnInfo = "非同业类客户授信，检查通过";
	    		}
	    	}else{
				returnFlag = "通过";
				returnInfo = "非同业类客户授信，检查通过";
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
