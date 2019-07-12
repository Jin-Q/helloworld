package com.yucheng.cmis.biz01line.esb.op.trade;

import java.math.BigDecimal;
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
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.FTPUtil;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 资产转让授权交易
 * @author liqh
 *
 */
public class TradeZczrsq extends TranClient {
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

			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			
			/** 通过交易码判断所需执行的交易，以及需要准备的交易数据 */
			KeyedCollection authKColl = dao.queryDetail(AUTHMODEL, tran_serno, connection);
			String authorize_no = (String)authKColl.getDataValue("authorize_no");
			
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, senceCode, TradeConstance.CONSUMERID, TagUtil.getSysHeadSerno(authorize_no, context),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			
			/** 封装写入文件的报文信息，包括整体报文信息 */
			CompositeData fileCD = new CompositeData();
			fileCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, senceCode, TradeConstance.CONSUMERID, TagUtil.getSysHeadSerno(authorize_no, context),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			
			/** 封装写入文件的报文信息，包括整体报文信息 */
			CompositeData bodyCD = new CompositeData();
			CompositeData bodyListCD = new CompositeData();
			
			
			IndexedCollection pvpIColl = dao.queryList(AUTHMODEL, " where authorize_no = '" + authorize_no + "' and status in('00','01')", connection);
			if(pvpIColl != null && pvpIColl.size() > 0){
				Array bodyArr = new Array();
				KeyedCollection authKColl1 = (KeyedCollection)pvpIColl.get(0);
				KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl1);
				
				/** 核算与信贷业务品种映射 START */
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
				String lmPrdId = service.getPrdBasicCLPM2LM(authKColl1.getDataValue("cont_no").toString(), authKColl1.getDataValue("prd_id").toString(), context, connection);
				/** 核算与信贷业务品种映射 END */
				
