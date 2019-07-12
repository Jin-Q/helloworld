package com.yucheng.cmis.biz01line.fnc.config.component;

import java.util.List;

import com.yucheng.cmis.biz01line.fnc.config.agent.FncConfDefFormatAgent;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * @Classname FncConfDefFormatComponent.java
 * @Version 1.0
 * @Since 1.0 2008-10-6 下午08:27:24
 * @Copyright yuchengtech
 * @Author Yu
 * @Description：
 * @Lastmodified
 * @Author
 */
public class FncConfDefFormatComponent extends CMISComponent {
/**
 * 
 * @param pfncConfDefFormat
 * @return  flagInfo 信息编码
 * @throws ComponentException
 */
	public String addFncConfDefFormat(FncConfDefFormat pfncConfDefFormat)
			throws ComponentException {
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncConfDefFormatAgent fncConfDefFormatAgent = (FncConfDefFormatAgent) this
				.getAgentInstance(PUBConstant.FNCCONFDEFFORMAT);

		// 新增一条成员信息
		flagInfo = fncConfDefFormatAgent.addRecord(pfncConfDefFormat);

		return flagInfo;
	};
/**
 * 
 * @param pfncConfItems
 * @return  flagInfo 信息编码
 * @throws ComponentException
 */
	public String modifyFncConfDefFormat(FncConfDefFormat pfncConfDefFormat)
			throws ComponentException {
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncConfDefFormatAgent fncConfDefFormatAgent = (FncConfDefFormatAgent) this
		.getAgentInstance(PUBConstant.FNCCONFDEFFORMAT);

		// 通过代理类进行修改操作
		flagInfo = fncConfDefFormatAgent.updateRecord(pfncConfDefFormat);

		return flagInfo;
	};
/**
 * 
 * @param itemId
 * @return
 * @throws ComponentException
 */
	public FncConfDefFormat findFncConfDefFormat(String styleId,String itemId)
			throws ComponentException {

		FncConfDefFormat pfncConfDefFormat = new FncConfDefFormat();
		// 创建业务代理类
		FncConfDefFormatAgent fncConfDefFormatAgent = (FncConfDefFormatAgent) this
		.getAgentInstance(PUBConstant.FNCCONFDEFFORMAT);
		// 通过代理类进行查看操作
		pfncConfDefFormat = fncConfDefFormatAgent.queryDetail(styleId,itemId);

		return pfncConfDefFormat;

	};
/**
 * 
 * @param itemId
 * @return  flagInfo 信息编码
 * @throws ComponentException
 */
	public String removeFncConfDefFormat(String styleId,String itemId) throws ComponentException {

		String flagInfo = CMISMessage.DEFEAT;
		// 创建业务代理类
		FncConfDefFormatAgent fncConfDefFormatAgent = (FncConfDefFormatAgent) this
		.getAgentInstance(PUBConstant.FNCCONFDEFFORMAT);
		// 通过代理类进行删除操作
		flagInfo = fncConfDefFormatAgent.deleteRecord(styleId,itemId);
		return flagInfo;
	};
	
	/**
	 * 
	 * @param styleId
	 * @return
	 * @throws ComponentException
	 */
	public List getFormats(String styleId) throws ComponentException{
//		 创建业务代理类
		FncConfDefFormatAgent fncConfDefFormatAgent = (FncConfDefFormatAgent) this
		.getAgentInstance(PUBConstant.FNCCONFDEFFORMAT);
		List list = fncConfDefFormatAgent.getFormats(styleId);
		return list;
	}
	/**
	 * 
	 * @param styleId
	 * @return
	 * @throws ComponentException
	 */
	public String getFncConfTyp(String styleId) throws ComponentException{
		///		 创建业务代理类
		FncConfDefFormatAgent fncConfDefFormatAgent = (FncConfDefFormatAgent) this
		.getAgentInstance(PUBConstant.FNCCONFDEFFORMAT);
		String fncConfTyp = fncConfDefFormatAgent.getFncConfTyp(styleId);
		return fncConfTyp;
	}
	
	public List getItems(String fncConfTyp)throws ComponentException{
//		 创建业务代理类
		FncConfDefFormatAgent fncConfDefFormatAgent = (FncConfDefFormatAgent) this
		.getAgentInstance(PUBConstant.FNCCONFDEFFORMAT);
		List list = fncConfDefFormatAgent.getItems(fncConfTyp);
		return list;
	}

}
