package com.yucheng.cmis.platform.organization.suserroledutyorg.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

/**
 * 查询用户所拥有的角色
 * @version 2014-03-18 可授予角色跟已授予角色不分页
 */
public class QueryUserRoleListOperations extends CMISOperation{
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String actorno = null;
			try {
				actorno = (String)context.getDataValue("actorno");
			} catch (Exception e) {}
			if(actorno == null || actorno.length() == 0)
				throw new EMPJDBCException("The value of pk[actorno] cannot be null!");
				
			String conditionStr = " where 1=1";
		
			//获取可以分页的OracleDao对象
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			//首先查询所有的角色信息
			List<String> list = new ArrayList<String>();
			list.add("roleno");
			list.add("rolename");
			/**modified  by lisj 2014-12-18 新增可授权角色排序功能，于2014-12-18 上线  begin**/
			IndexedCollection iColl = dao.queryList("SRole",list ,"order by rolename asc",connection);
			/**modified  by lisj 2014-12-18 新增可授权角色排序功能，于2014-12-18 上线  end**/
			iColl.setName(iColl.getName()+"List");//SRoleList
			this.putDataElement2Context(iColl, context);
			//接着查询当前用户所拥有的角色
			conditionStr = " where actorno='"+actorno+"'";
			list = new ArrayList<String>();
			list.add("roleno");
			iColl = dao.queryList("SRoleuser",list ,conditionStr,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			
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
	public void initialize() {}
}
