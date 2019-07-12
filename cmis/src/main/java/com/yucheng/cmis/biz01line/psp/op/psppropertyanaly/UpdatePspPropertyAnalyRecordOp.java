package com.yucheng.cmis.biz01line.psp.op.psppropertyanaly;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdatePspPropertyAnalyRecordOp extends CMISOperation {
	
	private final String modelId = "PspPropertyAnaly";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String modelIdSub = null;
			String property_type = null;
			KeyedCollection kColl = null;
			KeyedCollection kCollSub = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			property_type = (String)kColl.getDataValue("property_type");
			if("11".equals(property_type)||"12".equals(property_type)||"13".equals(property_type)){//房地产
				modelIdSub = "PspPropertyRealpro";
			}else if("20".equals(property_type)){//设备及器材
				modelIdSub = "PspPropertyEquip";
			}else if("30".equals(property_type)){//交通工具
				modelIdSub = "PspPropertyTraffic";
			}else if("40".equals(property_type)){//商标使用权
				modelIdSub = "PspPropertyLogo";
			}else if("50".equals(property_type)){//专利使用权
				modelIdSub = "PspPropertyPatent";
			}else if("60".equals(property_type)){//股权
				modelIdSub = "PspPropertyStockright";
			}else if("70".equals(property_type)){//海域使用权
				modelIdSub = "PspPropertySearight";
			}else if("80".equals(property_type)){//林权
				modelIdSub = "PspPropertyForestright";
			}
			
			try {
				kCollSub = (KeyedCollection)context.getDataElement(modelIdSub);
			} catch (Exception e) {}
			if(kCollSub == null || kCollSub.size() == 0)
				throw new EMPJDBCException("The values to update["+modelIdSub+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.update(kColl, connection);
			count=dao.update(kCollSub, connection);
			if(count!=1){
				throw new EMPException("Update Failed! Record Count: " + count);
			}

			context.addDataField("flag", "success");
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
