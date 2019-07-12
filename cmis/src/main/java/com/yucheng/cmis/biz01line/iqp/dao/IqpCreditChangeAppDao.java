package com.yucheng.cmis.biz01line.iqp.dao;

import javax.sql.DataSource;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.util.TableModelUtil;

public class IqpCreditChangeAppDao extends CMISDao {

	/**
	 * @return returnKColl 集合
	 * @throws Exception
	 */
	public IndexedCollection getIqpCreditChangeApp() throws Exception{
		IndexedCollection result = null;
		try {
			result = (IndexedCollection)SqlClient.queryList4IColl("getIqpCreditChangeApp", null, this.getConnection());
		} catch (Exception e) {
			throw new Exception("查询信用证修改新增页面数据报错！"); 
		}
		return result;
	}
	/**
	 * 担保变更新增时查询合同信息及客户经理绩效信息
	 * PageInfo pageInfo分页信息
	 * @return res_value 集合
	 * @throws Exception
	 */
	public IndexedCollection getIqpGuarChangeList(PageInfo pageInfo,String condition) throws Exception{
		IndexedCollection res_value = null;
		String sql = null;
		if(null == condition || "".equals(condition)){
			sql = "select a.* from ctr_loan_cont a";
		}else{
			sql = "select a.* from ctr_loan_cont a "+condition+"";
		}
		DataSource dataSource = (DataSource) this.getContext().getService(CMISConstance.ATTR_DATASOURCE);
		try {
			res_value = TableModelUtil.buildPageData(pageInfo, dataSource, sql);
		} catch (Exception e) {
			throw new EMPException("查询合同和主管客户经理失败，错误描述："+e.getMessage());
		}
		return res_value;
	}
}
