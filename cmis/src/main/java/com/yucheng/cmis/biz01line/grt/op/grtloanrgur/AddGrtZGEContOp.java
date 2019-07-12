package com.yucheng.cmis.biz01line.grt.op.grtloanrgur;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class AddGrtZGEContOp  extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String isFromLmt="";
		String guar_cont_no="";
		try{
			connection = this.getConnection(context);
			if(context.containsKey("isFromLmt")){
				isFromLmt = (String)context.getDataValue("isFromLmt");
				if("is".equals(isFromLmt)){
					if(context.containsKey("guar_cont_no")){
						guar_cont_no = (String)context.getDataValue("guar_cont_no");
						CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
						LmtServiceInterface service = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
						KeyedCollection resultKc = service.queryRLmtGuarContInfo(guar_cont_no,"3",connection);
						if(resultKc != null){
							context.addDataField("is_per_gur", resultKc.getDataValue("is_per_gur"));
							context.addDataField("is_add_guar", resultKc.getDataValue("is_add_guar"));
						}
					}else{
						throw new Exception("授信协议/授信台账编号为空，请检查!");
					}
				}
			}
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
