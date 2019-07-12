package com.yucheng.cmis.biz01line.cus.op.conOtherSys;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.client.ESBClient;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.ESBUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.TimeUtil;
import com.yucheng.cmis.pub.util.TranslateDic;

public class GetCusFromEcif extends CMISOperation {

	public String doExecute(Context context) throws EMPException {

		String flagInfo = "";
		String errorMsg = "";
		CompositeData cd = null;
		Connection connection=null;
		StringBuffer sb = new StringBuffer();//记录日志信息
		sb.append("-------------------------begin:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------\n");
	try{
		connection =this.getConnection(context);
		//证件类型、证件号码
		String cert_type ="0"; //(String)context.getDataValue("certType");
		String cert_code = (String)context.getDataValue("certCode");
		
		if(cert_type==null || "".equals(cert_type) || cert_code==null || "".equals(cert_code)){
			throw new Exception("证件类型或证件号码不允许为空！");
		}
		KeyedCollection headKcoll = new KeyedCollection();
		KeyedCollection bodyColl = new KeyedCollection("body");
		if("20".equals(cert_type)||"a".equals(cert_type)){//对公
			//组装报文头
			headKcoll.put("SvcCd", "20130001");
			headKcoll.put("ScnCd", "01");
			headKcoll.addDataField("TxnMd","ONLINE");
			headKcoll.addDataField("UsrLngKnd","CHINESE");
			headKcoll.addDataField("jkType","cbs");
			//BODY
			CompositeData body_struct = new CompositeData();
			Field custNo = new Field(new FieldAttr(FieldType.FIELD_STRING, 20));
			custNo.setValue("");
			body_struct.addField("CLIENT_NO", custNo);
			
			Field custType = new Field(new FieldAttr(FieldType.FIELD_STRING, 6));
			custType.setValue("");
			body_struct.addField("CLIENT_TYPE", custType);
			
			//证件号码
			Field certNo = new Field(new FieldAttr(FieldType.FIELD_STRING, 30));
			certNo.setValue(cert_code);
			body_struct.addField("GLOBAL_ID", certNo);
			//证件类型
			Field certType = new Field(new FieldAttr(FieldType.FIELD_STRING, 5));
			certType.setValue(cert_type);
			body_struct.addField("GLOBAL_TYPE", certType);

			cd.addStruct("BODY", body_struct);
		}else{//对私
			//新，查询(20130001)客户信息查询(01)客户指定条件信息查询
			
			headKcoll.addDataField("SvcCd","20130001");
			headKcoll.addDataField("ScnCd","01");
			headKcoll.addDataField("TxnMd","ONLINE");
			headKcoll.addDataField("UsrLngKnd","CHINESE");
			headKcoll.addDataField("jkType","cbs");
		
			bodyColl.addDataField("QryInd","2");//2-按证件查询
			bodyColl.addDataField("AcctNoCrdNo",""); 
			bodyColl.addDataField("CstNo",""); 
			bodyColl.addDataField("IdentTp",cert_type); 
			bodyColl.addDataField("IdentNo",cert_code); 
			bodyColl.addDataField("CtcTelNo",""); 
			bodyColl.addDataField("TaskSrlNo",""); 
			//组装报文头
		
		}
		KeyedCollection retKColl = new KeyedCollection();
		KeyedCollection kColl = new KeyedCollection();
		retKColl = ESBUtil.sendEsbMsg(headKcoll,bodyColl);
		
		//判断交易是否成功
//		String retStatus = (String)((KeyedCollection) retKColl.getDataElement("SYS_HEAD")).getDataValue("F");
		 
//		if ("S".equals(retStatus) && "000000".equals(retStatus)){
			KeyedCollection bodyCd = (KeyedCollection) retKColl.getDataElement("BODY");
			
				 String cusType = "";
				kColl.addDataField("cusType", cusType);
				
				
				//发证日期 到期日期
     	    	String expiryDate = (String) bodyCd.getDataValue("LglPrsnLdrIdentExprtnDt");
				if(expiryDate!=null&&!"".equals(expiryDate.trim())){
					expiryDate = expiryDate.substring(0, 4) + "-" + expiryDate.substring(4, 6) + "-" + expiryDate.substring(6);
				}
				kColl.addDataField("expiryDate", expiryDate);
				
				kColl.addDataField("creatDate",  (String) bodyCd.getDataValue("CrtDt"));
				//国别
				String cusCountry =  (String) bodyCd.getDataValue("LglPrsnLdrIdentExprtnDt");
				kColl.addDataField("cusCountry", cusCountry);
		
			
				kColl.addDataField("cusName", (String) bodyCd.getDataValue("CstChinNm"));
				
				//客户码 客户类型
				String cusId = (String) bodyCd.getDataValue("CstNo");
				
				kColl.addDataField("cusId", cusId);
				kColl.addDataField("cusType", cusType);
				//发证日期 到期日期
//				String indivIdExpDt = bodyCd.getArray("CLIENT_GLOBAL_INFO_ARRAY").getStruct(0).getField("EXPIRY_DATE").strValue();
//				if(indivIdExpDt!=null&&!"".equals(indivIdExpDt.trim())){
//					indivIdExpDt = indivIdExpDt.substring(0, 4) + "-" + indivIdExpDt.substring(4, 6) + "-" + indivIdExpDt.substring(6);
//				}
//				kColl.addDataField("indivIdExpDt", indivIdExpDt);
//				//性别 出生日期
//				String sex = bodyCd.getStruct("PERSON_CLIENT_INFO_STRUCT").getField("SEX").strValue();
//				String birthDay = bodyCd.getStruct("PERSON_CLIENT_INFO_STRUCT").getField("BIRTH_DATE").strValue();
//				if(birthDay!=null&&!"".equals(birthDay.trim())){
//					birthDay = birthDay.substring(0, 4) + "-" + birthDay.substring(4, 6) + "-" + birthDay.substring(6);
//				}
//				kColl.addDataField("sex", sex);
//				kColl.addDataField("birthDay", birthDay); 
			flagInfo = PUBConstant.SUCCESS;
//		}else if("F".equals(retStatus)){//返回失败
//			flagInfo = PUBConstant.FAIL;
//			errorMsg = resp.getStruct("SYS_HEAD").getArray("RET").getStruct(0).getField("RET_MSG").strValue();//错误信息
//		}
		sb.append("-------------------------end:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------");
		EMPLog.log("inReport", EMPLog.INFO, 0, sb.toString());//记录日志
		kColl.put("flag", flagInfo);
		kColl.put("errorMsg", errorMsg);//返回错误信息
		IndexedCollection iColl=new IndexedCollection("cusList"); 
		iColl.addDataElement(kColl);
		this.putDataElement2Context(iColl, context);
		
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
