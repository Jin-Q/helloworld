package com.yucheng.cmis.biz01line.lmt.op.lmtsubpaylist;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtFinSubpay.LmtFinSubpayComponent;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteLmtSubpayListRecordOp extends CMISOperation {

	private final String modelId = "LmtSubpayList";
	

	private final String pk_name = "pk";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);



			String pk_value = null;
			try {
				pk_value = (String)context.getDataValue(pk_name);
			} catch (Exception e) {}
			if(pk_value == null || pk_value.length() == 0)
				throw new EMPJDBCException("The value of ["+pk_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId,pk_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			String serno = context.getDataValue("serno").toString();
			LmtFinSubpayComponent Subpaycmpt = new LmtFinSubpayComponent();
			
			double totalSubpay = Subpaycmpt.getTotalSubpay(serno, connection);
			int subpayTimes = (int) Subpaycmpt.getSubpayTimes(serno, connection);
			
			KeyedCollection kCollApp = dao.queryFirst("LmtAppFinSubpay", null, "where serno='"+serno+"'", connection);
			kCollApp.setDataValue("subpay_totl_limit", totalSubpay);
			kCollApp.setDataValue("subpay_times", subpayTimes);
			dao.update(kCollApp, connection);
			context.addDataField("flag", "success");
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
