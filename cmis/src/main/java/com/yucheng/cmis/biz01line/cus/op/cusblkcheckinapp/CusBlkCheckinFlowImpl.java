package com.yucheng.cmis.biz01line.cus.op.cusblkcheckinapp;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

public class CusBlkCheckinFlowImpl extends CMISComponent implements
		BIZProcessInterface {
	
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行打回处理逻辑
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行拿回处理逻辑
	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		String serno = "";
		String tableName = "";
		Context context = this.getContext();
		try {
			Connection connection = this.getConnection();
			serno = wfiMsg.getPkValue();
			tableName = wfiMsg.getTableName();
			
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			if("CusBlkCheckinapp".equals(tableName)){	//不宜贷款户进入申请
				KeyedCollection kCollCheckin = dao.queryDetail("CusBlkCheckinapp", serno, connection);
				//获得客户码，先查询该客户是否已经是不宜贷款户，若存在则直接更新，若不存在则插入
				String cus_id = (String)kCollCheckin.getDataValue("cus_id");
//				String blk_level = (String)kCollCheckin.getDataValue("black_level");//不宜贷款户级别
				IndexedCollection iCollBlkList = dao.queryList("CusBlkList", " where cus_id='"+cus_id+"'", connection);
				if(iCollBlkList.size()>0){
					KeyedCollection kCollBlkList = (KeyedCollection)iCollBlkList.get(0);
					kCollBlkList.setDataValue("black_type", kCollCheckin.getDataValue("black_type"));
					kCollBlkList.setDataValue("black_level", kCollCheckin.getDataValue("black_level"));
					kCollBlkList.setDataValue("data_source", "12");//系统内不宜贷款户（人工审批）
					kCollBlkList.setDataValue("legal_name", kCollCheckin.getDataValue("legal_name"));
					kCollBlkList.setDataValue("legal_phone", kCollCheckin.getDataValue("legal_phone"));
					kCollBlkList.setDataValue("legal_addr", kCollCheckin.getDataValue("legal_addr"));
					kCollBlkList.setDataValue("black_reason", kCollCheckin.getDataValue("black_reason"));
					kCollBlkList.setDataValue("black_date", context.getDataValue("OPENDAY"));
					kCollBlkList.setDataValue("status", "002");//生效状态
					kCollBlkList.setDataValue("street", kCollCheckin.getDataValue("street"));
					kCollBlkList.setDataValue("input_id", kCollCheckin.getDataValue("input_id"));
					kCollBlkList.setDataValue("input_br_id", kCollCheckin.getDataValue("input_br_id"));
					kCollBlkList.setDataValue("input_date", context.getDataValue("OPENDAY"));
					kCollBlkList.setDataValue("manager_id", kCollCheckin.getDataValue("manager_id"));
					kCollBlkList.setDataValue("manager_br_id", kCollCheckin.getDataValue("manager_br_id"));
					dao.update(kCollBlkList, connection);
				}else{
//					String sernoBlkList = CMISSequenceService4JXXD.querySequenceFromDB("CUS", "all", connection, context);//生成黑名单表流水号
//					kCollCheckin.setDataValue("serno", sernoBlkList);
					kCollCheckin.addDataField("black_date", context.getDataValue("OPENDAY"));
					kCollCheckin.addDataField("status", "002");//生效状态
					kCollCheckin.addDataField("data_source", "12");//系统内不宜贷款户（人工审批）
					//移除多余字段
					kCollCheckin.remove("remarks");
					kCollCheckin.remove("approve_status");
					kCollCheckin.setName("CusBlkList");
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
					CusBase cusBase = serviceCus.getCusBaseByCusId(cus_id, context, connection);
					String cus_name = cusBase.getCusName();
					kCollCheckin.addDataField("cus_name", cus_name);
					//插入黑名单表
					dao.insert(kCollCheckin, connection);
				}
//				if(cus_id!=null&&!"".equals(cus_id)&&"3".equals(blk_level)){
//					//调用授信模块接口，将额度置为不可用(禁止类)
//					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//					LmtServiceInterface serviceLmt = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
//					serviceLmt.updateLmtUnUse("1", cus_id, null, null, connection);
//				}
			}else if("CusBlkLogoutapp".equals(tableName)){	//不宜贷款户注销
				KeyedCollection kCollLogout = dao.queryDetail("CusBlkLogoutapp", serno, connection);
				String cus_id = (String)kCollLogout.getDataValue("cus_id");//客户码
				KeyedCollection kCollBlk = dao.queryDetail("CusBlkList", cus_id, connection);
				kCollBlk.setDataValue("logout_reason", kCollLogout.getDataValue("logout_reason"));
				kCollBlk.setDataValue("logout_date", context.getDataValue("OPENDAY"));
				kCollBlk.setDataValue("status", "003");//注销状态
				//更新黑名单信息
				dao.update(kCollBlk, connection);
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
		String black_level = (String)kc.getDataValue("black_level");//不宜贷款户级别
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		param.put("black_level", black_level);
		
		try {
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
			String IsTeam="";
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			OrganizationServiceInterface userService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
			KeyedCollection kColl4STO = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByMemNo", manager_id, null, this.getConnection());
			if(kColl4STO != null && kColl4STO.getDataValue("team_no")!=null && !"".equals(kColl4STO.getDataValue("team_no"))){		
				IsTeam="yes";
			}else{
				List<SOrg> orgslist = userService.getDeptOrgByActno(manager_id, this.getConnection());
				if(orgslist!=null&&orgslist.size()==1){//责任人只有一个机构则取该机构码
					manager_br_id = orgslist.get(0).getOrganno();
				}
				IsTeam="no";
			}
			param.put("IsTeam", IsTeam);
			/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			SOrg supOrg = userService.getSupOrg(manager_br_id, conn);//上级机构
			param.put("super_org_id", supOrg.getOrganno());
			
			//获取客户所属条线
			String cus_id = (String)kc.getDataValue("cus_id");
			param.put("cus_id",cus_id);
			if(cus_id!=null&&!"".equals(cus_id)){//系统内客户
				CusServiceInterface cusService = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
				CusBase cusBase = cusService.getCusBaseByCusId(cus_id, this.getContext(), conn);
				String belg_line = cusBase.getBelgLine();
				param.put("belg_line", belg_line);//获得所属条线并放入变量中
//			}else{//系统外客户
//				String cert_type = (String)kc.getDataValue("cert_type");//证件类型
//				if("20".equals(cert_type)||"a".equals(cert_type)||"b".equals(cert_type)){//对公
//					param.put("belg_line", "BL100");
//				}else{//对私
//					param.put("belg_line", "BL300");
//				}
			}
		} catch (Exception e) {
			throw new EMPException(e);
		}
		return param;
	}

}
