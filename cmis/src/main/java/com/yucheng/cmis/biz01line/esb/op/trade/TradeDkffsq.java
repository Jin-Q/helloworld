package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.tagext.Tag;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.FieldType;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
/**
 * 贷款发放授权交易
 * @author liqh
 *
 */
public class TradeDkffsq extends TranClient{
	private static final String AUTHMODEL = "PvpAuthorize";
	private static final String PVPSUBMODEL = "PvpAuthorizeSub";
	
	public Map doExecuteYm(Context context, Connection connection)
			throws EMPException {
		/** 封装发送报文信息 */
		//王小虎注释//CompositeData reqCD= new CompositeData();
		KeyedCollection headKcoll=new KeyedCollection();
		Map<String, KeyedCollection> container=new HashMap<String, KeyedCollection>();
	try{
		String serviceCode = (String)context.getDataValue("service_code");
		String senceCode = (String)context.getDataValue("sence_code");
		String tran_serno = (String)context.getDataValue("tran_serno");
		//获取dao
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		/** 通过交易码判断所需执行的交易，以及需要准备的交易数据 */
		KeyedCollection authKColl = dao.queryDetail(AUTHMODEL, tran_serno, connection);
		String serno = (String)authKColl.getDataValue("serno");
		ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
		
		/** 系统头 */
		//王小虎注释//reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, TradeConstance.SERVICE_SCENE_DKFFSQ, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
		headKcoll.put("SvcCd", serviceCode);
		headKcoll.put("ScnCd", TradeConstance.SERVICE_SCENE_DKFFSQ);
		headKcoll.addDataField("TxnMd","ONLINE");
		headKcoll.addDataField("UsrLngKnd","CHINESE");
		headKcoll.addDataField("jkType","cbs");
		/** 应用头 */
		//王小虎注释//reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
		//王小虎注释//(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
		
		/** 封装报文体 */
		KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl);
		
//		/** 核算与信贷业务品种映射 START */
//		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//		ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//		String lmPrdId = service.getPrdBasicCLPM2LM(reflectKColl.getDataValue("CONTRACT_NO").toString(), authKColl.getDataValue("prd_id").toString(), context, connection);
//		/** 核算与信贷业务品种映射 END */
		
		////CompositeData bodyCD = new CompositeData();
		KeyedCollection bodyKcoll=new KeyedCollection("body");
		
		bodyKcoll.addDataField("ExecTp", reflectKColl.getDataValue("ExecTp"));
		bodyKcoll.addDataField("AcctNoCrdNo", reflectKColl.getDataValue("AcctNoCrdNo"));
		bodyKcoll.addDataField("TxnAmt", reflectKColl.getDataValue("TxnAmt"));
		bodyKcoll.addDataField("CstNo", reflectKColl.getDataValue("CstNo"));
		bodyKcoll.addDataField("DblNo", reflectKColl.getDataValue("DblNo"));
		bodyKcoll.addDataField("PdTp", reflectKColl.getDataValue("PdTp"));
		bodyKcoll.addDataField("AcctBlngInstNo", reflectKColl.getDataValue("AcctBlngInstNo"));
		bodyKcoll.addDataField("EstblshdInstNo", reflectKColl.getDataValue("EstblshdInstNo"));
		bodyKcoll.addDataField("LoanCstNo", reflectKColl.getDataValue("LoanCstNo"));
		bodyKcoll.addDataField("CstMgrCd", reflectKColl.getDataValue("CstMgrCd"));
		bodyKcoll.addDataField("PrftCnrlCd", reflectKColl.getDataValue("PrftCnrlCd"));
		bodyKcoll.addDataField("AcctDsc", reflectKColl.getDataValue("AcctDsc"));
		bodyKcoll.addDataField("Trm", reflectKColl.getDataValue("Trm"));
		bodyKcoll.addDataField("TrmTp", reflectKColl.getDataValue("TrmTp"));
		bodyKcoll.addDataField("BegDt", reflectKColl.getDataValue("BegDt"));
		bodyKcoll.addDataField("ExprtnDt", TagUtil.formatDate(reflectKColl.getDataValue("ExprtnDt")));
		bodyKcoll.addDataField("DstcWiOrWthtInd", reflectKColl.getDataValue("DstcWiOrWthtInd"));
		bodyKcoll.addDataField("BlngKnd", reflectKColl.getDataValue("BlngKnd"));
		bodyKcoll.addDataField("DbtCrdtFlg", reflectKColl.getDataValue("DbtCrdtFlg"));
		bodyKcoll.addDataField("AcctPpsCd", reflectKColl.getDataValue("AcctPpsCd"));
		bodyKcoll.addDataField("PlanMd", reflectKColl.getDataValue("PlanMd"));
		bodyKcoll.addDataField("IntStlmntFrqcy", reflectKColl.getDataValue("IntStlmntFrqcy"));
		bodyKcoll.addDataField("NxtStlmntIntDt", reflectKColl.getDataValue("NxtStlmntIntDt"));
		bodyKcoll.addDataField("IntStlmntDt", reflectKColl.getDataValue("IntStlmntDt"));
		bodyKcoll.addDataField("BlonLoanClcTrmTms", reflectKColl.getDataValue("BlonLoanClcTrmTms"));
		bodyKcoll.addDataField("FrstStgTrmNum", reflectKColl.getDataValue("FrstStgTrmNum"));
		bodyKcoll.addDataField("AcrItrvTrmNum", reflectKColl.getDataValue("AcrItrvTrmNum"));
		bodyKcoll.addDataField("AcrVal", reflectKColl.getDataValue("AcrVal"));
		bodyKcoll.addDataField("AcrRto", reflectKColl.getDataValue("AcrRto"));
		bodyKcoll.addDataField("IntRateEnblMd", reflectKColl.getDataValue("IntRateEnblMd"));
		bodyKcoll.addDataField("IntRateModDt", reflectKColl.getDataValue("IntRateModDt"));
		bodyKcoll.addDataField("IntRateModCcy", reflectKColl.getDataValue("IntRateModCcy"));
		bodyKcoll.addDataField("IntRateModDay", reflectKColl.getDataValue("IntRateModDay"));
		bodyKcoll.addDataField("ClctPnyIntCmpdIntFlg", reflectKColl.getDataValue("ClctPnyIntCmpdIntFlg"));
		bodyKcoll.addDataField("ClctSubCmpdIntFlg", reflectKColl.getDataValue("ClctSubCmpdIntFlg"));
		bodyKcoll.addDataField("DrtnWthdFlg", reflectKColl.getDataValue("DrtnWthdFlg"));
		bodyKcoll.addDataField("PrjNo", reflectKColl.getDataValue("PrjNo"));
		bodyKcoll.addDataField("Ccy", reflectKColl.getDataValue("Ccy"));
		bodyKcoll.addDataField("CtrOrgnlAmt", reflectKColl.getDataValue("CtrOrgnlAmt"));
		bodyKcoll.addDataField("FndSrcCtyCd", reflectKColl.getDataValue("FndSrcCtyCd"));
		bodyKcoll.addDataField("FndSrcProvCd", reflectKColl.getDataValue("FndSrcProvCd"));
		bodyKcoll.addDataField("DstrbtDdlnDt", reflectKColl.getDataValue("DstrbtDdlnDt"));
		bodyKcoll.addDataField("ClctPnyIntFlg", reflectKColl.getDataValue("ClctPnyIntFlg"));
		bodyKcoll.addDataField("ClctCmpdIntFlg", reflectKColl.getDataValue("ClctCmpdIntFlg"));
		bodyKcoll.addDataField("DcnLoanIntPnyIntRate", reflectKColl.getDataValue("DcnLoanIntPnyIntRate"));
		bodyKcoll.addDataField("DcnLoanElyRepymtLwsFineAmt", reflectKColl.getDataValue("DcnLoanElyRepymtLwsFineAmt"));
		bodyKcoll.addDataField("GrcTrmDayNum", reflectKColl.getDataValue("GrcTrmDayNum"));
		bodyKcoll.addDataField("GrcTrmStopMoEndFlg", reflectKColl.getDataValue("GrcTrmStopMoEndFlg"));
		bodyKcoll.addDataField("AutoStlmntFlg", reflectKColl.getDataValue("AutoStlmntFlg"));
		bodyKcoll.addDataField("MtchAmtRepymtPlanModMd", reflectKColl.getDataValue("MtchAmtRepymtPlanModMd"));
		bodyKcoll.addDataField("IntClrgWithPnpFlg", reflectKColl.getDataValue("IntClrgWithPnpFlg"));
		bodyKcoll.addDataField("PpsDsc", reflectKColl.getDataValue("PpsDsc"));
		bodyKcoll.addDataField("GrtMd", reflectKColl.getDataValue("GrtMd"));
		bodyKcoll.addDataField("LoanTrm", reflectKColl.getDataValue("LoanTrm"));
		bodyKcoll.addDataField("StatClFlg", reflectKColl.getDataValue("StatClFlg"));
		bodyKcoll.addDataField("StatClFlg1", reflectKColl.getDataValue("StatClFlg1"));
		/* modify by liqh 2013/11/27  授权信息增加是否收取印花税，印花税收取方式，支付类型，客户类型， 委托人客户号      START*/
		bodyKcoll.addDataField("CtrNo", reflectKColl.getDataValue("CtrNo"));
		bodyKcoll.addDataField("SyndLoanMgtBnkNo", reflectKColl.getDataValue("SyndLoanMgtBnkNo"));
		bodyKcoll.addDataField("SyndLoanBnkNo", reflectKColl.getDataValue("SyndLoanBnkNo"));
		bodyKcoll.addDataField("WithBrwRepymtFlg", reflectKColl.getDataValue("WithBrwRepymtFlg"));
		bodyKcoll.addDataField("SbsdyIntEndDt", reflectKColl.getDataValue("SbsdyIntEndDt"));
		bodyKcoll.addDataField("IntAmt", reflectKColl.getDataValue("IntAmt"));
		bodyKcoll.addDataField("SubPrjNo", reflectKColl.getDataValue("SubPrjNo"));
		/* modify by liqh 2013/11/27  授权信息增加是否收取印花税，印花税收取方式，支付类型，客户类型， 委托人客户号      END*/
		bodyKcoll.addDataField("SmyDsc", reflectKColl.getDataValue("SmyDsc"));
		bodyKcoll.addDataField("LoanFiveLvlKnd", reflectKColl.getDataValue("LoanFiveLvlKnd"));
		
//		if(reflectKColl.containsKey("PRE_INT_FLAG")){
//			bodyKcoll.addDataField("PRE_INT_FLAG", TagUtil.getEMPField(reflectKColl.getDataValue("PRE_INT_FLAG"), FieldType.FIELD_STRING, 1, 0));//是否预收息
//		}
//		//add by wangj 2015-06-02 HS141110017_保理业务改造 begin
//		if(reflectKColl.containsKey("CREDITOR_NO")){//债务人编号
//			bodyKcoll.addDataField("CREDITOR_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CREDITOR_NO"), FieldType.FIELD_STRING, 50, 0));//是否预收息
//		}
//		if(reflectKColl.containsKey("CREDITOR_NAME")){//债务人名称
//			bodyKcoll.addDataField("CREDITOR_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("CREDITOR_NAME"), FieldType.FIELD_STRING, 150, 0));//是否预收息
//		}
//		if(reflectKColl.containsKey("OUGHT_ACCEPT_AMT")){//应收账款金额
//			bodyKcoll.addDataField("OUGHT_ACCEPT_AMT", TagUtil.getEMPField(reflectKColl.getDataValue("OUGHT_ACCEPT_AMT"), FieldType.FIELD_DOUBLE, 20, 2));//是否预收息
//		}
		//add by wangj 2015-06-02 HS141110017_保理业务改造 end
		////reqCD.addStruct("BODY", bodyCD);
		
		/** 账号信息 */
		IndexedCollection zhIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_ZH+"' ", connection);
		if(zhIColl != null && zhIColl.size() > 0){
			////Array zharray = new Array(); 
			IndexedCollection zharray=new IndexedCollection();
			zharray.setName("StlmntInfArry");
			for(int i=0;i<zhIColl.size();i++){
				////CompositeData zhCD = new CompositeData();
				KeyedCollection zhCD=new KeyedCollection();
				KeyedCollection zhKColl = (KeyedCollection)zhIColl.get(i);
				KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(zhKColl);
				zhCD.addDataField("StlmntBrBnkCd", reflectSubKColl.getDataValue("StlmntBrBnkCd"));
				zhCD.addDataField("StlmntBnkCstNo", reflectSubKColl.getDataValue("StlmntBnkCstNo"));
				zhCD.addDataField("StlmntAcctTp", reflectSubKColl.getDataValue("StlmntAcctTp"));
				zhCD.addDataField("StlmntMd", reflectSubKColl.getDataValue("StlmntMd"));
				zhCD.addDataField("ClctnPymtFlg", reflectSubKColl.getDataValue("ClctnPymtFlg"));
				zhCD.addDataField("AmtTp", reflectSubKColl.getDataValue("AmtTp"));
				zhCD.addDataField("StlmntAcctPrimKeyCd", reflectSubKColl.getDataValue("StlmntAcctPrimKeyCd"));
				zhCD.addDataField("StlmntAcctNo", reflectSubKColl.getDataValue("StlmntAcctNo"));
				zhCD.addDataField("StlmntAcctPdTp", reflectSubKColl.getDataValue("StlmntAcctPdTp"));
				zhCD.addDataField("StlmntAcctCcy", reflectSubKColl.getDataValue("StlmntAcctCcy"));
				zhCD.addDataField("StlmntAcctSeqNo", reflectSubKColl.getDataValue("StlmntAcctSeqNo"));
				zhCD.addDataField("StlmntCcy", reflectSubKColl.getDataValue("StlmntCcy"));
				zhCD.addDataField("StlmntAmt", reflectSubKColl.getDataValue("StlmntAmt"));
				zhCD.addDataField("StlmntExgRate", reflectSubKColl.getDataValue("StlmntExgRate"));
				zhCD.addDataField("StlmntExgMd", reflectSubKColl.getDataValue("StlmntExgMd"));
				zhCD.addDataField("AutoLockInd", reflectSubKColl.getDataValue("AutoLockInd"));
				zhCD.addDataField("PrtyLvl", reflectSubKColl.getDataValue("PrtyLvl"));
				zhCD.addDataField("StlmntRto", reflectSubKColl.getDataValue("StlmntRto"));
				zhCD.addDataField("StlmntNo", reflectSubKColl.getDataValue("StlmntNo"));
				zhCD.addDataField("IvsRto", reflectSubKColl.getDataValue("IvsRto"));
				/* modify by liqh 2013/11/27  账号信息增加银行名称      START*/
				zhCD.addDataField("StlmntAcctNm", reflectSubKColl.getDataValue("StlmntAcctNm"));
				/* modify by liqh 2013/11/27  账号信息增加银行名称      END*/
				zhCD.addDataField("ClctnBnkNo", reflectSubKColl.getDataValue("ClctnBnkNo"));
				zhCD.addDataField("ClctnBnkNm", reflectSubKColl.getDataValue("ClctnBnkNm"));
				zhCD.addDataField("BnkInnrBnkOthrFlg", reflectSubKColl.getDataValue("BnkInnrBnkOthrFlg"));
//				if(reflectSubKColl.containsKey("GUARANTEE_PER")){
//					zhCD.addDataField("GUARANTEE_PER", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_PER"), FieldType.FIELD_DOUBLE, 20, 7));
//				}
				zhCD.addDataField("FrzPayMd", reflectSubKColl.getDataValue("FrzPayMd"));
				zhCD.addDataField("FdcrPymtNo", reflectSubKColl.getDataValue("FdcrPymtNo"));
				zhCD.addDataField("TxnTp", reflectSubKColl.getDataValue("TxnTp"));
				zhCD.addDataField("DvdnPrftRto", reflectSubKColl.getDataValue("DvdnPrftRto"));
				zhCD.addDataField("OwnCorprtnFlg", reflectSubKColl.getDataValue("OwnCorprtnFlg"));
				////zharray.addStruct(zhCD);
				zharray.addDataElement(zhCD);
			}
			////bodyCD.addArray("ACCT_INFO_ARRAY", zharray);
			bodyKcoll.addDataElement(zharray);
		}
		
