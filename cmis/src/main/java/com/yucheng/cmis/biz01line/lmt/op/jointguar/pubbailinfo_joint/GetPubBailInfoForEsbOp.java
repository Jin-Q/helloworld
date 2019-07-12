package com.yucheng.cmis.biz01line.lmt.op.jointguar.pubbailinfo_joint;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.esb.msi.ESBServiceInterface;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.ESBUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class GetPubBailInfoForEsbOp extends CMISOperation {

	private final String bail_acct_no_name = "bail_acct_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String bail_acct_no = null;
			String flag="success";
			String mes = "";
			try {
				bail_acct_no = (String)context.getDataValue(bail_acct_no_name);
			} catch (Exception e) {}
			if(bail_acct_no == null || bail_acct_no.length() == 0)
				throw new EMPJDBCException("The value of bail_acct_no cannot be null!");
			
			/*** 调用esb模块实时接口取交易明细 ***/
			/*CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ESBServiceInterface service = (ESBServiceInterface)serviceJndi.getModualServiceById("esbServices", "esb");
			KeyedCollection retKColl = null;
			KeyedCollection BODY = new KeyedCollection("BODY");
			try{
				retKColl = service.tradeBZJZH(bail_acct_no, context, connection);
				if(TagUtil.haveSuccess(retKColl, context)){//成功
					BODY = (KeyedCollection)retKColl.getDataElement("BODY");
					SInfoUtils.addSOrgName(BODY, new String[]{"OPEN_ACCT_BRANCH_ID"});
				}else{
					flag = "error";
					mes =(String)retKColl.getDataValue("RET_MSG");
				}
			}catch(Exception e){
				flag = "error";
				mes = "ESB通讯接口【获取保证金账户信息】交易失败："+e.getMessage();
			}*/
			
			KeyedCollection qryheadKcoll = new KeyedCollection();
			KeyedCollection qrybodyColl = new KeyedCollection("body");
			//组装报文头
			qryheadKcoll.put("SvcCd", "30130001");
			qryheadKcoll.put("ScnCd", "01");
			qryheadKcoll.addDataField("TxnMd","ONLINE");
			qryheadKcoll.addDataField("UsrLngKnd","CHINESE");
			qryheadKcoll.addDataField("jkType","cbs");
			//BODY
			qrybodyColl.addDataField("AcctNoCrdNo",bail_acct_no);//账号/卡号
			qrybodyColl.addDataField("PdTp",""); //产品类型
			qrybodyColl.addDataField("Ccy","CNY"); //币种
			qrybodyColl.addDataField("AcctSeqNo",""); //账户序号
			qrybodyColl.addDataField("PswdTp",""); //密码类型
			qrybodyColl.addDataField("AcctTp",""); //账户类型
			qrybodyColl.addDataField("AcctSt","");//账户状态 
			
			KeyedCollection retKColl = new KeyedCollection();
			KeyedCollection BODY = new KeyedCollection("BODY");
			try {
				retKColl = ESBUtil.sendEsbMsg(qryheadKcoll,qrybodyColl);
				KeyedCollection sysHead=(KeyedCollection)retKColl.getDataElement("SYS_HEAD");
				IndexedCollection retArr=(IndexedCollection)sysHead.getDataElement("RetInfArry");
				KeyedCollection retObj=(KeyedCollection)retArr.get(0);
				String retCd=(String)retObj.getDataValue("RetCd");
				if("000000".equals(retCd)){//成功
					BODY = (KeyedCollection)retKColl.getDataElement("BODY");
					SInfoUtils.addSOrgName(BODY, new String[]{"AcctBlngInstNo","TxnInstCd"});
				}else{
					flag = "error";
					mes =(String)retObj.getDataValue("RetInf");
				}
			} catch (Exception e) {
				// TODO: handle exception
				flag = "error";
				mes = "ESB通讯接口【获取保证金账户信息】交易失败："+e.getMessage();
			}
			context.addDataField("flag", flag);
			context.addDataField("mes", mes);
			putDataElement2Context(BODY, context);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
