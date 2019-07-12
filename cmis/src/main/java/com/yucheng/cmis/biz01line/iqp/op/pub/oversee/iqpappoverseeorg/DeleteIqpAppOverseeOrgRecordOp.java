package com.yucheng.cmis.biz01line.iqp.op.pub.oversee.iqpappoverseeorg;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.ccr.component.CcrComponent;
import com.yucheng.cmis.biz01line.iqp.component.OverseeUnderstoreComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CcrPubConstant;
import com.yucheng.cmis.pub.PUBConstant;

public class DeleteIqpAppOverseeOrgRecordOp extends CMISOperation {

	private final String modelId = "IqpAppOverseeOrg";
	
	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			
			OverseeUnderstoreComponent OverseeUnderstoreComponent = (OverseeUnderstoreComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("OverseeUnderstoreComponent", context, connection);
			//表模型和键值对的过滤条件
			Map<String,String> conditionFields = new HashMap<String,String>();
			conditionFields.put("serno", serno_value);
			
			//根据表模型和键值对的过滤条件删除相同监管机构下的下属仓库
			OverseeUnderstoreComponent.deleteIqpOverseeUnderstore("IqpOverseeUnderstore", conditionFields);
			
			//根据表模型和键值对的过滤条件删除相同监管机构下的前五大客户
			OverseeUnderstoreComponent.deleteIqpOverseeUnderstore("IqpOverseeCusinfo", conditionFields);
			
			//根据表模型和键值对的过滤条件删除相同监管机构下的主要管理人员
			OverseeUnderstoreComponent.deleteIqpOverseeUnderstore("IqpOverseeManager", conditionFields);
			
			//根据表模型和键值对的过滤条件删除相同监管机构下的公司主要资产及其他核心资产
			OverseeUnderstoreComponent.deleteIqpOverseeUnderstore("IqpOverseeAssent", conditionFields);
			
			//删除评级表
			CcrComponent ccrComponent = (CcrComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(CcrPubConstant.CCR_COMPONENT,context, connection);
			ccrComponent.deleteApp(serno_value);
			
			int count=dao.deleteByPk(modelId, serno_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			
			context.addDataField("flag", PUBConstant.SUCCESS);
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
