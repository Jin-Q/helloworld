package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 授信额度查询
 * @author xc
 * 根据客户号查询客户授信额度查询
 * 测试DBUtil.java
 */
public class Trade0200300000102 extends TranService {
	
	public KeyedCollection doExecute(CompositeData CD, Connection connection) throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String client_no = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			client_no = (String)reqBody.getDataValue("CLIENT_NO");//客户码
			
			/** 授信额度查询 */
			IndexedCollection IColl = (IndexedCollection) SqlClient.queryList4IColl("Trade0200300000102", client_no, connection);
			
			/** 组装返回报文 */
			retKColl.setDataValue("ret_msg", "【授信额度查询】交易处理完成");
			retKColl.put("client_no", client_no);
			retKColl.put("IColl", IColl);
			
			EMPLog.log("outReport", EMPLog.INFO, 0, "【授信额度查询】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【授信额度查询】业务处理失败！客户码为："+client_no);
			e.printStackTrace();
			EMPLog.log("outReport", EMPLog.ERROR, 0, "【授信额度查询】交易处理失败，客户号为："+client_no+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}
}
