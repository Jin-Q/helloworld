package com.yucheng.cmis.platform.riskmanage.riskinterface;

import java.util.List;

import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 风险拦截项目接口
 * @author zhangpu
 *
 */
public interface PrvRiskItemInterface {
	
	/**
	 * 根据风险拦项目截编号得到风险拦截项目对象
	 * @param itemId 风险拦截项目编号
	 * @return 风险拦截项目对象
	 * @throws ComponentException
	 */
	CMISDomain getPrvRiskItem(String itemId) throws ComponentException;
	
	 /**
	  * 执行风险拦截
	  * @param modelId 业务实体表模型
	 * @param serno 业务流水
	 * @param preventIdLst 拦截方案清单
	 * @param wfid 流程id
	 * @param nodeId 流程节点
	 * @throws ComponentException
	 */
	public  void doInspect(String modelId, String serno,String[] preventIdLst,String wfid,String nodeId) throws ComponentException;
	
	/**
	 * 返回某流程风险拦截结果条目
	 * @param serno 业务流水号
	 * @param wfid 流程id
	 * @param nodeId 流程节点
	 * @return 拦截条目domain list
	 * @throws ComponentException
	 */
	public List getAllRiskItems(String serno,String wfid,String nodeId) throws ComponentException;
	
	/**
	 * 返回风险拦截是否通过
	 * @param serno 业务流水号
	 * @param wfid 流程id
	 * @param nodeId 流程节点
	 * @return 是否通过
	 * @throws ComponentException
	 */
	public boolean getIfAccessOfListItem(String serno,String wfid,String nodeId) throws ComponentException;
	
	/**
	 * 返回风险拦截是否通过
	 * @param resultItem 检查结果明细
	 * @return 是否通过
	 * @throws ComponentException
	 */
	public boolean getIfAccessOfListItem(List resultItem) throws ComponentException;

	
	/**
	 * 流程结束后清除风险拦截条目
	 * @param serno 业务流水号
	 * @throws ComponentException
	 */
	public void cleanRiskItems(String serno) throws ComponentException;
	
	/**
	 * 各个场景获取之前风险拦截条目
	 * @param serno 业务流水号
	 * @return 拦截条目
	 * @throws ComponentException
	 */
	public List getPreRiskItems(String serno) throws ComponentException;
	
	/**
	 * <p>判断流程当前场景下是否需要进行风险拦截检查</p>
	 * @param preventIdLst 拦截方案清单
	 * @param wfid 流程id
	 * @param nodeId 流程节点
	 * @return true 需要进行风险拦截检查 false 不需要
	 * @throws ComponentException
	 */
	public boolean isNeedRiskInspected(String[] preventIdLst, String wfid,String nodeId) throws ComponentException;
}
