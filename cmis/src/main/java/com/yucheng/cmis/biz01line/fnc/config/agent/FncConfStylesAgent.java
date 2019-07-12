package com.yucheng.cmis.biz01line.fnc.config.agent;

import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
  /**
 *@Classname	FncConfStylesAgent.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-6 下午04:06:44  
 *@Copyright 	yuchengtech
 *@Author 		Yu
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncConfStylesAgent extends CMISAgent {
	/**
	 * 
	 * @param pfncConfStyles  报表样式列表信息对象
	 * @return  flagInfo 信息编码
	 * @throws AgentException
	 */
	public String addRecord(FncConfStyles pfncConfStyles) throws AgentException {

		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		//添加信息
		int count = this.insertCMISDomain(pfncConfStyles,PUBConstant.FNCCONFSTYLES); // 1成功  其他失败

		if (1 == count) {
			//表示成功
			flagInfo = CMISMessage.SUCCESS; //成功
		}
		return flagInfo;
	}
/**
 * 
 * @param pfncConfStyles  报表样式列表信息对象
 * @return   flagInfo 信息编码
 * @throws AgentException
 */
	public String updateRecord(FncConfStyles pfncConfStyles)
			throws AgentException {

		String flagInfo = CMISMessage.DEFEAT; //错误信息（默认失败）

		//更新信息
		int count = this.modifyCMISDomain(pfncConfStyles,
				PUBConstant.FNCCONFSTYLES);// 1成功  其他失败

		if (1 == count) {
			//成功
			flagInfo = CMISMessage.SUCCESS;
		}
		return flagInfo;

	}
/**
 * 
 * @param styleId  报表样式编号
 * @return  pfncConfStyles 报表样式列表信息对象
 * @throws AgentException
 */
	public FncConfStyles queryDetail(String styleId) throws AgentException {

		FncConfStyles pfncConfStyles = new FncConfStyles();

		//进行查询操作
		pfncConfStyles = (FncConfStyles) this.findCMISDomainByKeyword(
				pfncConfStyles, PUBConstant.FNCCONFSTYLES, styleId);

		return pfncConfStyles;
	}
	/**
	 * 
	 * @param styleId  报表样式编号
	 * @return   flagInfo 信息编码
	 * @throws AgentException
	 */
	public String deleteRecord(String styleId) throws AgentException {
		String flagInfo = CMISMessage.DEFEAT;	//错误信息（默认失败）
	       
        //进行删除操作
		int count = this.removeCMISDomainByKeyword(PUBConstant.FNCCONFSTYLES, styleId);
		if(1 == count){
			flagInfo = CMISMessage.SUCCESS;	//成功
		}  	  	
		return flagInfo;

}

}
