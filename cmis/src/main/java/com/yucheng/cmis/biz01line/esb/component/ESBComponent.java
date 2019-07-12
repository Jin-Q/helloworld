package com.yucheng.cmis.biz01line.esb.component;

import java.sql.Connection;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.client.ESBClient;
import com.dcfs.esb.pack.standardxml.PackUtil;
import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.FTPUtil;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

/**
 * 交易业务处理类，处理交易码转发的各类交易
 * 注：如果有用到FTP传输操作的，可以查看FTPUtil.java
 * @author Pansq
 */
public class ESBComponent extends CMISComponent {
	private static final String PVPSUBMODEL = "PvpAuthorizeSub";
	/**
	 * 贷款发放授权交易
	 * @param authKColl 授权信息
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 反馈信息
	 * @throws Exception
	 */
	public KeyedCollection sendEsb4Dkffsq(KeyedCollection authKColl, TableModelDAO dao, Context context, Connection connection) throws Exception {
		String service_code = (String)authKColl.getDataValue("tran_id");
		service_code = service_code.substring(0, service_code.length()-2);
		String serno = (String)authKColl.getDataValue("serno");
		ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData();
		/** 系统头 */
		reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(service_code, TradeConstance.SERVICE_SCENE_DKFFSQ, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
		/** 应用头 */
		reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
				(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
		
		/** 封装报文体 */
		KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl);
		CompositeData bodyCD = new CompositeData();
		bodyCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
		bodyCD.addField("APPLY_NO", TagUtil.getEMPField(reflectKColl.getDataValue("APPLY_NO"), FieldType.FIELD_STRING, 32, 0));
		bodyCD.addField("APPLY_DATE", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("APPLY_DATE")), FieldType.FIELD_STRING, 8, 0));
		bodyCD.addField("CONTRACT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 30, 0));
		bodyCD.addField("ORG_NO", TagUtil.getEMPField(reflectKColl.getDataValue("ORG_NO"), FieldType.FIELD_STRING, 20, 0));
		bodyCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 30, 0));
		bodyCD.addField("BANK_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BANK_ID"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CLIENT_NO"), FieldType.FIELD_STRING, 30, 0));
		bodyCD.addField("CLIENT_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("CLIENT_NAME"), FieldType.FIELD_STRING, 20, 0));
		bodyCD.addField("GLOBAL_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("GLOBAL_TYPE"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("GLOBAL_ID", TagUtil.getEMPField(reflectKColl.getDataValue("GLOBAL_ID"), FieldType.FIELD_STRING, 40, 0));
		bodyCD.addField("ISS_CTRY", TagUtil.getEMPField(reflectKColl.getDataValue("ISS_CTRY"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("DEALER_CDE", TagUtil.getEMPField(reflectKColl.getDataValue("DEALER_CDE"), FieldType.FIELD_STRING, 20, 0));
		bodyCD.addField("CCY", TagUtil.getEMPField(reflectKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("APPLY_AMOUNT", TagUtil.getEMPField(reflectKColl.getDataValue("APPLY_AMOUNT"), FieldType.FIELD_DOUBLE, 16, 2));
		bodyCD.addField("DRAW_DOWN_DATE", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("DRAW_DOWN_DATE")), FieldType.FIELD_STRING, 8, 0));
		bodyCD.addField("LOAN_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_TYPE"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("CONTRACT_EXPIRY_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("CONTRACT_EXPIRY_DATE"), FieldType.FIELD_STRING, 8, 0));
		bodyCD.addField("VALUE_DATE", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("VALUE_DATE")), FieldType.FIELD_STRING, 8, 0));
		bodyCD.addField("DEDUCT_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("DEDUCT_DATE"), FieldType.FIELD_DOUBLE, 2, 0));
		bodyCD.addField("INT_RATE_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("INT_RATE_MODE"), FieldType.FIELD_STRING, 2, 0));
		bodyCD.addField("INT_RATE_BASE", TagUtil.getEMPField(reflectKColl.getDataValue("INT_RATE_BASE"), FieldType.FIELD_STRING, 1, 0));
		bodyCD.addField("BASE_INT_RATE_CODE", TagUtil.getEMPField(reflectKColl.getDataValue("BASE_INT_RATE_CODE"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("BASE_INT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("BASE_INT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
		bodyCD.addField("INT_FLT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("INT_FLT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
		bodyCD.addField("SPREAD", TagUtil.getEMPField(reflectKColl.getDataValue("SPREAD"), FieldType.FIELD_DOUBLE, 16, 9));
		bodyCD.addField("ACT_INT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("ACT_INT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
		bodyCD.addField("OD_RATE_BASE", TagUtil.getEMPField(reflectKColl.getDataValue("OD_RATE_BASE"), FieldType.FIELD_STRING, 1, 0));
		bodyCD.addField("LOAN_OD_RATE_CODE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_RATE_CODE"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("LOAN_OD_BASE_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_BASE_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
		bodyCD.addField("LOAN_OD_BASE_FLT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_BASE_FLT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
		bodyCD.addField("LOAN_OD_ACT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_ACT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
		bodyCD.addField("REPAY_FREQUENCY_UNIT", TagUtil.getEMPField(reflectKColl.getDataValue("REPAY_FREQUENCY_UNIT"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("REPAY_FREQUENCY", TagUtil.getEMPField(reflectKColl.getDataValue("REPAY_FREQUENCY"), FieldType.FIELD_DOUBLE, 3, 0));
		bodyCD.addField("LOAN_REPAY_METHOD", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_REPAY_METHOD"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("LOAN_REPAY_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_REPAY_TYPE"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("BUSS_SOURCE", TagUtil.getEMPField(reflectKColl.getDataValue("BUSS_SOURCE"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("LOAN_GRACE_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_GRACE_TYPE"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("LOAN_GRACE_DAYS", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_GRACE_DAYS"), FieldType.FIELD_DOUBLE, 3, 0));
		bodyCD.addField("DEDUCT_METHOD", TagUtil.getEMPField(reflectKColl.getDataValue("DEDUCT_METHOD"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("FIX_OD_RATE_FLAG", TagUtil.getEMPField(reflectKColl.getDataValue("FIX_OD_RATE_FLAG"), FieldType.FIELD_STRING, 1, 0));
		bodyCD.addField("LOAN_OD_RATE_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_RATE_TYPE"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("LOAN_OD_ACT_FLT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_ACT_FLT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
		bodyCD.addField("LOAN_OD_COMM_PART", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_COMM_PART"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("LOAN_OD_CPD_FLAG", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_CPD_FLAG"), FieldType.FIELD_STRING, 1, 0));
		bodyCD.addField("NEXT_RADJ_OPT", TagUtil.getEMPField(reflectKColl.getDataValue("NEXT_RADJ_OPT"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("NEXT_RADJ_FREQ", TagUtil.getEMPField(reflectKColl.getDataValue("NEXT_RADJ_FREQ"), FieldType.FIELD_DOUBLE, 2, 0));
		bodyCD.addField("NEXT_RADJ_FREQ_UNIT", TagUtil.getEMPField(reflectKColl.getDataValue("NEXT_RADJ_FREQ_UNIT"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("FIRST_ADJUST_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("FIRST_ADJUST_DATE"), FieldType.FIELD_STRING, 8, 0));
		bodyCD.addField("DIVERT_FLT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("DIVERT_FLT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
		bodyCD.addField("OVERDUE_ACT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("OVERDUE_ACT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
		bodyCD.addField("OVERDUE_FLT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("OVERDUE_FLT_RATE"), FieldType.FIELD_DOUBLE, 16, 9));
		bodyCD.addField("ASSET_BUY_FLAG", TagUtil.getEMPField(reflectKColl.getDataValue("ASSET_BUY_FLAG"), FieldType.FIELD_STRING, 1, 0));
		bodyCD.addField("CONSIGN_AGREE", TagUtil.getEMPField(reflectKColl.getDataValue("CONSIGN_AGREE"), FieldType.FIELD_STRING, 30, 0));
		bodyCD.addField("TERM_PAY_FLAG", TagUtil.getEMPField(reflectKColl.getDataValue("TERM_PAY_FLAG"), FieldType.FIELD_STRING, 1, 0));
		bodyCD.addField("LOAN_PROMISE_DUEBILL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_PROMISE_DUEBILL_NO"), FieldType.FIELD_STRING, 30, 0));
		reqCD.addStruct("BODY", bodyCD);
		
		/** 账号信息 */
		IndexedCollection zhIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_ZH+"' ", connection);
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
				zharray.addStruct(zhCD);
			}
			bodyCD.addArray("ACCT_INFO_ARRAY", zharray);
		}
		
		EMPLog.log("inReport", EMPLog.INFO, 0, "***********************************信贷与核算交易*************************************");
		EMPLog.log("inReport", EMPLog.INFO, 0, "*-----------------------------贷款发放授权交易("+service_code+")-------------------------------*");
		EMPLog.log("inReport", EMPLog.INFO, 0, "*************************************************************************************");
		EMPLog.log("inReport", EMPLog.INFO, 0, "----------------------------------------请求报文start---------------------------------");
		EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(reqCD), "UTF-8"));
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------请求报文end---------------------------------------");
		
		
		/** 发送报文请求，开始与ESB建立连接 */
		CompositeData respCD = ESBClient.request(reqCD);
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------响应报文start-------------------------------------");
		EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(respCD), "UTF-8"));
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------响应报文end---------------------------------------");
		return esbInterfacesImple.getRespSysHeadCD(respCD);
	}
	
	/**
	 * 保函发放授权交易
	 * @param authKColl 授权信息
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 反馈信息
	 * @throws Exception
	 */
	public KeyedCollection sendEsb4Bhffsq(KeyedCollection authKColl, TableModelDAO dao, Context context, Connection connection) throws Exception {
		String service_code = (String)authKColl.getDataValue("tran_id");
		service_code = service_code.substring(0, service_code.length()-2);
		String serno = (String)authKColl.getDataValue("serno");
		ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData();
		/** 系统头 */
		reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(service_code, TradeConstance.SERVICE_SCENE_BHFFSQ, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
		/** 应用头 */
		reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
				(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
		
		/** 封装报文体 */
		KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl);
		CompositeData bodyCD = new CompositeData();
		bodyCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
		bodyCD.addField("BANK_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BANK_ID"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("BRANCH_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BRANCH_ID"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 30, 0));
		bodyCD.addField("GT_AGREE_NO", TagUtil.getEMPField(reflectKColl.getDataValue("GT_AGREE_NO"), FieldType.FIELD_STRING, 50, 0));
		bodyCD.addField("GLOBAL_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("GLOBAL_TYPE"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("GLOBAL_ID", TagUtil.getEMPField(reflectKColl.getDataValue("GLOBAL_ID"), FieldType.FIELD_STRING, 40, 0));
		bodyCD.addField("ISS_CTRY", TagUtil.getEMPField(reflectKColl.getDataValue("ISS_CTRY"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CLIENT_NO"), FieldType.FIELD_STRING, 30, 0));
		bodyCD.addField("GT_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("GT_NAME"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("CCY", TagUtil.getEMPField(reflectKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("LOAN_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_TYPE"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("GT_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("GT_TYPE"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("GT_AMOUNT", TagUtil.getEMPField(reflectKColl.getDataValue("GT_AMOUNT"), FieldType.FIELD_DOUBLE, 20, 2));
		bodyCD.addField("COMMISSION", TagUtil.getEMPField(reflectKColl.getDataValue("COMMISSION"), FieldType.FIELD_DOUBLE, 20, 2));
		bodyCD.addField("DEDUCT_METHOD", TagUtil.getEMPField(reflectKColl.getDataValue("DEDUCT_METHOD"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("OD_INT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("OD_INT_RATE"), FieldType.FIELD_DOUBLE, 20, 7));
		bodyCD.addField("TERM", TagUtil.getEMPField(TagUtil.replaceNull4Int(reflectKColl.getDataValue("TERM")), FieldType.FIELD_STRING, 5, 0));
		bodyCD.addField("TERM_TYPE", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("TERM_TYPE")), FieldType.FIELD_STRING, 5, 0));
		reqCD.addStruct("BODY", bodyCD);
		
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
				bzCD.addField("GUARANTEE_PER", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_PER"), FieldType.FIELD_STRING, 30, 0));
				bzCD.addField("GUARANTEE_AMT", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_AMT"), FieldType.FIELD_STRING, 30, 0));
				bzarray.addStruct(bzCD);
			}
			bodyCD.addArray("GUARANTEE_AUTH_INFO_ARRAY", bzarray);
		}
		
		/** 账号信息 */
		IndexedCollection zhIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_ZH+"' ", connection);
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
				zharray.addStruct(zhCD);
			}
			bodyCD.addArray("ACCT_INFO_ARRAY", zharray);
		}
		
		EMPLog.log("inReport", EMPLog.INFO, 0, "***********************************信贷与核算交易*************************************");
		EMPLog.log("inReport", EMPLog.INFO, 0, "*-----------------------------保函发放授权交易("+service_code+")-------------------------------*");
		EMPLog.log("inReport", EMPLog.INFO, 0, "*************************************************************************************");
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------请求报文start-------------------");
		EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(reqCD), "UTF-8"));
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------请求报文end---------------------------------------");
		
		
		/** 发送报文请求，开始与ESB建立连接 */
		CompositeData respCD = ESBClient.request(reqCD);
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------响应报文start-------------------------------------");
		EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(respCD), "UTF-8"));
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------响应报文end---------------------------------------");
		return esbInterfacesImple.getRespSysHeadCD(respCD);
	}
	
	/**
	 * 贷款承诺发放授权交易
	 * @param authKColl 授权信息
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 反馈信息
	 * @throws Exception
	 */
	public KeyedCollection sendEsb4Dkcnsq(KeyedCollection authKColl, TableModelDAO dao, Context context, Connection connection) throws Exception {
		String service_code = (String)authKColl.getDataValue("tran_id");
		service_code = service_code.substring(0, service_code.length()-2);
		String serno = (String)authKColl.getDataValue("serno");
		ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData();
		/** 系统头 */
		reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(service_code, TradeConstance.SERVICE_SCENE_DKCNSQ, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
		/** 应用头 */
		reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
				(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
		
		/** 封装报文体 */
		KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl);
		CompositeData bodyCD = new CompositeData();
		bodyCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
		bodyCD.addField("BANK_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BANK_ID"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("BRANCH_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BRANCH_ID"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 30, 0));
		bodyCD.addField("LOAN_PROMISE_AGREE_NO", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_PROMISE_AGREE_NO"), FieldType.FIELD_STRING, 30, 0));
		bodyCD.addField("CCY", TagUtil.getEMPField(reflectKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("LOAN_PROMISE_AMOUNT", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_PROMISE_AMOUNT"), FieldType.FIELD_DOUBLE, 20, 2));
		bodyCD.addField("COMMISSION", TagUtil.getEMPField(reflectKColl.getDataValue("COMMISSION"), FieldType.FIELD_DOUBLE, 20, 2));
		bodyCD.addField("GLOBAL_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("GLOBAL_TYPE"), FieldType.FIELD_STRING, 5, 0));
		bodyCD.addField("GLOBAL_ID", TagUtil.getEMPField(reflectKColl.getDataValue("GLOBAL_ID"), FieldType.FIELD_STRING, 30, 0));
		bodyCD.addField("ISS_CTRY", TagUtil.getEMPField(reflectKColl.getDataValue("ISS_CTRY"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CLIENT_NO"), FieldType.FIELD_STRING, 30, 0));
		reqCD.addStruct("BODY", bodyCD);
		
		/** 账号信息 */
		IndexedCollection zhIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_ZH+"' ", connection);
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
				zharray.addStruct(zhCD);
			}
			bodyCD.addArray("ACCT_INFO_ARRAY", zharray);
		}
		
		EMPLog.log("inReport", EMPLog.INFO, 0, "***********************************信贷与核算交易*************************************");
		EMPLog.log("inReport", EMPLog.INFO, 0, "*-----------------------------贷款承诺发放授权交易("+service_code+")-------------------------------*");
		EMPLog.log("inReport", EMPLog.INFO, 0, "*************************************************************************************");
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------请求报文start-------------------");
		EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(reqCD), "UTF-8"));
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------请求报文end---------------------------------------");
		
		
		/** 发送报文请求，开始与ESB建立连接 */
		CompositeData respCD = ESBClient.request(reqCD);
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------响应报文start-------------------------------------");
		EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(respCD), "UTF-8"));
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------响应报文end---------------------------------------");
		return esbInterfacesImple.getRespSysHeadCD(respCD);
	}

	/**
	 * 银承出账授权
	 * @param authKColl 授权表IndexedCollection
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 结果KeyedCollection
	 */
	public KeyedCollection sendEsb4Ycffsq(IndexedCollection pvpIColl, String service_code, String sence, String serno, TableModelDAO dao, Context context, Connection connection) throws Exception {
		ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData();
		reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(service_code, TradeConstance.SERVICE_SCENE_YCFFSQ, TradeConstance.CONSUMERID, TagUtil.getSysHeadSerno(serno, context),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
		/** 应用头 */
		reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
				(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
		
		/** 封装写入文件的报文信息，包括整体报文信息 */
		CompositeData fileCD = new CompositeData();
		fileCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(service_code, TradeConstance.SERVICE_SCENE_YCFFSQ, TradeConstance.CONSUMERID, TagUtil.getSysHeadSerno(serno, context),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
		/** 应用头 */
		fileCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
				(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
		
		
		CompositeData bodyListCD = new CompositeData();
		Array bodyArr = new Array();
		for(int i=0;i<pvpIColl.size();i++){
			KeyedCollection authKColl = (KeyedCollection)pvpIColl.get(i);
			KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl);
		
			CompositeData bodyArrCD = new CompositeData();
			
			bodyArrCD.addField("GEN_GL_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("GEN_GL_NO")), FieldType.FIELD_STRING, 30, 0));
			bodyArrCD.addField("BANK_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BANK_ID"), FieldType.FIELD_STRING, 30, 0));
			bodyArrCD.addField("BRANCH_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
			bodyArrCD.addField("LOAN_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_TYPE"), FieldType.FIELD_STRING, 20, 0));
			bodyArrCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 50, 0));
			bodyArrCD.addField("ACCEPT_AGREE_NO", TagUtil.getEMPField(reflectKColl.getDataValue("ACCEPT_AGREE_NO"), FieldType.FIELD_STRING, 30, 0));
			//bodyCD.addField("BILL_ISSUE_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("BILL_ISSUE_DATE"), FieldType.FIELD_STRING, 10, 0));
			//bodyCD.addField("EXPIRY_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("EXPIRY_DATE"), FieldType.FIELD_STRING, 8, 0));
			bodyArrCD.addField("CCY", TagUtil.getEMPField(reflectKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 3, 0));
			bodyArrCD.addField("FACE_AMT", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("FACE_AMT")), FieldType.FIELD_DOUBLE, 16, 2));
			bodyArrCD.addField("COMMISSION", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("COMMISSION")), FieldType.FIELD_DOUBLE, 20, 2));
			bodyArrCD.addField("GLOBAL_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("GLOBAL_TYPE"), FieldType.FIELD_STRING, 5, 0));
			bodyArrCD.addField("GLOBAL_ID", TagUtil.getEMPField(reflectKColl.getDataValue("GLOBAL_ID"), FieldType.FIELD_STRING, 30, 0));
			bodyArrCD.addField("ISS_CTRY", TagUtil.getEMPField(reflectKColl.getDataValue("ISS_CTRY"), FieldType.FIELD_STRING, 10, 0));
			bodyArrCD.addField("CLIENT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CLIENT_NO"), FieldType.FIELD_STRING, 20, 0));
			bodyArrCD.addField("BILL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("BILL_NO"), FieldType.FIELD_STRING, 50, 0));
			bodyArrCD.addField("DRAWER_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("DRAWER_NAME"), FieldType.FIELD_STRING, 150, 0));
			bodyArrCD.addField("OD_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("OD_INT_RATE")), FieldType.FIELD_STRING, 20, 7));
			bodyArrCD.addField("TERM", TagUtil.getEMPField(TagUtil.replaceNull4Int(reflectKColl.getDataValue("TERM")), FieldType.FIELD_STRING, 5, 0));
			bodyArrCD.addField("TERM_TYPE", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("TERM_TYPE")), FieldType.FIELD_STRING, 5, 0));
			bodyArr.addStruct(bodyArrCD);
		}
		bodyListCD.addArray("BASE_BODY", bodyArr);
		fileCD.addStruct("BODY", bodyListCD);
		
		/** 保证金信息 */
		IndexedCollection bzIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+serno+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_BZ+"' ", connection);
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
				bzCD.addField("GUARANTEE_PER", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_PER"), FieldType.FIELD_STRING, 30, 0));
				bzCD.addField("GUARANTEE_AMT", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_AMT"), FieldType.FIELD_STRING, 30, 0));
				bzarray.addStruct(bzCD);
			}
			bodyListCD.addArray("GUARANTEE_AUTH_INFO_ARRAY", bzarray);
		}
		/** 账号信息 */
		IndexedCollection zhIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+serno+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_ZH+"' ", connection);
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
				zharray.addStruct(zhCD);
			}
			bodyListCD.addArray("ACCT_INFO_ARRAY", zharray);
		}
		
		EMPLog.log("inReport", EMPLog.INFO, 0, "***********************************信贷与核算交易*************************************");
		EMPLog.log("inReport", EMPLog.INFO, 0, "*-----------------------------银承出账授权交易("+service_code+")-------------------------------*");
		EMPLog.log("inReport", EMPLog.INFO, 0, "*************************************************************************************");
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------请求报文start-------------------");
		EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(reqCD), "UTF-8"));
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------请求报文end---------------------------------------");
		/** 封装报文体 */
		CompositeData sendCD= new CompositeData();
		sendCD.addField("FILENAME",TagUtil.getEMPField(FTPUtil.getFileName(service_code, TradeConstance.SERVICE_SCENE_YCFFSQ, serno), FieldType.FIELD_STRING, 100, 0));
		sendCD.addField("FILE_PATH",TagUtil.getEMPField(FTPUtil.getFilePath(), FieldType.FIELD_STRING, 100, 0));
		FTPUtil.send2FTP(service_code, TradeConstance.SERVICE_SCENE_YCFFSQ, serno, fileCD);
		reqCD.addStruct(TradeConstance.ESB_BODY, sendCD);
		/** 发送报文请求，开始与ESB建立连接 */
		CompositeData respCD = ESBClient.request(reqCD);
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------响应报文start-------------------------------------");
		EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(respCD), "UTF-8"));
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------响应报文end---------------------------------------");
		return esbInterfacesImple.getRespSysHeadCD(respCD);
	}
	
	/**
	 * 贴现/转贴现买入卖出/内部转贴现/再贴现/回购/返售授权
	 * @param pvpIColl 授权表IndexedCollection
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 结果KeyedCollection
	 */
	public KeyedCollection sendEsb4Txffsq(IndexedCollection pvpIColl, String service_code, String sence, String serno, TableModelDAO dao, Context context, Connection connection) throws Exception {
		
		ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData();
		reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(service_code, TradeConstance.SERVICE_SCENE_TXFFSQ, TradeConstance.CONSUMERID, TagUtil.getSysHeadSerno(serno, context),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
		/** 应用头 */
		reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
				(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
		
		/** 封装写入文件的报文信息，包括整体报文信息 */
		CompositeData bodyCD = new CompositeData();
		CompositeData bodyListCD = new CompositeData();
		Array bodyArr = new Array();
		for(int i=0;i<pvpIColl.size();i++){
			KeyedCollection authKColl = (KeyedCollection)pvpIColl.get(i);
			KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl);
		
			CompositeData bodyArrCD = new CompositeData();
			bodyArrCD.addField("GEN_GL_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("GEN_GL_NO")), FieldType.FIELD_STRING, 30, 0));
			bodyArrCD.addField("OPERATION_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("OPERATION_TYPE"), FieldType.FIELD_STRING, 10, 0));
			bodyArrCD.addField("BACK_FLAG", TagUtil.getEMPField(reflectKColl.getDataValue("BACK_FLAG"), FieldType.FIELD_STRING, 10, 0));
			bodyArrCD.addField("INPUT_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("INPUT_DATE"), FieldType.FIELD_STRING, 8, 0));
			bodyArrCD.addField("BANK_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BANK_ID"), FieldType.FIELD_STRING, 30, 0));
			bodyArrCD.addField("DISCONT_AGREE_NO", TagUtil.getEMPField(reflectKColl.getDataValue("DISCONT_AGREE_NO"), FieldType.FIELD_STRING, 30, 0));
			bodyArrCD.addField("LOAN_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_TYPE"), FieldType.FIELD_STRING, 20, 0));
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
			bodyArrCD.addField("DISCOUNT_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("DISCOUNT_DATE"), FieldType.FIELD_STRING, 8, 0));
			bodyArrCD.addField("BILL_ISSUE_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("BILL_ISSUE_DATE"), FieldType.FIELD_STRING, 8, 0));
			bodyArrCD.addField("BILL_EXPIRY_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("BILL_EXPIRY_DATE"), FieldType.FIELD_STRING, 8, 0));
			bodyArrCD.addField("RETURN_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("RETURN_DATE"), FieldType.FIELD_STRING, 8, 0));
			bodyArrCD.addField("EXPIRY_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("EXPIRY_DATE"), FieldType.FIELD_STRING, 8, 0));
			bodyArrCD.addField("DEDUCT_METHOD", TagUtil.getEMPField(reflectKColl.getDataValue("DEDUCT_METHOD"), FieldType.FIELD_STRING, 10, 0));
			bodyArrCD.addField("TO_BRANCH_ID", TagUtil.getEMPField(reflectKColl.getDataValue("TO_BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
			bodyArrCD.addField("PAY_INT_ACCT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("PAY_INT_ACCT_NO"), FieldType.FIELD_STRING, 50, 0));
			bodyArrCD.addField("PAY_INT_ACCT_NO1", TagUtil.getEMPField(reflectKColl.getDataValue("PAY_INT_ACCT_NO1"), FieldType.FIELD_STRING, 51, 0));
			bodyArrCD.addField("PAY_INT_ACCT_NO2", TagUtil.getEMPField(reflectKColl.getDataValue("PAY_INT_ACCT_NO2"), FieldType.FIELD_STRING, 52, 0));
			bodyArrCD.addField("PAY_INT_ACCT_NO3", TagUtil.getEMPField(reflectKColl.getDataValue("PAY_INT_ACCT_NO3"), FieldType.FIELD_STRING, 53, 0));
			bodyArr.addStruct(bodyArrCD);
		}
		bodyListCD.addArray("BASE_BODY", bodyArr);
		bodyCD.addStruct("BODY", bodyListCD);
		//reqCD.addStruct("BODY", bodyListCD);
		/** 保证金信息 */
//		IndexedCollection bzIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_BZ+"' ", connection);
//		if(bzIColl != null && bzIColl.size() > 0){
//			Array bzarray = new Array(); 
//			for(int i=0;i<bzIColl.size();i++){
//				CompositeData bzCD = new CompositeData();
//				KeyedCollection bzKColl = (KeyedCollection)bzIColl.get(i);
//				KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(bzKColl);
//				bzCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
//				bzCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 50, 0));
//				bzCD.addField("BRANCH_ID", TagUtil.getEMPField(reflectSubKColl.getDataValue("BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
//				bzCD.addField("GUARANTEE_ACCT_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_ACCT_NO"), FieldType.FIELD_STRING, 50, 0));
//				bzCD.addField("GUARANTEE_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_NO"), FieldType.FIELD_STRING, 50, 0));
//				bzCD.addField("ACCT_TYPE", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_TYPE"), FieldType.FIELD_STRING, 6, 0));
//				bzCD.addField("GUARANTEE_EXPIRY_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_EXPIRY_DATE"), FieldType.FIELD_STRING, 8, 0));
//				bzCD.addField("ACCT_CODE", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_CODE"), FieldType.FIELD_STRING, 10, 0));
//				bzCD.addField("CA_TT_FLAG", TagUtil.getEMPField(reflectSubKColl.getDataValue("CA_TT_FLAG"), FieldType.FIELD_STRING, 2, 0));
//				bzCD.addField("ACCT_GL_CODE", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_GL_CODE"), FieldType.FIELD_STRING, 10, 0));
//				bzCD.addField("ACCT_NAME", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_NAME"), FieldType.FIELD_STRING, 150, 0));
//				bzCD.addField("CCY", TagUtil.getEMPField(reflectSubKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 30, 0));
//				bzCD.addField("GUARANTEE_PER", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_PER"), FieldType.FIELD_STRING, 30, 0));
//				bzCD.addField("GUARANTEE_AMT", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_AMT"), FieldType.FIELD_STRING, 30, 0));
//				bzarray.addStruct(bzCD);
//			}
//			bodyCD.addArray("GUARANTEE_AUTH_INFO_ARRAY", bzarray);
//		}
		/** 账号信息 */
		IndexedCollection zhIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+serno+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_ZH+"' ", connection);
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
				zharray.addStruct(zhCD);
			}
			bodyListCD.addArray("ACCT_INFO_ARRAY", zharray);
		}
		
		/** 封装报文体 */
		CompositeData sendCD= new CompositeData();
		sendCD.addField("FILENAME",TagUtil.getEMPField(FTPUtil.getFileName(service_code, TradeConstance.SERVICE_SCENE_TXFFSQ, serno), FieldType.FIELD_STRING, 100, 0));
		sendCD.addField("FILE_PATH",TagUtil.getEMPField(FTPUtil.getFilePath(), FieldType.FIELD_STRING, 100, 0));
		FTPUtil.send2FTP(service_code, TradeConstance.SERVICE_SCENE_TXFFSQ, serno, bodyCD);
		reqCD.addStruct("BODY", sendCD);
		
		EMPLog.log("inReport", EMPLog.INFO, 0, "***********************************信贷与核算交易*************************************");
		EMPLog.log("inReport", EMPLog.INFO, 0, "*-----------------------------贷款发放授权交易("+service_code+")-------------------------------*");
		EMPLog.log("inReport", EMPLog.INFO, 0, "*************************************************************************************");
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------请求报文start-------------------");
		EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(reqCD), "UTF-8"));
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------请求报文end---------------------------------------");
		
		/** 发送报文请求，开始与ESB建立连接 */
		CompositeData respCD = ESBClient.request(reqCD);
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------响应报文start-------------------------------------");
		EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(respCD), "UTF-8"));
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------响应报文end---------------------------------------");
		return esbInterfacesImple.getRespSysHeadCD(respCD);
	}
	
	
	/**还款方案试算*/
	public KeyedCollection createRepayPlan(KeyedCollection iqpKColl, TableModelDAO dao, Context context, Connection connection,PageInfo pageInfo) throws Exception {
		String service_code = TradeConstance.SERVICE_CODE_DKFKSQ;
		String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context); 
		
		ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData(); 
		/** 系统头 */
		reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(service_code, TradeConstance.SERVICE_SCENE_DKFKSQ, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
		/** 应用头 */
		reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
				(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
		
		//设置分页信息
		CompositeData appHead_struct = reqCD.getStruct("APP_HEAD");
		Field totalNum = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		totalNum.setValue(pageInfo.pageSize+"");
		appHead_struct.addField("TOTAL_NUM", totalNum);
		
		Field currentNum = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
		//计算当前页起始记录数
		int beginIdx = 1;
		if(pageInfo.pageIdx==1){
			beginIdx = 1;
		}else{
			beginIdx = pageInfo.pageSize*(pageInfo.pageIdx-1)+1;
			if(beginIdx<=0){
				beginIdx = 1;
			}
		}
		currentNum.setValue(beginIdx+"");
		appHead_struct.addField("CURRENT_NUM", currentNum);
		
		/** 封装报文体 */ 
		CompositeData bodyCD = new CompositeData();
		bodyCD.addField("APPLY_AMOUNT", TagUtil.getEMPField(iqpKColl.getDataValue("apply_amount"), FieldType.FIELD_DOUBLE, 20, 2));
		bodyCD.addField("DEDUCT_DAYS", TagUtil.getEMPField(iqpKColl.getDataValue("repay_date"), FieldType.FIELD_STRING, 2, 0));
		bodyCD.addField("DRAW_DOWN_DATE", TagUtil.getEMPField(TagUtil.formatDate(iqpKColl.getDataValue("apply_date")), FieldType.FIELD_STRING, 8, 0));
		bodyCD.addField("CONTRACT_EXPIRY_DATE", TagUtil.getEMPField(TagUtil.formatDate(iqpKColl.getDataValue("CONTRACT_EXPIRY_DATE")), FieldType.FIELD_STRING, 8, 0));
		bodyCD.addField("LOAN_TYPE", TagUtil.getEMPField(iqpKColl.getDataValue("loan_type"), FieldType.FIELD_STRING, 20, 0));
		bodyCD.addField("VALUE_DATE", TagUtil.getEMPField(TagUtil.formatDate(iqpKColl.getDataValue("apply_date")), FieldType.FIELD_STRING, 8, 0));
		bodyCD.addField("ACT_INT_RATE", TagUtil.getEMPField(iqpKColl.getDataValue("reality_ir_y"), FieldType.FIELD_DOUBLE, 20, 9));
		bodyCD.addField("REPAY_FREQUENCY_UNIT", TagUtil.getEMPField(iqpKColl.getDataValue("repay_term"), FieldType.FIELD_STRING, 2, 0));
		bodyCD.addField("REPAY_FREQUENCY", TagUtil.getEMPField(iqpKColl.getDataValue("repay_space"), FieldType.FIELD_STRING, 2, 0));
		bodyCD.addField("LOAN_REPAY_METHOD", TagUtil.getEMPField(iqpKColl.getDataValue("repay_type"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("LOAN_REPAY_TYPE", TagUtil.getEMPField(iqpKColl.getDataValue("repay_mode_type"), FieldType.FIELD_STRING, 10, 0));
		bodyCD.addField("TERM_PAY_FLAG", TagUtil.getEMPField(iqpKColl.getDataValue("is_term"), FieldType.FIELD_STRING, 1, 0));
		reqCD.addStruct("BODY", bodyCD);
		
		EMPLog.log("inReport", EMPLog.INFO, 0, "***********************************信贷与核算交易*************************************");
		EMPLog.log("inReport", EMPLog.INFO, 0, "*-----------------------------还款方案试算交易("+service_code+")-------------------------------*");
		EMPLog.log("inReport", EMPLog.INFO, 0, "*************************************************************************************");
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------请求报文start-------------------");
		EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(reqCD), "UTF-8"));
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------请求报文end---------------------------------------");
		
		
		/** 发送报文请求，开始与ESB建立连接 */
		CompositeData respCD = ESBClient.request(reqCD);
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------响应报文start-------------------------------------");
		EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(respCD), "UTF-8"));
		EMPLog.log("inReport", EMPLog.INFO, 0, "-----------------------------------------响应报文end---------------------------------------");
		
		return TagUtil.replaceCD2KColl(respCD);
	}
}
