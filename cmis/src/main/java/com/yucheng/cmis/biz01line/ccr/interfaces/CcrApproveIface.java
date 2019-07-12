package com.yucheng.cmis.biz01line.ccr.interfaces;

import java.util.Map;

import com.yucheng.cmis.pub.exception.ComponentException;

public interface CcrApproveIface {
/**
 * <h1>审批通过接口(单笔)</h1>
 * <p>修改申请的流程状态,修改客户评级,并将"申请信息"过度到历史表</p>
 * @param serno
 * @param cusId
 * @return
 * @throws ComponentException
 */
	public String approvalApp(String serno,String cusId,Map param) throws ComponentException;
/**
 * <h1>审批通过接口(批量)</h1>
 * <p>修改申请的流程状态,修改客户评级,并将"申请信息"过度到历史表</p>
 * @param serno
 * @param cusId
 * @return
 * @throws ComponentException
 */
	public String approvalBatchApp(String serno,Map param) throws ComponentException;
	
	public String CcrRatDirectApproval(String serno) throws ComponentException;

}
