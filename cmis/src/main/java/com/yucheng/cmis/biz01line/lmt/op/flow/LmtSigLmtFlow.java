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
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.lmt.component.intbank.LmtIntbankComponent;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class LmtSigLmtFlow extends CMISComponent implements BIZProcessInterface {

	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		
	}

	//流程审批通过
	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		Context context = this.getContext();
		Connection connection = null;
		KeyedCollection  kCollAcc = new KeyedCollection("LmtIntbankAcc");
		try{
			connection = this.getConnection();
			String serno = wfiMsg.getPkValue();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = null;
			//自动生成协议编号
			String agrNo = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);
			
			//获取提交客户的数据
			IndexedCollection iColl = dao.queryList("LmtSigLmt", " WHERE serno = '" + serno + "'", connection);
			//取出iColl里面数据，放入kColl中，进行逐条插入进台帐中
			for(int i=0;i<iColl.size();i++){
				kColl = (KeyedCollection) iColl.get(i);
				kColl.addDataField("agr_no", agrNo);
			}
			LmtIntbankComponent libc = (LmtIntbankComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtIntbank", context, connection);
			kCollAcc = libc.insert2kColl(kColl);
			//提交授信分项数据到授信使用明细
			IndexedCollection subiColl = dao.queryList("LmtSubApp"," WHERE serno = '" + serno + "'", connection);
				String cus_id = (String)kColl.getDataValue("cus_id");
				IndexedCollection ic = dao.queryList("LmtIntbankDetail", "where cus_id ='"+cus_id+"'", connection);
				if(ic.size()>0){
					for(int j=0;j<subiColl.size();j++){
						KeyedCollection kColldetail=(KeyedCollection)subiColl.get(j);
						kColldetail.remove("serno");
						kColldetail.addDataField("cus_id", cus_id);
						kColldetail.setName("LmtIntbankDetail");
						dao.update(kColldetail, connection);
					}	
				}else{
					for(int j=0;j<subiColl.size();j++){
						KeyedCollection kColldetail=(KeyedCollection)subiColl.get(j);
						kColldetail.remove("serno");
						kColldetail.addDataField("cus_id", cus_id);
						kColldetail.setName("LmtIntbankDetail");
						dao.insert(kColldetail, connection);
					}
				}
		}catch(Exception e){
			throw new EMPException("授信流程审批报错，错误描述："+e.getMessage());
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
		Context context = this.getContext();
		Connection connection = null;
		// 设置业务数据至流程变量之中
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		String cus_id = (String)kc.getDataValue("cus_id");
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();			
		Map<String, String> param = new HashMap<String, String>();
		try {
			connection = this.getConnection();
			String isIntbank = "N";
			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
			KeyedCollection CusSameOrg = service.getCusSameOrgKcoll(cus_id, context, connection);
			
			String same_org_type = (String)CusSameOrg.getDataValue("same_org_type");//同业机构客户类型
			if("1,2,3,4,5,6,7,10,21,13".indexOf(same_org_type)>0){
				isIntbank = "Y";
			}else{
				isIntbank = "N";
			}
			param.put("isIntbank", isIntbank);
			param.put("manager_br_id", manager_br_id);
			param.put("manager_id", manager_id);
		} catch (Exception e) {
			e.printStackTrace();
		}//调用客户接口，实现方法getCusSameOrgKcoll
		return param;
	}
}
