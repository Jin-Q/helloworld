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
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class RiskManageCtrSigleImple implements RiskManageInterface{

	private final String modelId = "IqpLoanApp";//业务申请表
	private final String modelIdLmt = "LmtAgrDetails";//授信额度台账
	
	/*单一法人授信额度及限额检查
	 * 单一法人授信金额 <= 授信总额。
	 */
	public Map<String, String> getResultMap(String tableName, String serno,Context context,Connection connection) throws Exception {
		
		//分开事务，自己创建连接
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection conn = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		String returnFlag = "";
		String returnInfo = "";
		String limit_acc_no = "";
		BigDecimal lmt_amt_total = new BigDecimal(0.00);
		BigDecimal lmt_canAmt_total = new BigDecimal(0.00);
		try {
			conn = this.getConnection(context, dataSource);
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kCollCtr = dao.queryDetail(modelId, serno, connection);
			String limit_ind = (String)kCollCtr.getDataValue("limit_ind");//授信额度使用标志
			
			/** 如果业务授信额度使用标志为[ 不使用额度]时不校验额度是否足额，也不校验总额是否额度     2014-08-19  唐顺岩    */
			if(null!=limit_ind && "1".equals(limit_ind)){
				returnFlag = "通过";
				returnInfo = "授信额度使用标志为[不使用额度]，无需校验此拦截项！";
				returnMap.put("OUT_是否通过", returnFlag);
				returnMap.put("OUT_提示信息", returnInfo);
				return returnMap;
			}
			/** END */
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
	    	IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			/**客户授信总额控制--------------------------------start-----------------------------------*/
	    	String cus_id = (String)kCollCtr.getDataValue("cus_id");//客户码
	    	String openDay = (String)context.getDataValue("OPENDAY");
	    	String condition = "where cus_id='"+cus_id+"' and end_date>'"+openDay+"' and lmt_status='10'";
	    	IndexedCollection iCollLmt = dao.queryList(modelIdLmt, condition, connection);
	    	for(int i=0;i<iCollLmt.size();i++){
	    		KeyedCollection kCollLmt = (KeyedCollection)iCollLmt.get(i);
	    		String limit_code = (String)kCollLmt.getDataValue("limit_code");
	    		if(limit_code!=null && !"".equals(limit_code)){
					KeyedCollection kCollRes = serviceIqp.getLmtAmtByArgNo(limit_code, "01", connection, context);
					BigDecimal lmt_amt = BigDecimalUtil.replaceNull(kCollRes.getDataValue("lmt_amt"));
					lmt_amt_total = lmt_amt_total.add(lmt_amt);
					//查询授信启用金额-冻结金额
					LmtServiceInterface serviceLmt = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
					IndexedCollection iColl = serviceLmt.queryLmtAgrDetailsByLimitCodeStr("'"+limit_code+"'", null, dataSource);
					if(iColl.size()>0){
						KeyedCollection kCollLmtDeta = (KeyedCollection)iColl.get(0);
						BigDecimal enable_amt =BigDecimalUtil.replaceNull(kCollLmtDeta.getDataValue("enable_amt"));
						BigDecimal froze_amt = BigDecimalUtil.replaceNull(kCollLmtDeta.getDataValue("froze_amt"));
						
						/**  总部风险部、总行行领导可对授信做临时冻结、解冻     需求编号：XD140630018     2014-08-04   唐顺岩 	 */
						//lmt_canAmt_total = lmt_canAmt_total.add(enable_amt.subtract(froze_amt));
						BigDecimal froze_amt_hq = BigDecimalUtil.replaceNull(kCollLmtDeta.getDataValue("froze_amt_hq"));   //总部冻结金额
						
						BigDecimal froze_amt_totl = new BigDecimal("0");
						froze_amt_totl = froze_amt.add(froze_amt_hq);  //汇总分项冻结金额=分行冻结金额+总部冻结金额
						//判断总冻结金额是否大于授信金额，如果大于直接赋值 授信金额
						if(froze_amt_totl.compareTo(enable_amt)>0){
							froze_amt_totl = enable_amt;
						}
						
						lmt_canAmt_total = lmt_canAmt_total.add(enable_amt.subtract(froze_amt_totl));  //启用金额-(支行冻结+总部冻结)
						/** END */
					}else{
						returnFlag = "不通过";
						returnInfo = "通过授信额度编号["+limit_code+"]未查询到授信台账，请联系系统管理员！";
					}
					
				}
	    	}
	    	
//	    	//判断集团成员下授信总额不能超过集团授信总额
//	    	KeyedCollection grpKColl = serviceIqp.getGrpInfo(serno, connection, context);
//			BigDecimal crd_totl_amt = new BigDecimal(0);
//			BigDecimal grp_lmt_amt = BigDecimalUtil.replaceNull(grpKColl.getDataValue("grp_lmt_amt"));//集团下成员授信占用总额
//			String grp_no = (String)grpKColl.getDataValue("grp_no");//集团编号
//			IndexedCollection iCollAgrGrp = dao.queryList("LmtAgrGrp", "where grp_no='"+grp_no+"'", connection);
//			if(iCollAgrGrp.size()>0){
//				KeyedCollection kCollAgrGrp = (KeyedCollection)iCollAgrGrp.get(0);
//				crd_totl_amt =BigDecimalUtil.replaceNull(kCollAgrGrp.getDataValue("crd_totl_amt"));
//				if(crd_totl_amt.compareTo(grp_lmt_amt)<0){
//					returnFlag = "不通过";
//					returnInfo = "集团成员下授信总额不能超过集团授信总额,集团下成员授信总额:"+grp_lmt_amt+",集团授信总额:"+crd_totl_amt;
//					returnMap.put("OUT_是否通过", returnFlag);
//					returnMap.put("OUT_提示信息", returnInfo);
//					return returnMap;
//				} 
//			}
	    	
	    	/**客户授信总额控制--------------------------------end-----------------------------------*/
	    	if(lmt_canAmt_total.compareTo(lmt_amt_total)>=0){
	    		//循环额度，一次性额度
		    	if("2".equals(limit_ind) || "3".equals(limit_ind) || "5".equals(limit_ind) || "6".equals(limit_ind)){
		    		/** 额度台账编号从业务与授信关系表中取     2014-08-19  唐顺岩    */
					//limit_acc_no = (String)kCollCtr.getDataValue("limit_acc_no");
		    		KeyedCollection kCollRBusLmt = dao.queryFirst("RBusLmtInfo",null, " WHERE SERNO='"+serno+"'", connection);
					limit_acc_no = (String)kCollRBusLmt.getDataValue("agr_no");   //获取授信额度编号-自有
					/** END */
					
					if(limit_acc_no!=null && !"".equals(limit_acc_no)){
						KeyedCollection kCollRes = serviceIqp.getLmtAmtByArgNo(limit_acc_no, "01", connection, context);
						BigDecimal lmt_amt = BigDecimalUtil.replaceNull(kCollRes.getDataValue("lmt_amt"));
						//查询授信启用金额-冻结金额
						LmtServiceInterface serviceLmt = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
//						String lmtAll = (String)serviceLmt.searchLmtAgrAmtByAgrNO(limit_acc_no, "01", connection);
//						BigDecimal lmt_total = BigDecimalUtil.replaceNull(lmtAll);
						IndexedCollection iColl = serviceLmt.queryLmtAgrDetailsByLimitCodeStr("'"+limit_acc_no+"'", null, dataSource);
						if(iColl.size()>0){
							KeyedCollection kCollLmtDeta = (KeyedCollection)iColl.get(0);
							BigDecimal enable_amt =BigDecimalUtil.replaceNull(kCollLmtDeta.getDataValue("enable_amt"));
							BigDecimal froze_amt = BigDecimalUtil.replaceNull(kCollLmtDeta.getDataValue("froze_amt"));
							
							/**  总部风险部、总行行领导可对授信做临时冻结、解冻     需求编号：XD140630018     2014-08-04    唐顺岩 	 */
							BigDecimal froze_amt_hq = BigDecimalUtil.replaceNull(kCollLmtDeta.getDataValue("froze_amt_hq"));   //总部冻结金额
							
							BigDecimal froze_amt_totl = new BigDecimal("0");
							froze_amt_totl = froze_amt.add(froze_amt_hq);  //汇总分项冻结金额=分行冻结金额+总部冻结金额
							//判断总冻结金额是否大于授信金额，如果大于直接赋值 授信金额
							if(froze_amt_totl.compareTo(enable_amt)>0){
								froze_amt_totl = enable_amt;
							}
							
							//if((enable_amt.subtract(froze_amt)).compareTo(lmt_amt) >= 0) {
							if((enable_amt.subtract(froze_amt_totl)).compareTo(lmt_amt) >= 0) { 
							/** END */
								returnFlag = "通过";
								returnInfo = "借款人授信额度可以覆盖敞口金额，校验通过。";
							}else{
								returnFlag = "不通过";
								returnInfo = "借款人授信台账["+limit_acc_no+"]可用额度["+enable_amt.subtract(froze_amt_totl)+"]不能覆盖已使用总敞口金额["+lmt_amt+"]，请检查！";
							}
						}else{
							returnFlag = "不通过";
							returnInfo = "通过授信额度编号["+limit_acc_no+"]未查询到授信台账，请联系系统管理员！";
						}
					}else{
						returnFlag = "不通过";
						returnInfo = "该笔业务未关联授信，请检查！";
					}
		    	}else{
		    		returnFlag = "通过";
					returnInfo = "授信额度使用标志为[不使用额度]，无需校验此拦截项。";
				}
	    	}else{
	    		returnFlag = "不通过";
				returnInfo = "借款人授信总额度["+lmt_canAmt_total+"]不能覆盖业务总敞口["+lmt_amt_total+"]，校验不通过！";
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
