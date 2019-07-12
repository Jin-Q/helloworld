package com.yucheng.cmis.platform.organization.suser.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.initializer.OrganizationInitializer;
import com.yucheng.cmis.pub.MD5;

public class AddSUserRecordOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SUser";
	
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
			//处理加密
			//String password=(String)kColl.getDataValue("password");
			String user=(String)kColl.getDataValue("actorno");
			//String password=user;
			//MD5 m = new MD5(); 
			// password=m.getMD5ofStr(user+password);
			// password=m.getMD5ofStr(user);
			//kColl.setDataValue("password", password);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String actorno_value = (String)kColl.getDataValue("actorno");
			String actorno_name = (String)kColl.getDataValue("actorname");
			
			//检验等新增用户是否已经存在于数据库 at 2010-11-2 11:06:51
			KeyedCollection existKColl = dao.queryDetail(modelId, actorno_value, connection);
			if(existKColl!=null && existKColl.containsKey("actorno") && existKColl.getDataValue("actorno") != null){
				flag = "exist";
			}
			else{
				MD5.md5Str2Kcol(kColl,user);
				//新增一条记录
				dao.insert(kColl, connection);
				
				OrganizationInitializer.addAndUpdateUserMapInfo(actorno_value, actorno_name);
				
//				/** 插入用户机构表中 */
//				if(kColl != null && kColl.getDataValue("orgid") != null && kColl.getDataValue("actorno") != null){
//					KeyedCollection kcDeptUser = new KeyedCollection("SDeptuser");
//					kcDeptUser.addDataField("organno", kColl.getDataValue("orgid"));
//					kcDeptUser.addDataField("actorno", kColl.getDataValue("actorno"));
//					kcDeptUser.addDataField("state", "1");
//					dao.insert(kcDeptUser, connection);
//					
//					flag = "sucess";
//				} else {
//					throw new EMPJDBCException("用户基本数据不完整（缺所属机构号或用户编号）");
//				}
				flag = "sucess";
			}
			context.addDataField("flag", flag);
			context.addDataField("actorno", actorno_value); 
			
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
