package com.yucheng.cmis.biz01line.esb.interfaces.imple;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dc.eai.data.Array;
import com.dc.eai.data.AtomData;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.client.ESBClient;
import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.pub.FTPUtil;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.pub.CMISComponent;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
/**
 * 神码ESB接口实现类(泉州新一代信贷管理系统)
 * @author Pansq
 */
public class ESBInterfacesImple extends CMISComponent implements ESBInterface {
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
		sysHead.addField("CONSUMER_ID", TagUtil.getEMPField(TradeConstance.CONSUMERID, FieldType.FIELD_STRING, 6, 0));
		
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
	 * 
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
	 * 获取本地扩展报文头结构体
	 * @return
	 * @throws Exception
	 */
	public CompositeData getLocalHeadCD() throws Exception {
		CompositeData localHead = new CompositeData();
		//localHead.addField("USER_ID", TagUtil.getEMPField(code, FieldType.FIELD_STRING, 30, 0));
		return localHead;
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
	 * 解析返回结构体报文，转换为信贷EMP中的IndexedCollection
	 * @param respCD 返回报文结构体
	 * @return EMP IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getRespBodyCD4IColl(CompositeData respCD) throws Exception {
		IndexedCollection iColl = new IndexedCollection(TradeConstance.ESB_BODY);
		AtomData element = respCD.getObject(TradeConstance.ESB_BODY);
		if(element instanceof Array){
			Array array = (Array)element;
			for(int i=0;i<array.size();i++){
				CompositeData arrCD = array.getStruct(i);
				iColl.add(TagUtil.replaceCD2KColl(arrCD));
			}
		}
		return iColl;
	}
	
	/**
	 * 解析请求结构体中系统头，转换为信贷EMP中的KeyedCollection
	 * @param respCD 返回报文结构体
	 * @return EMP KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getReqSysHead(CompositeData reqCD) throws Exception {
		CompositeData reqSysHeadCD = reqCD.getStruct(TradeConstance.ESB_SYS_HEAD);
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("SERVICE_CODE", reqSysHeadCD.getField("SERVICE_CODE").strValue());
		kColl.addDataField("SERVICE_SCENE", reqSysHeadCD.getField("SERVICE_SCENE").strValue());
		kColl.addDataField("CONSUMER_SEQ_NO", reqSysHeadCD.getField("CONSUMER_SEQ_NO").strValue());
		//kColl.addDataField("CONSUMER_SEQ_NO", "0123456789");
		//kColl.addDataField("TRAN_DATE", "20121111");
		kColl.addDataField("TRAN_DATE", reqSysHeadCD.getField("TRAN_DATE").strValue());
		kColl.setName("SYS_HEAD");
		return kColl;
		/*CompositeData reqSysHeadCD = reqCD.getStruct(TradeConstance.ESB_SYS_HEAD);
		return TagUtil.replaceCD2KColl(reqSysHeadCD);*/
	}
	/**
	 * 解析请求结构体中系统头，转换为信贷EMP中的KeyedCollection
	 * @param respCD 返回报文结构体
	 * @return EMP KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getReqSysHead4ECDS(CompositeData reqCD) throws Exception {
		CompositeData reqSysHeadCD = reqCD.getStruct(TradeConstance.ESB_SYS_HEAD);
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("SERVICE_CODE", reqSysHeadCD.getField("SERVICE_CODE").strValue());
		kColl.addDataField("SERVICE_SCENE", reqSysHeadCD.getField("SERVICE_SCENE").strValue());
		kColl.addDataField("CONSUMER_SEQ_NO", reqSysHeadCD.getField("CONSUMER_SEQ_NO").strValue());
		//kColl.addDataField("CONSUMER_SEQ_NO", "0123456789");
		//kColl.addDataField("TRAN_DATE", "20121111");
		kColl.addDataField("TRAN_DATE", reqSysHeadCD.getField("TRAN_DATE").strValue());
		kColl.addDataField("BRANCH_ID", reqSysHeadCD.getField("BRANCH_ID").strValue());
		kColl.setName("SYS_HEAD");
		return kColl;
	}
	/**
	 * 解析请求结构体中应用头，转换为信贷EMP中的KeyedCollection
	 * @param respCD 返回报文结构体
	 * @return EMP KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getReqAppHead(CompositeData reqCD) throws Exception {
		CompositeData reqAppHeadCD = reqCD.getStruct(TradeConstance.ESB_APP_HEAD);
		return TagUtil.replaceCD2KColl(reqAppHeadCD);
	}
	/**
	 * 解析请求结构体中本地报文头，转换为信贷EMP中的KeyedCollection
	 * @param respCD 返回报文结构体
	 * @return EMP KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getReqLocalHead(CompositeData reqCD) throws Exception {
		CompositeData reqBodyCD = reqCD.getStruct(TradeConstance.ESB_LOCAL_HEAD);
		return TagUtil.replaceCD2KColl(reqBodyCD);
	}
	/**
	 * 解析请求结构体中报文体，转换为信贷EMP中的KeyedCollection
	 * @param respCD 返回报文结构体
	 * @return EMP KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getReqBody(CompositeData reqCD) throws Exception {
		KeyedCollection reqBodyKColl = null;
		CompositeData reqBodyCD = reqCD.getStruct(TradeConstance.ESB_BODY);
		reqBodyKColl = TagUtil.replaceCD2KColl(reqBodyCD);
		reqBodyKColl.setName(TradeConstance.ESB_BODY);
		return reqBodyKColl;
	}
	
	/**
	 * 返回响应报文的结构体
	 * @param serviceCode 交易码
	 * @param serviceSence 交易场景
	 * @param openDay 营业时间
	 * @param nowDate 当前日期
	 * @param serno 流水号
	 * @param status 返回状态
	 * @param retCode 返回码
	 * @param retMsg 返回信息
	 * @return 响应报文结构体
	 * @throws Exception
	 */
	public CompositeData getRespSysHeadCD(String serviceCode, String serviceSence, String openDay, String nowDate, String serno, String status, String retCode, String retMsg) throws Exception {
		CompositeData respCD = new CompositeData();
		CompositeData sysHeadCD = new CompositeData();
		sysHeadCD.addField("SERVICE_CODE", TagUtil.getEMPField(serviceCode, FieldType.FIELD_STRING, 30, 0));
		sysHeadCD.addField("SERVICE_SCENE", TagUtil.getEMPField(serviceSence, FieldType.FIELD_STRING, 2, 0));
		sysHeadCD.addField("TRAN_DATE", TagUtil.getEMPField(openDay.replaceAll("-", ""), FieldType.FIELD_STRING, 8, 0));
		sysHeadCD.addField("TRAN_TIMESTAMP", TagUtil.getEMPField(nowDate.substring(12, 23).replaceAll(":", ""), FieldType.FIELD_STRING, 9, 0));
		sysHeadCD.addField("RET_STATUS", TagUtil.getEMPField(status, FieldType.FIELD_STRING, 8, 0));
		Array arr = new Array();
		CompositeData arrCD = new CompositeData();
		arrCD.addField("RET_CODE", TagUtil.getEMPField(retCode, FieldType.FIELD_STRING, 6, 0));
		arrCD.addField("RET_MSG", TagUtil.getEMPField(retMsg, FieldType.FIELD_STRING, 200, 0));
		arr.addStruct(arrCD);
		sysHeadCD.addArray("RET", arr);
		respCD.addStruct("SYS_HEAD", sysHeadCD);
		
		CompositeData bodyCD = new CompositeData();
		respCD.addStruct("BODY", bodyCD);
		
		return respCD;
	}
	/**
	 * 返回响应报文的结构体
	 * @param serviceCode 交易码
	 * @param serviceSence 交易场景
	 * @param openDay 营业时间
	 * @param nowDate 当前日期
	 * @param serno 流水号
	 * @param status 返回状态
	 * @param kColl 返回信息
	 * @return 响应报文结构体
	 * @throws Exception
	 */
	public CompositeData getRespSysHeadCD4ECDS(String serviceCode, String serviceSence, String openDay, String nowDate, String serno, String status, KeyedCollection resultKColl) throws Exception {
		CompositeData respCD = new CompositeData();
		CompositeData sysHeadCD = new CompositeData();
		String retCode = "";
		String retMsg = "";
		if(resultKColl!=null&&resultKColl.size()>0){
			retCode = (String)resultKColl.getDataValue("ret_code");
			retMsg = (String)resultKColl.getDataValue("ret_msg");
		}
		sysHeadCD.addField("SERVICE_CODE", TagUtil.getEMPField(retCode, FieldType.FIELD_STRING, 30, 0));
		sysHeadCD.addField("SERVICE_SCENE", TagUtil.getEMPField(serviceSence, FieldType.FIELD_STRING, 2, 0));
		sysHeadCD.addField("TRAN_DATE", TagUtil.getEMPField(openDay.replaceAll("-", ""), FieldType.FIELD_STRING, 8, 0));
		sysHeadCD.addField("TRAN_TIMESTAMP", TagUtil.getEMPField(nowDate.substring(12, 23).replaceAll(":", ""), FieldType.FIELD_STRING, 9, 0));
		sysHeadCD.addField("RET_STATUS", TagUtil.getEMPField(status, FieldType.FIELD_STRING, 8, 0));
		Array arr = new Array();
		CompositeData arrCD = new CompositeData();
		arrCD.addField("RET_CODE", TagUtil.getEMPField(retCode, FieldType.FIELD_STRING, 6, 0));
		arrCD.addField("RET_MSG", TagUtil.getEMPField(retMsg, FieldType.FIELD_STRING, 200, 0));
		arr.addStruct(arrCD);
		sysHeadCD.addArray("RET", arr);
		respCD.addStruct("SYS_HEAD", sysHeadCD);
		
		CompositeData bodyCD = new CompositeData();
		if(serviceCode!=null&&"03003000002".equals(serviceCode)&&resultKColl.containsKey("buss_deal_status")){
			bodyCD.addField("BUSS_DEAL_STATUS", TagUtil.getEMPField(resultKColl.getDataValue("buss_deal_status").toString(), FieldType.FIELD_STRING, 10, 0));
		}else if(serviceCode!=null&&"03004000002".equals(serviceCode)&&resultKColl.containsKey("cancel_result")){
			bodyCD.addField("CANCEL_RESULT", TagUtil.getEMPField(resultKColl.getDataValue("cancel_result").toString(), FieldType.FIELD_STRING, 10, 0));
		}
		respCD.addStruct("BODY", bodyCD);
		
		return respCD;
	}
	
}
