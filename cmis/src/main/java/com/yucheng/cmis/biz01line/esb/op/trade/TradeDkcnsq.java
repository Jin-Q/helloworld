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
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
/**
 * 贷款承诺授权交易
 * @author liqh
 *
 */
public class TradeDkcnsq extends TranClient {
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
			String serno = (String)authKColl.getDataValue("serno");
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			/** 系统头 */
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, TradeConstance.SERVICE_SCENE_DKCNSQ, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			
			/** 封装报文体 */
			KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl);
			
//			/** 核算与信贷业务品种映射 START */
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//			ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
//			String lmPrdId = service.getPrdBasicCLPM2LM(reflectKColl.getDataValue("DUEBILL_NO").toString(), authKColl.getDataValue("prd_id").toString(), context, connection);
//			/** 核算与信贷业务品种映射 END */
			
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
			bodyCD.addField("CLIENT_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("CLIENT_NAME"), FieldType.FIELD_STRING, 60, 0));
			bodyCD.addField("TERM", TagUtil.getEMPField(reflectKColl.getDataValue("TERM"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("TERM_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("TERM_TYPE"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("OPEN_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("OPEN_DATE"), FieldType.FIELD_STRING, 8, 0));
			bodyCD.addField("EXPIRY_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("EXPIRY_DATE"), FieldType.FIELD_STRING, 8, 0));
			bodyCD.addField("LOAN_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_TYPE"), FieldType.FIELD_STRING, 20, 0));
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
					/* modify by liqh 2013/11/27  账号信息增加银行名称      START*/
					zhCD.addField("BANK_NAME", TagUtil.getEMPField(reflectSubKColl.getDataValue("BANK_NAME"), FieldType.FIELD_STRING, 100, 0));
					/* modify by liqh 2013/11/27  账号信息增加银行名称      END*/
					zhCD.addField("AGREE_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 30, 0));
					zhCD.addField("OWN_BRANCH_FLAG", TagUtil.getEMPField(reflectSubKColl.getDataValue("OWN_BRANCH_FLAG"), FieldType.FIELD_STRING, 1, 0));
					zhCD.addField("ACCT_BANK_ADD", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_BANK_ADD"), FieldType.FIELD_STRING, 100, 0));
					
					zharray.addStruct(zhCD);
				}
				bodyCD.addArray("ACCT_INFO_ARRAY", zharray);
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
				bodyCD.addArray("FEE_AUTH_INFO_ARRAY", feeArray);
			}
		}catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		return reqCD;
	}
	
	//XD150520037_信贷系统利率调整修改优化
	public void doSuccess(Context context, Connection connection) throws EMPException{
		
	}
}
