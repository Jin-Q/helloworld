package com.yucheng.cmis.biz01line.iqp.op.iqpFlow;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.dc.eai.data.FieldType;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.iqp.component.IqpBailComponent;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.ESBUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class IqpBailSubDisFlowOp extends CMISComponent implements BIZProcessInterface {

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
			String serno_value = wfiMsg.getPkValue();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			//根据业务编号获取追加或者提取主申请信息
			KeyedCollection kColl = dao.queryDetail("IqpBailSubDis", serno_value, connection);
			//追加/提取后保证金比率
			String adjusted_bail_perc = (String) kColl.getDataValue("adjusted_bail_perc");
			//合同编号
			String cont_no = (String) kColl.getDataValue("cont_no");
			//将追加或者提取后的保证金比例回写合同表
			KeyedCollection kc = dao.queryDetail("CtrLoanCont",cont_no,connection);
//			kc.setDataValue("security_rate", adjusted_bail_perc);
//			dao.update(kc, connection);
			
			//如果是新增加的保证金则需要往保证金信息表(pub_bail_info)中增加一条数据
	        String condition = "where serno='"+serno_value+"'";
//			IndexedCollection iColl = dao.queryList("PubBailInfo", condition, connection);
//			if(iColl==null || iColl.size()==0){
//				 KeyedCollection kCollPubBailInfo = (KeyedCollection)iColl.get(0);
//				 KeyedCollection newKCollPubBailInfo = (KeyedCollection)kCollPubBailInfo.clone();
//				 newKCollPubBailInfo.put("serno", (String)kc.getDataValue("serno"));
//				 newKCollPubBailInfo.put("cont_no", cont_no);
//				 dao.insert(newKCollPubBailInfo, connection);
//			}
			
			//审批通过的同时生成授权信息
			IqpBailComponent ibComponent = (IqpBailComponent)CMISComponentFactory.getComponentFactoryInstance()
			.getComponentInstance("IqpBailComponent", this.getContext(), this.getConnection());
			ibComponent.doWfAgreeForBail(serno_value);
			/**调用ESB接口，发送报文*/
			/*CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			serviceRel.tradeBZJBG(serno_value, context, connection);*/
			
			KeyedCollection qryheadKcoll = new KeyedCollection();
			KeyedCollection qrybodyColl = new KeyedCollection("body");
			//组装报文头
			qryheadKcoll.put("SvcCd", "30120002");
			qryheadKcoll.put("ScnCd", "01");
			qryheadKcoll.addDataField("TxnMd","ONLINE");
			qryheadKcoll.addDataField("UsrLngKnd","CHINESE");
			qryheadKcoll.addDataField("jkType","cbs");
			//BODY
			
			KeyedCollection iqpbailsubdisColl = dao.queryDetail("IqpBailSubDis", serno_value, this.getConnection());
			IndexedCollection pubBailInfoiColl = dao.queryList("PubBailInfo", "where serno='"+serno_value+"'", this.getConnection());
			KeyedCollection pubBailInfoColl = (KeyedCollection)pubBailInfoiColl.get(0);
			
			IndexedCollection bodyArr=new IndexedCollection("MrgnInfArry");
			KeyedCollection bodyObj=new KeyedCollection();
			bodyObj.addDataField("TxnTp",valueOf(iqpbailsubdisColl.getDataValue("addflag")));//交易类型
			bodyObj.addDataField("BsnNo",serno_value); //业务编号
			bodyObj.addDataField("BsnTp","03"); //业务类型，担保
			bodyObj.addDataField("MrgnAcctNo",valueOf(pubBailInfoColl.getDataValue("bail_acct_no"))); //保证金账号
			bodyObj.addDataField("MrgnAcctPdTp","02040101"); //保证金账户产品类型02040101valueOf(pubBailInfoColl.getDataValue("bail_type"))
			bodyObj.addDataField("MrgnAcctSeqNo",valueOf(pubBailInfoColl.getDataValue("bail_acct_gl_code"))); //保证金账户序号
			bodyObj.addDataField("MrgnAcctCcy",valueOf(pubBailInfoColl.getDataValue("cur_type")));//保证金账户币种
			bodyObj.addDataField("DepBillNo","");//存单编号
			bodyObj.addDataField("MrgnAmt",valueOf(iqpbailsubdisColl.getDataValue("adjusted_bail_amt")));//保证金金额
			bodyObj.addDataField("FrzNo","");//冻结编号,待处理
			bodyObj.addDataField("NeedReChkFlg","");//需要复核标志
			bodyObj.addDataField("BsnDealSeqNo","");//业务处理序号
			bodyObj.addDataField("ReChkInstNo","");//需复核机构号
			bodyArr.addDataElement(bodyObj);
			qrybodyColl.addDataElement(bodyArr);
			
			KeyedCollection retKColl = new KeyedCollection();
			KeyedCollection BODY = new KeyedCollection("BODY");
			try {
				retKColl = ESBUtil.sendEsbMsg(qryheadKcoll,qrybodyColl);
				KeyedCollection sysHead=(KeyedCollection)retKColl.getDataElement("SYS_HEAD");
				IndexedCollection retArr=(IndexedCollection)sysHead.getDataElement("RetInfArry");
				KeyedCollection retObj=(KeyedCollection)retArr.get(0);
				String retCd=(String)retObj.getDataValue("RetCd");
				if("000000".equals(retCd)){//成功
					EMPLog.log(this.getClass().getName(), EMPLog.INFO, 0, "保证金变更接口调用成功！");
				}else{
					throw new EMPException((String)retObj.getDataValue("RetInf"));
				}
			} catch (Exception e) {
				// TODO: handle exception
				throw new EMPException(e);
			}
			
		}catch(Exception e){
			throw new EMPException("保证金追加或者提取流程审批报错，错误描述："+e.getMessage());
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
		
		try { 
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
		String IsTeam="";
		KeyedCollection kColl4STO = new KeyedCollection();
		try {
			kColl4STO = (KeyedCollection) SqlClient.queryFirst("getSTeamInfoByMemNo", manager_id, null, this.getConnection());
		} catch (SQLException e) {}
		if(kColl4STO != null && kColl4STO.getDataValue("team_no")!=null && !"".equals(kColl4STO.getDataValue("team_no"))){		
			IsTeam="yes";
		}else{
			IsTeam="no";
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("IsTeam", IsTeam);
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
		
		String cus_id = (String)kc.getDataValue("cus_id");
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
		CusBase Cus = serviceCus.getCusBaseByCusId(cus_id, this.getContext(), this.getConnection());
	    String belgLine = Cus.getBelgLine();
		param.put("bizline", belgLine);
		
		return param;
			} catch (Exception e) {
				throw new EMPException("设置业务数据至流程变量之中出错，错误描述："+e.getMessage());
			}
	}

	public String valueOf(Object obj){
		if(obj==null){
			return "";
		}else{
			return obj.toString();
		}
	}
}
