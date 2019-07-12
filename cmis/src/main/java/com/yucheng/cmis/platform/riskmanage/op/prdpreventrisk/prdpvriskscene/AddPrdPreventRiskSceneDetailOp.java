package com.yucheng.cmis.platform.riskmanage.op.prdpreventrisk.prdpvriskscene;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class AddPrdPreventRiskSceneDetailOp extends CMISOperation {

	private static final String relModel = "PrdPvRiskItemRel";
	private static final String itemModel = "PrdPvRiskItem";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			
			String prevent_id = (String)context.getDataValue("prevent_id");
			if(prevent_id == null || prevent_id.trim().length() == 0){
				throw new EMPException("获取拦截方案ID失败！");
			}
			
			
			/** 通过流程ID，获取流程节点，组装成流程节点字典项 */
			String flow_id = "";
			if(context.containsKey("flow_id")){
				flow_id = (String)context.getDataValue("flow_id");
			}
			if(flow_id != null && flow_id.trim().length() != 0){
				/** 通过流程字典项封装生成流程节点字典项,由于产品模块加载时已经将流程节点封装成字典项，规则为：流程名_FLOWNODE_TYPE，所以此处直接引用 */
				IndexedCollection nodeIColl = (IndexedCollection)((KeyedCollection)context.getDataElement("dictColl")).getDataElement(flow_id+"_FLOWNODE_TYPE");
				nodeIColl.setName("FLOW_SCENE_ID");
				this.putDataElement2Context(nodeIColl, context);
			}
			
			/** 加载风险拦截场景相关信息 */
			KeyedCollection fontKColl = new KeyedCollection();
			fontKColl.addDataField("prevent_id", prevent_id);
			fontKColl.addDataField("wfid", flow_id);
			fontKColl.setName("PrdPvRiskScene");
			this.putDataElement2Context(fontKColl, context);
			
			/** 通过关联项目表查询出关联的拦截项 */
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection returnIColl = new IndexedCollection();
			
			IndexedCollection relIColl = dao.queryList(relModel, " where prevent_id = '"+prevent_id+"'", connection);
			if(relIColl != null && relIColl.size() > 0){
				for(int i=0;i<relIColl.size();i++){
					KeyedCollection returnKColl = new KeyedCollection();
					KeyedCollection relKColl = (KeyedCollection)relIColl.get(i);
					String item_id = (String)relKColl.getDataValue("item_id");
					KeyedCollection itemKColl = dao.queryDetail(itemModel, item_id, connection);
					returnKColl.addDataField("item_id", itemKColl.getDataValue("item_id"));
					returnKColl.addDataField("item_name", itemKColl.getDataValue("item_name"));
					returnKColl.addDataField("used_ind", itemKColl.getDataValue("used_ind"));
					returnIColl.add(returnKColl);
				}
			}
			returnIColl.setName("PrdPvRiskSceneList");
			this.putDataElement2Context(returnIColl, context);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
