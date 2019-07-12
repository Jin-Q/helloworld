package com.yucheng.cmis.biz01line.pvp.component;

import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.pvp.agent.PvpAgent;
import com.yucheng.cmis.biz01line.pvp.pvptools.PvpConstant;
import com.yucheng.cmis.biz01line.pvp.pvptools.PvpUtils;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.ComponentException;

public class PvpComponent extends CMISComponent {
	/**
	 * 通过合同编号生成借据编号（泉州银行借据规则：合同编号+3位序列号）
	 * @param contNo 合同编号
	 * @return String 借据编号
	 * @throws Exception
	 */
	public String getBillNoByContNo(String contNo) throws Exception {
		String billNo = "";
		KeyedCollection kc = null;
		PvpAgent cmisAgent = (PvpAgent)this.getAgentInstance(PvpConstant.PVPAGENT);
		kc = cmisAgent.getBillNoByContNo(contNo);
		if(kc != null && kc.size() > 0){
			String sernum = "";
			sernum = (String)kc.getDataValue("sernum");
			int newNum = Integer.parseInt(sernum)+1;
			cmisAgent.updatePvpAutocode(contNo, String.valueOf(newNum));
			billNo = contNo + PvpUtils.numFormatToSeq(3, String.valueOf(newNum));
		}else {
			/** 插入一条新的记录号，序列号初始值为1 */
			cmisAgent.insertPvpAutocode(contNo);
			billNo = contNo + PvpUtils.numFormatToSeq(3, "1");
		}
		return billNo;
	}
	
	public String getSernoByContNo(String contNo) throws Exception{
		String serno =null;
		PvpAgent cmisAgent = (PvpAgent)this.getAgentInstance(PvpConstant.PVPAGENT);
		serno = cmisAgent.getSernoByContNo(contNo);
		return serno;
		
	}
	/**
	 * @author lisj
	 * @description 需求:【XD140818051】清空iqp_group_pvp表中所有数据方法
	 * @time 2014年12月10日
	 * @verion v1.0
	 */
	 public String deleteAll4IqpGroupPvp() throws ComponentException{		
		 PvpAgent pvpAgent = (PvpAgent)this.getAgentInstance(PvpConstant.PVPAGENT);
			String  result = pvpAgent.deleteAll4IqpGroupPvp();	
		return result;
	}	
}
