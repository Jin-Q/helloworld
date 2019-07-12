package com.yucheng.cmis.biz01line.cus.op.custrusteelog;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryCusTrusteeLogListOp extends CMISOperation {


	private final String modelId = "CusTrusteeLog";


	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		try{
			connection = this.getConnection(context);


			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}


			String cusId = (String) context.getDataValue("currentUserId");

			StringBuffer sb = new StringBuffer();
			sb.append(" where consignor_id='").append(cusId).append(
					"' or supervise_id='").append(cusId).append(
					"' or trustee_id='").append(cusId).append("' ");

			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+ sb + "order by serno desc";

			int size = 15;

			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));


			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);

			List<String> list = new ArrayList<String>();
			list.add("trustee_date");
			list.add("trustee_scope");
			list.add("consignor_br_id");
			list.add("consignor_id");
			list.add("trustee_br_id");
			list.add("trustee_id");
			list.add("supervise_br_id");
			list.add("supervise_id");
			list.add("retract_date");
			list.add("serno");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			SInfoUtils.addSOrgName(iColl, new String[]{"consignor_br_id", "trustee_br_id", "supervise_br_id"});
			SInfoUtils.addUSerName(iColl, new String[]{"consignor_id", "trustee_id", "supervise_id"});
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);


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
