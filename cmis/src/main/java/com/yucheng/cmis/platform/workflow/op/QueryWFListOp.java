package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.util.TableModelUtil;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

/**
 * <p>获取可用流程列表</p>
 * @author liuhw
 *
 */
public class QueryWFListOp extends CMISOperation {

	@Override
	public String doExecute(Context context) throws EMPException {
		
		String viewType; //返回jsp试图类型;normal普通列表页面,pop为pop框选择页面
		try {
			viewType = (String) context.getDataValue("viewType");
		} catch (Exception e) {
			viewType = "normal";
		}
		
		WorkflowServiceInterface wfi = null;
		Connection connection = null;
		KeyedCollection queryData = null;
		//只支持根据流程标识、流程名称查询
		String queryWfSign = null;
		String queryWfName = null;
		try {
			queryData = (KeyedCollection) context.getDataElement("WorkFlow");
			queryWfSign = (String) queryData.getDataValue("wfsign");
			queryWfName = (String) queryData.getDataValue("wfname");
		} catch (Exception e) {
		}
		
		try {
			String currentuserid = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			String orgid = (String) context.getDataValue(CMISConstance.ATTR_ORGID);
			String sysid = (String) context.getDataValue(WorkFlowConstance.ATTR_SYSID);
			wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			connection = this.getConnection(context);
			//sysid传null，查询所有
			List<WFIVO> wfivoList = wfi.getWFNameList(currentuserid, orgid, null, connection);
			List<WFIVO> queryList = new ArrayList<WFIVO>();
			//根据条件过滤
			for(WFIVO vo : wfivoList) {
				String wfSignTmp = vo.getWfSign();
				String wfNameTmp = vo.getWfName();
				boolean isExist1 = false;
				boolean isExist2 = false;
				try {
					isExist1 = wfSignTmp.contains(queryWfSign);
				} catch (Exception e) {
					isExist1 = true;
				}
				try {
					isExist2 = wfNameTmp.contains(queryWfName);
				} catch (Exception e) {
					isExist2 = true;
				}
				if(isExist1 && isExist2) {
					queryList.add(vo);
				}
			}
			
			//分页
			IndexedCollection icoll = new IndexedCollection("WorkFlowList");
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "15");
			int pageIdx = pageInfo.pageIdx;
			int begin = pageInfo.beginIdx;
			int end = pageInfo.endIdx;
			int recordSize = pageInfo.recordSize;
			if(queryList!=null && queryList.size()>0) {
				int size = queryList.size();
				if(recordSize != size) {
					pageInfo.setRecordSize(String.valueOf(size));
					pageInfo.setPageIdx("1");
					begin = 1;
					end = 15;
				} else {
					begin = (pageIdx-1)*15+1;
					end = pageIdx*15>size?size:pageIdx*15;
				}
				for(int i=0; i<size; i++) {
					if(i+1>=begin && i<end) {
						KeyedCollection kcoll = new KeyedCollection("WorkFlow");
						WFIVO vo = queryList.get(i);
						String wfId = vo.getWfId();
						String wfSign = vo.getWfSign();
						String wfName = vo.getWfName();
						String wfVer = vo.getWfVer();
						kcoll.put("wfid", wfId);
						kcoll.put("wfsign", wfSign);
						kcoll.put("wfname", wfName);
						kcoll.put("wfver", wfVer);
						icoll.add(kcoll);
					}
				}
			} else {
				pageInfo.setRecordSize("0");
			}
			this.putDataElement2Context(icoll, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}

		return viewType;
	}

}
