package com.yucheng.cmis.platform.workflow.meetingsign.op.wfisigntask.wfisignvote;

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
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class QueryWfiSignTaskWfiSignVoteDetailOp  extends CMISOperation {

	private final String modelId = "WfiSignVote";


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
			OrganizationCacheServiceInterface orgCacheMsi = (OrganizationCacheServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationCacheServices", "organization");
			orgCacheMsi.addUserName(kColl, new String[]{"sv_exe_user"});
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
