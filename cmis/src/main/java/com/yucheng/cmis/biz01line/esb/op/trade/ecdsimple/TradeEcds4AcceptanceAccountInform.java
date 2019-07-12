package com.yucheng.cmis.biz01line.esb.op.trade.ecdsimple;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.op.trade.TranService;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 电子商业汇票相关业务计账通知服务
 * @author yangzy
 * 说明：
 * 接收电票系统发送的票据批次，根据票据批次，发送授权记账
 * 
 */
public class TradeEcds4AcceptanceAccountInform extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection)
			throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			String BATCH_NO = reqBody.getDataValue("BATCH_NO").toString().trim();//票据批次
			
			KeyedCollection approveInfoKColl = (KeyedCollection)SqlClient.queryFirst("getAccAccpApproveInfo4Ecds", BATCH_NO, null, connection);
			
			if(approveInfoKColl!=null&&approveInfoKColl.size()>0){
				String flag = (String)approveInfoKColl.getDataValue("flag");
				if(flag!=null&&"4".equals(flag)){//审批通过
					Map param = new HashMap();
					param.put("status", "00");
					SqlClient.update("updateStatus4PvpAuthorize", BATCH_NO, param, null, connection);
					EMPLog.log("TradeEcds4AcceptanceAccountInform", EMPLog.INFO, 0, "【电子商业汇票相关业务计账通知服务】交易完成...", null);
				}else{
					retKColl.setDataValue("ret_code", "9999");
					retKColl.setDataValue("ret_msg", "【电子商业汇票相关业务计账通知服务】,业务处理失败,该批次业务未审批通过！");
				}
			}else{
				retKColl.setDataValue("ret_code", "9999");
				retKColl.setDataValue("ret_msg", "【电子商业汇票相关业务计账通知服务】,业务处理失败,不存在该批次业务审批！");
			}
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "9999");
			retKColl.setDataValue("ret_msg", "【电子商业汇票相关业务计账通知服务】,业务处理失败！");
			e.printStackTrace();
		}
		return retKColl;
	}

}
