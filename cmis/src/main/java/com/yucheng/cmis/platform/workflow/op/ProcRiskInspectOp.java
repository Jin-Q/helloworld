package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.pkgenerator.UNIDGenerator;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.riskmanage.RISKPUBConstant;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;

/**
 * <p>执行风险拦截检查</p>
 * <p>泉州银行风险拦截检查，通过流程节点检查</p>
 * @author Pansq
 */
public class ProcRiskInspectOp extends CMISOperation {
	private static final String WF_SCENE_START = "000";
	private static final String interfaceId = "RiskItem";
	@Override
	public String doExecute(Context context) throws EMPException {
		String modelId = null;  //表模型ID
		String pkVal = null;    //主键
		String wfsign = null;   //流程标识
		String scene = null;    //场景
		String pvIdList = null; //拦截方案清求 （可是多个，其中以 , 分隔）
		String procType = null; //流程业务类别
		try{
		  pvIdList = (String)context.getDataValue("pvId");
		}catch(ObjectNotFoundException ex){
		}
		if(pvIdList == null || pvIdList.trim().equals("") || pvIdList.trim().toLowerCase().equals("undefined")){
			/** 没有风险拦截方案清单，无需检查风险 直接返回*/
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"没有风险拦截方案清单，无需检查风险 直接返回");
			context.addDataField("result", 3);
			return null;
		}
		
		try{
		   modelId = (String)context.getDataValue("modelId");
		}catch(ObjectNotFoundException ex){
		}
		if(modelId == null || modelId.trim().equals("")){
			throw new EMPException("请求参数中没有申请表主键值，无法进行风险拦截");
		}		
		try{
		  pkVal = (String)context.getDataValue("pkVal");
		}catch(ObjectNotFoundException ex){
		}
		if(pkVal == null || pkVal.trim().equals("")){
			throw new EMPException("请求参数中没有申请表主键值，无法进行风险拦截");
		}
	
		/** 得流程属性-- 申请类别 */
		try {
			procType =  (String)context.getDataValue("applType");
		} catch (Exception e) {
		}
		if(procType == null || procType.trim().equals("") || procType.trim().toLowerCase().equals("undefined")){
			throw new EMPException("请求参数中没有申请类别值，无法进行风险拦截");
		}
		/** 获取流程关联业务表中是否配置了拦截方案ID，存在进行拦截检查，不存在直接通过！ */
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			KeyedCollection flow2bizKColl = (KeyedCollection)SqlClient.queryFirst("queryPreventByProcType", procType, null, connection);
			/** 1.判断是否存在拦截方案 */
			String preventList = (String)flow2bizKColl.getDataValue("prevent_list");
			wfsign = (String)flow2bizKColl.getDataValue("wfsign");
			if(preventList == null || preventList.trim().length() == 0){
				context.addDataField("result", PUBConstant.WFI_RISKINSPECT_RESULT_CANCEL);
				return null;
			}
			try{
				  scene =  (String)context.getDataValue("nodeId");
			}catch(ObjectNotFoundException ex){
			}
			if(scene == null || scene.trim().equals("")){
				/** 为空时默认为发起节点，根据流程标识查询当前流程发起节点 */
				WorkflowServiceInterface wfi = null;
				wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
				scene = (String)wfi.getWFPropertyByWfSign(wfsign, "WFFirstNodeDocID");
				
			}
			/** 2.取得拦截方案，进行拦截方案检查 */
			String pvList[] = preventList.split(",");
			StringBuffer pvListSql = new StringBuffer();
			for(int n=0; n<pvList.length; n++){
				pvListSql.append("'").append(pvList[n]).append("',");
			}
			
