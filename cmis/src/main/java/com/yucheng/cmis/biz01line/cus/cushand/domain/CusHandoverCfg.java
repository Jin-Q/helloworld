package com.yucheng.cmis.biz01line.cus.cushand.domain;
/**
 * Title: CusHandoverCfg.java
 * Description: 
 * @author��echow	heyc@yuchengtech.com
 * @create date��Mon Mar 26 14:33:32 CST 2012
 * @version��1.0
 */
public class CusHandoverCfg  implements com.yucheng.cmis.pub.CMISDomain{
	private String serno;
	private String tableMode;
	private String tableScope;
	private String extClass;
	private String memo;
	/**
	 * @���ø���clone����
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone()throws CloneNotSupportedException{
		Object result = super.clone();
		//TODO: ����clone���
		return result;
	}
	/**
 	 * @return ���� serno
 	 */
	public String getSerno(){
		return serno;
	}
	/**
 	 * @���� serno
 	 * @param serno
 	 */
	public void setSerno(String serno){
		this.serno = serno;
	}
	/**
 	 * @return ���� tableMode
 	 */
	public String getTableMode(){
		return tableMode;
	}
	/**
 	 * @���� tableMode
 	 * @param tableMode
 	 */
	public void setTableMode(String tableMode){
		this.tableMode = tableMode;
	}
	/**
 	 * @return ���� tableScope
 	 */
	public String getTableScope(){
		return tableScope;
	}
	/**
 	 * @���� tableScope
 	 * @param tableScope
 	 */
	public void setTableScope(String tableScope){
		this.tableScope = tableScope;
	}
	/**
 	 * @return ���� extClass
 	 */
	public String getExtClass(){
		return extClass;
	}
	/**
 	 * @���� extClass
 	 * @param extClass
 	 */
	public void setExtClass(String extClass){
		this.extClass = extClass;
	}
	/**
 	 * @return ���� memo
 	 */
	public String getMemo(){
		return memo;
	}
	/**
 	 * @���� memo
 	 * @param memo
 	 */
	public void setMemo(String memo){
		this.memo = memo;
	}
}