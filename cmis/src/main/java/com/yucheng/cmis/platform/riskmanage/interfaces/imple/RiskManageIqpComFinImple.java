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
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class RiskManageIqpComFinImple implements RiskManageInterface{

	private final String modelId = "IqpLoanApp";//业务申请表
	private static final Logger logger = Logger.getLogger(RiskManageIqpComFinImple.class);
	/*融资性担保公司授信额度及限额检查
	 * 1.当前业务下担保证人属融资性担保公司担保；
     * 2.通过当前营业日期判断，用信担保限额中最新设定的生效的限额信息，当营业日期<=最新设定限额到期日期时，进行如下检查：
     * 判断该担保公司是否为改客户做过担保，如果做过担保则校验（1），否则执行(2)
     * （1）当前融资性担保公司所担保的单户存量业务敞口金额+当前业务风险敞口金额<=用信限额中的单户限额（新增）；（2）当前融资性担保公司所担保的单户存量业务敞口金额+当前业务风险敞口金额<=用户限额中的单户限额（存量）；
     * 3.如果当前设定的用信担保限额，未对当前营业日期设定限额信息时，则进行如下判断：单户存量敞口占用<=融资协议中单户限额；
     * 4.当前融资性担保公司所担保的存量金额Σ（业务敞口金额/担保放大倍数)+(当前业务风险敞口金额/担保放大倍数) <= 融资性担保公司保证金账户余额
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
			
			KeyedCollection appkColl = dao.queryDetail(modelId, serno, conn);
			String cus_id = (String) appkColl.getDataValue("cus_id");
			
			//获取担保下的融资性担保公司
			IndexedCollection iColl = new IndexedCollection();
			iColl = SqlClient.queryList4IColl("queryFinCusIdBySerno", serno, conn);
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"融资性担保公司授信额度及限额检查:iColl.size()="+iColl.size(),null);
			if(iColl.size()==0){
				returnFlag = "通过";
				returnInfo = "无融资性担保公司担保，不需检查";
				returnMap.put("OUT_是否通过", returnFlag);
				returnMap.put("OUT_提示信息", returnInfo);
				return returnMap;
			}
			
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection) iColl.get(i);
				String fin_cus_id = (String) kColl.getDataValue("cus_id");
				//判断该担保公司是否为改客户做过担保,iCollIs.size大于0则表示做过担保
				IndexedCollection iCollIs = SqlClient.queryList4IColl("checkIs4Cus", fin_cus_id, conn);
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"融资性担保公司授信额度及限额检查:iCollIs.size()="+iCollIs.size(),null);
				String is4Grt = "";
				if(iCollIs.size()>0){
					is4Grt = "1";//该担保公司为该客户做过担保
				}else{
					is4Grt = "0";//该担保公司没有为该客户做过担保
				}
				
				//融资担保公司下的所有保证金账号，通过esb交易获取保证金账户余额并汇总   start
				IndexedCollection bailIColl = SqlClient.queryList4IColl("queryBailNoByFinCusId", fin_cus_id, conn);
				BigDecimal bail_amt = new BigDecimal("0");//保证金余额汇总
				String esbFlag = "";
				String esbInfo = "";
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"融资性担保公司授信额度及限额检查:bailIColl.size()="+bailIColl.size(),null);
				for(int k=0;k<bailIColl.size();k++){
					KeyedCollection bailKColl = (KeyedCollection) bailIColl.get(k);
					String bail_acct_no = (String) bailKColl.getDataValue("bail_acct_no");
					EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"融资性担保公司授信额度及限额检查:bail_acct_no："+bail_acct_no,null);					
					/*** 调用esb模块实时接口取交易明细 ***/
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
					KeyedCollection repKColl = null;
					try{
						repKColl = service.tradeZHZH(bail_acct_no, context, conn);
					}catch(Exception e){
						esbFlag = "不通过";
						esbInfo = "ESB通讯接口【获取保证金账户信息】交易失败："+e.getMessage();
						break;
					}
					EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"融资性担保公司授信额度及限额检查:step 2",null);
