package com.yucheng.cmis.biz01line.dth.op.dthApproveQuery;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class SingleDthApproveQueryPageOp extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			/*** 1.调用命名sql先取出客户表与授信表中的数据 ***/
			Object submitType = context.getDataValue("submitType");
			DthPubAction cmisOp = new DthPubAction();
			KeyedCollection kColl = new KeyedCollection();
			kColl.addDataField("submitType", submitType);
			IndexedCollection iColl_result = cmisOp.delSqlReturnIcoll(kColl,context);			
			iColl_result.setName("ApproveQueryList");

			SInfoUtils.addSOrgName(iColl_result, new String[]{"orgid"});
			this.putDataElement2Context(iColl_result, context);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)this.releaseConnection(context, connection);
		}
		return "0";
	}

}