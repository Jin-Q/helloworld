package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

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
 * 银票出票通知
 * @author Pansq
 * 更新银票台账状态为正常
 * 回写汇票号码到银票台账表
 * 更新出票日期為交易日期
 * 根据借据号更新授权表的状态为授权已确认
 * 
 */
public class TradeAccpInform extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection) throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String DUEBILL_NO = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			
			KeyedCollection app = esbInterface.getReqAppHead(CD);
			String fileName = (String) app.getDataValue("FILE_NAME");
			String filePath = (String) app.getDataValue("FILE_PATH");
			
			FTPUtil.FTP2local(filePath,fileName);//下载文件
			CompositeData reqCD = new CompositeData();
			FTPUtil.getFile2CD(fileName, reqCD);//文件转CD
			
			KeyedCollection kcoll = esbInterface.getReqBody(reqCD);
			IndexedCollection icoll = (IndexedCollection) kcoll.get("Serv1100200008014");
			
			for(int i=0;i<icoll.size();i++){
				KeyedCollection reqBody = (KeyedCollection) icoll.get(i);
				DUEBILL_NO = (String)reqBody.getDataValue("DUEBILL_NO");//借据号
				String BILL_NO = (String)reqBody.getDataValue("BILL_NO");//汇票号码
				String TRAN_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("TRAN_DATE"));//交易日期
				String BRANCH_ID = (String)reqBody.getDataValue("BRANCH_ID");//机构代码
				Map param = new HashMap();
				param.put("status", "1");
				param.put("porder_no", BILL_NO);
				param.put("date", TRAN_DATE);
				/** 更新信贷系统本地台帐状态 */
				SqlClient.update("updateAccAccpForOut", DUEBILL_NO, param, null, connection);
				//根据借据号更新授权表的状态为授权已确认
				SqlClient.update("updatePvpAuthorizeStatus", DUEBILL_NO, "04", null, connection);
			}
			
			EMPLog.log("TradeAccpInform", EMPLog.INFO, 0, "【银票出票通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【银票出票通知】交易处理失败，借据号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TradeAccpInform", EMPLog.ERROR, 0, "【银票出票通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
