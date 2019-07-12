package com.yucheng.cmis.platform.organization.suserroledutyorg.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
//查询用户所拥有的岗位
public class QueryUserDutyListOperations extends CMISOperation{
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String actorno = (String)context.getDataValue("actorno");
			String conditionStr = " where 1=1";
			PageInfo pageInfo = new PageInfo();
		
			//获取可以分页的OracleDao对象
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			//首先查询所有的岗位信息
			List list = new ArrayList();
			list.add("dutyno");
			list.add("dutyname");
			/**modified  by lisj 2014-12-18 新增可授权岗位排序功能 ，于2014-12-18 上线 begin**/
			IndexedCollection iColl = dao.queryList("SDuty", list, "order by dutyname asc", connection);
			/**modified  by lisj 2014-12-18 新增可授权岗位排序功能，于2014-12-18 上线  end**/
			//IndexedCollection iColl = dao.queryList("SDuty",list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");//SDutyList
			this.putDataElement2Context(iColl, context);
			//接着查询当前用户所拥有的岗位
			conditionStr = " where actorno='"+actorno+"'";
			list = new ArrayList();
			list.add("dutyno");
			IndexedCollection iColl2 = dao.queryList("SDutyuser", list, conditionStr, connection);
			//iColl = dao.queryList("SDutyuser",list ,conditionStr,pageInfo,connection);
			iColl2.setName(iColl2.getName()+"List");
			this.putDataElement2Context(iColl2, context);
			
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
