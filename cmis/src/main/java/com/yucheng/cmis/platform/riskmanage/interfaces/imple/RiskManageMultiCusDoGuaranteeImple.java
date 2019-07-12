package com.yucheng.cmis.platform.riskmanage.interfaces.imple;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
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
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
/**
 * 
*@author lisj
*@time 2015-1-6
*@description 需求编号：【XD141029074】增加授信集团/行业/联保小组关联担保提醒事项（集团/联保小组授信成员检查）
*@version v1.0
*
 */
public class RiskManageMultiCusDoGuaranteeImple implements RiskManageInterface {
	private final String modelId = "GrtGuarCont";//担保合同表
	private static final Logger logger = Logger.getLogger(RiskManageIqpComFinImple.class);
	
	public Map<String, String> getResultMap(String tableName, String serno, Context context, Connection connection) throws Exception {
		//分开事务，自己创建连接
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection conn = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		IndexedCollection MemList =null;//获取集团/行业/联保小组授信成员
		String returnFlag = "";
		String returnInfo = "";
	   // String conditionStr = "";
		String guar_cont_no ="";//担保合同编号
		String borrower_name ="";//借款人姓名
		String mem_name ="";//集团/联保小组授信成员姓名
		String guar_cont_state ="";//担保合同状态
		String cur_type ="";//币种
		BigDecimal used_amt = new BigDecimal(0.00);//占用金额
		try {
			 conn = this.getConnection(context, dataSource);
			 TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			// IqpLoanAppComponent iqpLoanAppComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
			 // .getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, conn);
			 if("LmtAppGrp".equals(tableName)){
				  MemList = dao.queryList("LmtApply", " where grp_serno='"+serno+"'", connection);
			 }else if("LmtAppJointCoop".equals(tableName)){
				  MemList = dao.queryList("LmtAppNameList", " where serno='"+serno+"'", connection);
			 }else if("LmtIndusApply".equals(tableName)){
				  MemList = dao.queryList("LmtIndusListApply", " where serno='"+serno+"'", connection);
			 }
			 if(MemList!=null && MemList.size() >0){
				 for(int t=0; t<MemList.size(); t++){
					 String mem_cus_id ="";
					 IndexedCollection GuarContNo4CusDoList =null;
					 KeyedCollection kCollApp = (KeyedCollection) MemList.get(t);
					 if("LmtIndusListApply".equals(tableName)){
						 String status =  (String)kCollApp.getDataValue("status");//成员状态
						 if("001".equals(status) || "003".equals(status)){	 
							 mem_cus_id  = (String)kCollApp.getDataValue("cus_id");//行业授信成员客户编号
							 //获取该授信企业为他人担保的担保合同编号列表
							 GuarContNo4CusDoList = SqlClient.queryList4IColl("queryGuarContNo4CusDoByCusId", mem_cus_id, conn);
						 }
					 }else{
						 mem_cus_id = (String)kCollApp.getDataValue("cus_id");//集团/联保小组授信成员客户编号 
						 GuarContNo4CusDoList = SqlClient.queryList4IColl("queryGuarContNo4CusDoByCusId", mem_cus_id, conn);
					 }		
					 CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
					 CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
					 CusBase memCusBase = csi.getCusBaseByCusId(mem_cus_id,context,connection);
					 mem_name = TagUtil.replaceNull4String(memCusBase.getCusName());//授信成员名称

					 if(GuarContNo4CusDoList!=null && GuarContNo4CusDoList.size()>0){
					  for(int i=0; i < GuarContNo4CusDoList.size(); i++){
						  KeyedCollection temp = (KeyedCollection) GuarContNo4CusDoList.get(i);
						  guar_cont_no = (String) temp.getDataValue("guar_cont_no");//授信成员为他人担保的担保合同编号
						   CusBase cusBase = csi.getCusBaseByCusId(((String) temp.getDataValue("cus_id")),context,connection);
						   borrower_name = TagUtil.replaceNull4String(cusBase.getCusName());//客户名称
						   guar_cont_state =(String) temp.getDataValue("guar_cont_state");//担保合同状态
						   cur_type = (String) temp.getDataValue("cur_type");
						 KeyedCollection kColl = dao.queryDetail(modelId, guar_cont_no, conn);
						 try{
							 /**modified by lisj 2015-1-9 修复前台数据显示不准确，影响查数功能，于2015-1-15上线 begin**/
								used_amt = BigDecimalUtil.replaceNull("0");
								used_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("guar_amt").toString());//直接取担保合同的保证金额
								/**modified by lisj 2015-1-9 修复前台数据显示不准确，影响查数功能，于2015-1-15上线 end**/
							}catch(Exception e){}
						/** if(kColl.containsKey("guar_cont_type")&&kColl.getDataValue("guar_cont_type")!=null
									&&"00".equals(kColl.getDataValue("guar_cont_type").toString())){
						//一般担保  1正常，5 已解除
						conditionStr = "where guar_cont_no='"+guar_cont_no+"' and is_add_guar='2' and corre_rel in('1','5')";
						//查询关联表中此担保合同已经引入的金额
						IndexedCollection iColl =  dao.queryList("GrtLoanRGur", conditionStr, conn);
							for(int j=0;j<iColl.size();j++){
								KeyedCollection kColl1 = (KeyedCollection)iColl.get(j);
								String is_per_gur = (String)kColl1.getDataValue("is_per_gur");
								if(is_per_gur != null && !"".equals(is_per_gur)){
									String pk_id = (String)kColl1.getDataValue("pk_id");
									String cont_no = (String)kColl1.getDataValue("cont_no");
									if(cont_no != null && !"".equals(cont_no)){
										String res = iqpLoanAppComponent.caculateGuarAmtSp(null, cont_no,pk_id);
										if("2".equals(res)){
											used_amt = used_amt.add(new BigDecimal(kColl1.getDataValue("guar_amt").toString()));
										}else{
											used_amt = used_amt.add(new BigDecimal(0));
										}
									}else{
										String sernoSelect = (String)kColl1.getDataValue("serno");
										String res = iqpLoanAppComponent.caculateGuarAmtSp(sernoSelect, null,pk_id);
										if("2".equals(res)){
											used_amt = used_amt.add(new BigDecimal(kColl1.getDataValue("guar_amt").toString()));
										}else{
											used_amt = used_amt.add(new BigDecimal(0));
										}
									}
								}
							}
						 }else{
							 //最高额担保
							 CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
							 IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
							 used_amt = BigDecimalUtil.replaceNull(service.getAmtForGuarCont(guar_cont_no, context, conn));
						 }**/
						 //担保合同状态为有效情况下，做提醒操作；担保合同状态为登记情况下，如果已担保金额不为0，做提醒操作
						 if(used_amt!=null && guar_cont_state !=null &&  guar_cont_state.equals("01")){
							 returnFlag ="不通过";
							 returnInfo += "该企业授信成员["+mem_name+"]已在我行为["+borrower_name+"]提供担保,担保金额["+cur_type+used_amt+"];";
					     }else if(used_amt!=null && guar_cont_state !=null && guar_cont_state.equals("00") && (used_amt.compareTo(new BigDecimal(0.00)) >0)){    	 
					    	 returnFlag ="不通过";
							 returnInfo += "该企业授信成员["+mem_name+"]已在我行为["+borrower_name+"]提供担保,担保金额["+cur_type+used_amt+"];";
					     }			  	
					  }
					}
				 }
				  if("".equals(returnFlag)){
			    	  returnFlag ="通过";
			    	  returnInfo +="该企业授信成员在我行未存在为他人担保情况，检查通过！";
				  }
			 }	 
			 	returnMap.put("OUT_是否通过", returnFlag);
				returnMap.put("OUT_提示信息", returnInfo);
				
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "企业在我行为他人担保检查失败！"+e.getMessage(), null);
			logger.error("企业在我行为他人担保检查失败！"+e.getStackTrace());
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
