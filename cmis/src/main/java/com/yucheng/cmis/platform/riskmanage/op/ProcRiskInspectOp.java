package com.yucheng.cmis.platform.riskmanage.op;

import java.sql.Connection;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.riskmanage.RISKPUBConstant;
import com.yucheng.cmis.platform.riskmanage.domain.IqpPvRiskResult;
import com.yucheng.cmis.platform.riskmanage.riskinterface.PrvRiskItemInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

/**
 * <p>执行风险拦截检查</p>
 * @author yuchengtech
 */
public class ProcRiskInspectOp extends CMISOperation {

	private static final String WF_SCENE_START = "000";
	private static final String interfaceId = "RiskItem";
	@Override
	public String doExecute(Context context) throws EMPException {
		String modelId = null;  //表模型ID
		String pkVal = null;    //主键
		String wfid = "000";   //流程标识
		String nodeId = "000";    //流程当前节点
		String pvIdList = null; //拦截方案清求 （可是多个，其中以 , 分隔）
		try{
		  pvIdList = (String)context.getDataValue("pvId");
		}catch(ObjectNotFoundException ex){
		}
		if(pvIdList == null || pvIdList.trim().equals("") || pvIdList.trim().toLowerCase().equals("undefined")){
				
			/** 没有风险拦截方案清单，无需检查风险 直接返回*/
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0,"没有风险拦截方案清单，无需检查风险 直接返回");
			context.addDataField("result", RISKPUBConstant.WFI_RISKINSPECT_RESULT_CANCEL);
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

		String currentUserId = (String)context.getDataValue("currentUserId");
		String orgid = (String)context.getDataValue("organNo");	
		
		Connection conn = getConnection(context);
		
		try{
			/** 判断流程的当前场景下是否需要进行风险拦截检 */
			PrvRiskItemInterface prv = (PrvRiskItemInterface)CMISComponentFactory.getComponentFactoryInstance()
			               .getComponentInterface(interfaceId, context, conn);
						
            String[] _pvId = pvIdList.split(",");//拦截方案清单
            
			if(!prv.isNeedRiskInspected(_pvId, wfid, nodeId)){
				/** 无需检查风险 直接返回*/
				context.addDataField("result", PUBConstant.WFI_RISKINSPECT_RESULT_CANCEL);
				return null;
			}
			
			/** 如果需要进行风险拦截检 ，则进行检查*/
			prv.doInspect(modelId, pkVal, _pvId, wfid, nodeId);
			
			/** 得到检查结果明细 */
			List<IqpPvRiskResult> riskItem = prv.getAllRiskItems(pkVal, wfid, nodeId);
			
			/** 根据检查结果返回 */
			if(prv.getIfAccessOfListItem(riskItem)){
			   /** 检查通过 */
			   context.addDataField("result", PUBConstant.WFI_RISKINSPECT_RESULT_PASS);
			}else{
			   /** 检查不通过 */
			   context.addDataField("result", PUBConstant.WFI_RISKINSPECT_RESULT_DENY);
			}
		   IndexedCollection iColl = this.domain2Kcol(riskItem);
		   this.putDataElement2Context(iColl, context);	
			
		}catch(Exception ex){
			ex.printStackTrace();
			context.addDataField("result", RISKPUBConstant.WFI_RISKINSPECT_RESULT_EXCEPTION);
			if(null!=ex.getCause()){
				throw new EMPException(ex.getCause().getMessage());
			}else{
				throw new EMPException(ex.getMessage());
			}
		}finally{
			if(conn != null)
			   this.releaseConnection(context, conn);
		}
		
		return null;
	}

	private IndexedCollection domain2Kcol(List<IqpPvRiskResult> riskItem){
		if(riskItem == null){
			return null;
		}
		IndexedCollection iColl = new IndexedCollection("riskInspectList");
		
		for(int n=0; n<riskItem.size(); n++){
			IqpPvRiskResult _result = riskItem.get(n);
			if(_result != null && _result.getPassState() != null ){
					//&& _result.getPassState().trim().equals(PUBConstant.WFI_RISKINSPECT_RESULT_DENY) ){
				////只显示检查不通过的////
			   KeyedCollection _kResult = new KeyedCollection();
			   try {
				  _kResult.addDataField("result_id",_result.getResultId());
				  _kResult.addDataField("item_id",_result.getItemId());
				  _kResult.addDataField("item_name",_result.getItemName());
				  _kResult.addDataField("item_desc",_result.getItemDesc());
				  _kResult.addDataField("pass_state",_result.getPassState());
				  _kResult.addDataField("risk_level",_result.getRiskLevel());
				  _kResult.addDataField("link_url",_result.getLinkUrl());
				  
				
			   } catch (InvalidArgumentException e) {
					e.printStackTrace();
			   } catch (DuplicatedDataNameException e) {
					e.printStackTrace();
			   }
			   iColl.add(_kResult);
			}
		}
		return iColl;
	}
}
