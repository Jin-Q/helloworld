package com.yucheng.cmis.biz01line.arp.op.arpcolldispapp;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.arp.component.ArpPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeleteArpCollDispAppRecordOp extends CMISOperation {

	private final String modelId = "ArpCollDispApp";

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String serno_value = null;
			String asset_disp_mode =null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
				asset_disp_mode = (String) context.getDataValue("asset_disp_mode");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, serno_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}
			Map<String,String> refFields = new HashMap<String,String>();
            refFields.put("serno", serno_value);
			ArpPubComponent cmisComponent = (ArpPubComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance("ArpPubComponent",context,connection);
			if("00".equals(asset_disp_mode)){//出售
				cmisComponent.deleteByField("ArpAssetSaleInfo", refFields);
			}else if("01".equals(asset_disp_mode)){//出租
				cmisComponent.deleteByField("ArpAssetRentInfo", refFields);
			}else if("02".equals(asset_disp_mode)){//转固
				cmisComponent.deleteByField("ArpAssetPegInfo", refFields);
			}else if("03".equals(asset_disp_mode)){//核销
				cmisComponent.deleteByField("ArpAssetWriteoffAccInfo", refFields);
			}
			context.addDataField("flag","success");
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
