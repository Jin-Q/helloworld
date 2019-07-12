package com.yucheng.cmis.biz01line.iqp.op.iqpassetrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckBillNoForIqpAssetRelOp extends CMISOperation {


	private final String modelId = "IqpAssetRel";
	private final String asModelId = "IqpAsset";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String bill_no = (String) context.getDataValue("bill_no");
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			String condition = " where bill_no = '"+bill_no+"'";
			IndexedCollection iColl = dao.queryList(modelId, null, condition, connection);
			if(iColl.size()>0){
				KeyedCollection kColl = (KeyedCollection) iColl.get(0);
				String asset_no = (String) kColl.getDataValue("asset_no");
				KeyedCollection asKColl = dao.queryDetail(asModelId, asset_no, connection);
				String status = (String) asKColl.getDataValue("status");
				if(!status.equals("03")){
					context.addDataField("flag", "failed");
					context.addDataField("msg", "注意！该借据已存在未办结的资产包中，资产包编号【"+asset_no+"】！");
					return "0";
				}
			}
			
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		context.addDataField("flag", "success");
		context.addDataField("msg", "");
		return "0";
	}

}
