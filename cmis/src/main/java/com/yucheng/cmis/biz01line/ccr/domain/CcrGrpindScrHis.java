package com.yucheng.cmis.biz01line.ccr.domain;
/**
 * Title: CcrGrpindScrHis.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Sat Apr 18 09:05:13 CST 2009
 * @version：1.0
 */
public class CcrGrpindScrHis  implements com.yucheng.cmis.pub.CMISDomain{
	private String indexNo;
	private String indexName;
	private String indexValue;
	private String indexScore;
	private String scoringManager;
	private String serno;
	private String cusId;
	private String ccrDate;
	private String indexSeq;
	private String groupNo;
	private String groupName;
	private String indOrgVal;
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
 	 * @return 返回 indexNo
 	 */
	public String getIndexNo(){
		return indexNo;
	}
	/**
 	 * @设置 indexNo
 	 * @param indexNo
 	 */
	public void setIndexNo(String indexNo){
		this.indexNo = indexNo;
	}
	public String getIndOrgVal() {
		return indOrgVal;
	}
	public void setIndOrgVal(String indOrgVal) {
		this.indOrgVal = indOrgVal;
	}
	/**
 	 * @return 返回 indexName
 	 */
	public String getIndexName(){
		return indexName;
	}
	/**
 	 * @设置 indexName
 	 * @param indexName
 	 */
	public void setIndexName(String indexName){
		this.indexName = indexName;
	}
	/**
 	 * @return 返回 indexValue
 	 */
	public String getIndexValue(){
		return indexValue;
	}
	/**
 	 * @设置 indexValue
 	 * @param indexValue
 	 */
	public void setIndexValue(String indexValue){
		this.indexValue = indexValue;
	}
	/**
 	 * @return 返回 indexScore
 	 */
	public String getIndexScore(){
		return indexScore;
	}
	/**
 	 * @设置 indexScore
 	 * @param indexScore
 	 */
	public void setIndexScore(String indexScore){
		this.indexScore = indexScore;
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
 	 * @return 返回 indexSeq
 	 */
	public String getIndexSeq(){
		return indexSeq;
	}
	/**
 	 * @设置 indexSeq
 	 * @param indexSeq
 	 */
	public void setIndexSeq(String indexSeq){
		this.indexSeq = indexSeq;
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
}