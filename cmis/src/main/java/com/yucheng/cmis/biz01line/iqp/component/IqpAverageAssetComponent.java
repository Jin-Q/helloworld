package com.yucheng.cmis.biz01line.iqp.component;

import javax.sql.DataSource;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.iqp.dao.IqpAverageAssetDao;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.util.TableModelUtil;

public class IqpAverageAssetComponent extends CMISComponent{
	/**
	 * 查询资产登记Pop框
	 * @param guar_cont_no_str 担保合同编号串
	 * @return res_value 返回所属客户编号下的担保合同信息
	 */
	public IndexedCollection getIqpAverageAssetListPop(String conditionStr,PageInfo pageInfo, DataSource dataSource) throws EMPException {
		IndexedCollection iColl = new IndexedCollection();
		IqpAverageAssetDao cmisDao = (IqpAverageAssetDao)this.getDaoInstance("IqpAverageAssetDao");
		iColl = cmisDao.getIqpAverageAssetListPop(conditionStr, pageInfo, dataSource);
		return iColl;
	}
	
	/**
	 * 查询资产登记Pop框（资产流转使用）
	 */
	public IndexedCollection getIqpAssetRegiPop(String conditionStr,PageInfo pageInfo, DataSource dataSource) throws EMPException {
		IndexedCollection iColl = new IndexedCollection();
		IqpAverageAssetDao cmisDao = (IqpAverageAssetDao)this.getDaoInstance("IqpAverageAssetDao");
		iColl = cmisDao.getIqpAssetRegiPop(conditionStr, pageInfo, dataSource);
		return iColl;
	}
	
	/**
	 * 资产登记卖出列表
	 * @param 
	 * @return
	 * @throws Exception
	 * @modified by lisj 2014-12-4 修改查询条件
	 */
	public IndexedCollection getIqpAverageAssetList(String condition,String conditionSelect,PageInfo pageInfo,DataSource dataSource) throws Exception {
		IndexedCollection iColl = new IndexedCollection("IqpAverageAssetList");
		String sql_select ="select aver.*,asset.asset_no as asset_no from Iqp_Average_Asset aver,Iqp_Asset_Rel asset where aver.bill_no = asset.bill_no(+)" +
				"  and aver.bill_no in(select bill_no from acc_view "+condition+")"+conditionSelect;
		iColl = TableModelUtil.buildPageData(pageInfo, dataSource, sql_select);
		return iColl;
	}
}
