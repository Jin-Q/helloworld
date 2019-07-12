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
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.prd.domain.PrdBasicinfo;
import com.yucheng.cmis.biz01line.prd.msi.PrdServiceInterface;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class RiskManageIqpCusHalfEqualImple implements RiskManageInterface{

	private final String modelId = "IqpLoanApp";//业务申请表
	private final String modelIdSub = "IqpLoanAppSub";//业务申请表从表
	/*客户及其配偶的半年日均合计检查
	 */
	public Map<String, String> getResultMap(String tableName, String serno,Context context,Connection connection) throws Exception {
		
		//分开事务，自己创建连接
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection conn = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		String returnFlag = "";
		String returnInfo = "";
		String esbFlag = "";
		String esbInfo = "";
		
		try {
			conn = this.getConnection(context, dataSource);
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			
			KeyedCollection kColl = dao.queryDetail(modelId, serno, conn);
			KeyedCollection kCollSub = dao.queryDetail(modelIdSub, serno, conn);
			String prd_id = (String)kColl.getDataValue("prd_id");
			String cus_id = (String)kColl.getDataValue("cus_id");
			String apply_cur_type = (String)kColl.getDataValue("apply_cur_type");
			BigDecimal apply_amount = BigDecimalUtil.replaceNull(kColl.getDataValue("apply_amount"));
			
			String is_close_loan = (String)kCollSub.getDataValue("is_close_loan");
			
			/**个人客户查询半年日均 -----start-----*/
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			PrdServiceInterface service = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
			ESBServiceInterface serviceEsb = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			
			//获取实时汇率  start
			KeyedCollection kCollRate = serviceIqp.getHLByCurrType(apply_cur_type, context, connection);
			if("failed".equals(kCollRate.getDataValue("flag"))){
				throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
			}
			BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
			//获取实时汇率  end
			
			PrdBasicinfo prdBasicinfo = service.getPrdBasicinfoList(prd_id, connection);
			String supcatalog = (String)prdBasicinfo.getSupcatalog();
			if("PRD20120802659".equals(supcatalog) && "1".equals(is_close_loan)){//如果是个人经营性贷款
				String spouse_cus_id ="";
				IndexedCollection iCollCus = serviceCus.getIndivSocRel(cus_id, "1", connection);
				if(iCollCus.size()>0){
                   KeyedCollection kCollCus = (KeyedCollection)iCollCus.get(0);
                   spouse_cus_id = (String)kCollCus.getDataValue("cus_id_rel");
				}
				/*** 调用核心实时接口半年日均 ***/
				KeyedCollection retKColl = null;
				KeyedCollection BODY = new KeyedCollection("BODY");
				try{
					retKColl = serviceEsb.tradeBNRJ(cus_id, spouse_cus_id, context, connection);
					if(TagUtil.haveSuccess(retKColl, context)){//成功
						BODY = (KeyedCollection)retKColl.getDataElement("BODY");
						BigDecimal day_eql_bal = BigDecimalUtil.replaceNull(BODY.getDataValue("DAY_EQL_BAL"));
						BigDecimal mate_day_eql_bal = BigDecimalUtil.replaceNull(BODY.getDataValue("MATE_DAY_EQL_BAL"));
					    BigDecimal totalAmt = day_eql_bal.add(mate_day_eql_bal);
					    BigDecimal rate = totalAmt.divide(apply_amount.multiply(exchange_rate),6,BigDecimal.ROUND_HALF_EVEN);
					    if(rate.compareTo(new BigDecimal(0.1))>=0){
					    	returnFlag ="通过";
							returnInfo ="客户半年日均检查通过!";
					    }else{
					    	returnFlag ="不通过";
							returnInfo ="客户及其配偶的半年日均合计占贷款金额占比低于10%";
					    }
					}else{
						esbFlag = "不通过";
						esbInfo = "ESB通讯接口【获取保证金账户信息】交易失败："+(String)retKColl.getDataValue("RET_MSG");
					}
				}catch(Exception e){
					esbFlag = "不通过";
					esbInfo = "ESB通讯接口【获取半年日均】交易失败："+e.getMessage();
				}
				if(esbFlag.equals("不通过")){
					returnMap.put("OUT_是否通过", esbFlag);
					returnMap.put("OUT_提示信息", esbInfo);
					return returnMap;
				}
			}else{
				returnFlag ="通过";
				returnInfo ="不适合此项查询";
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
