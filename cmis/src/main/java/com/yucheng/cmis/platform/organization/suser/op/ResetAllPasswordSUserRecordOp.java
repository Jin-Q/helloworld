package com.yucheng.cmis.platform.organization.suser.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.MD5;
import com.yucheng.cmis.util.TableModelUtil;

public class ResetAllPasswordSUserRecordOp extends CMISOperation {
	
	//所要操作的表模型
	private final String modelId = "SUser";
	
	//所要操作的表模型的主键
	private final String actorno_name = "actorno";
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String queryCondition="";
		String tmp_currentUserId = (String)(context.getDataValue("currentUserId"));
		EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
				tmp_currentUserId+"于"+new Date(System.currentTimeMillis())+"开始批量重置密码的操作！" );
		try{
			connection = this.getConnection(context);
		
		//获得查询的过滤数据
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			//获取查询 sql
			try{
				
				queryCondition = (String) context.getDataValue("queryCondition");
				queryCondition=" WHERE  "+queryCondition+" ";
				
			}catch(EMPException e){
				
			}
			
			//获得查询条件，交集、精确查询，忽略空值
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+queryCondition+"order by actorno desc";
			
			String [] conCheck=conditionStr.split("WHERE");
			
			if(conCheck.length==3){
				
				conditionStr=conCheck[0]+" WHERE "+conCheck[1]+" and "+conCheck[2];
			}
			
			
			
			//获取可以分页的OracleDao对象
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			/*List list = new ArrayList();
			list.add("actorno");
			list.add("actorname");
			list.add("telnum");
			list.add("idcardno");
			list.add("password");*/
			IndexedCollection iColl = dao.queryList(modelId ,conditionStr,connection);
			iColl.setName(iColl.getName()+"List");
			
			String actorno_value = null;
			KeyedCollection kColl=null;
			for(int i=0;i<iColl.size();i++){
				//获得查询需要的主键信息
				kColl=(KeyedCollection)iColl.get(i);
			
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
						EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
						"用户："+kColl.getDataValue("actorno")+"密码重置失败！操作影响了"+count+"条记录" );
						//throw new EMPException("重置失败！操作影响了"+count+"条记录");
					}
				 
				//this.putDataElement2Context(kColl, context);
				//System.err.println(">>>>\n" + kColl.toString()); 
			}	

			context.addDataField("flag", "success");
			context.addDataField("msg", "");
		}catch (EMPException ee) {
			context.addDataField("flag", "failure");
			context.addDataField("msg", "批量重置密码的操作失败！失败原因："+ee.getMessage());
			throw ee;
		} catch(Exception e){
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
					tmp_currentUserId+"于"+new Date(System.currentTimeMillis())+"批量重置密码的操作失败！" );
			throw new EMPException(e);
		} finally {
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,
					tmp_currentUserId+"于"+new Date(System.currentTimeMillis())+"结束批量重置密码的操作！" );
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
		

}
