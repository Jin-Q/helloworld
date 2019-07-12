package com.yucheng.cmis.platform.riskmanage.op.prdpreventrisk.prdpvriskscene;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.riskmanage.component.RiskManageComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeletePrdPreventRiskPrdPvRiskSceneRecordOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String prevent_id = (String)context.getDataValue("prevent_id");
			String wfSign = (String)context.getDataValue("wfid");//此处为流程标识
			
			if(prevent_id == null || prevent_id.trim().length() == 0){
				throw new EMPException("获取方案编号失败！");
			}
			if(wfSign == null || wfSign.trim().length() == 0){
				throw new EMPException("获取流程标识失败！");
			}
			
			/** 通过方案ID，流程标识删除关联表中的关联数据 */
			RiskManageComponent riskManageComponent = (RiskManageComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("RiskManage", context, connection);
			riskManageComponent.doDeleteSqlExecute("delete from prd_pv_risk_scene where prevent_id='"+prevent_id+"' and wfid='"+wfSign+"'", connection);
			
			context.addDataField("flag", "success");
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", "failed");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
