package com.yucheng.cmis.biz01line.dth.op.dthRiskWarn;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;

public class GetDthCcrWarnMainPageOp extends CMISOperation {

	/**
	 * 风险预警->评级预警
	 * @param context
	 * @throws EMPException
	 * @author GC.20131224
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String multiXML = delCcrWarnChart(context);
			String multiXMLTwo = delCcrWarnChartTwo(context);
			
			context.addDataField("multiXML", multiXML);
			context.addDataField("multiXMLTwo", multiXMLTwo);
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
	 * 信用等级户数处理 
	 */
	public String delCcrWarnChart(Context context) throws Exception {		
		DthPubAction cmisOp = new DthPubAction();
		KeyedCollection styles = new KeyedCollection();
		/*** flash样式设置 ***/
		styles.addDataField("Caption", "信用等级户数");
		styles.addDataField("Subcaption", "单位：户");

		/*** flash数据设置 ***/
		styles.addDataField("xName", "grade_cnname");
		styles.addDataField("yName", "nums");
		styles.addDataField("jsName", "getCcrWarnDetails");
		styles.addDataField("showCnname", "line_cnname");
		styles.addDataField("jsValue_one", "line_enname");
		styles.addDataField("jsValue_two", "grade_enname");
		String showName[]= "公司业务条线,小微业务条线,个人业务条线".split(",");
		
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("organNo", context.getDataValue("organNo"));
		kColl.addDataField("submitType", "getCcrWarnChart");
		String multiXML = cmisOp.delMultiCharts(context, styles, showName,kColl);
		
		return multiXML;
	}
	
	/**
	 * 评级到期比例处理
	 */
	public String delCcrWarnChartTwo(Context context) throws Exception {
		
		DthPubAction cmisOp = new DthPubAction();
		KeyedCollection styles = new KeyedCollection();
		/*** flash样式设置 ***/
		styles.addDataField("Caption", "评级到期比例");
		styles.addDataField("Subcaption", "");
		styles.addDataField("BaseFontSize", "15");
		/*** flash数据设置 ***/
		styles.addDataField("xName", "over_name");
		styles.addDataField("yName", "nums");
		styles.addDataField("jsValue", "over_status");
		styles.addDataField("jsName", "getCcrWarnDetailsTwo");
		
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("organNo", context.getDataValue("organNo"));
		kColl.addDataField("submitType", "getCcrWarnChartTwo");
		String singleXML = cmisOp.delSingleCharts(context, styles,kColl);

		return singleXML;
	}
}