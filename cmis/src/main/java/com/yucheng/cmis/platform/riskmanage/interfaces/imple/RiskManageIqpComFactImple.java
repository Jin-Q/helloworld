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
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class RiskManageIqpComFactImple implements RiskManageInterface{

	private final String modelId = "IqpLoanApp";//业务申请表
	private static final Logger logger = Logger.getLogger(RiskManageIqpComFactImple.class);
	/** 
	 * <pre> 
	 * Title:保理信息合规检查
	 * Description: 1.保理基本信息检查 2.保理关联的保理池在池金额是否覆盖业务敞口
	 * </pre>
	 * @author yangzy
	 * 创建日期：2015/05/29
	 * @version 1.00.00
	 * <pre>
	 *    修改后版本:        修改人：         修改日期:              修改内容: 
	 * </pre>
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
			
			KeyedCollection appkColl = dao.queryDetail(modelId, serno, conn);
			String prd_id = (String) appkColl.getDataValue("prd_id");
			if(!"800020".equals(prd_id)&&!"800021".equals(prd_id)){
				returnMap.put("OUT_是否通过", "通过");
				returnMap.put("OUT_提示信息", "非保理业务不需要检查");
				return returnMap;
			}
						
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ShuffleServiceInterface shuffleService = null;
			shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
			Map<String, String> inMap = new HashMap<String, String>();
			inMap.put("IN_SERNO", serno);
			inMap.put("IN_TABNAME", "IQP_LOAN_APP");
			Map<String, String> outMap = new HashMap<String, String>();
			outMap=shuffleService.fireTargetRule("IQPCOMRULE", "CHECKFACTORINGBIZINFO", inMap);
			String outFlag = outMap.get("OUT_是否通过").toString();
			String outInfo = outMap.get("OUT_提示信息").toString();
			if(outFlag.equals("不通过")){
				returnFlag = outFlag;
				returnInfo = outInfo;
				returnMap.put("OUT_是否通过", returnFlag);
				returnMap.put("OUT_提示信息", returnInfo);
				return returnMap;
			}
			
			KeyedCollection factkColl = dao.queryDetail("IqpInterFact", serno, conn);
			String po_no = "";
			if(factkColl!=null&&factkColl.containsKey("po_no")&&factkColl.getDataValue("po_no")!=null&&!"".equals(factkColl.getDataValue("po_no"))){
				po_no = (String) factkColl.getDataValue("po_no");
				KeyedCollection kCollMana_p1 = dao.queryFirst("IqpActrecpoMana", null, "where po_no = '"+po_no+"'", conn);
				BigDecimal crd_rgtchg_amt = new BigDecimal("0");
				BigDecimal pledge_rate = BigDecimalUtil.replaceNull(kCollMana_p1.getDataValue("pledge_rate"));
				String poType = (String)kCollMana_p1.getDataValue("po_type");
				BigDecimal bail_amt = new BigDecimal("0");
				IndexedCollection IqpBADList = dao.queryList("IqpBailaccDetail", "where po_no = '"+po_no+"'", conn);
				if(IqpBADList !=null && IqpBADList.size()>0){
					for(int i=0;i<IqpBADList.size();i++){
						KeyedCollection temp  = (KeyedCollection) IqpBADList.get(i);
						if(temp!=null&&temp.containsKey("bail_acc_no")&&temp.getDataValue("bail_acc_no")!=null&&!"".equals(temp.getDataValue("bail_acc_no"))){
							String bail_acc_no = (String)temp.getDataValue("bail_acc_no");
							KeyedCollection repKColl = null;
							CMISModualServiceFactory serviceJndi1 = CMISModualServiceFactory.getInstance();
							ESBServiceInterface service = (ESBServiceInterface)serviceJndi1.getModualServiceById("esbServices", "esb");
							IqpServiceInterface iqpservice = (IqpServiceInterface)serviceJndi1.getModualServiceById("iqpServices", "iqp");
							if("1".equals(poType)){
								try{
									repKColl = service.tradeZHZH(bail_acc_no, context, conn);
								}catch(Exception e){
									throw new Exception("ESB通讯接口【获取保证金账户信息】交易失败："+e.getMessage());
								}
								if(!TagUtil.haveSuccess(repKColl, context)){
									//交易失败信息
									String retMsg = (String) repKColl.getDataValue("RET_MSG");
									throw new Exception("ESB通讯接口【获取保证金账户信息】交易失败："+retMsg);
								}else{
									KeyedCollection bodyKColl = (KeyedCollection) repKColl.getDataElement(TradeConstance.ESB_BODY);
									String CCY = (String) bodyKColl.getDataValue("Ccy");//保证金币种
									KeyedCollection kCollRate = iqpservice.getHLByCurrType(CCY, context, conn);
									BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率，先不乘汇率
									if(exchange_rate==null){
										throw new Exception("获取不到币种"+CCY+"的汇率！");
									}
									bail_amt =bail_amt.add(BigDecimalUtil.replaceNull(bodyKColl.getDataValue("AcctBal")).multiply(exchange_rate));
								}
							}else{
								try{
									repKColl = service.tradeZHZH(bail_acc_no, context, conn);
								}catch(Exception e){
									throw new Exception("ESB通讯接口【获取保理账户信息】交易失败："+e.getMessage());
								}
								if(!TagUtil.haveSuccess(repKColl, context)){
									//交易失败信息
									String retMsg = (String) repKColl.getDataValue("RET_MSG");
									throw new Exception("ESB通讯接口【获取保理账户信息】交易失败："+retMsg);
								}else{
									KeyedCollection bodyKColl = (KeyedCollection) repKColl.getDataElement(TradeConstance.ESB_BODY);
									String CCY = (String) bodyKColl.getDataValue("Ccy");//币种
									KeyedCollection kCollRate = iqpservice.getHLByCurrType(CCY, context, conn);
									BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率，先不乘汇率
									if(exchange_rate==null){
										throw new Exception("获取不到币种"+CCY+"的汇率！");
									}
									//bail_amt = BigDecimalUtil.replaceNull(bodyKColl.getDataValue("AMT")).multiply(exchange_rate);//余额多少就占多少
									bail_amt =bail_amt.add(BigDecimalUtil.replaceNull(bodyKColl.getDataValue("AcctBal")).abs().multiply(exchange_rate));
								}
							}
						}
					}
				}
				
				IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, conn);
				KeyedCollection kColl4Query = new KeyedCollection();
				kColl4Query.put("serno", serno);
				kColl4Query.put("po_no", po_no);
				IndexedCollection iqpContIColl =  SqlClient.queryList4IColl("queryIqpContByPoNoSernoForFact", kColl4Query, conn);//存量业务（已包含了当前申请）
				BigDecimal loan_amt = new BigDecimal("0");//业务总敞口
				for(int j=0;j<iqpContIColl.size();j++){
					KeyedCollection iqpContKColl = (KeyedCollection) iqpContIColl.get(j);
					String cont_no = (String) iqpContKColl.getDataValue("cont_no");
					String iqpserno = (String) iqpContKColl.getDataValue("serno");
					if(cont_no==null){
						BigDecimal iqp_amt = iqpLoanAppComponent.getLmtAmtBySerno(iqpserno);//（不需判断是否使用额度，不需判断授信额度标识，即统计该流水的敞口金额汇总）
						loan_amt = loan_amt.add(iqp_amt);
					}else{
						BigDecimal iqp_amt = iqpLoanAppComponent.getLmtAmtByContNo(cont_no);//（不需判断是否使用额度，不需判断授信额度标识，即统计该合同的敞口金额汇总）
						loan_amt = loan_amt.add(iqp_amt);
					}
				}
				KeyedCollection ActrecbondKcoll = (KeyedCollection)SqlClient.queryFirst("queryIqpActrecbondDetail", po_no, null, conn);
				if(ActrecbondKcoll!=null&&ActrecbondKcoll.getDataValue("invcquant")!=null){
					crd_rgtchg_amt = (BigDecimal)ActrecbondKcoll.getDataValue("bondamt");
				}
				if(crd_rgtchg_amt.multiply(pledge_rate).add(bail_amt).compareTo(loan_amt)<0){
					returnFlag = "不通过";
					returnInfo = "保理业务关联相关保理池不能覆盖该保理池关联的存量业务敞口，请检查";
					returnMap.put("OUT_是否通过", returnFlag);
					returnMap.put("OUT_提示信息", returnInfo);
					return returnMap;
				}
				returnFlag = "通过";
				returnInfo = "保理业务及关联相关保理池检查通过";
			}else{
				returnFlag = "不通过";
				returnInfo = "保理业务未关联相关保理池信息，请检查";
				returnMap.put("OUT_是否通过", returnFlag);
				returnMap.put("OUT_提示信息", returnInfo);
				return returnMap;
			}
			
			returnMap.put("OUT_是否通过", returnFlag);
			returnMap.put("OUT_提示信息", returnInfo);
			
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "保理业务及关联相关保理池检查失败！"+e.getMessage(), null);
			logger.error("保理业务及关联相关保理池检查失败！"+e.getStackTrace());
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
