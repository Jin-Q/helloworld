package com.yucheng.cmis.biz01line.pvp.agent;

import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.pvp.dao.PvpDao;
import com.yucheng.cmis.biz01line.pvp.pvptools.PvpConstant;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.exception.AgentException;

public class PvpAgent extends CMISAgent {
	/**
	 * 通过合同编号生成借据编号（泉州银行借据规则：合同编号+3位序列号）
	 * @param contNo 合同编号
	 * @return KeyedCollection 
	 * @throws Exception
	 */
	public KeyedCollection getBillNoByContNo(String contNo) throws Exception {
		PvpDao cmisDao = (PvpDao)this.getDaoInstance(PvpConstant.PVPDAO);
		return cmisDao.getBillNoByContNo(contNo);
	}
	/**
	 * 根据合同号插入一条新的序列号记录
	 * @param contNo 合同编号
	 * @throws Exception
	 */
	public void insertPvpAutocode(String contNo) throws Exception {
		PvpDao cmisDao = (PvpDao)this.getDaoInstance(PvpConstant.PVPDAO);
		cmisDao.insertPvpAutocode(contNo);
	}
	/**
	 * 根据合同号更新序列号记录
	 * @param contNo 合同编号
	 * @param sernum 序列号
	 * @throws Exception
	 */
	public void updatePvpAutocode(String contNo, String sernum) throws Exception {
		PvpDao cmisDao = (PvpDao)this.getDaoInstance(PvpConstant.PVPDAO);
		cmisDao.updatePvpAutocode(contNo,sernum);
	}
	
	public String getSernoByContNo(String contNo) throws Exception{
		PvpDao cmisDao = (PvpDao)this.getDaoInstance(PvpConstant.PVPDAO);
		return cmisDao.getSernoByContNo(contNo);
	}
	/**
	 * @author lisj
	 * @description 需求:【XD140818051】清空iqp_group_pvp表中所有数据方法
	 * @time 2014年12月10日
	 * @verion v1.0
	 */
	public String deleteAll4IqpGroupPvp() throws AgentException{
		String sql = "delete from iqp_group_pvp";
		String result =this.executeSql(sql);	
		return result;
	}
}
