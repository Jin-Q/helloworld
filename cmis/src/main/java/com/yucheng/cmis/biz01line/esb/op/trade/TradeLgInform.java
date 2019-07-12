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
 * 保函开立通知
 * @author Pansq
 * 根据借据号更新贷款台账状态为1正常
 * 根据借据号更新授权表的状态为授权已确认
 * 
 */
public class TradeLgInform extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection) throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String DUEBILL_NO = "";
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
		    DUEBILL_NO = (String)reqBody.getDataValue("DUEBILL_NO");//借据号
			String TRAN_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("TRAN_DATE"));//交易日期
			String BRANCH_ID = (String)reqBody.getDataValue("BRANCH_ID");//机构代码
			
			/** 更新信贷系统本地台帐状态 */
			SqlClient.update("updateAccLoanStatus", DUEBILL_NO, "1", null, connection);
			//根据借据号更新授权表的状态为授权已确认
			SqlClient.update("updatePvpAuthorizeStatus", DUEBILL_NO, "04", null, connection);
			
			EMPLog.log("TradeLgInform", EMPLog.INFO, 0, "【保函开立通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【保函开立通知 】,业务处理失败！借据号为："+DUEBILL_NO);
			e.printStackTrace();
			EMPLog.log("TradeLgInform", EMPLog.ERROR, 0, "【保函开立通知】交易处理失败，借据号为："+DUEBILL_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
