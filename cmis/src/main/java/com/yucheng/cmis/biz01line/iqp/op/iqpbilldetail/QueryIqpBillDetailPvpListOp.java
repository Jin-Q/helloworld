package com.yucheng.cmis.biz01line.iqp.op.iqpbilldetail;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpBillDetailPvpListOp extends CMISOperation {
	private final String billModel = "IqpBillDetail";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection) context
						.getDataElement(this.billModel);
			} catch (Exception e) {
			}

			String contditionStr = TableModelUtil.getQueryCondition_bak(
					billModel, queryData, context, false, true, false);
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			contditionStr = recordRestrict.judgeQueryRestrict(this.billModel,
					contditionStr, context, connection);
			DataSource dataSource = (DataSource) context
					.getService(CMISConstance.ATTR_DATASOURCE);

			int size = 15;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one",
					String.valueOf(size));

			/*
			 * List<String> list = new ArrayList<String>();
			 * list.add("porder_no"); list.add("bill_type");
			 * list.add("bill_isse_date"); list.add("porder_end_date");
			 * list.add("drft_amt"); list.add("isse_name");
			 * list.add("pyee_name"); list.add("aaorg_name");
			 * list.add("aaorg_no"); list.add("aaorg_type");
			 * list.add("aorg_name"); list.add("aorg_no");
			 * list.add("aorg_type"); list.add("status");
			 */
			//XD150226013 20150413 Edited by FCL 
			String sql_select = "SELECT A.*, C.DSCNT_TYPE   FROM IQP_BILL_DETAIL A,  (SELECT *  FROM (SELECT ROW_NUMBER() OVER(PARTITION BY B.PORDER_NO ORDER BY B.DSCNT_TYPE ASC) RN,   B.PORDER_NO,   B.DSCNT_TYPE    FROM ACC_DRFT B) D WHERE 1 = 1   AND RN = 1) C  WHERE A.PORDER_NO = C.PORDER_NO AND A.PORDER_NO IN (SELECT PORDER_NO FROM IQP_BILL_DETAIL "
					+ contditionStr + ")";
			System.out.println("sql_select::"+sql_select);
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo,
					dataSource, sql_select);// dao.queryList(billModel,list
											// ,contditionStr,pageInfo,connection);

			iColl.setName("IqpBillDetailList");
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