		/** 利息信息 */
		IndexedCollection lxIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_LX+"' ", connection);
		if(lxIColl != null && lxIColl.size() > 0){
			////Array zharray = new Array(); 
			IndexedCollection lxarray=new IndexedCollection();
			lxarray.setName("IntInfArry");
			for(int i=0;i<lxIColl.size();i++){
				////CompositeData zhCD = new CompositeData();
				KeyedCollection lxCD=new KeyedCollection();
				KeyedCollection lxKColl = (KeyedCollection)lxIColl.get(i);
				KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(lxKColl);
				lxCD.addDataField("IntCtgryTp", reflectSubKColl.getDataValue("IntCtgryTp"));
				lxCD.addDataField("IntRateTp", reflectSubKColl.getDataValue("IntRateTp"));
				lxCD.addDataField("BnkInnrIntRate", reflectSubKColl.getDataValue("BnkInnrIntRate"));
				lxCD.addDataField("FltIntRate", reflectSubKColl.getDataValue("FltIntRate"));
				lxCD.addDataField("IntRateFltPntNum", reflectSubKColl.getDataValue("IntRateFltPntNum"));
				lxCD.addDataField("IntRateFltPct", reflectSubKColl.getDataValue("IntRateFltPct"));
				lxCD.addDataField("SubsAcctLvlFltPntPct", reflectSubKColl.getDataValue("SubsAcctLvlFltPntPct"));
				lxCD.addDataField("SubsAcctLvlFltPct", reflectSubKColl.getDataValue("SubsAcctLvlFltPct"));
				lxCD.addDataField("SubsAcctLvlFixIntRate", reflectSubKColl.getDataValue("SubsAcctLvlFixIntRate"));
				lxCD.addDataField("ExecIntRate", reflectSubKColl.getDataValue("ExecIntRate"));
				lxCD.addDataField("IntStlmntFrqcy", reflectSubKColl.getDataValue("IntStlmntFrqcy"));
				lxCD.addDataField("NxtStlmntIntDt", reflectSubKColl.getDataValue("NxtStlmntIntDt"));
				lxCD.addDataField("IntStlmntDt", reflectSubKColl.getDataValue("IntStlmntDt"));
				lxCD.addDataField("AnulBaseDayNum", reflectSubKColl.getDataValue("AnulBaseDayNum"));
				lxCD.addDataField("MoBaseDaysNum", reflectSubKColl.getDataValue("MoBaseDaysNum"));
				lxCD.addDataField("LwsIntRate", reflectSubKColl.getDataValue("LwsIntRate"));
				lxCD.addDataField("HestIntRate", reflectSubKColl.getDataValue("HestIntRate"));
				lxCD.addDataField("IntRateEnblMd", reflectSubKColl.getDataValue("IntRateEnblMd"));
				lxCD.addDataField("CptztnFlg", reflectSubKColl.getDataValue("CptztnFlg"));
				/* modify by liqh 2013/11/27  账号信息增加银行名称      START*/
				lxCD.addDataField("PnyIntRateUseMd", reflectSubKColl.getDataValue("PnyIntRateUseMd"));
				/* modify by liqh 2013/11/27  账号信息增加银行名称      END*/
				lxCD.addDataField("IntClcBegDt", reflectSubKColl.getDataValue("IntClcBegDt"));
				lxCD.addDataField("IntClcDdlnDt", reflectSubKColl.getDataValue("IntClcDdlnDt"));
				lxCD.addDataField("IntRateModDt", reflectSubKColl.getDataValue("IntRateModDt"));
//				if(reflectSubKColl.containsKey("GUARANTEE_PER")){
//					zhCD.addDataField("GUARANTEE_PER", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_PER"), FieldType.FIELD_DOUBLE, 20, 7));
//				}
				lxCD.addDataField("IntRateModCcy", reflectSubKColl.getDataValue("IntRateModCcy"));
				lxCD.addDataField("IntRateModDay", reflectSubKColl.getDataValue("IntRateModDay"));
				lxCD.addDataField("ClcIntFlg", reflectSubKColl.getDataValue("ClcIntFlg"));
				lxCD.addDataField("IntRateTkEffMd", reflectSubKColl.getDataValue("IntRateTkEffMd"));
				lxCD.addDataField("FllwExecIntFltFlg", reflectSubKColl.getDataValue("FllwExecIntFltFlg"));
				////zharray.addStruct(zhCD);
				lxarray.addDataElement(lxCD);
			}
			////bodyCD.addArray("ACCT_INFO_ARRAY", zharray);
			bodyKcoll.addDataElement(lxarray);
		}
		
		/** 费用信息 */
		IndexedCollection feeIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_FY+"' ", connection);
		if(feeIColl != null && feeIColl.size() > 0){
			////Array feeArray = new Array(); 
			IndexedCollection feeArray=new IndexedCollection();
			feeArray.setName("SvcFeeInfArry");
			for(int i=0;i<feeIColl.size();i++){
				////CompositeData feeCD = new CompositeData();
				KeyedCollection feeCD=new KeyedCollection();
				KeyedCollection feeKColl = (KeyedCollection)feeIColl.get(i);
				KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(feeKColl);
				feeCD.addDataField("SvcFeeTp", reflectSubKColl.getDataValue("SvcFeeTp"));
				feeCD.addDataField("ChrgCcy", reflectSubKColl.getDataValue("ChrgCcy"));
				feeCD.addDataField("FeeAmt", reflectSubKColl.getDataValue("FeeAmt"));
				feeCD.addDataField("OrgnlSvcFeeAmt", reflectSubKColl.getDataValue("OrgnlSvcFeeAmt"));
				feeCD.addDataField("DcnAmt", reflectSubKColl.getDataValue("DcnAmt"));
				feeCD.addDataField("DcnTp", reflectSubKColl.getDataValue("DcnTp"));
				feeCD.addDataField("DcnRate", reflectSubKColl.getDataValue("DcnRate"));
				feeCD.addDataField("EODFlg", reflectSubKColl.getDataValue("EODFlg"));
				feeCD.addDataField("ClctMd", reflectSubKColl.getDataValue("ClctMd"));
				feeCD.addDataField("SvcFeeClctAcctAcctNo", reflectSubKColl.getDataValue("SvcFeeClctAcctAcctNo"));
				feeCD.addDataField("SvcFeeClctAcctCcy", reflectSubKColl.getDataValue("SvcFeeClctAcctCcy"));
				feeCD.addDataField("SvcFeeClctAcctPdTp", reflectSubKColl.getDataValue("SvcFeeClctAcctPdTp"));
				feeCD.addDataField("SvcFeeClctAcctSeqNo", reflectSubKColl.getDataValue("SvcFeeClctAcctSeqNo"));
				feeCD.addDataField("DrwMd", reflectSubKColl.getDataValue("DrwMd"));
				feeCD.addDataField("TxnPswdStrg", reflectSubKColl.getDataValue("TxnPswdStrg"));
				feeCD.addDataField("TaxRateTp", reflectSubKColl.getDataValue("TaxRateTp"));
				feeCD.addDataField("TaxRate", reflectSubKColl.getDataValue("TaxRate"));
				feeCD.addDataField("IntTaxAmt", reflectSubKColl.getDataValue("IntTaxAmt"));
				feeCD.addDataField("VchrTp", reflectSubKColl.getDataValue("VchrTp"));
				feeCD.addDataField("PfxDsc", reflectSubKColl.getDataValue("PfxDsc"));
				feeCD.addDataField("VchrBegNo", reflectSubKColl.getDataValue("VchrBegNo"));
				feeCD.addDataField("VchrEndNo", reflectSubKColl.getDataValue("VchrEndNo"));
				feeCD.addDataField("VchrNum", reflectSubKColl.getDataValue("VchrNum"));
				feeCD.addDataField("UnitPrcAmt", reflectSubKColl.getDataValue("UnitPrcAmt"));
				////feeArray.addStruct(feeCD);
				feeArray.addDataElement(feeCD);
			}
			////bodyCD.addArray("FEE_AUTH_INFO_ARRAY", feeArray);
			bodyKcoll.addDataElement(feeArray);
		}
		
		//组装自由还款信息（查询授权子表类型为01的信息）
