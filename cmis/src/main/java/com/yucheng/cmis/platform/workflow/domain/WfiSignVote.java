package com.yucheng.cmis.platform.workflow.domain;
/**
 * Title: WfiSignVote.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Tue May 10 17:13:39 CST 2011
 * @version：1.0
 */
public class WfiSignVote  implements com.yucheng.cmis.pub.CMISDomain{
	private String svVoteId;
	private String stTaskId;
	private String svExeUser;
	private String svResult;
	private String svAdvice;
	private String svStatus;
	private String svStartTime;
	private String svEndTime;
	private String svRequestTime;
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
 	 * @return 返回 svVoteId
 	 */
	public String getSvVoteId(){
		return svVoteId;
	}
	/**
 	 * @设置 svVoteId
 	 * @param svVoteId
 	 */
	public void setSvVoteId(String svVoteId){
		this.svVoteId = svVoteId;
	}
	/**
 	 * @return 返回 stTaskId
 	 */
	public String getStTaskId(){
		return stTaskId;
	}
	/**
 	 * @设置 stTaskId
 	 * @param stTaskId
 	 */
	public void setStTaskId(String stTaskId){
		this.stTaskId = stTaskId;
	}
	/**
 	 * @return 返回 svExeUser
 	 */
	public String getSvExeUser(){
		return svExeUser;
	}
	/**
 	 * @设置 svExeUser
 	 * @param svExeUser
 	 */
	public void setSvExeUser(String svExeUser){
		this.svExeUser = svExeUser;
	}
	/**
 	 * @return 返回 svResult
 	 */
	public String getSvResult(){
		return svResult;
	}
	/**
 	 * @设置 svResult
 	 * @param svResult
 	 */
	public void setSvResult(String svResult){
		this.svResult = svResult;
	}
	/**
 	 * @return 返回 svAdvice
 	 */
	public String getSvAdvice(){
		return svAdvice;
	}
	/**
 	 * @设置 svAdvice
 	 * @param svAdvice
 	 */
	public void setSvAdvice(String svAdvice){
		this.svAdvice = svAdvice;
	}
	/**
 	 * @return 返回 svStatus
 	 */
	public String getSvStatus(){
		return svStatus;
	}
	/**
 	 * @设置 svStatus
 	 * @param svStatus
 	 */
	public void setSvStatus(String svStatus){
		this.svStatus = svStatus;
	}
	/**
 	 * @return 返回 svStartTime
 	 */
	public String getSvStartTime(){
		return svStartTime;
	}
	/**
 	 * @设置 svStartTime
 	 * @param svStartTime
 	 */
	public void setSvStartTime(String svStartTime){
		this.svStartTime = svStartTime;
	}
	/**
 	 * @return 返回 svEndTime
 	 */
	public String getSvEndTime(){
		return svEndTime;
	}
	/**
 	 * @设置 svEndTime
 	 * @param svEndTime
 	 */
	public void setSvEndTime(String svEndTime){
		this.svEndTime = svEndTime;
	}
	/**
 	 * @return 返回 svRequestTime
 	 */
	public String getSvRequestTime(){
		return svRequestTime;
	}
	/**
 	 * @设置 svRequestTime
 	 * @param svRequestTime
 	 */
	public void setSvRequestTime(String svRequestTime){
		this.svRequestTime = svRequestTime;
	}
}