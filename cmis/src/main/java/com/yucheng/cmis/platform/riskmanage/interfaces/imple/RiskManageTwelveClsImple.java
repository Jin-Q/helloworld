package com.yucheng.cmis.platform.riskmanage.interfaces.imple;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

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
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * 调用12级接口查询客户经理12级分类任务
 * @author QZCB
 *
 */
public class RiskManageTwelveClsImple implements RiskManageInterface{

	//private final String modelId = "CusManager";//客户经理表
	
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
			String manager_id = null;
			
			//业务申请，出账，责任人从客户经理绩效表中取主管客户经理(特殊)
			//其他的从主表中取
			if("IqpLoanApp".equals(tableName)||"IqpRpddscnt".equals(tableName)||"IqpAssetstrsf".equals(tableName)||"IqpAssetTransApp".equals(tableName)||"IqpAssetProApp".equals(tableName)){
				//取主管客户经理Id 
				String condition = "where is_main_manager='1' and serno='"+serno+"'";
				IndexedCollection iqpIColl = dao.queryList("CusManager", condition, conn);
				if(iqpIColl.size()>0){
					KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
					manager_id = (String)iqpKColl.getDataValue("manager_id");//取得责任人
				}
			}else if("CtrLimitApp".equals(tableName)){
				IndexedCollection ic =  dao.queryList("CusManager", "where is_main_manager='1' and serno='"+serno+"'", conn);
				if(ic != null && ic.size() > 0){
					KeyedCollection kc = (KeyedCollection)ic.get(0);
					manager_id = (String)kc.getDataValue("manager_id");//取得责任人
				}else {
					KeyedCollection kc = dao.queryDetail("CtrLimitApp", serno, conn);
					String cont_no = (String)kc.getDataValue("cont_no");
					KeyedCollection mKc = dao.queryFirst("CusManager", null, "where is_main_manager='1' and cont_no='"+cont_no+"'", conn);
					manager_id = (String)mKc.getDataValue("manager_id");//取得责任人
				}
			}else if("PvpLoanApp".equals(tableName)){ 
				//通过出账主键取合同号，再通过合同号取业务申请流水号
				KeyedCollection pvpKColl = dao.queryDetail(tableName, serno, conn);
				String cont_no = (String)pvpKColl.getDataValue("cont_no");
				String prd_id = (String) pvpKColl.getDataValue("prd_id");
				KeyedCollection ctrKColl = null;
				if("300024".equals(prd_id)||"300023".equals(prd_id)||"300022".equals(prd_id)){
					ctrKColl = (KeyedCollection)dao.queryDetail("CtrRpddscntCont", cont_no, conn);
				}else if("600020".equals(prd_id)){ 
					ctrKColl = (KeyedCollection)dao.queryDetail("CtrAssetstrsfCont", cont_no, conn);
				}else if("600021".equals(prd_id)){ 
					ctrKColl = (KeyedCollection)dao.queryDetail("CtrAssetTransCont", cont_no, conn);
				}else if("600022".equals(prd_id)){ 
					ctrKColl = (KeyedCollection)dao.queryDetail("CtrAssetProCont", cont_no, conn);
				}else{
					ctrKColl = (KeyedCollection)dao.queryDetail("CtrLoanCont", cont_no, conn);
				}
				String serno_select = (String)ctrKColl.getDataValue("serno");
				String condition = "where is_main_manager='1' and serno='"+serno_select+"'";  
				IndexedCollection iqpIColl = dao.queryList("CusManager", condition, conn);
				KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
				manager_id = (String)iqpKColl.getDataValue("manager_id");//取得责任人 
			}else if("IqpCreditChangeApp".equals(tableName) || "IqpGuarantChangeApp".equals(tableName) || "IqpGuarChangeApp".equals(tableName)){//信用证变更时取原业务的主管客户经理
				//取主管客户经理Id
				KeyedCollection kCollCrdChange = dao.queryDetail(tableName, serno, conn);
				String cont_no = (String)kCollCrdChange.getDataValue("cont_no");
				String condition = "where is_main_manager='1' and cont_no='"+cont_no+"'"; 
				IndexedCollection iqpIColl = dao.queryList("CusManager", condition, conn);
				if(iqpIColl.size()>0){
					KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
					manager_id = (String)iqpKColl.getDataValue("manager_id");//取得责任人
				}   
			}else if("IqpAverageAssetApp".equals(tableName)){//资产登记
				//取主管客户经理Id
				KeyedCollection kCollCrdChange = dao.queryDetail(tableName, serno, conn);
				String cont_no = (String)kCollCrdChange.getDataValue("cont_no");
				String condition = "where is_main_manager='1' and cont_no='"+cont_no+"'"; 
				IndexedCollection iqpIColl = dao.queryList("CusManager", condition, conn);
				if(iqpIColl.size()>0){
					KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
					manager_id = (String)iqpKColl.getDataValue("manager_id");//取得责任人
				}else{
					throw new Exception("获取不到主管客户经理！");
				}    
			}else if("IqpRateChangeApp".equals(tableName)){	//利率调整 取责任人
				KeyedCollection kCollRateChange = dao.queryDetail(tableName, serno, conn);
				String bill_no = (String)kCollRateChange.getDataValue("bill_no");
				if(bill_no!=null && !"".equals(bill_no)){
					KeyedCollection kCollAcc = dao.queryDetail("AccLoan", bill_no, conn);
					String cont_no = (String)kCollAcc.getDataValue("cont_no");
					String condition = "where is_main_manager='1' and cont_no='"+cont_no+"'";
					IndexedCollection iqpIColl = dao.queryList("CusManager", condition, conn);
					if(iqpIColl.size()>0){
						KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
						manager_id = (String)iqpKColl.getDataValue("manager_id");//取得责任人
					} 
				}
			}else if("PspAppCusVisit".equals(tableName)){//客户走访没有责任人责任机构，取登记人登记机构
				KeyedCollection kCollTmp = dao.queryDetail(tableName, serno, conn);
				manager_id = (String)kCollTmp.getDataValue("input_id");//取登记人
			}else if("ArpBadassetHandoverApp".equals(tableName)){//不良资产移交申请流程，取原主管客户经理
				KeyedCollection kCollTmp = dao.queryDetail(tableName, serno, conn);
				manager_id = (String)kCollTmp.getDataValue("fount_manager_id");//取原主管客户经理
			}else if("CusTrusteeApp".equals(tableName)){//托管申请流程
				KeyedCollection kCollTmp = dao.queryDetail(tableName, serno, conn);
				manager_id = (String)kCollTmp.getDataValue("input_id");//取得责任人
			}else if("IqpAssetRegiApp".equals(tableName)){//信贷资产登记
				//取主管客户经理Id
				KeyedCollection kCollCrdChange = dao.queryDetail(tableName, serno, conn);
				String cont_no = (String)kCollCrdChange.getDataValue("cont_no");
				String condition = "where is_main_manager='1' and cont_no='"+cont_no+"'"; 
				IndexedCollection iqpIColl = dao.queryList("CusManager", condition, conn);
				if(iqpIColl.size()>0){
					KeyedCollection iqpKColl = (KeyedCollection)iqpIColl.get(0);
					manager_id = (String)iqpKColl.getDataValue("manager_id");//取得责任人
				}else{
					throw new Exception("获取不到主管客户经理！");
				}   
			}else{
				KeyedCollection kCollTmp = dao.queryDetail(tableName, serno, conn);
				manager_id = (String)kCollTmp.getDataValue("manager_id");//取得责任人
			}
			
