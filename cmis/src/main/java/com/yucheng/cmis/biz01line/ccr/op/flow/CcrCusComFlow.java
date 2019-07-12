package com.yucheng.cmis.biz01line.ccr.op.flow;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.biz01line.lmt.op.lmtagrfinguar.FinGuarUtils;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.platform.workflow.msi.Workflow4BIZIface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class CcrCusComFlow extends CMISComponent implements BIZProcessInterface {

	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {//打回
		// TODO Auto-generated method stub
		Context context = this.getContext();
		Connection connection = null;
		try{
			connection = this.getConnection();
			String serno_value = wfiMsg.getPkValue();
			String is_authorize = "";
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			//根据业务编号获取评级申请信息
			KeyedCollection kColl_detail = dao.queryDetail("CcrAppDetail", serno_value, connection);
			is_authorize = (String) kColl_detail.getDataValue("is_authorize");
			if("1".equals(is_authorize)){//授信项下的评级需要同步更新授信表中的流程状态
				KeyedCollection lmtAppFinGuarKc = dao.queryDetail("LmtAppFinGuar",serno_value,connection);
				lmtAppFinGuarKc.setDataValue("approve_status","992");
				dao.update(lmtAppFinGuarKc, connection);
			}
			
		}catch(Exception e){
			throw new EMPException("评级流程审批报错，错误描述："+e.getMessage());
		}
		
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {//拿回
		// TODO Auto-generated method stub
		Context context = this.getContext();
		Connection connection = null;
		try{
			connection = this.getConnection();
			String serno_value = wfiMsg.getPkValue();
			String is_authorize = "";
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			//根据业务编号获取评级申请信息
			KeyedCollection kColl_detail = dao.queryDetail("CcrAppDetail", serno_value, connection);
			is_authorize = (String) kColl_detail.getDataValue("is_authorize");
			if("1".equals(is_authorize)){//授信项下的评级需要同步更新授信表中的流程状态
				KeyedCollection lmtAppFinGuarKc = dao.queryDetail("LmtAppFinGuar",serno_value,connection);
				lmtAppFinGuarKc.setDataValue("approve_status","992");
				dao.update(lmtAppFinGuarKc, connection);
			}
			
		}catch(Exception e){
			throw new EMPException("评级流程审批报错，错误描述："+e.getMessage());
		}
		
	}

	//流程审批通过
	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		Context context = this.getContext();
		String flag="";//标志位（1--对公客户，2--直接认定的对公客户，4--融资性担保公司，3--同业客户）
		String is_authorize = "";
		Connection connection = null;
		try{
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "客户评级，流程审批通过逻辑处理 start 。。。！", null);
			connection = this.getConnection();
			String serno_value = wfiMsg.getPkValue();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			//根据业务编号获取评级申请信息
			KeyedCollection kColl_info = dao.queryDetail("CcrAppInfo", serno_value, connection);
			//根据业务编号获取评级申请信息
			KeyedCollection kColl_detail = dao.queryDetail("CcrAppDetail", serno_value, connection);
			String cusId = (String) kColl_info.getDataValue("cus_id");
			
			//信用等级
			String adjusted_grade = (String) kColl_detail.getDataValue("adjusted_grade");
			//调用流程接口取流程变更后信用等级，若有变更则取变更后的等级
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			Workflow4BIZIface wfi4biz = (Workflow4BIZIface)serviceJndi.getModualServiceById("workflow4BizService", "workflow");
			HashMap hm = new HashMap();
			hm = (HashMap)wfi4biz.getAllModifiedBizVar(wfiMsg.getInstanceid(), connection);
			String finalGrade = (String)hm.get("adjusted_grade");
			if(finalGrade!=null && !"".equals(finalGrade)){
				adjusted_grade = finalGrade;
			}
			flag = (String) kColl_info.getDataValue("flag");
			//保存时更新客户信息中评级信息和评级时间
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			IqpServiceInterface service1 = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			//当前日期
			String cusGradeDt = context.getDataValue("OPENDAY").toString();
			//取当前日期推后一年的时间
			String nextYear = Integer.toString((Integer.parseInt(cusGradeDt.substring(0,4))+1))+cusGradeDt.substring(4);
			//办结日期
			kColl_info.setDataValue("app_end_date", cusGradeDt);
			//开始日期
			kColl_info.setDataValue("start_date", cusGradeDt);
			//到期日期
			kColl_info.setDataValue("expiring_date", nextYear);
			String guar_bail_multiple=(String) kColl_detail.getDataValue("bail_multi");
            String guar_cls=(String) kColl_detail.getDataValue("guar_type");
			dao.update(kColl_info, connection);
			//监管机构的评级，评级结果需要回写监管机构表
			if(flag.equals("6")){
			    EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "客户评级，流程审批通过逻辑处理【监管机构的评级，评级结果需要回写监管机构表】！", null);
			    service1.updateCdtEval(cusId, adjusted_grade, nextYear, connection);
			/*******个人客户评级审批通过处理方式不需要特殊处理，注释掉下面代码 add by tangzf 2014-02-17********/
//			}else if(flag.equals("5")){
//				CcrServiceInterface service2 = (CcrServiceInterface)serviceJndi.getModualServiceById("ccrServices", "ccr");
//				service2.updateCcrAppInfoIndiv(serno_value, context,connection);
			}else{
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "客户评级，流程审批通过逻辑处理【更新客户表中的评级信息】！", null);
				//更新客户表中的评级信息
				service.updateGrade(cusId, adjusted_grade, nextYear, flag,guar_bail_multiple,guar_cls, connection);
			}
			//授信项下的授信则需要更新授信表里面的申请状态字段。
			is_authorize = (String) kColl_detail.getDataValue("is_authorize");
			if("1".equals(is_authorize)){//授信项下的评级需要同步更新授信表中的流程状态
				//KeyedCollection lmtAppFinGuarKc = dao.queryDetail("LmtAppFinGuar",serno_value,connection);
				//lmtAppFinGuarKc.setDataValue("approve_status","997");
				//dao.update(lmtAppFinGuarKc, connection);
				FinGuarUtils.buildFinGuarAgrInfo(context, serno_value);
			}
		}catch(Exception e){
			throw new EMPException("评级流程审批报错，错误描述："+e.getMessage());
		}
	}

	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {//审批中
		// TODO Auto-generated method stub
		Context context = this.getContext();
		Connection connection = null;
		try{
			connection = this.getConnection();
			String serno_value = wfiMsg.getPkValue();
			String is_authorize = "";
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			//根据业务编号获取评级申请信息
			KeyedCollection kColl_detail = dao.queryDetail("CcrAppDetail", serno_value, connection);
			is_authorize = (String) kColl_detail.getDataValue("is_authorize");
			if("1".equals(is_authorize)){//授信项下的评级需要同步更新授信表中的流程状态
				KeyedCollection lmtAppFinGuarKc = dao.queryDetail("LmtAppFinGuar",serno_value,connection);
				lmtAppFinGuarKc.setDataValue("approve_status","111");
				dao.update(lmtAppFinGuarKc, connection);
			}
			
		}catch(Exception e){
			throw new EMPException("评级流程审批报错，错误描述："+e.getMessage());
		}
	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {//否决
		// TODO Auto-generated method stub
		Context context = this.getContext();
		Connection connection = null;
		try{
			connection = this.getConnection();
			String serno_value = wfiMsg.getPkValue();
			String is_authorize = "";
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			//根据业务编号获取评级申请信息
			KeyedCollection kColl_detail = dao.queryDetail("CcrAppDetail", serno_value, connection);
			is_authorize = (String) kColl_detail.getDataValue("is_authorize");
			if("1".equals(is_authorize)){//授信项下的评级需要同步更新授信表中的流程状态
				KeyedCollection lmtAppFinGuarKc = dao.queryDetail("LmtAppFinGuar",serno_value,connection);
				lmtAppFinGuarKc.setDataValue("approve_status","998");
				dao.update(lmtAppFinGuarKc, connection);
			}
			
		}catch(Exception e){
			throw new EMPException("评级流程审批报错，错误描述："+e.getMessage());
		}
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,
			String pkVal, KeyedCollection modifyVar) throws EMPException {
		Map<String, String> param = new HashMap<String, String>();
		Context context = this.getContext();
		Connection connection = null;
		try {
			// 设置业务数据至流程变量之中
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
			//评级详细信息
			KeyedCollection kc1 = dao.queryDetail("CcrAppDetail", pkVal, this.getConnection());
			String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
			String manager_id = (String)kc.getDataValue("manager_id");
			String IsBranch ;//分行下是否存在支行;Y--存在支行；N--不存在
			String IsAccord;//是否至少符合三项直接认定条件之一 ；Y--符合；N--不符合
			String AdjustedGrade="";//调整后评级结果 ;11--AAA,12--AA,13--A,14--BBB,15--BB,16--B,19--C,17--CCC,18--CC
			String BelgLine="";//所属条线;BL100--公司业务条线,BL200--小微业务条线,BL300--个人业务条线
			String cus_id = (String)kc.getDataValue("cus_id");
			String flag = (String) kc.getDataValue("flag");
			if("2".equals(flag)){//客户直接认定
				String reason_show = (String) kc1.getDataValue("reason_show");
				if("".equals(reason_show)||null==reason_show){
					IsAccord="N";
				}else{
					IsAccord="Y";
				}
				param.put("IsAccord",IsAccord);
			}
			String isIntbank = "";//同业客户时，是否银行业客户。Y--是银行；N--非银行金融业
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();	
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			connection = this.getConnection();
			/**责任人存在多个机构时取责任机构*/
			OrganizationServiceInterface userService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
			List<SOrg> orgslist = userService.getDeptOrgByActno(manager_id, this.getConnection());
			if(orgslist!=null&&orgslist.size()==1){//责任人只有一个机构则取该机构码
				manager_br_id = orgslist.get(0).getOrganno();
			}
			SOrg org_info = userService.getOrgByOrgId(manager_br_id, connection);//根据机构号获取机构信息
			String suporganno = org_info.getSuporganno();//获取主管客户经理所在机构的上级机构
			if(suporganno.equals("9350500000")){//（若上级机构不是泉州分行，则需要走分行行长）
				IsBranch ="N";
			}else{
				IsBranch ="Y";
			}
			//龙岩分行
			String is_space_back = "";
			String organNo = (String)this.getContext().getDataValue("organNo");
			if("9350800000".equals(organNo) || "9350801001".equals(organNo)){
				is_space_back = "Y";
			}else{
				is_space_back = "N";
			}
			if("3".equals(flag)){
				KeyedCollection CusSameOrg = service.getCusSameOrgKcoll(cus_id, context, connection);//同业客户
				String same_org_type = (String)CusSameOrg.getDataValue("same_org_type");//同业机构客户类型
				if(same_org_type != null && "1,2,3,4,5,6,7,10,21,13".indexOf(same_org_type)>0){
					isIntbank = "Y";
				}else{
					isIntbank = "N";
				}
				param.put("isIntbank", isIntbank);
			}
			
			CusBase CusCom = service.getCusBaseByCusId(cus_id, context, connection);//非同业客户
			Workflow4BIZIface service1 = (Workflow4BIZIface)serviceJndi.getModualServiceById("workflow4BizService", "workflow");
			Map<String,String>adjustedGrade = null;
			adjustedGrade =  service1.getAllModifiedBizVarBySerno(pkVal, tabModelId, connection);
			BelgLine = CusCom.getBelgLine();//所属条线赋值
			if(adjustedGrade==null||"".equals(adjustedGrade.get("adjusted_grade"))||null==adjustedGrade.get("adjusted_grade")){//未作评级等级的调整
				AdjustedGrade = (String) kc1.getDataValue("adjusted_grade");
			}else{//有做评级等级的调整
				AdjustedGrade = (String) adjustedGrade.get("adjusted_grade");
			}
			
			param.put("manager_br_id",manager_br_id);
			param.put("manager_id", manager_id);
			param.put("IsBranch",IsBranch);
			param.put("is_space_back",is_space_back);
			
			param.put("AdjustedGrade",AdjustedGrade);
			param.put("BelgLine",BelgLine);
		} catch (Exception e) {
			throw new EMPException("评级流程审批报错，错误描述："+e.getMessage());
		}
		return param;
	}

}
