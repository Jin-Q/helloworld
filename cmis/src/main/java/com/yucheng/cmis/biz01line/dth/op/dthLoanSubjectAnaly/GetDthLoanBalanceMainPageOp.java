package com.yucheng.cmis.biz01line.dth.op.dthLoanSubjectAnaly;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;

public class GetDthLoanBalanceMainPageOp extends CMISOperation {

	/**
	 * 贷款指标分析 -> 贷款余额
	 * @param context
	 * @throws EMPException
	 * @author GC.20131220
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			DthPubAction cmisOp = new DthPubAction();
			KeyedCollection styles = new KeyedCollection();
			/*** flash样式设置 ***/
			styles.addDataField("Caption", "贷款余额");
			styles.addDataField("Subcaption", "单位：万元");
			styles.addDataField("RotateNames", "1");

			/*** flash数据设置 ***/
			styles.addDataField("xName", "cnname");
			styles.addDataField("yName", "nums");
			styles.addDataField("jsValue", "enname");
			styles.addDataField("jsName", "getLoanBalanceDetails");
			
			KeyedCollection kColl = new KeyedCollection();
			kColl.addDataField("submitType", "getLoanBalanceChart");
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