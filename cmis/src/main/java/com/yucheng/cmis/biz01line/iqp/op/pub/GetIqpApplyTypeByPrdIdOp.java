package com.yucheng.cmis.biz01line.iqp.op.pub;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.prd.domain.PrdBasicinfo;
import com.yucheng.cmis.biz01line.prd.msi.PrdServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class GetIqpApplyTypeByPrdIdOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String prdid = "";
			if(context.containsKey("prdid")){
				prdid = (String)context.getDataValue("prdid");
			}
			if(prdid == null || prdid.trim().length() == 0){
				throw new EMPException("获取产品编号失败！");
			}
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			PrdServiceInterface psi = (PrdServiceInterface)serviceJndi.getModualServiceById("prdServices", "prd");
			PrdBasicinfo pb = psi.getPrdBasicinfoList(prdid, connection);
			
			String apply_type = pb.getLoanflow();
			if(apply_type != null && apply_type.trim().length() > 0){
				context.addDataField("flag", "success");
				context.addDataField("msg", "");
				context.addDataField("apply_type", apply_type);
			}else {
				context.addDataField("flag", "failed");
				context.addDataField("msg", "获取产品配置申请类型失败！请检查产品配置是否正确！");
				context.addDataField("apply_type", "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
