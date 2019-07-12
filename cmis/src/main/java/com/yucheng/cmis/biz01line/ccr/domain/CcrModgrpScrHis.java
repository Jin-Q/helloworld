package com.yucheng.cmis.biz01line.ccr.domain;
/**
 * Title: CcrModgrpScrHis.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Sat Apr 18 09:05:13 CST 2009
 * @version：1.0
 */
public class CcrModgrpScrHis  implements com.yucheng.cmis.pub.CMISDomain{
	private String serno;
	private String cusId;
	private String ccrDate;
	private String modelNo;
	private String modelName;
	private String groupNo;
	private String groupName;
	private String groupScore;
	private String scoringManager;
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
 	 * @return 返回 cusId
 	 */
	public String getCusId(){
		return cusId;
	}
	/**
 	 * @设置 cusId
 	 * @param cusId
 	 */
	public void setCusId(String cusId){
		this.cusId = cusId;
	}
	/**
 	 * @return 返回 ccrDate
 	 */
	public String getCcrDate(){
		return ccrDate;
	}
	/**
 	 * @设置 ccrDate
 	 * @param ccrDate
 	 */
	public void setCcrDate(String ccrDate){
		this.ccrDate = ccrDate;
	}
	/**
 	 * @return 返回 modelNo
 	 */
	public String getModelNo(){
		return modelNo;
	}
	/**
 	 * @设置 modelNo
 	 * @param modelNo
 	 */
	public void setModelNo(String modelNo){
		this.modelNo = modelNo;
	}
	/**
 	 * @return 返回 modelName
 	 */
	public String getModelName(){
		return modelName;
	}
	/**
 	 * @设置 modelName
 	 * @param modelName
 	 */
	public void setModelName(String modelName){
		this.modelName = modelName;
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
 	 * @return 返回 groupScore
 	 */
	public String getGroupScore(){
		return groupScore;
	}
	/**
 	 * @设置 groupScore
 	 * @param groupScore
 	 */
	public void setGroupScore(String groupScore){
		this.groupScore = groupScore;
	}
	/**
 	 * @return 返回 scoringManager
 	 */
	public String getScoringManager(){
		return scoringManager;
	}
	/**
 	 * @设置 scoringManager
 	 * @param scoringManager
 	 */
	public void setScoringManager(String scoringManager){
		this.scoringManager = scoringManager;
	}
}