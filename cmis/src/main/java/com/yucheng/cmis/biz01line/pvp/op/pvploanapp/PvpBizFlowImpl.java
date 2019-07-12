package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dc.eai.data.CompositeData;
import com.dcfs.esb.pack.standardxml.PackUtil;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.util.EMPUtils;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.op.trade.Trade0200200001001;
import com.yucheng.cmis.biz01line.esb.op.trade.TradeDkffsq;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.biz01line.pvp.component.PvpBizFlowComponent;
import com.yucheng.cmis.platform.organization.domains.SOrg;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;
import com.yucheng.cmis.platform.workflow.msi.BIZProcessInterface;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.ESBUtil;

public class PvpBizFlowImpl extends CMISComponent implements
		BIZProcessInterface {
	private static final String PVPLOANMODEL = "PvpLoanApp";// 出账申请

	public void executeAtCallback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行打回处理逻辑
		System.out.println("--------");
	}

	public void executeAtTakeback(WfiMsgQueue wfiMsg) throws EMPException {
		// 流程审批中执行拿回处理逻辑
		System.out.println("--------");
	}

	public void executeAtWFAgree(WfiMsgQueue wfiMsg) throws EMPException {
		String serno = "";
		String prdId = "";
		String tableName = "";
		try {
			serno = wfiMsg.getPkValue();
			tableName = wfiMsg.getTableName();
			PvpBizFlowComponent pvpBizFlowComponent = (PvpBizFlowComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(
							"PvpBizFlowComponent", this.getContext(),
							this.getConnection());

			// 展期业务
			if ("IqpExtensionPvp".equals(tableName)) {
				pvpBizFlowComponent.doWfAgreeForIqpExtend(serno);
			}
			// 非展期业务
			else {
				TableModelDAO dao = (TableModelDAO) this.getContext()
						.getService(CMISConstance.ATTR_TABLEMODELDAO);
				KeyedCollection pvpLoanKColl = dao.queryDetail(PVPLOANMODEL,
						serno, this.getConnection());
				prdId = (String) pvpLoanKColl.getDataValue("prd_id");
				// 银行承兑汇票业务
				if (prdId.equals("200024")) {
					pvpBizFlowComponent.doWfAgreeForIqpAccp(serno);
					pvpBizFlowComponent.updateContDate(serno);
					pvpBizFlowComponent.deductContBalance(serno);
				}
				// 直贴业务
				else if (prdId.equals("300021") || prdId.equals("300020")) {
					pvpBizFlowComponent.doWfAgreeForIqpDisc(serno);
					pvpBizFlowComponent.deductContBalance(serno);
				}
				// 转贴业务
				else if (prdId.equals("300024") || prdId.equals("300023")
						|| prdId.equals("300022")) {
					pvpBizFlowComponent.doWfAgreeForIqpRpddscnt(serno);
				}
				// 贸易融资:表内业务
				else if (prdId.equals("500026") || prdId.equals("500021")
						|| prdId.equals("500020") || prdId.equals("500031")
						|| prdId.equals("500022") || prdId.equals("500025")
						|| prdId.equals("500024") || prdId.equals("500023")) {
					pvpBizFlowComponent.updateContDate(serno);
					pvpBizFlowComponent.doWfAgreeForIqpTfLoan(serno);
					pvpBizFlowComponent.deductContBalance(serno);
				} else if (prdId.equals("500029")) {// 福费廷：出账走贴现交易
					pvpBizFlowComponent.updateContDate(serno);
					pvpBizFlowComponent.doWfAgreeForIqpTfLoanForFft(serno);
					pvpBizFlowComponent.deductContBalance(serno);

				} else if (prdId.equals("500028")) {// 延期信用证项下应收款买入：出账走贴现交易
					pvpBizFlowComponent.updateContDate(serno);
					pvpBizFlowComponent.doWfAgreeForIqpTfLoanForYQXYZMR(serno);
					pvpBizFlowComponent.deductContBalance(serno);

				} else if (prdId.equals("500027")) {// 远期信用证项下汇票贴现：出账走贴现交易
					pvpBizFlowComponent.updateContDate(serno);
					pvpBizFlowComponent.doWfAgreeForIqpTfLoanForYQXYZTX(serno);
					pvpBizFlowComponent.deductContBalance(serno);
				}
				// 贸易融资：表外业务[发往国结]
				else if (prdId.equals("400020") || prdId.equals("500032")
						|| prdId.equals("700020") || prdId.equals("700021")) {
					pvpBizFlowComponent.updateContDate(serno);
					pvpBizFlowComponent.doWfAgreeForIqpTfBw(serno);
					pvpBizFlowComponent.deductContBalance(serno);
				}
				// 资产转受让业务
				else if (prdId.equals("600020")) {
					pvpBizFlowComponent.doWfAgreeForIqpAsset(serno);
				}
				// 资产流转业务
				else if (prdId.equals("600021")) {
					pvpBizFlowComponent.doWfAgreeForIqpAssetTrans(serno);
				}
				// 资产证券化业务
				else if (prdId.equals("600022")) {
					pvpBizFlowComponent.doWfAgreeForIqpAssetPro(serno);
					/* added by lisj 2015-7-2 需求编号：【XD150123005】小微自助循环贷款改造 begin */
				} else if (prdId.equals("100088")) {
					// 小微自助循环贷款
					KeyedCollection kColl = dao.queryDetail(PVPLOANMODEL,
							serno, this.getConnection());
					String cont_no = (String) kColl.getDataValue("cont_no");
					Trade0200200001001 trade = new Trade0200200001001();
					trade.doWfAgreeForSelfLoan(cont_no, this.getContext(), this
							.getConnection());
					KeyedCollection retKColl = trade.trade0200100000101(
							cont_no, this.getContext(), this.getConnection()); // 调用接口

					if (!TagUtil.haveSuccess(retKColl, this.getContext())) {
						throw new Exception("【小微自助贷款协议授权】,交易失败！");
					} else {
						EMPLog.log("Trade0200200001001", EMPLog.INFO, 0,
								"【小微自助贷款协议发放】交易处理完成...", null);
					}
					pvpBizFlowComponent.deductContBalance(serno);
					/* added by lisj 2015-7-2 需求编号：【XD150123005】小微自助循环贷款改造 end */
				}// 普通贷款及其他业务
				else {
					pvpBizFlowComponent.updateContDate(serno);
					String tranSerno=pvpBizFlowComponent.doWfAgreeForIqpLoan(serno);
					pvpBizFlowComponent.deductContBalance(serno);
					//插入授权数据后直接调用放款接口进行放款
					TradeDkffsq tradeDkffsq=new TradeDkffsq();
					Context context=this.getContext();
					Connection connection=this.getConnection();
					context.put("tran_serno", tranSerno);
					context.put("service_code", "30210004");
					context.put("sence_code", "02");
					CompositeData reqCD = tradeDkffsq.doExecute(context, connection);
					/** 打印后台发送日志 */
					EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(reqCD), "UTF-8"));
					/** 执行发送操作 */
					KeyedCollection reqKcoll=TagUtil.replaceCD2KColl(reqCD);
					KeyedCollection retCD = ESBUtil.sendEsbMsg((KeyedCollection)reqKcoll.getDataElement("SYS_HEAD"), (KeyedCollection)reqKcoll.getDataElement("BODY"));
					System.out.println("****************************************");
					System.out.println(new String(PackUtil.pack(reqCD), "UTF-8"));
					//获取报文系统头
					KeyedCollection retKColl=(KeyedCollection)retCD.getDataElement("SYS_HEAD");
					//获取报文体
					KeyedCollection respBodyKColl = (KeyedCollection)retCD.getDataElement("BODY");
					
					IndexedCollection retArr=(IndexedCollection)retKColl.getDataElement("RetInfArry");
					KeyedCollection retObj=(KeyedCollection)retArr.get(0);
					if("000000".equals(retObj.getDataValue("RetCd"))){//成功
						dulReturnMsg(retCD,"02", context, connection);
						//更新借据账号序号
						KeyedCollection reqBody=(KeyedCollection)reqKcoll.getDataElement("BODY");
						KeyedCollection accLoan=dao.queryDetail("AccLoan", (String)reqBody.getDataValue("DblNo"), connection);
						accLoan.setDataValue("base_acct_no", (String)respBodyKColl.getDataValue("AcctNoCrdNo"));
						accLoan.setDataValue("acct_seq_no", (String)respBodyKColl.getDataValue("AcctNoSeqNo"));
						dao.update(accLoan, connection);//更新借据
					}else{
						dulReturnMsg(retCD,"01", context, connection);
						throw new EMPException("贷款开立发送核心失败，错误码："+retObj.getDataValue("RetCd")+",错误信息："+retObj.getDataValue("RetInf"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e.getMessage());
		}
	}

	public void executeAtWFAppProcess(WfiMsgQueue wfiMsg) throws EMPException {
		// TODO Auto-generated method stub
		System.out.println("--------");
	}

	public void executeAtWFDisagree(WfiMsgQueue wfiMsg) throws EMPException {
		// 审批否决时执行业务处理逻辑
		String serno = "";
		try {
			serno = wfiMsg.getPkValue();
			TableModelDAO dao = (TableModelDAO) this.getContext().getService(
					CMISConstance.ATTR_TABLEMODELDAO);

			KeyedCollection kCollPvp = dao.queryDetail(PVPLOANMODEL, serno,
					this.getConnection());
			BigDecimal pvp_amt = BigDecimalUtil.replaceNull(kCollPvp
					.getDataValue("pvp_amt"));
			// 取出账申请的登记日期
			String input_date = (String) kCollPvp.getDataValue("input_date");
			// 取出账队列额度控制表
			KeyedCollection limitKColl = dao.queryFirst("PvpLimitSet", null,
					" where open_day = '" + input_date + "'", this
							.getConnection());
			String sernoFromPvpLimitSet = (String) limitKColl
					.getDataValue("serno");
			if (sernoFromPvpLimitSet != null && "".equals(sernoFromPvpLimitSet)) {
				BigDecimal out_limit_amt = BigDecimalUtil
						.replaceNull(limitKColl.getDataValue("out_limit_amt"));
				out_limit_amt = out_limit_amt.subtract(pvp_amt);
				limitKColl.setDataValue("out_limit_amt", out_limit_amt);
				dao.update(limitKColl, this.getConnection());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException("否决流程业务处理异常，请检查业务处理逻辑！");
		}
	}

	public Map<String, String> putBizData2WfVar(String tabModelId,
			String pkVal, KeyedCollection modifyVar) throws EMPException {
		TableModelDAO dao = (TableModelDAO) this.getContext().getService(
				CMISConstance.ATTR_TABLEMODELDAO);
		Map<String, String> param = new HashMap<String, String>();
		String manager_id = null;
		String manager_br_id = null;
		String IsBranch = "";// 分行下是否存在支行;Y--存在支行；N--不存在
		KeyedCollection kc = dao.queryDetail(tabModelId, pkVal, this
				.getConnection());
		String prd_id = "";
		try {
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory
					.getInstance();
			if (tabModelId.equals("IqpExtensionPvp")) { // 展期表字段不同，单独处理
				manager_br_id = (String) kc.getDataValue("manager_br_id");// 管理机构
				manager_id = (String) kc.getDataValue("manager_id");
				
				param.put("manager_br_id", manager_br_id);
				param.put("manager_id", manager_id);
			} else {
				String cont_no = (String) kc.getDataValue("cont_no");
				prd_id = (String) kc.getDataValue("prd_id");
				KeyedCollection contKcoll = null;
				if ("300024".equals(prd_id) || "300023".equals(prd_id)
						|| "300022".equals(prd_id)) {
					contKcoll = (KeyedCollection) dao.queryDetail(
							"CtrRpddscntCont", cont_no, this.getConnection());
					String rpddscnt_type = (String) contKcoll
							.getDataValue("rpddscnt_type");
					// '01':'买入买断', '02':'买入返售', '03':'卖出卖断', '04':'卖出回购',
					// '05':'内部转贴现', '06':'再贴现'
					if ("01".equals(rpddscnt_type)
							|| "03".equals(rpddscnt_type)) {
						param.put("rpddscnt_type", "01");
					} else if ("02".equals(rpddscnt_type)
							|| "04".equals(rpddscnt_type)
							|| "06".equals(rpddscnt_type)) {
						param.put("rpddscnt_type", "02");
					} else {
						param.put("rpddscnt_type", "03");
					}
				} else if ("600020".equals(prd_id)) {
					contKcoll = (KeyedCollection) dao.queryDetail(
							"CtrAssetstrsfCont", cont_no, this.getConnection());
					String input_br_id = (String) kc
							.getDataValue("input_br_id");
					param.put("input_br_id", input_br_id);
				} else if ("600021".equals(prd_id)) {
					contKcoll = (KeyedCollection) dao.queryDetail(
							"CtrAssetTransCont", cont_no, this.getConnection());
					String input_br_id = (String) kc
							.getDataValue("input_br_id");
					param.put("input_br_id", input_br_id);
				} else if ("600022".equals(prd_id)) {
					contKcoll = (KeyedCollection) dao.queryDetail(
							"CtrAssetProCont", cont_no, this.getConnection());
					String input_br_id = (String) kc
							.getDataValue("input_br_id");
					param.put("input_br_id", input_br_id);
				} else {
					contKcoll = (KeyedCollection) dao.queryDetail(
							"CtrLoanCont", cont_no, this.getConnection());
					KeyedCollection contKcollSub = (KeyedCollection) dao
							.queryDetail("CtrLoanContSub", cont_no, this
									.getConnection());
					// 获取客户条线,委托贷款种类
					String belgLine = null;
					String cus_id = (String) contKcoll.getDataValue("cus_id");
					CusServiceInterface service = (CusServiceInterface) serviceJndi
							.getModualServiceById("cusServices", "cus");
					CusBase Cus = service.getCusBaseByCusId(cus_id, this
							.getContext(), this.getConnection());
					belgLine = Cus.getBelgLine();
					if (!"300021".equals(prd_id) && !"300020".equals(prd_id)) {
						String principal_loan_typ = (String) contKcollSub
								.getDataValue("principal_loan_typ");
						param.put("principal_loan_typ", principal_loan_typ);
					}
					param.put("bizline", belgLine);
				}
				String serno = (String) contKcoll.getDataValue("serno");
				String condition = "where is_main_manager='1' and serno='"
						+ serno + "'";
				IndexedCollection iqpIColl = dao.queryList("CusManager",
						condition, this.getConnection());

				if (iqpIColl.size() > 0) {
					KeyedCollection iqpKColl = (KeyedCollection) iqpIColl
							.get(0);
					manager_id = (String) iqpKColl.getDataValue("manager_id");// 取得责任人
				}
				manager_br_id = (String) kc.getDataValue("manager_br_id");// 管理机构
				param.put("manager_br_id", manager_br_id);
				param.put("manager_id", manager_id);
			} 
			/** XD150918069	丰泽鲤城区域团队业务流程改造  Edited By FChengLiang 2015-09-21  start*/
			String IsTeam = "";
			KeyedCollection kColl4tm = dao.queryFirst("STeamUser",null,"where 1=1 and mem_no = '"+manager_id+"'", this.getConnection());
			if(kColl4tm!=null && kColl4tm.size()>0&&kColl4tm.getDataValue("team_no")!=null&&!"".equals((String)kColl4tm.getDataValue("team_no"))){
				IsTeam = "yes";
			}else{
				IsTeam = "no";
			}
			param.put("IsTeam", IsTeam);
			/** XD150918069	丰泽鲤城区域团队业务流程改造  Edited By FChengLiang 2015-09-21 end*/
			// 判断管理机构上级机构是否是泉州银行
			OrganizationServiceInterface serviceUser;

			serviceUser = (OrganizationServiceInterface) serviceJndi
					.getModualServiceById("organizationServices",
							"organization");
			/** 责任人存在多个机构时取责任机构 */
			List<SOrg> orgslist = serviceUser.getDeptOrgByActno(manager_id,
					this.getConnection());
			if (orgslist != null && orgslist.size() == 1) {// 责任人只有一个机构则取该机构码
				manager_br_id = orgslist.get(0).getOrganno();
			}
			SOrg org_info = serviceUser.getOrgByOrgId(manager_br_id, this
					.getConnection());// 根据机构号获取机构信息
			String suporganno = org_info.getSuporganno();// 获取主管客户经理所在机构的上级机构
			if ("9350500000".equals(suporganno)) {// （若上级机构不是泉州分行，则需要走分行行长）
				IsBranch = "Y";
			} else {
				IsBranch = "N";
			}
			param.put("IsBranch", IsBranch);
			param.put("prd_id", prd_id);
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		return param;
	}
	
	/**
	 * 针对授权信息更新本地授权信息更新操作实现方法
	 * @param retKColl 反馈状态
	 * @param context 上下文
	 * @param connection 数据库连接
	 */
	private void dulReturnMsg(KeyedCollection retCD,String status, Context context, Connection connection) throws EMPException {
		String serno = null;
		if(context.containsKey("tran_serno")){//提交记录交易流水号
			serno = (String)context.getDataValue("tran_serno");
		}
		if(serno == null || serno.trim().length() == 0){
			throw new EMPException("更新授权台帐表，获取授权交易流水号失败！");
		}
		KeyedCollection retKColl=(KeyedCollection)retCD.getDataElement("SYS_HEAD");
		KeyedCollection respBodyKColl = (KeyedCollection)retCD.getDataElement("BODY");
		String acctNoCrdNo = (String)respBodyKColl.getDataValue("AcctNoCrdNo");//账号/卡号
		String acctNoSeqNo = (String)respBodyKColl.getDataValue("AcctNoSeqNo");//账号序号
		TableModelDAO dao = (TableModelDAO) context
				.getService(CMISConstance.ATTR_TABLEMODELDAO);
		KeyedCollection authKcoll = dao.queryDetail("PvpAuthorize", serno, connection);
		String authorize_no = (String)authKcoll.getDataValue("authorize_no");
		IndexedCollection pvpIColl = dao.queryList("PvpAuthorize", " where authorize_no = '"+authorize_no+"' and status <> '02'", connection);
		String sendNum = "";//发送次数
		for(int i=0;i<pvpIColl.size();i++){
			KeyedCollection kc = (KeyedCollection)pvpIColl.get(i);
			/** 发送次数+1 */
			sendNum = (String)kc.getDataValue("send_times");
			if(sendNum == null || sendNum.trim().length() == 0){
				sendNum = "0";
			}
			int sendTimes = Integer.parseInt(sendNum);
			sendNum = String.valueOf(sendTimes+1);
			kc.setDataValue("status", status);
			kc.setDataValue("send_times", sendNum);
			kc.setDataValue("tran_date", context.getDataValue(CMISConstance.OPENDAY));//授权发送时更新交易日期
			IndexedCollection retArr=(IndexedCollection)retKColl.getDataElement("RetInfArry");
			KeyedCollection retObj=(KeyedCollection)retArr.get(0);
			////kc.setDataValue("return_code", retKColl.getDataValue("RET_CODE"));
			////kc.setDataValue("return_desc", retKColl.getDataValue("RET_MSG"));
//			kc.addDataField("base_acct_no", acctNoCrdNo);
//			kc.addDataField("acct_seq_no", acctNoSeqNo);
			kc.setDataValue("return_code", retObj.getDataValue("RetCd"));
			kc.setDataValue("return_desc", retObj.getDataValue("RetInf"));
			dao.update(kc, connection);
		}
	}
}
