package com.yucheng.cmis.biz01line.esb.op;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.pack.standardxml.PackUtil;
import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.jdbc.ConnectionManager;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.NewTradeInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.Service;
import com.yucheng.cmis.biz01line.esb.interfaces.TradeInterface;
import com.yucheng.cmis.biz01line.esb.pub.FTPUtil;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.biz01line.iqp.msi.msiimple.IqpServiceInterfaceImple;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.ESBConstance;
import com.yucheng.cmis.pub.util.ESBUtil;
import com.yucheng.cmis.pub.util.TimeUtil;
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
public class DealEsbTrandOp implements Service {
	public static final String ESB_INTERFACE_CLASS = "com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple";
	NewTradeInterface tradeInterface;
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
			connection.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取系统头中交易码、交易场景判断处理业务逻辑实现类
	 * @param CD 请求报文结构体
	 * 增加返回值，用于后续判断是否需要执行业务逻辑处理（交易日期与当前营业日不一致不处理业务逻辑，待日终批处理结束后执行）
	 * @throws Exception 
	 * 
	 */
	public Boolean getTranExecute(JSONObject reqJson) throws Exception{
		try {
			 JSONObject jsonSysHeadObj = (JSONObject) reqJson.get("SYS_HEAD");
		      
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  reqJson.toString() );
			Map<String, String> param = new HashMap<String, String>();
			String error_res="2";//默认来源为日终
			/** 取出请求系统头中数据信息 */
			esbinterface = (ESBInterface)Class.forName(ESB_INTERFACE_CLASS).newInstance();  
			String serviceCode =  jsonSysHeadObj.getString("SvcCd");
		    String serviceSence =jsonSysHeadObj.getString("ScnCd");
		      
		      
			serno = (String)jsonSysHeadObj.getString("GlblSeqNo");
			if (StringUtils.isEmpty(serno)){
				throw new RuntimeException("SYS_HEAD 中的 GlblSeqNo 字段不能为空！");
			}
			String tranDate = (String)jsonSysHeadObj.getString("TxnDt");
			openDay = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			context.put("OPENDAY", openDay);
			/** ------------接收到交易信息，先保存改交易信息，保存信息文件名规则为：交易时间（年月日）+_+交易码+_+授权码.xml---------------- */
			String localFile = FTPUtil.getLocalFileNameForJson(serviceCode, serviceSence, serno, reqJson, tranDate);
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
			KeyedCollection reqSysKColl = new KeyedCollection();
			reqSysKColl.put("SERVICE_CODE", serviceCode);
			reqSysKColl.put("SERVICE_SCENE", serviceSence);
			/** 通过交易码查询所配置的交易实现类，决定所作的交易处理 */
			String impleClass = (String)SqlClient.queryFirst("queryEsbConfig", reqSysKColl, null, connection);
			tradeInterface = (NewTradeInterface)Class.forName(impleClass.trim()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("调用信贷系统【服务码"+serviceCode+"】"+"【场景码"+serviceSence+"】出错，\r错误信息为："+e.getMessage());
		}
		return succ;
	}
	/**
	 * 交易监听程序执行入口
	 */
	public String exec(JSONObject reqJson) { 
		String reJson ="";
		KeyedCollection resultKColl = new KeyedCollection();
		Map<String, String> paramValue = new HashMap<String, String>();
		String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());//精确到毫秒
		long time = System.currentTimeMillis();//计算交易耗时，单位ms
		KeyedCollection esbTradeInfo = new KeyedCollection();
		try {
			/**-- 初始化数据库连接 --*/
			init();
			/**-- 将交易文件写入本地，返回是否需要处理业务逻辑标志 --*/
			Boolean flag = getTranExecute(reqJson); 
			KeyedCollection reqKColl = ESBUtil.jsonToKcoll(reqJson.toString());
			
			//插入交易流水
			esbTradeInfo = ESBUtil.addEsbTradeInfo((KeyedCollection)reqKColl.getDataElement("SYS_HEAD"), reqJson.toString(), connection);
			
			resultKColl = tradeInterface.doExecute(reqKColl,connection);
			
			
			
			JSONObject retJson = new JSONObject();
			JSONObject sysHead = new JSONObject();
			JSONArray retInfArry= new JSONArray();
			JSONObject retInf = new JSONObject();
            if(resultKColl.containsKey("RetCd")&&"000000".equals((String)resultKColl.getDataValue("RetCd"))){
            	retInf.put("RetCd","000000");
            	retInf.put("RetInf","交易处理成功");
            	esbTradeInfo.put("suss_flag","1");
            	connection.commit();
            }else{
            	retInf.put("RetCd",resultKColl.get("RetCd"));
            	retInf.put("RetInf",resultKColl.get("RetInf"));
            	esbTradeInfo.put("suss_flag","2");
            	connection.rollback();
            }
            
            
            
            retInfArry.add(retInf);
            String tranDate = TimeUtil.getDateyyyyMMdd();
			String tranTime = TimeUtil.getDateHHmmss();
			String seqNum = String.valueOf((BigDecimal)SqlClient.queryFirst("queryEsbSeq", "", null, connection));
			  
			sysHead.put("SvcCd",reqJson.get("SvcCd")); 
			sysHead.put("ScnCd", reqJson.get("ScnCd"));  
			sysHead.put("CnsmrCnlTp",reqJson.get("CnsmrCnlTp"));  
			sysHead.put("CnsmrSysId", reqJson.get("CnsmrSysId"));  
			sysHead.put("CnsmrSysSrlNo", reqJson.get("CnsmrSysSrlNo"));  
			sysHead.put("PvdrSysId", "300400");  
			sysHead.put("PvdrSysSrlNo", seqNum);  
			sysHead.put("PvdrSrvInd", "");  
			sysHead.put("SrcCnsmrSysId", "");  
			sysHead.put("GlblSrlNo", reqJson.get("GlblSrlNo"));  
			sysHead.put("TxnDt", tranDate);  
			sysHead.put("TxnTm", tranTime);   
			sysHead.put("FileFlg", "0");  
			sysHead.put("FileNm", "");  
			sysHead.put("SvcVerNo", "");  
			sysHead.put("TmlNo", "");  
			sysHead.put("MACVal", ""); 
			sysHead.put("RetInfArry",retInfArry);
			retJson.put("SYS_HEAD",sysHead);
			
			JSONObject appHead = new JSONObject();
			 
			appHead.put("BrCd", reqJson.get("BrCd"));
			appHead.put("TlrNo", reqJson.get("TlrNo"));
			appHead.put("TurnPgFlg", "");
			appHead.put("PgDsplLineNum", "");
			appHead.put("CrnPgRcrdNo", "");
			appHead.put("TotLineNum", "");
			appHead.put("SmzgFlg", "");
			 
			retJson.put("APP_HEAD",appHead);
			if(resultKColl.containsKey("body")){
				KeyedCollection bodyKColl = (KeyedCollection) resultKColl.getDataElement("body");
				JSONObject body = ESBUtil.kCollToJson(bodyKColl);
				retJson.put("BODY", body); 
			}
			reJson = retJson.toString();
			
			
			if(esbTradeInfo != null){/**更新交易流水表信息**/
 				long time2 = System.currentTimeMillis();
 				String pvdrSysId = "300400";  
				esbTradeInfo.put("times",new BigDecimal(time2 - time));//计算交易耗时，单位ms 
				esbTradeInfo.put("response_report", reJson);
				esbTradeInfo.put("source_sys_id", pvdrSysId);
				 
				try{
					ESBUtil.updateEsbTradeInfo(esbTradeInfo, connection);//保存响应报文会报字段长度问题***********
				}catch(Exception e){
					EMPLog.log(ESBConstance.ESB_LOG_ID, EMPLog.INFO,0, "+++++++++++更新交易日志失败："+e.getMessage()+"=======+++++++++++\n");
				}
				connection.commit();
			}else{
				EMPLog.log(ESBConstance.ESB_LOG_ID, EMPLog.INFO,0, "+++++++++++交易流水对象esbTradeInfo不存在！+++++++++++\n");
			}
		} catch (Exception e) {  
			e.printStackTrace();
			if(esbTradeInfo != null){/**更新交易流水表信息**/
 				long time2 = System.currentTimeMillis();
 				String pvdrSysId = "300400";  
				esbTradeInfo.put("times",new BigDecimal(time2 - time));//计算交易耗时，单位ms 
				esbTradeInfo.put("response_report", reJson);
				esbTradeInfo.put("source_sys_id", pvdrSysId);
				 
				try{
					ESBUtil.updateEsbTradeInfo(esbTradeInfo, connection);//保存响应报文会报字段长度问题***********
					connection.commit();
				}catch(Exception ee){
					EMPLog.log(ESBConstance.ESB_LOG_ID, EMPLog.INFO,0, "+++++++++++更新交易日志失败："+e.getMessage()+"=======+++++++++++\n");
				}
				;
			}else{
				EMPLog.log(ESBConstance.ESB_LOG_ID, EMPLog.INFO,0, "+++++++++++交易流水对象esbTradeInfo不存在！+++++++++++\n");
			}
		} finally {
			try {
				if(connection != null){
					ConnectionManager.releaseConnection(dataSource, connection);
				}                
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return reJson;
	}

	 
}
