package com.yucheng.cmis.biz01line.cont.pub.sequence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.session.SessionException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.PUBConstant;

public class CMISSequenceService4Cont {
	/**
	 * 获取数据库连接池
	 * @param context
	 * @return
	 * @throws EMPException
	 */
	protected static Connection getConnection(Context context) throws EMPException {
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		if (dataSource == null){
			throw new EMPException("获取数据库连接!");
		}
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			throw new EMPException(e);
		}
		EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0,
				"Apply new connection from data source: " + dataSource
						+ " success!");
		return connection;
	}

	/**
	 * 释放数据库连接
	 * @param context
	 * @param connection
	 * @throws EMPException
	 * @throws EMPJDBCException
	 * @throws SessionException
	 */
	protected static void releaseConnection(Connection connection) throws EMPException {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new EMPException(e);
		}
	}
	/**
	 * 关闭连接
	 * @param stmt
	 * @param rs
	 */
	private static void closeResource(PreparedStatement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	private static String replace(String strSource, String strFrom, String strTo) {
		if (strSource == null) {
			return null;
		}
		int i = 0;
		if ((i = strSource.indexOf(strFrom, i)) >= 0) {
			char[] cSrc = strSource.toCharArray();
			char[] cTo = strTo.toCharArray();
			int len = strFrom.length();
			StringBuffer buf = new StringBuffer(cSrc.length);
			buf.append(cSrc, 0, i).append(cTo);
			i += len;
			int j = i;
			while ((i = strSource.indexOf(strFrom, i)) > 0) {
				buf.append(cSrc, j, i - j).append(cTo);
				i += len;
				j = i;
			}
			buf.append(cSrc, j, cSrc.length - j);
			return buf.toString();
		}
		return strSource;
	}

	/**
	 * 在纯数字前补0 转换成 长度为5位的字符串（如果数字大于100000，则取数字尾数）。 如
	 * 123-->00123，9999-->09999，100123123-->23123
	 * @param value
	 * @return
	 */
	private static String numFormatToSeq(int len, int value) {
		// 如果要改动返回字符串长度，改动maxValue(返回几位数字，则尾数改为几个0)
		if (len <= 0)
			return "";
		// 补足指定长度的0
		StringBuffer seq = new StringBuffer();
		for (int i = 0; i < len; i++) {
			seq.append('0');
		}
		seq.append(value);
		return seq.substring(seq.length() - len);
	}

	/**
	 * 提供合同号生成服务（泉州银行）
	 * @param aType 类型
	 * @param owner 所有者
	 * @param bizType 业务分类
	 * @param connection 数据库连接
	 * @param context 上下文
	 * @return
	 * @throws EMPException
	 */
	public static String querySequenceFromDB(String aType, String owner,String bizType, Connection connection, Context context) throws EMPException {
		String sqlStr = null;
		Connection conn = getConnection(context);
		PreparedStatement state = null;
		String cur_sernum = null; // 序列号
		String initcycle = null; // 1--序列号递增
		String zero_flg = null; // 序列号长度
		String serStyle = null; // 序列号样式
		String org = "";
		/*
		 * 流程中取不到申请机构
		 */
		try {
			if(context.containsKey("inputOrg")&&context.getDataValue("inputOrg")!=null){
				org = context.getDataValue("inputOrg").toString();
			}else{
				org = context.getDataValue("organNo").toString();
			}
		} catch (Exception e1) {
		}

		String date = context.getDataValue(PUBConstant.OPENDAY).toString(); // 营业日期
		ResultSet rs = null;
		try {
			String year_short = date.substring(2, 4);// 年份后两位
			String year = date.substring(0, 4);
			String month = date.substring(5, 7);

			sqlStr = "select cur_sernum,initcycle,zero_flg,sequencestyle from s_autocode where atype=? and owner=?";
			state = conn.prepareStatement(sqlStr);
			state.setObject(1, aType);
			state.setObject(2, owner);
			rs = state.executeQuery();
			if (rs.next()) {
				cur_sernum = rs.getString(1);
				initcycle = rs.getString(2);
				zero_flg = rs.getString(3);
				serStyle = rs.getString(4);
			}
			closeResource(state, rs);

			if (cur_sernum != null && "1".equals(initcycle)) {
				sqlStr = "select cur_sernum from s_autocode where atype=? and owner=? for update";
				state = conn.prepareStatement(sqlStr);
				state.setObject(1, aType);
				state.setObject(2, owner);
				rs = state.executeQuery();
				if (rs.next()) {
					cur_sernum = rs.getString(1);
				}

				closeResource(state, rs);

				sqlStr = "update s_autocode set cur_sernum=? where  atype=? and owner=?";
				state = conn.prepareStatement(sqlStr);

				long num = Long.valueOf(cur_sernum).longValue();
				num = num + 1;
				state.setObject(1, Long.valueOf(num));

				state.setObject(2, aType);
				state.setObject(3, owner);
				state.executeUpdate();

				closeResource(state, null);

			} else {// 若没有相关的流水号配置，则抛出异常
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0,
						"Can not found the sequence style for aType[" + aType
								+ "] and owner[" + owner + "]");
				throw new EMPException(
						"Can not found the sequence number for aType[" + aType
								+ "] and owner[" + owner + "]");
			}
			if (serStyle == null || "".equals(serStyle)) {
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0,
						"Can not found the sequence style for aType[" + aType
								+ "] and owner[" + owner + "]");
				throw new EMPException(
						"Can not found the sequence style for aType[" + aType
								+ "] and owner[" + owner + "]");
			}

			/* 组装序列号长度 */
			cur_sernum = numFormatToSeq(Integer.parseInt(zero_flg), Integer
					.parseInt(cur_sernum));
			date = replace(date, "-", "");
			/**
			 * 组装序列号生成器
			 */
			serStyle = replace(serStyle, "$ORG$", org);
			serStyle = replace(serStyle, "$YEAR$", year);
			serStyle = replace(serStyle, "$MONTH$", month);
			serStyle = replace(serStyle, "$SER$", cur_sernum);
			serStyle = replace(serStyle, "$DATE$", date);
			serStyle = replace(serStyle, "$ORGAPP$", owner);
			serStyle = replace(serStyle, "${BIZTYPE}", bizType);
			serStyle = replace(serStyle, "${OWNER}", org);
			serStyle = replace(serStyle, "${MONTH}", month);
			serStyle = replace(serStyle, "${CURYEAR_SHORT}", year_short);
			serStyle = replace(serStyle, "${1F}", cur_sernum);

			return serStyle;
		} catch (Exception e) {
			EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0,
					"The CMISSequenceService4Oracle occur an error:"
							+ e.getMessage());
			throw new EMPException(e);
		} finally {
			closeResource(state, rs);
			if (conn != null)
				releaseConnection(conn);
		}
	}
}
