package com.yucheng.cmis.biz01line.lmt.op.lmtqsdinfo;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateLmtQsdInfoRecordOp extends CMISOperation {
	

	private final String modelId = "LmtQsdInfo";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			//保存生效日期和时间
			String openDay = (String)context.getDataValue(CMISConstance.OPENDAY);
			String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());//精确到毫秒
			kColl.setDataValue("inure_date", openDay);
			kColl.setDataValue("inure_time", openDay+nowDate.substring(10, 23));
			
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String,String> idMap = new HashMap<String,String>();
			idMap.put("serno",(String)kColl.getDataValue("serno"));
			idMap.put("org_limit_code",(String)kColl.getDataValue("org_limit_code"));
			KeyedCollection qsdKcoll = dao.queryDetail(modelId, idMap, connection);
			if(qsdKcoll!=null&&qsdKcoll.getDataValue("serno")!=null&&!"".equals(qsdKcoll.getDataValue("serno"))){
				int count=dao.update(kColl, connection);
				if(count!=1){
					throw new EMPException("Update Failed! Record Count: " + count);
				}
			}else{
				dao.insert(kColl, connection);
			}
			
			context.addDataField("flag", "success");
			context.addDataField("value", kColl.getDataValue("crd_amt"));
		}catch (EMPException ee) {
			ee.printStackTrace();
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
