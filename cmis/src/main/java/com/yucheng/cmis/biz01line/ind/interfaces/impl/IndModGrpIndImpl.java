package com.yucheng.cmis.biz01line.ind.interfaces.impl;

import java.util.ArrayList;
import java.util.HashMap;

import com.yucheng.cmis.biz01line.ind.IndPubConstant;
import com.yucheng.cmis.biz01line.ind.component.IndComponent;
import com.yucheng.cmis.biz01line.ind.domain.IndLibDomain;
import com.yucheng.cmis.biz01line.ind.interfaces.IndModGrpIndIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.exception.ComponentException;

public class IndModGrpIndImpl extends CMISComponent implements
		IndModGrpIndIface {
	public ArrayList getFncIndexArray(String modelNo)
			throws ComponentException {
		// TODO Auto-generated method stub
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		ArrayList fncIndArray = indcom.getFncIndexArray(modelNo);
		return fncIndArray;
	}

	public ArrayList<HashMap> queryGrpIndexesList(String groupNo)
			throws ComponentException {  
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		ArrayList<HashMap> list=indcom.queryGrpIndexesList(groupNo);
		return list;
	}
	
	public ArrayList<HashMap> queryModGrpList(String modelNo)
			throws ComponentException { 
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		ArrayList<HashMap> list=indcom.queryModGrpList(modelNo);
		return list;
	}

	public <CMISDomain> ArrayList queryIndGroupDomain(String conditionValues)
			throws ComponentException { 
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		ArrayList domainList=indcom.queryIndGroupDomain(conditionValues);
		return domainList;
	}

	public <CMISDomain> ArrayList queryIndGroupIndexDomain(
			String conditionValues) throws ComponentException { 
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		ArrayList domainList=indcom.queryIndGroupIndexDomain(conditionValues);
		return domainList;
	}

	public CMISDomain queryIndLibDetail(String index_no)
			throws ComponentException { 
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		IndLibDomain  indLibDomain = new IndLibDomain();
		indLibDomain =(IndLibDomain)indcom.queryIndLibDetail(index_no);
		return indLibDomain;
	}

}
