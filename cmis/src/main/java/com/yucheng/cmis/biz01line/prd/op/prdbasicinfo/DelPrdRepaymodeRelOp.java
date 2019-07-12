package com.yucheng.cmis.biz01line.prd.op.prdbasicinfo;

import java.sql.Connection;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class DelPrdRepaymodeRelOp extends CMISOperation {
	private static final String relModel = "RPrdRepaymode";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String prdid = (String)context.getDataValue("prdid");
			String repay_mode_id = (String)context.getDataValue("repay_mode_id");
			if(prdid == null || prdid.trim().length() == 0){
				throw new EMPException("引入还款方式【产品编号】ID获取失败！");
			}
			if(repay_mode_id == null || repay_mode_id.trim().length() == 0){
				throw new EMPException("引入还款方式【还款方式代码】ID获取失败！");
			}
			
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("prdid", prdid);
			map.put("repay_mode_id", repay_mode_id);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.deleteByPks(relModel, map, connection);
			context.addDataField("flag", "success");
		} catch (Exception e) {
			context.addDataField("flag", "falied");
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
