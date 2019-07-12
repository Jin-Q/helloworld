package com.yucheng.cmis.platform.organization.sduty.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.initializer.OrganizationInitializer;

public class AddSDutyRecordOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SDuty";
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			String dutyno_value = (String)kColl.getDataValue("dutyno");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			
			//校验该岗位是否已经存在于数据库中  at 2010-11-2 10:32:10
			KeyedCollection existKColl = dao.queryDetail(modelId, dutyno_value, connection);
			if(existKColl!=null && existKColl.containsKey("dutyno") && existKColl.getDataValue("dutyno")!=null){
				flag = "exist";
			}
			//新增一条记录
			else {
				dao.insert(kColl, connection);
				
				String dutyname = (String)kColl.getDataValue("dutyname");
				OrganizationInitializer.addAndUpdateDutyMapInfo(dutyno_value, dutyname);
				flag = "sucess";
			}
			
			context.addDataField("flag", flag);
			context.addDataField("dutyno", dutyno_value);
			
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
