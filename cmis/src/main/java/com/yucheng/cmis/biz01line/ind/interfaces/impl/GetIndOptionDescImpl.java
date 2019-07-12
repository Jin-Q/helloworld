package com.yucheng.cmis.biz01line.ind.interfaces.impl;
import com.yucheng.cmis.biz01line.ind.IndPubConstant;
import com.yucheng.cmis.biz01line.ind.component.IndComponent;
import com.yucheng.cmis.biz01line.ind.interfaces.GetIndOptionDescIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.ComponentException;

public class GetIndOptionDescImpl extends CMISComponent implements GetIndOptionDescIface {

	 
	/**
	 * 根据指标值，指标选项值获取指标名称
	 * @param indexNo  指标编号
	 * @param indexValue 指标选项值
	 * @return
	 * @throws ComponentException
	 */
	public String getIndOptionDesc(String indexNo,String indexValue) throws ComponentException
	{
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		String Reason = indcom.queryIndDesc(indexNo,indexValue);
		return Reason;
	}

}