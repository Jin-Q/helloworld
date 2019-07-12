package com.yucheng.cmis.biz01line.fnc.op.importreports;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;

public class SaveCurrentFlowOp extends CMISOperation {

	/**
	 * 保存现金流量表
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			IndexedCollection dataList = null;
			KeyedCollection tableInfo = null;
			
			
			try {
				dataList = (IndexedCollection)context.getDataElement("DataList");
				tableInfo = (KeyedCollection)context.getDataElement("TableInfo");
			} catch (Exception e) {}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection tempKcoll = null;
			
			if(dataList != null){
				for(int i=0;i<dataList.size();++i){
					tempKcoll = (KeyedCollection)dataList.get(i);
					tempKcoll.setName("IqpMeFncCf");
					dao.update(tempKcoll, connection);
				}
			}
			if(tableInfo != null){
				tableInfo.setName("IqpMeFncCf");
				dao.update(tableInfo, connection);
			}
			context.put("flag", "success");
		}catch (EMPException ee) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "保存现金流量表失败！");
			throw ee;
		} catch(Exception e){
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "保存现金流量表失败！");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}


}
