package com.yucheng.cmis.biz01line.psp.op.psppropertyanaly;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddPspPropertyAnalyRecordOp extends CMISOperation {
	
	private final String modelId = "PspPropertyAnaly";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String returnFlag = "";
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			String property_type = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			property_type = (String)kColl.getDataValue("property_type");//资产类型
			if("11".equals(property_type)||"12".equals(property_type)||"13".equals(property_type)){//房地产
				returnFlag = "realPro";
			}else if("20".equals(property_type)){//设备及器材
				returnFlag = "equip";
			}else if("30".equals(property_type)){//交通工具
				returnFlag = "traffic";
			}else if("40".equals(property_type)){//商标使用权
				returnFlag = "logo";
			}else if("50".equals(property_type)){//专利使用权
				returnFlag = "patent";
			}else if("60".equals(property_type)){//股权
				returnFlag = "stock";
			}else if("70".equals(property_type)){//海域使用权
				returnFlag = "sea";
			}else if("80".equals(property_type)){//林权
				returnFlag = "forest";
			}
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return returnFlag;
	}
}
