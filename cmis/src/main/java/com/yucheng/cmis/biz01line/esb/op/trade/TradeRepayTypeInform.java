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
import com.yucheng.cmis.biz01line.esb.pub.TagUtil;
import com.yucheng.cmis.pub.dao.SqlClient;
/**
 * 新增还款方式通知
 * @author Pansq
 * 全表清除还款方式表数据，加载核算传过来的数据
 * 全表清除还款策略表数据，加载核算传过来的数据
 * 
 */
public class TradeRepayTypeInform extends TranService {

	@Override
	public KeyedCollection doExecute(CompositeData CD, Connection connection)
			throws Exception {
		KeyedCollection retKColl = TagUtil.getDefaultResultKColl();
		try {
			ESBInterface esbInterface = new ESBInterfacesImple();
			KeyedCollection reqBody = esbInterface.getReqBody(CD);
			IndexedCollection PrdRepayMode = (IndexedCollection)reqBody.getDataElement("REPAY_TYPE_ARRAY");//还款方式数据
			IndexedCollection PrdRepayPlan = (IndexedCollection)reqBody.getDataElement("REPAY_STRA_ARRAY");//还款策略数据
			//清除还款方式全表数据
			SqlClient.delete("deletePrdRepayMode", null, connection);
			//清除还款策略全表数据
			SqlClient.delete("deletePrdRepayPlan", null, connection);
			Map param = new HashMap();
			//循环遍历还款方式数据，逐条插入
			for(int i=0;i<PrdRepayMode.size();i++){
				KeyedCollection tmp = (KeyedCollection)PrdRepayMode.get(i);
				String REPAY_MODE_ID        = (String)tmp.getDataValue("REPAY_TYPE_CODE");
				String REPAY_MODE_DEC       = (String)tmp.getDataValue("REPAY_TYPE_DESC");
				String REPAY_MODE_TYPE      = (String)tmp.getDataValue("REPAY_TYPE_KIND");
				String MIN_TERM             = (String)tmp.getDataValue("SUPPORT_MIN_PERIODS").toString();
				String MAX_TERM             = (String)tmp.getDataValue("SUPPORT_MAX_PERIODS").toString();
				String INCR_DECL_BASIC      = (String)tmp.getDataValue("ADD_OR_REDUCE_BASE");
				String CAP_INTERVAL         = (String)tmp.getDataValue("CORPUS_REPAY_FREQUENCY");
				String INCR_REPAY_CYCLE     = (String)tmp.getDataValue("ADD_REPAY_PERIOD");
				String CAP_INCR_DECL_PERC   = (String)tmp.getDataValue("CORPUS_ADD_OR_REDUCE_RATE").toString();
				String CAP_INCR_DECL_AMT    = (String)tmp.getDataValue("CORPUS_ADD_OR_REDUCE_AMT").toString();
				String REPAY_INTERVAL       = (String)tmp.getDataValue("REPAY_FREQUENCY");
				String PERC_PHASE           = (String)tmp.getDataValue("DIVIDE_PERIOD_FLAG");
				String CAP_PERC_UNIT        = (String)tmp.getDataValue("CORPUS_PERCENT_UNIT");
				String FIRSTPAY_PERC        = (String)tmp.getDataValue("FIRST_PAY_PERCENT").toString();
				String LASTPAY_PERC         = (String)tmp.getDataValue("LAST_PAY_PERCENT").toString();
				String PARAM_STATUS         = (String)tmp.getDataValue("PARAM_STATUS");
				String CHANGE_TIME          = (String)tmp.getDataValue("LAST_CHANGE_TIME");
				String CHANGE_USER          = (String)tmp.getDataValue("LAST_CHANGE_USER");
				String IS_INSTM             = (String)tmp.getDataValue("IS_TERM_PAY_FLAG");
				param.clear();
				param.put("repay_mode_id",REPAY_MODE_ID);
			    param.put("repay_mode_dec",REPAY_MODE_DEC);
			    param.put("repay_mode_type",REPAY_MODE_TYPE);
				param.put("min_term",MIN_TERM);
				param.put("max_term",MAX_TERM);
				param.put("incr_decl_basic",INCR_DECL_BASIC);
				param.put("cap_interval",CAP_INTERVAL);
				param.put("incr_repay_cycle",INCR_REPAY_CYCLE);
				param.put("cap_incr_decl_perc",CAP_INCR_DECL_PERC);
				param.put("cap_incr_decl_amt",CAP_INCR_DECL_AMT);
				param.put("repay_interval",REPAY_INTERVAL);
				param.put("perc_phase",PERC_PHASE);
				param.put("cap_perc_unit",CAP_PERC_UNIT);
				param.put("firstpay_perc",FIRSTPAY_PERC);
				param.put("lastpay_perc",LASTPAY_PERC);
				param.put("param_status",PARAM_STATUS);
				param.put("change_time",CHANGE_TIME);
				param.put("change_user",CHANGE_USER);
				if(IS_INSTM.equals("Y")){
					IS_INSTM = "1";
				}else{
					IS_INSTM = "2";
				}
				param.put("is_instm",IS_INSTM);
				//插入还款方式表
				SqlClient.insert("insertPrdRepayMode", param, connection);
			}
			//循环遍历还款策略数据，逐条插入
			for(int i=0;i<PrdRepayPlan.size();i++){
				KeyedCollection tmp = (KeyedCollection)PrdRepayPlan.get(i);
				String SERNO              = "000000"+i ;//数据量少，每次都有全量清表，流水号以循环次数填充
				String REPAY_MODE_ID      = (String) tmp.getDataValue("REPAY_TYPE_CODE");
				String EXE_TIMES          = (String) tmp.getDataValue("TOTAL_REPAY_COUNT").toString();
				String CAP_PERC           = (String) tmp.getDataValue("CORPUS_PERCENT").toString();
				String CAL_TERM           = (String) tmp.getDataValue("CALCULATE_TERM").toString();
				String REPAY_MODE         = (String) tmp.getDataValue("LOAN_REPAY_METHOD");
				String REPAY_TYPE         = (String) tmp.getDataValue("LOAN_REPAY_TYPE");
				String INT_CAL_BASIC      = (String) tmp.getDataValue("INTEREST_BASE");
				String IS_UPDATE          = (String) tmp.getDataValue("MOD_FLAG");
				String RATE_TYPE          = (String) tmp.getDataValue("BASE_INT_RATE_CODE");
				String RATE_MODE          = (String) tmp.getDataValue("INT_RATE_MODE");
				String RATE_SPRD          = (String) tmp.getDataValue("SPREAD").toString();
				String RATE_PEFLOAT       = (String) tmp.getDataValue("INT_FLT_RATE").toString();
				String RATE_CAL_BASIC     = (String) tmp.getDataValue("INT_RATE_BASE");
				String NEW_CHANGE_TIME    = (String) tmp.getDataValue("LAST_CHANGE_TIME");
				String NEW_CHANGE_USER    = (String) tmp.getDataValue("LAST_CHANGE_USER");
				String REPAY_TREM         = (String) tmp.getDataValue("REPAY_FREQUENCY");
				String REPAY_INTERVAL     = (String) tmp.getDataValue("REPAY_FREQ_NUM").toString();
				param.clear();
				param.put("serno",SERNO);
				param.put("repay_mode_id",REPAY_MODE_ID);
				param.put("exe_times",EXE_TIMES);
				param.put("cap_perc",CAP_PERC);
			    param.put("cal_term",CAL_TERM);
				param.put("repay_mode",REPAY_MODE);
				param.put("repay_type",REPAY_TYPE);
				param.put("int_cal_basic",INT_CAL_BASIC);
				param.put("is_update",IS_UPDATE);
				param.put("rate_type",RATE_TYPE);
				param.put("rate_mode",RATE_MODE);
				param.put("rate_sprd",RATE_SPRD);
				param.put("rate_pefloat",RATE_PEFLOAT);
				param.put("rate_cal_basic",RATE_CAL_BASIC);
				param.put("new_change_time",NEW_CHANGE_TIME);
				param.put("new_change_user",NEW_CHANGE_USER);
				param.put("repay_trem",REPAY_TREM);
				param.put("repay_interval",REPAY_INTERVAL);
				//插入还款策略表
				SqlClient.insert("insertPrdRepayPlan", param, connection);
			}
			
			EMPLog.log("TradeRepayTypeInform", EMPLog.INFO, 0, "【新增还款方式通知】交易处理完成...", null);
		} catch (Exception e) {
			retKColl.setDataValue("ret_code", "999999");
			retKColl.setDataValue("ret_msg", "【新增还款方式通知】 ,业务处理失败！");
			e.printStackTrace();
			EMPLog.log("TradeRepayTypeInform", EMPLog.ERROR, 0, "【新增还款方式通知】交易处理失败,异常信息为："+e.getMessage(), null);
		}
		return retKColl;
	}

}
