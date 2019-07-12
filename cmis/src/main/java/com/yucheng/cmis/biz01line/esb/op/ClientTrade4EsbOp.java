package com.yucheng.cmis.biz01line.esb.op;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import com.dc.eai.data.CompositeData;
import com.dcfs.esb.client.ESBClient;
import com.dcfs.esb.pack.standardxml.PackUtil;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.interfaces.ClientTradeInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.ESBUtil;
/**
 * XD作为客户端请求ESB交易转发类，此类主要初始化数据，并且转发给不同交易
 * 传递的参数必须包括交易码以及交易场景，其余参数更具自身需求添加
 * @author Pansq
 * @version V1.0
 * 修改记录：
 * 版本号    修改人       修改日期      修改内容
 * V1.1		liqh		2013/11/14		1.增加交易处理结束后更新授权表状态 2.增加控制交易执行失败默认执行5次
 */
public class ClientTrade4EsbOp extends CMISOperation {
	ClientTradeInterface clientTradeInterface;
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		boolean result = false;//反馈信息标识
		KeyedCollection retKColl;
		
		try {
			connection = this.getConnection(context);
			
			String serviceCode = null;
			String senceCode = null;
			if(context.containsKey("service_code")){
				serviceCode = (String)context.getDataValue("service_code");
			}
			if(context.containsKey("sence_code")){
				senceCode = (String)context.getDataValue("sence_code");
			}
			if(serviceCode == null || serviceCode.trim().length() == 0){
				throw new EMPException("交易码获取失败！");
			}
			if(senceCode == null || senceCode.trim().length() == 0){
				throw new EMPException("交易场景获取失败！");
			}
			//执行失败，则默认重复执行5次控制
			for(int i=0;i<5;i++){
			if(result){//执行成功则直接跳出循环
				break;
			}
			/** 加载转发机制 */
			getTranExecute(serviceCode, senceCode, connection);
			/** 执行客户端封装报文操作 */
			CompositeData reqCD = clientTradeInterface.doExecute(context, connection);
			/** 打印后台发送日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(reqCD), "UTF-8"));
			/** 执行发送操作 */
			////CompositeData retCD = ESBClient.request(reqCD);
			KeyedCollection reqKcoll=TagUtil.replaceCD2KColl(reqCD);
			KeyedCollection retCD = ESBUtil.sendEsbMsg((KeyedCollection)reqKcoll.getDataElement("SYS_HEAD"), (KeyedCollection)reqKcoll.getDataElement("BODY"));
			System.out.println("****************************************");
			System.out.println(new String(PackUtil.pack(reqCD), "UTF-8"));
			/** 打印后台反馈日志 */
			////EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文 */
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			/** 解析反馈报文头 */
			////retKColl = esbInterfacesImple.getRespSysHeadCD(retCD);
			retKColl=(KeyedCollection)retCD.getDataElement("SYS_HEAD");
			/** 解析反馈报文体 */
			////KeyedCollection respBodyKColl = esbInterfacesImple.getRespBodyCD4KColl(retCD);
			KeyedCollection respBodyKColl = (KeyedCollection)retCD.getDataElement("BODY");
			this.putDataElement2Context(respBodyKColl, context);
			
			IndexedCollection retArr=(IndexedCollection)retKColl.getDataElement("RetInfArry");
			KeyedCollection retObj=(KeyedCollection)retArr.get(0);
			
			/** 判断报文发送成功与否 */
			////if(TagUtil.haveSuccess(retKColl, context)){//成功
			if("000000".equals(retObj.getDataValue("RetCd"))){//成功
				result=true;
				/** 判断是否为授权交易，如果为授权交易则需要更新授权表信息 */
				if(("30210004".equals(serviceCode)&&"02".equals(senceCode))||
				   ("02001000001".equals(serviceCode)&&"07".equals(senceCode))||
				   ("02001000001".equals(serviceCode)&&"08".equals(senceCode))||
				   ("02001000001".equals(serviceCode)&&"09".equals(senceCode))||
				   ("03001000008".equals(serviceCode)&&"01".equals(senceCode))||
				   ("02002000011".equals(serviceCode)&&"02".equals(senceCode))||
				   ("02001000003".equals(serviceCode)&&"02".equals(senceCode))||
				   ("06001000003".equals(serviceCode)&&"01".equals(senceCode))||
				   (TradeConstance.SERVICE_CODE_GJ.equals(serviceCode) && TradeConstance.SERVICE_SCENE_GJSQ.equals(senceCode))||
				   (TradeConstance.SERVICE_CODE_ZYGL.equals(serviceCode) && TradeConstance.SERVICE_SCENE_ZCZRSQ.equals(senceCode))){
					dulReturnMsg(retCD,"02", context, connection);
				}else if("02002000007".equals(serviceCode)&&"01".equals(senceCode)){
					//XD150520037_信贷系统利率调整修改优化
					dulReturnMsg(retCD,"02", context, connection);
					clientTradeInterface.doSuccess(context, connection);
				}
			}else {//失败
				result=false;
				/** 判断是否为授权交易，如果为授权交易则需要更新授权表信息 */
				if(("30210004".equals(serviceCode)&&"02".equals(senceCode))||
				   ("02001000001".equals(serviceCode)&&"07".equals(senceCode))||
				   ("02001000001".equals(serviceCode)&&"08".equals(senceCode))||
				   ("02001000001".equals(serviceCode)&&"09".equals(senceCode))||
				   ("03001000008".equals(serviceCode)&&"01".equals(senceCode))||
				   ("02002000011".equals(serviceCode)&&"02".equals(senceCode))||
				   ("02001000003".equals(serviceCode)&&"02".equals(senceCode))||
				   ("06001000003".equals(serviceCode)&&"01".equals(senceCode))||
				   (TradeConstance.SERVICE_CODE_GJ.equals(serviceCode)&& TradeConstance.SERVICE_SCENE_GJSQ.equals(senceCode))||
				   (TradeConstance.SERVICE_CODE_ZYGL.equals(serviceCode) && TradeConstance.SERVICE_SCENE_ZCZRSQ.equals(senceCode))||
				   //XD150520037_信贷系统利率调整修改优化
				   ("02002000007".equals(serviceCode)&&"01".equals(senceCode))){
					dulReturnMsg(retCD,"01", context, connection);
				}
				this.setRetStatus("failed", (String)retObj.getDataValue("RetInf"), context);
			}
		 }
		} catch (Exception e) {
			if(context.containsKey("flag")){
				context.setDataValue("flag", "error");
			}else{
				context.addDataField("flag", "error");
			}
			if(context.containsKey("RetInf")){
				context.setDataValue("RetInf", e.getMessage());
			}else{
				context.addDataField("RetInf", e.getMessage());
			}
			KeyedCollection respBodyKColl = new KeyedCollection("BODY");
			this.putDataElement2Context(respBodyKColl, context);
			try{
				connection.rollback();
			}catch(SQLException ee){
				ee.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

	/**
	 * 设置交易的返回信息
	 * @param flag 返回成功标识
	 * @param retMsg 返回信息
	 * @param context 上下文
	 * @throws EMPException
	 */
	private void setRetStatus(String flag, String RetInf, Context context) throws EMPException {
		if(context.containsKey("flag")){
			context.setDataValue("flag", flag);
		}else {
			context.addDataField("flag", flag);
		}
		if(context.containsKey("RetInf")){
			context.setDataValue("RetInf", RetInf);
		}else {
			context.addDataField("RetInf", RetInf);
		}
	}
	
	/**
	 * 通过交易码查询需要转发的不同交易信息
	 * @param serviceCode 交易码
	 * @param senceCode 交易场景
	 */
	private void getTranExecute(String serviceCode, String senceCode, Connection connection) throws EMPException{
		KeyedCollection kc = new KeyedCollection();
		kc.addDataField("SERVICE_CODE", serviceCode);
		kc.addDataField("SERVICE_SCENE", senceCode);
		/** 通过交易码查询所配置的交易实现类，决定所作的交易处理 */
		String impleClass;
		try {
			impleClass = (String)SqlClient.queryFirst("queryEsbConfig", kc, null, connection);
			if(impleClass == null || impleClass.trim().length() == 0){
				throw new EMPException("交易码【"+serviceCode+"】，交易场景【"+senceCode+"】未获取到配置的业务实现类");
			}
			clientTradeInterface = (ClientTradeInterface)Class.forName(impleClass.trim()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 针对授权信息更新本地授权信息更新操作实现方法
	 * @param retKColl 反馈状态
	 * @param context 上下文
	 * @param connection 数据库连接
	 */
	private void dulReturnMsg(KeyedCollection retCD,String status, Context context, Connection connection) throws EMPException {
		String serno = null;
		if(context.containsKey("tran_serno")){//提交记录交易流水号
			serno = (String)context.getDataValue("tran_serno");
		}
		if(serno == null || serno.trim().length() == 0){
			throw new EMPException("更新授权台帐表，获取授权交易流水号失败！");
		}
		KeyedCollection retKColl=(KeyedCollection)retCD.getDataElement("SYS_HEAD");
		KeyedCollection respBodyKColl = (KeyedCollection)retCD.getDataElement("BODY");
		String acctNoCrdNo = (String)respBodyKColl.getDataValue("AcctNoCrdNo");//账号/卡号
		String acctNoSeqNo = (String)respBodyKColl.getDataValue("AcctNoSeqNo");//账号序号
		TableModelDAO dao = this.getTableModelDAO(context);
		KeyedCollection authKcoll = dao.queryDetail("PvpAuthorize", serno, connection);
		String authorize_no = (String)authKcoll.getDataValue("authorize_no");
		IndexedCollection pvpIColl = dao.queryList("PvpAuthorize", " where authorize_no = '"+authorize_no+"' and status <> '02'", connection);
		String sendNum = "";//发送次数
		for(int i=0;i<pvpIColl.size();i++){
			KeyedCollection kc = (KeyedCollection)pvpIColl.get(i);
			/** 发送次数+1 */
			sendNum = (String)kc.getDataValue("send_times");
			if(sendNum == null || sendNum.trim().length() == 0){
				sendNum = "0";
			}
			int sendTimes = Integer.parseInt(sendNum);
			sendNum = String.valueOf(sendTimes+1);
			kc.put("status", status);
			kc.put("send_times", sendNum);
			kc.put("tran_date", context.getDataValue(CMISConstance.OPENDAY));//授权发送时更新交易日期
			IndexedCollection retArr=(IndexedCollection)retKColl.getDataElement("RetInfArry");
			KeyedCollection retObj=(KeyedCollection)retArr.get(0);
			////kc.setDataValue("return_code", retKColl.getDataValue("RET_CODE"));
			////kc.setDataValue("return_desc", retKColl.getDataValue("RET_MSG"));
//			kc.put("base_acct_no", acctNoCrdNo);
//			kc.put("acct_seq_no", acctNoSeqNo);
			kc.put("return_code", retObj.getDataValue("RetCd"));
			kc.put("return_desc", retObj.getDataValue("RetInf"));
			dao.update(kc, connection);
		}
	}
	
}