//		IndexedCollection iqpFreedomPayInfoIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_HK+"' ", connection);
//		if(iqpFreedomPayInfoIColl != null && iqpFreedomPayInfoIColl.size() > 0){
//			////Array payArray = new Array(); 
//			IndexedCollection payArray=new IndexedCollection();
//			payArray.setName("REPAY_PLAN_ARRAY");
//			for(int i=0;i<iqpFreedomPayInfoIColl.size();i++){
//				////CompositeData payCD = new CompositeData();
//				KeyedCollection payCD=new KeyedCollection();
//				KeyedCollection iqpFreedomPayInfoKColl = (KeyedCollection)iqpFreedomPayInfoIColl.get(i);
//				KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(iqpFreedomPayInfoKColl);
//				payCD.addDataField("DUEBILL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 50, 0));
//				payCD.addDataField("PERIOD_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("PERIOD_NO"), FieldType.FIELD_STRING, 40, 0));
//				payCD.addDataField("REPAY_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("REPAY_DATE"), FieldType.FIELD_STRING, 10, 0));
//				payCD.addDataField("CORPUS", TagUtil.getEMPField(reflectSubKColl.getDataValue("CORPUS"), FieldType.FIELD_DOUBLE, 20, 2));
//				payCD.addDataField("INTEREST", TagUtil.getEMPField(reflectSubKColl.getDataValue("INTEREST"), FieldType.FIELD_DOUBLE, 20, 2));
//				////payArray.addStruct(payCD);
//				payArray.addDataElement(payCD);
//			}
//			////bodyCD.addArray("REPAY_PLAN_ARRAY", payArray);
//			bodyKcoll.addDataElement(payArray);
//		}
		container.put("body", bodyKcoll);
		container.put("head", headKcoll);
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		return container;
	}
	
	//XD150520037_信贷系统利率调整修改优化
	public void doSuccess(Context context, Connection connection) throws EMPException{
		
	}

	//@Override
	public CompositeData doExecute2(Context context, Connection connection)
			throws EMPException {
		// TODO Auto-generated method stub
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData();
	try{
		String serviceCode = (String)context.getDataValue("service_code");
		String senceCode = (String)context.getDataValue("sence_code");
		String tran_serno = (String)context.getDataValue("tran_serno");
		//获取dao
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		/** 通过交易码判断所需执行的交易，以及需要准备的交易数据 */
		KeyedCollection authKColl = dao.queryDetail(AUTHMODEL, tran_serno, connection);
		String serno = (String)authKColl.getDataValue("serno");
		ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
		
		/** 系统头 */
		//reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, TradeConstance.SERVICE_SCENE_DKFFSQ, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
		CompositeData headKcoll= new CompositeData();
		headKcoll.addField("SvcCd", TagUtil.getEMPField(serviceCode, FieldType.FIELD_STRING, 30, 0));
		headKcoll.addField("ScnCd", TagUtil.getEMPField(TradeConstance.SERVICE_SCENE_DKFFSQ, FieldType.FIELD_STRING, 2, 0));
		headKcoll.addField("TxnMd", TagUtil.getEMPField("ONLINE", FieldType.FIELD_STRING, 6, 0));
		headKcoll.addField("UsrLngKnd", TagUtil.getEMPField("UsrLngKnd", FieldType.FIELD_STRING, 20, 0));
		headKcoll.addField("jkType", TagUtil.getEMPField("cbs", FieldType.FIELD_STRING, 5, 0));
		reqCD.addStruct("SYS_HEAD", headKcoll);
		/** 应用头 */
		//王小虎注释//reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
				//王小虎注释//(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
		
		/** 封装报文体 */
		KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl);
		
		/** 核算与信贷业务品种映射 START */
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
		String lmPrdId = service.getPrdBasicCLPM2LM(reflectKColl.getDataValue("CONTRACT_NO").toString(), authKColl.getDataValue("prd_id").toString(), context, connection);
		/** 核算与信贷业务品种映射 END */
		
		////CompositeData bodyCD = new CompositeData();
		CompositeData bodyKcoll = new CompositeData();
		//KeyedCollection bodyKcoll=new KeyedCollection("body");
		
		bodyKcoll.addField("ExecTp", TagUtil.getEMPField(reflectKColl.getDataValue("ExecTp"), FieldType.FIELD_STRING, 10, 0));
		bodyKcoll.addField("AcctNoCrdNo", TagUtil.getEMPField(reflectKColl.getDataValue("AcctNoCrdNo"), FieldType.FIELD_STRING, 50, 0));
		bodyKcoll.addField("TxnAmt", TagUtil.getEMPField(reflectKColl.getDataValue("TxnAmt"), FieldType.FIELD_DOUBLE, 17, 2));
		bodyKcoll.addField("CstNo", TagUtil.getEMPField(reflectKColl.getDataValue("CstNo"),  FieldType.FIELD_STRING, 20, 0));
		bodyKcoll.addField("DblNo", TagUtil.getEMPField(reflectKColl.getDataValue("DblNo"),  FieldType.FIELD_STRING, 30, 0));
		bodyKcoll.addField("PdTp", TagUtil.getEMPField(reflectKColl.getDataValue("PdTp"),  FieldType.FIELD_STRING, 50, 0));
		bodyKcoll.addField("AcctBlngInstNo", TagUtil.getEMPField(reflectKColl.getDataValue("AcctBlngInstNo"), FieldType.FIELD_STRING, 20, 0));
		bodyKcoll.addField("EstblshdInstNo", TagUtil.getEMPField(reflectKColl.getDataValue("EstblshdInstNo"), FieldType.FIELD_STRING, 20, 0));
		bodyKcoll.addField("LoanCstNo", TagUtil.getEMPField(reflectKColl.getDataValue("LoanCstNo"), FieldType.FIELD_STRING, 20, 0));
		bodyKcoll.addField("CstMgrCd", TagUtil.getEMPField(reflectKColl.getDataValue("CstMgrCd"), FieldType.FIELD_STRING, 30, 0));
		bodyKcoll.addField("PrftCnrlCd", TagUtil.getEMPField(reflectKColl.getDataValue("PrftCnrlCd"), FieldType.FIELD_STRING, 12, 0));
		bodyKcoll.addField("AcctDsc", TagUtil.getEMPField(reflectKColl.getDataValue("AcctDsc"), FieldType.FIELD_STRING, 150, 0));
		bodyKcoll.addField("Trm", TagUtil.getEMPField(reflectKColl.getDataValue("Trm"), FieldType.FIELD_STRING, 5, 0));
		bodyKcoll.addField("TrmTp", TagUtil.getEMPField(reflectKColl.getDataValue("TrmTp"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("BegDt", TagUtil.getEMPField(reflectKColl.getDataValue("BegDt"),FieldType.FIELD_STRING,8,0));
		bodyKcoll.addField("ExprtnDt", TagUtil.getEMPField(reflectKColl.getDataValue("ExprtnDt"),FieldType.FIELD_STRING,8,0));
		bodyKcoll.addField("DstcWiOrWthtInd", TagUtil.getEMPField(reflectKColl.getDataValue("DstcWiOrWthtInd"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("BlngKnd", TagUtil.getEMPField(reflectKColl.getDataValue("BlngKnd"),FieldType.FIELD_STRING,2,0));
		bodyKcoll.addField("DbtCrdtFlg", TagUtil.getEMPField(reflectKColl.getDataValue("DbtCrdtFlg"),FieldType.FIELD_STRING,2,0));
		bodyKcoll.addField("AcctPpsCd", TagUtil.getEMPField(reflectKColl.getDataValue("AcctPpsCd"),FieldType.FIELD_STRING,6,0));
		bodyKcoll.addField("PlanMd", TagUtil.getEMPField(reflectKColl.getDataValue("PlanMd"),FieldType.FIELD_STRING,3,0));
		bodyKcoll.addField("IntStlmntFrqcy", TagUtil.getEMPField(reflectKColl.getDataValue("IntStlmntFrqcy"),FieldType.FIELD_STRING,2,0));
		bodyKcoll.addField("NxtStlmntIntDt", TagUtil.getEMPField(reflectKColl.getDataValue("NxtStlmntIntDt"),FieldType.FIELD_STRING,8,0));
		bodyKcoll.addField("IntStlmntDt", TagUtil.getEMPField(reflectKColl.getDataValue("IntStlmntDt"),FieldType.FIELD_STRING,2,0));
		bodyKcoll.addField("BlonLoanClcTrmTms", TagUtil.getEMPField(reflectKColl.getDataValue("BlonLoanClcTrmTms"),FieldType.FIELD_STRING,5,0));
		bodyKcoll.addField("FrstStgTrmNum", TagUtil.getEMPField(reflectKColl.getDataValue("FrstStgTrmNum"),FieldType.FIELD_STRING,5,0));
		bodyKcoll.addField("AcrItrvTrmNum", TagUtil.getEMPField(reflectKColl.getDataValue("AcrItrvTrmNum"),FieldType.FIELD_STRING,5,0));
		bodyKcoll.addField("AcrVal", TagUtil.getEMPField(reflectKColl.getDataValue("AcrVal"),FieldType.FIELD_DOUBLE,17,2));
		bodyKcoll.addField("AcrRto", TagUtil.getEMPField(reflectKColl.getDataValue("AcrRto"),FieldType.FIELD_DOUBLE,5,2));
		bodyKcoll.addField("IntRateEnblMd", TagUtil.getEMPField(reflectKColl.getDataValue("IntRateEnblMd"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("IntRateModDt", TagUtil.getEMPField(reflectKColl.getDataValue("IntRateModDt"),FieldType.FIELD_STRING,8,0));
		bodyKcoll.addField("IntRateModCcy", TagUtil.getEMPField(reflectKColl.getDataValue("IntRateModCcy"),FieldType.FIELD_STRING,2,0));
		bodyKcoll.addField("IntRateModDay", TagUtil.getEMPField(reflectKColl.getDataValue("IntRateModDay"),FieldType.FIELD_STRING,2,0));
		bodyKcoll.addField("ClctPnyIntCmpdIntFlg", TagUtil.getEMPField(reflectKColl.getDataValue("ClctPnyIntCmpdIntFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("ClctSubCmpdIntFlg", TagUtil.getEMPField(reflectKColl.getDataValue("ClctSubCmpdIntFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("DrtnWthdFlg", TagUtil.getEMPField(reflectKColl.getDataValue("DrtnWthdFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("PrjNo", TagUtil.getEMPField(reflectKColl.getDataValue("PrjNo"),FieldType.FIELD_STRING,14,0));
		bodyKcoll.addField("Ccy", TagUtil.getEMPField(reflectKColl.getDataValue("Ccy"),FieldType.FIELD_STRING,3,0));
		bodyKcoll.addField("CtrOrgnlAmt", TagUtil.getEMPField(reflectKColl.getDataValue("CtrOrgnlAmt"),FieldType.FIELD_DOUBLE,17,2));
		bodyKcoll.addField("FndSrcCtyCd", TagUtil.getEMPField(reflectKColl.getDataValue("FndSrcCtyCd"),FieldType.FIELD_STRING,3,0));
		bodyKcoll.addField("FndSrcProvCd", TagUtil.getEMPField(reflectKColl.getDataValue("FndSrcProvCd"),FieldType.FIELD_STRING,10,0));
		bodyKcoll.addField("DstrbtDdlnDt", TagUtil.getEMPField(reflectKColl.getDataValue("DstrbtDdlnDt"),FieldType.FIELD_STRING,8,0));
		bodyKcoll.addField("ClctPnyIntFlg", TagUtil.getEMPField(reflectKColl.getDataValue("ClctPnyIntFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("ClctCmpdIntFlg", TagUtil.getEMPField(reflectKColl.getDataValue("ClctCmpdIntFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("DcnLoanIntPnyIntRate", TagUtil.getEMPField(reflectKColl.getDataValue("DcnLoanIntPnyIntRate"),FieldType.FIELD_DOUBLE,15,8));
		bodyKcoll.addField("DcnLoanElyRepymtLwsFineAmt", TagUtil.getEMPField(reflectKColl.getDataValue("DcnLoanElyRepymtLwsFineAmt"),FieldType.FIELD_DOUBLE,17,2));
		bodyKcoll.addField("GrcTrmDayNum", TagUtil.getEMPField(reflectKColl.getDataValue("GrcTrmDayNum"),FieldType.FIELD_STRING,5,0));
		bodyKcoll.addField("GrcTrmStopMoEndFlg", TagUtil.getEMPField(reflectKColl.getDataValue("GrcTrmStopMoEndFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("AutoStlmntFlg", TagUtil.getEMPField(reflectKColl.getDataValue("AutoStlmntFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("MtchAmtRepymtPlanModMd", TagUtil.getEMPField(reflectKColl.getDataValue("MtchAmtRepymtPlanModMd"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("IntClrgWithPnpFlg", TagUtil.getEMPField(reflectKColl.getDataValue("IntClrgWithPnpFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("PpsDsc", TagUtil.getEMPField(reflectKColl.getDataValue("PpsDsc"),FieldType.FIELD_STRING,6,0));
		bodyKcoll.addField("GrtMd", TagUtil.getEMPField(reflectKColl.getDataValue("GrtMd"),FieldType.FIELD_STRING,6,0));
		bodyKcoll.addField("LoanTrm", TagUtil.getEMPField(reflectKColl.getDataValue("LoanTrm"),FieldType.FIELD_STRING,6,0));
		bodyKcoll.addField("StatClFlg", TagUtil.getEMPField(reflectKColl.getDataValue("StatClFlg"),FieldType.FIELD_STRING,6,0));
		bodyKcoll.addField("StatClFlg1", TagUtil.getEMPField(reflectKColl.getDataValue("StatClFlg1"),FieldType.FIELD_STRING,6,0));
		/* modify by liqh 2013/11/27  授权信息增加是否收取印花税，印花税收取方式，支付类型，客户类型， 委托人客户号      START*/
		bodyKcoll.addField("CtrNo", TagUtil.getEMPField(reflectKColl.getDataValue("CtrNo"),FieldType.FIELD_STRING,50,0));
		bodyKcoll.addField("SyndLoanMgtBnkNo", TagUtil.getEMPField(reflectKColl.getDataValue("SyndLoanMgtBnkNo"),FieldType.FIELD_STRING,20,0));
		bodyKcoll.addField("SyndLoanBnkNo", TagUtil.getEMPField(reflectKColl.getDataValue("SyndLoanBnkNo"),FieldType.FIELD_STRING,20,0));
		bodyKcoll.addField("WithBrwRepymtFlg", TagUtil.getEMPField(reflectKColl.getDataValue("WithBrwRepymtFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("SbsdyIntEndDt", TagUtil.getEMPField(reflectKColl.getDataValue("SbsdyIntEndDt"),FieldType.FIELD_STRING,8,0));
		bodyKcoll.addField("IntAmt", TagUtil.getEMPField(reflectKColl.getDataValue("IntAmt"),FieldType.FIELD_DOUBLE,17,2));
		bodyKcoll.addField("SubPrjNo", TagUtil.getEMPField(reflectKColl.getDataValue("SubPrjNo"),FieldType.FIELD_STRING,30,0));
		/* modify by liqh 2013/11/27  授权信息增加是否收取印花税，印花税收取方式，支付类型，客户类型， 委托人客户号      END*/
		bodyKcoll.addField("SmyDsc", TagUtil.getEMPField(reflectKColl.getDataValue("SmyDsc"),FieldType.FIELD_STRING,300,0));
		bodyKcoll.addField("LoanFiveLvlKnd", TagUtil.getEMPField(reflectKColl.getDataValue("LoanFiveLvlKnd"),FieldType.FIELD_STRING,6,0));
		
//		if(reflectKColl.containsKey("PRE_INT_FLAG")){
//			bodyKcoll.addDataField("PRE_INT_FLAG", TagUtil.getEMPField(reflectKColl.getDataValue("PRE_INT_FLAG"), FieldType.FIELD_STRING, 1, 0));//是否预收息
//		}
//		//add by wangj 2015-06-02 HS141110017_保理业务改造 begin
//		if(reflectKColl.containsKey("CREDITOR_NO")){//债务人编号
//			bodyKcoll.addDataField("CREDITOR_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CREDITOR_NO"), FieldType.FIELD_STRING, 50, 0));//是否预收息
//		}
//		if(reflectKColl.containsKey("CREDITOR_NAME")){//债务人名称
//			bodyKcoll.addDataField("CREDITOR_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("CREDITOR_NAME"), FieldType.FIELD_STRING, 150, 0));//是否预收息
//		}
//		if(reflectKColl.containsKey("OUGHT_ACCEPT_AMT")){//应收账款金额
//			bodyKcoll.addDataField("OUGHT_ACCEPT_AMT", TagUtil.getEMPField(reflectKColl.getDataValue("OUGHT_ACCEPT_AMT"), FieldType.FIELD_DOUBLE, 20, 2));//是否预收息
//		}
		//add by wangj 2015-06-02 HS141110017_保理业务改造 end
		////reqCD.addStruct("BODY", bodyCD);
		
		/** 账号信息 */
		IndexedCollection zhIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_ZH+"' ", connection);
		if(zhIColl != null && zhIColl.size() > 0){
			Array zharray = new Array(); 
			for(int i=0;i<zhIColl.size();i++){
				CompositeData zhCD = new CompositeData();
				KeyedCollection zhKColl = (KeyedCollection)zhIColl.get(i);
				KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(zhKColl);
				zhCD.addField("StlmntBrBnkCd", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntBrBnkCd"),FieldType.FIELD_STRING,20,0));
				zhCD.addField("StlmntBnkCstNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntBnkCstNo"),FieldType.FIELD_STRING,20,0));
				zhCD.addField("StlmntAcctTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntAcctTp"),FieldType.FIELD_STRING,3,0));
				zhCD.addField("StlmntMd", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntMd"),FieldType.FIELD_STRING,3,0));
				zhCD.addField("ClctnPymtFlg", TagUtil.getEMPField(reflectSubKColl.getDataValue("ClctnPymtFlg"),FieldType.FIELD_STRING,3,0));
				zhCD.addField("AmtTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("AmtTp"),FieldType.FIELD_STRING,10,0));
				zhCD.addField("StlmntAcctPrimKeyCd", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntAcctPrimKeyCd"),FieldType.FIELD_LONG,15,0));
				zhCD.addField("StlmntAcctNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntAcctNo"),FieldType.FIELD_STRING,50,0));
				zhCD.addField("StlmntAcctPdTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntAcctPdTp"),FieldType.FIELD_STRING,50,0));
				zhCD.addField("StlmntAcctCcy", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntAcctCcy"),FieldType.FIELD_STRING,3,0));
				zhCD.addField("StlmntAcctSeqNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntAcctSeqNo"),FieldType.FIELD_STRING,8,0));
				zhCD.addField("StlmntCcy", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntCcy"),FieldType.FIELD_STRING,3,0));
				zhCD.addField("StlmntAmt", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntAmt"),FieldType.FIELD_DOUBLE,17,2));
				zhCD.addField("StlmntExgRate", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntExgRate"),FieldType.FIELD_DOUBLE,15,8));
				zhCD.addField("StlmntExgMd", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntExgMd"),FieldType.FIELD_STRING,1,0));
				zhCD.addField("AutoLockInd", TagUtil.getEMPField(reflectSubKColl.getDataValue("AutoLockInd"),FieldType.FIELD_STRING,1,0));
				zhCD.addField("PrtyLvl", TagUtil.getEMPField(reflectSubKColl.getDataValue("PrtyLvl"),FieldType.FIELD_STRING,5,0));
				zhCD.addField("StlmntRto", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntRto"),FieldType.FIELD_DOUBLE,5,2));
				zhCD.addField("StlmntNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntNo"),FieldType.FIELD_STRING,30,0));
				zhCD.addField("IvsRto", TagUtil.getEMPField(reflectSubKColl.getDataValue("IvsRto"),FieldType.FIELD_STRING,17,0));
				/* modify by liqh 2013/11/27  账号信息增加银行名称      START*/
				zhCD.addField("StlmntAcctNm", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntAcctNm"),FieldType.FIELD_STRING,200,0));
				/* modify by liqh 2013/11/27  账号信息增加银行名称      END*/
				zhCD.addField("ClctnBnkNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("ClctnBnkNo"),FieldType.FIELD_STRING,12,0));
				zhCD.addField("ClctnBnkNm", TagUtil.getEMPField(reflectSubKColl.getDataValue("ClctnBnkNm"),FieldType.FIELD_STRING,60,0));
				zhCD.addField("BnkInnrBnkOthrFlg", TagUtil.getEMPField(reflectSubKColl.getDataValue("BnkInnrBnkOthrFlg"),FieldType.FIELD_STRING,1,0));
//				if(reflectSubKColl.containsKey("GUARANTEE_PER")){
//					zhCD.addDataField("GUARANTEE_PER", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_PER"), FieldType.FIELD_DOUBLE, 20, 7));
//				}
				zhCD.addField("FrzPayMd", TagUtil.getEMPField(reflectSubKColl.getDataValue("FrzPayMd"),FieldType.FIELD_STRING,1,0));
				zhCD.addField("FdcrPymtNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("FdcrPymtNo"),FieldType.FIELD_STRING,50,0));
				zhCD.addField("TxnTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("TxnTp"),FieldType.FIELD_STRING,4,0));
				zhCD.addField("DvdnPrftRto", TagUtil.getEMPField(reflectSubKColl.getDataValue("DvdnPrftRto"),FieldType.FIELD_STRING,17,0));
				zhCD.addField("OwnCorprtnFlg", TagUtil.getEMPField(reflectSubKColl.getDataValue("OwnCorprtnFlg"),FieldType.FIELD_STRING,17,0));
				zharray.addStruct(zhCD);
			}
			bodyKcoll.addArray("StlmntInfArry", zharray);
		}
		
		/** 利息信息 */
		IndexedCollection lxIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_LX+"' ", connection);
		if(lxIColl != null && lxIColl.size() > 0){
			Array lxarray = new Array(); 
			for(int i=0;i<lxIColl.size();i++){
				CompositeData lxCD = new CompositeData();
				KeyedCollection lxKColl = (KeyedCollection)lxIColl.get(i);
				KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(lxKColl);
				lxCD.addField("IntCtgryTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntCtgryTp"),FieldType.FIELD_STRING,10,0));
				lxCD.addField("IntRateTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntRateTp"),FieldType.FIELD_STRING,3,0));
				lxCD.addField("BnkInnrIntRate", TagUtil.getEMPField(reflectSubKColl.getDataValue("BnkInnrIntRate"),FieldType.FIELD_DOUBLE,15,8));
				lxCD.addField("FltIntRate", TagUtil.getEMPField(reflectSubKColl.getDataValue("FltIntRate"),FieldType.FIELD_DOUBLE,15,8));
				lxCD.addField("IntRateFltPntNum", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntRateFltPntNum"),FieldType.FIELD_DOUBLE,15,8));
				lxCD.addField("IntRateFltPct", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntRateFltPct"),FieldType.FIELD_DOUBLE,15,2));
				lxCD.addField("SubsAcctLvlFltPntPct", TagUtil.getEMPField(reflectSubKColl.getDataValue("SubsAcctLvlFltPntPct"),FieldType.FIELD_DOUBLE,15,8));
				lxCD.addField("SubsAcctLvlFltPct", TagUtil.getEMPField(reflectSubKColl.getDataValue("SubsAcctLvlFltPct"),FieldType.FIELD_DOUBLE,15,2));
				lxCD.addField("SubsAcctLvlFixIntRate", TagUtil.getEMPField(reflectSubKColl.getDataValue("SubsAcctLvlFixIntRate"),FieldType.FIELD_DOUBLE,15,8));
				lxCD.addField("ExecIntRate", TagUtil.getEMPField(reflectSubKColl.getDataValue("ExecIntRate"),FieldType.FIELD_DOUBLE,15,8));
				lxCD.addField("IntStlmntFrqcy", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntStlmntFrqcy"),FieldType.FIELD_STRING,2,0));
				lxCD.addField("NxtStlmntIntDt", TagUtil.getEMPField(reflectSubKColl.getDataValue("NxtStlmntIntDt"),FieldType.FIELD_STRING,8,0));
				lxCD.addField("IntStlmntDt", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntStlmntDt"),FieldType.FIELD_STRING,2,0));
				lxCD.addField("AnulBaseDayNum", TagUtil.getEMPField(reflectSubKColl.getDataValue("AnulBaseDayNum"),FieldType.FIELD_STRING,3,0));
				lxCD.addField("MoBaseDaysNum", TagUtil.getEMPField(reflectSubKColl.getDataValue("MoBaseDaysNum"),FieldType.FIELD_STRING,3,0));
				lxCD.addField("LwsIntRate", TagUtil.getEMPField(reflectSubKColl.getDataValue("LwsIntRate"),FieldType.FIELD_DOUBLE,15,8));
				lxCD.addField("HestIntRate", TagUtil.getEMPField(reflectSubKColl.getDataValue("HestIntRate"),FieldType.FIELD_DOUBLE,15,8));
				lxCD.addField("IntRateEnblMd", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntRateEnblMd"),FieldType.FIELD_STRING,1,0));
				lxCD.addField("CptztnFlg", TagUtil.getEMPField(reflectSubKColl.getDataValue("CptztnFlg"),FieldType.FIELD_STRING,1,0));
				/* modify by liqh 2013/11/27  账号信息增加银行名称      START*/
				lxCD.addField("PnyIntRateUseMd", TagUtil.getEMPField(reflectSubKColl.getDataValue("PnyIntRateUseMd"),FieldType.FIELD_STRING,1,0));
				/* modify by liqh 2013/11/27  账号信息增加银行名称      END*/
				lxCD.addField("IntClcBegDt", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntClcBegDt"),FieldType.FIELD_STRING,8,0));
				lxCD.addField("IntClcDdlnDt", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntClcDdlnDt"),FieldType.FIELD_STRING,8,0));
				lxCD.addField("IntRateModDt", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntRateModDt"),FieldType.FIELD_STRING,8,0));
//				if(reflectSubKColl.containsKey("GUARANTEE_PER")){
//					zhCD.addDataField("GUARANTEE_PER", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_PER"), FieldType.FIELD_DOUBLE, 20, 7));
//				}
				lxCD.addField("IntRateModCcy", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntRateModCcy"),FieldType.FIELD_STRING,2,0));
				lxCD.addField("IntRateModDay", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntRateModDay"),FieldType.FIELD_STRING,2,0));
				lxCD.addField("ClcIntFlg", TagUtil.getEMPField(reflectSubKColl.getDataValue("ClcIntFlg"),FieldType.FIELD_STRING,1,0));
				lxCD.addField("IntRateTkEffMd", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntRateTkEffMd"),FieldType.FIELD_STRING,1,0));
				lxCD.addField("FllwExecIntFltFlg", TagUtil.getEMPField(reflectSubKColl.getDataValue("FllwExecIntFltFlg"),FieldType.FIELD_STRING,1,0));
				lxarray.addStruct(lxCD);
			}
			bodyKcoll.addArray("IntInfArry", lxarray);
		}
		
		/** 费用信息 */
		IndexedCollection feeIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_FY+"' ", connection);
		if(feeIColl != null && feeIColl.size() > 0){
			Array feeArray = new Array();
			for(int i=0;i<feeIColl.size();i++){
				CompositeData feeCD = new CompositeData();
				KeyedCollection feeKColl = (KeyedCollection)feeIColl.get(i);
				KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(feeKColl);
				feeCD.addField("SvcFeeTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("SvcFeeTp"),FieldType.FIELD_STRING,8,0));
				feeCD.addField("ChrgCcy", TagUtil.getEMPField(reflectSubKColl.getDataValue("ChrgCcy"),FieldType.FIELD_STRING,3,0));
				feeCD.addField("FeeAmt", TagUtil.getEMPField(reflectSubKColl.getDataValue("FeeAmt"),FieldType.FIELD_DOUBLE,17,2));
				feeCD.addField("OrgnlSvcFeeAmt", TagUtil.getEMPField(reflectSubKColl.getDataValue("OrgnlSvcFeeAmt"),FieldType.FIELD_DOUBLE,17,2));
				feeCD.addField("DcnAmt", TagUtil.getEMPField(reflectSubKColl.getDataValue("DcnAmt"),FieldType.FIELD_DOUBLE,17,2));
				feeCD.addField("DcnTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("DcnTp"),FieldType.FIELD_STRING,20,0));
				feeCD.addField("DcnRate", TagUtil.getEMPField(reflectSubKColl.getDataValue("DcnRate"),FieldType.FIELD_DOUBLE,15,8));
				feeCD.addField("EODFlg", TagUtil.getEMPField(reflectSubKColl.getDataValue("EODFlg"),FieldType.FIELD_STRING,1,0));
				feeCD.addField("ClctMd", TagUtil.getEMPField(reflectSubKColl.getDataValue("ClctMd"),FieldType.FIELD_STRING,2,0));
				feeCD.addField("SvcFeeClctAcctAcctNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("SvcFeeClctAcctAcctNo"),FieldType.FIELD_STRING,50,0));
				feeCD.addField("SvcFeeClctAcctCcy", TagUtil.getEMPField(reflectSubKColl.getDataValue("SvcFeeClctAcctCcy"),FieldType.FIELD_STRING,3,0));
				feeCD.addField("SvcFeeClctAcctPdTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("SvcFeeClctAcctPdTp"),FieldType.FIELD_STRING,50,0));
				feeCD.addField("SvcFeeClctAcctSeqNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("SvcFeeClctAcctSeqNo"),FieldType.FIELD_STRING,8,0));
				feeCD.addField("DrwMd", TagUtil.getEMPField(reflectSubKColl.getDataValue("DrwMd"),FieldType.FIELD_STRING,1,0));
				feeCD.addField("TxnPswdStrg", TagUtil.getEMPField(reflectSubKColl.getDataValue("TxnPswdStrg"),FieldType.FIELD_STRING,50,0));
				feeCD.addField("TaxRateTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("TaxRateTp"),FieldType.FIELD_STRING,3,0));
				feeCD.addField("TaxRate", TagUtil.getEMPField(reflectSubKColl.getDataValue("TaxRate"),FieldType.FIELD_DOUBLE,15,8));
				feeCD.addField("IntTaxAmt", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntTaxAmt"),FieldType.FIELD_DOUBLE,17,2));
				feeCD.addField("VchrTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("VchrTp"),FieldType.FIELD_STRING,10,0));
				feeCD.addField("PfxDsc", TagUtil.getEMPField(reflectSubKColl.getDataValue("PfxDsc"),FieldType.FIELD_STRING,10,0));
				feeCD.addField("VchrBegNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("VchrBegNo"),FieldType.FIELD_STRING,50,0));
				feeCD.addField("VchrEndNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("VchrEndNo"),FieldType.FIELD_STRING,50,0));
				feeCD.addField("VchrNum", TagUtil.getEMPField(reflectSubKColl.getDataValue("VchrNum"),FieldType.FIELD_STRING,10,0));
				feeCD.addField("UnitPrcAmt", TagUtil.getEMPField(reflectSubKColl.getDataValue("UnitPrcAmt"),FieldType.FIELD_DOUBLE,17,2));
				feeArray.addStruct(feeCD);
			}
			bodyKcoll.addArray("SvcFeeInfArry", feeArray);
		}
		
		//组装自由还款信息（查询授权子表类型为01的信息）
//		IndexedCollection iqpFreedomPayInfoIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_HK+"' ", connection);
//		if(iqpFreedomPayInfoIColl != null && iqpFreedomPayInfoIColl.size() > 0){
//			////Array payArray = new Array(); 
//			IndexedCollection payArray=new IndexedCollection();
//			payArray.setName("REPAY_PLAN_ARRAY");
//			for(int i=0;i<iqpFreedomPayInfoIColl.size();i++){
//				////CompositeData payCD = new CompositeData();
//				KeyedCollection payCD=new KeyedCollection();
//				KeyedCollection iqpFreedomPayInfoKColl = (KeyedCollection)iqpFreedomPayInfoIColl.get(i);
//				KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(iqpFreedomPayInfoKColl);
//				payCD.addDataField("DUEBILL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 50, 0));
//				payCD.addDataField("PERIOD_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("PERIOD_NO"), FieldType.FIELD_STRING, 40, 0));
//				payCD.addDataField("REPAY_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("REPAY_DATE"), FieldType.FIELD_STRING, 10, 0));
//				payCD.addDataField("CORPUS", TagUtil.getEMPField(reflectSubKColl.getDataValue("CORPUS"), FieldType.FIELD_DOUBLE, 20, 2));
//				payCD.addDataField("INTEREST", TagUtil.getEMPField(reflectSubKColl.getDataValue("INTEREST"), FieldType.FIELD_DOUBLE, 20, 2));
//				////payArray.addStruct(payCD);
//				payArray.addDataElement(payCD);
//			}
//			////bodyCD.addArray("REPAY_PLAN_ARRAY", payArray);
//			bodyKcoll.addDataElement(payArray);
//		}
		reqCD.addStruct("BODY", bodyKcoll);
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		return reqCD;
	}
	
	
	@Override
	public CompositeData doExecute(Context context, Connection connection)
			throws EMPException {
		// TODO Auto-generated method stub
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData();
	try{
		String serviceCode = (String)context.getDataValue("service_code");
		String senceCode = (String)context.getDataValue("sence_code");
		String tran_serno = (String)context.getDataValue("tran_serno");
		//获取dao
		TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
		/** 通过交易码判断所需执行的交易，以及需要准备的交易数据 */
		KeyedCollection authKColl = dao.queryDetail(AUTHMODEL, tran_serno, connection);
		String serno = (String)authKColl.getDataValue("serno");
		ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
		
		/** 系统头 */
		//reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, TradeConstance.SERVICE_SCENE_DKFFSQ, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
		CompositeData headKcoll= new CompositeData();
		headKcoll.addField("SvcCd", TagUtil.getEMPField(serviceCode, FieldType.FIELD_STRING, 30, 0));
		headKcoll.addField("ScnCd", TagUtil.getEMPField(TradeConstance.SERVICE_SCENE_DKFFSQ, FieldType.FIELD_STRING, 2, 0));
		headKcoll.addField("TxnMd", TagUtil.getEMPField("ONLINE", FieldType.FIELD_STRING, 6, 0));
		headKcoll.addField("UsrLngKnd", TagUtil.getEMPField("UsrLngKnd", FieldType.FIELD_STRING, 20, 0));
		headKcoll.addField("jkType", TagUtil.getEMPField("cbs", FieldType.FIELD_STRING, 5, 0));
		reqCD.addStruct("SYS_HEAD", headKcoll);
		/** 应用头 */
		//王小虎注释//reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
				//王小虎注释//(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
		
		/** 封装报文体 */
		KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl);
		
		/** 核算与信贷业务品种映射 START */
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
		String lmPrdId = service.getPrdBasicCLPM2LM(reflectKColl.getDataValue("CtrNo").toString(), authKColl.getDataValue("prd_id").toString(), context, connection);
		/** 核算与信贷业务品种映射 END */
		
		////CompositeData bodyCD = new CompositeData();
		CompositeData bodyKcoll = new CompositeData();
		//KeyedCollection bodyKcoll=new KeyedCollection("body");
		
		bodyKcoll.addField("CstNo", TagUtil.getEMPField(reflectKColl.getDataValue("CstNo"), FieldType.FIELD_STRING, 20, 0));
		bodyKcoll.addField("PdTp", TagUtil.getEMPField(reflectKColl.getDataValue("PdTp"), FieldType.FIELD_STRING, 50, 0));
		bodyKcoll.addField("AcctBlngInstNo", TagUtil.getEMPField(authKColl.getDataValue("in_acct_br_id"), FieldType.FIELD_STRING, 20, 0));
		bodyKcoll.addField("LoanOprtInstCd", TagUtil.getEMPField(authKColl.getDataValue("in_acct_br_id"),  FieldType.FIELD_STRING, 20, 0));
		bodyKcoll.addField("LoanCstNo", TagUtil.getEMPField(reflectKColl.getDataValue("LoanCstNo"),  FieldType.FIELD_STRING, 20, 0));
		bodyKcoll.addField("CstMgrCd", TagUtil.getEMPField(reflectKColl.getDataValue("CstMgrCd"),  FieldType.FIELD_STRING, 30, 0));
		bodyKcoll.addField("PrftCnrlCd", TagUtil.getEMPField(reflectKColl.getDataValue("PrftCnrlCd"), FieldType.FIELD_STRING, 12, 0));
		bodyKcoll.addField("AcctDsc", TagUtil.getEMPField(reflectKColl.getDataValue("AcctDsc"), FieldType.FIELD_STRING, 150, 0));
		bodyKcoll.addField("LoanCstNo", TagUtil.getEMPField(reflectKColl.getDataValue("LoanCstNo"), FieldType.FIELD_STRING, 20, 0));
		bodyKcoll.addField("Trm", TagUtil.getEMPField(reflectKColl.getDataValue("Trm"), FieldType.FIELD_STRING, 5, 0));
		bodyKcoll.addField("TrmTp", TagUtil.getEMPField(reflectKColl.getDataValue("TrmTp"), FieldType.FIELD_STRING, 1, 0));
		bodyKcoll.addField("BegDt", TagUtil.getEMPField(reflectKColl.getDataValue("BegDt"), FieldType.FIELD_STRING, 8, 0));
		bodyKcoll.addField("ExprtnDt", TagUtil.getEMPField(reflectKColl.getDataValue("ExprtnDt"), FieldType.FIELD_STRING, 8, 0));
		bodyKcoll.addField("DstcWiOrWthtInd", TagUtil.getEMPField(reflectKColl.getDataValue("DstcWiOrWthtInd"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("BlngKnd", TagUtil.getEMPField(reflectKColl.getDataValue("BlngKnd"),FieldType.FIELD_STRING,2,0));
		bodyKcoll.addField("DbtCrdtFlg", TagUtil.getEMPField(reflectKColl.getDataValue("DbtCrdtFlg"),FieldType.FIELD_STRING,2,0));
		bodyKcoll.addField("PlanMd", TagUtil.getEMPField(reflectKColl.getDataValue("PlanMd"),FieldType.FIELD_STRING,3,0));
		bodyKcoll.addField("IntStlmntFrqcy", TagUtil.getEMPField(reflectKColl.getDataValue("IntStlmntFrqcy"),FieldType.FIELD_STRING,2,0));
		bodyKcoll.addField("NxtStlmntIntDt", TagUtil.getEMPField(reflectKColl.getDataValue("NxtStlmntIntDt"),FieldType.FIELD_STRING,8,0));
		bodyKcoll.addField("IntStlmntDt", TagUtil.getEMPField(reflectKColl.getDataValue("IntStlmntDt"),FieldType.FIELD_STRING,2,0));
		bodyKcoll.addField("BlonLoanClcTrmTms", TagUtil.getEMPField(reflectKColl.getDataValue("BlonLoanClcTrmTms"),FieldType.FIELD_STRING,5,0));
		bodyKcoll.addField("FrstStgTrmNum", TagUtil.getEMPField(reflectKColl.getDataValue("FrstStgTrmNum"),FieldType.FIELD_STRING,5,0));
		bodyKcoll.addField("AcrItrvTrmNum", TagUtil.getEMPField(reflectKColl.getDataValue("AcrItrvTrmNum"),FieldType.FIELD_STRING,5,0));
		bodyKcoll.addField("AcrVal", TagUtil.getEMPField(reflectKColl.getDataValue("AcrVal"),FieldType.FIELD_DOUBLE,17,2));
		bodyKcoll.addField("AcrRto", TagUtil.getEMPField(reflectKColl.getDataValue("AcrRto"),FieldType.FIELD_DOUBLE,15,8));
		bodyKcoll.addField("ClctPnyIntCmpdIntFlg", TagUtil.getEMPField(reflectKColl.getDataValue("ClctPnyIntCmpdIntFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("ClctSubCmpdIntFlg", TagUtil.getEMPField(reflectKColl.getDataValue("ClctSubCmpdIntFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("DrtnWthdFlg", TagUtil.getEMPField(reflectKColl.getDataValue("DrtnWthdFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("PrjNo", TagUtil.getEMPField(reflectKColl.getDataValue("PrjNo"),FieldType.FIELD_STRING,14,0));
		bodyKcoll.addField("Ccy", TagUtil.getEMPField(reflectKColl.getDataValue("Ccy"),FieldType.FIELD_STRING,3,0));
		bodyKcoll.addField("CtrOrgnlAmt", TagUtil.getEMPField(reflectKColl.getDataValue("CtrOrgnlAmt"),FieldType.FIELD_DOUBLE,17,2));
		bodyKcoll.addField("FndSrcCtyCd", TagUtil.getEMPField(reflectKColl.getDataValue("FndSrcCtyCd"),FieldType.FIELD_STRING,3,0));
		bodyKcoll.addField("FndSrcProvCd", TagUtil.getEMPField(reflectKColl.getDataValue("FndSrcProvCd"),FieldType.FIELD_STRING,10,0));
		bodyKcoll.addField("DstrbtDdlnDt", TagUtil.getEMPField(reflectKColl.getDataValue("DstrbtDdlnDt"),FieldType.FIELD_STRING,8,0));
		bodyKcoll.addField("ClctPnyIntFlg", TagUtil.getEMPField(reflectKColl.getDataValue("ClctPnyIntFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("ClctCmpdIntFlg", TagUtil.getEMPField(reflectKColl.getDataValue("ClctCmpdIntFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("DcnLoanIntPnyIntRate", TagUtil.getEMPField(reflectKColl.getDataValue("DcnLoanIntPnyIntRate"),FieldType.FIELD_DOUBLE,15,8));
		bodyKcoll.addField("DcnLoanElyRepymtLwsFineAmt", TagUtil.getEMPField(reflectKColl.getDataValue("DcnLoanElyRepymtLwsFineAmt"),FieldType.FIELD_DOUBLE,17,2));
		bodyKcoll.addField("GrcTrmDayNum", TagUtil.getEMPField(reflectKColl.getDataValue("GrcTrmDayNum"),FieldType.FIELD_STRING,5,0));
		bodyKcoll.addField("GrcTrmStopMoEndFlg", TagUtil.getEMPField(reflectKColl.getDataValue("GrcTrmStopMoEndFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("AutoStlmntFlg", TagUtil.getEMPField(reflectKColl.getDataValue("AutoStlmntFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("MtchAmtRepymtPlanModMd", TagUtil.getEMPField(reflectKColl.getDataValue("MtchAmtRepymtPlanModMd"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("IntClrgWithPnpFlg", TagUtil.getEMPField(reflectKColl.getDataValue("IntClrgWithPnpFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("PpsDsc", TagUtil.getEMPField(reflectKColl.getDataValue("PpsDsc"),FieldType.FIELD_STRING,6,0));
		bodyKcoll.addField("GrtMd", TagUtil.getEMPField(reflectKColl.getDataValue("GrtMd"),FieldType.FIELD_STRING,6,0));
		bodyKcoll.addField("StatClFlg", TagUtil.getEMPField(reflectKColl.getDataValue("StatClFlg"),FieldType.FIELD_STRING,6,0));
		bodyKcoll.addField("StatClFlg1", TagUtil.getEMPField(reflectKColl.getDataValue("StatClFlg1"),FieldType.FIELD_STRING,6,0));
		bodyKcoll.addField("StatClFlg2", TagUtil.getEMPField(reflectKColl.getDataValue("StatClFlg2"),FieldType.FIELD_STRING,6,0));
		bodyKcoll.addField("CtrNo", TagUtil.getEMPField(reflectKColl.getDataValue("CtrNo"),FieldType.FIELD_STRING,50,0));
		bodyKcoll.addField("SyndLoanMgtBnkNo", TagUtil.getEMPField(reflectKColl.getDataValue("SyndLoanMgtBnkNo"),FieldType.FIELD_STRING,20,0));
		bodyKcoll.addField("SyndLoanBnkNo", TagUtil.getEMPField(reflectKColl.getDataValue("SyndLoanBnkNo"),FieldType.FIELD_STRING,20,0));
		bodyKcoll.addField("WithBrwRepymtFlg", TagUtil.getEMPField(reflectKColl.getDataValue("WithBrwRepymtFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("SbsdyIntEndDt", TagUtil.getEMPField(reflectKColl.getDataValue("SbsdyIntEndDt"),FieldType.FIELD_STRING,8,0));
		bodyKcoll.addField("RepymtFrqcyTp", TagUtil.getEMPField(reflectKColl.getDataValue("RepymtFrqcyTp"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("CrnMoRepymtFlg", TagUtil.getEMPField(reflectKColl.getDataValue("CrnMoRepymtFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("NotFullPrdMrgFlg", TagUtil.getEMPField(reflectKColl.getDataValue("NotFullPrdMrgFlg"),FieldType.FIELD_STRING,1,0));
		/* modify by liqh 2013/11/27  授权信息增加是否收取印花税，印花税收取方式，支付类型，客户类型， 委托人客户号      START*/
		bodyKcoll.addField("DblNo", TagUtil.getEMPField(reflectKColl.getDataValue("DblNo"),FieldType.FIELD_STRING,50,0));
		bodyKcoll.addField("NeedReChkFlg", TagUtil.getEMPField(reflectKColl.getDataValue("NeedReChkFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("LoanTrm", TagUtil.getEMPField(reflectKColl.getDataValue("LoanTrm"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("FullAmtWthdFlg", TagUtil.getEMPField(reflectKColl.getDataValue("FullAmtWthdFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("OrgnlDblNo", TagUtil.getEMPField(reflectKColl.getDataValue("OrgnlDblNo"),FieldType.FIELD_STRING,50,0));
		bodyKcoll.addField("EndtrmMrgFlg", TagUtil.getEMPField(reflectKColl.getDataValue("EndtrmMrgFlg"),FieldType.FIELD_STRING,1,0));
		bodyKcoll.addField("AcctPpsCd", TagUtil.getEMPField(reflectKColl.getDataValue("AcctPpsCd"),FieldType.FIELD_STRING,6,0));
		
//		if(reflectKColl.containsKey("PRE_INT_FLAG")){
//			bodyKcoll.addDataField("PRE_INT_FLAG", TagUtil.getEMPField(reflectKColl.getDataValue("PRE_INT_FLAG"), FieldType.FIELD_STRING, 1, 0));//是否预收息
//		}
//		//add by wangj 2015-06-02 HS141110017_保理业务改造 begin
//		if(reflectKColl.containsKey("CREDITOR_NO")){//债务人编号
//			bodyKcoll.addDataField("CREDITOR_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CREDITOR_NO"), FieldType.FIELD_STRING, 50, 0));//是否预收息
//		}
//		if(reflectKColl.containsKey("CREDITOR_NAME")){//债务人名称
//			bodyKcoll.addDataField("CREDITOR_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("CREDITOR_NAME"), FieldType.FIELD_STRING, 150, 0));//是否预收息
//		}
//		if(reflectKColl.containsKey("OUGHT_ACCEPT_AMT")){//应收账款金额
//			bodyKcoll.addDataField("OUGHT_ACCEPT_AMT", TagUtil.getEMPField(reflectKColl.getDataValue("OUGHT_ACCEPT_AMT"), FieldType.FIELD_DOUBLE, 20, 2));//是否预收息
//		}
		//add by wangj 2015-06-02 HS141110017_保理业务改造 end
		////reqCD.addStruct("BODY", bodyCD);
		
		/** 账号信息 */
		IndexedCollection zhIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_ZH+"' ", connection);
		if(zhIColl != null && zhIColl.size() > 0){
			Array zharray = new Array(); 
			for(int i=0;i<zhIColl.size();i++){
				CompositeData zhCD = new CompositeData();
				KeyedCollection zhKColl = (KeyedCollection)zhIColl.get(i);
				KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(zhKColl);
				zhCD.addField("StlmntBrBnkCd", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntBrBnkCd"),FieldType.FIELD_STRING,20,0));
				zhCD.addField("StlmntBnkCstNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntBnkCstNo"),FieldType.FIELD_STRING,20,0));
				zhCD.addField("StlmntAcctTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntAcctTp"),FieldType.FIELD_STRING,3,0));
				zhCD.addField("StlmntMd", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntMd"),FieldType.FIELD_STRING,3,0));
				zhCD.addField("ClctnPymtFlg", TagUtil.getEMPField(reflectSubKColl.getDataValue("ClctnPymtFlg"),FieldType.FIELD_STRING,3,0));
				zhCD.addField("AmtTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("AmtTp"),FieldType.FIELD_STRING,10,0));
				zhCD.addField("StlmntAcctNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntAcctNo"),FieldType.FIELD_STRING,50,0));
				zhCD.addField("StlmntAcctPdTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntAcctPdTp"),FieldType.FIELD_STRING,50,0));
				zhCD.addField("StlmntAcctSeqNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntAcctSeqNo"),FieldType.FIELD_STRING,8,0));
				zhCD.addField("StlmntCcy", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntCcy"),FieldType.FIELD_STRING,3,0));
				if (!"AUT".equals(reflectSubKColl.getDataValue("StlmntAcctTp")))
					zhCD.addField("StlmntAmt", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntAmt"),FieldType.FIELD_DOUBLE,17,2));
				else
					zhCD.addField("StlmntAmt", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntAmt"),FieldType.FIELD_STRING,10,3));
				zhCD.addField("AutoLockInd", TagUtil.getEMPField(reflectSubKColl.getDataValue("AutoLockInd"),FieldType.FIELD_STRING,1,0));
				zhCD.addField("PrtyLvl", TagUtil.getEMPField(reflectSubKColl.getDataValue("PrtyLvl"),FieldType.FIELD_STRING,5,0));
				zhCD.addField("StlmntRto", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntRto"),FieldType.FIELD_DOUBLE,5,2));
				zhCD.addField("StlmntNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntNo"),FieldType.FIELD_STRING,30,0));
				zhCD.addField("IvsRto", TagUtil.getEMPField(reflectSubKColl.getDataValue("IvsRto"),FieldType.FIELD_STRING,17,0));
				/* modify by liqh 2013/11/27  账号信息增加银行名称      START*/
				zhCD.addField("StlmntAcctNm", TagUtil.getEMPField(reflectSubKColl.getDataValue("StlmntAcctNm"),FieldType.FIELD_STRING,200,0));
				/* modify by liqh 2013/11/27  账号信息增加银行名称      END*/
				zhCD.addField("ClctnBnkNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("ClctnBnkNo"),FieldType.FIELD_STRING,12,0));
				zhCD.addField("ClctnBnkNm", TagUtil.getEMPField(reflectSubKColl.getDataValue("ClctnBnkNm"),FieldType.FIELD_STRING,60,0));
				zhCD.addField("BnkInnrBnkOthrFlg", TagUtil.getEMPField(reflectSubKColl.getDataValue("BnkInnrBnkOthrFlg"),FieldType.FIELD_STRING,1,0));
				zhCD.addField("FrzPayMd", TagUtil.getEMPField(reflectSubKColl.getDataValue("FrzPayMd"),FieldType.FIELD_STRING,1,0));
				zhCD.addField("FdcrPymtNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("FdcrPymtNo"),FieldType.FIELD_STRING,50,0));
				zhCD.addField("TxnTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("TxnTp"),FieldType.FIELD_STRING,4,0));
				zhCD.addField("DvdnPrftRto", TagUtil.getEMPField(reflectSubKColl.getDataValue("DvdnPrftRto"),FieldType.FIELD_STRING,17,0));
				zharray.addStruct(zhCD);
			}
			bodyKcoll.addArray("StlmntInfArry", zharray);
		}
		
		/** 利息信息 */
		IndexedCollection lxIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_LX+"' ", connection);
		if(lxIColl != null && lxIColl.size() > 0){
			Array lxarray = new Array(); 
			for(int i=0;i<lxIColl.size();i++){
				CompositeData lxCD = new CompositeData();
				KeyedCollection lxKColl = (KeyedCollection)lxIColl.get(i);
				KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(lxKColl);
				lxCD.addField("IntCtgryTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntCtgryTp"),FieldType.FIELD_STRING,10,0));
				lxCD.addField("IntRateTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntRateTp"),FieldType.FIELD_STRING,3,0));
				lxCD.addField("BnkInnrIntRate", TagUtil.getEMPField(reflectSubKColl.getDataValue("BnkInnrIntRate"),FieldType.FIELD_DOUBLE,15,8));
				lxCD.addField("FltIntRate", TagUtil.getEMPField(reflectSubKColl.getDataValue("FltIntRate"),FieldType.FIELD_DOUBLE,15,8));
				//lxCD.addField("SubsAcctLvlFltPntPct", TagUtil.getEMPField(reflectSubKColl.getDataValue("SubsAcctLvlFltPntPct"),FieldType.FIELD_DOUBLE,15,8));
				//lxCD.addField("SubsAcctLvlFltPct", TagUtil.getEMPField(reflectSubKColl.getDataValue("SubsAcctLvlFltPct"),FieldType.FIELD_DOUBLE,15,2));
				//lxCD.addField("SubsAcctLvlFixIntRate", TagUtil.getEMPField(reflectSubKColl.getDataValue("SubsAcctLvlFixIntRate"),FieldType.FIELD_DOUBLE,15,8));
				//如果固定利率为空则传空，不能为0
				if("".equals(reflectSubKColl.getDataValue("SubsAcctLvlFltPntPct"))){
					lxCD.addField("SubsAcctLvlFltPntPct", TagUtil.getEMPField(reflectSubKColl.getDataValue("SubsAcctLvlFltPntPct"),FieldType.FIELD_STRING,15,0));
				} else {
					lxCD.addField("SubsAcctLvlFltPntPct", TagUtil.getEMPField(Double.parseDouble((String)reflectSubKColl.getDataValue("SubsAcctLvlFltPntPct"))/100,FieldType.FIELD_DOUBLE,15,8));
				}
				if("".equals(reflectSubKColl.getDataValue("SubsAcctLvlFltPct"))) {
					lxCD.addField("SubsAcctLvlFltPct", TagUtil.getEMPField(reflectSubKColl.getDataValue("SubsAcctLvlFltPct"),FieldType.FIELD_STRING,15,0));
				} else {
					lxCD.addField("SubsAcctLvlFltPct", TagUtil.getEMPField(Double.parseDouble((String)reflectSubKColl.getDataValue("SubsAcctLvlFltPct"))*100,FieldType.FIELD_DOUBLE,15,2));
				}
				//如果固定利率为空则传空，不能为0
				if("".equals(reflectSubKColl.getDataValue("SubsAcctLvlFixIntRate"))){
					lxCD.addField("SubsAcctLvlFixIntRate", TagUtil.getEMPField(reflectSubKColl.getDataValue("SubsAcctLvlFixIntRate"),FieldType.FIELD_STRING,15,0));
				} else {
					lxCD.addField("SubsAcctLvlFixIntRate", TagUtil.getEMPField(((Double.parseDouble((String)reflectSubKColl.getDataValue("SubsAcctLvlFixIntRate")))*100),FieldType.FIELD_DOUBLE,15,8));
				}
				lxCD.addField("ExecIntRate", TagUtil.getEMPField(reflectSubKColl.getDataValue("ExecIntRate"),FieldType.FIELD_DOUBLE,15,8));
				lxCD.addField("AnulBaseDayNum", TagUtil.getEMPField(reflectSubKColl.getDataValue("AnulBaseDayNum"),FieldType.FIELD_STRING,3,0));
				lxCD.addField("MoBaseDaysNum", TagUtil.getEMPField(reflectSubKColl.getDataValue("MoBaseDaysNum"),FieldType.FIELD_STRING,3,0));
				lxCD.addField("IntRateEnblMd", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntRateEnblMd"),FieldType.FIELD_STRING,1,0));
				/* modify by liqh 2013/11/27  账号信息增加银行名称      START*/
				lxCD.addField("PnyIntRateUseMd", TagUtil.getEMPField(reflectSubKColl.getDataValue("PnyIntRateUseMd"),FieldType.FIELD_STRING,1,0));
				/* modify by liqh 2013/11/27  账号信息增加银行名称      END*/
				lxCD.addField("IntClcBegDt", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntClcBegDt"),FieldType.FIELD_STRING,8,0));
				lxCD.addField("IntClcDdlnDt", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntClcDdlnDt"),FieldType.FIELD_STRING,8,0));
				lxCD.addField("IntRateModDt", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntRateModDt"),FieldType.FIELD_STRING,8,0));
				lxCD.addField("IntRateModCyc", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntRateModCyc"),FieldType.FIELD_STRING,2,0));
				lxCD.addField("IntRateModDay", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntRateModDay"),FieldType.FIELD_STRING,2,0));
				lxCD.addField("ClcIntFlg", TagUtil.getEMPField(reflectSubKColl.getDataValue("ClcIntFlg"),FieldType.FIELD_STRING,1,0));
				lxCD.addField("IntRateTkEffMd", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntRateTkEffMd"),FieldType.FIELD_STRING,1,0));
				lxarray.addStruct(lxCD);
			}
			bodyKcoll.addArray("IntInfArry", lxarray);
		}
		
		/** 费用信息 */
//		IndexedCollection feeIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_FY+"' ", connection);
//		if(feeIColl != null && feeIColl.size() > 0){
//			Array feeArray = new Array();
//			for(int i=0;i<feeIColl.size();i++){
//				CompositeData feeCD = new CompositeData();
//				KeyedCollection feeKColl = (KeyedCollection)feeIColl.get(i);
//				KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(feeKColl);
//				feeCD.addField("SvcFeeTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("SvcFeeTp"),FieldType.FIELD_STRING,8,0));
//				feeCD.addField("ChrgCcy", TagUtil.getEMPField(reflectSubKColl.getDataValue("ChrgCcy"),FieldType.FIELD_STRING,3,0));
//				feeCD.addField("FeeAmt", TagUtil.getEMPField(reflectSubKColl.getDataValue("FeeAmt"),FieldType.FIELD_DOUBLE,17,2));
//				feeCD.addField("OrgnlSvcFeeAmt", TagUtil.getEMPField(reflectSubKColl.getDataValue("OrgnlSvcFeeAmt"),FieldType.FIELD_DOUBLE,17,2));
//				feeCD.addField("DcnAmt", TagUtil.getEMPField(reflectSubKColl.getDataValue("DcnAmt"),FieldType.FIELD_DOUBLE,17,2));
//				feeCD.addField("DcnTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("DcnTp"),FieldType.FIELD_STRING,20,0));
//				feeCD.addField("DcnRate", TagUtil.getEMPField(reflectSubKColl.getDataValue("DcnRate"),FieldType.FIELD_DOUBLE,15,8));
//				feeCD.addField("EODFlg", TagUtil.getEMPField(reflectSubKColl.getDataValue("EODFlg"),FieldType.FIELD_STRING,1,0));
//				feeCD.addField("ClctMd", TagUtil.getEMPField(reflectSubKColl.getDataValue("ClctMd"),FieldType.FIELD_STRING,2,0));
//				feeCD.addField("SvcFeeClctAcctAcctNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("SvcFeeClctAcctAcctNo"),FieldType.FIELD_STRING,50,0));
//				feeCD.addField("SvcFeeClctAcctCcy", TagUtil.getEMPField(reflectSubKColl.getDataValue("SvcFeeClctAcctCcy"),FieldType.FIELD_STRING,3,0));
//				feeCD.addField("SvcFeeClctAcctPdTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("SvcFeeClctAcctPdTp"),FieldType.FIELD_STRING,50,0));
//				feeCD.addField("SvcFeeClctAcctSeqNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("SvcFeeClctAcctSeqNo"),FieldType.FIELD_STRING,8,0));
//				feeCD.addField("DrwMd", TagUtil.getEMPField(reflectSubKColl.getDataValue("DrwMd"),FieldType.FIELD_STRING,1,0));
//				feeCD.addField("TxnPswdStrg", TagUtil.getEMPField(reflectSubKColl.getDataValue("TxnPswdStrg"),FieldType.FIELD_STRING,50,0));
//				feeCD.addField("TaxRateTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("TaxRateTp"),FieldType.FIELD_STRING,3,0));
//				feeCD.addField("TaxRate", TagUtil.getEMPField(reflectSubKColl.getDataValue("TaxRate"),FieldType.FIELD_DOUBLE,15,8));
//				feeCD.addField("IntTaxAmt", TagUtil.getEMPField(reflectSubKColl.getDataValue("IntTaxAmt"),FieldType.FIELD_DOUBLE,17,2));
//				feeCD.addField("VchrTp", TagUtil.getEMPField(reflectSubKColl.getDataValue("VchrTp"),FieldType.FIELD_STRING,10,0));
//				feeCD.addField("PfxDsc", TagUtil.getEMPField(reflectSubKColl.getDataValue("PfxDsc"),FieldType.FIELD_STRING,10,0));
//				feeCD.addField("VchrBegNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("VchrBegNo"),FieldType.FIELD_STRING,50,0));
//				feeCD.addField("VchrEndNo", TagUtil.getEMPField(reflectSubKColl.getDataValue("VchrEndNo"),FieldType.FIELD_STRING,50,0));
//				feeCD.addField("VchrNum", TagUtil.getEMPField(reflectSubKColl.getDataValue("VchrNum"),FieldType.FIELD_STRING,10,0));
//				feeCD.addField("UnitPrcAmt", TagUtil.getEMPField(reflectSubKColl.getDataValue("UnitPrcAmt"),FieldType.FIELD_DOUBLE,17,2));
//				feeArray.addStruct(feeCD);
//			}
//			bodyKcoll.addArray("SvcFeeInfArry", feeArray);
//		}
		
		//组装自由还款信息（查询授权子表类型为01的信息）
//		IndexedCollection iqpFreedomPayInfoIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_HK+"' ", connection);
//		if(iqpFreedomPayInfoIColl != null && iqpFreedomPayInfoIColl.size() > 0){
//			////Array payArray = new Array(); 
//			IndexedCollection payArray=new IndexedCollection();
//			payArray.setName("REPAY_PLAN_ARRAY");
//			for(int i=0;i<iqpFreedomPayInfoIColl.size();i++){
//				////CompositeData payCD = new CompositeData();
//				KeyedCollection payCD=new KeyedCollection();
//				KeyedCollection iqpFreedomPayInfoKColl = (KeyedCollection)iqpFreedomPayInfoIColl.get(i);
//				KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(iqpFreedomPayInfoKColl);
//				payCD.addDataField("DUEBILL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 50, 0));
//				payCD.addDataField("PERIOD_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("PERIOD_NO"), FieldType.FIELD_STRING, 40, 0));
//				payCD.addDataField("REPAY_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("REPAY_DATE"), FieldType.FIELD_STRING, 10, 0));
//				payCD.addDataField("CORPUS", TagUtil.getEMPField(reflectSubKColl.getDataValue("CORPUS"), FieldType.FIELD_DOUBLE, 20, 2));
//				payCD.addDataField("INTEREST", TagUtil.getEMPField(reflectSubKColl.getDataValue("INTEREST"), FieldType.FIELD_DOUBLE, 20, 2));
//				////payArray.addStruct(payCD);
//				payArray.addDataElement(payCD);
//			}
//			////bodyCD.addArray("REPAY_PLAN_ARRAY", payArray);
//			bodyKcoll.addDataElement(payArray);
//		}
		reqCD.addStruct("BODY", bodyKcoll);
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		return reqCD;
	}
}
