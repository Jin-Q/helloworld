package com.yucheng.cmis.biz01line.fnc.master.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.domain.Fnc4Query;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncIndexRpt;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBs;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatCfs;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatIs;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatSl;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatSoe;
import com.yucheng.cmis.biz01line.fnc.master.domain.RptItemData;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.FNCPubConstant;
import com.yucheng.cmis.pub.FncNumber;
import com.yucheng.cmis.pub.exception.AgentJDBCException;
import com.yucheng.cmis.pub.exception.DaoException;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.TimeUtil;

/**
 *@Classname FncStatCommonDao.java
 *@Version 1.0
 *@Since 1.0 Oct 13, 2008 4:14:42 PM
 *@Copyright yuchengtech
 *@Author Eric
 *@Description：财务组件基本数据访问类
 *@Lastmodified
 *@Author
 */
public class FncStatCommonDao {
	private static final Logger logger = Logger.getLogger(FncStatCommonDao.class);
	/**
	 * 从prptItemData对象中得到对应的sql，新增一条项目信息记录。-------------(暂时未用)
	 * 如果该年的记录不存在，执行新增操作。存在则执行更新操作
	 * 
	 * @param conn
	 *            数据库连接
	 * @param prptItemData
	 *            报表项目信息数据
	 * @throws AgentJDBCException
	 */
	public void insertRptItemData(Connection conn, RptItemData prptItemData)
			throws AgentJDBCException {

		Statement stmt = null;
		ResultSet rs = null;

		String sql_query = null; // 查询语句
		String sql_update = null; // 更新语句
		String sql_insert = null; // 插入语句

		try {
			// 初始话需要插入的表中字段名称
			prptItemData.initdataField();

			/*
			 * 根据对象中已经初始化的成员对象，组装sql
			 */
			sql_query = prptItemData.AssembleQuerySql();
			sql_update = prptItemData.AssembleUpdateSql();
			sql_insert = prptItemData.AssembleInsertSql();
		} catch (Exception e1) {
			
			e1.printStackTrace();
			throw new AgentJDBCException(CMISMessage.MESSAGEDEFAULT,
					"传入的RptItemData对象数据不完整不能取得sql语句");
		}
		try {

			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql_query);

			/*
			 * 查找该项记录是否在数据库中存在，存在则执行更新操作 不存在执行新增操作
			 */
			if (rs.next()) {
				stmt.execute(sql_update);
			} else {
				stmt.execute(sql_insert);
			}

			sql_query = null;
			sql_update = null;
			sql_insert = null;

		} catch (SQLException e) {
			
//			e.printStackTrace();
//			EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, e.getMessage());
			 if(logger.isDebugEnabled()){
	            	logger.debug(e.getMessage());
	         }
			throw new AgentJDBCException(CMISMessage.MESSAGEDEFAULT, "操作数据项["
					+ prptItemData.getItemId() + "]失败！");
		} finally {
			this.freeResource(stmt, rs);
		}

	}

	/**
	 * 从prptItemData对象中得到对应的sql，修改一条项目信息记录。 如果该年的记录不存在，执行新增操作。存在则执行更新操作
	 * 
	 * @param conn
	 *            数据库连接
	 * @param prptItemData
	 *            报表项目信息数据
	 * @throws AgentJDBCException
	 */
	public void updateRptItemData(Connection conn, RptItemData prptItemData)
			throws AgentJDBCException {

		Statement stmt = null;

		String sql_update = null; // 更新语句

		try {
			// 初始话需要插入的表中字段名称
			prptItemData.initdataField();
			/*
			 * 根据对象中已经初始化的成员对象，组装sql
			 */
			if("05".equals(prptItemData.getFncConfStyle())){
				sql_update = prptItemData.AssembleUpdateSql8();
			}else{
				sql_update = prptItemData.AssembleUpdateSql();
			}

		} catch (Exception e1) {
			
//			e1.printStackTrace();
			throw new AgentJDBCException(CMISMessage.MESSAGEDEFAULT,
					"传入的RptItemData对象数据不完整不能取得sql语句");
		}
		if(logger.isDebugEnabled()){
        	logger.debug(sql_update);
		}
//		EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, sql_update);
		try {

			stmt = conn.createStatement();

			/*
			 * 执行更新操作
			 */
			stmt.execute(sql_update);

			sql_update = null;

		} catch (SQLException e) {
			
//			e.printStackTrace();
			if(logger.isDebugEnabled()){
            	logger.debug(e.getMessage());
			}
//			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.getMessage());
			throw new AgentJDBCException(CMISMessage.MESSAGEDEFAULT, "操作数据项["
					+ prptItemData.getItemId() + "]失败！");
		} finally {
			this.freeResource(stmt, null);

		}

	}

	/**
	 * 根据传入报表项目信息数据对象中的客户ID,报表年份,表名更新数据
	 * 
	 * @param conn
	 *            数据库连接
	 * @param riData
	 *            报表项目信息数据
	 * @return 信息标志(具体参考CMISMessage中定义)
	 * @throws AgentJDBCException
	 */
	public String updateFncStatByItem(Connection conn, RptItemData riData)
			throws AgentJDBCException {
		String flagInfo = CMISMessage.DEFEAT; // 信息标识（默认失败）
		Statement stmt = null;

		String sql_update = null; // 更新语句
		try {
			// 组装更新语句
			sql_update = riData.AssembleFncStatSql();

		} catch (Exception e) {
			
			e.printStackTrace();
			throw new AgentJDBCException(CMISMessage.MESSAGEDEFAULT,
					"传入的RptItemData对象数据不完整" + "不能取得更新整张报表数据的sql语句");
		}
		// 写日志
		if(logger.isDebugEnabled()){
        	logger.debug(sql_update);
		}
//		EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "EXECUTE SQL"
//				+ sql_update);

		try {

			stmt = conn.createStatement();

			/*
			 * 执行更新操作
			 */
			stmt.execute(sql_update);

			sql_update = null;

			flagInfo = CMISMessage.SUCCESS;
		} catch (SQLException e) {
//			e.printStackTrace();
			if(logger.isDebugEnabled()){
            	logger.debug(e.getMessage());
			}
//			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.getMessage());
			throw new AgentJDBCException(CMISMessage.MESSAGEDEFAULT, "删除报表["
					+ riData.getTableName() + "]中客户ID为[" + riData.getCusId()
					+ "] 年份为[" + riData.getRptYear() + "]的数据失败");
		} finally {
			this.freeResource(stmt, null);

		}
		return flagInfo;
	}

	/**
	 * 更新报表状态
	 * 
	 * @param conn 数据库连接
	 * @param cusId 客户ID
	 * @param statPrdStyle 报表周期类型
	 * @param statPrd 报表期间
	 * @param StateFlg 状态
	 * @return 信息标志(具体参考CMISMessage中定义)
	 * @throws AgentJDBCException
	 */
	public String updateFncStatBaseInfo(Connection conn, String cusId,
			String statPrdStyle, String statPrd, String StateFlg,String statStyle,String fncType)
			throws AgentJDBCException {
		String flagInfo = CMISMessage.DEFEAT; // 信息标识（默认失败）

		Statement stmt = null;
		// 拼查询语句
		String sql = "UPDATE fnc_stat_base SET state_flg='" + StateFlg
				+ "' WHERE cus_id='" + cusId 
				+ "' AND stat_prd_style='" + statPrdStyle 
				+ "' AND stat_prd='" + statPrd + "'" 
				+ " AND stat_style='"+statStyle+"'"
				+ " AND fnc_type='"+fncType+"'";
		if(logger.isDebugEnabled()){
        	logger.debug(sql);
		}
		try {
			stmt = conn.createStatement();
			stmt.execute(sql);
			flagInfo = CMISMessage.SUCCESS;
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
            	logger.debug(e.getMessage());
			}
			throw new AgentJDBCException(CMISMessage.MESSAGEDEFAULT,"更新报表状态信息失败");
		} finally {
			this.freeResource(stmt, null);
		}

		return flagInfo;
	}

	/**
	 * 根据客户ID,"年份"查询出该报表所有的项目id
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cusId
	 *            客户ID
	 * @param statYear
	 *            报表年份
	 * @param tableName
	 *            表名
	 * @return 项目ID列表
	 * @throws AgentJDBCException
	 */
	public List getFncStatItemIdList(Connection conn, String cusId,
			String statYear, String tableName,String statStyle) throws AgentJDBCException {
		List<String> itemIdList = new ArrayList<String>();

		Statement stmt = null;
		ResultSet rs = null;

		if (tableName == null) {
			throw new AgentJDBCException(CMISMessage.MESSAGEDEFAULT,
					"未传入报表命名参数值");
		}

		String sql = "SELECT stat_item_id FROM " + tableName
				+ " WHERE cus_id='" + cusId + "' AND stat_year='" + statYear
				+ "' AND stat_style='"+statStyle+"'";

//		EMPLog.log(this.getClass().getName(), EMPLog.DEBUG, 0, "EXECUTE SQL"
//				+ sql);
		if(logger.isDebugEnabled()){
        	logger.debug(sql);
		}
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				if (rs.getString("stat_item_id") != null) {
					itemIdList
							.add((String) rs.getString("stat_item_id").trim());
				}
			}
		} catch (Exception e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
//			e.printStackTrace();
//			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.getMessage());
			throw new AgentJDBCException(CMISMessage.MESSAGEDEFAULT,
					"查询报表中存在的数据失败");
		} finally {
			this.freeResource(stmt, rs);
		}

		return itemIdList;
	}

	/**
	 * 执行批量Sql
	 * 
	 * @param conn
	 *            数据库连接
	 * @param sqlList
	 *            sql列表
	 * @return 信息标志(具体参考CMISMessage中定义)
	 * @throws AgentJDBCException
	 */
	public String optBatchSql(Connection conn, List sqlList)
			throws AgentJDBCException {
		String flagInfo = CMISMessage.DEFEAT; // 信息标识（默认失败）
		Statement stmt = null;

		try {
			stmt = conn.createStatement();

			if ((sqlList != null) && (sqlList.size() > 0)) {
				for (int i = 0; i < sqlList.size(); i++) {
					String sql = (String) sqlList.get(i);
					stmt.addBatch(sql);
				}
			}
			stmt.executeBatch();

		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
			e.printStackTrace();
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.getMessage());
			throw new AgentJDBCException(CMISMessage.MESSAGEDEFAULT,
					"执行批量sql失败");
		} finally {
			this.freeResource(stmt, null);
		}

		return flagInfo;
	}

	/**
	 * 组装标签样式对象(带数据的)
	 * 
	 * @param 客户编号
	 *            cusId
	 * @param 报表周期类型
	 *            statPrdStyle (1:月报 2:季报 3:半年报 4:年报)
	 * @param 报表期间
	 *            statPrd 格式：YYYYMM
	 * @param 表名
	 *            tableName
	 * @param 报表种类
	 *            fncConfTyp(01:资产负债表 02:损益表 03:现金流量 04:财务指标 05.所有者权益变动表 06财务简表)
	 * @param Connection
	 * @return FncConfStyles
	 */
	public FncConfStyles queryDetailFncConfStyles(String cusId,
			String statPrdStyle, String statPrd, String styleId,
			Connection conn, String tableName, String fncConfTyp,String stat_style)
			throws AgentJDBCException {

		FncConfStyles fncConfStyles = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String postfix = null; // 字段属性后缀的标示

		try {

			// 拆分报表期间statPrd
			String year = statPrd.substring(0, 4);
			String month = statPrd.substring(4);

			/**
			 * 根据报表周期类型判断该报表的类型.进行拼sql
			 */
			if ("1".equals(statPrdStyle)) { // 月报 _Amt6
				postfix = month;
				if (postfix.indexOf("0") == 0) {
					postfix = postfix.substring(1);
				}
			} else if ("2".equals(statPrdStyle)) { // 季报 _Amt_Q2
				postfix = FncNumber.getJibao(month);
			} else if ("3".equals(statPrdStyle)) { // 半年报 _Amt_Y1
				postfix = FncNumber.getBanNianBao(month);
			} else if ("4".equals(statPrdStyle)) { // 年报 _Amt_Y
				postfix = FncNumber.getNianBao(month);
			}

			/**
			 * 开始组装sql语句(根据报表类型组装)
			 */
			String sql = "";
			/*if("05".equals(fncConfTyp)){
				sql = this.getSqlForSoe(tableName, fncConfTyp, postfix);
			}else{*/
			sql = this.getSql_xyp(tableName, fncConfTyp, postfix);
			// System.out.println("sql="+sql);
			ps = conn.prepareStatement(sql);
			ps.setString(1, cusId);
			ps.setString(2, year);
			ps.setString(3, stat_style);
			ps.setString(4, styleId);
			rs = ps.executeQuery();

			List<FncConfDefFormat> items = new ArrayList<FncConfDefFormat>();
			FncConfDefFormat fdf = null;
			fncConfStyles = new FncConfStyles();
			while (rs.next()) {
				fdf = new FncConfDefFormat();
				fdf.setItemId(rs.getString("item_id"));
				fdf.setFncCnfAppRow(rs.getInt("fnc_cnf_app_row"));
				fdf.setFncConfCalFrm(rs.getString("fnc_conf_cal_frm"));

				fdf.setFncConfChkFrm(rs.getString("fnc_conf_chk_frm"));
				fdf.setFncConfCotes(rs.getInt("fnc_conf_cotes"));
				fdf.setFncConfDispAmt(rs.getDouble("fnc_conf_disp_amt"));

				fdf.setFncConfDispTpy(rs.getString("fnc_conf_disp_tpy"));
				fdf.setFncConfIndent(rs.getInt("fnc_conf_indent"));
				fdf.setFncConfOrder(rs.getInt("fnc_conf_order"));

				fdf.setFncConfPrefix(rs.getString("fnc_conf_prefix"));
				fdf.setFncConfRowFlg(rs.getString("fnc_conf_row_flg"));
				fdf.setFncItemEditTyp(rs.getString("fnc_item_edit_typ"));

				fdf.setItemName(rs.getString("item_name"));
				
				/*if ("04".equals(fncConfTyp)) {
					double avg = this.getAverage(rs.getString("item_id"),
							cusId, year, conn);
					fdf.setAverage(avg);
				}else*/ if( "05".equals(fncConfTyp)){
					double[] dataA = new double[8];  //用于盛放期初数据
					double[] dataB = new double[8];  //用于盛放期初数据
					dataA[0] = rs.getDouble("data3");
					dataA[1] = rs.getDouble("data5");
					dataA[2] = rs.getDouble("data7");
					dataA[3] = rs.getDouble("data9");
					dataA[4] = rs.getDouble("data11");
					dataA[5] = rs.getDouble("data13");
					dataA[6] = rs.getDouble("data15");
					dataA[7] = rs.getDouble("data1");
					
					dataB[0] = rs.getDouble("data4");
					dataB[1] = rs.getDouble("data6");
					dataB[2] = rs.getDouble("data8");
					dataB[3] = rs.getDouble("data10");
					dataB[4] = rs.getDouble("data12");
					dataB[5] = rs.getDouble("data14");
					dataB[6] = rs.getDouble("data16");
					dataB[7] = rs.getDouble("data2");
					fdf.setDataA(dataA);
					fdf.setDataB(dataB);
				}else{
					if(rs.getInt("fnc_conf_data_col")==1){
						fdf.setData1(rs.getDouble("data1"));
					}else if(rs.getInt("fnc_conf_data_col")==2){
						fdf.setData1(rs.getDouble("data1"));
						fdf.setData2(rs.getDouble("data2"));
					}
					
				}

				items.add(fdf);	
				
				fncConfStyles.setStyleId(styleId);
				fncConfStyles.setFncName(rs.getString("fnc_name"));
				fncConfStyles.setFncConfDisName(rs.getString("fnc_conf_dis_name"));
				fncConfStyles.setFncConfDataCol(rs.getInt("fnc_conf_data_col"));
				fncConfStyles.setFncConfCotes(rs.getInt("fnc_conf_cotes"));
				fncConfStyles.setFncConfTyp(rs.getString("fnc_conf_typ"));
				
			}
			
			
			fncConfStyles.setItems(items);

		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
//			e.printStackTrace();
//			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.getMessage());
			throw new AgentJDBCException(CMISMessage.MESSAGEDEFAULT,
					"查询报表基本信息失败");
		} finally {
			this.freeResource(ps, rs);
		}
		return fncConfStyles;
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	public FncConfStyles queryDetailFncConfStyles0(String cusId,
			String statPrdStyle, String statPrd, String styleId,
			Connection conn, String tableName, String fncConfTyp,String statStyle)
			throws AgentJDBCException {

		FncConfStyles fncConfStyles = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String postfix = null; // 字段属性后缀的标示

		try {

			// 拆分报表期间statPrd
			String year = statPrd.substring(0, 4);
			String month = statPrd.substring(4);

			/**
			 * 根据报表周期类型判断该报表的类型.进行拼sql
			 */
			if ("1".equals(statPrdStyle)) { // 月报 _Amt6
				postfix = month;
				if (postfix.indexOf("0") == 0) {
					postfix = postfix.substring(1);
				}
			} else if ("2".equals(statPrdStyle)) { // 季报 _Amt_Q2
				postfix = FncNumber.getJibao(month);
			} else if ("3".equals(statPrdStyle)) { // 半年报 _Amt_Y1
				postfix = FncNumber.getBanNianBao(month);
			} else if ("4".equals(statPrdStyle)) { // 年报 _Amt_Y
				postfix = FncNumber.getNianBao(month);
			}

			/**
			 * 开始组装sql语句(根据报表类型组装)
			 */
			String sql = this.getSql(tableName, fncConfTyp, postfix);
			// System.out.println("sql="+sql);
			ps = conn.prepareStatement(sql);
			ps.setString(1, cusId);
			ps.setString(2, year);
			ps.setString(3, statStyle);
			ps.setString(4, styleId);
			rs = ps.executeQuery();

			List<FncConfDefFormat> items = new ArrayList<FncConfDefFormat>();
			FncConfDefFormat fdf = null;
			fncConfStyles = new FncConfStyles();
			while (rs.next()) {
				fdf = new FncConfDefFormat();
				fdf.setItemId(rs.getString("item_id"));
				fdf.setFncCnfAppRow(rs.getInt("fnc_cnf_app_row"));
				fdf.setFncConfCalFrm(rs.getString("fnc_conf_cal_frm"));

				fdf.setFncConfChkFrm(rs.getString("fnc_conf_chk_frm"));
				fdf.setFncConfCotes(rs.getInt("fnc_conf_cotes"));
				fdf.setFncConfDispAmt(rs.getDouble("fnc_conf_disp_amt"));

				fdf.setFncConfDispTpy(rs.getString("fnc_conf_disp_tpy"));
				fdf.setFncConfIndent(rs.getInt("fnc_conf_indent"));
				fdf.setFncConfOrder(rs.getInt("fnc_conf_order"));

				fdf.setFncConfPrefix(rs.getString("fnc_conf_prefix"));
				fdf.setFncConfRowFlg(rs.getString("fnc_conf_row_flg"));
				fdf.setFncItemEditTyp(rs.getString("fnc_item_edit_typ"));

				fdf.setItemName(rs.getString("item_name"));
				fdf.setData1(rs.getDouble("data1"));
				if ("01".equals(fncConfTyp) || "02".equals(fncConfTyp)) {
					fdf.setData2(rs.getDouble("data2"));
				}

//				if ("04".equals(fncConfTyp)) {
//					double avg = this.getAverage(rs.getString("item_id"),
//							cusId, year, conn);
//					fdf.setAverage(avg);
//				}

				items.add(fdf);	
				
				fncConfStyles.setStyleId(styleId);
				fncConfStyles.setFncName(rs.getString("fnc_name"));
				fncConfStyles.setFncConfDisName(rs.getString("fnc_conf_dis_name"));
				fncConfStyles.setFncConfDataCol(rs.getInt("fnc_conf_data_col"));
				fncConfStyles.setFncConfCotes(rs.getInt("fnc_conf_cotes"));
				fncConfStyles.setFncConfTyp(rs.getString("fnc_conf_typ"));
				
			}
			
			
			fncConfStyles.setItems(items);

		} catch (SQLException e) {
//			e.printStackTrace();
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
//			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.getMessage());
			throw new AgentJDBCException(CMISMessage.MESSAGEDEFAULT,
					"查询报表基本信息失败");
		} finally {
			this.freeResource(ps, rs);
		}
		return fncConfStyles;
	}
	
	//////////////////////////////////////////////////////////////////////////////
	

	public List QueryItemsList(String cusId,
			String statPrdStyle, String statPrd, String styleId,
			Connection conn, String tableName, String fncConfTyp,String stat_style)
			throws AgentJDBCException {
		
		List<FncConfDefFormat> items = new ArrayList<FncConfDefFormat>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String postfix = null; // 字段属性后缀的标示

		try {

			// 拆分报表期间statPrd
			String year = statPrd.substring(0, 4);
			String month = statPrd.substring(4);

			/**
			 * 根据报表周期类型判断该报表的类型.进行拼sql
			 */
			if ("1".equals(statPrdStyle)) { // 月报 _Amt6
				postfix = month;
				if (postfix.indexOf("0") == 0) {
					postfix = postfix.substring(1);
				}
			} else if ("2".equals(statPrdStyle)) { // 季报 _Amt_Q2
				postfix = FncNumber.getJibao(month);
			} else if ("3".equals(statPrdStyle)) { // 半年报 _Amt_Y1
				postfix = FncNumber.getBanNianBao(month);
			} else if ("4".equals(statPrdStyle)) { // 年报 _Amt_Y
				postfix = FncNumber.getNianBao(month);
			}

			/**
			 * 开始组装sql语句(根据报表类型组装)
			 */
			String sql = "";
			if("05".equals(fncConfTyp)){
				sql = this.getSqlForSoe(tableName, fncConfTyp, postfix);
			}else{
				sql = this.getSql_new(tableName, fncConfTyp, postfix);
			}

			ps = conn.prepareStatement(sql);
			ps.setString(1, cusId);
			ps.setString(2, year);
			
			ps.setString(3, stat_style);
			ps.setString(4, styleId);
			rs = ps.executeQuery();

			
			FncConfDefFormat fdf = null;
			while (rs.next()) {
				fdf = new FncConfDefFormat();
				fdf.setItemId(rs.getString("item_id"));
				fdf.setFncCnfAppRow(rs.getInt("fnc_cnf_app_row"));
				fdf.setFncConfCalFrm(rs.getString("fnc_conf_cal_frm"));

				fdf.setFncConfChkFrm(rs.getString("fnc_conf_chk_frm"));
				fdf.setFncConfCotes(rs.getInt("fnc_conf_cotes"));
				fdf.setFncConfDispAmt(rs.getDouble("fnc_conf_disp_amt"));

				fdf.setFncConfDispTpy(rs.getString("fnc_conf_disp_tpy"));
				fdf.setFncConfIndent(rs.getInt("fnc_conf_indent"));
				fdf.setFncConfOrder(rs.getInt("fnc_conf_order"));

				fdf.setFncConfPrefix(rs.getString("fnc_conf_prefix"));
				fdf.setFncConfRowFlg(rs.getString("fnc_conf_row_flg"));
				fdf.setFncItemEditTyp(rs.getString("fnc_item_edit_typ"));

				fdf.setItemName(rs.getString("item_name"));
				//fdf.setData1(rs.getDouble("data1"));
				//if ("01".equals(fncConfTyp) || "02".equals(fncConfTyp)) {
				//	fdf.setData2(rs.getDouble("data2"));
				//}
				/*if ("04".equals(fncConfTyp)) {
					double avg = this.getAverage(rs.getString("item_id"),
							cusId, year, conn);
					fdf.setAverage(avg);
				}else*/ if( "05".equals(fncConfTyp)){
					double[] dataA = new double[8];  //用于盛放期初数据
					double[] dataB = new double[8];  //用于盛放期初数据
					dataA[0] = rs.getDouble("data3");
					dataA[1] = rs.getDouble("data5");
					dataA[2] = rs.getDouble("data7");
					dataA[3] = rs.getDouble("data9");
					dataA[4] = rs.getDouble("data11");
					dataA[5] = rs.getDouble("data13");
					dataA[6] = rs.getDouble("data15");
					dataA[7] = rs.getDouble("data1");
					
					dataB[0] = rs.getDouble("data4");
					dataB[1] = rs.getDouble("data6");
					dataB[2] = rs.getDouble("data8");
					dataB[3] = rs.getDouble("data10");
					dataB[4] = rs.getDouble("data12");
					dataB[5] = rs.getDouble("data14");
					dataB[6] = rs.getDouble("data16");
					dataB[7] = rs.getDouble("data2");
					fdf.setDataA(dataA);
					fdf.setDataB(dataB);
				}else{
					if("04".equals(fncConfTyp)){
						fdf.setData1(rs.getDouble("data1"));
					}else{
						fdf.setData1(rs.getDouble("data1"));
						fdf.setData2(rs.getDouble("data2"));
					}
					
				}

				items.add(fdf);	
			}

		} catch (SQLException e) {
//			e.printStackTrace();
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
//			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.getMessage());
			throw new AgentJDBCException(CMISMessage.MESSAGEDEFAULT,
					"查询报表基本信息失败");
		} finally {
			this.freeResource(ps, rs);
		}
		return items;
		
	}
	
	public String getSql_new(String tableName, String fncConfTyp, String postfix) {

		StringBuffer sb = new StringBuffer(); // 用于存放拼成的sql
		sb.append("SELECT f.item_id,f.fnc_conf_order,f.fnc_conf_cotes,f.fnc_conf_row_flg,f.fnc_conf_indent,"
						+ "f.fnc_conf_prefix,f.fnc_item_edit_typ,f.fnc_cnf_app_row,f.fnc_conf_disp_tpy,f.fnc_conf_disp_amt,"
						+ "f.fnc_conf_chk_frm,f.fnc_conf_cal_frm,n.item_name,");
		if ("01".equals(fncConfTyp) || "02".equals(fncConfTyp) 
				|| "03".equals(fncConfTyp) || "06".equals(fncConfTyp)) {
			sb.append("b.stat_init_amt" + postfix + " as data1, b.stat_end_amt"
					+ postfix + "  as data2 ");
		} else if ("04".equals(fncConfTyp)) {
			sb.append("b.stat_init_amt" + postfix + "  as data1 ");
		}
		sb.append(" FROM fnc_conf_def_fmt f ");
		sb.append(" LEFT JOIN "+ tableName + " b ON b.stat_item_id=f.item_id and b.cus_Id = ? AND b.stat_year=? AND b.stat_style=? ");
		sb.append(" LEFT JOIN fnc_conf_items n ON f.item_id=n.item_id ");
		sb.append(" WHERE f.style_Id = ? AND f.fnc_item_edit_typ IN ('0','1','2')  ORDER BY f.fnc_conf_cotes ASC, f.fnc_conf_order ASC ");
		// sb.append(" WHERE f.style_Id = ?  ORDER BY f.fnc_conf_cotes ASC, f.fnc_conf_order ASC ");
		return sb.toString();
	}
	
	
	public String getSqlForSoe(String tableName, String fncConfTyp, String postfix){
		StringBuffer sb = new StringBuffer(); // 用于存放拼成的sql
		sb.append("SELECT f.item_id,f.fnc_conf_order,f.fnc_conf_cotes,f.fnc_conf_row_flg,f.fnc_conf_indent,"
						+ "f.fnc_conf_prefix,f.fnc_item_edit_typ,f.fnc_cnf_app_row,f.fnc_conf_disp_tpy,f.fnc_conf_disp_amt,"
						+ "f.fnc_conf_chk_frm,f.fnc_conf_cal_frm,n.item_name,");
		if ("05".equals(fncConfTyp)) {
			sb.append("b.stat_init_amt" + postfix + " as data1, b.STAT_END_AMT"      //所有者权益合计
					+ postfix + "  as data2, ");
			sb.append("b.STAT_INIT_AMTA" + postfix + " as data3, b.STAT_END_AMTA"   //实收资本（股本）
					+ postfix + "  as data4, ");
			sb.append("b.STAT_INIT_AMTB" + postfix + " as data5, b.STAT_END_AMTB"   //资本公积
					+ postfix + "  as data6, ");
			sb.append("b.STAT_INIT_AMTC" + postfix + " as data7, b.STAT_END_AMTC"   //减：库存股
					+ postfix + "  as data8, ");
			sb.append("b.STAT_INIT_AMTD" + postfix + " as data9, b.STAT_END_AMTD"   //盈余公积期
					+ postfix + "  as data10, ");
			sb.append("b.STAT_INIT_AMTE" + postfix + " as data11, b.STAT_END_AMTE"  //未分配利润
					+ postfix + "  as data12, ");
			sb.append("b.STAT_INIT_AMTF" + postfix + " as data13, b.STAT_END_AMTF"  //外币报表折算差额
					+ postfix + "  as data14, ");
			sb.append("b.STAT_INIT_AMTG" + postfix + " as data15, b.STAT_END_AMTG"  //少数股东权益
					+ postfix + "  as data16 ");
		}
		sb.append(" FROM fnc_conf_def_fmt f ");
		sb.append(" LEFT JOIN "+ tableName + " b ON b.stat_item_id=f.item_id and b.cus_Id = ? AND b.stat_year=? AND b.stat_style=? ");
		sb.append(" LEFT JOIN fnc_conf_items n ON f.item_id=n.item_id ");
		sb.append(" WHERE f.style_Id = ? AND f.fnc_item_edit_typ IN ('0','1','2')  ORDER BY f.fnc_conf_cotes ASC, f.fnc_conf_order ASC ");
		// sb.append(" WHERE f.style_Id = ?  ORDER BY f.fnc_conf_cotes ASC, f.fnc_conf_order ASC ");
		return sb.toString();
	}
	
	/**
	 * 组装sql
	 * 
	 * @param tableName
	 * @param fncConfTyp
	 * @param postfix
	 * @return
	 */
	public String getSql(String tableName, String fncConfTyp, String postfix) {

		StringBuffer sb = new StringBuffer(); // 用于存放拼成的sql
		sb.append("SELECT f.item_id,f.fnc_conf_order,f.fnc_conf_cotes,f.fnc_conf_row_flg,f.fnc_conf_indent,"
						+ "f.fnc_conf_prefix,f.fnc_item_edit_typ,f.fnc_cnf_app_row,f.fnc_conf_disp_tpy,f.fnc_conf_disp_amt,"
						+ "f.fnc_conf_chk_frm,f.fnc_conf_cal_frm,n.item_name,");
		if ("01".equals(fncConfTyp) || "02".equals(fncConfTyp)|| "05".equals(fncConfTyp)) {
			sb.append("b.stat_init_amt" + postfix + " as data1, b.stat_end_amt" + postfix + "  as data2,");
		} else if ("03".equals(fncConfTyp) || "04".equals(fncConfTyp) || "06".equals(fncConfTyp)) {
			sb.append("b.stat_init_amt" + postfix + "  as data1, ");
		}
		sb.append(" m.fnc_name,m.fnc_conf_dis_name,m.fnc_conf_data_col,m.fnc_conf_cotes,m.fnc_conf_typ ");
		sb.append(" FROM fnc_conf_def_fmt f ");
		sb.append(" LEFT JOIN "
						+ tableName
						+ " b ON b.stat_item_id=f.item_id and b.cus_Id = ? AND b.stat_year=?  AND b.stat_style=? ");
		sb.append(" LEFT JOIN fnc_conf_items n ON f.item_id=n.item_id ");
		sb.append(" LEFT JOIN fnc_conf_styles m ON f.style_id=m.style_id ");
		sb.append(" WHERE f.style_Id = ? AND f.fnc_item_edit_typ IN ('0','1','2')  ORDER BY f.fnc_conf_cotes ASC, f.fnc_conf_order ASC ");
		// sb.append(" WHERE f.style_Id = ?  ORDER BY f.fnc_conf_cotes ASC, f.fnc_conf_order ASC ");
		return sb.toString();
	}
	
	public String getSql_xyp(String tableName, String fncConfTyp, String postfix) {

		StringBuffer sb = new StringBuffer(); // 用于存放拼成的sql
		sb.append("SELECT f.item_id,f.fnc_conf_order,f.fnc_conf_cotes,f.fnc_conf_row_flg,f.fnc_conf_indent,"
						+ "f.fnc_conf_prefix,f.fnc_item_edit_typ,f.fnc_cnf_app_row,f.fnc_conf_disp_tpy,f.fnc_conf_disp_amt,"
						+ "f.fnc_conf_chk_frm,f.fnc_conf_cal_frm,n.item_name,");
		if ("01".equals(fncConfTyp) || "02".equals(fncConfTyp) || "06".equals(fncConfTyp)||"03".equals(fncConfTyp)) {
			sb.append("b.stat_init_amt" + postfix + " as data1, b.stat_end_amt"
					+ postfix + "  as data2,");
		} else if ( "04".equals(fncConfTyp)) {
			sb.append("b.stat_init_amt" + postfix + "  as data1, ");
		}else if("05".equals(fncConfTyp)){
			sb.append("b.stat_init_amt" + postfix + " as data1, b.STAT_END_AMT"      //所有者权益合计
					+ postfix + "  as data2, ");
			sb.append("b.STAT_INIT_AMTA" + postfix + " as data3, b.STAT_END_AMTA"   //实收资本（股本）
					+ postfix + "  as data4, ");
			sb.append("b.STAT_INIT_AMTB" + postfix + " as data5, b.STAT_END_AMTB"   //资本公积
					+ postfix + "  as data6, ");
			sb.append("b.STAT_INIT_AMTC" + postfix + " as data7, b.STAT_END_AMTC"   //减：库存股
					+ postfix + "  as data8, ");
			sb.append("b.STAT_INIT_AMTD" + postfix + " as data9, b.STAT_END_AMTD"   //盈余公积期
					+ postfix + "  as data10, ");
			sb.append("b.STAT_INIT_AMTE" + postfix + " as data11, b.STAT_END_AMTE"  //未分配利润
					+ postfix + "  as data12, ");
			sb.append("b.STAT_INIT_AMTF" + postfix + " as data13, b.STAT_END_AMTF"  //外币报表折算差额
					+ postfix + "  as data14, ");
			sb.append("b.STAT_INIT_AMTG" + postfix + " as data15, b.STAT_END_AMTG"  //少数股东权益
					+ postfix + "  as data16 ,");
		}
		sb.append(" m.fnc_name,m.fnc_conf_dis_name,m.fnc_conf_data_col,m.fnc_conf_cotes,m.fnc_conf_typ ");
		sb.append(" FROM fnc_conf_def_fmt f ");
		sb.append(" LEFT JOIN "
						+ tableName
						+ " b ON b.stat_item_id=f.item_id and b.cus_Id = ? AND b.stat_year=?  AND b.stat_style=? ");
		sb.append(" LEFT JOIN fnc_conf_items n ON f.item_id=n.item_id ");
		sb.append(" LEFT JOIN fnc_conf_styles m ON f.style_id=m.style_id ");
		sb.append(" WHERE f.style_Id = ? AND f.fnc_item_edit_typ IN ('0','1','2','3')  ORDER BY f.fnc_conf_cotes ASC, f.fnc_conf_order ASC ");
		// sb.append(" WHERE f.style_Id = ?  ORDER BY f.fnc_conf_cotes ASC, f.fnc_conf_order ASC ");
		return sb.toString();
	}

	/**
	 * 得到一个客户报表基本信息对象
	 * 
	 * @param fStatBase 报表基本信息对象
	 * @param Connection
	 * @return 报表基本信息对象
	 * @throws EMPException 
	 */
	public FncStatBase queryDetailFncStatBase(FncStatBase fStatBase, Connection conn) throws EMPException {

		FncStatBase fncStatBase = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String cusId = fStatBase.getCusId();
		String statPrdStyle = fStatBase.getStatPrdStyle();
		String statPrd = fStatBase.getStatPrd();
		String stat_style = fStatBase.getStatStyle();
		String fncType = fStatBase.getFncType();
		try {
			String sql = "select cus_id,stat_prd_style,stat_prd,stat_bs_style_id,stat_pl_style_id,stat_cf_style_id,"
					+ "stat_fi_style_id,stat_soe_style_id,stat_sl_style_id,style_id1,style_id2,state_flg,"
					+ "stat_is_nrpt,stat_style,stat_is_audit,stat_adt_entr,stat_adt_conc,stat_adj_rsn,"
					+ "input_id,input_br_id,input_date,last_upd_id,last_upd_date"
					+ " from Fnc_Stat_Base where cus_id = ? and stat_prd_style = ? and stat_prd = ?"
					+ " and stat_style=? and fnc_type=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, cusId);
			ps.setString(2, statPrdStyle);
			ps.setString(3, statPrd);
			ps.setString(4, stat_style);
			ps.setString(5, fncType);
			if(logger.isDebugEnabled()){
	        	logger.debug(sql);
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				fncStatBase = new FncStatBase();
				fncStatBase.setCusId(cusId);
				fncStatBase.setStatPrdStyle(statPrdStyle);
				fncStatBase.setStatPrd(statPrd);
				fncStatBase.setStatStyle(stat_style);

				fncStatBase.setStatBsStyleId(rs.getString("STAT_BS_STYLE_ID"));// 资产样式编号
				fncStatBase.setStatPlStyleId(rs.getString("STAT_PL_STYLE_ID"));// 损益表编号
				fncStatBase.setStatCfStyleId(rs.getString("STAT_CF_STYLE_ID"));// 现金流量表编号
				fncStatBase.setStatFiStyleId(rs.getString("STAT_FI_STYLE_ID"));// 财务指标表编号
				fncStatBase.setStatSlStyleId(rs.getString("stat_sl_style_id"));// 财务简表编号
				fncStatBase.setStatSoeStyleId(rs.getString("stat_soe_style_id"));// 所有者权益表编号
				fncStatBase.setStateFlg(rs.getString("STATE_FLG"));// 报表状态
				fncStatBase.setInputDate(rs.getString("input_date"));
				fncStatBase.setInputId(rs.getString("input_id"));
				fncStatBase.setInputBrId(rs.getString("input_br_id"));
				
				ComponentHelper cHelper = new ComponentHelper();
				KeyedCollection kColl = cHelper.domain2kcol(fncStatBase, "FncStatBase");
				SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
				SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id"});
				fncStatBase.setInputId((String) kColl.get("input_id_displayname"));
				fncStatBase.setInputBrId((String) kColl.get("input_br_id_displayname"));
			}
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
			throw new AgentJDBCException(CMISMessage.MESSAGEDEFAULT, "查询报表基本信息失败");
		} finally {
			this.freeResource(ps, rs);
		}
		return fncStatBase;
	}

	/**
	 * 查看在tableName表中有没存在该条记录
	 * 
	 * @param tableName
	 * @param cusId
	 * @param stat_year
	 * @param stat_item_id
	 * @param conn
	 * @return int 有反回1,没有返回0
	 */
	public int queryOneFncConfStyles(String tableName, String cusId,
			String stat_year, String stat_item_id, Connection conn,String stat_style) {
		Statement ps = null;
		ResultSet rs = null;
		int count = 0;
		String sql = "select * from " + tableName + " where stat_item_id='"
				+ stat_item_id + "' and cus_Id = '" + cusId
				+ "' and stat_year='" + stat_year + "' and stat_style='"+stat_style+"'";
		try {
			ps = conn.createStatement();
			rs = ps.executeQuery(sql);
			if (rs.next()) {
				count++;
			}
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
//			e.printStackTrace();
		} finally {
			this.freeResource(ps, rs);
		}
		return count;
	}
	
	public int addOneFncConfStyles(String sql0, Connection conn) {
		Statement ps = null;
		int count = 0;
		String sql = sql0;
		try {
			ps = conn.createStatement();
			boolean f = ps.execute(sql);
			if(f){
				count = 1;
			}

		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
//			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}
	/**
	 * 查看有没有该客户的报表信息
	 * @param tableName
	 * @param cusId
	 * @param stat_year
	 * @param conn
	 * @return
	 */
	public int findOneFncConfStyles(String tableName, String cusId,
			String stat_year,  Connection conn,String stat_style) {
		Statement ps = null;
		ResultSet rs = null;
		int count = 0;
		String sql = "select * from " + tableName + " where cus_Id = '" + cusId
				+ "' and stat_year='" + stat_year + "' and stat_style='"+stat_style+"'";
		try {
			ps = conn.createStatement();
			rs = ps.executeQuery(sql);
			if (rs.next()) {
				count++;
			}
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
//			e.printStackTrace();
		} finally {
			this.freeResource(ps, rs);
		}
		return count;

	}

	/**
	 * 
	 * @param fncStatBs
	 * @param conn
	 * @return
	 */
	public FncStatBs QueryFncStatBs(FncStatBs fncStatBs, Connection conn) {
		FncStatBs tempBs = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = "select CUS_ID,STAT_YEAR,STAT_ITEM_ID,STAT_STYLE from FNC_STAT_BS where CUS_ID='"
				+ fncStatBs.getCusId()
				+ "'"
				+ " and STAT_YEAR='"
				+ fncStatBs.getStatYear()
				+ "' and STAT_ITEM_ID='"
				+ fncStatBs.getStatItemId() + "'"
				+ " and STAT_STYLE='"+fncStatBs.getStatStyle()+"'";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				tempBs = new FncStatBs();
				tempBs.setCusId(rs.getString("CUS_ID"));
				tempBs.setStatYear(rs.getString("STAT_YEAR"));
				tempBs.setStatItemId(rs.getString("STAT_ITEM_ID"));
				tempBs.setStatStyle(rs.getString("STAT_STYLE"));
			}
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
			e.printStackTrace();
		} finally {
			this.freeResource(st, rs);
		}
		return tempBs;
	}

	/**
	 * 
	 * @param fncStatBs
	 * @param conn
	 * @return
	 */
	public FncStatCfs QueryFncStatCfs(FncStatCfs fncStatCfs, Connection conn) {
		FncStatCfs tempCfs = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = "select CUS_ID,STAT_YEAR,STAT_ITEM_ID,STAT_STYLE from FNC_STAT_CFS where CUS_ID='"
				+ fncStatCfs.getCusId()
				+ "'"
				+ " and STAT_YEAR='"
				+ fncStatCfs.getStatYear()
				+ "' and STAT_ITEM_ID='"
				+ fncStatCfs.getStatItemId() + "'"
				+ " and STAT_STYLE='"+fncStatCfs.getStatStyle()+"'";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				tempCfs = new FncStatCfs();
				tempCfs.setCusId(rs.getString("CUS_ID"));
				tempCfs.setStatYear(rs.getString("STAT_YEAR"));
				tempCfs.setStatItemId(rs.getString("STAT_ITEM_ID"));
				tempCfs.setStatStyle(rs.getString("STAT_STYLE"));
			}
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
			e.printStackTrace();
		} finally {
			this.freeResource(st, rs);
		}
		return tempCfs;
	}

	/**
	 * 
	 * @param fncIndexRpt
	 * @param conn
	 * @return
	 */
	public FncIndexRpt QueryFncIndexRpt(FncIndexRpt fncIndexRpt, Connection conn) {
		FncIndexRpt tempFi = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = "select CUS_ID,STAT_YEAR,STAT_ITEM_ID,STAT_STYLE from FNC_INDEX_RPT where CUS_ID='"
				+ fncIndexRpt.getCusId()
				+ "'"
				+ " and STAT_YEAR='"
				+ fncIndexRpt.getStatYear()
				+ "' and STAT_ITEM_ID='"
				+ fncIndexRpt.getStatItemId() + "'"
				+ " and STAT_STYLE='"+fncIndexRpt.getStatStyle()+"'";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				tempFi = new FncIndexRpt();
				tempFi.setCusId(rs.getString("CUS_ID"));
				tempFi.setStatYear(rs.getString("STAT_YEAR"));
				tempFi.setStatItemId(rs.getString("STAT_ITEM_ID"));
				tempFi.setStatStyle(rs.getString("STAT_STYLE"));
			}
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
//			e.printStackTrace();
		} finally {
			this.freeResource(st, rs);
		}
		return tempFi;
	}

	/**
	 * 
	 * @param fncStatIs
	 * @param conn
	 * @return
	 */
	public FncStatIs QueryFncStatIs(FncStatIs fncStatIs, Connection conn) {
		FncStatIs tempIs = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = "select CUS_ID,STAT_YEAR,STAT_ITEM_ID,STAT_STYLE from FNC_STAT_IS where CUS_ID='"
				+ fncStatIs.getCusId()
				+ "'"
				+ " and STAT_YEAR='"
				+ fncStatIs.getStatYear()
				+ "' and STAT_ITEM_ID='"
				+ fncStatIs.getStatItemId() + "'"
				+" and STAT_STYLE='"+fncStatIs.getStatStyle()+"'";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				tempIs = new FncStatIs();
				tempIs.setCusId(rs.getString("CUS_ID"));
				tempIs.setStatYear(rs.getString("STAT_YEAR"));
				tempIs.setStatItemId(rs.getString("STAT_ITEM_ID"));
				tempIs.setStatStyle(rs.getString("STAT_STYLE"));
			}
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
//			e.printStackTrace();
		} finally {
			this.freeResource(st, rs);
		}
		return tempIs;
	}

	/**
	 * 
	 * @param fncStatSl
	 * @param conn
	 * @return
	 */
	public FncStatSl QueryFncStatSl(FncStatSl fncStatSl, Connection conn) {
		FncStatSl tempSl = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = "select CUS_ID,STAT_YEAR,STAT_ITEM_ID,STAT_STYLE from FNC_STAT_SL where CUS_ID='"
				+ fncStatSl.getCusId()
				+ "'"
				+ " and STAT_YEAR='"
				+ fncStatSl.getStatYear()
				+ "' and STAT_ITEM_ID='"
				+ fncStatSl.getStatItemId() + "'"
				+ " and STAT_STYLE='"+fncStatSl.getStatStyle()+"'";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				tempSl = new FncStatSl();
				tempSl.setCusId(rs.getString("CUS_ID"));
				tempSl.setStatYear(rs.getString("STAT_YEAR"));
				tempSl.setStatItemId(rs.getString("STAT_ITEM_ID"));
				tempSl.setStatStyle(rs.getString("STAT_STYLE"));
			}
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
//			e.printStackTrace();
		} finally {
			this.freeResource(st, rs);
		}
		return tempSl;
	}

	/**
	 * 
	 * @param fncStatSoe
	 * @param conn
	 * @return
	 */
	public FncStatSoe QueryFncStatSoe(FncStatSoe fncStatSoe, Connection conn) {
		FncStatSoe tempSl = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = "select CUS_ID,STAT_YEAR,STAT_ITEM_ID,STAT_STYLE from FNC_STAT_SOE where CUS_ID='"
				+ fncStatSoe.getCusId()
				+ "'"
				+ " and STAT_YEAR='"
				+ fncStatSoe.getStatYear()
				+ "' and STAT_ITEM_ID='"
				+ fncStatSoe.getStatItemId() + "'"
				+ " and STAT_STYLE='"+fncStatSoe.getStatStyle()+"'";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				tempSl = new FncStatSoe();
				tempSl.setCusId(rs.getString("CUS_ID"));
				tempSl.setStatYear(rs.getString("STAT_YEAR"));
				tempSl.setStatItemId(rs.getString("STAT_ITEM_ID"));
				tempSl.setStatStyle(rs.getString("STAT_STYLE"));
			}
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
//			e.printStackTrace();
		} finally {
			this.freeResource(st, rs);
		}
		return tempSl;
	}

	/**
	 * 
	 * @param fncStatBs
	 * @param conn
	 * @return
	 * @throws DaoException 
	 */
	public String insertFncStatBs(FncStatBs fncStatBs, Connection conn) throws DaoException {
		String flagInfo = CMISMessage.DEFEAT; // 错误信息（默认失败）
		PreparedStatement pt = null;
		String sql = "insert into FNC_STAT_BS(CUS_ID, STAT_YEAR, STAT_ITEM_ID,STAT_STYLE, STAT_INIT_AMT1, STAT_END_AMT1,"
				+ " STAT_INIT_AMT2, STAT_END_AMT2, STAT_INIT_AMT3, STAT_END_AMT3, STAT_INIT_AMT4, STAT_END_AMT4, "
				+ " STAT_INIT_AMT5, STAT_END_AMT5, STAT_INIT_AMT6, STAT_END_AMT6, STAT_INIT_AMT7, STAT_END_AMT7, "
				+ " STAT_INIT_AMT8, STAT_END_AMT8, STAT_INIT_AMT9, STAT_END_AMT9, STAT_INIT_AMT10, STAT_END_AMT10, "
				+ " STAT_INIT_AMT11, STAT_END_AMT11, STAT_INIT_AMT12, STAT_END_AMT12, STAT_INIT_AMT_Q1, "
				+ " STAT_END_AMT_Q1, STAT_INIT_AMT_Q2, STAT_END_AMT_Q2, STAT_INIT_AMT_Q3, STAT_END_AMT_Q3, "
				+ " STAT_INIT_AMT_Q4, STAT_END_AMT_Q4,STAT_INIT_AMT_Y1, STAT_END_AMT_Y1, STAT_INIT_AMT_Y2, "
				+ " STAT_END_AMT_Y2, STAT_INIT_AMT_Y, STAT_END_AMT_Y) values('"
				+ fncStatBs.getCusId()
				+ "',"
				+ "'"
				+ fncStatBs.getStatYear()
				+ "','"
				+ fncStatBs.getStatItemId()
				+ "','"
				+ fncStatBs.getStatStyle()
				+ "',0,0,0,0,0,0,0"
				+ ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0"
				+ ",0,0,0,0,0,0,0,0,0,0,0)";
		try {
			pt = conn.prepareStatement(sql);
			// pt.setString(1, fncStatBs.getCusId());
			// pt.setString(2, fncStatBs.getStatYear());
			// pt.setString(3, fncStatBs.getStatItemId());
			int flg = pt.executeUpdate();
			if (flg > 0) {
				flagInfo = CMISMessage.SUCCESS;
			}

			pt.close();
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
			throw new DaoException("插入资产负债表错误，错误信息："+e.getMessage());
		}
		return flagInfo;
	}

	/**
	 * 
	 * @param fncStatCfs
	 * @param conn
	 * @return
	 * @throws DaoException 
	 */
	public String insertFncStatCfs(FncStatCfs fncStatCfs, Connection conn) throws DaoException {
		String flagInfo = CMISMessage.DEFEAT; // 错误信息（默认失败）
		Statement st = null;
		String sql = "insert into FNC_STAT_CFS (CUS_ID, STAT_YEAR, STAT_ITEM_ID,STAT_STYLE, STAT_INIT_AMT1, STAT_INIT_AMT2, "
				+ " STAT_INIT_AMT3, STAT_INIT_AMT4, STAT_INIT_AMT5, STAT_INIT_AMT6, STAT_INIT_AMT7, STAT_INIT_AMT8, "
				+ " STAT_INIT_AMT9, STAT_INIT_AMT10, STAT_INIT_AMT11, STAT_INIT_AMT12, STAT_INIT_AMT_Q1, "
				+ " STAT_INIT_AMT_Q2, STAT_INIT_AMT_Q3, STAT_INIT_AMT_Q4, STAT_INIT_AMT_Y1, STAT_INIT_AMT_Y2, STAT_INIT_AMT_Y) "
				+ " values ('"
				+ fncStatCfs.getCusId()
				+ "','"
				+ fncStatCfs.getStatYear()
				+ "','"
				+ fncStatCfs.getStatItemId()
				+ "','"
				+ fncStatCfs.getStatStyle()
				+ "'," + "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)";
		try {
			st = conn.createStatement();
			int flg = st.executeUpdate(sql);
			if (flg > 0) {
				flagInfo = CMISMessage.SUCCESS;
			}

			st.close();
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
			throw new DaoException("插入现金流量表错误，错误信息："+e.getMessage());
//			e.printStackTrace();
		}
		return flagInfo;
	}

	/**
	 * 
	 * @param fncIndexRpt
	 * @param conn
	 * @return
	 * @throws DaoException 
	 */
	public String insertFncIndexRpt(FncIndexRpt fncIndexRpt, Connection conn) throws DaoException {
		String flagInfo = CMISMessage.DEFEAT; // 错误信息（默认失败）
		Statement st = null;
		String sql = "insert into FNC_INDEX_RPT (CUS_ID, STAT_YEAR, STAT_ITEM_ID,STAT_STYLE, STAT_INIT_AMT1, STAT_INIT_AMT2, "
				+ " STAT_INIT_AMT3, STAT_INIT_AMT4, STAT_INIT_AMT5, STAT_INIT_AMT6, STAT_INIT_AMT7, STAT_INIT_AMT8, "
				+ " STAT_INIT_AMT9, STAT_INIT_AMT10, STAT_INIT_AMT11, STAT_INIT_AMT12, STAT_INIT_AMT_Q1, "
				+ " STAT_INIT_AMT_Q2, STAT_INIT_AMT_Q3, STAT_INIT_AMT_Q4, STAT_INIT_AMT_Y1, STAT_INIT_AMT_Y2, STAT_INIT_AMT_Y) "
				+ " values ('"
				+ fncIndexRpt.getCusId()
				+ "','"
				+ fncIndexRpt.getStatYear()
				+ "','"
				+ fncIndexRpt.getStatItemId()
				+ "','"
				+ fncIndexRpt.getStatStyle()
				+ "',"
				+ "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)";
		try {
			st = conn.createStatement();
			int flg = st.executeUpdate(sql);
			if (flg > 0) {
				flagInfo = CMISMessage.SUCCESS;
			}

			st.close();
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
			throw new DaoException("插入财务指标表错误，错误信息："+e.getMessage());
//			e.printStackTrace();
		}
		return flagInfo;
	}

	/**
	 * 
	 * @param fncStatIs
	 * @param conn
	 * @return
	 * @throws DaoException 
	 */
	public String insertFncStatIs(FncStatIs fncStatIs, Connection conn) throws DaoException {
		String flagInfo = CMISMessage.DEFEAT; // 错误信息（默认失败）
		Statement st = null;
		String sql = "insert into FNC_STAT_IS (CUS_ID, STAT_YEAR, STAT_ITEM_ID,STAT_STYLE, STAT_INIT_AMT1, STAT_END_AMT1, "
				+ " STAT_INIT_AMT2, STAT_END_AMT2, STAT_INIT_AMT3, STAT_END_AMT3, STAT_INIT_AMT4, STAT_END_AMT4, "
				+ " STAT_INIT_AMT5, STAT_END_AMT5, STAT_INIT_AMT6, STAT_END_AMT6, STAT_INIT_AMT7, STAT_END_AMT7, "
				+ " STAT_INIT_AMT8, STAT_END_AMT8, STAT_INIT_AMT9, STAT_END_AMT9, STAT_INIT_AMT10, STAT_END_AMT10, "
				+ " STAT_INIT_AMT11, STAT_END_AMT11, STAT_INIT_AMT12, STAT_END_AMT12, STAT_INIT_AMT_Q1, "
				+ " STAT_END_AMT_Q1, STAT_INIT_AMT_Q2, STAT_END_AMT_Q2, STAT_INIT_AMT_Q3, STAT_END_AMT_Q3, "
				+ " STAT_INIT_AMT_Q4, STAT_END_AMT_Q4, STAT_INIT_AMT_Y1, STAT_END_AMT_Y1, STAT_INIT_AMT_Y2, "
				+ " STAT_END_AMT_Y2, STAT_INIT_AMT_Y, STAT_END_AMT_Y) "
				+ " values('"
				+ fncStatIs.getCusId()
				+ "',"
				+ "'"
				+ fncStatIs.getStatYear()
				+ "','"
				+ fncStatIs.getStatItemId()
				+ "','"
				+ fncStatIs.getStatStyle()
				+ "',0,0,0,0,0,0,0"
				+ ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0"
				+ ",0,0,0,0,0,0,0,0,0,0,0)";
		try {
			st = conn.createStatement();
			int flg = st.executeUpdate(sql);
			if (flg > 0) {
				flagInfo = CMISMessage.SUCCESS;
			}

			st.close();
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
			throw new DaoException("插入损益表错误，错误信息："+e.getMessage());
//			e.printStackTrace();
		}
		return flagInfo;
	}

	/**
	 * 新增财务简表信息
	 * 
	 * @param fncStatSl
	 * @param conn
	 * @return
	 * @throws DaoException 
	 */
	public String insertFncStatSl(FncStatSl fncStatSl, Connection conn) throws DaoException {
		String flagInfo = CMISMessage.DEFEAT; // 错误信息（默认失败）
		Statement st = null;
		String sql = "insert into FNC_STAT_SL (CUS_ID, STAT_YEAR, STAT_ITEM_ID,STAT_STYLE, STAT_INIT_AMT1, STAT_INIT_AMT2, "
				+ " STAT_INIT_AMT3, STAT_INIT_AMT4, STAT_INIT_AMT5, STAT_INIT_AMT6, STAT_INIT_AMT7, STAT_INIT_AMT8, "
				+ " STAT_INIT_AMT9, STAT_INIT_AMT10, STAT_INIT_AMT11, STAT_INIT_AMT12, STAT_INIT_AMT_Q1, STAT_INIT_AMT_Q2, "
				+ " STAT_INIT_AMT_Q3, STAT_INIT_AMT_Q4, STAT_INIT_AMT_Y1, STAT_INIT_AMT_Y2, STAT_INIT_AMT_Y) "
				+ " values ('"
				+ fncStatSl.getCusId()
				+ "','"
				+ fncStatSl.getStatYear()
				+ "','"
				+ fncStatSl.getStatItemId()
				+ "','"
				+ fncStatSl.getStatStyle()
				+ "'," + "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)";
		try {
			st = conn.createStatement();
			int flg = st.executeUpdate(sql);
			if (flg > 0) {
				flagInfo = CMISMessage.SUCCESS;
			}

			st.close();
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
			throw new DaoException("插入财务简表错误，错误信息："+e.getMessage());
//			e.printStackTrace();
		}
		return flagInfo;
	}

	/**
	 * 
	 * @param fncStatSoe
	 * @param conn
	 * @return
	 */
	public String insertFncStatSoe(FncStatSoe fncStatSoe, Connection conn) {
		String flagInfo = CMISMessage.DEFEAT; // 错误信息（默认失败）
		Statement st = null;
		String sql = "insert into FNC_STAT_SOE (CUS_ID, STAT_YEAR, STAT_ITEM_ID,STAT_STYLE, STAT_INIT_AMT1, STAT_INIT_AMT2, "
				+ " STAT_INIT_AMT3, STAT_INIT_AMT4, STAT_INIT_AMT5, STAT_INIT_AMT6, STAT_INIT_AMT7, STAT_INIT_AMT8, "
				+ " STAT_INIT_AMT9, STAT_INIT_AMT10, STAT_INIT_AMT11, STAT_INIT_AMT12, STAT_INIT_AMT_Q1, STAT_INIT_AMT_Q2, "
				+ " STAT_INIT_AMT_Q3, STAT_INIT_AMT_Q4, STAT_INIT_AMT_Y1, STAT_INIT_AMT_Y2, STAT_INIT_AMT_Y) "
				+ " values ('"
				+ fncStatSoe.getCusId()
				+ "','"
				+ fncStatSoe.getStatYear()
				+ "','"
				+ fncStatSoe.getStatItemId()
				+ "','"
				+ fncStatSoe.getStatStyle()
				+ "'," + "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)";
		try {
			st = conn.createStatement();
			int flg = st.executeUpdate(sql);
			if (flg > 0) {
				flagInfo = CMISMessage.SUCCESS;
			}

			st.close();
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
//			e.printStackTrace();
		}
		return flagInfo;
	}

	public void freeResource(Statement stmt, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public FncStatBase getOneFncStatBase(FncStatBase fncStatBase, Connection conn) {
		FncStatBase tempBase = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = "select STAT_BS_STYLE_ID,STAT_PL_STYLE_ID,STAT_CF_STYLE_ID,STAT_FI_STYLE_ID,STAT_SOE_STYLE_ID,STAT_SL_STYLE_ID,STATE_FLG,FNC_TYPE"
				+ " from FNC_STAT_BASE where CUS_ID='" + fncStatBase.getCusId() + "'"
				+ " and STAT_PRD_STYLE='" + fncStatBase.getStatPrdStyle()
				+ "' and STAT_PRD='" + fncStatBase.getStatPrd() + "'"
				+ " and STAT_STYLE='"+fncStatBase.getStatStyle()+"'"
				+ " and FNC_TYPE='"+fncStatBase.getFncType()+"'";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				tempBase = new FncStatBase();

				tempBase.setCusId(fncStatBase.getCusId());
				tempBase.setStatPrd(fncStatBase.getStatPrd());
				tempBase.setStatPrdStyle(fncStatBase.getStatPrdStyle());
				tempBase.setStatStyle(fncStatBase.getStatStyle());

				tempBase.setStatBsStyleId(rs.getString("STAT_BS_STYLE_ID"));
				tempBase.setStatPlStyleId(rs.getString("STAT_PL_STYLE_ID"));
				tempBase.setStatCfStyleId(rs.getString("STAT_CF_STYLE_ID"));
				tempBase.setStatFiStyleId(rs.getString("STAT_FI_STYLE_ID"));
				tempBase.setStatSoeStyleId(rs.getString("STAT_SOE_STYLE_ID"));
				tempBase.setStatSlStyleId(rs.getString("STAT_SL_STYLE_ID"));
				tempBase.setFncType(rs.getString("FNC_TYPE"));
				tempBase.setStateFlg(rs.getString("STATE_FLG"));

			}
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
//			e.printStackTrace();
		} finally {
			this.freeResource(st, rs);
		}

		return tempBase;
	}

	public FncConfStyles getData(FncStatBase tempBase,Connection conn,String styleType,String tableName){
		FncConfStyles fs = new FncConfStyles();
		Statement st = null;
		PreparedStatement ps = null;
		String styleId = "";
		if("01".equals(styleType)){
			styleId = tempBase.getStatBsStyleId();
		}else if("02".equals(styleType)){
			styleId = tempBase.getStatPlStyleId();
		}else if("03".equals(styleType)){
			styleId = tempBase.getStatCfStyleId();
		}else if("04".equals(styleType)){
			styleId = tempBase.getStatFiStyleId();
		}else if("05".equals(styleType)){
			styleId = tempBase.getStatSoeStyleId();
		}else if("06".equals(styleType)){
			styleId = tempBase.getStatSlStyleId();
		}
		try {
//			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
//			"读取数据库中的财务科目数据START..." );
			if(logger.isInfoEnabled()){
				logger.info("读取数据库中的财务科目数据START...");
			}
			
			String sql = "select STYLE_ID,FNC_NAME,FNC_CONF_DIS_NAME,FNC_CONF_TYP,FNC_CONF_DATA_COL,FNC_CONF_COTES " +
						 "from FNC_CONF_STYLES where STYLE_ID = '" + styleId + "'";
			ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				
				fs.setStyleId(rs.getString(1));
				fs.setFncName(rs.getString(2));
				fs.setFncConfDisName(rs.getString(3));
				fs.setFncConfTyp(rs.getString(4));
				fs.setFncConfDataCol(rs.getInt(5));
				fs.setFncConfCotes(rs.getInt(6));
				
				//读取数据库中的报表配置定义表中的信息
				List<FncConfDefFormat> list = new ArrayList<FncConfDefFormat>();
				try {
					list = this.QueryItemsList(tempBase.getCusId(), tempBase.getStatPrdStyle(), tempBase.getStatPrd(), styleId, 
							conn, tableName, styleType,tempBase.getStatStyle());
				} catch (AgentJDBCException e) {
					e.printStackTrace();
				}

				fs.setItems(list);
			}
			
			ps.close();
			rs.close();
			if(logger.isInfoEnabled()){
	        	logger.info("读取数据库中的财务科目数据END");
			}
//			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
//			"读取数据库中的财务科目数据END..." );
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug("读据失败:"+e.getMessage());
			}
