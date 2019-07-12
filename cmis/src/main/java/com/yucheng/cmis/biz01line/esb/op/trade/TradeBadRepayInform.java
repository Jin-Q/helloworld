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
 * 呆账归还通知
 * @author Pansq
 * 说明：呆账归还通知（呆账台帐未定义，接口待定。。。）
 */
public class TradeBadRepayInform extends TranService {

	public KeyedCollection doExecute(CompositeData CD, Connection connection)
			throws Exception {
		KeyedCollection retkColl = TagUtil.getDefaultResultKColl();
		String DUEBILL_NO ="";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			DUEBILL_NO = (String)reqBody.getDataValue("DUEBILL_NO");	//借据号
			String RECYCLE_CORPUS = (String)reqBody.getDataValue("RECYCLE_CORPUS");	//回收金额
			String RECYCLE_TYPE = (String)reqBody.getDataValue("RECYCLE_TYPE");//回收方式

			/*** 更新台账表中核销余额 ***/
			Map<String, String> param = new HashMap<String, String>();
			param.put("writeoff_cap_bal", RECYCLE_TYPE.equals("1")?"0":RECYCLE_CORPUS);	//核销本金
			param.put("writeoff_int_bal", RECYCLE_TYPE.equals("1")?RECYCLE_CORPUS:"0");	//核销利息
			SqlClient.update("setBadRepayAcc", DUEBILL_NO, param , null, connection);

			EMPLog.log("TradeBadRepayInform", EMPLog.INFO, 0, "【呆账归还通知】交易成功！", null);
		} catch (Exception e) {
			retkColl.setDataValue("ret_code", "999999");
			retkColl.setDataValue("ret_msg", "【呆账归还通知】交易处理失败，借据编号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TradeBadRepayInform", EMPLog.ERROR, 0, "【呆账归还通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retkColl;
	}

}