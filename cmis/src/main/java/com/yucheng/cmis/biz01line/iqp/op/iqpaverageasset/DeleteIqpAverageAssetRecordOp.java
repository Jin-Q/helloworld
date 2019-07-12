package com.yucheng.cmis.biz01line.iqp.op.iqpaverageasset;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteIqpAverageAssetRecordOp extends CMISOperation {

	private final String modelId = "IqpAverageAsset";
	private final String modelIdIqpAssetRel = "IqpAssetRel";//资产清单关联表
	

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
            
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			String bill_no = (String)kColl.getDataValue("bill_no");
			//判断是否被资产包引入
			String condition = "where bill_no='"+bill_no+"' and asset_no in(select asset_no from iqp_asset where status !='03')";
			IndexedCollection iColl = dao.queryList(modelIdIqpAssetRel, condition, connection);
			if(iColl.size()>0){
				context.addDataField("flag", "error");
				context.addDataField("msg", "该资产已被引用，不能作废!");
			}else{
				kColl.put("average_status", "2");
				int count = dao.update(kColl, connection);
				if(count!=1){
					throw new EMPException("update Failed! Records :"+count);
				}
				context.addDataField("flag", "success");
				context.addDataField("msg", "success");
			}
		}catch (EMPException ee) {
			context.addDataField("flag", "error");
			context.addDataField("msg", ee.getMessage());
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
