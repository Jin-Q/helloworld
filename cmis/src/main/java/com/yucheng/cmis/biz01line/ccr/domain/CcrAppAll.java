package com.yucheng.cmis.biz01line.ccr.domain;

import java.util.ArrayList;

import com.yucheng.cmis.pub.CMISDomain;

public class CcrAppAll implements CMISDomain {
	private CcrAppInfo ccrAppInfo;
	private CcrAppDetail ccrAppDetail;
	private CcrAppFinGuar ccrAppFinGuar;
	private CcrModelScore ccrModelScore;
	private ArrayList <CcrMGroupScore>ccrMGroupScoreList;
	private ArrayList <CcrGIndScore>ccrGIndScoreList;
	
	/**
	 *==============CcrAppInfo===================================
 	 * @return  CcrAppInfo
 	 */
	public CcrAppInfo getCcrAppInfo(){
		return ccrAppInfo;
	}
	/**
 	 * @set CcrAppInfo
 	 * @param CcrAppInfo
 	 */
	public void setCcrAppInfo(CcrAppInfo ccrAppInfo){
		this.ccrAppInfo = ccrAppInfo;
	}
	/**
	 *==============CcrAppDetail===================================
	 * @return  CcrAppDetail
	 */
	public CcrAppDetail getCcrAppDetail() {
		return ccrAppDetail;
	}
	public void setCcrAppDetail(CcrAppDetail ccrAppDetail) {
		this.ccrAppDetail = ccrAppDetail;
	}
	/**
	 *==============CcrAppFinGuar===================================
	 * @return  ccrAppFinGuar
	 */
	public CcrAppFinGuar getCcrAppFinGuar(){
		return ccrAppFinGuar;
	}
	/**
	 * @set ccrAppDetail
	 * @param ccrAppDetail
	**/
	public void setCcrAppFinGuar(CcrAppFinGuar ccrAppFinGuar){
		this.ccrAppFinGuar = ccrAppFinGuar;
	}

	/**
	 * ==============CcrGIndScoreList===================================
 	 * @return ccrGIndScoreList
 	 */	
	public ArrayList<CcrGIndScore> getCcrGIndScoreList(){
		return this.ccrGIndScoreList;
	}
	/**
 	 * @set ccrGIndScoreList
 	 * @param ccrGIndScoreList
 	 */	

	public void setCcrGIndScoreList(ArrayList <CcrGIndScore>ccrGIndScoreList){
		this.ccrGIndScoreList=ccrGIndScoreList;
	}
	
	/**
	 * ==============ccrMGroupScoreList===================================
 	 * @return ccrGIndScoreList
 	 */	
	public ArrayList<CcrMGroupScore> getCcrMGroupScoreList(){
		return this.ccrMGroupScoreList;
	}
	/**
 	 * @set ccrModelScore
 	 * @param ccrModelScore
 	 */	

	public void setCcrMGroupScoreList(ArrayList <CcrMGroupScore>ccrMGroupScoreList){
		this.ccrMGroupScoreList=ccrMGroupScoreList;
	}
	/**
	 * ==============ccrModelScore===================================
	 * @return ccrModelScore
	 */	
	public CcrModelScore getCcrModelScore(){
		return this.ccrModelScore;
	}
	/**
	 * @set ccrModelScore
	 * @param ccrModelScore
	 */	
	
	public void setCcrModelScore(CcrModelScore ccrModelScore){
		this.ccrModelScore=ccrModelScore;
	}
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}
