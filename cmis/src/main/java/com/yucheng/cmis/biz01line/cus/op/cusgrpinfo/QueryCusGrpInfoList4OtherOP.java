package com.yucheng.cmis.biz01line.cus.op.cusgrpinfo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusGrpInfoList4OtherOP extends CMISOperation {

	private final String modelId = "CusGrpInfo";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {

			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection) context
						.getDataElement(this.modelId);
			} catch (Exception e) {
			}

			String conditionStr = TableModelUtil.getQueryCondition(
					this.modelId, queryData, context, false, false, false);
			conditionStr = StringUtil.transConditionStr(conditionStr, "grp_no");
			conditionStr = StringUtil.transConditionStr(conditionStr, "grp_name");
/*
			if (grpno != null && !grpno.trim().equals("")
					&& queryData.size() > 0) {
				conditionStr = conditionStr + " and grp_no='" + grpno + "'";
			} else if (grpno != null && !grpno.trim().equals("")
					&& queryData.size() == 0) {
				conditionStr = conditionStr + " grp_no='" + grpno + "'";
			}*/
			conditionStr = conditionStr + "  order by grp_no desc";

			int size = 15;

			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one",
					String.valueOf(size));

			TableModelDAO dao = (TableModelDAO) this.getTableModelDAO(context);

			List<String> list = new ArrayList<String>();
			list.add("grp_no");
			list.add("grp_name");
			list.add("parent_cus_id");
			list.add("parent_cus_name");
			list.add("cus_manager");
			list.add("main_br_id");
			IndexedCollection iColl = dao.queryList(modelId, list,
					conditionStr, pageInfo, connection);
			iColl.setName(iColl.getName() + "List");
			SInfoUtils.addSOrgName(iColl, new String[] { "main_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "cus_manager" });
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
