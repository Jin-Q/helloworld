package com.yucheng.cmis.biz01line.cus.interfaces;



import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusTrusteeApp;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.pub.exception.ComponentException;

public interface CusTrusteeAppIface {
	/**
	 * <p>审批同意时执行业务处理逻辑</p>
	 * @param wfiMsg 流程消息
	 */
	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException;
	
	/**
	 * <p>审批否决时执行业务处理逻辑</p>
	 * @param wfiMsg 流程消息
	 */
	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException;
	
	/**
	 * <p>流程审批中执行打回处理逻辑</p>
	 * @param wfiMsg 流程消息
	 */
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException;
	
	/**
	 * <p>流程审批中执行拿回处理逻辑</p>
	 * @param wfiMsg 流程消息
	 */
	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException;
	
	/**
	 * <p>流程审批中执行业务处理逻辑</p>
	 * @param wfiMsg 流程消息
	 */
	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException;
	
	/**
	 * <p>设置业务数据至流程变量之中</p>
	 * @param tableNm 表模型ID
	 * @param pkVal 主键值
	 * @param modifyVar 审批变更修改的业务要素
	 * @return 返回key=value方式的流程变量对象
	 * @throws EMPException
	 */
	public Map<String,String> putBizData2WfVar(String tabModelId, String pkVal, KeyedCollection modifyVar) throws EMPException;
	
}
