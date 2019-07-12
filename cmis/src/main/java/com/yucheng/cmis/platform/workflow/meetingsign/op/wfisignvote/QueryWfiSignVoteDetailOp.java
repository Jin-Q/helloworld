package com.yucheng.cmis.platform.workflow.meetingsign.op.wfisignvote;

import java.sql.Connection;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.msi.OrganizationCacheServiceInterface;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class QueryWfiSignVoteDetailOp  extends CMISOperation {
	
	private final String modelId = "WfiSignVote";
	private final String modelId1 = "WfiSignTask";

	private final String sv_vote_id_name = "sv_vote_id";
	
	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			String sv_vote_id_value = null;
			try {
				sv_vote_id_value = (String)context.getDataValue(sv_vote_id_name);
			} catch (Exception e) {}
			if(sv_vote_id_value == null || sv_vote_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+sv_vote_id_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, sv_vote_id_value, connection);
			//根据投票id得到任务id
			String stTaskId=(String)kColl.getDataValue("st_task_id");
			KeyedCollection kColl1=dao.queryDetail(modelId1, stTaskId, connection);
			String wfi_biz_page=(String)kColl1.getDataValue("wfi_biz_page");
            String instanceid=(String)kColl1.getDataValue("wfi_instance_id");
            if(wfi_biz_page!=null&&!wfi_biz_page.equals("")){
            	// 参数处理。参数形式为pkVal=${pk_value}，pkVal为url中的参数名称，pk_value则为context中某个属性值或者接入表wfi_join的某个字段名称
                WFIComponent wfiComponent = (WFIComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, connection);
    			wfi_biz_page = wfiComponent.processURLParam(wfi_biz_page, instanceid, null);
                kColl1.setDataValue("wfi_biz_page", wfi_biz_page);
            }
            OrganizationCacheServiceInterface orgCacheMsi = (OrganizationCacheServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationCacheServices", "organization");
            orgCacheMsi.addUserName(kColl1, new String[]{"st_exe_user"});
			this.putDataElement2Context(kColl1, context);
			String serNO=(String)kColl1.getDataValue("serno");
			String bizType=(String)kColl1.getDataValue("biz_type");
			String stTaskName=(String)kColl1.getDataValue("st_task_name");
			String stAdvice=(String)kColl1.getDataValue("st_advice");
//			String stExeUser = (String) kColl1.getDataValue("");
			
			kColl.addDataField("serno", serNO);
			kColl.addDataField("biz_type", bizType);
			kColl.addDataField("st_task_name", stTaskName);
			kColl.addDataField("st_advice", stAdvice);
			
			this.putDataElement2Context(kColl, context);
			
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
