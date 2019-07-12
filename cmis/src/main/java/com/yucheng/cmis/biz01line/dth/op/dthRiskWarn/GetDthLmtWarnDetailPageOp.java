package com.yucheng.cmis.biz01line.dth.op.dthRiskWarn;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class GetDthLmtWarnDetailPageOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);		
			String value = context.getDataValue("value").toString();			
			String type = context.getDataValue("type").toString();
			String sql = "";
			
			/*** 总分支行机构判断处理begin ***/
			DthPubAction cmisOp = new DthPubAction();
			String org_condition = cmisOp.judgeOrg(context.getDataValue("organNo").toString(), connection);
			/*** 总分支行机构判断处理end ***/
			
			if(type.equals("Five")){
				org_condition = org_condition.replaceAll("manager_br_id", "main_br_id");
				sql = " select cus.cus_id,cus.cus_name,cus.cust_mgr,cus.main_br_id , '"+ value
						+ "' as belg_line,'CNY' as cur_type, "
						+ "  (select sum(crd_amt) from lmt_agr_details where cus_id = c.cus_id and lmt_status = '10') as crd_amt,  "
						+ "  (select sum(guar_amt) from grt_guarantee where cus_id = c.cus_id) as guar_amt  "
						+ "  from (select  distinct (a.cus_id) cus_id "
						+ "  from cus_base a,GRT_GUARANTEE b, lmt_agr_details d where a.cus_id = b.cus_id and a.cus_id = d.cus_id"+org_condition
						+ "  and lmt_status = '10' and belg_line = '"+ value
						+ "' ) c  ,cus_base cus  where c.cus_id = cus.cus_id ";
			} else if (type.equals("Threefour")) {
				org_condition = org_condition.replaceAll("manager_br_id", "main_br_id");
				sql = "select c.cus_id,c.belg_line ,'CNY' as cur_type,crd_amt,c.cus_name,c.cust_mgr,c.main_br_id  "
						+ "  from (select b.belg_line, d.cus_id, sum(d.crd_amt) as crd_amt  "
						+ "   from lmt_agr_details d, cus_base b "
						+ "   where d.lmt_status = '10' and d.cus_id = b.cus_id(+)" 
						+ "   and belg_line = '"+value+"'"+org_condition
						+ "   group by d.cus_id,b.belg_line)l, cus_base c  "
						+ "  where c.cus_id(+) = l.cus_id";
			}else if(type.equals("Onetwo")){
				String values[] = value.split(",");	//拆分成line_enname和guar_enname
				 
				sql = "select l.cus_id,sum(l.crd_amt) as crd_amt ,'CNY' as cur_type ,guar_type ,'"+values[0]+"' as belg_line, "
					+ "  (select cus_name from cus_base where cus_id = l.cus_id) as cus_name,  "
					+ "  (select cust_mgr from cus_base where cus_id = l.cus_id) as cust_mgr,  "
					+ "  a.manager_br_id as main_br_id  "
					+ "  from lmt_agr_details l , lmt_agr_info a where guar_type = '"+values[1]
					+ "' and l.lmt_status = '10' and l.agr_no = a.agr_no"+org_condition
					+ "  and l.cus_id in (select cus_id from cus_base where belg_line = '"+values[0]+"') " 
					+ "  group by l.cus_id ,guar_type,a.manager_br_id"
				    + " union all select l.cus_id,sum(l.crd_amt) as crd_amt ,'CNY' as cur_type ,l.guar_type ,'"+values[0]+"' as belg_line, "
						+ "  (select cus_name from cus_base where cus_id = l.cus_id) as cus_name,  "
						+ "  (select cust_mgr from cus_base where cus_id = l.cus_id) as cust_mgr,  "
						+ "  a.manager_br_id as main_br_id  "
						+ "  from lmt_agr_details l , lmt_agr_indiv a where l.guar_type = '"+values[1]
						+ "' and l.lmt_status = '10' and l.agr_no = a.agr_no"+org_condition
						+ "  and l.cus_id in (select cus_id from cus_base where belg_line = '"+values[0]+"') " 
						+ "  group by l.cus_id ,l.guar_type,a.manager_br_id";
				
				
				    
			}
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "10");
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);			
			SInfoUtils.addSOrgName(iColl, new String[] { "main_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "cust_mgr" });
			iColl.setName("LmtWarnDetail");
			
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)this.releaseConnection(context, connection);
		}
		return "0";
	}

}