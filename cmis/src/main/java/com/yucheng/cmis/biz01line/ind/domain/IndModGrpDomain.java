package com.yucheng.cmis.biz01line.ind.domain;

import com.yucheng.cmis.pub.CMISDomain;

/**
 * Title: IndModelGroup.java
 * Description: 
 * @author：echow	heyc@yuchengtech.com
 * @create date：Tue Mar 10 15:48:44 CST 2009
 * @version：1.0
 */
public class IndModGrpDomain implements CMISDomain {
	private String modelNo;
	private String groupNo;
	private double weight;
	private int seqno;
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
 	 * @return 返回 weight
 	 */
	public double getWeight(){
		return weight;
	}
	/**
 	 * @设置 weight
 	 * @param weight
 	 */
	public void setWeight(double weight){
		this.weight = weight;
	}
	/**
 	 * @return 返回 seqno
 	 */
	public int getSeqno(){
		return seqno;
	}
	/**
 	 * @设置 seqno
 	 * @param seqno
 	 */
	public void setSeqno(int seqno){
		this.seqno = seqno;
	}
	
	public/*protected*/ Object clone() throws CloneNotSupportedException { 

		// call父类的clone方法 

		Object result = super.clone(); 



		//TODO: 定制clone数据 

		return result; 

		} 

}