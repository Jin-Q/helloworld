package com.yucheng.cmis.biz01line.fnc.master.agent;

import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.dao.FncStatBaseDao;
import com.yucheng.cmis.biz01line.fnc.master.dao.FncStatIsDao;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.exception.AgentException;

public class FncStatIsAgent extends CMISAgent {

	/**
	 * 得到一个客户的财报对象
	 * @param cusId
	 * @param statPrdStyle
	 * @param statPrd
	 * @return
	 */
	public FncStatBase queryDetailFncStatBase(String cusId,String statPrdStyle,String statPrd)throws AgentException{
		
		FncStatBase fncStatBase = null;
		FncStatBaseDao bDao = new FncStatBaseDao();
		fncStatBase = bDao.queryDetailFncStatBase(cusId, statPrdStyle, statPrd,this.getConnection());
		return fncStatBase;
	}
	
	/**
	 * 组装标签样式对象(带数据的)
	 * @param cusId
	 * @param statPrdStyle
	 * @param statPrd
	 * @param styleId
	 * @return
	 */
	public FncConfStyles queryDetailFncConfStyles(String cusId,String statPrdStyle,String statPrd,String styleId)throws AgentException{
		
		FncConfStyles fncConfStyles = null;
		FncStatIsDao fDao = new FncStatIsDao();
		fncConfStyles = fDao.queryDetailFncConfStyles(cusId, statPrdStyle, statPrd, styleId,this.getConnection());
		return fncConfStyles;
	}
}
