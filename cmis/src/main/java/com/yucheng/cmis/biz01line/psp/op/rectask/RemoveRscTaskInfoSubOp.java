package com.yucheng.cmis.biz01line.psp.op.rectask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class RemoveRscTaskInfoSubOp  extends CMISOperation {
	private final String modelId = "RscTaskInfoSub";
	/**
	 * <p>
	 * <h2>简述</h2>
	 *    <ol>删除记录</ol>
	 * <h2>功能描述</h2>
	 * 		<ol>无</ol>
	 * </p>
	* @param context EMP上下文
	 * @return String
	 * @throws EMPException
	 */

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			//通过组件服务实例化业务组件
			//删除数据
			TableModelDAO dao = this.getTableModelDAO(context);
			String serno = (String)queryData.getDataValue("serno");
			int count=dao.deleteByPk(modelId,serno,connection);
			 
			//将处理结果放入Context中以便前端获取
			if(count==1){
				context.addDataField("flag", "success");
			} else {
				context.addDataField("flag", "failed");
			}
		} catch (EMPException ee) {
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
