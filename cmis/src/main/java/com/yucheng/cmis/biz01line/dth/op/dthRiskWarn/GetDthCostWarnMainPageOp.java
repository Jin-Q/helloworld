package com.yucheng.cmis.biz01line.dth.op.dthRiskWarn;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;

public class GetDthCostWarnMainPageOp extends CMISOperation {
	
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
			
			String multiXMLOne = delCostWarnOne(context);	//逾期业务比例
			
			context.addDataField("multiXMLOne", multiXMLOne);
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
	 * 逾期客户比例
	 */
	public String delCostWarnOne(Context context)	throws Exception {
		DthPubAction cmisOp = new DthPubAction();
		KeyedCollection styles = new KeyedCollection();
		/*** flash样式设置 ***/
		styles.addDataField("Caption", "逾期业务比例");
		styles.addDataField("Subcaption", "单位：笔");
		styles.addDataField("BaseFontSize", "15");
		/*** flash数据设置 ***/
		styles.addDataField("xName", "over_name");
		styles.addDataField("yName", "nums");
		styles.addDataField("jsValue", "over_status");
		styles.addDataField("jsName", "getCostWarnDetailsOne");
		
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("organNo", context.getDataValue("organNo"));
		kColl.addDataField("submitType", "getCostWarnChartOne");
		String singleXML = cmisOp.delSingleCharts(context, styles,kColl);

		return singleXML;
	}
	
}