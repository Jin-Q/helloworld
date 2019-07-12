package com.yucheng.cmis.biz01line.iqp.component;

import java.util.Map;
import com.yucheng.cmis.biz01line.iqp.agent.OverseeUnderstoreAgent;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.ComponentException;

public class OverseeUnderstoreComponent extends CMISComponent{
	/**
	 * 根据监管机构编号删除该监管机构下对应的下属仓库、前五大客户、主要管理人员、公司主要资产及其他核心资产
	 * @param model 表模型
	 * @param conditionFields 目录编号
	 * @return 成功记录数
	 */
	public int deleteIqpOverseeUnderstore(String model, Map<String,String> conditionFields)throws ComponentException {
		try {
			OverseeUnderstoreAgent OverseeUnderstoreAgent = (OverseeUnderstoreAgent)this.getAgentInstance("OverseeUnderstoreAgent");
			return OverseeUnderstoreAgent.deleteIqpOverseeUnderstore(model, conditionFields);
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
	}
}
