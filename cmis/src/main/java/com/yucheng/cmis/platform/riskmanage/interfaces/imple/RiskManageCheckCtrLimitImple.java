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

public class RiskManageCheckCtrLimitImple implements RiskManageInterface{

	private final String modelId = "IqpLoanApp";//业务申请表
	private final String modelIdCtr = "CtrLoanCont";//合同主表
	private final String modelIdCtrLimit = "CtrLimitCont";//额度合同表
	
	/*检查额度合同项下支用时，申请金额是否超过额度合同金额
	 * 第一步，判断是否为额度合同项下支用
	 * 第二部，如果是，统计额度合同被占用金额（按照循环额度处理）
	 * 第三部，申请部分+合同部分
	 */
	public Map<String, String> getResultMap(String tableName, String serno,Context context,Connection connection) throws Exception {
		
		//分开事务，自己创建连接
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection conn = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		String returnFlag = "";
		String returnInfo = "";
		BigDecimal lmt_iqp_all = new BigDecimal(0);
		BigDecimal lmt_ctr_all = new BigDecimal(0);
		try {
			conn = this.getConnection(context, dataSource);
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			
			KeyedCollection kColl = dao.queryDetail(modelId, serno, connection);
			String is_limit_cont_pay = (String)kColl.getDataValue("is_limit_cont_pay");
			//是否额度合同项下支用
			if("1".equals(is_limit_cont_pay)){
				//额度合同编号
				String limit_cont_no = (String)kColl.getDataValue("limit_cont_no");
				String condition = "where limit_cont_no='"+limit_cont_no+"' and approve_status not in('997','998')";
			    IndexedCollection iqpiColl = dao.queryList(modelId, condition, connection);
			    //统计申请部分占用
			    for(int i=0;i<iqpiColl.size();i++){
			    	KeyedCollection iqpkColl = (KeyedCollection)iqpiColl.get(i);
			    	String allserno = (String)iqpkColl.getDataValue("serno");
			    	BigDecimal lmt_iqp = BigDecimalUtil.replaceNull(iqpLoanAppComponent.getLmtAmtBySerno(allserno));
			    	lmt_iqp_all = lmt_iqp_all.add(lmt_iqp);
			    }
			    String conditionCont = "where limit_cont_no='"+limit_cont_no+"'";
			    IndexedCollection ctriColl = dao.queryList(modelIdCtr, conditionCont, connection);
			    //统计合同部分占用
			    for(int i=0;i<ctriColl.size();i++){
			    	KeyedCollection ctrkColl = (KeyedCollection)ctriColl.get(i);
			    	String cont_no = (String)ctrkColl.getDataValue("cont_no");
			    	BigDecimal lmt_ctr = BigDecimalUtil.replaceNull(iqpLoanAppComponent.getLmtAmtByContNo(cont_no));
			    	lmt_ctr_all = lmt_ctr_all.add(lmt_ctr);
			    }
			    //查询此笔额度合同的合同金额
			    KeyedCollection ctrLimitkColl = dao.queryDetail(modelIdCtrLimit, limit_cont_no, connection);
			    String cur_type = (String)ctrLimitkColl.getDataValue("cur_type");
			    BigDecimal cont_amt = BigDecimalUtil.replaceNull(ctrLimitkColl.getDataValue("cont_amt"));
			    //获取实时汇率 
			    KeyedCollection kCollRate = service.getHLByCurrType(cur_type, context, connection);
				if("failed".equals(kCollRate.getDataValue("flag"))){
					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
				}
				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
				//合同金额*汇率 - 申请部分+合同部分 >= 0
			    if((cont_amt.multiply(exchange_rate)).compareTo((lmt_iqp_all.add(lmt_ctr_all)))>=0){
			    	returnFlag = "通过";
					returnInfo = "额度合同项下支用，额度合同金额能够覆盖申请金额";
			    }else{
			    	returnFlag = "不通过";
					returnInfo = "额度合同项下支用，额度合同金额不能覆盖申请金额";
			    }
			}else{
				returnFlag = "通过";
				returnInfo = "不适合此项检查";
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
