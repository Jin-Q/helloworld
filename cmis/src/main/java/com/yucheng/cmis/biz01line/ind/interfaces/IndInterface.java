package com.yucheng.cmis.biz01line.ind.interfaces;
import com.yucheng.cmis.base.CMISException;

public interface IndInterface {
	/**
	 * 检验指标组管理的必输项是否全部输入
	 * @throws CMISException 
	 * 
	 */
	public boolean CheckAllRequiredInput(String serno,String modelId) throws CMISException;
}
