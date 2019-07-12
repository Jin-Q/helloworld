package com.yucheng.cmis.biz01line.lmt.op.flow;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

public class LmtAppFrozenFlowImpl extends CMISComponent implements BIZProcessInterface {

	private static final String LMTAPPDETAILS = "LmtAppDetails";//授信分项申请表
	private static final String LMTAGRDETAILS = "LmtAgrDetails";//授信分项台账表
	/**added by wangj 2015/05/28  需求编号:XD141222087,法人账户透支需求变更  begin**/	
	private static final String CTRLOANCONTSUB = "CtrLoanContSub";//贷款合同从表
	private static final String COUNTSQLID = "queryCtrLoanCountByLimtCodeSerno";//获取额度关联的合同信息	
	/**added by wangj 2015/05/28  需求编号:XD141222087,法人账户透支需求变更  end**/	
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
			String table = wfiMsg.getTableName();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection kColl = dao.queryDetail(table, serno_value, connection);
			String app_type = (String) kColl.getDataValue("app_type");
			
			String condition = "where serno = '"+serno_value+"'";
			IndexedCollection iColl = dao.queryList(LMTAPPDETAILS, condition, connection);
			
			if(app_type.equals("03")){//冻结
				for(int i=0;i<iColl.size();i++){
					KeyedCollection detkColl = (KeyedCollection) iColl.get(i);
					if(detkColl.getDataValue("froze_amt")!=null&&!"".equals(detkColl.getDataValue("froze_amt"))){
						BigDecimal det_froze_amt = new BigDecimal(detkColl.getDataValue("froze_amt")+"");
						String org_limit_code = (String) detkColl.getDataValue("org_limit_code");
						KeyedCollection argKcoll = dao.queryDetail(LMTAGRDETAILS, org_limit_code, connection);
						String froze_amt_str = (String) argKcoll.getDataValue("froze_amt");
						BigDecimal froze_amt = new BigDecimal("0");
						if(froze_amt_str!=null&&!froze_amt_str.equals("")){
							froze_amt = new BigDecimal(froze_amt_str);
						}
						froze_amt=froze_amt.add(det_froze_amt);
						argKcoll.setDataValue("froze_amt", froze_amt);
						dao.update(argKcoll, connection);
					}
				}
				/**added by wangj 2015/05/28  需求编号:XD141222087,法人账户透支需求变更  begin**/	
				IndexedCollection iCollResult = SqlClient.queryList4IColl(COUNTSQLID,serno_value,connection);// 返回两个字段 客户号 cus_id,合同编号cont_no,合同状态cont_status
				if (iCollResult != null&&iCollResult.size()>0 ) {
					for(int i=0;i<iCollResult.size();i++){
						KeyedCollection CountkColl=(KeyedCollection) iCollResult.get(i);
						String cus_id = (String) CountkColl.getDataValue("cus_id");//合同编号
						String cont_no = (String) CountkColl.getDataValue("cont_no");//合同编号
						String cont_status = (String) CountkColl.getDataValue("cont_status");//合同状态
						KeyedCollection CountSubkColl=dao.queryDetail(CTRLOANCONTSUB, cont_no, connection);
						String is_freeze=TagUtil.replaceNull4String(CountSubkColl.getDataValue("is_freeze"));
						if(!"1".equals(is_freeze)){
							CountSubkColl.setDataValue("is_freeze", "1");//合同冻结
							dao.update(CountSubkColl, connection);
						}
						if(!"1".equals(is_freeze)&&"200".equals(cont_status)){//合同有效才需要发送交易
							KeyedCollection resqkColl=new KeyedCollection();
							resqkColl.addDataField("CLIENT_NO",cus_id);
							resqkColl.addDataField("OPERATION_TYPE","10");//10-冻结  20-解冻 30-终止
							resqkColl.addDataField("CONTRACT_NO",cont_no);
							resqkColl.addDataField("APPLY_TYPE","003");//001 个人自助 002 小微自助  003 法人透支
							/**调用ESB接口，发送报文*/
							CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
							ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
							serviceRel.trade0200200000503(resqkColl, context, connection);
						}
					}
				}
				/**added by wangj 2015/05/28  需求编号:XD141222087,法人账户透支需求变更  end**/
				
				/**added by lisj 2015-6-29  需求编号：【XD150123005】小微自助循环贷款改造  begin**/	
				IndexedCollection iCollResult4MRL = SqlClient.queryList4IColl("queryRelCLCBySerno",serno_value,connection);
				if (iCollResult4MRL != null&&iCollResult4MRL.size()>0 ) {
					for(int i=0;i<iCollResult4MRL.size();i++){
						KeyedCollection CountkColl=(KeyedCollection) iCollResult4MRL.get(i);
						String cus_id = (String) CountkColl.getDataValue("cus_id");//合同编号
						String cont_no = (String) CountkColl.getDataValue("cont_no");//合同编号
						String cont_status = (String) CountkColl.getDataValue("cont_status");//合同状态
						KeyedCollection CountSubkColl=dao.queryDetail(CTRLOANCONTSUB, cont_no, connection);
						String is_freeze=TagUtil.replaceNull4String(CountSubkColl.getDataValue("is_freeze"));
						if(!"1".equals(is_freeze)){
							CountSubkColl.setDataValue("is_freeze", "1");//合同冻结
							dao.update(CountSubkColl, connection);
						}
						IndexedCollection pvpIColl = dao.queryList("PvpLoanApp", " where cont_no='"+cont_no+"' and approve_status='997'", connection);//查询出账信息
						if(pvpIColl!=null && pvpIColl.size()>0){
							if(!"1".equals(is_freeze)&& "200".equals(cont_status)){//合同有效才需要发送交易
								KeyedCollection resqkColl=new KeyedCollection();
								resqkColl.addDataField("CLIENT_NO",cus_id);
								resqkColl.addDataField("OPERATION_TYPE","10");//10-冻结  20-解冻 30-终止
								resqkColl.addDataField("CONTRACT_NO",cont_no);
								resqkColl.addDataField("APPLY_TYPE","002");//001 个人自助 002 小微自助  003 法人透支
								/**调用ESB接口，发送报文*/
								CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
								ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
								serviceRel.trade0200200000502(resqkColl, context, connection);//小微自助循环贷款额度冻结/解冻发送交易
							}
						}
					}
				}
				/**added by lisj 2015-6-29  需求编号：【XD150123005】小微自助循环贷款改造  end**/
			}else if(app_type.equals("04")){//解冻
				for(int i=0;i<iColl.size();i++){
					KeyedCollection detkColl = (KeyedCollection) iColl.get(i);
					if(detkColl.getDataValue("unfroze_amt")!=null&&!"".equals(detkColl.getDataValue("unfroze_amt"))){
						String org_limit_code = (String) detkColl.getDataValue("org_limit_code");
						BigDecimal det_unfroze_amt = new BigDecimal(detkColl.getDataValue("unfroze_amt")+"");
						KeyedCollection argKcoll = dao.queryDetail(LMTAGRDETAILS, org_limit_code, connection);
						BigDecimal froze_amt = new BigDecimal(argKcoll.getDataValue("froze_amt")+"");
						froze_amt=froze_amt.subtract(det_unfroze_amt);
						argKcoll.setDataValue("froze_amt", froze_amt);
						dao.update(argKcoll, connection);
					}
				}
				/**added by wangj 2015/05/28  需求编号:XD141222087,法人账户透支需求变更  begin**/	
				IndexedCollection iCollResult = SqlClient.queryList4IColl(COUNTSQLID,serno_value,connection);// 返回两个字段 客户号 cus_id,合同编号cont_no,合同状态cont_status
				if (iCollResult != null&&iCollResult.size()>0 ) {
					for(int i=0;i<iCollResult.size();i++){
						KeyedCollection CountkColl=(KeyedCollection) iCollResult.get(i);
						String cus_id = (String) CountkColl.getDataValue("cus_id");//合同编号
						String cont_no = (String) CountkColl.getDataValue("cont_no");//合同编号
						String cont_status = (String) CountkColl.getDataValue("cont_status");//合同状态
						KeyedCollection CountSubkColl=dao.queryDetail(CTRLOANCONTSUB, cont_no, connection);
						String is_freeze=TagUtil.replaceNull4String(CountSubkColl.getDataValue("is_freeze"));
						if(!"2".equals(is_freeze)){
							CountSubkColl.setDataValue("is_freeze", "2");//合同解冻
							dao.update(CountSubkColl, connection);
						}
						if(!"2".equals(is_freeze)&&"200".equals(cont_status)){//合同有效才需要发送交易
							KeyedCollection resqkColl=new KeyedCollection();
							resqkColl.addDataField("CLIENT_NO",cus_id);
							resqkColl.addDataField("OPERATION_TYPE","20");//10-冻结  20-解冻 30-终止
							resqkColl.addDataField("CONTRACT_NO",cont_no);
							resqkColl.addDataField("APPLY_TYPE","003");//001 个人自助 002 小微自助  003 法人透支
							/**调用ESB接口，发送报文*/
							CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
							ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
							serviceRel.trade0200200000503(resqkColl, context, connection);
						}
					}
				}
				/**added by wangj 2015/05/28  需求编号:XD141222087,法人账户透支需求变更  end**/	
				
				/**added by lisj 2015-6-29  需求编号：【XD150123005】小微自助循环贷款改造  begin**/	
				IndexedCollection iCollResult4MRL = SqlClient.queryList4IColl("queryRelCLCBySerno",serno_value,connection);
				if (iCollResult4MRL != null&&iCollResult4MRL.size()>0 ) {
					for(int i=0;i<iCollResult4MRL.size();i++){
						KeyedCollection CountkColl=(KeyedCollection) iCollResult4MRL.get(i);
						String cus_id = (String) CountkColl.getDataValue("cus_id");//合同编号
						String cont_no = (String) CountkColl.getDataValue("cont_no");//合同编号
						String cont_status = (String) CountkColl.getDataValue("cont_status");//合同状态
						KeyedCollection CountSubkColl=dao.queryDetail(CTRLOANCONTSUB, cont_no, connection);
						String is_freeze=TagUtil.replaceNull4String(CountSubkColl.getDataValue("is_freeze"));
						if(!"2".equals(is_freeze)){
							CountSubkColl.setDataValue("is_freeze", "2");//合同解冻
							dao.update(CountSubkColl, connection);
						}
						IndexedCollection pvpIColl = dao.queryList("PvpLoanApp", " where cont_no='"+cont_no+"' and approve_status='997'", connection);//查询出账信息
						if(pvpIColl!=null && pvpIColl.size()>0){
							if("200".equals(cont_status)){//合同有效才需要发送交易
								KeyedCollection resqkColl=new KeyedCollection();
								resqkColl.addDataField("CLIENT_NO",cus_id);
								resqkColl.addDataField("OPERATION_TYPE","20");//10-冻结  20-解冻 30-终止
								resqkColl.addDataField("CONTRACT_NO",cont_no);
								resqkColl.addDataField("APPLY_TYPE","002");//001 个人自助 002 小微自助  003 法人透支
								/**调用ESB接口，发送报文*/
								CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
								ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
								serviceRel.trade0200200000502(resqkColl, context, connection);//小微自助循环贷款额度冻结/解冻发送交易
							}
						}
					}
				}
				/**added by lisj 2015-6-29  需求编号：【XD150123005】小微自助循环贷款改造  end**/
					
			}else{
				throw new EMPException("非冻结解冻类型！");
			}
			
		}catch(Exception e){
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new EMPException("单一法人授信流程审批报错，错误描述："+e.getMessage());
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
		// 设置业务数据至流程变量之中
		TableModelDAO dao = (TableModelDAO)this.getContext().getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this.getConnection());
		String manager_br_id = (String)kc.getDataValue("manager_br_id");//管理机构
		String manager_id = (String)kc.getDataValue("manager_id");
		/*added by wangj 额度冻结解冻流程修改 需求编号：XD141222087,法人账户透支需求变更 begin*/
		String agr_no = (String)kc.getDataValue("agr_no");
		String flag="zong2";
		try {
			IndexedCollection iColl= dao.queryList("LmtAppDetails", " where serno='"+pkVal+"'  and limit_name='100051' and ( froze_amt>0 or unfroze_amt>0 ) ",this.getConnection());
			if(iColl==null||iColl.isEmpty()||iColl.size()==0){
				flag="zhi";
			}else{
				KeyedCollection kColl=new KeyedCollection();
				kColl.addDataField("agr_no", agr_no);
				IndexedCollection ic = SqlClient.queryList4IColl("checkNodeRecordened", kColl, this.getConnection());
				if(ic!=null&&ic.size()>0) {
					KeyedCollection kcoll=(KeyedCollection)ic.get(0);
					flag = TagUtil.replaceNull4String(kcoll.getDataValue("flag"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("单一法人授信流程审批报错，错误描述："+e.getMessage());
		}
		
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		String cus_id = (String)kc.getDataValue("cus_id");
		CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");
		CusBase Cus = serviceCus.getCusBaseByCusId(cus_id, this.getContext(), this.getConnection());
		String belgLine = Cus.getBelgLine();
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("orgid", manager_br_id);
		param.put("manager_br_id", manager_br_id);
		param.put("manager_id", manager_id);
		param.put("flag", flag);
		param.put("bizline", belgLine);
		/*added by wangj 额度冻结解冻流程修改 需求编号：XD141222087,法人账户透支需求变更 end*/
		/** modified by wangj 2015-9-25 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
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
		param.put("IsTeam", IsTeam);
		/** modified by wangj 2015-9-25 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
		
		return param;
		} catch (Exception e) {
			throw new EMPException("设置业务数据至流程变量之中出错，错误描述："+e.getMessage());
		}
	}

}
