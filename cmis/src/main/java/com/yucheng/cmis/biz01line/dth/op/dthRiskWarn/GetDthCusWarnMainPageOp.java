package com.yucheng.cmis.biz01line.dth.op.dthRiskWarn;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;

public class GetDthCusWarnMainPageOp extends CMISOperation {
	
	/**
	 * 风险预警->客户预警
	 * @param context
	 * @throws EMPException
	 * @author GC.20131220
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String multiXMLOne = delLmtNums(context,"我行关联客户贷款","单位：户");	//关联客户
			String multiXMLTwo = delCusAgt(context,"对公客户日均存款(月)情况图","F");	//对公存款
			String multiXMLThree = delCusAgt(context,"个人客户日均存款(月)情况图","Z");	//个人存款
			
			context.addDataField("multiXMLOne", multiXMLOne);
			context.addDataField("multiXMLTwo", multiXMLTwo);
			context.addDataField("multiXMLThree", multiXMLThree);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)this.releaseConnection(context, connection);
		}
		return "0";
	}
	
	/**
	 * 我行关联客户贷款
	 */
	public String delLmtNums(Context context,String Caption,String Subcaption)	throws Exception {
		DthPubAction cmisOp = new DthPubAction();
		KeyedCollection styles = new KeyedCollection();
		
		/*** flash样式设置 ***/	
		styles.addDataField("Caption", Caption);
		styles.addDataField("Subcaption", Subcaption);

		/*** flash数据设置 ***/
		styles.addDataField("xName", "cnname");
		styles.addDataField("yName", "nums");
		styles.addDataField("jsValue", "enname");
		styles.addDataField("jsName", "getCusWarnDetails");
		
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("organNo", context.getDataValue("organNo"));
		kColl.addDataField("submitType", "getCusWarnChart");
		String singleXML = cmisOp.delSingleCharts(context, styles,kColl);
		
		return singleXML;
	}
	
	/**
	 * 存款信息处理
	 */
	public String delCusAgt(Context context,String Caption,String cus_type) throws Exception {
		DthPubAction cmisOp = new DthPubAction();
		KeyedCollection styles = new KeyedCollection();
		
		/*** flash样式设置 ***/
		styles.addDataField("Caption", Caption);
		styles.addDataField("Subcaption", "单位：户");
		styles.addDataField("OutCnvBaseFontSize", "12");

		/*** flash数据设置 ***/
		styles.addDataField("xName", "cnname");
		styles.addDataField("yName", "enname");
		styles.addDataField("jsValue", "ods_amt");
		styles.addDataField("jsName", "getCusAgtDetails"+cus_type);	
		
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("organNo", context.getDataValue("organNo"));
		kColl.addDataField("submitType", "getCusSaveChart"+cus_type);
		String singleXML = cmisOp.delSingleCharts(context, styles,kColl);
		return singleXML;
	}
	
}