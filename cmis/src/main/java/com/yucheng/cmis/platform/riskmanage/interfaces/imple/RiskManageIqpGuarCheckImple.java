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
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class RiskManageIqpGuarCheckImple implements RiskManageInterface{

	/*贷款申请 担保合同金额能否覆盖敞口金额
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
	    	IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
	    	
	    	KeyedCollection kColl = dao.queryDetail(tableName, serno, connection);
			String prd_id = (String)kColl.getDataValue("prd_id");

			if("400022".equals(prd_id) || "400023".equals(prd_id) || "400024".equals(prd_id) || "300021".equals(prd_id)){
				returnFlag = "通过";
				returnInfo = "不适合此项检查";	
			}else{
				String assure_main = (String)kColl.getDataValue("assure_main");//担保方式
				//'100':'抵押', '200':'质押', '210':'准全额质押', '220':'低风险质押', '300':'保证', '400':'信用', '500':'100%保证金', '510':'准全额保证金'
				if("100".equals(assure_main) || "200".equals(assure_main) || "210".equals(assure_main) || "220".equals(assure_main) || "300".equals(assure_main)){
					String sql = "select nvl(sum(guar_amt),0) as amt from grt_loan_r_gur where serno ='"+serno+"' and is_add_guar<>'1'";
					IndexedCollection res_value = TableModelUtil.buildPageData(null, dataSource, sql);
					if(res_value.size()>0){
						BigDecimal risk_open_amt = null;
						KeyedCollection kCollValue = (KeyedCollection)res_value.get(0);
						BigDecimal amt = BigDecimalUtil.replaceNull(kCollValue.getDataValue("amt"));
						// 计算风险敞口
						risk_open_amt = iqpLoanAppComponent.getLmtAmtBySerno(serno);
						
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
