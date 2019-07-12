package com.yucheng.cmis.biz01line.arp.op.flow;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.util.EMPUtils;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.arp.component.ArpPubComponent;
import com.yucheng.cmis.biz01line.arp.component.ArpTradeComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.ESBUtil;
import com.yucheng.cmis.pub.util.TimeUtil;

public class ArpBadassetHandoverApp extends CMISComponent implements
		BIZProcessInterface {

	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub

	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub

	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		Context context = this.getContext();
		Connection connection = null;
		String serno = "";
		String tableName = "";
		
		try {
			connection = this.getConnection();
			serno = wfiMsg.getPkValue();
			tableName = wfiMsg.getTableName();
			
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			ArpPubComponent cmisComponent = (ArpPubComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance("ArpPubComponent",context,connection);
			KeyedCollection tranc_kColl = new KeyedCollection("TransValue");//传到dao中的值
			
			if(tableName.equals("ArpBadassetHandoverApp")){ //不良移交申请流程
				tranc_kColl.addDataField("serno", serno);
				cmisComponent.delExecuteSql("ArpBadassetHandoverApp", tranc_kColl);
			}else if(tableName.equals("ArpLawLawsuitApp")){ //诉讼申请流程
				KeyedCollection kColl = dao.queryDetail(tableName, serno, connection);
				String manager_br_id = kColl.getDataValue("manager_br_id").toString();
				String case_no = CMISSequenceService4JXXD.querySequenceFromSQ("SQ", "all", manager_br_id, connection, context);
				tranc_kColl.addDataField("serno", serno);
				tranc_kColl.addDataField("case_no", case_no);
				cmisComponent.delExecuteSql("ArpLawLawsuitAppFlow", tranc_kColl);
			}else if(tableName.equals("ArpBondReducApp")){ //债权减免申请流程
				/*** 改申请表日期 ***/
				KeyedCollection kColl = dao.queryDetail("ArpBondReducApp", serno, connection);
				kColl.setDataValue("over_date", context.getDataValue("OPENDAY"));
				dao.update(kColl, connection);
			}else if(tableName.equals("ArpDbtCongnizApp")){ //呆账认定申请流程
				/*** 1.生成呆账台账，2.改贷款台账中呆账金额，3.改申请表日期 ***/
				tranc_kColl.addDataField("serno", serno);
				cmisComponent.delExecuteSql("ArpDbtCongnizApp", tranc_kColl);
				
				/*** 调用iqp接口，改贷款台账中呆账金额 ***/
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");			
				IndexedCollection iColl = dao.queryList("ArpDbtBizRel", "where serno = '"+serno+"' ", connection);
				for(int i = 0 ;i < iColl.size();i++){
					KeyedCollection iqp_kColl = (KeyedCollection) iColl.get(i);
					iqp_kColl.addDataField("type", "updateBadDbtBalance");
					service.updateIqpByCondition(iqp_kColl, context, connection); 
				}
				
			}else if(tableName.equals("ArpDbtWriteoffApp")){ //呆账核销申请流程
				/*** 1.生成呆账核销台账，2.改申请表日期，3.生成授权发往核算 ***/
				tranc_kColl.addDataField("serno", serno);
				cmisComponent.delExecuteSql("ArpDbtWriteoffApp", tranc_kColl);
				
				/*** 根据核心核销操作情况，多笔借据是逐笔核销并发送通知。所以必须对每笔借据都生成一笔授权，非出账授权直接发送 ***/
				ArpTradeComponent cmisTradeComponent = (ArpTradeComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance("ArpTradeComponent", this.getContext(), this.getConnection());
				cmisTradeComponent.DbtWriteoffAuthorize(serno);
				
				/**调用ESB接口，发送报文*/
				/*CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
				serviceRel.tradeDKHX(serno, context, connection);*/
				
				KeyedCollection qryheadKcoll = new KeyedCollection();
				KeyedCollection qrybodyColl = new KeyedCollection("body");
				//组装报文头
				qryheadKcoll.put("SvcCd", "30220005");
				qryheadKcoll.put("ScnCd", "01");
				qryheadKcoll.addDataField("TxnMd","ONLINE");
				qryheadKcoll.addDataField("UsrLngKnd","CHINESE");
				qryheadKcoll.addDataField("jkType","cbs");
				//BODY
				IndexedCollection arpIcoll=dao.queryList(tableName,"where serno='"+serno+"'" ,connection);
				KeyedCollection arpKcoll=(KeyedCollection)arpIcoll.get(0);
				String clsAcctRsn=(String)arpKcoll.getDataValue("writeoff_resn");
				//查询核销详细信息
				IndexedCollection arpdtIcoll=dao.queryList("ArpDbtWriteoffDetail","where serno='"+serno+"'" ,connection);
				KeyedCollection arpDtKcoll=(KeyedCollection)arpdtIcoll.get(0);
				String billNo=(String)arpDtKcoll.getDataValue("bill_no");
				//根据借据号查询到对应贷款号进行接口调用-------
				Map<String,Object> accLoanMap = new HashMap<String,Object>();
				accLoanMap.put("bill_no", billNo);
				KeyedCollection accLoanKcoll = dao.queryDetail("AccLoan", accLoanMap, connection);
				String acctNo = "";
				if(accLoanKcoll!=null){
					acctNo = (String) accLoanKcoll.getDataValue("base_acct_no");
				}else{
					throw new EMPException("贷款账号查询为空！");
				}
				qrybodyColl.addDataField("AcctNoCrdNo",acctNo);//贷款号，待处理
				qrybodyColl.addDataField("CnclAcctFlg","Y");//核销标志 
				qrybodyColl.addDataField("AcctStModDt",TimeUtil.getDateTime("yyyyMMdd")); //账户状态变更日期
				qrybodyColl.addDataField("ClsAcctRsn",clsAcctRsn); //销户原因
				qrybodyColl.addDataField("OprtnTp","01");//操作类型
				
				KeyedCollection retKColl = new KeyedCollection(); 
				retKColl = ESBUtil.sendEsbMsg(qryheadKcoll,qrybodyColl);
				KeyedCollection sysHead=(KeyedCollection)retKColl.getDataElement("SYS_HEAD");
				IndexedCollection retArr=(IndexedCollection)sysHead.getDataElement("RetInfArry");
				KeyedCollection retObj=(KeyedCollection)retArr.get(0);
				String retCd=(String)retObj.getDataValue("RetCd");
				if(!"000000".equals(retCd)){
					throw new EMPException((String)retObj.getDataValue("RetInf"));
				}else{
					EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "核销申请通知发送成功！");
				}
			}	
			
		} catch (Exception e) {
			throw new EMPException("流程审批报错，错误描述：" + e.getMessage());
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
		Connection conn = this.getConnection();
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, conn);
		String manager_br_id = "";
		String manager_id= "";
		Map<String, String> param = new HashMap<String, String>();
		
		if (tabModelId.equals("ArpBadassetHandoverApp")) { //不良移交申请流程，取接收机构、接收人员
			manager_br_id = (String) kc.getDataValue("rcv_org");
			manager_id = (String) kc.getDataValue("rcv_person");
		} else if (tabModelId.equals("ArpDbtWriteoffApp")||tabModelId.equals("ArpBondReducApp")
				||tabModelId.equals("ArpDbtCongnizApp")) {	//核销申请处理核销金额和申请机构
			if(tabModelId.equals("ArpDbtWriteoffApp")){
				KeyedCollection kColl_trans = new KeyedCollection("TransValue");
				ArpPubComponent cmisComponent = (ArpPubComponent)CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance("ArpPubComponent",this.getContext(),this.getConnection());
				kColl_trans.addDataField("serno", pkVal);
				kColl_trans = cmisComponent.delReturnSql("SumDbtWriteoffDetail", kColl_trans);
				
				String debt_in_amt = kColl_trans.getDataValue("writeoff_amt_sum").toString(); //核销总金额
				double amt = Double.parseDouble(debt_in_amt);
				// XD150710051 Edited by FCL 调整总行行长呆账核销审批权
				double WriteoffAmt = 0.0;
				WriteoffAmt = amt/10000;
				/*if (amt <= 15000000) {
					WriteoffAmt = "1"; 
				} else if (amt > 15000000 && amt < 20000000){
					WriteoffAmt = "2"; // 核销金额在500W到1000W之间
				}else{
					WriteoffAmt = "3";
				}*/
				//-----------------END-------------------------
				param.put("WriteoffAmt", ""+WriteoffAmt);
			}
			
			manager_br_id = (String) kc.getDataValue("manager_br_id"); 
			manager_id = (String) kc.getDataValue("manager_id");			
			try {
				String cus_id = (String)kc.getDataValue("cus_id");
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
				CusBase Cus = serviceCus.getCusBaseByCusId(cus_id, this.getContext(), this.getConnection());
			    String belgLine = Cus.getBelgLine();
				param.put("bizline", belgLine);
				
			} catch (Exception e) {
				throw new EMPException("流程审批初始化参数报错，错误描述："+e.getMessage());
			}
			
		}else if(tabModelId.equals("ArpLawLawsuitApp")){
			try {
				manager_br_id = (String) kc.getDataValue("manager_br_id"); 
			OrganizationServiceInterface orgMsi = (OrganizationServiceInterface) CMISModualServiceFactory.getInstance().getModualServiceById("organizationServices", "organization");
			SOrg supOrg = orgMsi.getSupOrg(manager_br_id, getConnection());
		    String	organNoTmp = supOrg.getOrganno();
		    if("9908".equals(organNoTmp)){
		    	param.put("bizline", "BL300");
		    }else{
		    	param.put("bizline", "BL100");
		    }
			} catch (Exception e) {
				throw new EMPException("流程审批初始化参数报错，错误描述："+e.getMessage());
			}
			
	    }else {
			manager_br_id = (String) kc.getDataValue("manager_br_id"); // 管理机构
			manager_id = (String) kc.getDataValue("manager_id");
		}		
		
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		return param;
	}

}