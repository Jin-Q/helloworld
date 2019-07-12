package com.yucheng.cmis.biz01line.dth.op.dthRiskWarn;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;

public class GetDthCostWarnIndusPageOp extends CMISOperation {
	
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
			
			String multiXMLTwo = delCostWarnTwothree(context, "amt", "行业分类比例", "单位：百万元");	//行业分类下业务笔数
			String multiXMLThree = delCostWarnTwothree(context, "amt", "行业分类下业务发生额", "单位：百万元");	//行业分类下业务发生额

			context.addDataField("multiXMLTwo", multiXMLTwo);
			context.addDataField("multiXMLThree", multiXMLThree);
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
	 * 行业分类下业务处理
	 */
	public String delCostWarnTwothree(Context context,String value,String Caption,String Subcaption)	throws Exception {
		DthPubAction cmisOp = new DthPubAction();
		KeyedCollection styles = new KeyedCollection();
		/*** flash样式设置 ***/
		styles.addDataField("Caption", Caption);
		styles.addDataField("Subcaption", Subcaption);
		styles.addDataField("OutCnvBaseFontSize", "12");
		//styles.addDataField("BaseFontSize", "11");

		/*** flash数据设置 ***/
		styles.addDataField("xName", "indus_cnname");
		styles.addDataField("yName", value);
		styles.addDataField("jsValue", "");
		styles.addDataField("jsName", "");
		
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("organNo", context.getDataValue("organNo"));
		kColl.addDataField("submitType", "getCostWarnChartTwothree");
		String singleXML = cmisOp.delSingleCharts(context, styles,kColl);

		return singleXML;
	}
	
}