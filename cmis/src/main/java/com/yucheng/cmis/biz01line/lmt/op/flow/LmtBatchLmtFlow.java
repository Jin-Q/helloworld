package com.yucheng.cmis.biz01line.lmt.op.flow;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.lmt.component.intbank.LmtIntbankComponent;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class LmtBatchLmtFlow extends CMISComponent implements BIZProcessInterface {

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
		try{
			connection = this.getConnection();
			String serno = wfiMsg.getPkValue();
			KeyedCollection  kCollAcc = new KeyedCollection("LmtIntbankAcc");
			KeyedCollection  kCollAgr = new KeyedCollection("LmtBatchLmtAgr");
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			
			//获取提交客户的数据
			KeyedCollection kColl = dao.queryDetail("LmtBatchLmt", serno, connection);
//			IndexedCollection iColl = dao.queryList("LmtBatchLmt", " WHERE serno = '" + serno + "'", connection);
//			//取出iColl里面数据，放入kColl中，进行逐条插入进台帐中
//			for(int i=0;i<iColl.size();i++){
//				//自动生成协议编号
//				String agrNo = CMISSequenceService4JXXD.querySequenceFromDB("LMT_AGR", "all", connection, context);
//				kColl = (KeyedCollection) iColl.get(i);
//				kColl.addDataField("agr_no", agrNo);
//			}
			
			
//			kColl = (KeyedCollection) iColl.get(0);
//			kColl.addDataField("agr_no", agrNo);
			//判断批量包中客户是否进行授信
			//将数据插入台帐表中
			LmtIntbankComponent libc_acc = (LmtIntbankComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtIntbank", context, connection);
//			LmtIntbankComponent libc_agr = (LmtIntbankComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtIntbank", context, connection);
			kCollAcc = libc_acc.insert2kColl4Acc(kColl);
			
			//批量客户进行授信，提交生成一条协议----批量协议暂时不生成
//			kCollAgr = libc_agr.insert2kColl4Agr(kColl);
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
		// 设置业务数据至流程变量之中
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		String isIntbank = "N";
		Map<String, String> param = new HashMap<String, String>();
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		param.put("isIntbank", isIntbank);
		return param;
	}

}
