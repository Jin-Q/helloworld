package com.yucheng.cmis.biz01line.dth.op.dthLoanSubjectAnaly;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.dth.op.pubAction.DthPubAction;
import com.yucheng.cmis.operation.CMISOperation;

public class GetDthloanStruMainPageOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);			
			DthPubAction cmisOp = new DthPubAction();
			int i = 0;

			KeyedCollection kColl = new KeyedCollection();
			kColl.addDataField("submitType", "getLoanBalanceChart");
			IndexedCollection iColl_data = cmisOp.delSqlReturnIcoll(kColl, context);	//取去年未到上月日期
			for(i = 0;i < iColl_data.size() ; i++){
				KeyedCollection kColl_data = (KeyedCollection) iColl_data.get(i);
				String multiXML = delloanStruChart(context, kColl_data.getDataValue("enname"), kColl_data.getDataValue("cnname"));
				context.addDataField("multiXML"+i, multiXML);
			}
			context.addDataField("nums", i);
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
	 * 贷款结构数据处理
	 */
	public String delloanStruChart(Context context,Object date_enname,Object date_cnname) throws Exception {
		DthPubAction cmisOp = new DthPubAction();
		KeyedCollection styles = new KeyedCollection();
		
		/*** flash样式设置 ***/
		styles.addDataField("Caption", date_cnname+"各担保方式下贷款结构");
		styles.addDataField("Subcaption", "单位：亿元");
		styles.addDataField("BaseFontSize", "15");
		/*** flash数据设置 ***/
		styles.addDataField("xName", "prdname");
		styles.addDataField("yName", "nums");
		styles.addDataField("jsValue", "prdid");
		styles.addDataField("jsValuePlus", date_enname);
		styles.addDataField("jsName", "getloanStruDetails");
		
		KeyedCollection kColl = new KeyedCollection();
		kColl.addDataField("submitType", "getloanStruChart");
		kColl.addDataField("value", date_enname);
		String singleXML = cmisOp.delSingleCharts(context, styles,kColl);

		return singleXML;
	}

}