package com.yucheng.cmis.biz01line.lmt.op.lmtinduslistapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class ImportLmtIndusListPopOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement("CusBase");
			} catch (Exception e) {}			
			String conditionStr = TableModelUtil.getQueryCondition_bak("CusBase", queryData, context, false, true, false);
			
			/*** 行业名单中已有的客户直接过滤掉 ***/
			String conditionWhere = " cus_id not in  ( select cus_id from lmt_indus_list_apply where serno in "
					+ " (select serno from lmt_indus_apply where approve_status not in ('990','997','998')) "
					+ " union select cus_id from lmt_indus_list_mana ) and BELG_LINE in ('BL100','BL200') and cus_status='20'  "
					+ " order by main_br_id ";
			if (conditionStr.indexOf("WHERE") == -1) {
				conditionStr = "where " + conditionWhere;
			} else {
				conditionStr = conditionStr + " and " + conditionWhere;
			}

			String sql = "select cus_id,cus_name,cert_type,cert_code,cust_mgr,main_br_id,belg_line from cus_base "+conditionStr;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "10");
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);
			
			SInfoUtils.addSOrgName(iColl, new String[] { "main_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "cust_mgr" });			
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