package com.yucheng.cmis.biz01line.fnc.master.component;


import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.agent.FncStatIsAgent;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

public class FncStatIsComponent extends CMISComponent {

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
		FncStatIsAgent fncStatIsAgent = (FncStatIsAgent) this
				.getAgentInstance(PUBConstant.FNCSTATIS);
		
		pfncStatBase = fncStatIsAgent.queryDetailFncStatBase(cusId, statPrdStyle, statPrd);
		
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
		FncStatIsAgent fncStatIsAgent = (FncStatIsAgent) this
				.getAgentInstance(PUBConstant.FNCSTATIS);
		pfcConfStyles = fncStatIsAgent.queryDetailFncConfStyles(cusId, statPrdStyle, statPrd,styleId);
		return pfcConfStyles;
	}
}
