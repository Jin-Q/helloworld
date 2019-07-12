package com.yucheng.cmis.biz01line.iqp.agent;

import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.dao.CusManagerDao;
import com.yucheng.cmis.pub.CMISAgent;
import com.yucheng.cmis.pub.exception.ComponentException;

public class CusManagerAgent extends CMISAgent{
	/**
	 * 产品配置关联机构设置，遍历iColl中的kColl,根据kColl中的操作参数进行相关才做
	 * @param iColl 产品适用机构iColl
	 * @param prdId 产品编号
	 * @author 王硕
	 * @throws Exception 
	 */
	public int insertCusManagerByIColl(String serno, IndexedCollection iColl,String cont_no) throws Exception {
		CusManagerDao cmisDao = (CusManagerDao)this.getDaoInstance(AppConstant.CUSMANAGERDAO);
		int m =cmisDao.insertCusManagerByIColl(serno, iColl,cont_no);
		return m;
	}
}
