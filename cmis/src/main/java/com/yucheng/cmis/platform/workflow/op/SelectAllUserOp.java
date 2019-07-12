package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>节点办理人选择所有用户</p>
 * @author liuhw
 *
 */
public class SelectAllUserOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		String orgId = null;
		try {
			orgId = (String) context.getDataValue("orgIdTmp");
		} catch (Exception e) {
		}
		
		try {
			connection = this.getConnection(context);
			OrganizationServiceInterface orgMsi = (OrganizationServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationServices", "organization");
			SOrg org = null;
			if(orgId!=null && !orgId.equals("")) {
				org = orgMsi.getOrgByOrgId(orgId, connection);
			} else {
				org = orgMsi.getRootOrg(connection);
			}
			context.put("rootOrg", org);
		} catch (Exception e) {
			EMPLog.log("SelectAllUserOp", EMPLog.ERROR, EMPLog.ERROR, "处理节点办理人选择所有用户出错！异常信息为："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
