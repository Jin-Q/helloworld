package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;
import com.dc.eai.data.CompositeData;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.FTPUtil;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 担保机构迁移成功通知
 * @author zhaozq
 * 
 */
public class Trade1100200008070 extends TranService {
	
	public KeyedCollection doExecute(CompositeData CD, Connection connection) throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			
			KeyedCollection app = esbInterface.getReqAppHead(CD);
			String fileName = (String) app.getDataValue("FILE_NAME");
			String filePath = (String) app.getDataValue("FILE_PATH");
			
			FTPUtil.FTP2local(filePath,fileName);//下载文件
			CompositeData reqCD = new CompositeData();
			FTPUtil.getFile2CD(fileName, reqCD);//文件转CD
			
			KeyedCollection kcoll = esbInterface.getReqBody(reqCD);
			IndexedCollection icoll = (IndexedCollection) kcoll.get("Serv1100200008070");
			
			for(int i=0;i<icoll.size();i++){
				KeyedCollection reqBody = (KeyedCollection) icoll.get(i);
				
				String COLL_ID_NO = (String)reqBody.getDataValue("COLL_ID_NO");//担保品编号
				String ORI_ACCOUNT_BRANCH = (String)reqBody.getDataValue("ORI_ACCOUNT_BRANCH");//原机构号
				String NEW_ACCOUNT_BRANCH = (String)reqBody.getDataValue("NEW_ACCOUNT_BRANCH");//新机构号
				
				//通过授权编号，查询利率调整申请信息
				SqlClient.update("updateCretiKeepOrgNo", COLL_ID_NO, NEW_ACCOUNT_BRANCH, null, connection);
			}
			EMPLog.log("Trade1100200008070", EMPLog.INFO, 0, "【批量利率调整/账户调整通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【担保机构迁移成功通知】交易处理失败");
			e.printStackTrace();
			EMPLog.log("Trade1100200008070", EMPLog.ERROR, 0, "【担保机构迁移成功通知】交易处理失败，异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}
}
