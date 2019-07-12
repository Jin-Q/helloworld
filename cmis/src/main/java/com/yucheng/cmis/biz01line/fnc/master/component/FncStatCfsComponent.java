package com.yucheng.cmis.biz01line.fnc.master.component;



import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.agent.FncStatCfsAgent;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;
  /**
 *@Classname	FncStatCfsComponent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 13, 2008 5:31:56 PM  
 *@Copyright 	yuchengtech
 *@Author 		an
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncStatCfsComponent extends CMISComponent{
	/**
	 * 
	 * @param cusId
	 * @param statPrdStyle
	 * @param statPrd
	 * @return
	 * @throws ComponentException
	 */
	public FncStatBase findOneFncStatBase(String cusId,String statPrdStyle,String statPrd)throws ComponentException{
		FncStatBase pfncStatBase = null;
		//创建业务代理类
		FncStatCfsAgent fncStatCfsAgent = (FncStatCfsAgent) this
				.getAgentInstance(PUBConstant.FNCSTATCFS);
		
		pfncStatBase = fncStatCfsAgent.queryDetailFncStatBase(cusId, statPrdStyle, statPrd);
		
		return pfncStatBase;
	}
	/**
	 * 
	 * @param cusId
	 * @param statPrdStyle
	 * @param statPrd
	 * @param styleId
	 * @return
	 * @throws ComponentException
	 */
	public FncConfStyles findOneFncConfStyles(String cusId,String statPrdStyle,String statPrd,String styleId)throws ComponentException{
		FncConfStyles pfcConfStyles = null;
		//创建业务代理类
		FncStatCfsAgent fncStatCfsAgent = (FncStatCfsAgent) this
				.getAgentInstance(PUBConstant.FNCSTATCFS);
		pfcConfStyles = fncStatCfsAgent.queryDetailFncConfStyles(cusId, statPrdStyle, statPrd,styleId);
		return pfcConfStyles;
	}
}
