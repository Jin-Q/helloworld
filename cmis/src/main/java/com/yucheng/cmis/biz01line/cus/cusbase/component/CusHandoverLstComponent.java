package com.yucheng.cmis.biz01line.cus.cusbase.component;

import java.util.ArrayList;
import java.util.List;
import com.yucheng.cmis.biz01line.cus.cusbase.agent.CusHandoverLstAgent;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CusHandoverLstComponent extends CMISComponent {
	
   
 
   public List<CMISDomain> findCusHandoverLstListBySerno(String serno) throws ComponentException{
		List<CMISDomain> cusHandoverLst = new ArrayList<CMISDomain>();
		CusHandoverLstAgent cusHandoverLstAgent = (CusHandoverLstAgent) this
		.getAgentInstance(PUBConstant.CUSHANDOVERLST);
		cusHandoverLst = cusHandoverLstAgent.findCusBySernoList(serno);
		return cusHandoverLst;
	}
   public String deleteCusHandoverLst(String serno) throws ComponentException{
		
		CusHandoverLstAgent cusHandoverLstAgent = (CusHandoverLstAgent) this
		.getAgentInstance(PUBConstant.CUSHANDOVERLST);
		String  cusHandoverLst = cusHandoverLstAgent.deleteCusHandoverLst(serno);
		return cusHandoverLst;
	}
}