//					if(context.containsKey("bail_acct_no")){
//						context.setDataValue("bail_acct_no", bail_acct_no);
//					}else{
//						context.addDataField("bail_acct_no", bail_acct_no);
//					}
//					context.addDataField("service_code", "11003000007");//交易码
//					context.addDataField("sence_code", "16");//交易场景
//					KeyedCollection repKColl = TagUtil.getRespCD("com.yucheng.cmis.biz01line.esb.op.trade.QueryBailInfo", context, conn);
					
					if(!TagUtil.haveSuccess(repKColl, context)){
						//交易失败信息
						String retMsg = (String) repKColl.getDataValue("RET_MSG");
						esbFlag = "不通过";
						esbInfo = "ESB通讯接口【获取保证金账户信息】交易失败："+retMsg;
						break;
					}else{
						KeyedCollection bodyKColl = (KeyedCollection) repKColl.getDataElement(TradeConstance.ESB_BODY);
						bail_amt =bail_amt.add(BigDecimalUtil.replaceNull(bodyKColl.getDataValue("AcctBal")));
					}
				}
				
				if(esbFlag.equals("不通过")){
					returnInfo = esbInfo;
					break;
				}
				//融资担保公司下的所有保证金账号，通过esb交易获取保证金账户余额并汇总   end
				
				//获取当前融资性担保公司所担保的单户存量业务的敞口占用
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"融资性担保公司授信额度及限额检查，获取所担保单户存量业务的敞口占用",null);
				IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, conn);
				KeyedCollection kColl4Query = new KeyedCollection();
				kColl4Query.addDataField("CUS_ID", cus_id);
				kColl4Query.addDataField("FIN_CUS_ID", fin_cus_id);
				IndexedCollection iqpContcusIColl =  SqlClient.queryList4IColl("queryIqpContByFinCusIdForCus", kColl4Query, conn);//存量业务（已包含了当前申请）
				BigDecimal fincus_lmt_amt = new BigDecimal("0");//授信占用
				for(int n=0;n<iqpContcusIColl.size();n++){
					KeyedCollection iqpContKColl = (KeyedCollection) iqpContcusIColl.get(n);
					String cont_no = (String) iqpContKColl.getDataValue("cont_no");
					String iqpserno = (String) iqpContKColl.getDataValue("serno");
					EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"融资性担保公司授信额度及限额检查，获取所担保单户存量业务的敞口占用,获取合同号为：cont_no："+cont_no,null);
					if(cont_no==null){
						BigDecimal iqp_amt = iqpLoanAppComponent.getLmtAmtBySerno(iqpserno);//（不需判断是否使用额度，不需判断授信额度标识，即统计该流水的敞口金额汇总）
						fincus_lmt_amt = fincus_lmt_amt.add(iqp_amt);
					}else{
						BigDecimal iqp_amt = iqpLoanAppComponent.getLmtAmtByContNo(cont_no);//（不需判断是否使用额度，不需判断授信额度标识，即统计该合同的敞口金额汇总）
						fincus_lmt_amt = fincus_lmt_amt.add(iqp_amt);
					}
				}
				
				//获取当前融资性担保公司所担保的存量业务的敞口占用
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"融资性担保公司授信额度及限额检查，获取所担保存量业务的敞口占用",null);
				IndexedCollection iqpContIColl =  SqlClient.queryList4IColl("queryIqpContByFinCusId", fin_cus_id, conn);//存量业务（已包含了当前申请）
				BigDecimal fin_lmt_amt = new BigDecimal("0");//授信占用
				BigDecimal cal_lmt_amt = new BigDecimal("0");//除放大倍数后占用
				BigDecimal loan_amt = new BigDecimal("0");//有效贷款总额
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "融资性担保公司授信额度及限额检查，获取存量业务queryIqpContByFinCusId：size："+iqpContIColl.size(), null);
				for(int j=0;j<iqpContIColl.size();j++){
					/* modified by yangzy 2014/12/20 融资性担保公司担保占用改造(增加存量过渡数据) start */
					KeyedCollection iqpContKColl = (KeyedCollection) iqpContIColl.get(j);
					String cont_no = (String) iqpContKColl.getDataValue("cont_no");
					String iqpserno = "";
					BigDecimal ass_sec_multiple = new BigDecimal(1);
					if(iqpContKColl.containsKey("serno")&&iqpContKColl.getDataValue("serno")!=null){
						iqpserno = (String) iqpContKColl.getDataValue("serno");
						KeyedCollection iqploanappKColl = dao.queryDetail(modelId, iqpserno, conn);
						ass_sec_multiple = new BigDecimal(iqploanappKColl.getDataValue("ass_sec_multiple")+"");
					}else{
						KeyedCollection ctrloancontKColl = dao.queryDetail("CtrLoanCont", cont_no, conn);
						ass_sec_multiple = new BigDecimal(ctrloancontKColl.getDataValue("ass_sec_multiple")+"");
					}
					/* modified by yangzy 2014/12/20 融资性担保公司担保占用改造(增加存量过渡数据) end */
					EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "融资性担保公司授信额度及限额检查，获取所担保存量业务的敞口占用,获取合同号为：cont_no："+cont_no+ "    计数："+ j, null);
					logger.info("融资性担保公司授信额度及限额检查，获取所担保存量业务的敞口占用,获取合同号为：cont_no："+cont_no + "    计数："+ j);
					if(cont_no==null){
						BigDecimal iqp_amt = iqpLoanAppComponent.getLmtAmtBySerno(iqpserno);//（不需判断是否使用额度，不需判断授信额度标识，即统计该流水的敞口金额汇总）
						EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "融资性担保公司授信额度及限额检查，获取所担保存量业务的敞口占用,业务金额：：iqp_amt："+iqp_amt, null);
						fin_lmt_amt = fin_lmt_amt.add(iqp_amt);
						cal_lmt_amt = cal_lmt_amt.add(iqp_amt.divide(ass_sec_multiple,2,BigDecimal.ROUND_UP));
						EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "融资性担保公司授信额度及限额检查，获取所担保存量业务的敞口占用,获取合同号为：cal_lmt_amt："+cal_lmt_amt, null);
						logger.info("融资性担保公司授信额度及限额检查，获取所担保存量业务的敞口占用,获取合同号为：cal_lmt_amt："+cal_lmt_amt);						
						//获取该申请的有效贷款总额
						BigDecimal amt = (BigDecimal) SqlClient.queryFirst("queryLoanAmtBySerno", iqpserno, null, connection);
						loan_amt = loan_amt.add(amt);
					}else{
						EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "融资性担保公司授信额度及限额检查，存在合同情况下：getLmtAmtByContNo：", null);
						BigDecimal iqp_amt = iqpLoanAppComponent.getLmtAmtByContNo(cont_no);//（不需判断是否使用额度，不需判断授信额度标识，即统计该合同的敞口金额汇总）
						fin_lmt_amt = fin_lmt_amt.add(iqp_amt);
						cal_lmt_amt = cal_lmt_amt.add(iqp_amt.divide(ass_sec_multiple,2,BigDecimal.ROUND_UP));
						EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "融资性担保公司授信额度及限额检查，存在合同情况下：cal_lmt_amt："+cal_lmt_amt, null);
						//获取该合同的有效贷款总额
						BigDecimal amt = (BigDecimal) SqlClient.queryFirst("queryLoanAmtByContNo", cont_no, null, connection);
						loan_amt = loan_amt.add(amt);
					}
				}
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "融资性担保公司授信额度及限额检查，开始调用规则检查", null);
				logger.info("融资性担保公司授信额度及限额检查，开始调用规则检查");
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				ShuffleServiceInterface shuffleService = null;
				shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
				Map<String, String> inMap = new HashMap<String, String>();
				inMap.put("IN_SERNO", serno);
				inMap.put("IN_CUS_ID", fin_cus_id);
				inMap.put("IN_LMT_AMT", fin_lmt_amt+"");
				inMap.put("IN_BAIL_AMT", bail_amt+"");
				inMap.put("IN_CAL_LMT_AMT", cal_lmt_amt+"");
				inMap.put("IN_FINCUS_LMT_AMT", fincus_lmt_amt+"");
				inMap.put("IN_TOTAL_LOAN_AMT", loan_amt+"");
				inMap.put("IN_IS4GRT", is4Grt);
				Map<String, String> outMap = new HashMap<String, String>();
				outMap=shuffleService.fireTargetRule("IQPCOMRULE", "IQPCOMRULE_2", inMap);
				String outFlag = outMap.get("OUT_是否通过").toString();
				String outInfo = "融资性担保公司【"+fin_cus_id+"】"+outMap.get("OUT_提示信息").toString();
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
			}
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "融资性担保公司授信额度及限额检查，调用规则检查结束", null);
			logger.info("融资性担保公司授信额度及限额检查，调用规则检查结束");			
			//组装返回信息
			if(returnInfo.equals("")){
				returnFlag = "通过";
				returnInfo = "融资性担保公司授信额度及限额检查通过";
			}else if(returnFlag.equals("通过")){
				returnFlag = "通过";
			}else{
				returnFlag = "不通过";
			}
			returnMap.put("OUT_是否通过", returnFlag);
			returnMap.put("OUT_提示信息", returnInfo);
			
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "融资性担保公司授信额度及限额检查失败！"+e.getMessage(), null);
			logger.error("融资性担保公司授信额度及限额检查失败！"+e.getStackTrace());
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
