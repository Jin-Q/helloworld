package com.yucheng.cmis.biz01line.iqp.component;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.iqp.agent.IqpBatchAgent;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.pub.CMISComponent;

public class IqpBatchComponent extends CMISComponent {
	
	/**
	 * 查询批次下票据期限不能包含制定日期的票据
	 * @param batch_no 批次号
	 * @param date 日期
	 * @return returnKColl 票据集合
	 * @throws Exception
	 */
	public IndexedCollection getBillsByDate(String batch_no,String date) throws Exception {
		IndexedCollection returnKColl = null;
		IqpBatchAgent batchAgent = (IqpBatchAgent)this.getAgentInstance(AppConstant.IQPBATCHAGENT);
		returnKColl = batchAgent.getBillsByDate(batch_no,date);
		
		return returnKColl;
	}
	
	/**
	 * 根据流水号获取,该业务下票据批次的实付总金额和贴现总利息
	 * @param serno 业务流水号
	 * @return returnKColl 票据批次综合信息
	 * @throws Exception
	 */
	public IndexedCollection getBatchTotalInfoBySerno(String serno) throws Exception {
		IndexedCollection returnKColl = null;
		IqpBatchAgent batchAgent = (IqpBatchAgent)this.getAgentInstance(AppConstant.IQPBATCHAGENT);
		returnKColl = batchAgent.getBatchTotalInfoBySerno(serno);
		
		return returnKColl;
	}
	/**
	 * 根据流水号获取,该业务下票据批次明细
	 * @param serno 业务流水号
	 * @return returnKColl 票据批次综合信息
	 * @throws Exception
	 */
	public KeyedCollection getBatchMngBySerno(String serno) throws Exception {
		KeyedCollection returnKColl = null;
		IqpBatchAgent batchAgent = (IqpBatchAgent)this.getAgentInstance(AppConstant.IQPBATCHAGENT);
		returnKColl = batchAgent.getBatchMngBySerno(serno);
		
		return returnKColl;
	}
}
