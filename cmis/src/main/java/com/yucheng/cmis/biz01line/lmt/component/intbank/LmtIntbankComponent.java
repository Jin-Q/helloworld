package com.yucheng.cmis.biz01line.lmt.component.intbank;

import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.lmt.agent.intbank.LmtIntbankLmtAgent;
import com.yucheng.cmis.pub.CMISComponent;

public class LmtIntbankComponent extends CMISComponent {
	public void insertcus2batchlist(Map<String,String> map) throws EMPException{
		LmtIntbankLmtAgent intbankagent = (LmtIntbankLmtAgent)this.getAgentInstance("LmtIntbank");
		intbankagent.insertcus2batchlist(map);		
	}
	public IndexedCollection QueryCusInfoList (String batchNo)throws EMPException{
		LmtIntbankLmtAgent intbankagent = (LmtIntbankLmtAgent)this.getAgentInstance("LmtIntbank");
		return intbankagent.QueryCusInfoList(batchNo);
	}
	//将单笔授信插入台账
	public KeyedCollection insert2kColl(KeyedCollection kColl) throws EMPException{
		LmtIntbankLmtAgent intbankLmtAgent = (LmtIntbankLmtAgent)this.getAgentInstance("LmtIntbank");
		return intbankLmtAgent.insert2kColl(kColl);
	}
	public KeyedCollection insert2kColl4Agr(KeyedCollection kColl) throws EMPException{
		LmtIntbankLmtAgent intbankLmtAgent = (LmtIntbankLmtAgent)this.getAgentInstance("LmtIntbank");
		return intbankLmtAgent.insert2kColl4Agr(kColl);
	}
	//将批量授信包里的单户授信逐一插入台账中
	public KeyedCollection insert2kColl4Acc(KeyedCollection kColl) throws EMPException{
		LmtIntbankLmtAgent intbankLmtAgent = (LmtIntbankLmtAgent)this.getAgentInstance("LmtIntbank");
		return intbankLmtAgent.insert2kColl4Acc(kColl);
	}
	
	public double getOddAmt(KeyedCollection kColl) throws EMPException{
		LmtIntbankLmtAgent intbankLmtAgent =(LmtIntbankLmtAgent)this.getAgentInstance("LmtIntbank");
		return intbankLmtAgent.getOddAmt(kColl);
	}
	public void DeleteLmtSub(String serno) throws EMPException{
		LmtIntbankLmtAgent intbankLmtAgent = (LmtIntbankLmtAgent)this.getAgentInstance("LmtIntbank");
		intbankLmtAgent.DeleteLmtSub(serno);
	}
	public void updateSub(IndexedCollection iColl) throws EMPException{
		LmtIntbankLmtAgent intbankLmtAgent = (LmtIntbankLmtAgent)this.getAgentInstance("LmtIntbank");
		intbankLmtAgent.updateSub(iColl);
	}
	//根据批量客户编号修改批量包的状态
	public void updateStatus(String batch_cus_no) throws EMPException{
		LmtIntbankLmtAgent intbankLmtAgent = (LmtIntbankLmtAgent)this.getAgentInstance("LmtIntbank");
		intbankLmtAgent.updateStatus(batch_cus_no);
	}


}
