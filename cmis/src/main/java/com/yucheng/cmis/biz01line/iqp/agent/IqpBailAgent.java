package com.yucheng.cmis.biz01line.iqp.agent;

import javax.sql.DataSource;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.dao.IqpBailDao;
import com.yucheng.cmis.pub.CMISAgent;

public class IqpBailAgent extends CMISAgent {
	
	
	/**
	 * 查询所有已生效的合同
	 * @param pageInfo 分页信息
	 * @return returnIColl已生效的合同信息
	 * @throws Exception
	 */
	public IndexedCollection queryPubBailInfoList(String conditionStr,String condition,PageInfo pageInfo,DataSource dataSource)  throws Exception {
		IqpBailDao IqpBailDao = (IqpBailDao)this.getDaoInstance(AppConstant.IQPBAILDAO);
		return IqpBailDao.queryPubBailInfoList(conditionStr,condition,pageInfo,dataSource);
	}
	
	/**
	 * 根据合同编号获取保证金金额（保证金金额=合同金额*保证金比例）
	 * @param cont_no 合同编号
	 * @return returnKColl保证金比例和保证金金额kc
	 * @throws Exception
	 */
	public KeyedCollection getBailAmtByContNo(String cont_no)  throws Exception {
		// TODO Auto-generated method stub
		IqpBailDao IqpBailDao = (IqpBailDao)this.getDaoInstance(AppConstant.IQPBAILDAO);
		return IqpBailDao.getBailAmtByContNo(cont_no);
	}
	/**
	 * 根据业务编号删除保证金追加/提取申请信息及其明细
	 * @param serno 合同编号
	 * @return result删除的记录数
	 * @throws Exception
	 */
	public int deleteBailAppBySerno(String serno) throws Exception{
		// TODO Auto-generated method stub
		IqpBailDao IqpBailDao = (IqpBailDao)this.getDaoInstance(AppConstant.IQPBAILDAO);
		return IqpBailDao.deleteBailAppBySerno(serno);
	}
}
