package com.yucheng.cmis.biz01line.fnc.detail.agent;


import java.util.HashMap;
import java.util.Map;

import com.yucheng.cmis.biz01line.fnc.detail.domain.FncProject;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;

/**
 * 在建工程明细代理类
 *@Classname	FncProjectAgent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 8, 2008 7:00:47 PM  
 *@Copyright  2008 yuchengtech
 *@Author 		gongjx
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncProjectAgent extends CMISAgent {
	
	/**
		* 新增在建工程明细信息
		* @param fncProject  在建工程明细
		* @return	String   返回值说明
		* @throws Exception
	 */
	public String addRecord(FncProject fncProject) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
		
		//新增在建工程明细
		int count = this.insertCMISDomain(fncProject, PUBConstant.FNCPROJECT);	// 1成功  其他失败
			//如果失败，给标志信息赋值
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}
		return flagInfo;
	}
	
	/**
		 * 删除在建工程明细
		 * @param  fncProject 在建工程明细信息
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
			//根据主键删除信息
			int count = this.removeCMISDomainByKeywords(PUBConstant.FNCPROJECT, pk_values);	// 1成功  其他失败
				//如果失败，给标志信息赋值
			if(1 == count){
				flagInfo = CMISMessage.SUCCESS;	//成功
			}	
			return flagInfo;
	}
	
	/**
		 * 修改在建工程明细
		 * @param fncProject  在建工程明细
		 * @return	String   返回提示信息
		 * @throws Exception
	 */
	public String updateRecord(FncProject fncProject) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
       //更新信息
		int count = this.modifyCMISDomain(fncProject, PUBConstant.FNCPROJECT);// 1成功  其他失败
			
		//如果失败，给标志信息赋值
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}
  	  	
		return flagInfo;
	}
	
	/**
	 * 查询在建工程明细
	 * @param int seq  客户序号
	 * @param String cusId  客户代码
	 * @param String fncYm  年月
	 * @param String fcnTyp  报表周期类型
	 * @return	FncProject  在建工程明细
	 * @throws Exception
 */
	public FncProject queryDetail(String cusId,String fncYm,String fncTyp,int seq) throws AgentException {
		FncProject fncProject = new FncProject();

		//把联合主键放入Map中
		Map<String, String> pk_values = new HashMap<String, String>();
		pk_values.put("cus_id", cusId);
		pk_values.put("fnc_ym", fncYm);
		pk_values.put("fnc_typ", fncTyp);
		pk_values.put("seq", seq+"");
		//得到查询结果
		fncProject = (FncProject)this.findCMISDomainByKeywords(fncProject,
				PUBConstant.FNCPROJECT, pk_values);
		
		return fncProject;
	}
	
	
	
}








