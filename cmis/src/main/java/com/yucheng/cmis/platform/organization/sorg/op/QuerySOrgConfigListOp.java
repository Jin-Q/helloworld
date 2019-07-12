package com.yucheng.cmis.platform.organization.sorg.op;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class QuerySOrgConfigListOp extends CMISOperation {

	private final String modelId = "SOrg";

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
			if (conditionStr == null || "".trim().equals(conditionStr)) {
				conditionStr = " where 1=1";
			}
			String artiorganno=(String)context.getDataValue("ARTI_ORGANNO");
			conditionStr+=" and arti_organno='"+artiorganno+"' ";
			try {
				String currid = (String) context.getDataValue("currentUserId");

				if (currid.equals("admin")) {
					conditionStr = " where 1=1 ";
				}
			} catch (Exception e) {
			}
			conditionStr += " order by organno desc";
			// 添加记录级权限
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId,
					conditionStr, context, connection);

			int size = 15;

			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one",
					String.valueOf(size));

			TableModelDAO dao = (TableModelDAO) this.getTableModelDAO(context);

			List list = new ArrayList();
			list.add("organno");
			list.add("suporganno");
			list.add("arti_organno");
			list.add("organname");
			list.add("fincode");
			list.add("distno");
			list.add("area_dev_cate_type");
			IndexedCollection iColl = dao.queryList(modelId, list,
					conditionStr, pageInfo, connection);
			iColl.setName(iColl.getName() + "List");
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
