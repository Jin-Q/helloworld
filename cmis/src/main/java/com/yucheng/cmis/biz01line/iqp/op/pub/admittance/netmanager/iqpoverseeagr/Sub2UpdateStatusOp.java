package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpoverseeagr;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class Sub2UpdateStatusOp extends CMISOperation {

	private final String modelId = "IqpOverseeAgr";
	

	private final String oversee_agr_no_name = "oversee_agr_no";
	private final String oprate_name = "oprate";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String oversee_agr_no_value = null;
			String oprate = null;
			try {
				oversee_agr_no_value = (String)context.getDataValue(oversee_agr_no_name);//获取监管协议编号
				oprate = (String)context.getDataValue(oprate_name);//获取监管协议编号
			} catch (Exception e) {}
			if(oversee_agr_no_value == null || oversee_agr_no_value.length() == 0 || oprate == null || oprate.length() == 0)
				throw new EMPJDBCException("输入参数异常，请检查!");
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryAllDetail(modelId, oversee_agr_no_value, connection);
			if("cfirm".equals(oprate)){
				kColl.setDataValue("status", "1");//确认操作，更改协议状态为生效
			}else if("rec".equals(oprate)){
				kColl.setDataValue("status", "3");//解除监管操作，更改协议状态为解除监管
			}else if("cfirmrec".equals(oprate)){
				kColl.setDataValue("status", "0");//解除监管确认操作，更改协议状态为失效
			}
			dao.update(kColl, connection);
			context.addDataField("flag", "success");
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException("更新协议状态失败！"+e.getMessage());
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
