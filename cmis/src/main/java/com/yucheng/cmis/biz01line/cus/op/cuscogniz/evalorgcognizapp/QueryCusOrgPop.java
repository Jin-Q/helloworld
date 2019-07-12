package com.yucheng.cmis.biz01line.cus.op.cuscogniz.evalorgcognizapp;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusOrgPop extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			
            connection = this.getConnection(context);
            
            KeyedCollection queryData = null;
            try {
                queryData = (KeyedCollection)context.getDataElement("CusOrgPop");
            } catch (Exception e) {}            
            String conditionStr = TableModelUtil.getQueryCondition_bak("CusBase", queryData, context, false, true, false);
            if("".equals(conditionStr)||conditionStr==null){
            	conditionStr = " where 1=1 " ;
            }
          //added by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 start
			String cusQueryCondition = "";
			if(context.containsKey("cusTypCondition")&&context.getDataValue("cusTypCondition")!=null&&!"".equals(context.getDataValue("cusTypCondition"))){
				cusQueryCondition = (String) context.getDataValue("cusTypCondition");
				conditionStr += " and "+cusQueryCondition;
			}
			//added by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 end
            conditionStr = " and b.cus_id in (select cus_id from Cus_Base "+conditionStr+")";
            /**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
            /* modified by yangzy 2014/10/13 评估机构客户码选取改造 begin */
			String sql = " select b.cus_id,                                                                         "
						+"        b.cus_name,                                                                       "
						+"        b.cert_code,                                                                      "
						+"        c.acu_addr as extr_eval_addr,                                                     "
						+"        c.street,                                                                         "
						+"        (select cus_name from cus_base cb where cb.cus_id = p1.cus_id_rel) fic_per,       "
						+"        (select cus_name from cus_base cb where cb.cus_id = p2.cus_id_rel) real_oper_per, "
						+"        c.com_str_date,                                                                   "
						+"        c.reg_cap_amt,                                                                    "
						+"        c.legal_phone,                                                                    "
						+"		  b.cust_mgr,                                                                       "
						+"		  b.main_br_id	   																    "	
						+"   from cus_base b,                                                                       "
						+"        cus_com c,                                                                        "
						+"        (select cus_id_rel, cus_id                                                        "
						+"           from cus_com_manager                                                           "
						+"          where com_mrg_typ = '02'                                                        "
						+"            and cus_id_rel is not null) p1,                                               "
						+"        (select cus_id_rel, cus_id                                                        "
						+"           from cus_com_manager                                                           "
						+"          where com_mrg_typ = '01'                                                        "
						+"            and cus_id_rel is not null) p2                                                "
						+"  where b.cus_id = c.cus_id                                                               "
						+"    and b.cus_id = p1.cus_id(+)                                                           "
						+"    and b.cus_id = p2.cus_id(+)                                                           "
						+"    and b.cus_status = '20'                                                               "
						+"    and b.BELG_LINE in ('BL100', 'BL200')                                                 "+ conditionStr;
			/* modified by yangzy 2014/10/13 评估机构客户码选取改造 end */
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "15");
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);
			
			Map<String,String> map = new HashMap<String, String>();
			map.put("extr_eval_addr", "STD_GB_AREA_ALL");
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(iColl, map, service);
			SInfoUtils.addUSerName(iColl, new String[] { "cust_mgr" });
            SInfoUtils.addSOrgName(iColl, new String[] { "main_br_id" });
			/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}