package com.yucheng.cmis.platform.riskmanage.interfaces.imple;

import java.util.HashMap;
import java.util.Iterator;
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

public class RiskManageIqpPromissoryImple implements RiskManageInterface{

	private final String modelIdIqp = "IqpLoanApp";//业务申请表
	private final String modelIdCtr = "CtrLoanCont";//合同表
	
	/*承诺函项下金额是否覆盖该笔业务金额
	 */
	public Map<String, String> getResultMap(String tableName, String serno,Context context,Connection connection) throws Exception {
		
		//分开事务，自己创建连接
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection conn = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		String returnFlag = "";
		String returnInfo = "";
		String is_promissory_note = "";//是否承诺函下
		String promissory_note = "";//承诺函
		BigDecimal total_amt = new BigDecimal("0");//授信占用
		BigDecimal loan_amt = new BigDecimal("0");//授信占用
		try {
			conn = this.getConnection(context, dataSource);
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail(tableName, serno, connection);
			IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
			
	    	is_promissory_note = (String)kColl.getDataValue("is_promissory_note");
            if("1".equals(is_promissory_note)){
            	//统计在途业务和生成合同的业务
            	promissory_note = (String)kColl.getDataValue("promissory_note");
            	KeyedCollection kCollCtrPromissory = dao.queryDetail(modelIdCtr, promissory_note, connection);
            	//获取实时汇率  start
        		String cont_cur_type = (String)kCollCtrPromissory.getDataValue("cont_cur_type");
        		KeyedCollection kCollRateSelect = cmisComponent.getHLByCurrType(cont_cur_type);
        		if("failed".equals(kCollRateSelect.getDataValue("flag"))){
					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
				}
				BigDecimal exchange_rate_select = BigDecimalUtil.replaceNull(kCollRateSelect.getDataValue("sld"));//汇率
				//获取实时汇率  end
            	loan_amt = BigDecimalUtil.replaceNull(kCollCtrPromissory.getDataValue("cont_amt")).multiply(exchange_rate_select);//承诺函金额
            	
            	String condition = "where promissory_note='"+promissory_note+"' and approve_status not in('997','998')";
            	String condtitionStr ="where promissory_note='"+promissory_note+"' and cont_status not in('700','800')";
            	IndexedCollection iCollIqp = dao.queryList(modelIdIqp, condition, connection);
            	IndexedCollection iCollCtr = dao.queryList(modelIdCtr, condtitionStr, connection);
            	//计算未生成合同部分,即申请时金额
            	for(Iterator iterator = iCollIqp.iterator(); iterator.hasNext();){
            		KeyedCollection kCollIqp = (KeyedCollection)iterator.next();
            		//获取实时汇率  start
            		String cur_type = (String)kCollIqp.getDataValue("apply_cur_type");
            		KeyedCollection kCollRate = cmisComponent.getHLByCurrType(cur_type);
            		if("failed".equals(kCollRate.getDataValue("flag"))){
    					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
    				}
    				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
    				//获取实时汇率  end
            		total_amt = total_amt.add(BigDecimalUtil.replaceNull(kCollIqp.getDataValue("apply_amount")).multiply(exchange_rate));
            	}
            	//计算有合同部分金额
            	for(Iterator iterator = iCollCtr.iterator();iterator.hasNext();){
            		KeyedCollection kCollCtr = (KeyedCollection)iterator.next();
            		//获取实时汇率  start
            		String cur_type = (String)kCollCtr.getDataValue("cont_cur_type");
            		KeyedCollection kCollRate = cmisComponent.getHLByCurrType(cur_type);
            		if("failed".equals(kCollRate.getDataValue("flag"))){
    					throw new Exception("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
    				}
    				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
    				//获取实时汇率  end
    				total_amt = total_amt.add(BigDecimalUtil.replaceNull(kCollCtr.getDataValue("cont_amt")).multiply(exchange_rate));
            	}
            	if(loan_amt.compareTo(total_amt)>=0){
            		returnFlag="通过";
                	returnInfo="承诺函项下金额能覆盖该笔业务金额!";
            	}else{
            		returnFlag="不通过";
                	returnInfo="承诺函项下金额不能覆盖该笔业务金额!";
            	}
            }else{
            	returnFlag="通过";
            	returnInfo="不适合此项检查!";
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
