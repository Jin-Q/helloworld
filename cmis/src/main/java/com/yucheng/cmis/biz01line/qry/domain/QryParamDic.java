package com.yucheng.cmis.biz01line.qry.domain;
/**
 * Title: QryParamDic.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Fri Jul 03 15:09:35 CST 2009
 * @version：1.0
 */
public class QryParamDic  implements com.yucheng.cmis.pub.CMISDomain{
	private String paramDicNo;
	private String name;
	private String parDicType;
	private String opttype;
	private String querySql;
	private String popname;
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
 	 * @return 返回 paramDicNo
 	 */
	public String getParamDicNo(){
		return paramDicNo;
	}
	/**
 	 * @设置 paramDicNo
 	 * @param paramDicNo
 	 */
	public void setParamDicNo(String paramDicNo){
		this.paramDicNo = paramDicNo;
	}
	/**
 	 * @return 返回 name
 	 */
	public String getName(){
		return name;
	}
	/**
 	 * @设置 name
 	 * @param name
 	 */
	public void setName(String name){
		this.name = name.trim();
	}
	/**
 	 * @return 返回 parDicType
 	 */
	public String getParDicType(){
		return parDicType;
	}
	/**
 	 * @设置 parDicType
 	 * @param parDicType
 	 */
	public void setParDicType(String parDicType){
		this.parDicType = parDicType.trim();
	}
	/**
 	 * @return 返回 opttype
 	 */
	public String getOpttype(){
		return opttype;
	}
	/**
 	 * @设置 opttype
 	 * @param opttype
 	 */
	public void setOpttype(String opttype){
		this.opttype = opttype.trim();
	}
	/**
 	 * @return 返回 querySql
 	 */
	public String getQuerySql(){
		return querySql;
	}
	/**
 	 * @设置 querySql
 	 * @param querySql
 	 */
	public void setQuerySql(String querySql){
		this.querySql = querySql;
	}
	/**
 	 * @return 返回 popname
 	 */
	public String getPopname(){
		return popname;
	}
	/**
 	 * @设置 popname
 	 * @param popname
 	 */
	public void setPopname(String popname){
		this.popname = popname.trim();
	}
}