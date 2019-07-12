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
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
/**
 * 账户信息查询
 * @author Pansq
 */
public class QueryAcctNo extends TranClient {

	@Override
	public CompositeData doExecute(Context context, Connection connection) throws EMPException {
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData();
		try {
			String acct_no = (String)context.getDataValue("acct_no");
		/*	String serviceCode = (String)context.getDataValue("service_code");
			String senceCode = (String)context.getDataValue("sence_code");
			
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			
			*//** 系统头 *//*
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, senceCode, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			*//** 应用头 *//*
			reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			
			*//** 封装报文体 *//*
			CompositeData bodyCD = new CompositeData();
			bodyCD.addField("ACCT_NO", TagUtil.getEMPField(acct_no, FieldType.FIELD_STRING, 32, 0));
			reqCD.addStruct(TradeConstance.ESB_BODY, bodyCD);*/
			/** 系统头 */
			//reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, TradeConstance.SERVICE_SCENE_DKFFSQ, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			CompositeData headKcoll= new CompositeData();
			headKcoll.addField("SvcCd", TagUtil.getEMPField("30130001", FieldType.FIELD_STRING, 30, 0));
			headKcoll.addField("ScnCd", TagUtil.getEMPField("01", FieldType.FIELD_STRING, 2, 0));
			headKcoll.addField("TxnMd", TagUtil.getEMPField("ONLINE", FieldType.FIELD_STRING, 6, 0));
			headKcoll.addField("UsrLngKnd", TagUtil.getEMPField("UsrLngKnd", FieldType.FIELD_STRING, 20, 0));
			headKcoll.addField("jkType", TagUtil.getEMPField("cbs", FieldType.FIELD_STRING, 5, 0));
			reqCD.addStruct("SYS_HEAD", headKcoll);
			/** 应用头 */
			//王小虎注释//reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					//王小虎注释//(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			
			/** 封装报文体 */
			
			CompositeData bodyKcoll = new CompositeData();
			
			bodyKcoll.addField("AcctNoCrdNo", TagUtil.getEMPField(acct_no, FieldType.FIELD_STRING, 20, 0));
			bodyKcoll.addField("PdTp", TagUtil.getEMPField("", FieldType.FIELD_STRING, 5, 0));
			bodyKcoll.addField("Ccy", TagUtil.getEMPField("CNY", FieldType.FIELD_STRING, 3, 0));
			bodyKcoll.addField("AcctSeqNo", TagUtil.getEMPField("", FieldType.FIELD_STRING, 10, 0));
			bodyKcoll.addField("PswdTp", TagUtil.getEMPField("", FieldType.FIELD_STRING, 2, 0));
			bodyKcoll.addField("AcctTp", TagUtil.getEMPField("", FieldType.FIELD_STRING, 2, 0));
			bodyKcoll.addField("AcctSt", TagUtil.getEMPField("", FieldType.FIELD_STRING, 2, 0));
		
			reqCD.addStruct("BODY", bodyKcoll);
			
			
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		return reqCD;
	}

	//XD150520037_信贷系统利率调整修改优化
	public void doSuccess(Context context, Connection connection) throws EMPException{
		
	}
}
