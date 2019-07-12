package com.yucheng.cmis.biz01line.cus.cussubmitinfo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ecc.emp.core.Context;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.CusPubConstant;

public class CusSubmitInfoDao extends CMISDao {
	
	/**
	 * 得到集中作业岗任务最少的人
	 * @param connection 
	 * @param context 
	 * @return
	 * @throws Exception 
	 */
	public String getCurOrgLeastTaskUser(Context context, Connection connection  ) throws Exception {
		Statement stmt=  connection.createStatement();
		ResultSet rs = null;
		String actorno = "";
		
		try {
			initializeData(stmt , rs);
			actorno = selectRecord(stmt,rs);
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				throw e;
			}
		}

		return actorno;
	}
	
	/*** 若cus_approve_count中没有当天数据，则初始化 ***/
	private void initializeData(Statement stmt, ResultSet rs) throws Exception {
		/*** 1.取cus_approve_count当天记录数 ***/
		String sql = "select count(*)CC from cus_approve_count where approve_date = (select openday from pub_sys_info)";
		rs = stmt.executeQuery(sql);
		String cc = "";
		if(rs!=null && rs.next()){
			cc = rs.getString("CC");
		}
		
		/*** 2.无记录时初始化 ***/
		if(cc.equals("0")){
			sql = "insert into cus_approve_count(select distinct(d.actorno)actorno,"
				+ "(select openday from pub_sys_info)openday,0 as nums from s_roleuser d where roleno = '"+CusPubConstant.ROLE_JZZY+"'" +
				//休假登记中生效并且未过期用户不初始化     2014-07-08   唐顺岩
				" AND ACTORNO NOT IN(SELECT ACTORNO FROM S_HOLIDAY_REGISTER WHERE (STATUS='01' AND PLAN_END_DATE>=(SELECT OPENDAY FROM PUB_SYS_INFO)))) ";  
			stmt.executeUpdate(sql);
		}
	}

	/*** 取表中最小记录用户，将其记录数加1后，返回用户码 
	 * 2014-11-14 Edited by FCL 当天休假，当天过滤，不接收新业务
	 * ***/
	private String selectRecord(Statement stmt, ResultSet rs) throws Exception {
		/*** 1.在最小记录用户群中随机取一个 ***/
		String sql = "  select actorno                                                                "
					+"    from (select actorno                                                        "
					+"            from (select *                                                      "
					+"                    from cus_approve_count                                      "
					+"                   where approve_qnt =                                          "
					+"                         (select min(approve_qnt)                               "
					+"                            from cus_approve_count                              "
					+"                           WHERE APPROVE_DATE =                                 "
					+"                                 (SELECT OPENDAY FROM PUB_SYS_INFO)             "
					+"                             and actorno not in                                 "
					+"                                 (SELECT ACTORNO                                "
					+"                                    FROM S_HOLIDAY_REGISTER                     "
					+"                                   WHERE (STATUS = '01' AND                     "
					+"                                         PLAN_END_DATE >=                       "
					+"                                         (SELECT OPENDAY FROM PUB_SYS_INFO))))  "
					+"                     and actorno not in                                         "
					+"                         (select actorno                                        "
					+"                            from s_holiday_register                             "
					+"                           where (STATUS = '01' AND                             "
					+"                                 PLAN_END_DATE >=                               "
					+"                                 (SELECT OPENDAY FROM PUB_SYS_INFO))))          "
					+"           WHERE APPROVE_DATE = (SELECT OPENDAY FROM PUB_SYS_INFO)              "
					+"           order by dbms_random.value)                                          "
					+"   where rownum = '1'                                                           ";
		rs = stmt.executeQuery(sql);
		String actorno = "";
		if(rs!=null && rs.next()){
			actorno = rs.getString("actorno");
		}
		
		/*** 2.更新此用户记录，将记录数据加1 ***/
		sql = "update cus_approve_count set approve_qnt = approve_qnt + 1 where actorno = '"+actorno+"'" +
		//根据日期更新当天的计数       2014-07-08   唐顺岩
			"and  APPROVE_DATE=(SELECT OPENDAY FROM PUB_SYS_INFO) ";
		stmt.executeUpdate(sql);
		
		return actorno ;
	}
}