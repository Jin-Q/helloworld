package com.yucheng.cmis.biz01line.cus.cushand.domain;
/**
 * Title: CusHandoverDetail.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Wed Mar 02 10:54:31 CST 2011
 * @version：1.0
 */
public class CusHandoverDetail  implements com.yucheng.cmis.pub.CMISDomain{
	private String subSerno;
	private String serno;
	private String tableCode;
	private String tableName;
	private String extSql;
	private String memo;
	/**
	 * @调用父类clone方法
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone()throws CloneNotSupportedException{
		Object result = super.clone();
		//TODO: 定制clone数据
		return result;
	}
	/**
 	 * @return 返回 subSerno
 	 */
	public String getSubSerno(){
		return subSerno;
	}
	/**
 	 * @设置 subSerno
 	 * @param subSerno
 	 */
	public void setSubSerno(String subSerno){
		this.subSerno = subSerno;
	}
	/**
 	 * @return 返回 serno
 	 */
	public String getSerno(){
		return serno;
	}
	/**
 	 * @设置 serno
 	 * @param serno
 	 */
	public void setSerno(String serno){
		this.serno = serno;
	}
	/**
 	 * @return 返回 tableCode
 	 */
	public String getTableCode(){
		return tableCode;
	}
	/**
 	 * @设置 tableCode
 	 * @param tableCode
 	 */
	public void setTableCode(String tableCode){
		this.tableCode = tableCode;
	}
	/**
 	 * @return 返回 tableName
 	 */
	public String getTableName(){
		return tableName;
	}
	/**
 	 * @设置 tableName
 	 * @param tableName
 	 */
	public void setTableName(String tableName){
		this.tableName = tableName;
	}
	/**
 	 * @return 返回 extSql
 	 */
	public String getExtSql(){
		return extSql;
	}
	/**
 	 * @设置 extSql
 	 * @param extSql
 	 */
	public void setExtSql(String extSql){
		this.extSql = extSql;
	}
	/**
 	 * @return 返回 memo
 	 */
	public String getMemo(){
		return memo;
	}
	/**
 	 * @设置 memo
 	 * @param memo
 	 */
	public void setMemo(String memo){
		this.memo = memo;
	}
}