package com.yucheng.cmis.platform.riskmanage.interfaces.imple;

import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import javax.sql.DataSource;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.ConnectionManager;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.session.SessionException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.riskmanage.interfaces.RiskManageInterface;
import com.yucheng.cmis.util.TableModelUtil;

public class RiskManageLmtAppIndivForSHDImple implements RiskManageInterface{
/*
 * 个人授信生活贷校验，需求编号：XD140925064
 * added by yangzy 2014/12/1
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
			IndexedCollection iColl = dao.queryList("LmtAppDetails", " where serno = '"+serno+"' and limit_name in ('100080', '100081', '100082', '100083') ", connection);
			returnFlag = "通过";
			returnInfo = "不存在生活贷授信。";
			
			if(iColl==null || iColl.size()<1){
				returnMap.put("OUT_是否通过", returnFlag);
				returnMap.put("OUT_提示信息", returnInfo);
				return returnMap;
			}else{
				IndexedCollection iColl1 = dao.queryList("LmtAppDetails", " where serno = '"+serno+"' and guar_type in ('400', '300') ", connection);
				if(iColl1!=null&&iColl1.size()>1){
					returnMap.put("OUT_是否通过", "不通过");
					returnMap.put("OUT_提示信息", "存在其他保证、信用类授信，不能发起生活贷授信!");
					return returnMap;
				}else{
					String condition = " where exists (select 1                                                        "
									  +"          from lmt_app_details                                                 "
									  +"         where lmt_agr_details.cus_id = lmt_app_details.cus_id                 "
									  +"           and lmt_app_details.serno = '"+serno+"')                            "
									  +"   and guar_type in ('300', '400')                                             "
									  +"   and lmt_status <> '30'                                                      "
									  +"   and to_char(add_months(to_date(end_date, 'yyyy-mm-dd'), 6),                 "
									  +"               'yyyy-mm-dd') >= (select openday from pub_sys_info)             "
									  +"   and not exists                                                              "
									  +" (select 1                                                                     "
									  +"          from lmt_app_details                                                 "
									  +"         where lmt_agr_details.limit_code = lmt_app_details.org_limit_code     "
									  +"           and lmt_app_details.serno = '"+serno+"')                            ";
					IndexedCollection iColl2 = dao.queryList("LmtAgrDetails", condition, connection);
					/**modified by lisj 2015-5-10 需求编号：【XD150407025】分支机构授信审批权限配置 (社区支行)begin**/
					String conditionSelect ="select p1.* from lmt_app_indiv p1, wfi_org_lifeloan_rel p2 where p1.comm_branch_id = p2.org_id"
											+" and p1.is_comm_branch = '1' and p2.is_life_loan = '1' and p1.serno ='"+serno+"'";
					IndexedCollection cbiColl = TableModelUtil.buildPageData(null, dataSource, conditionSelect);
					if(iColl2!=null&&iColl2.size()>0){
						returnMap.put("OUT_是否通过", "不通过");
						returnMap.put("OUT_提示信息", "存在其他保证、信用类授信，不能发起生活贷授信!");
						return returnMap;
					}/**else{
						returnMap.put("OUT_是否通过", "通过");
						returnMap.put("OUT_提示信息", "不存在其他保证、信用类授信，生活贷授信通过!");
						//return returnMap;
					}**/
					else if(cbiColl ==null || cbiColl.size()< 1){
						returnMap.put("OUT_是否通过", "不通过");
						returnMap.put("OUT_提示信息", "该授信不是来自社区支行或者该社区支行未开通生活贷权限!");
						return returnMap;
					}else{
						returnMap.put("OUT_是否通过", "通过");
						returnMap.put("OUT_提示信息", "生活贷授信检查通过!");
						return returnMap;
					}
				}
				/**modified by lisj 2015-5-10 需求编号：【XD150407025】分支机构授信审批权限配置 (社区支行)end**/
			}
	    }catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (conn != null)
				this.releaseConnection(dataSource, conn);
		}
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
