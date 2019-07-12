package com.yucheng.cmis.biz01line.fnc.detail.agent;

import java.util.HashMap;
import java.util.Map;

import com.yucheng.cmis.biz01line.fnc.detail.domain.FncInventory;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;

/**
 *@Classname	FncInventoryAgent.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-7 下午03:12:32  
 *@Copyright 	2008 yucheng Co. Ltd.
 *@Author 		biwq
 *@Description：财务报表明细信息—主要存货明细业务逻辑处理类Agent
 *@Lastmodified 
 *@Author
 */

public class FncInventoryAgent extends CMISAgent{

	/**
	 * 插入财务报表明细信息—主要存货明细
	 * @param fncInventory	财务报表明细信息—主要存货明细
	 * @return 信息编码flag为"000000"的时候表示成功 默认"999999"失败
	 * @throws AgentException
	 */
	public String addRecord(FncInventory fncInventory) throws AgentException {
		
		String flag = CMISMessage.DEFEAT; //错误信息（默认失败）
		
		//新增一条记录
		int count = this.insertCMISDomain(fncInventory,PUBConstant.FNCINVENTORY);// 1成功  其他失败
		
		//如果成功，给标志信息赋值
		if(1 == count){
			flag = CMISMessage.SUCCESS;	//成功
		}
		return flag;
	}
	/**
	 * 删除财务报表明细信息—主要存货明细
	 * @param cusId
	 * @param fncYm
	 * @param fncTyp
	 * @param seq
	 * @return 信息编码flag为"000000"的时候表示成功 默认"999999"失败
	 * @throws AgentException
	 */
	public String deleteRecord(String cusId,String fncYm,String fncTyp,int seq) throws AgentException {
		
		String flag = CMISMessage.DEFEAT; //错误信息（默认失败）
		
		//创建MAP存储 财务报表明细信息—主要存货明细 客户代码 和 年月 报表周期类型 序号
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusId);	
		pk_values.put("fnc_ym", fncYm);
		pk_values.put("fnc_typ", fncTyp);
		pk_values.put("seq", String.valueOf(seq));
		
		//		根据主键删除客户信息
		int count = this.removeCMISDomainByKeywords(PUBConstant.FNCINVENTORY, pk_values);	// 1成功  其他失败
		
		//如果成功，给标志信息赋值
		if(1 == count){
			flag = CMISMessage.SUCCESS;	//成功
		}
		return flag;
	}
	/**
	 * 更新财务报表明细信息—主要存货明细
	 * @param fncInventory	财务报表明细信息—主要存货明细
	 * @return 信息编码flag为"000000"的时候表示成功 默认"999999"失败
	 * @throws AgentException
	 */
	public String updateRecord(FncInventory fncInventory) throws AgentException {
		
		String flag = CMISMessage.DEFEAT; //错误信息（默认失败）
		
		//更新信息
		int count = this.modifyCMISDomain(fncInventory,PUBConstant.FNCINVENTORY);// 1成功  其他失败
		
		//		如果成功，给标志信息赋值
		if(1 == count){
			flag = CMISMessage.SUCCESS;	//成功
		}
		return flag;	
	}
	/**
	 * 查询财务报表明细信息—主要存货明细
	 * @param cusId
	 * @param fncYm
	 * @param fncTyp
	 * @param seq
	 * @return fncInventory
	 * @throws AgentException
	 */
	public FncInventory queryDetail(String cusId,String fncYm,String fncTyp,int seq) throws AgentException {
		
		FncInventory fncInventory = new FncInventory();
		
		//		把联合主键放进Map中
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusId);	
		pk_values.put("fnc_ym", fncYm);
		pk_values.put("fnc_typ", fncTyp);
		pk_values.put("seq", String.valueOf(seq));
		
		//根据主键查询
		fncInventory = (FncInventory)this.findCMISDomainByKeywords(fncInventory,PUBConstant.FNCINVENTORY, pk_values);
		return fncInventory;
	}
}
