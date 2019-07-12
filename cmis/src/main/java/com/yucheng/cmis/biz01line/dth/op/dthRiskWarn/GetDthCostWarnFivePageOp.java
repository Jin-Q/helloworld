package com.yucheng.cmis.biz01line.dth.op.dthRiskWarn;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;

public class GetDthCostWarnFivePageOp extends CMISOperation {
	
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
			
			String multiXMLFour = delCostWarnFiveclass(context, "全行五级分类下业务比例", "BLall");	//全行五级分类下业务比例
			String multiXMLFive = delCostWarnFiveclass(context, "公司五级分类下业务比例", "BL100");	//公司五级分类下业务比例
			String multiXMLSix = delCostWarnFiveclass(context, "个人五级分类下业务比例", "BL300");	//个人五级分类下业务比例
			String multiXMLSeven = delCostWarnFiveclass(context, "小微五级分类下业务比例", "BL200");	//小微五级分类下业务比例
			
			context.addDataField("multiXMLFour", multiXMLFour);
			context.addDataField("multiXMLFive", multiXMLFive);
			context.addDataField("multiXMLSix", multiXMLSix);
			context.addDataField("multiXMLSeven", multiXMLSeven);
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
	 * 各种五级分类处理
	 */
	public String delCostWarnFiveclass(Context context,String Caption,String class_type)	throws Exception {
		DthPubAction cmisOp = new DthPubAction();
		KeyedCollection styles = new KeyedCollection();
		/*** flash样式设置 ***/
		styles.addDataField("Caption", Caption);
		styles.addDataField("Subcaption", "单位：百万元");
		styles.addDataField("BaseFontSize", "15");

		/*** flash数据设置 ***/
		styles.addDataField("xName", "class_cnname");
		styles.addDataField("yName", "amt");
		styles.addDataField("jsValue", "class_enname");
		styles.addDataField("jsName", "getCostWarnDetails"+class_type);
		
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("organNo", context.getDataValue("organNo"));
		kColl.addDataField("submitType", "getCostWarnChartFiveclass");
		if(class_type.length() > 0){
			kColl.addDataField("class_type", class_type);
		}
		String singleXML = cmisOp.delSingleCharts(context, styles , kColl);

		return singleXML;
	}
	
}