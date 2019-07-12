package com.yucheng.cmis.biz01line.dth.op.dthRiskWarn;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;

public class GetDthIntbankWarnMainPageOp extends CMISOperation {

	/**
	 * 风险预警->同业预警
	 * @param context
	 * @throws EMPException
	 * @author GC.20131226
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			DthPubAction cmisOp = new DthPubAction();
			KeyedCollection styles = new KeyedCollection();
			/*** flash样式设置 ***/
			styles.addDataField("Caption", "同业用信情况");
			styles.addDataField("Subcaption", "单位：百万元");

			/*** flash数据设置 ***/
			styles.addDataField("xName", "cus_name");
			styles.addDataField("yName", "amt");
			styles.addDataField("jsValue", "cus_id");
			styles.addDataField("jsName", "getIntbankWarnDetails");
			
			KeyedCollection kColl = new KeyedCollection();
			kColl.addDataField("submitType", "getIntbankWarnChart");
			String singleXML = cmisOp.delSingleCharts(context, styles,kColl);
			
			context.addDataField("singleXML", singleXML);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)this.releaseConnection(context, connection);
		}
		return "0";
	}

}