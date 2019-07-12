package com.yucheng.cmis.biz01line.iqp.agent;

import java.util.Map;

import com.yucheng.cmis.biz01line.iqp.dao.OverseeUnderstoreDao;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.exception.AgentException;

public class OverseeUnderstoreAgent extends CMISAgent{
	/**
	 * 根据监管机构编号删除该监管机构下对应的下属仓库、前五大客户、主要管理人员、公司主要资产及其他核心资产
	 * @param model 表模型
	 * @param conditionFields 目录编号
	 * @return 成功记录数
	 */
	public int deleteIqpOverseeUnderstore(String model, Map<String,String> conditionFields)throws AgentException {
		try {
			OverseeUnderstoreDao OverseeUnderstoreDao = (OverseeUnderstoreDao)this.getDaoInstance("OverseeUnderstoreDao");
			this.getContext();
			return OverseeUnderstoreDao.deleteByField(model, conditionFields);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
}
