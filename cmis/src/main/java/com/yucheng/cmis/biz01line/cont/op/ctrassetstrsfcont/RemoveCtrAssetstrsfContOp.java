package com.yucheng.cmis.biz01line.cont.op.ctrassetstrsfcont;

import java.sql.Connection;


import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class RemoveCtrAssetstrsfContOp extends CMISOperation {
	private final String modelIdCont = "CtrAssetstrsfCont";
	private final String modelIdAsset= "IqpAsset";
	private final String cont_no_name = "cont_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String cont_no_value = null;
			try {
				cont_no_value = (String)context.getDataValue(cont_no_name);
			} catch (Exception e) {}
			if(cont_no_value == null || cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cont_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			
			//跟新合同状态
	        KeyedCollection kColl = dao.queryDetail(modelIdCont, cont_no_value, connection);
		    kColl.setDataValue("cont_status", "800");//撤销操作把合同状态改为作废
			dao.update(kColl, connection);
			
			//跟新资产包状态
			String addet_no = (String)kColl.getDataValue("asset_no");//资产包编号
			if(addet_no != null && !"".equals(addet_no)){
				KeyedCollection kCollAsset = dao.queryDetail(modelIdAsset, addet_no, connection);
				kCollAsset.put("status", "04");
				dao.update(kCollAsset, connection);
				//'01':'登记', '02':'已引用', '03':'已办结','04作废'
			}
			
			context.addDataField("flag", "success");
	      
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
