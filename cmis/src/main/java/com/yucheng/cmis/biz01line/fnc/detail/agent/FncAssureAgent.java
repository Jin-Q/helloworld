package com.yucheng.cmis.biz01line.fnc.detail.agent;


import java.util.HashMap;
import java.util.Map;

import com.yucheng.cmis.biz01line.fnc.detail.domain.FncAssure;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;


/**
 *@Classname	FncAssureAgent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 7, 2008 5:13:41 PM  
 *@Copyright 	yuchengtech
 *@Author 		Eric
 *@Description：本来处理主要对外担保及表外业务明细有关的数据业务
 *@Lastmodified 
 *@Author
 */
public class FncAssureAgent extends CMISAgent{
	
	/**
	 * 新增一条对外担保及表外业务明细信息
	 * @param pfncAssure	对外担保及表外业务明细信息
	 * @return	信息编码
	 * @throws AgentException
	 */
	public String addRecord(FncAssure pfncAssure) throws AgentException{
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）

		//新增记录
		int count = this.insertCMISDomain(pfncAssure, PUBConstant.FNCASSURE);	// 1成功  其他失败
			//如果失败，给标志信息赋值
		
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}
  	  	
		return flagInfo;
	}
	
	/**
	 * 根据主键删除记录
	 * @param cusId 客户ID
	 * @param Fnc_Ym	年月	
	 * @param Fnc_Typ	报表周期类型
	 * @param seq	序号
	 * @return 信息编码
	 * @throws AgentException
	 */
	public String deleteRecord(String cusId, String fncYm, String fncTyp, String seq ) throws AgentException{
		
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
		//创建MAP存储 高管家族成员客户代码 和 客户代码
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusId);	
		pk_values.put("fnc_ym", fncYm);
		pk_values.put("fnc_typ", fncTyp);
		pk_values.put("seq", seq);

		//根据主键删除客户信息
		int count = this.removeCMISDomainByKeywords(PUBConstant.FNCASSURE, pk_values);	// 1成功  其他失败
			//如果失败，给标志信息赋值
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}
  	  	
		return flagInfo;
	
	}
	
	/**
	 * 修改对外担保及表外业务明细信息
	 * @param pfncAssure 对外担保及表外业务明细信息
	 * @return 信息编码
	 * @throws AgentException
	 */
	public String updateRecord(FncAssure pfncAssure) throws AgentException{
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
		
		//更新信息
		int count = this.modifyCMISDomain(pfncAssure, PUBConstant.FNCASSURE);// 1成功  其他失败
			
		//如果失败，给标志信息赋值
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}
  	  	
		return flagInfo;
	
	}
	
	
	/**
	 * 查询一条对外担保及表外业务明细信息通过主键
	 * @param cusId 客户ID
	 * @param Fnc_Ym	年月	
	 * @param Fnc_Typ	报表周期类型
	 * @param seq	序号
	 * @return
	 * @throws AgentException
	 */
	public FncAssure queryDetail(String cusId, String fncYm, String fncTyp, String seq ) throws AgentException{
		
		//创建对外担保及表外业务明细信息容器
		FncAssure fAssure = new FncAssure();
		
		//设置emp查询参数
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusId);	
		pk_values.put("fnc_ym", fncYm);
		pk_values.put("fnc_typ", fncTyp);
		pk_values.put("seq", seq);
		
		//得到查询结果
		fAssure = (FncAssure)this.findCMISDomainByKeywords(fAssure,
						PUBConstant.FNCASSURE, pk_values);
		
		return fAssure;
		
	}

}
