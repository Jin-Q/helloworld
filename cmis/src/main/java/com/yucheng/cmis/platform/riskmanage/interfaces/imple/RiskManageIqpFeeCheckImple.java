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
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.TreeDicTools;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class RiskManageIqpFeeCheckImple implements RiskManageInterface{

	private final String modelId = "IqpLoanApp";//业务申请表
	private final String modelIdPrd = "PrdBasicinfo";//产品表
	private final String modelIdFee = "IqpAppendTerms";//产品表
	private final String modelCsgnLoanInfo = "IqpCsgnLoanInfo";//委托贷款关联信息
	private final String modelAppendTerms = "IqpAppendTerms";//费用信息
	
	/*费用检查
	 */
	public Map<String, String> getResultMap(String tableName, String serno,Context context,Connection connection) throws Exception {
		
		//分开事务，自己创建连接
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection conn = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		String returnFlag = "";
		String returnInfo = "";
		String fee_code = "";
		String[] needFeeCodeStr=null;
		int size = 0;
		try {
			conn = this.getConnection(context, dataSource);
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kCollIqp = dao.queryDetail(modelId, serno, connection);
			String prd_id = (String)kCollIqp.getDataValue("prd_id");
			if(prd_id != null && !"".equals(prd_id)){
				//如果为企业委托贷款、个人委托贷款 (100063,100065)
				if("100063".equals(prd_id) || "100065".equals(prd_id)){
				   //查询委托人信息（委托人一般账号）
				   KeyedCollection kCollCsgn = dao.queryDetail(modelCsgnLoanInfo, serno, connection);
				   String csgn_acct_no = (String)kCollCsgn.getDataValue("csgn_acct_no");
				   BigDecimal csgn_chrg_pay_rate = BigDecimalUtil.replaceNull(kCollCsgn.getDataValue("csgn_chrg_pay_rate"));//委托人手续费支付比例
				   BigDecimal debit_chrg_pay_rate = BigDecimalUtil.replaceNull(kCollCsgn.getDataValue("debit_chrg_pay_rate"));//借款人手续费支付比例
				   //{'01':'借款人委托贷款费用', '09':'委托人委托贷款费用', }
				   if(csgn_chrg_pay_rate.compareTo(new BigDecimal(0))>0){
					   IndexedCollection iColl09 = dao.queryList(modelAppendTerms, "where serno='"+serno+"' and fee_code='09'", connection);
				       if(iColl09.size()<=0){
				    	   returnFlag = "不通过";
						   returnInfo = "[费用信息]中未录入委托人委托贷款费用";
						   returnMap.put("OUT_是否通过", returnFlag);
						   returnMap.put("OUT_提示信息", returnInfo);
						   return returnMap;
				       }
				   }
				   if(debit_chrg_pay_rate.compareTo(new BigDecimal(0))>0){
					   IndexedCollection iColl01 = dao.queryList(modelAppendTerms, "where serno='"+serno+"' and fee_code='01'", connection);
					   if(iColl01.size()<=0){
				    	   returnFlag = "不通过";
						   returnInfo = "[费用信息]中未录入借款人委托贷款费用";
						   returnMap.put("OUT_是否通过", returnFlag);
						   returnMap.put("OUT_提示信息", returnInfo);
						   return returnMap;
				       }
				   }
				   
				   
				   //费用信息，委托人委托贷款费用
				   String condition = "where serno='"+serno+"' and fee_code='09'";
				   KeyedCollection kCollAppendTerms = dao.queryFirst(modelAppendTerms, null, condition, connection);
				   String fee_acct_no = (String)kCollAppendTerms.getDataValue("fee_acct_no");
				   if(fee_acct_no != null && !"".equals(fee_acct_no)){
					   if(!fee_acct_no.equals(csgn_acct_no)){
						   returnFlag = "不通过";
						   returnInfo = "[委托人委托贷款费用]的[费用账号]与[委托人信息]中[委托人一般账号]不一致";
						   returnMap.put("OUT_是否通过", returnFlag);
						   returnMap.put("OUT_提示信息", returnInfo);
						   return returnMap;
					   }
				   }
				}
				KeyedCollection kCollPrd = dao.queryDetail(modelIdPrd, prd_id, connection);
				String needFeeCode = (String)kCollPrd.getDataValue("needFeeCode");
				if(needFeeCode == null || "".equals(needFeeCode)){
					returnFlag = "通过";
					returnInfo = "该产品未配置必需费用，无需检查";
				}else{
					//查询业务下费用信息
                    String condition ="where serno='"+serno+"'";
                    IndexedCollection iCollFee = dao.queryList(modelIdFee, condition, connection);
				    needFeeCodeStr = needFeeCode.split(",");
					for(int j=0;j<needFeeCodeStr.length;j++){
						for(int i=0;i<iCollFee.size();i++){
	                    	KeyedCollection kCollFee = (KeyedCollection)iCollFee.get(i);
	                    	fee_code = (String)kCollFee.getDataValue("fee_code");
	                    	if(fee_code.equals(needFeeCodeStr[j])){
	                    		size += 1;
	                    	}
	                    }
						if(size == j){
							if(size == needFeeCodeStr.length-1){
								returnInfo = returnInfo + needFeeCodeStr[j];
							}else{
								returnInfo = returnInfo + needFeeCodeStr[j] + ",";
							}
							size += 1;
                    	}
					}
					if("".equals(returnInfo) || returnInfo == null){
						returnFlag = "通过";
						returnInfo = "产品费用检查通过！";
					}else{
						TreeDicTools treeDicSer = new TreeDicTools();
						if(returnInfo.endsWith(",")){
							returnInfo = returnInfo.substring(0, returnInfo.length()-1);
						}
						String showTxt = treeDicSer.getDicsName(returnInfo, "STD_ZB_FEE_CODE", connection);	
						returnFlag = "不通过";
						returnInfo = "该产品需添加如下费用:["+showTxt+"]";
					}
				}
			}else{
				returnFlag = "不通过";
				returnInfo = "产品编号为空，请检查！";
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
