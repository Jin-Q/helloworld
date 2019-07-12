package com.yucheng.cmis.biz01line.ind.interfaces.impl;

import java.util.ArrayList;
import java.util.HashMap;

import com.yucheng.cmis.biz01line.ind.IndPubConstant;
import com.yucheng.cmis.biz01line.ind.component.IndComponent;
import com.yucheng.cmis.biz01line.ind.interfaces.IndResultValIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.ComponentException;

public class IndInsertResultImpl extends CMISComponent implements IndResultValIface {

	 
	public int insertIndResultVal(HashMap<String, String> hs)
			throws ComponentException {
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		int count = indcom.insertIndResultVal(hs);
		
		return count ;
	}
	
	public String queryIndResultVal(String serno,int year,int month,int day,String indexno) throws ComponentException{
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		String retVal=indcom.queryIndResVal(serno, year, month, day, indexno);
		return retVal;
	}

	public void deleteIndResultVal(String serno, int year, int month, int day,
			String indexno) throws ComponentException { 
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		indcom.delIndResVal(serno, year, month, day, indexno);
	}

	public void deleteIndResultValByNo(String serno, String indexno)
			throws ComponentException {
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		 indcom.delIndResValByNo(serno, indexno);
	}

	public String queryIndResultValByNo(String serno, String indexno)
			throws ComponentException {
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		String retVal=indcom.queryIndResValByNo(serno, indexno);
		return retVal;
	}
	/**
	 * 查询一个客户 该次评级的 所有定量指标的指标值
	 * @param serno 客户号
	 * @throws ComponentException
	 */
	public ArrayList<HashMap> queryIndResultValHm(String serno) throws ComponentException{
		ArrayList<HashMap> arr = null;
		IndComponent indcom = (IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		arr = indcom.queryIndResValList(serno);
		return arr;
	}

}
