package com.yucheng.cmis.biz01line.arp.op.arpcolldispapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddArpCollDispAppRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "ArpCollDispApp";
	private final String modelId1 = "ArpAssetRentInfo";//出租
	private final String modelId2 = "ArpAssetPegInfo";//转固
	private final String modelId3 = "ArpAssetSaleInfo";//出售
	private final String modelId4 = "ArpAssetWriteoffInfo";//核销
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String asset_disp_mode ="";
			KeyedCollection kColl = null;
			KeyedCollection kCollTemp=new KeyedCollection();
			String serno ="";
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
				kColl.setDataValue("serno",serno);
				kCollTemp.addDataField("serno",serno);
				kCollTemp.addDataField("guaranty_no",kColl.getDataValue("guaranty_no"));
				asset_disp_mode = (String) kColl.getDataValue("asset_disp_mode");
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			if("00".equals(asset_disp_mode)){//出售
				kCollTemp.setName(modelId3);
			}else if("01".equals(asset_disp_mode)){//出租
				kCollTemp.setName(modelId1);
			}else if("02".equals(asset_disp_mode)){//转固
				kCollTemp.setName(modelId2);
			}else if("03".equals(asset_disp_mode)){//核销
				kCollTemp.setName(modelId4);
			}
			dao.insert(kColl, connection);
			dao.insert(kCollTemp, connection);
			context.addDataField("flag","success");
			context.addDataField("serno",serno);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
