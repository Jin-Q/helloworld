package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.msi.OrganizationCacheServiceInterface;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.platform.workflow.domain.WfiBizVarRecordVO;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>获取流程中业务审批变更，用于前台显示</p>
 * @author liuhw
 *
 */
public class GetWfiBizVarHisOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		String instanceId = null;
		instanceId = (String) context.get(WorkFlowConstance.ATTR_INSTANCEID);
		if(instanceId==null || instanceId.trim().length()<=0) {
			throw new EMPException("获取参数流程实例号【instacneId】失败！");
		}
		WFIComponent wfiComponent = null;
		OrganizationCacheServiceInterface orgCacheMsi = null;
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			wfiComponent = (WFIComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, connection);
			List<WfiBizVarRecordVO> list = wfiComponent.queryWfiBizVarByNode(instanceId);
			if(list!=null && list.size()>0) {
				orgCacheMsi = (OrganizationCacheServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationCacheServices", "organization");
				//将用户ID转换为名称
				for(WfiBizVarRecordVO vo : list) {
					String userId = vo.getInputId();
					String userName = orgCacheMsi.getUserName(userId);
					vo.setInputName(userName);
				}
			}
			
			context.put("wfiBizVarList", list);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		
		return null;
	}

}