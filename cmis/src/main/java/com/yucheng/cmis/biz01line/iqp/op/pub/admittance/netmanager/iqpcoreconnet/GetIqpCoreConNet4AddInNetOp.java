package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpcoreconnet;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class GetIqpCoreConNet4AddInNetOp extends CMISOperation {
	
	private final String modelId = "IqpCoreConNet";
	private final String modelIdNetMag = "IqpNetMagInfo";
	private final String modelIdMemMana = "IqpMemMana";
	private final String modelIdOverseeAgr = "IqpOverseeAgr";
	private final String modelIdDesbuyPlan = "IqpDesbuyPlan";
	private final String modelIdBconCoopAgr = "IqpBconCoopAgr";
	private final String modelIdDepotAgr = "IqpDepotAgr";
	private final String modelIdPsaleCont = "IqpPsaleCont";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String net_agr_no = "";
			String flow_type ="";
			try{
				net_agr_no = (String)context.getDataValue("net_agr_no");
				flow_type = (String)context.getDataValue("flow_type");
			}catch(Exception e){
				throw new Exception("数据为空！");
			}
			
			TableModelDAO dao = getTableModelDAO(context);
			//存在在途的入/退网申请
			String condition = "where net_agr_no='"+net_agr_no+"' and approve_status not in ('997','998')";
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			
			if(iColl.size() > 0){
				context.addDataField("flag","Have");
				context.addDataField("serno","");
			}else{
				String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
				KeyedCollection kColl = dao.queryDetail(modelIdNetMag, net_agr_no, connection);
				kColl.setName("IqpCoreConNet");
				kColl.addDataField("approve_status", "000");
				kColl.setDataValue("flow_type", flow_type);
				kColl.addDataField("app_type", "02");
				kColl.setDataValue("serno", serno);
				dao.insert(kColl, connection);
				
				String condit = "where net_agr_no ='"+net_agr_no+"' and status='1'";
				IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent)CMISComponentFactory.getComponentFactoryInstance()
				.getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context, connection);
				//原成员信息
				IndexedCollection iCollMemMana = dao.queryList(modelIdMemMana, condit, connection);
				for(int i=0;i<iCollMemMana.size();i++){
					KeyedCollection kCollMemMana = (KeyedCollection)iCollMemMana.get(i);
					kCollMemMana.setName("IqpAppMemMana");
					kCollMemMana.addDataField("serno", serno);
					kCollMemMana.setDataValue("status", "3");
					dao.insert(kCollMemMana, connection);
				}
				//监管协议信息
				IndexedCollection iCollOverseeAgr = dao.queryList(modelIdOverseeAgr, condit, connection);
				for(int i=0;i<iCollOverseeAgr.size();i++){
					KeyedCollection kCollOverseeAgr = (KeyedCollection)iCollOverseeAgr.get(i);
					kCollOverseeAgr.setName("IqpAppOverseeAgr");
					kCollOverseeAgr.addDataField("serno", serno);
					kCollOverseeAgr.put("status","1");
					kCollOverseeAgr.put("cont_status","0");
					dao.insert(kCollOverseeAgr, connection);
				}
				//订货计划申请表
				IndexedCollection iCollDesbuyPlan = dao.queryList(modelIdDesbuyPlan, condit, connection);
				for(int i=0;i<iCollDesbuyPlan.size();i++){
					KeyedCollection kCollDesbuyPlan = (KeyedCollection)iCollDesbuyPlan.get(i);
					kCollDesbuyPlan.setName("IqpAppDesbuyPlan");
					kCollDesbuyPlan.addDataField("serno", serno);
					kCollDesbuyPlan.put("status","1");
					kCollDesbuyPlan.put("cont_status","0");
					dao.insert(kCollDesbuyPlan, connection);
				}
				//银企合作协议申请表
				IndexedCollection iCollBconCoopAgr = dao.queryList(modelIdBconCoopAgr, condit, connection);
				for(int i=0;i<iCollBconCoopAgr.size();i++){
					KeyedCollection kCollBconCoopAgr = (KeyedCollection)iCollBconCoopAgr.get(i);
					kCollBconCoopAgr.setName("IqpAppBconCoopAgr");
					kCollBconCoopAgr.addDataField("serno", serno);
					kCollBconCoopAgr.put("status","1");
					kCollBconCoopAgr.put("cont_status","0");
					dao.insert(kCollBconCoopAgr, connection);
				}
				//保兑仓协议申请表
				IndexedCollection iCollDepotAgr = dao.queryList(modelIdDepotAgr, condit, connection);
				for(int i=0;i<iCollDepotAgr.size();i++){
					KeyedCollection kCollDepotAgr = (KeyedCollection)iCollDepotAgr.get(i);
					kCollDepotAgr.setName("IqpAppDepotAgr");
					kCollDepotAgr.addDataField("serno", serno);
					kCollDepotAgr.put("status","1");
					kCollDepotAgr.put("cont_status","0");
					dao.insert(kCollDepotAgr, connection);
				}
				//购销合同申请表
				IndexedCollection iCollPsaleCont = dao.queryList(modelIdPsaleCont, condit, connection);
				for(int i=0;i<iCollPsaleCont.size();i++){
					KeyedCollection kCollPsaleCont = (KeyedCollection)iCollPsaleCont.get(i);
					kCollPsaleCont.setName("IqpAppPsaleCont");
					kCollPsaleCont.addDataField("serno", serno);
					kCollPsaleCont.put("status","1");
					kCollPsaleCont.put("cont_status","0");
					dao.insert(kCollPsaleCont, connection);
					
					//购销合同商品表
					String psale_cont = (String)kCollPsaleCont.getDataValue("psale_cont");
					String conditionStr = "where psale_cont='"+psale_cont+"'";
					IndexedCollection iCollGood = dao.queryList("IqpPsaleContGood", conditionStr, connection);
					for(int m=0;m<iCollGood.size();m++){
						KeyedCollection kCollGood = (KeyedCollection)iCollGood.get(m);
						kCollGood.setName("IqpAppPsaleContGood");
						kCollGood.put("serno", serno);
						dao.insert(kCollGood, connection);
					}
				}
				context.addDataField("flag","notHave");
				context.addDataField("serno",serno);
			}
			 
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
