package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;
import java.util.HashMap;
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
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 利率调整授权提交，成功后直接处理利率，不用再在柜面做了
 * XD150520037_信贷系统利率调整修改优化
 * @author zhaozq
 *
 */
public class TradeLltzsq extends TranClient {
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
		CompositeData bodyArrCD = new CompositeData();
		bodyArrCD.addField("GEN_GL_NO", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("GEN_GL_NO")), FieldType.FIELD_STRING, 30, 0));
		bodyArrCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 30, 0));
//		bodyArrCD.addField("CLIENT_NO", TagUtil.getEMPField(reflectKColl.getDataValue("CLIENT_NO"), FieldType.FIELD_STRING, 30, 0));
//		bodyArrCD.addField("CCY", TagUtil.getEMPField(reflectKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 3, 0));
//		bodyArrCD.addField("LOAN_AMT", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("LOAN_AMT")), FieldType.FIELD_DOUBLE, 20, 2));
//		bodyArrCD.addField("LOAN_BALANCE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("LOAN_BALANCE")), FieldType.FIELD_DOUBLE, 20, 2));
//		bodyArrCD.addField("DRAW_DOWN_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("DRAW_DOWN_DATE"), FieldType.FIELD_STRING, 8, 0));
//		bodyArrCD.addField("EXPIRY_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("EXPIRY_DATE"), FieldType.FIELD_STRING, 8, 0));
//		bodyArrCD.addField("INT_ACCORD_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("INT_ACCORD_TYPE"), FieldType.FIELD_STRING, 5, 0));
//		bodyArrCD.addField("INT_KIND", TagUtil.getEMPField(reflectKColl.getDataValue("INT_KIND"), FieldType.FIELD_STRING, 15, 0));
//		bodyArrCD.addField("BASE_INT_RATE", TagUtil.getEMPField(reflectKColl.getDataValue("BASE_INT_RATE"), FieldType.FIELD_DOUBLE, 20, 7));
//		bodyArrCD.addField("FLUCTUATION_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("FLUCTUATION_MODE"), FieldType.FIELD_STRING, 8, 0));
//		bodyArrCD.addField("INTEST_RATE_FLUCT_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("INTEST_RATE_FLUCT_MODE"), FieldType.FIELD_STRING, 10, 0));
		bodyArrCD.addField("INT_RATE_FLT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("INT_RATE_FLT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
		bodyArrCD.addField("INT_RATE_FLOW_SPREAD", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("INT_RATE_FLOW_SPREAD")), FieldType.FIELD_DOUBLE, 20, 7));
