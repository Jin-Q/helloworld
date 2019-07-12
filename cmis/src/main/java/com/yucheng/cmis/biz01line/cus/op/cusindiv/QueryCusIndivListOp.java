package com.yucheng.cmis.biz01line.cus.op.cusindiv;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

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

public class QueryCusIndivListOp extends CMISOperation {

	private final String modelId = "CusBase";

	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		try {
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection) context.getDataElement(this.modelId);
			} catch (Exception e) {}

			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
	    	RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId,conditionStr, context, connection);
			
			conditionStr = StringUtil.transConditionStr(conditionStr,"cus_name");
			
			if(conditionStr==null||"".equals(conditionStr)){
				conditionStr = " where belg_line='BL300'";
			}else{
				conditionStr = conditionStr + " and belg_line='BL300'";
			}
			conditionStr += " order by input_date desc, cus_id desc";

			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one",String.valueOf(size));

			TableModelDAO dao = this.getTableModelDAO(context);

			List<String> list = new ArrayList<String>();
			
			IndexedCollection iColl = dao.queryList(modelId, list,conditionStr, pageInfo, connection);
			iColl.setName(iColl.getName() + "List");
			SInfoUtils.addSOrgName(iColl, new String[] { "main_br_id" });
			SInfoUtils.addUSerName(iColl, new String[] { "cust_mgr"});
			
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
