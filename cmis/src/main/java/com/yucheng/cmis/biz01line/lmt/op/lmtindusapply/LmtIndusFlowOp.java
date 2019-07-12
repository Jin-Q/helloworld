package com.yucheng.cmis.biz01line.lmt.op.lmtindusapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class LmtIndusFlowOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		context.addDataField("operMsg", "");
		try{
			connection = this.getConnection(context);
			String menuId = (String) context.getDataValue("menuId");
			String serno = (String) context.getDataValue("serno");
			String agr_no = "";
			LmtPubComponent ccrComponent = (LmtPubComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance("LmtPubComponent",context,connection);
			
			if(menuId.equals("indus_crd_change")){
				agr_no = ccrComponent.getAgrno("getAgrno",serno);
			}else{
				agr_no = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);
			}
			ccrComponent.doVirtualSubmit(menuId,serno,agr_no);
			
			context.setDataValue("operMsg", "1");
		}catch (EMPException ee) {
			context.setDataValue("operMsg", "2");
			throw ee;
		}catch (Exception e) {
			context.setDataValue("operMsg", "2");
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}
}