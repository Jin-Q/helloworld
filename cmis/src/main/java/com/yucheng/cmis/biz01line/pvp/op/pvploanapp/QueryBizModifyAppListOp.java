package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 
*@author lisj
*@time 2015-8-6
*@description TODO 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求
*@version v1.0
* <pre>
 * 修改记录
 *    修改后版本：     修改人：     修改日期：     修改内容： 
 *    
 * </pre>
*
 */
public class QueryBizModifyAppListOp extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{  
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement("PvpBizModify");
			} catch (Exception e) {}
			String conditionStr = TableModelUtil.getQueryCondition_bak("PvpLoanApp", queryData, context, false, true, false);
			String currentUserId = (String)context.getDataValue("currentUserId");//当前登录人
			if(conditionStr!=null && !"".equals(conditionStr)){
				conditionStr+=" and input_id='"+currentUserId+"' and approve_status not in('997','998')";
			}else{
				conditionStr+=" where input_id='"+currentUserId+"' and approve_status not in('997','998')";
			}
			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			DataSource dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			//存在打回业务修改审批流程关联表中的数据
			String conditionSelect ="select serno,                                     "
				+"       cont_no,                                                      "
				+"       cus_id,                                                       "
				+"       prd_id,                                                       "
				+"       assure_main,                                                  "
				+"       cont_cur_type,                                                "
				+"       cont_amt,                                                     "
				+"       cont_balance,                                                 "
				+"       manager_br_id,                                                "
				+"       input_id,                                                     "
				+"       cont_status,                                                  "
				+"       biz_cate,                                                     "
				+"       modify_rel_serno,                                             "
				+"       approve_status                                                "
				+"  from (select p.serno,                                              "
				+"               c.cont_no,                                            "
				+"               c.cus_id,                                             "
				+"               c.prd_id,                                             "
				+"               c.assure_main,                                        "
				+"               c.cont_cur_type,                                      "
				+"               c.cont_amt,                                           "
				+"               c.cont_balance,                                       "
				+"               c.manager_br_id,                                      "
				+"               p.input_id,                                           "
				+"               c.cont_status,                                        "
				+"               case                                                  "
				+"                 when (select cb.belg_line                           "
				+"                         from cus_base cb                            "
				+"                        where cb.cus_id = c.cus_id) = 'BL300' then   "
				+"                  '0012'                                             "
				+"                 else                                                "
				+"                  '0011'                                             "
				+"               end as biz_cate,                                      "
				+"               c.modify_rel_serno,                                   "
				+"               p1.approve_status                                     "
				+"     from ctr_loan_cont_tmp c, pvp_loan_app p, pvp_biz_modify_rel p1 "
				+"         where c.cont_no = p.cont_no(+)                              "
				+"           and c.modify_rel_serno = p1.modify_rel_serno              "
				+"           and p.approve_modify_right = '1'                          "
				+"        union all                                                    "
				+"        select t.serno,                                              "
				+"               i.agr_no,                                             "
				+"               i.cus_id,                                             "
				+"               (select a.prd_id                                      "
				+"                  from acc_loan a                                    "
				+"                 where a.bill_no = t.fount_bill_no) as prd_id,       "
				+"               (select c1.assure_main                                "
				+"                  from ctr_loan_cont c1                              "
				+"                 where c1.cont_no = t.fount_cont_no) as assure_main, "
				+"               i.fount_cur_type,                                     "
				+"               i.fount_loan_amt,                                     "
				+"               i.fount_loan_balance,                                 "
				+"               i.manager_br_id,                                      "
				+"               t.input_id,                                           "
				+"               i.status,                                             "
				+"               '016' as biz_cate,                                    "
				+"               i.modify_rel_serno,                                   "
				+"               p1.approve_status                                     "
				+"          from iqp_extension_agr_tmp  i,                             "
				+"               iqp_extension_pvp  t,                                 "
				+"               pvp_biz_modify_rel p1                                 "
				+"         where i.agr_no = t.agr_no(+)                                "
				+"           and i.modify_rel_serno = p1.modify_rel_serno              "
				+"           and t.approve_modify_right = '1')                         ";
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, dataSource, conditionSelect+conditionStr);
			iColl.setName("BizModifyAppList");
			
			String[] args=new String[] { "cus_id","cont_no","prd_id"};
			String[] modelIds=new String[]{"CusBase","CtrLoanCont","PrdBasicinfo"};
			String[] modelForeign=new String[]{"cus_id","cont_no","prdid"};
			String[] fieldName=new String[]{"cus_name","serno","prdname"};
			String[] resultName = new String[] { "cus_id_displayname","fount_serno","prd_id_displayname"};
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});
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