				bodyCD.addField("GEN_GL_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("GEN_GL_NO")), FieldType.FIELD_STRING, 30, 0));
				bodyCD.addField("BANK_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BANK_ID"), FieldType.FIELD_STRING, 30, 0));
				bodyCD.addField("ORG_NO", TagUtil.getEMPField(reflectKColl.getDataValue("ORG_NO"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("ASSET_NO", TagUtil.getEMPField(reflectKColl.getDataValue("ASSET_NO"), FieldType.FIELD_STRING, 30, 0));
				bodyCD.addField("LOAN_TYP", TagUtil.getEMPField(lmPrdId, FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("TRANSFER_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("TRANSFER_MODE"), FieldType.FIELD_STRING, 2, 0));
				bodyCD.addField("COUNTER_BRANCH_ID", TagUtil.getEMPField(reflectKColl.getDataValue("COUNTER_BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("COUNTER_BRANCH_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("COUNTER_BRANCH_NAME"), FieldType.FIELD_STRING, 150, 0));
				bodyCD.addField("COUNTER_ACCT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("COUNTER_ACCT_NO"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("COUNTER_ACCT_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("COUNTER_ACCT_NAME"), FieldType.FIELD_STRING, 150, 0));
				bodyCD.addField("COUNTER_OPEN_BRANCH_ID", TagUtil.getEMPField(reflectKColl.getDataValue("COUNTER_OPEN_BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("COUNTER_OPEN_BRANCH_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("COUNTER_OPEN_BRANCH_NAME"), FieldType.FIELD_STRING, 150, 0));
				bodyCD.addField("ACCT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("ACCT_NO"), FieldType.FIELD_STRING, 50, 0));
				bodyCD.addField("ACCT_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("ACCT_NAME"), FieldType.FIELD_STRING, 150, 0));
				bodyCD.addField("OPEN_BRANCH_ID", TagUtil.getEMPField(reflectKColl.getDataValue("OPEN_BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("OPEN_BRANCH_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("OPEN_BRANCH_NAME"), FieldType.FIELD_STRING, 150, 0));
				bodyCD.addField("CCY_CDE", TagUtil.getEMPField(reflectKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 3, 0));				
				bodyCD.addField("ASSET_TOTAL_AMT", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("ASSET_TOTAL_AMT")), FieldType.FIELD_DOUBLE, 20, 2));
				bodyCD.addField("TX_AMT", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("TRANSFER_TOTAL_AMT")), FieldType.FIELD_DOUBLE, 20, 2));
				bodyCD.addField("TRANSFER_TOTAL_INT", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("TRANSFER_TOTAL_INT")), FieldType.FIELD_DOUBLE, 20, 2));
				bodyCD.addField("CHARGE_INTEREST_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("CHARGE_INTEREST_MODE"), FieldType.FIELD_STRING, 8, 0));
				bodyCD.addField("TX_DT", TagUtil.getEMPField(reflectKColl.getDataValue("TRANSFER_DATE"), FieldType.FIELD_STRING, 8, 0));
				bodyCD.addField("CONSIGN_FEE_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("CONSIGN_FEE_RATE")), FieldType.FIELD_STRING, 20, 7));
				bodyCD.addField("TRANSFER_CNT", TagUtil.getEMPField(TagUtil.replaceNull4Int(reflectKColl.getDataValue("TRANSFER_CNT")), FieldType.FIELD_INT, 10, 0));
				bodyCD.addField("RISK_TRANSFER_FLAG", TagUtil.getEMPField(reflectKColl.getDataValue("RISK_TRANSFER_FLAG"), FieldType.FIELD_STRING, 1, 0));
				bodyCD.addField("LOAN_AMT_TOTL", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("LOAN_AMT_TOTL")), FieldType.FIELD_DOUBLE, 20, 2));
				bodyCD.addField("LOAN_BALANCE_TOTL", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("LOAN_BALANCE_TOTL")), FieldType.FIELD_DOUBLE, 20, 2));
				bodyCD.addField("TRANS_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("TRANS_RATE")), FieldType.FIELD_DOUBLE, 20, 2));
				bodyCD.addField("TRANS_ASSET_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("TRANS_ASSET_TYPE"), FieldType.FIELD_STRING, 1, 0));
				bodyArr.addStruct(bodyCD);
				bodyListCD.addArray("BASE_BODY", bodyArr);
				fileCD.addStruct("BODY", bodyListCD);
				
				//判断资产流转或者资产转让
				String prd_id = (String) authKColl1.getDataValue("prd_id");
				String cont_no = (String) authKColl1.getDataValue("cont_no");
				if(prd_id.equals("600020")){//资产转让业务
					/** 资产清单数据*/
					String asset_no = (String) reflectKColl.getDataValue("ASSET_NO");
					String transfer_mode = (String) reflectKColl.getDataValue("TRANSFER_MODE");
					String charge_interest_mode = (String) reflectKColl.getDataValue("CHARGE_INTEREST_MODE");//收息方式
					String condition = " where asset_no = '" + asset_no + "'";
					IndexedCollection relIColl = dao.queryList("IqpAssetRel", condition, connection);
					
					/**封装借据信息  借据信息直接从清单表取*/
					Array duebillInfoArray = new Array(); 
					for(int i=0;i<relIColl.size();i++){
						CompositeData duebillInfoCD = new CompositeData();
						KeyedCollection duebillInfoKColl = (KeyedCollection)relIColl.get(i);
						
						/** 资产清单里产品类型   核算与信贷业务品种映射 START */
						String ori_bill_no = (String) duebillInfoKColl.getDataValue("bill_no");
						String ori_lmPrdId = service.getPrdBasicAssetstrsf2LM(asset_no, ori_bill_no, context, connection);
						/** 资产清单里产品类型  核算与信贷业务品种映射 END */
						
						String acccondition = " where ori_bill_no = '"+ori_bill_no+"'";
						KeyedCollection accAssetstrsfkc = dao.queryFirst("AccAssetstrsf", null, acccondition, connection);
						
						String ori_fina_br_id = (String) duebillInfoKColl.getDataValue("fina_br_id");
						
						duebillInfoCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("APPL_SEQ", TagUtil.getEMPField(authKColl1.getDataValue("serno"), FieldType.FIELD_STRING, 32, 0));
						duebillInfoCD.addField("INPUT_DT", TagUtil.getEMPField(TagUtil.formatDate(duebillInfoKColl.getDataValue("loan_start_date")), FieldType.FIELD_STRING, 8, 0));
						duebillInfoCD.addField("CONT_NO", TagUtil.getEMPField(authKColl1.getDataValue("cont_no"), FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("BCH_CDE", TagUtil.getEMPField(ori_fina_br_id, FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("LOAN_NO", TagUtil.getEMPField(accAssetstrsfkc.getDataValue("bill_no"), FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("BANK_CDE", TagUtil.getEMPField(TradeConstance.BANK_ID, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("CUST_ID", TagUtil.getEMPField(duebillInfoKColl.getDataValue("cus_id"), FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("CUST_NAME", TagUtil.getEMPField(duebillInfoKColl.getDataValue("cus_name"), FieldType.FIELD_STRING, 60, 0));
						duebillInfoCD.addField("ID_TYPE", TagUtil.getEMPField("", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("ID_NO", TagUtil.getEMPField("", FieldType.FIELD_STRING, 40, 0));
						duebillInfoCD.addField("ISS_CTRY", TagUtil.getEMPField("", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("DEALER_CDE", TagUtil.getEMPField("", FieldType.FIELD_STRING, 20, 0));
						duebillInfoCD.addField("LOAN_CCY", TagUtil.getEMPField(authKColl1.getDataValue("cur_type"), FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("ORIG_PRCP", TagUtil.getEMPField(duebillInfoKColl.getDataValue("takeover_amt"), FieldType.FIELD_DOUBLE, 16, 2));//传转让本金
						duebillInfoCD.addField("LOAN_ACTV_DT", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("TRANSFER_DATE")), FieldType.FIELD_STRING, 8, 0));//传转让日期
						
						//卖出代保管，为委托贷款形式，需传WT类型
						if(transfer_mode.equals("01")&&charge_interest_mode.equals("2")){
							duebillInfoCD.addField("LOAN_TYP", TagUtil.getEMPField(lmPrdId, FieldType.FIELD_STRING, 10, 0));
						}else{
							duebillInfoCD.addField("LOAN_TYP", TagUtil.getEMPField(ori_lmPrdId, FieldType.FIELD_STRING, 10, 0));
						}
						
						duebillInfoCD.addField("LAST_DUE_DT", TagUtil.getEMPField(duebillInfoKColl.getDataValue("loan_end_date"), FieldType.FIELD_STRING, 8, 0));
						duebillInfoCD.addField("INT_START_DT", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("TRANSFER_DATE")), FieldType.FIELD_STRING, 8, 0));//传转让日期
						String latest_repay = (String) duebillInfoKColl.getDataValue("latest_repay");
						if(latest_repay!=null&&!"".equals(latest_repay)){
							duebillInfoCD.addField("DUE_DAY", TagUtil.getEMPField(latest_repay.substring(latest_repay.length()-2,latest_repay.length()), FieldType.FIELD_STRING, 2, 0));
						}else{
							duebillInfoCD.addField("DUE_DAY", TagUtil.getEMPField("", FieldType.FIELD_STRING, 2, 0));
						}
						String ir_accord_type = TagUtil.replaceNull4String(duebillInfoKColl.getDataValue("ir_accord_type"));
						String ir_adjust_type = TagUtil.replaceNull4String(duebillInfoKColl.getDataValue("ir_adjust_type"));
						if(ir_accord_type == null || ir_accord_type.equals("") || ir_accord_type.equals("03")){
							ir_accord_type = "";
						}else if(ir_accord_type.equals("01")){
							//议价利率依据
							ir_accord_type = "FX";//FX: 固定利率
						}else{
							//牌告利率依据、正常利率上浮动
							if(ir_adjust_type == null || ir_adjust_type.equals("")){//固定不变
								ir_accord_type = "FX";//FX:固定利率
							}else{
								ir_accord_type = "RV";//RV: 浮动利率
							}
						}
						duebillInfoCD.addField("LOAN_RATE_MODE", TagUtil.getEMPField(ir_accord_type, FieldType.FIELD_STRING, 2, 0));
						duebillInfoCD.addField("RATE_BASE", TagUtil.getEMPField("Y", FieldType.FIELD_STRING, 1, 0));
						
						//获取基准利率代码  start
//						IqpLoanAppComponent cmisComponent = (IqpLoanAppComponent) CMISComponentFactory
//								.getComponentFactoryInstance().getComponentInstance(AppConstant.IQPLOANAPPCOMPONENT, context,connection);
//						String currType = (String) authKColl1.getDataValue("cur_type");
//						String termType = (String) authKColl1.getDataValue("cur_type");
//						KeyedCollection ic = cmisComponent.getRate("9999",currType,termType,term,context,connection);
						//获取基准利率代码  end
						
						duebillInfoCD.addField("LOAN_RATE_TYP", TagUtil.getEMPField("", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_BASE_RATE", TagUtil.getEMPField(duebillInfoKColl.getDataValue("ruling_ir"), FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("INT_ADJ_PCT", TagUtil.getEMPField(duebillInfoKColl.getDataValue("ir_float_rate"), FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_SPRD", TagUtil.getEMPField(duebillInfoKColl.getDataValue("ir_float_point"), FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_INT_RATE", TagUtil.getEMPField(duebillInfoKColl.getDataValue("reality_ir_y"), FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("OD_RATE_BASE", TagUtil.getEMPField("Y", FieldType.FIELD_STRING, 1, 0));
						duebillInfoCD.addField("LOAN_OD_RATE_TYP", TagUtil.getEMPField("", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_OD_BASE_RATE", TagUtil.getEMPField("", FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("INT_OD_ADJ_PCT", TagUtil.getEMPField("", FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_OD_INT_RATE", TagUtil.getEMPField(duebillInfoKColl.getDataValue("overdue_rate_y"), FieldType.FIELD_DOUBLE, 16, 9));
						String repay_type = TagUtil.replaceNull4String(duebillInfoKColl.getDataValue("repay_type"));//还款方式
						
						//垫款没有还款方式，默认为利随本清  add by zhaozq 20141202 start
						//先判断转让台账是否来自垫款台账
						BigDecimal pad_count = (BigDecimal)SqlClient.queryFirst("queryLoanBillFromAccPad", ori_bill_no, null, connection);
						int pad = Integer.parseInt(pad_count.toString());
						if(pad!=0){//来自垫款
							repay_type = "A005";
						}
						//垫款没有还款方式，默认为利随本清  add by zhaozq 20141202 end
						
						String repay_mode_type = "";//还款方式种类
						if(repay_type!=null && !"".equals(repay_type)){
							KeyedCollection prdRepayModeKColl = dao.queryDetail("PrdRepayMode", repay_type, connection);
							repay_mode_type = TagUtil.replaceNull4String(prdRepayModeKColl.getDataValue("repay_mode_type"));
						}
						String repay_term = "";
						String repay_space = "";
						if(repay_type.equals("A005")){//利随本清传1月
							repay_term = "M";//还款间隔单位
							repay_space = "1";//还款间隔
						}else{
							repay_term = TagUtil.replaceNull4String(duebillInfoKColl.getDataValue("repay_term"));//还款间隔单位
							repay_space = TagUtil.replaceNull4String(duebillInfoKColl.getDataValue("repay_space"));//还款间隔
						}
						duebillInfoCD.addField("PAYM_FREQ_UNIT", TagUtil.getEMPField(repay_term, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("PAYM_FREQ_FREQ", TagUtil.getEMPField(repay_space, FieldType.FIELD_STRING, 2, 0));
						duebillInfoCD.addField("LOAN_PAYM_MTD", TagUtil.getEMPField(repay_type, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_PAYM_TYP", TagUtil.getEMPField(repay_mode_type, FieldType.FIELD_STRING, 10, 0));
						
						/*
						 * 目前调整为所有资产买入业务类型均传NLOAN（普通贷款）
						 * */
						if(transfer_mode.equals("03")){
							duebillInfoCD.addField("BUSS_TYP", TagUtil.getEMPField("NLOAN", FieldType.FIELD_STRING, 10, 0));
						}else{
							duebillInfoCD.addField("BUSS_TYP", TagUtil.getEMPField("TLOAN", FieldType.FIELD_STRING, 10, 0));
						}
						
						duebillInfoCD.addField("LOAN_GRACE_TYP", TagUtil.getEMPField("P", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_OD_GRACE", TagUtil.getEMPField("0", FieldType.FIELD_DOUBLE, 3, 0));
						duebillInfoCD.addField("LOAN_REPAY_MTHD", TagUtil.getEMPField("AUTOPAY", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_FIX_OD_INT_IND", TagUtil.getEMPField("N", FieldType.FIELD_STRING, 1, 0));
						duebillInfoCD.addField("LOAN_OD_CAT", TagUtil.getEMPField("L", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_OD_RATE_ADJ_PCT", TagUtil.getEMPField(duebillInfoKColl.getDataValue("default_rate"), FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_OD_COMM_PART", TagUtil.getEMPField("", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_OD_CPD_IND", TagUtil.getEMPField("", FieldType.FIELD_STRING, 1, 0));
						String date = (String) duebillInfoKColl.getDataValue("loan_start_date");
						String ir_next_adjust_term = "";//下一次利率调整间隔
						String ir_next_adjust_unit = "";//下一次利率调整间隔单位
						String fir_adjust_day = "";//第一次调整日
						//固定不变
						if(ir_adjust_type.equals("0")){
							ir_adjust_type = "";
						}
						//按月调整
						else if(ir_adjust_type.equals("1")){
							ir_adjust_type = "FIX";
							ir_next_adjust_term = "1";
							ir_next_adjust_unit = "M";
							fir_adjust_day = DateUtils.getNextDate("M", date);
						}
						//按季调整
						else if(ir_adjust_type.equals("2")){
							ir_adjust_type = "FIX";
							ir_next_adjust_term = "1";
							ir_next_adjust_unit = "Q";
							fir_adjust_day = DateUtils.getNextDate("Q", date);
						}
						//按年调整
						else if(ir_adjust_type.equals("3")){
							ir_adjust_type = "FIX";
							ir_next_adjust_term = "1";
							ir_next_adjust_unit = "Y";
							fir_adjust_day = DateUtils.getNextDate("Y", date);
						}
						duebillInfoCD.addField("NEXT_REPC_OPT", TagUtil.getEMPField(ir_adjust_type, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("NEXT_REPC_NUM", TagUtil.getEMPField(ir_next_adjust_term, FieldType.FIELD_DOUBLE, 2, 0));
						duebillInfoCD.addField("NEXT_REPC_UNIT", TagUtil.getEMPField(ir_next_adjust_unit, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("FIRST_REPC_DT", TagUtil.getEMPField(fir_adjust_day, FieldType.FIELD_STRING, 8, 0));
						duebillInfoCD.addField("DIVER_ADJ_PCT", TagUtil.getEMPField("", FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_OD_INT_RATE1", TagUtil.getEMPField(duebillInfoKColl.getDataValue("overdue_rate_y"), FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_OD_RATE_ADJ_PCT1", TagUtil.getEMPField(duebillInfoKColl.getDataValue("overdue_rate"), FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("PRCP_BUY_IND", TagUtil.getEMPField("Y", FieldType.FIELD_STRING, 1, 0));//是否资产买入
						duebillInfoCD.addField("TRUST_LOAN_NO", TagUtil.getEMPField("", FieldType.FIELD_STRING, 30, 0));
//						String is_term = TagUtil.replaceNull4String(duebillInfoKColl.getDataValue("is_term"));//期供标志
//						if(is_term.equals("1")){
//							is_term = "Y";
//						}else{
//							is_term = "N";
//						}
						duebillInfoCD.addField("INSTM_IND", TagUtil.getEMPField("N", FieldType.FIELD_STRING, 1, 0));//目前默认非期供
						duebillInfoCD.addField("CMMT_LOAN_NO", TagUtil.getEMPField("", FieldType.FIELD_STRING, 30, 0));
						
						//资产买入需要传印花税收取   2014-05-12
						if(transfer_mode.equals("03")){
							duebillInfoCD.addField("STAMP_IND", TagUtil.getEMPField("1", FieldType.FIELD_STRING, 2, 0));
							/*印花税收取方式
							1、双边各自支付
							2、全由借款人支付
							3、全由委托人支付
							4、仅代扣借款人
							5、仅扣行内*/
							duebillInfoCD.addField("STAMP_TYP", TagUtil.getEMPField("5", FieldType.FIELD_STRING, 10, 0));//收取方式默认为：5 仅扣行内
						}else{
							duebillInfoCD.addField("STAMP_IND", TagUtil.getEMPField("2", FieldType.FIELD_STRING, 2, 0));
							duebillInfoCD.addField("STAMP_TYP", TagUtil.getEMPField("", FieldType.FIELD_STRING, 10, 0));
						}
						
						duebillInfoCD.addField("PAY_TYP", TagUtil.getEMPField("1", FieldType.FIELD_STRING, 60, 0));//目前默认自主支付
						
						//HX150119017_申请更正个人贷款支付方式   add by zhaozq start
						CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
						CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
						String cus_id = (String) duebillInfoKColl.getDataValue("cus_id");
						CusBase cusBase = csi.getCusBaseByCusId(cus_id,context,connection);
						String belg_line = "";
						if(cusBase!=null){
							belg_line = TagUtil.replaceNull4String(cusBase.getBelgLine());//所属条线
						}
						duebillInfoCD.addField("CLIENT_TYPE", TagUtil.getEMPField(belg_line, FieldType.FIELD_STRING, 6, 0));
						//HX150119017_申请更正个人贷款支付方式   add by zhaozq end
						duebillInfoCD.addField("CONSIGN_CUST_ID", TagUtil.getEMPField("", FieldType.FIELD_STRING, 20, 0));
						duebillInfoCD.addField("CONS_COMMI_PAY_RATE", TagUtil.getEMPField("", FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("DR_COMMI_PAY_RATE", TagUtil.getEMPField("", FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("OLD_LOAN_NO", TagUtil.getEMPField(duebillInfoKColl.getDataValue("bill_no"), FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("TAKEOVER_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(duebillInfoKColl.getDataValue("takeover_rate")), FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("TAKEOVER_INT", TagUtil.getEMPField(TagUtil.replaceNull4Double(duebillInfoKColl.getDataValue("takeover_int")), FieldType.FIELD_DOUBLE, 16, 2));
						duebillInfoCD.addField("TRANS_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(""), FieldType.FIELD_DOUBLE, 16, 2));//转让比例
						
						duebillInfoArray.addStruct(duebillInfoCD);
					}
					bodyListCD.addArray("ASSET_DUEBILL_INFO_ARRAY", duebillInfoArray);
				}else if(prd_id.equals("600021")){//资产流转业务
					/** 资产清单数据*/
					String condition = " where cont_no = '" + cont_no + "'";
					IndexedCollection relIColl = dao.queryList("IqpAssetTransList", condition, connection);
					
					/** 资产流转协议数据*/
					KeyedCollection transCtr = dao.queryDetail("CtrAssetTransCont", cont_no, connection);
					
					/**封装借据信息  借据信息直接从清单表取*/
					Array duebillInfoArray = new Array(); 
					for(int i=0;i<relIColl.size();i++){
						CompositeData duebillInfoCD = new CompositeData();
						KeyedCollection duebillInfoKColl = (KeyedCollection)relIColl.get(i);
						
						String ori_bill_no = (String) duebillInfoKColl.getDataValue("bill_no"); 
						
						//新生成借据信息
						String transcondition = " where cont_no = '"+cont_no+"' and ori_bill_no = '"+ori_bill_no+"' ";
						KeyedCollection transkcoll = dao.queryFirst("AccAssetTrans", null, transcondition, connection);
						String bill_no = (String)transkcoll.getDataValue("bill_no");
						KeyedCollection acckcoll = dao.queryDetail("AccLoan", bill_no, connection);
						String ori_cont_no = (String) acckcoll.getDataValue("cont_no");
						String ori_fina_br_id = (String) acckcoll.getDataValue("fina_br_id");
						//原合同信息
						KeyedCollection ctrkcoll = dao.queryDetail("CtrLoanCont", ori_cont_no, connection);
						String cus_id = (String) ctrkcoll.getDataValue("cus_id");
						//通过客户编号查询【客户信息】
						CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
						CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
						CusBase cusBase = csi.getCusBaseByCusId(cus_id,context,connection);
						String cus_name = TagUtil.replaceNull4String(cusBase.getCusName());//客户名称
						String cert_type = TagUtil.replaceNull4String(cusBase.getCertType());//证件类型
						String cert_code = TagUtil.replaceNull4String(cusBase.getCertCode());//证件号码
						String belg_line = TagUtil.replaceNull4String(cusBase.getBelgLine());//所属条线
						//合同从表
						KeyedCollection ctrsubkcoll =  dao.queryDetail("CtrLoanContSub", ori_cont_no, connection);
						String ir_accord_type = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("ir_accord_type"));//利率依据方式
						String ir_adjust_type = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("ir_adjust_type"));//下一次利率调整选项
						String ruling_ir_code = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("ruling_ir_code"));//基准利率代码
						String ruling_ir = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("ruling_ir"));//基准利率（年）
						String ir_float_rate = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("ir_float_rate"));//浮动比例
						String ir_float_point = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("ir_float_point"));//浮动点数
						String reality_ir_y = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("reality_ir_y"));//执行利率（年）
						Double overdue_rate_y = TagUtil.replaceNull4Double(ctrsubkcoll.getDataValue("overdue_rate_y"));//罚息执行利率
						String repay_type = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("repay_type"));//还款方式
						String repay_mode_type = "";//还款方式种类
						if(repay_type!=null && !"".equals(repay_type)){
							KeyedCollection prdRepayModeKColl = dao.queryDetail("PrdRepayMode", repay_type, connection);
							repay_mode_type = TagUtil.replaceNull4String(prdRepayModeKColl.getDataValue("repay_mode_type"));
						}
						String repay_term = "";
						String repay_space = "";
						if(repay_type.equals("A005")){//利随本清传1月
							repay_term = "M";//还款间隔单位
							repay_space = "1";//还款间隔
						}else{
							repay_term = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("repay_term"));//还款间隔单位
							repay_space = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("repay_space"));//还款间隔
						}
						Double default_rate =  TagUtil.replaceNull4Double(ctrsubkcoll.getDataValue("default_rate"));//罚息执行利率浮动比
						String date = context.getDataValue(PUBConstant.OPENDAY).toString();//营业日期
						String ir_next_adjust_term = "";//下一次利率调整间隔
						String ir_next_adjust_unit = "";//下一次利率调整间隔单位
						String fir_adjust_day = "";//第一次调整日
						//固定不变
						if(ir_adjust_type.equals("0")){
							ir_adjust_type = "";
						}
						//按月调整
						else if(ir_adjust_type.equals("1")){
							ir_adjust_type = "FIX";
							ir_next_adjust_term = "1";
							ir_next_adjust_unit = "M";
							fir_adjust_day = DateUtils.getNextDate("M", date);
						}
						//按季调整
						else if(ir_adjust_type.equals("2")){
							ir_adjust_type = "FIX";
							ir_next_adjust_term = "1";
							ir_next_adjust_unit = "Q";
							fir_adjust_day = DateUtils.getNextDate("Q", date);
						}
						//按年调整
						else if(ir_adjust_type.equals("3")){
							ir_adjust_type = "FIX";
							ir_next_adjust_term = "1";
							ir_next_adjust_unit = "Y";
							fir_adjust_day = DateUtils.getNextDate("Y", date);
						}
						Double overdue_rate = TagUtil.replaceNull4Double(ctrsubkcoll.getDataValue("overdue_rate"));//逾期利率浮动比
						String is_term = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("is_term"));//期供标志
						if(is_term.equals("1")){
							is_term = "Y";
						}else{
							is_term = "N";
						}
						String repay_date = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("repay_date"));//还款日
						
						duebillInfoCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("APPL_SEQ", TagUtil.getEMPField(authKColl1.getDataValue("serno"), FieldType.FIELD_STRING, 32, 0));
						duebillInfoCD.addField("INPUT_DT", TagUtil.getEMPField(TagUtil.formatDate(transCtr.getDataValue("int_start_date")), FieldType.FIELD_STRING, 8, 0));
						duebillInfoCD.addField("CONT_NO", TagUtil.getEMPField(ori_cont_no, FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("BCH_CDE", TagUtil.getEMPField(ori_fina_br_id, FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("LOAN_NO", TagUtil.getEMPField(bill_no, FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("BANK_CDE", TagUtil.getEMPField(TradeConstance.BANK_ID, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("CUST_ID", TagUtil.getEMPField(cus_id, FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("CUST_NAME", TagUtil.getEMPField(cus_name, FieldType.FIELD_STRING, 60, 0));
						duebillInfoCD.addField("ID_TYPE", TagUtil.getEMPField(cert_type, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("ID_NO", TagUtil.getEMPField(cert_code, FieldType.FIELD_STRING, 40, 0));
						duebillInfoCD.addField("ISS_CTRY", TagUtil.getEMPField("CN", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("DEALER_CDE", TagUtil.getEMPField("", FieldType.FIELD_STRING, 20, 0));
						duebillInfoCD.addField("LOAN_CCY", TagUtil.getEMPField(acckcoll.getDataValue("cur_type"), FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("ORIG_PRCP", TagUtil.getEMPField(duebillInfoKColl.getDataValue("trans_amt"), FieldType.FIELD_DOUBLE, 16, 2));//传转让金额
						duebillInfoCD.addField("LOAN_ACTV_DT", TagUtil.getEMPField(TagUtil.formatDate(acckcoll.getDataValue("distr_date")), FieldType.FIELD_STRING, 8, 0));//传转让日期
						duebillInfoCD.addField("LOAN_TYP", TagUtil.getEMPField(lmPrdId, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LAST_DUE_DT", TagUtil.getEMPField(TagUtil.formatDate(acckcoll.getDataValue("end_date")), FieldType.FIELD_STRING, 8, 0));
						duebillInfoCD.addField("INT_START_DT", TagUtil.getEMPField(TagUtil.formatDate(transCtr.getDataValue("int_start_date")), FieldType.FIELD_STRING, 8, 0));//传转让日期
						duebillInfoCD.addField("DUE_DAY", TagUtil.getEMPField(repay_date, FieldType.FIELD_STRING, 2, 0));
						if(ir_accord_type == null || ir_accord_type.equals("") || ir_accord_type.equals("03")){
							ir_accord_type = "";
						}else if(ir_accord_type.equals("01")){
							//议价利率依据
							ir_accord_type = "FX";//FX: 固定利率
						}else{
							//牌告利率依据、正常利率上浮动
							if(ir_adjust_type == null || ir_adjust_type.equals("")){//固定不变
								ir_accord_type = "FX";//FX:固定利率
							}else{
								ir_accord_type = "RV";//RV: 浮动利率
							}
						}
						duebillInfoCD.addField("LOAN_RATE_MODE", TagUtil.getEMPField(ir_accord_type, FieldType.FIELD_STRING, 2, 0));
						duebillInfoCD.addField("RATE_BASE", TagUtil.getEMPField("Y", FieldType.FIELD_STRING, 1, 0));
						duebillInfoCD.addField("LOAN_RATE_TYP", TagUtil.getEMPField(ruling_ir_code, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_BASE_RATE", TagUtil.getEMPField(ruling_ir, FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("INT_ADJ_PCT", TagUtil.getEMPField(ir_float_rate, FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_SPRD", TagUtil.getEMPField(ir_float_point, FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_INT_RATE", TagUtil.getEMPField(reality_ir_y, FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("OD_RATE_BASE", TagUtil.getEMPField("Y", FieldType.FIELD_STRING, 1, 0));
						duebillInfoCD.addField("LOAN_OD_RATE_TYP", TagUtil.getEMPField("", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_OD_BASE_RATE", TagUtil.getEMPField("", FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("INT_OD_ADJ_PCT", TagUtil.getEMPField("", FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_OD_INT_RATE", TagUtil.getEMPField(overdue_rate_y, FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("PAYM_FREQ_UNIT", TagUtil.getEMPField(repay_term, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("PAYM_FREQ_FREQ", TagUtil.getEMPField(repay_space, FieldType.FIELD_STRING, 2, 0));
						duebillInfoCD.addField("LOAN_PAYM_MTD", TagUtil.getEMPField(repay_type, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_PAYM_TYP", TagUtil.getEMPField(repay_mode_type, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("BUSS_TYP", TagUtil.getEMPField("FLOAN", FieldType.FIELD_STRING, 10, 0));//资产流传传FLOAN
						duebillInfoCD.addField("LOAN_GRACE_TYP", TagUtil.getEMPField("P", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_OD_GRACE", TagUtil.getEMPField("0", FieldType.FIELD_DOUBLE, 3, 0));
						duebillInfoCD.addField("LOAN_REPAY_MTHD", TagUtil.getEMPField("AUTOPAY", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_FIX_OD_INT_IND", TagUtil.getEMPField("N", FieldType.FIELD_STRING, 1, 0));
						duebillInfoCD.addField("LOAN_OD_CAT", TagUtil.getEMPField("L", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_OD_RATE_ADJ_PCT", TagUtil.getEMPField(default_rate, FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_OD_COMM_PART", TagUtil.getEMPField("", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_OD_CPD_IND", TagUtil.getEMPField("", FieldType.FIELD_STRING, 1, 0));
						duebillInfoCD.addField("NEXT_REPC_OPT", TagUtil.getEMPField(ir_adjust_type, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("NEXT_REPC_NUM", TagUtil.getEMPField(ir_next_adjust_term, FieldType.FIELD_DOUBLE, 2, 0));
						duebillInfoCD.addField("NEXT_REPC_UNIT", TagUtil.getEMPField(ir_next_adjust_unit, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("FIRST_REPC_DT", TagUtil.getEMPField(fir_adjust_day, FieldType.FIELD_STRING, 8, 0));
						duebillInfoCD.addField("DIVER_ADJ_PCT", TagUtil.getEMPField("", FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_OD_INT_RATE1", TagUtil.getEMPField(overdue_rate_y, FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_OD_RATE_ADJ_PCT1", TagUtil.getEMPField(overdue_rate, FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("PRCP_BUY_IND", TagUtil.getEMPField("L", FieldType.FIELD_STRING, 1, 0));//资产流转传“L”
						duebillInfoCD.addField("TRUST_LOAN_NO", TagUtil.getEMPField("", FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("INSTM_IND", TagUtil.getEMPField("N", FieldType.FIELD_STRING, 1, 0));//目前默认非期供
						duebillInfoCD.addField("CMMT_LOAN_NO", TagUtil.getEMPField("", FieldType.FIELD_STRING, 30, 0));				
						duebillInfoCD.addField("STAMP_IND", TagUtil.getEMPField("2", FieldType.FIELD_STRING, 2, 0));
						duebillInfoCD.addField("STAMP_TYP", TagUtil.getEMPField("", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("PAY_TYP", TagUtil.getEMPField("1", FieldType.FIELD_STRING, 60, 0));//目前默认自主支付
						duebillInfoCD.addField("CLIENT_TYPE", TagUtil.getEMPField(belg_line, FieldType.FIELD_STRING, 6, 0));
						duebillInfoCD.addField("CONSIGN_CUST_ID", TagUtil.getEMPField("", FieldType.FIELD_STRING, 20, 0));
						duebillInfoCD.addField("CONS_COMMI_PAY_RATE", TagUtil.getEMPField("", FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("DR_COMMI_PAY_RATE", TagUtil.getEMPField("", FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("OLD_LOAN_NO", TagUtil.getEMPField(ori_bill_no, FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("TAKEOVER_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(""), FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("TAKEOVER_INT", TagUtil.getEMPField(TagUtil.replaceNull4Double(""), FieldType.FIELD_DOUBLE, 16, 2));
						duebillInfoCD.addField("TRANS_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(duebillInfoKColl.getDataValue("trans_rate")), FieldType.FIELD_DOUBLE, 16, 2));//转让比例
						
						duebillInfoArray.addStruct(duebillInfoCD);
					}
					bodyListCD.addArray("ASSET_DUEBILL_INFO_ARRAY", duebillInfoArray);
				}else if(prd_id.equals("600022")){//资产证券化业务
					/** 资产清单数据*/
					String condition = " where cont_no = '" + cont_no + "'";
					IndexedCollection relIColl = dao.queryList("IqpAssetProList", condition, connection);
					
					/** 资产流转协议数据*/
					KeyedCollection transCtr = dao.queryDetail("CtrAssetProCont", cont_no, connection);
					
					/**封装借据信息  借据信息直接从清单表取*/
					Array duebillInfoArray = new Array(); 
					for(int i=0;i<relIColl.size();i++){
						CompositeData duebillInfoCD = new CompositeData();
						KeyedCollection duebillInfoKColl = (KeyedCollection)relIColl.get(i);
						
						String ori_bill_no = (String) duebillInfoKColl.getDataValue("bill_no"); 
						//根据是否有追索权设置业务品种：有追索权lmPrdId+"1",没有追索权lmPrdId+"2"  modify by zhaozq 2014-08-25  start
						String ori_lmPrdId = service.getPrdBasicAssetPro2LM(ori_bill_no, context, connection);
						if(transCtr.getDataValue("is_rgt_res")!=null&&"1".equals(transCtr.getDataValue("is_rgt_res"))){
							ori_lmPrdId = ori_lmPrdId + "1";
						}else if(transCtr.getDataValue("is_rgt_res")!=null&&"2".equals(transCtr.getDataValue("is_rgt_res"))){
							ori_lmPrdId = ori_lmPrdId + "2";
						}
						//根据是否有追索权设置业务品种：有追索权lmPrdId+"1",没有追索权lmPrdId+"2"  modify by zhaozq 2014-08-25  start
						
						//获取资产信息
						KeyedCollection regikc = dao.queryFirst("IqpAssetRegiApp", null, " where bill_no = '"+ori_bill_no+"'", connection);
						String loan_balance = TagUtil.replaceNull4String(regikc.getDataValue("loan_balance"));//资产余额
						
						//新生成借据信息
						String transcondition = " where cont_no = '"+cont_no+"' and ori_bill_no = '"+ori_bill_no+"' ";
						KeyedCollection transkcoll = dao.queryFirst("AccAssetTrans", null, transcondition, connection);
						String bill_no = (String)transkcoll.getDataValue("bill_no");
						KeyedCollection acckcoll = dao.queryDetail("AccLoan", bill_no, connection);
						String ori_cont_no = (String) acckcoll.getDataValue("cont_no");
						String ori_fina_br_id = (String) acckcoll.getDataValue("fina_br_id");
						//原合同信息
						KeyedCollection ctrkcoll = dao.queryDetail("CtrLoanCont", ori_cont_no, connection);
						String cus_id = (String) ctrkcoll.getDataValue("cus_id");
						//通过客户编号查询【客户信息】
						CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
						CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
						CusBase cusBase = csi.getCusBaseByCusId(cus_id,context,connection);
						String cus_name = TagUtil.replaceNull4String(cusBase.getCusName());//客户名称
						String cert_type = TagUtil.replaceNull4String(cusBase.getCertType());//证件类型
						String cert_code = TagUtil.replaceNull4String(cusBase.getCertCode());//证件号码
						String belg_line = TagUtil.replaceNull4String(cusBase.getBelgLine());//所属条线
						//合同从表
						KeyedCollection ctrsubkcoll =  dao.queryDetail("CtrLoanContSub", ori_cont_no, connection);
						String ir_accord_type = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("ir_accord_type"));//利率依据方式
						String ir_adjust_type = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("ir_adjust_type"));//下一次利率调整选项
						String ruling_ir_code = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("ruling_ir_code"));//基准利率代码
						String ruling_ir = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("ruling_ir"));//基准利率（年）
						String ir_float_rate = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("ir_float_rate"));//浮动比例
						String ir_float_point = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("ir_float_point"));//浮动点数
						String reality_ir_y = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("reality_ir_y"));//执行利率（年）
						Double overdue_rate_y = TagUtil.replaceNull4Double(ctrsubkcoll.getDataValue("overdue_rate_y"));//罚息执行利率
						String repay_type = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("repay_type"));//还款方式
						String repay_mode_type = "";//还款方式种类
						if(repay_type!=null && !"".equals(repay_type)){
							KeyedCollection prdRepayModeKColl = dao.queryDetail("PrdRepayMode", repay_type, connection);
							repay_mode_type = TagUtil.replaceNull4String(prdRepayModeKColl.getDataValue("repay_mode_type"));
						}
						String repay_term = "";
						String repay_space = "";
						if(repay_type.equals("A005")){//利随本清传1月
							repay_term = "M";//还款间隔单位
							repay_space = "1";//还款间隔
						}else{
							repay_term = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("repay_term"));//还款间隔单位
							repay_space = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("repay_space"));//还款间隔
						}
						Double default_rate =  TagUtil.replaceNull4Double(ctrsubkcoll.getDataValue("default_rate"));//罚息执行利率浮动比
						String date = context.getDataValue(PUBConstant.OPENDAY).toString();//营业日期
						String ir_next_adjust_term = "";//下一次利率调整间隔
						String ir_next_adjust_unit = "";//下一次利率调整间隔单位
						String fir_adjust_day = "";//第一次调整日
						//固定不变
						if(ir_adjust_type.equals("0")){
							ir_adjust_type = "";
						}
						//按月调整
						else if(ir_adjust_type.equals("1")){
							ir_adjust_type = "FIX";
							ir_next_adjust_term = "1";
							ir_next_adjust_unit = "M";
							fir_adjust_day = DateUtils.getNextDate("M", date);
						}
						//按季调整
						else if(ir_adjust_type.equals("2")){
							ir_adjust_type = "FIX";
							ir_next_adjust_term = "1";
							ir_next_adjust_unit = "Q";
							fir_adjust_day = DateUtils.getNextDate("Q", date);
						}
						//按年调整
						else if(ir_adjust_type.equals("3")){
							ir_adjust_type = "FIX";
							ir_next_adjust_term = "1";
							ir_next_adjust_unit = "Y";
							fir_adjust_day = DateUtils.getNextDate("Y", date);
						}
						Double overdue_rate = TagUtil.replaceNull4Double(ctrsubkcoll.getDataValue("overdue_rate"));//逾期利率浮动比
						String is_term = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("is_term"));//期供标志
						if(is_term.equals("1")){
							is_term = "Y";
						}else{
							is_term = "N";
						}
						String repay_date = TagUtil.replaceNull4String(ctrsubkcoll.getDataValue("repay_date"));//还款日
						
						duebillInfoCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("APPL_SEQ", TagUtil.getEMPField(authKColl1.getDataValue("serno"), FieldType.FIELD_STRING, 32, 0));
						duebillInfoCD.addField("INPUT_DT", TagUtil.getEMPField(TagUtil.formatDate(transCtr.getDataValue("input_date")), FieldType.FIELD_STRING, 8, 0));
						duebillInfoCD.addField("CONT_NO", TagUtil.getEMPField(ori_cont_no, FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("BCH_CDE", TagUtil.getEMPField(ori_fina_br_id, FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("LOAN_NO", TagUtil.getEMPField(bill_no, FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("BANK_CDE", TagUtil.getEMPField(TradeConstance.BANK_ID, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("CUST_ID", TagUtil.getEMPField(cus_id, FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("CUST_NAME", TagUtil.getEMPField(cus_name, FieldType.FIELD_STRING, 60, 0));
						duebillInfoCD.addField("ID_TYPE", TagUtil.getEMPField(cert_type, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("ID_NO", TagUtil.getEMPField(cert_code, FieldType.FIELD_STRING, 40, 0));
						duebillInfoCD.addField("ISS_CTRY", TagUtil.getEMPField("CN", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("DEALER_CDE", TagUtil.getEMPField("", FieldType.FIELD_STRING, 20, 0));
						duebillInfoCD.addField("LOAN_CCY", TagUtil.getEMPField(acckcoll.getDataValue("cur_type"), FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("ORIG_PRCP", TagUtil.getEMPField(loan_balance, FieldType.FIELD_DOUBLE, 16, 2));//传转让金额
						duebillInfoCD.addField("LOAN_ACTV_DT", TagUtil.getEMPField(TagUtil.formatDate(acckcoll.getDataValue("distr_date")), FieldType.FIELD_STRING, 8, 0));//传转让日期
						duebillInfoCD.addField("LOAN_TYP", TagUtil.getEMPField(ori_lmPrdId, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LAST_DUE_DT", TagUtil.getEMPField(TagUtil.formatDate(acckcoll.getDataValue("end_date")), FieldType.FIELD_STRING, 8, 0));
						duebillInfoCD.addField("INT_START_DT", TagUtil.getEMPField(TagUtil.formatDate(acckcoll.getDataValue("distr_date")), FieldType.FIELD_STRING, 8, 0));//必须和贷款发放日期一致
						duebillInfoCD.addField("DUE_DAY", TagUtil.getEMPField(repay_date, FieldType.FIELD_STRING, 2, 0));
						if(ir_accord_type == null || ir_accord_type.equals("") || ir_accord_type.equals("03")){
							ir_accord_type = "";
						}else if(ir_accord_type.equals("01")){
							//议价利率依据
							ir_accord_type = "FX";//FX: 固定利率
						}else{
							//牌告利率依据、正常利率上浮动
							if(ir_adjust_type == null || ir_adjust_type.equals("")){//固定不变
								ir_accord_type = "FX";//FX:固定利率
							}else{
								ir_accord_type = "RV";//RV: 浮动利率
							}
						}
						duebillInfoCD.addField("LOAN_RATE_MODE", TagUtil.getEMPField(ir_accord_type, FieldType.FIELD_STRING, 2, 0));
						duebillInfoCD.addField("RATE_BASE", TagUtil.getEMPField("Y", FieldType.FIELD_STRING, 1, 0));
						duebillInfoCD.addField("LOAN_RATE_TYP", TagUtil.getEMPField(ruling_ir_code, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_BASE_RATE", TagUtil.getEMPField(ruling_ir, FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("INT_ADJ_PCT", TagUtil.getEMPField(ir_float_rate, FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_SPRD", TagUtil.getEMPField(ir_float_point, FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_INT_RATE", TagUtil.getEMPField(reality_ir_y, FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("OD_RATE_BASE", TagUtil.getEMPField("Y", FieldType.FIELD_STRING, 1, 0));
						duebillInfoCD.addField("LOAN_OD_RATE_TYP", TagUtil.getEMPField("", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_OD_BASE_RATE", TagUtil.getEMPField("", FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("INT_OD_ADJ_PCT", TagUtil.getEMPField("", FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_OD_INT_RATE", TagUtil.getEMPField(overdue_rate_y, FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("PAYM_FREQ_UNIT", TagUtil.getEMPField(repay_term, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("PAYM_FREQ_FREQ", TagUtil.getEMPField(repay_space, FieldType.FIELD_STRING, 2, 0));
						duebillInfoCD.addField("LOAN_PAYM_MTD", TagUtil.getEMPField(repay_type, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_PAYM_TYP", TagUtil.getEMPField(repay_mode_type, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("BUSS_TYP", TagUtil.getEMPField("FLOAN", FieldType.FIELD_STRING, 10, 0));//资产证券化传FLOAN
						duebillInfoCD.addField("LOAN_GRACE_TYP", TagUtil.getEMPField("P", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_OD_GRACE", TagUtil.getEMPField("0", FieldType.FIELD_DOUBLE, 3, 0));
						duebillInfoCD.addField("LOAN_REPAY_MTHD", TagUtil.getEMPField("AUTOPAY", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_FIX_OD_INT_IND", TagUtil.getEMPField("N", FieldType.FIELD_STRING, 1, 0));
						duebillInfoCD.addField("LOAN_OD_CAT", TagUtil.getEMPField("L", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_OD_RATE_ADJ_PCT", TagUtil.getEMPField(default_rate, FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_OD_COMM_PART", TagUtil.getEMPField("", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("LOAN_OD_CPD_IND", TagUtil.getEMPField("", FieldType.FIELD_STRING, 1, 0));
						duebillInfoCD.addField("NEXT_REPC_OPT", TagUtil.getEMPField(ir_adjust_type, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("NEXT_REPC_NUM", TagUtil.getEMPField(ir_next_adjust_term, FieldType.FIELD_DOUBLE, 2, 0));
						duebillInfoCD.addField("NEXT_REPC_UNIT", TagUtil.getEMPField(ir_next_adjust_unit, FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("FIRST_REPC_DT", TagUtil.getEMPField(fir_adjust_day, FieldType.FIELD_STRING, 8, 0));
						duebillInfoCD.addField("DIVER_ADJ_PCT", TagUtil.getEMPField("", FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_OD_INT_RATE1", TagUtil.getEMPField(overdue_rate_y, FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("LOAN_OD_RATE_ADJ_PCT1", TagUtil.getEMPField(overdue_rate, FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("PRCP_BUY_IND", TagUtil.getEMPField("Q", FieldType.FIELD_STRING, 1, 0));//资产证券化传“Q”
						duebillInfoCD.addField("TRUST_LOAN_NO", TagUtil.getEMPField("", FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("INSTM_IND", TagUtil.getEMPField("N", FieldType.FIELD_STRING, 1, 0));//目前默认非期供
						duebillInfoCD.addField("CMMT_LOAN_NO", TagUtil.getEMPField("", FieldType.FIELD_STRING, 30, 0));				
						duebillInfoCD.addField("STAMP_IND", TagUtil.getEMPField("2", FieldType.FIELD_STRING, 2, 0));
						duebillInfoCD.addField("STAMP_TYP", TagUtil.getEMPField("", FieldType.FIELD_STRING, 10, 0));
						duebillInfoCD.addField("PAY_TYP", TagUtil.getEMPField("1", FieldType.FIELD_STRING, 60, 0));//目前默认自主支付
						duebillInfoCD.addField("CLIENT_TYPE", TagUtil.getEMPField(belg_line, FieldType.FIELD_STRING, 6, 0));
						duebillInfoCD.addField("CONSIGN_CUST_ID", TagUtil.getEMPField("", FieldType.FIELD_STRING, 20, 0));
						duebillInfoCD.addField("CONS_COMMI_PAY_RATE", TagUtil.getEMPField("", FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("DR_COMMI_PAY_RATE", TagUtil.getEMPField("", FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("OLD_LOAN_NO", TagUtil.getEMPField(ori_bill_no, FieldType.FIELD_STRING, 30, 0));
						duebillInfoCD.addField("TAKEOVER_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(""), FieldType.FIELD_DOUBLE, 16, 9));
						duebillInfoCD.addField("TAKEOVER_INT", TagUtil.getEMPField(TagUtil.replaceNull4Double(""), FieldType.FIELD_DOUBLE, 16, 2));
						duebillInfoCD.addField("TRANS_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(""), FieldType.FIELD_DOUBLE, 16, 2));//转让比例
						
						duebillInfoArray.addStruct(duebillInfoCD);
					}
					bodyListCD.addArray("ASSET_DUEBILL_INFO_ARRAY", duebillInfoArray);
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
						zhCD.addField("BALANCE", TagUtil.getEMPField(reflectSubKColl.getDataValue("BALANCE"), FieldType.FIELD_DOUBLE, 20, 2));
						zhCD.addField("COMMISSION_PAYMENT_AMOUNT", TagUtil.getEMPField(reflectSubKColl.getDataValue("COMMISSION_PAYMENT_AMOUNT"), FieldType.FIELD_DOUBLE, 20, 2));
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
						feeCD.addField("BRANCH_ID", TagUtil.getEMPField(reflectSubKColl.getDataValue("BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
						feeCD.addField("FEE_SPAN", TagUtil.getEMPField(reflectSubKColl.getDataValue("FEE_SPAN"), FieldType.FIELD_STRING, 10, 0));
						feeArray.addStruct(feeCD);
					}
					bodyListCD.addArray("FEE_AUTH_INFO_ARRAY", feeArray);
				}
				
				//组装自由还款信息（查询授权子表类型为01的信息）
				IndexedCollection iqpFreedomPayInfoIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_HK+"' ", connection);
				if(iqpFreedomPayInfoIColl != null && iqpFreedomPayInfoIColl.size() > 0){
					Array payArray = new Array(); 
					for(int i=0;i<iqpFreedomPayInfoIColl.size();i++){
						CompositeData payCD = new CompositeData();
						KeyedCollection iqpFreedomPayInfoKColl = (KeyedCollection)iqpFreedomPayInfoIColl.get(i);
						KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(iqpFreedomPayInfoKColl);
						payCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 50, 0));
						payCD.addField("PERIOD_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("PERIOD_NO"), FieldType.FIELD_STRING, 40, 0));
						payCD.addField("REPAY_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("REPAY_DATE"), FieldType.FIELD_STRING, 10, 0));
						payCD.addField("CORPUS", TagUtil.getEMPField(reflectSubKColl.getDataValue("CORPUS"), FieldType.FIELD_DOUBLE, 20, 2));
						payCD.addField("INTEREST", TagUtil.getEMPField(reflectSubKColl.getDataValue("INTEREST"), FieldType.FIELD_DOUBLE, 20, 2));
						payArray.addStruct(payCD);
					}
					bodyListCD.addArray("REPAY_PLAN_ARRAY", payArray);
				}
				
				
				/** 应用头 */
				CompositeData APP_HEAD= esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
						(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM);
				APP_HEAD.addField("FILE_NAME",TagUtil.getEMPField(FTPUtil.getFileName(serviceCode, senceCode, authorize_no), FieldType.FIELD_STRING, 100, 0));
				APP_HEAD.addField("FILE_PATH",TagUtil.getEMPField(FTPUtil.getFilePath(), FieldType.FIELD_STRING, 100, 0));
				APP_HEAD.addField("TOTAL_ROWS",TagUtil.getEMPField(pvpIColl.size(), FieldType.FIELD_STRING, 100, 0));
				reqCD.addStruct("APP_HEAD", APP_HEAD);
				
				/**扩展头**/
				CompositeData LOCAL_HEAD= new CompositeData();
				LOCAL_HEAD.addField("FILE_FLAG", TagUtil.getEMPField("1", FieldType.FIELD_STRING, 6, 0));
				reqCD.addStruct("LOCAL_HEAD", LOCAL_HEAD);
				
				FTPUtil.send2FTP(serviceCode, senceCode, authorize_no, fileCD);
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
