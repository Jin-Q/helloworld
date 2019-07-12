package com.yucheng.cmis.biz01line.cus.op.cuscom;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class GetPcodebyRegStateCodeOp extends CMISOperation {
	private final String modelId = "CusPcode";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);

			String post_addr = (String)context.getDataValue("post_addr");
			String addSort = (String)context.getDataValue("addSort");
			String conditionStr = "where regionalism='"+ post_addr + "'" ;
			KeyedCollection kColl = dao.queryFirst(modelId, null, conditionStr, connection);
			String pcode = (String)kColl.getDataValue("pcode");
			
			context.addDataField("flag",PUBConstant.SUCCESS);
			context.addDataField("post_code",pcode);
			context.setDataValue("addSort", addSort);
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}
}