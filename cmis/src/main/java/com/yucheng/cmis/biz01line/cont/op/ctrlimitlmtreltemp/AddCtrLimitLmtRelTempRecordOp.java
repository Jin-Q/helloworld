package com.yucheng.cmis.biz01line.cont.op.ctrlimitlmtreltemp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.pkgenerator.UNIDGenerator;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddCtrLimitLmtRelTempRecordOp extends CMISOperation {
	private final String modelId = "CtrLimitLmtRelTemp";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			String limitSerno = (String)kColl.getDataValue("limit_serno");
			String lmtCodeNo = (String)kColl.getDataValue("lmt_code_no");
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection queryIColl = dao.queryList(modelId, null, " where limit_serno = '"+limitSerno+"' and lmt_code_no = '"+lmtCodeNo+"'", connection);
			if(queryIColl != null && queryIColl.size() > 0){
				context.addDataField("flag", "failed");
				context.addDataField("cont_no", kColl.getDataValue("limit_cont_no"));
				context.addDataField("msg", "该笔业务引入的授信额度品种编号【"+lmtCodeNo+"】已存在，不允许重复引入！");
			}else {
				UNIDGenerator unid = new UNIDGenerator();
				kColl.setDataValue("pk_id", unid.getUNID());
				dao.insert(kColl, connection);
				context.addDataField("flag", "success");
				context.addDataField("cont_no", kColl.getDataValue("limit_cont_no"));
				context.addDataField("msg", "");
			}
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
