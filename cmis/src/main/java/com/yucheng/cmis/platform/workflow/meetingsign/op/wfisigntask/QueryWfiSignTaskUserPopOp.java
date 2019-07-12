package com.yucheng.cmis.platform.workflow.meetingsign.op.wfisigntask;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.domains.SUser;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.ComponentHelper;

/**
 * 调用组织机构模块服务，根据节点配置的办理人（岗位）获取可选的会签成员
 * @author liuhw
 *
 */
public class QueryWfiSignTaskUserPopOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		String duty = (String) context.getDataValue("duty");
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			List<SUser> userList = new ArrayList<SUser>();
			OrganizationServiceInterface orgMsi = (OrganizationServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationServices", "organization");
			if(duty.equals(WorkFlowConstance.ALL_USER) || duty.equals("{alluser}")) { //传递参数丢失了$
				userList = orgMsi.getAllUsers(connection);
			} else {
				if(duty.indexOf(",")>=0){//配置多个岗位
					String dutys[] = duty.split(",");
					List<SUser> tmplist = new ArrayList<SUser>();
					for(int i=0;i<dutys.length;i++){
						tmplist = orgMsi.getUsersByDutyId(dutys[i], connection);
						userList.addAll(tmplist);
					}
				}else{//只有一个岗位
					userList = orgMsi.getUsersByDutyId(duty, connection);
				}
			}
			
			ComponentHelper helper = new ComponentHelper();
			IndexedCollection icoll = helper.domain2icol(userList, "SUser");
			icoll.setName("SUserList");
			this.putDataElement2Context(icoll, context);
			
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
