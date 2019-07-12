package com.yucheng.cmis.platform.organization.suser.op;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.MD5;

public class ResetPasswordSUserRecordOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SUser";
	
	//所要操作的表模型的主键
	private final String actorno_name = "actorno";
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		/*	//查询特定一条记录
			if(this.updateCheck){
				//对修改进行记录级权限判断
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}*/
			
			//获得查询需要的主键信息
			String actorno_value = null;
			try {
				actorno_value = (String)context.getDataValue(actorno_name);
			} catch (Exception e) {}
			if(actorno_value == null || actorno_value.length() == 0){
				context.addDataField("flag", "failure");
				context.addDataField("msg", "客户号["+actorno_name+"] 不存在!");
				return null;
			}

			
			//查询列表信息
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, actorno_value, connection);
			
			//处理加密
			//String password=(String)kColl.getDataValue("password");
			String user=(String)kColl.getDataValue("actorno");
			/*String password=user;
			 MD5 m = new MD5();
			 password=m.getMD5ofStr(user+password);
			 kColl.setDataValue("password", password);*/
			MD5.md5Str2Kcol(kColl,user);
			 int count=dao.update(kColl, connection);
				if(count!=1){
					context.addDataField("flag", "failure");
					context.addDataField("msg", "重置失败！操作影响了"+count+"条记录");
					return null;
				}
			 
			//this.putDataElement2Context(kColl, context);
			System.err.println(">>>>\n" + kColl.toString()); 
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "");
		}catch (EMPException ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "重置失败！失败原因："+ee.getMessage());
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
