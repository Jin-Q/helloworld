package com.yucheng.cmis.biz01line.dth.op.dthRiskWarn;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;

public class GetDthLmtWarnMainPageOp extends CMISOperation {

	/**
	 * 风险预警->授信预警
	 * @param context
	 * @throws EMPException
	 * @author GC.20131224
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String multiXMLOne = delLmtNums(context,"nums","授信用户数","单位：户");	//授信用户数
			String multiXMLTwo = delLmtNums(context,"amts","授信金额","单位：百万元");	//授信金额数
			String multiXMLThree = delLmtRate(context,"nums","授信用户比例","单位：户");	//授信用户比例
			String multiXMLFour = delLmtRate(context,"amts","授信金额比例","单位：百万元");	//授信金额比例
			String multiXMLFive = delLmtAndGrt(context);	//同时为授信及担保户
			String multiXMLSix = delLmtTenCus(context,"总授信十大户","Six","");
			String multiXMLSeven = delLmtTenCus(context,"集团十大户","Seven","");/*** 这里取的是：属于集团客户的总授信金额前十 ***/
			String multiXMLEight = delLmtTenCus(context,"质押十大户","Eight","200");
			String multiXMLNine = delLmtTenCus(context,"抵押十大户","Nine","100");
			String multiXMLTen = delLmtTenCus(context,"保证十大户","Ten","300");
			String multiXMLEleven = delLmtTenCus(context,"信用十大户","Eleven","400");
			
			context.addDataField("multiXMLOne", multiXMLOne);
			context.addDataField("multiXMLTwo", multiXMLTwo);
			context.addDataField("multiXMLThree", multiXMLThree);
			context.addDataField("multiXMLFour", multiXMLFour);
			context.addDataField("multiXMLFive", multiXMLFive);
			context.addDataField("multiXMLSix", multiXMLSix);
			context.addDataField("multiXMLSeven", multiXMLSeven);
			context.addDataField("multiXMLEight", multiXMLEight);
			context.addDataField("multiXMLNine", multiXMLNine);
			context.addDataField("multiXMLTen", multiXMLTen);
			context.addDataField("multiXMLEleven", multiXMLEleven);			
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
	 * 授信户数，金额处理
	 */
	public String delLmtNums(Context context,String value,String Caption,String Subcaption)	throws Exception {
		DthPubAction cmisOp = new DthPubAction();
		KeyedCollection styles = new KeyedCollection();
		/*** flash样式设置 ***/
		styles.addDataField("Caption", Caption);
		styles.addDataField("Subcaption", Subcaption);
		
		/*** flash数据设置 ***/
		styles.addDataField("xName", "guar_cnname");
		styles.addDataField("yName", value);
		styles.addDataField("jsName", "getLmtWarnChartOnetwo");
		styles.addDataField("showCnname", "line_cnname");
		styles.addDataField("jsValue_one", "line_enname");
		styles.addDataField("jsValue_two", "guar_enname");
		String showName[]= "公司业务条线,小微业务条线,个人业务条线".split(",");
		
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("organNo", context.getDataValue("organNo"));
		kColl.addDataField("submitType", "getLmtWarnChartOnetwo");
		String multiXML = cmisOp.delMultiCharts(context, styles, showName,kColl);
		
		return multiXML;
	}
	
	/**
	 * 授信户数，金额比例
	 */
	public String delLmtRate(Context context,String value,String Caption,String Subcaption)	throws Exception {
		DthPubAction cmisOp = new DthPubAction();
		KeyedCollection styles = new KeyedCollection();
		/*** flash样式设置 ***/
		styles.addDataField("Caption", Caption);
		styles.addDataField("Subcaption", Subcaption);
		styles.addDataField("BaseFontSize", "15");
		/*** flash数据设置 ***/
		styles.addDataField("xName", "line_cnname");
		styles.addDataField("yName", value);
		styles.addDataField("jsValue", "line_enname");
		styles.addDataField("jsName", "getLmtWarnChartThreefour");
		
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("organNo", context.getDataValue("organNo"));
		kColl.addDataField("submitType", "getLmtWarnChartThreefour");
		String singleXML = cmisOp.delSingleCharts(context, styles,kColl);

		return singleXML;
	}
	
	/**
	 * 同时为授信及担保户
	 */
	public String delLmtAndGrt(Context context)	throws Exception {
		DthPubAction cmisOp = new DthPubAction();
		KeyedCollection styles = new KeyedCollection();
		/*** flash样式设置 ***/
		styles.addDataField("Caption", "既是我行授信户又是担保户的客户");
		styles.addDataField("Subcaption", "单位：户");

		/*** flash数据设置 ***/
		styles.addDataField("xName", "line_cnname");
		styles.addDataField("yName", "nums");
		styles.addDataField("jsValue", "line_enname");
		styles.addDataField("jsName", "getLmtWarnDetailFive");
		
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("organNo", context.getDataValue("organNo"));
		kColl.addDataField("submitType", "getLmtWarnChartFive");
		String singleXML = cmisOp.delSingleCharts(context, styles,kColl);

		return singleXML;
	}
	
	/**
	 * 各种十大户处理
	 */
	public String delLmtTenCus(Context context,String Caption,String nums, String guar_type)	throws Exception {
		DthPubAction cmisOp = new DthPubAction();
		KeyedCollection styles = new KeyedCollection();
		/*** flash样式设置 ***/
		styles.addDataField("Caption", Caption);
		styles.addDataField("Subcaption", "单位：百万元");
		styles.addDataField("OutCnvBaseFontSize", "12");
		styles.addDataField("BaseFontSize", "11");
		
		/*** flash数据设置 ***/
		styles.addDataField("xName", "cus_name");
		styles.addDataField("yName", "lmt_amt");
		styles.addDataField("jsValue", "");
		styles.addDataField("jsName", "");
		
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("organNo", context.getDataValue("organNo"));
		kColl.addDataField("submitType", "getLmtWarnChart"+nums);
		if(guar_type.length() > 0){
			kColl.addDataField("guar_type", guar_type);
		}
		String singleXML = cmisOp.delSingleCharts(context, styles , kColl);

		return singleXML;
	}
	
}