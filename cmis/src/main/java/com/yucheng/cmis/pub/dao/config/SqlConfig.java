package com.yucheng.cmis.pub.dao.config;

import java.util.HashMap;

public class SqlConfig {

	/** SQL ID */
	private String sqlid="";
	
	/** SQL类型  是SELECT、UPDATE、DELETE */
	private String sqlType = "";
	
	/** SQL */
	private String sql="";
	
	/** SQL输入值类型，支持基本型、自定义类型 、EMP KeyCollection*/
	private String parameterClass = "";
	
	/** 数据值类型，用于INSERT/UPDATE，支持基本型、自定义类型 、EMP KeyCollection*/
	private String valueClass = "";
	
	/** SQL输入值类型，用于SELECT，支持基本型、自定义类型 、EMP KeyCollection*/
	private String resultClass = "";
	
	/** 操作表名 用于INSERT/UPDATE，支持基本型、自定义类型 、EMP KeyCollection*/
	private String updTableName = "";
	
	/** 是否只返回第一条记录 */
	private boolean onlyReturnFirst = false;
	
	/** 是否能全表更新（即是检查UPDATE之时是否有过滤条件） */
	private boolean canUpdateAll = false;
	
	/** 可选条件集合 （KEY 为 optId） */
	private HashMap optCondition = null;

	public String getSqlid() {
		return sqlid;
	}

	public void setSqlid(String sqlid) {
		this.sqlid = sqlid;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getParameterClass() {
		return parameterClass;
	}

	public void setParameterClass(String parameterClass) {
		this.parameterClass = parameterClass;
	}

	public String getValueClass() {
		return valueClass;
	}

	public void setValueClass(String valueClass) {
		this.valueClass = valueClass;
	}

	public String getResultClass() {
		return resultClass;
	}

	public void setResultClass(String resultClass) {
		this.resultClass = resultClass;
	}

	public String getUpdTableName() {
		return updTableName;
	}

	public void setUpdTableName(String updTableName) {
		this.updTableName = updTableName;
	}

	public boolean isCanUpdateAll() {
		return canUpdateAll;
	}

	public void setCanUpdateAll(boolean canUpdateAll) {
		this.canUpdateAll = canUpdateAll;
	}

	public boolean isOnlyReturnFirst() {
		return onlyReturnFirst;
	}

	public void setOnlyReturnFirst(boolean onlyReturnFirst) {
		this.onlyReturnFirst = onlyReturnFirst;
	}

	public HashMap getOptCondition() {
		return optCondition;
	}

	public void setOptCondition(HashMap optCondition) {
		this.optCondition = optCondition;
	}
	
	
}
