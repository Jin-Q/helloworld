package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 银票退票通知
 * @author Pansq
 * 根据借据号更新银票台账状态为8退回未用，核销日期更新为交易日期
 * 
 */
public class TradeAccpBackInform extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection)
			throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String DUEBILL_NO = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			DUEBILL_NO = (String)reqBody.getDataValue("DUEBILL_NO");//借据号
			String BILL_NO = (String)reqBody.getDataValue("BILL_NO");//汇票号码
			String TRAN_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("TRAN_DATE"));//交易日期
			String BRANCH_ID = (String)reqBody.getDataValue("BRANCH_ID");//机构代码
			Map param = new HashMap();
			param.put("status", "8");
			param.put("tran_date", TRAN_DATE);
			//核销银票台账
			SqlClient.update("updateAccAccpStatus", DUEBILL_NO, param, null, connection);
			EMPLog.log("TradeAccpBackInform", EMPLog.INFO, 0, "【银票退票通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【银票退票通知】交易处理失败，借据号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TradeAccpBackInform", EMPLog.ERROR, 0, "【银票退票通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
