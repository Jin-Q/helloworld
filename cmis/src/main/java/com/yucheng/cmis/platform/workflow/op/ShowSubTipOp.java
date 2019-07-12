package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISDataDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFICommentVO;
import com.yucheng.cmis.platform.workflow.domain.WFIGatherCommentVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>查看子流程或会办相关意见信息</p>
 * @author liuhw
 *
 */
public class ShowSubTipOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {

		String subType = (String) context.getDataValue("subType");
		String subID = (String) context.getDataValue("subID");
		String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
		String orgId = (String) context.getDataValue(CMISConstance.ATTR_ORGID);
		WorkflowServiceInterface wfi = null;
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			if(subType.equals("subGather")){
				
				//取流程意见
				List<WFIGatherCommentVO> gatherWfComment = wfi.getGatherComment(subID, currentUserId, "0", connection);
				context.put("GatherWfCommentList", gatherWfComment);
				
				//取会办意见
				List<WFIGatherCommentVO> gatherComment = wfi.getGatherComment(subID, currentUserId, "1", connection);
				context.put("GatherCommentList", gatherComment);
				
			} else if(subType.equals("subFlow")||subType.equals("mainFlow")){
				
				List listGz = wfi.getWorkFlowHistory(subID, currentUserId, null, orgId, connection);
				context.put("WorkFlowHisList", listGz);
				
				List<WFICommentVO> listYj = wfi.getAllComments(subID, currentUserId, false, connection);
				/**
				 * 暂时用比较原始的办法获取意见标识名称
				 */
				Map<String, String> dicMap = new HashMap<String, String>();
				KeyedCollection dictColl = (KeyedCollection)context.getDataElement(CMISConstance.ATTR_DICTDATANAME);
				if(dictColl!=null) {
					IndexedCollection icoll = (IndexedCollection) dictColl.getDataElement("WF_APP_RESULT");
					if(icoll != null) {
						for(int i=0; i<icoll.size(); i++) {
							KeyedCollection kcoll = (KeyedCollection) icoll.get(i);
							dicMap.put((String)kcoll.getDataValue(CMISDataDicService.ATTR_ENNAME), (String)kcoll.getDataValue(CMISDataDicService.ATTR_CNNAME));
						}
					}
				}
				if(listYj!=null && listYj.size()>0) {
					for(WFICommentVO cvo : listYj) {
						cvo.setCommentSignName(dicMap.get(cvo.getCommentSign()));
					}
				}
				context.put("WFICommentList", listYj);
			}
			
		} catch (Exception e) {
			EMPLog.log("ShowSubTipOp", EMPLog.ERROR, EMPLog.ERROR, "查看子流程或会办相关意见信息出错。异常信息："+e.getMessage());
			throw new EMPException(e);
		} finally {
			if(connection != null) {
				this.releaseConnection(context, connection);
			}
		}
		
		return null;
	}

}
