package com.yucheng.cmis.biz01line.qry.op.qrytemplet;

/**
 * 异步查询查询分析下参数个数
 */

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.qry.component.QryExeComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.QryPubConstant;

public class CheckParamCount extends CMISOperation {
	
	private final String temp_no_name = "temp_no";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String temp_no_value = null;
			try {
				//获取查询模版编号
				temp_no_value = (String)context.getDataValue(temp_no_name);
			} catch (Exception e) {}

			if(temp_no_value == null || temp_no_value.length() == 0){
				context.addDataField("flag","fild");
				context.addDataField("msg","异步查询查询分析下参数个数，错误描述：传入主键字段[查询模板编号]为空！");
				return "0";
			}
			
			QryExeComponent qryExeComponent = (QryExeComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(QryPubConstant.QRYEXECOMPONENT, context, connection);
			String count = qryExeComponent.selectParamCountByTempno(temp_no_value);
			
			if(null!=count && !"".equals(count)){
				int num = Integer.parseInt(count);
				if(num>0){
					context.addDataField("res", "Y");
				}else{
					context.addDataField("res", "N");
				}
			}else{
				context.addDataField("res", "N");
			}
			context.addDataField("flag","success");
			context.addDataField("msg", "success");
		}catch(Exception e){
			context.addDataField("res", "N");
			context.addDataField("flag","fild");
			context.addDataField("msg","异步查询查询分析下参数个数，错误描述："+e.getCause().getCause().getMessage());
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
