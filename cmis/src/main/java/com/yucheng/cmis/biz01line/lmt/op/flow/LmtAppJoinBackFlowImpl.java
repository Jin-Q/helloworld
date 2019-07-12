package com.yucheng.cmis.biz01line.lmt.op.flow;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;

public class LmtAppJoinBackFlowImpl extends CMISComponent implements
		BIZProcessInterface {
	
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行打回处理逻辑
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行拿回处理逻辑
	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		String serno = "";
		Context context = this.getContext();
		try {
			Connection connection = this.getConnection();
			serno = wfiMsg.getPkValue();
			
			//获得申请信息
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kCollApp = dao.queryDetail("LmtAppJoinBack", serno, connection);
			String app_flag = (String)kCollApp.getDataValue("app_flag");
			String agr_no = (String)kCollApp.getDataValue("agr_no");
//			String openDate = (String)context.getDataValue("OPENDAY");
			
			//获得申请名单信息
			String condition = " where serno='"+serno+"'";
			IndexedCollection iCollAppNL = dao.queryList("LmtAppNameList", condition, connection);
			if("0".equals(app_flag)){	//入圈申请
				for(int i=0;i<iCollAppNL.size();i++){
					KeyedCollection kCollTemp = (KeyedCollection)iCollAppNL.get(i);
					String cus_id = (String)kCollTemp.getDataValue("cus_id");
					//先查询该客户是否已经在该圈商中（状态为无效）
					String conditionNl = " where agr_no='"+agr_no+"' and cus_id='"+cus_id+"'";
					IndexedCollection iCollNl = dao.queryList("LmtNameList", conditionNl, connection);
					if(iCollNl.size()>0){
						KeyedCollection kCollNl = (KeyedCollection)iCollNl.get(0);
						kCollNl.setDataValue("cus_status", "1");
						dao.update(kCollNl, connection);
					}else {
						//该客户是否设置额度,暂时注掉
//					String is_limit_set = (String)kCollTemp.getDataValue("is_limit_set");
//					if("1".equals(is_limit_set)){
//						String conditionStr = " where cus_id='"+cus_id+"' and serno='"+serno+"'";
//						IndexedCollection iCollAppDet = dao.queryList(modelIdAppDe, conditionStr, connection);
//						for(int j=0;j<iCollAppDet.size();j++){
//							KeyedCollection kCollAppDet = (KeyedCollection)iCollAppDet.get(j);
//							kCollAppDet.addDataField("agr_no", agr_no);
//							kCollAppDet.setDataValue("start_date", openDate);
//							String termType = (String)kCollAppDet.getDataValue("term_type");
//							String termDet = (String)kCollAppDet.getDataValue("term");
//							String endDate = LmtUtils.computeEndDate(openDate, termType, termDet);
//							kCollAppDet.setDataValue("end_date", endDate);
//							kCollAppDet.remove("serno");
//							kCollAppDet.remove("ori_crd_amt");
//							kCollAppDet.remove("org_limit_code");
//							kCollAppDet.setName(modelId);
//							dao.insert(kCollAppDet, connection);
//						}
//					}
						//名单表信息
						kCollTemp.addDataField("agr_no", agr_no);
						kCollTemp.addDataField("cus_status", "1");//客户状态置为有效
						kCollTemp.remove("serno");
						kCollTemp.setName("LmtNameList");
						dao.insert(kCollTemp, connection);
					}
				}
			}else {	//退圈申请，审批通过时将客户状态置为无效，并将该客户在商圈协议下的台账置为终止
				for(int i=0;i<iCollAppNL.size();i++){
					KeyedCollection kCollTmp = (KeyedCollection)iCollAppNL.get(i);
					String cus_id = (String)kCollTmp.getDataValue("cus_id");
					String condition1 = " where cus_id='"+cus_id+"' and agr_no='"+agr_no+"'";
					IndexedCollection iCollNLTmp = dao.queryList("LmtNameList", condition1, connection);
					if(iCollNLTmp.size()>0){
						KeyedCollection kCollNL = (KeyedCollection)iCollNLTmp.get(0);
						kCollNL.setDataValue("cus_status", "2");//将客户状态置为无效
						dao.update(kCollNL, connection);
						String is_limit_set = (String)kCollNL.getDataValue("is_limit_set");
						if("1".equals(is_limit_set)){
							String conditionAgr = " where agr_no='"+agr_no+"' and cus_id='"+cus_id+"'";
							IndexedCollection iCollAgrDe = dao.queryList("LmtAgrDetails", conditionAgr, connection);
							for(int j=0;j<iCollAgrDe.size();j++){
								KeyedCollection kCollAgrDe = (KeyedCollection)iCollAgrDe.get(j);
								kCollAgrDe.setDataValue("lmt_status", "30");//台账状态置为终止
								dao.update(kCollAgrDe, connection);
							}
						}
					}else{
						throw new Exception("客户["+cus_id+"]不在商圈["+agr_no+"]名单中！");
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("流程结束业务处理异常，请检查业务处理逻辑！");
		}
	}

	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,
			String pkVal, KeyedCollection modifyVar) throws EMPException {
		// 设置业务数据至流程变量之中
		Connection conn = this.getConnection();
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, conn);
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
//		try {
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//			OrganizationServiceInterface userService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
//			SUser sUser = userService.getUserByUserId(manager_id, conn);
//			String userBrId = sUser.getOrgid();//责任人所在机构
//			SOrg supOrg = userService.getSupOrg(userBrId, conn);//上级机构
//			param.put("super_org_id", supOrg.getOrganno());
//		} catch (Exception e) {
//			throw new EMPException(e);
//		}
		
		return param;
	}

}