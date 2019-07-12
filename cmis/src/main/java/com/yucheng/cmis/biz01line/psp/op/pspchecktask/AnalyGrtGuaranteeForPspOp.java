package com.yucheng.cmis.biz01line.psp.op.pspchecktask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class AnalyGrtGuaranteeForPspOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String guar_cus_id = null;//保证人客户码
		String belg_line = null;
		String fin_type = null;//财务报表类型
		
		connection = this.getConnection(context);
//		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		try {
			guar_cus_id = (String) context.getDataValue("guar_cus_id");
		} catch (Exception e) {
        	EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0, "获取不到保证人客户编号", null);
        	throw new EMPException(e);
        }
		//根据客户码获取客户信息，并把所属条线放入context中
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		try {
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			CusBase cusBase = service.getCusBaseByCusId(guar_cus_id, context, connection);
			belg_line = cusBase.getBelgLine();
			if(belg_line!=null&&!"BL300".equals(belg_line)){
				CusCom cusCom = service.getCusComByCusId(guar_cus_id, context, connection);
				fin_type = cusCom.getComFinRepType();
			}
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		context.put("belg_line", belg_line);
		context.put("fin_type", fin_type);
		
		return null;
	}
}
