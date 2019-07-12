package com.yucheng.cmis.biz01line.fnc.detail.agent;


import java.util.HashMap;
import java.util.Map;

import com.yucheng.cmis.biz01line.fnc.detail.domain.FncAccPayable;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
  /**
 *@Classname	FncAccPayAgent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 7, 2008 11:44:55 AM  
 *@Copyright 	yuchengtech
 *@Author 		Administrator
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncAccPayAgent extends CMISAgent{
/**
 * 新增信息
 * @param pfncDetAccPay
 * @return String
 * @throws AgentException
 */
	public String addRecord(FncAccPayable pfncDetAccPay) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		//添加信息
		int count = this.insertCMISDomain(pfncDetAccPay,PUBConstant.FNCACCPAYABLE); // 1成功  其他失败

		if (1 == count) {
			//表示成功
			flagInfo = CMISMessage.SUCCESS; //成功
		}
		return flagInfo;
	}
/**
 * 根据主键删除信息
 * @param cusId
 * @param fncYm
 * @param fcnTyp
 * @param seq
 * @return String
 * @throws AgentException
 */
	public String deleteRecord(String cusId, String fncYm, String fcnTyp,int seq) throws AgentException{
		// TODO Auto-generated method stub
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
		
		//把联合主键放进Map中
		Map<String, String> pk_values = new HashMap<String, String>();	
		pk_values.put("cus_id", cusId);
		pk_values.put("fnc_ym", fncYm);
		pk_values.put("fnc_typ", fcnTyp);
		String seq_value = String.valueOf(seq);
		pk_values.put("seq", seq_value);
		
        //进行删除操作
		int count = this.removeCMISDomainByKeywords(PUBConstant.FNCACCPAYABLE, pk_values);
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}  	  	
		return flagInfo;
	}
/**
 * 根据主键查找信息
 * @param cusId
 * @param fncYm
 * @param fncTyp
 * @param seq
 * @return FncAccPayable
 * @throws AgentException
 */
	public FncAccPayable queryDetail(String cusId, String fncYm, String fncTyp, int seq) throws AgentException{
		FncAccPayable pfncAccPay = new FncAccPayable();
		
        //把联合主键放进Map中
		Map<String, String> pk_values = new HashMap<String, String>();	
		pk_values.put("cus_id", cusId);
		pk_values.put("fnc_ym", fncYm);
		pk_values.put("fnc_typ", fncTyp);
		String seq_value = String.valueOf(seq);
		pk_values.put("seq", seq_value);

		//进行查询操作
		pfncAccPay = (FncAccPayable) this.findCMISDomainByKeywords(
				pfncAccPay, PUBConstant.FNCACCPAYABLE, pk_values);

		return pfncAccPay;
	}
/**
 * 修改信息
 * @param fncDetAccPay
 * @return String
 * @throws AgentException
 */
	public String updateRecord(FncAccPayable fncDetAccPay) throws AgentException {
		// TODO Auto-generated method stub
		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		//更新信息
		int count = this.modifyCMISDomain(fncDetAccPay,
				PUBConstant.FNCACCPAYABLE);// 1成功  其他失败

		if (1 == count) {
			//成功
			flagInfo = CMISMessage.SUCCESS;
		}
		return flagInfo;
	}

}
