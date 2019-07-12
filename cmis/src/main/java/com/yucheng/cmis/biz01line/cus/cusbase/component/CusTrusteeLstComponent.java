package com.yucheng.cmis.biz01line.cus.cusbase.component;

import java.util.ArrayList;
import java.util.List;
import com.yucheng.cmis.biz01line.cus.cusbase.agent.CusTrusteeLstAgent;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CusTrusteeLstComponent extends CMISComponent {
	
   
 
   public List<CMISDomain> findCusTrusteeLstListBySerno(String serno) throws ComponentException{
		List<CMISDomain> cusHandoverLst = new ArrayList<CMISDomain>();
		CusTrusteeLstAgent cusHandoverLstAgent = (CusTrusteeLstAgent) this
		.getAgentInstance(PUBConstant.CUSTRUSTEELST);
		cusHandoverLst = cusHandoverLstAgent.findCusBySernoList(serno);
		return cusHandoverLst;
	}
}
