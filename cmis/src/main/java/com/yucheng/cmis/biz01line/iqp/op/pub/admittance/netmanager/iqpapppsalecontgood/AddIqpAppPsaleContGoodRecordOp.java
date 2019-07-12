package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpapppsalecontgood;

import java.sql.Connection;
import java.util.HashMap;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddIqpAppPsaleContGoodRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpAppPsaleContGood";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			String serno = (String)kColl.getDataValue("serno");//业务流水号
			String psale_cont = (String)kColl.getDataValue("psale_cont");//购销合同编号
			String commo_name = (String)kColl.getDataValue("commo_name");//商品名称
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("serno", serno);
			map.put("psale_cont", psale_cont);
			map.put("commo_name", commo_name);
			KeyedCollection kCollSelect = dao.queryDetail(modelId, map, connection);
			if(kCollSelect!=null){
				String sernoSelect = (String)kCollSelect.getDataValue("serno");
				if(sernoSelect != null && !"".equals(sernoSelect)){
					context.put("flag", "error");
					context.put("msg", "已存在该商品，请重新录入");
				}else{
					dao.insert(kColl, connection);
					context.put("flag", "success");
					context.put("msg", "");
				}
			}else{
				dao.insert(kColl, connection);
				context.put("flag", "success");
				context.put("msg", "");
			}
			
		}catch (EMPException ee) {
			context.put("flag", "error");
			context.put("msg", ee.getMessage());
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
