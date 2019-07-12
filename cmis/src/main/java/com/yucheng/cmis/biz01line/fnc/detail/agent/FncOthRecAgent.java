package com.yucheng.cmis.biz01line.fnc.detail.agent;


import java.util.HashMap;
import java.util.Map;

import com.yucheng.cmis.biz01line.fnc.detail.domain.FncOthReceive;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
  /**
 *@Classname	FncOthRecAgent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 7, 2008 11:44:15 AM  
 *@Copyright 	yuchengtech
 *@Author 		Administrator
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncOthRecAgent extends CMISAgent{
    
	/**
	 * 新增其它应收款信息
	 * @param fncDetOthRec
	 * @return String
	 * @throws AgentException
	 */
	public String addRecord(FncOthReceive fncDetOthRec) throws AgentException {
		// TODO Auto-generated method stub
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)
		
		//新增信息
		int flag = this.insertCMISDomain(fncDetOthRec, PUBConstant.FNCOTHERRECEIVABLE);
		if(flag == 1){
			flagInfo = CMISMessage.SUCCESS;
		}
		
		return flagInfo;
	}

	/**
	 * 根据主键删除其它应收款信息
	 * @param cusId
	 * @param fncYm
	 * @param fncTyp
	 * @param seq
	 * @return String
	 * @throws AgentException
	 */
	public String deleteRecord(String cusId, String fncYm, String fncTyp, int seq) throws AgentException {
		// TODO Auto-generated method stub
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
		
		//把联合主键放进Map中
		Map<String, String> pk_values = new HashMap<String, String>();	
		pk_values.put("cus_id", cusId);
		pk_values.put("fnc_ym", fncYm);
		pk_values.put("fnc_typ", fncTyp);
		String seq_value = String.valueOf(seq);
		pk_values.put("seq", seq_value);
		
		//删除信息
		int count = this.removeCMISDomainByKeywords(PUBConstant.FNCOTHERRECEIVABLE, pk_values);
		if(count == 1){
			flagInfo = CMISMessage.SUCCESS;
		}
		return flagInfo;
	}

    /**
     * 根据主键查找其它应收款信息
     * @param cusId
     * @param fncYm
     * @param fncTyp
     * @param seq
     * @return FncOthReceive
     * @throws AgentException
     */
	public FncOthReceive queryDetail(String cusId, String fncYm, String fncTyp, int seq)throws AgentException {
		// TODO Auto-generated method stub
		FncOthReceive pfncOthReceive = new FncOthReceive();
		
		//把联合主键放进Map中
		Map<String, String> pk_values = new HashMap<String, String>();	
		pk_values.put("cus_id", cusId);
		pk_values.put("fnc_ym", fncYm);
		pk_values.put("fnc_typ", fncTyp);
		String seq_value = String.valueOf(seq);
		pk_values.put("seq", seq_value);
		//查看信息
		pfncOthReceive = (FncOthReceive)this.findCMISDomainByKeywords(pfncOthReceive,PUBConstant.FNCOTHERRECEIVABLE, pk_values);
		
		return pfncOthReceive;
	}

    /**
     * 修改其它应收款信息
     * @param fncDetOthRec
     * @return String
     * @throws AgentException
     */
	public String updateRecord(FncOthReceive fncDetOthRec) throws AgentException {
		// TODO Auto-generated method stub
		String flagInfo = CMISMessage.DEFEAT;
		
		//修改其它应收款信息
		int count = this.modifyCMISDomain(fncDetOthRec, PUBConstant.FNCOTHERRECEIVABLE);
		if(count == 1){
			flagInfo = CMISMessage.SUCCESS;
		}
		return flagInfo;
	}

}
