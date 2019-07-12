package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;
import java.util.Map;

import com.dc.eai.data.CompositeData;
import com.dc.eai.data.FieldType;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
/**
 * 保证金账户信息查询
 * @author Pansq
 */
public class QueryBailInfo extends TranClient {

	@Override
	public CompositeData doExecute(Context context, Connection connection)
			throws EMPException {
		/** 封装发送报文信息 */
		CompositeData reqCD= new CompositeData();
		try {
			String bail_acct_no = (String)context.getDataValue("bail_acct_no");
			String serviceCode = (String)context.getDataValue("service_code");
			String senceCode = (String)context.getDataValue("sence_code");
			
			ESBInterfacesImple esbInterfacesImple = (ESBInterfacesImple)CMISComponentFactory.getComponentFactoryInstance().getComponentInterface(TradeConstance.ESBINTERFACE, context, connection);
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			
			/** 系统头 */
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, senceCode, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			
			/** 封装报文体 */
			CompositeData bodyCD = new CompositeData();
			bodyCD.addField("GUARANTEE_ACCT_NO", TagUtil.getEMPField(bail_acct_no, FieldType.FIELD_STRING, 50, 0));
			reqCD.addStruct(TradeConstance.ESB_BODY, bodyCD);
			
			
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		return reqCD;
	}
	
	//XD150520037_信贷系统利率调整修改优化
	public void doSuccess(Context context, Connection connection) throws EMPException{
		
	}
}
