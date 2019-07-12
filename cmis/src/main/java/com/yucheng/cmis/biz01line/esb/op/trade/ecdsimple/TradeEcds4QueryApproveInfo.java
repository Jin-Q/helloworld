package com.yucheng.cmis.biz01line.esb.op.trade.ecdsimple;

import java.sql.Connection;

import com.dc.eai.data.CompositeData;
import com.dc.eai.data.FieldType;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.op.trade.TranService;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 电子商业汇票相关业务审批结果查询
 * @author yangzy
 * 说明：
 * 接收电票系统发送的承兑票据号码，查询该票据业务的审批情况
 * 
 */
public class TradeEcds4QueryApproveInfo extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection)
			throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String buss_deal_status = "";
		retKColl.put("buss_deal_status", buss_deal_status);
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			String BATCH_NO = reqBody.getDataValue("BATCH_NO").toString().trim();//票据批次
			KeyedCollection approveInfoKColl = (KeyedCollection)SqlClient.queryFirst("getAccAccpApproveInfo4Ecds", BATCH_NO, null, connection);
			
			if(approveInfoKColl!=null&&approveInfoKColl.size()>0){
				String flag = (String)approveInfoKColl.getDataValue("flag");
				if(flag!=null&&"1".equals(flag)){
					buss_deal_status = "03";
				}else if(flag!=null&&"2".equals(flag)){
					buss_deal_status = "00";
				}else if(flag!=null&&"3".equals(flag)){
					buss_deal_status = "01";
				}else if(flag!=null&&"4".equals(flag)){
					buss_deal_status = "02";
				}
			}
			if("".equals(buss_deal_status)){
				retKColl.setDataValue("ret_code", "9999");
				retKColl.setDataValue("ret_msg", "【电子商业汇票相关业务审批结果查询】,业务查询失败,不存在该批次的电票业务！");
			}else{
				retKColl.put("buss_deal_status", buss_deal_status);
				EMPLog.log("TradeEcds4QueryApproveInfo", EMPLog.INFO, 0, "【电子商业汇票相关业务审批结果查询】交易完成...", null);
			}
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "9999");
			retKColl.setDataValue("ret_msg", "【电子商业汇票相关业务审批结果查询】,业务查询失败！");
			retKColl.put("buss_deal_status", buss_deal_status);
			e.printStackTrace();
		}
		return retKColl;
	}

}
