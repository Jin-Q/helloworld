package com.yucheng.cmis.biz01line.dth.op.dthLoanSubjectAnaly;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;

public class GetDthIndivBadAnalyMainPageOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String multiXMLOne = delIndivBadAnalyChartOne(context);
			String multiXMLTwo = delIndivBadAnalyChartTwo(context);
			
			context.addDataField("multiXMLOne", multiXMLOne);
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
	 * 各个贷品种下不良贷款比例(饼图)
	 */
	public String delIndivBadAnalyChartOne(Context context) throws Exception {
		DthPubAction cmisOp = new DthPubAction();
		KeyedCollection styles = new KeyedCollection();
		/*** flash样式设置 ***/
		styles.addDataField("Caption", "个贷品种下不良贷款比例");
		styles.addDataField("Subcaption", "单位：万元");
		styles.addDataField("BaseFontSize", "15");
		/*** flash数据设置 ***/
		styles.addDataField("xName", "prd_cnname");
		styles.addDataField("yName", "nums");
		styles.addDataField("jsValue", "prd_enname");
		styles.addDataField("jsName", "getIndivBadAnalyDetailsOne");
		
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("submitType", "getIndivBadAnalyChartOne");
		String singleXML = cmisOp.delSingleCharts(context, styles,kColl);

		return singleXML;
	}

	/**
	 * 个人不良贷款前10的客户经理分布(柱状图)
	 */
	public String delIndivBadAnalyChartTwo(Context context) throws Exception {
		DthPubAction cmisOp = new DthPubAction();
		KeyedCollection styles = new KeyedCollection();
		/*** flash样式设置 ***/
		styles.addDataField("Caption", "个人不良贷款前10的客户经理分布");
		styles.addDataField("Subcaption", "单位：万元");
		/*** flash数据设置 ***/
		styles.addDataField("xName", "mgr_name");
		styles.addDataField("yName", "nums");
		styles.addDataField("jsValue", "cust_mgr");
		styles.addDataField("jsName", "getIndivBadAnalyChartTwo");
		
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("submitType", "getIndivBadAnalyChartTwo");
		String multiXML = cmisOp.delSingleCharts(context, styles,kColl);
		
		return multiXML;
	}
		
}