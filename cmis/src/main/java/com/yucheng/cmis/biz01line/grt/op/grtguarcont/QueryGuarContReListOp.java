package com.yucheng.cmis.biz01line.grt.op.grtguarcont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class QueryGuarContReListOp extends CMISOperation {
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String guar_cont_no = "";
			if(context.containsKey("guar_cont_no")){  
				guar_cont_no = (String)context.getDataValue("guar_cont_no");
			}
//			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "15");
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			IndexedCollection iCollIqp = service.getIqpGuarContReByGuarContNo(guar_cont_no, connection);
			iCollIqp.setName("IqpGuarContReList");
			this.putDataElement2Context(iCollIqp, context);
			LmtServiceInterface service1mt = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
			IndexedCollection iCollLmt = service1mt.getLmtGuarReByGuarContNo(guar_cont_no, connection);
			iCollLmt.setName("LmtGuarContReList");
			this.putDataElement2Context(iCollLmt, context);
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
