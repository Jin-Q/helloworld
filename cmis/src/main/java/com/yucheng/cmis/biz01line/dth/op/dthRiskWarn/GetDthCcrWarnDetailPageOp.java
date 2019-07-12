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

public class GetDthCcrWarnDetailPageOp extends CMISOperation {

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
			org_condition = org_condition.replaceAll("manager_br_id", "main_br_id");	//cus_base
			/*** 总分支行机构判断处理end ***/
			
			/*** 授信是以客户为单位的，客户经理无法取授信申请中责任人，这里取客户信息的客户经理 ***/
			if(type.equals("one")){
				String values[] = value.split(",");	//拆分成line_enname和grade_enname
				sql = "select b.belg_line,b.cus_id,b.cus_name,b.cus_crd_grade,b.cus_crd_dt,b.cust_mgr,b.main_br_id "
					+ " from cus_base b where b.cus_crd_grade = '"+ values[1]
					+ "' and b.belg_line = '"+ values[0]+ "' and b.cus_status = '20'"+org_condition;
			}else{
				String condition = "";
				if(value.equals("include")){	//到期标识
					condition = " and  cus_crd_dt > (select openday from pub_sys_info)";
				}else{
					condition = " and  cus_crd_dt < (select openday from pub_sys_info)";
				}
				sql = "select cus_id,cus_name,b.cus_crd_grade,b.cus_crd_dt,b.cust_mgr,b.main_br_id, "
				+ "case when cus_crd_dt < (select openday from pub_sys_info) then '已到期' else '未到期' end as over_status "
				+ " from cus_base b where cus_status = '20' and cus_crd_grade > '00' " + condition +org_condition;
			}

			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "10");
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);			
			SInfoUtils.addSOrgName(iColl, new String[] { "main_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "cust_mgr" });
			iColl.setName("CcrWarnDetail");
			
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