package com.yucheng.cmis.pub.sequence.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.jdbc.ConnectionManager;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.sequence.SequenceNotFoundException;

public class AllSequenceTemplate {

	/**
	 * 鄞州银行流水号格式化通用方法
	 * @author 娄建成
	 * @param fromType
	 * @param sequence
	 * @param sequence2 
	 * @param length
	 * @param context
	 * @return
	 * @throws EMPException 
	 */
	public String format(String beforeString,String fromType, String sequence,  int length,
			Context context) throws EMPException
	{
		String applyNumber = "";
		try {
			/*
			 * 生成的代码规则 key + '-' + 营业日期(YYYYMMDD) + '-' +
			 * 序列号，目前规则中未加入owner（所属行），
			 * 如：LOANCONT2009053100002，ACCPCONT2009053100003
			 */
			StringBuffer sb = new StringBuffer(beforeString);
			if("fromDate".equals(fromType))
			{
				String date = (String) context.getDataValue("OPENDAY");
				date = date.replaceAll("-", "");
				sb.append(date);
			}
			else if("fromOrg".equals(fromType))
			{
				String date = (String) context.getDataValue("OPENDAY");
				date = date.substring(0,4);
				sb.append(date);
			}else
			{
				throw new EMPException("生成流水号出错，可能没有传入流水号来源fromType");
			}

			// 格式化成length位数据
			String seq = numFormatToSeq(length - sb.length(), Integer
					.valueOf(sequence));
			sb.append(seq);

			applyNumber = sb.toString();
		} catch (Exception e) {
			throw new EMPException(e);
		}
		
		return applyNumber;
	}
	/**
     * 在纯数字前补0 转换成 长度为5位的字符串（如果数字大于100000，则取数字尾数）。
     * 如 123-->00123，9999-->09999，100123123-->23123
     * @param value
     * @return
     */
    private String numFormatToSeq(int len,int value){
    	
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
	public synchronized String querySequenceFromDB(String beforeString, String fromType,
			Context context) throws EMPException {
		//TODO WEBLOGIC报错,先改成从ConnectionManager获得connection
		String sqlStr = null;
		
		PreparedStatement state = null;	
		String cur_sernum = null;
		String initcycle = null;
		ResultSet rs = null;
		Connection connection = null;
		DataSource dataSource = null;
		dataSource = (DataSource)context.getService(CMISConstance.ATTR_DATASOURCE);
		
		
        EMPLog.log(EMPConstance.EMP_TRANSACTION, EMPLog.INFO, 0, (new StringBuilder()).append("Apply new connection from data source: ").append(dataSource).append(" success!").toString());
        try
        {
        	connection = ConnectionManager.getConnection(dataSource);
        }catch(Exception e)
        {
        	throw new EMPException(e);
        }
		try{
			sqlStr = "select cur_sernum,initcycle from s_autocode where atype=? and owner=?";
			state = connection.prepareStatement(sqlStr);
			state.setObject(1, beforeString);
			state.setObject(2, fromType);
			rs = state.executeQuery();
			if(rs.next()){
				cur_sernum = rs.getString(1);
				initcycle = rs.getString(2);
			}
			rs.close();
			rs = null;
			state.close();
			state = null;
			
			
			if (initcycle != null && !"9".equals(initcycle)) {
				sqlStr = "select cur_sernum from s_autocode where atype=? and owner=? " ;
				state = connection.prepareStatement(sqlStr);
				state.setObject(1, beforeString);
				state.setObject(2, fromType);
				rs = state.executeQuery();
				if(rs.next()){
					cur_sernum = rs.getString(1);
				}
				rs.close();
				rs = null;
				state.close();
				state = null;
				
				sqlStr = "update s_autocode set cur_sernum=? where  atype=? and owner=?";
				state = connection.prepareStatement(sqlStr);

				long num = Long.valueOf(cur_sernum).longValue();
				num = num + 1;
				state.setObject(1, Long.valueOf(num));

				state.setObject(2, beforeString);
				state.setObject(3, fromType);
				state.executeUpdate();

				state.close();
				state = null;
			} else if (initcycle != null && "9".equals(initcycle)) {
				sqlStr = "select " + cur_sernum + ".nextval from dual";
				rs = state.executeQuery(sqlStr);
				if (rs.next()) {
					cur_sernum = rs.getString(1);
				}
			} else {// 若没有相关的流水号配置，则抛出异常
				EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "Can not found the sequence number for aType[" + beforeString + "] and owner[" + fromType + "]");
				throw new SequenceNotFoundException("Can not found the sequence number for aType[" + beforeString + "] and owner[" + fromType + "]");
			}
			
		}catch(Exception e){
			EMPLog.log(EMPConstance.EMP_CORE, EMPLog.ERROR, 0, "The CMISSequenceService4Oracle occur an error:"+e.getMessage());
			try
			{
				connection.rollback();
			}catch(Exception ee)
			{
				throw new EMPException(e);
			}
		}finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (state != null) {
				try {
					state.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(connection != null)
			{
				try {
					ConnectionManager.releaseConnection(dataSource, connection);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return cur_sernum;
	}
	
}
