package com.yucheng.cmis.biz01line.cus.op.cuscom;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.cuscom.component.ModifyHistoryComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class UpdateCusComRecordOp extends CMISOperation {

	//模型ID
	private final String modelId = "CusCom";
	private final String modelIdBase = "CusBase";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = "";
		try {
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			KeyedCollection kCollBase = null;
			
			try {
				kColl = (KeyedCollection) context.getDataElement(modelId);
				kCollBase = (KeyedCollection) context.getDataElement(modelIdBase);
			} catch (Exception e) {}
			
			if (kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update[" + modelId + "] cannot be empty!");
			if (kCollBase == null || kCollBase.size() == 0)
				throw new EMPJDBCException("The values to update[" + modelIdBase + "] cannot be empty!");

			String cus_id = (String) kCollBase.getDataValue("cus_id");
			kColl.addDataField("cus_id", cus_id);
			TableModelDAO dao = this.getTableModelDAO(context);
			//=========修改日志记录 add by xukaixi========================
			KeyedCollection oldKColl = dao.queryAllDetail(modelId, cus_id,connection);
			KeyedCollection oldKCollBase = dao.queryAllDetail(modelIdBase, cus_id,connection);
			//历史记录
			ModifyHistoryComponent historyComponent = (ModifyHistoryComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.MODIFY_HISTORY_COMPONENT,context, connection);
			HttpServletRequest request = (HttpServletRequest)context.getDataValue(EMPConstance.SERVLET_REQUEST);
			String userIP = request.getRemoteAddr();
			//保存com表历史
			//先根据配置进行字段过滤
			KeyedCollection oldKColl4Save = historyComponent.getCfgedKColl(modelId, (KeyedCollection)oldKColl.clone());
			KeyedCollection newKColl4Save = historyComponent.getCfgedKColl(modelId, (KeyedCollection)kColl.clone());
			if((oldKColl4Save!=null&&oldKColl4Save.size()>0)||(newKColl4Save!=null&&newKColl4Save.size()>0)){
				historyComponent.recordHistoryModify(oldKColl4Save, newKColl4Save, modelId, userIP);
			}
			//保存base表历史
			KeyedCollection oldKCollBase4Save = historyComponent.getCfgedKColl(modelIdBase, (KeyedCollection)oldKCollBase.clone());
			KeyedCollection newKCollBase4Save = historyComponent.getCfgedKColl(modelIdBase, (KeyedCollection)kCollBase.clone());
			if((oldKCollBase4Save!=null&&oldKCollBase4Save.size()>0)||(newKCollBase4Save!=null&&newKCollBase4Save.size()>0)){
				historyComponent.recordHistoryModify(oldKCollBase4Save, newKCollBase4Save, modelIdBase, userIP);
			}
			//============end  add====================

//			kColl.addDataField("cus_id", cus_id);
			kCollBase.addDataField("last_update_date", context.getDataValue("OPENDAY"));
			int count = dao.update(kColl, connection);
			if (count != 1) {
				throw new EMPException("更新企业客户信息失败");
			}
			count = dao.update(kCollBase, connection);
			if (count != 1) {
				throw new EMPException("更新企业客户信息失败");
			}
			ComponentHelper cHelper = new ComponentHelper();
			CusCom cusCom = new CusCom();
			CusBase cusBase = new CusBase();
			cusCom = (CusCom) cHelper.kcolTOdomain(cusCom, kColl);
			cusBase = (CusBase) cHelper.kcolTOdomain(cusBase, kCollBase);
//			CusLoanRelComponent cusLoanRelComponent = (CusLoanRelComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSLOANREL, context, connection);
//			List<CMISDomain> cusLoanRelList = null;
//			String cusLoanRelCustMgr = cusCom.getCustMgr();
//			if(cusLoanRelCustMgr==null) cusLoanRelCustMgr="";
//			if(cusLoanRelCustMgr.equals("")) cusLoanRelCustMgr=(String)context.getDataValue(PUBConstant.currentUserId);
//			cusLoanRelList = cusLoanRelComponent.queryCusLoanRelByCondition(" where cus_id='"+cusCom.getCusId()+"' and main_cus_mgr='"+cusLoanRelCustMgr+"' ");
//			if (cusLoanRelList == null || cusLoanRelList.size()<1) {
//				cusLoanRelComponent.addCusLoanRel(cusCom, modelId);
//			} else {
//				CusLoanRel cusLoanRel = null;
//				for(int i=0;i<cusLoanRelList.size();i++){
//					cusLoanRel = (CusLoanRel)cusLoanRelList.get(i);
//					cusLoanRel.setBankFlg("1");
//					cusLoanRel.setAgriFlg("2");// 是否农户（对公的都是非农户）
//					cusLoanRel.setCusName(cusCom.getCusName());// 客户名称
//					cusLoanRel.setCusType(cusCom.getCusType());// 客户类型
//					cusLoanRel.setCertType(cusCom.getCertType());// 证件类型
//					cusLoanRel.setCertCode(cusCom.getCertCode());// 证件号码
//					cusLoanRel.setAreaCode(cusCom.getRegStateCode());// 区域编号
//					cusLoanRel.setAreaName(cusCom.getRegAreaName());// 区域名称
//					cusLoanRel.setOutCusId(cusCom.getCusId());// 外部客户代码
//					cusLoanRel.setCusStatus(cusCom.getCusStatus());// 客户操作状态
//					String result = cusLoanRelComponent.updateCusLoanRel(cusLoanRel, modelId);
//					if(!result.equals(CMISMessage.ADDSUCCEESS)){
//						throw new ComponentException("更新客户信息表失败");
//					}
//				}
//			}

//			if (cusCom.getComGrpMode() != null&& !cusCom.getComGrpMode().trim().equals("")) {
//				/**
//				 * 根据集团客户类型去更新集团客户表
//				 * */
//				String grpMode = cusCom.getComGrpMode().trim();
//				String grpNo = cusCom.getGrpNo().trim();
//				String oldGrpMode = (String) kColl.getDataValue("old_com_grp_mode");
//				String oldGrpNo = (String) kColl.getDataValue("old_grp_no");
//				if (grpMode.equals("1") && oldGrpMode.trim().equals("2")) {
//					// 原先是集团子公司，但现在选为集团客户母公司
//					CusGrpMemberComponent cgmc = (CusGrpMemberComponent) CMISComponentFactory.getComponentFactoryInstance()
//							.getComponentInstance(PUBConstant.CUSGRPMEMBER,context, connection);
//					cgmc.removeCusGrpMember(oldGrpNo, cusCom.getCusId());
//
//					CusGrpInfoComponent cgic = (CusGrpInfoComponent) CMISComponentFactory.getComponentFactoryInstance()
//							.getComponentInstance(PUBConstant.CUSGRPINFO,context, connection);
//
//					grpNo = CMISSequenceService4JXXD.querySequenceFromDB("CUSGRP", "all", 
//			        		connection, context);
//					CusGrpInfo cusGrpInfo = new CusGrpInfo();
//					cusGrpInfo.setGrpNo(grpNo);
//					cusGrpInfo.setGrpName(cusCom.getCusName());
//					cusGrpInfo.setParentCusId(cusCom.getCusId());
//					cusGrpInfo.setParentCusName(cusCom.getCusName());
//					cusGrpInfo.setParentLoanCard(cusCom.getLoanCardId());
//					cusGrpInfo.setParentOrgCode(cusCom.getComInsCode());
//					cusGrpInfo.setGrpFinanceType("01");
//					cusGrpInfo.setGrpDetail("");
//					cusGrpInfo.setManagerBrId(cusCom.getMainBrId());
//					cusGrpInfo.setManagerId(cusCom.getCustMgr());
//					cusGrpInfo.setInputId(cusCom.getInputId());
//					cusGrpInfo.setInputDate(cusCom.getInputDate());
//					//cusGrpInfo.setInputBrId(cusCom.getInputBrId());
//
//					cgic.addCusGrpInfo(cusGrpInfo);
//				} else if (grpMode.equals("2") && oldGrpMode.trim().equals("1")) {
//					// 原先是集团客户母公司，但现在选为集团客户子公司
//					CusGrpInfoComponent cgic = (CusGrpInfoComponent) CMISComponentFactory
//							.getComponentFactoryInstance()
//							.getComponentInstance(PUBConstant.CUSGRPINFO,
//									context, connection);
//					cgic.delCusGrpInfoByCusId(cusCom.getCusId());
//
//					CusGrpMemberComponent cgmc = (CusGrpMemberComponent) CMISComponentFactory
//							.getComponentFactoryInstance()
//							.getComponentInstance(PUBConstant.CUSGRPMEMBER,
//									context, connection);
//					CusGrpMember cgm = new CusGrpMember();
//					cgm.setGrpNo(grpNo);
//					cgm.setCusId(cusCom.getCusId());
//					cgm.setCusName(cusCom.getCusName());
//					cgm.setGrpCorreType("2");
//					cgm.setGrpCorreDetail("");
//					cgm.setMainBrId(cusCom.getMainBrId());
//					cgm.setCusManager(cusCom.getCustMgr());
//					cgm.setInputUserId(cusCom.getInputId());
//					cgm.setInputDate(cusCom.getInputDate());
//					cgm.setInputBrId(cusCom.getInputBrId());
//					cgmc.addCusGrpMember(cgm);
//				} else if (grpMode.equals("2") && oldGrpMode.trim().equals("2")&& !grpNo.equals(oldGrpNo.trim())) {
//					// 原先是集团客户子公司，现在也是集团客户子公司，但所属集团不一样
//					CusGrpMemberComponent cgmc = (CusGrpMemberComponent) CMISComponentFactory
//							.getComponentFactoryInstance()
//							.getComponentInstance(PUBConstant.CUSGRPMEMBER,
//									context, connection);
//					cgmc.removeCusGrpMember(oldGrpNo, cusCom.getCusId());
//
//					CusGrpMember cgm = new CusGrpMember();
//					cgm.setGrpNo(grpNo);
//					cgm.setCusId(cusCom.getCusId());
//					cgm.setCusName(cusCom.getCusName());
//					cgm.setGrpCorreType("2");
//					cgm.setGrpCorreDetail("");
//					cgm.setMainBrId(cusCom.getMainBrId());
//					cgm.setCusManager(cusCom.getCustMgr());
//					cgm.setInputUserId(cusCom.getInputId());
//					cgm.setInputDate(cusCom.getInputDate());
//					cgm.setInputBrId(cusCom.getInputBrId());
//					cgmc.addCusGrpMember(cgm);
//				} else if (grpMode.equals("9") && oldGrpMode.trim().equals("1")) {
//					// 原先是集团母公司，但现在选为非集团客户
//					CusGrpInfoComponent cgic = (CusGrpInfoComponent) CMISComponentFactory
//							.getComponentFactoryInstance()
//							.getComponentInstance(PUBConstant.CUSGRPINFO,
//									context, connection);
//					cgic.delCusGrpInfoByCusId(cusCom.getCusId());
//				} else if (grpMode.equals("9") && oldGrpMode.trim().equals("2")) {
//					// 原先是集团子公司，但现在选为非集团客户
//					CusGrpMemberComponent cgmc = (CusGrpMemberComponent) CMISComponentFactory
//							.getComponentFactoryInstance()
//							.getComponentInstance(PUBConstant.CUSGRPMEMBER,
//									context, connection);
//					cgmc.removeCusGrpMember(oldGrpNo, cusCom.getCusId());
//				} else if (grpMode.equals("1")
//						&& (oldGrpMode == null || oldGrpMode.trim().equals("9") || oldGrpMode
//								.trim().equals(""))) {
//					// 原先未选，现在选为集团母公司
//					CusGrpInfoComponent cgic = (CusGrpInfoComponent) CMISComponentFactory
//							.getComponentFactoryInstance()
//							.getComponentInstance(PUBConstant.CUSGRPINFO,
//									context, connection);
//	
//					grpNo = CMISSequenceService4JXXD.querySequenceFromDB("CUSGRP", "all", 
//			        		connection, context);
//					CusGrpInfo cusGrpInfo = new CusGrpInfo();
//					cusGrpInfo.setGrpNo(grpNo);
//					cusGrpInfo.setGrpName(cusCom.getCusName());
//					cusGrpInfo.setParentCusId(cusCom.getCusId());
//					cusGrpInfo.setParentCusName(cusCom.getCusName());
//					cusGrpInfo.setParentLoanCard(cusCom.getLoanCardId());
//					cusGrpInfo.setParentOrgCode(cusCom.getComInsCode());
//					cusGrpInfo.setGrpFinanceType("01");
//					cusGrpInfo.setGrpDetail("");
//					cusGrpInfo.setManagerBrId(cusCom.getMainBrId());
//					cusGrpInfo.setManagerId(cusCom.getCustMgr());
//					cusGrpInfo.setInputId(cusCom.getInputId());
//					cusGrpInfo.setInputDate(cusCom.getInputDate());
//					//cusGrpInfo.setInputBrId(cusCom.getInputBrId());
//
//					cgic.addCusGrpInfo(cusGrpInfo);
//				} else if (grpMode.equals("2")
//						&& (oldGrpMode == null || oldGrpMode.trim().equals("9") || oldGrpMode
//								.trim().equals(""))) {
//					// 原先未选，现在选为集团子公司
//					CusGrpMemberComponent cgmc = (CusGrpMemberComponent) CMISComponentFactory
//							.getComponentFactoryInstance()
//							.getComponentInstance(PUBConstant.CUSGRPMEMBER,
//									context, connection);
//					CusGrpMember cgm = new CusGrpMember();
//					cgm.setGrpNo(grpNo);
//					cgm.setCusId(cusCom.getCusId());
//					cgm.setCusName(cusCom.getCusName());
//					cgm.setGrpCorreType("2");
//					cgm.setGrpCorreDetail("");
//					cgm.setMainBrId(cusCom.getMainBrId());
//					cgm.setCusManager(cusCom.getCustMgr());
//					cgm.setInputUserId(cusCom.getInputId());
//					cgm.setInputDate(cusCom.getInputDate());
//					cgm.setInputBrId(cusCom.getInputBrId());
//					cgmc.addCusGrpMember(cgm);
//				}
//		}
		//===========add by xukaixi===========================
		
//		TableModelDAO daoo = this.getTableModelDAO(context);
//		KeyedCollection cus_base = new KeyedCollection();
//		cus_base.addDataField("cus_id", cusCom.getCusId());
//		cus_base.addDataField("reg_code", cusCom.getRegCode());
//		cus_base.addDataField("cus_type", cusCom.getCusType());
//		//cus_base.addDataField("open_date", context.getDataValue(PUBConstant.OPENDAY));
//
//		cus_base.setName("CusBase");
//		daoo.update(cus_base, connection);
//		KeyedCollection cus_com = new KeyedCollection();
//		cus_com.addDataField("cus_id", cusCom.getCusId());
//		cus_com.addDataField("main_br_id", context.getDataValue(PUBConstant.organNo));
//		cus_com.addDataField("input_id", context.getDataValue(PUBConstant.currentUserId));
//		cus_com.addDataField("cust_mgr", context.getDataValue(PUBConstant.currentUserId));
//		cus_com.setName("CusCom");
//		daoo.update(cus_com, connection);

		//===============end add=====================================
		flag = "success";
		context.addDataField("flag", flag);
		
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
