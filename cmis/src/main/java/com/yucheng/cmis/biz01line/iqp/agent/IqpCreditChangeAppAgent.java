package com.yucheng.cmis.biz01line.iqp.agent;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.dao.IqpBatchDao;
import com.yucheng.cmis.biz01line.iqp.dao.IqpCreditChangeAppDao;
import com.yucheng.cmis.pub.CMISAgent;

public class IqpCreditChangeAppAgent extends CMISAgent {

	/**
	 * 查询合同和出账表 
	 * @param date 日期
	 * @return returnKColl 集合
	 * @throws Exception
	 */
	public IndexedCollection getIqpCreditChangeApp() throws Exception {
		IqpCreditChangeAppDao creditDao = (IqpCreditChangeAppDao)this.getDaoInstance(AppConstant.IQPCREDITCHANGEAPPDAO);
		return creditDao.getIqpCreditChangeApp();
	}
	
	/**
	 * 担保变更新增时查询合同信息及客户经理绩效信息
	 * @return res_value 集合
	 * @throws Exception
	 */
	public IndexedCollection getIqpGuarChangeList(PageInfo pageInfo,String condition) throws Exception{
		IqpCreditChangeAppDao creditDao = (IqpCreditChangeAppDao)this.getDaoInstance(AppConstant.IQPCREDITCHANGEAPPDAO);
	    IndexedCollection res_value = creditDao.getIqpGuarChangeList(pageInfo,condition);  
		return res_value;
	}
}
