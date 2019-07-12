package com.yucheng.cmis.biz01line.iqp.dao;

import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.pub.dao.SqlClient;

public class IqpBatchDao extends CMISDao {

	/**
	 * 查询批次下票据期限不能包含制定日期的票据
	 * @param batch_no 批次号
	 * @param date 日期
	 * @return returnKColl 票据集合
	 * @throws Exception
	 */
	public IndexedCollection getBillsByDate(String batch_no,String date) throws Exception{
		IndexedCollection result = null;
		try {
			Map paramMap = new HashMap();
			paramMap.put("batch_no", batch_no);
			paramMap.put("bill_isse_date", date);
			paramMap.put("porder_end_date", date);
			result = (IndexedCollection)SqlClient.queryList4IColl("getBillsByDate", paramMap, this.getConnection());
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("查询批次下票据期限不能包含制定日期的票据报错！");
		}
		return result;
	}

	/**
	 * 根据流水号获取,该业务下票据批次的实付总金额和贴现总利息
	 * @param serno 业务流水号
	 * @return returnKColl 票据批次综合信息
	 * @throws Exception
	 */
	public IndexedCollection getBatchTotalInfoBySerno(String serno) throws Exception{
		IndexedCollection result = null;
		try {
			Map paramMap = new HashMap();
			paramMap.put("serno", serno);
			result = (IndexedCollection)SqlClient.queryList4IColl("getBatchTotalInfoBySerno", paramMap, this.getConnection());
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("查询批次下票据期限不能包含制定日期的票据报错！");
		}
		return result;
	}
	/**
	 * 根据流水号获取,该业务下票据批次信息
	 * @param serno 业务流水号
	 * @return returnKColl 票据批次综合信息
	 * @throws Exception
	 */
	public KeyedCollection getBatchMngBySerno(String serno) throws Exception{
		KeyedCollection result = null;
		try {
			result = (KeyedCollection)SqlClient.queryFirst("getBatchMngBySerno", serno, null, this.getConnection());
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("查询失败！");
		}
		return result;
	}
}
