package com.yucheng.cmis.biz01line.iqp.op.iqpaccaccp.iqpaccpdetail;

import java.sql.Connection;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpAccAccpIqpAccpDetailRecordOp extends CMISOperation {
	
	private final String modelId = "IqpAccpDetail";
	private final String modelIdAcc = "IqpAccAccp";
	
	private final String pk1_name = "pk1";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String pk1_value = null;
			String serno = null;
				
			TableModelDAO dao = this.getTableModelDAO(context);
		
			//获得流水号
			serno = (String)context.getDataValue("serno");
			
			//得到前台传过来的多选记录
			IndexedCollection icoll = (IndexedCollection)context.getDataElement("IqpAccpDetailList");
			if(null==icoll && icoll.size()<1){
				throw new EMPJDBCException("未取得需要删除的银票明细记录！");
			}
			for (Iterator iterator = icoll.iterator(); iterator.hasNext();) {
				KeyedCollection kcoll = (KeyedCollection) iterator.next();
				pk1_value = (String)kcoll.getDataValue(pk1_name);
				
				int count=dao.deleteByPk(modelId, pk1_value, connection);
				if(count!=1){
					throw new EMPException("Remove Failed! Record Count: " + count);
				}
			}
			//pk1_value = (String)context.getDataValue(pk1_name);
				
			String condition = "where serno='"+serno+"'";
			
			KeyedCollection kColl = dao.queryDetail(modelIdAcc, serno, connection);
			
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			kColl.setDataValue("bill_qty", iColl.size());
			dao.update(kColl, connection); 
			context.addDataField("flag", "success");
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
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
