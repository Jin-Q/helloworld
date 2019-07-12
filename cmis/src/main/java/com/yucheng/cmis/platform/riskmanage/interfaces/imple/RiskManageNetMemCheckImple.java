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

public class RiskManageNetMemCheckImple implements RiskManageInterface{

	private final String modelId = "IqpLoanApp";//业务申请表
	private final String modelIdMemMana = "IqpMemMana";//网络下成员表
	private final String modelIdNetMagInfo = "IqpNetMagInfo";//网络管理信息
	
	/*
	 * 同业授信金额 <= 授信总额。
	 */
	public Map<String, String> getResultMap(String tableName, String serno,Context context,Connection connection) throws Exception {
		
		//分开事务，自己创建连接
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection conn = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		String returnFlag = "";
		String returnInfo = "";
		BigDecimal batair_lmt_amt = new BigDecimal(0);
		BigDecimal lmt_amt_all = new BigDecimal(0);
		BigDecimal lmt_quota = new BigDecimal(0);
		try {
			conn = this.getConnection(context, dataSource);
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			KeyedCollection iqpKColl = dao.queryDetail(tableName, serno, connection);
			String biz_type = (String)iqpKColl.getDataValue("biz_type");
			String belong_net = (String)iqpKColl.getDataValue("belong_net");
			if("7".equals(biz_type) || "8".equals(biz_type)){
				returnMap.put("OUT_是否通过", "通过");
				returnMap.put("OUT_提示信息", "非供应链下业务，不适合此项检查");
				return returnMap;
			}
			
			String cus_id = (String)iqpKColl.getDataValue("cus_id");
			//第一步，判断该客户是否属于网络下的成员
		    String condition ="where mem_cus_id='"+cus_id+"' and net_agr_no='"+belong_net+"'";
			IndexedCollection memIColl = dao.queryList(modelIdMemMana, condition, connection);
			if(memIColl.size()>0){
			//第二步，如果是网络中成员，则查询网络信息(网络间接授信)
				KeyedCollection memkColl = (KeyedCollection)memIColl.get(0);
				String net_agr_no = (String)memkColl.getDataValue("net_agr_no");
				if(!"".equals(net_agr_no)&& net_agr_no!=null){
					KeyedCollection netkColl = dao.queryDetail(modelIdNetMagInfo, net_agr_no, connection);
					batair_lmt_amt = BigDecimalUtil.replaceNull(netkColl.getDataValue("batair_lmt_amt"));//网络间接授信
					//查询网络下成员
					String conditionStr = "where net_agr_no='"+net_agr_no+"'";
					IndexedCollection netMemiColl = dao.queryList(modelIdMemMana, conditionStr, connection);
					for(int i=0;i<netMemiColl.size();i++){
						KeyedCollection netMemkColl = (KeyedCollection)netMemiColl.get(i);
						String memCusId = (String)netMemkColl.getDataValue("mem_cus_id");
						BigDecimal lmt_amt = BigDecimalUtil.replaceNull(iqpLoanAppComponent.getRiskOpenAmtByCusId(memCusId,belong_net, context, connection));
						//成员授信限额
						if(cus_id.equals(memCusId)){
						   lmt_quota = BigDecimalUtil.replaceNull(netMemkColl.getDataValue("lmt_quota"));
						   if(lmt_quota.compareTo(lmt_amt)<0){
							   returnFlag ="不通过";
							   returnInfo ="网络中设定成员授信限额不能覆盖该成员风险敞口金额";
							   returnMap.put("OUT_是否通过", returnFlag);
							   returnMap.put("OUT_提示信息", returnInfo);
							   return returnMap;
						   }
						}
						
						lmt_amt_all = lmt_amt_all.add(lmt_amt);
					}
				}
				if(batair_lmt_amt.compareTo(lmt_amt_all)>=0){
					returnFlag ="通过";
					returnInfo ="网络信息中设定核心企业间接额度可以覆盖成员风险敞口总额";
				}else{
					returnFlag ="不通过";
					returnInfo ="网络信息中设定核心企业间接额度不能覆盖成员风险敞口总额";
				}
			}else{
				returnMap.put("OUT_是否通过", "通过");
				returnMap.put("OUT_提示信息", "不适合此项检查");
				return returnMap;
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
