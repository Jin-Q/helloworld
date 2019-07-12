package com.yucheng.cmis.biz01line.esb.interfaces;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
/**
 * 神码ESB接口解析(泉州新一代信贷管理系统)
 * @author Pansq
 */
public interface ESBInterface {
	/** ------------------------------------信贷作为客户端请求调用START------------------------------------------------ */
	/**
	 * 获取系统头报文头结构体
	 * @param code 服务代码
	 * @param scene 服务场景
	 * @param consumerId 消费者ID
	 * @param serno 消费者流水号
	 * @param context 上下文
	 * @return 系统报文头结构体 CompositeData
	 * @throws Exception
	 */
	public CompositeData getSysHeadCD(String code, String scene, String consumerId, String serno,String userId,String userName, String orgId, String sysId, Context context) throws Exception;
	/**
	 * 获取应用层报文头结构体（待定、预留）
	 * @param userId 操作人员ID
	 * @param userName 操作人员名称
	 * @param orgId 操作人员所在机构
	 * @param sysId 系统ID
	 * @return 应用报文头结构体
	 * @throws Exception
	 */
	public CompositeData getAppHeadCD(String userId, String userName, String orgId, String sysId) throws Exception; 
	/**
	 * 获取本地扩展报文头结构体
	 * @return
	 * @throws Exception
	 */
	public CompositeData getLocalHeadCD() throws Exception;
	/**
	 * 贷款发放授权接口
	 * @param reqCD 请求报文体（包括系统报文头、应用报文头、报文体）
	 * @return 返回结果结构体
	 * @throws Exception 
	 */
	public CompositeData tradeDKFFSQ(CompositeData reqCD) throws Exception;
	
	/** ------ESB客户端发送示例：CompositeData resCD = ESBClient.request(reqCD);------ */
	
	/**
	 * 解析返回结构体的系统报文头(暂不适用与系统头有循环)
	 * @param respCD 返回报文结构体
	 * @return EMP KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getRespSysHeadCD(CompositeData respCD) throws Exception;
	/**
	 * 解析返回结构体报文，转换为信贷EMP中的KeyedCollection
	 * @param respCD 返回报文结构体
	 * @return EMP KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getRespBodyCD4KColl(CompositeData respCD) throws Exception;
	/**
	 * 解析返回结构体报文，转换为信贷EMP中的IndexedCollection
	 * @param respCD 返回报文结构体
	 * @return EMP IndexedCollection
	 * @throws Exception
	 */
	public IndexedCollection getRespBodyCD4IColl(CompositeData respCD) throws Exception;
	
	/** ------------------------------------信贷作为客户端请求调用END------------------------------------------------ */
	
	/** ------------------------------------信贷作为服务端解析请求START------------------------------------------------ */
	/**
	 * 解析请求结构体中系统头，转换为信贷EMP中的KeyedCollection(预留特殊需求，待定)
	 * @param respCD 返回报文结构体
	 * @return EMP KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getReqSysHead(CompositeData reqCD) throws Exception;
	/**
	 * 解析请求结构体中系统头，转换为信贷EMP中的KeyedCollection(预留特殊需求，待定)
	 * @param respCD 返回报文结构体
	 * @return EMP KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getReqSysHead4ECDS(CompositeData reqCD) throws Exception;
	/**
	 * 解析请求结构体中应用头，转换为信贷EMP中的KeyedCollection(预留特殊需求，待定)
	 * @param respCD 返回报文结构体
	 * @return EMP KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getReqAppHead(CompositeData reqCD) throws Exception;
	/**
	 * 解析请求结构体中本地报文头，转换为信贷EMP中的KeyedCollection(预留特殊需求，待定)
	 * @param respCD 返回报文结构体
	 * @return EMP KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getReqLocalHead(CompositeData reqCD) throws Exception;
	/**
	 * 解析请求结构体中报文体，转换为信贷EMP中的KeyedCollection(预留特殊需求，待定)
	 * @param respCD 返回报文结构体
	 * @return EMP KeyedCollection
	 * @throws Exception
	 */
	public KeyedCollection getReqBody(CompositeData reqCD) throws Exception;
	
	/** ------------------------------------信贷作为服务端解析请求END------------------------------------------------ */
	
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
	public CompositeData getRespSysHeadCD(String serviceCode, String serviceSence, String openDay, String nowDate, String serno, String status, String retCode, String retMsg) throws Exception;
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
	public CompositeData getRespSysHeadCD4ECDS(String serviceCode, String serviceSence, String openDay, String nowDate, String serno, String status, KeyedCollection resultKColl) throws Exception;
}
