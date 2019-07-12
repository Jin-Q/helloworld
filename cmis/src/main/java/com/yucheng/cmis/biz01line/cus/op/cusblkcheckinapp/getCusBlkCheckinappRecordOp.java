package com.yucheng.cmis.biz01line.cus.op.cusblkcheckinapp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.StringUtil;

public class getCusBlkCheckinappRecordOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String cus_id = null;
		try{
			connection = this.getConnection(context);
			
			try {
				cus_id = (String)context.getDataValue("cus_id");
			} catch (Exception e) {}
			if(cus_id == null || cus_id.length() == 0)
				throw new EMPJDBCException("The value of pk["+cus_id+"] cannot be null!");
			
			KeyedCollection cus_kColl = (KeyedCollection)SqlClient.queryFirst("selectCusAddrByCusid", cus_id, null, connection);
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("addr", "STD_GB_AREA_ALL");//实际经营地址
			//树形菜单服务
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			SInfoUtils.addPopName(cus_kColl, map, service);
			String legal_addr = "";
			String legal_addr_displayname = "";
			String street = "";
			String legal_name = "";
			
			if(cus_kColl.getDataValue("addr")!=null&&!"".equals(cus_kColl.getDataValue("addr"))){
				legal_addr = (String)cus_kColl.getDataValue("addr");
				legal_addr_displayname = (String)cus_kColl.getDataValue("addr_displayname");
			}
			if(cus_kColl.getDataValue("street")!=null&&!"".equals(cus_kColl.getDataValue("street"))){
				street = (String)cus_kColl.getDataValue("street");
			}
			if(cus_kColl.getDataValue("legal_name")!=null&&!"".equals(cus_kColl.getDataValue("legal_name"))){
				legal_name = (String)cus_kColl.getDataValue("legal_name");
			}
			context.addDataField("legal_addr",legal_addr);
			context.addDataField("legal_addr_displayname", legal_addr_displayname);
			context.addDataField("street", street);
			context.addDataField("legal_name", legal_name);
			context.addDataField("flag", "success");
			context.addDataField("message", "success");
		}catch(Exception e){
			context.addDataField("flag", "error");
			context.addDataField("message", "异步查询客户地址信息失败："+e.getMessage());			
			try {
				connection.rollback();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "异步查询客户地址信息失败："+e.getMessage(), null);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
