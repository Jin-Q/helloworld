package com.yucheng.cmis.biz01line.iqp.op.iqpbatchmng;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryIqpBatchMngDetailOp  extends CMISOperation {
	private final String modelId = "IqpBatchMng";
	private final String batch_no_name = "batch_no";
	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			String batchno = null;
			try {
				batchno = (String)context.getDataValue(batch_no_name);
			} catch (Exception e) {}
			if(batchno == null || batchno.length() == 0)
				throw new EMPJDBCException("The value of pk["+batch_no_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, batchno, connection);
			this.putDataElement2Context(kColl, context);
			
			/** 组织机构、登记机构翻译 */
			//SInfoUtils.addUSerName(kColl, new String[]{"manager_id"});
			//SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id"});
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
