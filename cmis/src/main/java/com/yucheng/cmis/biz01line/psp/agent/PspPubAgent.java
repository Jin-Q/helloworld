package com.yucheng.cmis.biz01line.psp.agent;

import java.util.Map;

import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.psp.dao.PspPubDao;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.DaoException;

public class PspPubAgent extends CMISAgent {
	/**
	 * 贷后管理专用带返回值的命名sql调用
	 * @param submitType 处理类型
	 * @param kcoll  传值kcoll
	 */
	public void delExecuteSql(String submitType, KeyedCollection kcoll) throws AgentException {
		try {
			PspPubDao cmisDao = (PspPubDao)this.getDaoInstance("PspPubDao");
			cmisDao.delExecuteSql(submitType, kcoll);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	
	/**
	 * 贷后管理专用带返回值的命名sql调用
	 * @param submitType 处理类型
	 * @param kcoll  传值kcoll
	 * @return result_kcoll 返回kcoll
	 */
	public KeyedCollection delReturnSql(String submitType, KeyedCollection kcoll) throws AgentException {
		try {
			PspPubDao cmisDao = (PspPubDao)this.getDaoInstance("PspPubDao");
			return cmisDao.delReturnSql(submitType, kcoll);
		} catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
	}
	/**
	 * 根据表模型ID 及条件字段删除数据
	 * @param model 表模型
	 * @param conditionFields  过滤条件键值对
	 * @return 执行删除记录条数
	 * @throws DaoException
	 */
	public int deleteByField(String model, Map<String,String> conditionFields) throws AgentException {
		int count = 0;
		try {
			PspPubDao lmtPubDao = (PspPubDao)this.getDaoInstance("PspPubDao");
			count = lmtPubDao.deleteByField(model, conditionFields);
		}catch (Exception e) {
			throw new AgentException(e.getMessage());
		}
		return count;
	}
	
}