			String isNeedSql =  " where wfid = '" + wfsign + "' and scene_id = '" + scene + "' and PREVENT_ID in (" + pvListSql.substring(0,pvListSql.length()-1) + ")";
			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection sceneIColl = dao.queryList("PrdPvRiskScene", isNeedSql, connection);
			if(sceneIColl != null && sceneIColl.size() > 0){
				/** 3.进行风险拦截项检查，逐一检查风向拦截项 ，检查之前删除原有拦截记录信息*/
				//1.删除原有风险拦截应用
				Map<String,String> delParam = new HashMap();
				delParam.put("serno", pkVal);
				delParam.put("wfid", wfsign);
				delParam.put("node_id", scene);
				SqlClient.executeUpd("deleteIqpPvRiskResult", delParam, null, null, connection);
				
				for(int i=0;i<pvList.length;i++){
		    		if(pvList[i] != null && !pvList[i].trim().equals("")){
			    		String preventId=pvList[i];
			    		//2.执行风险拦截应用
			    		/**2.1判断该风险拦截方案是否启用 */
			    		KeyedCollection riskKColl = dao.queryDetail("PrdPreventRisk", preventId, connection);
			    		String used_ind = (String)riskKColl.getDataValue("used_ind");
			    		if(used_ind != null){
			    			if(used_ind.equals("2")){
			    			}else {
			    				// 调用规则管理模块对外提供的服务：根据用户号得到该用户拥有的所有角色
			    				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			    				ShuffleServiceInterface shuffleService = null;
			    				try {
			    					shuffleService = (ShuffleServiceInterface) serviceJndi.getModualServiceById("shuffleServices","shuffle");
			    				} catch (Exception e) {
			    					EMPLog.log("shuffle", EMPLog.ERROR, 0,"getModualServiceById error!", e);
			    					throw new EMPException(e);
			    				}
			    				/** 2.2查询拦截方案下所有的拦截场景PRD_PV_RISK_SCENE */
			    				IndexedCollection checkIColl = dao.queryList("PrdPvRiskScene", " where wfid = '" + wfsign + "' and scene_id = '" + scene + "' and prevent_id='"+preventId+"'", connection);
			    				Map<String, String> returnMap = new HashMap<String, String>();
			    				UNIDGenerator unidGenerator = new UNIDGenerator();
			    				/**2.3依次检查拦截场景中每一项拦截项 */
			    				if(checkIColl != null && checkIColl.size() > 0){
			    					for(int j=0;j<checkIColl.size();j++){
			    						KeyedCollection sceneKColl = (KeyedCollection)checkIColl.get(j);
			    						String item_id = (String)sceneKColl.getDataValue("item_id");
			    						String risk_level = (String)sceneKColl.getDataValue("risk_level");
			    						/**2.4通过item_id查询拦截项信息*/
			    						KeyedCollection itemKColl = dao.queryDetail("PrdPvRiskItem", item_id, connection);
			    						String usedInd = (String)itemKColl.getDataValue("used_ind");
			    						if(usedInd.equals("2")){
			    							//不拦截
			    						}else {
			    							String item_desc = (String)itemKColl.getDataValue("item_desc");
				    						String link_url = (String)itemKColl.getDataValue("link_url");
				    						String item_rule=(String) itemKColl.getDataValue("item_rules");
				    						if(link_url!=null){
				    							link_url=link_url.replace("$_pkVal", pkVal);
				    			    		}else{
				    			    			link_url="";
				    			    		}
				    						/**2.5规则输入参数封装，此处只提供业务主键，所有数据通过主键查询*/
				    						Map inputValueMap=new HashMap();
				    			    		inputValueMap.put("IN_SERNO", pkVal);
				    			    		/**2.6执行规则检查，获得规则检查结果  */
				    			    		returnMap=shuffleService.fireTargetRule(item_rule.split("_")[0],item_rule.split("_")[1] , inputValueMap);	
				    			    		String msg = returnMap.get("OUT_提示信息");
				    			    		if(msg == null || "".equals(msg)){
				    			    			msg="无提示信息！";
				    			    		}	
				    						/**2.7将检查结果插入规则结果表 */
				    			    		String resultId = unidGenerator.getUNID();
				    			    		KeyedCollection riskResultKColl = new KeyedCollection();
				    			    		riskResultKColl.addDataField("result_id",resultId);
				    			    		riskResultKColl.addDataField("serno",pkVal);
				    			    		riskResultKColl.addDataField("wfid",wfsign);
				    			    		riskResultKColl.addDataField("node_id",scene);
				    			    		riskResultKColl.addDataField("item_id",item_id);
				    			    		riskResultKColl.addDataField("risk_level",risk_level);
				    			    		riskResultKColl.addDataField("pass_state",RISKPUBConstant.WFI_RISKINSPECT_RESULT.get(returnMap.get("OUT_是否通过")));
				    			    		riskResultKColl.addDataField("item_name",item_desc);
				    			    		riskResultKColl.addDataField("item_desc",msg);
				    			    		riskResultKColl.addDataField("link_url",link_url);
				    			    		riskResultKColl.setName("IqpPvRiskResult");
				    			    		
				    			    		dao.insert(riskResultKColl, connection);
			    						}
			    						returnMap.clear();
			    					}
			    				}
			    			}
			    		}
		    		}
		    	}
				/** 4.得到检查结果明细 */
				IndexedCollection riskIColl = dao.queryList("IqpPvRiskResult", " where serno='"+pkVal+"' and wfid='"+wfsign+"' and node_id='"+scene+"'", connection);
				/** 5.判断检查结果是否通过，判断依据包括拦截类型，以及返回结果 */
				boolean isPass = true;
				if(riskIColl != null && riskIColl.size() > 0){
					for(int k=0;k<riskIColl.size();k++){
						KeyedCollection resultKColl = (KeyedCollection)riskIColl.get(k);
						String riskLevel = (String)resultKColl.getDataValue("risk_level");
						String passState = (String)resultKColl.getDataValue("pass_state");
						if(passState.trim().equals(PUBConstant.WFI_RISKINSPECT_RESULT_DENY)&&riskLevel.equals(PUBConstant.WFI_RISKINSPECT_RESULT_DENY)){
							isPass = false;
						}
					}
				}
				if(isPass){
				   /** 检查通过 */
				   context.addDataField("result", PUBConstant.WFI_RISKINSPECT_RESULT_PASS);
				}else{
				   /** 检查不通过 */
				   context.addDataField("result", PUBConstant.WFI_RISKINSPECT_RESULT_DENY);
				}
				riskIColl.setName("riskInspectList");
				this.putDataElement2Context(riskIColl, context);
			}else {
				context.addDataField("result", PUBConstant.WFI_RISKINSPECT_RESULT_CANCEL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}
}
