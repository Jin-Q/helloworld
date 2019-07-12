package com.yucheng.cmis.biz01line.lmt.op.lmtquotaadjust;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.utils.LmtUtils;
import com.yucheng.cmis.operation.CMISOperation;

public class GetLmtQuotaAdjustAddPageOp extends CMISOperation {

	private final String modelId = "LmtQuotaAdjustApp";

	private final String fin_agr_no_name = "fin_agr_no";

	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {

			String fin_agr_no_value = null;
			try {
				fin_agr_no_value = (String) context.getDataValue(fin_agr_no_name);
			} catch (Exception e) {
			}
			if (fin_agr_no_value == null || fin_agr_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk[" + fin_agr_no_name +"] cannot be null!");

			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);

			String conditionStr = "where fin_agr_no= '" + fin_agr_no_value +"' AND STATUS in ('1','3') and approve_status in ('000','111','992','993') order by end_date desc";
			String lmt_start_date = "";
			KeyedCollection kColl = dao.queryDetail("LmtAgrFinGuar", fin_agr_no_value, connection);
			if(kColl!=null&&kColl.containsKey("lmt_start_date")){
				lmt_start_date = kColl.getDataValue("lmt_start_date").toString();
			}
			
			KeyedCollection kCollTemp = dao.queryFirst(modelId, null, conditionStr, connection);
			
			String date = (String) kCollTemp.getDataValue("end_date");//上一笔的到期日期
			
			if (date == null || "".equals("date")) {//如果没有date则把授信协议的起始日期默认赋给生效日期
				context.addDataField("end_date", lmt_start_date);
			}else{
				String endDate = LmtUtils.computeEndDate(date, "003", "1");
				context.addDataField("end_date", endDate);
			}

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
