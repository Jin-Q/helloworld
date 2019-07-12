package com.yucheng.cmis.biz01line.iqp.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class IqpLoanAppDao extends CMISDao {

	/**
	 * 通过币种获取汇率
	 * @param currType
	 * @return 汇率以及反馈信息组成的KeyedCollection 
	 * @throws Exception
	 */
	public KeyedCollection getHLByCurrType(String currType) throws Exception {
		KeyedCollection returnKColl = new KeyedCollection();
		try {
			returnKColl = (KeyedCollection)SqlClient.queryFirst("getHLByCurrType", currType, null, this.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnKColl;
	}
	/**
	 * 通过客户码查询客户所属业务条线
	 * @param cus_id
	 * @return 业务信息以及反馈信息组成的KeyedCollection 
	 * @throws Exception
	 */
	public KeyedCollection getBelglineByKhm(String cus_id) throws Exception {
		KeyedCollection returnKColl = new KeyedCollection();
		try {
			returnKColl = (KeyedCollection)SqlClient.queryFirst("getBelglineByKhm", cus_id, null, this.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnKColl;
	}
	
	/**
	 * 通过操作类型以及表单数据，完成对数据库的增删改
	 * @param optType 操作类型【add新增、del删除、none不做任何处理、""修改】
	 * @param kc
	 * @throws Exception
	 */
	public void doCusMangerByOptType(String optType, KeyedCollection kc) throws Exception {
		if("add".endsWith(optType)){
			//SqlClient.insert("insertDoCusMangerByOptType", kc, this.getConnection());
			kc.removeDataElement("displayid");
			kc.removeDataElement("optType");
			kc.addDataField("input_id", this.getContext().getDataValue(PUBConstant.currentUserId));
			kc.addDataField("input_org", this.getContext().getDataValue(PUBConstant.organNo));
			kc.addDataField("input_date", this.getContext().getDataValue(PUBConstant.OPENDAY));
			
		}else if("del".endsWith(optType)){
			SqlClient.delete("deleteDoCusMangerByOptType", kc, this.getConnection());
		}else if("none".endsWith(optType)){
			//无效操作不做任何处理
		}else {
			kc.addDataField("update_date", this.getContext().getDataValue(PUBConstant.OPENDAY));
			SqlClient.executeUpd("updateDoCusMangerByOptType", kc, kc, null, this.getConnection());
		}
		
		
	}
	/**
	 * 通过业务品种、币种、期限类型、期限获取利率信息
	 * @param prdId 业务品种
	 * @param currType 币种
	 * @param termM 期限
	 * @return 利率以及反馈信息组成的KeyedCollection 
	 * @throws Exception
	 */
	public KeyedCollection getRate(String prdId, String currType, int termM) throws Exception {
		KeyedCollection returnKColl = null;
		try {
			Map paramMap = new HashMap();
			paramMap.put("prdid", prdId);
			paramMap.put("curtype", currType);
			paramMap.put("termM", termM);
			returnKColl = (KeyedCollection)SqlClient.queryFirst("getRate", paramMap, null, this.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnKColl;
	}
	/**
	 * 通过流水号查询所有业务申请表中是否有记录数据
	 * @param serno 流水号
	 * @return 表名、流水号组成的集合
	 * @throws Exception
	 */
	public List querySubTablesBySerno(String serno) throws Exception {
		List list = new ArrayList();
		list = SqlClient.queryList4IColl("querySubTablesBySerno", serno, this.getConnection());
		return list;
	}
	/**
	 * 通过表名和流水号删除表中记录
	 * @param tname 表名
	 * @param serno 流水号
	 * @return
	 * @throws Exception
	 */
	public int delSubTablesRecordBySerno(String tname, String serno) throws Exception {
		int result = 0;
		PreparedStatement pstmt = null;
		try {
			String sql = "delete "+tname+" where serno = '"+serno+"'";
			pstmt = this.getConnection().prepareStatement(sql);
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null){
				pstmt.close();
				pstmt = null;
			}
		}
		return result;
	}
	/**
	 * 往票据批次关联表中插入关联数据
	 * @param param 数据Map
	 * @return
	 * @throws Exception
	 */
	public int insertIqpBatchBillRel(Map<String, String> param) throws Exception {
		int result = SqlClient.insert("insertIqpBatchBillRel", param, this.getConnection());
		return result;
	}
	/**
	 * 通过汇票号码获取汇票信息
	 * @param porderno 汇票号码
	 * @return
	 * @throws Exception
	 */
	public KeyedCollection getPorderMsgByPorderNo(String porderno) throws Exception {
		KeyedCollection result = (KeyedCollection)SqlClient.queryFirst("getPorderMsgByPorderNo", porderno, null, this.getConnection());
		return result; 
	}
	public int deleteTabByAssetno(String asset_no) throws Exception{
		int result = SqlClient.delete("deleteTabByAssetNo", asset_no, this.getConnection());
		return result;
	}
	/**
	 * 修改业务担保合同关系表中的担保等级
	 * @return 修改结果
	 * @throws Exception
	 */
	public int updateGrtLoanRGurLvlYB(Map<String, String> map,Connection connection) throws Exception{
		int result = SqlClient.update("updateGrtLoanRGurLvlYB", map, null, null, connection);
		return result;  
	}
	/**
	 * 修改业务担保合同关系表中的担保等级
	 * @return 修改结果
	 * @throws Exception
	 */
	public int updateGrtLoanRGurLvlZGE(Map<String, String> map,Connection connection) throws Exception{
		int result = SqlClient.update("updateGrtLoanRGurLvlZGE", map, null, null, connection);
		return result;    
	}
	
	/**
	 * 信用证修改时，关联关系为正常的改为续作
	 * cont_no 合同编号
	 * @return 修改结果
	 * @throws Exception 
	 */
	public int updateGrtLoanRGurCorreRel(String serno,Connection connection) throws Exception{
		int result = SqlClient.update("updateGrtLoanRGurCorreRel", serno, null, null, connection);
		return result;     
	}
	/**
	 * 通过担保合同编号字符串查询合同编号
	 * guar_cont_no 担保合同字符串 
	 * @return 结果
	 * @throws Exception 
	 */
	public IndexedCollection getGrtLoanRGur(String guar_cont_no,Connection connection) throws Exception{
		IndexedCollection iColl = new IndexedCollection();
		iColl = SqlClient.queryList4IColl("getGrtLoanRGur", guar_cont_no, connection);
	    return iColl; 	
	}
	/**
	 * 删除网络中成员名单，及其项下的从表
	 * @return 修改结果
	 * @throws Exception
	 */
	public int removeIqpAppMem(Map<String, String> map,Connection connection) throws Exception{
		int result1 = SqlClient.delete("removeIqpAppMem", map, connection);
		int result2 = SqlClient.delete("removeIqpAppOverseeAgr", map, connection);
		int result3 = SqlClient.delete("removeIqpAppDesbuyPlan", map, connection);
		int result4 = SqlClient.delete("removeIqpAppBconCoopAgr", map, connection);
		int result5 = SqlClient.delete("removeIqpAppDepotAgr", map, connection);
		int result6 = SqlClient.delete("removeIqpAppPsaleCont", map, connection);
		return 0;    
	}
	/**
	 *删除入网退网申请删除网络中成员名单，及其项下的从表
	 * @return 修改结果
	 * @throws Exception
	 */
	public int removeIqpAppMemByApp(String serno,Connection connection) throws Exception{
		int result1 = SqlClient.delete("removeIqpAppMemByApp", serno, connection);
		int result2 = SqlClient.delete("removeIqpAppOverseeAgrByApp", serno, connection);
		int result3 = SqlClient.delete("removeIqpAppDesbuyPlanByApp", serno, connection);
		int result4 = SqlClient.delete("removeIqpAppBconCoopAgrByApp", serno, connection);
		int result5 = SqlClient.delete("removeIqpAppDepotAgrByApp", serno, connection);
		int result6 = SqlClient.delete("removeIqpAppPsaleContByApp", serno, connection);
		return 0;
	}
	
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppMemMana(IndexedCollection iColl,String serno,Connection conn) throws EMPException {
//		Connection conn = this.getConnection();
		Statement ps = null;
		try {
			String sql = "";
			ps = conn.createStatement();
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(i);
				sql = "insert into Iqp_App_Mem_Mana values ('"+serno+"','"+kCollTmp.getDataValue("mem_cus_id")+"','"+kCollTmp.getDataValue("mem_manuf_type")+"','"+kCollTmp.getDataValue("term")+"','"+kCollTmp.getDataValue("lmt_type")+"','3','"+kCollTmp.getDataValue("lmt_quota")+"')";
				ps.addBatch(sql);
			}
			EMPLog.log("入网/退网申请数据初始化,成员",EMPLog.WARNING, 0, "sql: " + sql);
			ps.executeBatch();
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				throw new EMPException(e);
			}
		}
		return "";
	}
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppOverseeAgr(IndexedCollection iColl,String serno,Connection conn,Context context) throws EMPException {
//		Connection conn = this.getConnection();
		Statement ps = null;
		try {
			String sql = "";
			ps = conn.createStatement();
			for(int i=0;i<iColl.size();i++){
				String oversee_agr_no = CMISSequenceService4JXXD.querySequenceFromDB("OA", "all", conn, context);
				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(i);
				sql = "insert into IqpApp_Oversee_Agr values ('"+oversee_agr_no+"','"+kCollTmp.getDataValue("mortgagor_id")+"','"+kCollTmp.getDataValue("bail_acct")+"','"+kCollTmp.getDataValue("bail_acct_name")+"','"+kCollTmp.getDataValue("oversee_con_id")+"','"+kCollTmp.getDataValue("oversee_store")+"','"+kCollTmp.getDataValue("start_date")+"','"+kCollTmp.getDataValue("end_date")+"','"+kCollTmp.getDataValue("oversee_mode")+"','"+kCollTmp.getDataValue("low_auth_value")+"','"+kCollTmp.getDataValue("vigi_line")+"','"+kCollTmp.getDataValue("stor_line")+"','"+kCollTmp.getDataValue("froze_line")+"','"+kCollTmp.getDataValue("insure_mode")+"','"+kCollTmp.getDataValue("memo")+"','"+kCollTmp.getDataValue("input_id")+"','"+kCollTmp.getDataValue("input_br_id")+"','"+kCollTmp.getDataValue("input_date")+"','"+serno+"')";
				ps.addBatch(sql);
			}
			EMPLog.log("入网/退网申请数据初始化,监管协议",EMPLog.WARNING, 0, "sql: " + sql);
			ps.executeBatch();
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				throw new EMPException(e);
			}
		}
		return "";
	}
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppDesbuyPlan(IndexedCollection iColl,String serno,Connection conn,Context context) throws EMPException {
//		Connection conn = this.getConnection();
		Statement ps = null;
		try {
			String sql = "";
			ps = conn.createStatement();
			for(int i=0;i<iColl.size();i++){
				String desgoods_plan_no = CMISSequenceService4JXXD.querySequenceFromDB("DP", "all", conn, context);
				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(i);
				sql = "insert into Iqp_App_Desbuy_Plan values ('"+serno+"','"+desgoods_plan_no+"','"+kCollTmp.getDataValue("cus_id")+"','"+kCollTmp.getDataValue("commo_name")+"','"+kCollTmp.getDataValue("desbuy_qnt")+"','"+kCollTmp.getDataValue("desbuy_qnt_unit")+"','"+kCollTmp.getDataValue("desbuy_unit_price")+"','"+kCollTmp.getDataValue("desbuy_total")+"','"+kCollTmp.getDataValue("start_date")+"','"+kCollTmp.getDataValue("end_date")+"','"+kCollTmp.getDataValue("fore_disp_date")+"','"+kCollTmp.getDataValue("memo")+"','"+kCollTmp.getDataValue("input_id")+"','"+kCollTmp.getDataValue("input_br_id")+"','"+kCollTmp.getDataValue("input_date")+"','"+kCollTmp.getDataValue("for_manuf")+"')";
				ps.addBatch(sql);
			}
			EMPLog.log("入网/退网申请数据初始化,订货计划申请",EMPLog.WARNING, 0, "sql: " + sql);
			ps.executeBatch();
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				throw new EMPException(e);
			}
		}
		return "";
	}
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppBconCoopAgr(IndexedCollection iColl,String serno,Connection conn,Context context) throws EMPException {
//		Connection conn = this.getConnection();
		Statement ps = null;
		try {
			String sql = "";
			ps = conn.createStatement();
			for(int i=0;i<iColl.size();i++){
				String coop_agr_no = CMISSequenceService4JXXD.querySequenceFromDB("BCA", "all", conn, context);
				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(i);
				sql = "insert into Iqp_App_Bcon_Coop_Agr values ('"+serno+"','"+coop_agr_no+"','"+kCollTmp.getDataValue("borrow_cus_id")+"','"+kCollTmp.getDataValue("manuf_cus_id")+"','"+kCollTmp.getDataValue("psale_cont")+"','"+kCollTmp.getDataValue("desgoods_plan_no")+"','"+kCollTmp.getDataValue("low_bail_perc")+"','"+kCollTmp.getDataValue("vigi_line")+"','"+kCollTmp.getDataValue("froze_line")+"','"+kCollTmp.getDataValue("stor_line")+"','"+kCollTmp.getDataValue("consign_cus_id")+"','"+kCollTmp.getDataValue("consign_addr")+"','"+kCollTmp.getDataValue("refndmt_acct")+"','"+kCollTmp.getDataValue("refndmt_acct_name")+"','"+kCollTmp.getDataValue("start_date")+"','"+kCollTmp.getDataValue("end_date")+"','"+kCollTmp.getDataValue("memo")+"','"+kCollTmp.getDataValue("input_id")+"','"+kCollTmp.getDataValue("input_br_id")+"','"+kCollTmp.getDataValue("input_date")+"')";
				ps.addBatch(sql);
			}
			EMPLog.log("入网/退网申请数据初始化,银企合作协议申请",EMPLog.WARNING, 0, "sql: " + sql);
			ps.executeBatch();
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				throw new EMPException(e);
			}
		}
		return "";
	}
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppDepotAgr(IndexedCollection iColl,String serno,Connection conn,Context context) throws EMPException {
//		Connection conn = this.getConnection();
		Statement ps = null;
		try {
			String sql = "";
			ps = conn.createStatement();
			for(int i=0;i<iColl.size();i++){
				String depot_agr_no = CMISSequenceService4JXXD.querySequenceFromDB("DPT", "all", conn, context);
				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(i);
				sql = "insert into Iqp_App_Depot_Agr values ('"+serno+"','"+depot_agr_no+"','"+kCollTmp.getDataValue("cus_id")+"','"+kCollTmp.getDataValue("psale_cont_no")+"','"+kCollTmp.getDataValue("desgoods_plan_no")+"','"+kCollTmp.getDataValue("fst_bail_perc")+"','"+kCollTmp.getDataValue("contacc_freq_unit")+"','"+kCollTmp.getDataValue("contacc_freq")+"','"+kCollTmp.getDataValue("contacc_mode")+"','"+kCollTmp.getDataValue("start_date")+"','"+kCollTmp.getDataValue("end_date")+"','"+kCollTmp.getDataValue("memo")+"','"+kCollTmp.getDataValue("input_id")+"','"+kCollTmp.getDataValue("input_br_id")+"','"+kCollTmp.getDataValue("input_date")+"','"+kCollTmp.getDataValue("fst_deliv_agreed")+"','"+kCollTmp.getDataValue("agreed_rate")+"')";
				ps.addBatch(sql);
			}
			EMPLog.log("入网/退网申请数据初始化,保兑仓协议申请",EMPLog.WARNING, 0, "sql: " + sql);
			ps.executeBatch();
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				throw new EMPException(e);
			}
		}
		return "";
	}
	
	/**
	 * 入网/退网申请初始化数据
	 * @param 
	 * @return
	 * @throws EMPException
	 * @throws SQLException
	 */
	public String insertIqpAppPsaleCont(IndexedCollection iColl,String serno,Connection conn,Context context) throws EMPException {
//		Connection conn = this.getConnection();
		Statement ps = null;
		try {
			String sql = "";
			ps = conn.createStatement();
			for(int i=0;i<iColl.size();i++){
				String psale_cont = CMISSequenceService4JXXD.querySequenceFromDB("PC", "all", conn, context);
				KeyedCollection kCollTmp = (KeyedCollection)iColl.get(i);
				sql = "insert into Iqp_App_Psale_Cont values ('"+serno+"','"+psale_cont+"','"+kCollTmp.getDataValue("buyer_cus_id")+"','"+kCollTmp.getDataValue("barg_cus_id")+"','"+kCollTmp.getDataValue("cont_amt")+"','"+kCollTmp.getDataValue("start_date")+"','"+kCollTmp.getDataValue("end_date")+"','"+kCollTmp.getDataValue("commo_name")+"','"+kCollTmp.getDataValue("qnt")+"','"+kCollTmp.getDataValue("unit_price")+"','"+kCollTmp.getDataValue("total")+"','"+kCollTmp.getDataValue("memo")+"','"+kCollTmp.getDataValue("input_id")+"','"+kCollTmp.getDataValue("input_br_id")+"','"+kCollTmp.getDataValue("input_date")+"','"+kCollTmp.getDataValue("qnt_unit")+"','"+kCollTmp.getDataValue("mem_cus_id")+"')";
				ps.addBatch(sql);
			}
			EMPLog.log("入网/退网申请数据初始化,购销合同申请",EMPLog.WARNING, 0, "sql: " + sql);
			ps.executeBatch();
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				throw new EMPException(e);
			}
		}
		return "";
	}
	/**@author lisj
	 * @time 2014年11月18日 
	 * @description 需求:【XD140818051】零售合同评分改造
	 * 1.通过流水号计算零售个人业务合同评分
	 * 2.通过传入参数【流水号】调用客户接口，获取客户所属业务条线
	 * 3.获取零售合同评分配置信息进行评分计算
	 * 4.零售合同评分 = 担保模式得分 + 利率定价得分 + 特殊加分
	 * @throws Exception 
	 */
	public BigDecimal getIntRatPriModeScoreBySerno(String serno) throws Exception {
		BigDecimal intRatPriModeScore = new BigDecimal("0");
		intRatPriModeScore =  (BigDecimal) SqlClient.queryFirst("getIntRatPriModeScoreBySerno", serno, null, this.getConnection());
		return intRatPriModeScore;
		}
	public BigDecimal getGuarModeScoreBySerno(String serno) throws Exception {
		BigDecimal guarModeScore = new BigDecimal("0");
		guarModeScore =  (BigDecimal) SqlClient.queryFirst("getGuarModeScoreBySerno", serno, null, this.getConnection());
		return guarModeScore;
	}
}
