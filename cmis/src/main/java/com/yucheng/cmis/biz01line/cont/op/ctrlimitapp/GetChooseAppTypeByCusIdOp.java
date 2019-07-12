package com.yucheng.cmis.biz01line.cont.op.ctrlimitapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class GetChooseAppTypeByCusIdOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String cus_id = (String)context.getDataValue("cus_id");
			/** 通过客户编号查询额度合同表中是否存在该客户的有效的额度合同 */
			
			String flag = "";
			String cont_no = "";
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection ic = dao.queryList("CtrLimitCont", null, " where cont_status = '200' and cus_id = '"+cus_id+"'", connection);
			if(ic != null && ic.size() > 0){
				KeyedCollection kc = (KeyedCollection)ic.get(0);
				flag = "yes";
				cont_no = (String)kc.getDataValue("cont_no"); 
			}else {
				flag = "no";
			}
			context.addDataField("flag", flag);
			context.addDataField("cont_no", cont_no);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
