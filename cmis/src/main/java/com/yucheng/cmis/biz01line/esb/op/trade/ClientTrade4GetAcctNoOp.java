package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;

import com.dc.eai.data.CompositeData;
import com.dcfs.esb.client.ESBClient;
import com.dcfs.esb.pack.standardxml.PackUtil;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.interfaces.ClientTradeInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

public class ClientTrade4GetAcctNoOp extends CMISOperation {

	ClientTradeInterface clientTradeInterface;
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			
			KeyedCollection retKColl;
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
			/** 加载转发机制 */
			getTranExecute(serviceCode, senceCode, connection);
			/** 执行客户端封装报文操作 */
			CompositeData reqCD = clientTradeInterface.doExecute(context, connection);
			/** 打印后台发送日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(reqCD), "UTF-8"));
			/** 执行发送操作 */
			CompositeData retCD = ESBClient.request(reqCD);
			/** 打印后台反馈日志 */
			EMPLog.log("inReport", EMPLog.INFO, 0,  new String(PackUtil.pack(retCD), "UTF-8"));
			/** 解析反馈报文 */
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			retKColl = esbInterfacesImple.getRespSysHeadCD(retCD);
			
			/** 解析反馈报文体 */
			KeyedCollection bodyKColl = esbInterfacesImple.getRespBodyCD4KColl(retCD);
			
			/** 判断报文发送成功与否 */
			if(haveSuccess(retKColl, context)){
				context.addDataField("flag", "success");
				context.addDataField("msg", (String)retKColl.getDataValue("RET_MSG"));
				context.addDataField("ACCT_NO", (String)bodyKColl.getDataValue("ACCT_NO"));
				context.addDataField("ACCT_NAME", (String)bodyKColl.getDataValue("ACCT_NAME"));
				context.addDataField("ACCT_TYPE", (String)bodyKColl.getDataValue("ACCT_TYPE"));
				context.addDataField("OPEN_ACCT_BRANCH_ID", (String)bodyKColl.getDataValue("OPEN_ACCT_BRANCH_ID"));
				context.addDataField("OPEN_ACCT_BRANCH_NAME", (String)bodyKColl.getDataValue("OPEN_ACCT_BRANCH_NAME"));
				context.addDataField("ORG_NO", (String)bodyKColl.getDataValue("ORG_NO"));
			}else {
				context.addDataField("flag", "failed");
				context.addDataField("msg", (String)retKColl.getDataValue("RET_MSG"));
				context.addDataField("ACCT_NO", (String)bodyKColl.getDataValue("ACCT_NO"));
				context.addDataField("ACCT_NAME", (String)bodyKColl.getDataValue("ACCT_NAME"));
				context.addDataField("ACCT_TYPE", (String)bodyKColl.getDataValue("ACCT_TYPE"));
				context.addDataField("OPEN_ACCT_BRANCH_ID", (String)bodyKColl.getDataValue("OPEN_ACCT_BRANCH_ID"));
				context.addDataField("OPEN_ACCT_BRANCH_NAME", (String)bodyKColl.getDataValue("OPEN_ACCT_BRANCH_NAME"));
				context.addDataField("ORG_NO", (String)bodyKColl.getDataValue("ORG_NO"));
			}
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
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
	 * 解析报文反馈信息是否正确，true表示交易成功，false表示失败
	 * @param returnKColl  反馈信息
	 * @param context 上下文
	 * @return boolean
	 * @throws EMPException
	 */
	private boolean haveSuccess(KeyedCollection returnKColl, Context context) throws EMPException{
		boolean flag  = true;
		if(returnKColl != null && returnKColl.size() > 0){
			String retStatus = (String)returnKColl.getDataValue("RET_STATUS");
			String retCode = (String)returnKColl.getDataValue("RET_CODE");
			if(!(retStatus.equals(TradeConstance.RETSTATUS1)&&retCode.equals(TradeConstance.RETCODE1))){
				flag = false;
			}
		}else {
			flag = false;
		}
		return flag;
	}
}