package com.yucheng.cmis.biz01line.ccr.interfaces.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.yucheng.cmis.biz01line.ccr.component.CcrComponent;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppDetail;
import com.yucheng.cmis.biz01line.ccr.domain.CcrAppInfo;
import com.yucheng.cmis.biz01line.ccr.interfaces.CcrGetAppIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CcrGetAppImpl  extends CMISComponent implements CcrGetAppIface {
	private static final Logger logger = Logger.getLogger(CMISComponent.class);
	/**
	 * 通过业务编号"serno" 查询 CcrAppInfo Domain对象
	 * @param serno
	 * @return
	 * @throws ComponentException
	 */
	public CcrAppInfo getCcrAppInfo(String serno) throws ComponentException {
		{
			CcrAppInfo retApp=null;
			// TODO Auto-generated method stub
			try {
				CcrComponent ccrComponent = (CcrComponent)
				this.getOtherComponentInstance(CcrPubConstant.CCR_COMPONENT);
				retApp = ccrComponent.loadCcrAppInfo(serno);
			} catch (ComponentException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(), e);
				throw new ComponentException(e);
			}
			return retApp;
		}
	}
	/**
	 * 通过业务编号"serno",以及客户内部编码"cusId" 查询CcrAppDetail Domain对象
	 * @param serno
	 * @param cusId
	 * @return
	 * @throws ComponentException
	 */
	public CcrAppDetail getCcrAppDetail(String serno, String cusId)
	throws ComponentException {
		CcrAppDetail retApp=null;
		// TODO Auto-generated method stub
		try {
			CcrComponent ccrComponent = (CcrComponent)
			this.getOtherComponentInstance(CcrPubConstant.CCR_COMPONENT);
	//		retApp = ccrComponent.loadCcrAppDetail(serno, cusId);
		} catch (ComponentException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
			throw new ComponentException(e);
		}
		return retApp;
	}
	public List<CcrAppDetail> getCcrAppDetailList(String serno)
			throws ComponentException {
		// TODO Auto-generated method stub
		ArrayList retAppList=null;
		// TODO Auto-generated method stub
		try {
			CcrComponent ccrComponent = (CcrComponent)
			this.getOtherComponentInstance(CcrPubConstant.CCR_COMPONENT);
			retAppList = ccrComponent.getCcrAppDetailList(serno);
		} catch (ComponentException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
			throw new ComponentException(e);
		}
		return retAppList;
	}

}
