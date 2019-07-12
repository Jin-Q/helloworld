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
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class RiskManageIqpChangeSameBackImple implements RiskManageInterface{

	private final String modelIdCtr = "CtrLoanCont";//合同表
	private final String modelIdLmtAcc = "LmtIntbankAcc";
	private final String modelIdRBusLmt = "RBusLmtcreditInfo";
	
	/*信用证/保函修改校验 同业授信额度及限额检查
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
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			KeyedCollection kColl = dao.queryDetail(tableName, serno, connection);
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
	    	IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
	    	String cont_no = (String)kColl.getDataValue("cont_no");
	    	KeyedCollection kCollCtr = dao.queryDetail(modelIdCtr, cont_no, connection);
	    	//修改后业务风险敞口
			BigDecimal lmt_amt_new = iqpLoanAppComponent.getLmtAmtBySerno4IqpChange(tableName, serno);
			//查询前业务的授信占用
			BigDecimal lmt_amt_old = new BigDecimal(0);
			LmtServiceInterface lmtServiceInterface = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
			String agr_no = (String)kCollCtr.getDataValue("limit_acc_no");
			if(agr_no == null || "".equals(agr_no)){
				String aaa = "'"+agr_no+"'";
				IndexedCollection lmtIColl = lmtServiceInterface.queryLmtAgrDetailsByLimitCodeStr(aaa, null, dataSource);
				String limit_type = "";
				if(lmtIColl.size()>0){
					KeyedCollection lmtKColl = (KeyedCollection) lmtIColl.get(0);
					limit_type = (String) lmtKColl.getDataValue("limit_type");
				}
				if("01".equals(limit_type)){
					lmt_amt_old = iqpLoanAppComponent.getLmtAmtByContNo(cont_no);//查询该合同下的授信占用，并循环累加（循环额度方式）
				}else{
					lmt_amt_old = iqpLoanAppComponent.getOneLmtAmtByContNo(cont_no);//查询该合同下的授信占用，并循环累加（一次性额度方式）
				}
			}
	    	String condition = "where cont_no='"+cont_no+"'";
	    	KeyedCollection kCollRBusLmt = dao.queryFirst(modelIdRBusLmt,null, condition, connection);
    		limit_credit_no = (String)kCollRBusLmt.getDataValue("agr_no");//第三方授信编号
	    	//第三方授信编号如果为空，则不进行检查 
	    	if(!"".equals(limit_credit_no) && limit_credit_no != null){
	    		//查询同业授信台账
	    		IndexedCollection iCollLmtAcc = dao.queryList(modelIdLmtAcc, "where agr_no='"+limit_credit_no+"'", connection);
	    		if(iCollLmtAcc.size()>0){
	    			KeyedCollection kCollRes = serviceIqp.getLmtAmtByArgNo(limit_credit_no, "02", connection, context);
					BigDecimal lmt_amt = BigDecimalUtil.replaceNull(kCollRes.getDataValue("lmt_amt"));
					lmt_amt = lmt_amt.subtract(lmt_amt_old).add(lmt_amt_new);
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
