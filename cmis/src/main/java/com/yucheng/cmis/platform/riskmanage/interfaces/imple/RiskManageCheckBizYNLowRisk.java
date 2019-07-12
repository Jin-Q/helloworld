package com.yucheng.cmis.platform.riskmanage.interfaces.imple;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 
*@author lisj
*@time 2015-4-9
*@description TODO 需求编号：【XD150407025】分支机构授信审批权限配置
*				         检查授信申请是否属于低风险授权业务
*@version v1.0
*
 */
public class RiskManageCheckBizYNLowRisk implements RiskManageInterface {
	private final String modelId = "RLmtAppGuarCont";//授信申请和担保合同关系表
	private static final Logger logger = Logger.getLogger(RiskManageIqpComFinImple.class);
	
	public Map<String, String> getResultMap(String tableName, String serno, Context context, Connection connection) throws Exception {
		//分开事务，自己创建连接
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection conn = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		String returnFlag = "";
		String returnInfo = "";
		try {
			 conn = this.getConnection(context, dataSource);
			 TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			 KeyedCollection kColl4LmtApp = dao.queryDetail(tableName, serno, conn);
			 if(kColl4LmtApp.containsKey("lrisk_type") && (kColl4LmtApp.getDataValue("lrisk_type"))!=null){
				 String lrisk_type = kColl4LmtApp.getDataValue("lrisk_type").toString().trim();
				 /** modified by lisj 2015-10-29 需求编号：XD150930072 分支机构部分授权权限调整，变更低风险业务授权使用范围 begin **/
				 //10为低风险，20为非低风险
				 if("10".equals(lrisk_type)){
					 /**修复低风险做授信变未过滤已解除旧担保合同关联关系BUG，于2015-6-25上线 begin**/
					 IndexedCollection iColl4RLmtGuar = dao.queryList(modelId, "where serno ='"+serno+"' and corre_rel not in ('3','5')", conn);
					 /**修复低风险做授信变未过滤已解除旧担保合同关联关系BUG，于2015-6-25上线 end**/
					 if(iColl4RLmtGuar!=null && iColl4RLmtGuar.size()>0){
						 String gcn = "";//不合规担保合同编号字符串
						 for(Iterator<KeyedCollection> iterator =iColl4RLmtGuar.iterator();iterator.hasNext();){
							 KeyedCollection temp = (KeyedCollection)iterator.next();
							 String guar_cont_no  = (String) temp.getDataValue("guar_cont_no");//授信分项中担保合同编号
							BigDecimal count = (BigDecimal) SqlClient.queryFirst("checkBizYNLowRisk", guar_cont_no, null, conn);
							//不属于低风险授权
							if(count.compareTo(BigDecimal.ZERO) <= 0 ){
								gcn = gcn+guar_cont_no+";";	
							}
						 }
						 if(!"".equals(gcn)){
							 returnFlag ="不通过";
							 returnInfo = "低风险授权仅适用于存单质押（利息前置除外）、国债质押、银行承兑汇票和我行发行的保本型理财产品质押（未来利息收益前置）的授信业务，"
									       +"该授信业务下的授信分项中担保合同编号为【"+gcn+"】不合规，检查不通过";
						 }
					 }else{
						 returnFlag = "不通过";
						 returnInfo = "该授信申请的【授信分项】不存在【担保合同信息】,请检查！";
					 } 
					 if("".equals(returnFlag)){
						 returnFlag = "通过";
						 returnInfo = "低风险授权仅适用于存单质押（利息前置除外）、国债质押、银行承兑汇票和我行发行的保本型理财产品质押（未来利息收益前置）的授信业务,该授信申请检查通过";
					 }
				 }	 
			 }
			 /** modified by lisj 2015-10-29 需求编号：XD150930072 分支机构部分授权权限调整，变更低风险业务授权使用范围 end **/
			 if("".equals(returnFlag)){
				 returnFlag = "通过";
				 returnInfo = "该授信申请不属于该项风险拦截检查范围，通过";
			 }
			 	returnMap.put("OUT_是否通过", returnFlag);
				returnMap.put("OUT_提示信息", returnInfo);
				
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "检查授信申请是否属于低风险授权业务检查失败！"+e.getMessage(), null);
			logger.error("检查授信申请是否属于低风险授权业务检查失败！"+e.getStackTrace());
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
