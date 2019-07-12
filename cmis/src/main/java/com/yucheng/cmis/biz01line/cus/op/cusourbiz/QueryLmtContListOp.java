package com.yucheng.cmis.biz01line.cus.op.cusourbiz;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class QueryLmtContListOp extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String cusId = "";
		try{
			connection = this.getConnection(context);
			cusId = (String)context.getDataValue("cusId");//LmtCont.cus_id
			//获取授信模块MSI接口
			CMISModualServiceFactory modualServiceFactory = CMISModualServiceFactory.getInstance();
			LmtServiceInterface lmtService = (LmtServiceInterface)modualServiceFactory.getModualServiceById("lmtServices","lmt");
			
			IndexedCollection iColl = lmtService.searchLmtAgrInfoList(cusId, "1", connection);
			iColl.setName("LmtAgrInfoList");
			this.putDataElement2Context(iColl, context);
//			TableModelUtil.parsePageInfo(context, pageInfo);
			
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
