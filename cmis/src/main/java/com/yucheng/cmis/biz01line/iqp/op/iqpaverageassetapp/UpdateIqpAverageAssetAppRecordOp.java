package com.yucheng.cmis.biz01line.iqp.op.iqpaverageassetapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateIqpAverageAssetAppRecordOp extends CMISOperation {
	

	private final String modelId = "IqpAverageAssetApp";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			//判断此笔借据是否存在
			String bill_no = (String)kColl.getDataValue("bill_no");
			TableModelDAO dao = this.getTableModelDAO(context);
		    IndexedCollection iColl = dao.queryList(modelId, "where bill_no='"+bill_no+"' and approve_status<>'000'", connection);
		    if(iColl.size()>0){
				context.addDataField("flag", "error");
				context.addDataField("msg", "已存在此笔借据!");
		    }else{
		    	int count=dao.update(kColl, connection);
				if(count!=1){
					throw new EMPException("Update Failed! Record Count: " + count);
				}
				context.addDataField("flag", "success");
				context.addDataField("msg", "保存成功");
		    }
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
			context.addDataField("msg","保存失败,失败原因:"+ee.getMessage());
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
