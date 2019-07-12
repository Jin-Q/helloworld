package com.yucheng.cmis.pub.domain;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;

import com.ecc.emp.core.Context;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.pub.PUBConstant;

/**
 * 序列服务数据基类
 *   所有自定义的数据实现类都需要继承该基类
 * @Version bsbcmis
 * @author wuming 2012-8-30 
 * Description:
 */
public class SeqTemDataSupport implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 数据项的长度
	 */
	protected int length;
	
	/**
	 * 缺省数据值，如果得到的值为空 
	 * 那么获取这个 缺省值
	 */
	protected String defaultvalue;
	
	/**
	 * 这个数据项的值
	 *  一般用作固定不变的值
	 */
	protected String value;
	
	/**
	 * 获取数据项类型
	 */
	protected String type = "text";
	
	/**
	 * 序列创建模版
	 */
	private final static String SEQ_TEMPLATE="CREATE SEQUENCE SEQNAME_TEMPLATE MINVALUE 1 MAXVALUE 9999999999999 START WITH 1 INCREMENT BY 1 CACHE 5 ";
	
    private final static String SEQNAME_TEMPLATE="SEQNAME_TEMPLATE";
	/**
	 * 根据指定的序列名字 和 预定义好的序列模版 创建序列
	 * @param seqName
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public boolean createSeq(String seqName,Connection con){
		String seqStr = SeqTemDataSupport.SEQ_TEMPLATE.replaceAll(SeqTemDataSupport.SEQNAME_TEMPLATE,seqName);
		
		Statement st = null;
		try{
			st = con.createStatement();
			return st.execute(seqStr);
			
		}catch(SQLException e){
			EMPLog.log(PUBConstant.EMPSEQ, EMPLog.WARNING, 0, "创建["+seqStr+"]错误！",e);
			return false;
		}finally{
			if(st != null){
				try {st.close();} catch (SQLException e) {}
			}
		}		
	}
	/**
	 * 根据指定的序列名字 和 预定义好的序列模版 查询序列是否存在
	 * @param seqName
	 * @param con
	 * @return
	 * @throws SQLException 
	 * @throws SQLException
	 */
	public String querySeq(String seqName,Connection con) throws SQLException{
		Statement st = null;
		ResultSet rs = null;
		try{
			st = con.createStatement();
			rs = st.executeQuery("select to_char("+seqName+".nextval) from dual");
			if(rs.next()){
				return  rs.getString(1);
			}
		}catch(SQLException e){
			EMPLog.log(PUBConstant.EMPSEQ, EMPLog.WARNING, 0, "序列["+seqName+"]不存在！");
			throw e;
		}finally{
			if(st != null){
				try {st.close();} catch (SQLException e) {}
			}
			if(rs != null){
				try {rs.close();} catch (SQLException e) {}
			}
		}		
		return null;
	}
	/**
	 * 获取数据该数据模板的值,本方法不可被重写
	 *     子类继承父类，如果有不同的取值类型，需要自己设定一个类型
	 *     自动执行 getTextValue  的方法获取不同类型的值。如果没有该方法采用默认的实现方法
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	public final String getDataValue(String seqId,Context context,Connection con) throws Exception{
		
		//利用反射获取相关类型看是否存在该类型的方法然后开始执行
		Method method;
		String result = null;
		try {//setAttributes   getTextValue
			method = this.getClass().getMethod("getTextValue",String.class, Context.class,Connection.class);
			//method = this.getClass().getMethod("setAttributes", new Class[]{Map.class});
			result = (String)method.invoke(this, new Object[]{seqId,context,con});
			
		} catch (SecurityException e) {
			
			EMPLog.log(PUBConstant.EMPSEQ, EMPLog.WARNING, 0, "获取方法错误！"+e.getMessage());
			//return getTextValue();
		} catch (NoSuchMethodException e) {
			EMPLog.log(PUBConstant.EMPSEQ, EMPLog.WARNING, 0, "获取方法错误:实例化的当前类["+this.getClass().getName()+"]"+e.getMessage());
			//return getTextValue();
		} catch(InvocationTargetException e){
			EMPLog.log(PUBConstant.EMPSEQ, EMPLog.WARNING, 0, "实例化的当前类["+this.getClass().getName()+"]执行方法[getTextValue]错误！",e);
			//return getTextValue();
			throw new Exception(e);
		} catch(Exception e){
			EMPLog.log(PUBConstant.EMPSEQ, EMPLog.WARNING, 0, "错误！"+e.getMessage());
			//return getTextValue();
		}
		//调用自由实现类方法失败，那么执行缺省的方法getTextValue
		if(result == null){
			return getTextValue(seqId);
		}
		
		return formatLen(seqId,result);
	}
	
	/**
	 * 获取默认的文本类型的值，缺省实现。
	 *   规则： 如果设定好值，那么直接拿值并且返回
	 *         如果设定的值为空，那么返回相关的缺省值设置
	 * 获取子类实现的 getDataValue 的方法     
	 * 
	 * @param context
	 * @param seqId
	 * @param con
	 * @return
	 */
	private String getTextValue(String seqId){
		if(StringUtils.isBlank(value)){
			 return formatLen(seqId,defaultvalue);
		}
		return formatLen(seqId,value);
		
	}
	
	public String formatLen(String seqId,String src){
    	if(StringUtils.isBlank(src)){
    		return "";
    	}
    	
    	if(length <= 0){
    		return src;
    	}
    	
    	if(src.length() > this.length){
    		EMPLog.log(PUBConstant.EMPSEQ, EMPLog.WARNING, 0, "序列配置["+seqId+"]的配置长度小于获取的数据长度！");
    		
    		return src;
    	}
    	
    	StringBuilder sb = new StringBuilder();
    	for(int i=0;i< this.length - src.length() ;i++){
    		sb.append("0");
   	    }
    	
    	sb.append(src);
    	return sb.toString();
    	
    }

    public int getLength() {
		return length;
	}

    /**
     * 解析xml的解析器使用的是 字符串获取 所以这个方法调整一下
     * @param length
     */
    public void setLength(int length) {
	    
    	this.length = length;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefaultvalue() {
		return defaultvalue;
	}

	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	} 
    
	
    
}
