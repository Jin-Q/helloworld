package com.yucheng.cmis.biz01line.qry.op.qrytemplet;

/**
 * 生成查询静态页面,总共会生成 四个文件
 * 1，查询条件页面2，查询结果显示字段 3，查询结果排序，4，js页面
 * 在模版staticQuery页面里嵌入
 */

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.qry.component.QryGenPageComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.QryPubConstant;

public class GenPageOp extends CMISOperation {
	
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
				context.addDataField("msg","生成查询页面错误，错误描述：传入主键字段[查询模板编号]为空！");
				return "0";
			}
			
			//根据查询模版编号 ，生成 相应的查询JSP页面。
			QryGenPageComponent qryGenPageComponent = (QryGenPageComponent)CMISComponentFactory
			 .getComponentFactoryInstance().getComponentInstance(QryPubConstant.QRYGENPAGECOMPONENT,context, connection);
			qryGenPageComponent.generateAnalyseHtml(temp_no_value);
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "success");
		}catch(Exception e){
			context.addDataField("flag","fild");
			context.addDataField("msg","生成查询页面错误，错误描述："+e.getMessage());
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
