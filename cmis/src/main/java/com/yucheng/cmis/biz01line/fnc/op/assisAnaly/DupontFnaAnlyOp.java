package com.yucheng.cmis.biz01line.fnc.op.assisAnaly;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.exception.ComponentException;

public class DupontFnaAnlyOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			String analy_type = (String)context.getDataValue("analy_type");	//分析类型
			if(analy_type.equals("21")){//21杜邦普通分析
				delDupontAnaly(context);
			}else if(analy_type.equals("22")){//21杜邦综合分析
				delDupontChart(context);
			}
			EMPLog.log("MESSAGE", EMPLog.DEBUG, 0, context.toString(), null);
		}catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)this.releaseConnection(context, connection);
		}
		return "0";
	}
	
	/**** 21杜邦普通分析 by GC 20130826*****/
	public void delDupontAnaly(Context context ) throws EMPException {
		String cus_id = (String)context.getDataValue("cus_id");	//客户号
		String stat_style = (String)context.getDataValue("stat_style");	//报表口径，本部/合并报表
		String stat_prd = (String)context.getDataValue("stat_prd");	//报表期间，六位年月
		String stat_prd_style = (String)context.getDataValue("stat_prd_style");	//报表周期类型，年报月报等
		DecimalFormat trans_num = new DecimalFormat("0.0000");
		int i = 0;
		
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		ShuffleServiceInterface shuffleService = null;
		try {
			shuffleService = (ShuffleServiceInterface) serviceJndi
					.getModualServiceById("shuffleServices", "shuffle");
		} catch (Exception e) {
			EMPLog.log("shuffle", EMPLog.ERROR, 0,"getModualServiceById error!", e);
			throw new EMPException(e);
		}
		
		/***1.处理在表中取到的基本项**/
		String[] item_name = ("主营业务收入,净利润,资产总计,财务费用,管理费用,营业费用,主营业务成本,其他业务利润,所得税," +
				"流动资产合计,长期投资,固定资产,无形资产,货币资金,短期投资,应收账款,存货,其他流动资产,其他长期资产,负债合计").split(",");
		String[] item_no = ("L01000000,L06010000,Z01000001,L03050000,L03040000,L03030000,L02020000,L03020000," +
				"L05020000,Z01010001,Z01020100,Z01030001,Z01040100,Z01010100,Z01010210,Z01010610,Z01011210," +
				"Z01011600,Z01040300,Z02000001").split(",");
		float[] item_value = new float[item_no.length]; //存放取到的值
		
		for(i = 0 ; i < item_no.length ; i++){
			Map<String, String> modelMap=new HashMap<String, String>();
			modelMap.put("IN_客户编号", cus_id);
			modelMap.put("IN_项目编号", item_no[i]);
			modelMap.put("IN_报表口径", stat_style);
			modelMap.put("IN_报表周期类型", stat_prd_style);
			modelMap.put("IN_报表期间", stat_prd);
			Map<String, String> outMap=new HashMap<String, String>();
			try {
				outMap=shuffleService.fireTargetRule("FNCANALYSIS", "FncAnalyseAll", modelMap);
			} catch (Exception e1) {
				throw new ComponentException(CMISMessage.QUERYERROR,"获取模型失败");
			}
			item_value[i] =Float.parseFloat(outMap.get("OUT_期末数"));
		}
		
		/***2.计算杜邦分析结果：***/
		String[] result_name = ("长期资产,全部成本,资产总额,总资产周转率," +
				"主营业务利润率,总资产收益率,权益乘数,净资产收益率").split(",");
		float[] result_value = new float[result_name.length]; //存放计算结果
		/*** (1)长期资产 = 长期投资+固定资产+无形资产+其他长期资产 ***/
		result_value[0] = item_value[10] + item_value[11] + item_value[12] + item_value[18];		
		/*** (2)全部成本 = 主营业务成本+营业费用+管理费用+财务费用 ***/		
		result_value[1] = item_value[6] + item_value[5] + item_value[4] + item_value[3];		
		/*** (3)资产总额 = 资产总计 ***/		
		result_value[2] = item_value[2];		
		/*** (4)总资产周转率 = 主营业务收入/资产总额 ***/		
		result_value[3] = item_value[0]/result_value[2];		
		/*** (5)主营业务利润率 = 净利润/主营业务收入 ***/		
		result_value[4] = item_value[1]/item_value[0];		
		/*** (7)权益乘数 = 1/（1-负债总额/资产总额）x100％，（这里只算到：负债总额/资产总额） ***/		
		result_value[6] = item_value[19]/item_value[2];
		
		/***3.数据出现除0时特殊处理：***/
		if(result_value[2] == 0 ){
			result_value[3] = 0 ;
			result_value[6] = 0 ;
		}
		if(item_value[0] == 0 ){
			result_value[4] = 0 ;
		}
		/*** (6)总资产收益率 = 主营业务利润率*总资产周转率 ***/
		result_value[5] = result_value[3] * result_value[4];
		/*** (8)净资产收益率 = 总资产收益率*权益乘数 ***/
		result_value[7] = result_value[5] * (1 / (1-item_value[19]/item_value[2]));
		if(result_value[2] == 0 ){
			result_value[7] = 0 ;
		}
		
		
		/***4.结尾传值，并处理保留小数、格式问题：***/
		KeyedCollection kColl = new KeyedCollection("DupontAnaly") ;
		for(i = 0 ; i< item_no.length ; i++){
			kColl.addDataField(item_name[i], trans_num.format(item_value[i]));
		}
		
		for(i = 0 ; i< result_value.length ; i++){
			kColl.addDataField(result_name[i], trans_num.format(result_value[i]));
		}
		
		this.putDataElement2Context(kColl, context);
	}
	
	/**** 22杜邦综合分析，综合分析必须支持多条报表记录处理 by GC 20130826*****/
	public void delDupontChart(Context context) throws EMPException {
		String[] cus_id = ((String)context.getDataValue("cus_id")).split(",");	//客户号
		String[] stat_style = ((String)context.getDataValue("stat_style")).split(",");	//报表口径，本部/合并报表
		String[] stat_prd = ((String)context.getDataValue("stat_prd")).split(",");	//报表期间，六位年月
		String[] stat_prd_style = ((String)context.getDataValue("stat_prd_style")).split(",");	//报表周期类型，年报月报等
		DecimalFormat trans_num = new DecimalFormat("0.0000"); //将科学计数法下的float值转为正常格式
		int i = 0,j = 0;
		
		/***1.处理在表中取到的基本项**/
		String[] item_name = ("主营业务收入,净利润,资产总计,财务费用,管理费用,营业费用,主营业务成本,其他业务利润,所得税," +
				"流动资产合计,长期投资,固定资产,无形资产,货币资金,短期投资,应收账款,存货,其他流动资产,其他长期资产,负债合计").split(",");
		String[] item_no = ("L01000000,L06010000,Z01000001,L03050000,L03040000,L03030000,L02020000,L03020000," +
				"L05020000,Z01010001,Z01020100,Z01030001,Z01040100,Z01010100,Z01010210,Z01010610,Z01011210," +
				"Z01011600,Z01040300,Z02000001").split(",");
		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		ShuffleServiceInterface shuffleService = null;
		try {
			shuffleService = (ShuffleServiceInterface) serviceJndi
					.getModualServiceById("shuffleServices", "shuffle");
		} catch (Exception e) {
			EMPLog.log("shuffle", EMPLog.ERROR, 0,"getModualServiceById error!", e);
			throw new EMPException(e);
		}		
		
		for(j = 0 ; j < cus_id.length ; j++){
			float[] item_value = new float[item_no.length]; //存放取到的值		
			for(i = 0 ; i < item_no.length ; i++){
				Map<String, String> modelMap=new HashMap<String, String>();
				modelMap.put("IN_客户编号", cus_id[j]);
				modelMap.put("IN_项目编号", item_no[i]);
				modelMap.put("IN_报表口径", stat_style[j]);
				modelMap.put("IN_报表周期类型", stat_prd_style[j]);
				modelMap.put("IN_报表期间", stat_prd[j]);
				Map<String, String> outMap=new HashMap<String, String>();
				try {
					outMap=shuffleService.fireTargetRule("FNCANALYSIS", "FncAnalyseAll", modelMap);
				} catch (Exception e1) {
					throw new ComponentException(CMISMessage.QUERYERROR,"获取模型失败");
				}
				item_value[i] =Float.parseFloat(outMap.get("OUT_期末数"));//杜邦分析只取期末数
			}
			
			/***2.计算杜邦分析结果：***/
			String[] result_name = ("长期资产,全部成本,资产总额,总资产周转率," +
					"主营业务利润率,总资产收益率,权益乘数,净资产收益率").split(",");
			float[] result_value = new float[result_name.length]; //存放计算结果
			/*** (1)长期资产 = 长期投资+固定资产+无形资产+其他长期资产 ***/
			result_value[0] = item_value[10] + item_value[11] + item_value[12] + item_value[18];		
			/*** (2)全部成本 = 主营业务成本+营业费用+管理费用+财务费用 ***/		
			result_value[1] = item_value[6] + item_value[5] + item_value[4] + item_value[3];		
			/*** (3)资产总额 = 资产总计 ***/		
			result_value[2] = item_value[2];		
			/*** (4)总资产周转率 = 主营业务收入/资产总额 ***/		
			result_value[3] = item_value[0]/result_value[2];		
			/*** (5)主营业务利润率 = 净利润/主营业务收入 ***/		
			result_value[4] = item_value[1]/item_value[0];	
			/*** (7)权益乘数 = 1/（1-负债总额/资产总额）x100％ ***/
			result_value[6] = 1/(1-item_value[19]/item_value[2]);
			
			/***3.数据出现除0时特殊处理：***/
			if(result_value[2] == 0 ){
				result_value[3] = 0 ;
				result_value[6] = 0 ;
			}
			if(item_value[0] == 0 ){
				result_value[4] = 0 ;
			}
			/*** (6)总资产收益率 = 主营业务利润率*总资产周转率 ***/
			result_value[5] = result_value[3] * result_value[4];
			/*** (8)净资产收益率 = 总资产收益率*权益乘数 ***/
			result_value[7] = result_value[5] * (1 / (1-item_value[19]/item_value[2]));
			if(result_value[2] == 0 ){
				result_value[7] = 0 ;
			}			
			
			/***4.结尾传值，并处理保留小数、格式问题：***/
			KeyedCollection kColl = new KeyedCollection("DupontAnaly"+j) ;
			for(i = 0 ; i< item_no.length ; i++){
				kColl.addDataField(item_name[i], trans_num.format(item_value[i]));
			}			
			for(i = 0 ; i< result_value.length ; i++){
				kColl.addDataField(result_name[i], trans_num.format(result_value[i]));
			}			
			this.putDataElement2Context(kColl, context);
		}
	}	
}