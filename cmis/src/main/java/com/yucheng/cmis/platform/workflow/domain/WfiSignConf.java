package com.yucheng.cmis.platform.workflow.domain;
/**
 * Title: WfiSignConf.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Tue May 10 17:13:39 CST 2011
 * @version：1.0
 */
public class WfiSignConf  implements com.yucheng.cmis.pub.CMISDomain{
	private String signId;
	private String signName;
	private String signDesc;
	private String signClass;
	private String signState;
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
 	 * @return 返回 signId
 	 */
	public String getSignId(){
		return signId;
	}
	/**
 	 * @设置 signId
 	 * @param signId
 	 */
	public void setSignId(String signId){
		this.signId = signId;
	}
	/**
 	 * @return 返回 signName
 	 */
	public String getSignName(){
		return signName;
	}
	/**
 	 * @设置 signName
 	 * @param signName
 	 */
	public void setSignName(String signName){
		this.signName = signName;
	}
	/**
 	 * @return 返回 signDesc
 	 */
	public String getSignDesc(){
		return signDesc;
	}
	/**
 	 * @设置 signDesc
 	 * @param signDesc
 	 */
	public void setSignDesc(String signDesc){
		this.signDesc = signDesc;
	}
	/**
 	 * @return 返回 signClass
 	 */
	public String getSignClass(){
		return signClass;
	}
	/**
 	 * @设置 signClass
 	 * @param signClass
 	 */
	public void setSignClass(String signClass){
		this.signClass = signClass;
	}
	/**
 	 * @return 返回 signState
 	 */
	public String getSignState(){
		return signState;
	}
	/**
 	 * @设置 signState
 	 * @param signState
 	 */
	public void setSignState(String signState){
		this.signState = signState;
	}
}