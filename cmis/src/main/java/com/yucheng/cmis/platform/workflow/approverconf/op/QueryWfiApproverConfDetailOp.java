package com.yucheng.cmis.platform.workflow.approverconf.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.util.WorkFlowUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryWfiApproverConfDetailOp  extends CMISOperation {
	
	private final String modelId = "WfiApproverConf";
	

	private final String confid_name = "confid";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String confid_value = null;
			try {
				confid_value = (String)context.getDataValue(confid_name);
			} catch (Exception e) {}
			if(confid_value == null || confid_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+confid_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, confid_value, connection);
			kColl.addDataField("wfname", WorkFlowUtil.getWFPropertyByWfSign((String)kColl.getDataValue("wfsign"), "WFName"));
			kColl.addDataField("nodename", WorkFlowUtil.getWFNodeProperty((String)kColl.getDataValue("nodeid"), "NodeName"));
			SInfoUtils.addSOrgName(kColl, new String[]{"orgid"});
			
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
