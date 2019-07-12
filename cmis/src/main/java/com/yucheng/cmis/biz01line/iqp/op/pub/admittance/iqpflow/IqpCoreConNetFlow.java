package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpflow;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusCom;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.ComponentHelper;

public class IqpCoreConNetFlow extends CMISComponent implements
		BIZProcessInterface {
	
	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行打回处理逻辑
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行拿回处理逻辑
	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		try {
			Connection connection = this.getConnection();
			String serno = wfiMsg.getPkValue();
			TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail("IqpCoreConNet", serno, connection);
			String net_agr_no = (String)kColl.getDataValue("net_agr_no");
			//申请类型
			String app_type = (String)kColl.getDataValue("app_type");
//			double lmt_amt = 0;
//			double batair_lmt_amt = 0;
//			if(kColl.getDataValue("batair_lmt_amt")!=null&&!"".equals(kColl.getDataValue("batair_lmt_amt"))){
//				batair_lmt_amt = Double.valueOf(kColl.getDataValue("batair_lmt_amt").toString());
//			}
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//			LmtServiceInterface lmtservice = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");//获取授信接口
//			KeyedCollection LmtkColl = lmtservice.getLmtAgrInfoMsg(kColl.getDataValue("cus_id").toString(),"05",connection);
//			if(LmtkColl != null && LmtkColl.size()>0){
//				lmt_amt = Double.valueOf(LmtkColl.getDataValue("crd_totl_amt").toString());
//			}
//			CusServiceInterface service = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
//			CusCom cuscom = service.getCusComByCusId(kColl.getDataValue("cus_id").toString(), this.getContext(), connection);//对公客户信息，获取企业规模
//			ComponentHelper helper = new ComponentHelper();//获取工具类
//			KeyedCollection ComkColl = new KeyedCollection("CusCom");
//			ComkColl = helper.domain2kcol(cuscom, "CusCom");
//			String com_scale = "";
//			if(ComkColl.containsKey("com_scale")&&ComkColl.getDataValue("com_scale")!=null&&!"".equals(ComkColl.getDataValue("com_scale"))){
//				com_scale = ComkColl.getDataValue("com_scale").toString();
//			}
//			if(("30".equals(com_scale)||"31".equals(com_scale))&&(BigDecimal.valueOf(lmt_amt).add(BigDecimal.valueOf(batair_lmt_amt)).compareTo(new BigDecimal(5000000))<=0)){
//				service.updateCusBelgLine(kColl.getDataValue("cus_id").toString(), "BL200", connection);  //更改客户所属条线为小微
//			}else{   //授信金额 大于 500W 变更客户条线为公司条线 
//				service.updateCusBelgLine(kColl.getDataValue("cus_id").toString(), "BL100", connection);  //更改客户所属条线为公司
//			}
			
			if("01".equals(app_type)){//建网申请
				kColl.addDataField("status", "1");//网络状态
				kColl.setName("IqpNetMagInfo");
				dao.insert(kColl, connection);
			}else if("02".equals(app_type)){//入网/退网申请
				//网络基本信息
				KeyedCollection kCollNet = dao.queryDetail("IqpCoreConNet", serno, connection);
				kCollNet.setName("IqpNetMagInfo");
				dao.update(kCollNet, connection);
				
				String condition = "where serno='"+serno+"' and status in ('1','2','3')";
				IndexedCollection iColl = dao.queryList("IqpAppMemMana", condition, connection);
				for(int j=0;j<iColl.size();j++){
					KeyedCollection kCollMem = (KeyedCollection)iColl.get(j);
					String status = (String)kCollMem.getDataValue("status");
					String mem_cus_id = (String)kCollMem.getDataValue("mem_cus_id");
					//入网成员
					if("1".equals(status)){
						//原成员信息
						kCollMem.setName("IqpMemMana");
						kCollMem.addDataField("net_agr_no", net_agr_no);//网络编号
						kCollMem.setDataValue("status", "1");//成员状态
						String memmanaCondition = "where net_agr_no='"+net_agr_no+"' and mem_cus_id ='"+mem_cus_id+"'";
						IndexedCollection iCollMemMana = dao.queryList("IqpMemMana", memmanaCondition, connection);
						if(iCollMemMana.size()>0){
							dao.update(kCollMem, connection);
						}else{
							dao.insert(kCollMem, connection);
						}
						//监管协议信息
						String conditOverseeAgr = "where serno='"+serno+"' and mortgagor_id='"+mem_cus_id+"'";
						IndexedCollection iCollOverseeAgr = dao.queryList("IqpAppOverseeAgr", conditOverseeAgr, connection);
						for(int i=0;i<iCollOverseeAgr.size();i++){
							KeyedCollection kCollOverseeAgr = (KeyedCollection)iCollOverseeAgr.get(i);
							kCollOverseeAgr.setName("IqpOverseeAgr");
							kCollOverseeAgr.addDataField("net_agr_no", net_agr_no);//网络编号
							dao.insert(kCollOverseeAgr, connection);
						}
						//订货计划申请表
						String conditDesbuyPlan = "where serno='"+serno+"' and cus_id='"+mem_cus_id+"'";
						IndexedCollection iCollDesbuyPlan = dao.queryList("IqpAppDesbuyPlan", conditDesbuyPlan, connection);
						for(int i=0;i<iCollDesbuyPlan.size();i++){
							KeyedCollection kCollDesbuyPlan = (KeyedCollection)iCollDesbuyPlan.get(i);
							kCollDesbuyPlan.setName("IqpDesbuyPlan");
							kCollDesbuyPlan.addDataField("net_agr_no", net_agr_no);//网络编号
							dao.insert(kCollDesbuyPlan, connection);
						}
						//银企合作协议申请表
						String conditBconCoopAgr = "where serno='"+serno+"' and borrow_cus_id='"+mem_cus_id+"'";
						IndexedCollection iCollBconCoopAgr = dao.queryList("IqpAppBconCoopAgr", conditBconCoopAgr, connection);
						for(int i=0;i<iCollBconCoopAgr.size();i++){
							KeyedCollection kCollBconCoopAgr = (KeyedCollection)iCollBconCoopAgr.get(i);
							kCollBconCoopAgr.setName("IqpBconCoopAgr");
							kCollBconCoopAgr.addDataField("net_agr_no", net_agr_no);//网络编号
							dao.insert(kCollBconCoopAgr, connection);
						}
						//保兑仓协议申请表
						String conditDepotAgr = "where serno='"+serno+"' and cus_id='"+mem_cus_id+"'";
						IndexedCollection iCollDepotAgr = dao.queryList("IqpAppDepotAgr", conditDepotAgr, connection);
						for(int i=0;i<iCollDepotAgr.size();i++){
							KeyedCollection kCollDepotAgr = (KeyedCollection)iCollDepotAgr.get(i);
							kCollDepotAgr.setName("IqpDepotAgr");
							kCollDepotAgr.addDataField("net_agr_no", net_agr_no);//网络编号
							dao.insert(kCollDepotAgr, connection);
						}
						//购销合同申请表
						String conditPsaleCont = "where serno='"+serno+"' and mem_cus_id='"+mem_cus_id+"'";
						IndexedCollection iCollPsaleCont = dao.queryList("IqpAppPsaleCont", conditPsaleCont, connection);
						for(int i=0;i<iCollPsaleCont.size();i++){
							KeyedCollection kCollPsaleCont = (KeyedCollection)iCollPsaleCont.get(i);
							kCollPsaleCont.setName("IqpPsaleCont");
							kCollPsaleCont.addDataField("net_agr_no", net_agr_no);//网络编号
							dao.insert(kCollPsaleCont, connection);
							
							//购销合同商品表
							String psale_cont = (String)kCollPsaleCont.getDataValue("psale_cont");
							String conditionStr = "where psale_cont='"+psale_cont+"' and serno='"+serno+"'";
							IndexedCollection iCollGood = dao.queryList("IqpAppPsaleContGood", conditionStr, connection);
							for(int m=0;m<iCollGood.size();m++){
								KeyedCollection kCollGood = (KeyedCollection)iCollGood.get(m);
								kCollGood.setName("IqpPsaleContGood");
								dao.insert(kCollGood, connection);
							}
						}
					//退网成员
					}else if("2".equals(status)){
						//原成员信息
						kCollMem.setName("IqpMemMana");
						kCollMem.addDataField("net_agr_no", net_agr_no);//网络编号
						kCollMem.setDataValue("status", "2");//无效
						dao.update(kCollMem, connection);
						//监管协议信息
						String conditOverseeAgr = "where net_agr_no='"+net_agr_no+"' and mortgagor_id='"+mem_cus_id+"'";
						IndexedCollection iCollOverseeAgr = dao.queryList("IqpOverseeAgr", conditOverseeAgr, connection);
						for(int m=0;m<iCollOverseeAgr.size();m++){
							KeyedCollection kCollOverseeAgr = (KeyedCollection)iCollOverseeAgr.get(m);
							kCollOverseeAgr.put("status", "0");//失效
							dao.update(kCollOverseeAgr, connection);
						}
						//订货计划申请表
						String conditDesbuyPlan = "where net_agr_no='"+net_agr_no+"' and cus_id='"+mem_cus_id+"'";
						IndexedCollection iCollDesbuyPlan = dao.queryList("IqpDesbuyPlan", conditDesbuyPlan, connection);
						for(int i=0;i<iCollDesbuyPlan.size();i++){
							KeyedCollection kCollDesbuyPlan = (KeyedCollection)iCollDesbuyPlan.get(i);
							kCollDesbuyPlan.put("status", "0");//失效
							dao.update(kCollDesbuyPlan, connection);
						}
						//银企合作协议申请表
						String conditBconCoopAgr = "where net_agr_no='"+net_agr_no+"' and borrow_cus_id='"+mem_cus_id+"'";
						IndexedCollection iCollBconCoopAgr = dao.queryList("IqpBconCoopAgr", conditBconCoopAgr, connection);
						for(int i=0;i<iCollBconCoopAgr.size();i++){
							KeyedCollection kCollBconCoopAgr = (KeyedCollection)iCollBconCoopAgr.get(i);
							kCollBconCoopAgr.put("status", "0");//失效
							dao.update(kCollBconCoopAgr, connection);
						}
						//保兑仓协议申请表
						String conditDepotAgr = "where net_agr_no='"+net_agr_no+"' and cus_id='"+mem_cus_id+"'";
						IndexedCollection iCollDepotAgr = dao.queryList("IqpDepotAgr", conditDepotAgr, connection);
						for(int i=0;i<iCollDepotAgr.size();i++){
							KeyedCollection kCollDepotAgr = (KeyedCollection)iCollDepotAgr.get(i);
							kCollDepotAgr.put("status", "0");//失效
							dao.update(kCollDepotAgr, connection);
						}
						//购销合同申请表
						String conditPsaleCont = "where net_agr_no='"+net_agr_no+"' and mem_cus_id='"+mem_cus_id+"'";
						IndexedCollection iCollPsaleCont = dao.queryList("IqpPsaleCont", conditPsaleCont, connection);
						for(int i=0;i<iCollPsaleCont.size();i++){
							KeyedCollection kCollPsaleCont = (KeyedCollection)iCollPsaleCont.get(i);
							kCollPsaleCont.put("status", "0");//失效
							dao.update(kCollPsaleCont, connection);
						}
					//原有成员	
					}else if("3".equals(status)){
						kCollMem.setName("IqpMemMana");
						kCollMem.put("net_agr_no", net_agr_no);//网络编号
						kCollMem.put("status", "1");//有效
						dao.update(kCollMem, connection);
						//监管协议信息
						String conditOverseeAgrStr = "where net_agr_no='"+net_agr_no+"' and mortgagor_id='"+mem_cus_id+"' and status='1'";
						IndexedCollection iCollOverseeAgr = dao.queryList("IqpOverseeAgr", conditOverseeAgrStr, connection);
						for(int m=0;m<iCollOverseeAgr.size();m++){
							KeyedCollection kCollOverseeAgr = (KeyedCollection)iCollOverseeAgr.get(m);
							dao.deleteByPk("IqpOverseeAgr", kCollOverseeAgr.getDataValue("oversee_agr_no").toString(), connection);
						}
						String conditOverseeAgr = "where serno='"+serno+"' and mortgagor_id='"+mem_cus_id+"'";
						IndexedCollection iCollAppOverseeAgr = dao.queryList("IqpAppOverseeAgr", conditOverseeAgr, connection);
						for(int i=0;i<iCollAppOverseeAgr.size();i++){
							KeyedCollection kCollOverseeAgr = (KeyedCollection)iCollAppOverseeAgr.get(i);
							kCollOverseeAgr.setName("IqpOverseeAgr");
							kCollOverseeAgr.addDataField("net_agr_no", net_agr_no);//网络编号
							dao.insert(kCollOverseeAgr, connection);
						}
						//订货计划申请表
						String conditDesbuyPlanStr = "where net_agr_no='"+net_agr_no+"' and cus_id='"+mem_cus_id+"' and status='1'";
						IndexedCollection iCollDesbuyPlan = dao.queryList("IqpDesbuyPlan", conditDesbuyPlanStr, connection);
						for(int i=0;i<iCollDesbuyPlan.size();i++){
							KeyedCollection kCollDesbuyPlan = (KeyedCollection)iCollDesbuyPlan.get(i);
							dao.deleteByPk("IqpDesbuyPlan", kCollDesbuyPlan.getDataValue("desgoods_plan_no").toString(), connection);
						}
						String conditDesbuyPlan = "where serno='"+serno+"' and cus_id='"+mem_cus_id+"'";
						IndexedCollection iCollAppDesbuyPlan = dao.queryList("IqpAppDesbuyPlan", conditDesbuyPlan, connection);
						for(int i=0;i<iCollAppDesbuyPlan.size();i++){
							KeyedCollection kCollDesbuyPlan = (KeyedCollection)iCollAppDesbuyPlan.get(i);
							kCollDesbuyPlan.setName("IqpDesbuyPlan");
							kCollDesbuyPlan.addDataField("net_agr_no", net_agr_no);//网络编号
							dao.insert(kCollDesbuyPlan, connection);
						}
						//银企合作协议申请表
						String conditBconCoopAgrStr = "where net_agr_no='"+net_agr_no+"' and borrow_cus_id='"+mem_cus_id+"' and status='1'";
						IndexedCollection iCollBconCoopAgr = dao.queryList("IqpBconCoopAgr", conditBconCoopAgrStr, connection);
						for(int i=0;i<iCollBconCoopAgr.size();i++){
							KeyedCollection kCollBconCoopAgr = (KeyedCollection)iCollBconCoopAgr.get(i);
							dao.deleteByPk("IqpBconCoopAgr", kCollBconCoopAgr.getDataValue("coop_agr_no").toString(), connection);
						}
						String conditBconCoopAgr = "where serno='"+serno+"' and borrow_cus_id='"+mem_cus_id+"'";
						IndexedCollection iCollAppBconCoopAgr = dao.queryList("IqpAppBconCoopAgr", conditBconCoopAgr, connection);
						for(int i=0;i<iCollAppBconCoopAgr.size();i++){
							KeyedCollection kCollBconCoopAgr = (KeyedCollection)iCollAppBconCoopAgr.get(i);
							kCollBconCoopAgr.setName("IqpBconCoopAgr");
							kCollBconCoopAgr.addDataField("net_agr_no", net_agr_no);//网络编号
							dao.insert(kCollBconCoopAgr, connection);
						}
						//保兑仓协议申请表
						String conditDepotAgrStr = "where net_agr_no='"+net_agr_no+"' and cus_id='"+mem_cus_id+"' and status='1'";
						IndexedCollection iCollDepotAgr = dao.queryList("IqpDepotAgr", conditDepotAgrStr, connection);
						for(int i=0;i<iCollDepotAgr.size();i++){
							KeyedCollection kCollDepotAgr = (KeyedCollection)iCollDepotAgr.get(i);
							dao.deleteByPk("IqpDepotAgr", kCollDepotAgr.getDataValue("depot_agr_no").toString(), connection);
						}
						String conditDepotAgr = "where serno='"+serno+"' and cus_id='"+mem_cus_id+"'";
						IndexedCollection iCollAppDepotAgr = dao.queryList("IqpAppDepotAgr", conditDepotAgr, connection);
						for(int i=0;i<iCollAppDepotAgr.size();i++){
							KeyedCollection kCollDepotAgr = (KeyedCollection)iCollAppDepotAgr.get(i);
							kCollDepotAgr.setName("IqpDepotAgr");
							kCollDepotAgr.addDataField("net_agr_no", net_agr_no);//网络编号
							dao.insert(kCollDepotAgr, connection);
						}
						//购销合同申请表
						String conditPsaleContStr = "where net_agr_no='"+net_agr_no+"' and mem_cus_id='"+mem_cus_id+"' and status='1'";
						IndexedCollection iCollPsaleCont = dao.queryList("IqpPsaleCont", conditPsaleContStr, connection);
						for(int i=0;i<iCollPsaleCont.size();i++){
							KeyedCollection kCollPsaleCont = (KeyedCollection)iCollPsaleCont.get(i);
							dao.deleteByPk("IqpPsaleCont", kCollPsaleCont.getDataValue("psale_cont").toString(), connection);
							
							//购销合同商品表
							String psale_cont = (String)kCollPsaleCont.getDataValue("psale_cont");
							String conditionStr = "where psale_cont='"+psale_cont+"'";
							IndexedCollection iCollGood = dao.queryList("IqpPsaleContGood", conditionStr, connection);
							for(int m=0;m<iCollGood.size();m++){
								KeyedCollection kCollGood = (KeyedCollection)iCollGood.get(m);
								Map pkMap = new HashMap();
								pkMap.put("psale_cont",kCollGood.getDataValue("psale_cont").toString());
								pkMap.put("commo_name",kCollGood.getDataValue("commo_name").toString());
								dao.deleteByPks("IqpPsaleContGood", pkMap, connection);
							}
						}
						String conditPsaleCont = "where serno='"+serno+"' and mem_cus_id='"+mem_cus_id+"'";
						IndexedCollection iCollAppPsaleCont = dao.queryList("IqpAppPsaleCont", conditPsaleCont, connection);
						for(int i=0;i<iCollAppPsaleCont.size();i++){
							KeyedCollection kCollPsaleCont = (KeyedCollection)iCollAppPsaleCont.get(i);
							kCollPsaleCont.setName("IqpPsaleCont");
							kCollPsaleCont.addDataField("net_agr_no", net_agr_no);//网络编号
							dao.insert(kCollPsaleCont, connection);
							
							//购销合同商品表
							String psale_cont = (String)kCollPsaleCont.getDataValue("psale_cont");
							String conditionStr = "where psale_cont='"+psale_cont+"' and serno='"+serno+"'";
							IndexedCollection iCollGood = dao.queryList("IqpAppPsaleContGood", conditionStr, connection);
							for(int m=0;m<iCollGood.size();m++){
								KeyedCollection kCollGood = (KeyedCollection)iCollGood.get(m);
								kCollGood.setName("IqpPsaleContGood");
								dao.insert(kCollGood, connection);
							}
						}
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
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		return param;
	}

}
