package com.yucheng.cmis.biz01line.psp.component;

import java.util.Map;

import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.psp.agent.PspPubAgent;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

public class PspPubComponent extends CMISComponent {
	/**
	 * 贷后管理专用带返回值的命名sql调用
	 * @param submitType 处理类型
	 * @param kcoll  传值kcoll
	 */
	public void delExecuteSql(String submitType, KeyedCollection kcoll) throws AgentException {
		try {
			PspPubAgent cmisAgent = (PspPubAgent)this.getAgentInstance("PspPubAgent");
			cmisAgent.delExecuteSql(submitType, kcoll);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 *  贷后管理专用带返回值的命名sql调用
	 * @param submitType 处理类型
	 * @param kcoll  传值kcoll
	 * @return result_kcoll 返回kcoll
	 */
	public KeyedCollection delReturnSql(String submitType, KeyedCollection kcoll) throws AgentException {
		try {
			PspPubAgent cmisAgent = (PspPubAgent)this.getAgentInstance("PspPubAgent");
			return cmisAgent.delReturnSql(submitType, kcoll);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	/**
	 * 根据表模型ID 及条件字段删除数据
	 * @param model 表模型
	 * @param conditionFields  过滤条件键值对
	 * @return 执行删除记录条数
	 * @throws AgentException
	 */
	public int deleteByField(String model, Map<String,String> conditionFields ) throws ComponentException {
		int count = 0;
		try {
			PspPubAgent lmtPubAgent = (PspPubAgent)this.getAgentInstance("PspPubAgent");
			count = lmtPubAgent.deleteByField(model, conditionFields);
		}catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
		return count;
	}
}