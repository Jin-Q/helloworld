package com.yucheng.cmis.biz01line.lmt.op.lmtindusapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class CheckUniqueTypeOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection conn = null;
		try {
			conn = this.getConnection(context);
			String type = (String)context.getDataValue("type");
			String value = (String)context.getDataValue("value");
			String records = "0";
			
			if(type.equals("indusType")||type.equals("indusList")||type.equals("indusAgr")||type.equals("listMana")){
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance("LmtPubComponent",context,conn);
				records = lmtComponent.getAgrno(type, value);
			}else if(type.equals("LmtQuotaManager")){
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance("LmtPubComponent",context,conn);
				String code_id = (String)context.getDataValue("code_id");
				String serno = " ";
				if(context.containsKey("serno")){
					serno = (String)context.getDataValue("serno");
				}				
				String prd_id = "";
				String prd_ids[] = value.split(",");
				
				if(code_id.length() > 0 && value.length() > 0 ){
					prd_id = lmtComponent.getAgrno(type, code_id+","+serno);
					for(int i = 0 ; i < prd_ids.length ; i++){
						if(prd_id.indexOf(prd_ids[i]) >= 0){
							records = "failue";
						}
					}
				}				
			}
			
			if(records.equals("0")){
				context.addDataField("flag", "success");
			}else{
				context.addDataField("flag", "failue");
			}			
		}catch (EMPException ee) {
			context.addDataField("flag", "failue");
			throw ee;
		} catch (Exception e) {
			context.addDataField("flag", "failue");
			e.printStackTrace();
		} finally {
			if (conn != null)
				this.releaseConnection(context, conn);
		}
		return "0";
	}

}
