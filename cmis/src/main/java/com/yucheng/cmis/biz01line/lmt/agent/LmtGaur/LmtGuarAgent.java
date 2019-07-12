package com.yucheng.cmis.biz01line.lmt.agent.LmtGaur;

import java.sql.Connection;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.dao.IqpLoanAppDao;
import com.yucheng.cmis.biz01line.lmt.dao.LmtGuar.LmtGuarDao;
import com.yucheng.cmis.pub.CMISAgent;

public class LmtGuarAgent extends CMISAgent {
	public String queryGuarNo(String serno) throws EMPException{
		LmtGuarDao lmtguardao = (LmtGuarDao)this.getDaoInstance("LmtRGuar");
		return lmtguardao.queryGuarNo(serno);
	}
	public String queryCusId(String serno) throws EMPException{
		LmtGuarDao lmtguardao = (LmtGuarDao)this.getDaoInstance("LmtRGuar");
		return lmtguardao.queryCusId(serno);
	}
	
	public int updateRLmtGuarLvlYB(Map<String, String> map,Connection connection) throws Exception{
		LmtGuarDao lmtguardao = (LmtGuarDao)this.getDaoInstance("LmtRGuar");
		return lmtguardao.updateRLmtGuarLvlYB(map, connection);
	}
	public int updateRLmtGuarLvlZGE(Map<String, String> map,Connection connection) throws Exception{
		LmtGuarDao lmtguardao = (LmtGuarDao)this.getDaoInstance("LmtRGuar");
		return lmtguardao.updateRLmtGuarLvlZGE(map, connection); 
	}   
} 
