package com.yucheng.cmis.biz01line.fnc.master.component;


import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.agent.FncIndexRptAgent;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 *@Classname	FncIndexRptComponent.java
 *@Version 1.0	
 *@Since   1.0 	2008-10-13 下午07:59:57  
 *@Copyright 	2008 yucheng Co. Ltd.
 *@Author 		biwq
 *@Description：
 *@Lastmodified 
 *@Author
 */

public class FncIndexRptComponent extends CMISComponent{
	/**
	 * 得到一个客户的财报对象
	 * @param cusId
	 * @param statPrdStyle
	 * @param statPrd
	 * @return
	 * @throws ComponentException
	 */
	public FncStatBase findOneFncStatBase(String cusId,String statPrdStyle,String statPrd)throws ComponentException{
		FncStatBase pfncStatBase = null;
		String flagInfo = CMISMessage.DEFEAT; // 信息编码(默认失败)
		//创建业务代理类
		FncIndexRptAgent fncIndexRptAgent = (FncIndexRptAgent) this
				.getAgentInstance(PUBConstant.FNCINDEXRPT);
		
		pfncStatBase = fncIndexRptAgent.queryDetailFncStatBase(cusId, statPrdStyle, statPrd);
		
		return pfncStatBase;
	}
	
	/**
	 * 组装标签样式对象(带数据的)
	 * @param cusId
	 * @param statPrdStyle
	 * @param statPrd
	 * @return
	 * @throws ComponentException
	 */
	public FncConfStyles findOneFncConfStyles(String cusId,String statPrdStyle,String statPrd,String styleId)throws ComponentException{
		FncConfStyles pfcConfStyles = null;
		//创建业务代理类
		FncIndexRptAgent fncStatFiAgent = (FncIndexRptAgent) this
				.getAgentInstance(PUBConstant.FNCINDEXRPT);
		pfcConfStyles = fncStatFiAgent.queryDetailFncConfStyles(cusId, statPrdStyle, statPrd,styleId);
		return pfcConfStyles;
	}
	
	/**
	 * 根据客户号，指标编号，年号获取客户财务指标表 中此项指标的年末值
	 * @param cusId
	 * @param statItemId
	 * @param statYear
	 * @return
	 * @throws ComponentException
	 */
	
	public String getYearEndValue(String cusId,String statItemId,String statYear)throws ComponentException{
		FncIndexRptAgent fncStatFiAgent = (FncIndexRptAgent) this
		.getAgentInstance(PUBConstant.FNCINDEXRPT);
		return fncStatFiAgent.getYearEndValue(cusId, statItemId, statYear);
	}
}
