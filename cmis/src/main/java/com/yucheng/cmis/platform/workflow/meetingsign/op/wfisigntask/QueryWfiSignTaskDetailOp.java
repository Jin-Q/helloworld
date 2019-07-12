package com.yucheng.cmis.platform.workflow.meetingsign.op.wfisigntask;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.msi.OrganizationCacheServiceInterface;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class QueryWfiSignTaskDetailOp extends CMISOperation {

	private final String modelId = "WfiSignTask";

	private final String st_task_id_name = "st_task_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String condition="";

			String st_task_id_value = null;
			try {
				st_task_id_value = (String)context.getDataValue(st_task_id_name);
			} catch (Exception e) {}
			if(st_task_id_value == null || st_task_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+st_task_id_name+"] cannot be null!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryAllDetail(modelId, st_task_id_value, connection);
			OrganizationCacheServiceInterface orgCacheMsi = (OrganizationCacheServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationCacheServices", "organization");
			orgCacheMsi.addUserName(kColl, new String[]{"st_exe_user"});
			orgCacheMsi.addOrgName(kColl, new String[]{"st_exe_org"});
			String signId = (String) kColl.getDataValue("st_config");
			KeyedCollection kcollConf = dao.queryAllDetail("WfiSignConf", signId, connection);
			String signName = (String) kcollConf.getDataValue("sign_name");
			kColl.put("st_config_displayname", signName);
			//处理申请信息url参数
			WFIComponent wfiComponent = (WFIComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, connection);
			String bizPage = wfiComponent.processURLParam((String)kColl.getDataValue("wfi_biz_page"), (String)kColl.getDataValue("wfi_instance_id"), null);
			kColl.setDataValue("wfi_biz_page", bizPage);
			
			this.putDataElement2Context(kColl, context);

			condition=" WHERE ST_TASK_ID='"+st_task_id_value+"'";
			IndexedCollection iColl_WfiSignVote = dao.queryList("WfiSignVote",condition, connection);
			orgCacheMsi.addUserName(iColl_WfiSignVote, new String[]{"sv_exe_user"});
			this.putDataElement2Context(iColl_WfiSignVote, context);
			
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
