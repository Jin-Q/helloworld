package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.pvp.component.PvpComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * @author lisj
 * @time 2014年11月18日
 * @description 需求:【XD140818051】出账队列管理导出功能op
 * @modified by lisj 2014-12-10 合同评分配置，导出方法修改为以报表形式展示
 */
public class CtrLoanContExpBatchToExcelOp extends CMISOperation {
	private final String modelId = "IqpGroupPvp";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection iqpGroupPvpIColl= null;
			//IndexedCollection CtrLoanContSumIColl= null;
			String conditionStr ="";
			String contNo= context.getDataValue("contNo").toString().trim();//获取的合同编号连接字符串
			conditionStr +=" P2.CONT_NO = P3.CONT_NO and P2.CONT_NO = P4.CONT_NO(+) and P2.CUS_ID = P5.CUS_ID(+) and instr('"+ contNo+"', P2.CONT_NO)>0";
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			String conditionSelect = " SELECT P2.cont_no,                                                 "
									+"        P2.input_br_id,                                             "
									+"        P2.prd_id,                                                  "
									+"        case when           										  "
									+"			p3.is_close_loan = '1' then	 '无间贷'				      "														
									+"		  else												  		  "
									+"	(select s.cnname from s_dic s where s.opttype ='STD_LOAN_FORM'    "
									+"						      and s.enname = p3.loan_form)			  "								       
									+"		end as add_or_colse_loan,                                     "
									+"        P2.cus_id,                                                  "
									+"        P2.cont_amt,                                                "
									+"        P3.cont_term || '(' || (SELECT S.CNNAME FROM S_DIC S        "
									+"                     WHERE S.OPTTYPE = 'STD_ZB_TERM_TYPE'           "
									+"         AND S.ENNAME = P3.TERM_TYPE) || ')' AS CONT_TERM,          "
									+"        P3.reality_ir_y,                                            "
									+"        P3.ir_float_rate,                                           "
									+"        P2.assure_main,                                             "	
									+"        case when  P4.agr_no is not null then '1' else '2'		  "												
									+"		  end as joint_guar_flag,                                     "
									+"		  nvl(P5.dep_contribute, 0) as dep_contribute,				  "
									+"		  nvl(P3.collateral_type,' ') as collateral_type,			  "
									+"        row_number()over(order by P2.cont_number desc) row_number,  "
									+"		  P2.input_id                                                 "
									+" FROM CTR_LOAN_CONT P2, CTR_LOAN_CONT_SUB P3,(select rbli.cont_no,  " 
									+" lad.agr_no from r_bus_lmt_info rbli, lmt_agr_details lad where     " 
									+" rbli.agr_no = lad.limit_code and lad.sub_type = '03') P4,          " 
									+" (select cdc.dep_contribute, cdc.cus_id from cus_dep_contribute cdc) P5 WHERE" +conditionStr;
			iqpGroupPvpIColl = TableModelUtil.buildPageData(null, dataSource, conditionSelect);		
			iqpGroupPvpIColl.setName("iqpGroupPvpList");
			
			/**String conditionSelect4Sum = "SELECT P2.input_br_id, sum(P2.cont_amt) sum_cont_amt from ctr_loan_cont P2 WHERE "
				+"instr('"+contNo+"', P2.cont_no)>0 GROUP BY input_br_id";
			CtrLoanContSumIColl = TableModelUtil.buildPageData(null, dataSource, conditionSelect4Sum);
			CtrLoanContSumIColl.setName("CtrLoanContSumList");**/
			
			
			String[] args=new String[] { "prd_id","cus_id"};
			String[] modelIds=new String[]{"PrdBasicinfo","CusBase","CtrLoanContSub","CtrLoanContSub"};
			String[]modelForeign=new String[]{"prdid ","cus_id","cont_no","cont_no"};
			String[] fieldName=new String[]{"prdname","cus_name","is_close_loan"};
			String[] resultName = new String[] { "prdname","cus_name","is_close_loan"};
			//详细信息翻译时调用			
		    SystemTransUtils.dealName(iqpGroupPvpIColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName); 
		    SystemTransUtils.dealPointName(iqpGroupPvpIColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName, resultName);
			SInfoUtils.addSOrgName(iqpGroupPvpIColl, new String[]{"input_br_id"});
			//SInfoUtils.addSOrgName(CtrLoanContSumIColl, new String[]{"input_br_id"});
			
			//this.putDataElement2Context(iqpGroupPvpIColl, context);
			//this.putDataElement2Context(CtrLoanContSumIColl, context);
			
			//执行插入数据前，先清空iqp_group_pvp表中的数据
			PvpComponent pvpComponent = (PvpComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("PvpComponent", context, connection);
			String result = pvpComponent.deleteAll4IqpGroupPvp();//1表示删除成功 ，2表示删除失败
			if(result.equals("1")){
				for(int i=0; i<iqpGroupPvpIColl.size();i++){
						KeyedCollection temp  = (KeyedCollection) iqpGroupPvpIColl.get(i);
						temp.setName(modelId);
						dao.insert(temp, connection);
				}
			}
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
