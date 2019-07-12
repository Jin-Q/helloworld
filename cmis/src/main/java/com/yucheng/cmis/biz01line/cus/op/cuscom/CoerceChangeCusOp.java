package com.yucheng.cmis.biz01line.cus.op.cuscom;

import java.sql.Connection;


import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class CoerceChangeCusOp extends CMISOperation {

	//模型ID
	private final String modelIdBase = "CusBase";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String cus_id = "";
			try {
				cus_id = (String)context.getDataValue("cus_id");
			} catch (Exception e) {
				throw new EMPException("客户码为空！");
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kCollCus = dao.queryDetail(modelIdBase, cus_id, connection);
			kCollCus.setDataValue("cus_status", "20");//客户状态改为正式客户
			dao.update(kCollCus, connection);
			CusBaseComponent cusBaseComp = (CusBaseComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance("CusBase", context, connection);
			//插入强制变更正式客户 记录信息
			int count = cusBaseComp.coerceChangeCusOp(cus_id, context, connection);
		    context.put("flag", "success");
		    context.put("msg", "强制变更成功!");
		    
		} catch (EMPException ee) {
			context.put("flag", "success");
			context.put("msg", ee.getMessage());
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
