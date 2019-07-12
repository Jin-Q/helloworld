package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.domains.SUser;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.ComponentHelper;

/**
 * <p>获取机构下的用户</p>
 * @author liuhw
 *
 */
public class GetUserByOrg4WFOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try {
			String getyhOrgid = (String) context.getDataValue("getyhOrgid");
			connection = this.getConnection(context);
			OrganizationServiceInterface orgMsi = (OrganizationServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationServices", "organization");
			List<SUser> list = orgMsi.getUsersByOrgId(getyhOrgid, connection);
			ComponentHelper helper = new ComponentHelper();
			IndexedCollection icoll = helper.domain2icol(list, "SUser");
			icoll.setName("users");
			this.putDataElement2Context(icoll, context);
			
		} catch (Exception e) {
			EMPLog.log("GetUserByOrgOp", EMPLog.ERROR, EMPLog.ERROR, "获取机构下的用户出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
