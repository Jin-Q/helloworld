/**
 * 
 */
package com.yucheng.cmis.platform.riskmanage.riskinterface.imple;

import java.util.HashMap;
import java.util.List;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.platform.riskmanage.component.RiskManageComponent;
import com.yucheng.cmis.platform.riskmanage.riskinterface.PrvRiskItemInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

/**
 * 风险拦截项目接口实例
 * 
 * @author zhangpu
 * 
 */
public class PrvRiskItemInstance extends CMISComponent implements
		PrvRiskItemInterface {

	/**
	 * 日志信息
	 */
	//private static final Logger log = Logger.getLogger(WfiWorkflow2OrgInstance.class);

	/**
	 * 组件ID
	 */
	private String componentID = "RiskManage";
	
	/**
	 * 表模型ID
	 */
	private String modelID = PUBConstant.PRDPVRISKITEM;

	/**
	 * 根据风险拦项目截编号得到风险拦截项目对象
	 * 
	 * @param itemId
	 *            风险拦截项目编号
	 * @return 风险拦截项目对象
	 * @throws ComponentException
	 */
	public CMISDomain getPrvRiskItem(String itemId) throws ComponentException {
		if(itemId == null || itemId.trim().length() == 0){
			throw new ComponentException("风险拦截项目编号为空，查询失败，请重试！");
		}
		//log.debug("风险拦截项目编号：" + itemId);
		RiskManageComponent riskManageComponent = (RiskManageComponent) this
				.getComponent(componentID);
		String conditionStr = " where item_id = '" + itemId + "'";
		//log.debug("查询条件语句： " + conditionStr);
		CMISDomain domain = null;
		try {
			domain = riskManageComponent.queryDetail(modelID, conditionStr, componentID);
		} catch (EMPException e) {
			throw new ComponentException(e);
		}
		return domain;
	}

	/* (non-Javadoc)
	 * @see com.yucheng.cmis.prdinterface.PrvRiskItemInterface#doInspect(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.HashMap)
	 */
	public void doInspect(String modelId, String serno, String[] preventIdLst, String wfid,
			String nodeId) throws ComponentException {
		// TODO Auto-generated method stub
		
		RiskManageComponent riskManageComponent = (RiskManageComponent) this.
		getOtherComponentInstanceWithOldCon(componentID);
		//.getComponent(componentID);
		
		riskManageComponent.doInspect(modelId, serno, preventIdLst, wfid, nodeId);
		
	}

	/* (non-Javadoc)
	 * @see com.yucheng.cmis.prdinterface.PrvRiskItemInterface#getAllRiskItems(java.lang.String, java.lang.String, java.lang.String)
	 */
	public List getAllRiskItems(String serno, String wfid, String nodeId)
			throws ComponentException {
		// TODO Auto-generated method stub
		
		RiskManageComponent riskManageComponent = (RiskManageComponent) this
		.getComponent(componentID);
		
		return riskManageComponent.getAllRiskItems(serno, wfid, nodeId);
	}

	/* (non-Javadoc)
	 * @see com.yucheng.cmis.prdinterface.PrvRiskItemInterface#getIfAccessOfListItem(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean getIfAccessOfListItem(String serno, String wfid,
			String nodeId) throws ComponentException {
		// TODO Auto-generated method stub
		RiskManageComponent riskManageComponent = (RiskManageComponent) this
		.getComponent(componentID);
		
		return riskManageComponent.getIfAccessOfListItem(serno, wfid, nodeId);
	}
	
	public boolean getIfAccessOfListItem(List resultItem) throws ComponentException {
		// TODO Auto-generated method stub
		RiskManageComponent riskManageComponent = (RiskManageComponent) this
		.getComponent(componentID);
		
		return riskManageComponent.getIfAccessOfListItem(resultItem);
	}	

	/* (non-Javadoc)
	 * @see com.yucheng.cmis.prdinterface.PrvRiskItemInterface#cleanRiskItems(java.lang.String)
	 */
	public void cleanRiskItems(String serno) throws ComponentException {
		// TODO Auto-generated method stub
		RiskManageComponent riskManageComponent = (RiskManageComponent) this
		.getComponent(componentID);
		
		riskManageComponent.cleanRiskItems(serno);
		
	}

	public List getPreRiskItems(String serno) throws ComponentException {
		RiskManageComponent riskManageComponent = (RiskManageComponent) this
		.getComponent(componentID);
		
		return riskManageComponent.getPreRiskItems(serno);
	}

	/**
	 * <p>判断流程当前场景下是否需要进行风险拦截检查</p>
	 * @param preventIdLst 拦截方案清单
	 * @param wfid 流程id
	 * @param nodeId 流程节点
	 * @return true 需要进行风险拦截检查 false 不需要
	 * @throws ComponentException
	 */
	public boolean isNeedRiskInspected(String[] preventIdLst,String wfid, String nodeId)
			throws ComponentException {
		RiskManageComponent riskManageComponent = (RiskManageComponent) this.getOtherComponentInstanceWithOldCon(componentID);
		return riskManageComponent.isNeedRiskInspected(preventIdLst, wfid, nodeId);
	}

}
