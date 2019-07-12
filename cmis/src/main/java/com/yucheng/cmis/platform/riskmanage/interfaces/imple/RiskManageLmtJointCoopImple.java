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
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class RiskManageLmtJointCoopImple implements RiskManageInterface{
/*
 * 申请联保用户是否已在其他小组申请联保
 * added by yangzy 2014/10/31
 */
	public Map<String, String> getResultMap(String tableName, String serno,Context context,Connection connection) throws Exception {
		
		//分开事务，自己创建连接
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		Connection conn = null;
		Map<String, String> returnMap = new HashMap<String, String>();
		String returnFlag = "";
		String returnInfo = "";
		String err_cus = "";
		try {
			conn = this.getConnection(context, dataSource);
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			IndexedCollection iColl = dao.queryList("LmtAppNameList", " where serno = '"+serno+"' and exists (select 1 from lmt_name_list where lmt_name_list.cus_id = lmt_app_name_list.cus_id and lmt_name_list.cus_status = '1' and exists (select 1 from Lmt_Agr_Joint_Coop where lmt_name_list.agr_no = Lmt_Agr_Joint_Coop.agr_no and Lmt_Agr_Joint_Coop.agr_status not in ('001', '003'))) ", connection);
			returnFlag = "通过";
			returnInfo = "否";
			
			if(iColl==null || iColl.size()<1){
				returnMap.put("OUT_是否通过", returnFlag);
				returnMap.put("OUT_提示信息", returnInfo);
				return returnMap;
			}else{
				String condition=" select '客户[' || cus_name || '],已经在联保小组[' || agr_no || ']名单中' as msg "
								+"   from (select (select cb.cus_name from cus_base cb                         "
								+"                  where cb.cus_id = lmt_name_list.cus_id) as cus_name,       "
								+"                LISTAGG(agr_no, ',') WITHIN GROUP(ORDER BY agr_no) AS agr_no "
								+"           from lmt_name_list                                                "
								+"          where lmt_name_list.cus_status = '1'                               "
								+"            and exists                                                       "
								+"          (select 1                                                          "
								+"                   from lmt_agr_joint_coop                                   "
								+"                  where lmt_name_list.agr_no = lmt_agr_joint_coop.agr_no     "
								+"                    and lmt_agr_joint_coop.agr_status not in ('001', '003')) "
								+"            and exists                                                       "
								+"          (select 1                                                          "
								+"                   from lmt_app_name_list                                    "
								+"                  where lmt_app_name_list.cus_id = lmt_name_list.cus_id      "
								+"                    and lmt_app_name_list.serno = '"+serno+"')               "
								+"            and not exists                                                   "
								+"          (select 1                                                          "
								+"                   from (select agr_no, listagg(cus_id, ',') within          "
								+"                          group(                                             "
								+"                          order by cus_id) as cus_id_str                     "
								+"                           from lmt_name_list                                "
								+"                          where lmt_name_list.cus_status = '1'               "
								+"                            and exists (select 1                             "
								+"                                   from lmt_agr_joint_coop                   "
								+"                                  where lmt_name_list.agr_no =               "
								+"                                        lmt_agr_joint_coop.agr_no            "
								+"                                    and lmt_agr_joint_coop.agr_status not in "
								+"                                        ('001', '003'))                      "
								+"                          group by lmt_name_list.agr_no) p1                  "
								+"                  where p1.agr_no = lmt_name_list.agr_no                     "
								+"                    and exists                                               "
								+"                  (select 1                                                  "
								+"                           from (select listagg(cus_id, ',') within          "
								+"                                  group(order by cus_id) as cus_id_str       "
								+"                                   from lmt_app_name_list                    "
								+"                                  where serno = '"+serno+"') p2              "
								+"                          where p1.cus_id_str = p2.cus_id_str))              "
								+"          group by cus_id)                                                   ";
				IndexedCollection iColl1 = TableModelUtil.buildPageData(null, dataSource, condition);
				for(int i=0;i<iColl1.size();i++){
		    		KeyedCollection kColl = (KeyedCollection)iColl1.get(i);
		    		String msg = (String)kColl.getDataValue("msg");
					err_cus+=msg+"|";
		    	}
				if(!"".equals(err_cus)){
					err_cus = err_cus.substring(0, err_cus.length()-1);
					returnFlag = "不通过";
					returnInfo = err_cus;
					returnMap.put("OUT_是否通过", returnFlag);
					returnMap.put("OUT_提示信息", returnInfo);
				}else{
					returnInfo = "原有联保小组授信尚未到期！";
					returnMap.put("OUT_是否通过", returnFlag);
					returnMap.put("OUT_提示信息", returnInfo);
					return returnMap;
				}
			}
	    	
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