//			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
//			"读取数据库中的财务科目数据失败！" );
//				e.printStackTrace();
	
		}
		return fs;
	}
	
	public String updateFncStatFlg(String tempState, Connection conn,
			FncStatBase tempBase) throws SQLException {
		String flagInfo = CMISMessage.DEFEAT+"|"+tempBase.getStateFlg(); // 默认失败
		String sql = "update fnc_stat_base set STATE_FLG = '" + tempState+"',LAST_UPD_DATE='"+tempBase.getLastUpdDate()+"',LAST_UPD_ID='"+tempBase.getLastUpdId()
				+ "' where cus_id='" + tempBase.getCusId() + "' "
				+ "and STAT_PRD_STYLE='" + tempBase.getStatPrdStyle()
				+ "'  and STAT_PRD = '" + tempBase.getStatPrd() + "'"
				+ " and stat_style='"+tempBase.getStatStyle()+"' and fnc_type='"+tempBase.getFncType()+"'";
		Statement st = null;
		int count = 0;
		try {
			st = conn.createStatement();
			count = st.executeUpdate(sql);
			if (count > 0) {
				flagInfo = CMISMessage.SUCCESS+"|"+tempState;
			}
			st.close();
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
			throw new SQLException(e.getMessage());
		}
		return flagInfo;
	}

	/**
	 * 根据查询对象查询报表的item的值
	 * @param fq
	 * @param conn
	 * @return
	 */
	public double QueryItemValue(Fnc4Query fq, Connection conn) {
		double rv = 0;

		FncStatBs tempBs = null;
		Statement st = null;
		ResultSet rs = null;

		String cusId = fq.getCusId();
		String fncType = fq.getFncType(); // 财报类型01:资产负债表 02:损益表 03:现金流量 04:财务指标 05.所有者权益变动表 06财务简表
		String itemId = fq.getItemId(); // 财务指标编号
		String termType = fq.getTermType(); // 报表周期类型  1 月报 2 季报 3 半年报 4 年报
		String vDate = fq.getVDate(); //日期

		String queryField = this.getItemValueQuaryField(fncType, vDate, termType);

		// 评级一般是财务指标表的年 STAT_INIT_AMT_Y
		String sql = "select "+queryField+"  from  ";
		
		
		if(fncType.equals(FNCPubConstant.BS)){
			sql=sql+FNCPubConstant.BS_TBALE_NAME;
		}else if(fncType.equals(FNCPubConstant.IS)){
			sql=sql+FNCPubConstant.IS_TABLE_NAME;
		}else if(fncType.equals(FNCPubConstant.CFS)){
			sql=sql+FNCPubConstant.CFS_TABLE_NAME;
		}else if(fncType.equals(FNCPubConstant.SL)){
			sql=sql+FNCPubConstant.SL_TABLE_NAME;
		}else if(fncType.equals(FNCPubConstant.SOE)){
			sql=sql+FNCPubConstant.SOE_TABLE_NAME;
		}else if(fncType.equals(FNCPubConstant.IND)){
			sql=sql+FNCPubConstant.IND_TABLE_NAME;
		}
		
		// SELECT STAT_INIT_AMT_Y FROM FNC_INDEX_RPT 
		
		sql=sql+" where CUS_ID='"
				+ cusId
				+ "'"
				+ " and STAT_YEAR='"
				+ TimeUtil.getCurYear(vDate)
				+ "' and STAT_ITEM_ID='"
				+ itemId
				+ "' and stat_style='"+fq.getStatStyle()+"'";
		
		/*容错处理，暂不使用
		 * if(null==fq.getStatStyle()){
			sql=sql	+ "'";	
		}else{
			sql=sql	+ "' and stat_style='"+fq.getStatStyle()+"'";
		}
		*/
		
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				rv=rs.getDouble(queryField);
			}
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
//			e.printStackTrace();
		} finally {
			this.freeResource(st, rs);
		}
		return rv;
	}

	/**
	 * 财务分析与接口使用，获取需要查询具体item值所在字段
	 * @param fncType
	 * @param vDate
	 * @param termType
	 * @return
	 */
	public String getItemValueQuaryField(String fncType, String vDate,
			String termType) {
		String rv = "";
		String m = TimeUtil.getCurMonth(vDate);
		String y = TimeUtil.getCurYear(vDate);
		String d = "";
		if (vDate.length() == 8) {
			d = TimeUtil.getCurDay(vDate);
		}

		if ( fncType.equals(FNCPubConstant.IND)
				|| fncType.equals(FNCPubConstant.CFS)
				|| fncType.equals(FNCPubConstant.SL)
				|| fncType.equals(FNCPubConstant.SOE)) {//所有者权益变动表暂时不支持 分项的财务分析只支持汇总
			switch (Integer.parseInt(termType)) {
			case 1:// 月
				rv = FNCPubConstant.STAT_INIT_AMT + TimeUtil.removeZero(m);
				break;
			case 2:// 季
				rv = FNCPubConstant.STAT_INIT_AMT_Q
						+ TimeUtil.getQuarter(TimeUtil.removeZero(m));
				break;
			case 3:// 半年
				rv = FNCPubConstant.STAT_INIT_AMT_Y
						+ TimeUtil.getHelfYear(TimeUtil.removeZero(m));
				break;
			case 4:// 年
				rv = FNCPubConstant.STAT_INIT_AMT_Y;
				break;
			default:
			}
		} else if (fncType.equals(FNCPubConstant.BS)||fncType.equals(FNCPubConstant.IS)) {
			switch (Integer.parseInt(termType)) {
			case 1:// 月
				if (d.length() == 0 ||  "30".equals(d)) {
					rv = FNCPubConstant.STAT_END_AMT;
				} else {
					rv = FNCPubConstant.STAT_INIT_AMT;
				}
				rv = rv + TimeUtil.removeZero(m);
				break;
			case 2:// 季
				if (d.length() == 0 || "30".equals(d)) {
					rv = FNCPubConstant.STAT_END_AMT_Q;
				} else {
					rv = FNCPubConstant.STAT_INIT_AMT_Q;
				}
				rv = rv + TimeUtil.getQuarter(TimeUtil.removeZero(m));
				break;
			case 3:// 半年
				if (d.length() == 0 || "30".equals(d)) {
					rv = FNCPubConstant.STAT_END_AMT_Y;
				} else {
					rv = FNCPubConstant.STAT_INIT_AMT_Y;
				}
				rv = rv + TimeUtil.getHelfYear(TimeUtil.removeZero(m));
				break;
			case 4:// 年
				if (d.length() == 0 || "30".equals(d)) {
					rv = FNCPubConstant.STAT_END_AMT_Y;
				} else {
					rv = FNCPubConstant.STAT_INIT_AMT_Y;
				}
				break;
			default:
			}

		} 
		return rv;
	}
	
	/**
	   * 以下是财报导入导出使用
	   */
	  /**
	   * 根据客户编号获取客户的财务报表类型
	   */
	public String getComFinRepType(String cusId,Connection connection){
		String repType = "";
		String fncName = "";
		Statement st = null;
		ResultSet rs = null;
		String sql = "select c.com_fin_rep_type,b.fnc_name from (select a.cus_id,a.com_fin_rep_type from cus_com a union all select b.cus_id,b.com_fin_rep_type  from cus_same_org b) c,FNC_CONF_TEMPLATE B" +
				" where c.CUS_ID = '"+cusId+"' and c.COM_FIN_REP_TYPE=B.FNC_ID";
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			if(rs.next()){
				repType = rs.getString("COM_FIN_REP_TYPE");
				fncName = rs.getString("FNC_NAME");
			}
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
//			e.printStackTrace();
		}
		return repType;
  }
	
	public FncIndexRpt getIndexValue(String cusId,String itemId,String statStyle,Connection conn){
		FncIndexRpt fncIndexRpt = null;
		String sql = "select " +
				" STAT_YEAR," +
				" STAT_INIT_AMT1," +
				" STAT_INIT_AMT2," +
				" STAT_INIT_AMT3," +
				" STAT_INIT_AMT4," +
				" STAT_INIT_AMT5," +
				" STAT_INIT_AMT6," +
				" STAT_INIT_AMT7," +
				" STAT_INIT_AMT8," +
				" STAT_INIT_AMT9," +
				" STAT_INIT_AMT10," +
				" STAT_INIT_AMT11," +
				" STAT_INIT_AMT12," +
				" STAT_INIT_AMT_Q1," +
				" STAT_INIT_AMT_Q2," +
				" STAT_INIT_AMT_Q3," +
				" STAT_INIT_AMT_Q4," +
				" STAT_INIT_AMT_Y1," +
				" STAT_INIT_AMT_Y2," +
				" STAT_INIT_AMT_Y from fnc_index_rpt where CUS_ID='"+cusId+"'" +
				" and STAT_ITEM_ID='"+itemId+"'" +
						" and STAT_STYLE='"+statStyle+"'" +
								" order by STAT_YEAR desc";
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(rs.next()){
				fncIndexRpt = new FncIndexRpt();
				fncIndexRpt.setCusId(cusId);
				fncIndexRpt.setStatItemId(itemId);
				fncIndexRpt.setStatStyle(statStyle);	
				fncIndexRpt.setStatYear(rs.getString("STAT_YEAR"));
				
				fncIndexRpt.setStatInitAmt1(rs.getDouble("STAT_INIT_AMT1"));
				fncIndexRpt.setStatInitAmt2(rs.getDouble("STAT_INIT_AMT2"));
				fncIndexRpt.setStatInitAmt3(rs.getDouble("STAT_INIT_AMT3"));
				fncIndexRpt.setStatInitAmt4(rs.getDouble("STAT_INIT_AMT4"));
				fncIndexRpt.setStatInitAmt5(rs.getDouble("STAT_INIT_AMT5"));
				fncIndexRpt.setStatInitAmt6(rs.getDouble("STAT_INIT_AMT6"));
				fncIndexRpt.setStatInitAmt7(rs.getDouble("STAT_INIT_AMT7"));
				fncIndexRpt.setStatInitAmt8(rs.getDouble("STAT_INIT_AMT8"));
				fncIndexRpt.setStatInitAmt9(rs.getDouble("STAT_INIT_AMT9"));
				fncIndexRpt.setStatInitAmt10(rs.getDouble("STAT_INIT_AMT10"));
				fncIndexRpt.setStatInitAmt11(rs.getDouble("STAT_INIT_AMT11"));
				fncIndexRpt.setStatInitAmt12(rs.getDouble("STAT_INIT_AMT12"));
				
				fncIndexRpt.setStatInitAmtQ1(rs.getDouble("STAT_INIT_AMT_Q1"));
				fncIndexRpt.setStatInitAmtQ2(rs.getDouble("STAT_INIT_AMT_Q2"));
				fncIndexRpt.setStatInitAmtQ3(rs.getDouble("STAT_INIT_AMT_Q3"));
				fncIndexRpt.setStatInitAmtQ4(rs.getDouble("STAT_INIT_AMT_Q4"));
				
				fncIndexRpt.setStatInitAmtY1(rs.getDouble("STAT_INIT_AMT_Y1"));
				fncIndexRpt.setStatInitAmtY2(rs.getDouble("STAT_INIT_AMT_Y2"));
				
				fncIndexRpt.setStatInitAmtY(rs.getDouble("STAT_INIT_AMT_Y"));
			}
			
			rs.close();
			st.close();
		} catch (SQLException e) {
			if(logger.isDebugEnabled()){
	        	logger.debug(e.getMessage());
			}
//			e.printStackTrace();
		}
		return fncIndexRpt;
	}
	
	/**
	 * 得到一个客户报表数目
	 * 
	 * @param fStatBase
	 *            报表基本信息对象
	 * @param Connection
	 * @return int 记录数
	 */
	public HashMap<String, String> queryCountFncStatBase(String cusId,String statPrdStyle,String statStyle,
			Connection conn) throws AgentJDBCException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap<String, String> rq=new HashMap<String, String>();
		int count = 0;
		
		try {
			String sql = "select cus_id,stat_prd_style,stat_prd,stat_bs_style_id,stat_pl_style_id,stat_cf_style_id,"
					+ "stat_fi_style_id,stat_soe_style_id,stat_sl_style_id,style_id1,style_id2,state_flg,"
					+ "stat_is_nrpt,stat_style,stat_is_audit,stat_adt_entr,stat_adt_conc,stat_adj_rsn,"
					+ "input_id,input_br_id,input_date,last_upd_id,last_upd_date"
					+ " from Fnc_Stat_Base where cus_id = ? and stat_prd_style = ? and stat_Style=? and substr(STATE_FLG,9)='2' order by stat_prd desc";
			ps = conn.prepareStatement(sql);
			ps.setString(1, cusId);
			ps.setString(2, statPrdStyle);
			ps.setString(3, statStyle);

			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "EXECUTE SQL"
					+ sql);

			rs = ps.executeQuery();
			while(rs.next()) {
				count++;
				rq.put(Integer.toString(count), rs.getString("stat_prd"));
			}

		} catch (SQLException e) {
			EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, e.toString());
			throw new AgentJDBCException(CMISMessage.MESSAGEDEFAULT, "查询报表基本信息失败");
		} finally {
			this.freeResource(ps, rs);
		}
		return rq;
	}
}