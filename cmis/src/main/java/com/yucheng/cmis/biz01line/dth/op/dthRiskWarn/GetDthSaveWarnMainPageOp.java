package com.yucheng.cmis.biz01line.dth.op.dthRiskWarn;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;

public class GetDthSaveWarnMainPageOp extends CMISOperation {

	/**
	 * 风险预警->储备项目
	 * @param context
	 * @throws EMPException
	 * @author GC.20131226
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String multiXMLOne = delSaveWarnChart(context,"储备项目数","单位：项","nums");
			String multiXMLTwo = delSaveWarnChart(context,"储备项目授信金额","单位：百万元","amts");
			
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
	 * 储备项目处理
	 */
	public String delSaveWarnChart(Context context,String Caption,String Subcaption , String nums) throws Exception {
		DthPubAction cmisOp = new DthPubAction();
		KeyedCollection styles = new KeyedCollection();
		/*** flash样式设置 ***/
		styles.addDataField("Caption", Caption);
		styles.addDataField("Subcaption", Subcaption);

		/*** flash数据设置 ***/
		styles.addDataField("xName", "org_name");
		styles.addDataField("yName", nums);
		styles.addDataField("jsName", "getSaveWarnDetails");
		styles.addDataField("showCnname", "line_cnname");
		styles.addDataField("jsValue_one", "manager_br_id");
		styles.addDataField("jsValue_two", "line_enname");
		String showName[]= "公司业务条线,小微业务条线".split(",");
		
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("submitType", "getSaveWarnChart");
		String multiXML = cmisOp.delMultiCharts(context, styles, showName,kColl);
		
		return multiXML;
	}

}