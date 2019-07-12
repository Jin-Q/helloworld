package com.yucheng.cmis.biz01line.iqp.drfpo.component;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.iqp.drfpo.agent.DpoDrfpoAgent;
import com.yucheng.cmis.biz01line.iqp.drfpo.dpopub.DpoConstant;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.ComponentException;

public class DpoDrfpoComponent extends CMISComponent{
	
	/**
	 * 通过汇票号码获取汇票信息（票据池时使用）
	 * @param porderno 汇票号码
	 * @return
	 * @throws Exception
	 */
	public KeyedCollection getPorderMsgByPorderNoDrfpo(String porderno,String drfpo_no) throws ComponentException {
		KeyedCollection kc = null;
		try {	
		DpoDrfpoAgent cmisAgent = (DpoDrfpoAgent)this.getAgentInstance(DpoConstant.DPODRFPOAGENT);
		kc = cmisAgent.getPorderMsgByPorderNoDrfpo(porderno,drfpo_no);
		} catch (Exception e) {
			throw new ComponentException("通过汇票号码获取汇票信息失败，失败原因："+e.getMessage());
			}
		return kc;
	}
	/**
	 * 通过票据池编号删除票据池信息以及池内票据与其关联的关系表记录
	 * @param drfpoNo 票据池编号
	 * @return
	 * @throws Exception
	 */
	public int deleteDrfpoByDrfpoNo(String drfpoNo) throws ComponentException {
		int count=0;
		try {	
			DpoDrfpoAgent cmisAgent = (DpoDrfpoAgent)this.getAgentInstance(DpoConstant.DPODRFPOAGENT);
			count = cmisAgent.deleteDrfpoByDrfpoNo(drfpoNo);
		} catch (Exception e) {
		throw new ComponentException("通过票据池编号删除票据池信息以及池内票据与其关联的关系表记录失败，失败原因："+e.getMessage());
		}
			return count;
    }
	/**
	 * 通过池编号来查询相关的汇票信息
	 * @param drfpoNo 票据池编号 Stutas 票据在池状态
	 * @return
	 * @throws Exception
	 * */
	public IndexedCollection getPorderListByDrfpoNo(String drfpoNo,String status) throws ComponentException {
		IndexedCollection ic = null;
		try {	
		DpoDrfpoAgent cmisAgent = (DpoDrfpoAgent)this.getAgentInstance(DpoConstant.DPODRFPOAGENT);
		ic = cmisAgent.getPorderListByDrfpoNo(drfpoNo,status);
//		if(ic.size()!=0){
//			for(int i = 0;i<ic.size();i++){
//				kc = (KeyedCollection) ic.get(i);
//				kc.addDataField("status",status);
//				kc.addDataField("drfpo_no",drfpoNo);
//			}
//		}
		} catch (Exception e) {
			throw new ComponentException("通过池编号来查询相关的汇票信息失败，失败原因："+e.getMessage());
		}
		return ic;
	}
	/**
	 * 根据池编号获得所属池下的票据信息
	 * @param drfpoNo 汇票号码
	 * @return
	 * @throws Exception
	 */
	public IndexedCollection getBillInfoByDrfpoNo(String drfpoNo) throws ComponentException {
		IndexedCollection ic = null;
		KeyedCollection kc = null;
		try {	
		DpoDrfpoAgent cmisAgent = (DpoDrfpoAgent)this.getAgentInstance(DpoConstant.DPODRFPOAGENT);
		ic = cmisAgent.getBillInfoByDrfpoNo(drfpoNo);
		if(ic.size()!=0){
			for(int i = 0;i<ic.size();i++){
				kc = (KeyedCollection) ic.get(i);
				kc.addDataField("drfpo_no",drfpoNo);
			}
		}
		} catch (Exception e) {
			throw new ComponentException("根据池编号获得所属池下的票据信息失败，失败原因："+e.getMessage());
		}
		return ic;
	}
	/**
	 * 通过池编号和票据的池状态来查询汇票票面金额
	 * @param drfpoNo 票据池编号 Stutas 票据在池状态 （00-待入池，01-在池，02-托收，03-出池）
	 * @return
	 * @throws Exception
	 * */
	public Double getDrftAmtByDrfpoNo(String drfpoNo,String status) throws ComponentException {
		double count = 0.0;
		try {	
		DpoDrfpoAgent cmisAgent = (DpoDrfpoAgent)this.getAgentInstance(DpoConstant.DPODRFPOAGENT);
		count = cmisAgent.getDrftAmtByDrfpoNo(drfpoNo,status);
		} catch (Exception e) {
			throw new ComponentException("通过池编号和票据的池状态来查询汇票票面金额失败，失败原因："+e.getMessage());
		}
		return count;
	}
	
}
