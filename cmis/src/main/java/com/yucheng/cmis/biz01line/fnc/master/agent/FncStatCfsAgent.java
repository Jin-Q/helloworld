package com.yucheng.cmis.biz01line.fnc.master.agent;


import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.dao.FncStatBaseDao;
import com.yucheng.cmis.biz01line.fnc.master.dao.FncStatCfsDao;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.exception.AgentException;
  /**
 *@Classname	FncStatCfsAgent.java
 *@Version 1.0	
 *@Since   1.0 	Oct 13, 2008 5:32:13 PM  
 *@Copyright 	yuchengtech
 *@Author 		an
 *@Description：
 *@Lastmodified 
 *@Author
 */
public class FncStatCfsAgent extends CMISAgent{
/**
 * 
 * @param cusId
 * @param statPrdStyle
 * @param statPrd
 * @return
 * @throws AgentException
 */
	public FncStatBase queryDetailFncStatBase(String cusId, String statPrdStyle, String statPrd) throws AgentException {
		FncStatBase fncStatBase = null;
		FncStatBaseDao bDao = new FncStatBaseDao();
		fncStatBase = bDao.queryDetailFncStatBase(cusId, statPrdStyle, statPrd,this.getConnection());
		return fncStatBase;
	}
/**
 * 
 * @param cusId
 * @param statPrdStyle
 * @param statPrd
 * @param styleId
 * @return
 * @throws AgentException
 */
	public FncConfStyles queryDetailFncConfStyles(String cusId, String statPrdStyle, String statPrd, String styleId) throws AgentException {
		FncConfStyles fncConfStyles = null;
		FncStatCfsDao fDao = new FncStatCfsDao();
		fncConfStyles = fDao.queryDetailFncConfStyles(cusId, statPrdStyle, statPrd, styleId,this.getConnection());
		return fncConfStyles;
	}

}
