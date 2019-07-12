package com.yucheng.cmis.biz01line.cus.op.cuscom;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.StringUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusComListOp extends CMISOperation {

	private final String modelId = "CusBase";

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection) context.getDataElement(modelId);
			} catch (Exception e) {}
//			try {
//				String cus_id = (String) queryData.getDataValue("cus_id");
//				String cus_name = (String) queryData.getDataValue("cus_name");
//				String cert_type = (String) queryData.getDataValue("cert_type");
//				String cert_code = (String) queryData.getDataValue("cert_code");
//				String cust_mgr = (String) queryData.getDataValue("cust_mgr");
//				String main_br_id = (String) queryData.getDataValue("main_br_id");
//				context.addDataField("cus_id", cus_id);
//				context.addDataField("cus_name", cus_name);
//				context.addDataField("cert_type", cert_type);
//				context.addDataField("cert_code", cert_code);
//				context.addDataField("cust_mgr", cust_mgr);
//				context.addDataField("main_br_id", main_br_id);
//				queryData = null;
//			} catch (Exception e) {
//			}
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			conditionStr = StringUtil.transConditionStr(conditionStr,"com_cll_name");
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId,conditionStr, context, connection);
			conditionStr = StringUtil.transConditionStr(conditionStr, "cus_name");
			
			if(conditionStr == null ||"".equals(conditionStr.trim()) ){
				conditionStr = " where belg_line in('BL100','BL200') order by input_date desc,cus_id desc";
			}else{
				conditionStr += " and belg_line in('BL100','BL200') order by input_date desc,cus_id desc";
			}
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one",String.valueOf(size));
			TableModelDAO dao = this.getTableModelDAO(context);
			
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr, pageInfo, connection);
			iColl.setName(iColl.getName() + "List");
			
			SInfoUtils.addSOrgName(iColl, new String[]{"main_br_id"});
			SInfoUtils.addUSerName(iColl, new String[]{"cust_mgr"});
			
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