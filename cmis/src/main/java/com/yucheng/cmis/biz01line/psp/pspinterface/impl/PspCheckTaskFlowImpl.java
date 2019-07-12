package com.yucheng.cmis.biz01line.psp.pspinterface.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

public class PspCheckTaskFlowImpl extends CMISComponent implements
		BIZProcessInterface {

	private static final String modelId = "PspGuarantyValueReeval";//担保品价值重估表（贷后）
	private static final String valueModelId = "MortGuarantyEvalValue";//评估价值信息
	
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行打回处理逻辑

	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行拿回处理逻辑

	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		/** 审批同意时执行业务处理逻辑 */
		String task_id = "";
		/**modified by lisj 2015-1-21 需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量） begin**/
		try {
			task_id = wfiMsg.getPkValue();
			String app_type = wfiMsg.getApplType();
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			if(!app_type.equals("058")){
				//任务中有对担保品进行重估，审批通过修改担保品的评估价值及评估方式			
				String condition = " where task_id = '"+task_id+"'";
				IndexedCollection iColl = dao.queryList(modelId, null, condition, this.getConnection());
				for(int i=0;i<iColl.size();i++){
					KeyedCollection kColl = (KeyedCollection) iColl.get(i);
					String guaranty_no = (String) kColl.getDataValue("guaranty_no");
					String condition1 = " where guaranty_no='"+guaranty_no+"'";
					KeyedCollection valueKColl = dao.queryFirst(valueModelId, null, condition1, this.getConnection());
					valueKColl.setDataValue("eval_amt", kColl.getDataValue("reeval_value"));
					valueKColl.setDataValue("eval_type", "0300");//评估类型--内部评估
					dao.update(valueKColl, this.getConnection());
				}
				
				//处理预警信息
				KeyedCollection kCollTask = dao.queryDetail(wfiMsg.getTableName(), task_id, this.getConnection());
				String check_type = (String)kCollTask.getDataValue("check_type");//检查类型
				String task_type = (String)kCollTask.getDataValue("task_type");//任务类型
				//常规检查、专项检查时才有预警信息
				if(("02".equals(check_type)&&"01".equals(task_type))||("02".equals(check_type)&&"02".equals(task_type))
					||("02".equals(check_type)&&"03".equals(task_type))||("02".equals(check_type)&&"05".equals(task_type))||("02".equals(check_type)&&"06".equals(task_type))
					||("03".equals(check_type)&&"01".equals(task_type))||("03".equals(check_type)&&"02".equals(task_type))
					||("03".equals(check_type)&&"03".equals(task_type))||("03".equals(check_type)&&"05".equals(task_type))||("03".equals(check_type)&&"06".equals(task_type))){
					String condAlt = "where task_id='"+task_id+"'";
					IndexedCollection iCollAlt = dao.queryList("PspAppAltSignal", condAlt, this.getConnection());
					for(int i=0;i<iCollAlt.size();i++){
						KeyedCollection kCollAlt = (KeyedCollection)iCollAlt.get(i);
						kCollAlt.setName("PspAltSignal");
						kCollAlt.put("signal_status", "1");//状态置为“生效”
						dao.insert(kCollAlt, this.getConnection());
					}
				}else if("02".equals(check_type) && "09".equals(task_type)){
					String conditionStr = "where major_task_id='"+task_id+"'";
					IndexedCollection PBTR = dao.queryList("PspBatchTaskRel",null ,conditionStr,this.getConnection());
					for(Object obj4PBTR:PBTR){
						KeyedCollection kColl = (KeyedCollection)obj4PBTR;
						KeyedCollection PCT = dao.queryDetail(wfiMsg.getTableName(), kColl.getDataValue("sub_task_id").toString(), this.getConnection());
						    PCT.put("approve_status", "997");
							dao.update(PCT, this.getConnection());
						}
					}
			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 start */
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//			KeyedCollection PCT = dao.queryDetail(wfiMsg.getTableName(), task_id, this.getConnection());
//			String check_type = (String)PCT.getDataValue("check_type");//检查类型
//			String task_type = (String)PCT.getDataValue("task_type");//任务类型
//			if("02".equals(check_type) && "09".equals(task_type)){
//				String conditionStr = "where major_task_id='"+task_id+"'";
//				IndexedCollection PBTR = dao.queryList("PspBatchTaskRel",null ,conditionStr,this.getConnection());
//				for(Object obj4PBTR:PBTR){
//					KeyedCollection kColl = (KeyedCollection)obj4PBTR;
//					KeyedCollection retKColl = new KeyedCollection();
//					KeyedCollection kColl4trade = new KeyedCollection();
//					kColl4trade.put("CLIENT_NO", "");
//					kColl4trade.put("BUSS_SEQ_NO", "");
//					kColl4trade.put("TASK_ID",  kColl.getDataValue("sub_task_id").toString());
//					try{
//						retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, this.getContext(), this.getConnection());	//调用影像锁定接口
//					}catch(Exception e){
//						throw new Exception("影像锁定接口失败!");
//					}
//					if(!TagUtil.haveSuccess(retKColl, this.getContext())){
//						//交易失败信息
//						throw new Exception("影像锁定接口失败!");
//					}
//				}
//			}else{	
//				KeyedCollection retKColl = new KeyedCollection();
//				KeyedCollection kColl4trade = new KeyedCollection();
//				kColl4trade.put("CLIENT_NO", "");
//				kColl4trade.put("BUSS_SEQ_NO", "");
//				kColl4trade.put("TASK_ID", task_id);
//				try{
//					retKColl = serviceRel.tradeIMAGELOCKED(kColl4trade, this.getContext(), this.getConnection());	//调用影像锁定接口
//				}catch(Exception e){
//					throw new Exception("影像锁定接口失败!");
//				}
//				if(!TagUtil.haveSuccess(retKColl, this.getContext())){
//					//交易失败信息
//					throw new Exception("影像锁定接口失败!");
//				}
//			}
			/* added by yangzy 2014/10/21 授信通过增加影响锁定 end */
			/**modified by lisj 2015-1-21 需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量） end**/
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("流程结束业务处理异常，请检查业务处理逻辑！错误描述："+e.getMessage());
		}
	}

	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行业务处理逻辑

	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {
		// 审批否决时执行业务处理逻辑
		/**modified by lisj 2015-1-21 需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量） begin**/
		try {
			String task_id = wfiMsg.getPkValue();
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kCollTask = dao.queryDetail(wfiMsg.getTableName(), task_id, this.getConnection());
			String check_type = (String)kCollTask.getDataValue("check_type");//检查类型
			String task_type = (String)kCollTask.getDataValue("task_type");//任务类型
			if("02".equals(check_type) && "09".equals(task_type)){
				String conditionStr = "where major_task_id='"+task_id+"'";
				IndexedCollection PBTR = dao.queryList("PspBatchTaskRel",null ,conditionStr,this.getConnection());
				for(Object obj4PBTR:PBTR){
					KeyedCollection kColl = (KeyedCollection)obj4PBTR;
					KeyedCollection PCT = dao.queryDetail(wfiMsg.getTableName(), kColl.getDataValue("sub_task_id").toString(), this.getConnection());
					    PCT.put("approve_status", "992");
						dao.update(PCT, this.getConnection());
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("流程结束业务处理异常，请检查业务处理逻辑！错误描述："+e.getMessage());
		}
		/**modified by lisj 2015-1-21 需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量） end**/
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,
			String pkVal, KeyedCollection modifyVar) throws EMPException {
		try { 
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		String cus_id = (String)kc.getDataValue("cus_id");
		/**add by lisj 2015-6-2 需求编号：XD150504034 贷后管理常规检查任务改造 begin**/
		String task_type = (String)kc.getDataValue("task_type");
		/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
		String IsTeam="";
		KeyedCollection kColl4STO = new KeyedCollection();
		try {
			kColl4STO = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByMemNo", manager_id, null, this.getConnection());
		} catch (SQLException e) {}
		if(kColl4STO != null && kColl4STO.getDataValue("team_no")!=null && !"".equals(kColl4STO.getDataValue("team_no"))){		
			IsTeam="yes";
		}else{
			IsTeam="no";
		}
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
		CusBase Cus = serviceCus.getCusBaseByCusId(cus_id, this.getContext(), this.getConnection());
		String belgLine = Cus.getBelgLine();
		 
		Map<String, String> param = new HashMap<String, String>();
		param.put("IsTeam", IsTeam);
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		param.put("task_type", task_type);
		param.put("cus_id", cus_id);
		param.put("bizline", belgLine);
		/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
		/**add by lisj 2015-6-2 需求编号：XD150504034 贷后管理常规检查任务改造 end**/
		return param;
		}catch (Exception e) {
			throw new EMPException("设置业务数据至流程变量之中出错，错误描述："+e.getMessage());
		}
	}

}
