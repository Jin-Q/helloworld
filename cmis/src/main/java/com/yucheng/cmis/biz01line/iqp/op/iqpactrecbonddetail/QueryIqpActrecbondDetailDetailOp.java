package com.yucheng.cmis.biz01line.iqp.op.iqpactrecbonddetail;

import java.net.URLDecoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryIqpActrecbondDetailDetailOp extends CMISOperation {

	private final String modelId = "IqpActrecbondDetail";

	private final String cont_name = "cont_no";
	private final String invc_no_name = "invc_no";

	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String returnStr = "retPage";
		try {
			connection = this.getConnection(context);

			if (this.updateCheck) {
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}

			String cont_no_value = null;
			try {
				cont_no_value = (String) context.getDataValue(cont_name);
			} catch (Exception e) {
			}
			if (cont_no_value == null || cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk[" + cont_name
						+ "] cannot be null!");

			String invc_no_value = null;
			try {
				invc_no_value = (String) context.getDataValue(invc_no_name);
			} catch (Exception e) {
			}
			if (invc_no_value == null || invc_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk[" + invc_no_name
						+ "] cannot be null!");
			
			//中文转码
			cont_no_value = URLDecoder.decode(cont_no_value,"UTF-8");
			invc_no_value = URLDecoder.decode(invc_no_value,"UTF-8");

			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("cont_no", cont_no_value);
			pkMap.put("invc_no", invc_no_value);
			KeyedCollection kColl = dao.queryDetail(modelId, pkMap, connection);
			this.putDataElement2Context(kColl, context);

			//如果存在目标参数，说明是做回款登记，跳转到回款登记页面   2014-10-20 唐顺岩
			if(context.containsKey("target")){
				returnStr = "repayPage";
			}
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return returnStr;
	}
}
