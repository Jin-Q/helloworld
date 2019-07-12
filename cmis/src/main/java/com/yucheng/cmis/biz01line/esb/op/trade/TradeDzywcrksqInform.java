package com.yucheng.cmis.biz01line.esb.op.trade;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 质押出入库授权成功通知
 * @author Pansq
 */
public class TradeDzywcrksqInform extends ESBTranService {

	@Override
	public KeyedCollection doExecute(KeyedCollection kColl, Connection connection) throws Exception {
		KeyedCollection retKColl = new KeyedCollection();
		String AUTH_NO = "";
		String CERTI_NO ="";
		try {
			KeyedCollection reqBody = (KeyedCollection)kColl.get("BODY");
			
			AUTH_NO = (String)reqBody.getDataValue("AuthNo");//授权编号
			String hxSerno = (String)reqBody.getDataValue("CltlPldgNo");//核心流水号
			KeyedCollection mortGuarantyCertiInfoKcoll = (KeyedCollection)SqlClient.queryFirst("queryGuarantyNoByHxSerno", hxSerno, null, connection);//查询押品编号
			String GUARANTY_NO = "";
			if(mortGuarantyCertiInfoKcoll!=null){
				if(mortGuarantyCertiInfoKcoll.containsKey("guaranty_no")){
					GUARANTY_NO = (String)mortGuarantyCertiInfoKcoll.getDataValue("guaranty_no");
				}
			}
			String CERTI_TYPE = (String)reqBody.getDataValue("VluablScrtTp");//权证类型
			CERTI_NO = (String)reqBody.getDataValue("VluablScrtNo");//权证编号
			String TRAN_DATE = TagUtil.formatDate2Ten(reqBody.getDataValue("AcctTxnDt"));//交易日期
			//通过授权号查询获取出入库方式
			KeyedCollection AuthKcoll = (KeyedCollection)SqlClient.queryFirst("queryModeByAuthNo", AUTH_NO, null, connection);//出入库方式
			String IN_OUT_TYPE = (String)reqBody.getDataValue("OutInWrhsMd");//出入库方式
			String EXWA_TYPE = "";
			if(AuthKcoll!=null){
				//IN_OUT_TYPE = (String)AuthKcoll.getDataValue("stor_exwa_mode");
				EXWA_TYPE = (String)AuthKcoll.getDataValue("exwa_type");
			}
			//TRAN_DATE = TRAN_DATE.substring(0,4)+"-"+TRAN_DATE.substring(4,6)+"-"+TRAN_DATE.substring(6,8);
			KeyedCollection paraKc = new KeyedCollection();
			paraKc.addDataField("warrant_type", CERTI_TYPE);
			paraKc.addDataField("warrant_no", CERTI_NO);
			KeyedCollection valueKc = new KeyedCollection();
			if(EXWA_TYPE!=null&&("01".equals(EXWA_TYPE)||"02".equals(EXWA_TYPE))){//应收账款池,保理池
				if(IN_OUT_TYPE.equals("03")){//出池
					//MortStorExwaDetail 出库
					//IqpActrecbondDetail 出池
					valueKc.addDataField("warrant_state", "6");
					valueKc.addDataField("sys_update_time",TRAN_DATE);
					SqlClient.update("updateIqpActrecbondDetail", CERTI_NO, "3", null, connection);
				}else if(IN_OUT_TYPE.equals("04")){//入池
					//MortStorExwaDetail 在库
					//IqpActrecbondDetail 在池
					valueKc.addDataField("warrant_state", "3");
					valueKc.addDataField("sys_update_time",TRAN_DATE);
					SqlClient.update("updateIqpActrecbondDetail", CERTI_NO, "2", null, connection);
				}
				SqlClient.update("updateMortStorExwaDetail", paraKc, valueKc, null, connection);
				//IqpActrecpoMana更新池状态为生效
				KeyedCollection ActrecbondKcoll = (KeyedCollection)SqlClient.queryFirst("queryIqpActrecbondDetail", GUARANTY_NO, null, connection);
				String invc_quant = "";
				String invc_amt = "";
				String crd_rgtchg_amt = "";
				if(ActrecbondKcoll!=null&&ActrecbondKcoll.getDataValue("invcquant")!=null){
					invc_quant = String.valueOf((BigDecimal)ActrecbondKcoll.getDataValue("invcquant"));
					invc_amt = String.valueOf((BigDecimal)ActrecbondKcoll.getDataValue("invcamt"));
					crd_rgtchg_amt = String.valueOf((BigDecimal)ActrecbondKcoll.getDataValue("bondamt"));
				}
				Map<String, String> paramValue = new HashMap<String, String>();
				paramValue.put("invc_quant", invc_quant);
				paramValue.put("invc_amt", invc_amt);
				paramValue.put("crd_rgtchg_amt", crd_rgtchg_amt);
				paramValue.put("status", "2");
				SqlClient.update("updateIqpActrecpoMana", GUARANTY_NO, paramValue, null, connection);
			}else if(EXWA_TYPE!=null&&"03".equals(EXWA_TYPE)){//票据池
				KeyedCollection paramKcoll = new KeyedCollection();
				paramKcoll.put("drfpo_no", GUARANTY_NO);
				paramKcoll.put("porder_no", CERTI_NO);
				if(IN_OUT_TYPE.equals("03")){//出池
					//MortStorExwaDetail 出库
					//IqpBillDetailInfo  解质押
					//IqpCorreInfo 出池/托收
					valueKc.addDataField("warrant_state", "6");
					valueKc.addDataField("sys_update_time",TRAN_DATE);
					SqlClient.update("updateIqpBillDetailInfo", CERTI_NO, "06", null, connection);
					String status = (String) SqlClient.queryFirst("queryIqpCorreInfo", paramKcoll, null,connection);
					if(status!=null&&"05".equals(status)){//托收记账中
						SqlClient.update("updateIqpCorreInfoStatus", paramKcoll, "02", null, connection);
					}else{//出池记账中
						SqlClient.update("updateIqpCorreInfoStatus", paramKcoll, "03", null, connection);
					}
				}else if(IN_OUT_TYPE.equals("04")){//入池
					//MortStorExwaDetail 在库
					//IqpBillDetailInfo  质押
					//IqpCorreInfo 在池
					valueKc.addDataField("warrant_state", "3");
					valueKc.addDataField("sys_update_time",TRAN_DATE);
					SqlClient.update("updateIqpBillDetailInfo", CERTI_NO, "05", null, connection);
					SqlClient.update("updateIqpCorreInfoStatus", paramKcoll, "01", null, connection);
				}
				SqlClient.update("updateMortStorExwaDetail", paraKc, valueKc, null, connection);
				//IqpDrfpoMana 状态为有效状态，更新在池票据总金额字段
				double inPoolAmt = 0.0;
				Map<String, String> selMap = new HashedMap();
				selMap.put("status","01");
				selMap.put("drfpo_no",GUARANTY_NO);
				IndexedCollection nextIColl = SqlClient.queryList4IColl("getDrftAmtByDrfpoNo",selMap, connection);
				if(nextIColl.size()!=0){
					for(int j=0;j<nextIColl.size();j++){
						KeyedCollection kc = (KeyedCollection) nextIColl.get(j);
						inPoolAmt+=((BigDecimal) kc.getDataValue("drft_amt")).doubleValue() ;
					}
				}
				Map<String, String> paramValue = new HashMap<String, String>();
				paramValue.put("bill_amt", String.valueOf(inPoolAmt));
				paramValue.put("status", "01");
				SqlClient.update("updateIqpDrfpoMana", GUARANTY_NO, paramValue, null, connection);
			}else{//押品权证
				if("06".equals(IN_OUT_TYPE)){//取出还贷（更改权证状态为出库）
					valueKc.addDataField("warrant_state", "6");
					valueKc.addDataField("sys_update_time",TRAN_DATE);
					KeyedCollection kc = (KeyedCollection) SqlClient.queryFirst("checkMortGuarantyCertiInfo", GUARANTY_NO, null, connection);
					String cc = kc.getDataValue("cc")+"";
					if(Double.parseDouble(cc)==1){
						//更改押品状态为出库核销
						SqlClient.update("updateMortGuarantyBaseInfo", GUARANTY_NO, "4", null, connection);
					}
				}else if("07".equals(IN_OUT_TYPE)){//临时借出（更改权证状态为借出）
					valueKc.addDataField("warrant_state", "4");
					valueKc.addDataField("sys_update_time",TRAN_DATE);
				}else if("10".equals(IN_OUT_TYPE)){//注销出库（更改权证状态为核销）
					KeyedCollection kc = (KeyedCollection) SqlClient.queryFirst("checkMortGuarantyCertiInfo", GUARANTY_NO, null, connection);
					String cc = kc.getDataValue("cc")+"";
					if(Double.parseDouble(cc)==1){
						//更改押品状态为出库核销
						SqlClient.update("updateMortGuarantyBaseInfo", GUARANTY_NO, "4", null, connection);
					}
					valueKc.addDataField("warrant_state", "7");
					valueKc.addDataField("sys_update_time",TRAN_DATE);
				}else if("05".equals(IN_OUT_TYPE)){//入库（更改权证状态为在库）
					//更改押品状态为有效
					SqlClient.update("updateMortGuarantyBaseInfo", GUARANTY_NO, "3", null, connection);
					valueKc.addDataField("warrant_state", "3");
					valueKc.addDataField("sys_update_time",TRAN_DATE);
				}else if("08".equals(IN_OUT_TYPE)){
					SqlClient.update("updateMortGuarantyBaseInfo", GUARANTY_NO, "3", null, connection);
					valueKc.addDataField("warrant_state", "3");
					valueKc.addDataField("sys_update_time",TRAN_DATE);
				}
				SqlClient.update("updateMortGuarantyCertiInfo", paraKc, valueKc, null, connection);
				SqlClient.update("updateMortStorExwaDetail", paraKc, valueKc, null, connection);
			}
			
			//根据授权编号更新授权表的状态为授权已确认
			SqlClient.update("updatePvpAuthorizeStatusByGenNo", AUTH_NO, "04", null, connection);
			EMPLog.log("TradeDzywcrksqInform", EMPLog.INFO, 0, "【抵质押物权证出/入库通知】交易处理完成...", null);
			retKColl.addDataField("RetCd","000000");//成功码
		} catch (Exception e) {
			retKColl.setDataValue("RetCd", "999999");
			retKColl.setDataValue("RetInf", "【抵质押物权证出/入库通知】交易处理失败，权证编号为："+CERTI_NO+" 错误信息："+e.getMessage());
			e.printStackTrace();
			EMPLog.log("TradeDzywcrksqInform", EMPLog.ERROR, 0, "【抵质押物权证出/入库通知】交易处理失败，权证编号为："+CERTI_NO+"异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
