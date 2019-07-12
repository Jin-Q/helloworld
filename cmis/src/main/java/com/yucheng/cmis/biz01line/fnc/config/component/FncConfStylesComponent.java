package com.yucheng.cmis.biz01line.fnc.config.component;


import java.util.List;

import com.yucheng.cmis.biz01line.fnc.config.agent.FncConfDefFormatAgent;
import com.yucheng.cmis.biz01line.fnc.config.agent.FncConfStylesAgent;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfDefFormat;
import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * @Classname FncConfStylesComponent.java
 * @Version 1.0
 * @Since 1.0 2008-10-6 下午04:07:12
 * @Copyright yuchengtech
 * @Author Yu
 * @Description：
 * @Lastmodified
 * @Author
 */
public class FncConfStylesComponent extends CMISComponent {
/**
 * 
 * @param pfncConfStyles  报表样式列表信息对象
 * @return flagInfo 信息编码
 * @throws ComponentException
 */
	public String addFncConfStyles(FncConfStyles pfncConfStyles)
			throws ComponentException {
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncConfStylesAgent fncConfStylesAgent = (FncConfStylesAgent) this
				.getAgentInstance("FncConfStyles");

		// 新增一条成员信息
		flagInfo = fncConfStylesAgent.addRecord(pfncConfStyles);

		return flagInfo;
	};
/**
 * 
 * @param pfncConfStyles  报表样式列表信息对象
 * @return  flagInfo 信息编码
 * @throws ComponentException
 */
	public String modifyFncConfStyles(FncConfStyles pfncConfStyles)
			throws ComponentException {
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)

		// 创建业务代理类
		FncConfStylesAgent fncConfStylesAgent = (FncConfStylesAgent) this
				.getAgentInstance("FncConfStyles");

		// 通过代理类进行修改操作
		flagInfo = fncConfStylesAgent.updateRecord(pfncConfStyles);

		return flagInfo;
	};
/**
 * 
 * @param styleId  报表样式编号
 * @return  pfncConfStyles 报表样式列表信息对象
 * @throws ComponentException
 */
	public FncConfStyles findFncConfStyles(String styleId)
			throws ComponentException {

		FncConfStyles pfncConfStyles = new FncConfStyles();
		// 创建业务代理类
		FncConfStylesAgent fncConfStylesAgent = (FncConfStylesAgent) this
				.getAgentInstance("FncConfStyles");
		// 通过代理类进行查看操作
		pfncConfStyles = fncConfStylesAgent.queryDetail(styleId);

		return pfncConfStyles;

	};
	/**
	 * 
	 * @param styleId   报表样式编号
	 * @return  flagInfo 信息编码
	 * @throws ComponentException
	 */
	public String removeFncConfStyles(String styleId) throws ComponentException {

		String flagInfo = CMISMessage.DEFEAT;
		//创建业务代理类
		FncConfStylesAgent fncConfStylesAgent = (FncConfStylesAgent) this
				.getAgentInstance("FncConfStyles");
		//通过代理类进行删除操作
		flagInfo = fncConfStylesAgent.deleteRecord(styleId);
		return flagInfo;
	};
	
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
public List getFormats(String styleId) throws ComponentException{
//	 创建业务代理类
	FncConfDefFormatAgent fncConfDefFormatAgent = (FncConfDefFormatAgent) this
	.getAgentInstance(PUBConstant.FNCCONFDEFFORMAT);
	List list = fncConfDefFormatAgent.getFormats(styleId);
	return list;
}

}
