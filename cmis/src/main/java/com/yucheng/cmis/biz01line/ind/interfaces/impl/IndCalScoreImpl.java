package com.yucheng.cmis.biz01line.ind.interfaces.impl;

import java.util.ArrayList;
import java.util.HashMap;

import com.yucheng.cmis.biz01line.ind.IndPubConstant;
import com.yucheng.cmis.biz01line.ind.component.IndComponent;
import com.yucheng.cmis.biz01line.ind.interfaces.IndCalScoreIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.ComponentException;

public class IndCalScoreImpl extends CMISComponent implements IndCalScoreIface {
  
	public String getGrpScore(String grpno, HashMap hm)
			throws ComponentException { 
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		String grpsc=indcom.getGrpScore(grpno, hm);
		return grpsc;
	}

	public String getIndScore(String grpno, String indexno, String indexval)
			throws ComponentException {
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		String indsc=indcom.getIndScore(grpno, indexno, indexval,null);
		return indsc;
	}
	 
	public String getModelScore(String modelno, HashMap hm,ArrayList list)
			throws ComponentException {
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		String modsc=indcom.getModelScore(modelno, hm,list); 
		return modsc;
	}
	
	public String getIndValue(String indexno, HashMap hm)throws ComponentException { 
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		String retVal=indcom.getIndexValue(indexno, hm);
		return retVal;
	}

	public HashMap<String, String> getIndOrgValAndOptVal(String modelno,
			String indexno, HashMap hm) throws ComponentException { 
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		System.out.println(this.getConnection());
		String retVal=indcom.getIndexValue(indexno, hm);
		HashMap<String,String> retHm=new HashMap<String,String>();
		if(retVal!=null&&!retVal.trim().equals("")&&retVal.trim().indexOf("#")!=-1){
			String values[]=retVal.split("#");
			if(values!=null&&values.length==2){
				retHm.put("optvalue", values[1]);
				retHm.put("orgvalue", values[0]);
			}else{
				retHm.put("optvalue", "");
				retHm.put("orgvalue", retVal);
			}
		}else{
			retHm.put("optvalue", "");
			retHm.put("orgvalue", retVal);
		}
		return retHm;
	}
	
	public String getIndScore(String grpno, String indexno, String indexval,HashMap para)
			throws ComponentException {
		IndComponent indcom = (IndComponent) this
				.getComponent(IndPubConstant.IND_COMPONENT);
		String indsc = indcom.getIndScore(grpno, indexno, indexval, para);
		return indsc;
	}

	public String getIndScore(String grpno, String indexno, String indexval,
			String orgVal) throws ComponentException {
		HashMap hmpara=new HashMap();
		if(orgVal==null)orgVal="";
		hmpara.put("optvalue", indexval);
		hmpara.put("orgvalue", orgVal); 
		return this.getIndScore(grpno,indexno,indexval,hmpara);
	}

	public String getIndScore(HashMap<String, String> hm)
			throws ComponentException {
		// TODO Auto-generated method stub
		String grpno =hm.get("grpno");
		String indexno =hm.get("indexno");
		String indexval =hm.get("indexval");
		
		hm.put("optvalue", indexval);
		
		return this.getIndScore(grpno,indexno,indexval,hm);
	}

}
