package com.yucheng.cmis.pub.sequence;
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

/**
 *@Classname	CMISSequenceService4JXXD.java
 *@Version 1.0	
 *@Since   1.0 	Jan 26, 2010 
 *@Copyright 	yuchengtech
 *@Author 		eric
 *@Description：统计的序列号生成器 理论是支持所有类型 申请号 合同号 中文合同编号的生成
 *				对配置表initcycle 要求是一般都配成 1  能够自动增长的方式
 *				目前只对 $ORG$ -- 机构  $YEAR$ -- 当前年份 $MONTH$ --当前月份 
 *				$SER$ -- 当前的序列号 $DATE$ --营业日期 这些字符提供解析能力  
 *				如果受客户方特殊需求的需要其他特殊字段成为 一个属性的时候 需要自己写方法解析 你说写的 特殊属性的解析方法
 *				比如 ： SEQUENCESTYLE中的字符串为 "SQ%ORG%$YEAR$SER" 系统根据用户的机构 1000119 当前营业日期年份为、
 *				2009 序列号为28 长度 5位  那么生成的序列号为 SQ1000119200900028
 *				 SEQUENCESTYLE中的字符串为 "嘉银借字[$SER$]号" 当前序列号为 346 长度为5位 那么生成的序列号为：
 *				嘉银借字[00346]号
 *				如果存在上述不存在的 特殊属性 如 SQ$NAME$$YEAR$SER 那么需要重写方法 为NAME的取值提供解析方法
 *				所有的属性都要 以'$'开头  '$'结尾
 *
 *
 *				必须保证你所生成的序列号 一定要小于等于 你所赋值的字段 的长度！！！
 *@Lastmodified Feb 3 2010
 *@Description  新增标识  $ORGAPP$ --申请机构 解决流程审批结束后不能按照申请机构生成序列号的问题
 *				如果数据库里面配置的标识为 $ORGAPP$ 需要传入的owner为申请机构 owner 会替换$ORGAPP$标识
 *
 *				例如s_autocode 配置的样式为 SQ$ORGAPP$ 调用时出入的owner 为1001 那么生成的序列号为 SQ1001
 *				
 *@Author	    eric
 */
