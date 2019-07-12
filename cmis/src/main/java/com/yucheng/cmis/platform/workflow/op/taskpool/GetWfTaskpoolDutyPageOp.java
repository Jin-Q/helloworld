package com.yucheng.cmis.platform.workflow.op.taskpool;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.domains.SDuty;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>设置项目池关联岗位，获取所有岗位及已经关联的岗位</p>
 * @author liuhw
 *
 */
public class GetWfTaskpoolDutyPageOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		OrganizationServiceInterface orgMsi = null;
		try {
			String tpid = (String) context.getDataValue("tpid");
			if(tpid==null || "".equals(tpid.trim())) {
				throw new EMPException("参数[tpid]获取失败！");
			}
			connection = this.getConnection(context);
			orgMsi = (OrganizationServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationServices", "organization");
			//获取所有岗位
			List<SDuty> dutyList = orgMsi.getAllDutys(connection);
			//获取已经设置关联的岗位
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection icoll = dao.queryList("WfTaskpoolUser", "WHERE TPID='"+tpid+"' ", connection);
			List<SDuty> hasDutyList = new ArrayList<SDuty>();
			if(icoll!=null && icoll.size()>0) {
				for(Object o : icoll) {
					KeyedCollection kcoll = (KeyedCollection) o;
					SDuty duty = new SDuty();
					duty.setDutyno((String)kcoll.getDataValue("userid"));
					hasDutyList.add(duty);
				}
			}
			context.put("AllDutyList", dutyList);
			context.put("HasDutyList", hasDutyList);
			
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
