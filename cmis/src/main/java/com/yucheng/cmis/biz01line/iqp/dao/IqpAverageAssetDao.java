package com.yucheng.cmis.biz01line.iqp.dao;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.grt.msi.msiimple.GrtServiceInterfaceImple;
import com.yucheng.cmis.pub.CMISDao;
import com.yucheng.cmis.util.TableModelUtil;

public class IqpAverageAssetDao extends CMISDao{
	private static final Logger logger = Logger.getLogger(GrtServiceInterfaceImple.class);
	
	/**
	 * 查询资产登记Pop框
	 * @param guar_cont_no_str 担保合同编号串
	 * @return res_value 返回所属客户编号下的担保合同信息
	 * @modified by lisj 2014-11-27 查询SQL增加币种字段
	 */
	public IndexedCollection getIqpAverageAssetListPop(String conditionStr,PageInfo pageInfo, DataSource dataSource) throws EMPException {
		IndexedCollection res_value = new IndexedCollection();
		logger.info("---------------查询资产登记Pop框开始---------------");
		try {
			String sql_select ="select b.repay_type,b.interest_term,b.ir_accord_type,b.ir_type," +
	          "b.pad_rate_y,b.ir_adjust_type,b.ir_next_adjust_term,b.ir_next_adjust_unit,ruling_ir," +
	          "b.fir_adjust_day,b.ir_float_type,b.ir_float_rate,b.ir_float_point,b.reality_ir_y," +
	          "b.overdue_float_type,b.overdue_rate,b.overdue_point,b.overdue_rate_y," +
	          "b.default_float_type,b.default_rate,b.default_point,b.default_rate_y," +
	          "b.repay_term,b.repay_space,a.assure_main,a.prd_id,c.average_status,c.serno,c.bill_no,c.cont_no,c.cus_id, " +
	          "d.bill_bal as loan_balance,d.bill_amt as loan_amt,d.start_date as distr_date,d.end_date,d.five_class,d.manager_br_id,d.fina_br_id, a.cont_cur_type cur_type, d.table_model " +
	          "from ctr_loan_cont a, ctr_loan_cont_sub b,iqp_average_asset c,acc_view d " +
	          "where a.cont_no = b.cont_no and a.cont_no = c.cont_no and c.bill_no=d.bill_no " +
	          "and c.average_status='1' and d.bill_bal > 0 ";
			if(null != conditionStr && !"".equals(conditionStr)){
				sql_select +=" and ("+conditionStr.substring(6)+")";
			}
			res_value = TableModelUtil.buildPageData(pageInfo, dataSource, sql_select);
		}catch (Exception e) {
			logger.error("查询资产登记Pop框失败，错误描述："+e.getMessage());
			throw new EMPException("获查询资产登记Pop框失败，错误描述："+e.getMessage());
		}
		logger.info("---------------查询资产登记Pop框结束---------------");
		return res_value;
	}
	
	/**
	 * 查询资产登记Pop框（资产流转使用）
	 */
	public IndexedCollection getIqpAssetRegiPop(String conditionStr,PageInfo pageInfo, DataSource dataSource) throws EMPException {
		IndexedCollection res_value = new IndexedCollection();
		logger.info("---------------查询资产登记Pop框开始---------------");
		try {
			String sql_select ="select a.*,b.asset_status from iqp_asset_regi_app a,iqp_asset_regi b" +
					" where a.serno = b.serno and b.asset_status = '01' " +
					" and a.bill_no not in (select bill_no from iqp_asset_trans_list ti,iqp_asset_trans_app ta where ti.serno = ta.serno and ta.approve_status != '998') " +//不在资产清单
					" and a.bill_no not in (select bill_no from iqp_asset_pro_list pi,iqp_asset_pro_app pa where pi.serno = pa.serno and pa.approve_status != '998') ";//不在资产清单
			if(null != conditionStr && !"".equals(conditionStr)){
				sql_select +=" and ("+conditionStr.substring(6)+")";
			}
			res_value = TableModelUtil.buildPageData(pageInfo, dataSource, sql_select);
		}catch (Exception e) {
			logger.error("查询资产登记Pop框失败，错误描述："+e.getMessage());
			throw new EMPException("获查询资产登记Pop框失败，错误描述："+e.getMessage());
		}
		logger.info("---------------查询资产登记Pop框结束---------------");
		return res_value;
	}
}