//		bodyArrCD.addField("ADVANCE_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("ADVANCE_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
		bodyArrCD.addField("ACT_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("ACT_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
//		bodyArrCD.addField("OVERDUE_FLT_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("OVERDUE_FLT_MODE"), FieldType.FIELD_STRING, 5, 0));
		bodyArrCD.addField("OVERDUE_FLT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("OVERDUE_FLT_RATE")), FieldType.FIELD_DOUBLE, 20, 9));
		bodyArrCD.addField("OVERDUE_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("OVERDUE_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
//		bodyArrCD.addField("PENALTY_INT_FLT_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("PENALTY_INT_FLT_MODE"), FieldType.FIELD_STRING, 5, 0));
		bodyArrCD.addField("PNY_INT_FLT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("PNY_INT_FLT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
//		bodyArrCD.addField("PNY_RATE_FLOW_SPREAD", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("PNY_RATE_FLOW_SPREAD")), FieldType.FIELD_DOUBLE, 20, 7));
		bodyArrCD.addField("PENALTY_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("PENALTY_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
//		bodyArrCD.addField("BASE_INT_RATE_CODE", TagUtil.getEMPField(reflectKColl.getDataValue("BASE_INT_RATE_CODE"), FieldType.FIELD_STRING, 10, 0));
//		bodyArrCD.addField("NEW_BASE_RATE_CODE", TagUtil.getEMPField(reflectKColl.getDataValue("NEW_BASE_RATE_CODE"), FieldType.FIELD_STRING, 10, 0));
//		bodyArrCD.addField("NEW_INT_ACCORD_TYPE", TagUtil.getEMPField(reflectKColl.getDataValue("NEW_INT_ACCORD_TYPE"), FieldType.FIELD_STRING, 5, 0));
//		bodyArrCD.addField("NEW_INT_KIND", TagUtil.getEMPField(reflectKColl.getDataValue("NEW_INT_KIND"), FieldType.FIELD_STRING, 15, 0));
//		bodyArrCD.addField("NEW_BASE_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_BASE_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
//		bodyArrCD.addField("NEW_ADV_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_ADV_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
//		bodyArrCD.addField("NEW_FLT_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("NEW_FLT_MODE"), FieldType.FIELD_STRING, 8, 0));
//		bodyArrCD.addField("NEW_RATE_FLUCT_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("NEW_RATE_FLUCT_MODE"), FieldType.FIELD_STRING, 10, 0));
//		bodyArrCD.addField("NEW_OVERDUE_FLT_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("NEW_OVERDUE_FLT_MODE"), FieldType.FIELD_STRING, 5, 0));
//		bodyArrCD.addField("NEW_PNY_INT_FLT_MODE", TagUtil.getEMPField(reflectKColl.getDataValue("NEW_PNY_INT_FLT_MODE"), FieldType.FIELD_STRING, 5, 0));
		bodyArrCD.addField("NEW_PNY_INT_FLT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_PNY_INT_FLT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
//		bodyArrCD.addField("NEW_PNY_FLOW_SPREAD", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_PNY_FLOW_SPREAD")), FieldType.FIELD_DOUBLE, 20, 7));
		bodyArrCD.addField("NEW_PNT_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_PNT_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
		bodyArrCD.addField("NEW_INT_RATE_FLT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_INT_RATE_FLT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
		bodyArrCD.addField("NEW_INT_FLOW_SPREAD", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_INT_FLOW_SPREAD")), FieldType.FIELD_DOUBLE, 20, 7));
		bodyArrCD.addField("NEW_ACT_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_ACT_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
		bodyArrCD.addField("NEW_OVERDUE_FLT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_OVERDUE_FLT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
		bodyArrCD.addField("NEW_OVERDUE_INT_RATE", TagUtil.getEMPField(TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_OVERDUE_INT_RATE")), FieldType.FIELD_DOUBLE, 20, 7));
//		bodyArrCD.addField("NEW_INT_EFF_DATE", TagUtil.getEMPField(reflectKColl.getDataValue("NEW_INT_EFF_DATE"), FieldType.FIELD_STRING, 8, 0));
		bodyArrCD.addField("FLUCTUATION_MODE", TagUtil.getEMPField(TagUtil.replaceNull4String(reflectKColl.getDataValue("NEW_FLT_MODE")), FieldType.FIELD_STRING, 8, 0));
		
		reqCD.addStruct("BODY", bodyArrCD);
		
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		return reqCD;
	}
	
	public void doSuccess(Context context, Connection connection) throws EMPException{
		try{
			String tran_serno = (String)context.getDataValue("tran_serno");
			//获取dao
			TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
			/** 通过交易码判断所需执行的交易，以及需要准备的交易数据 */
			KeyedCollection authKColl = dao.queryDetail(AUTHMODEL, tran_serno, connection);
			String authorize_no = (String) authKColl.getDataValue("authorize_no");
			KeyedCollection reflectKColl = TagUtil.getReflectKColl(authKColl);
			//成功后更新借据表
			KeyedCollection iqpkColl = (KeyedCollection)SqlClient.queryFirst("queryRateAppByAuthorizeNo", authorize_no, null, connection);
			Map value = new HashMap();
			value.put("ruling_ir", iqpkColl.getDataValue("ruling_ir"));
			value.put("ir_float_rate", TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_INT_RATE_FLT_RATE")));
			value.put("ir_float_point", TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_INT_FLOW_SPREAD")));
			value.put("reality_ir_y", TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_ACT_INT_RATE")));
			value.put("overdue_rate_y", TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_OVERDUE_INT_RATE")));
			value.put("default_rate_y", TagUtil.replaceNull4Double(reflectKColl.getDataValue("NEW_OVERDUE_INT_RATE")));
			SqlClient.update("updateAccRateInfo", authKColl.getDataValue("bill_no"), value, null, connection);
			
			//根据授权编号更新授权表的状态为授权已确认
			SqlClient.update("updatePvpAuthorizeStatusByGenNo", authorize_no, "04", null, connection);
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
	}
}
