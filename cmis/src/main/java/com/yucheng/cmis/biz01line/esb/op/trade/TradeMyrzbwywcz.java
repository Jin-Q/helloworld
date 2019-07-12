package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;
import java.util.Map;

import com.dc.eai.data.CompositeData;
import com.dc.eai.data.FieldType;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.pub.CMISComponentFactory;
/**
 * 贸易融资表外业务出账授权交易
 * @author liqh
 *
 */
public class TradeMyrzbwywcz extends TranClient {
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
			reqCD.addStruct("SYS_HEAD", esbInterfacesImple.getSysHeadCD(serviceCode, senceCode, TradeConstance.CONSUMERID, serno,(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID),(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM, context));
			/** 应用头 */
			reqCD.addStruct("APP_HEAD", esbInterfacesImple.getAppHeadCD((String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID), 
					(String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERNAME), (String)context.getDataValue(CMISConstance.ATTR_ORGID), TradeConstance.TRADE_SYSTEM));
			
			/** 封装报文体 */
			KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl);
			CompositeData bodyCD = new CompositeData();
			bodyCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 50, 0));
			bodyCD.addField("CONTRACT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CONTRACT_NO"), FieldType.FIELD_STRING, 60, 0));
			bodyCD.addField("CLIENT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CLIENT_NO"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("INCOME_ORG_NO", TagUtil.getEMPField(reflectKColl.getDataValue("INCOME_ORG_NO"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("REGIST_ORG_NO", TagUtil.getEMPField(reflectKColl.getDataValue("REGIST_ORG_NO"), FieldType.FIELD_STRING, 20, 0));
			bodyCD.addField("TRAN_AMT", TagUtil.getEMPField(reflectKColl.getDataValue("TRAN_AMT"), FieldType.FIELD_DOUBLE, 20, 2));
			bodyCD.addField("TRAN_CCY", TagUtil.getEMPField(reflectKColl.getDataValue("TRAN_CCY"), FieldType.FIELD_STRING, 3, 0));
			bodyCD.addField("GUARANTEE_PER", TagUtil.getEMPField(reflectKColl.getDataValue("GUARANTEE_PER"), FieldType.FIELD_DOUBLE, 20, 7));
			bodyCD.addField("GUARANTEE_ACCT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("GUARANTEE_ACCT_NO"), FieldType.FIELD_STRING, 50, 0));
			bodyCD.addField("GUARANTEE_AMT", TagUtil.getEMPField(reflectKColl.getDataValue("GUARANTEE_AMT"), FieldType.FIELD_DOUBLE, 20, 2));
			bodyCD.addField("GUARANTEE_CCY", TagUtil.getEMPField(reflectKColl.getDataValue("GUARANTEE_CCY"), FieldType.FIELD_STRING, 3, 0));
			bodyCD.addField("LC_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("LC_TYPE"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("IS_CYCLE_FLAG", TagUtil.getEMPField(reflectKColl.getDataValue("IS_CYCLE_FLAG"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("DR_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("DR_DATE"), FieldType.FIELD_STRING, 8, 0));
			bodyCD.addField("VALUE_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("VALUE_DATE"), FieldType.FIELD_STRING, 8, 0));
			bodyCD.addField("EFF_PERIOD", TagUtil.getEMPField(reflectKColl.getDataValue("EFF_PERIOD"), FieldType.FIELD_STRING, 8, 0));
			bodyCD.addField("TERM", TagUtil.getEMPField(reflectKColl.getDataValue("TERM"), FieldType.FIELD_STRING, 10, 0));
			bodyCD.addField("LC_OVERFLOW_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("LC_OVERFLOW_RATE"), FieldType.FIELD_DOUBLE, 20, 2));
			bodyCD.addField("LC_REDUCE_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("LC_REDUCE_RATE"), FieldType.FIELD_DOUBLE, 20, 2));
			bodyCD.addField("LC_NO", TagUtil.getEMPField(reflectKColl.getDataValue("LC_NO"), FieldType.FIELD_STRING, 20, 0));
			bodyCD.addField("SETTLE_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("SETTLE_MODE"), FieldType.FIELD_STRING, 42, 0));
			bodyCD.addField("NORMAL_INTEREST", TagUtil.getEMPField(reflectKColl.getDataValue("NORMAL_INTEREST"), FieldType.FIELD_DOUBLE, 20, 2));
			bodyCD.addField("OD_INT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("OD_INT_RATE"), FieldType.FIELD_DOUBLE, 20, 7));
			bodyCD.addField("BUSS_KIND", TagUtil.getEMPField(reflectKColl.getDataValue("BUSS_KIND"), FieldType.FIELD_STRING, 30, 0));
			bodyCD.addField("BAK_FIELD", TagUtil.getEMPField(reflectKColl.getDataValue("BAK_FIELD"), FieldType.FIELD_STRING, 300, 0));
			bodyCD.addField("BAK_FIELD2", TagUtil.getEMPField(reflectKColl.getDataValue("BAK_FIELD2"), FieldType.FIELD_STRING, 300, 0));
			reqCD.addStruct("BODY", bodyCD);
		}catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		return reqCD;
	}

	//XD150520037_信贷系统利率调整修改优化
	public void doSuccess(Context context, Connection connection) throws EMPException{
		
	}
}
