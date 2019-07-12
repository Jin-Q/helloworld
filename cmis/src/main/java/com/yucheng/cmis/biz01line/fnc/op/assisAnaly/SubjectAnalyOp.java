package com.yucheng.cmis.biz01line.fnc.op.assisAnaly;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.exception.ComponentException;

public class SubjectAnalyOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);			
			String cus_id = (String)context.getDataValue("cus_id");	//客户号
			String stat_style = (String)context.getDataValue("stat_style");	//报表口径，本部/合并报表
			String stat_prd = (String)context.getDataValue("stat_prd");	//报表期间，六位年月
			String stat_prd_style = (String)context.getDataValue("stat_prd_style");	//报表周期类型，年报月报等			
			DecimalFormat trans_num = new DecimalFormat("0.00"); //格式保留两位小数，与财报一致
			
			/**
			 * 调用规则[财务指标分析]，传入参数（IN_指标编号）的值是00到20，
			 * 传入00则代表所有指标分析，01-20则代表01到20号规则单独调用。
			 * 规则中变量命名：后缀11（资产负债表期初），12（资产负债表期未），21（损益表上年同期数），22（损益表本年累计数）
			 */
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			ShuffleServiceInterface shuffleService = null;
			try {
				shuffleService = (ShuffleServiceInterface) serviceJndi
						.getModualServiceById("shuffleServices", "shuffle");
			} catch (Exception e) {
				EMPLog.log("shuffle", EMPLog.ERROR, 0,"getModualServiceById error!", e);
				throw new EMPException(e);
			}
			Map<String, String> modelMap = new HashMap<String, String>();
			modelMap.put("IN_客户编号", cus_id);
			modelMap.put("IN_报表口径", stat_style);
			modelMap.put("IN_报表周期类型", stat_prd_style);
			modelMap.put("IN_报表期间", stat_prd);
			modelMap.put("IN_指标编号", "00");
			Map<String, String> outMap = new HashMap<String, String>();
			try {
				outMap = shuffleService.fireTargetRule("SUBJECTANALY","AllSubjectAnaly", modelMap);
			} catch (Exception e1) {
				throw new ComponentException(CMISMessage.QUERYERROR, "获取模型失败");
			}
			
			/*** 规则输出参数都是string，以','作为分隔符 ***/
			String values = "",memos = "",items="",types="";
			values = outMap.get("OUT_值");
			memos = outMap.get("OUT_备注");
			items = outMap.get("OUT_指标");
			types = outMap.get("OUT_类别");
			values = values.substring(1, values.length());
			memos = memos.substring(1, memos.length());
			items = items.substring(1, items.length());
			types = types.substring(1, types.length());
			
			
			IndexedCollection iColl = new IndexedCollection("subjectAnalyList");
			String[] value = values.split(",");
			String[] meno = memos.split(",");
			String[] item = items.split(",");
			String[] type = types.split(",");
			for(int i = 0;i<value.length;i++){
				KeyedCollection resultData = new KeyedCollection("result"+i);
				resultData.addDataField("titles", item[i]);
				if(value[i].indexOf("%")>0){	//%略作处理
					value[i] = value[i].replaceAll("%", "");
					resultData.addDataField("values", trans_num.format(Double.parseDouble(value[i]))+"%");
				}else{
					resultData.addDataField("values", trans_num.format(Double.parseDouble(value[i])));
				}				
				resultData.addDataField("memos", meno[i]);
				iColl.add(resultData);
			}
			
			/*** 相同的类别要作合并处理 ***/
			delType(type,context);			
			this.putDataElement2Context(iColl, context);
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}		
		return "0";
	}

	/*** 合并相同类别，处理后便于页面上表格显示 ***/
	public void delType(String[] type,Context context) throws EMPException{
		int i,j=0;
		String item = type[0];		
		String types = type[0];
		String cols = "0";
		for(i=1;i<type.length;i++){
			if (!item.equals(type[i])) {
				j++;
				cols = cols+","+ i;
				types = types+","+ type[i];
				item = type[i];
			}
		}
		
		context.addDataField("cols", cols+","+type.length);
		context.addDataField("types", types);
	}
}