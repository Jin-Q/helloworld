package com.yucheng.cmis.biz01line.dth.op.dthRiskWarn;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;

public class GetDthCostWarnTenPageOp extends CMISOperation {
	
	/**
	 * 风险预警->用信预警
	 * @param context
	 * @throws EMPException
	 * @author GC.20131225
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);

			String multiXMLEight = delCostWarnTenCus(context,"用信十大户","YX");	//用信十大户
			String multiXMLNine = delCostWarnTenCus(context,"贷款十大户","10");	//贷款十大户
			String multiXMLTen = delCostWarnTenCus(context,"银承十大户","20");	//银承十大户
			String multiXMLEleven = delCostWarnTenCus(context,"贴现十大户","30");	//贴现十大户
			String multiXMLTwelve = delCostWarnTenCus(context,"保函十大户","40");	//保函十大户
			String multiXMLThirteen = delCostWarnTenCus(context,"贸易融资十大户","50");	//贸易融资十大户
			String multiXMLFourteen = delCostWarnTenCus(context,"垫款十大户","DK");	//垫款十大户

			context.addDataField("multiXMLEight", multiXMLEight);
			context.addDataField("multiXMLNine", multiXMLNine);
			context.addDataField("multiXMLTen", multiXMLTen);
			context.addDataField("multiXMLEleven", multiXMLEleven);
			context.addDataField("multiXMLTwelve", multiXMLTwelve);
			context.addDataField("multiXMLThirteen", multiXMLThirteen);
			context.addDataField("multiXMLFourteen", multiXMLFourteen);
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
		
	/**
	 * 各种十大户处理
	 */
	public String delCostWarnTenCus(Context context,String Caption,String prd_type)throws Exception {
		DthPubAction cmisOp = new DthPubAction();
		KeyedCollection styles = new KeyedCollection();
		/*** flash样式设置 ***/
		styles.addDataField("Caption", Caption);
		styles.addDataField("Subcaption", "单位：百万元");
		styles.addDataField("OutCnvBaseFontSize", "12");
		styles.addDataField("BaseFontSize", "11");

		/*** flash数据设置 ***/
		styles.addDataField("xName", "cus_name");
		styles.addDataField("yName", "amt");
		styles.addDataField("jsValue", "");
		styles.addDataField("jsName", "");
		
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("organNo", context.getDataValue("organNo"));
		kColl.addDataField("submitType", "getCostWarnTenCus");
		if(prd_type.length() > 0){
			kColl.addDataField("prd_type", prd_type);
		}
		String singleXML = cmisOp.delSingleCharts(context, styles , kColl);

		return singleXML;
	}

}