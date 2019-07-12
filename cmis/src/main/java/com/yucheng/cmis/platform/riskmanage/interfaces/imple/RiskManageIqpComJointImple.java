package com.yucheng.cmis.platform.riskmanage.interfaces.imple;

import java.util.HashMap;
import java.util.Iterator;
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
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class RiskManageIqpComJointImple implements RiskManageInterface{

	private final String modelId = "IqpLoanApp";//业务申请表
	private static final Logger logger = Logger.getLogger(RiskManageIqpComJointImple.class);
	/*联保小组授信额度及限额检查
	 * 1.检查当前业务单户存量风险敞口金额 <= 联保小组授信协议单户限额；
     * 2.检查当前业务风险敞口金额+联保小组下所有成员存量业务风险敞口金额 <= 授信总额；
     * 3.检查当前业务风险敞口金额+联保小组下所有成员存量业务风险敞口金额 <= 联保小组下所有保证金账户余额之和(废弃)
     * 3.检查当前业务单户存量风险敞口金额中最大的一笔 <= 联保小组下所有保证金账户余额之和(启用)
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
				if(!limit_ind.equals("1")&&!limit_ind.equals("4")){
					String limit_acc_no = (String) kColl.getDataValue("limit_acc_no");
					
					//通过esb交易获取联保协议下所有保证金账户余额并汇总   start
					IndexedCollection bailIColl = SqlClient.queryList4IColl("queryJointBailNoByLimitCode", limit_acc_no, conn);
					BigDecimal bail_amt = new BigDecimal("0");//保证金余额汇总
					String esbFlag = "";
					String esbInfo = "";
					for(int k=0;k<bailIColl.size();k++){
						KeyedCollection bailKColl = (KeyedCollection) bailIColl.get(k);
						String bail_acct_no = (String) bailKColl.getDataValue("bail_acct_no");
						
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
						
//						if(context.containsKey("bail_acct_no")){
//							context.setDataValue("bail_acct_no", bail_acct_no);
//						}else{
//							context.addDataField("bail_acct_no", bail_acct_no);
//						}
//						context.addDataField("service_code", "11003000007");//交易码
//						context.addDataField("sence_code", "16");//交易场景
//						KeyedCollection repKColl = TagUtil.getRespCD("com.yucheng.cmis.biz01line.esb.op.trade.QueryBailInfo", context, conn);
						if(!TagUtil.haveSuccess(repKColl, context)){
							//交易失败信息
							String retMsg = (String) repKColl.getDataValue("RET_MSG");
							esbFlag = "不通过";
							esbInfo = "ESB通讯接口【获取保证金账户信息】交易失败："+retMsg;
							break;
						}else{
							KeyedCollection bodyKColl = (KeyedCollection) repKColl.getDataElement(TradeConstance.ESB_BODY);
							BigDecimal amt = BigDecimalUtil.replaceNull(bodyKColl.getDataValue("AcctBal"));
							bail_amt = bail_amt.add(amt); 
						}
					}
					
					if(esbFlag.equals("不通过")){
						returnMap.put("OUT_是否通过", esbFlag);
						returnMap.put("OUT_提示信息", esbInfo);
						return returnMap;
					}
					//通过esb交易获取保证金账户余额并汇总   end
					
					/**modified by yangzy 2015/04/22  关于信贷联保授信业务变更 start**/
					/**add by lisj 2015-3-5 需求编号 :【XD150306018】关于信贷联保授信业务变更 begin**/
					//查询该客户下所有联保授信台账的授信台账编号列表，通过查询出的列表计算出总的授信额度占用情况，与引入的授信台账额度的授信金额进行比较
					IndexedCollection jointIC = SqlClient.queryList4IColl("queryJointLimitCodeByLimitCode", limit_acc_no, conn);//获取联保协议下的所有额度台账
					if(jointIC!=null&&jointIC.size()>0){
						BigDecimal lmt_amt_count = new BigDecimal("0");//初始化联保额度授信总占用金额
						IndexedCollection  agrNoList4JC = dao.queryList("LmtAgrDetails", "where cus_id='"+cus_id+"' and sub_type='03'", connection);
						BigDecimal crdAmt = (BigDecimal) SqlClient.queryFirst("queryCrdAmtByLimitCode", limit_acc_no, null, connection);//授信金额
						if(agrNoList4JC!=null && agrNoList4JC.size()>0){
							for(Iterator<KeyedCollection> iterator = agrNoList4JC.iterator();iterator.hasNext();){
								KeyedCollection temp = (KeyedCollection)iterator.next();
								String limit_code = temp.getDataValue("limit_code").toString();
								CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
								IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
								KeyedCollection kCollTemp = serviceIqp.getAgrUsedInfoByArgNo(limit_code, "01", connection, context);
								lmt_amt_count =  lmt_amt_count.add(BigDecimalUtil.replaceNull(kCollTemp.getDataValue("lmt_amt").toString()));//授信总占用金额
							}
						}
						//授信金额小于联保额度授信总占用金额
						if(crdAmt.compareTo(lmt_amt_count)<0){
							returnFlag = "不通过";
							returnInfo = "引入的授信台账【"+limit_acc_no+"】授信金额小于授信总占用金额，检查不通过";
							returnMap.put("OUT_是否通过", returnFlag);
							returnMap.put("OUT_提示信息", returnInfo);
							return returnMap;
						}
					}
					/**add by lisj 2015-3-5 需求编号 :【XD150306018】关于信贷联保授信业务变更 end**/
					/**modified by yangzy 2015/04/22  关于信贷联保授信业务变更 end**/
					//联保协议存量授信占用（包含本次）：联保比较特殊，使用的是授信占用，不是统计实时风险敞口，即区分一次性类型额度
					BigDecimal joint_lmt_amt = new BigDecimal("0");
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
					IndexedCollection jointIColl = SqlClient.queryList4IColl("queryJointLimitCodeByLimitCode", limit_acc_no, conn);//获取联保协议下的所有额度台账
					for(int i=0;i<jointIColl.size();i++){
						KeyedCollection jointKColl = (KeyedCollection) jointIColl.get(i);
						String limit_code = (String) jointKColl.getDataValue("limit_code");
						KeyedCollection lmtKColl = service.getAgrUsedInfoByArgNo(limit_code,"01",conn,context);
						joint_lmt_amt = joint_lmt_amt.add(new BigDecimal(lmtKColl.getDataValue("lmt_amt")+""));//授信占用
					}
					
					//单户存量授信占用（包含本次）：联保比较特殊，使用的是授信占用，不是统计实时风险敞口，即区分一次性类型额度
					BigDecimal jointcus_lmt_amt = new BigDecimal("0");
					KeyedCollection kColl4Query = new KeyedCollection();
					kColl4Query.addDataField("CUS_ID", cus_id);
					kColl4Query.addDataField("LIMIT_ACC_NO", limit_acc_no);
					IndexedCollection jointcusIColl = SqlClient.queryList4IColl("queryJointLimitCodeByLimitCodeForCus", kColl4Query, conn);
					for(int j=0;j<jointcusIColl.size();j++){
						KeyedCollection jointcusKColl = (KeyedCollection) jointcusIColl.get(j);
						String limit_code = (String) jointcusKColl.getDataValue("limit_code");
						KeyedCollection lmtKColl = service.getAgrUsedInfoByArgNo(limit_code,"01",conn,context);
						jointcus_lmt_amt = jointcus_lmt_amt.add(new BigDecimal(lmtKColl.getDataValue("lmt_amt")+""));//授信占用
					}
					//取当前业务单户存量风险敞口金额中最大的一笔
					BigDecimal jointcus_big_lmt_amt = new BigDecimal("0");
					for(int j=0;j<jointcusIColl.size();j++){
						KeyedCollection jointcusKColl = (KeyedCollection) jointcusIColl.get(j);
						String limit_code = (String) jointcusKColl.getDataValue("limit_code");
						KeyedCollection lmtKColl = service.getAgrUsedInfoByArgNo(limit_code,"01",conn,context);
						BigDecimal jointcus_lmt_amt_this = BigDecimalUtil.replaceNull(lmtKColl.getDataValue("lmt_amt"));//授信占用
						if(jointcus_big_lmt_amt!= new BigDecimal("0")){
							if(jointcus_lmt_amt_this.compareTo(jointcus_big_lmt_amt)>0){
								jointcus_big_lmt_amt = jointcus_lmt_amt_this;
							}
						}
					}
					
					ShuffleServiceInterface shuffleService = null;
					shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
					Map<String, String> inMap = new HashMap<String, String>();
					inMap.put("IN_SERNO", serno);
					inMap.put("IN_JOINT_LMT_AMT", joint_lmt_amt+"");
					inMap.put("IN_BAIL_AMT", bail_amt+"");
					inMap.put("IN_JOINTCUS_BIG_LMT_AMT", jointcus_big_lmt_amt+"");
					inMap.put("IN_JOINTCUS_LMT_AMT", jointcus_lmt_amt+"");
					Map<String, String> outMap = new HashMap<String, String>();
					outMap=shuffleService.fireTargetRule("IQPCOMRULE", "IQPCOMRULE_4", inMap);
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
					returnInfo = "未使用联保授信，不需检查";
					returnMap.put("OUT_是否通过", returnFlag);
					returnMap.put("OUT_提示信息", returnInfo);
					return returnMap;
				}
			}
			
			//组装返回信息
			if(returnInfo.equals("")){
				returnFlag = "通过";
				returnInfo = "联保小组授信额度及限额检查通过";
			}else if(returnFlag.equals("通过")){
				returnFlag = "通过";
			}else{
				returnFlag = "不通过";
			}
			returnMap.put("OUT_是否通过", returnFlag);
			returnMap.put("OUT_提示信息", returnInfo);
			
		}catch(Exception e){
			logger.error("联保小组授信额度及限额检查失败！"+e.getMessage());
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
