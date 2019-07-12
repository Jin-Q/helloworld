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

public class RiskManageIqpComGuarImple implements RiskManageInterface{
	private static final Logger logger = Logger.getLogger(RiskManageIqpComGuarImple.class);
	/*担保合同与其下担保品信息合规性检查（仅取新增的担保合同，即为登记状态且为一般担保合同）
	 * 1.担保方式为“抵押”时，其下至少有一个抵押物，并且抵押物担保金额>=担保合同金额；
     * 2.担保方式为“质押”时，其下至少有一个质押物，并且质押物担保金额>=担保合同金额；
     * 3.担保方式为“单人担保”时，其下至少并且只能有一个保证人，并且保证人担保金额>=担保合同金额；
     * 4.担保方式为“多人分保”时，其下至少有两个（包含）以上的保证人，并且所有的担保人担保金额>=担保合同金额；
     * 5.担保方式为“多人联保”时，其下至少有两个（包含）以上的保证人，并且每个担保人担保金额 = 担保合同金额。
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
			//先检查业务下所有担保合同的终止日是否小于当前营业日期
			IndexedCollection iCollAll = SqlClient.queryList4IColl("guarContNoBySerno", serno, conn);
			if(iCollAll.size()!=0){
				returnFlag = "不通过";
				returnInfo = "存在担保合同终止日小于等于当前营业日期";
				returnMap.put("OUT_是否通过", returnFlag);
				returnMap.put("OUT_提示信息", returnInfo);
				return returnMap;
			}
			
			IndexedCollection iColl = SqlClient.queryList4IColl("queryGuarContNoBySernoForRiskManage", serno, conn);
			
			if(iColl.size()==0){
				returnFlag = "通过";
				/* added by yangzy 2014/11/03 应收账款类质押校验担保金额合规是需要回款保证金金额 start */
				//returnInfo = "未新增一般担保合同，不需检查";
				returnInfo = "未存在需要检查的担保合同，不需检查";
				/* added by yangzy 2014/11/03 应收账款类质押校验担保金额合规是需要回款保证金金额 end */
				returnMap.put("OUT_是否通过", returnFlag);
				returnMap.put("OUT_提示信息", returnInfo);
				return returnMap;
			}
			
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection) iColl.get(i);
				String guar_cont_no = (String) kColl.getDataValue("guar_cont_no");
				/* added by yangzy 2014/12/03 应收账款类质押校验担保金额合规(担保合同已用金额与在池票据金额是否覆盖) start */
				IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
				TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
				KeyedCollection kColl4GrtCont = dao.queryDetail("GrtGuarCont", guar_cont_no, connection);
				BigDecimal used_amt = new BigDecimal(0.00);
				if(kColl4GrtCont.containsKey("guar_cont_type")&&kColl4GrtCont.getDataValue("guar_cont_type")!=null&&"00".equals(kColl4GrtCont.getDataValue("guar_cont_type").toString())){
					//一般担保  --1 正常  2 新增  3 解除  4 续作   5 已解除  6 被续作
					//根据合同编号和担保合同编号查询是否存在担保变更记录--start---------------
					
					String condtitionSelectIsChange = "where cont_no is null and corre_rel in ('2','4','3') and guar_cont_no = '"+guar_cont_no+"'";
				    IndexedCollection iCollSelectIsChange = dao.queryList("GrtLoanRGur", condtitionSelectIsChange, connection);
				    //根据合同编号和担保合同编号查询是否存在担保变更记录--end--------------------
				    String conditionStr = "";
				    conditionStr = "where guar_cont_no='"+guar_cont_no+"' and is_add_guar='2' and corre_rel in('1','5')";

					/**查询关联表中此担保合同已已经引入的金额*/
					IndexedCollection iColl4RGur =  dao.queryList("GrtLoanRGur", conditionStr, connection);
					for(int j=0;j<iColl4RGur.size();j++){
					   KeyedCollection kColl1 = (KeyedCollection)iColl4RGur.get(j);
					   String is_per_gur = (String)kColl1.getDataValue("is_per_gur");
					   if(is_per_gur != null && !"".equals(is_per_gur)){
						   String pk_id = (String)kColl1.getDataValue("pk_id");
						   String cont_no = (String)kColl1.getDataValue("cont_no");
						   if(cont_no != null && !"".equals(cont_no)){
							   String res = iqpLoanAppComponent.caculateGuarAmtSp(null, cont_no,pk_id);
							   if("2".equals(res)){
								   used_amt = used_amt.add(new BigDecimal(kColl1.getDataValue("guar_amt").toString()));
							   }else{
								   used_amt = used_amt.add(new BigDecimal(0));
							   }
						   }else{
							   String sernoSelect = (String)kColl1.getDataValue("serno");
							   String res = iqpLoanAppComponent.caculateGuarAmtSp(sernoSelect, null,pk_id);
							   if("2".equals(res)){
								   used_amt = used_amt.add(new BigDecimal(kColl1.getDataValue("guar_amt").toString()));
							   }else{
								   used_amt = used_amt.add(new BigDecimal(0));
							   }
						   }
					   }
					}
				}else{
					//最高额担保
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
					used_amt = BigDecimalUtil.replaceNull(service.getAmtForGuarCont(guar_cont_no, context, connection));
				}
				/* added by yangzy 2014/12/03 应收账款类质押校验担保金额合规(担保合同已用金额与在池票据金额是否覆盖) end */
				
				/**modified by lisj 2015-2-2 需求编号【HS141110017】保理业务改造 begin**/
				/* added by yangzy 2014/11/03 应收账款类质押校验担保金额合规是需要回款保证金金额 start */
				BigDecimal bail_amt = new BigDecimal("0");
				IndexedCollection bailInfoIColl = SqlClient.queryList4IColl("getBailAccInfo4Grt", guar_cont_no, connection);
				if(bailInfoIColl!=null && bailInfoIColl.size()>0){
					for(int t=0;t<bailInfoIColl.size();t++){
						KeyedCollection temp = (KeyedCollection) bailInfoIColl.get(t);
						if(temp!=null&&temp.containsKey("bail_acc_no")&&temp.getDataValue("bail_acc_no")!=null&&!"".equals(temp.getDataValue("bail_acc_no"))){
							String bail_acc_no = (String)temp.getDataValue("bail_acc_no");
							KeyedCollection repKColl = null;
							CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
							ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
							IqpServiceInterface iqpservice = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
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
								bail_amt = bail_amt.add(BigDecimalUtil.replaceNull(bodyKColl.getDataValue("AcctBal")).multiply(exchange_rate));//余额多少就占多少
							}
						}
					}
				}
				/* added by yangzy 2014/11/03 应收账款类质押校验担保金额合规是需要回款保证金金额 end */
				/**modified by lisj 2015-2-2 需求编号【HS141110017】保理业务改造 end**/
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				ShuffleServiceInterface shuffleService = null;
				shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
				Map<String, String> inMap = new HashMap<String, String>();
				inMap.put("IN_GUAR_CONT_NO", guar_cont_no);
				/* added by yangzy 2014/12/03 应收账款类质押校验担保金额合规(担保合同已用金额与在池票据金额是否覆盖) start */
				inMap.put("IN_USED_AMT", used_amt+"");
				/* added by yangzy 2014/12/03 应收账款类质押校验担保金额合规(担保合同已用金额与在池票据金额是否覆盖) end */
				/* added by yangzy 2014/11/03 应收账款类质押校验担保金额合规是需要回款保证金金额 start */
				inMap.put("IN_BAIL_AMT", bail_amt+"");
				/* added by yangzy 2014/11/03 应收账款类质押校验担保金额合规是需要回款保证金金额 end */
				Map<String, String> outMap = new HashMap<String, String>();
				outMap=shuffleService.fireTargetRule("IQPCOMRULE", "IQPCOMRULE_1", inMap);
				String outFlag = outMap.get("OUT_是否通过").toString();
				String outInfo = "担保合同【"+guar_cont_no+"】"+outMap.get("OUT_提示信息").toString();
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
			
			//组装返回信息
			if(returnInfo.equals("")){
				returnFlag = "通过";
				returnInfo = "担保合同与其下担保品信息合规性检查通过";
			}else if(returnFlag.equals("通过")){
				returnFlag = "通过";
			}else{
				returnFlag = "不通过";
			}
			returnMap.put("OUT_是否通过", returnFlag);
			returnMap.put("OUT_提示信息", returnInfo);
			
		}catch(Exception e){
			logger.error("担保合同与其下担保品信息合规性检查失败！"+e.getMessage());
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
