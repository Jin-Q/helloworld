package com.yucheng.cmis.biz01line.fnc.detail.agent;


import java.util.HashMap;
import java.util.Map;

import com.yucheng.cmis.biz01line.fnc.detail.domain.FncOtherPayable;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;

/**
 * 其它应付款明细代理
 *@Classname	FncOthPayAgent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 8, 2008 4:51:24 PM  
 *@Copyright  2008 yuchengtech
 *@Author 		gongjx
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncOthPayAgent extends CMISAgent {
	
	/**
		* 新增其它应付款明细对象
		* @param  fncOthPay 其它应付款明细
		* @return	String   返回值说明
		* @throws Exception
	 */
	public String addRecord(FncOtherPayable fncOthPay) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
		
		//新增其它应付款明细记录
		int count = this.insertCMISDomain(fncOthPay, PUBConstant.FNCOTHERPAYABLE);	// 1成功  其他失败
			//如果失败，给标志信息赋值
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}
		return flagInfo;
	}
	
	/**
	* 删除其它应付款明细对象
	* @param  String cusId 客户代码
	* @param  String fncYm 年月
	* @param  String fncTyp 报表周期类型
	* @param  int seq 序号
	* @return	String   返回值说明
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
			int count = this.removeCMISDomainByKeywords(PUBConstant.FNCOTHERPAYABLE, pk_values);	// 1成功  其他失败
				//如果失败，给标志信息赋值
			if(1 == count){
				flagInfo = CMISMessage.SUCCESS;	//成功
			}	
			return flagInfo;
	}
	
	/**
		* 修改其它应付款明细对象
		* @param  fncOthPay 其它应付款明细对象
		* @return	String   返回值说明
		* @throws Exception
	 */
	public String updateRecord(FncOtherPayable fncOthPay) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
       //更新信息
		int count = this.modifyCMISDomain(fncOthPay, PUBConstant.FNCOTHERPAYABLE);// 1成功  其他失败
			
		//如果失败，给标志信息赋值
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}
  	  	
		return flagInfo;
	}
	
	/**
		 * 查询其它应付款明细
		 * @param fncOthPay  其它应付款明细
		 * @param int seq  客户序号
		 * @param String cusId  客户代码
		 * @param String fncYm  年月
		 * @param String fcnTyp  报表周期类型
		 * @return	FncOthPay  其它应付款明细
		 * @throws Exception
	 */
	public FncOtherPayable queryDetail(String cusId,String fncYm,String fncTyp,int seq) throws AgentException {
		FncOtherPayable fncOthPay = new FncOtherPayable();

		//把联合主键放入Map中
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusId);
		pk_values.put("fnc_ym", fncYm);
		pk_values.put("fnc_typ", fncTyp);
		pk_values.put("seq", seq+"");
		//得到查询结果
		fncOthPay = (FncOtherPayable)this.findCMISDomainByKeywords(fncOthPay,
						PUBConstant.FNCOTHERPAYABLE, pk_values);
		
		return fncOthPay;
	}
	
}
