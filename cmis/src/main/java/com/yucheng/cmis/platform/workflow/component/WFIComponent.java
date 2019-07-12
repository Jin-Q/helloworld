package com.yucheng.cmis.platform.workflow.component;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.ecc.echain.db.DbControl;
import com.ecc.echain.log.WfLog;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.pkgenerator.PkGeneratorSet;
import com.ecc.emp.dbmodel.service.pkgenerator.UNIDGenerator;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.domain.WFIVO;
import com.yucheng.cmis.platform.workflow.domain.WfiBizVarRecordVO;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.domain.WfiWorkflow2biz;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.platform.workflow.util.WorkFlowUtil;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.exception.ComponentException;
import com.yucheng.cmis.pub.util.TimeUtil;

public class WFIComponent extends CMISComponent {

	/**
	 * <p>根据主键流程实例号查询流程中间表信息</p>
	 * @param instanceId 流程实例号
	 * @return 中间表信息kColl
	 * @throws ComponentException 
	 */
	public KeyedCollection queryWfiJoin(String instanceId) throws ComponentException {
		KeyedCollection kColl = new KeyedCollection();
		String sqlId = "getWfiJoinByInstanceId";
		try {
			kColl = (KeyedCollection) SqlClient.queryFirst(sqlId, instanceId, null, getConnection());
			if(kColl != null)
				kColl.setName("WfiJoin");
		} catch (ComponentException e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据主键流程实例号查询流程中间表信息[queryWfiJoin]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		} catch (SQLException e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据主键流程实例号查询流程中间表信息[queryWfiJoin]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return kColl;
	}
	
	/**
	 * <p>根据主键流程实例号查询流程中间历史表信息</p>
	 * @param instanceId 流程实例号
	 * @return 中间表信息kColl
	 * @throws ComponentException 
	 */
	public KeyedCollection queryWfiJoinHis(String instanceId) throws ComponentException {
		KeyedCollection kColl = new KeyedCollection();
		String sqlId = "getWfiJoinHisByInstanceId";
		try {
			kColl = (KeyedCollection) SqlClient.queryFirst(sqlId, instanceId, null, getConnection());
			if(kColl != null)
				kColl.setName("WfiJoinHis");
		} catch (ComponentException e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据主键流程实例号查询流程中间历史表信息[queryWfiJoinHis]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		} catch (SQLException e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据主键流程实例号查询流程中间历史表信息[queryWfiJoinHis]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return kColl;
	}
	
	/**
	 * <p>根据主流程实例号查询子流程中间表列表信息</p>
	 * @param mainInstanceId 主流程实例号
	 * @return 中间表列表信息icoll
	 * @throws ComponentException 
	 */
	public IndexedCollection querySubWfiJoinList(String mainInstanceId) throws ComponentException {
		IndexedCollection icoll = new IndexedCollection();
		String sqlId = "getSubWfiJoinByInstanceId";
		try {
			Collection col = SqlClient.queryList(sqlId, mainInstanceId, getConnection());
			Iterator colItr = col.iterator();
			while(colItr.hasNext()) {
				KeyedCollection kcoll = (KeyedCollection) colItr.next();
				icoll.add(kcoll);
			}
		} catch (Exception e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据主流程实例号查询子流程中间表列表信息[querySubWfiJoinList]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		
		return icoll;
	}
	
	
	/**
	 * <p>根据主流程实例号查询子流程中间表历史列表信息</p>
	 * @param mainInstanceId 主流程实例号
	 * @return 中间表列表信息icoll
	 * @throws ComponentException 
	 */
	public IndexedCollection querySubWfiJoinHisList(String mainInstanceId) throws ComponentException {
		IndexedCollection icoll = new IndexedCollection();
		String sqlId = "getSubWfiJoinHisByInstanceId";
		try {
			Collection col = SqlClient.queryList(sqlId, mainInstanceId, getConnection());
			Iterator colItr = col.iterator();
			while(colItr.hasNext()) {
				KeyedCollection kcoll = (KeyedCollection) colItr.next();
				icoll.add(kcoll);
			}
		} catch (Exception e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据主流程实例号查询子流程中间表历史列表信息[querySubWfiJoinHisList]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return icoll;
	}
	
	
	/**
	 * <p>根据业务表模型ID、业务流水号（主键值）查询主流程中间表信息</p>
	 * @param modelId 申请表模型ID
	 * @param pkValue 申请主键值
	 * @return 中间表信息kColl
	 * @throws ComponentException
	 */
	public KeyedCollection queryMainWfiJoinByBiz(String modelId, String pkValue) throws ComponentException {
		
		String sqlId = "getMainWfiJoinByBiz";
		KeyedCollection WJInfo = null;
		try {
			KeyedCollection condi = new KeyedCollection();
			condi.addDataField("table_name", modelId);
			condi.addDataField("pk_value", pkValue);
			WJInfo = (KeyedCollection)SqlClient.queryFirst(sqlId, condi, null, this.getConnection());
		} catch (SQLException e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据业务表模型ID、业务流水号（主键值）查询信贷流程中间表信息[getMainWfiJoinByBiz]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		} catch (InvalidArgumentException e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据业务表模型ID、业务流水号（主键值）查询信贷流程中间表信息[getMainWfiJoinByBiz]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		} catch (DuplicatedDataNameException e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据业务表模型ID、业务流水号（主键值）查询信贷流程中间表信息[getMainWfiJoinByBiz]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return WJInfo;
	}
	
	
	/**
	 * <p>根据业务表模型ID、业务流水号（主键值）查询主流程中间历史表信息</p>
	 * @param modelId 申请表模型ID
	 * @param pkValue 申请主键值
	 * @return 中间表信息kColl
	 * @throws ComponentException
	 */
	public KeyedCollection queryMainWfiJoinHisByBiz(String modelId, String pkValue) throws ComponentException {
		
		String sqlId = "getMainWfiJoinHisByBiz";
		KeyedCollection WJInfo = null;
		try {
			KeyedCollection condi = new KeyedCollection();
			condi.addDataField("table_name", modelId);
			condi.addDataField("pk_value", pkValue);
			WJInfo = (KeyedCollection)SqlClient.queryFirst(sqlId, condi, null, this.getConnection());
		} catch (SQLException e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据业务表模型ID、业务流水号（主键值）查询信贷流程中间历史表信息[queryMainWfiJoinHisByBiz]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		} catch (InvalidArgumentException e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据业务表模型ID、业务流水号（主键值）查询信贷流程中间历史表信息[queryMainWfiJoinHisByBiz]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		} catch (DuplicatedDataNameException e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据业务表模型ID、业务流水号（主键值）查询信贷流程中间历史表信息[queryMainWfiJoinHisByBiz]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return WJInfo;
	}

	/**
	 * <p>根据申请类型获取流程定义的扩展属性[申请类型]匹配的流程标识</p>
	 * @param applyType 申请类型
	 * @return 流程标识list
	 * @throws ComponentException 
	 */
	public List<String> getWfsignByApplType(String applType) throws ComponentException {
		List<String> wfSignList = new ArrayList<String>();
		try {
			WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			Context  context = this.getContext();
			String currentUserId = (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			String orgId = (String) context.getDataValue(CMISConstance.ATTR_ORGID);
			String sysId = (String) context.getDataValue(WorkFlowConstance.ATTR_SYSID);
			List<WFIVO> wfivos = wfi.getWFNameList(currentUserId, orgId, sysId, null);
			for(WFIVO wfivo : wfivos) {
				String wfSign = wfivo.getWfSign();
				String applTypeTmp = wfi.getWFExtPropertyByWfSign(wfSign, "appltype");
				if(applTypeTmp!=null && applTypeTmp.equals(applType)) {
					wfSignList.add(wfSign);
				}
			}
		} catch (Exception e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据申请类型获取流程定义的扩展属性[申请类型]匹配的流程标识[getWfsignByApplType]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return wfSignList;
	}
	
	/**
	 * <p>根据申请类型和机构号从流程关联业务配置、流程关联机构配置中获取要发起的流程标识</p>
	 * 处理逻辑：<br>
	 * <li>1.先从流程关联业务配置中根据申请类型查找，如果查找到只有一条记录，则直接返回；否则进行第2步处理。
	 * <li>2.直接根据机构号、申请类型从流程关联机构配置中查找，如果查到，则返回；否则使用继承配置关系逐步查找。
	 * @param applType 申请类型
	 * @param organNo 流程发起机构号
	 * @return
	 * @throws ComponentException 
	 */
	public String getWfSignByConf(String applType, String organNo) throws ComponentException {
		String wfSign = null;
		String sqlId1 = "getWfSignFromWF2biz";
		try {
			Collection wfSignCol = SqlClient.queryList(sqlId1, applType, getConnection());
			if(wfSignCol==null || wfSignCol.size()<1) {
				throw new ComponentException("申请类型["+applType+"]没有关联的物理流程， 不能发起流程！");
			} else if(wfSignCol.size() == 1) {
				wfSign = (String) ((KeyedCollection) ((ArrayList)wfSignCol).get(0)).getDataValue("wfsign");
			} else {
				String sqlId = "getWfSignFromWF2org";
				String wfSignTmp = null;
				String organNoTmp = organNo;
				OrganizationServiceInterface orgMsi = (OrganizationServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationServices", "organization");
				while(wfSignTmp == null) {
					KeyedCollection paramKcoll = new KeyedCollection();
					paramKcoll.put("appl_type", applType);
					paramKcoll.put("org_id", organNoTmp);
					wfSignTmp = (String) SqlClient.queryFirst(sqlId, paramKcoll, null, getConnection());
					if(wfSignTmp != null) {
						wfSign = wfSignTmp;
						break;
					}
					String organNoBefore = organNoTmp;
					SOrg supOrg = orgMsi.getSupOrg(organNoTmp, getConnection());
					organNoTmp = supOrg.getOrganno();
					//上级机构为空或者就是本身，则表示该机构已经是总行最高机构。
					if("".equals(organNoTmp) || organNoBefore.equals(organNoTmp)) {
						throw new ComponentException("根据申请类型["+applType+"]与机构["+organNo+"]在流程关联机构配置中没有查询到匹配的信息，不能发起流程！");
					}
				}
			}
		} catch (Exception e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据申请类型和机构号从流程关联业务配置、流程关联机构配置中获取要发起的流程标识[getWfSignByConf]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return wfSign;
	}
	
	
	/**
	 * <p>删除流程实例信息，包括流程接入信息、子流程实例信息</p>
	 * @param instanceId 流程实例号
	 * @param currentUserId 当前操作人
	 * @return true-操作成功，false-操作失败
	 * @throws ComponentException 
	 */
	public boolean deleteWfInstance_unuseful(String instanceId, String currentUserId) throws ComponentException {
		boolean result = false;
		try {
			String sqlId1 = "delWfiBizVarByInstanceId";
			String sqlId1_ = "delWfiBizVarHisByInstanceId";
			String sqlId2 = "delWfiMsgByInstanceId";
			String sqlId2_ = "delWfiMsgHisByInstanceId";
			String sqlId3 = "delWfiJoinByInstanceId";
			String sqlId3_ = "delWfiJoinHisByInstanceId";
			IndexedCollection subIcoll = querySubWfiJoinList(instanceId);
			if(subIcoll.size() > 0) {  //存在子流程信息，则先删除子流程相关信息
				for(Object obj : subIcoll) {
					KeyedCollection kcoll = (KeyedCollection) obj;
					String subInstanceId = (String) kcoll.getDataValue("instanceid");
					if(!subInstanceId.equals(instanceId))  //发起同步子流程mainInstanceId与instanceId相同，应该过滤，否则死循环
						deleteWfInstance_unuseful(subInstanceId, currentUserId);
				}
			} else {
				IndexedCollection subHisIcoll = querySubWfiJoinHisList(instanceId);
				if(subHisIcoll.size() > 0) {  //存在子流程信息，则先删除子流程相关信息
					for(Object obj : subHisIcoll) {
						KeyedCollection kcoll = (KeyedCollection) obj;
						String subInstanceId = (String) kcoll.getDataValue("instanceid");
						if(!subInstanceId.equals(instanceId))
							deleteWfInstance_unuseful(subInstanceId, currentUserId);
					}
				}
			}
			SqlClient.delete(sqlId1, instanceId, getConnection());
			SqlClient.delete(sqlId1_, instanceId, getConnection());
			SqlClient.delete(sqlId2, instanceId, getConnection());
			SqlClient.delete(sqlId2_, instanceId, getConnection());
			SqlClient.delete(sqlId3, instanceId, getConnection());
			SqlClient.delete(sqlId3_, instanceId, getConnection());
			WorkflowServiceInterface wfi = (WorkflowServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById(WorkFlowConstance.WORKFLOW_SERVICE_ID, WorkFlowConstance.WORKFLOW_MODUAL_ID);
			Map paramMap = new HashMap();
			paramMap.put(WorkFlowConstance.ATTR_EMPCONTEXT, this.getContext());
			//added by yangzy 20150818 实例删除日志 start
			EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.ERROR, 0,"DelWorkFlowOp-PART4",null);
			//added by yangzy 20150818 实例删除日志 end
			wfi.wfDelInstance(instanceId, currentUserId, paramMap, getConnection());
			result = true;
			
		} catch (Exception e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "删除流程实例信息[deleteWfInstance]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		
		return result;
	}

	/**
	 * <p>向流程接入中间表插入记录，建立流程引擎与业务关联关系</p>
	 * @param joinKcoll 中间表单数据
	 * @return
	 * @throws ComponentException
	 */
	public int addWfiJoin(KeyedCollection joinKcoll) throws ComponentException {
		String sqlId = "insertWfiJoin";
		int count = -1;
		try {
			count = SqlClient.insertAuto(sqlId, joinKcoll, this.getConnection());
			if(count != 1) {
				throw new ComponentException("向流程接入中间表插入记录失败！影响记录条数为："+count);
			}
		} catch (Exception e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "向流程接入中间表插入记录，建立流程引擎与业务关联关系[addWfiJoin]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return count;
	}

	/**
	 * <p>根据申请类型从流程关联业务配置中查询记录</p>
	 * @param applType 申请类型
	 * @return
	 * @throws ComponentException
	 */
	public KeyedCollection getWorkFlow2BizByApplType(String applType) throws ComponentException {
		KeyedCollection kcoll = new KeyedCollection();
		String sqlId = "getWorkFlow2BizByApplType";
		try {
			kcoll = (KeyedCollection) SqlClient.queryFirst(sqlId, applType, null, getConnection());
		} catch (Exception e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据申请类型从流程关联业务配置中查询记录[getWorkFlow2BizByApplType]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return kcoll;
	}
	
	/**add by tangzf 2013.11.26
	 * <p>根据流程标志、申请类型从流程关联业务配置中查询记录</p>
	 * @param wfSign 流程标志
	 * @return
	 * @throws ComponentException
	 */
	public KeyedCollection getWorkFlow2BizByWfSignApplType(String wfSign,String applType) throws ComponentException {
		KeyedCollection kcoll = new KeyedCollection();
		String sqlId = "getWorkFlow2BizByWfSignApplType";
		try {
			Map<String, String> paraMap = new HashMap<String, String>();
			paraMap.put("wfSign", wfSign);
			paraMap.put("applType", applType);
			kcoll = (KeyedCollection) SqlClient.queryFirst(sqlId, paraMap, null, getConnection());
		} catch (Exception e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据流程标志从流程关联业务配置中查询记录[getWorkFlow2BizByWfSignApplType]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return kcoll;
	}
	
	/**
	 * <p>保存流程审批中修改的业务要素</p>
	 * @param varVal 被修改的要素值
	 * @param varNm  被修改的要素名称
	 * @param varType 被修改的要素数据类型
	 * @param varDisp 被修改的要素的显示值（用于SELECT标签）
	 * @param commentId 对应的流程意见ID 
	 * @param wfInfo 流程属性
	 */
	public void saveBizVariable(KeyedCollection varVal, KeyedCollection varNm, KeyedCollection varType, KeyedCollection varDisp, String commentId)throws ComponentException {
		String instanceId, modelId, pkValue, nodeId, nodeName;
		if(varVal == null || varNm == null || varType == null){
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.WARNING, EMPLog.WARNING, "修改业务要素信息不完整，无法执行保存操作[saveBizVariable]");
			return ;
		}
		Context context = this.getContext();
		TableModelDAO dao = (TableModelDAO) context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		PkGeneratorSet pkservice = (PkGeneratorSet) context.getService(CMISConstance.ATTR_PRIMARYKEYSERVICE);
		UNIDGenerator pk = (UNIDGenerator) pkservice.getGenerator("UNID");
		Set set = varVal.keySet();
		List listVar = new ArrayList ();
		try {
			instanceId = (String) context.getDataValue("instanceId");
			modelId = (String) context.getDataValue("modelId");
			pkValue = (String) context.getDataValue("pkVal");
			nodeId = (String) context.getDataValue("nodeId");
			nodeName = (String) context.getDataValue("nodeName");
			if(instanceId==null || modelId==null || pkValue==null || nodeId==null || nodeName==null) {
				throw new ComponentException("保存流程审批中修改的业务要素[saveBizVariable]出错，获取参数失败。");
			}
			String nodelevel = String.valueOf(WorkFlowUtil.getWFNodeProperty(nodeId, "nodelevel")); //节点级别
			String timeStr = TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss");
			KeyedCollection kcoll = dao.queryDetail(modelId, pkValue, this.getConnection());
			for(Iterator it = set.iterator();it.hasNext();) {
				String key = (String) it.next();
				if(key.endsWith("_old")){	//以old结尾的都是修改前的值不需要保存，只做显示用
					continue;
				}
				String varValue = (String)varVal.get(key);
				if(varValue==null || varValue.equals("")) {
					continue;
				}
				String varOldValue = (String)varVal.get(key+"_old");
				WfiBizVarRecordVO vo = new WfiBizVarRecordVO();
				vo.setPk1(pk.getUNID());
				vo.setVarValue(varValue);
				vo.setVarOldValue(varOldValue);
				vo.setVarName((String)varNm.getDataValue(key));				
				vo.setVarKey(key);
				vo.setInstanceid(instanceId);
				vo.setNodeid(nodeId);
				vo.setOpTime(timeStr);
				vo.setVarType((String)varType.getDataValue(key));
				vo.setInputBrId((String)context.getDataValue(CMISConstance.ATTR_ORGID));
				vo.setInputId((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID));
				vo.setNodename(nodeName);
				vo.setCommentid(commentId);
				vo.setRemark(nodelevel);  //将节点级别设置到备注字段，可用于业务意见查询权限控制
				
//				String varOldValue = "";
//				try{
//					if(kcoll != null){
//						varOldValue = (String)kcoll.getDataValue(key);
//						vo.setVarOldValue(varOldValue);
//					}
//				}catch(Exception ex){
//					vo.setVarOldValue(varOldValue);  //类似授信分项变更，主表不能取到值。暂设置空
//				}
				/**********保存时将上次修改变量存入本次var_old_value中 add by tangzf 2013-12-18 start***********/
//				if(varOldValue==null||"".equals(varOldValue)){
//					String condition = "WHERE INSTANCEID='"+instanceId+"' AND VAR_KEY='"+key+"' ORDER BY OP_TIME DESC";
//					IndexedCollection iColl = dao.queryList("WfiBizVarRecord", condition, this.getConnection());
//					if(iColl.size()>0){
//						KeyedCollection kCollTmp = (KeyedCollection)iColl.get(0);
//						vo.setVarOldValue((String)kCollTmp.getDataValue("var_value"));
//						vo.setVarOldDispvalue((String)kCollTmp.getDataValue("var_dispvalue"));
//					}
//				}
				/**********保存时将上次修改变量存入本次var_old_value中 add by tangzf 2013-12-18 end***********/
				String disValue = (String)varVal.get(key);
				try{
				  if(varDisp != null) {
				      String disValueTemp = (String)varDisp.getDataValue(key);
				      if(disValueTemp!=null && !"".equals(disValueTemp))
				    	  disValue = disValueTemp;
				  }
				}catch(Exception ex){///如果没有设显示值，则用实际值
				}
				vo.setVarDispvalue(disValue);
				listVar.add(vo);
			}
			SqlClient.executeBatch("insertWfiBizVarRecord", listVar, this.getConnection());
			
		} catch (Exception e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "保存流程审批中修改的业务要素[saveBizVariable]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
	}
	
	/**
	 * <p>根据办理人和流程节点清除暂存的审批变更数据</p>
	 * @param instanceId 流程实例号
	 * @param nodeId 流程节点ID
	 * @param userId 办理人
	 * @throws ComponentException
	 */
	public void clearWfiVariable(String instanceId,String nodeId,String userId,String commentid) throws ComponentException{
		 KeyedCollection kc = new KeyedCollection();
		 try {
			kc.addDataField("instanceId", instanceId);
			kc.addDataField("nodeId", nodeId);
			kc.addDataField("userId", userId);
			kc.addDataField("commentId", commentid);
			SqlClient.delete("deleteWfiBizVarRecord", kc, this.getConnection());
		} catch (Exception e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据办理人和流程节点清除暂存的审批变更数据[clearWfiVariable]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
	}
	
	/**
	 * 根据申请类型及适用范围获取流程关联业务配置信息
	 * @param applType 申请类型
	 * @param scope 配置适用范围（999所有、111审批中、997通过、998否决）WF_2BIZ_SCOPE
	 * @return
	 * @throws ComponentException 
	 */
	public KeyedCollection getWf2bizConfByApplType(String applType, String scope) throws ComponentException {
		KeyedCollection kcoll = new KeyedCollection();
		String sqlId = "getWf2bizConfByApplType";
		try {
			KeyedCollection condi = new KeyedCollection();
			condi.addDataField("appl_type", applType);
			condi.addDataField("scene_scope", scope);
			kcoll = (KeyedCollection)SqlClient.queryFirst(sqlId, condi, null, this.getConnection());
		} catch (Exception e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据申请类型及适用范围获取流程关联业务配置信息[getWf2bizConfByApplType]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return kcoll;
	}
	
	/**
	 * 根据申请类型及适用范围获取流程关联业务配置信息
	 * @param applType 申请类型
	 * @param wfSign 流程标识
	 * @param scope 配置适用范围（999所有、111审批中、997通过、998否决）WF_2BIZ_SCOPE
	 * @return
	 * @throws ComponentException 
	 */
	public KeyedCollection getWf2bizConfByApplTypeAndSign(String applType, String wfSign, String scope) throws ComponentException {
		KeyedCollection kcoll = new KeyedCollection();
		String sqlId = "getWf2bizConfByApplTypeAndSign";
		try {
			KeyedCollection condi = new KeyedCollection();
			condi.addDataField("appl_type", applType);
			condi.addDataField("wf_sign", wfSign);
			condi.addDataField("scene_scope", scope);
			kcoll = (KeyedCollection)SqlClient.queryFirst(sqlId, condi, null, this.getConnection());
		} catch (Exception e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据申请类型及适用范围获取流程关联业务配置信息[getWf2bizConfByApplTypeAndSign]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return kcoll;
	}
	
	/**
	 * 根据节申请类型、点及适用范围获取流程关联业务配置信息
	 * @param applType 申请类型
	 * @param scope 配置适用范围（999所有、111审批中、997通过、998否决）WF_2BIZ_SCOPE
	 * @param nodeId 配置节点ID
	 * @return
	 * @throws ComponentException 
	 */
	public KeyedCollection getWf2bizConfByNode(String applType, String scope, String nodeId) throws ComponentException {
		KeyedCollection kcoll = new KeyedCollection();
		String sqlId = "getWf2bizConfByNode";
		try {
			KeyedCollection condi = new KeyedCollection();
			condi.addDataField("appl_type", applType);
			condi.addDataField("scene_scope", scope);
			condi.addDataField("nodeid", nodeId);
			kcoll = (KeyedCollection)SqlClient.queryFirst(sqlId, condi, null, this.getConnection());
		} catch (Exception e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据节申请类型、点及适用范围获取流程关联业务配置信息[getWf2bizConfByNode]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return kcoll;
	}
	
	/**
	 * 根据节申请类型、点及适用范围获取流程关联业务配置信息
	 * @param applType 申请类型
	 * @param wfSign 流程标识
	 * @param scope 配置适用范围（999所有、111审批中、997通过、998否决）WF_2BIZ_SCOPE
	 * @param nodeId 配置节点ID
	 * @return
	 * @throws ComponentException 
	 */
	public KeyedCollection getWf2bizConfByNodeAndSign(String applType, String wfSign, String scope, String nodeId) throws ComponentException {
		KeyedCollection kcoll = new KeyedCollection();
		String sqlId = "getWf2bizConfByNodeAndSign";
		try {
			KeyedCollection condi = new KeyedCollection();
			condi.addDataField("appl_type", applType);
			condi.addDataField("wf_sign", wfSign);
			condi.addDataField("scene_scope", scope);
			condi.addDataField("nodeid", nodeId);
			kcoll = (KeyedCollection)SqlClient.queryFirst(sqlId, condi, null, this.getConnection());
		} catch (Exception e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据节申请类型、点及适用范围获取流程关联业务配置信息[getWf2bizConfByNodeAndSign]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return kcoll;
	}
	
	/**
	 * <p>获取流程关联业务配置信息</p>
	 * 使用以下三个组合条件匹配，优先级依次从高到低；一旦匹配成功，立即返回并停止。三个组合条件如下：<br>
	 * <li>1.申请类型 + 节点ID + 指定范围
	 * <li>2.申请类型 + 指定范围
	 * <li>3.申请类型 + 范围【所有999】
	 * @param applType 申请类型
	 * @param nodeId 当前节点Id
	 * @param sceneScope 配置适用范围
	 * @return
	 */
	public WfiWorkflow2biz getWf2bizConf(String applType, String nodeId, String sceneScope) throws ComponentException {
		
		WfiWorkflow2biz workflow2biz = new WfiWorkflow2biz();
		workflow2biz.setApplType(applType);
		workflow2biz.setSceneScope(sceneScope);
		try {
			String sceneScopeTmp = sceneScope;
			KeyedCollection kcollMain = this.getWf2bizConfByApplType(applType, sceneScopeTmp);
			if(kcollMain==null || kcollMain.getDataValue("appl_type")==null) {
				if(!WorkFlowConstance.WFI_2BIZ_SCOPE_ALL.equals(sceneScopeTmp)){
					sceneScopeTmp = WorkFlowConstance.WFI_2BIZ_SCOPE_ALL;
					kcollMain = this.getWf2bizConfByApplType(applType, sceneScopeTmp);
				}
			}
			KeyedCollection kcollSub = this.getWf2bizConfByNode(applType, sceneScopeTmp, nodeId);
			boolean fromNode = false; //是否从节点字表获取URL
			if(kcollSub!=null && applType.equals(kcollSub.getDataValue("appl_type"))) {
				fromNode = true;
			}
			
			if(!fromNode && (kcollMain==null||kcollMain.getDataValue("appl_type")==null)) {
				throw new ComponentException("申请类型["+applType+"]获取流程关联业务配置失败。请确定是否设置好流程关联业务配置！");
			}
			String appUrl, bizUrl;
			if(fromNode) {
				appUrl = (String) kcollSub.getDataValue("node_app_url");
				//当节点关联配置取值失败，取主表
				if(appUrl==null || "".equals(appUrl.trim())) {
					appUrl = (String) kcollMain.getDataValue("wf_app_url");
				}
				bizUrl = (String) kcollSub.getDataValue("node_biz_url");
				if(bizUrl==null || "".equals(bizUrl.trim())) {
					bizUrl = (String) kcollMain.getDataValue("wf_biz_url");
				}
			} else {
				appUrl = (String) kcollMain.getDataValue("wf_app_url");
				bizUrl = (String) kcollMain.getDataValue("wf_biz_url");
			}
			workflow2biz.setAppUrl(appUrl);
			workflow2biz.setBizUrl(bizUrl);
			workflow2biz.setPreventList((String)kcollMain.getDataValue("prevent_list"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException(e);
		}
		return workflow2biz;
	}
	
	/**
	 * <p>获取流程关联业务配置信息</p>
	 * 使用以下三个组合条件匹配，优先级依次从高到低；一旦匹配成功，立即返回并停止。三个组合条件如下：<br>
	 * <li>1.申请类型 + 节点ID + 指定范围
	 * <li>2.申请类型 + 指定范围
	 * <li>3.申请类型 + 范围【所有999】
	 * @param wfSign 流程标识
	 * @param nodeId 当前节点Id
	 * @param sceneScope 配置适用范围
	 * @return
	 */
	public WfiWorkflow2biz getWf2bizConfBySign(String applType, String wfSign, String nodeId, String sceneScope) throws ComponentException {
		
		WfiWorkflow2biz workflow2biz = new WfiWorkflow2biz();
		workflow2biz.setApplType(applType);
		workflow2biz.setSceneScope(sceneScope);
		try {
			String sceneScopeTmp = sceneScope;
			KeyedCollection kcollMain = this.getWf2bizConfByApplTypeAndSign(applType, wfSign, sceneScopeTmp);
			if(kcollMain==null || kcollMain.getDataValue("appl_type")==null) {
				if(!WorkFlowConstance.WFI_2BIZ_SCOPE_ALL.equals(sceneScopeTmp)){
					sceneScopeTmp = WorkFlowConstance.WFI_2BIZ_SCOPE_ALL;
					kcollMain = this.getWf2bizConfByApplTypeAndSign(applType, wfSign, sceneScopeTmp);
				}
			}
			KeyedCollection kcollSub = this.getWf2bizConfByNodeAndSign(applType, wfSign, sceneScopeTmp, nodeId);
			boolean fromNode = false; //是否从节点字表获取URL
			if(kcollSub!=null && applType.equals(kcollSub.getDataValue("appl_type"))) {
				fromNode = true;
			}
			
			if(!fromNode && (kcollMain==null||kcollMain.getDataValue("appl_type")==null)) {
				throw new ComponentException("申请类型["+applType+"]、流程标识["+wfSign+"]获取流程关联业务配置失败。请确定是否设置好流程关联业务配置！");
			}
			String appUrl, bizUrl;
			if(fromNode) {
				appUrl = (String) kcollSub.getDataValue("node_app_url");
				//当节点关联配置取值失败，取主表
				if(appUrl==null || "".equals(appUrl.trim())) {
					appUrl = (String) kcollMain.getDataValue("wf_app_url");
				}
				bizUrl = (String) kcollSub.getDataValue("node_biz_url");
				if(bizUrl==null || "".equals(bizUrl.trim())) {
					bizUrl = (String) kcollMain.getDataValue("wf_biz_url");
				}
			} else {
				appUrl = (String) kcollMain.getDataValue("wf_app_url");
				bizUrl = (String) kcollMain.getDataValue("wf_biz_url");
			}
			workflow2biz.setAppUrl(appUrl);
			workflow2biz.setBizUrl(bizUrl);
			workflow2biz.setPreventList((String)kcollMain.getDataValue("prevent_list"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException(e);
		}
		return workflow2biz;
	}
	
	
	/**
	 * <p>流程结束后将接入层的数据迁移到历史表中</p>
	 * @param instanceId 流程实例号
	 * @param connection 数据库连接
	 * @return true执行成功，false执行失败
	 * @throws ComponentException 
	 */
	public boolean transDataWfi(String instanceId, Connection connection) throws ComponentException {
		boolean result = false;
		String insertWJHis = "insertWfiJoinHis";
		String deleteWJ = "deleteWfiJoin";
		String insertWBVHis = "insertWfiBizVarHis";
		String deleteWBV = "deletetWfiBizVar";
		String insertWMQHis = "insertWfiMsgQueueHis";
		String deleteWMQ = "deleteWfiMsgQueue";
		int count = 0;
		try {
			count += SqlClient.insert(insertWJHis, instanceId, null, connection);
			count += SqlClient.delete(deleteWJ, instanceId, connection);
			SqlClient.insert(insertWBVHis, instanceId, null, connection);
			SqlClient.delete(deleteWBV, instanceId, connection);
			SqlClient.insert(insertWMQHis, instanceId, null, connection);
			SqlClient.delete(deleteWMQ, instanceId, connection);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException(e);
		}
		result = count==2?true:false;
		return result;
	}
	
	/**
	 * 根据消息id查询
	 * @param msgId
	 * @return
	 * @throws ComponentException
	 */
	public WfiMsgQueue queryWfiMsgQueueById(String msgid) throws ComponentException{
		WfiMsgQueue rslMsg = null;
		String sqlId = "queryWfiMsgQueueById";
		try {
			rslMsg = (WfiMsgQueue)SqlClient.queryFirst(sqlId, msgid, null, this.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ComponentException(e);
		}
		return rslMsg;
	}
	
	/**
	 * 根据消息id查询历史
	 * @param msgId
	 * @return
	 * @throws ComponentException
	 */
	public WfiMsgQueue queryWfiMsgQueueHisById(String msgid) throws ComponentException{
		WfiMsgQueue rslMsg = null;
		String sqlId = "queryWfiMsgQueueHisById";
		try {
			rslMsg = (WfiMsgQueue)SqlClient.queryFirst(sqlId, msgid, null, this.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ComponentException(e);
		}
		return rslMsg;
	}
	
	/**
	 * <p>异常业务处理成功，更新消息处理状态为‘结束’</p>
	 * @param msgId 消息ID
	 * @param his 是否历史表
	 * @return
	 * @throws ComponentException
	 */
	public boolean updateWfiMesQueue(WfiMsgQueue wfiMsgQueue, boolean his) throws ComponentException{
		int c = 0;
		String sqlId = "updateWfiMsgQueue";
		if(his) {
			sqlId = "updateWfiMsgQueueHis";
		}
		String msgId = wfiMsgQueue.getMsgid();
		try {
			c = SqlClient.update(sqlId, msgId, wfiMsgQueue, null, this.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ComponentException(e);
		}
		return c == 1?true:false;
	}
	
	/**
	 * <p>根据用户与申请类型查询当前有效的工作委托设置</p>
	 * @param userId 用户Id
	 * @param applType 申请类型
	 * @return icoll
	 * @throws ComponentException
	 */
	public IndexedCollection queryEntrustByUser(String userId, String applType) throws ComponentException {
		IndexedCollection icoll = new IndexedCollection();
		String sqlId = "queryEntrustByUser";
		KeyedCollection paramKcoll = new KeyedCollection();
		paramKcoll.put("userId", userId);
		paramKcoll.put("applType", applType);
		String nowTime = TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss");
		paramKcoll.put("nowTime", nowTime);
		try {
			icoll = SqlClient.queryList4IColl(sqlId, paramKcoll, getConnection());
		} catch (SQLException e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "根据用户与申请类型查询当前有效的工作委托设置[queryEntrustByUser]出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		
		return icoll;
	}

	/**
	 * 按节点、办理人分组查询流程审批变更列表
	 * @param instanceId 流程实例号
	 * @return list
	 * @throws ComponentException 
	 */
	public List<WfiBizVarRecordVO> queryWfiBizVarByNode(String instanceId) throws ComponentException {
		List<WfiBizVarRecordVO> bizVarList = new ArrayList<WfiBizVarRecordVO>();
		String sqlId = "queryWfiBizVarByNode";
		String sqlIdHis = "queryWfiBizVarByNodeHis";
		try {
			bizVarList = (List<WfiBizVarRecordVO>) SqlClient.queryList(sqlId, instanceId, getConnection());
			if(bizVarList==null || bizVarList.size()==0) {
				bizVarList = (List<WfiBizVarRecordVO>) SqlClient.queryList(sqlIdHis, instanceId, getConnection());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException(e);
		}
		
		return bizVarList;
	}
	
	/**
	 * 处理带参数的URL
	 * @param oriUrl 带参数的URL,参数形式为pkVal=${pk_value}
	 * @param instanceId 流程实例号（instanceId与wfiJoin至少有一个不为null）
	 * @param wfiJoin 信贷流程接入中间表kcoll（instanceId与wfiJoin至少有一个不为null）
	 * @return
	 * @throws ComponentException
	 */
	public String processURLParam(String oriUrl, String instanceId, KeyedCollection wfiJoin) throws ComponentException {
		String destUrl = oriUrl;
		try {
			if(wfiJoin!=null && !wfiJoin.isEmpty()) {
				
			} else {
				wfiJoin = queryWfiJoin(instanceId);
				if(wfiJoin==null || wfiJoin.isEmpty() || wfiJoin.getDataValue("instanceid")==null)
					wfiJoin = queryWfiJoinHis(instanceId);
			}
			/**
			 * 参数处理。参数形式为pkVal=${pk_value}，pkVal为url中的参数名称，pk_value则为context中某个属性值或者接入表wfi_join的某个字段名称
			 */
			Map<String, String> paramMap = new HashMap<String, String>();
			Context context = this.getContext();
			//将context中的参数值设置进map
			Iterator iterator = context.keySet().iterator();
			while(iterator.hasNext()) {
				String id = (String) iterator.next();
				try {
					String value = (String) context.getDataValue(id); //只取DataField
					paramMap.put(id, value);
				} catch (Exception e) {
				}
			}
			Context parentConx = context.getParent();
			Iterator itr = parentConx.keySet().iterator();
			while(itr.hasNext()) {
				String id = (String) itr.next();
				try {
					String value = (String) parentConx.getDataValue(id); //只取DataField
					paramMap.put(id, value);
				} catch (Exception e) {
				}
			}
			//将接入表设置进map
			Iterator iteratorwj = wfiJoin.keySet().iterator();
			while(iteratorwj.hasNext()) {
				String id = (String) iteratorwj.next();
				String value = "";
				if(wfiJoin.getDataValue(id) instanceof BigDecimal){
					value = String.valueOf(wfiJoin.getDataValue(id));
				}else {
					value = (String) wfiJoin.getDataValue(id);
				}
				paramMap.put(id, value);
			}
			destUrl = WorkFlowUtil.processURLParam(oriUrl, paramMap);
		} catch (Exception e) {
			EMPLog.log(WorkFlowConstance.WFI_COMPONENTID, EMPLog.ERROR, EMPLog.ERROR, "处理URL参数出错。异常信息："+e.getMessage(), e);
			throw new ComponentException(e);
		}
		return destUrl;
	}
	
	
    //获取当前最新一次操作的流程意见ID，用于保存到业务变更表WfiBizVarRecord中，页面整合使用
	public String getCurrentCommId(String instanceId, String currentUserId,
			Connection connection, String nodeId) throws Exception {
		String commentid = null;
		try {
			//修改提示：从意见表中查询出当前流程 节点 本办理人的操作id为空的最新的一条意见
			String SqlStr = "select commentID from wf_comment where instanceid='"
				+ instanceId
				+ "' and nodeid='"
				+ nodeId
				+ "' and userid='"
				+ currentUserId
				+ "' and NodeActionID is null order by commentTime desc";
			Vector vecData = DbControl.getInstance().performQuery(SqlStr,
					connection);
			if (vecData != null && vecData.size() > 0) {
				commentid = (String) ((Vector) vecData.get(0)).get(0);
			}
		} catch (Exception e) {
			WfLog.runtimeException(this, "setComment", e);
			throw e;
		}
		return commentid;
	}
	
	
}
