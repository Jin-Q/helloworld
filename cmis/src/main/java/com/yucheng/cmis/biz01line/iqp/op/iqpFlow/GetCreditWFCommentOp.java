package com.yucheng.cmis.biz01line.iqp.op.iqpFlow;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISDataDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFICommentVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>获取流程意见</p>
 * @author liuhw
 *
 */
public class GetCreditWFCommentOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {

		String currentUserId = null;
		String instanceId = null;
		Connection connection = null;
		WorkflowServiceInterface wfi = null;
		try {
			currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			instanceId = (String) context.getDataValue(WorkFlowConstance.ATTR_INSTANCEID);
			connection = this.getConnection(context);
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			List<WFICommentVO> list = wfi.getAllComments(instanceId, currentUserId, false, connection);
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
			if(list!=null && list.size()>0) {
				for(WFICommentVO cvo : list) {
					cvo.setCommentSignName(dicMap.get(cvo.getCommentSign()));
				}
			}
			context.put("CreditWFICommentList", list);
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
