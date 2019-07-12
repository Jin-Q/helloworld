package com.yucheng.cmis.biz01line.fnc.config.component;


import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.fnc.config.agent.FncConfItemsAgent;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfItems;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfItems4Query;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.FNCItemsFactory;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 *@Classname	Component.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-6 下午04:05:31  
 *@Copyright 	yuchengtech
 *@Author 		Yu
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncConfItemsComponent extends CMISComponent {
/**
 * 
 * @param pfncConfItems  报表配置项目列表信息
 * @return  flagInfo 信息编码
 * @throws ComponentException
 */
	public String addFncConfItems(FncConfItems pfncConfItems)
			throws ComponentException {
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncConfItemsAgent fncConfItemsAgent = (FncConfItemsAgent) this
				.getAgentInstance("FncConfItems");

		// 新增一条成员信息
		flagInfo = fncConfItemsAgent.addRecord(pfncConfItems);

		return flagInfo;
	};
/**
 * 
 * @param pfncConfItems  报表配置项目列表信息
 * @return  flagInfo 信息编码
 * @throws ComponentException
 */
	public String modifyFncConfItems(FncConfItems pfncConfItems)
			throws ComponentException {
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncConfItemsAgent fncConfItemsAgent = (FncConfItemsAgent) this
				.getAgentInstance("FncConfItems");

		//通过代理类进行修改操作
		flagInfo = fncConfItemsAgent.updateRecord(pfncConfItems);

		return flagInfo;
	};
/**
 * 
 * @param itemId  项目编号
 * @return  pfncConfItems 报表配置项目列表信息
 * @throws ComponentException
 */
	public FncConfItems4Query findFncConfItems(String itemId)
			throws ComponentException {

		FncConfItems4Query pfncConfItems = new FncConfItems4Query();
		//创建业务代理类
		FncConfItemsAgent fncConfItemsAgent = (FncConfItemsAgent) this
				.getAgentInstance("FncConfItems");
		//通过代理类进行查看操作
		pfncConfItems = fncConfItemsAgent.queryDetail(itemId);

		return pfncConfItems;

	};
	
	/**
	 * 从缓存中获取item对象
	 * @param itemId  项目编号
	 * @return  pfncConfItems 报表配置项目列表信息
	 * @throws EMPException 
	 */
		public FncConfItems4Query findFncConfItemsFromCashe(String itemId)
				throws EMPException {

			FncConfItems4Query pfncConfItems = new FncConfItems4Query();
			
			pfncConfItems=FNCItemsFactory.getFNCItemsFactoryInstance().
			getFNCItmeInstance(itemId);
			

			return pfncConfItems;

		};
	
	/**
	 * 
	 * @param itemId  项目编号
	 * @return  flagInfo 信息编码
	 * @throws ComponentException
	 */

	public String removeFncConfItems(String itemId) throws ComponentException {

		String flagInfo = CMISMessage.DEFEAT;
		//创建业务代理类
		FncConfItemsAgent fncConfItemsAgent = (FncConfItemsAgent) this
		.getAgentInstance("FncConfItems");
		//通过代理类进行删除操作
		flagInfo = fncConfItemsAgent.deleteRecord(itemId);
		return flagInfo;
	};

}
