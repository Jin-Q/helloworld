package com.yucheng.cmis.platform.riskmanage.interfaces.imple;

import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;
import java.sql.Connection;

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
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

public class RiskManageIqpComCoopImple implements RiskManageInterface{

	private final String modelId = "IqpLoanApp";//业务申请表
	private static final Logger logger = Logger.getLogger(RiskManageIqpComCoopImple.class);
	/*合作方授信额度及限额检查
	 * 1.检查当前业务风险敞口金额 <= 合作方单户限额；
     * 2.检查当前业务风险敞口金额+合作方下所关联的存量业务风险敞口金额 <= 合作方授信总额。
     * 
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
			
			KeyedCollection kColl = dao.queryDetail(modelId, serno, conn);
			String limit_ind = (String) kColl.getDataValue("limit_ind");
			String cus_id = (String) kColl.getDataValue("cus_id");
			
			if(limit_ind!=null&&!limit_ind.equals("")){
				if(limit_ind.equals("4")||limit_ind.equals("5")||limit_ind.equals("6")){
					String limit_credit_no = (String) kColl.getDataValue("limit_credit_no");
					
					//获取合作方下所关联的存量业务风险敞口金额
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
					KeyedCollection lmtKColl = service.getAgrUsedInfoByArgNo(limit_credit_no,"03",conn,context);
					BigDecimal coop_lmt_amt = new BigDecimal(lmtKColl.getDataValue("lmt_amt")+"");//授信占用
					
					//获取合作方下所关联的单户的存量业务风险敞口金额
					IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
					.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, conn);
					KeyedCollection kColl4Query = new KeyedCollection();
					kColl4Query.addDataField("CUS_ID", cus_id);
					kColl4Query.addDataField("LIMIT_CREDIT_NO", limit_credit_no);
					IndexedCollection iqpContIColl =  SqlClient.queryList4IColl("queryIqpContByCoopCusId", kColl4Query, conn);//存量业务（已包含了当前申请）
					BigDecimal coopcus_lmt_amt = new BigDecimal("0");//单户的存量业务风险敞口金额
					for(int i=0;i<iqpContIColl.size();i++){
						KeyedCollection iqpContKColl = (KeyedCollection) iqpContIColl.get(i);
						String cont_no = (String) iqpContKColl.getDataValue("cont_no");
						String iqpserno = (String) iqpContKColl.getDataValue("serno");
						if(cont_no==null){
							BigDecimal iqp_amt = iqpLoanAppComponent.getLmtAmtBySerno(iqpserno);//（不需判断是否使用额度，不需判断授信额度标识，即统计该流水的敞口金额汇总）
							coopcus_lmt_amt = coopcus_lmt_amt.add(iqp_amt);
						}else{
							BigDecimal iqp_amt = iqpLoanAppComponent.getLmtAmtByContNo(cont_no);//（不需判断是否使用额度，不需判断授信额度标识，即统计该合同的敞口金额汇总）
							coopcus_lmt_amt = coopcus_lmt_amt.add(iqp_amt);
						}
					}
					
					
					ShuffleServiceInterface shuffleService = null;
					shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
					Map<String, String> inMap = new HashMap<String, String>();
					inMap.put("IN_SERNO", serno);
					inMap.put("IN_COOP_LMT_AMT", coop_lmt_amt+"");
					inMap.put("IN_COOPCUS_LMT_AMT", coopcus_lmt_amt+"");
					Map<String, String> outMap = new HashMap<String, String>();
					outMap=shuffleService.fireTargetRule("IQPCOMRULE", "IQPCOMRULE_3", inMap);
					String outFlag = outMap.get("OUT_是否通过").toString();
					String outInfo = outMap.get("OUT_提示信息").toString();
					if(outFlag.equals("不通过")){
						if(returnInfo.equals("")){
							returnInfo = outInfo;
						}else{
							returnInfo = returnInfo + ";" + outInfo;
						}
					}else{
						returnFlag = outFlag;
						returnInfo = outInfo;
					}
				}else{
					returnFlag = "通过";
					returnInfo = "未使用合作方授信，不需检查";
					returnMap.put("OUT_是否通过", returnFlag);
					returnMap.put("OUT_提示信息", returnInfo);
					return returnMap;
				}
			}
			
			//组装返回信息
			if(returnInfo.equals("")){
				returnFlag = "通过";
				returnInfo = "合作方授信额度及限额检查通过";
			}else if(returnFlag.equals("通过")){
				returnFlag = "通过";
			}else{
				returnFlag = "不通过";
			}
			returnMap.put("OUT_是否通过", returnFlag);
			returnMap.put("OUT_提示信息", returnInfo);
			
		}catch(Exception e){
			logger.error("合作方授信额度及限额检查失败！"+e.getMessage());
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
