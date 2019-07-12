package com.yucheng.cmis.biz01line.lmt.op.flow;

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
import com.yucheng.cmis.biz01line.lmt.utils.LmtUtils;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class LmtAppBizAreaFlowImpl extends CMISComponent implements
		BIZProcessInterface {
	
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行打回处理逻辑
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行拿回处理逻辑
	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		String serno = "";
//		String tableName = "";
		Context context = this.getContext();
		try {
			Connection connection = this.getConnection();
			serno = wfiMsg.getPkValue();
//			tableName = wfiMsg.getTableName();
			
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kCollBa = dao.queryDetail("LmtAppBizArea", serno, connection);
			String openDate = (String)context.getDataValue(PUBConstant.OPENDAY);
			String biz_area_type = (String)kCollBa.getDataValue("biz_area_type");//圈商类型
			kCollBa.setDataValue("over_date", openDate);
			//更新
			dao.update(kCollBa, connection);
			
			String term_type = (String)kCollBa.getDataValue("term_type");
			String term  = (String)kCollBa.getDataValue("term");
			kCollBa.remove("app_date");
			kCollBa.remove("over_date");
			kCollBa.remove("term_type");
			kCollBa.remove("term");
			kCollBa.remove("approve_status");
			
			//得到商圈编号(协议编号)
			String agrNo = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);//协议编号
			kCollBa.addDataField("agr_no",agrNo);
			kCollBa.addDataField("agr_status","002");
			//起始日期  到期日期  协议状态
			kCollBa.addDataField("start_date", openDate);
			String end_date = LmtUtils.computeEndDate(openDate, term_type, term);
			kCollBa.addDataField("end_date", end_date);
			kCollBa.setName("LmtAgrBizArea");
			//add协议数据
			dao.insert(kCollBa, connection);
			
			//插入圈商分类表
			if("0".equals(biz_area_type)){//一般商圈
				KeyedCollection kCollBac = dao.queryDetail("LmtAppBizAreaComn", serno, connection);
				kCollBac.remove("serno");
				kCollBac.addDataField("agr_no", agrNo);
				kCollBac.setName("LmtAgrBizAreaComn");
				dao.insert(kCollBac, connection);
			}else if("1".equals(biz_area_type)){//核心企业
				KeyedCollection kCollBac = dao.queryDetail("LmtAppBizAreaCore", serno, connection);
				kCollBac.remove("serno");
				kCollBac.addDataField("agr_no", agrNo);
				kCollBac.setName("LmtAgrBizAreaCore");
				dao.insert(kCollBac, connection);
			}else{//超市百货
				IndexedCollection iCollSup = dao.queryList("LmtAppBizAreaSupmk", " where serno = '"+serno+"'", connection);
				for(int i=0; i<iCollSup.size(); i++ ){
					KeyedCollection kCollSup = (KeyedCollection) iCollSup.get(i);
					kCollSup.remove("serno");
					kCollSup.addDataField("agr_no", agrNo);
					String supmk_serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
					kCollSup.setDataValue("supmk_serno", supmk_serno);
					kCollSup.setName("LmtAgrBizAreaSupmk");
					dao.insert(kCollSup, connection);
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
		
		/**责任人存在多个机构时取责任机构*/
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		OrganizationServiceInterface userService;
		List<SOrg> orgslist = null;
		try {
			userService = (OrganizationServiceInterface)serviceJndi.getModualServiceById("organizationServices", "organization");
			orgslist = userService.getDeptOrgByActno(manager_id, this.getConnection());
			if(orgslist!=null&&orgslist.size()==1){//责任人只有一个机构则取该机构码
				manager_br_id = orgslist.get(0).getOrganno();
			}
			SOrg supOrg = userService.getSupOrg(manager_br_id, conn);//上级机构
			param.put("super_org_id", supOrg.getOrganno());
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		
		return param;
	}

}