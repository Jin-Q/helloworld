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
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.biz01line.esb.pub.TradeConstance;
import com.yucheng.cmis.pub.CMISComponentFactory;
/**
 * 保证金出账授权交易
 * @author liqh
 * 
 */
public class TradeBailChange extends TranClient {
	private static final String AUTHMODEL = "PvpAuthorize";
	private static final String PVPSUBMODEL = "PvpAuthorizeSub";
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
			reqCD.addStruct("BODY", bodyCD);
			/** 保证金授权信息 */
			IndexedCollection zhIColl = dao.queryList(PVPSUBMODEL, " where auth_no ='"+authKColl.getDataValue("authorize_no")+"' and busi_cls ='"+TradeConstance.BUSI_TYPE_ZH+"' ", connection);
			if(zhIColl != null && zhIColl.size() > 0){
				Array zharray = new Array(); 
				for(int i=0;i<zhIColl.size();i++){
					CompositeData zhCD = new CompositeData();
					KeyedCollection zhKColl = (KeyedCollection)zhIColl.get(i);
					KeyedCollection reflectSubKColl = TagUtil.getReflectKColl(zhKColl);
					zhCD.addField("GEN_GL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("GEN_GL_NO"), FieldType.FIELD_STRING, 30, 0));
					zhCD.addField("DUEBILL_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("DUEBILL_NO"), FieldType.FIELD_STRING, 50, 0));
					zhCD.addField("BRANCH_ID", TagUtil.getEMPField(reflectSubKColl.getDataValue("BRANCH_ID"), FieldType.FIELD_STRING, 20, 0));
					zhCD.addField("GUARANTEE_ACCT_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_ACCT_NO"), FieldType.FIELD_STRING, 50, 0));
					zhCD.addField("GUARANTEE_NO", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_NO"), FieldType.FIELD_STRING, 50, 0));
					zhCD.addField("ACCT_TYPE", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_TYPE"), FieldType.FIELD_STRING, 6, 0));
					zhCD.addField("GUARANTEE_EXPIRY_DATE", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_EXPIRY_DATE"), FieldType.FIELD_STRING, 8, 0));
					zhCD.addField("ACCT_CODE", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_CODE"), FieldType.FIELD_STRING, 10, 0));
					zhCD.addField("CA_TT_FLAG", TagUtil.getEMPField(reflectSubKColl.getDataValue("CA_TT_FLAG"), FieldType.FIELD_STRING, 2, 0));
					zhCD.addField("ACCT_GL_CODE", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_GL_CODE"), FieldType.FIELD_STRING, 10, 0));
					zhCD.addField("ACCT_NAME", TagUtil.getEMPField(reflectSubKColl.getDataValue("ACCT_NAME"), FieldType.FIELD_STRING, 150, 0));
					zhCD.addField("CCY", TagUtil.getEMPField(reflectSubKColl.getDataValue("CCY"), FieldType.FIELD_STRING, 3, 0));
					zhCD.addField("GUARANTEE_PER", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_PER"), FieldType.FIELD_DOUBLE, 20, 7));
					zhCD.addField("GUARANTEE_AMT", TagUtil.getEMPField(reflectSubKColl.getDataValue("GUARANTEE_AMT"), FieldType.FIELD_DOUBLE, 20, 2));
					zharray.addStruct(zhCD);
				}
				bodyCD.addArray("GUARANTEE_AUTH_INFO_ARRAY", zharray);
			}
		}catch (Exception e) {
			throw new EMPException(e.getMessage());
		}
		return reqCD;
	}
	
	//XD150520037_信贷系统利率调整修改优化
	public void doSuccess(Context context, Connection connection) throws EMPException{
		
	}
}
