package com.yucheng.cmis.biz01line.esb.op.trade;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.dc.eai.data.CompositeData;
import com.dc.eai.data.FieldType;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
/**
 * 贷款展期授权交易
 * @author liqh
 *
 */
public class TradeDkzqsq extends TranClient {
	private static final String AUTHMODEL = "PvpAuthorize";
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
//			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, senceCode, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
//			/** 应用头 */
//			reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
//					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			
			CompositeData headKcoll= new CompositeData();
			headKcoll.addField("SvcCd", TagUtil.getEMPField(serviceCode, FieldType.FIELD_STRING, 30, 0));
			headKcoll.addField("ScnCd", TagUtil.getEMPField(senceCode, FieldType.FIELD_STRING, 2, 0));
			headKcoll.addField("TxnMd", TagUtil.getEMPField("ONLINE", FieldType.FIELD_STRING, 6, 0));
			headKcoll.addField("UsrLngKnd", TagUtil.getEMPField("UsrLngKnd", FieldType.FIELD_STRING, 20, 0));
			headKcoll.addField("jkType", TagUtil.getEMPField("cbs", FieldType.FIELD_STRING, 5, 0));
			reqCD.addStruct("SYS_HEAD", headKcoll);
			
			/** 封装报文体 */
			KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl);//去除"@"符号，变成可读字段
			CompositeData bodyCD = new CompositeData();
			
			/** 挡板测试代码，待改为正式代码  TODO add by huangtao on 2019-03-12 -start*/
			/*bodyCD.addField("AcctNoCrdNo", TagUtil.getEMPField("999902019000000083", FieldType.FIELD_STRING, 50, 0));
			bodyCD.addField("AcctSeqNo", TagUtil.getEMPField("123456", FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("NewExprtnDt", TagUtil.getEMPField("20180710", FieldType.FIELD_STRING, 8, 0));
			bodyCD.addField("ExprtnDt", TagUtil.getEMPField("20170710", FieldType.FIELD_STRING, 8, 0));
			bodyCD.addField("TrmModTp", TagUtil.getEMPField("01", FieldType.FIELD_STRING, 8, 0));*/
			/** 挡板测试代码，待改为正式代码  TODO add by huangtao on 2019-03-12 -end*/
			/** 获取借据信息中的贷款号 */
			KeyedCollection accLoan =  dao.queryDetail("AccLoan", (String) authKColl.getDataValue("bill_no"), connection);
			String base_acct_no = (String)accLoan.getDataValue("base_acct_no");
			String acct_seq_no = (String)accLoan.getDataValue("acct_seq_no");
			
			bodyCD.addField("AcctNoCrdNo", TagUtil.getEMPField(base_acct_no, FieldType.FIELD_STRING, 50, 0));//贷款账号
			bodyCD.addField("AcctSeqNo", TagUtil.getEMPField(acct_seq_no, FieldType.FIELD_STRING, 10, 0));//贷款发放好
			KeyedCollection retKcoll = (KeyedCollection)SqlClient.queryFirst("queryAcctNoByBillNo", reflectKColl.getDataValue("DUEBILL_NO"), null, connection);
			bodyCD.addField("NewExprtnDt", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("EXPIRY_DATE")), FieldType.FIELD_STRING, 8, 0));//新到期日
			bodyCD.addField("ExprtnDt", TagUtil.getEMPField(reflectKColl.getDataValue("END_DATE"), FieldType.FIELD_STRING, 8, 0));//变更前到期日
			//判断一下展期类型：正常展期/逾期展期
			String trmModTp = "01";
			 if (accLoan.getDataValue("overdue_balance") != null ) {
				 double overdueBalance = Double.parseDouble(accLoan.getDataValue("overdue_balance").toString());
				 trmModTp = overdueBalance > 0 ? "02" : "01";
			 }
			bodyCD.addField("TrmModTp", TagUtil.getEMPField(trmModTp, FieldType.FIELD_STRING, 8, 0));//期限变更类型
			
			/** ------------------------------原有逻辑，不适用于裕民银行故注释  modified by huangtao on 2019-03-12 start ------------------------------------- */
