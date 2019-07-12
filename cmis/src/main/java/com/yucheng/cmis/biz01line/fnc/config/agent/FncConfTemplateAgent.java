package com.yucheng.cmis.biz01line.fnc.config.agent;

import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfTemplate;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
  /**
 *@Classname	FncConfTemplateAgent.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-6 下午08:25:36  
 *@Copyright 	yuchengtech
 *@Author 		Yu
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncConfTemplateAgent extends CMISAgent {
	
	public String addRecord(FncConfTemplate pfncConfTemplate) throws AgentException {

		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		//添加信息
		int count = this.insertCMISDomain(pfncConfTemplate,
				PUBConstant.FNCCONFTEMPLATE); // 1成功  其他失败

		if (1 == count) {
			//表示成功
			flagInfo = CMISMessage.SUCCESS; //成功
		}
		return flagInfo;
	}
/**
 * 
 * @param pfncConfItems  报表配置项目列表信息
 * @return   flagInfo 信息编码
 * @throws AgentException
 */
	public String updateRecord(FncConfTemplate pfncConfTemplate)
			throws AgentException {

		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		//更新信息
		int count = this.modifyCMISDomain(pfncConfTemplate,
				PUBConstant.FNCCONFTEMPLATE);// 1成功  其他失败

		if (1 == count) {
			//成功
			flagInfo = CMISMessage.SUCCESS;
		}
		return flagInfo;

	}
/**
 * 
 * @param itemId  项目编号
 * @return  报表配置项目列表信息对象
 * @throws AgentException
 */
	public FncConfTemplate queryDetail(String fncId) throws AgentException {

		FncConfTemplate pfncConfTemplate = new FncConfTemplate();

		//进行查询操作
		pfncConfTemplate = (FncConfTemplate) this.findCMISDomainByKeyword(
				pfncConfTemplate, PUBConstant.FNCCONFTEMPLATE, fncId);

		return pfncConfTemplate;
	}
	/**
	 * 
	 * @param itemId   项目编号
	 * @return   flagInfo 信息编码
	 * @throws AgentException
	 */
	public String deleteRecord(String fncId) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
	       
        //进行删除操作
		int count = this.removeCMISDomainByKeyword(PUBConstant.FNCCONFTEMPLATE, fncId);
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}  	  	
		return flagInfo;

}

}
