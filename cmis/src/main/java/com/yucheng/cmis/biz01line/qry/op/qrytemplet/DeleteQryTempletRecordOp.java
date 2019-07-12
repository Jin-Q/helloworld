package com.yucheng.cmis.biz01line.qry.op.qrytemplet;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.qry.component.QryComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.QryPubConstant;

public class DeleteQryTempletRecordOp extends CMISOperation {
	
	private final String modelId = "QryTemplet";

	private final String temp_no_name = "temp_no";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String temp_no_value = null;
			try {
				temp_no_value = (String)context.getDataValue(temp_no_name);
			} catch (Exception e) {}
			if(temp_no_value == null || temp_no_value.length() == 0){
				context.addDataField("flag","fild");
				context.addDataField("msg","删除查询模板失败，错误描述，传入主键字段[查询模板编号]为空！");
				return "0";
			}
			/**
			 * 调用component删除子表内容
			 */
			QryComponent qryComponent = (QryComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(QryPubConstant.QRYCOMPONENT,context, connection);

			qryComponent.deleteQryStaffByTempNo(temp_no_value);
			/**
			 * 删除主表内容
			 */
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, temp_no_value, connection);
			if(count!=1){
				context.addDataField("flag","fild");
				context.addDataField("msg","删除查询模板失败，错误描述：未找到对应的配置信息！");
				return "0";
			}
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "success");
		}catch(Exception e){
			context.addDataField("flag","fild");
			context.addDataField("msg","删除查询模板失败，错误描述："+e.getMessage());
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
