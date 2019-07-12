package com.yucheng.cmis.biz01line.esb.op;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.pack.standardxml.PackUtil;
import com.dcfs.esb.server.service.Service;
import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.jdbc.ConnectionManager;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.TradeInterface;
import com.yucheng.cmis.biz01line.esb.pub.FTPUtil;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.biz01line.iqp.msi.msiimple.IqpServiceInterfaceImple;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
/**
 * ESB服务端监听,或得ESB请求的结构体，解析结构体，转发给不同的业务交易
 * @author Pansq
 * @version V1.0
 * 修改记录：
 * 版本号    修改人       修改日期      修改内容
 * V1.1		liqh		2014/1/9		1、将实时交易报文全部存储到本地目录，判断交易日期是否等于当前营业日期
 * 											交易日期等于当前营业日期，执行业务逻辑，同时存储到实时交易信息表，根据业务逻辑执行结果更新交易信息表
 * 										    交易日期不等于当前营业日期则不执行业务逻辑，同时存储实时交易信息表失败记录，日终结束后处理，反馈报文均为成功
 * 										2、电票特殊交易特殊处理
 * 
 * 
 */
public class ReciveEsbTrandOp implements Service {
	public static final String ESB_INTERFACE_CLASS = "com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple";
	TradeInterface tradeInterface;
	ESBInterface esbinterface;
	Context context;
	Connection connection = null;
	DataSource dataSource = null;
	String openDay = null;
	String serviceCode = null;
	String serviceSence = null;
	String serno = null;
	boolean succ = false;//交易时间判断标识
	String pk1 = "";
	
