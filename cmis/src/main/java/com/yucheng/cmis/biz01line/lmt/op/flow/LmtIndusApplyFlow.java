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
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

/**
 * 行业授信流程处理类
 * @author GC-20130924
 */
public class LmtIndusApplyFlow extends CMISComponent implements
		BIZProcessInterface {

	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {

	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {

	}

	/*** 流程审批通过时数据处理 ***/
	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		Context context = this.getContext();
		Connection connection = null;
		String serno = "";
		String tableName = "";
		String agr_no = "";
		String menuId = "";
		
		try {			
			connection = this.getConnection();
			serno = wfiMsg.getPkValue();
			tableName = wfiMsg.getTableName();
			LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance("LmtPubComponent",context,connection);
			
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail(tableName, serno, connection);
			String apply_type = kColl.getDataValue("apply_type").toString();
			if("001".equals(apply_type)){//行业授信申请
				agr_no = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);
				menuId = "indus_crd_apply";
			}else if("002".equals(apply_type)){//行业授信变更				
				agr_no = lmtComponent.getAgrno("getAgrno",serno);
				menuId = "indus_crd_change";
			}			
			lmtComponent.doVirtualSubmit(menuId,serno,agr_no);
			
		} catch (Exception e) {
			throw new EMPException("授信流程审批报错，错误描述：" + e.getMessage());
		}
	}

	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {

	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {

	}

	public Map<String, String> putBizData2WfVar(String tabModelId,
			String pkVal, KeyedCollection modifyVar) throws EMPException {
		// 设置业务数据至流程变量之中
		Connection conn = this.getConnection();
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, conn);
		Map<String, String> param = new HashMap<String, String>();
		
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		
		IndexedCollection iColl = dao.queryList("LmtIndusAuthApply", "where serno = '"+pkVal+"' ", conn);
		if(iColl != null && iColl.size() > 0){
			param.put("auth", "true");
		}else{
			param.put("auth", "false");
		}
		
		return param;
	}

}