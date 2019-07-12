package com.yucheng.cmis.biz01line.cus.op.cusother.cusaffil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class ExpCusAffilToExcelOp extends CMISOperation {
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement("CusAffil");
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition_bak("CusAffil", queryData, context, false, true, false);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict("CusAffil", conditionStr, context, connection);

			List<String> tableFields = new ArrayList<String>();
			tableFields.add("affil_rela");
			tableFields.add("cus_id");
			tableFields.add("cus_name");
			tableFields.add("auth_crd_balance");
			tableFields.add("cus_affil_id");
			tableFields.add("cus_affil_name");
			tableFields.add("affil_auth_crd_balance");

			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList("CusAffil", tableFields, conditionStr, connection);
			iColl.setName("CusSameOrgList");
			this.putDataElement2Context(iColl, context);


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
