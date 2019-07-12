package com.yucheng.cmis.biz01line.iqp.dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.util.TableModelUtil;

public class IqpBailDao extends CMISDao {
	
	/**
	 * 查询所有已生效的合同
	 * @param pageInfo 分页信息
	 * @return returnIColl已生效的合同信息
	 * @throws Exception
	 */
	public IndexedCollection queryPubBailInfoList(String conditionStr,String condition,PageInfo pageInfo,DataSource dataSource) throws Exception{
		IndexedCollection result = new IndexedCollection();
		try {
			String 	sql_select=null;
			condition = condition.substring(6, condition.length());
			if(conditionStr.equals("")){
			 	sql_select="SELECT a.*,b.security_rate FROM pub_bail_info a ,ctr_loan_cont b WHERE b.cont_status = '200' and a.cont_no = b.cont_no and "+condition;
			}else{
				sql_select="SELECT a.*,b.security_rate FROM (SELECT * FROM pub_bail_info "+conditionStr+" ) a , ctr_loan_cont b WHERE b.cont_status = '200' and  a.cont_no = b.cont_no and "+condition;
			}
			result = TableModelUtil.buildPageData(pageInfo, dataSource, sql_select);
		} catch (Exception e) {
			throw new Exception("查询所有已生效的合同出错！");
		}
		return result;
	}
	
	
	
	/**
	 * 根据合同编号获取保证金金额（保证金金额=合同金额*保证金比例）
	 * @param cont_no 合同编号
	 * @return returnKColl保证金比例和保证金金额kc
	 * @throws Exception
	 */
	public KeyedCollection getBailAmtByContNo(String cont_no) throws Exception{
		KeyedCollection result = new KeyedCollection();
		try {
			result = (KeyedCollection)SqlClient.queryFirst("getBailAmtByContNo",cont_no,null, this.getConnection());
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("根据合同编号获取保证金金额出错！");
		}
		return result;
	}
	/**
	 * 根据业务编号删除保证金追加/提取申请信息及其明细
	 * @param serno 合同编号
	 * @return result删除的记录数
	 * @throws Exception
	 */
	public int deleteBailAppBySerno(String serno) throws Exception{
		int result = 0;
		try {
			result = SqlClient.executeUpd("deleteBailAppBySerno", serno, null, null, this.getConnection());
			result += SqlClient.executeUpd("deleteBailDetailBySerno", serno, null, null, this.getConnection());
			result += SqlClient.executeUpd("deletePubBailBySerno", serno, null, null, this.getConnection());
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("根据业务编号删除保证金追加/提取申请信息及其明细出错！");
		}
		return result;
	}
}
