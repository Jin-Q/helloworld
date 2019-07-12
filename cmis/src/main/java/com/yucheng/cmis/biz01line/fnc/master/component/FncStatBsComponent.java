package com.yucheng.cmis.biz01line.fnc.master.component;


import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.agent.FncStatBsAgent;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

public class FncStatBsComponent extends CMISComponent {

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
		FncStatBsAgent fncStatBsAgent = (FncStatBsAgent) this
				.getAgentInstance(PUBConstant.FNCSTATBS);
		
		pfncStatBase = fncStatBsAgent.queryDetailFncStatBase(cusId, statPrdStyle, statPrd);
		
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
		FncStatBsAgent fncStatBsAgent = (FncStatBsAgent) this
				.getAgentInstance(PUBConstant.FNCSTATBS);
		pfcConfStyles = fncStatBsAgent.queryDetailFncConfStyles(cusId, statPrdStyle, statPrd,styleId);
		return pfcConfStyles;
	}
	
	/**
	 * 根据客户号，指标编号，年号获取客户资产负债表中此项指标的年末值
	 * @param cusId
	 * @param statItemId
	 * @param statYear
	 * @return
	 * @throws ComponentException
	 */
	
	public String getYearEndValue(String cusId,String statItemId,String statYear)throws ComponentException{
		FncStatBsAgent fncStatBsAgent = (FncStatBsAgent) this
		.getAgentInstance(PUBConstant.FNCSTATBS);
		return fncStatBsAgent.getYearEndValue(cusId, statItemId, statYear);
	}
}
