package com.yucheng.cmis.biz01line.iqp.op.iqpbillincome;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckForeRebuyDateOp extends CMISOperation {

	private final String modelId = "IqpBillIncome";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String porder_no = "";//汇票号码
			try {
				porder_no = (String)context.getDataValue("porder_no");
			} catch (Exception e) {
			    throw new Exception("检查OP中传入的汇票号码参数为空，请尝试刷新....");
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = "where porder_no='"+porder_no+"' and biz_type='02'";
			
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			String fore_rebuy_date = "";
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection)iColl.get(i);
				fore_rebuy_date = (String)kColl.getDataValue("fore_rebuy_date");
			}
			
			context.addDataField("flag", "success");
			context.addDataField("date", fore_rebuy_date);
		}catch (EMPException ee) {
			context.addDataField("flag", "failed");
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