			if(manager_id==null || "".equals(manager_id)){
				throw new EMPException("根据流水号【"+serno+"】查询业务对应主管客户经理为空！");
			}
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface serviceEsb = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			/*** 调用12级接口查询12级分类任务 ***/
			KeyedCollection retKColl = null;
			KeyedCollection BODY = new KeyedCollection("BODY");
			try{
				retKColl = serviceEsb.tradeSEJFLRW(manager_id, "all", "all", context, connection);
				if(TagUtil.haveSuccess(retKColl, context)){//成功
					BODY = (KeyedCollection)retKColl.getDataElement("BODY");
					String flag = (String)BODY.getDataValue("FLAG");
				    if("1".equals(flag)){
				    	returnFlag = "不通过";
						returnInfo = (String)BODY.getDataValue("MSG");//12级返回信息
				    }else{
				    	returnFlag = "通过";
						returnInfo = "该客户经理12级分类任务已完成！";
				    }
				}else{
					esbFlag = "不通过";
					esbInfo = "ESB通讯接口【获取12级分类任务是否完成】交易失败："+(String)retKColl.getDataValue("RET_MSG");
				}
			}catch(Exception e){
				esbFlag = "不通过";
				esbInfo = "ESB通讯接口【获取12级分类任务是否完成】交易失败："+e.getMessage();
			}
			if(esbFlag.equals("不通过")){
				returnMap.put("OUT_是否通过", esbFlag);
				returnMap.put("OUT_提示信息", esbInfo);
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
