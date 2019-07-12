package com.yucheng.cmis.platform.organization.suser.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.initializer.OrganizationInitializer;

public class DeleteSUserRecordOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SUser";
	private final String modelId_SDutyuser = "SDutyuser";
	private final String modelId_SRoleuser = "SRoleuser";
	private final String modelId_SDeptuser = "SDeptuser";
	//所要操作的表模型的主键
	private final String actorno_name = "actorno";
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			//删除一条特定的记录


			//获得删除需要的主键信息
			String actorno_value = null;
			try {
				actorno_value = (String)context.getDataValue(actorno_name);
			} catch (Exception e) {}
			if(actorno_value == null || actorno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+actorno_name+"] cannot be null!");
				

			//删除指定记录
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, actorno_value, connection);
			if(count!=1){
				context.addDataField("flag", "failure");
				context.addDataField("msg", "删除数据失败！操作影响了"+count+"条记录");
				return null;
			}
			IndexedCollection iColl = dao.queryList(modelId_SRoleuser, " where actorno='"+actorno_value+"' ", connection);
			Iterator it = iColl.iterator();
			while (it.hasNext()) {
			    KeyedCollection kColl = (KeyedCollection)it.next();
			    Map map = new HashMap();
			    map.put("roleno", (String)kColl.getDataValue("roleno"));
			    map.put("actorno", (String)kColl.getDataValue("actorno"));
                            dao.deleteByPks(modelId_SRoleuser, map, connection);
			}
			iColl = dao.queryList(modelId_SDutyuser, " where actorno='"+actorno_value+"' ", connection);
                        it = iColl.iterator();
                        while (it.hasNext()) {
                            KeyedCollection kColl = (KeyedCollection)it.next();
                            Map map = new HashMap();
                            map.put("dutyno", (String)kColl.getDataValue("dutyno"));
                            map.put("actorno", (String)kColl.getDataValue("actorno"));
                            dao.deleteByPks(modelId_SDutyuser, map, connection);
                        }
                        iColl = dao.queryList(modelId_SDeptuser, " where actorno='"+actorno_value+"' ", connection);
                        it = iColl.iterator();
                        while (it.hasNext()) {
                            KeyedCollection kColl = (KeyedCollection)it.next();
                            Map map = new HashMap();
                            map.put("organno", (String)kColl.getDataValue("organno"));
                            map.put("actorno", (String)kColl.getDataValue("actorno"));
                            dao.deleteByPks(modelId_SDeptuser, map, connection);
                        }
                        
          OrganizationInitializer.removeUserMapInfo(actorno_value);
          
          	context.addDataField("flag", "success");
			context.addDataField("msg", "");
		}catch (EMPException ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "删除失败！失败原因："+ee.getMessage());
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