//			bodyCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
//			bodyCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 30, 0));
//			bodyCD.addField("EXPIRY_DATE", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("EXPIRY_DATE")), FieldType.FIELD_STRING, 8, 0));
//			bodyCD.addField("INT_RATE_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("INT_RATE_MODE"), FieldType.FIELD_STRING, 2, 0));
//			bodyCD.addField("INT_FLT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("INT_FLT_RATE"), FieldType.FIELD_DOUBLE, 20, 9));
//			bodyCD.addField("BASE_INT_RATE_CODE", TagUtil.getEMPField(reflectKColl.getDataValue("BASE_INT_RATE_CODE"), FieldType.FIELD_STRING, 10, 0));
//			bodyCD.addField("BASE_INT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("BASE_INT_RATE"), FieldType.FIELD_DOUBLE, 20, 9));
//			bodyCD.addField("SPREAD", TagUtil.getEMPField(reflectKColl.getDataValue("SPREAD"), FieldType.FIELD_DOUBLE, 20, 9));
//			bodyCD.addField("ACT_INT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("ACT_INT_RATE"), FieldType.FIELD_DOUBLE, 20, 9));
//			bodyCD.addField("LOAN_OD_FLT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_FLT_RATE"), FieldType.FIELD_DOUBLE, 20, 9));
//			bodyCD.addField("LOAN_OD_ACT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_OD_ACT_RATE"), FieldType.FIELD_DOUBLE, 20, 9));
//			bodyCD.addField("OVERDUE_FLT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("OVERDUE_FLT_RATE"), FieldType.FIELD_DOUBLE, 20, 9));
//			bodyCD.addField("OVERDUE_ACT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("OVERDUE_ACT_RATE"), FieldType.FIELD_DOUBLE, 20, 9));
			/** ------------------------------原有逻辑，不适用于裕民银行故注释  modified by huangtao on 2019-03-12 end ------------------------------------- */
			reqCD.addStruct("BODY", bodyCD);
		}catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		return reqCD;
	}

	//XD150520037_信贷系统利率调整修改优化
	public void doSuccess(Context context, Connection connection) throws EMPException{
		
	}
	
	public KeyedCollection doYmExcute(Context context,Connection conn) throws EMPException{
		/** 封装发送报文信息 */
		KeyedCollection reqCD= new KeyedCollection();
		try{
			String openDay = (String)context.getDataValue("OPENDAY");
			String tran_serno = (String)context.getDataValue("tran_serno");
			//获取dao
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			/** 通过交易码判断所需执行的交易，以及需要准备的交易数据 */
			KeyedCollection authKColl = dao.queryDetail(AUTHMODEL, tran_serno, conn);
			/** 系统头 */		
			KeyedCollection headKcoll= new KeyedCollection();
			headKcoll.put("SvcCd","30220002");//利率调整服务码
			headKcoll.put("ScnCd","03");//利率调整场景码
			//跟核心交互的接口在头里还得传这三个参数
			headKcoll.put("TxnMd","ONLINE");
			headKcoll.put("UsrLngKnd","CHINESE");
			headKcoll.put("jkType","cbs");
			headKcoll.setName("SYS_HEAD");
			reqCD.addKeyedCollection(headKcoll);
			
			/** 封装报文体 */
			KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl);//去除"@"符号，变成可读字段
			KeyedCollection bodyCD = new KeyedCollection();
			bodyCD.setName("BODY");
			KeyedCollection accLoan =  dao.queryDetail("AccLoan", (String) authKColl.getDataValue("bill_no"), conn);
			String base_acct_no = (String)accLoan.getDataValue("base_acct_no");//贷款号
			String acct_seq_no = (String)accLoan.getDataValue("acct_seq_no");//账户序号
			String distr_date = (String)accLoan.getDataValue("distr_date");//贷款起始日
			
			
			Map<String,Object> _param = new HashMap<String, Object>();
			_param.put("base_acct_no", base_acct_no);
			_param.put("acct_seq_no", acct_seq_no);
			//KeyedCollection extendkc = (KeyedCollection)SqlClient.queryFirst("queryExtendInfoByAuthorizeNo", _param, null, conn);
			BigDecimal base_rate = new BigDecimal((String)reflectKColl.getDataValue("ACT_INT_RATE"));//展期基准利率
			BigDecimal extension_rate = new BigDecimal((String)reflectKColl.getDataValue("ACT_INT_RATE")).multiply(new BigDecimal("100"));//展期利率
			BigDecimal overdue_rate = new BigDecimal((String)reflectKColl.getDataValue("LOAN_OD_ACT_RATE")).multiply(new BigDecimal("100"));//逾期利率
			BigDecimal default_rate = new BigDecimal((String)reflectKColl.getDataValue("OVERDUE_ACT_RATE")).multiply(new BigDecimal("100"));//罚息利率
			String currType = (String) authKColl.getDataValue("cur_type");//币种
			
			String termM = getQs(distr_date,reflectKColl.getDataValue("EXPIRY_DATE").toString());
			Map paramMap = new HashMap();
			paramMap.put("prdid", "9999");//裕民银行prdId默认写死“9999”
			paramMap.put("curtype", currType);
			paramMap.put("termM", termM);
			KeyedCollection returnKColl = (KeyedCollection)SqlClient.queryFirst("getRate", paramMap, null, conn);
			String baseRemitType = (String) returnKColl.getDataValue("base_remit_type");
			
			bodyCD.put("DealTp", "03");
			bodyCD.put("LoanNo", base_acct_no);
			bodyCD.put("AcctNoSeqNo", acct_seq_no);
			bodyCD.put("LoanEndDt", reflectKColl.getDataValue("END_DATE").toString().replaceAll("-", ""));//原到期日
			bodyCD.put("NewLoanEndDt", reflectKColl.getDataValue("EXPIRY_DATE").toString().replaceAll("-", ""));//新到期日
			bodyCD.put("OprtnTp", "01");//"01:保存,"02"：修改,"03"：删除
			bodyCD.put("SysInd", "10");//系统标识
			IndexedCollection loanModInfArry = new IndexedCollection();
			loanModInfArry.setName("LoanModInfArry");
			KeyedCollection loanModInfKColl = new KeyedCollection();
			loanModInfKColl.put("IntCtgryTp", "INT");//利息分类
			loanModInfKColl.put("NewIntRateTp", baseRemitType);//新利率类型
			loanModInfKColl.put("TkEffDt", openDay.replaceAll("-", ""));//生效日期
			loanModInfKColl.put("AfModSubsAcctLvlFixIntRate", extension_rate);//变更后分户固定利率
			loanModInfKColl.put("NewIntRateEnblMd","N");//暂时不予变更
			//loanModInfKColl.put("NewIntRateModDt", openDay.replaceAll("-", ""));//变更日期
			loanModInfArry.add(loanModInfKColl);
			
			KeyedCollection loanModInfKColl1 = new KeyedCollection();
			loanModInfKColl1.put("IntCtgryTp", "ODP");//利息分类
			loanModInfKColl1.put("NewIntRateTp", baseRemitType);//新利率类型
			loanModInfKColl1.put("TkEffDt", openDay.replaceAll("-", ""));//生效日期
			loanModInfKColl1.put("AfModSubsAcctLvlFixIntRate", overdue_rate);//变更后逾期固定利率
			loanModInfKColl1.put("NewIntRateEnblMd","N");//暂时不予变更
			//loanModInfKColl1.put("NewIntRateModDt", openDay.replaceAll("-", ""));//变更日期
			loanModInfArry.add(loanModInfKColl1);
			
			KeyedCollection loanModInfKColl2 = new KeyedCollection();
			loanModInfKColl2.put("IntCtgryTp", "ODI");//利息分类
			loanModInfKColl2.put("NewIntRateTp", baseRemitType);//新利率类型
			loanModInfKColl2.put("TkEffDt", openDay.replaceAll("-", ""));//生效日期
			loanModInfKColl2.put("AfModSubsAcctLvlFixIntRate", default_rate);//变更后违约固定利率
			loanModInfKColl2.put("NewIntRateEnblMd","N");//暂时不予变更
			//loanModInfKColl2.put("NewIntRateModDt", openDay.replaceAll("-", ""));//变更日期
			loanModInfArry.add(loanModInfKColl2);
			
			bodyCD.addIndexedCollection(loanModInfArry);
			reqCD.addKeyedCollection(bodyCD);
		}catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		return reqCD;
	}
	
	public String getQs(String startDate,String endDate){
		String yearF,yearT,monF,monT,dayF,dayT,Qs,Qs1,Qs2,Qs3;
		yearF = startDate.substring(0,4);
		yearT = endDate.substring(0,4);
		monF = startDate.substring(5,7);
		monT = endDate.substring(5,7);
		dayF = startDate.substring(8,10);
		dayT = endDate.substring(8,10);
		Qs1 = String.valueOf((Integer.parseInt(yearT)-Integer.parseInt(yearF))*12);
		Qs2 = String.valueOf(Integer.parseInt(monT)-Integer.parseInt(monF));
		if((Integer.parseInt(dayT)) > (Integer.parseInt(dayF)))
			Qs3 = "1";
		else
			Qs3 = "0";
		Qs = String.valueOf(Integer.parseInt(Qs1)+Integer.parseInt(Qs2)+Integer.parseInt(Qs3));
		return Qs;
	}
}
