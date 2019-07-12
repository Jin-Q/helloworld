package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtintbankbatchlist;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeleteLmtIntbankBatchListRecordOp extends CMISOperation {
	
	private final String modelId = "LmtIntbankBatchList";

	private final String serno_name = "serno";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = "success";
		try{
			connection = this.getConnection(context);
			try{
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				//recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}catch (EMPException ee) {
				flag = "当前用户没有权限操作此记录!";
			}
			if("success".equals(flag)){
				String serno_value = null;
				try {
					serno_value = (String)context.getDataValue(serno_name);
				} catch (Exception e) {}
				if(serno_value == null || serno_value.length() == 0)
					throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
				TableModelDAO dao = this.getTableModelDAO(context);
	            KeyedCollection kColl = dao.queryFirst("LmtIntbankBatchList", null, "where serno='"+serno_value+"'", connection);
	            String batch_cus_no = (String)kColl.getDataValue("batch_cus_no");
	            Map<String,String> refFields = new HashMap<String,String>();
	            refFields.put("batch_cus_no", batch_cus_no);
				
				int count=dao.deleteByPk(modelId, serno_value, connection);
				if(count!=1){
					throw new EMPException("Remove Failed! Record Count: " + count);
				}
				//根据批量客户号删除关系表LmtBatchCorre中的数据
				LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
	            lmtComponent.deleteByField("LmtBatchCorre", refFields);
			}
			 
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			context.addDataField("flag", flag);
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
