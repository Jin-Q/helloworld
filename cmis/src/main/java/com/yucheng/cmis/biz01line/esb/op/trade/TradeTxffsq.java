package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;
import java.util.Map;

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
import com.yucheng.cmis.biz01line.esb.pub.FTPUtil;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
/**
 * 贴现/转贴现买入卖出/内部转贴现/再贴现/回购/返售授权交易
 * @author liqh
 *
 */
public class TradeTxffsq extends TranClient {
	private static final String AUTHMODEL = "PvpAuthorize";
	private static final String PVPSUBMODEL = "PvpAuthorizeSub";
	@Override
	public CompositeData doExecute(Context context, Connection connection)
			throws EMPException {
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
			String authorize_no = (String)authKColl.getDataValue("authorize_no");
			IndexedCollection pvpIColl = dao.queryList(AUTHMODEL, " where authorize_no = '"+authorize_no+"' and status <> '02'", connection);
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, TradeConstance.SERVICE_SCENE_TXFFSQ, TradeConstance.CONSUMERID, TagUtil.getSysHeadSerno(authorize_no, context),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			/** 封装写入文件的报文信息，包括整体报文信息 */
			CompositeData fileCD = new CompositeData();
			CompositeData bodyListCD = new CompositeData();
			Array bodyArr = new Array();
			for(int i=0;i<pvpIColl.size();i++){
				KeyedCollection authKColl1 = (KeyedCollection)pvpIColl.get(i);
				KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl1);
				
				/** 核算与信贷业务品种映射 START */
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
				String lmPrdId = service.getPrdBasicCLPM2LM(reflectKColl.getDataValue("DUEBILL_NO").toString(), authKColl.getDataValue("prd_id").toString(), context, connection);
				/** 核算与信贷业务品种映射 END */
				
				CompositeData bodyArrCD = new CompositeData();
				bodyArrCD.addField("GEN_GL_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("GEN_GL_NO")), FieldType.FIELD_STRING, 30, 0));
				bodyArrCD.addField("OPERATION_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("OPERATION_TYPE"), FieldType.FIELD_STRING, 10, 0));
				bodyArrCD.addField("BACK_FLAG", TagUtil.getEMPField(reflectKColl.getDataValue("BACK_FLAG"), FieldType.FIELD_STRING, 10, 0));
				bodyArrCD.addField("INPUT_DATE", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("INPUT_DATE")), FieldType.FIELD_STRING, 8, 0));
				bodyArrCD.addField("BANK_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BANK_ID"), FieldType.FIELD_STRING, 30, 0));
				bodyArrCD.addField("BRANCH_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BRANCH_ID"), FieldType.FIELD_STRING, 40, 0));
				bodyArrCD.addField("DISCONT_AGREE_NO", TagUtil.getEMPField(reflectKColl.getDataValue("DISCONT_AGREE_NO"), FieldType.FIELD_STRING, 30, 0));
				/*modified by wangj XD150407026_贸易融资汇率问题 begin*/
				if ("500029".equals(authKColl.getDataValue("prd_id").toString())
						|| "500028".equals(authKColl.getDataValue("prd_id")
								.toString())
						|| "500027".equals(authKColl.getDataValue("prd_id")
								.toString())) {
					bodyArrCD.addField("LOAN_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_TYPE"), FieldType.FIELD_STRING, 20, 0));
				}else{
					bodyArrCD.addField("LOAN_TYPE", TagUtil.getEMPField(lmPrdId, FieldType.FIELD_STRING, 20, 0));
				}
				/*modified by wangj XD150407026_贸易融资汇率问题 begin*/
				bodyArrCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 50, 0));
				bodyArrCD.addField("BILL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("BILL_NO"), FieldType.FIELD_STRING, 50, 0));
				bodyArrCD.addField("BILL_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("BILL_TYPE"), FieldType.FIELD_STRING, 10, 0));
				bodyArrCD.addField("APPLYER_GLOBAL_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("APPLYER_GLOBAL_TYPE"), FieldType.FIELD_STRING, 5, 0));
				bodyArrCD.addField("APPLYER_GLOBAL_ID", TagUtil.getEMPField(reflectKColl.getDataValue("APPLYER_GLOBAL_ID"), FieldType.FIELD_STRING, 30, 0));
				bodyArrCD.addField("APPLYER_ISS_CTRY", TagUtil.getEMPField(reflectKColl.getDataValue("APPLYER_ISS_CTRY"), FieldType.FIELD_STRING, 10, 0));
				bodyArrCD.addField("APPLYER_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("APPLYER_NAME"), FieldType.FIELD_STRING, 200, 0));
				bodyArrCD.addField("APPLYER_ACCT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("APPLYER_ACCT_NO"), FieldType.FIELD_STRING, 200, 0));
				bodyArrCD.addField("BILL_BRANCH_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BILL_BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
				bodyArrCD.addField("DISCOUNT_KIND", TagUtil.getEMPField(reflectKColl.getDataValue("DISCOUNT_KIND"), FieldType.FIELD_STRING, 10, 0));
				bodyArrCD.addField("DISCOUNT_CCY", TagUtil.getEMPField(reflectKColl.getDataValue("DISCOUNT_CCY"), FieldType.FIELD_STRING, 3, 0));
				bodyArrCD.addField("DISCOUNT_AMT", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("DISCOUNT_AMT")), FieldType.FIELD_DOUBLE, 16, 2));
				bodyArrCD.addField("DISCOUNT_DAYS", TagUtil.getEMPField(TagUtil.replaceNull4Int(reflectKColl.getDataValue("DISCOUNT_DAYS")), FieldType.FIELD_INT, 10, 0));
				bodyArrCD.addField("DISCOUNT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("DISCOUNT_RATE")), FieldType.FIELD_DOUBLE, 20, 9));
				bodyArrCD.addField("TRANSFER_DISCOUNT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("TRANSFER_DISCOUNT_RATE")), FieldType.FIELD_DOUBLE, 20, 9));
				bodyArrCD.addField("TRANSFER_DISCOUNT_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("TRANSFER_DISCOUNT_DATE"), FieldType.FIELD_STRING, 8, 0));
				bodyArrCD.addField("DISCOUNT_INTEREST", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("DISCOUNT_INTEREST")), FieldType.FIELD_DOUBLE, 20, 2));
				bodyArrCD.addField("DISCOUNT_DATE", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("DISCOUNT_DATE")), FieldType.FIELD_STRING, 8, 0));
				bodyArrCD.addField("BILL_EXPIRY_DATE", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("BILL_EXPIRY_DATE")), FieldType.FIELD_STRING, 8, 0));
				bodyArrCD.addField("RETURN_DATE", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("RETURN_DATE")), FieldType.FIELD_STRING, 8, 0));
				bodyArrCD.addField("EXPIRY_DATE", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("EXPIRY_DATE")), FieldType.FIELD_STRING, 8, 0));
				bodyArrCD.addField("DEDUCT_METHOD", TagUtil.getEMPField(reflectKColl.getDataValue("DEDUCT_METHOD"), FieldType.FIELD_STRING, 10, 0));
				bodyArrCD.addField("TO_BRANCH_ID", TagUtil.getEMPField(reflectKColl.getDataValue("TO_BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
//				bodyArrCD.addField("PAY_INT_ACCT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("PAY_INT_ACCT_NO"), FieldType.FIELD_STRING, 50, 0));
//				bodyArrCD.addField("PAY_INT_ACCT_NO1", TagUtil.getEMPField(reflectKColl.getDataValue("PAY_INT_ACCT_NO1"), FieldType.FIELD_STRING, 51, 0));
//				bodyArrCD.addField("PAY_INT_ACCT_NO2", TagUtil.getEMPField(reflectKColl.getDataValue("PAY_INT_ACCT_NO2"), FieldType.FIELD_STRING, 52, 0));
//				bodyArrCD.addField("PAY_INT_ACCT_NO3", TagUtil.getEMPField(reflectKColl.getDataValue("PAY_INT_ACCT_NO3"), FieldType.FIELD_STRING, 53, 0));
				bodyArrCD.addField("BILL_ISSUE_DATE", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("BILL_ISSUE_DATE")), FieldType.FIELD_STRING, 8, 0));
				bodyArrCD.addField("DSCNT_INT_PAY_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("DSCNT_INT_PAY_MODE"), FieldType.FIELD_STRING, 5, 0));
				bodyArrCD.addField("AGENTOR_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("AGENTOR_NAME"), FieldType.FIELD_STRING, 150, 0));
	            bodyArrCD.addField("AGENTOR_ACCT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("AGENTOR_ACCT_NO"), FieldType.FIELD_STRING, 50, 0));
	            bodyArrCD.addField("AGENT_ORG_NO", TagUtil.getEMPField(reflectKColl.getDataValue("AGENT_ORG_NO"), FieldType.FIELD_STRING, 50, 0));
	            bodyArrCD.addField("AGENT_ORG_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("AGENT_ORG_NAME"), FieldType.FIELD_STRING, 150, 0));
	            bodyArrCD.addField("COUNTER_OPEN_BRANCH_ID", TagUtil.getEMPField(reflectKColl.getDataValue("COUNTER_OPEN_BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
	            bodyArrCD.addField("COUNTER_OPEN_BRANCH_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("COUNTER_OPEN_BRANCH_NAME"), FieldType.FIELD_STRING, 40, 0));
	            bodyArrCD.addField("AA_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("AA_NAME"), FieldType.FIELD_STRING, 150, 0));
	            bodyArrCD.addField("AAORG_NO", TagUtil.getEMPField(reflectKColl.getDataValue("AAORG_NO"), FieldType.FIELD_STRING, 40, 0));
	            bodyArrCD.addField("AAORG_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("AAORG_NAME"), FieldType.FIELD_STRING, 150, 0));
	            bodyArrCD.addField("PAY_AMT", TagUtil.getEMPField(reflectKColl.getDataValue("PAY_AMT"), FieldType.FIELD_DOUBLE, 20, 2));
	            bodyArrCD.addField("ARRANGR_DEDUCT_OPT", TagUtil.getEMPField(reflectKColl.getDataValue("ARRANGR_DEDUCT_OPT"), FieldType.FIELD_DOUBLE, 20, 2));
	            bodyArrCD.addField("APPLYER_ID", TagUtil.getEMPField(reflectKColl.getDataValue("APPLYER_ID"), FieldType.FIELD_STRING, 30, 0));
	            bodyArrCD.addField("E_CDE", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("E_CDE")), FieldType.FIELD_STRING, 2, 0));
	            bodyArrCD.addField("BATCH_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("BATCH_NO")), FieldType.FIELD_STRING, 40, 0));
	            if(reflectKColl.containsKey("CUST_TYP")&&reflectKColl.getDataValue("CUST_TYP")!=null&&!"".equals(reflectKColl.getDataValue("CUST_TYP"))){
	            	bodyArrCD.addField("CUST_TYP", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("CUST_TYP")), FieldType.FIELD_STRING, 10, 0));
	            }else{
	            	bodyArrCD.addField("CUST_TYP", TagUtil.getEMPField(TagUtil.replaceNull4String(""), FieldType.FIELD_STRING, 10, 0));
	            }
	            bodyArrCD.addField("DISC_OD_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("DISC_OD_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 9));
	            bodyArrCD.addField("BILL_APP_NAME", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("BILL_APP_NAME")), FieldType.FIELD_STRING, 150, 0));
	            bodyArrCD.addField("ACP_BANK_ACCT_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("ACP_BANK_ACCT_NO")), FieldType.FIELD_STRING, 40, 0));
	            bodyArrCD.addField("DSCNT_TYPE", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("DSCNT_TYPE")), FieldType.FIELD_STRING, 10, 0));
	            bodyArrCD.addField("BILL_ACCT_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("BILL_ACCT_NO")), FieldType.FIELD_STRING, 40, 0));
	            bodyArrCD.addField("BILL_OPEN_BRANCH_ID", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("BILL_OPEN_BRANCH_ID")), FieldType.FIELD_STRING, 40, 0));
	            bodyArrCD.addField("BILL_OPEN_BRANCH_NAME", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("BILL_OPEN_BRANCH_NAME")), FieldType.FIELD_STRING, 150, 0));
	            
	            bodyArr.addStruct(bodyArrCD);
			}
			bodyListCD.addArray("BASE_BODY", bodyArr);
			fileCD.addStruct("BODY", bodyListCD);
			/** 账号信息 */
			IndexedCollection zhIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authorize_no+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_ZH+"' ", connection);
			if(zhIColl != null && zhIColl.size() > 0){
				Array zharray = new Array(); 
				for(int i=0;i<zhIColl.size();i++){
					CompositeData zhCD = new CompositeData();
					KeyedCollection zhKColl = (KeyedCollection)zhIColl.get(i);
					KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(zhKColl);
					zhCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
					zhCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 50, 0));
					zhCD.addField("LOAN_ACCT_TYPE", TagUtil.getEMPField(reflectSubKColl.getDataValue("LOAN_ACCT_TYPE"), FieldType.FIELD_STRING, 6, 0));
					zhCD.addField("ACCT_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_NO"), FieldType.FIELD_STRING, 50, 0));
					zhCD.addField("CCY", TagUtil.getEMPField(reflectSubKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 3, 0));
					zhCD.addField("BANK_ID", TagUtil.getEMPField(reflectSubKColl.getDataValue("BANK_ID"), FieldType.FIELD_STRING, 30, 0));
					zhCD.addField("BRANCH_ID", TagUtil.getEMPField(reflectSubKColl.getDataValue("BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
					zhCD.addField("ACCT_NAME", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_NAME"), FieldType.FIELD_STRING, 150, 0));
					zhCD.addField("ISS_CTRY", TagUtil.getEMPField(reflectSubKColl.getDataValue("ISS_CTRY"), FieldType.FIELD_STRING, 10, 0));
					zhCD.addField("ACCT_TYPE", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_TYPE"), FieldType.FIELD_STRING, 6, 0));
					zhCD.addField("GLOBAL_TYPE", TagUtil.getEMPField(reflectSubKColl.getDataValue("GLOBAL_TYPE"), FieldType.FIELD_STRING, 5, 0));
					zhCD.addField("GLOBAL_ID", TagUtil.getEMPField(reflectSubKColl.getDataValue("GLOBAL_ID"), FieldType.FIELD_STRING, 30, 0));
					zhCD.addField("REMARK", TagUtil.getEMPField(reflectSubKColl.getDataValue("REMARK"), FieldType.FIELD_STRING, 300, 0));
					zhCD.addField("CARD_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("CARD_NO"), FieldType.FIELD_STRING, 20, 0));
					zhCD.addField("CA_TT_FLAG", TagUtil.getEMPField(reflectSubKColl.getDataValue("CA_TT_FLAG"), FieldType.FIELD_STRING, 2, 0));
					zhCD.addField("ACCT_CODE", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_CODE"), FieldType.FIELD_STRING, 10, 0));
					zhCD.addField("ACCT_GL_CODE", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_GL_CODE"), FieldType.FIELD_STRING, 10, 0));
					zhCD.addField("BALANCE", TagUtil.getEMPField(reflectSubKColl.getDataValue("BALANCE"), FieldType.FIELD_STRING, 20, 2));
					zhCD.addField("COMMISSION_PAYMENT_AMOUNT", TagUtil.getEMPField(reflectSubKColl.getDataValue("COMMISSION_PAYMENT_AMOUNT"), FieldType.FIELD_STRING, 20, 2));
					/* modify by liqh 2013/11/27  账号信息增加银行名称      START*/
					zhCD.addField("BANK_NAME", TagUtil.getEMPField(reflectSubKColl.getDataValue("BANK_NAME"), FieldType.FIELD_STRING, 100, 0));
					/* modify by liqh 2013/11/27  账号信息增加银行名称      END*/
					zhCD.addField("AGREE_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 30, 0));
					zhCD.addField("OWN_BRANCH_FLAG", TagUtil.getEMPField(reflectSubKColl.getDataValue("OWN_BRANCH_FLAG"), FieldType.FIELD_STRING, 1, 0));
					zhCD.addField("ACCT_BANK_ADD", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_BANK_ADD"), FieldType.FIELD_STRING, 100, 0));
					zharray.addStruct(zhCD);
				}
				bodyListCD.addArray("ACCT_INFO_ARRAY", zharray);
			}
			/** 费用信息 */
			IndexedCollection feeIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_FY+"' ", connection);
			if(feeIColl != null && feeIColl.size() > 0){
				Array feeArray = new Array(); 
				for(int i=0;i<feeIColl.size();i++){
					CompositeData feeCD = new CompositeData();
					KeyedCollection feeKColl = (KeyedCollection)feeIColl.get(i);
					KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(feeKColl);
					feeCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
					feeCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 50, 0));
					feeCD.addField("FEE_CODE", TagUtil.getEMPField(reflectSubKColl.getDataValue("FEE_CODE"), FieldType.FIELD_STRING, 10, 0));
					feeCD.addField("CCY", TagUtil.getEMPField(reflectSubKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 3, 0));
					feeCD.addField("FEE_AMOUNT", TagUtil.getEMPField(reflectSubKColl.getDataValue("FEE_AMOUNT"), FieldType.FIELD_DOUBLE, 20, 2));
					feeCD.addField("INOUT_FLAG", TagUtil.getEMPField(reflectSubKColl.getDataValue("INOUT_FLAG"), FieldType.FIELD_STRING, 1, 0));
					feeCD.addField("FEE_TYPE", TagUtil.getEMPField(reflectSubKColl.getDataValue("FEE_TYPE"), FieldType.FIELD_STRING, 10, 0));
					feeCD.addField("BASE_AMOUNT", TagUtil.getEMPField(reflectSubKColl.getDataValue("BASE_AMOUNT"), FieldType.FIELD_DOUBLE, 20, 2));
					feeCD.addField("FEE_RATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("FEE_RATE"), FieldType.FIELD_DOUBLE, 20, 7));
					feeCD.addField("DEDUCT_ACCT_TYPE", TagUtil.getEMPField(reflectSubKColl.getDataValue("DEDUCT_ACCT_TYPE"), FieldType.FIELD_STRING, 150, 0));
					feeCD.addField("CHARGE_PERIODICITY_FLAG", TagUtil.getEMPField(reflectSubKColl.getDataValue("CHARGE_PERIODICITY_FLAG"), FieldType.FIELD_STRING, 1, 0));
					feeCD.addField("CHARGE_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("CHARGE_DATE"), FieldType.FIELD_STRING, 8, 0));
					feeCD.addField("AMOR_AMT", TagUtil.getEMPField(reflectSubKColl.getDataValue("AMOR_AMT"), FieldType.FIELD_DOUBLE, 20, 2));
					feeCD.addField("CONTRACT_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 30, 0));
//					feeCD.addField("ACCT_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_NO"), FieldType.FIELD_STRING, 50, 0));
//					feeCD.addField("ACCT_NAME", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_NAME"), FieldType.FIELD_STRING, 150, 0));
//					feeCD.addField("OPEN_ACCT_BRANCH_ID", TagUtil.getEMPField(reflectSubKColl.getDataValue("OPAC_ORG_NO"), FieldType.FIELD_STRING, 20, 0));
//					feeCD.addField("OPEN_ACCT_BRANCH_NAME", TagUtil.getEMPField(reflectSubKColl.getDataValue("OPAN_ORG_NAME"), FieldType.FIELD_STRING, 150, 0));
//					feeCD.addField("ACCT_CCY", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_CCY"), FieldType.FIELD_STRING, 3, 0));
//					feeCD.addField("OWN_BRANCH_FLAG", TagUtil.getEMPField(reflectSubKColl.getDataValue("IS_THIS_ORG_ACCT"), FieldType.FIELD_STRING, 1, 0));
					feeCD.addField("BRANCH_ID", TagUtil.getEMPField(reflectSubKColl.getDataValue("BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
					feeCD.addField("FEE_SPAN", TagUtil.getEMPField(reflectSubKColl.getDataValue("FEE_SPAN"), FieldType.FIELD_STRING, 10, 0));
					feeArray.addStruct(feeCD);
				}
				bodyListCD.addArray("FEE_AUTH_INFO_ARRAY", feeArray);
			}
			/** 保证金信息 */
			IndexedCollection bzIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_BZ+"' ", connection);
			if(bzIColl != null && bzIColl.size() > 0){
				Array bzarray = new Array(); 
				for(int i=0;i<bzIColl.size();i++){
					CompositeData bzCD = new CompositeData();
					KeyedCollection bzKColl = (KeyedCollection)bzIColl.get(i);
					KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(bzKColl);
					bzCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
					bzCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 50, 0));
					bzCD.addField("BRANCH_ID", TagUtil.getEMPField(reflectSubKColl.getDataValue("BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
					bzCD.addField("GUARANTEE_ACCT_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_ACCT_NO"), FieldType.FIELD_STRING, 50, 0));
					bzCD.addField("GUARANTEE_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_NO"), FieldType.FIELD_STRING, 50, 0));
					bzCD.addField("ACCT_TYPE", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_TYPE"), FieldType.FIELD_STRING, 6, 0));
					bzCD.addField("GUARANTEE_EXPIRY_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_EXPIRY_DATE"), FieldType.FIELD_STRING, 8, 0));
					bzCD.addField("ACCT_CODE", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_CODE"), FieldType.FIELD_STRING, 10, 0));
					bzCD.addField("CA_TT_FLAG", TagUtil.getEMPField(reflectSubKColl.getDataValue("CA_TT_FLAG"), FieldType.FIELD_STRING, 2, 0));
					bzCD.addField("ACCT_GL_CODE", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_GL_CODE"), FieldType.FIELD_STRING, 10, 0));
					bzCD.addField("ACCT_NAME", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_NAME"), FieldType.FIELD_STRING, 150, 0));
					bzCD.addField("CCY", TagUtil.getEMPField(reflectSubKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 30, 0));
					bzCD.addField("GUARANTEE_PER", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_PER"), FieldType.FIELD_DOUBLE, 16, 2));
					bzCD.addField("GUARANTEE_AMT", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_AMT"), FieldType.FIELD_DOUBLE, 16, 2));
					bzCD.addField("CONTRACT_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 30, 0));
					bzCD.addField("ALL_PAY_FLAG", TagUtil.getEMPField(reflectSubKColl.getDataValue("ALL_PAY_FLAG"), FieldType.FIELD_STRING, 5, 0));
					bzarray.addStruct(bzCD);
				}
				bodyListCD.addArray("GUARANTEE_AUTH_INFO_ARRAY", bzarray);
			}
			
			/** 应用头 */
			CompositeData APP_HEAD= esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM);
			APP_HEAD.addField("FILE_NAME",TagUtil.getEMPField(FTPUtil.getFileName(serviceCode, TradeConstance.SERVICE_SCENE_TXFFSQ, authorize_no), FieldType.FIELD_STRING, 100, 0));
			APP_HEAD.addField("FILE_PATH",TagUtil.getEMPField(FTPUtil.getFilePath(), FieldType.FIELD_STRING, 100, 0));
			APP_HEAD.addField("TOTAL_ROWS",TagUtil.getEMPField(pvpIColl.size(), FieldType.FIELD_STRING, 100, 0));
			reqCD.addStruct("APP_HEAD", APP_HEAD);
			/**扩展头**/
			CompositeData LOCAL_HEAD= new CompositeData();
			LOCAL_HEAD.addField("FILE_FLAG", TagUtil.getEMPField("1", FieldType.FIELD_STRING, 6, 0));
			reqCD.addStruct("LOCAL_HEAD", LOCAL_HEAD);
			
			FTPUtil.send2FTP(serviceCode, TradeConstance.SERVICE_SCENE_TXFFSQ, authorize_no, fileCD);
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e.getMessage());
		}
		return reqCD;
	}

	//XD150520037_信贷系统利率调整修改优化
	public void doSuccess(Context context, Connection connection) throws EMPException{
		
	}
}
