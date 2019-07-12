package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class UpdateIqpLoanApp4RepayBillOp extends CMISOperation {
	
	//operation TableModel
	private final String modelIdCtr = "CtrLoanCont";
	private final String modelIdCtrSub = "CtrLoanContSub";
	private final String modelIdView = "AccView";
	private final String rbuslmtModelId = "RBusLmtInfo";//业务和授信关联表
	private final String rbuslmtCreditModel = "RBusLmtcreditInfo";//业务和第三方授信关联表
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String flag = "success";
			String msg = "";
			String bill_no = "";
			String prd_id = "";
			String cus_id = "";
			String manager_br_id = "";
			String supcatalog = "";
			String loan_form = "";
			String is_close_loan = "";
			String is_spe_cus = "";
			BigDecimal cus_total_amt = new BigDecimal(0);
			try {
				bill_no = (String)context.getDataValue("bill_no");//偿还借据的借据编号
				prd_id = (String)context.getDataValue("prd_id");//产品编号
				cus_id = (String)context.getDataValue("cus_id");//客户码
				manager_br_id = (String)context.getDataValue("manager_br_id");//管理机构
				supcatalog = (String)context.getDataValue("supcatalog");//产品上级目录
				loan_form = (String)context.getDataValue("loan_form");//贷款形式
				is_close_loan = (String)context.getDataValue("is_close_loan");//是否无间贷
				is_spe_cus = (String)context.getDataValue("is_spe_cus");//是否特殊客户
				cus_total_amt = BigDecimalUtil.replaceNull(context.getDataValue("cus_total_amt"));//个人半年日均
			} catch (Exception e) {}
			if("".equals(bill_no) || "".equals(prd_id) || "".equals(cus_id) || "".equals(manager_br_id)){
				throw new EMPException("偿还借据的借据编号、借据余额、产品编号、客户码、管理机构为空!");
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			String condition = "where bill_no='"+bill_no+"'";
			
			KeyedCollection kCollAccView = dao.queryFirst(modelIdView, null, condition, connection);
			if(kCollAccView != null){
				String cont_no = (String)kCollAccView.getDataValue("cont_no");
				String cur_type = (String)kCollAccView.getDataValue("cur_type");
				
				//获取实时汇率  start
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
				KeyedCollection kCollRate = service.getHLByCurrType(cur_type, context, connection);
				if("failed".equals(kCollRate.getDataValue("flag"))){
					throw new EMPException("汇率表中未取到匹配汇率，请确认汇率表汇率配置！");
				}
				BigDecimal exchange_rate = BigDecimalUtil.replaceNull(kCollRate.getDataValue("sld"));//汇率
				//获取实时汇率  end
				
				BigDecimal bill_bal = BigDecimalUtil.replaceNull(kCollAccView.getDataValue("bill_bal"));
				//第一步：判断是否是个人经营性贷款,如果是则需校验个人半年日均
				if("PRD20120802659".equals(supcatalog)){
					/* modified by yangzy 2015/01/12  删除个人经营性无间贷日均存款校验 start */
				    /* modified by yangzy 2014/11/17 个人经营性无间贷日均存款校验 start */
					//BigDecimal rate = cus_total_amt.divide(bill_bal.multiply(exchange_rate),6,BigDecimal.ROUND_HALF_EVEN);
					//BigDecimal rate1 = (new BigDecimal(0.05)).subtract(rate).setScale(6,BigDecimal.ROUND_HALF_EVEN);
					//if(rate1.compareTo(new BigDecimal(0.00))>0){
			        //    msg = "客户及其配偶的半年日均合计占贷款金额占比不得低于5%";
			        //    flag = "false";
			        //    context.put("flag", flag);
					//	context.put("msg", msg);
					//	context.put("serno", "");
			        //    return "0";
			        //}
			        /* modified by yangzy 2014/11/17 个人经营性无间贷日均存款校验 end */
					/* modified by yangzy 2015/01/12 删除个人经营性无间贷日均存款校验 end */
				}
				//第二步：取借据业务数据保存到本笔无间贷或者借新还旧业务中
				KeyedCollection kCollCtr = dao.queryDetail(modelIdCtr, cont_no, connection);
				KeyedCollection kCollCtrSub = dao.queryDetail(modelIdCtrSub, cont_no, connection);
				String serno4Ctr = (String)kCollCtr.getDataValue("serno");
				
				String conditionStr = "where serno='"+serno4Ctr+"'";
				
				KeyedCollection lmtKcollOld = dao.queryAllDetail(rbuslmtModelId, serno4Ctr, connection);
				KeyedCollection lmtCreditKcollOld = dao.queryFirst(rbuslmtCreditModel,null, conditionStr, connection);
				String limit_acc_no = "";
				String limit_credit_no = "";
				if(lmtKcollOld != null){
					limit_acc_no = (String)lmtKcollOld.getDataValue("agr_no");
				}
				if(lmtCreditKcollOld != null){
					limit_credit_no = (String)lmtCreditKcollOld.getDataValue("agr_no");
				}
				
				//业务主表
				kCollCtr.setName("IqpLoanApp");
				kCollCtr.put("serno", serno);
				kCollCtr.put("apply_amount", bill_bal+"");
				kCollCtr.put("is_spe_cus", is_spe_cus);
				kCollCtr.put("apply_cur_type", kCollCtr.getDataValue("cont_cur_type")+"");
				kCollCtr.put("apply_date", context.getDataValue("OPENDAY").toString());
				kCollCtr.put("risk_open_amt", 0);
				kCollCtr.put("risk_open_rate", 0);
				kCollCtr.put("limit_acc_no", limit_acc_no);
				kCollCtr.put("limit_credit_no", limit_credit_no);
				kCollCtr.put("manager_br_id", manager_br_id);
				kCollCtr.put("input_id", context.getDataValue("currentUserId").toString());
				kCollCtr.put("input_br_id", context.getDataValue("organNo").toString());
				kCollCtr.put("input_date", context.getDataValue("OPENDAY").toString());
				kCollCtr.put("approve_status", "000");
				dao.insert(kCollCtr, connection);
				/**modified by lisj 2015-6-10 无间贷默认支付方式为自主支付 ,与2015-6-11上线 begin**/		
				/**modified by wangj 2015-6-26 需求编号：XD150609042 关于无间贷业务申请改造  begin*/
				/*CusServiceInterface serviceCus = (CusServiceInterface)serviceJndi.getModualServiceById("cusServices", "cus");//获取客户接口		
				//调用客户接口
				CusBase cusbase = serviceCus.getCusBaseByCusId(cus_id, context, connection);
                String belg_line  = cusbase.getBelgLine();*/
				/**modified by wangj 2015-6-26 需求编号：XD150609042 关于无间贷业务申请改造  end*/
				//业务从表
				kCollCtrSub.setName("IqpLoanAppSub");
				kCollCtrSub.put("serno", serno);
				kCollCtrSub.put("loan_form", loan_form);
				kCollCtrSub.put("is_close_loan", is_close_loan);
				kCollCtrSub.put("repay_bill", bill_no);
				kCollCtrSub.put("conf_pay_type", TagUtil.replaceNull4String(kCollCtrSub.getDataValue("is_conf_pay_type")));
				kCollCtrSub.put("apply_term", TagUtil.replaceNull4String(kCollCtrSub.getDataValue("cont_term")));
				kCollCtrSub.put("strategy_new_loan", TagUtil.replaceNull4String(kCollCtrSub.getDataValue("strategy_new_type")));
				/**modified by wangj 2015-6-26 需求编号：XD150609042 关于无间贷业务申请改造  begin*/
				if("1".equals(is_close_loan) ){ /*&& !"BL300".equals(belg_line)*/
					kCollCtrSub.setDataValue("conf_pay_type", "1");
				/**modified by wangj 2015-6-26 需求编号：XD150609042 关于无间贷业务申请改造  end*/
					kCollCtrSub.setDataValue("pay_type", "0");//无间贷默认支付方式为自主支付
				}
				dao.insert(kCollCtrSub, connection);
				/**modified by lisj 2015-6-10 无间贷默认支付方式为自主支付 ,与2015-6-11上线 end**/
				
				String contditionCtr = "where cont_no='"+cont_no+"'";
				String contditionIqp = "where serno='"+serno4Ctr+"'";
				//Tab页从表
				//客户经理登记
				IndexedCollection iCollCusManager = (IndexedCollection)dao.queryList("CusManager", contditionCtr, connection);
				for(int i = 0;i<iCollCusManager.size();i++){
					KeyedCollection kCollCusManager = (KeyedCollection)iCollCusManager.get(i);
					kCollCusManager.put("cont_no", "");
					kCollCusManager.put("serno", serno);
					dao.insert(kCollCusManager, connection);
				}
				//绿色减排信息
				KeyedCollection kCollIqpGreenDeclInfo = dao.queryDetail("IqpGreenDeclInfo", serno4Ctr, connection);
				if(kCollIqpGreenDeclInfo != null){
					if((String)kCollIqpGreenDeclInfo.getDataValue("serno") != null && !"".equals((String)kCollIqpGreenDeclInfo.getDataValue("serno"))){
						kCollIqpGreenDeclInfo.put("cont_no", "");
						kCollIqpGreenDeclInfo.put("serno", serno);
						dao.insert(kCollIqpGreenDeclInfo, connection);
					}
				}
				//PubBailInfo   保证金信息表  cont_no
				IndexedCollection iCollPubBailInfo = (IndexedCollection)dao.queryList("PubBailInfo", contditionCtr, connection);
				for(int i = 0;i<iCollPubBailInfo.size();i++){
					KeyedCollection kCollPubBailInfo = (KeyedCollection)iCollPubBailInfo.get(i);
					kCollPubBailInfo.put("cont_no", "");
					kCollPubBailInfo.put("serno", serno);
					dao.insert(kCollPubBailInfo, connection);
				}
				/**modified by lisj 2015-6-10 过滤无间贷放款账号信息 ,与2015-6-11上线 begin**/
				//IqpCusAcct 账户信息  cont_no
				IndexedCollection iCollIqpCusAcct = (IndexedCollection)dao.queryList("IqpCusAcct", contditionCtr, connection);
				if(!"".equals(is_close_loan) && "1".equals(is_close_loan)){
					for(int i = 0;i<iCollIqpCusAcct.size();i++){
						KeyedCollection kCollIqpCusAcct = (KeyedCollection)iCollIqpCusAcct.get(i);
						String acct_attr = (String) kCollIqpCusAcct.getDataValue("acct_attr");
						if(!"01".equals(acct_attr)){
							String pk_id = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
							kCollIqpCusAcct.put("cont_no", "");
							kCollIqpCusAcct.put("serno", serno);
							kCollIqpCusAcct.put("pk_id", pk_id);
							dao.insert(kCollIqpCusAcct, connection);
						}
					}
				}else{
					for(int i = 0;i<iCollIqpCusAcct.size();i++){
						KeyedCollection kCollIqpCusAcct = (KeyedCollection)iCollIqpCusAcct.get(i);
						String pk_id = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
						kCollIqpCusAcct.put("cont_no", "");
						kCollIqpCusAcct.put("serno", serno);
						kCollIqpCusAcct.put("pk_id", pk_id);
						dao.insert(kCollIqpCusAcct, connection);
					}
				}
				/**modified by lisj 2015-6-10 过滤无间贷放款账号信息 ,与2015-6-11上线 end**/
				//GrtLoanRGur           cont_no
				//IqpAccAccp  银行承兑汇票
				KeyedCollection kCollIqpAccAccp = dao.queryDetail("IqpAccAccp", serno4Ctr, connection);
				if(kCollIqpAccAccp != null){
					if((String)kCollIqpAccAccp.getDataValue("serno") != null && !"".equals((String)kCollIqpAccAccp.getDataValue("serno"))){
						kCollIqpAccAccp.put("serno", serno);
						dao.insert(kCollIqpAccAccp, connection);
					}
				}
				
				//IqpAccpDetail  承兑汇票申请明细
				IndexedCollection iCollIqpAccpDetail = (IndexedCollection)dao.queryList("IqpAccpDetail", contditionIqp, connection);
				for(int i = 0;i<iCollIqpAccpDetail.size();i++){
					KeyedCollection kCollIqpAccpDetail = (KeyedCollection)iCollIqpAccpDetail.get(i);
					kCollIqpAccpDetail.put("serno", serno);
					kCollIqpAccpDetail.put("pk1", "");
					dao.insert(kCollIqpAccpDetail, connection);
				}
				//IqpAppendTerms  费用信息
				IndexedCollection iCollIqpAppendTerms = (IndexedCollection)dao.queryList("IqpAppendTerms", contditionIqp, connection);
				for(int i = 0;i<iCollIqpAppendTerms.size();i++){
					KeyedCollection kCollIqpAppendTerms = (KeyedCollection)iCollIqpAppendTerms.get(i);
					String append_terms_pk = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
					kCollIqpAppendTerms.put("serno", serno);
					kCollIqpAppendTerms.put("append_terms_pk", append_terms_pk);
					dao.insert(kCollIqpAppendTerms, connection);
				}
				//IqpBksyndic     银团从表
				KeyedCollection kCollIqpBksyndic = dao.queryDetail("IqpBksyndic", serno4Ctr, connection);
				if(kCollIqpBksyndic != null){
					if((String)kCollIqpBksyndic.getDataValue("serno") != null && !"".equals((String)kCollIqpBksyndic.getDataValue("serno"))){
						kCollIqpBksyndic.put("serno", serno);
						dao.insert(kCollIqpBksyndic, connection);
					}
				}
				
				//IqpBksyndicInfo 银团信息
				IndexedCollection iCollIqpBksyndicInfo = (IndexedCollection)dao.queryList("IqpBksyndicInfo", contditionIqp, connection);
				for(int i = 0;i<iCollIqpBksyndicInfo.size();i++){
					KeyedCollection kCollIqpBksyndicInfo = (KeyedCollection)iCollIqpBksyndicInfo.get(i);
					String pk = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
					kCollIqpBksyndicInfo.put("serno", serno);
					kCollIqpBksyndicInfo.put("pk1", pk);
					dao.insert(kCollIqpBksyndicInfo, connection);
				}
				//IqpBtProInfo    Bt项目融资项目信息
				KeyedCollection kCollIqpBtProInfo = dao.queryDetail("IqpBtProInfo", serno4Ctr, connection);
				if(kCollIqpBtProInfo != null){
					if((String)kCollIqpBtProInfo.getDataValue("serno") != null && !"".equals((String)kCollIqpBtProInfo.getDataValue("serno"))){
						kCollIqpBtProInfo.put("serno", serno);
						dao.insert(kCollIqpBtProInfo, connection);
					}
				}
				
				//IqpCredit       信用证从表
				KeyedCollection kCollIqpCredit = dao.queryDetail("IqpCredit", serno4Ctr, connection);
				if(kCollIqpCredit != null){
					if((String)kCollIqpCredit.getDataValue("serno") != null && !"".equals((String)kCollIqpCredit.getDataValue("serno"))){
						kCollIqpCredit.put("serno", serno);
						dao.insert(kCollIqpCredit, connection);
					}
				}
				
				//IqpCreditExportApp 信用证项下出口押汇从表
				KeyedCollection kCollIqpCreditExportApp = dao.queryDetail("IqpCreditExportApp", serno4Ctr, connection);
				if(kCollIqpCreditExportApp != null){
					if((String)kCollIqpCreditExportApp.getDataValue("serno") != null && !"".equals((String)kCollIqpCreditExportApp.getDataValue("serno"))){
						kCollIqpCreditExportApp.put("serno", serno);
						dao.insert(kCollIqpCreditExportApp, connection);
					}
				}
				
				//IqpCsgnLoanInfo   委托贷款关联信息
				KeyedCollection kCollIqpCsgnLoanInfo = dao.queryDetail("IqpCsgnLoanInfo", serno4Ctr, connection);
				if(kCollIqpCsgnLoanInfo != null){
					if((String)kCollIqpCsgnLoanInfo.getDataValue("serno") != null && !"".equals((String)kCollIqpCsgnLoanInfo.getDataValue("serno"))){
						kCollIqpCsgnLoanInfo.put("serno", serno);
						dao.insert(kCollIqpCsgnLoanInfo, connection);
					}
				}
				
				//IqpDealercarInfo  经销商汽车信息
				IndexedCollection iCollIqpDealercarInfo = dao.queryList("IqpDealercarInfo", contditionIqp, connection);
				for(int i = 0;i<iCollIqpDealercarInfo.size();i++){
					KeyedCollection kCollIqpDealercarInfo = (KeyedCollection)iCollIqpDealercarInfo.get(i);
					String car_serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
					kCollIqpDealercarInfo.put("serno", serno);
					kCollIqpDealercarInfo.put("car_serno", car_serno);
					dao.insert(kCollIqpDealercarInfo, connection);
				}
				
				//IqpDelayCreditPur  延期信用证项下应收款买入从表
				KeyedCollection kCollIqpDelayCreditPur = dao.queryDetail("IqpDelayCreditPur", serno4Ctr, connection);
				if(kCollIqpDelayCreditPur != null){
					if((String)kCollIqpDelayCreditPur.getDataValue("serno") != null && !"".equals((String)kCollIqpDelayCreditPur.getDataValue("serno"))){
						kCollIqpDelayCreditPur.put("serno", serno);
						dao.insert(kCollIqpDelayCreditPur, connection);
					}
				}
				
				//IqpDelivAssure   提货担保从表
				KeyedCollection kCollIqpDelivAssure = dao.queryDetail("IqpDelivAssure", serno4Ctr, connection);
				if(kCollIqpDelivAssure != null){
					if((String)kCollIqpDelivAssure.getDataValue("serno") != null && !"".equals((String)kCollIqpDelivAssure.getDataValue("serno"))){
						kCollIqpDelivAssure.put("serno", serno);
						dao.insert(kCollIqpDelivAssure, connection);
					}
				}
				
				//IqpEquipInfo   银租通设备信息、
				IndexedCollection iCollIqpEquipInfo = (IndexedCollection)dao.queryList("IqpEquipInfo", contditionIqp, connection);
				for(int i = 0;i<iCollIqpEquipInfo.size();i++){
					KeyedCollection kCollIqpEquipInfo = (KeyedCollection)iCollIqpEquipInfo.get(i);
					String equip_pk = CMISSequenceService4JXXD.querySequenceFromDB("YZT", "fromDate", connection, context);
					kCollIqpEquipInfo.put("serno", serno);
					kCollIqpEquipInfo.put("equip_pk", equip_pk);
					dao.insert(kCollIqpEquipInfo, connection);
				}
				//IqpExportCollectCont 出口托收贷款从表
				KeyedCollection kCollIqpExportCollectCont = dao.queryDetail("IqpExportCollectCont", serno4Ctr, connection);
				if(kCollIqpExportCollectCont != null){
					if((String)kCollIqpExportCollectCont.getDataValue("serno") != null && !"".equals((String)kCollIqpExportCollectCont.getDataValue("serno"))){
						kCollIqpExportCollectCont.put("serno", serno);
						dao.insert(kCollIqpExportCollectCont, connection);
					}
				}
				
				///IqpExportOrderFin 出口订单融资从表
				KeyedCollection kCollIqpExportOrderFin = dao.queryDetail("IqpExportOrderFin", serno4Ctr, connection);
				if(kCollIqpExportOrderFin != null){
					if((String)kCollIqpExportOrderFin.getDataValue("serno") != null && !"".equals((String)kCollIqpExportOrderFin.getDataValue("serno"))){
						kCollIqpExportOrderFin.put("serno", serno);
						dao.insert(kCollIqpExportOrderFin, connection);
					}
				}
				
				//IqpExportPorderFin 出口商票融资从表
				KeyedCollection kCollIqpExportPorderFin = dao.queryDetail("IqpExportPorderFin", serno4Ctr, connection);
				if(kCollIqpExportPorderFin != null){
					if((String)kCollIqpExportPorderFin.getDataValue("serno") != null && !"".equals((String)kCollIqpExportPorderFin.getDataValue("serno"))){
						kCollIqpExportPorderFin.put("serno", serno);
						dao.insert(kCollIqpExportPorderFin, connection);
					}
				}
				
				//IqpForftin 福费廷从表
				KeyedCollection kCollIqpForftin = dao.queryDetail("IqpForftin", serno4Ctr, connection);
				if(kCollIqpForftin != null){
					if((String)kCollIqpForftin.getDataValue("serno") != null && !"".equals((String)kCollIqpForftin.getDataValue("serno"))){
						kCollIqpForftin.put("serno", serno);
						dao.insert(kCollIqpForftin, connection);
					}
				}
				
				//IqpFreedomPayInfo  自由还款信息
				IndexedCollection ICollIqpFreedomPayInfo = dao.queryList("IqpFreedomPayInfo", contditionIqp, connection);
				for(int i = 0;i<ICollIqpFreedomPayInfo.size();i++){
					KeyedCollection kCollIqpFreedomPayInfo = (KeyedCollection)ICollIqpFreedomPayInfo.get(i);
					kCollIqpFreedomPayInfo.put("serno", serno);
					dao.insert(kCollIqpFreedomPayInfo, connection);
				}
				//IqpGuarantInfo  保函信息表
				KeyedCollection kCollIqpGuarantInfo = dao.queryDetail("IqpGuarantInfo", serno4Ctr, connection);
				if(kCollIqpGuarantInfo != null){
					if((String)kCollIqpGuarantInfo.getDataValue("serno") != null && !"".equals((String)kCollIqpGuarantInfo.getDataValue("serno"))){
						kCollIqpGuarantInfo.put("serno", serno);
						dao.insert(kCollIqpGuarantInfo, connection);
					}
				}
				
				//IqpHouseInfo 厂房信息
				KeyedCollection kCollIqpHouseInfo = dao.queryDetail("IqpHouseInfo", serno4Ctr, connection);
				if(kCollIqpHouseInfo != null){
					if((String)kCollIqpHouseInfo.getDataValue("serno") != null && !"".equals((String)kCollIqpHouseInfo.getDataValue("serno"))){
						kCollIqpHouseInfo.put("serno", serno);
						dao.insert(kCollIqpHouseInfo, connection);
					}
				}
				
//				//IqpHousePurInfo 厂房按揭购置信息
//				KeyedCollection kCollIqpHousePurInfo = dao.queryDetail("IqpHousePurInfo", serno4Ctr, connection);
//				if(kCollIqpHousePurInfo != null){
//					if((String)kCollIqpHousePurInfo.getDataValue("serno") != null && !"".equals((String)kCollIqpHousePurInfo.getDataValue("serno"))){
//						kCollIqpHousePurInfo.put("serno", serno);
//						dao.insert(kCollIqpHousePurInfo, connection);
//					}
//				}
				
				//IqpIntbankAgt  同业代付从表
				KeyedCollection kCollIqpIntbankAgt = dao.queryDetail("IqpIntbankAgt", serno4Ctr, connection);
				if(kCollIqpIntbankAgt != null){
					if((String)kCollIqpIntbankAgt.getDataValue("serno") != null && !"".equals((String)kCollIqpIntbankAgt.getDataValue("serno"))){
						kCollIqpIntbankAgt.put("serno", serno);
						dao.insert(kCollIqpIntbankAgt, connection);
					}
				}
				
				//IqpInterFact  国际保理从表
				KeyedCollection kCollIqpInterFact = dao.queryDetail("IqpInterFact", serno4Ctr, connection);
				if(kCollIqpInterFact != null){
					if((String)kCollIqpInterFact.getDataValue("serno") != null && !"".equals((String)kCollIqpInterFact.getDataValue("serno"))){
						kCollIqpInterFact.put("serno", serno);
						dao.insert(kCollIqpInterFact, connection);
					}
				}
				
				//IqpLoanPromissory 项目背景信息
				KeyedCollection kCollIqpLoanPromissory = dao.queryDetail("IqpLoanPromissory", serno4Ctr, connection);
				if(kCollIqpLoanPromissory != null){
					if((String)kCollIqpLoanPromissory.getDataValue("serno") != null && !"".equals((String)kCollIqpLoanPromissory.getDataValue("serno"))){
						kCollIqpLoanPromissory.put("serno", serno);
						dao.insert(kCollIqpLoanPromissory, connection);
					}
				}
				
				//IqpPackLoan  打包贷款从表
				KeyedCollection kCollIqpPackLoan = dao.queryDetail("IqpPackLoan", serno4Ctr, connection);
				if(kCollIqpPackLoan != null){
					if((String)kCollIqpPackLoan.getDataValue("serno") != null && !"".equals((String)kCollIqpPackLoan.getDataValue("serno"))){
						kCollIqpPackLoan.put("serno", serno);
						dao.insert(kCollIqpPackLoan, connection);
					}
				}
				
				//IqpPurcarInfo  机构法人购车信息
				KeyedCollection kCollIqpPurcarInfo = dao.queryDetail("IqpPurcarInfo", serno4Ctr, connection);
				if(kCollIqpPurcarInfo != null){
					if((String)kCollIqpPurcarInfo.getDataValue("serno") != null && !"".equals((String)kCollIqpPurcarInfo.getDataValue("serno"))){
						kCollIqpPurcarInfo.put("serno", serno);
						dao.insert(kCollIqpPurcarInfo, connection);
					}
				}
				
				//IqpRentInfo  银租通出租信息
				IndexedCollection ICollIqpRentInfo = dao.queryList("IqpRentInfo", contditionIqp, connection);
				for(int i = 0;i<ICollIqpRentInfo.size();i++){
					KeyedCollection kCollIqpRentInfo = (KeyedCollection)ICollIqpRentInfo.get(i);
					String rent_serno = CMISSequenceService4JXXD.querySequenceFromDB("YZT", "fromDate", connection, context);
					kCollIqpRentInfo.put("serno", serno);
					kCollIqpRentInfo.put("rent_serno", rent_serno);
					dao.insert(kCollIqpRentInfo, connection);
				}
				
				//IqpStermGuarFin 短期信保融资从表
				KeyedCollection kCollIqpStermGuarFin = dao.queryDetail("IqpStermGuarFin", serno4Ctr, connection);
				if(kCollIqpStermGuarFin != null){
					if((String)kCollIqpStermGuarFin.getDataValue("serno") != null && !"".equals((String)kCollIqpStermGuarFin.getDataValue("serno"))){
						kCollIqpStermGuarFin.put("serno", serno);
						dao.insert(kCollIqpStermGuarFin, connection);
					}
				}
				
				//IqpTogetherRqstr 共同申请人
				IndexedCollection ICollIqpTogetherRqstr = dao.queryList("IqpTogetherRqstr", contditionIqp, connection);
				for(int i = 0;i<ICollIqpTogetherRqstr.size();i++){
					KeyedCollection kCollIqpTogetherRqstr = (KeyedCollection)ICollIqpTogetherRqstr.get(i);
					kCollIqpTogetherRqstr.put("serno", serno);
					dao.insert(kCollIqpTogetherRqstr, connection);
				}
				
				//IqpTrustReceiptLoan  信托收据贷款从表
				KeyedCollection kCollIqpTrustReceiptLoan = dao.queryDetail("IqpTrustReceiptLoan", serno4Ctr, connection);
				if(kCollIqpTrustReceiptLoan != null){
					if((String)kCollIqpTrustReceiptLoan.getDataValue("serno") != null && !"".equals((String)kCollIqpTrustReceiptLoan.getDataValue("serno"))){
						kCollIqpTrustReceiptLoan.put("serno", serno);
						dao.insert(kCollIqpTrustReceiptLoan, connection);
					}
				}
				//调用授信处理方法。
				doLimitManage(context,kCollCtr);
			}
			
			context.put("flag", flag);
			context.put("msg", msg);
			context.addDataField("serno", serno);
			
		}catch (EMPException ee) {
			context.put("flag", "false");
			context.put("msg", ee.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
	
	/**
	 * 授信处理方法，授信使用情况有如下几种情况
	 * 1、不使用授信 2、使用循环额度 3、使用一次性额度 4、合作方额度 5、使用循环额度+合作方额度 6、使用一次性额度+合作方额度
	 * @param context
	 * @throws EMPException
	 */
	public void doLimitManage(Context context,KeyedCollection kColl) throws EMPException{
		Connection connection = null;
		try {
            connection = this.getConnection(context);
			
			String serno = (String)kColl.getDataValue("serno");
			String limit_ind = (String)kColl.getDataValue("limit_ind");;//授信使用标志
			String prd_id = (String)kColl.getDataValue("prd_id");;//产品编号
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = "where serno='"+serno+"'";
			
			KeyedCollection lmtKcoll = dao.queryAllDetail(rbuslmtModelId, serno, connection);
			KeyedCollection lmtCreditKcoll = dao.queryFirst(rbuslmtCreditModel,null, condition, connection);
			
			//1、授信使用情况为：使用循环额度  或者  使用一次性额度
	        if("2".equals(limit_ind) || "3".equals(limit_ind)){
	        	//1.1、检查是否已经存在自有授信关联记录，如果有，那么更新记录；如果没有，则新增。
	        	if(!"".equals(lmtKcoll.getDataValue("serno")) && lmtKcoll.getDataValue("serno")!=null){
	        		lmtKcoll.setDataValue("agr_no", kColl.getDataValue("limit_acc_no"));
	        		dao.update(lmtKcoll, connection);
	        	}else{
	            	lmtKcoll.setDataValue("agr_no", kColl.getDataValue("limit_acc_no"));
	            	lmtKcoll.setDataValue("serno", serno);
	            	lmtKcoll.setDataValue("cont_no", "");
	            	dao.insert(lmtKcoll, connection);
	        	}
	        	//1.2、检查是否存在第三方授信关联记录，如果有则删除。
	        	if(!"".equals(lmtCreditKcoll.getDataValue("serno")) && lmtCreditKcoll.getDataValue("serno")!=null){
	        		String agr_no = (String)lmtCreditKcoll.getDataValue("agr_no");
	        		HashMap<String,String> hashMap = new HashMap<String,String>();
	        		hashMap.put("serno", serno);
	        		hashMap.put("agr_no", agr_no);
	        		dao.deleteAllByPks(rbuslmtCreditModel, hashMap, connection);
	        	}
	        	//setGreenInfo(serno,kColl.getDataValue("limit_acc_no")+"",context,connection);
	        }else if("4".equals(limit_ind)){//2、授信使用情况为：合作方额度
	        	String lmt_type = "";
	        	String limit_credit_no = (String)kColl.getDataValue("limit_credit_no");
	        	if("300021".equals(prd_id)){
	        		//不处理,到修改页面录入票据处理
	        	}else{
	        		lmt_type="03";
	        		//2.1、检查是否已经存在自有授信关联记录，如果有，则删除。
		        	if(!"".equals(lmtKcoll.getDataValue("serno")) && lmtKcoll.getDataValue("serno")!=null){
		        		dao.deleteAllByPk(rbuslmtModelId, serno, connection);
		        	}
		        	//2.2、检查是否存在第三方授信关联记录，如果有则更新；如果没有，则新增。
		        	if(!"".equals(lmtCreditKcoll.getDataValue("serno")) && lmtCreditKcoll.getDataValue("serno")!=null){
		        		String old_agr_no = (String)lmtCreditKcoll.getDataValue("agr_no");
		        		HashMap<String,String> hashMap = new HashMap<String,String>();
		        		hashMap.put("serno", serno);
		        		hashMap.put("agr_no", old_agr_no);
		        		dao.deleteAllByPks(rbuslmtCreditModel, hashMap, connection);
		        		
		        		lmtCreditKcoll.put("agr_no", kColl.getDataValue("limit_credit_no"));
		            	lmtCreditKcoll.put("lmt_type", "03");//03-合作方
		            	lmtCreditKcoll.put("serno", serno);
		            	lmtCreditKcoll.put("cont_no", "");
		            	dao.insert(lmtCreditKcoll, connection);
		        	}else{
		            	lmtCreditKcoll.setDataValue("agr_no", limit_credit_no);
		            	lmtCreditKcoll.setDataValue("lmt_type", lmt_type);//03-合作方
		            	lmtCreditKcoll.setDataValue("serno", serno);
		            	lmtCreditKcoll.setDataValue("cont_no", "");
		            	dao.insert(lmtCreditKcoll, connection);
		        	}
	        	}
	        	//setGreenInfo(serno,kColl.getDataValue("limit_credit_no")+"",context,connection);
	        }//3、授信使用情况为：使用循环额度+合作方额度   或者  使用一次性额度+合作方额度
	        else if("5".equals(limit_ind) || "6".equals(limit_ind)){
	        	//3.1、如果存在自有额度关联记录，那么先删除，再新增。
	        	if(!"".equals(lmtKcoll.getDataValue("serno")) && lmtKcoll.getDataValue("serno")!=null){
	        		dao.deleteAllByPk(rbuslmtModelId, serno, connection);
	        	}
	        	lmtKcoll.setDataValue("agr_no", kColl.getDataValue("limit_acc_no"));
	        	lmtKcoll.setDataValue("serno", serno);
	        	lmtKcoll.setDataValue("cont_no", "");
	        	dao.insert(lmtKcoll, connection);
	        	
	        	//3.2、如果存在第三方额度关联记录，那么先删除，再新增。
	        	if(!"".equals(lmtCreditKcoll.getDataValue("serno")) && lmtCreditKcoll.getDataValue("serno")!=null){
	        		String agr_no = (String)lmtCreditKcoll.getDataValue("agr_no");
	        		HashMap<String,String> hashMap = new HashMap<String,String>();
	        		hashMap.put("serno", serno);
	        		hashMap.put("agr_no", agr_no);
	        		dao.deleteAllByPks(rbuslmtCreditModel, hashMap, connection);
	        	}
	        	
	        	lmtCreditKcoll.setDataValue("agr_no", kColl.getDataValue("limit_credit_no"));
	        	lmtCreditKcoll.setDataValue("lmt_type", "03");//03-合作方
	        	lmtCreditKcoll.setDataValue("serno", serno);
	        	lmtCreditKcoll.setDataValue("cont_no", "");
	        	dao.insert(lmtCreditKcoll, connection);
	        	//setGreenInfo(serno,kColl.getDataValue("limit_acc_no")+"",context,connection);
	        }//4、授信使用情况为：不使用授信
	        else if("1".equals(limit_ind)){
	        	//4.1、如果存在自有额度关联记录，那么先删除，再新增。
	        	if(!"".equals(lmtKcoll.getDataValue("serno")) && lmtKcoll.getDataValue("serno")!=null){
	        		dao.deleteAllByPk(rbuslmtModelId, serno, connection);
	        	}
	        	
	        	//4.2、如果存在第三方额度关联记录，那么先删除，再新增。
	        	if(!"".equals(lmtCreditKcoll.getDataValue("serno")) && lmtCreditKcoll.getDataValue("serno")!=null){
	        		String agr_no = (String)lmtCreditKcoll.getDataValue("agr_no");
	        		HashMap<String,String> hashMap = new HashMap<String,String>();
	        		hashMap.put("serno", serno);
	        		hashMap.put("agr_no", agr_no);
	        		dao.deleteAllByPks(rbuslmtCreditModel, hashMap, connection);
	        	}
	        	//setGreenInfo(serno,null,context,connection);
	        }
		} catch (Exception e) {
			throw new EMPException(e);
		}
	}
	
	public void setGreenInfo(String serno,String limit_credit_no,Context context,Connection connection)throws EMPException{
		try {
		TableModelDAO dao = this.getTableModelDAO(context);
		String green_indus = "";
		KeyedCollection kCollLmt = null;
		KeyedCollection kColl = dao.queryDetail("IqpGreenDeclInfo", "", connection);
		if(!"".equals(limit_credit_no) && limit_credit_no != null){
			kCollLmt = dao.queryDetail("LmtAgrDetails", limit_credit_no, connection);
			green_indus = (String)kCollLmt.getDataValue("green_indus");
			kColl = dao.queryDetail("IqpGreenDeclInfo", serno, connection);
			kColl.put("green_indus", green_indus);
		}
		
		if(green_indus!=null && !"".equals(green_indus)){
		}else{
			kColl.put("green_indus", "2");
			kColl.put("green_indus_displayname", "否");
		}
		//如果IqpGreenDeclInfo中不存在数据，则保存
		String serno_value = (String)kColl.getDataValue("serno");
		if(("".equals(serno_value) || serno_value == null) && (green_indus == null || "".equals(green_indus) || "2".equals(green_indus))){
			kColl.put("green_indus", "2");
			kColl.put("serno", serno);
			dao.insert(kColl, connection);
		}
		} catch (Exception e) {
			throw new EMPException(e);
		}
	}
}