public class CMISSequenceService4JXXD {
	private static String defaultOwner = "0000";
	/**
	 * 获取数据库连接
	 * @param context
	 * @return
	 * @throws EMPException 
	 * @throws SQLException 
	 * @throws EMPJDBCException
	 * @throws SessionException
	 * @throws Exception 
	 */
	protected static Connection getConnection(Context context) throws EMPException{
		DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		if (dataSource == null)
			throw new EMPException("获取数据库连接!");
		Connection connection = null;
		try {
			connection =dataSource.getConnection();
		} catch (SQLException e) {
			throw new EMPException(e);
		
		}
		EMPLog.log( EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0, "Apply new connection from data source: "+dataSource+" success!");
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
	protected static void releaseConnection(Connection connection) throws EMPException{
		try {
			connection.close();
		} catch (SQLException e) {
              throw		new  EMPException(e);
		}
	}
	/**
	 * 根据序列号类型 序列号所有者 确定唯一的序列号生成样式 并返回该样式的序列号
	 * @param aType	序列号类型
	 * @param owner	序列号所有者
	 * @param connection	数据库
	 * @param context		
	 * @return
	 * @throws Exception 
	 * @throws Exception 
	 */
	public static String querySequenceFromDB(String aType, String owner, Connection connection , Context context) throws EMPException {
		String sqlStr = null;
		Connection conn = getConnection(context);
		PreparedStatement state = null;	
		String cur_sernum = null;	//序列号
		String initcycle = null;	//1--序列号递增
		String zero_flg  = null;	// 序列号长度 
		String serStyle = null;		//序列号样式
		String org = "";
		/*
		 * 流程中取不到申请机构
		 */
		try {
			org = context.getDataValue("organNo").toString();
		} catch (Exception e1) {}

		String date     =  context.getDataValue(PUBConstant.OPENDAY).toString();	//营业日期
		ResultSet rs = null;
		try{					
			String year_short = date.substring(2,4);//年份后两位
			String year = date.substring(0, 4);
			String month = date.substring(5, 7);
			
			sqlStr = "select cur_sernum,initcycle,zero_flg,sequencestyle from s_autocode where atype=? and owner=?";
			state = conn.prepareStatement(sqlStr);
			state.setObject(1, aType);
			state.setObject(2, owner);
			rs = state.executeQuery();
			if(rs.next()){
				cur_sernum = rs.getString(1);
				initcycle = rs.getString(2);
				zero_flg  = rs.getString(3);
				serStyle = rs.getString(4);
			}
			closeResource(state, rs);
			
			if (cur_sernum != null && "1".equals(initcycle)) {
				sqlStr = "select cur_sernum from s_autocode where atype=? and owner=? " ;
				state = conn.prepareStatement(sqlStr);
				state.setObject(1, aType);
				state.setObject(2, owner);
				rs = state.executeQuery();
				if(rs.next()){
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
			
				
			}else {// 若没有相关的流水号配置，则抛出异常
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "Can not found the sequence style for aType[" + aType + "] and owner[" + owner + "]");
				throw new EMPException("Can not found the sequence number for aType[" + aType + "] and owner[" + owner + "]");
			}
			if(serStyle==null||"".equals(serStyle)){
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "Can not found the sequence style for aType[" + aType + "] and owner[" + owner + "]");
				throw new EMPException("Can not found the sequence style for aType[" + aType + "] and owner[" + owner + "]");
			}
			
			/*组装序列号长度*/
			cur_sernum =numFormatToSeq(Integer.parseInt(zero_flg), Integer.parseInt(cur_sernum));
			date =  replace(date,"-", "");
			/**
			 * 组装序列号生成器
			 */
			if(org==null||"".equals(org)){
				org = "9999";
			}
			org = "00"+org;
			serStyle = replace(serStyle,"$ORG$",org);
			serStyle = replace(serStyle,"$YEAR$", year);
			serStyle = replace(serStyle,"$MONTH$", month);
			serStyle = replace(serStyle,"$SER$", cur_sernum);
			serStyle = replace(serStyle,"$DATE$", date);
			serStyle = replace(serStyle,"$ORGAPP$", owner);
			serStyle = replace(serStyle,"${OWNER}", org);
			serStyle = replace(serStyle,"${CURYEAR_SHORT}", year_short);
			serStyle = replace(serStyle,"${1F}", cur_sernum);
			
			
			return serStyle;
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "The CMISSequenceService4Oracle occur an error:"+e.getMessage());
			throw new EMPException(e);
		}finally {
			closeResource(state, rs);
			if (conn != null)
				releaseConnection(conn);
		}
	}
	
	/**
	 * 根据序列号类型 序列号所有者 确定唯一的序列号生成样式 并返回该样式的序列号
	 * @param aType	序列号类型
	 * @param owner	序列号所有者
	 * @param connection	数据库
	 * @param context		
	 * @return
	 * @throws Exception 
	 * @throws Exception 
	 */
	public static String querySequenceFromSQ(String aType, String owner, String org, Connection connection , Context context) throws EMPException {
		String sqlStr = null;
		Connection conn = getConnection(context);
		PreparedStatement state = null;	
		String cur_sernum = null;	//序列号
		String initcycle = null;	//1--序列号递增
		String zero_flg  = null;	// 序列号长度 
		String serStyle = null;		//序列号样式
		String date     =  context.getDataValue(PUBConstant.OPENDAY).toString();	//营业日期
		ResultSet rs = null;
		try{					
			sqlStr = "select cur_sernum,initcycle,zero_flg,sequencestyle from s_autocode where atype=? and owner=?";
			state = conn.prepareStatement(sqlStr);
			state.setObject(1, aType);
			state.setObject(2, owner);
			rs = state.executeQuery();
			if(rs.next()){
				cur_sernum = rs.getString(1);
				initcycle = rs.getString(2);
				zero_flg  = rs.getString(3);
				serStyle = rs.getString(4);
			}
			closeResource(state, rs);
			if (cur_sernum != null && "1".equals(initcycle)) {
				sqlStr = "select cur_sernum from s_autocode where atype=? and owner=? " ;
				state = conn.prepareStatement(sqlStr);
				state.setObject(1, aType);
				state.setObject(2, owner);
				rs = state.executeQuery();
				if(rs.next()){
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
			}else {// 若没有相关的流水号配置，则抛出异常
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "Can not found the sequence style for aType[" + aType + "] and owner[" + owner + "]");
				throw new EMPException("Can not found the sequence number for aType[" + aType + "] and owner[" + owner + "]");
			}
			if(serStyle==null||"".equals(serStyle)){
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "Can not found the sequence style for aType[" + aType + "] and owner[" + owner + "]");
				throw new EMPException("Can not found the sequence style for aType[" + aType + "] and owner[" + owner + "]");
			}
			/*组装序列号长度*/
			cur_sernum =numFormatToSeq(Integer.parseInt(zero_flg), Integer.parseInt(cur_sernum));
			date =  replace(date,"-", "");
			/**
			 * 组装序列号生成器
			 */
			org = "00"+org;
//			org = org.substring(org.length()-6, org.length());//裕民银行机构号为4位
			serStyle = replace(serStyle,"$ORG$",org);
			serStyle = replace(serStyle,"$SER$", cur_sernum);
			serStyle = replace(serStyle,"$DATE$", date);
			return serStyle;
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "The CMISSequenceService4Oracle occur an error:"+e.getMessage());
			throw new EMPException(e);
		}finally {
			closeResource(state, rs);
			if (conn != null)
				releaseConnection(conn);
		}
	}
	
	public static String querySequenceFromED(String aType, String owner, Connection connection , Context context) throws EMPException {
		String sqlStr = null;
		Connection conn = getConnection(context);
		PreparedStatement state = null;	
		String cur_sernum = null;	//序列号
		String initcycle = null;	//1--序列号递增
		String zero_flg  = null;	// 序列号长度 
		String serStyle = null;		//序列号样式

		String date     =  context.getDataValue(PUBConstant.OPENDAY).toString();	//营业日期
		ResultSet rs = null;
		try{					
			
			sqlStr = "select cur_sernum,initcycle,zero_flg,sequencestyle from s_autocode where atype=? and owner=?";
			state = conn.prepareStatement(sqlStr);
			state.setObject(1, aType);
			state.setObject(2, owner);
			rs = state.executeQuery();
			if(rs.next()){
				cur_sernum = rs.getString(1);
				initcycle = rs.getString(2);
				zero_flg  = rs.getString(3);
				serStyle = rs.getString(4);
			}
			closeResource(state, rs);
			if (cur_sernum != null && "1".equals(initcycle)) {
				sqlStr = "select cur_sernum from s_autocode where atype=? and owner=? " ;
				state = conn.prepareStatement(sqlStr);
				state.setObject(1, aType);
				state.setObject(2, owner);
				rs = state.executeQuery();
				if(rs.next()){
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
			}else {// 若没有相关的流水号配置，则抛出异常
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "Can not found the sequence style for aType[" + aType + "] and owner[" + owner + "]");
				throw new EMPException("Can not found the sequence number for aType[" + aType + "] and owner[" + owner + "]");
			}
			if(serStyle==null||"".equals(serStyle)){
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "Can not found the sequence style for aType[" + aType + "] and owner[" + owner + "]");
				throw new EMPException("Can not found the sequence style for aType[" + aType + "] and owner[" + owner + "]");
			}
			
			/*组装序列号长度*/
			cur_sernum =numFormatToSeq(Integer.parseInt(zero_flg), Integer.parseInt(cur_sernum));
			date =  replace(date,"-", "");
			/**
			 * 组装序列号生成器
			 */
			serStyle = replace(serStyle,"$SER$", cur_sernum);
			serStyle = replace(serStyle,"$DATE$", date);
			
			return serStyle;
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "The CMISSequenceService4Oracle occur an error:"+e.getMessage());
			throw new EMPException(e);
		}finally {
			closeResource(state, rs);
			if (conn != null)
				releaseConnection(conn);
		}
	}
	public static String querySequenceFromDB(String aType, String owner,String dbfs, Connection connection , Context context) throws EMPException {
		String sqlStr = null;
		Connection conn = getConnection(context);
		PreparedStatement state = null;	
		String cur_sernum = null;	//序列号
		String initcycle = null;	//1--序列号递增
		String zero_flg  = null;	// 序列号长度 
		String serStyle = null;		//序列号样式
		String org = "";
		/*
		 * 流程中取不到申请机构
		 */
		try {
			org = context.getDataValue("organNo").toString();
		} catch (Exception e1) {}

		String date     =  context.getDataValue(PUBConstant.OPENDAY).toString();	//营业日期
		ResultSet rs = null;
		try{					
			String year = date.substring(0, 4);
			String month = date.substring(5, 7);
			sqlStr = "select cur_sernum,initcycle,zero_flg,sequencestyle from s_autocode where atype=? and owner=?";
			state = conn.prepareStatement(sqlStr);
			state.setObject(1, aType);
			state.setObject(2, owner);
			rs = state.executeQuery();
			if(rs.next()){
				cur_sernum = rs.getString(1);
				initcycle = rs.getString(2);
				zero_flg  = rs.getString(3);
				serStyle = rs.getString(4);
			}
			closeResource(state, rs);
			
			if (cur_sernum != null && "1".equals(initcycle)) {
				sqlStr = "select cur_sernum from s_autocode where atype=? and owner=? " ;
				state = conn.prepareStatement(sqlStr);
				state.setObject(1, aType);
				state.setObject(2, owner);
				rs = state.executeQuery();
				if(rs.next()){
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
			}else {// 若没有相关的流水号配置，则抛出异常
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "Can not found the sequence style for aType[" + aType + "] and owner[" + owner + "]");
				throw new EMPException("Can not found the sequence number for aType[" + aType + "] and owner[" + owner + "]");
			}
			if(serStyle==null||"".equals(serStyle)){
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "Can not found the sequence style for aType[" + aType + "] and owner[" + owner + "]");
				throw new EMPException("Can not found the sequence style for aType[" + aType + "] and owner[" + owner + "]");
			}
			
			/*组装序列号长度*/
			cur_sernum =numFormatToSeq(Integer.parseInt(zero_flg), Integer.parseInt(cur_sernum));
			date =  replace(date,"-", "");
			/**
			 * 组装序列号生成器
			 */
			serStyle = replace(serStyle,"$ORG$", 	org);
			serStyle = replace(serStyle,"$YEAR$", year);
			serStyle = replace(serStyle,"$MONTH$", month);
			serStyle = replace(serStyle,"$SER$", cur_sernum);
			serStyle = replace(serStyle,"$DATE$", date);
			serStyle = replace(serStyle,"$ORGAPP$", owner);
			serStyle = replace(serStyle,"$DBFS$", dbfs);
			
			return serStyle;
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "The CMISSequenceService4Oracle occur an error:"+e.getMessage());
			throw new EMPException(e);
		}finally {
			closeResource(state, rs);
			if (conn != null)
				releaseConnection(conn);
		}
	}
	
	private static void closeResource(PreparedStatement stmt , ResultSet rs) {
		if(rs != null){
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
	
	public String getDefaultOwner() {
		return defaultOwner;
	}

	public void setDefaultOwner(String defaultOwner1) {
		defaultOwner = defaultOwner1;
	}
	  /**
	    * 在纯数字前补0 转换成 长度为5位的字符串（如果数字大于100000，则取数字尾数）。
	    * 如 123-->00123，9999-->09999，100123123-->23123
	    * @param value
	    * @return
	    */
	private static String numFormatToSeq(int len,int value){
	    	
	        // 如果要改动返回字符串长度，改动maxValue(返回几位数字，则尾数改为几个0)
			if (len <= 0) return "";
			
			//补足指定长度的0
	        StringBuffer seq = new StringBuffer();
	        for(int i=0;i<len;i++){
	        	seq.append('0');
	        }
	        seq.append(value);
	        
	        return seq.substring(seq.length()-len);
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

}
