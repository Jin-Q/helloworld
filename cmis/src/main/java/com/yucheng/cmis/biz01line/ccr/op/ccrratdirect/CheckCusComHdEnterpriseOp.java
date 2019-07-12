package com.yucheng.cmis.biz01line.ccr.op.ccrratdirect;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class CheckCusComHdEnterpriseOp extends CMISOperation {
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String cus_id = (String) context.getDataValue("cus_id");
			//从客户表获取相关信息
			CusComComponent cusComComp = (CusComComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOM, context, connection);
			CusCom cusCom = cusComComp.getCusCom(cus_id);
			String com_hd_enterprise = cusCom.getComHdEnterprise();
			//此客户为国家级龙头企业或者省级龙头企业
			if("1".equals(com_hd_enterprise)||"2".equals(com_hd_enterprise)){
				context.addDataField("flag","success");
			}else{
				context.addDataField("flag","fail");
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
