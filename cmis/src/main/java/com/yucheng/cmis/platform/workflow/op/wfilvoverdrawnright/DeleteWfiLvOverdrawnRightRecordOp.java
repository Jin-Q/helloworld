package com.yucheng.cmis.platform.workflow.op.wfilvoverdrawnright;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
/**
 * 
*@author wangj
*@time 2015-5-14
*@description TODO 需求编号：【XD141222087】法人账户透支需求变更
*@version v1.0
*
 */
public class DeleteWfiLvOverdrawnRightRecordOp extends CMISOperation {

	private final String modelId = "WfiLvOverdrawnRight";
	
	private final String pk_id_name = "pk_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String pk_id_value = null;
			try {
				pk_id_value = (String)context.getDataValue(pk_id_name);
			} catch (Exception e) {}
			if(pk_id_value == null || pk_id_value.length() == 0){
				context.addDataField("flag","fild");
				context.addDataField("msg","删除授信申请信息失败，错误描述，传入主键字段为空！");
				return "0";
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, pk_id_value, connection);
			if(count!=1){
				context.addDataField("flag","fild");
				context.addDataField("msg","删除透支额度控制信息失败，错误描述：未找到对应的记录！");
				return "0";
			}
			context.addDataField("flag", PUBConstant.SUCCESS);
			context.addDataField("msg", PUBConstant.SUCCESS);
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
}
