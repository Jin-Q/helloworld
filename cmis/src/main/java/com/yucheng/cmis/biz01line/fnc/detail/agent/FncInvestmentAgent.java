package com.yucheng.cmis.biz01line.fnc.detail.agent;

import java.util.HashMap;
import java.util.Map;

import com.yucheng.cmis.biz01line.fnc.detail.domain.FncInvestment;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;

/**
 *@Classname	FncInvestmentAgent.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-7 下午03:13:35  
 *@Copyright 	2008 yucheng Co. Ltd.
 *@Author 		biwq
 *@Description：
 *@Lastmodified 
 *@Author
 */

public class FncInvestmentAgent extends CMISAgent{
	/**
	 * 插入财务报表明细信息—主要长期投资明细
	 * @param fncInvestment 财务报表明细信息—主要长期投资明细
	 * @return 信息编码flag为"000000"的时候表示成功 默认"999999"失败
	 * @throws AgentException
	 */
	public String addRecord (FncInvestment fncInvestment) throws AgentException{
		
		String flag = CMISMessage.DEFEAT; //错误信息(默认失败)
		
		//新增一条记录
		int count = this.insertCMISDomain(fncInvestment, PUBConstant.FNCINVESTMENT);
		
		if(1 == count){
			flag = CMISMessage.SUCCESS;//如果成功,则给标志看
		}
		return flag;
	}
	/**
	 * 更新财务报表明细信息—主要长期投资明细
	 * @param fncInvestment
	 * @return fncInvestment	财务报表明细信息—主要长期投资明细
	 * @throws AgentException
	 */
	public String updateRecord (FncInvestment fncInvestment)throws AgentException{
		
		String flag = CMISMessage.DEFEAT;//错误信息(默认失败)
		
		//修改一条记录
		int count = this.modifyCMISDomain(fncInvestment, PUBConstant.FNCINVESTMENT);
		
		if(1 == count){
			flag = CMISMessage.SUCCESS;//如果成功,则给标志看
		}
		return flag;
	}
	/**
	 * 删除财务报表明细信息—主要长期投资明细
	 * @param cusId
	 * @param fncYm
	 * @param fncTyp
	 * @param seq
	 * @return 信息编码flag为"000000"的时候表示成功 默认"999999"失败
	 * @throws AgentException
	 */
	public String deleteRecord (String cusId,String fncYm,String fncTyp,String seq)throws AgentException{
		
		String flag = CMISMessage.DEFEAT; //错误信息(默认失败)
		
		//创建MAP存储 财务报表明细信息—主要存货明细 客户代码 和 年月 报表周期类型 序号
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusId);	
		pk_values.put("fnc_ym", fncYm);
		pk_values.put("fnc_typ", fncTyp);
		pk_values.put("seq", String.valueOf(seq));
		
		//删除一条记录
		int count = this.removeCMISDomainByKeywords(PUBConstant.FNCINVESTMENT, pk_values);
		
		if(1 == count){
			flag = CMISMessage.SUCCESS;//如果成功,则给标志看
		}
		return flag;
	}
	/**
	 * 查询财务报表明细信息—主要长期投资明细
	 * @param cusId
	 * @param fncYm
	 * @param fncTyp
	 * @param seq
	 * @return fncInvestment 财务报表明细信息—主要长期投资明细
	 * @throws AgentException
	 */
	public FncInvestment queryDetail(String cusId,String fncYm,String fncTyp,String seq)throws AgentException{
		
		FncInvestment fncInvestment = new FncInvestment();
		
		//		把联合主键放进Map中
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusId);	
		pk_values.put("fnc_ym", fncYm);
		pk_values.put("fnc_typ", fncTyp);
		pk_values.put("seq", seq);
		
		//根据主键查询
		fncInvestment = (FncInvestment)this.findCMISDomainByKeywords(fncInvestment,PUBConstant.FNCINVESTMENT, pk_values);
		return fncInvestment;
	}
}
