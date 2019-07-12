package com.yucheng.cmis.biz01line.esb.op.trade;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.interfaces.ESBInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.imple.ESBInterfacesImple;
import com.yucheng.cmis.biz01line.esb.pub.CreateSno;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;

import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 国业汇率变化通知
 * @author Pansq
 * 只要汇率有发生变化，国结系统就发起该交易通知信贷系统，信贷系统保存到信贷汇率库表中
 * 
 * 
 */
public class TradeGYHLTBInform extends TranService {
	
	public KeyedCollection doExecute(CompositeData CD, Connection connection) throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		String CCY = "";
		try {
			//币种对照
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
		    CCY = (String)reqBody.getDataValue("CCY");//币种
			String BASE_PRICE = reqBody.getDataValue("BASE_PRICE")+"";//基准价
			String MIDDLE_PRICE = reqBody.getDataValue("MIDDLE_PRICE")+"";//中间价
			String EXCHANGE_BUY_PRICE = reqBody.getDataValue("EXCHANGE_BUY_PRICE")+"";//现汇买入价
			String EXCHANGE_SALE_PRICE = reqBody.getDataValue("EXCHANGE_SALE_PRICE")+"";//现汇卖出价
			String CASH_BUY_PRICE = reqBody.getDataValue("CASH_BUY_PRICE")+"";//现钞买入价
			String CASH_SALE_PRICE = reqBody.getDataValue("CASH_SALE_PRICE")+"";//现钞卖出价
			String SETTLE_FLAT_PRICE = reqBody.getDataValue("SETTLE_FLAT_PRICE")+"";//结汇平盘价
			String SALE_FLAT_PRICE = reqBody.getDataValue("SALE_FLAT_PRICE")+"";//售汇平盘价
			String QUATE_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("QUATE_DATE"));//交易日期
			
			BigDecimal BASE_PRICE_BG = new BigDecimal(BASE_PRICE).divide(new BigDecimal("100"),6,BigDecimal.ROUND_HALF_UP);
			BigDecimal MIDDLE_PRICE_BG = new BigDecimal(MIDDLE_PRICE).divide(new BigDecimal("100"),6,BigDecimal.ROUND_HALF_UP);
			BigDecimal EXCHANGE_BUY_PRICE_BG = new BigDecimal(EXCHANGE_BUY_PRICE).divide(new BigDecimal("100"),6,BigDecimal.ROUND_HALF_UP);
			BigDecimal EXCHANGE_SALE_PRICE_BG = new BigDecimal(EXCHANGE_SALE_PRICE).divide(new BigDecimal("100"),6,BigDecimal.ROUND_HALF_UP);
			BigDecimal CASH_BUY_PRICE_BG = new BigDecimal(CASH_BUY_PRICE).divide(new BigDecimal("100"),6,BigDecimal.ROUND_HALF_UP);
			BigDecimal CASH_SALE_PRICE_BG = new BigDecimal(CASH_SALE_PRICE).divide(new BigDecimal("100"),6,BigDecimal.ROUND_HALF_UP);
			BigDecimal SETTLE_FLAT_PRICE_BG = new BigDecimal(SETTLE_FLAT_PRICE).divide(new BigDecimal("100"),6,BigDecimal.ROUND_HALF_UP);
			BigDecimal SALE_FLAT_PRICE_BG = new BigDecimal(SALE_FLAT_PRICE).divide(new BigDecimal("100"),6,BigDecimal.ROUND_HALF_UP);
			
			//判断此币种在信贷是否已经存在，存在则更新，否则插入新的记录
			String serno = (String)SqlClient.queryFirst("queryPrdRateMaintain", CCY, null, connection);
			HashMap mapVal = new HashMap();
			mapVal.put("FOUNT_CUR_TYPE", CCY);
			mapVal.put("COMP_CUR_TYPE", "CNY");
			mapVal.put("BASE_REMIT", BASE_PRICE_BG);
			mapVal.put("MIDDLE_REMIT", MIDDLE_PRICE_BG);
			mapVal.put("REMIT_BUY", EXCHANGE_BUY_PRICE_BG);
			mapVal.put("REMIT_SLD", EXCHANGE_SALE_PRICE_BG);
			mapVal.put("XCMRHL", CASH_BUY_PRICE_BG);
			mapVal.put("XCMCHL", CASH_SALE_PRICE_BG);
			mapVal.put("JHPPHL", SETTLE_FLAT_PRICE_BG);
			mapVal.put("SHPPHL", SALE_FLAT_PRICE_BG);
			mapVal.put("UPDATE_DATE", QUATE_DATE);
			
			if(serno!=null&&!serno.equals("")){//信贷已存在记录，则更新
				SqlClient.update("updatePrdRateMaintain", serno, mapVal, null, connection);
			}else{//信贷不存在记录，新增
				String no = "HL"+CreateSno.createSnoByTime();
				mapVal.put("SERNO", no);
				SqlClient.insert("insertPrdRateMaintain", mapVal, connection);
			}
			/** added by yangzy 2015/07/09 需求：XD150407026， 更新存量外币业务的汇率 start **/
			if(CCY!=null&&!CCY.equals("")&&!"CNY".equals(CCY)){//更新存量外币业务的汇率
				HashMap mapVal2 = new HashMap();
				mapVal2.put("base_remit", BASE_PRICE_BG);
				SqlClient.update("updateIqpCurType4GYHLTB", CCY, mapVal2, null, connection);//更新业务申请外币汇率
				SqlClient.update("updateIqpSecCurType4GYHLTB", CCY, mapVal2, null, connection);//更新业务保证金外币汇率
				SqlClient.update("updateCtrCurType4GYHLTB", CCY, mapVal2, null, connection);//更新合同外币汇率
				SqlClient.update("updateCtrSecCurType4GYHLTB", CCY, mapVal2, null, connection);//更新合同保证金外币汇率
				SqlClient.update("updateCreditCurType4GYHLTB", CCY, mapVal2, null, connection);//更新存量信用证修改外币汇率
				SqlClient.update("updateCreditSecCurType4GYHLTB", CCY, mapVal2, null, connection);//更新存量信用证修改保证金外币汇率
				SqlClient.update("updateGuarantSecCurType4GYHLTB", CCY, mapVal2, null, connection);//更新存量保函修改外币汇率
				SqlClient.update("updateGuarantSecCurType4GYHLTB", CCY, mapVal2, null, connection);//更新存量保函修改保证金外币汇率
			}
			/** added by yangzy 20150709 需求：XD150407026， 更新存量外币业务的汇率 end **/
			
			EMPLog.log("TradeGYHLTBInform", EMPLog.INFO, 0, "【国业汇率变化通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【国业汇率变化通知】业务处理失败！币种为："+CCY);
			e.printStackTrace();
			EMPLog.log("TradeGYHLTBInform", EMPLog.INFO, 0, "【国业汇率变化通知】交易处理失败！币种为："+CCY+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
