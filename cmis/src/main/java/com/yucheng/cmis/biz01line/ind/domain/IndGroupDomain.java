package com.yucheng.cmis.biz01line.ind.domain;

import com.yucheng.cmis.pub.CMISDomain;

 

/**
 * Title: IndGroup.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Tue Mar 10 15:48:44 CST 2009
 * @version：1.0
 */
public class IndGroupDomain implements CMISDomain {
	private String groupNo;
	private String groupName;
	private String groupKind;
	private String ratingRules;
	private String supGroupNo;
	private String memo;
	private String transId;
	
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	/**
 	 * @return 返回 groupNo
 	 */
	public String getGroupNo(){
		return groupNo;
	}
	/**
 	 * @设置 groupNo
 	 * @param groupNo
 	 */
	public void setGroupNo(String groupNo){
		this.groupNo = groupNo;
	}
	/**
 	 * @return 返回 groupName
 	 */
	public String getGroupName(){
		return groupName;
	}
	/**
 	 * @设置 groupName
 	 * @param groupName
 	 */
	public void setGroupName(String groupName){
		this.groupName = groupName;
	}
	/**
 	 * @return 返回 groupKind
 	 */
	public String getGroupKind(){
		return groupKind;
	}
	/**
 	 * @设置 groupKind
 	 * @param groupKind
 	 */
	public void setGroupKind(String groupKind){
		this.groupKind = groupKind;
	}
	/**
 	 * @return 返回 ratingRules
 	 */
	public String getRatingRules(){
		return ratingRules;
	}
	/**
 	 * @设置 ratingRules
 	 * @param ratingRules
 	 */
	public void setRatingRules(String ratingRules){
		this.ratingRules = ratingRules;
	}
	/**
 	 * @return 返回 supGroupNo
 	 */
	public String getSupGroupNo(){
		return supGroupNo;
	}
	/**
 	 * @设置 supGroupNo
 	 * @param supGroupNo
 	 */
	public void setSupGroupNo(String supGroupNo){
		this.supGroupNo = supGroupNo;
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
	
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}