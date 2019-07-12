package com.yucheng.cmis.biz01line.fnc.detail.agent;

import java.util.HashMap;
import java.util.Map;

//import com.yucheng.cmis.fnc.detail.domain.FncLoan; 2012.6.16杨蕾注释
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;

/**
 * 主要借款明细信息的代理
 *@Classname	FncLoanAgent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 8, 2008 4:07:31 PM  
 *@Copyright  2008 yuchengtech
 *@Author 		ghost
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncLoanAgent extends CMISAgent {
	/**
		* 新增主要借款明细信息
		* @param  fncLoan 主要借款明细信息
		* @return	String   返回值说明
		* @throws Exception
	 */
	/*
	 * 2012.6.16 杨蕾注释
	 * public String addRecord(FncLoan fncLoan) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
		
		//新增主要借款明细信息
		int count = this.insertCMISDomain(fncLoan, PUBConstant.FNCLOAN);	// 1成功  其他失败
			//如果失败，给标志信息赋值
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}
		return flagInfo;
	}*/
	
	/**
		 * 删除主要借款明细信息
		 * @param  fncLoan  主要借款明细信息
		 * @param  String cusId 客户代码
		 * @param  String fncYm 年月
		 * @param  String fncTyp 报表周期类型
		 * @param  int seq 序号
		 * @return	String   返回提示信息
		 * @throws Exception
	 */
	public String deleteRecord(String cusId,String fncYm,String fncTyp,int seq) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
		
			Map<String, String> pk_values = new HashMap<String, String>();
			pk_values.put("cus_id", cusId);
			pk_values.put("fnc_ym", fncYm);
			pk_values.put("fnc_typ", fncTyp);
			pk_values.put("seq", seq+"");
			//根据主键删除客户信息
			int count = this.removeCMISDomainByKeywords(PUBConstant.FNCLOAN, pk_values);	// 1成功  其他失败
				//如果失败，给标志信息赋值
			if(1 == count){
				flagInfo = CMISMessage.SUCCESS;	//成功
			}	
			return flagInfo;
	}
	
	/**
		 * 修改主要借款明细信息
		 * @param  fncLoan  主要借款明细信息
		 * @return	String   返回提示信息
		 * @throws Exception
	 */
	/*public String updateRecord(FncLoan fncLoan) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
       //更新信息
		int count = this.modifyCMISDomain(fncLoan, PUBConstant.FNCLOAN);// 1成功  其他失败
			
		//如果失败，给标志信息赋值
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}
  	  	
		return flagInfo;
	}*/
	
	/**
		* 查询主要借款明细信息
		* @param  String cusId 客户代码
		* @param  String fncYm 年月
		* @param  String fncTyp 报表周期类型
		* @param  int seq 序号
		* @return	FncLoan   主要借款明细信息
		* @throws Exception
	 */
/*	
 * 2012.6.16杨蕾注释
 * public FncLoan queryDetail(String cusId,String fncYm,String fncTyp,int seq) throws AgentException {
		FncLoan fncLoan = new FncLoan();

		//把联合主键放入Map中
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusId);
		pk_values.put("fnc_ym", fncYm);
		pk_values.put("fnc_typ", fncTyp);
		pk_values.put("seq", seq+"");
		//得到查询结果
		fncLoan = (FncLoan)this.findCMISDomainByKeywords(fncLoan,
				PUBConstant.FNCLOAN, pk_values);
		
		return fncLoan;
	}
	*/
}








