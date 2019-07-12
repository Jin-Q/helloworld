package com.yucheng.cmis.biz01line.cus.op.cussameorg;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class AddCusSameOrgDetailOp extends CMISOperation {
	/**
	 * ҵ���߼�ִ�еľ���ʵ�ַ���
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = ""; 
		KeyedCollection kColl = new KeyedCollection("CusSameOrg");
		try{
			connection = this.getConnection(context);

			kColl.addDataField("input_id",context.getDataValue("currentUserId"));
            kColl.addDataField("input_br_id",context.getDataValue("organNo"));
            SInfoUtils.addUSerName(kColl, new String[] {"input_id"});
    		SInfoUtils.addSOrgName(kColl, new String[] {"input_br_id"});
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		kColl.addDataField("serno", serno);
		this.putDataElement2Context(kColl, context);
		return "0";
	}
}
