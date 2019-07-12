package com.yucheng.cmis.biz01line.dth.op.dthLoanSubjectAnaly;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;

public class GetDthOrgBadAnalyMainPageOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			DthPubAction cmisOp = new DthPubAction();			
			KeyedCollection kColl = new KeyedCollection();			
			kColl.addDataField("submitType", "getOrgBadAnalyChart");
			IndexedCollection iColl = cmisOp.delSqlReturnIcoll(kColl, context);
			iColl.setName("OrgBadAnalyList");
			
			this.putDataElement2Context(iColl, context);			
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