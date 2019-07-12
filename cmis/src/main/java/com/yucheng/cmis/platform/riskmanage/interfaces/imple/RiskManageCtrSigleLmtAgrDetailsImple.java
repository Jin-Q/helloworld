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
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 
*@author wangj
*@time 2015-5-07
*@description TODO 需求编号：【XD141222087】法人账户透支需求变更
*@version v1.0
*
 */
public class RiskManageCtrSigleLmtAgrDetailsImple implements RiskManageInterface{

	private final String LmtAgrDetailsModelId = "LmtAgrDetails";//授信额度台账表
	private final String modelId = "IqpLoanApp";//业务申请表
	  private final String sqlId="queryCtrloanContexistbyCusId";
	/*单一法人授信法人账户透支 ：有且只能有一个有效的
	 * 检查是否存在多个授信品种为法人账户透支的授信分项
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
			String condition=" where serno ='" + serno+"' and prd_id='100051' ";
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			if(iColl!=null&&iColl.size()>0){
				/* 需求编号：XD150825064_源泉宝法人账户透支改造  begin */
				KeyedCollection subKColl = dao.queryDetail("IqpLoanAppSub", serno, connection);
				String belgline=TagUtil.replaceNull4String(subKColl.getDataValue("belg_line"));
				String repayType=TagUtil.replaceNull4String(subKColl.getDataValue("repay_type"));
				//还款方式检查
				//repayType A005 利随本清  A004按期结息
				if("BL100".equals(belgline)){
					if(!"A004".equals(repayType)){
						returnFlag = "不通过";
						returnInfo = "业务条线为公司业务条线时还款方式必须为按期结息,请检查！";
					}
				}else if("BL300".equals(belgline)){
					if(!"A005".equals(repayType)){
						 returnFlag = "不通过";
						 returnInfo = "业务条线为个人业务条线时还款方式必须为利随本清,请检查！";
					}
				}else{
					 returnFlag = "不通过";
					 returnInfo = "业务条线录入出错,请检查！";
				}
				//费用检查
				if("".equals(returnFlag)&&"BL100".equals(belgline)){
					IndexedCollection iatKColl=dao.queryList("IqpAppendTerms", " where serno ='" + serno+"' and fee_code='23' ", connection);
					if(iatKColl==null||iatKColl.isEmpty()){
						 returnFlag = "不通过";
						 returnInfo = "业务条线为公司业务条线时,在[附加条款]必须录入费用信息,请检查！";
					}
				}
				//费用检查
				if("".equals(returnFlag)&&"BL300".equals(belgline)){
					IndexedCollection iatKColl=dao.queryList("IqpAppendTerms", " where serno ='" + serno+"' ", connection);
					if(iatKColl!=null&&iatKColl.size()>0){
						 returnFlag = "不通过";
						 returnInfo = "业务条线为个人业务条线时不收取年费、手续费，在[附加条款]不能录入费用信息,请检查！";
					}
				}
				if("".equals(returnFlag)){
					KeyedCollection kColl=(KeyedCollection) iColl.get(0);
					String cusId=(String) kColl.getDataValue("cus_id");//客户号
					String limit_acc_no=(String) kColl.getDataValue("limit_acc_no");//授信台帐编号
					condition=" where limit_code ='" + limit_acc_no+"' and Limit_Name='100051' ";
					IndexedCollection iColl2 = dao.queryList(LmtAgrDetailsModelId, condition, connection);
					if(iColl2!=null&&iColl2.size()>0){
						returnFlag = "通过";
						returnInfo = "关联授信的额度品种是法人账户透支,检查通过！";
						IndexedCollection iCollResult = SqlClient.queryList4IColl(sqlId,cusId,conn);
						if (iCollResult != null&&iCollResult.size()>0 ) {
							 returnFlag = "不通过";
							 returnInfo = "该客户存在未生效/生效/审批中的法人账户透支合同,请检查！";
						}
					 }else{
						 returnFlag = "不通过";
						 returnInfo = "关联授信的额度品种不是法人账户透支,请检查！";
					 }
				}
				/* 需求编号：XD150825064_源泉宝法人账户透支改造  end */
			}else{
				returnFlag = "通过";
				returnInfo = "该业务不属于法人账户透支业务,检查通过！";
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
