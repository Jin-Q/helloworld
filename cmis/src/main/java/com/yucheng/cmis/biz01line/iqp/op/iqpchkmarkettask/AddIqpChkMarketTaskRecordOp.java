package com.yucheng.cmis.biz01line.iqp.op.iqpchkmarkettask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class AddIqpChkMarketTaskRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpMortValueAdj";
	private final String valModelId = "IqpMortValueMana";
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String value_no = "";
		try{
			connection = this.getConnection(context);
			value_no = (String) context.getDataValue("value_no");
			if(value_no==null){
				throw new EMPException("价格编号不能为空！");
			}
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			KeyedCollection valKColl = dao.queryDetail(valModelId, value_no, connection);
			
			KeyedCollection kColl = new KeyedCollection();
			kColl.addDataField("value_no", value_no);
			kColl.addDataField("org_valve", valKColl.getDataValue("market_value"));
			kColl.addDataField("input_id", context.getDataValue("currentUserId"));
			kColl.addDataField("input_br_id", context.getDataValue("organNo"));
			kColl.addDataField("input_date", context.getDataValue("OPENDAY"));
			kColl.addDataField("status", "1");//任务处理状态
			kColl.setName(modelId);
			dao.insert(kColl, connection);
			
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
