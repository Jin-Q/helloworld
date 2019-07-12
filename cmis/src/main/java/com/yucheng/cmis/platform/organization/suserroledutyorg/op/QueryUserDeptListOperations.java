package com.yucheng.cmis.platform.organization.suserroledutyorg.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryUserDeptListOperations extends CMISOperation{
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String actorno = (String)context.getDataValue("actorno");
			String conditionStr = "";
			//获取可以分页的OracleDao对象
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			//首先查询所有的岗位信息
			List list = new ArrayList();
			list.add("organno");
			list.add("organname");
			/**modified  by lisj 2014-12-18 新增可授权机构排序功能，于2014-12-18 上线  begin**/
			IndexedCollection iColl = dao.queryList("SOrg",list, "order by organno asc", connection);
			/**modified  by lisj 2014-12-18 新增可授权机构排序功能，于2014-12-18 上线  end**/
			iColl.setName("SDeptList");//SDeptList
			this.putDataElement2Context(iColl, context);
			//接着查询当前用户所拥有的岗位
			conditionStr = " where actorno='"+actorno+"'";
			list = new ArrayList();
			list.add("organno");
			iColl = dao.queryList("SDeptuser",list ,conditionStr,connection);
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
