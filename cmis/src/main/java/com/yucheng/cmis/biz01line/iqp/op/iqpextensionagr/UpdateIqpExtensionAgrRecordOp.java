package com.yucheng.cmis.biz01line.iqp.op.iqpextensionagr;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;

public class UpdateIqpExtensionAgrRecordOp extends CMISOperation {
	

	private final String modelId = "IqpExtensionAgr";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = new KeyedCollection();			
			
			if(context.containsKey("action")){	//作废与撤销
				kColl = dao.queryDetail(modelId,context.getDataValue("serno").toString() , connection);
				String agr_no = kColl.getDataValue("agr_no").toString();
				IndexedCollection iCollPvp = dao.queryList("IqpExtensionPvp", "where agr_no='"+agr_no+"'", connection);
				if( iCollPvp.size() > 0 && iCollPvp != null ){
					context.addDataField("flag", PUBConstant.FAIL);
					return "0";
				}
				kColl.setDataValue("status", context.getDataValue("action"));
			}else{	//合同签订
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
				try {
					kColl = (KeyedCollection)context.getDataElement(modelId);
				} catch (Exception e) {}
				if(kColl == null || kColl.size() == 0)
					throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
				
				/*** 合同签订时写入签订日期，更改协议状态 ***/
				kColl.setDataValue("sign_date", context.getDataValue("OPENDAY"));
				kColl.setDataValue("status", "200");
			}
			
			int count=dao.update(kColl, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}

			context.addDataField("flag", PUBConstant.SUCCESS);
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
