package com.yucheng.cmis.biz01line.cus.op.cusindivsocrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryLegalPersonOp extends CMISOperation {

	private final String modelId4Mmg = "CusComManager";	//高管关联表

	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		String cus_id = "";		//对公客户id
		String legal_id = "";	//法定代表人id
		try {
			connection = this.getConnection(context);

			
			cus_id = (String) context.getDataValue("cus_id");
			TableModelDAO dao = (TableModelDAO) this.getTableModelDAO(context);
			
			String condStr = " where cus_id='"+cus_id+"' and com_mrg_typ='02'";
			KeyedCollection kColl4Mng = dao.queryFirst(modelId4Mmg, null, condStr,  connection);
			if(kColl4Mng.size()>0  && kColl4Mng.getDataValue("cus_id_rel")!=null){
				legal_id = kColl4Mng.getDataValue("cus_id_rel").toString();
			}

			context.addDataField("legal_id", legal_id);
			
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
