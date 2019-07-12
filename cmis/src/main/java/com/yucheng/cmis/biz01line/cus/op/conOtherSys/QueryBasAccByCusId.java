package com.yucheng.cmis.biz01line.cus.op.conOtherSys;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.client.ESBClient;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.ESBUtil;
import com.yucheng.cmis.pub.util.NewStringUtils;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.TimeUtil;

public class QueryBasAccByCusId extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
 
		CompositeData cd = null;
		Connection connection=null;
		String certCode = "";
		String certType = "";
		StringBuffer sb = new StringBuffer();//记录日志信息
	try{
		connection =this.getConnection(context);
		//客户信息
		certCode = (String)context.getDataValue("cert_code");
		certType = (String)context.getDataValue("cert_type");
		if((certCode==null||"".equals(certCode))&&(certType==null||"".equals(certType))){
			throw new Exception("证件类型证件号为空！");
		}
		TableModelDAO dao = this.getTableModelDAO(context);
		
		sb.append("-------------------------begin:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------\n");
	 
	 
		
		KeyedCollection qryheadKcoll = new KeyedCollection();
		KeyedCollection qrybodyColl = new KeyedCollection("body"); 
		//组装报文头
		qryheadKcoll.put("SvcCd", "30130001");
		qryheadKcoll.put("ScnCd", "02");
		qryheadKcoll.addDataField("TxnMd","ONLINE");
		qryheadKcoll.addDataField("UsrLngKnd","CHINESE");
		qryheadKcoll.addDataField("jkType","cbs");
		//BODY 
		qrybodyColl.addDataField("CstNo",""); 
		qrybodyColl.addDataField("IdentNo",certCode); 
		qrybodyColl.addDataField("IdentTp",getCertType(certType)); 
		
		qrybodyColl.addDataField("IssuCtyCd",""); 
		qrybodyColl.addDataField("TopCstFlg","Y");  
		
		KeyedCollection kColl = new KeyedCollection();
		KeyedCollection retKColl = new KeyedCollection(); 
		retKColl = ESBUtil.sendEsbMsg(qryheadKcoll,qrybodyColl); 
		KeyedCollection sysHead=(KeyedCollection)retKColl.getDataElement("SYS_HEAD");
		IndexedCollection retArr=(IndexedCollection)sysHead.getDataElement("RetInfArry");
		KeyedCollection retObj=(KeyedCollection)retArr.get(0);
		String retCode=(String)retObj.getDataValue("RetCd");
		if(!"000000".equals(retCode)){
			context.put("msg",(String)retObj.getDataValue("RetInf")); 
		}else{
		 
			KeyedCollection body = (KeyedCollection)retKColl.getDataElement("BODY");
			IndexedCollection acctDtlInfArry=(IndexedCollection)body.getDataElement("AcctDtlInfArry");
			//客户码
		if(acctDtlInfArry!=null&&acctDtlInfArry.size()>0){
			
			for(int i=0;i<acctDtlInfArry.size();i++){
				KeyedCollection acctDtlInfArryColl = (KeyedCollection)acctDtlInfArry.get(i);
				if("4".equals((String)acctDtlInfArryColl.getDataValue("AcctAttr"))){
					String bas_acc_no = (String) ((KeyedCollection)acctDtlInfArry.get(0)).getDataValue("AcctNo");
					String bas_acc_date = (String) ((KeyedCollection)acctDtlInfArry.get(0)).getDataValue("OpnAcctDt");
					String bas_acc_bank = (String) ((KeyedCollection)acctDtlInfArry.get(0)).getDataValue("AcctBlngInstNo");
					if(bas_acc_date!=null&&!"".equals(bas_acc_date)){
						bas_acc_date = bas_acc_date.substring(0, 4) + "-" + bas_acc_date.substring(4, 6) + "-" + bas_acc_date.substring(6);
					} 
					kColl.addDataField("bas_acc_no", bas_acc_no);
					kColl.addDataField("bas_acc_date", bas_acc_date);
					kColl.addDataField("bas_acc_bank", bas_acc_bank); 
					kColl.addDataField("flag", "success"); 
					SInfoUtils.addSOrgName(kColl, new String[]{"bas_acc_bank"});//裕民银行的机构那张prdbank表没有  
					
					}
				}   
		  }else{ 
			kColl.addDataField("flag","notexists");
		  }
		context.put("msg","");
		}
		sb.append("-------------------------end:"+TimeUtil.getDateTime("yyyy-MM-dd HH:mm:ss")+"-------------------------------");
		EMPLog.log("inReport", EMPLog.INFO, 0, sb.toString());//记录日志
		IndexedCollection iColl = new IndexedCollection();
		iColl.addDataElement(kColl);
		iColl.setName("accList");
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
		return "0";
	}
	
	public String getCertType(String oldCertType){
		Map<String, String> tCertType=new HashMap<String, String>();
		tCertType.put("0", "101");
		tCertType.put("6", "116");
		tCertType.put("7", "102");
		tCertType.put("8", "117");
		tCertType.put("9", "121");
		tCertType.put("a", "231");
		tCertType.put("b", "211");
		tCertType.put("1", "104");
		tCertType.put("2", "103");
		tCertType.put("3", "107");
		tCertType.put("4", "113");
		tCertType.put("c", "120");
		tCertType.put("X", "232");
		tCertType.put("d", "122");
		tCertType.put("e", "123");
		tCertType.put("f", "124");
		tCertType.put("5", "125");
		tCertType.put("99", "120");
		tCertType.put("20", "231");
		return tCertType.get(oldCertType);
	}
}