	/** 获得数据库连接 */
	private void init(){
		try {
			EMPFlowComponentFactory factory = (EMPFlowComponentFactory) EMPFlowComponentFactory.getComponentFactory("CMISBiz");
		    context = factory.getContextNamed(factory.getRootContextName());
			dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
			connection = dataSource.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取系统头中交易码、交易场景判断处理业务逻辑实现类
	 * @param CD 请求报文结构体
	 * 增加返回值，用于后续判断是否需要执行业务逻辑处理（交易日期与当前营业日不一致不处理业务逻辑，待日终批处理结束后执行）
	 * 
	 */
	public Boolean getTranExecute(CompositeData CD){
		try {
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(CD), "UTF-8"));
			Map<String, String> param = new HashMap<String, String>();
			String error_res="2";//默认来源为日终
			/** 取出请求系统头中数据信息 */
			esbinterface = (ESBInterface)Class.forName(ESB_INTERFACE_CLASS).newInstance();
			KeyedCollection reqSysKColl = esbinterface.getReqSysHead(CD);
			serviceCode = (String)reqSysKColl.getDataValue("SERVICE_CODE");
			serviceSence = (String)reqSysKColl.getDataValue("SERVICE_SCENE");
			serno = (String)reqSysKColl.getDataValue("CONSUMER_SEQ_NO");
			String tranDate = (String)reqSysKColl.getDataValue("TRAN_DATE");
			openDay = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			context.put("OPENDAY", openDay);
			/** ------------接收到交易信息，先保存改交易信息，保存信息文件名规则为：交易时间（年月日）+_+交易码+_+授权码.xml---------------- */
			String localFile = FTPUtil.getLocalFileName(serviceCode, serviceSence, serno, CD, tranDate);
			if(tranDate.equals(openDay.replaceAll("-", ""))){
				succ = true;
				error_res="1";
			}
			pk1 = CMISSequenceService4JXXD.querySequenceFromDB("DAYBAT", "all", connection, context);
			param.put("pk1", pk1);
			param.put("consumer_seq_no", serno);
			param.put("service_code", serviceCode);
			param.put("service_scene", serviceSence);
			param.put("tran_date", tranDate);
			param.put("locate_file", localFile);
			param.put("is_succ", "1");
			param.put("deal_flag", "1");
			param.put("error_res", error_res);
			SqlClient.insert("insertDaybatTrandInfo", param, connection);
			
			/** 通过交易码查询所配置的交易实现类，决定所作的交易处理 */
			String impleClass = (String)SqlClient.queryFirst("queryEsbConfig", reqSysKColl, null, connection);
			tradeInterface = (TradeInterface)Class.forName(impleClass.trim()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return succ;
	}
	/**
	 * 交易监听程序执行入口
	 */
	public CompositeData exec(CompositeData CD) {
		CompositeData respCD = null;
		KeyedCollection resultKColl = null;
		Map<String, String> paramValue = new HashMap<String, String>();
		String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());//精确到毫秒
		try {
			/**-- 初始化数据库连接 --*/
			init();
			/**-- 将交易文件写入本地，返回是否需要处理业务逻辑标志 --*/
			Boolean flag = getTranExecute(CD);
			//保证金占用查询交易
			if(serviceCode!=null&&serviceCode.equals(TradeConstance.SERVICE_CODE_BZJZYCX)&&serviceSence.equals(TradeConstance.SERVICE_SCENE_BZJZYCX)){
				resultKColl = tradeInterface.doExecute(CD,connection);
				respCD = esbinterface.getRespSysHeadCD(serviceCode, serviceSence, openDay, nowDate, serno, "S", (String)resultKColl.getDataValue("ret_code"), (String)resultKColl.getDataValue("ret_msg"));
				CompositeData bodyCD = respCD.getStruct("BODY");
				bodyCD.addField("GUARANTEE_ACCT_NO", TagUtil.getEMPField(resultKColl.getDataValue("acct_no").toString(), FieldType.FIELD_STRING, 50, 0));
				bodyCD.addField("OWNER_TOTAL_AMT", TagUtil.getEMPField(Double.parseDouble(resultKColl.getDataValue("total_amt").toString()), FieldType.FIELD_DOUBLE, 20, 2));
			}
			//网银接口授信情况查询交易
			else if(serviceCode!=null&&serviceCode.equals(TradeConstance.SERVICE_CODE_SXQKCX2EBANK)&&serviceSence.equals(TradeConstance.SERVICE_SCENE_SXQKCX2EBANK)){
				resultKColl = tradeInterface.doExecute(CD,connection);
				respCD = esbinterface.getRespSysHeadCD(serviceCode, serviceSence, openDay, nowDate, serno, "S", (String)resultKColl.getDataValue("ret_code"), (String)resultKColl.getDataValue("ret_msg"));
				CompositeData bodyCD = respCD.getStruct("BODY");
				IndexedCollection IColl =(IndexedCollection) resultKColl.get("IColl");
					if(IColl != null && IColl.size() > 0){
						Array array = new Array(); 
						for(int i=0;i<IColl.size();i++){
							CompositeData sxCD = new CompositeData();
							KeyedCollection reflectSubKColl = (KeyedCollection)IColl.get(i);
							sxCD.addField("CREIDT_TYPE", TagUtil.getEMPField(reflectSubKColl.getDataValue("creidt_type"), FieldType.FIELD_STRING, 5, 0));
							sxCD.addField("CREDIT_LIMIT", TagUtil.getEMPField(reflectSubKColl.getDataValue("credit_limit"), FieldType.FIELD_DOUBLE, 16, 2));
							KeyedCollection lmt_amt_kc=new IqpServiceInterfaceImple().getAgrUsedInfoByArgNo((String)reflectSubKColl.getDataValue("agr_no"), "01", connection,context);
							sxCD.addField("USED_AMT", TagUtil.getEMPField(lmt_amt_kc.getDataValue("lmt_amt"), FieldType.FIELD_DOUBLE, 16, 2));
							BigDecimal credit_limit = BigDecimalUtil.replaceNull(reflectSubKColl.getDataValue("credit_limit"));
							BigDecimal lmt_amt = BigDecimalUtil.replaceNull(lmt_amt_kc.getDataValue("lmt_amt"));
							sxCD.addField("ACTUAL_BAL", TagUtil.getEMPField(credit_limit.subtract(lmt_amt), FieldType.FIELD_DOUBLE, 16, 2));
							sxCD.addField("CREDIT_EXPIRY_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("end_date"), FieldType.FIELD_STRING, 8, 0));
							sxCD.addField("CREDIT_STATUS", TagUtil.getEMPField(reflectSubKColl.getDataValue("lmt_status"), FieldType.FIELD_STRING, 2, 0));
							array.addStruct(sxCD);
						}
						bodyCD.addArray("CREIDT_LIMIT_ARRAY", array);
					}
			}
			//网银接口贷款信息查询
			else if(serviceCode!=null&&serviceCode.equals(TradeConstance.SERVICE_CODE_DKQKCX2EBANK)&&serviceSence.equals(TradeConstance.SERVICE_SCENE_DKQKCX2EBANK)){
				resultKColl = tradeInterface.doExecute(CD,connection);
				respCD = esbinterface.getRespSysHeadCD(serviceCode, serviceSence, openDay, nowDate, serno, "S", (String)resultKColl.getDataValue("ret_code"), (String)resultKColl.getDataValue("ret_msg"));
				CompositeData bodyCD = respCD.getStruct("BODY");
				IndexedCollection IColl =(IndexedCollection) resultKColl.get("IColl");
					if(IColl != null && IColl.size() > 0){
						Array array = new Array(); 
						for(int i=0;i<IColl.size();i++){
							CompositeData TempCD = new CompositeData();
							KeyedCollection reflectSubKColl = (KeyedCollection)IColl.get(i);
							TempCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("bill_no"), FieldType.FIELD_STRING, 50, 0));
							TempCD.addField("LOAN_KIND", TagUtil.getEMPField(reflectSubKColl.getDataValue("prd_id"), FieldType.FIELD_STRING, 20, 0));
							TempCD.addField("LOAN_ACCT_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("loan_acct_no"), FieldType.FIELD_STRING, 20, 0));
							TempCD.addField("LOAN_AMT", TagUtil.getEMPField(reflectSubKColl.getDataValue("loan_amt"), FieldType.FIELD_DOUBLE, 16, 2));
							TempCD.addField("LOAN_BALANCE", TagUtil.getEMPField(reflectSubKColl.getDataValue("loan_balance"), FieldType.FIELD_DOUBLE, 16, 2));
							TempCD.addField("START_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("distr_date"), FieldType.FIELD_STRING, 8, 0));
							TempCD.addField("END_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("end_date"), FieldType.FIELD_STRING, 8, 0));
							TempCD.addField("GUARANTEE_MODE", TagUtil.getEMPField(reflectSubKColl.getDataValue("dbfs"), FieldType.FIELD_STRING, 10, 0));
							TempCD.addField("STATUS", TagUtil.getEMPField(reflectSubKColl.getDataValue("status"), FieldType.FIELD_STRING, 10, 2));
							TempCD.addField("CLEARANCE_STATUS", TagUtil.getEMPField(reflectSubKColl.getDataValue("acc_status"), FieldType.FIELD_STRING, 10, 0));
							TempCD.addField("SETTLE_ACCT_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("acct_no"), FieldType.FIELD_STRING, 50, 0));
							array.addStruct(TempCD);
						}
						bodyCD.addArray("DD_ARRAY", array);
					}
			}
			
			//网银接口贴现票据查询
			else if(serviceCode!=null&&serviceCode.equals(TradeConstance.SERVICE_CODE_TXQKCX2EBANK)&&serviceSence.equals(TradeConstance.SERVICE_SCENE_TXQKCX2EBANK)){
				resultKColl = tradeInterface.doExecute(CD,connection);
				respCD = esbinterface.getRespSysHeadCD(serviceCode, serviceSence, openDay, nowDate, serno, "S", (String)resultKColl.getDataValue("ret_code"), (String)resultKColl.getDataValue("ret_msg"));
				CompositeData bodyCD = respCD.getStruct("BODY");
				IndexedCollection IColl =(IndexedCollection) resultKColl.get("IColl");
					if(IColl != null && IColl.size() > 0){
						Array array = new Array(); 
						for(int i=0;i<IColl.size();i++){
							CompositeData TempCD = new CompositeData();
							KeyedCollection reflectSubKColl = (KeyedCollection)IColl.get(i);
							TempCD.addField("BUSS_TYPE", TagUtil.getEMPField(reflectSubKColl.getDataValue("dscnt_type"), FieldType.FIELD_STRING, 50, 0));
							TempCD.addField("FACE_AMT", TagUtil.getEMPField(reflectSubKColl.getDataValue("drft_amt"),  FieldType.FIELD_DOUBLE, 16, 2));
							TempCD.addField("DISCOUNT_AMT", TagUtil.getEMPField(reflectSubKColl.getDataValue("pad_amt"), FieldType.FIELD_DOUBLE, 16, 2));
							TempCD.addField("DISCOUNT_INTEREST ", TagUtil.getEMPField(reflectSubKColl.getDataValue("dscnt_int"), FieldType.FIELD_DOUBLE, 16, 2));
							TempCD.addField("START_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("dscnt_date"), FieldType.FIELD_STRING, 8, 0));
							TempCD.addField("BILL_EXPIRY_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("porder_end_date"), FieldType.FIELD_STRING, 8, 0));
							TempCD.addField("GUARANTEE_PER", TagUtil.getEMPField(reflectSubKColl.getDataValue("security_rate"), FieldType.FIELD_DOUBLE, 10, 6));
							TempCD.addField("BILL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("porder_no"), FieldType.FIELD_STRING, 50, 0));
							TempCD.addField("GUARANTEE_MODE", TagUtil.getEMPField(reflectSubKColl.getDataValue("assure_main"), FieldType.FIELD_STRING, 10, 0));
							TempCD.addField("STATUS", TagUtil.getEMPField(reflectSubKColl.getDataValue("accp_status"), FieldType.FIELD_STRING, 10, 2));
							TempCD.addField("ACCT_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("paorg_acct_no"), FieldType.FIELD_STRING, 50, 0));
							array.addStruct(TempCD);
						}
						bodyCD.addArray("BILL_ARRAY", array);
					}
			}
			
			//网银接口垫款信息查询
			else if(serviceCode!=null&&serviceCode.equals(TradeConstance.SERVICE_CODE_DIANKQKCX2EBANK)&&serviceSence.equals(TradeConstance.SERVICE_SCENE_DIANKQKCX2EBANK)){
				resultKColl = tradeInterface.doExecute(CD,connection);
				respCD = esbinterface.getRespSysHeadCD(serviceCode, serviceSence, openDay, nowDate, serno, "S", (String)resultKColl.getDataValue("ret_code"), (String)resultKColl.getDataValue("ret_msg"));
				CompositeData bodyCD = respCD.getStruct("BODY");
				IndexedCollection IColl =(IndexedCollection) resultKColl.get("IColl");
					if(IColl != null && IColl.size() > 0){
						Array array = new Array(); 
						for(int i=0;i<IColl.size();i++){
							CompositeData TempCD = new CompositeData();
							KeyedCollection reflectSubKColl = (KeyedCollection)IColl.get(i);
							TempCD.addField("BUSS_TYPE", TagUtil.getEMPField(reflectSubKColl.getDataValue("pad_type"), FieldType.FIELD_STRING, 20, 0));
							TempCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("bill_no"), FieldType.FIELD_STRING, 50, 0));
							TempCD.addField("ADVANCE_CASH_AMT", TagUtil.getEMPField(reflectSubKColl.getDataValue("pad_amt"),  FieldType.FIELD_DOUBLE, 16, 2));
							TempCD.addField("ADVANCE_CASH_BALANCE", TagUtil.getEMPField(reflectSubKColl.getDataValue("pad_bal"),  FieldType.FIELD_DOUBLE, 16, 2));
							TempCD.addField("START_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("pad_date"), FieldType.FIELD_STRING, 8, 0));
							TempCD.addField("STATUS", TagUtil.getEMPField(reflectSubKColl.getDataValue("five_class"), FieldType.FIELD_STRING, 10, 0));
							array.addStruct(TempCD);
						}
						bodyCD.addArray("ADVANCE_CASH_ARRAY", array);
					}
			}
			
			//网银接口委托贷款查询
			else if(serviceCode!=null&&serviceCode.equals(TradeConstance.SERVICE_CODE_WTDKQKCX2EBANK)&&serviceSence.equals(TradeConstance.SERVICE_SCENE_WTDKQKCX2EBANK)){
				resultKColl = tradeInterface.doExecute(CD,connection);
				respCD = esbinterface.getRespSysHeadCD(serviceCode, serviceSence, openDay, nowDate, serno, "S", (String)resultKColl.getDataValue("ret_code"), (String)resultKColl.getDataValue("ret_msg"));
				CompositeData bodyCD = respCD.getStruct("BODY");
				IndexedCollection IColl =(IndexedCollection) resultKColl.get("IColl");
					if(IColl != null && IColl.size() > 0){
						Array array = new Array(); 
						for(int i=0;i<IColl.size();i++){
							CompositeData TempCD = new CompositeData();
							KeyedCollection reflectSubKColl = (KeyedCollection)IColl.get(i);
							TempCD.addField("LOAN_INT_RATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("reality_ir_y"),  FieldType.FIELD_DOUBLE, 10, 6));
							TempCD.addField("AMT", TagUtil.getEMPField(reflectSubKColl.getDataValue("loan_amt"),  FieldType.FIELD_DOUBLE, 16, 2));
							TempCD.addField("START_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("distr_date"), FieldType.FIELD_STRING, 8, 0));
							TempCD.addField("EXPIRY_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("end_date"), FieldType.FIELD_STRING, 8, 0));
							TempCD.addField("GUARANTEE_MODE", TagUtil.getEMPField(reflectSubKColl.getDataValue("assure_main"), FieldType.FIELD_STRING, 10, 0));
							TempCD.addField("DR_NAME", TagUtil.getEMPField(reflectSubKColl.getDataValue("dr_name"), FieldType.FIELD_STRING, 60, 0));
							TempCD.addField("CONSIGN_NAME", TagUtil.getEMPField(reflectSubKColl.getDataValue("consign_name"), FieldType.FIELD_STRING, 150, 0));
							TempCD.addField("STATUS", TagUtil.getEMPField(reflectSubKColl.getDataValue("five_class"), FieldType.FIELD_STRING, 10, 0));
							TempCD.addField("SETTLE_ACCT_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("acct_no"), FieldType.FIELD_STRING, 50, 0));
							array.addStruct(TempCD);
						}
						bodyCD.addArray("LOAN_MSG_ARRAY", array);
					}
			}
			
			//网银接口银承用信查询
			else if(serviceCode!=null&&serviceCode.equals(TradeConstance.SERVICE_CODE_YCQKCX2EBANK)&&serviceSence.equals(TradeConstance.SERVICE_SCENE_YCQKCX2EBANK)){
				resultKColl = tradeInterface.doExecute(CD,connection);
				respCD = esbinterface.getRespSysHeadCD(serviceCode, serviceSence, openDay, nowDate, serno, "S", (String)resultKColl.getDataValue("ret_code"), (String)resultKColl.getDataValue("ret_msg"));
				CompositeData bodyCD = respCD.getStruct("BODY");
				IndexedCollection IColl =(IndexedCollection) resultKColl.get("IColl");
					if(IColl != null && IColl.size() > 0){
						Array array = new Array(); 
						for(int i=0;i<IColl.size();i++){
							CompositeData TempCD = new CompositeData();
							KeyedCollection reflectSubKColl = (KeyedCollection)IColl.get(i);
							TempCD.addField("FACE_AMT", TagUtil.getEMPField(reflectSubKColl.getDataValue("drft_amt"),  FieldType.FIELD_DOUBLE, 16, 2));
							TempCD.addField("BILL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("bill_no"), FieldType.FIELD_STRING, 50, 0));
							TempCD.addField("START_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("isse_date"), FieldType.FIELD_STRING, 8, 0));
							TempCD.addField("END_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("porder_end_date"), FieldType.FIELD_STRING, 8, 0));
							TempCD.addField("GUARANTEE_PER", TagUtil.getEMPField(reflectSubKColl.getDataValue("security_rate"), FieldType.FIELD_DOUBLE, 10, 6));
							TempCD.addField("GUARANTEE_MODE", TagUtil.getEMPField(reflectSubKColl.getDataValue("assure_main"), FieldType.FIELD_STRING, 10, 0));
							TempCD.addField("CLEARANCE_STATUS", TagUtil.getEMPField(reflectSubKColl.getDataValue("accp_status"), FieldType.FIELD_STRING, 10, 0));
							TempCD.addField("ACCT_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("acct_no"), FieldType.FIELD_STRING, 50, 0));
							array.addStruct(TempCD);
						}
						bodyCD.addArray("DD_ARRAY", array);
					}
			}
			//网银接口保函业务查询
			else if(serviceCode!=null&&serviceCode.equals(TradeConstance.SERVICE_CODE_BHQKCX2EBANK)&&serviceSence.equals(TradeConstance.SERVICE_SCENE_BHQKCX2EBANK)){
				resultKColl = tradeInterface.doExecute(CD,connection);
				respCD = esbinterface.getRespSysHeadCD(serviceCode, serviceSence, openDay, nowDate, serno, "S", (String)resultKColl.getDataValue("ret_code"), (String)resultKColl.getDataValue("ret_msg"));
				CompositeData bodyCD = respCD.getStruct("BODY");
				IndexedCollection IColl =(IndexedCollection) resultKColl.get("IColl");
					if(IColl != null && IColl.size() > 0){
						Array array = new Array(); 
						for(int i=0;i<IColl.size();i++){
							CompositeData TempCD = new CompositeData();
							KeyedCollection reflectSubKColl = (KeyedCollection)IColl.get(i);
							TempCD.addField("FACE_AMT", TagUtil.getEMPField(reflectSubKColl.getDataValue("loan_amt"),  FieldType.FIELD_DOUBLE, 16, 2));
							TempCD.addField("START_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("distr_date"), FieldType.FIELD_STRING, 8, 0));
							TempCD.addField("END_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("end_date"), FieldType.FIELD_STRING, 8, 0));
							TempCD.addField("GUARANTEE_PER", TagUtil.getEMPField(reflectSubKColl.getDataValue("security_rate"), FieldType.FIELD_DOUBLE, 10, 6));
							TempCD.addField("GUARANTEE_MODE", TagUtil.getEMPField(reflectSubKColl.getDataValue("assure_main"), FieldType.FIELD_STRING, 10, 0));
							TempCD.addField("STATUS", TagUtil.getEMPField(reflectSubKColl.getDataValue("acc_status"), FieldType.FIELD_STRING, 10, 0));
							array.addStruct(TempCD);
						}
						bodyCD.addArray("GL_ARRAY", array);
					}
			}
			
			//电票特殊交易处理，必须执行业务逻辑
			else if(serviceCode!=null&&(TradeConstance.SERVICE_CODE_ECDS1.equals(serviceCode) || TradeConstance.SERVICE_CODE_ECDS2.equals(serviceCode) || TradeConstance.SERVICE_CODE_ECDS3.equals(serviceCode) || TradeConstance.SERVICE_CODE_ECDS4.equals(serviceCode) || TradeConstance.SERVICE_CODE_ECDS5.equals(serviceCode)|| TradeConstance.SERVICE_CODE_ECDS6.equals(serviceCode))){
				resultKColl = tradeInterface.doExecute(CD,connection);
				respCD = esbinterface.getRespSysHeadCD4ECDS(serviceCode, serviceSence, openDay, nowDate, serno, "S", resultKColl);
				paramValue.put("is_succ", "1");
				paramValue.put("resolve_type", "1");
				paramValue.put("deal_flag", "2");
				paramValue.put("trand_msg", "电票特殊业务处理成功");
				SqlClient.update("updateDaybatTrandInfo", pk1, paramValue, null, connection);
			}
			/**add by fw 2015-4-7 需求编号：【XD150318023】关于信贷系统增加循环贷类个贷产品需求 begin**/
			//微贷额度登记
			else if(serviceCode!=null&&serviceCode.equals(TradeConstance.SERVICE_CODE_LOANQUOREG)&&serviceSence.equals(TradeConstance.SERVICE_SCENE_LOANQUOREG)){
				resultKColl=tradeInterface.doExecute(CD,connection);
				respCD = esbinterface.getRespSysHeadCD(serviceCode, serviceSence, openDay, nowDate, serno, "S", (String)resultKColl.getDataValue("ret_code"), (String)resultKColl.getDataValue("ret_msg"));

			}
			/**add by fw 2015-4-7 需求编号：【XD150318023】关于信贷系统增加循环贷类个贷产品需求 end**/
			/**add by lisj 2015-4-7 需求编号：【XD150318023】关于信贷系统增加循环贷类个贷产品需求 begin**/
			//个人自助贷款协议发放交易
			else if(serviceCode!=null&&(TradeConstance.SERVICE_CODE_LOANINFOMAINTAIN.equals(serviceCode) && serviceSence.equals(TradeConstance.SERVICE_SCENE_LOANINFOMAINTAIN))){
				resultKColl = tradeInterface.doExecute(CD,connection);
				respCD = esbinterface.getRespSysHeadCD(serviceCode, serviceSence, openDay, nowDate, serno, "S", (String)resultKColl.getDataValue("ret_code"), (String)resultKColl.getDataValue("ret_msg"));
			}
			/**add by lisj 2015-4-7 需求编号：【XD150318023】关于信贷系统增加循环贷类个贷产品需求 end**/
			//保证金本金反算查询交易
			else if(serviceCode!=null&&serviceCode.equals(TradeConstance.SERVICE_CODE_DKQKCX2EBANK)&&serviceSence.equals(TradeConstance.SERVICE_SCENE_BZJBJFSCX)){
				resultKColl = tradeInterface.doExecute(CD,connection);
				respCD = esbinterface.getRespSysHeadCD(serviceCode, serviceSence, openDay, nowDate, serno, "S", (String)resultKColl.getDataValue("ret_code"), (String)resultKColl.getDataValue("ret_msg"));
				CompositeData bodyCD = respCD.getStruct("BODY");
				IndexedCollection IColl =(IndexedCollection) resultKColl.get("IColl");
					if(IColl != null && IColl.size() > 0){
						KeyedCollection reflectSubKColl = (KeyedCollection)IColl.get(0);
						bodyCD.addField("GUARANTEE_ACCT_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("bail_acct_no").toString(), FieldType.FIELD_STRING, 50, 0));
						bodyCD.addField("GUAR_FACE_TOTAL_AMT", TagUtil.getEMPField(Double.parseDouble(reflectSubKColl.getDataValue("security_amt").toString()), FieldType.FIELD_DOUBLE, 20, 2));
						bodyCD.addField("MIX_TERM", TagUtil.getEMPField(reflectSubKColl.getDataValue("term").toString(), FieldType.FIELD_INT, 10, 0));
					}
			}else{//非电票特殊交易则需要根据返回值判断是否执行业务逻辑，无条件返回成功反馈报文
				if(flag){//报文交易日期与当前系统营业日期一致执行业务逻辑处理
					resultKColl = tradeInterface.doExecute(CD,connection);
					respCD = esbinterface.getRespSysHeadCD(serviceCode, serviceSence, openDay, nowDate, serno, "S", (String)resultKColl.getDataValue("ret_code"), (String)resultKColl.getDataValue("ret_msg"));
					paramValue.put("is_succ", "1");
					paramValue.put("resolve_type", "1");
					paramValue.put("deal_flag", "2");
					paramValue.put("trand_msg", "日间处理成功");
					SqlClient.update("updateDaybatTrandInfo", pk1, paramValue, null, connection);
				}else{
					respCD = esbinterface.getRespSysHeadCD(serviceCode, serviceSence, openDay, nowDate, serno, "S", TradeConstance.RETCODE1,"接收报文成功，日终期间不进行业务逻辑处理");
					paramValue.put("is_succ", "2");
					paramValue.put("resolve_type", "");
					paramValue.put("deal_flag", "1");
					paramValue.put("trand_msg", "接收报文成功，日终期间不进行业务逻辑处理");
					SqlClient.update("updateDaybatTrandInfo", pk1, paramValue, null, connection);
				}
			}
			
			/** 打印后台反馈日志 */
			EMPLog.log("outReport", EMPLog.INFO, 0,  new String(PackUtil.pack(respCD), "UTF-8"));
		} catch (Exception e) {
			try {
				//电票特殊交易处理，返回实际执行结果
				if(serviceCode!=null&&(TradeConstance.SERVICE_CODE_ECDS1.equals(serviceCode) || TradeConstance.SERVICE_CODE_ECDS2.equals(serviceCode) || TradeConstance.SERVICE_CODE_ECDS3.equals(serviceCode) || TradeConstance.SERVICE_CODE_ECDS4.equals(serviceCode) || TradeConstance.SERVICE_CODE_ECDS5.equals(serviceCode))){
					respCD = esbinterface.getRespSysHeadCD(serviceCode, serviceSence, openDay, nowDate, serno, "F", (String)resultKColl.getDataValue("ret_code"), (String)resultKColl.getDataValue("ret_msg"));
					paramValue.put("is_succ", "2");
					paramValue.put("resolve_type", "");
					paramValue.put("deal_flag", "2");
					paramValue.put("trand_msg", (String)resultKColl.getDataValue("ret_msg"));
					SqlClient.update("updateDaybatTrandInfo", pk1, paramValue, null, connection);
				}else{//非电票特殊交易则无条件返回成功反馈报文
					respCD = esbinterface.getRespSysHeadCD(serviceCode, serviceSence, openDay, nowDate, serno, "S", TradeConstance.RETCODE1,"发生异常，先返回成功，信贷后续处理");
					paramValue.put("is_succ", "2");
					paramValue.put("resolve_type", "");
					paramValue.put("deal_flag", "1");
					paramValue.put("trand_msg", "发生异常，先返回成功，信贷后续处理");
					SqlClient.update("updateDaybatTrandInfo", pk1, paramValue, null, connection);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if(connection != null){
					ConnectionManager.releaseConnection(dataSource, connection);
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return respCD;
	}
}
