package com.yucheng.cmis.biz01line.lmt.component.LmtGuar;

import java.sql.Connection;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.iqp.agent.IqpLoanAppAgent;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.lmt.agent.LmtGaur.LmtGuarAgent;
import com.yucheng.cmis.pub.CMISComponent;
public class LmtGuarComponent extends CMISComponent {
	public String queryGuarNo(String serno) throws EMPException{
		LmtGuarAgent lmtguaragent = (LmtGuarAgent)this.getAgentInstance("LmtRGuar");
		return lmtguaragent.queryGuarNo(serno);
		
	}
	public String queryCusId(String serno) throws EMPException{
		LmtGuarAgent lmtguaragent = (LmtGuarAgent)this.getAgentInstance("LmtRGuar");
		return lmtguaragent.queryCusId(serno);
		
	}
	
	public int updateRLmtGuarLvlYB(Map<String, String> map,Connection connection) throws Exception{
		LmtGuarAgent lmtguaragent = (LmtGuarAgent)this.getAgentInstance("LmtRGuar");
		return lmtguaragent.updateRLmtGuarLvlYB(map, connection);
	}  
	public int updateRLmtGuarLvlZGE(Map<String, String> map,Connection connection) throws Exception{
		LmtGuarAgent lmtguaragent = (LmtGuarAgent)this.getAgentInstance("LmtRGuar");
		return lmtguaragent.updateRLmtGuarLvlZGE(map, connection);   
	}  

}
