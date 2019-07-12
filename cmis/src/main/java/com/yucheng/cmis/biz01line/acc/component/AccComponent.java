package com.yucheng.cmis.biz01line.acc.component;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.acc.accPub.AccConstant;
import com.yucheng.cmis.biz01line.acc.agent.AccAgent;
import com.yucheng.cmis.biz01line.acc.dao.AccDao;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.util.TableModelUtil;

public class AccComponent extends CMISComponent{
	
	/**
	 * 通过合同编号查询台账信息
	 * @param guar_cont_no_str 担保合同编号
	 * @throws ComponentException
	 * @throws SQLException 
	 */
	public IndexedCollection getAcc(IndexedCollection iCollAcc,Connection connection) throws Exception{
		String cont_no_str = "";
		AccAgent cmisAgent = (AccAgent)this.getAgentInstance(AccConstant.ACCAGENT); 
		//把担保合同编号拼装成一个String 
		for(int i=0;i<iCollAcc.size();i++){ 
			KeyedCollection kColl = (KeyedCollection)iCollAcc.get(i); 
			String cont_no = (String)kColl.getDataValue("cont_no");
			cont_no_str += "'"+cont_no+"',"; 
		}
		if(cont_no_str.length()>1){
			cont_no_str = cont_no_str.substring(0, cont_no_str.length()-1);
		}
		IndexedCollection iColl = cmisAgent.getAcc(cont_no_str,connection);		
		return iColl;
	}
	/**
	 * 通过客户码查询台账信息
	 * @param cus_id 客户码
	 * @throws ComponentException
	 * @throws SQLException 
	 */
	public IndexedCollection getAccPop(String cus_id,String openDay,Connection connection) throws Exception{
		AccAgent cmisAgent = (AccAgent)this.getAgentInstance(AccConstant.ACCAGENT); 
		IndexedCollection iColl = cmisAgent.getAccPop(cus_id,openDay, connection);  
		return iColl; 	 
	}
	
	public IndexedCollection getPvpCont(String condition,DataSource dataSource,PageInfo pageInfo) throws Exception{
		IndexedCollection iColl = new IndexedCollection();
		try{
			//XD150520037_信贷系统利率调整修改优化 start
			String sql = "select a.bill_no,a.cont_no,a.prd_id,a.cus_id,a.cur_type,a.loan_amt,a.loan_balance,a.distr_date,a.end_date,a.acc_status,b.ir_accord_type,b.ir_type,a.ruling_ir,b.pad_rate_y,b.ir_adjust_type,b.ir_float_type,b.ir_float_rate,b.ir_float_point,b.reality_ir_y,b.overdue_float_type,b.overdue_rate,b.overdue_point,b.overdue_rate_y,b.default_float_type,b.default_rate,b.default_point,b.default_rate_y,b.cont_term,b.term_type,b.ruling_ir_code,c.manager_br_id from acc_loan a,ctr_loan_cont_sub b,ctr_loan_cont c "+condition+" and a.cont_no = b.cont_no and b.cont_no = c.cont_no";
			//XD150520037_信贷系统利率调整修改优化 end
			iColl = TableModelUtil.buildPageData(pageInfo, dataSource, sql);
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("获取台账合同信息失败，失败原因："+e.getMessage());
		}
		return iColl;
	}
	/**
	 * 通过客户码，任务流水查询贷后台账信息
     * added by yangzy 2014/10/17 贷后任务改造
	 * @param cus_id 客户码
     * @param task_id 任务流水
	 * @throws ComponentException
	 * @throws SQLException 
	 */
	public IndexedCollection getPvpContForPsp(String condition1,String condition2,String cus_id,String task_id,DataSource dataSource,PageInfo pageInfo) throws Exception{
		IndexedCollection iColl = new IndexedCollection();
		try{
			String sql = " select bill_no,               "
						+"        cont_no,               "
						+"        prd_id,                "
						+"        cus_id,                "
						+"        cur_type,              "
						+"        bill_amt loan_amt,     "
						+"        bill_bal loan_balance, "
						+"        start_date distr_date, "
						+"        end_date,              "
						+"        status acc_status      "
						+"   from acc_view               "+condition1;
			iColl = TableModelUtil.buildPageData(pageInfo, dataSource, sql);
			if(iColl==null ||iColl.size()<1){
				sql = " select bill_no,                                             "
					 +"        cont_no,                                             "
					 +"        prd_id,                                              "
					 +"        cus_id,                                              "
					 +"        cur_type,                                            "
					 +"        bill_amt loan_amt,                                   "
					 +"        bill_bal loan_balance,                               "
					 +"        start_date distr_date,                               "
					 +"        end_date,                                            "
					 +"        status acc_status                                    "
					 +"   from acc_view                                             "+condition2;
					 
					 //		"or to_date(start_date, 'yyyy-mm-dd') = "
					 //+"        (select to_date(TASK_CREATE_DATE, 'yyyy-mm-dd')      "
					 //+"                            from psp_check_task              "
					 //+"                           where task_id = '"+task_id+"'))   ";
				iColl = TableModelUtil.buildPageData(pageInfo, dataSource, sql);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("获取台账合同信息失败，失败原因："+e.getMessage());
		}
		return iColl;
	}
}
