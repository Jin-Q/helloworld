package com.yucheng.cmis.biz01line.iqp.drfpo.agent;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.iqp.drfpo.dao.DpoDrfpoDao;
import com.yucheng.cmis.biz01line.iqp.drfpo.dpopub.DpoConstant;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.exception.AgentException;
import com.yucheng.cmis.pub.exception.ComponentException;

public class DpoDrfpoAgent extends CMISAgent{
	/**
	 * 通过汇票号码获取汇票信息（票据池时使用）
	 * @param porderno 汇票号码
	 * @return
	 * @throws AgentException
	 */
	public KeyedCollection getPorderMsgByPorderNoDrfpo(String porderno,String drfpo_no) throws AgentException {
		try {
			DpoDrfpoDao cmisDao = (DpoDrfpoDao)this.getDaoInstance(DpoConstant.DPODRFPODAO);
			return cmisDao.getPorderMsgByPorderNoDrfpo(porderno,drfpo_no);
		} catch (Exception e) {
			throw new AgentException("通过汇票号码获取汇票信息失败，失败原因："+e.getMessage());
		}
	}
	
	/**
	 * 通过票据池编号删除票据池信息以及池内票据与其关联的关系表记录
	 * @param drfpoNo 票据池编号
	 * @return
	 * @throws AgentException
	 */
	public int deleteDrfpoByDrfpoNo(String guarId) throws AgentException {
		int count=0;
		try {
			DpoDrfpoDao cmisDao = (DpoDrfpoDao)this.getDaoInstance(DpoConstant.DPODRFPODAO);
			count = cmisDao.deleteDrfpoByDrfpoNo(guarId);
		} catch (Exception e) {
			throw new AgentException("通过票据池编号删除票据池信息以及池内票据与其关联的关系表记录失败，失败原因："+e.getMessage());
		}
		return count;
		
	}
	/**
	 * 通过汇票号码获取汇票信息（票据池时使用）
	 * @param drfpoNo 汇票号码，status 票据状态
	 * @return
	 * @throws AgentException
	 */
	public IndexedCollection getPorderListByDrfpoNo(String drfpoNo,String status) throws AgentException {
		try {
		DpoDrfpoDao cmisDao = (DpoDrfpoDao)this.getDaoInstance(DpoConstant.DPODRFPODAO);
		return cmisDao.getPorderListByDrfpoNo(drfpoNo,status);
		} catch (Exception e) {
			throw new AgentException("通过汇票号码获取汇票信息失败，失败原因："+e.getMessage());
		}
	}
	
	/**
	 * 根据池编号获得所属池下的票据信息
	 * @param drfpoNo 汇票号码
	 * @return
	 * @throws AgentException
	 */
	public IndexedCollection getBillInfoByDrfpoNo(String drfpoNo) throws AgentException {
		try {
		DpoDrfpoDao cmisDao = (DpoDrfpoDao)this.getDaoInstance(DpoConstant.DPODRFPODAO);
		return cmisDao.getBillInfoByDrfpoNo(drfpoNo);
		} catch (Exception e) {
			throw new AgentException("根据池编号获得所属池下的票据信息失败，失败原因："+e.getMessage());
		}
	}
	/**
	 * 通过汇票号码获取汇票信息（票据池时使用）
	 * @param porderno 汇票号码
	 * @return
	 * @throws AgentException
	 */
	public Double getDrftAmtByDrfpoNo(String drfpoNo,String status) throws AgentException {
		try {
		DpoDrfpoDao cmisDao = (DpoDrfpoDao)this.getDaoInstance(DpoConstant.DPODRFPODAO);
		return cmisDao.getDrftAmtByDrfpoNo(drfpoNo,status);
		} catch (Exception e) {
			throw new AgentException("通过汇票号码获取汇票信息失败，失败原因："+e.getMessage());
		}
	}
	
}
