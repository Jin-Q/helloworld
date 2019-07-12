package com.yucheng.cmis.biz01line.iqp.op.iqpbatchmng;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpBatchMngRecordOp extends CMISOperation {
	private final String modelId = "IqpBatchMng";
	private final String relModel = "IqpBatchBillRel";
	private final String batch_no_name = "batch_no";
	private final String inModel = "IqpBillIncome";
	private final String pintDetailModel = "IqpBillPintDetail";
	public String doExecute(Context context) throws EMPException {
		/** 删除操作删除批次表iqp_batch_mng以及批次和票据关联表iqp_batch_bill_rel中信息  
		 * 补充：批次删除时，批次下的票据利息计算记录也全部要删除。add by MQ 2013-08-16
		 * */
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			String batch_no_value = null;
			try {
				batch_no_value = (String)context.getDataValue(batch_no_name);
			} catch (Exception e) {}
			if(batch_no_value == null || batch_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+batch_no_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			
			//1、删除批次下利息计算信息
			IndexedCollection biIColl = dao.queryList(relModel, " where batch_no='"+batch_no_value+"'", connection);
			int count;
			String porderno = null;
			Map<String,String> billincomeMap =  new HashMap<String,String>();
			billincomeMap.put("batch_no", batch_no_value);
			if(biIColl != null && biIColl.size() > 0){
				for(int i=0;i<biIColl.size();i++){
					KeyedCollection kc = (KeyedCollection)biIColl.get(i);
					porderno = (String)kc.getDataValue("porder_no");
					billincomeMap.put("porder_no", porderno);
					count = dao.deleteAllByPks(inModel, billincomeMap, connection);
					if(count!=1 && count!=0){
						throw new EMPException("Remove Failed! Records :"+count);
					}
					
					//2、删除批次与票据关系表
				    count = dao.deleteAllByPks(relModel, billincomeMap, connection);
					if(count!=1 && count!=0){
						throw new EMPException("Remove Failed! Records :"+count);
					}
					
					//4、先查询付息信息，再删除
					String condition = " where batch_no = '"+batch_no_value+"' and porder_no = '"+porderno+"'";
					IndexedCollection pintIColl = dao.queryList(pintDetailModel, condition, connection);
					for(int j=0;j<pintIColl.size();j++){
						KeyedCollection pintKColl = (KeyedCollection) pintIColl.get(j);
						String pk = (String) pintKColl.getDataValue("pk");
						dao.deleteByPk(pintDetailModel, pk, connection);
					}
				}
			}
			
			//3、删除批次包
			count=dao.deleteByPk(modelId, batch_no_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			context.addDataField("flag","success");
		}catch (EMPException ee) {
			context.addDataField("flag","error");
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
