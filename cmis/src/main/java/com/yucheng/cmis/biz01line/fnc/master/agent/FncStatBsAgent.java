package com.yucheng.cmis.biz01line.fnc.master.agent;


import com.yucheng.cmis.biz01line.fnc.config.domain.FncConfStyles;
import com.yucheng.cmis.biz01line.fnc.master.dao.FncStatBaseDao;
import com.yucheng.cmis.biz01line.fnc.master.dao.FncStatBsDao;
import com.yucheng.cmis.biz01line.fnc.master.domain.FncStatBase;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.exception.AgentException;

public class FncStatBsAgent extends CMISAgent {

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
		FncStatBsDao fDao = new FncStatBsDao();
		fncConfStyles = fDao.queryDetailFncConfStyles(cusId, statPrdStyle, statPrd, styleId,this.getConnection());
		return fncConfStyles;
	}
	
	/**
	 * 根据客户号，指标编号，年号获取客户资产负债表中此项指标的年末值
	 * @param cusId
	 * @param statItemId
	 * @param statYear
	 * @return
	 * @throws AgentException
	 */
	public String getYearEndValue(String cusId,String statItemId,String statYear)throws AgentException{
		FncStatBsDao fDao = new FncStatBsDao();
		return fDao.getYearEndValue(cusId, statItemId, statYear, this.getConnection());
	}
}
