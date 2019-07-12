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
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class RiskManageIqpGuarChangeImple implements RiskManageInterface{

	/*担保变更 担保合同金额能否覆盖敞口金额
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
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
	    	IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
	    	IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
	    	
	    	KeyedCollection kColl = dao.queryDetail(tableName, serno, connection);
			String prd_id = (String)kColl.getDataValue("prd_id");
			String cont_no = (String)kColl.getDataValue("cont_no");
			KeyedCollection kCollCtr = (KeyedCollection)dao.queryDetail("CtrLoanCont", cont_no, connection);
			String oldSerno = (String)kCollCtr.getDataValue("serno");
			
			if("400022".equals(prd_id) || "400023".equals(prd_id) || "400024".equals(prd_id) || "300021".equals(prd_id)){
				returnFlag = "通过";
				returnInfo = "不适合此项检查";
			}else{
				String new_assure_main = (String)kColl.getDataValue("new_assure_main");//修改后担保方式
				//'100':'抵押', '200':'质押', '210':'准全额质押', '220':'低风险质押', '300':'保证', '400':'信用', '500':'100%保证金', '510':'准全额保证金'
				if("100".equals(new_assure_main) || "200".equals(new_assure_main) || "210".equals(new_assure_main) || "220".equals(new_assure_main) || "300".equals(new_assure_main)){
					String sql = "select nvl(sum(guar_amt),0) as amt from grt_loan_r_gur where serno ='"+serno+"' and is_add_guar<>'1'  and corre_rel<>'3'";
					IndexedCollection res_value = TableModelUtil.buildPageData(null, dataSource, sql);
					if(res_value.size()>0){
						BigDecimal risk_open_amt = null;
						KeyedCollection kCollValue = (KeyedCollection)res_value.get(0);
						BigDecimal amt = BigDecimalUtil.replaceNull(kCollValue.getDataValue("amt"));
						// 计算风险敞口
						BigDecimal cont_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("cont_amt"));//合同金额
						BigDecimal security_rate = BigDecimalUtil.replaceNull(kColl.getDataValue("security_rate"));//保证金比例
						String cont_cur_type = (String)kColl.getDataValue("cont_cur_type");//币种
						//获取实时汇率  start
						/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 start **/
						//String security_cur_type = (String) kCollCtr.getDataValue("security_cur_type");//保证金币种
						//KeyedCollection kCollRate = serviceIqp.getHLByCurrType(cont_cur_type, context, connection);
						//KeyedCollection kCollRateSecurity = serviceIqp.getHLByCurrType(security_cur_type, context, connection);
						//if("failed".equals(kCollRate.getDataValue("flag"))){
						//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
						//}
						//if("failed".equals(kCollRateSecurity.getDataValue("flag"))){
						//	throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
						//}
						//BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
						//BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollRateSecurity.getDataValue("sld"));//保证金币种汇率
						BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollCtr.getDataValue("exchange_rate"));//汇率
						BigDecimal exchange_rate_security = BigDecimalUtil.replaceNull(kCollCtr.getDataValue("security_exchange_rate"));//保证金币种汇率
						/** modified by yangzy 2015/07/16 需求：XD150407026， 存量外币业务取当时时点汇率 end **/
						
						//获取实时汇率  end
						//risk_open_amt = ((new_cont_amt.multiply(new BigDecimal(1).subtract(new_security_rate))).multiply(exchange_rate));
						BigDecimal securityAmt = cont_amt.multiply(security_rate).multiply(exchange_rate).divide(exchange_rate_security,2,BigDecimal.ROUND_HALF_EVEN);
						java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
						nf.setGroupingUsed(false);
						String caculateAmt = String.valueOf(securityAmt);
						securityAmt = BigDecimalUtil.replaceNull(Math.ceil(Double.valueOf(caculateAmt)/100)*100);
						String changeAmt = nf.format(securityAmt);
						securityAmt = BigDecimalUtil.replaceNull(changeAmt);
						risk_open_amt = ((cont_amt.multiply(exchange_rate).subtract(securityAmt.multiply(exchange_rate_security))));
						risk_open_amt = iqpLoanAppComponent.caculateLimitSpac(oldSerno, risk_open_amt);
						
						risk_open_amt = risk_open_amt.setScale(2, BigDecimal.ROUND_HALF_UP);
						if(amt.compareTo(risk_open_amt)>=0){
							returnFlag = "通过";
							returnInfo = "本次担保金额可覆盖敞口金额";
						}else{
							returnFlag = "不通过";
							returnInfo = "本次担保合同金额不能覆盖风险敞口金额";
						}
					}else{
						returnFlag = "不通过";
						returnInfo = "本次担保合同金额为空";
					}
				}else{
					returnFlag = "通过";
					returnInfo = "此担保方式无需担保合同";
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
