package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>根据岗位获取我的项目池</p>
 * @author liuhw
 *
 */
public class GetMyTaskPoolOp extends CMISOperation {

	private final String modelId = "WfTaskpool";
	@Override
	public String doExecute(Context context) throws EMPException {
		
		WorkflowServiceInterface wfi = null;
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			Vector vecPool = wfi.queryUserTaskPool(currentUserId, connection);
			IndexedCollection icoll = new IndexedCollection(modelId+"List");
			KeyedCollection queryData = (KeyedCollection) context.get(modelId);
			if(vecPool!=null && vecPool.size()>0) {
				TableModelDAO dao = this.getTableModelDAO(context);
				for(int i=0; i<vecPool.size(); i++) {
					Vector vecRow = (Vector) vecPool.elementAt(i);
					String tpid=(String)vecRow.elementAt(0);
					if(queryData!=null && queryData.containsKey("tpid")) {
						String queryTpid = (String) queryData.getDataValue("tpid");
						if(queryTpid!=null && !queryTpid.trim().equals("") && !tpid.contains(queryTpid)) {
							continue;
						}
					}
					String tpname=(String)vecRow.elementAt(1);
					if(queryData!=null && queryData.containsKey("tpname")) {
						String querytpname = (String) queryData.getDataValue("tpname");
						if(querytpname!=null && !querytpname.trim().equals("") && !tpname.contains(querytpname)) {
							continue;
						}
					}
					String tpdsc=(String)vecRow.elementAt(2);
					if(queryData!=null && queryData.containsKey("tpdsc")) {
						String querytpdsc = (String) queryData.getDataValue("tpdsc");
						if(querytpdsc!=null && !querytpdsc.trim().equals("") && !tpdsc.contains(querytpdsc)) {
							continue;
						}
					}
					KeyedCollection kcoll = new KeyedCollection(modelId);
					kcoll.put("tpid", tpid);
					kcoll.put("tpname", tpname);
					kcoll.put("tpdsc", tpdsc);
					//追加项目池中可以认领的任务数量
					List fields = new ArrayList();
					fields.add("instanceid");
					String condition = "WHERE CURRENTNODEUSER='T."+tpid+";' AND (WFSTATUS='0' OR WFSTATUS='5') ";
					IndexedCollection icollTmp = dao.queryList("WfiWorklistTodo", fields, condition, connection);
					kcoll.put("tpsize", icollTmp.size());
					
					icoll.add(kcoll);
				}
			}
			
			this.putDataElement2Context(icoll, context);
//			context.put("taskPoolList", vecPool);
					
		} catch (Exception e) {
			EMPLog.log("GetMyTaskPoolOp", EMPLog.ERROR, EMPLog.ERROR, "根据岗位获取我的项目池出错！异常信息："+e.getMessage(), e);
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
