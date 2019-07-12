package com.yucheng.cmis.biz01line.cus.op.conOtherSys;

import java.sql.Connection;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.client.ESBClient;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.TimeUtil;

public class SendCusMsg extends CMISOperation {

	public String doExecute(Context context) throws EMPException {

		CompositeData cd = null;
		Connection connection=null;
		String flagInfo = null;
		String cusId = "";
		String errorMsg = "";
		KeyedCollection kColl = null;
		StringBuffer sb = new StringBuffer();//记录日志信息
	try{
		connection =this.getConnection(context);
		//客户信息
		kColl = (KeyedCollection)context.getDataElement("CusBase");
		if(kColl==null||kColl.size()==0){
			throw new Exception("开户信息不完整！");
		}
		
		sb.append("-------------------------begin:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------\n");
		String cert_type = (String)kColl.getDataValue("cert_type");
		String cert_code = (String)kColl.getDataValue("cert_code");
		String cus_name = (String)kColl.getDataValue("cus_name");
		String cus_type = (String)kColl.getDataValue("cus_type");
		String cus_country = (String)kColl.getDataValue("cus_country");
		String sex = "";
		if(kColl.containsKey("indiv_sex")){
			sex = (String)kColl.getDataValue("indiv_sex");
		}
		
		if("20".equals(cert_type)||"a".equals(cert_type) || "b".equals(cert_type) || "X".equals(cert_type)){
			//组装报文头
			cd = EsbReportTool.produceHead("11002000019", "08", context);
			//BODY
			CompositeData body_struct = new CompositeData();
			CompositeData cbi_struct = new CompositeData();
			CompositeData cci_struct = new CompositeData();
			CompositeData cboi_struct = new CompositeData();
			CompositeData cgi_struct = new CompositeData();
			Array cgi_array = new Array();
			CompositeData ccni_struct = new CompositeData();
			Array ccni_array = new Array();
			CompositeData cbia_struct = new CompositeData();
			Array cbia_array = new Array();
			CompositeData ccei_struct = new CompositeData();
			
			//客户类型
			Field clientType = new Field(new FieldAttr(FieldType.FIELD_STRING, 6));
			clientType.setValue(cus_type);
			cbi_struct.addField("CLIENT_TYPE", clientType);
			body_struct.addStruct("CORP_BASE_INFO_STRUCT", cbi_struct);
			
			//成立日期
			Field creatDate = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
			creatDate.setValue("");
			cci_struct.addField("GLOBAL_ID", creatDate);
			body_struct.addStruct("CORP_CLIENT_INFO_STRUCT", cci_struct);
			
			//所属机构
			Field belongOrg = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			belongOrg.setValue("");
			cboi_struct.addField("CLIENT_BELONG_ORG", belongOrg);
			body_struct.addStruct("CLIENT_BELONG_ORG_INFO_STRUCT", cboi_struct);
			
			//证件类型、证件号码
			Field globalType = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
			globalType.setValue(cert_type);
			Field globalId = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			globalId.setValue(cert_code);
			cgi_struct.addField("GLOBAL_TYPE", globalType);
			cgi_struct.addField("GLOBAL_ID", globalId);
			cgi_array.addStruct(cgi_struct);
			body_struct.addArray("CLIENT_GLOBAL_INFO_ARRAY", cgi_array);
			
			//客户名称、客户名称类型
			Field clientName = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
			clientName.setValue(cus_name);
			Field clientNameType = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			clientNameType.setValue("12");
			ccni_struct.addField("CLIENT_NAME", clientName);
			ccni_struct.addField("CLIENT_NAME_TYPE", clientNameType);
			ccni_array.addStruct(ccni_struct);
			body_struct.addArray("CORP_CLIENT_NAME_INFO_ARRAY", ccni_array);
			
			//所属客户经理
			Field mng_id = new Field(new FieldAttr(FieldType.FIELD_STRING, 16));
			mng_id.setValue(context.getDataValue("currentUserId").toString());
			cbia_struct.addField("CUST_MANAGER_ID", mng_id);
			cbia_array.addStruct(cbia_struct);
			body_struct.addArray("CLIENT_BELONG_INFO_ARRAY", cbia_array);
			
			//国家代码
			Field countryCode = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
			countryCode.setValue(cus_country);
			ccei_struct.addField("COUNTRY_CODE", countryCode);
			body_struct.addStruct("CORP_CLIENT_EXT_INFO_STRUCT", ccei_struct);

			cd.addStruct("BODY", body_struct);
		}else{
			//组装报文头
			cd = EsbReportTool.produceHead("11002000019", "04", context);
			//BODY
			CompositeData body_struct = new CompositeData();
			CompositeData pbi_struct = new CompositeData();
			CompositeData pci_struct = new CompositeData();
			CompositeData cboi_struct = new CompositeData();
			CompositeData cgi_struct = new CompositeData();
			Array cgi_array = new Array();
			CompositeData pcni_struct = new CompositeData();
			Array pcni_array = new Array();
			CompositeData cbi_struct = new CompositeData();
			Array cbi_array = new Array();
			CompositeData pcei_struct = new CompositeData();
			
			//客户类型
			Field clientType = new Field(new FieldAttr(FieldType.FIELD_STRING, 6));
			clientType.setValue(cus_type);
			pbi_struct.addField("CLIENT_TYPE", clientType);
			body_struct.addStruct("PERSON_BASE_INFO_STRUCT", pbi_struct);
			
			//出生日期、性别
			Field birthDate = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
			birthDate.setValue("");
			pci_struct.addField("BIRTH_DATE", birthDate);
			Field sexDate = new Field(new FieldAttr(FieldType.FIELD_STRING, 8));
			sexDate.setValue(sex);
			pci_struct.addField("SEX", sexDate);
			body_struct.addStruct("PERSON_CLIENT_INFO_STRUCT", pci_struct);
			
			//所属机构
			Field belongOrg = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			belongOrg.setValue("");
			cboi_struct.addField("CLIENT_BELONG_ORG", belongOrg);
			body_struct.addStruct("CLIENT_BELONG_ORG_INFO_STRUCT", cboi_struct);
			
			//证件类型、证件号码
			Field globalType = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
			globalType.setValue(cert_type);
			Field globalId = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			globalId.setValue(cert_code);
			cgi_struct.addField("GLOBAL_TYPE", globalType);
			cgi_struct.addField("GLOBAL_ID", globalId);
			cgi_array.addStruct(cgi_struct);
			body_struct.addArray("CLIENT_GLOBAL_INFO_ARRAY", cgi_array);
			
			//客户名称、客户名称类型
			Field clientName = new Field(new FieldAttr(FieldType.FIELD_STRING, 150));
			clientName.setValue(cus_name);
			Field clientNameType = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			clientNameType.setValue("12");
			pcni_struct.addField("CLIENT_NAME", clientName);
			pcni_struct.addField("CLIENT_NAME_TYPE", clientNameType);
			pcni_array.addStruct(pcni_struct);
			body_struct.addArray("PERSON_CLIENT_NAME_INFO_ARRAY", pcni_array);
			
			//所属客户经理
			Field mng_id = new Field(new FieldAttr(FieldType.FIELD_STRING, 16));
			mng_id.setValue(context.getDataValue("currentUserId").toString());
			cbi_struct.addField("CUST_MANAGER_ID", mng_id);
			cbi_array.addStruct(cbi_struct);
			body_struct.addArray("CLIENT_BELONG_INFO_ARRAY", cbi_array);
			
			//国家代码
			Field countryCode = new Field(new FieldAttr(FieldType.FIELD_STRING, 3));
			countryCode.setValue(cus_country);
			pcei_struct.addField("COUNTRY_CITIZEN", countryCode);
			body_struct.addStruct("PERSON_CLIENT_EXT_INFO_STRUCT", pcei_struct);

			cd.addStruct("BODY", body_struct);
		}
		
		sb.append("**********************请求报文开始************************\n");
		sb.append(cd);//记录请求报文
		sb.append("**********************请求报文结束************************\n");
		CompositeData resp = ESBClient.request(cd);//发送报文
		sb.append("**********************接收报文开始************************\n");
		sb.append(resp);//记录返回报文
		sb.append("**********************接收报文结束************************\n");
		
		//判断交易是否成功
		String retStatus = resp.getStruct("SYS_HEAD").getField("RET_STATUS").strValue();
		String retCode = resp.getStruct("SYS_HEAD").getArray("RET").getStruct(0).getField("RET_CODE").strValue();
		if ("S".equals(retStatus) && "000000".equals(retCode)){
			CompositeData bodyCd = resp.getStruct("BODY");
			//客户码 
			cusId = bodyCd.getArray("CLIENT_GLOBAL_INFO_ARRAY").getStruct(0).getField("CLIENT_NO").strValue();
			flagInfo = PUBConstant.SUCCESS;
		}else if("F".equals(retStatus)){
			flagInfo = PUBConstant.FAIL;
			errorMsg = resp.getStruct("SYS_HEAD").getArray("RET").getStruct(0).getField("RET_MSG").strValue();//错误信息
		}
		sb.append("-------------------------end:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------");
		EMPLog.log("inReport", EMPLog.INFO, 0, sb.toString());//记录日志
		context.put("flag", flagInfo);
		context.put("cusId", cusId);
		context.put("errorMsg", errorMsg);

		}catch (EMPException ee) {
			sb.append("-----------------------异常：--end:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------");
			EMPLog.log("inReport", EMPLog.INFO, 0, sb.toString());//记录日志
			throw ee;
		} catch(Exception e){
			sb.append("-----------------------异常：--end:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------");
			EMPLog.log("inReport", EMPLog.INFO, 0, sb.toString());//记录日志
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return flagInfo;
	}
}
