package com.yucheng.cmis.biz01line.ind.interfaces.impl;
import com.yucheng.cmis.biz01line.ind.IndPubConstant;
import com.yucheng.cmis.biz01line.ind.component.IndComponent;
import com.yucheng.cmis.biz01line.ind.interfaces.DeleteIndResultValIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.ComponentException;

public class DeleteIndResultValImpl extends CMISComponent implements DeleteIndResultValIface {

	/**
	 * 根据编号，日期删除指标结果表里面的记录
	 * @param cus_id  编号
	 * @param app_begin_date 日期
	 * @return
	 * @throws ComponentException
	 */ 
	public int deleteIndResultVal(String cus_id,String app_begin_date) throws ComponentException
	{
		IndComponent indcom=(IndComponent)this.getComponent(IndPubConstant.IND_COMPONENT);
		int count = indcom.deleteIndResultVal(cus_id,app_begin_date);
		
		return count ;
	}

}
