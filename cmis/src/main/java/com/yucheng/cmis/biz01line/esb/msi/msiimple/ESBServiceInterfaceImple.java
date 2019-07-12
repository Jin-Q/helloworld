package com.yucheng.cmis.biz01line.esb.msi.msiimple;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.client.ESBClient;
import com.dcfs.esb.pack.standardxml.PackUtil;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.biz01line.cus.msi.CusServiceInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.FTPUtil;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualService;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.ESBUtil;
import com.yucheng.cmis.util.TableModelUtil;

public class ESBServiceInterfaceImple extends CMISModualService implements
		ESBServiceInterface {
	private static final String _ACCT_QUERY_SVC = "30130001";
	private static final String _TRADE_SEQ_QUERY_SVC = "30130002";
	private static final String _ACCT_QUERY_SCN = "01";
	private static final String _TRADE_SEQ_QUERY_SCN = "01";
	
	private static final Logger logger = Logger.getLogger(ESBServiceInterfaceImple.class);
	/**
	 * 获取系统头报文体结构体
	 * @param code 服务代码
	 * @param scene 服务场景
	 * @param consumerId 消费者ID
	 * @param serno 消费者流水号
	 * @param context 上下文
	 * @return 系统报文头结构体 CompositeData
	 * @throws Exception
	 */
	public CompositeData getSysHeadCD(String code, String scene, String consumerId, String serno, Context context) throws Exception {
		CompositeData sysHead = new CompositeData();
		String openDay = (String)context.getDataValue(CMISConstance.OPENDAY);
		//String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());//精确到毫秒
		String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		
		sysHead.addField("SERVICE_CODE", TagUtil.getEMPField(code, FieldType.FIELD_STRING, 11, 0));
		sysHead.addField("SERVICE_SCENE", TagUtil.getEMPField(scene, FieldType.FIELD_STRING, 2, 0));
		sysHead.addField("CONSUMER_ID", TagUtil.getEMPField(consumerId, FieldType.FIELD_STRING, 8, 0));
		sysHead.addField("TRAN_DATE", TagUtil.getEMPField(openDay.replaceAll("-", ""), FieldType.FIELD_STRING, 10, 0));
		sysHead.addField("TRAN_TIMESTAMP", TagUtil.getEMPField(nowDate.substring(12, 20).replaceAll(":", ""), FieldType.FIELD_STRING, 12, 0));
		sysHead.addField("CONSUMER_SEQ_NO", TagUtil.getEMPField(serno, FieldType.FIELD_STRING, 42, 0));
		
		return sysHead;
	}

	/**
	 * 获取应用层报文结构体（待定、预留）
	 * @return
	 * @throws Exception
	 */
	public CompositeData getAppHeadCD() throws Exception {
		return null;
	}
	/**
	 * 获取本地扩展报文头结构体
	 * @return
	 * @throws Exception
	 */
	public CompositeData getLocalHeadCD() throws Exception {
		return null;
	}
	/**
	 * 贷款发放授权接口
	 * @param reqCD 请求报文体（包括系统报文头、应用报文头、报文体）
	 * @return 返回结果结构体
	 * @throws Exception 
	 */
	public CompositeData tradeDKFFSQ(CompositeData reqCD) throws Exception {
		CompositeData rspData = ESBClient.request(reqCD);
		return rspData;
	}
	
	/**
	 * 保证金账户查询
	 * @param  传入保证金账号，context，connection
	 * @return 返回报文头信息及账户信息
	 * @throws Exception 
	 */
	public KeyedCollection tradeBZJZH(String bail_acct_no,Context context,Connection connection) throws Exception {
		try{
			/** 封装发送报文信息 */
			CompositeData reqCD= new CompositeData();
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			/** 系统头 */
			reqCD.addStruct("SYS_HEAD", this.getSysHeadCD("11003000007", "16", TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.CONSUMERID, context));
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", this.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			/** 封装报文体 */
			CompositeData bodyCD = new CompositeData();
			bodyCD.addField("GUARANTEE_ACCT_NO", TagUtil.getEMPField(bail_acct_no, FieldType.FIELD_STRING, 50, 0));
			reqCD.addStruct(TradeConstance.ESB_BODY, bodyCD);
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			
			if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
				
			}else{
				/** 解析反馈报文体 */
				KeyedCollection kColl = this.getRespBodyCD4KColl(retCD);
				kColl.setName("BODY");
				retKColl.addDataElement(kColl);
			}
			return retKColl;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("保证金账户查询通讯失败"+e.getMessage());
		}
	}
	
	/**
	 * 个人半年日均查询
	 * @param  传入个人客户码，配偶客户码(若无可传空)，context，connection
	 * @return 返回报文头信息及个人半年日均金额
	 * @throws Exception 
	 */
	public KeyedCollection tradeBNRJ(String cus_id,String spouse_cus_id,Context context,Connection connection) throws Exception {
		try{
			/** 封装发送报文信息 */
			CompositeData reqCD= new CompositeData();
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			/** 系统头 */
			reqCD.addStruct("SYS_HEAD", this.getSysHeadCD("11003000004", "11", TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.CONSUMERID, context));
			CompositeData LOCAL_HEAD= new CompositeData();
			LOCAL_HEAD.addField("FILE_FLAG", TagUtil.getEMPField("0", FieldType.FIELD_STRING, 1, 0));
			reqCD.addStruct("LOCAL_HEAD", LOCAL_HEAD);
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", this.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			/** 封装报文体 */
			CompositeData bodyCD = new CompositeData();
			bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(cus_id, FieldType.FIELD_STRING, 20, 0));//个人客户码
			bodyCD.addField("MATE_CLIENT_NO", TagUtil.getEMPField(spouse_cus_id, FieldType.FIELD_STRING, 20, 0));//配偶客户码
			reqCD.addStruct(TradeConstance.ESB_BODY, bodyCD);
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			
			if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
				
			}else{
				/** 解析反馈报文体 */
				KeyedCollection kColl = this.getRespBodyCD4KColl(retCD);
				kColl.setName("BODY");
				retKColl.addDataElement(kColl);
			}
			return retKColl;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("个人半年日均查询通讯失败"+e.getMessage());
		}
	}
	
	/**
	 * 账户信息查询 该方法暂时废除 modified by huangtao 2019/02/28
	 * @param  传入账户账号，context，connection
	 * @return 返回报文头信息及账户信息
	 * @throws Exception 
	 */
	public KeyedCollection tradeZHZH_UNUSE(String acct_no,Context context,Connection connection) throws Exception {
		try{
			/** 封装发送报文信息 */
			CompositeData reqCD= new CompositeData();
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			/** 系统头 */
			reqCD.addStruct("SYS_HEAD", this.getSysHeadCD("11003000007", "17", TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.CONSUMERID, context));
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", this.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			/** 封装报文体 */
			CompositeData bodyCD = new CompositeData();
			bodyCD.addField("ACCT_NO", TagUtil.getEMPField(acct_no, FieldType.FIELD_STRING, 50, 0));
			reqCD.addStruct(TradeConstance.ESB_BODY, bodyCD);
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			
			if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
				
			}else{
				/** 解析反馈报文体 */
				KeyedCollection kColl = this.getRespBodyCD4KColl(retCD);
				kColl.setName("BODY");
				retKColl.addDataElement(kColl);
			}
			return retKColl;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("账户信息查询通讯失败"+e.getMessage());
		}
	}
	/**
	 * 账户信息查询
	 * @param  传入账户账号，context，connection
	 * @return 返回报文头信息及账户信息
	 * @throws Exception 
	 */
	public KeyedCollection tradeZHZH(String acct_no,Context context,Connection connection) throws Exception {
		try{
			/** 封装发送报文信息 */
			KeyedCollection reqHead = new KeyedCollection();
//			组装报文头中服务代码和场景代码
			reqHead.put("SvcCd", _ACCT_QUERY_SVC);//账户信息查询服务代码
			reqHead.put("ScnCd",_ACCT_QUERY_SCN);//(01)账户基本信息查询场景代码
			//跟核心交互的接口在头里还得传这三个参数
			reqHead.put("TxnMd","ONLINE");
			reqHead.put("UsrLngKnd","CHINESE");
			reqHead.put("jkType","cbs");
			/** 封装报文体 */
			KeyedCollection reqBody = new KeyedCollection();
			reqBody.put("AcctNoCrdNo", acct_no);
			reqBody.put("Ccy", "CNY");
			/** 执行发送操作 */
			KeyedCollection rsp = ESBUtil.sendEsbMsg(reqHead, reqBody);
			
			/** 解析反馈报文头判断该笔交易成功与否 */
			KeyedCollection rspSysHead = (KeyedCollection)rsp.get("SYS_HEAD");
			IndexedCollection retInfoIColl = (IndexedCollection) rspSysHead.getDataElement("RetInfArry");
			Object obj = retInfoIColl.get(0);
			KeyedCollection returnKColl = (KeyedCollection)rsp.get("SYS_HEAD");
			if(obj instanceof KeyedCollection){
				KeyedCollection kc = (KeyedCollection)obj;
				String retCd = (String) kc.getDataValue("RetCd");
				String retSts = (String) kc.getDataValue("RetInf");
				returnKColl.put("RET_CODE", retCd);
				returnKColl.put("RET_STATUS", retSts);
				returnKColl.addDataElement(rsp.getDataElement("BODY"));
			}
			return returnKColl;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("账户信息查询通讯失败"+e.getMessage());
		}
	}
	
	/**
	 * 获取系统头报文体结构体
	 * @param code 服务代码
	 * @param scene 服务场景
	 * @param consumerId 消费者ID
	 * @param serno 消费者流水号
	 * @param userId 登录人ID
	 * @param orgId 登录机构ID
	 * @param sysId 交易发送系统
	 * @param context 上下文
	 * @return 系统报文头结构体 CompositeData
	 * @throws Exception
	 */
	public CompositeData getSysHeadCD(String code, String scene, String consumerId, String serno,String userId,String userName, String orgId, String sysId, Context context) throws Exception {
		CompositeData sysHead = new CompositeData();
		String openDay = (String)context.getDataValue(CMISConstance.OPENDAY);
		String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());//精确到毫秒
		
		sysHead.addField("SERVICE_CODE", TagUtil.getEMPField(code, FieldType.FIELD_STRING, 30, 0));
		sysHead.addField("SERVICE_SCENE", TagUtil.getEMPField(scene, FieldType.FIELD_STRING, 2, 0));
		sysHead.addField("CONSUMER_ID", TagUtil.getEMPField(consumerId, FieldType.FIELD_STRING, 6, 0));
		sysHead.addField("TRAN_DATE", TagUtil.getEMPField(openDay.replaceAll("-", ""), FieldType.FIELD_STRING, 8, 0));
		sysHead.addField("TRAN_TIMESTAMP", TagUtil.getEMPField(nowDate.substring(12, 23).replaceAll(":", ""), FieldType.FIELD_STRING, 9, 0));
		sysHead.addField("CONSUMER_SEQ_NO", TagUtil.getEMPField(serno, FieldType.FIELD_STRING, 52, 0));
		sysHead.addField("USER_ID", TagUtil.getEMPField(userId, FieldType.FIELD_STRING, 50, 0));
		sysHead.addField("USER_NAME", TagUtil.getEMPField(userName, FieldType.FIELD_STRING, 150, 0));
		sysHead.addField("BRANCH_ID", TagUtil.getEMPField(orgId, FieldType.FIELD_STRING, 20, 0));
		
		return sysHead;
	}
	
	/**
	 * 获取应用层报文头结构体（待定、预留）
	 * @param userId 操作人员ID
	 * @param userName 操作人员名称
	 * @param orgId 操作人员所在机构
	 * @param sysId 系统ID
	 * @return 应用报文头结构体
	 * @throws Exception
	 */
	public CompositeData getAppHeadCD(String userId, String userName, String orgId, String sysId) throws Exception {
		CompositeData appHead = new CompositeData();
		appHead.addField("USER_ID", TagUtil.getEMPField(userId, FieldType.FIELD_STRING, 50, 0));
		appHead.addField("USER_NAME", TagUtil.getEMPField(userName, FieldType.FIELD_STRING, 150, 0));
		appHead.addField("BRANCH_ID", TagUtil.getEMPField(orgId, FieldType.FIELD_STRING, 20, 0));
		appHead.addField("CONSUMER_ID", TagUtil.getEMPField(sysId, FieldType.FIELD_STRING, 6, 0));
		return appHead;
	}
	
	/**
	 * 解析返回结构体的系统报文头
	 * @param respCD 返回报文结构体
	 * @return EMP KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getRespSysHeadCD(CompositeData respCD) throws Exception {
		if(respCD == null){
			return null;
		}
		CompositeData respSysHeadCD = respCD.getStruct(TradeConstance.ESB_SYS_HEAD);
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("RET_STATUS", respSysHeadCD.getField("RET_STATUS").strValue());
		kColl.addDataField("RET_CODE",respSysHeadCD.getArray("RET").getStruct(0).getField("RET_CODE").strValue());
		kColl.addDataField("RET_MSG",respSysHeadCD.getArray("RET").getStruct(0).getField("RET_MSG").strValue());
		kColl.setName("SYS_HEAD");
		return kColl;
	}
	
	/**
	 * 解析返回结构体报文，转换为信贷EMP中的KeyedCollection
	 * @param respCD 返回报文结构体
	 * @return EMP KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getRespBodyCD4KColl(CompositeData respCD) throws Exception {
		KeyedCollection respBodyKColl = null;
		CompositeData reqBodyCD = respCD.getStruct(TradeConstance.ESB_BODY);
		respBodyKColl = TagUtil.replaceCD2KColl(reqBodyCD);
		respBodyKColl.setName(TradeConstance.ESB_BODY);
		return respBodyKColl;
	}
	
	/**
	 * 解析返回结构的系统头APP_HEAD 
	 * @param respCD 返回报文结构体
	 * @return EMP String
	 * @throws Exception
	 */
	public String getRespApp4String(CompositeData respCD) throws Exception {
		CompositeData reqApp = respCD.getStruct(TradeConstance.ESB_APP_HEAD);
		String totalRowsR = (String)reqApp.getField("TOTAL_ROWS").strValue();
		return totalRowsR;
	}
	/**
	 * 交易流水查询接口
	 * @param  传入账户号，context，connection
	 * @return 返回交易明细iColl
	 * @throws Exception 
	 */
	public KeyedCollection tradeJYLS(String acctNo,Context context,Connection connection,PageInfo pageInfo) throws Exception {
		try{
			/** 封装发送报文信息 */
			KeyedCollection reqHead = new KeyedCollection();
			KeyedCollection reqKcoll = new KeyedCollection();
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			reqHead.put("SvcCd", _TRADE_SEQ_QUERY_SVC);//交易流水明细查询服务代码
			reqHead.put("ScnCd",_TRADE_SEQ_QUERY_SCN);//交易流水明细查询场景代码
			
			//跟核心交互的接口在头里还得传这三个参数
			reqHead.put("TxnMd","ONLINE");
			reqHead.put("UsrLngKnd","CHINESE");
			reqHead.put("jkType","cbs");
			//设置分页信息
			String page  = String.valueOf(pageInfo.pageSize);
			
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
			
			reqHead.put("TurnPgFlg", "1");//翻译标志，默认1
			reqHead.put("PgDsplLineNum", String.valueOf(pageInfo.pageSize));//每页显示条数
			reqHead.put("CrnPgRcrdNo", String.valueOf(pageInfo.pageIdx));//当前页记录号			
			
			/** 封装报文体 */
//			KeyedCollection retKcoll = (KeyedCollection)SqlClient.queryFirst("queryAcctNoByBillNo", bill_no, null, connection);
			reqKcoll.put("AcctNoCrdNo",acctNo);//账号
			reqKcoll.put("BegDt",String.valueOf(Integer.parseInt(((String)context.getDataValue("OPENDAY")).replaceAll("-", "").substring(0, 4))-1)+((String)context.getDataValue("OPENDAY")).replaceAll("-", "").substring(4, 8)       );//起始日期/开始日期
			reqKcoll.put("EndDt", ((String)context.getDataValue("OPENDAY")).replaceAll("-", ""));//结束日期/终止日期 //只支持12个月内
			reqKcoll.put("TxnMdTp", "A");//交易方式类型
			reqKcoll.put("TxnSrlNoTp","1");//交易流水类型
			/** 执行发送操作 */
			KeyedCollection rspKColl = ESBUtil.sendEsbMsg(reqHead, reqKcoll);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  rspKColl.toString());
			/** 解析反馈报文头判断该笔交易成功与否 */
			KeyedCollection rspSysHead = (KeyedCollection)rspKColl.get("SYS_HEAD");
			IndexedCollection retInfoIColl = (IndexedCollection) rspSysHead.getDataElement("RetInfArry");
			Object obj = retInfoIColl.get(0);
			KeyedCollection returnKColl = (KeyedCollection)rspKColl.get("SYS_HEAD");
			if(obj instanceof KeyedCollection){
				KeyedCollection kc = (KeyedCollection)obj;
				String retCd = (String) kc.getDataValue("RetCd");
				String retSts = (String) kc.getDataValue("RetInf");
				returnKColl.put("RET_CODE", retCd);
				returnKColl.put("RET_STATUS", retSts);

				returnKColl.addDataElement(rspKColl.getDataElement("APP_HEAD"));
				returnKColl.addDataElement(rspKColl.getDataElement("BODY"));
			}
			return returnKColl;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("交易明细接口查询失败");
		}
	}
	/**
	 * 还款计划查询接口
	 * @param  传入借据号，context，connection
	 * @return 返回交易明细iColl
	 * @throws Exception 
	 */
	public KeyedCollection tradeHKJH(String bill_no,Context context,Connection connection,PageInfo pageInfo) throws Exception {
		try{
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData();
		String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
		/** 系统头 */
		reqCD.addStruct("SYS_HEAD", this.getSysHeadCD("02003000002", "11", TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.CONSUMERID, context));
		/** 应用头 */
		reqCD.addStruct("APP_HEAD", this.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
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
		bodyCD.addField("DUEBILL_NO", TagUtil.getEMPField(bill_no, FieldType.FIELD_STRING, 50, 0));
		reqCD.addStruct(TradeConstance.ESB_BODY, bodyCD);
		/** 执行发送操作 */
		CompositeData retCD = ESBClient.request(reqCD);
		/** 打印后台反馈日志 */
		EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
		/** 解析反馈报文头 */
		KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
		if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
			
		}else{
			/** 解析反馈报文体 */
			KeyedCollection kColl = this.getRespBodyCD4KColl(retCD);
			String totalRowsR = this.getRespApp4String(retCD);
			IndexedCollection respBodyIColl  = (IndexedCollection)kColl.getDataElement("REPAY_PLAN_ARRAY");
			respBodyIColl.setName("BODY");
			retKColl.addDataField("totalRowsR", totalRowsR);
			retKColl.addDataElement(respBodyIColl);
		}
		return retKColl;
	}catch(Exception e){
		throw new Exception("还款计划查询接口失败");
	}
	}
	/**
	 * 授权撤销接口
	 * @param  授权信息，context，connection
	 * @return 返回报文头信息及撤销标志
	 * @throws Exception 
	 */
	public KeyedCollection tradeSQCX(KeyedCollection auKColl,Context context,Connection connection) throws Exception {
		try{
			/** 封装发送报文信息 */
			CompositeData reqCD= new CompositeData();
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			/** 系统头 */
			reqCD.addStruct("SYS_HEAD", this.getSysHeadCD("02004000001", "01", TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.CONSUMERID, context));
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", this.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			/** 封装报文体 */
			CompositeData bodyCD = new CompositeData();
			bodyCD.addField("GEN_GL_NO", TagUtil.getEMPField(auKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("TRAN_TYPE", TagUtil.getEMPField(auKColl.getDataValue("TRAN_TYPE"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("OLD_GEN_GL_NO", TagUtil.getEMPField(auKColl.getDataValue("OLD_GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
			reqCD.addStruct(TradeConstance.ESB_BODY, bodyCD);
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
				
			}else{
				/** 解析反馈报文体 */
				KeyedCollection kColl = this.getRespBodyCD4KColl(retCD);
				kColl.setName("BODY");
				retKColl.addDataElement(kColl);
			}
			return retKColl;
		}catch(Exception e){
			throw new Exception("授权撤销接口失败");
		}
	}
	/**
	 * 抵质押物权证出/入库
	 * @param  出/入库申请业务流水号 serno，context，connection
	 * @return 返回报文头信息及撤销标志
	 * @throws Exception 
	 */
	public KeyedCollection tradeDZYWQZCRK(String serno,Context context, Connection connection) throws Exception {
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData();
		try{
			String authorize_no = null;
			//获取dao
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection PvpKColl = dao.queryFirst("PvpAuthorize",null,"where serno='"+serno+"'",connection);
			authorize_no = (String) PvpKColl.getDataValue("authorize_no");
			/** 系统头 */
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(TradeConstance.SERVICE_CODE_ZYGL, TradeConstance.SERVICE_SCENE_ZYCRKSQ, TradeConstance.CONSUMERID, TagUtil.getSysHeadSerno(authorize_no, context),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			/** 封装写入文件的报文信息，包括整体报文信息 */
			CompositeData fileCD = new CompositeData();
			fileCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(TradeConstance.SERVICE_CODE_ZYGL, TradeConstance.SERVICE_SCENE_ZYCRKSQ, TradeConstance.CONSUMERID, TagUtil.getSysHeadSerno(authorize_no, context),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			/** 封装写入文件的报文信息，包括整体报文信息 */
			CompositeData bodyListCD = new CompositeData();
			Array bodyArr = new Array();
			IndexedCollection pvpIColl = dao.queryList("PvpAuthorize", " where serno = '"+serno+"' and status is null", connection);
			if(pvpIColl != null && pvpIColl.size() > 0){
				for(int i=0;i<pvpIColl.size();i++){
					KeyedCollection authKColl1 = (KeyedCollection)pvpIColl.get(i);
					KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl1);
					CompositeData bodyArrCD = new CompositeData();
					bodyArrCD.addField("GEN_GL_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("GEN_GL_NO")), FieldType.FIELD_STRING, 30, 0));
					bodyArrCD.addField("PLEDGE_NO", TagUtil.getEMPField(reflectKColl.getDataValue("PLEDGE_NO"), FieldType.FIELD_STRING, 52, 0));
					bodyArrCD.addField("TICKET_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("TICKET_TYPE"), FieldType.FIELD_STRING, 10, 0));
					bodyArrCD.addField("TICKET_NO", TagUtil.getEMPField(reflectKColl.getDataValue("TICKET_NO"), FieldType.FIELD_STRING, 100, 0));
					bodyArrCD.addField("IN_OUT_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("IN_OUT_TYPE"), FieldType.FIELD_STRING, 10, 0));
					//bodyArrCD.addField("COMMISSION", TagUtil.getEMPField(reflectKColl.getDataValue("COMMISSION"), FieldType.FIELD_DOUBLE, 20, 2));
					bodyArrCD.addField("LOT_NUM", TagUtil.getEMPField(reflectKColl.getDataValue("LOT_NUM"), FieldType.FIELD_INT, 5, 0));
					bodyArrCD.addField("CLIENT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CLIENT_NO"), FieldType.FIELD_STRING, 40, 0));
					bodyArrCD.addField("CLIENT_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("CLIENT_NAME"), FieldType.FIELD_STRING, 80, 0));
					
					bodyArrCD.addField("BANK_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BANK_ID"), FieldType.FIELD_STRING, 10, 0));
					bodyArrCD.addField("BRANCH_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BRANCH_ID"), FieldType.FIELD_STRING, 10, 0));
					bodyArrCD.addField("PREDGE_AMT", TagUtil.getEMPField(reflectKColl.getDataValue("PREDGE_AMT"), FieldType.FIELD_DOUBLE, 16, 2));
					bodyArrCD.addField("PLEDGE_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("PLEDGE_TYPE"), FieldType.FIELD_STRING, 10, 0));
					bodyArrCD.addField("CCY", TagUtil.getEMPField(reflectKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 3, 0));
					bodyArr.addStruct(bodyArrCD);
				}
			}
			bodyListCD.addArray("BASE_BODY", bodyArr);
			fileCD.addStruct("BODY", bodyListCD);
			/** 应用头 */
			CompositeData APP_HEAD= esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM);
			APP_HEAD.addField("FILE_NAME",TagUtil.getEMPField(FTPUtil.getFileName(TradeConstance.SERVICE_CODE_ZYGL, TradeConstance.SERVICE_SCENE_ZYCRKSQ, authorize_no), FieldType.FIELD_STRING, 30, 0));
			APP_HEAD.addField("FILE_PATH",TagUtil.getEMPField(FTPUtil.getFilePath(), FieldType.FIELD_STRING, 512, 0));
			APP_HEAD.addField("TOTAL_ROWS",TagUtil.getEMPField(pvpIColl.size(), FieldType.FIELD_STRING, 12, 0));
			reqCD.addStruct("APP_HEAD", APP_HEAD);
			/**扩展头**/
			CompositeData LOCAL_HEAD= new CompositeData();
			LOCAL_HEAD.addField("FILE_FLAG", TagUtil.getEMPField("1", FieldType.FIELD_STRING, 1, 0));
			reqCD.addStruct("LOCAL_HEAD", LOCAL_HEAD);
			FTPUtil.send2FTP(TradeConstance.SERVICE_CODE_ZYGL, TradeConstance.SERVICE_SCENE_ZYCRKSQ, authorize_no, fileCD);
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 解析反馈报文头 */
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			
			if(TagUtil.haveSuccess(retKColl, context)){//执行成功
				//根据授权编号更新授权表的状态为已授权
				SqlClient.update("updatePvpAuthorizeStatusByGenNo", authorize_no, "02", null, connection);
			}else{
				throw new Exception("抵质押物权证出/入库(商链通池出/入池)授权发送失败!");
			}
			return retKColl;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("抵质押物权证出/入库(商链通池出/入池)授权发送失败"+e.getMessage());
		}
	}
	
	/**
	 * 贷款核销授权文件发送交易
	 * @param  呆账核销申请业务流水号 serno，context，connection
	 * @return 返回报文头信息及撤销标志
	 * @throws Exception 
	 */
	public KeyedCollection tradeDKHX(String serno,Context context, Connection connection) throws Exception {
		try{
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			CompositeData reqCD= new CompositeData();	//系统头
			CompositeData fileCD = new CompositeData();	//报文信息
			CompositeData APP_HEAD = new CompositeData();	//应用头
			CompositeData LOCAL_HEAD= new CompositeData();	//扩展头
			String authorize_no = "";
			
			/** 系统头 */
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(TradeConstance.SERVICE_CODE_DKHXSQ, TradeConstance.SERVICE_SCENE_DKZQSQ, TradeConstance.CONSUMERID, TagUtil.getSysHeadSerno(authorize_no, context),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			
			/** 封装写入文件的报文信息，包括整体报文信息 */
			fileCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(TradeConstance.SERVICE_CODE_DKHXSQ, TradeConstance.SERVICE_SCENE_DKZQSQ, TradeConstance.CONSUMERID, TagUtil.getSysHeadSerno(authorize_no, context),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			CompositeData bodyListCD = new CompositeData();
			Array bodyArr = new Array();
			IndexedCollection pvpIColl = dao.queryList("PvpAuthorize", " where serno = '"+serno+"' ", connection);
			if(pvpIColl != null && pvpIColl.size() > 0){
				for(int i=0;i<pvpIColl.size();i++){
					KeyedCollection authKColl = (KeyedCollection)pvpIColl.get(i);
					CompositeData bodyArrCD = new CompositeData();
					bodyArrCD.addField("GEN_GL_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(authKColl.getDataValue("authorize_no")), FieldType.FIELD_STRING, 30, 0));
					bodyArrCD.addField("DUEBILL_NO", TagUtil.getEMPField(authKColl.getDataValue("bill_no"), FieldType.FIELD_STRING, 50, 0));
					bodyArr.addStruct(bodyArrCD);
					if(i == 0){
						authorize_no = authKColl.getDataValue("authorize_no").toString();
					}
				}
			}
			bodyListCD.addArray("BASE_BODY", bodyArr);
			fileCD.addStruct("BODY", bodyListCD);
			
			/** 应用头 */
			APP_HEAD= esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM);
			APP_HEAD.addField("FILE_NAME",TagUtil.getEMPField(FTPUtil.getFileName(TradeConstance.SERVICE_CODE_DKHXSQ, TradeConstance.SERVICE_SCENE_DKZQSQ, authorize_no), FieldType.FIELD_STRING, 30, 0));
			APP_HEAD.addField("FILE_PATH",TagUtil.getEMPField(FTPUtil.getFilePath(), FieldType.FIELD_STRING, 512, 0));
			APP_HEAD.addField("TOTAL_ROWS",TagUtil.getEMPField(pvpIColl.size(), FieldType.FIELD_STRING, 12, 0));
			reqCD.addStruct("APP_HEAD", APP_HEAD);
			
			/**扩展头**/			
			LOCAL_HEAD.addField("FILE_FLAG", TagUtil.getEMPField("1", FieldType.FIELD_STRING, 1, 0));
			reqCD.addStruct("LOCAL_HEAD", LOCAL_HEAD);
			FTPUtil.send2FTP(TradeConstance.SERVICE_CODE_DKHXSQ, TradeConstance.SERVICE_SCENE_DKZQSQ, authorize_no, fileCD);
			
			/** 执行发送操作，并解析反馈报文头 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);			
			if(TagUtil.haveSuccess(retKColl, context)){//执行成功
				//根据授权编号更新授权表的状态为已授权
				SqlClient.update("updatePvpAuthorizeStatusByGenNo", authorize_no, "02", null, connection);
			}else{
				throw new Exception("贷款核销授权文件发送交易失败");
			}
			
			return retKColl;//返回报文
		}catch(Exception e){
			throw new Exception("贷款核销授权文件发送交易"+e.getMessage());
		}
	}

	/**
	 * 影像核对归档接口
	 * @param  传入业务信息kColl，context，connection
	 * @return 返回报文头信息及账户信息
	 * @throws Exception 
	 */
	public KeyedCollection tradeXYHD(KeyedCollection kColl, Context context,Connection connection) throws Exception {
		try{
			String image_action = kColl.getDataValue("image_action").toString();	//影像接口调用类型
			String code = "11002000039" ;	//服务名称
			String scene = "" ;	//服务场景
			
			/** 封装发送报文信息 */
			CompositeData reqCD= new CompositeData();
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);

			/** 应用头 */
			reqCD.addStruct("APP_HEAD", this.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), 
					TradeConstance.TRADE_SYSTEM));
			
			/** 封装报文体 */
			CompositeData bodyCD = new CompositeData();
			if(image_action.equals("Check3131")){	//3.1.3.1 客户资料归档接口
				scene = "01";
				bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(kColl.getDataValue("cus_id"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("CLIENT_NAME", TagUtil.getEMPField(kColl.getDataValue("cus_name"), FieldType.FIELD_STRING, 150, 0));
				bodyCD.addField("CLIENT_TYPE", TagUtil.getEMPField(kColl.getDataValue("cus_type"), FieldType.FIELD_STRING, 6, 0));
				bodyCD.addField("MANAGE_ORG_NO", TagUtil.getEMPField(kColl.getDataValue("main_br_id"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("MANAGE_ORG_NAME", TagUtil.getEMPField(kColl.getDataValue("main_br_id_displayname"), FieldType.FIELD_STRING, 50, 0));
				bodyCD.addField("MANAGER_ID", TagUtil.getEMPField(kColl.getDataValue("cust_mgr"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("MANAGER_NAME", TagUtil.getEMPField(kColl.getDataValue("cust_mgr_displayname"), FieldType.FIELD_STRING, 50, 0));
				bodyCD.addField("CHECK_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("currentUserId"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("CHECK_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("currentUserName"), FieldType.FIELD_STRING, 50, 0));
				bodyCD.addField("LAST_UPDATE_DATE", TagUtil.getEMPField((kColl.getDataValue("OPENDAY").toString()).replaceAll("-", ""), FieldType.FIELD_STRING, 8, 0));
				bodyCD.addField("REGIST_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("input_id"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("REGIST_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("input_id_displayname"), FieldType.FIELD_STRING, 50, 0));
				bodyCD.addField("REGIST_ORG_NO", TagUtil.getEMPField(kColl.getDataValue("input_br_id"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("REGIST_ORG_NAME", TagUtil.getEMPField(kColl.getDataValue("input_br_id_displayname"), FieldType.FIELD_STRING, 50, 0));
				bodyCD.addField("REGISTERED_DATE", TagUtil.getEMPField((kColl.getDataValue("input_date").toString()).replaceAll("-", ""), FieldType.FIELD_STRING, 8, 0));
			}else if(image_action.equals("Check3132")){	//3.1.3.2 业务资料归档接口 
				scene = "02";
				String prd_id = kColl.getDataValue("prd_id").toString();
				String busi_serno = kColl.getDataValue("serno").toString();
				String str = prd_id.substring(0,1);
				
				if(busi_serno.indexOf(",") >= 0){	//展期要同时传原业务编号和展期编号，中间以","隔开
					String[] sernos = busi_serno.split(",");
					bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(kColl.getDataValue("cus_id"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("CLIENT_NAME", TagUtil.getEMPField(kColl.getDataValue("cus_name"), FieldType.FIELD_STRING, 150, 0));
					bodyCD.addField("BUSS_KIND", TagUtil.getEMPField(kColl.getDataValue("prd_id"), FieldType.FIELD_STRING, 30, 0));
					/**modified by lisj 2015-7-30 修复展期影像核对报错bug,于2015-7-30上线 begin**/
					bodyCD.addField("BUSS_SEQ_NO", TagUtil.getEMPField(sernos[0], FieldType.FIELD_STRING, 52, 0));	//先取展期编号，这个还在定
					/**modified by lisj 2015-7-30 修复展期影像核对报错bug,于2015-7-30上线 end**/
					bodyCD.addField("CONTRACT_NO", TagUtil.getEMPField(kColl.getDataValue("agr_no"), FieldType.FIELD_STRING, 60, 0));
					bodyCD.addField("CN_CONTRACT_NO", TagUtil.getEMPField(kColl.getDataValue("cn_cont_no"), FieldType.FIELD_STRING, 60, 0));
					bodyCD.addField("CONTRACT_SIGN_DATE", TagUtil.getEMPField(kColl.getDataValue("sign_date"), FieldType.FIELD_STRING, 8, 0));
					bodyCD.addField("CONTRACT_STATUS", TagUtil.getEMPField(kColl.getDataValue("status"), FieldType.FIELD_STRING, 50, 0));
					bodyCD.addField("AMT", TagUtil.getEMPField(kColl.getDataValue("extension_amt"), FieldType.FIELD_DOUBLE, 16, 2));
					bodyCD.addField("MANAGE_ORG_NO", TagUtil.getEMPField(kColl.getDataValue("manager_br_id"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("MANAGE_ORG_NAME", TagUtil.getEMPField(kColl.getDataValue("manager_br_id_displayname"), FieldType.FIELD_STRING, 50, 0));
					bodyCD.addField("REGIST_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("input_id"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("REGIST_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("input_id_displayname"), FieldType.FIELD_STRING, 50, 0));
					bodyCD.addField("DUTY_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("manager_id"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("DUTY_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("manager_id_displayname"), FieldType.FIELD_STRING, 50, 0));
					bodyCD.addField("ILL_FLAG", TagUtil.getEMPField(kColl.getDataValue("ill_flag"), FieldType.FIELD_STRING, 10, 0));
					bodyCD.addField("CLEARANCE_DATE", TagUtil.getEMPField(kColl.getDataValue("clearance_date"), FieldType.FIELD_STRING, 8, 0));
					bodyCD.addField("CHECK_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("currentUserId"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("CHECK_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("currentUserName"), FieldType.FIELD_STRING, 50, 0));
					
					CompositeData zhCD = new CompositeData();
					zhCD.addField("ORI_GUARANTEE_MODE", TagUtil.getEMPField(kColl.getDataValue("assure_main"), FieldType.FIELD_STRING, 10, 0));
					zhCD.addField("ORI_DUEBILL_NO", TagUtil.getEMPField(kColl.getDataValue("fount_bill_no"), FieldType.FIELD_STRING, 50, 0));
					zhCD.addField("ORI_CONTRACT_NO", TagUtil.getEMPField(kColl.getDataValue("fount_cont_no"), FieldType.FIELD_STRING, 60, 0));
					zhCD.addField("ORI_START_DATE", TagUtil.getEMPField(kColl.getDataValue("fount_start_date"), FieldType.FIELD_STRING, 8, 0));
					zhCD.addField("ORI_END_DATE", TagUtil.getEMPField(kColl.getDataValue("fount_end_date"), FieldType.FIELD_STRING, 8, 0));
					zhCD.addField("EXE_EXPI_DATE", TagUtil.getEMPField(kColl.getDataValue("extension_date"), FieldType.FIELD_STRING, 8, 0));
					bodyCD.addStruct("BILL_INFO_STRUCT", zhCD);
				}else if(str.equals("1")||str.equals("2")||prd_id.equals("300020")||prd_id.equals("300021")
						||str.equals("4")||str.equals("5") || str.equals("7")|| str.equals("8")){
					bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(kColl.getDataValue("cus_id"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("CLIENT_NAME", TagUtil.getEMPField(kColl.getDataValue("cus_name"), FieldType.FIELD_STRING, 150, 0));
					bodyCD.addField("BUSS_KIND", TagUtil.getEMPField(kColl.getDataValue("prd_id"), FieldType.FIELD_STRING, 30, 0));
					bodyCD.addField("BUSS_SEQ_NO", TagUtil.getEMPField(kColl.getDataValue("serno"), FieldType.FIELD_STRING, 52, 0));
					bodyCD.addField("CONTRACT_NO", TagUtil.getEMPField(kColl.getDataValue("cont_no"), FieldType.FIELD_STRING, 60, 0));
					bodyCD.addField("CN_CONTRACT_NO", TagUtil.getEMPField(kColl.getDataValue("cn_cont_no"), FieldType.FIELD_STRING, 60, 0));
					bodyCD.addField("CONTRACT_SIGN_DATE", TagUtil.getEMPField(kColl.getDataValue("ser_date"), FieldType.FIELD_STRING, 8, 0));
					bodyCD.addField("CONTRACT_STATUS", TagUtil.getEMPField(kColl.getDataValue("cont_status"), FieldType.FIELD_STRING, 50, 0));
					bodyCD.addField("AMT", TagUtil.getEMPField(kColl.getDataValue("cont_amt"), FieldType.FIELD_DOUBLE, 16, 2));
					bodyCD.addField("MANAGE_ORG_NO", TagUtil.getEMPField(kColl.getDataValue("manager_br_id"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("MANAGE_ORG_NAME", TagUtil.getEMPField(kColl.getDataValue("manager_br_id_displayname"), FieldType.FIELD_STRING, 50, 0));
					bodyCD.addField("REGIST_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("input_id"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("REGIST_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("input_id_displayname"), FieldType.FIELD_STRING, 50, 0));
					bodyCD.addField("DUTY_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("manager_id"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("DUTY_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("manager_id_displayname"), FieldType.FIELD_STRING, 50, 0));
					bodyCD.addField("ILL_FLAG", TagUtil.getEMPField(kColl.getDataValue("ill_flag"), FieldType.FIELD_STRING, 10, 0));
					bodyCD.addField("CLEARANCE_DATE", TagUtil.getEMPField(kColl.getDataValue("cancel_date"), FieldType.FIELD_STRING, 8, 0));
					bodyCD.addField("CHECK_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("currentUserId"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("CHECK_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("currentUserName"), FieldType.FIELD_STRING, 50, 0));
					
					CompositeData zhCD = new CompositeData();
					if(str.equals("1") ||str.equals("5") || str.equals("7")|| str.equals("8") ){	//产品编号1开头为贷款类
						zhCD.addField("GUARANTEE_MODE", TagUtil.getEMPField(kColl.getDataValue("assure_main"), FieldType.FIELD_STRING, 10, 0));
						zhCD.addField("START_DATE", TagUtil.getEMPField(kColl.getDataValue("cont_start_date"), FieldType.FIELD_STRING, 8, 0));
						zhCD.addField("END_DATE", TagUtil.getEMPField(kColl.getDataValue("cont_end_date"), FieldType.FIELD_STRING, 8, 0));
						bodyCD.addStruct("LOAN_INFO_STRUCT", zhCD);
					}else if(str.equals("2")){	//产品编号2开头为银承
						zhCD.addField("NUM", TagUtil.getEMPField(kColl.getDataValue("bill_qty"), FieldType.FIELD_INT, 10, 0));
						zhCD.addField("GUARANTEE_PER", TagUtil.getEMPField(kColl.getDataValue("security_rate"), FieldType.FIELD_DOUBLE, 20, 7));
						zhCD.addField("GUARANTEE_MODE", TagUtil.getEMPField(kColl.getDataValue("assure_main"), FieldType.FIELD_STRING, 10, 0));
						bodyCD.addStruct("ACCP_INFO_STRUCT", zhCD);
					}else if(prd_id.equals("300020") || prd_id.equals("300021")){	//贴现
						zhCD.addField("BILL_KIND", TagUtil.getEMPField(kColl.getDataValue("bill_type"), FieldType.FIELD_STRING, 10, 0));
						zhCD.addField("BILL_NUM", TagUtil.getEMPField(kColl.getDataValue("bill_qty"), FieldType.FIELD_INT, 10, 0));
						bodyCD.addStruct("BILL_INFO_STRUCT", zhCD);
					}else if(str.equals("4")){	//产品编号4开头为保函
						zhCD.addField("GUARANTEE_PER", TagUtil.getEMPField(kColl.getDataValue("guarant_type"), FieldType.FIELD_DOUBLE, 20, 7));
						zhCD.addField("GUARANTEE_MODE", TagUtil.getEMPField(kColl.getDataValue("assure_main"), FieldType.FIELD_STRING, 10, 0));
						zhCD.addField("GT_TYPE", TagUtil.getEMPField(kColl.getDataValue("security_rate"), FieldType.FIELD_STRING, 10, 0));
						bodyCD.addStruct("GT_INFO_STRUCT", zhCD);
					}
				}else if(prd_id.equals("300022") || prd_id.equals("300023") || prd_id.equals("300024")){	//转贴现是单独表，单独处理
					bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(kColl.getDataValue("cus_id"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("CLIENT_NAME", TagUtil.getEMPField(kColl.getDataValue("this_acct_name"), FieldType.FIELD_STRING, 150, 0));
					bodyCD.addField("BUSS_KIND", TagUtil.getEMPField(kColl.getDataValue("prd_id"), FieldType.FIELD_STRING, 30, 0));
					bodyCD.addField("BUSS_SEQ_NO", TagUtil.getEMPField(kColl.getDataValue("serno"), FieldType.FIELD_STRING, 52, 0));
					bodyCD.addField("CONTRACT_NO", TagUtil.getEMPField(kColl.getDataValue("cont_no"), FieldType.FIELD_STRING, 60, 0));
					bodyCD.addField("CN_CONTRACT_NO", TagUtil.getEMPField(kColl.getDataValue("cn_cont_no"), FieldType.FIELD_STRING, 60, 0));
					bodyCD.addField("CONTRACT_SIGN_DATE", TagUtil.getEMPField(kColl.getDataValue("rpddscnt_date"), FieldType.FIELD_STRING, 8, 0));
					bodyCD.addField("CONTRACT_STATUS", TagUtil.getEMPField(kColl.getDataValue("cont_status"), FieldType.FIELD_STRING, 50, 0));
					bodyCD.addField("AMT", TagUtil.getEMPField(kColl.getDataValue("bill_total_amt"), FieldType.FIELD_DOUBLE, 16, 2));
					bodyCD.addField("MANAGE_ORG_NO", TagUtil.getEMPField(kColl.getDataValue("manager_br_id"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("MANAGE_ORG_NAME", TagUtil.getEMPField(kColl.getDataValue("manager_br_id_displayname"), FieldType.FIELD_STRING, 50, 0));
					bodyCD.addField("REGIST_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("input_id"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("REGIST_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("input_id_displayname"), FieldType.FIELD_STRING, 50, 0));
					bodyCD.addField("DUTY_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("manager_id"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("DUTY_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("manager_id_displayname"), FieldType.FIELD_STRING, 50, 0));
					bodyCD.addField("ILL_FLAG", TagUtil.getEMPField(kColl.getDataValue("ill_flag"), FieldType.FIELD_STRING, 10, 0));
					bodyCD.addField("CLEARANCE_DATE", TagUtil.getEMPField(kColl.getDataValue("rpddscnt_date"), FieldType.FIELD_STRING, 8, 0));
					bodyCD.addField("CHECK_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("currentUserId"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("CHECK_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("currentUserName"), FieldType.FIELD_STRING, 50, 0));
					
					CompositeData zhCD = new CompositeData();
					zhCD.addField("BILL_KIND", TagUtil.getEMPField(kColl.getDataValue("bill_type"), FieldType.FIELD_STRING, 10, 0));
					zhCD.addField("BILL_NUM", TagUtil.getEMPField(kColl.getDataValue("bill_qty"), FieldType.FIELD_INT, 10, 0));
					bodyCD.addStruct("BILL_INFO_STRUCT", zhCD);
				}else if(str.equals("6")){	//产品编号6开头为资产转受让
					bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(kColl.getDataValue("cus_id"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("CLIENT_NAME", TagUtil.getEMPField(kColl.getDataValue("this_acct_name"), FieldType.FIELD_STRING, 150, 0));
					bodyCD.addField("BUSS_KIND", TagUtil.getEMPField(kColl.getDataValue("prd_id"), FieldType.FIELD_STRING, 30, 0));
					bodyCD.addField("BUSS_SEQ_NO", TagUtil.getEMPField(kColl.getDataValue("serno"), FieldType.FIELD_STRING, 52, 0));
					bodyCD.addField("CONTRACT_NO", TagUtil.getEMPField(kColl.getDataValue("cont_no"), FieldType.FIELD_STRING, 60, 0));
					bodyCD.addField("CN_CONTRACT_NO", TagUtil.getEMPField(kColl.getDataValue("cn_cont_no"), FieldType.FIELD_STRING, 60, 0));
					bodyCD.addField("CONTRACT_SIGN_DATE", TagUtil.getEMPField(kColl.getDataValue("takeover_date"), FieldType.FIELD_STRING, 8, 0));
					bodyCD.addField("CONTRACT_STATUS", TagUtil.getEMPField(kColl.getDataValue("cont_status"), FieldType.FIELD_STRING, 50, 0));
					bodyCD.addField("AMT", TagUtil.getEMPField(kColl.getDataValue("asset_total_amt"), FieldType.FIELD_DOUBLE, 16, 2));
					bodyCD.addField("MANAGE_ORG_NO", TagUtil.getEMPField(kColl.getDataValue("manager_br_id"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("MANAGE_ORG_NAME", TagUtil.getEMPField(kColl.getDataValue("manager_br_id_displayname"), FieldType.FIELD_STRING, 50, 0));
					bodyCD.addField("REGIST_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("input_id"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("REGIST_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("input_id_displayname"), FieldType.FIELD_STRING, 50, 0));
					bodyCD.addField("DUTY_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("manager_id"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("DUTY_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("manager_id_displayname"), FieldType.FIELD_STRING, 50, 0));
					bodyCD.addField("ILL_FLAG", TagUtil.getEMPField(kColl.getDataValue("ill_flag"), FieldType.FIELD_STRING, 10, 0));
					bodyCD.addField("CLEARANCE_DATE", TagUtil.getEMPField(kColl.getDataValue("takeover_date"), FieldType.FIELD_STRING, 8, 0));
					bodyCD.addField("CHECK_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("currentUserId"), FieldType.FIELD_STRING, 20, 0));
					bodyCD.addField("CHECK_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("currentUserName"), FieldType.FIELD_STRING, 50, 0));
					
					CompositeData zhCD = new CompositeData();
					zhCD.addField("TRANSFER_AMT", TagUtil.getEMPField(kColl.getDataValue("takeover_total_amt"), FieldType.FIELD_DOUBLE, 20, 2));
					zhCD.addField("TRANSFER_NUM", TagUtil.getEMPField(kColl.getDataValue("takeover_qnt"), FieldType.FIELD_INT, 10, 0));
					zhCD.addField("TRANSFER_MODE", TagUtil.getEMPField(kColl.getDataValue("takeover_type"), FieldType.FIELD_STRING, 2, 0));
					bodyCD.addStruct("BILL_INFO_STRUCT", zhCD);
				}
			}else if(image_action.equals("Check3133")){	//3.1.3.3 担保资料归档接口
				scene = "03";
				bodyCD.addField("GUARANTEE_GOODS_ID", TagUtil.getEMPField(kColl.getDataValue("guaranty_no"), FieldType.FIELD_STRING, 10, 0));
				bodyCD.addField("GUARANTEE_GOODS_NAME", TagUtil.getEMPField(kColl.getDataValue("guaranty_name"), FieldType.FIELD_STRING, 10, 0));
				bodyCD.addField("GUARANTEE_MODE", TagUtil.getEMPField(kColl.getDataValue("guar_way"), FieldType.FIELD_STRING, 10, 0));
				bodyCD.addField("GUARANTEE_GOODS_TYPE", TagUtil.getEMPField(kColl.getDataValue("guaranty_type"), FieldType.FIELD_STRING, 10, 0));
				bodyCD.addField("GUARANTEE_CONTRACT_NO", TagUtil.getEMPField(kColl.getDataValue("guar_cont_no"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("GUARANTEE_CONTRACT_TYPE", TagUtil.getEMPField(kColl.getDataValue("guar_cont_type"), FieldType.FIELD_STRING, 10, 0));
				bodyCD.addField("CN_CONTRACT_NO", TagUtil.getEMPField(kColl.getDataValue("guar_cont_cn_no"), FieldType.FIELD_STRING, 60, 0));
				bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(kColl.getDataValue("grt_cus_id"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("CLIENT_NAME", TagUtil.getEMPField(kColl.getDataValue("grt_cus_name"), FieldType.FIELD_STRING, 150, 0));
				bodyCD.addField("GUARANTEE_ID", TagUtil.getEMPField(kColl.getDataValue("mort_cus_id"), FieldType.FIELD_STRING, 60, 0));
				bodyCD.addField("GUARANTEE_NAME", TagUtil.getEMPField(kColl.getDataValue("mort_cus_name"), FieldType.FIELD_STRING, 60, 0));
				bodyCD.addField("AMT", TagUtil.getEMPField(kColl.getDataValue("guar_amt"), FieldType.FIELD_DOUBLE, 16, 2));
				bodyCD.addField("START_DATE", TagUtil.getEMPField(kColl.getDataValue("guar_start_date"), FieldType.FIELD_STRING, 8, 0));
				bodyCD.addField("END_DATE", TagUtil.getEMPField(kColl.getDataValue("guar_end_date"), FieldType.FIELD_STRING, 8, 0));
				bodyCD.addField("REGIST_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("input_id"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("REGIST_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("input_id_displayname"), FieldType.FIELD_STRING, 50, 0));
				bodyCD.addField("REGIST_ORG_NO", TagUtil.getEMPField(kColl.getDataValue("input_br_id"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("REGIST_ORG_NAME", TagUtil.getEMPField(kColl.getDataValue("input_br_id_displayname"), FieldType.FIELD_STRING, 50, 0));
				bodyCD.addField("REGISTERED_DATE", TagUtil.getEMPField(kColl.getDataValue("input_date"), FieldType.FIELD_STRING, 8, 0));
				bodyCD.addField("MANAGE_ORG_ID", TagUtil.getEMPField(kColl.getDataValue("manager_br_id"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("MANAGE_ORG_NAME", TagUtil.getEMPField(kColl.getDataValue("manager_br_id_displayname"), FieldType.FIELD_STRING, 50, 0));
				bodyCD.addField("DUTY_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("manager_id"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("DUTY_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("manager_id_displayname"), FieldType.FIELD_STRING, 50, 0));
				bodyCD.addField("CHECK_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("currentUserId"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("CHECK_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("currentUserName"), FieldType.FIELD_STRING, 50, 0));
			}else if(image_action.equals("Check3134")){	//3.1.3.4 贷后资料归档接口
				scene = "04";
				bodyCD.addField("TASK_ID", TagUtil.getEMPField(kColl.getDataValue("serno"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(kColl.getDataValue("cus_id"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("CLIENT_NAME", TagUtil.getEMPField(kColl.getDataValue("cus_id_displayname"), FieldType.FIELD_STRING, 150, 0));
				bodyCD.addField("CNT", TagUtil.getEMPField(kColl.getDataValue("qnt"), FieldType.FIELD_INT, 10, 0));
				bodyCD.addField("AMT", TagUtil.getEMPField(kColl.getDataValue("loan_totl_amt"), FieldType.FIELD_DOUBLE, 16, 2));
				bodyCD.addField("START_DATE", TagUtil.getEMPField((kColl.getDataValue("task_create_date").toString()).replaceAll("-", ""), FieldType.FIELD_STRING, 8, 0));
				bodyCD.addField("END_DATE", TagUtil.getEMPField((kColl.getDataValue("task_request_time").toString()).replaceAll("-", ""), FieldType.FIELD_STRING, 8, 0));
				bodyCD.addField("EXEC_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("task_huser"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("EXEC_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("task_huser_displayname"), FieldType.FIELD_STRING, 50, 0));
				bodyCD.addField("EXEC_ORG_NO", TagUtil.getEMPField(kColl.getDataValue("task_horg"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("EXEC_ORG_NAME", TagUtil.getEMPField(kColl.getDataValue("task_horg_displayname"), FieldType.FIELD_STRING, 50, 0));
				bodyCD.addField("CHECK_PERSON_ID", TagUtil.getEMPField(kColl.getDataValue("currentUserId"), FieldType.FIELD_STRING, 20, 0));
				bodyCD.addField("CHECK_PERSON_NAME", TagUtil.getEMPField(kColl.getDataValue("currentUserName"), FieldType.FIELD_STRING, 50, 0));
			}
			reqCD.addStruct(TradeConstance.ESB_BODY, bodyCD);
			
			/** 系统头 */
			reqCD.addStruct("SYS_HEAD", this.getSysHeadCD(code,scene, TradeConstance.CONSUMERID, serno,
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), 
					(String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.CONSUMERID, context));

			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			
			if(!TagUtil.haveSuccess(retKColl, context)){ //执行失败
				
			}else{
				/** 解析反馈报文体 */
				KeyedCollection back_kColl = this.getRespBodyCD4KColl(retCD);
				back_kColl.setName("BODY");
				retKColl.addDataElement(back_kColl);
			}		
			return retKColl;	//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("影像核对归档接口失败");
		}
		
	}

	/**
	 * 影像锁定接口
	 * @param  传入业务信息kColl，context，connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 */
	public KeyedCollection tradeIMAGELOCKED(KeyedCollection kColl, Context context,Connection connection) throws Exception {
		try{
			String code = "11002000039" ;	//服务名称
			String scene = "07" ;	        //服务场景
			
			/** 封装发送报文信息 */
			CompositeData reqCD= new CompositeData();
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);

			/** 应用头 */
			reqCD.addStruct("APP_HEAD", this.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), 
					TradeConstance.TRADE_SYSTEM));
			
			/** 封装报文体 */
			CompositeData bodyCD = new CompositeData();
			bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(kColl.getDataValue("CLIENT_NO"), FieldType.FIELD_STRING, 20, 0));
			bodyCD.addField("BUSS_SEQ_NO", TagUtil.getEMPField(kColl.getDataValue("BUSS_SEQ_NO"), FieldType.FIELD_STRING, 52, 0));
			bodyCD.addField("TASK_ID", TagUtil.getEMPField(kColl.getDataValue("TASK_ID"), FieldType.FIELD_STRING, 20, 0));
			reqCD.addStruct(TradeConstance.ESB_BODY, bodyCD);
			
			/** 系统头 */
			reqCD.addStruct("SYS_HEAD", this.getSysHeadCD(code,scene, TradeConstance.CONSUMERID, serno,
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), 
					(String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.CONSUMERID, context));

			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			
			if(!TagUtil.haveSuccess(retKColl, context)){ //执行失败
				
			}else{
				/** 解析反馈报文体 */
				KeyedCollection back_kColl = this.getRespBodyCD4KColl(retCD);
				back_kColl.setName("BODY");
				retKColl.addDataElement(back_kColl);
			}		
			return retKColl;	//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("影像锁定接口失败");
		}
		
	}
	
	/**
	 * 抵质押临时编号获取接口
	 * @param  传入业务信息kColl，context，connection
	 * @return 返回报文头信息及账户信息
	 * @throws Exception 
	 */
	public KeyedCollection tradeXYYPBHHQ(KeyedCollection kColl, Context context,Connection connection) throws Exception {
		try{
			String cus_id = kColl.getDataValue("cus_id").toString();
			/** 封装发送报文信息 */
			CompositeData reqCD= new CompositeData();
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			/** 系统头 */
			reqCD.addStruct("SYS_HEAD", this.getSysHeadCD("11003000025", "01",
					TradeConstance.CONSUMERID, serno, (String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),
					(String) context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME),
					(String) context.getDataValue(CMISConstance.ATTR_ORGID),TradeConstance.CONSUMERID, context));
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", this.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			/** 封装报文体 */
			CompositeData bodyCD = new CompositeData();
			bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(cus_id, FieldType.FIELD_STRING, 20, 0));
			reqCD.addStruct(TradeConstance.ESB_BODY, bodyCD);
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
				
			}else{
				/** 解析反馈报文体 */
				KeyedCollection back_kColl = this.getRespBodyCD4KColl(retCD);
				back_kColl.setName("BODY");
				retKColl.addDataElement(back_kColl);
			}
			return retKColl;
		}catch(Exception e){
			throw new Exception("抵质押临时编号获取接口失败");
		}
	}
	
	/**
	 * 核算与信贷业务品种映射接口
	 * @param pk_value 业务主键
	 * @param prd_id 信贷业务编号
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 核算业务编号
	 * @throws Exception 
	 */
	public String getPrdBasicCLPM2LM(String pk_value,String prd_id,Context context,Connection connection) throws Exception {
		try{
			String lmPrdId = "";
			String belgFlag = "";
			KeyedCollection kcoll_detail = new KeyedCollection();
			KeyedCollection kcoll_detail2 = new KeyedCollection();
			KeyedCollection kcollInfo = new KeyedCollection();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			
			KeyedCollection kcoll = dao.queryFirst("PrdBasicClpm2lm", null, " where fldvalue10='1' and instr(prd_id,'"+prd_id+"')>0 ", connection);
			if(kcoll != null && kcoll.getDataValue("belg_flag")!=null && !"".equals(kcoll.getDataValue("belg_flag"))){
				belgFlag = (String)kcoll.getDataValue("belg_flag");
				if("00".equals(belgFlag)){//直接映射
					lmPrdId = (String)kcoll.getDataValue("lm_prd_id");
					/**added by wangj  需求编号:XD141222087,法人账户透支改造 XD150825064_源泉宝法人账户透支改造   begin*/
					if("100051".equals(prd_id)){
						KeyedCollection kcoll_sub = dao.queryFirst("CtrLoanContSub", null, " where cont_no = '"+pk_value+"' ", connection);
						String belg=(String)kcoll_sub.getDataValue("belg_line");
						KeyedCollection kcoll2info = dao.queryFirst("PrdBasicClpm2lm", null, " where prd_id='"+prd_id+"' and belg_flag='00' and fldvalue01='"+belg+"' ", connection);
						if(kcoll2info != null && kcoll2info.getDataValue("belg_flag")!=null && !"".equals(kcoll2info.getDataValue("belg_flag"))){
							lmPrdId = (String)kcoll2info.getDataValue("lm_prd_id");
						}
					}
					/**added by wangj  需求编号:XD141222087,法人账户透支改造  XD150825064_源泉宝法人账户透支改造   end*/
				}else if("01".equals(belgFlag)){//保函
					String guarantType = "";//保函种类
					String openType = "";//开立类型
					String benAcctOrgNo = "";//受益人开户行行号
					String acctOrgType = "";//委托代开行类型
					kcoll_detail = dao.queryFirst("IqpGuarantInfo", null, " where exists (select 1 from ctr_loan_cont where iqp_guarant_info.serno = ctr_loan_cont.serno and ctr_loan_cont.cont_no = '"+pk_value+"') ", connection);
					if(kcoll_detail!=null&&kcoll_detail.getDataValue("serno")!=null&&!"".equals(kcoll_detail.getDataValue("serno"))){
						guarantType = (String)kcoll_detail.getDataValue("guarant_type");
						openType = (String)kcoll_detail.getDataValue("open_type");
						benAcctOrgNo = (String)kcoll_detail.getDataValue("ben_acct_org_no");
						if(benAcctOrgNo!=null&&!"".equals(benAcctOrgNo)){
							if(benAcctOrgNo.length()>3){
								acctOrgType = benAcctOrgNo.substring(0,3).toString();
								if(!"102".equals(acctOrgType)&&!"103".equals(acctOrgType)&&!"104".equals(acctOrgType)&&!"105".equals(acctOrgType)){
									acctOrgType = "OTH";
								}
							}else{
								acctOrgType = "OTH";
							}
						}else{
							acctOrgType = "OTH";
						}
					}
					kcollInfo = dao.queryFirst("PrdBasicClpm2lm", null, " where instr(prd_id,'"+prd_id+"')>0 and (case when fldvalue01 is not null then fldvalue01 else '"+openType+"' end) = '"+openType+"' and (case when fldvalue02 is not null then fldvalue02 else '"+guarantType+"' end) = '"+guarantType+"' and (case when fldvalue03 is not null then fldvalue03 else '"+acctOrgType+"' end) = '"+acctOrgType+"' ", connection);
					if(kcollInfo != null && kcollInfo.getDataValue("belg_flag")!=null && !"".equals(kcollInfo.getDataValue("belg_flag"))){
						lmPrdId = (String)kcollInfo.getDataValue("lm_prd_id");
					}
				}else if("02".equals(belgFlag)){//贷款
					String assureMain = "";//担保方式
					String cont_term = "";//合同期限
					String term_type = "";//合同期限类型 
					String loanBelong1 = "";//贷款归属1
					kcoll_detail = dao.queryFirst("CtrLoanCont", null, " where cont_no = '"+pk_value+"' ", connection);
					if(kcoll_detail!=null&&kcoll_detail.getDataValue("cont_no")!=null&&!"".equals(kcoll_detail.getDataValue("cont_no"))){
						assureMain = (String)kcoll_detail.getDataValue("assure_main");
					}
					kcoll_detail2 = dao.queryFirst("CtrLoanContSub", null, " where cont_no = '"+pk_value+"' ", connection);
					if(kcoll_detail2!=null&&kcoll_detail2.getDataValue("cont_no")!=null&&!"".equals(kcoll_detail2.getDataValue("cont_no"))){
						cont_term = (String)kcoll_detail2.getDataValue("cont_term");
						term_type = (String)kcoll_detail2.getDataValue("term_type");
						if("001".equals(term_type)){//年
							if(new BigDecimal(cont_term).compareTo(new BigDecimal(1))>0){
								loanBelong1 = "30";
							}else{
								loanBelong1 = "10";
							}
						}else if("002".equals(term_type)){//月
							if(new BigDecimal(cont_term).compareTo(new BigDecimal(12))>0){
								loanBelong1 = "30";
							}else{
								loanBelong1 = "10";
							}
						}else if("003".equals(term_type)){//日
							if(new BigDecimal(cont_term).compareTo(new BigDecimal(365))>0){
								loanBelong1 = "30";
							}else{
								loanBelong1 = "10";
							}
						}
					}
					kcollInfo = dao.queryFirst("PrdBasicClpm2lm", null, " where instr(prd_id,'"+prd_id+"')>0 and (case when fldvalue01 is not null then instr(fldvalue01,'"+assureMain+"') else 1 end) > 0 and (case when fldvalue02 is not null then fldvalue02 else '"+loanBelong1+"' end) = '"+loanBelong1+"' ", connection);
					if(kcollInfo != null && kcollInfo.getDataValue("belg_flag")!=null && !"".equals(kcollInfo.getDataValue("belg_flag"))){
						lmPrdId = (String)kcollInfo.getDataValue("lm_prd_id");
					}
				}else if("03".equals(belgFlag)){//贴现
					String billType = "";//票据种类
					String aorgType = "";//承兑行类型
					kcoll_detail = dao.queryFirst("IqpBillDetail", null, " where porder_no = (select porder_no from acc_drft where bill_no = '"+pk_value+"') ", connection);
					if(kcoll_detail!=null&&kcoll_detail.getDataValue("porder_no")!=null&&!"".equals(kcoll_detail.getDataValue("porder_no"))){
						billType = (String)kcoll_detail.getDataValue("bill_type");
						if("100".equals(billType)){
							aorgType = (String)kcoll_detail.getDataValue("aorg_type");
						}
					}
					kcollInfo = dao.queryFirst("PrdBasicClpm2lm", null, " where instr(prd_id,'"+prd_id+"')>0 and (case when fldvalue01 is not null then fldvalue01 else '"+billType+"' end) = '"+billType+"' and (case when fldvalue02 is not null then instr(fldvalue02,'"+aorgType+"') else 1 end) > 0 ", connection);
					if(kcollInfo != null && kcollInfo.getDataValue("belg_flag")!=null && !"".equals(kcollInfo.getDataValue("belg_flag"))){
						lmPrdId = (String)kcollInfo.getDataValue("lm_prd_id");
					}
				}else if("04".equals(belgFlag)){//转贴现
					String billType = "";//票据种类
					String bizType = "";//业务种类
					String aorgType = "";//承兑行类型
					String oppOrgType = "";//交易对手行类型
					kcoll_detail = dao.queryFirst("IqpBillDetail", null, " where porder_no = (select porder_no from acc_drft where bill_no = '"+pk_value+"') ", connection);
					if(kcoll_detail!=null&&kcoll_detail.getDataValue("porder_no")!=null&&!"".equals(kcoll_detail.getDataValue("porder_no"))){
						billType = (String)kcoll_detail.getDataValue("bill_type");
						if("100".equals(billType)){
							aorgType = (String)kcoll_detail.getDataValue("aorg_type");
						}
					}
					kcoll_detail2 = dao.queryFirst("IqpBatchMng", null, " where cont_no = (select cont_no from acc_drft where bill_no = '"+pk_value+"') ", connection);
					if(kcoll_detail2!=null&&kcoll_detail2.getDataValue("batch_no")!=null&&!"".equals(kcoll_detail2.getDataValue("batch_no"))){
						bizType = (String)kcoll_detail2.getDataValue("biz_type");
						oppOrgType = (String)kcoll_detail2.getDataValue("opp_org_type");
					}
					kcollInfo = dao.queryFirst("PrdBasicClpm2lm", null, " where instr(prd_id,'"+prd_id+"')>0 and (case when fldvalue01 is not null then fldvalue01 else '"+billType+"' end) = '"+billType+"' and (case when fldvalue02 is not null then instr(fldvalue02,'"+aorgType+"') else 1 end) > 0 and (case when fldvalue03 is not null then instr(fldvalue03,'"+bizType+"') else 1 end) > 0 and (case when fldvalue04 is not null then instr(fldvalue04,'"+oppOrgType+"') else 1 end) > 0 ", connection);
					if(kcollInfo != null && kcollInfo.getDataValue("belg_flag")!=null && !"".equals(kcollInfo.getDataValue("belg_flag"))){
						lmPrdId = (String)kcollInfo.getDataValue("lm_prd_id");
					}
					
				}else if("05".equals(belgFlag)){//贸易融资
					String assureMain = "";//担保方式
					String cont_term = "";//合同期限
					String term_type = "";//合同期限类型 
					String loanBelong1 = "";//贷款归属1
					kcoll_detail = dao.queryFirst("CtrLoanCont", null, " where cont_no = '"+pk_value+"' ", connection);
					if(kcoll_detail!=null&&kcoll_detail.getDataValue("cont_no")!=null&&!"".equals(kcoll_detail.getDataValue("cont_no"))){
						assureMain = (String)kcoll_detail.getDataValue("assure_main");
					}
					kcoll_detail2 = dao.queryFirst("CtrLoanContSub", null, " where cont_no = '"+pk_value+"' ", connection);
					if(kcoll_detail2!=null&&kcoll_detail2.getDataValue("cont_no")!=null&&!"".equals(kcoll_detail2.getDataValue("cont_no"))){
						cont_term = (String)kcoll_detail2.getDataValue("cont_term");
						term_type = (String)kcoll_detail2.getDataValue("term_type");
						if("001".equals(term_type)){//年
							if(new BigDecimal(cont_term).compareTo(new BigDecimal(1))>0){
								loanBelong1 = "30";
							}else{
								loanBelong1 = "10";
							}
						}else if("002".equals(term_type)){//月
							if(new BigDecimal(cont_term).compareTo(new BigDecimal(12))>0){
								loanBelong1 = "30";
							}else{
								loanBelong1 = "10";
							}
						}else if("003".equals(term_type)){//日
							if(new BigDecimal(cont_term).compareTo(new BigDecimal(365))>0){
								loanBelong1 = "30";
							}else{
								loanBelong1 = "10";
							}
						}
					}
					kcollInfo = dao.queryFirst("PrdBasicClpm2lm", null, " where instr(prd_id,'"+prd_id+"')>0 and (case when fldvalue01 is not null then instr(fldvalue01,'"+assureMain+"') else 1 end) > 0 and (case when fldvalue02 is not null then fldvalue02 else '"+loanBelong1+"' end) = '"+loanBelong1+"' ", connection);
					if(kcollInfo != null && kcollInfo.getDataValue("belg_flag")!=null && !"".equals(kcollInfo.getDataValue("belg_flag"))){
						lmPrdId = (String)kcollInfo.getDataValue("lm_prd_id");
					}else{
						lmPrdId = prd_id;
					}
				}else if("06".equals(belgFlag)){//同业代付
					String serno = "";//申请流水
					String is_internal_cert_agt = "";//是否国内证项下代付
					kcoll_detail = dao.queryFirst("CtrLoanCont", null, " where cont_no = '"+pk_value+"' ", connection);
					if(kcoll_detail!=null&&kcoll_detail.getDataValue("cont_no")!=null&&!"".equals(kcoll_detail.getDataValue("cont_no"))){
						serno = (String)kcoll_detail.getDataValue("serno");
					}
					KeyedCollection kColl4Info = dao.queryDetail("IqpIntbankAgt", serno, connection);
					if(kColl4Info!=null&&kColl4Info.getDataValue("is_internal_cert_agt")!=null){
						is_internal_cert_agt = (String)kColl4Info.getDataValue("is_internal_cert_agt");
					}
					kcollInfo = dao.queryFirst("PrdBasicClpm2lm", null, " where instr(prd_id,'"+prd_id+"')>0 and fldvalue01 = '"+is_internal_cert_agt+"' ", connection);
					if(kcollInfo != null && kcollInfo.getDataValue("belg_flag")!=null && !"".equals(kcollInfo.getDataValue("belg_flag"))){
						lmPrdId = (String)kcollInfo.getDataValue("lm_prd_id");
					}
				}else if("07".equals(belgFlag)){//国内保理
					String serno = "";//申请流水
					String fin_type = "";//融资类型：1-表内融资，2-表外融资
					kcoll_detail = dao.queryFirst("CtrLoanCont", null, " where cont_no = '"+pk_value+"' ", connection);
					if(kcoll_detail!=null&&kcoll_detail.getDataValue("cont_no")!=null&&!"".equals(kcoll_detail.getDataValue("cont_no"))){
						serno = (String)kcoll_detail.getDataValue("serno");
					}
					KeyedCollection kColl4Info = dao.queryDetail("IqpInterFact", serno, connection);
					if(kColl4Info!=null&&kColl4Info.getDataValue("fin_type")!=null){
						fin_type = (String)kColl4Info.getDataValue("fin_type");
					}
					kcollInfo = dao.queryFirst("PrdBasicClpm2lm", null, " where instr(prd_id,'"+prd_id+"')>0 and fldvalue01 = '"+fin_type+"' ", connection);
					if(kcollInfo != null && kcollInfo.getDataValue("belg_flag")!=null && !"".equals(kcollInfo.getDataValue("belg_flag"))){
						lmPrdId = (String)kcollInfo.getDataValue("lm_prd_id");
					}
				}
			}
			return lmPrdId;
		}catch(Exception e){
			logger.error("核算与信贷业务品种映射接口,获取核算科目失败！");
			throw new Exception("核算与信贷业务品种映射接口,获取核算科目失败！");
		}
	}
	/**
	 * 资产转让业务品种映射接口
	 * @param asset_no 资产包编号
	 * @param bill_no 借据编号
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 核算业务编号
	 * @throws Exception 
	 */
	public String getPrdBasicAssetstrsf2LM(String asset_no,String bill_no,Context context,Connection connection) throws Exception {
		try{
			String lmPrdId = "";
			String prdId = "";
			String guarType = "";
			String loanStartDate = "";
			String loanEndDate = "";
			String loanBelong1 = "";
			KeyedCollection kcoll_detail = new KeyedCollection();
			KeyedCollection kcollInfo = new KeyedCollection();
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			String condition = " where asset_no = '"+asset_no+"' and bill_no = '"+bill_no+"'";
			IndexedCollection relIColl = dao.queryList("IqpAssetRel", condition, connection);
			if(relIColl!=null&&relIColl.size()>0){
				kcoll_detail = (KeyedCollection) relIColl.get(0);
				if(kcoll_detail.containsKey("prd_id")&&kcoll_detail.getDataValue("prd_id")!=null&&!"".equals(kcoll_detail.getDataValue("prd_id"))){
					prdId = (String)kcoll_detail.getDataValue("prd_id");
				}
				if(kcoll_detail.containsKey("guar_type")&&kcoll_detail.getDataValue("guar_type")!=null&&!"".equals(kcoll_detail.getDataValue("guar_type"))){
					guarType = (String)kcoll_detail.getDataValue("guar_type");
				}
				if(kcoll_detail.containsKey("loan_start_date")&&kcoll_detail.getDataValue("loan_start_date")!=null&&!"".equals(kcoll_detail.getDataValue("loan_start_date"))){
					loanStartDate = (String)kcoll_detail.getDataValue("loan_start_date");
				}
				if(kcoll_detail.containsKey("loan_end_date")&&kcoll_detail.getDataValue("loan_end_date")!=null&&!"".equals(kcoll_detail.getDataValue("loan_end_date"))){
					loanEndDate = (String)kcoll_detail.getDataValue("loan_end_date");
				}
				if(!"".equals(loanStartDate)&&!"".equals(loanEndDate)){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date dateA = sdf.parse(loanStartDate);
				    Date dateB = sdf.parse(loanEndDate);
				    long timeA = dateA.getTime();
				    long timeB = dateB.getTime();
				    if(new BigDecimal((timeB-timeA)/(1000*60*60*24)).compareTo(new BigDecimal(365))>0){
						loanBelong1 = "30";
					}else{
						loanBelong1 = "10";
					}
				}
				kcollInfo = dao.queryFirst("PrdBasicClpm2lm", null, " where instr(prd_id,'"+prdId+"')>0 and (case when fldvalue01 is not null then instr(fldvalue01,'"+guarType+"') else 1 end) > 0 and (case when fldvalue02 is not null then fldvalue02 else '"+loanBelong1+"' end) = '"+loanBelong1+"' ", connection);
				if(kcollInfo != null && kcollInfo.getDataValue("belg_flag")!=null && !"".equals(kcollInfo.getDataValue("belg_flag"))){
					lmPrdId = (String)kcollInfo.getDataValue("lm_prd_id");
				}
			}
			return lmPrdId;
		}catch(Exception e){
			logger.error("核算与信贷业务品种映射接口,获取核算科目失败！");
			throw new Exception("核算与信贷业务品种映射接口,获取核算科目失败！");
		}
	}
	
	/**
	 * 资产证券化业务品种映射接口  add by zhaozq 2014-08-25 
	 * @param bill_no
	 * @param context
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public String getPrdBasicAssetPro2LM(String bill_no,Context context,Connection connection) throws Exception {
		try{
			String lmPrdId = "";
			String belgFlag = "";
			String pk_value = "";
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection accViewKc = dao.queryDetail("AccView", bill_no, connection);
			String prd_id = (String) accViewKc.getDataValue("prd_id");
			KeyedCollection kcoll = dao.queryFirst("PrdBasicClpm2lm", null, " where instr(prd_id,'"+prd_id+"')>0 ", connection);
			if(kcoll != null && kcoll.getDataValue("belg_flag")!=null && !"".equals(kcoll.getDataValue("belg_flag"))){
				belgFlag = (String)kcoll.getDataValue("belg_flag");
				if("00".equals(belgFlag)){//直接映射
					pk_value = "";
				}else if("01".equals(belgFlag)){//保函
					pk_value = (String) accViewKc.getDataValue("cont_no");
				}else if("02".equals(belgFlag)){//贷款
					pk_value = (String) accViewKc.getDataValue("cont_no");
				}else if("03".equals(belgFlag)){//贴现
					pk_value = bill_no;
				}else if("04".equals(belgFlag)){//转贴现
					pk_value = bill_no;
				}else if("05".equals(belgFlag)){//贸易融资
					pk_value = (String) accViewKc.getDataValue("cont_no");
				}else if("06".equals(belgFlag)){//同业代付
					pk_value = (String) accViewKc.getDataValue("cont_no");
				}
			}
			
			lmPrdId = this.getPrdBasicCLPM2LM(pk_value, prd_id, context, connection);
			
			return lmPrdId;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("核算与信贷业务品种映射接口,获取核算科目失败！");
			throw new Exception("核算与信贷业务品种映射接口,获取核算科目失败！");
		}
	}
	
	/**
	 * 利率调整授权
	 * @param  利率调整申请业务流水号 serno，context，connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 */
	public KeyedCollection tradeLVTZ(String serno,Context context, Connection connection) throws Exception {
		try{
			/** 封装发送报文信息 */
			KeyedCollection headKColl = new KeyedCollection();
//			组装报文头中服务代码和场景代码
			headKColl.put("SvcCd","30220002");//利率调整服务码
			headKColl.put("ScnCd","03");//利率调整场景码
			//跟核心交互的接口在头里还得传这三个参数
			headKColl.put("TxnMd","ONLINE");
			headKColl.put("UsrLngKnd","CHINESE");
			headKColl.put("jkType","cbs");
			/** 封装报文体 */
			KeyedCollection bodyKColl = new KeyedCollection();
			//获取数据
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection PvpKColl = dao.queryFirst("PvpAuthorize",null,"where serno='"+serno+"'",connection);
			KeyedCollection reflectKColl = TagUtil.getReflectKColl(PvpKColl);
			/** 获取借据信息中的贷款号 */
			KeyedCollection accLoan =  dao.queryDetail("AccLoan", (String) PvpKColl.getDataValue("bill_no"), connection);
			String base_acct_no = (String)accLoan.getDataValue("base_acct_no");
			String acct_seq_no = (String)accLoan.getDataValue("acct_seq_no");
			bodyKColl.put("DealTp", "02");//处理类型
			bodyKColl.put("LoanNo", base_acct_no);//贷款号
			bodyKColl.put("AcctNoSeqNo", acct_seq_no);//账号序号（发放号）
			bodyKColl.put("SysInd", "10");//系统标识符
//			bodyKColl.put("LoanNo", "99990202000040016");//贷款号
//			bodyKColl.put("AcctNoSeqNo", "123456");//账号序号（发放号）
			bodyKColl.put("OprtnTp", "01");//操作类型 
//											01:保存
//											02：修改
//											03：删除
//											如果变更数组有值，必填
			IndexedCollection loanModInfArry = new IndexedCollection("LoanModInfArry");//贷款变更信息数组
			KeyedCollection loanModInfArryItem = new KeyedCollection();
			//调整后的执行利率
			String ir_accord_type = (String)reflectKColl.getDataValue("NEW_INT_ACCORD_TYPE"); //利率依据方式
			String ir_adjust_type = (String)reflectKColl.getDataValue("NEW_FLT_MODE"); //利率调整方式
			String ir_float_type = (String)reflectKColl.getDataValue("NEW_RATE_FLUCT_MODE"); //利率浮动方式
			//loanModInfArryItem.put("IntCtgryTp", reflectKColl.getDataValue("INT_KIND"));//利息分类
			loanModInfArryItem.put("IntCtgryTp", "INT");//利息分类
			if("01".equals(ir_accord_type)||"03".equals(ir_accord_type) || "0".equals(ir_adjust_type)){//固定利率,议价利率依据,不计息，或者利率调整周期为固定不变
				loanModInfArryItem.put("AfModSubsAcctLvlFltPntPct", "");//分户级浮动百分点
				loanModInfArryItem.put("AfModSubsAcctLvlFltPct", "");//分户级浮动百分比
				loanModInfArryItem.put("AfModSubsAcctLvlFixIntRate", Double.parseDouble(reflectKColl.getDataValue("NEW_ACT_INT_RATE").toString())*100); //正常的执行利率
			}else if(("02".equals(ir_accord_type)||"04".equals(ir_accord_type)) && (!"0".equals(ir_adjust_type))){//浮动利率;牌告利率依据,正常利率上浮动,并且利率调整方式不为固定不变
				if("0".equals(ir_float_type)){//浮动方式0——加百分比，1——加点
					loanModInfArryItem.addDataField("AfModSubsAcctLvlFltPntPct", "");//分户级浮动百分点
					loanModInfArryItem.addDataField("AfModSubsAcctLvlFltPct", Double.parseDouble(reflectKColl.getDataValue("NEW_INT_RATE_FLT_RATE").toString())*100);//分户级浮动百分比
				}else{
					loanModInfArryItem.addDataField("AfModSubsAcctLvlFltPntPct", Double.parseDouble(reflectKColl.getDataValue("NEW_INT_FLOW_SPREAD").toString())/100);//分户级浮动百分点
					loanModInfArryItem.addDataField("AfModSubsAcctLvlFltPct", "");//分户级浮动百分比
				}
				loanModInfArryItem.put("AfModSubsAcctLvlFixIntRate", ""); //正常的执行利率
			}
			//loanModInfArryItem.put("NewIntRateTp", reflectKColl.getDataValue("NEW_INT_KIND"));//新利率类型
			loanModInfArryItem.put("NewIntRateTp", reflectKColl.getDataValue("IntRateTp"));//新利率类型
			loanModInfArryItem.put("TkEffDt", reflectKColl.getDataValue("NEW_INT_EFF_DATE"));//调整后的生效日期
			loanModInfArryItem.put("NewIntRateEnblMd","N");//暂时不予变更
			loanModInfArry.add(loanModInfArryItem);
			
			//调整后的逾期利率
			KeyedCollection loanModInfArryItem1 = new KeyedCollection();
			//loanModInfArryItem.put("IntCtgryTp", reflectKColl.getDataValue("INT_KIND"));//利息分类
			loanModInfArryItem1.put("IntCtgryTp", "ODP");//利息分类
			if("01".equals(ir_accord_type)||"03".equals(ir_accord_type) || "0".equals(ir_adjust_type)){//固定利率,议价利率依据,不计息，或者利率调整周期为固定不变
				loanModInfArryItem1.put("AfModSubsAcctLvlFltPntPct", "");//分户级浮动百分点
				loanModInfArryItem1.put("AfModSubsAcctLvlFltPct", "");//分户级浮动百分比
				loanModInfArryItem1.put("AfModSubsAcctLvlFixIntRate", Double.parseDouble(reflectKColl.getDataValue("NEW_OVERDUE_INT_RATE").toString())*100);
			}else if(("02".equals(ir_accord_type)||"04".equals(ir_accord_type)) && (!"0".equals(ir_adjust_type))){//浮动利率;牌告利率依据,正常利率上浮动,并且利率调整方式不为固定不变
				loanModInfArryItem1.addDataField("AfModSubsAcctLvlFltPntPct", "");//分户级浮动百分点
				loanModInfArryItem1.addDataField("AfModSubsAcctLvlFltPct", Double.parseDouble(reflectKColl.getDataValue("NEW_OVERDUE_FLT_RATE").toString())*100);//分户级浮动百分比
				loanModInfArryItem1.put("AfModSubsAcctLvlFixIntRate", ""); //正常的执行利率
			}
			//loanModInfArryItem.put("NewIntRateTp", reflectKColl.getDataValue("NEW_INT_KIND"));//新利率类型
			loanModInfArryItem1.put("NewIntRateTp", reflectKColl.getDataValue("IntRateTp"));//新利率类型
			loanModInfArryItem1.put("TkEffDt", reflectKColl.getDataValue("NEW_INT_EFF_DATE"));//调整后生效日期
			loanModInfArryItem1.put("NewIntRateEnblMd","N");//暂时不予变更
			loanModInfArry.add(loanModInfArryItem1);
			//调整后的违约利率
			KeyedCollection loanModInfArryItem2 = new KeyedCollection();
			//loanModInfArryItem.put("IntCtgryTp", reflectKColl.getDataValue("INT_KIND"));//利息分类
			loanModInfArryItem2.put("IntCtgryTp", "ODI");//利息分类
			if("01".equals(ir_accord_type)||"03".equals(ir_accord_type) || "0".equals(ir_adjust_type)){//固定利率,议价利率依据,不计息，或者利率调整周期为固定不变
				loanModInfArryItem2.put("AfModSubsAcctLvlFltPntPct", "");//分户级浮动百分点
				loanModInfArryItem2.put("AfModSubsAcctLvlFltPct", "");//分户级浮动百分比
				loanModInfArryItem2.put("AfModSubsAcctLvlFixIntRate", Double.parseDouble(reflectKColl.getDataValue("NEW_PNT_INT_RATE").toString())*100);
			}else if(("02".equals(ir_accord_type)||"04".equals(ir_accord_type)) && (!"0".equals(ir_adjust_type))){//浮动利率;牌告利率依据,正常利率上浮动,并且利率调整方式不为固定不变
				loanModInfArryItem2.addDataField("AfModSubsAcctLvlFltPntPct", "");//分户级浮动百分点
				loanModInfArryItem2.addDataField("AfModSubsAcctLvlFltPct", Double.parseDouble(reflectKColl.getDataValue("NEW_PNY_INT_FLT_RATE").toString())*100);//分户级浮动百分比
				loanModInfArryItem2.put("AfModSubsAcctLvlFixIntRate", ""); //正常的执行利率
			}
			//loanModInfArryItem.put("NewIntRateTp", reflectKColl.getDataValue("NEW_INT_KIND"));//新利率类型
			loanModInfArryItem2.put("NewIntRateTp", reflectKColl.getDataValue("IntRateTp"));//新利率类型
			loanModInfArryItem2.put("TkEffDt", reflectKColl.getDataValue("NEW_INT_EFF_DATE"));//生效日期
			loanModInfArryItem2.put("NewIntRateEnblMd","N");//暂时不予变更
			loanModInfArry.add(loanModInfArryItem2);
			
			
			bodyKColl.addDataElement(loanModInfArry);
			
			/** 执行发送操作 */
			KeyedCollection rsp = ESBUtil.sendEsbMsg(headKColl, bodyKColl);
			
			/** 解析反馈报文头判断该笔交易成功与否 */
			KeyedCollection rspSysHead = (KeyedCollection)rsp.get("SYS_HEAD");
			IndexedCollection retInfoIColl = (IndexedCollection) rspSysHead.getDataElement("RetInfArry");
			Object obj = retInfoIColl.get(0);
			KeyedCollection returnKColl = (KeyedCollection)rsp.get("SYS_HEAD");
			if(obj instanceof KeyedCollection){
				KeyedCollection kc = (KeyedCollection)obj;
				String retCd = (String) kc.getDataValue("RetCd");
				String retSts = (String) kc.getDataValue("RetInf");
				returnKColl.put("RET_CODE", retCd);
				returnKColl.put("RET_STATUS", retSts);
				returnKColl.addDataElement(rsp.getDataElement("BODY"));
			}
			return returnKColl;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("利率调整授权授权发送失败"+e.getMessage());
		}
	}
	/**
	 * 利率调整授权
	 * @param  利率调整申请业务流水号 serno，context，connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 */
	public KeyedCollection tradeLVTZ_UNUSE(String serno,Context context, Connection connection) throws Exception {
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData();
		try{
			String authorize_no = null;
			//获取dao
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection PvpKColl = dao.queryFirst("PvpAuthorize",null,"where serno='"+serno+"'",connection);
			authorize_no = (String) PvpKColl.getDataValue("authorize_no");
			/** 系统头 */
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(TradeConstance.SERVICE_CODE_DKLLXXWH, TradeConstance.SERVICE_SCENE_LLTZSQ, TradeConstance.CONSUMERID, TagUtil.getSysHeadSerno(authorize_no, context),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", this.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			
			KeyedCollection reflectKColl = TagUtil.getReflectKColl(PvpKColl);
			CompositeData bodyArrCD = new CompositeData();
			bodyArrCD.addField("GEN_GL_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("GEN_GL_NO")), FieldType.FIELD_STRING, 30, 0));
			bodyArrCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 30, 0));
//			bodyArrCD.addField("CLIENT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CLIENT_NO"), FieldType.FIELD_STRING, 30, 0));
//			bodyArrCD.addField("CCY", TagUtil.getEMPField(reflectKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 3, 0));
//			bodyArrCD.addField("LOAN_AMT", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("LOAN_AMT")), FieldType.FIELD_DOUBLE, 20, 2));
//			bodyArrCD.addField("LOAN_BALANCE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("LOAN_BALANCE")), FieldType.FIELD_DOUBLE, 20, 2));
//			bodyArrCD.addField("DRAW_DOWN_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("DRAW_DOWN_DATE"), FieldType.FIELD_STRING, 8, 0));
//			bodyArrCD.addField("EXPIRY_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("EXPIRY_DATE"), FieldType.FIELD_STRING, 8, 0));
//			bodyArrCD.addField("INT_ACCORD_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("INT_ACCORD_TYPE"), FieldType.FIELD_STRING, 5, 0));
//			bodyArrCD.addField("INT_KIND", TagUtil.getEMPField(reflectKColl.getDataValue("INT_KIND"), FieldType.FIELD_STRING, 15, 0));
//			bodyArrCD.addField("BASE_INT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("BASE_INT_RATE"), FieldType.FIELD_DOUBLE, 20, 7));
//			bodyArrCD.addField("FLUCTUATION_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("FLUCTUATION_MODE"), FieldType.FIELD_STRING, 8, 0));
//			bodyArrCD.addField("INTEST_RATE_FLUCT_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("INTEST_RATE_FLUCT_MODE"), FieldType.FIELD_STRING, 10, 0));
			bodyArrCD.addField("INT_RATE_FLT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("INT_RATE_FLT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
			bodyArrCD.addField("INT_RATE_FLOW_SPREAD", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("INT_RATE_FLOW_SPREAD")), FieldType.FIELD_DOUBLE, 20, 7));
//			bodyArrCD.addField("ADVANCE_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("ADVANCE_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
			bodyArrCD.addField("ACT_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("ACT_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
//			bodyArrCD.addField("OVERDUE_FLT_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("OVERDUE_FLT_MODE"), FieldType.FIELD_STRING, 5, 0));
			bodyArrCD.addField("OVERDUE_FLT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("OVERDUE_FLT_RATE")), FieldType.FIELD_DOUBLE, 20, 9));
			bodyArrCD.addField("OVERDUE_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("OVERDUE_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
//			bodyArrCD.addField("PENALTY_INT_FLT_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("PENALTY_INT_FLT_MODE"), FieldType.FIELD_STRING, 5, 0));
			bodyArrCD.addField("PNY_INT_FLT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("PNY_INT_FLT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
//			bodyArrCD.addField("PNY_RATE_FLOW_SPREAD", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("PNY_RATE_FLOW_SPREAD")), FieldType.FIELD_DOUBLE, 20, 7));
			bodyArrCD.addField("PENALTY_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("PENALTY_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
//			bodyArrCD.addField("BASE_INT_RATE_CODE", TagUtil.getEMPField(reflectKColl.getDataValue("BASE_INT_RATE_CODE"), FieldType.FIELD_STRING, 10, 0));
//			bodyArrCD.addField("NEW_BASE_RATE_CODE", TagUtil.getEMPField(reflectKColl.getDataValue("NEW_BASE_RATE_CODE"), FieldType.FIELD_STRING, 10, 0));
//			bodyArrCD.addField("NEW_INT_ACCORD_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("NEW_INT_ACCORD_TYPE"), FieldType.FIELD_STRING, 5, 0));
//			bodyArrCD.addField("NEW_INT_KIND", TagUtil.getEMPField(reflectKColl.getDataValue("NEW_INT_KIND"), FieldType.FIELD_STRING, 15, 0));
//			bodyArrCD.addField("NEW_BASE_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_BASE_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
//			bodyArrCD.addField("NEW_ADV_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_ADV_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
//			bodyArrCD.addField("NEW_FLT_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("NEW_FLT_MODE"), FieldType.FIELD_STRING, 8, 0));
//			bodyArrCD.addField("NEW_RATE_FLUCT_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("NEW_RATE_FLUCT_MODE"), FieldType.FIELD_STRING, 10, 0));
//			bodyArrCD.addField("NEW_OVERDUE_FLT_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("NEW_OVERDUE_FLT_MODE"), FieldType.FIELD_STRING, 5, 0));
//			bodyArrCD.addField("NEW_PNY_INT_FLT_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("NEW_PNY_INT_FLT_MODE"), FieldType.FIELD_STRING, 5, 0));
			bodyArrCD.addField("NEW_PNY_INT_FLT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_PNY_INT_FLT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
//			bodyArrCD.addField("NEW_PNY_FLOW_SPREAD", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_PNY_FLOW_SPREAD")), FieldType.FIELD_DOUBLE, 20, 7));
			bodyArrCD.addField("NEW_PNT_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_PNT_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
			bodyArrCD.addField("NEW_INT_RATE_FLT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_INT_RATE_FLT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
			bodyArrCD.addField("NEW_INT_FLOW_SPREAD", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_INT_FLOW_SPREAD")), FieldType.FIELD_DOUBLE, 20, 7));
			bodyArrCD.addField("NEW_ACT_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_ACT_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
			bodyArrCD.addField("NEW_OVERDUE_FLT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_OVERDUE_FLT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
			bodyArrCD.addField("NEW_OVERDUE_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_OVERDUE_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
//			bodyArrCD.addField("NEW_INT_EFF_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("NEW_INT_EFF_DATE"), FieldType.FIELD_STRING, 8, 0));
			bodyArrCD.addField("FLUCTUATION_MODE", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("NEW_FLT_MODE")), FieldType.FIELD_STRING, 8, 0));
			
			reqCD.addStruct("BODY", bodyArrCD);
			
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			
			if(TagUtil.haveSuccess(retKColl, context)){//执行成功
				//根据授权编号更新授权表的状态为已授权
				SqlClient.update("updatePvpAuthorizeStatusByGenNo", authorize_no, "02", null, connection);
			}else{
				throw new Exception("利率调整授权授权发送失败!");
			}
			return retKColl;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("利率调整授权授权发送失败"+e.getMessage());
		}
	}
	
	/**
	 * 保证金变更
	 * @param  保证金变更申请业务流水号 serno，context，connection
	 * @return 返回报文头信息及撤销标志
	 * @throws Exception 
	 */
	public KeyedCollection tradeBZJBG(String serno,Context context, Connection connection) throws Exception {
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData();
		try{
			String authorize_no = null;
			//获取dao
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection PvpKColl = dao.queryFirst("PvpAuthorize",null,"where serno='"+serno+"'",connection);
			authorize_no = (String) PvpKColl.getDataValue("authorize_no");
			/** 系统头 */
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(TradeConstance.SERVICE_CODE_BZJBG, TradeConstance.SERVICE_SCENE_BZJBG, TradeConstance.CONSUMERID, TagUtil.getSysHeadSerno(authorize_no, context),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", this.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), 
					TradeConstance.TRADE_SYSTEM));
			
			KeyedCollection authKColl1 = dao.queryFirst("PvpAuthorize", null, " where serno = '"+serno+"' and status is null", connection);
			KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl1);
			CompositeData bodyArrCD = new CompositeData();
			bodyArrCD.addField("GEN_GL_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("GEN_GL_NO")), FieldType.FIELD_STRING, 30, 0));
			bodyArrCD.addField("CONTRACT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 30, 0));
			bodyArrCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 50, 0));
			bodyArrCD.addField("BRANCH_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
			bodyArrCD.addField("GUARANTEE_ACCT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("GUARANTEE_ACCT_NO"), FieldType.FIELD_STRING, 50, 0));
			bodyArrCD.addField("GUARANTEE_NO", TagUtil.getEMPField(reflectKColl.getDataValue("GUARANTEE_NO"), FieldType.FIELD_STRING, 50, 0));
			bodyArrCD.addField("ACCT_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("ACCT_TYPE"), FieldType.FIELD_STRING, 6, 0));
			bodyArrCD.addField("GUARANTEE_EXPIRY_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("GUARANTEE_EXPIRY_DATE"), FieldType.FIELD_STRING, 8, 0));
			
			bodyArrCD.addField("ACCT_CODE", TagUtil.getEMPField(reflectKColl.getDataValue("ACCT_CODE"), FieldType.FIELD_STRING, 10, 0));
			bodyArrCD.addField("CA_TT_FLAG", TagUtil.getEMPField(reflectKColl.getDataValue("CA_TT_FLAG"), FieldType.FIELD_STRING, 2, 0));
			bodyArrCD.addField("ACCT_GL_CODE", TagUtil.getEMPField(reflectKColl.getDataValue("ACCT_GL_CODE"), FieldType.FIELD_STRING, 10, 2));
			bodyArrCD.addField("ACCT_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("ACCT_NAME"), FieldType.FIELD_STRING, 150, 0));
			bodyArrCD.addField("CCY", TagUtil.getEMPField(reflectKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 3, 0));
			bodyArrCD.addField("CONTRACT_GUARANTEE_PER", TagUtil.getEMPField(reflectKColl.getDataValue("CONTRACT_GUARANTEE_PER"), FieldType.FIELD_DOUBLE, 20, 7));
			bodyArrCD.addField("CONTRACT_GUARANTEE_AMT", TagUtil.getEMPField(reflectKColl.getDataValue("CONTRACT_GUARANTEE_AMT"), FieldType.FIELD_DOUBLE, 20, 2));
			bodyArrCD.addField("ACCT_GUARANTEE_AMT", TagUtil.getEMPField(reflectKColl.getDataValue("ACCT_GUARANTEE_AMT"), FieldType.FIELD_DOUBLE, 20, 2));
			bodyArrCD.addField("GUARANTEE_MOD_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("GUARANTEE_MOD_TYPE"), FieldType.FIELD_STRING, 20, 0));
			reqCD.addStruct("BODY", bodyArrCD);
			
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			
			if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
				throw new Exception("保证金变更授权发送失败!");
			}
			return retKColl;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("保证金变更授权发送失败"+e.getMessage());
		}
	}
	
	/**
	 * 信贷保证金追加实时通知国结
	 * @return 返回报文头信息及撤销标志
	 * @throws Exception 
	 */
	public KeyedCollection tradeBZJZJTZGJ(String authorize_no,Context context, Connection connection) throws Exception {
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData();
		try{
			//获取dao
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection PvpKColl = dao.queryFirst("PvpAuthorize",null,"where authorize_no='"+authorize_no+"'",connection);
			String serno = (String) PvpKColl.getDataValue("serno");
			KeyedCollection appkc = dao.queryDetail("IqpBailSubDis", serno, connection);
			String cont_no = (String) appkc.getDataValue("cont_no");
			KeyedCollection bailkc = dao.queryFirst("PubBailInfo", null, " where cont_no = '"+cont_no+"'", connection);
			/** 系统头 */
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(TradeConstance.SERVICE_CODE_BZJZJTZGJ, TradeConstance.SERVICE_SCENE_BZJZJTZGJ, TradeConstance.CONSUMERID, TagUtil.getSysHeadSerno(authorize_no, context),
					"admin","admin", "9350000000", TradeConstance.TRADE_SYSTEM, context));
			
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", this.getAppHeadCD("admin","admin", "9350000000",TradeConstance.TRADE_SYSTEM));
			
			CompositeData bodyArrCD = new CompositeData();
			bodyArrCD.addField("SEQ_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(serno), FieldType.FIELD_STRING, 52, 0));
			bodyArrCD.addField("DUEBILL_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(""), FieldType.FIELD_STRING, 50, 0));
			bodyArrCD.addField("CONTRACT_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(cont_no), FieldType.FIELD_STRING, 60, 0));
			bodyArrCD.addField("CLIENT_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(appkc.getDataValue("cus_id")), FieldType.FIELD_STRING, 30, 0));
			bodyArrCD.addField("REGIST_ORG_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(appkc.getDataValue("input_br_id")), FieldType.FIELD_STRING, 20, 0));
			bodyArrCD.addField("GUARANTEE_ACCT_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(bailkc.getDataValue("bail_acct_no")), FieldType.FIELD_STRING, 50, 0));
			bodyArrCD.addField("GUARANTEE_ADD_AMT", TagUtil.getEMPField(TagUtil.replaceNull4Double(appkc.getDataValue("adjust_bail_amt")), FieldType.FIELD_DOUBLE, 20, 2));
			bodyArrCD.addField("GUARANTEE_CCY", TagUtil.getEMPField(TagUtil.replaceNull4String(bailkc.getDataValue("cur_type")), FieldType.FIELD_STRING, 3, 0));
			bodyArrCD.addField("BAK_FIELD", TagUtil.getEMPField(TagUtil.replaceNull4String(""), FieldType.FIELD_STRING, 300, 0));
			bodyArrCD.addField("BAK_FIELD2", TagUtil.getEMPField(TagUtil.replaceNull4String(""), FieldType.FIELD_STRING, 300, 0));
			
			reqCD.addStruct("BODY", bodyArrCD);
			
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			
			if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
				throw new Exception("贷保证金追加实时通知国结!");
			}
			return retKColl;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("贷保证金追加实时通知国结"+e.getMessage());
		}
	}
	
	/**
	 * 获取12级分类任务是否完成(02003000002  56)
	 * @param  managerId 客户经理代码
	 * @param  roleId 角色代码
	 * @param  taskType 任务类型
	 * @return 返回报文头信息
	 * @throws Exception 
	 */
	public KeyedCollection tradeSEJFLRW(String managerId,String roleId,String taskType,Context context,Connection connection) throws Exception {
		try{
			/** 封装发送报文信息 */
			CompositeData reqCD= new CompositeData();
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			/** 系统头 */
			reqCD.addStruct("SYS_HEAD", this.getSysHeadCD("02003000002", "56", TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.CONSUMERID, context));
//			CompositeData LOCAL_HEAD= new CompositeData();
//			LOCAL_HEAD.addField("FILE_FLAG", TagUtil.getEMPField("0", FieldType.FIELD_STRING, 1, 0));
//			reqCD.addStruct("LOCAL_HEAD", LOCAL_HEAD);
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", this.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			/** 封装报文体 */
			CompositeData bodyCD = new CompositeData();
			bodyCD.addField("CUST_MANAGER_ID", TagUtil.getEMPField(managerId, FieldType.FIELD_STRING, 16, 0));//客户经理代码
			bodyCD.addField("ROLE_CODE", TagUtil.getEMPField(roleId, FieldType.FIELD_STRING, 10, 0));//角色码
			bodyCD.addField("TASK_TYPE", TagUtil.getEMPField(taskType, FieldType.FIELD_STRING, 10, 0));//任务类型
			reqCD.addStruct(TradeConstance.ESB_BODY, bodyCD);
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			
			if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
				throw new Exception("十二级分类任务通讯失败!");
			}else{
				/** 解析反馈报文体 */
				KeyedCollection kColl = this.getRespBodyCD4KColl(retCD);
				kColl.setName("BODY");
				retKColl.addDataElement(kColl);
			}
			return retKColl;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("十二级分类任务通讯失败"+e.getMessage());
		}
	}
	
	/**
	 * 保证金利息试算
	 * @param  保证金账号
	 * @param  出账流水号/保函修改，信用证改正流水号
	 * @param  保函修改，信用证保证金金额
	 * @param  表名
	 * @return 返回报文头信息及撤销标志
	 * @throws Exception 
	 */
	public KeyedCollection tradeBZJLXSS(String bail_acct_no,String serno,BigDecimal seAmt,String tableName,Context context, Connection connection) throws Exception {
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData();
		try{
			String authorize_no = null;
			BigDecimal security_amt = new BigDecimal(0);
			//获取dao
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			int size = 0;
			/** 系统头 */
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			IqpServiceInterface serviceIqp = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(TradeConstance.SERVICE_CODE_BZJLXSS, TradeConstance.SERVICE_SCENE_BZJLXSS, TradeConstance.CONSUMERID, TagUtil.getSysHeadSerno(authorize_no, context),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			/** 组装上传到FTP上的文件*/
			String filename = TradeConstance.SERVICE_CODE_BZJLXSS+"_"+TradeConstance.SERVICE_SCENE_BZJLXSS+"_"+serno+".txt";//拼装文件名
			StringBuffer sb = new StringBuffer();
			if(seAmt != null){
				int term = 0;
				size=1;
				if("IqpGuarantChangeApp".equals(tableName)){
					KeyedCollection kCollChange = dao.queryDetail(tableName, serno, connection);
					String cont_no = (String)kCollChange.getDataValue("cont_no");
					KeyedCollection kCollCtrSub = dao.queryDetail("CtrLoanContSub", cont_no, connection);
					String cont_term = (String)kCollCtrSub.getDataValue("cont_term");
					String term_type = (String)kCollCtrSub.getDataValue("term_type");
					if(cont_term != null && !"".equals(cont_term) && term_type != null && !"".equals(term_type)){
						term = Integer.parseInt(cont_term);
						term = DateUtils.getChangeTerm(term_type,term);
					}
				}
				//组装文件
				sb.append(bail_acct_no).append("|").append(seAmt).append("|").append(term).append("|");
				sb.append("\n");
			}else{
				KeyedCollection pvpKColl = dao.queryDetail(tableName, serno, connection);
				String prd_id = (String)pvpKColl.getDataValue("prd_id");
				String cont_no = (String)pvpKColl.getDataValue("cont_no");
				
				KeyedCollection kCollCtr = dao.queryDetail("CtrLoanCont", cont_no, connection);
				String sernoIqp = (String)kCollCtr.getDataValue("serno");
				if("200024".equals(prd_id)){
					IndexedCollection iCollAccDetail = dao.queryList("IqpAccpDetail", "where serno='"+sernoIqp+"'", connection);
					size = iCollAccDetail.size();
					for(int i=0;i<iCollAccDetail.size();i++){
						KeyedCollection kCollAccDetail = (KeyedCollection)iCollAccDetail.get(i);
						BigDecimal drft_amt = BigDecimalUtil.replaceNull(kCollAccDetail.getDataValue("drft_amt"));
						String term_type = (String)kCollAccDetail.getDataValue("term_type");
						int term = Integer.parseInt(kCollAccDetail.getDataValue("term")+"");
						term = DateUtils.getChangeTerm(term_type,term);
						//获取保证金金额
						security_amt = BigDecimalUtil.replaceNull(serviceIqp.getBailAmt(cont_no,drft_amt, context, connection));
						//组装文件
						sb.append(bail_acct_no).append("|").append(security_amt).append("|").append(term).append("|");
						sb.append("\n");
					}
				}else if("300020".equals(prd_id) || "300021".equals(prd_id)){
					String condition = "where porder_no in(select rel.porder_no from Iqp_Batch_Bill_Rel rel where rel.batch_no in(select batch.batch_no from Iqp_Batch_Mng batch where batch.cont_no ='"+cont_no+"'))";
				    IndexedCollection iCollBillDetail = dao.queryList("IqpBillDetail", condition, connection);
				    size = iCollBillDetail.size();
				    for(int i=0;i<iCollBillDetail.size();i++){
				    	KeyedCollection kCollBillDetail = (KeyedCollection)iCollBillDetail.get(i);
						BigDecimal drft_amt = BigDecimalUtil.replaceNull(kCollBillDetail.getDataValue("drft_amt"));
						String porder_no = (String)kCollBillDetail.getDataValue("porder_no");
						String conditionStr = "where batch_no =(select batch.batch_no from Iqp_Batch_Mng batch where batch.cont_no='"+cont_no+"') and porder_no='"+porder_no+"'";
						KeyedCollection kCollBillIncome = dao.queryFirst("IqpBillIncome", null, conditionStr, connection);
						int disc_days = Integer.parseInt(kCollBillIncome.getDataValue("disc_days")+"");
						int adj_days = Integer.parseInt(kCollBillIncome.getDataValue("adj_days")+"");
						int term = disc_days+adj_days;
						//获取保证金金额
						security_amt = BigDecimalUtil.replaceNull(serviceIqp.getBailAmt(cont_no,drft_amt, context, connection));
						//组装文件
						sb.append(bail_acct_no).append("|").append(security_amt).append("|").append(term).append("|");
						sb.append("\n");
				    }
				}else{
					size=1;
					KeyedCollection kCollCtrSub = dao.queryDetail("CtrLoanContSub", cont_no, connection);
					String cont_term = (String)kCollCtrSub.getDataValue("cont_term");
					String term_type = (String)kCollCtrSub.getDataValue("term_type");
					int term = 0;
					if(cont_term != null && !"".equals(cont_term) && term_type != null && !"".equals(term_type)){
						term = Integer.parseInt(cont_term);
						term = DateUtils.getChangeTerm(term_type,term);
					}
					//获取保证金金额
					security_amt = BigDecimalUtil.replaceNull(serviceIqp.getBailAmt(cont_no,new BigDecimal(0), context, connection));
					//组装文件
					sb.append(bail_acct_no).append("|").append(security_amt).append("|").append(term).append("|");
					sb.append("\n");
				}
			}
			
			/** 应用头 */
			CompositeData APP_HEAD= esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM);
			APP_HEAD.addField("FILE_NAME",TagUtil.getEMPField(filename, FieldType.FIELD_STRING, 30, 0));
			APP_HEAD.addField("FILE_PATH",TagUtil.getEMPField(FTPUtil.getFilePath(), FieldType.FIELD_STRING, 512, 0));
			APP_HEAD.addField("TOTAL_ROWS",TagUtil.getEMPField(size, FieldType.FIELD_STRING, 12, 0));
			reqCD.addStruct("APP_HEAD", APP_HEAD);
			/**扩展头**/
			CompositeData LOCAL_HEAD= new CompositeData();
			LOCAL_HEAD.addField("FILE_FLAG", TagUtil.getEMPField("1", FieldType.FIELD_STRING, 1, 0));
			reqCD.addStruct("LOCAL_HEAD", LOCAL_HEAD);
			FTPUtil.send2FTP4Txt(filename,sb);
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 解析反馈报文头 */
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
				
			}else{
				/** 解析反馈报文体 */
				KeyedCollection kColl = this.getRespBodyCD4KColl(retCD);
				kColl.setName("BODY");
				retKColl.addDataElement(kColl);
			}
			if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
				throw new Exception(retKColl.getDataValue("RET_MSG")+"");
			}
			return retKColl;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("保证金利息试算接口发送失败"+e.getMessage());
		}
	}
	
	/**
	 * 自助贷款协议授权信息采集
	 * @param contNo 合同编号
	 * @throws Exception
	 * added by yangzy 2015/04/07 需求编号:XD150318023,微贷平台零售自助贷款改造
	 */
	public void doWfAgreeForSelfLoan(String contNo,Context context, Connection connection)throws Exception {
		try {
			String IQPLOANAPPMODEL = "IqpLoanApp";//贷款申请
			String CTRCONTSUBMODEL = "CtrLoanContSub";//合同从表
			String AUTHORIZEMODEL = "PvpAuthorize";//出账授权
			String AUTHORIZESUBMODEL = "PvpAuthorizeSub";//授权信息从表
			String PUBBAILINFO= "PubBailInfo";//保证金信息表
			String IQPCUSACCT= "IqpCusAcct";//账户信息表
			String IQPFEE= "IqpAppendTerms";//账户信息表
			String PrdRepayMode= "PrdRepayMode";//还款方式
			
			if(contNo == null || contNo == ""){
				throw new Exception("获取业务合同编号失败！");
			}
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			/** 1.数据准备：通过业务流水号查询【合同】 */
			KeyedCollection ctrLoanKColl = dao.queryDetail("CtrLoanCont", contNo, connection);
			String Iqpserno = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("serno"));//授权交易流水号
			String prd_id = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("prd_id"));//产品编号
			String cus_id = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("cus_id"));//客户编码
			String cont_no = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("cont_no"));//合同编号
			BigDecimal cont_amt = BigDecimalUtil.replaceNull(ctrLoanKColl.getDataValue("cont_amt"));//协议金额
			String manager_br_id = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("manager_br_id"));//管理机构
			String in_acct_br_id = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("in_acct_br_id"));//入账机构
			String cur_type = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("cont_cur_type"));//币种
			String date = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			//String input_id = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("input_id"));//登记人
			String input_br_id = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("input_br_id"));//登记机构
			
			String openDay = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			context.put("OPENDAY",openDay);
			context.put("currentUserId","admin");
			context.put("currentUserName","超级管理员");
			context.put("organNo",input_br_id);
			
			/** 核算与信贷业务品种映射 START */
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			String lmPrdId = service.getPrdBasicCLPM2LM(cont_no, prd_id, context, connection);
			/** 核算与信贷业务品种映射 END */
			
			/** 自助业务申请类型 */
			String apply_type = "";
			if("100085".equals(prd_id) || "100084".equals(prd_id)){
				apply_type = "01";//个人自助
			}
			
			/** 2.数据准备：通过业务流水号查询【业务申请】【合同信息】 */					
			KeyedCollection ctrContSubKColl =  dao.queryDetail(CTRCONTSUBMODEL, cont_no, connection);
			String loanSerno = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("serno"));//业务申请流水号
			String ruling_ir = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ruling_ir"));//基准利率（年）
			String ir_float_rate = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_float_rate"));//浮动比例
			String ir_float_point = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_float_point"));//浮动点数
			String reality_ir_y = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("reality_ir_y"));//执行利率（年）
			String cont_start_date = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("cont_start_date"));//合同起始日期
			String cont_end_date = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("cont_end_date"));//合同到期日期
			String repay_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_type"));//还款方式
			Double security_rate = TagUtil.replaceNull4Double(ctrLoanKColl.getDataValue("security_rate"));//保证金比例
			String repay_mode_type = "";//还款方式种类
			if(repay_type!=null && !"".equals(repay_type)){
				KeyedCollection prdRepayModeKColl = dao.queryDetail(PrdRepayMode, repay_type, connection);
				repay_mode_type = TagUtil.replaceNull4String(prdRepayModeKColl.getDataValue("repay_mode_type"));
			}   
			String card_no = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("cus_card_code"));//卡号
			//String is_collect_stamp = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("is_collect_stamp"));//是否收取印花税
			//String stamp_collect_mode = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("stamp_collect_mode"));//印花税收取方式
			String pay_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("pay_type"));//支付方式
			//信贷支付方式为：0：自主支付 1：受托支付 2：跨境受托支付。核算支付方式为：1：自主支付 2：受托支付 3：跨境受托支付
			pay_type= "1";
			//获取委托贷款关联信息
			BigDecimal contAmt = new BigDecimal(TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("cont_amt")));//合同金额
			String cont_cur_type = TagUtil.replaceNull4String(ctrLoanKColl.getDataValue("cont_cur_type"));//合同币种
			Integer cont_term = TagUtil.replaceNull4Int(ctrContSubKColl.getDataValue("cont_term"));//合同期限
			String term_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("term_type"));//期限类型
			String repay_date = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_date"));//还款日
			String ir_accord_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_accord_type"));//利率依据方式
			String ruling_ir_code = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ruling_ir_code"));//基准利率代码
			Double overdue_rate_y = TagUtil.replaceNull4Double(ctrContSubKColl.getDataValue("overdue_rate_y"));//罚息执行利率
			String repay_term = "";
			String repay_space = "";
			if(repay_type.equals("A005")){//利随本清传1月
				repay_term = "M";//还款间隔单位
				repay_space = "1";//还款间隔
			}else{
				repay_term = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_term"));//还款间隔单位
				repay_space = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("repay_space"));//还款间隔
			}
			Double default_rate =  TagUtil.replaceNull4Double(ctrContSubKColl.getDataValue("default_rate"));//罚息执行利率浮动比
			String ir_adjust_type = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("ir_adjust_type"));//下一次利率调整选项
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
			Double overdue_rate = TagUtil.replaceNull4Double(ctrContSubKColl.getDataValue("overdue_rate"));//逾期利率浮动比
			String is_term = TagUtil.replaceNull4String(ctrContSubKColl.getDataValue("is_term"));//期供标志
			if(is_term.equals("1")){
				is_term = "Y";
			}else{
				is_term = "N";
			}
			
			//获取贷款申请相关信息
			KeyedCollection iqpLoanAppKColl =  dao.queryDetail(IQPLOANAPPMODEL, loanSerno, connection);
			String apply_date = TagUtil.replaceNull4String(iqpLoanAppKColl.getDataValue("apply_date"));//业务申请日期

			//通过客户编号查询【客户信息】
			CMISModualServiceFactory jndiService = CMISModualServiceFactory.getInstance();
			CusServiceInterface csi = (CusServiceInterface)jndiService.getModualServiceById("cusServices", "cus");
			CusBase cusBase = csi.getCusBaseByCusId(cus_id,context,connection);
			String cus_name = TagUtil.replaceNull4String(cusBase.getCusName());//客户名称
			String cert_type = TagUtil.replaceNull4String(cusBase.getCertType());//证件类型
			String cert_code = TagUtil.replaceNull4String(cusBase.getCertCode());//证件号码
			
			//生成授权主表信息
			KeyedCollection authorizeKColl = new KeyedCollection(AUTHORIZEMODEL);
			String tranSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connection, context);//生成交易流水号
			String authSerno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connection, context);//生成授权编号
			authorizeKColl.addDataField("tran_serno", tranSerno);//交易流水号
			authorizeKColl.addDataField("serno", Iqpserno);//业务流水号
			authorizeKColl.addDataField("authorize_no", authSerno);//授权编号
			authorizeKColl.addDataField("prd_id", prd_id);//产品编号
			authorizeKColl.addDataField("cus_id", cus_id);//客户编码
			authorizeKColl.addDataField("cus_name", cus_name);//客户名称
			authorizeKColl.addDataField("cont_no", cont_no);//合同编号
			authorizeKColl.addDataField("bill_no", "");//借据编号
			authorizeKColl.addDataField("tran_id", TradeConstance.SERVICE_CODE_DKFFSQ + TradeConstance.SERVICE_SCENE_ZZDKFFSQ);
			authorizeKColl.addDataField("tran_amt", cont_amt);//交易金额
			authorizeKColl.addDataField("tran_date", date);//交易日期
			authorizeKColl.addDataField("send_times", "0");//发送次数
			authorizeKColl.addDataField("return_code", "");//返回编码
			authorizeKColl.addDataField("return_desc", "");//返回信息
			authorizeKColl.addDataField("manager_br_id", manager_br_id);//管理机构
			authorizeKColl.addDataField("in_acct_br_id", in_acct_br_id);//入账机构
			authorizeKColl.addDataField("status", "00");//状态
			authorizeKColl.addDataField("cur_type", cur_type);//币种
			
			//费用信息
			String conditionFee = "where serno='"+loanSerno+"'";
			IndexedCollection iqpAppendTermsIColl = dao.queryList(IQPFEE, conditionFee, connection);
			
			//计算手续费率  start
			BigDecimal chrg_rate = new BigDecimal("0.00");
			BigDecimal commission = new BigDecimal("0.00");
			for(int fee_i=0;fee_i<iqpAppendTermsIColl.size();fee_i++){
				KeyedCollection feekc = (KeyedCollection) iqpAppendTermsIColl.get(fee_i);
				String fee_rate_str = TagUtil.replaceNull4String(feekc.getDataValue("fee_rate"));
				if(fee_rate_str==null||fee_rate_str.equals("")){
					fee_rate_str = "0";
				}
				BigDecimal fee_rate = new BigDecimal(fee_rate_str);
				chrg_rate = chrg_rate.add(fee_rate);
				
				//手续费不使用手续费率计算，固定金额直接加，防止精度丢失
				String collect_type = TagUtil.replaceNull4String(feekc.getDataValue("collect_type"));//01-按固定金额，02-按比率
				BigDecimal fee_amt = BigDecimalUtil.replaceNull(feekc.getDataValue("fee_amt"));
				if("02".equals(collect_type)){
					commission = commission.add(cont_amt.multiply(fee_rate));
				}else{
					commission = commission.add(fee_amt);
				}
			}
			//计算手续费率  end
			
			/*其他普通贷款：授权信息组装*/

			authorizeKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编码
			authorizeKColl.addDataField("fldvalue02", "APPLY_TYPE@" + apply_type);//申请方式	
			authorizeKColl.addDataField("fldvalue03", "APPLY_NO@" + Iqpserno);//业务流水号
			authorizeKColl.addDataField("fldvalue04", "APPLY_DATE@" + TagUtil.formatDate(apply_date));//业务申请日期
			authorizeKColl.addDataField("fldvalue05", "CONTRACT_NO@" + cont_no);//合同编号
			authorizeKColl.addDataField("fldvalue06", "BRANCH_ID@" + in_acct_br_id);//入账机构
			authorizeKColl.addDataField("fldvalue07", "BANK_ID@" + TradeConstance.BANK_ID);//银行代码
			authorizeKColl.addDataField("fldvalue08", "CLIENT_NO@" + cus_id);//客户码
			authorizeKColl.addDataField("fldvalue09", "CLIENT_NAME@" + cus_name);//客户名称
			authorizeKColl.addDataField("fldvalue10", "GLOBAL_TYPE@" + cert_type);//证件类型（非必输）
			authorizeKColl.addDataField("fldvalue11", "GLOBAL_ID@" + cert_code);//证件号码（非必输）
			authorizeKColl.addDataField("fldvalue12", "ISS_CTRY@" + "CN");//发证国家
			authorizeKColl.addDataField("fldvalue13", "DEALER_CDE@" + "");//经销商代码（非必输,不填）
			authorizeKColl.addDataField("fldvalue14", "CCY@" + cont_cur_type);//币种
			authorizeKColl.addDataField("fldvalue15", "APPLY_AMOUNT@" + contAmt);//申请金额
			authorizeKColl.addDataField("fldvalue16", "DRAW_DOWN_DATE@" + TagUtil.formatDate(cont_start_date));//合同发放日期
			authorizeKColl.addDataField("fldvalue17", "LOAN_TYPE@" + lmPrdId );//贷款品种
			authorizeKColl.addDataField("fldvalue18", "CONTRACT_EXPIRY_DATE@" + TagUtil.formatDate(cont_end_date));//合同到期日
			authorizeKColl.addDataField("fldvalue19","CARD_NO@" + card_no);	//卡号
			authorizeKColl.addDataField("fldvalue20","TERM@" + cont_term);	//期限
			authorizeKColl.addDataField("fldvalue21","TERM_TYPE@" + term_type);	//期限类型
			authorizeKColl.addDataField("fldvalue22", "DEDUCT_DATE@" + repay_date);//扣款日
			
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
			authorizeKColl.addDataField("fldvalue23", "INT_RATE_MODE@" + ir_accord_type);//利率模式
			authorizeKColl.addDataField("fldvalue24", "INT_RATE_BASE@" + "Y");//利率基础
			authorizeKColl.addDataField("fldvalue25", "BASE_INT_RATE_CODE@" + ruling_ir_code);//利率类型=基准利率代码 
			authorizeKColl.addDataField("fldvalue26", "BASE_INT_RATE@" + ruling_ir);//基准利率
			authorizeKColl.addDataField("fldvalue27", "INT_FLT_RATE@" + ir_float_rate);//利率浮动比例
			authorizeKColl.addDataField("fldvalue28", "SPREAD@" + ir_float_point);//利差
			authorizeKColl.addDataField("fldvalue29", "ACT_INT_RATE@" + reality_ir_y);//执行利率
			authorizeKColl.addDataField("fldvalue30", "OD_RATE_BASE@"+"Y");//罚息利率基础
			authorizeKColl.addDataField("fldvalue31", "LOAN_OD_RATE_CODE@"+"");//罚息利率代码
			authorizeKColl.addDataField("fldvalue32", "LOAN_OD_BASE_RATE@"+"");//基准罚息利率
			authorizeKColl.addDataField("fldvalue33", "LOAN_OD_BASE_FLT_RATE@" + "");//罚息基准利率浮动比
			authorizeKColl.addDataField("fldvalue34", "LOAN_OD_ACT_RATE@" + overdue_rate_y);//罚息执行利率
			authorizeKColl.addDataField("fldvalue35", "REPAY_FREQUENCY_UNIT@" + repay_term);//还款间隔单位
			authorizeKColl.addDataField("fldvalue36", "REPAY_FREQUENCY@" + repay_space);//还款间隔
			authorizeKColl.addDataField("fldvalue37", "LOAN_REPAY_METHOD@" + repay_type);//还款方式
			authorizeKColl.addDataField("fldvalue38", "LOAN_REPAY_TYPE@" + repay_mode_type);//还款方式类型
			String buss_source = "";
			String CONSIGN_AGREE = "";
			buss_source = "NLOAN";
			authorizeKColl.addDataField("fldvalue39", "BUSS_SOURCE@" + buss_source);//业务数据来源
			authorizeKColl.addDataField("fldvalue40", "LOAN_GRACE_TYPE@" + "P");//宽限期类型
			authorizeKColl.addDataField("fldvalue41", "LOAN_GRACE_DAYS@" + "0");//宽限期天数
			authorizeKColl.addDataField("fldvalue42", "DEDUCT_METHOD@" + "AUTOPAY");//扣款方式
			authorizeKColl.addDataField("fldvalue43", "FIX_OD_RATE_FLAG@" + "N");//是否采用固定罚息利率
			authorizeKColl.addDataField("fldvalue44", "LOAN_OD_RATE_TYPE@" + "L");//罚息利率种类
			authorizeKColl.addDataField("fldvalue45", "LOAN_OD_ACT_FLT_RATE@" + default_rate);//罚息执行利率浮动比
			authorizeKColl.addDataField("fldvalue46", "LOAN_OD_COMM_PART@" + "");//计算罚息部分
			authorizeKColl.addDataField("fldvalue47", "LOAN_OD_CPD_FLAG@" + "");//是否计算罚息复利
			authorizeKColl.addDataField("fldvalue48", "NEXT_RADJ_OPT@" + ir_adjust_type);//下一次利率调整选项
			authorizeKColl.addDataField("fldvalue49", "NEXT_RADJ_FREQ@" + ir_next_adjust_term);//下一次利率调整间隔
			authorizeKColl.addDataField("fldvalue50", "NEXT_RADJ_FREQ_UNIT@" + ir_next_adjust_unit);//下一次利率调整间隔单位
			authorizeKColl.addDataField("fldvalue51", "FIRST_ADJUST_DATE@" + fir_adjust_day);//第一次调整日
			authorizeKColl.addDataField("fldvalue52", "DIVERT_FLT_RATE@" + "");//挪用利率浮动比例
			authorizeKColl.addDataField("fldvalue53", "OVERDUE_ACT_RATE@" + overdue_rate_y);//逾期执行利率
			authorizeKColl.addDataField("fldvalue54", "OVERDUE_FLT_RATE@" + overdue_rate);//逾期利率浮动比
			authorizeKColl.addDataField("fldvalue55", "ASSET_BUY_FLAG@" + "N");//是否资产买入
			authorizeKColl.addDataField("fldvalue56", "CONSIGN_AGREE@" + CONSIGN_AGREE);//委托协议
			authorizeKColl.addDataField("fldvalue57", "TERM_PAY_FLAG@" + is_term);//期供标志
			authorizeKColl.addDataField("fldvalue58", "LOAN_PROMISE_DUEBILL_NO@" + "");//贷款承诺借据号
								
			//生成授权信息：保证金信息
			//查询保证金信息表获取保证金相关信息
			String condition = "where serno='"+loanSerno+"'";
			IndexedCollection iqpBailInfoIColl = dao.queryList(PUBBAILINFO, condition, connection);
			for(int i=0;i<iqpBailInfoIColl.size();i++){
				KeyedCollection iqpBailInfoKColl = (KeyedCollection)iqpBailInfoIColl.get(i);
				String bail_acct_no = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_no"));//保证金账号
				String bail_acct_name = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_name"));//保证金账号名称
				String open_org = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("open_org"));//开户机构
				String bail_acct_gl_code = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("bail_acct_gl_code"));//科目号
				String ccy = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("cur_type"));//币种
				String interbank_id = TagUtil.replaceNull4String(iqpBailInfoKColl.getDataValue("interbank_id"));//银联行号
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);

				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "03");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "CONTRACT_NO@" + cont_no);//合同号
				authorizeSubKColl.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + "GUTR");//贷款账户类型 :GUTR 保证金账号
				authorizeSubKColl.addDataField("fldvalue04", "ACCT_NO@" + bail_acct_no);//账号
				authorizeSubKColl.addDataField("fldvalue05", "CCY@" + ccy);//币种
				authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + interbank_id);//帐号银行代码
				authorizeSubKColl.addDataField("fldvalue07", "BRANCH_ID@" + open_org);//机构代码
				authorizeSubKColl.addDataField("fldvalue08", "ACCT_NAME@" + bail_acct_name);//户名
				authorizeSubKColl.addDataField("fldvalue09", "ISS_CTRY@" + "CN");//发证国家
				authorizeSubKColl.addDataField("fldvalue10", "ACCT_TYPE@" + "2");//账户类型:1-结算账户 2-保证金				
				authorizeSubKColl.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue13", "REMARK@" + "");//备注（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue14", "CARD_NO@" + "");//卡号（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue15", "CA_TT_FLAG@" + "");//钞汇标志（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue16", "ACCT_CODE@" + "");//账户代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue17", "ACCT_GL_CODE@" + bail_acct_gl_code);//账号科目代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue18", "BALANCE@" + "");//账户余额（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@" + "");//受托支付金额
				authorizeSubKColl.addDataField("fldvalue20", "BANK_NAME@" + "");//银行名称
				authorizeSubKColl.addDataField("fldvalue21", "OWN_BRANCH_FLAG@" + "1");//是否本行
				
				//开户行地址
				if(interbank_id!=null&&!"".equals(interbank_id)){
					KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryCoreZfxtZbByBnkcode", interbank_id, null, connection);
					if(kColl!=null){
						authorizeSubKColl.addDataField("fldvalue22", "ACCT_BANK_ADD@" + TagUtil.replaceNull4String(kColl.getDataValue("addr")));//地址
					}else{
						authorizeSubKColl.addDataField("fldvalue22", "ACCT_BANK_ADD@" + "");//地址
					}
				}else{
					authorizeSubKColl.addDataField("fldvalue22", "ACCT_BANK_ADD@" + "");//地址
				}
				authorizeSubKColl.addDataField("fldvalue23", "GUARANTEE_PER@" + security_rate);//保证金比例
			
				dao.insert(authorizeSubKColl, connection);
			}
			
			//生成授权信息：费用信息
			Map<String, String> feemap = new HashMap<String, String>();//定义一个账号对应map
			int feecount = 1;

			for(int i=0;i<iqpAppendTermsIColl.size();i++){				
				KeyedCollection iqpAppendTermsKColl = (KeyedCollection)iqpAppendTermsIColl.get(i);
				String fee_code = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_code"));//费用代码
				String fee_code_hs = "";
				if(fee_code.equals("09")){//01-借款人委托贷款费用、09-委托人委托贷款费用  都对应 核算系统的  01-委托费用
					fee_code_hs = "01";
				}else if(fee_code.equals("13")){//保理费用
					fee_code_hs = "21";
				}else{
					fee_code_hs = fee_code;
				}
				String fee_cur_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_cur_type"));//币种
				Double fee_amt = TagUtil.replaceNull4Double(iqpAppendTermsKColl.getDataValue("fee_amt"));//费用金额
				String fee_type = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_type"));//费用类型
				String fee_rate = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_rate"));//费用比率
				String is_cycle_chrg = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("is_cycle_chrg"));//是否周期性收费标识 
				String chrg_date = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_date"));//收费日期
				String acct_no = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("fee_acct_no"));//账号
				String chrg_freq = TagUtil.replaceNull4String(iqpAppendTermsKColl.getDataValue("chrg_freq"));//账号
				if(chrg_freq.equals("Y")){
					chrg_freq = "12";
				}else if(chrg_freq.equals("Q")){
					chrg_freq = "3";
				}else if(chrg_freq.equals("M")){
					chrg_freq = "1";
				}else{
					chrg_freq = "";
				}
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "02");//业务类别--费用信息(02 费用  03账户 04保证金)
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "CONTRACT_NO@" + cont_no);//合同号
				authorizeSubKColl.addDataField("fldvalue03", "FEE_CODE@" + fee_code_hs);//费用代码				
				authorizeSubKColl.addDataField("fldvalue04", "CCY@" + fee_cur_type);//币种
				authorizeSubKColl.addDataField("fldvalue05", "FEE_AMOUNT@" + fee_amt);//费用金额
			    authorizeSubKColl.addDataField("fldvalue06", "INOUT_FLAG@" + TradeConstance.is_pay_flag);//收付标志 默认传 R: 收
				authorizeSubKColl.addDataField("fldvalue07", "FEE_TYPE@" + fee_type);//费用类型
				authorizeSubKColl.addDataField("fldvalue08", "BASE_AMOUNT@" + fee_amt);//基准金额 同费用金额
				authorizeSubKColl.addDataField("fldvalue09", "FEE_RATE@" + fee_rate);//费用比率
				if(fee_code.equals("01")){
					if(feemap.containsKey(acct_no)){
						authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + feemap.get(acct_no));//扣款账户类型  默认传 FEE:收费账户
					}else{
						authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + "FEE");//扣款账户类型  默认传 FEE:收费账户
						feemap.put(acct_no, "FEE");
					}
				}else{
					if(feemap.containsKey(acct_no)){
						authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + feemap.get(acct_no));//扣款账户类型  默认传 FEE:收费账户
					}else{
						authorizeSubKColl.addDataField("fldvalue10", "DEDUCT_ACCT_TYPE@" + "FEE"+feecount);//扣款账户类型  默认传 FEE:收费账户
						feemap.put(acct_no, "FEE"+feecount);
						feecount++;
					}
				}
				authorizeSubKColl.addDataField("fldvalue11", "CHARGE_PERIODICITY_FLAG@" + this.TransSDicForESB("STD_ZX_YES_NO",is_cycle_chrg));//是否周期性收费标识
				authorizeSubKColl.addDataField("fldvalue12", "CHARGE_DATE@" + chrg_date);//收费日期
				authorizeSubKColl.addDataField("fldvalue13", "AMOR_AMT@" + fee_amt);//摊销金额
				authorizeSubKColl.addDataField("fldvalue21", "BRANCH_ID@" + in_acct_br_id);//入账机构
				authorizeSubKColl.addDataField("fldvalue22", "FEE_SPAN@" + chrg_freq);//收费间隔
				dao.insert(authorizeSubKColl, connection);
			}			
			
			//生成授权信息：结算账户信息
			String conditionCusAcct = "where serno='"+loanSerno+"'";
			IndexedCollection iqpCusAcctIColl = dao.queryList(IQPCUSACCT, conditionCusAcct, connection);
			int eactcount = 0;
			for(int i=0;i<iqpCusAcctIColl.size();i++){				
				KeyedCollection iqpCusAcctKColl = (KeyedCollection)iqpCusAcctIColl.get(i);
				String acct_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_no"));//账号
				String opac_org_no = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opac_org_no"));//开户行行号
				String opan_org_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("opan_org_name"));//开户行行名
				//String is_this_org_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行账户
				String acct_name = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_name"));//户名
				Double pay_amt = TagUtil.replaceNull4Double(iqpCusAcctKColl.getDataValue("pay_amt"));//受托支付金额
				String acct_gl_code = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_gl_code"));//科目号
				String ccy = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("cur_type"));//币种
				String is_acct = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("is_this_org_acct"));//是否本行
				/*01放款账号  02印花税账号 03收息收款账号    04受托支付账号     05	主办行资金归集账户*/
				String acct_attr = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("acct_attr"));//账户属性
				String interbank_id = TagUtil.replaceNull4String(iqpCusAcctKColl.getDataValue("interbank_id"));//银联行号
				
				KeyedCollection authorizeSubKColl = new KeyedCollection(AUTHORIZESUBMODEL);
				authorizeSubKColl.addDataField("auth_no", authSerno);//授权编号
				authorizeSubKColl.addDataField("busi_cls", "03");//业务类别
				authorizeSubKColl.addDataField("fldvalue01", "GEN_GL_NO@" + authSerno);//授权编号
				authorizeSubKColl.addDataField("fldvalue02", "CONTRACT_NO@" + cont_no);//合同号
				
				/*PAYM:还款账户 ACTV:放款账号 TURE:委托账号 FEE:收费账户 EACT:最后付款帐号 GUTR:保证金账号*/
				if(!"".equals(acct_attr) && acct_attr.equals("01")){
					acct_attr = "ACTV";
				}else if(!"".equals(acct_attr) && acct_attr.equals("02")){
					acct_attr = "STAMP1";
				}else if(!"".equals(acct_attr) && acct_attr.equals("03")){
					acct_attr = "PAYM";
				}else if(!"".equals(acct_attr) && acct_attr.equals("04")){
					if(eactcount==0){
						acct_attr = "EACT";
					}else{
						acct_attr = "EACT"+eactcount;
					}
					eactcount++;
				}else if(!"".equals(acct_attr) && acct_attr.equals("05")){
					acct_attr = "TURE";
				}else if(!"".equals(acct_attr) && acct_attr.equals("06")){
					acct_attr = "STAMP2";
				}else if(!"".equals(acct_attr) && acct_attr.equals("07")){
					if(feemap.containsKey(acct_no)){
						acct_attr = feemap.get(acct_no);
					}else{
						continue;//找不到对应费用信息，该费用账号不传
					}
				}else{
					acct_attr = "";
				}
				authorizeSubKColl.addDataField("fldvalue03", "LOAN_ACCT_TYPE@" + acct_attr);//贷款账户类型 				
				authorizeSubKColl.addDataField("fldvalue04", "ACCT_NO@" + acct_no);//账号
				authorizeSubKColl.addDataField("fldvalue05", "CCY@" + ccy);//币种
				authorizeSubKColl.addDataField("fldvalue06", "BANK_ID@" + interbank_id);//帐号银行代码
				authorizeSubKColl.addDataField("fldvalue07", "BRANCH_ID@" + opac_org_no);//机构代码
				authorizeSubKColl.addDataField("fldvalue08", "ACCT_NAME@" + acct_name);//户名
				authorizeSubKColl.addDataField("fldvalue09", "ISS_CTRY@" + "CN");//发证国家
				authorizeSubKColl.addDataField("fldvalue10", "ACCT_TYPE@" + "1");//账户类型:1-结算账户 2-保证金				
				authorizeSubKColl.addDataField("fldvalue11", "GLOBAL_TYPE@" + "");//证件类型（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue12", "GLOBAL_ID@" + "");//证件号码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue13", "REMARK@" + "");//备注（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue14", "CARD_NO@" + "");//卡号（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue15", "CA_TT_FLAG@" + "");//钞汇标志（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue16", "ACCT_CODE@" + "");//账户代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue17", "ACCT_GL_CODE@" + acct_gl_code);//账号科目代码（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue18", "BALANCE@" + "");//账户余额（目前无此字段）
				authorizeSubKColl.addDataField("fldvalue19", "COMMISSION_PAYMENT_AMOUNT@" + pay_amt);//受托支付金额
				authorizeSubKColl.addDataField("fldvalue20", "BANK_NAME@" + opan_org_name);//银行名称
				authorizeSubKColl.addDataField("fldvalue21", "OWN_BRANCH_FLAG@" + is_acct);//是否本行
				
				//开户行地址
				if(interbank_id!=null&&!"".equals(interbank_id)){
					KeyedCollection kColl = (KeyedCollection)SqlClient.queryFirst("queryCoreZfxtZbByBnkcode", interbank_id, null, connection);
					if(kColl!=null){
						authorizeSubKColl.addDataField("fldvalue22", "ACCT_BANK_ADD@" + TagUtil.replaceNull4String(kColl.getDataValue("addr")));//地址
					}else{
						authorizeSubKColl.addDataField("fldvalue22", "ACCT_BANK_ADD@" + "");//地址
					}
				}else{
					authorizeSubKColl.addDataField("fldvalue22", "ACCT_BANK_ADD@" + "");//地址
				}
				dao.insert(authorizeSubKColl, connection);
			}
			dao.insert(authorizeKColl, connection);
			/**调用ESB接口，发送报文*/
			//CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			//ESBServiceInterface serviceRel = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			//serviceRel.trade0200100000101(contNo, context, connection);
		
		}catch (Exception e) {
			throw new Exception("自助贷款协议授权信息采集异常"+e.getMessage());
		}
	}
	
	/**
	 * 特殊字典翻译
	 * @param opttype 字典项，enname
	 * @throws Exception
	 * added by yangzy 2015/04/07 需求编号:XD150318023,微贷平台零售自助贷款改造
	 */	
	private String TransSDicForESB(String opttype,String enname)throws Exception{
		String esbenname = "";
		
		if(opttype.equals("STD_DRFT_TYPE")){//票据类型
			if(enname.equals("100")){
				esbenname = "DD";
			}else if(enname.equals("200")){
				esbenname = "CD";
			}
		}
		
		if(opttype.equals("STD_ZX_YES_NO")){//票据类型
			if(enname.equals("1")){
				esbenname = "Y";
			}else if(enname.equals("2")){
				esbenname = "N";
			}
		}
		
		return esbenname;
	}
	/**
	 * 自助贷款协议授权
	 * @param  合同流水号 contNo，context，connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 * added by yangzy 2015/04/08 需求编号:XD150318023,微贷平台零售自助贷款改造
	 */
	public KeyedCollection trade0200100000101(String contNo,Context context, Connection connection) throws Exception {
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData();
		try{
			//获取dao
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection PvpKColl = dao.queryFirst("PvpAuthorize",null,"where cont_no='"+contNo+"'",connection);
			String tran_serno = (String) PvpKColl.getDataValue("tran_serno");
			String tran_id = (String) PvpKColl.getDataValue("tran_id");
			String serviceCode = tran_id.substring(0, 11);
			String senceCode = tran_id.substring(11, 13);
			String openDay = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			context.put("OPENDAY",openDay);
			context.put("currentUserId","admin");
			context.put("currentUserName","超级管理员");
			context.put("organNo","9350000000");

			/** 通过交易码判断所需执行的交易，以及需要准备的交易数据 */
			KeyedCollection authKColl = dao.queryDetail("PvpAuthorize", tran_serno, connection);
			String serno = (String)authKColl.getDataValue("serno");
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			/** 系统头 */
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, senceCode, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			
			/** 封装报文体 */
			KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl);
			
			CompositeData bodyCD = new CompositeData();
			bodyCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("APPLY_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("APPLY_TYPE"), FieldType.FIELD_STRING, 2, 0));
			bodyCD.addField("APPLY_NO", TagUtil.getEMPField(reflectKColl.getDataValue("APPLY_NO"), FieldType.FIELD_STRING, 32, 0));
			bodyCD.addField("APPLY_DATE", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("APPLY_DATE")), FieldType.FIELD_STRING, 8, 0));
			bodyCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("CONTRACT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("BANK_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BANK_ID"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("BRANCH_ID", TagUtil.getEMPField(reflectKColl.getDataValue("BRANCH_ID"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CLIENT_NO"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("CLIENT_NAME", TagUtil.getEMPField(reflectKColl.getDataValue("CLIENT_NAME"), FieldType.FIELD_STRING, 150, 0));
			bodyCD.addField("GLOBAL_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("GLOBAL_TYPE"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("GLOBAL_ID", TagUtil.getEMPField(reflectKColl.getDataValue("GLOBAL_ID"), FieldType.FIELD_STRING, 40, 0));
			bodyCD.addField("ISS_CTRY", TagUtil.getEMPField(reflectKColl.getDataValue("ISS_CTRY"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("DEALER_CDE", TagUtil.getEMPField(reflectKColl.getDataValue("DEALER_CDE"), FieldType.FIELD_STRING, 20, 0));
			bodyCD.addField("CCY", TagUtil.getEMPField(reflectKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("APPLY_AMOUNT", TagUtil.getEMPField(reflectKColl.getDataValue("APPLY_AMOUNT"), FieldType.FIELD_DOUBLE, 16, 2));
			bodyCD.addField("DRAW_DOWN_DATE", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("DRAW_DOWN_DATE")), FieldType.FIELD_STRING, 8, 0));
			bodyCD.addField("LOAN_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("LOAN_TYPE"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("CONTRACT_EXPIRY_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("CONTRACT_EXPIRY_DATE"), FieldType.FIELD_STRING, 8, 0));
			bodyCD.addField("CARD_NO", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("CARD_NO")), FieldType.FIELD_STRING, 50, 0));
			bodyCD.addField("TERM", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("TERM")), FieldType.FIELD_STRING, 3, 0));
			bodyCD.addField("TERM_TYPE", TagUtil.getEMPField(TagUtil.formatDate(reflectKColl.getDataValue("TERM_TYPE")), FieldType.FIELD_STRING, 3, 0));
			bodyCD.addField("DEDUCT_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("DEDUCT_DATE"), FieldType.FIELD_STRING, 2, 0));
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
			bodyCD.addField("REPAY_FREQUENCY", TagUtil.getEMPField(reflectKColl.getDataValue("REPAY_FREQUENCY"), FieldType.FIELD_STRING, 2, 0));
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
			IndexedCollection zhIColl = dao.queryList("PvpAuthorizeSub", " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_ZH+"' ", connection);
			if(zhIColl != null && zhIColl.size() > 0){
				Array zharray = new Array(); 
				for(int i=0;i<zhIColl.size();i++){
					CompositeData zhCD = new CompositeData();
					KeyedCollection zhKColl = (KeyedCollection)zhIColl.get(i);
					KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(zhKColl);
					zhCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
					zhCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 50, 0));
					zhCD.addField("AGREE_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 50, 0));
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
					zhCD.addField("BANK_NAME", TagUtil.getEMPField(reflectSubKColl.getDataValue("BANK_NAME"), FieldType.FIELD_STRING, 100, 0));
					zhCD.addField("OWN_BRANCH_FLAG", TagUtil.getEMPField(reflectSubKColl.getDataValue("OWN_BRANCH_FLAG"), FieldType.FIELD_STRING, 1, 0));
					zhCD.addField("ACCT_BANK_ADD", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_BANK_ADD"), FieldType.FIELD_STRING, 100, 0));
					if(reflectSubKColl.containsKey("GUARANTEE_PER")){
						zhCD.addField("GUARANTEE_PER", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_PER"), FieldType.FIELD_DOUBLE, 20, 7));
					}
					
					zharray.addStruct(zhCD);
				}
				bodyCD.addArray("ACCT_INFO_ARRAY", zharray);
			}
			
			/** 费用信息 */
			IndexedCollection feeIColl = dao.queryList("PvpAuthorizeSub", " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_FY+"' ", connection);
			if(feeIColl != null && feeIColl.size() > 0){
				Array feeArray = new Array(); 
				for(int i=0;i<feeIColl.size();i++){
					CompositeData feeCD = new CompositeData();
					KeyedCollection feeKColl = (KeyedCollection)feeIColl.get(i);
					KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(feeKColl);
					feeCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
					feeCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 50, 0));
					feeCD.addField("CONTRACT_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 50, 0));
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
					feeCD.addField("BRANCH_ID", TagUtil.getEMPField(reflectSubKColl.getDataValue("BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
					feeCD.addField("FEE_SPAN", TagUtil.getEMPField(reflectSubKColl.getDataValue("FEE_SPAN"), FieldType.FIELD_STRING, 10, 0));
					feeArray.addStruct(feeCD);
				}
				bodyCD.addArray("FEE_AUTH_INFO_ARRAY", feeArray);
			}			
			
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			
			if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
				throw new Exception("自助贷款协议授权发送失败!");
			}
			return retKColl;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("自助贷款协议授权发送失败"+e.getMessage());
		}
	}
	
	/**
	 * 法人账户透支改造
	 * @param  业务信息kColl，context，connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 * added by wangj 2015/05/28 需求编号:XD141222087,法人账户透支改造
	 */
	public KeyedCollection trade0200200000503(KeyedCollection kColl,Context context, Connection connection) throws Exception {
		/** 封装发送报文信息 */
		String serviceCode="02002000005";
		String senceCode="03";
		String opType=(String) kColl.getDataValue("OPERATION_TYPE");//10-冻结  20-解冻 30-终止
		String opStr="冻结";
		if("20".equals(opType)){
			opStr="解冻";
		}else if("30".endsWith(opType)){
			opStr="终止";
		}
		CompositeData reqCD= new CompositeData();
		try{
			String openDay = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			context.put("OPENDAY",openDay);
			/*context.put("currentUserId","admin");
			context.put("currentUserName","超级管理员");
			context.put("organNo","9350000000");*/

			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			/** 系统头 */
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, senceCode, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			
			/** 封装报文体 */
		
			CompositeData bodyCD = new CompositeData();
			bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(kColl.getDataValue("CLIENT_NO"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("OPERATION_TYPE", TagUtil.getEMPField(kColl.getDataValue("OPERATION_TYPE"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("CONTRACT_NO", TagUtil.getEMPField(kColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 40, 0));
			bodyCD.addField("APPLY_TYPE", TagUtil.getEMPField(TagUtil.formatDate(kColl.getDataValue("APPLY_TYPE")), FieldType.FIELD_STRING, 2, 0));
			reqCD.addStruct("BODY", bodyCD);
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
				throw new Exception("法人账户透支额度"+opStr+"发送失败:"+retKColl.getDataValue("RET_MSG"));
			}
			/** 解析反馈报文体 */
			KeyedCollection back_kColl = this.getRespBodyCD4KColl(retCD);
			back_kColl.setName(TradeConstance.ESB_BODY);
			retKColl.addDataElement(back_kColl);
			return retKColl;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("法人账户透支额度"+opStr+"发送失败:"+e.getMessage());
		}
	}
	
	/**
	 * 小微自助循环贷款改造
	 * @param  业务信息kColl，context，connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 * added by lisj 2015-6-1 需求编号：【XD150123005】小微自助循环贷款改造
	 */
	public KeyedCollection trade0200200000502(KeyedCollection kColl,Context context, Connection connection) throws Exception {
		/** 封装发送报文信息 */
		String serviceCode="02002000005";
		String senceCode="03";
		String opType=(String) kColl.getDataValue("OPERATION_TYPE");//10-冻结  20-解冻 30-终止
		String opStr="冻结";
		if("20".equals(opType)){
			opStr="解冻";
		}else if("30".endsWith(opType)){
			opStr="终止";
		}
		CompositeData reqCD= new CompositeData();
		try{
			String openDay = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			context.put("OPENDAY",openDay);
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			/** 系统头 */
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, senceCode, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			
			/** 封装报文体 */
		
			CompositeData bodyCD = new CompositeData();
			bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(kColl.getDataValue("CLIENT_NO"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("OPERATION_TYPE", TagUtil.getEMPField(kColl.getDataValue("OPERATION_TYPE"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("CONTRACT_NO", TagUtil.getEMPField(kColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 40, 0));
			bodyCD.addField("APPLY_TYPE", TagUtil.getEMPField(TagUtil.formatDate(kColl.getDataValue("APPLY_TYPE")), FieldType.FIELD_STRING, 2, 0));
			reqCD.addStruct("BODY", bodyCD);
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
				throw new Exception("小微自助循环贷款额度"+opStr+"发送失败："+retKColl.getDataValue("RET_MSG"));
			}
			/** 解析反馈报文体 */
			KeyedCollection back_kColl = this.getRespBodyCD4KColl(retCD);
			back_kColl.setName(TradeConstance.ESB_BODY);
			retKColl.addDataElement(back_kColl);
			return retKColl;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("小微自助循环贷款额度"+opStr+"发送失败!"+e.getMessage());
		}
	}
	
	/**
	 * 无间贷风险拦截
	 * @param  业务信息kColl，context，connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 * added by wangj 2015/07/01  需求编号:XD150611043,关于在信贷管理系统中新增相关业务检查并进行信息提示需求
	 */
	
	public KeyedCollection trade0200200000205(KeyedCollection kColl,Context context, Connection connection) throws Exception {
		/** 封装发送报文信息 */
		String serviceCode="02002000002";
		String senceCode="05";
		CompositeData reqCD= new CompositeData();
		try{
			String openDay = (String)SqlClient.queryFirst("querySysInfo", null, null, connection);
			context.put("OPENDAY",openDay);
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			/** 系统头 */
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, senceCode, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			
			/** 封装报文体 */
		
			CompositeData bodyCD = new CompositeData();
			bodyCD.addField("DUEBILL_NO", TagUtil.getEMPField(kColl.getDataValue("repay_bill"), FieldType.FIELD_STRING, 50, 0));
			bodyCD.addField("AGREE_REPAY_DATE", TagUtil.getEMPField(openDay, FieldType.FIELD_STRING, 8, 0));
			reqCD.addStruct("BODY", bodyCD);
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
				throw new Exception("已放贷款试算器交易发送失败!");
			}
			/** 解析反馈报文体 */
			KeyedCollection back_kColl = this.getRespBodyCD4KColl(retCD);
			back_kColl.setName(TradeConstance.ESB_BODY);
			retKColl.addDataElement(back_kColl);
			return retKColl;//返回报文信息
		}catch(Exception e){
			throw new Exception("已放贷款试算器交易发送失败"+e.getMessage());
		}
	}

	/**
	 * 印花税试算   需求编号：XD150611043,关于在信贷管理系统中新增相关业务检查并进行信息提示需求
	 *        变更需求编号：XD150925071,关于在信贷管理系统中新增相关业务检查并进行信息提示需求
	 * @param kColl  业务信息
	 * @param context
	 * @param connection
	 * @return 返回报文头信息
	 * @throws Exception 
	 */
	public KeyedCollection trade0200200000207(KeyedCollection kColl,Context context, Connection connection) throws Exception {
		/** 封装发送报文信息 */
		String serviceCode="02002000002";
		String senceCode="07";
		CompositeData reqCD= new CompositeData();
		try{
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			/** 系统头 */
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, senceCode, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			
			/** 封装报文体 */
		
			CompositeData bodyCD = new CompositeData();
			bodyCD.addField("ACCEPT_BRANCH_ID", TagUtil.getEMPField(TagUtil.replaceNull4String(kColl.getDataValue("accept_branch_id")), FieldType.FIELD_STRING, 20, 0));
			bodyCD.addField("CCY", TagUtil.getEMPField(TagUtil.replaceNull4String(kColl.getDataValue("ccy")), FieldType.FIELD_STRING, 3, 0));
			bodyCD.addField("TRANT_AMT", TagUtil.getEMPField(TagUtil.replaceNull4Double(kColl.getDataValue("trant_amt")), FieldType.FIELD_DOUBLE, 20, 4));
			bodyCD.addField("DR_ACCT_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(kColl.getDataValue("dr_acct_no")), FieldType.FIELD_STRING, 50, 0));
			bodyCD.addField("CONSIGN_ACCT_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(kColl.getDataValue("consign_acct_no")), FieldType.FIELD_STRING, 50, 0));
			bodyCD.addField("CHARGE_MODE", TagUtil.getEMPField(TagUtil.replaceNull4String(kColl.getDataValue("charge_mode")), FieldType.FIELD_STRING, 1, 0));
			reqCD.addStruct("BODY", bodyCD);
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
				throw new Exception("印花税试算交易发送失败:"+retKColl.getDataValue("RET_MSG"));
			}
			/** 解析反馈报文体 */
			KeyedCollection back_kColl = this.getRespBodyCD4KColl(retCD);
			back_kColl.setName(TradeConstance.ESB_BODY);
			retKColl.addDataElement(back_kColl);
			return retKColl;//返回报文信息
		}catch(Exception e){
			throw new Exception("印花税试算交易发送失败:"+e.getMessage());
		}
	}

	/**
	 * 通用银行费用查询    需求编号：XD150611043,关于在信贷管理系统中新增相关业务检查并进行信息提示需求
	 *  			变更需求编号：XD150925071,关于在信贷管理系统中新增相关业务检查并进行信息提示需求
	 * @param kColl  业务信息
	 * @param context
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public KeyedCollection trade1100300001602(KeyedCollection kColl,Context context, Connection connection) throws Exception {
		/** 封装发送报文信息 */
		String serviceCode="11003000016";
		String senceCode="02";
		CompositeData reqCD= new CompositeData();
		try{
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			/** 系统头 */
			
			CompositeData sysHead= esbInterfacesImple.getSysHeadCD(serviceCode, senceCode, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context);
			sysHead.addField("FDRMX_ZDLX", TagUtil.getEMPField("K", FieldType.FIELD_STRING, 1, 0));
			reqCD.addStruct("SYS_HEAD",sysHead);
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			
			/** 封装报文体 */
		
			CompositeData bodyCD = new CompositeData();
			bodyCD.addField("ACCEPT_BRANCH_ID", TagUtil.getEMPField(TagUtil.replaceNull4String(kColl.getDataValue("accept_branch_id")), FieldType.FIELD_STRING, 20, 0));
			bodyCD.addField("ACCT_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(kColl.getDataValue("acct_no")), FieldType.FIELD_STRING, 50, 0));
			bodyCD.addField("TRANT_AMT", TagUtil.getEMPField(TagUtil.replaceNull4Double(kColl.getDataValue("trant_amt")), FieldType.FIELD_DOUBLE, 20, 4));
			bodyCD.addField("CASH_TRAN_FLAG", TagUtil.getEMPField(TagUtil.replaceNull4String(kColl.getDataValue("cash_tran_flag")), FieldType.FIELD_STRING, 1, 0));
			bodyCD.addField("CCY", TagUtil.getEMPField(TagUtil.replaceNull4String(kColl.getDataValue("ccy")), FieldType.FIELD_STRING, 3, 0));
			bodyCD.addField("PAY_TYPE", TagUtil.getEMPField(TagUtil.replaceNull4String(kColl.getDataValue("pay_type")), FieldType.FIELD_STRING, 1, 0));
			reqCD.addStruct("BODY", bodyCD);
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文头 */
			KeyedCollection retKColl = this.getRespSysHeadCD(retCD);
			if(!TagUtil.haveSuccess(retKColl, context)){//执行失败
				throw new Exception("银行费用查询交易发送失败:"+retKColl.getDataValue("RET_MSG"));
			}
			/** 解析反馈报文体 */
			KeyedCollection back_kColl = this.getRespBodyCD4KColl(retCD);
			back_kColl.setName(TradeConstance.ESB_BODY);
			String totalRowsR = this.getRespApp4String(retCD);
			back_kColl.put("TOTAL_ROWS", totalRowsR);//把费用条数返回
			
			retKColl.addDataElement(back_kColl);
			return retKColl;//返回报文信息
		}catch(Exception e){
			throw new Exception("银行费用查询交易发送失败:"+e.getMessage());
		}
	}
	
	/**
	 * 抵质押物权证出/入库
	 * @param  出/入库申请业务流水号 serno，context，connection
	 * @return 返回报文头信息及撤销标志
	 * @throws Exception 
	 */
	public KeyedCollection tradeDZYWQZCRKYM(String serno,Context context, Connection connection) throws Exception {
		/** 封装发送报文信息 */
		try{
			KeyedCollection retKColl = null;
			String authorize_no = null;
			//获取dao
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			KeyedCollection PvpKColl = dao.queryFirst("PvpAuthorize",null,"where serno='"+serno+"'",connection);
			authorize_no = (String) PvpKColl.getDataValue("authorize_no");
			IndexedCollection pvpIColl = dao.queryList("PvpAuthorize", " where serno = '"+serno+"' and status is null", connection);
			if(pvpIColl != null && pvpIColl.size() > 0){
				for(int i=0;i<pvpIColl.size();i++){
					KeyedCollection kColl = (KeyedCollection)pvpIColl.get(i);
					KeyedCollection reflectKColl = TagUtil.getReflectKColl(kColl);
					
					String guarantyNo = (String) reflectKColl.getDataValue("PLEDGE_NO");//押品编号
					KeyedCollection guarkColl = dao.queryDetail("MortGuarantyBaseInfo", guarantyNo, connection);
					String guarantyName = (String) guarkColl.getDataValue("guaranty_name");//抵质押物名称
					String cusId = (String) reflectKColl.getDataValue("CLIENT_NO");//抵质押物名称
					String tranSerno = (String)kColl.getDataValue("tran_serno");//交易流水号
					String authorizeNo = (String)kColl.getDataValue("authorize_no");//授权编号
					String openDay = (String)context.getDataValue(CMISConstance.OPENDAY);//当前营业日期，作为出入库时间更新
					/** 封装发送报文信息 */
					KeyedCollection headKcoll = new KeyedCollection();
					KeyedCollection bodyKcoll = new KeyedCollection();
					
					headKcoll.put("SvcCd", "40120003");
					headKcoll.put("ScnCd", "01");
					headKcoll.addDataField("TxnMd","ONLINE");
					headKcoll.addDataField("UsrLngKnd","CHINESE");
					headKcoll.addDataField("jkType","cbs"); 
					bodyKcoll.addDataField("CltlPldgNo", reflectKColl.getDataValue("HX_SERNO")==null?"":reflectKColl.getDataValue("HX_SERNO"));//抵质押物编号
					bodyKcoll.addDataField("BrwCstNo", cusId);//核心客户号
					bodyKcoll.addDataField("CltlPldgPrsnNm", reflectKColl.getDataValue("CLIENT_NAME"));//客户名称
					bodyKcoll.addDataField("CltlPldgTp", reflectKColl.getDataValue("PLEDGE_TYPE"));//抵质押物类型
					bodyKcoll.addDataField("CltlPldgNm", guarantyName==null?"":guarantyName);//抵质押物名称
					bodyKcoll.addDataField("CltlPldgAmt", "1");//抵质押物金额
					bodyKcoll.addDataField("Ccy", reflectKColl.getDataValue("CCY"));//币种
					bodyKcoll.addDataField("TxnInd", reflectKColl.getDataValue("IN_OUT_TYPE"));//出入库类型
					bodyKcoll.addDataField("VluablScrtTp", reflectKColl.getDataValue("TICKET_TYPE"));//权证类型
					bodyKcoll.addDataField("VluablScrtNo", reflectKColl.getDataValue("TICKET_NO"));//权证编号
					bodyKcoll.addDataField("TxnInstCd", reflectKColl.getDataValue("BRANCH_ID")==null?"":reflectKColl.getDataValue("BRANCH_ID"));//权证保管机构
					bodyKcoll.addDataField("AuthNo", authorizeNo);//授权编号
					logger.info("*******************************************请求报文体："+bodyKcoll);
					retKColl = ESBUtil.sendEsbMsg(headKcoll,bodyKcoll);
					logger.info("*******************************************核心返回报文："+retKColl);
				}
			}
			KeyedCollection sysHead=(KeyedCollection) retKColl.getDataElement("SYS_HEAD");
			IndexedCollection retArr=(IndexedCollection)sysHead.getDataElement("RetInfArry");
			KeyedCollection retObj=(KeyedCollection)retArr.get(0);
			String retCd=(String)retObj.getDataValue("RetCd");
			if("000000".equals(retCd)){//执行成功
				//根据授权编号更新授权表的状态为已授权
				SqlClient.update("updatePvpAuthorizeStatusByGenNo", authorize_no, "02", null, connection);
			}else{
				String RetInf=(String)retObj.getDataValue("RetInf");
				throw new Exception("错误信息："+RetInf);
			}
			return retKColl;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new Exception("抵质押物权证出/入库(商链通池出/入池)授权发送失败"+e.getMessage());
		}
	}
	
	/**
	 * 裕民银行还款计划查询接口
	 * @param  传入借据号，context，connection
	 * @return 返回交易明细iColl
	 * @throws Exception 
	 * @author jch
	 */
	public KeyedCollection tradeHKJHYM(String bill_no,Context context,Connection conn,PageInfo pageInfo) throws Exception {
		logger.info("------------------进入还款计划查询接口  start----------------");
		KeyedCollection returnKColl = new KeyedCollection();//返回结果
		KeyedCollection retKColl = new KeyedCollection();//返回报文
		try{
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
			logger.info("------------------查询贷款台账获取贷款起始日期，到期日期，贷款账号----------------");
			KeyedCollection acctNoKcoll = (KeyedCollection) SqlClient.queryFirst("queryBaseAcctNoByBillNo", bill_no, null, conn);
			String acctCardNo = "";
			String acctSeqNo = "";
			if(acctNoKcoll.containsKey("acct_no")){
				if(!"".equals((String)acctNoKcoll.getDataValue("acct_no"))&&null!=acctNoKcoll.getDataValue("acct_no")){
					acctCardNo = (String) acctNoKcoll.getDataValue("acct_no");//贷款账号
				}else{
					throw new Exception("通过借据号查询贷款账号为空！");
				}
			}else{
				throw new Exception("通过借据号查询贷款账号为空！");
			}
			if(acctNoKcoll.containsKey("acct_seq_no")){
				if(!"".equals((String)acctNoKcoll.getDataValue("acct_seq_no"))&&null!=acctNoKcoll.getDataValue("acct_seq_no")){
					acctSeqNo = (String) acctNoKcoll.getDataValue("acct_seq_no");//发放号
				}
			}else{
				throw new Exception("通过借据号查询发放号为空！");
			}
			
			logger.info("------------------组装还款计划查询接口请求报文  start----------------");
			KeyedCollection qryheadKcoll = new KeyedCollection("sys_head");
			KeyedCollection qrybodyColl = new KeyedCollection("body");
			//组装报文头
			qryheadKcoll.addDataField("SvcCd","30230001");
			qryheadKcoll.addDataField("ScnCd","03");
			qryheadKcoll.addDataField("TxnMd","ONLINE");
			qryheadKcoll.addDataField("UsrLngKnd","CHINESE");
			qryheadKcoll.addDataField("jkType","cbs");
			qryheadKcoll.addDataField("TurnPgFlg","1");
			qryheadKcoll.addDataField("PgDsplLineNum", pageInfo.pageSize);//每页展示条数
			qryheadKcoll.addDataField("CrnPgRcrdNo", String.valueOf(beginIdx));//当前页
			//报文体
			qrybodyColl.addDataField("AcctNoCrdNo",acctCardNo);//贷款帐号
			qrybodyColl.addDataField("AcctSeqNo",acctSeqNo);//发放号
			logger.info("------------------组装还款计划查询接口请求报文  end----------------");
			
			logger.info("----------------------发送请求报文："+qrybodyColl);
			retKColl = ESBUtil.sendEsbMsg(qryheadKcoll,qrybodyColl);
			logger.info("----------------------返回报文："+retKColl);

			if(retKColl.containsKey("SYS_HEAD")){
				KeyedCollection retSysHead=(KeyedCollection)retKColl.getDataElement("SYS_HEAD");//返回报文头
				//KeyedCollection retAppHead=(KeyedCollection)retKColl.getDataElement("APP_HEAD");//返回报文头
				KeyedCollection retBody=(KeyedCollection)retKColl.getDataElement("BODY");//返回报文体
				
				IndexedCollection retArr=(IndexedCollection)retSysHead.getDataElement("RetInfArry");
				KeyedCollection retObj=(KeyedCollection)retArr.get(0);
				String retCd=(String)retObj.getDataValue("RetCd");
				logger.info("------------------------返回结果标识："+retCd);
				if(!"000000".equals(retCd)){
					throw new EMPException((String)retObj.getDataValue("RetInf"));
				}else{
					logger.info("------------------解析反馈报文体  start----------------");
					IndexedCollection respBodyIColl  = (IndexedCollection)retBody.getDataElement("AcctDtlInfArry");//还款计划
					String totalRowsR = String.valueOf(respBodyIColl.size());//总笔数
					respBodyIColl.setName("BODY");
					returnKColl.addDataField("totalRowsR", totalRowsR);
					returnKColl.addDataElement(respBodyIColl);
					returnKColl.addDataField("RET_CODE", "000000");
					logger.info("------------------解析反馈报文体  end----------------");
				}
			}else{
				throw new Exception("核心还款计划查询接口请求失败");
			}
			logger.info("------------------进入还款计划查询接口  end----------------");
			return returnKColl;
		}catch(Exception e){
			throw new Exception("还款计划查询接口失败,错误信息："+e.getMessage());
		}
	}
	
	/**
	 * 交易流水查询接口(还款明细)
	 * @param  传入借据号，context，connection
	 * @return 返回交易明细iColl
	 * @throws Exception
	 * @author jch
	 */
	public KeyedCollection tradeJYLSYM(String bill_no,Context context,Connection conn,PageInfo pageInfo) throws Exception {
		logger.info("------------------进入还款历史查询接口  start----------------");
		KeyedCollection retKColl = new KeyedCollection();//返回报文
		KeyedCollection ret = null;
		try{
			//TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
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
			logger.info("------------------查询贷款台账获取贷款起始日期，到期日期，贷款账号----------------");
			KeyedCollection acctNoKcoll = (KeyedCollection) SqlClient.queryFirst("queryBaseAcctNoByBillNo", bill_no, null, conn);

			String begDt = (String)acctNoKcoll.getDataValue("distr_date");//发放日期
			if(begDt.indexOf("-")!=-1){
				begDt = begDt.substring(0, 4)+begDt.substring(5, 7)+begDt.substring(8, 10);//日期转换
			}
			String endDt = (String)acctNoKcoll.getDataValue("end_date");//到期日期
			if(endDt.indexOf("-")!=-1){
				endDt = endDt.substring(0, 4)+endDt.substring(5, 7)+endDt.substring(8, 10);//日期转换
			}
			String acctCardNo = "";
			if(acctNoKcoll.containsKey("acct_no")){
				if(!"".equals((String)acctNoKcoll.getDataValue("acct_no"))&&null!=acctNoKcoll.getDataValue("acct_no")){
					acctCardNo = (String) acctNoKcoll.getDataValue("acct_no");//贷款账号
				}else{
					throw new Exception("通过借据号查询贷款账号为空！");
				}
			}else{
				throw new Exception("通过借据号查询贷款账号为空！");
			}
			
			/*KeyedCollection accLoanKcoll = dao.queryDetail("AccLoan", bill_no, conn);//查询贷款台账
			String begDt = (String)accLoanKcoll.getDataValue("distr_date");//发放日期
			begDt = begDt.substring(0, 4)+begDt.substring(5, 7)+begDt.substring(8, 10);//日期转换
			String endDt = (String)accLoanKcoll.getDataValue("end_date");//到期日期
			endDt = endDt.substring(0, 4)+endDt.substring(5, 7)+endDt.substring(8, 10);//日期转换
			String cusId = (String)accLoanKcoll.getDataValue("cus_id");//到期日期
			
			logger.info("------------------通过客户号查询贷款账号----------------");
			KeyedCollection cusBaseKcoll = dao.queryDetail("CusBase", cusId, conn);
			String acctCardNo = (String) cusBaseKcoll.getDataValue("loan_card_id");*/

			logger.info("------------------组装还款历史查询接口请求报文  start----------------");
			KeyedCollection qryheadKcoll = new KeyedCollection();
			KeyedCollection qrybodyColl = new KeyedCollection("body");
			//组装报文头
			qryheadKcoll.addDataField("SvcCd","30230001");
			qryheadKcoll.addDataField("ScnCd","04");
			qryheadKcoll.addDataField("TxnMd","ONLINE");
			qryheadKcoll.addDataField("UsrLngKnd","CHINESE");
			qryheadKcoll.addDataField("jkType","cbs");
			qryheadKcoll.addDataField("TurnPgFlg","1");
			qryheadKcoll.addDataField("PgDsplLineNum", pageInfo.pageSize);//每页展示条数
			qryheadKcoll.addDataField("CrnPgRcrdNo", String.valueOf(beginIdx));//当前页
			//报文体
			qrybodyColl.addDataField("AcctNoCrdNo",acctCardNo);//贷款账号
			qrybodyColl.addDataField("AcctSeqNo","");//发放号
			qrybodyColl.addDataField("DblNo",bill_no);//借据号
			qrybodyColl.addDataField("BegDt",begDt);//起始日期
			qrybodyColl.addDataField("EndDt",endDt);//结束日期
			logger.info("------------------组装还款历史查询接口请求报文  end----------------");
			
			logger.info("----------------------发送请求报文："+qrybodyColl);
			retKColl = ESBUtil.sendEsbMsg(qryheadKcoll,qrybodyColl);
			logger.info("----------------------返回报文："+retKColl);

			if(retKColl.containsKey("SYS_HEAD")){
				KeyedCollection retSysHead=(KeyedCollection)retKColl.getDataElement("SYS_HEAD");//返回报文头
				KeyedCollection retAppHead=(KeyedCollection)retKColl.getDataElement("APP_HEAD");//返回报文头
				KeyedCollection retBody=(KeyedCollection)retKColl.getDataElement("BODY");//返回报文体
				
				IndexedCollection retArr=(IndexedCollection)retSysHead.getDataElement("RetInfArry");
				KeyedCollection retObj=(KeyedCollection)retArr.get(0);
				String retCd=(String)retObj.getDataValue("RetCd");
				logger.info("------------------------返回结果标识："+retCd);
				if(!"000000".equals(retCd)){
					throw new EMPException((String)retObj.getDataValue("RetInf"));
				}else{
					logger.info("------------------解析反馈报文体  start----------------");
					IndexedCollection respBodyIColl  = (IndexedCollection)retBody.getDataElement("RepymtDtlInfArry");//还款计划
					String totalRowsR = String.valueOf(respBodyIColl.size());//总笔数
					respBodyIColl.setName("BODY");
					ret = new KeyedCollection();
					ret.put("totalRowsR", totalRowsR);
					ret.addDataElement(respBodyIColl);
					ret.put("RET_CODE", "000000");
					
//					retKColl.addDataField("totalRowsR", totalRowsR);
//					retKColl.addDataElement(respBodyIColl);
//					retKColl.addDataField("RET_CODE", "000000");
					logger.info("------------------解析反馈报文体  end----------------");
				}
			}else{
				throw new Exception("核心还款历史信息查询接口请求失败");
			}
			logger.info("------------------进入还款计划查询接口  end----------------");
			return ret;
		}catch(Exception e){
			throw new Exception("交易明细接口查询失败，错误信息："+e.getMessage());
		}
	}
	
	/**
	 * 裕民银行授权撤销接口
	 * @param  授权信息，context，connection
	 * @return 返回报文头信息及撤销标志
	 * @throws Exception
	 * @author jch 
	 */
	public KeyedCollection tradeSQCXYM(KeyedCollection auKColl,Context context,Connection conn) throws Exception {
		logger.info("------------------进入授权撤销查询接口  start----------------");
		KeyedCollection retKColl = new KeyedCollection();//返回报文
		try{
			String authorizeNo = (String) auKColl.getDataValue("GEN_GL_NO");//授权编号
			
			logger.info("------------------通过授权编号查询贷款号和发放号 start ----------------");
			KeyedCollection pvpAuthorizeKcoll = (KeyedCollection) SqlClient.queryFirst("queryBaseAcctNoByAuthorizeNo", authorizeNo, null, conn);
			String acctCardNo = "";//贷款账号
			String acctSeqNo = "";//发放号
			if(pvpAuthorizeKcoll!=null){
				acctCardNo = (String) pvpAuthorizeKcoll.getDataValue("acct_no");
				acctSeqNo = (String) pvpAuthorizeKcoll.getDataValue("acct_seq_no");
				if(StringUtils.isEmpty(acctCardNo)){
					throw new Exception("查询贷款账号为空！");
				}
				if(StringUtils.isEmpty(acctSeqNo)){
					throw new Exception("查询发放号为空！");
				}
			}else{
				throw new Exception("查询贷款账号、发放号为空！");
			}
			logger.info("------------------通过授权编号查询贷款号和发放号 end ----------------");
			
			logger.info("------------------组装撤销接口请求报文  start----------------");
			KeyedCollection qryheadKcoll = new KeyedCollection();
			KeyedCollection qrybodyColl = new KeyedCollection("body");
			//组装报文头
			qryheadKcoll.addDataField("SvcCd","30220002");
			qryheadKcoll.addDataField("ScnCd","01");
			qryheadKcoll.addDataField("TxnMd","ONLINE");
			qryheadKcoll.addDataField("UsrLngKnd","CHINESE");
			qryheadKcoll.addDataField("jkType","cbs");
			//报文体
			qrybodyColl.addDataField("AcctNoCrdNo",acctCardNo);//贷款账号
			qrybodyColl.addDataField("AcctSeqNo",acctSeqNo);//发放号
			logger.info("------------------组装撤销接口请求报文  end----------------");
			
			logger.info("----------------------发送请求报文："+qrybodyColl);
			retKColl = ESBUtil.sendEsbMsg(qryheadKcoll,qrybodyColl);
			logger.info("----------------------返回报文："+retKColl);

			if(retKColl.containsKey("SYS_HEAD")){
				KeyedCollection retSysHead=(KeyedCollection)retKColl.getDataElement("SYS_HEAD");//返回报文头
				KeyedCollection retAppHead=(KeyedCollection)retKColl.getDataElement("APP_HEAD");//返回报文头
				KeyedCollection retBody=(KeyedCollection)retKColl.getDataElement("BODY");//返回报文体
				
				IndexedCollection retArr=(IndexedCollection)retSysHead.getDataElement("RetInfArry");
				KeyedCollection retObj=(KeyedCollection)retArr.get(0);
				String retCd=(String)retObj.getDataValue("RetCd");
				logger.info("------------------------返回结果标识："+retCd);
				if(!"000000".equals(retCd)){
					throw new EMPException((String)retObj.getDataValue("RetInf"));
				}
			}else{
				throw new Exception("核心授权撤销接口请求失败");
			}
			logger.info("------------------组装撤销接口请求报文  end----------------");
			return retKColl;
		}catch(Exception e){
			throw new Exception("授权撤销接口失败，错误信息："+e.getMessage());
		}
	}
	
	/**
	 * 还款试算接口实现
	 * @author huangtao
	 *//*
	@Override
	public KeyedCollection tradeHKSS(KeyedCollection reqParam, Context context, Connection connection)
			throws EMPException {
		try{
			*//** 封装发送报文信息 *//*
			KeyedCollection reqHead = new KeyedCollection();
//			组装报文头中服务代码和场景代码
			reqHead.put("SvcCd", "30230001");//还款试算服务代码
			reqHead.put("ScnCd","09");//还款试算场景代码
			//跟核心交互的接口在头里还得传这三个参数
			reqHead.put("TxnMd","ONLINE");
			reqHead.put("UsrLngKnd","CHINESE");
			reqHead.put("jkType","cbs");
			*//** 列表分页所需参数 *//*
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			reqHead.put("TurnPgFlg", "1");//翻译标志，默认1
			reqHead.put("PgDsplLineNum", String.valueOf(pageInfo.pageSize));//每页显示条数
			reqHead.put("CrnPgRcrdNo", String.valueOf(pageInfo.pageIdx-1));//当前页记录号			
			
			*//** 封装报文体 *//*
			KeyedCollection reqBody = new KeyedCollection();
			reqBody.put("LoanAmt", reqParam.getDataValue("apply_amount"));//贷款金额
			reqBody.put("ExecIntRate", reqParam.getDataValue("reality_ir_y"));//执行利率（裕民银行对接神马核心只有年利率）
			String appDate=(String)reqParam.getDataValue("apply_date");
			reqBody.put("OpnAcctDt", appDate.replaceAll("-", ""));//开户日期
			reqBody.put("AcctExprtnDt", ((String)reqParam.getDataValue("CONTRACT_EXPIRY_DATE")).replaceAll("-", ""));//账户到期日期
			String repayType = (String)reqParam.getDataValue("repay_type");
			reqBody.put("PlanMd", getRepayType(repayType));//计划方式
			String interest_term = (String)reqParam.getDataValue("interest_term");
			reqBody.put("IntStlmntFrqcy", getIntStlmntFrqcy(interest_term));//结息频率
			String prdId=(String)reqParam.getDataValue("prd_id");//产品编号
			String repay_date = (String)reqParam.getDataValue("repay_date");//还款日
			//根据对公对私贷款区分不同结息日及结息日期
			String intStlmntDt="21";//默认21
			String nxtStlmntIntDt="";
			if("100046".equals(prdId)){//对公贷款
				nxtStlmntIntDt=DateUtils.getNextJxDate(interest_term, appDate, intStlmntDt);//对公下一结息日期
			}else{//对私贷款repay_date
				int len=repay_date.length();
				if(len==2){
					intStlmntDt = repay_date;
				}else{
					intStlmntDt = "0"+repay_date;
				}
				nxtStlmntIntDt=DateUtils.getNextJxDate(interest_term, appDate, intStlmntDt);
			}
			reqBody.put("NxtStlmntIntDt", nxtStlmntIntDt.replaceAll("-", "") );//下一结息日期
			reqBody.put("IntStlmntDt", intStlmntDt);//结息日期
			reqBody.put("MoBaseDaysNum", "30");//月基准天数
			reqBody.put("AnulBaseDayNum", "360");//年基准天数
			reqBody.put("OprtnTp", "4");//操作类型
			*//** -----------------------核心校验必输字段---------------------- *//*

			
			*//** -----------------------核心校验必输字段---------------------- *//*
			
			IndexedCollection repymtPlanInfArry = new IndexedCollection("RepymtPlanInfArry");//还款计划信息数组
			KeyedCollection repymtPlanInfArryItem = new KeyedCollection();
			repymtPlanInfArryItem.put("BegDt", reqParam.getDataValue("apply_date"));//起始日期/开始日期
			repymtPlanInfArryItem.put("EndDt", reqParam.getDataValue("CONTRACT_EXPIRY_DATE"));//结束日期/终止日期
			repymtPlanInfArryItem.put("PlanMd", "2");//计划方式
//		不传	repymtPlanInfArry.add(repymtPlanInfArryItem);
//			reqBody.addDataElement(repymtPlanInfArry);
			
			IndexedCollection cmbRepymtMdInfArry = new IndexedCollection("CmbRepymtMdInfArry");//组合还款方式信息数组
			KeyedCollection cmbRepymtMdInfArryItem = new KeyedCollection();
//			cmbRepymtMdInfArryItem.put("RepymtMd", reqParam.getDataValue("repay_type"));//还款方式
			cmbRepymtMdInfArryItem.put("RepymtMd", "2");//还款方式
			cmbRepymtMdInfArryItem.put("NxtDealDt", DateUtils.getAddDate("M", (String) reqParam.getDataValue("apply_date"), 1)  );//下一处理日期
			cmbRepymtMdInfArryItem.put("NxtIntDealDt", "17");//下一利息处理日期
//		不传	cmbRepymtMdInfArry.add(cmbRepymtMdInfArryItem);
//			reqBody.addDataElement(cmbRepymtMdInfArry);
			
			*//** 执行发送操作 *//*
			KeyedCollection rsp = ESBUtil.sendEsbMsg(reqHead, reqBody);
			*//** 解析反馈报文头判断该笔交易成功与否 *//*
			KeyedCollection rspSysHead = (KeyedCollection)rsp.get("SYS_HEAD");
			IndexedCollection retInfoIColl = (IndexedCollection) rspSysHead.getDataElement("RetInfArry");
			Object obj = retInfoIColl.get(0);
			KeyedCollection returnKColl = (KeyedCollection)rsp.get("SYS_HEAD");
			if(obj instanceof KeyedCollection){
				KeyedCollection kc = (KeyedCollection)obj;
				String retCd = (String) kc.getDataValue("RetCd");
				String retSts = (String) kc.getDataValue("RetInf");
				returnKColl.put("RET_CODE", retCd);
				returnKColl.put("RET_STATUS", retSts);
				returnKColl.addDataElement(rsp.getDataElement("BODY"));
			}
			return rsp;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new EMPException("账户信息查询通讯失败"+e.getMessage());
		}
	}*/
	
	public KeyedCollection tradeHKSS(KeyedCollection reqParam, Context context, Connection connection) throws EMPException {
		logger.info("******************************进入还款试算接口************************");
		try{
			//** 封装发送报文信息 *//
			KeyedCollection reqHead = new KeyedCollection();
			//组装报文头中服务代码和场景代码
			reqHead.put("SvcCd", "30230001");//还款试算服务代码
			reqHead.put("ScnCd","12");//还款试算场景代码
			//跟核心交互的接口在头里还得传这三个参数
			reqHead.put("TxnMd","ONLINE");
			reqHead.put("UsrLngKnd","CHINESE");
			reqHead.put("jkType","cbs");
			//** 列表分页所需参数 *//
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			reqHead.put("TurnPgFlg", "1");//翻译标志，默认1
			reqHead.put("PgDsplLineNum", String.valueOf(pageInfo.pageSize));//每页显示条数
			reqHead.put("CrnPgRcrdNo", String.valueOf(pageInfo.pageIdx-1));//当前页记录号			
			
			//** 封装报文体 *//
			logger.info("***********************************封装报文体**********************************");
			KeyedCollection reqBody = new KeyedCollection();
			reqBody.put("AcctRelFlg", "N");//账户相关标识
			reqBody.put("LoanLmt", reqParam.getDataValue("apply_amount"));//贷款额度
			reqBody.put("ExecIntRate", Double.parseDouble((String)reqParam.getDataValue("reality_ir_y"))*100);//执行利率
			String appDate=(String)reqParam.getDataValue("apply_date");
			reqBody.put("OpnAcctDt", appDate.replaceAll("-", ""));//贷款起始日期
			reqBody.put("AcctExprtnDt", ((String)reqParam.getDataValue("CONTRACT_EXPIRY_DATE")).replaceAll("-", ""));//贷款终止日期
			String repayType = (String)reqParam.getDataValue("repay_type");
			reqBody.put("MainPlanMd", getRepayType(repayType));//主计划方式
			reqBody.put("PlanMd", getRepayType(repayType));//计划方式
			String interest_term = (String)reqParam.getDataValue("interest_term");
			reqBody.put("IntStlmntFrqcy", getIntStlmntFrqcy(interest_term));//结息频率
			//根据对公对私贷款区分不同结息日及结息日期
			String intStlmntDt="21";//默认21
			String date = context.getDataValue(PUBConstant.OPENDAY).toString();//当前营业日期
			String prdId=(String)reqParam.getDataValue("prd_id");//产品编号
			String repay_date = (String)reqParam.getDataValue("repay_date");//还款日
			//String nxtStlmntIntDt="";
			if("100046".equals(prdId)){//对公贷款
				//nxtStlmntIntDt=DateUtils.getNextJxDate(interest_term, appDate, intStlmntDt);//对公下一结息日期
			}else{//对私贷款repay_date
				int len=repay_date.length();
				if(len==2){
					intStlmntDt = repay_date;
				}else{
					intStlmntDt = "0"+repay_date;
				}
				//nxtStlmntIntDt=DateUtils.getNextJxDate(interest_term, appDate, intStlmntDt);
			}
			String nxtStlmntIntDt=DateUtils.getNextJxDate(interest_term, date, intStlmntDt);//对公下一结息日期
			reqBody.put("NxtStlmntIntDt", nxtStlmntIntDt.replaceAll("-", ""));//下一结息日期
			reqBody.put("IntStlmntDt", repay_date);//结息日期
			reqBody.put("MoBaseDaysNum", "30");//月基准天数
			reqBody.put("AnulBaseDayNum", "360");//年基准天数
			reqBody.put("PlanAmt", reqParam.getDataValue("apply_amount"));//计划金额
			reqBody.put("OprtnTp", "01");//操作类型
			//reqBody.put("BlonLoanClcTrmTms", "");//气球贷计算期数
			logger.info("******************************************请求报文体："+reqBody);
			//** 执行发送操作 *//
			KeyedCollection rsp = ESBUtil.sendEsbMsg(reqHead, reqBody);
			logger.info("******************************************返回报文："+rsp);
			return rsp;//返回包含错误信息的报文信息
		}catch(Exception e){
			throw new EMPException("还款试算失败："+e.getMessage());
		}
	}
	
	//还款方式转换
	public String getRepayType(String oldRepType){
		Map<String,String> container=new HashMap<String,String>();
		container.put("A001", "5");
		container.put("A002", "1");
		container.put("A003", "2");
		container.put("A004", "4");
		container.put("A005", "3");
		return container.get(oldRepType);
	}
			
	//结息频率转换
	public String getIntStlmntFrqcy(String oldIntStlmntFrqcy){
		Map<String,String> container=new HashMap<String,String>();
		container.put("0", "Y1");
		container.put("1", "M3");
		container.put("2", "M1");
		return container.get(oldIntStlmntFrqcy)==null?"":container.get(oldIntStlmntFrqcy);
	}
}
