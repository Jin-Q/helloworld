package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 贸易融资表外业务出账通知
 * @author liqh
 * 说明：
 * 根据借据号更新贷款台账的台账状态为1正常
 * 根据业务类型判断如果为信用证，则更新信用证明细表，将信用证号码更新到表中
 * 根据借据号更新授权表的状态为授权已确认
 * 
 */
public class TradeMyrzbwywczInform extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection)
			throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String DUEBILL_NO = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			String BUSS_KIND = (String)reqBody.getDataValue("BUSS_KIND");//业务类型
			DUEBILL_NO = (String)reqBody.getDataValue("DUEBILL_NO");//借据号
			String LC_NO = (String)reqBody.getDataValue("LC_NO");//信用证编号
			//更新贷款台账状态
			SqlClient.update("updateAccLoanStatus", DUEBILL_NO, "1", null, connection);
			if(BUSS_KIND.equals("01")){//业务类型为信用证则更新信用证编号到明细表，其他业务类型不做处理
				//更新信用证明细表
				SqlClient.update("updateCertNoByBillNo", DUEBILL_NO, LC_NO, null, connection);
			}
			//根据借据号更新授权表的状态为授权已确认
			SqlClient.update("updatePvpAuthorizeStatus", DUEBILL_NO, "04", null, connection);
			
			EMPLog.log("TradeMyrzbwywczInform", EMPLog.INFO, 0, "【贸易融资表外业务出账通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【贸易融资表外业务出账通知】,业务处理失败！借据编号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TradeMyrzbwywczInform", EMPLog.ERROR, 0, "【贸易融资表外业务出账通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
