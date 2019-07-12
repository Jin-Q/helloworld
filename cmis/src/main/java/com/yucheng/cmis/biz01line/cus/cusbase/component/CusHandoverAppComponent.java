package com.yucheng.cmis.biz01line.cus.cusbase.component;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusbase.agent.CusHandoverAppAgent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusHandoverApp;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusHandoverLog;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CusHandoverAppComponent extends CMISComponent {

	/**
	 * 添加申请日志
	 * @param cusHandoverApp  移交申请对象
	 * 
     * @return 
	 * 
	 */
	public String doReceive(CusHandoverApp cusHandoverApp) throws AgentException{
		
	   CusHandoverAppAgent cusHandoverAppAgent = (CusHandoverAppAgent) this
		   .getAgentInstance(PUBConstant.CUSHANDOVERAPP);
	    //得到要添加的移交日志对象
		CusHandoverLog cusHandoverLog = this.getCusHandoverLog(cusHandoverApp);
	    //添加移交日志
		String str = cusHandoverAppAgent.insertCusHandoverLog(cusHandoverLog);
		//pcusLoanRel=cusHandoverAppAgent.findCusLoanRel(cusId, brId);
		return str;
	
	}
	
	/**
	 * 根据客户号查找该接受人是否存在当前客户经理申请中
	 * @param cusId
	 * @return
	 * @throws Exception 
	 * @throws AgentException
	 */
	public CusHandoverApp checkCusHandoverApp(String cusId,String handoverId)  throws Exception{
		
		CusHandoverApp  cusHa = new CusHandoverApp();
		CusHandoverAppAgent cusHandoverAppAgent = (CusHandoverAppAgent) this.getAgentInstance(PUBConstant.CUSHANDOVERAPP);
		cusHa = cusHandoverAppAgent.checkCusHandoverApp(cusId,handoverId);
		return cusHa;
	}
	
	/**
	 * 更新申请表的状态位
	 * @param serno  业务流水号
	 * @param statues  状态位的值
     * @return 
	 * @throws Exception 
	 * 
	 */
	public String updateStatus(String serno,String statues) throws Exception{
	
		String strMessage=CMISMessage.DEFEAT;
		
		CusHandoverAppAgent cusHandoverAppAgent = (CusHandoverAppAgent) this
			.getAgentInstance(PUBConstant.CUSHANDOVERAPP);
		String retMessage=cusHandoverAppAgent.updateStatus(serno, statues);
		if(retMessage.equals(CMISMessage.ADDSUCCEESS)){
			strMessage=CMISMessage.SUCCESS;
		}
		return strMessage;	
	}
	
	
	/**
	 * 构建移交日志对象
	 * @param cusHandoverApp  移交申请对象
	 *
     * @return pcusHandoverLog  移交日志对象
	 * 
	 */
	private  CusHandoverLog  getCusHandoverLog(CusHandoverApp cusHandoverApp) {
	 
	 	CusHandoverLog pcusHandoverLog = new CusHandoverLog();

		pcusHandoverLog.setSerno(cusHandoverApp.getSerno());//申请流水号
		pcusHandoverLog.setOrgType(cusHandoverApp.getOrgType());//接收机构与移出机构关系
		pcusHandoverLog.setHandoverScope(cusHandoverApp.getHandoverScope());//移交范围
		pcusHandoverLog.setHandoverMode(cusHandoverApp.getHandoverMode());//移交方式
		pcusHandoverLog.setAreaCode(cusHandoverApp.getAreaCode());//区域编码
		pcusHandoverLog.setAreaName(cusHandoverApp.getAreaName());//区域名称
		pcusHandoverLog.setHandoverBrId(cusHandoverApp.getHandoverBrId());//移出机构
		pcusHandoverLog.setHandoverId(cusHandoverApp.getHandoverId());//移出人
		pcusHandoverLog.setSuperviseBrId(cusHandoverApp.getSuperviseBrId());//监交机构RECEIVER_ID
		pcusHandoverLog.setSuperviseId(cusHandoverApp.getSuperviseId());//监交人
		pcusHandoverLog.setReceiverBrId(cusHandoverApp.getReceiverBrId());//接收机构
		pcusHandoverLog.setReceiverId(cusHandoverApp.getReceiverId());//接收人
		pcusHandoverLog.setHandoverDetail(cusHandoverApp.getHandoverDetail());//移交说明
		pcusHandoverLog.setHandoverDate(cusHandoverApp.getInputDate());//移交日期

	return pcusHandoverLog;	
	}
	
	public boolean hasCusTrusteeList(String serno) throws Exception{
		CusHandoverAppAgent cusHandoverAppAgent = (CusHandoverAppAgent) this
			.getAgentInstance(PUBConstant.CUSHANDOVERAPP);
		return cusHandoverAppAgent.hasCusTrusteeList(serno);
	}
	/**
	 * 处理客户移交申请流程
	 * @param serno 移交申请流水号
	 * @throws ComponentException
	 */
//	public void cusHandoverApp(String serno) throws ComponentException{
//		CusHandoverAppAgent cusHandoverAppAgent = (CusHandoverAppAgent) this
//			.getAgentInstance(PUBConstant.CUSHANDOVERAPP);
//		cusHandoverAppAgent.cusHandoverApp(serno);
//	}
	
	public CusHandoverApp getCusHandoverAppByKey(String serno) throws EMPException{
		CusHandoverAppAgent cusHandoverAppAgent = (CusHandoverAppAgent) this
		.getAgentInstance(PUBConstant.CUSHANDOVERAPP);
		return cusHandoverAppAgent.getCusHandoverAppByKey(serno);
	}
}
