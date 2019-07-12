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
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class RiskManageCsgnLoanInfoImple implements RiskManageInterface{

	private final String modelIdCsgnLoanInfo = "IqpCsgnLoanInfo";//
	
	/*手续费支付比例 校验
	 * 。
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
			KeyedCollection kCollCsgnLoanInfo = (KeyedCollection)dao.queryDetail(modelIdCsgnLoanInfo, serno, conn);
			if(kCollCsgnLoanInfo != null){
				String sernoCsgnLoanInfo = (String)kCollCsgnLoanInfo.getDataValue("serno");
				if(sernoCsgnLoanInfo != null && !"".equals(sernoCsgnLoanInfo)){
					BigDecimal csgn_chrg_pay_rate = BigDecimalUtil.replaceNull(kCollCsgnLoanInfo.getDataValue("csgn_chrg_pay_rate"));
					BigDecimal debit_chrg_pay_rate = BigDecimalUtil.replaceNull(kCollCsgnLoanInfo.getDataValue("debit_chrg_pay_rate"));
					//委托人手续费支付比例 校验
					if(csgn_chrg_pay_rate.compareTo(new BigDecimal(0))>0){
						String condition = "where serno='"+serno+"' and fee_code='09'";
						IndexedCollection iCollTerms = (IndexedCollection)dao.queryList("IqpAppendTerms", condition, conn);
						if(iCollTerms.size()>0){
							BigDecimal fee_rate = new BigDecimal(0);
							for(int i=0;i<iCollTerms.size();i++){
								KeyedCollection kCollTerms = (KeyedCollection)iCollTerms.get(i);
								fee_rate = fee_rate.add(BigDecimalUtil.replaceNull(kCollTerms.getDataValue("fee_rate")));
							}
							if(csgn_chrg_pay_rate.compareTo(fee_rate)!=0){
								returnMap.put("OUT_是否通过", "不通过");
								returnMap.put("OUT_提示信息", "委托人手续费支付比例与附加条款中借款人委托贷款费用比率不一致!");
								return returnMap;
							}else{
								returnFlag = "通过";
								returnInfo = "检查通过";
							}
						}else{
							returnMap.put("OUT_是否通过", "不通过");
							returnMap.put("OUT_提示信息", "附加条款中未录入委托人委托贷款费用");
							return returnMap;
						}
					}
					//借款人手续费支付比例  校验
					if(debit_chrg_pay_rate.compareTo(new BigDecimal(0))>0){
						String condition = "where serno='"+serno+"' and fee_code='01'";
						IndexedCollection iCollTerms = (IndexedCollection)dao.queryList("IqpAppendTerms", condition, conn);
						if(iCollTerms.size()>0){
							BigDecimal fee_rate = new BigDecimal(0);
							for(int i=0;i<iCollTerms.size();i++){
								KeyedCollection kCollTerms = (KeyedCollection)iCollTerms.get(i);
								fee_rate = fee_rate.add(BigDecimalUtil.replaceNull(kCollTerms.getDataValue("fee_rate")));
							}
							if(csgn_chrg_pay_rate.compareTo(fee_rate)!=0){
								returnMap.put("OUT_是否通过", "不通过");
								returnMap.put("OUT_提示信息", "借款人手续费支付比例与附加条款中借款人委托贷款费用比率不一致!");
								return returnMap;
							}else{
								returnFlag = "通过";
								returnInfo = "检查通过";
							}
						}else{
							returnMap.put("OUT_是否通过", "不通过");
							returnMap.put("OUT_提示信息", "附加条款中未录入借款人委托贷款费用");
							return returnMap;
						}
					}
				}
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
