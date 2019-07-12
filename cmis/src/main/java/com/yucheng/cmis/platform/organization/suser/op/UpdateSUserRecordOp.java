package com.yucheng.cmis.platform.organization.suser.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.MD5;

public class UpdateSUserRecordOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SUser";
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			//更新一条指定的记录
			
			KeyedCollection kColl = null;
			String actorno = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			 
				
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			
			
			
			actorno = (String)kColl.getDataValue("actorno");
			//处理加密
			String password=(String)kColl.getDataValue("password");
			String user=(String)kColl.getDataValue("actorno");
			MD5.md5Str2Kcol(kColl,user,password);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			
			//取得原始表数据
//			KeyedCollection kcUserinfoOld = dao.queryDetail(modelId,actorno , connection);
			
			//修改指定记录
			int count=dao.update(kColl, connection);
			if(count!=1){
				context.addDataField("flag", "failure");
				context.addDataField("msg", "修改数据失败！操作影响了"+count+"条记录");
				return null;
//			} else {
//				//更新SDeptuser表
//				if(kcUserinfoOld != null){
//					//删除原有配置
//					HashMap para = new HashMap();
//					para.put("actorno", kcUserinfoOld.getDataValue("actorno"));
//					para.put("organno", kcUserinfoOld.getDataValue("orgid"));
//					dao.deleteByPks("SDeptuser", para, connection);
//					
//			        //插入新数据
//					KeyedCollection kcDeptUser = new KeyedCollection("SDeptuser");
//					kcDeptUser.addDataField("organno", kColl.getDataValue("orgid"));
//					kcDeptUser.addDataField("actorno", kColl.getDataValue("actorno"));
//					kcDeptUser.addDataField("state", "1");
//					dao.insert(kcDeptUser, connection);					
//				} else {
//					context.addDataField("flag", "failure");
//					context.addDataField("msg", "用户号" + actorno + "不存在");
//					return null;
//				}
//				
//				String actorName = (String)kColl.getDataValue("actorname");
//				OrganizationInitializer.addAndUpdateUserMapInfo(actorno, actorName);
			}
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
