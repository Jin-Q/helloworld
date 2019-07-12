package com.yucheng.cmis.biz01line.cus.cusbase.component;

import java.sql.Connection;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusbase.agent.CusTrusteeAppAgent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusTrusteeApp;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusTrusteeLog;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CusTrusteeAppComponent extends CMISComponent {
	
	
	
	/**
	 * 更新托管申请表的状态位  用于申请提交操作调用
	 * @param serno  业务流水号
	 * @param statues  状态位的值
     * @return 
	 * 
	 */
	public String updateStatus(String serno,String statues) throws ComponentException{
	
		String strMessage=CMISMessage.DEFEAT;
		
		CusTrusteeAppAgent cusTrusteeAppAgent = (CusTrusteeAppAgent) this
			.getAgentInstance(PUBConstant.CUSTRUSTEEAPP);
		String retMessage=cusTrusteeAppAgent.updateStatus(serno, statues);
		if(retMessage.equals(CMISMessage.ADDSUCCEESS)){
			strMessage=CMISMessage.SUCCESS;
		}
		return strMessage;	
	}
	
	/**
	 * 添加申请日志
	 * @param cusTrusteeApp  托管申请对象
	 * 
     * @return 
	 * 
	 */
	public String addCusTrusteeLog(CusTrusteeApp cusTrusteeApp) throws AgentException{
		
		CusTrusteeAppAgent cusTrusteeAppAgent = (CusTrusteeAppAgent) this
		.getAgentInstance(PUBConstant.CUSTRUSTEEAPP);
	    //得到要添加的托管日志对象
		CusTrusteeLog cusTrusteeLog = this.getCusTrusteeLog(cusTrusteeApp);
	    //添加移交日志
		String str = cusTrusteeAppAgent.insertCusTrusteeLog(cusTrusteeLog);
		return str;
	
	}

	/**
	 * 构建托管日志对象
	 * @param cusTrusteeApp  托管申请对象
	 *
     * @return pcusHandoverLog  托管日志对象
	 * 
	 */
	private  CusTrusteeLog  getCusTrusteeLog(CusTrusteeApp cusTrusteeApp) {
	 
	 CusTrusteeLog pCusTrusteeLog = new CusTrusteeLog();

	    pCusTrusteeLog.setSerno(cusTrusteeApp.getSerno());//申请流水号
	    pCusTrusteeLog.setOrgType(cusTrusteeApp.getOrgType());//接收机构与移出机构关系
	    pCusTrusteeLog.setTrusteeScope(cusTrusteeApp.getTrusteeScope());//托管范围
	    pCusTrusteeLog.setAreaCode(cusTrusteeApp.getAreaCode());//区域编码
	    pCusTrusteeLog.setAreaName(cusTrusteeApp.getAreaName());//区域名称
	    pCusTrusteeLog.setConsignorId(cusTrusteeApp.getConsignorId());//委托人
	    pCusTrusteeLog.setConsignorBrId(cusTrusteeApp.getConsignorBrId());//委托机构
	    pCusTrusteeLog.setSuperviseBrId(cusTrusteeApp.getSuperviseBrId());//监交机构
	    pCusTrusteeLog.setSuperviseId(cusTrusteeApp.getSuperviseId());//监交人
	    pCusTrusteeLog.setTrusteeId(cusTrusteeApp.getTrusteeId());//托管人
	    pCusTrusteeLog.setTrusteeBrId(cusTrusteeApp.getTrusteeBrId());//托管机构
	    pCusTrusteeLog.setTrusteeDetail(cusTrusteeApp.getTrusteeDetail());//托管说明
	    pCusTrusteeLog.setTrusteeDate(cusTrusteeApp.getTrusteeDate());//托管日期
	    pCusTrusteeLog.setRetractDate(cusTrusteeApp.getRetractDate());//收回日期

	return pCusTrusteeLog;	
	}
	
	public String updateLogRetractDate(String serno, String date) throws EMPException {
		CusTrusteeAppAgent cusTrusteeAppAgent = (CusTrusteeAppAgent) this
				.getAgentInstance(PUBConstant.CUSTRUSTEEAPP);
		// 添加移交日志
		String str = cusTrusteeAppAgent.updateLogRetractDate(serno, date);
		return str;
	}
	
	public boolean hasCusTrusteeList(String serno) throws EMPException {
		CusTrusteeAppAgent cusTrusteeAppAgent = (CusTrusteeAppAgent) this
				.getAgentInstance(PUBConstant.CUSTRUSTEEAPP);
		return cusTrusteeAppAgent.hasCusTrusteeList(serno);
	}

	public boolean checkCusHandoverApp(String managerId,
			String consignorId,Connection con) throws AgentException {
		// TODO Auto-generated method stub
		CusTrusteeAppAgent cusTrusteeAppAgent = (CusTrusteeAppAgent) this
		.getAgentInstance(PUBConstant.CUSTRUSTEEAPP);
		return cusTrusteeAppAgent.checkCusHandoverApp( managerId,
				 consignorId, con);
		
	}
	/**
	 * 处理托管申请流程
	 * @param serno
	 * @return
	 * @throws EMPException
	 */
	public void cusTrusteeApp(String serno) throws EMPException {
		CusTrusteeAppAgent cusTrusteeAppAgent = (CusTrusteeAppAgent) this
				.getAgentInstance(PUBConstant.CUSTRUSTEEAPP);
		cusTrusteeAppAgent.cusTrusteeApp(serno);
	}
	public void callback(String consignor_id,String trusteeId) throws AgentException{
		CusTrusteeAppAgent cusTrusteeAppAgent = (CusTrusteeAppAgent) this
		.getAgentInstance(PUBConstant.CUSTRUSTEEAPP);
		cusTrusteeAppAgent.callback(consignor_id,trusteeId);
		
	}
	
	public boolean getExistCusTrustee(String consignorId,String trusteeId)throws EMPException{
		CusTrusteeAppAgent cusTrusteeAppAgent = (CusTrusteeAppAgent) this.getAgentInstance(PUBConstant.CUSTRUSTEEAPP);
		return cusTrusteeAppAgent.getExistCusTrustee(consignorId,trusteeId);
	}
	
	public CusTrusteeApp getCusTrusteeAppByKey(String serno)
	throws EMPException {
		CusTrusteeAppAgent cusTrusteeAppAgent = (CusTrusteeAppAgent) this.getAgentInstance(PUBConstant.CUSTRUSTEEAPP);
		return cusTrusteeAppAgent.getCusTrusteeAppByKey(serno);
	}

	public boolean hasCusTrustee(String consignorId,String trustee_id)throws EMPException{
		CusTrusteeAppAgent cusTrusteeAppAgent = (CusTrusteeAppAgent) this.getAgentInstance(PUBConstant.CUSTRUSTEEAPP);
		return cusTrusteeAppAgent.hasCusTrustee(consignorId,trustee_id,this.getConnection());
	}
}
