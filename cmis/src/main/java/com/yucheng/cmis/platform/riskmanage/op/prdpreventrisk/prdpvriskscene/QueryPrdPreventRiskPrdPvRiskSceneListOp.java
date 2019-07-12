package com.yucheng.cmis.platform.riskmanage.op.prdpreventrisk.prdpvriskscene;

import java.sql.Connection;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.riskmanage.component.RiskManageComponent;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPrdPreventRiskPrdPvRiskSceneListOp extends CMISOperation {
	private final String modelId = "PrdPvRiskScene";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String prevent_id_value = (String)context.getDataValue("PrdPreventRisk.prevent_id");
			
			if(prevent_id_value==null){
				throw new EMPException("parent primary key not found!");
			}
			WorkflowServiceInterface wfi = null;
			RiskManageComponent riskManageComponent = (RiskManageComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("RiskManage", context, connection);
			IndexedCollection sceneIColl = riskManageComponent.queryListByPreventId(prevent_id_value,connection);
			IndexedCollection ic = new IndexedCollection();
			if(sceneIColl != null && sceneIColl.size() > 0){
				String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
				String orgid = (String) context.getDataValue(CMISConstance.ATTR_ORGID);
				wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
				List<WFIVO> wfivoList = wfi.getWFNameList(currentUserId, orgid, null, connection);
				
				for(int i=0;i<sceneIColl.size();i++){
					KeyedCollection kc = (KeyedCollection)sceneIColl.get(i);
					String wfid = (String)kc.getDataValue("wfid");
					for(WFIVO vo : wfivoList) {
						String wfId = vo.getWfSign();
						if(wfId.equals(wfid)){
							String wfName = vo.getWfName();
							kc.addDataField("wfiname", wfName);
						}
					}
					ic.add(kc);
				}
			}
			ic.setName("PrdPvRiskSceneList");
			
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			this.putDataElement2Context(ic, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

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
