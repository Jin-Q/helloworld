package com.yucheng.cmis.platform.riskmanage.op.risklist;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;


import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.iqp.msi.msiimple.IqpServiceInterfaceImple;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class CheckBailInfo4Iqp implements RiskManageInterface {

	private final String modelIdBailInfo = "PubBailInfo";
	private final String modelIdCont = "CtrLoanCont";
	private static final Logger logger = Logger.getLogger(IqpServiceInterfaceImple.class);
	public Map<String, String> getResultMap(String tableName, String serno, Context context,Connection connection) throws Exception {
		
		Map<String,String> param = new HashMap<String,String>();
		BigDecimal amt = new BigDecimal(0.00);
		BigDecimal amt_all =new BigDecimal(0.00);
		try {
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			//出账时校验保证金是否足额
			KeyedCollection kColl = dao.queryDetail(tableName, serno, connection);
			
			//保证金/保函修改校验保证金
			if("IqpCreditChangeApp".equals(tableName) || "IqpGuarantChangeApp".equals(tableName)){
				String cont_no = (String)kColl.getDataValue("cont_no");
				IndexedCollection iColl = dao.queryList(modelIdBailInfo, "where cont_no='"+cont_no+"'", connection);
				//保证金条数只能为一条
				if(iColl.size()>1){
					param.put("OUT_是否通过","不通过");
					param.put("OUT_提示信息","保证金信息只允许录入一条");
					return param;
				}
				
				KeyedCollection kCollCtr = dao.queryDetail("CtrLoanCont", cont_no, connection);
				//获取新的保证金比例
				BigDecimal new_security_rate =BigDecimalUtil.replaceNull(kColl.getDataValue("new_security_rate"));
				String cont_cur_type = (String)kColl.getDataValue("cont_cur_type");
				//String security_cur_type = (String)kColl.getDataValue("security_cur_type");
				String prd_id = (String)kColl.getDataValue("prd_id");
				BigDecimal new_apply_amt = null;
				BigDecimal cont_amt_old = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_amt"));
				if("IqpCreditChangeApp".equals(tableName)){
					new_apply_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("new_apply_amt"));
				}else{
					new_apply_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("new_cont_amt"));
				}
				
				if(new_security_rate.compareTo(new BigDecimal(0))>0){
					if(iColl.size()>0){
						KeyedCollection kCollBailInfo = (KeyedCollection)iColl.get(0);
						String security_cur_type = (String)kCollBailInfo.getDataValue("cur_type");
						//获取实时汇率
						KeyedCollection kCollRate = serviceIqp.getHLByCurrType(cont_cur_type, context, connection);
						KeyedCollection kCollRateSecurity = serviceIqp.getHLByCurrType(security_cur_type, context, connection);
						if("failed".equals(kCollRate.getDataValue("flag"))){
							throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
						}
						if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
							throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
						}
						BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
						BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
						
						BigDecimal cont_balance = BigDecimalUtil.replaceNull(kCollCtr.getDataValue("cont_balance"));
						cont_balance = cont_balance.add(new_apply_amt.subtract(cont_amt_old));
						//判断是否为信用证业务
						BigDecimal floodact_perc = new BigDecimal(0);//溢装比例
						if("700020".equals(prd_id) || "700021".equals(prd_id)){
								floodact_perc = BigDecimalUtil.replaceNull(kColl.getDataValue("new_floodact_perc"));
						}
						//合同余额*保证金比例*（1+溢装比例）*申请汇率/保证金汇率
						//计算结果进百
						//进百后乘保证金汇率
						BigDecimal securityAmt = cont_balance.multiply(new_security_rate).multiply(new BigDecimal(1).add(floodact_perc)).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
						securityAmt = cmisComponent.carryCurrency(securityAmt);
						cont_balance = securityAmt.multiply(exchange_rate_security);
						//贷款余额
						BigDecimal loan_balance_all = serviceIqp.getLoanBalanceByContNo(cont_no,context,connection);
						//贷款余额*保证金比例*申请汇率/保证金汇率
						//计算结果进百
						//进百后乘保证金汇率
						loan_balance_all = loan_balance_all.multiply(new_security_rate).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
						loan_balance_all = cmisComponent.carryCurrency(loan_balance_all);
						loan_balance_all = loan_balance_all.multiply(exchange_rate_security);
						cont_balance = cont_balance.add(loan_balance_all);
						amt = amt.add(cont_balance);
						amt = amt.divide(new BigDecimal(1),2,BigDecimal.ROUND_HALF_EVEN);
						/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
						amt_all = this.getAmt(iColl,serno,amt,null,tableName, context, connection);
						/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
						// 
						BigDecimal contAmt = serviceIqp.getBailInfo4Change(cont_no, context, connection);
						//保证金总额-（合同占用总额（除去本笔）+本次金额*币种*汇率）
						BigDecimal res = amt_all.subtract(contAmt.add(amt));
						if(res.compareTo(new BigDecimal(0))>=0){
							param.put("OUT_是否通过","通过");
							param.put("OUT_提示信息","保证金账户信息检查通过");
						}else{
							param.put("OUT_是否通过","不通过");
							param.put("OUT_提示信息","保证金账户金额不能满足本次业务申请需要");
						}
					}else{
				    	param.put("OUT_是否通过","不通过");
						param.put("OUT_提示信息","保证金信息缺失");
				    }
				}else{
					param.put("OUT_是否通过","通过");
					param.put("OUT_提示信息","保证金比例为0,无需检查保证金账户信息");
				}
				return param;
			}else if("IqpLoanApp".equals(tableName)){
				Double security_rate = Double.valueOf(kColl.getDataValue("security_rate")+"");
				String prdid = "";
				if(kColl.containsKey("prd_id")){
					prdid = (String)kColl.getDataValue("prd_id");
				}
				if(security_rate>0||"200024".equals(prdid)){
					IndexedCollection iColl = dao.queryList(modelIdBailInfo, "where serno='"+serno+"'", connection);
					//保证金条数只能为一条
					if(iColl.size()>1){
						param.put("OUT_是否通过","不通过");
						param.put("OUT_提示信息","保证金信息只允许录入一条");
						return param;
					}
					//判断保证金比例是否大于0
					if(iColl.size()>0){
					   param.put("OUT_是否通过","通过");
					   param.put("OUT_提示信息","保证金账户信息检查通过");
					}else{
					   param.put("OUT_是否通过","不通过");
					   param.put("OUT_提示信息","保证金信息缺失");
					}
				}else{
					param.put("OUT_是否通过","通过");
					param.put("OUT_提示信息","保证金比例为0,无需检查保证金账户信息");
				}
				return param;
			}else{
				String cont_no = (String)kColl.getDataValue("cont_no");
				KeyedCollection kCollCont = dao.queryDetail(modelIdCont, cont_no, connection);
	           	String iqpSerno = (String)kCollCont.getDataValue("serno");
	           	/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
	           	BigDecimal security_exchange_rate = BigDecimalUtil.replaceNull(kCollCont.getDataValue("security_exchange_rate"));//保证金币种汇率
	           	/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
	           	if(iqpSerno == null || "".equals(iqpSerno)){
	           		param.put("OUT_是否通过","通过");
					param.put("OUT_提示信息","不适合此项检查!");
					return param;
	           	}else{
	           		Double security_rate = Double.valueOf(kCollCont.getDataValue("security_rate")+"");
					IndexedCollection iColl = dao.queryList(modelIdBailInfo, "where cont_no='"+cont_no+"'", connection);
					//保证金条数只能为一条
					if(iColl.size()>1){
						param.put("OUT_是否通过","不通过");
						param.put("OUT_提示信息","保证金信息只允许录入一条");
						return param;
					}
					//判断保证金比例是否大于0
					if(security_rate>0){
					    if(iColl.size()>0){
					    	//amt_all = this.getAmt(iColl, context, connection);
							/**统计占用保证金金额*/
							//BigDecimal iqpAmt = serviceIqp.getBailInfo4IqpApp(iqpSerno, context, connection);
							BigDecimal contAmt = serviceIqp.getBailInfo4cont(iqpSerno, context, connection);
							/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
							amt_all = this.getAmt(iColl,serno,null,security_exchange_rate,tableName, context, connection);
							/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
							BigDecimal res = amt_all.subtract(contAmt);
							if(res.compareTo(new BigDecimal(0))>=0){
								param.put("OUT_是否通过","通过");
								param.put("OUT_提示信息","保证金账户信息检查通过");
							}else{
								param.put("OUT_是否通过","不通过");
								param.put("OUT_提示信息","保证金账户金额不能满足本次业务申请需要");
							}
					    }else{
					    	param.put("OUT_是否通过","不通过");
							param.put("OUT_提示信息","保证金信息缺失");
					    }
					}else{
						param.put("OUT_是否通过","通过");
						param.put("OUT_提示信息","保证金比例为0,无需检查保证金账户信息");
					}
					return param;
	           	}
			}
		} catch (Exception e) {
			throw new Exception("保证金检查比较失败!"+e.getMessage());
		}
	}
	
	private BigDecimal getAmt(IndexedCollection iColl,String serno,BigDecimal seAmt,BigDecimal securityExchangeRate,String tableName,Context context, Connection connection)throws Exception{
		BigDecimal amt = new BigDecimal(0.00);
		BigDecimal guarantee_amt = new BigDecimal(0.00);
		BigDecimal guarantee_int = new BigDecimal(0.00);
		BigDecimal amt_all =new BigDecimal(0.00);
		BigDecimal exchange_rate =new BigDecimal(0.00);
		/**实时查询保证金金额---------------start----------------------------------*/
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
		IqpServiceInterface iqpservice = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
		//获取实时汇率  end
		for(int i=0;i<iColl.size();i++){
    		KeyedCollection kCollBail = (KeyedCollection)iColl.get(i);
    		String bail_acct_no = (String)kCollBail.getDataValue("bail_acct_no");
    		/*** 调用esb模块实时接口取交易明细 ***/
			KeyedCollection repKColl = null;
			try{
				repKColl = service.tradeZHZH(bail_acct_no, context, connection);
				KeyedCollection bodyKColl = (KeyedCollection)repKColl.getDataElement("BODY");
				//bail_acct_no = (String)bodyKColl.getDataValue("GUARANTEE_ACCT_NO");
				bail_acct_no = (String)bodyKColl.getDataValue("AcctNoCrdNo");
				if(bail_acct_no != null && !"".equals(bail_acct_no)){
					guarantee_amt = BigDecimalUtil.replaceNull(bodyKColl.getDataValue("AcctBal"));
					guarantee_int = BigDecimalUtil.replaceNull(bodyKColl.getDataValue("NetIntAmt"));
					amt = guarantee_amt.add(guarantee_int);
					logger.info("---------------核心返回保证金金额为:"+guarantee_amt);
					logger.info("---------------核心返回保证金利息为:"+guarantee_int);
					String cur_type = (String)bodyKColl.getDataValue("Ccy");
					//获取实时汇率
					KeyedCollection kCollRate = iqpservice.getHLByCurrType(cur_type, context, connection);
					if("failed".equals(kCollRate.getDataValue("flag"))){
						throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
					}
					/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
					if("IqpCreditChangeApp".equals(tableName) || "IqpGuarantChangeApp".equals(tableName)){
						exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
					}else{
						exchange_rate = securityExchangeRate;
					}
					/** modified by yangzy 2015/07/15 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
					amt = amt.multiply(exchange_rate);
					amt_all = amt_all.add(amt);
				}else{
					throw new Exception("保证金账号信息为空");
				}
			}catch(Exception e){
				throw new Exception("ESB通讯接口【获取保证金账户信息】交易失败："+e.getMessage());
			}
    	}
		/**实时查询保证金金额-----------------end-------------------------------------------*/
		return amt_all;
	}
	
}
