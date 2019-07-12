package com.yucheng.cmis.biz01line.fnc.config.agent;

import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfItems;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfItems4Query;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;

/**
 *@Classname	FncConfItemsAgent.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-6 下午04:04:12  
 *@Copyright 	yuchengtech
 *@Author 		Yu
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncConfItemsAgent extends CMISAgent {
 /**
  * 
  * @param pfncConfItems   报表配置项目列表信息
  * @return    flagInfo 信息编码
  * @throws AgentException
  */
	public String addRecord(FncConfItems pfncConfItems) throws AgentException {

		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		//添加信息
		int count = this.insertCMISDomain(pfncConfItems,
				PUBConstant.FNCCONFITEMS); // 1成功  其他失败

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
	public String updateRecord(FncConfItems pfncConfItems)
			throws AgentException {

		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		//更新信息
		int count = this.modifyCMISDomain(pfncConfItems,
				PUBConstant.FNCCONFITEMS);// 1成功  其他失败

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
	public FncConfItems4Query queryDetail(String itemId) throws AgentException {

		FncConfItems4Query pfncConfItems = new FncConfItems4Query();

		//进行查询操作
		pfncConfItems = (FncConfItems4Query) this.findCMISDomainByKeyword(
				pfncConfItems, PUBConstant.FNCCONFITEMS, itemId);

		return pfncConfItems;
	}
	/**
	 * 
	 * @param itemId   项目编号
	 * @return   flagInfo 信息编码
	 * @throws AgentException
	 */
	public String deleteRecord(String itemId) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
	       
        //进行删除操作
		int count = this.removeCMISDomainByKeyword(PUBConstant.FNCCONFITEMS, itemId);
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}  	  	
		return flagInfo;

}

}
