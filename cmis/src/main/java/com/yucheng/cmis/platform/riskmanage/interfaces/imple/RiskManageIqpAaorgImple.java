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

public class RiskManageIqpAaorgImple implements RiskManageInterface{

	private final String modelId = "IqpLoanApp";//业务申请表
	
	/*银票直贴业务判断票据承兑行的总行授信是否足额
	 * 1.是银票直贴业务
     * 2.统计关系表中每个总行下的持有状态的票据的票面金额是否超过总行的授信余额
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
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			
			
			KeyedCollection kColl = dao.queryDetail(modelId, serno, conn);
			String limit_ind = (String) kColl.getDataValue("limit_ind");
			String prd_id = (String) kColl.getDataValue("prd_id");
			BigDecimal lmt_amt = new BigDecimal(0);
			String msg = "";
			
			//占用承兑行授信 银票直贴业务
			if("4".equals(limit_ind) && "300021".equals(prd_id)){
				String conditionStr = "where same_org_no in (select d.head_org_no from cus_same_org d where d.same_org_no in (select c.aorg_no from iqp_bill_detail c where c.porder_no in (select a.porder_no from iqp_batch_bill_rel a where a.batch_no in (select b.batch_no from Iqp_Batch_Mng b where b.serno = '"+serno+"'))))";
        		IndexedCollection iCollCus = dao.queryList("CusSameOrg", conditionStr, connection);
				String condition ="where serno='"+serno+"'";
				IndexedCollection iCollRel = dao.queryList("RBusLmtcreditInfo", condition, connection);
				if(iCollCus.size()!=iCollRel.size()){
					returnFlag = "不通过";
					returnInfo = "请去修改页面点击[保存]按钮!";
				}else{
					for(int i=0;i< iCollRel.size();i++){
						   KeyedCollection kCollRel = (KeyedCollection)iCollRel.get(i);
						   String agr_no = (String)kCollRel.getDataValue("agr_no");
						   //统计总行已占用的授信额度
						   KeyedCollection kCollRes = serviceIqp.getLmtAmtByArgNo(agr_no, "02", connection, context);
						   lmt_amt = BigDecimalUtil.replaceNull(kCollRes.getDataValue("lmt_amt"));
						   //查询授信余额
						   LmtServiceInterface serviceLmt = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
						   BigDecimal enable_amt = BigDecimalUtil.replaceNull(serviceLmt.searchLmtAgrAmtByAgrNO(agr_no, "02", connection));
						   if(enable_amt.compareTo(lmt_amt)<0){
							   if(i!=(iCollRel.size()-1)){
								   msg += agr_no+",";
							   }else{
								   msg += agr_no;
							   }
						   }
						}
						if("".equals(msg)){
							returnFlag = "通过";
							returnInfo = "银票贴现业务承兑行授信足额校验通过";
						}else{
							returnFlag = "不通过";
							returnInfo = "银票贴现业务承兑行授信余额不足,分别为以下授信台账编号:"+msg;
						}
				}
			}else{
				returnFlag="通过";
				returnInfo="不适合此项检查";
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
