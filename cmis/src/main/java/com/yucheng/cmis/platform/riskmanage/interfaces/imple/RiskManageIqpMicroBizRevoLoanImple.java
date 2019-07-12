package com.yucheng.cmis.platform.riskmanage.interfaces.imple;

import java.util.HashMap;
import java.util.Map;
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
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
/**
 * 
*@author lisj
*@time 2015-5-27
*@description TODO 需求编号：【XD150123005】小微自助循环贷款改造
*@version v1.0
*
 */
public class RiskManageIqpMicroBizRevoLoanImple implements RiskManageInterface{

	private final String LADModelId = "LmtAgrDetails";//授信额度台账表
	private final String modelId = "IqpLoanApp";//业务申请表
	/*小微自助循环贷款 ：有且只能有一个有效的
	 * 检查授信分项是否存在多个品种为小微自助循环贷款的分项
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
			IndexedCollection iColl = dao.queryList(modelId, " where serno ='" + serno+"' and prd_id='100088' ", connection);
			if(iColl!=null&&iColl.size()>0){
				KeyedCollection kColl=(KeyedCollection) iColl.get(0);
				String limit_acc_no=(String) kColl.getDataValue("limit_acc_no");//授信台帐编号
				String cus_id = (String) kColl.getDataValue("cus_id");
				IndexedCollection iColl4LAD = dao.queryList(LADModelId, " where limit_code ='" + limit_acc_no+"' and limit_name='100088' ", connection);
				if(iColl4LAD!=null&&iColl4LAD.size()>0){
					//returnFlag = "通过";
					//returnInfo = "关联授信的额度品种是小微自助循环贷,检查通过！";
					//查询该客户是否存在【审批中】状态的业务申请信息以及【未生效】/【已生效】合同信息
					IndexedCollection iColl4ILA = dao.queryList(modelId, " where serno <>'"+serno+"' and cus_id='"+cus_id
							                        +"' and prd_id='100088' and approve_status ='111'", connection);
					IndexedCollection iColl4CLC = dao.queryList("CtrLoanCont", " where cus_id='"+cus_id
							                         +"' and prd_id='100088' and cont_status in('100','200')", connection);
					if(iColl4ILA!=null && iColl4ILA.size()>0){
						returnFlag = "不通过";
						returnInfo = "该客户已存在审批中或通过状态的小微自助循环贷款，不允许重复申请！";
					}else{
						if(iColl4CLC!=null && iColl4CLC.size()>0){
							returnFlag = "不通过";
							returnInfo = "该客户已存在未生效或生效状态的小微自助循环贷款合同信息！";
						}
					}
				 }else{
					 returnFlag = "不通过";
					 returnInfo = "业务申请未关联小微自助循环贷额度！";
				 }
			}else{
				returnFlag = "通过";
				returnInfo = "业务申请品种不属于小微自助循环贷,无需检查！";
			}
			if("".equals(returnFlag)){
				returnFlag = "通过";
				returnInfo = "小微自助循环贷款业务申请合规,检查通过！";
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
