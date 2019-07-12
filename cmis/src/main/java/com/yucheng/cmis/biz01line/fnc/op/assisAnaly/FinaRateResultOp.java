package com.yucheng.cmis.biz01line.fnc.op.assisAnaly;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.flashcharts.FCFCharHelper;
import com.yucheng.cmis.platform.shuffle.msi.ShuffleServiceInterface;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.exception.ComponentException;

public class FinaRateResultOp extends CMISOperation {
	private int records_length; //财报记录数量

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String[] cus_id = ((String)context.getDataValue("cus_id")).split(",");	//客户号
			String[] stat_prd = ((String)context.getDataValue("stat_prd")).split(",");	//报表期间，六位年月
			String chartType = (String)context.getDataValue("chartType");	//图形样式：柱状bar，线状line
			String display_type = (String)context.getDataValue("display_type");	//是否仅显示比较：是/否
			String showType = (String)context.getDataValue("showType");	//显示结果类型：百分比(1-前/后)，绝对值(后-前)等
			String rate_type = (String)context.getDataValue("rate_type");	//财务比率分类 ：营运能力分析，长期偿债能力分析，短期偿债能力分析
			
			/*** 新建若干处理所需变量 ***/
			if(rate_type.equals(""))rate_type = "001,002,003";
			records_length = cus_id.length;
			KeyedCollection[] kColl_count = new KeyedCollection[records_length*2];	//kcoll用于生成页面表格
			IndexedCollection[] iColls;	//icoll用于生成页面flash图像
			String[] yName = stat_prd;
			int i,j,num=0;
			String multiXML ="";	//flash生成参数
			
			/*** 设计上页面表格的一纵列对应一个kcoll，这里处理结果kcoll，如果带分析期的还要加上分析期kcoll ***/
			String[] item_name = delShuffle(context, rate_type, kColl_count);
			if(showType.equals("02")||showType.equals("03")){
				delCount(kColl_count,showType);
				iColls = new IndexedCollection[records_length*2-1];
				yName = delYname(yName);
			}else{
				iColls = new IndexedCollection[records_length];
			}			
			for(j = 0 ; j < kColl_count.length ; j++){
				if(kColl_count[j] != null){
					iColls[num] = new IndexedCollection("FinaRateAnaly"+num);
					for (i = 0; i < item_name.length; i++) {
						KeyedCollection kColl = new KeyedCollection();
						kColl.addDataField("xName", item_name[i]);
						kColl.addDataField("yName", ((DataField) kColl_count[j].getDataElement(i)).getValue());
						iColls[num].add(kColl);
					}
					num++;
				}			
			}			

			/*** 生成flash所需icoll数组后，配flash样式，并调用通用方法生成xml ***/
			KeyedCollection styles = new KeyedCollection();
			if(chartType.equals("bar")){
				styles.addDataField("Caption", "财务比率分析柱状图");
			}else if(chartType.equals("line")){
				styles.addDataField("Caption", "财务比率分析线状图");
			}			
			styles.addDataField("ShowName", "1");
			styles.addDataField("FormatNumberScale", "0");
			styles.addDataField("Subcaption", "");
			if(display_type.equals("1") && records_length >1 && !showType.equals("01")){
				IndexedCollection[] iColl_Chart = new IndexedCollection[records_length-1];
				String[] yName_Chart = new String[records_length-1];
				if(!showType.equals("01")){
					for(i = 0 ; i < records_length-1; i ++){
						yName_Chart[i] = yName[2*i+2];
						iColl_Chart[i]=iColls[2*i+2];
					}
				}
				multiXML = FCFCharHelper.createXMLDataMultis(iColl_Chart,styles, yName_Chart);
			}else{
				multiXML = FCFCharHelper.createXMLDataMultis(iColls, styles, yName);
			}
			
			/*** 处理后台数据，向页面传值 ***/			
			KeyedCollection itemName = new KeyedCollection("item");
			for(j=0;j<item_name.length;j++){
				itemName.addDataField("item"+j, item_name[j]);
			}
			this.putDataElement2Context(itemName, context);	//财报项项目名称放在item的kcoll中
			
			String y_name = "";
			for(i=0;i<yName.length;i++){	//表头名称
				y_name = y_name + ","+yName[i];
			}
			
			j=0;
			for(i = 0 ; i < kColl_count.length ; i++){
				if(kColl_count[i] != null){
					kColl_count[i].setId("FinaRateAnaly"+j);
					this.putDataElement2Context(kColl_count[i], context);	//财报项值存在FinaRateAnalyi的kcoll数组中
					j++;
				}
			}
			
			context.addDataField("multiXML", multiXML);	//生成flash
			context.addDataField("yName", y_name);
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
	
	/**
	 * 调用规则[财务比率分析总调用]，传入参数（IN_指标编号）的值是100营运能力分析，200长期偿债能力分析，300短期偿债能力分析
	 * 规则中变量命名：后缀11（资产负债表期初），12（资产负债表期未），21（损益表上年同期数），22（损益表本年累计数）
	 * @throws Exception 
	 */
	public String[] delShuffle(Context context , String rate_type,KeyedCollection[]kColl_count) 
		throws  Exception {
		String[] cus_id = ((String)context.getDataValue("cus_id")).split(",");	//客户号
		String[] stat_style = ((String)context.getDataValue("stat_style")).split(",");	//报表口径，本部/合并报表
		String[] stat_prd = ((String)context.getDataValue("stat_prd")).split(",");	//报表期间，六位年月
		String[] stat_prd_style = ((String)context.getDataValue("stat_prd_style")).split(",");	//报表周期类型，年报月报等
		String[] item_name = null ;

		CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
		ShuffleServiceInterface shuffleService = null;
		try {
			shuffleService = (ShuffleServiceInterface) serviceJndi
					.getModualServiceById("shuffleServices", "shuffle");
		} catch (Exception e) {
			EMPLog.log("shuffle", EMPLog.ERROR, 0,"getModualServiceById error!", e);
			throw new EMPException(e);
		}
		
		/*** 多记录处理，按记录条数循环 ***/
		for(int i=0;i<records_length;i++){
			Map<String, String> modelMap = new HashMap<String, String>();
			modelMap.put("IN_客户编号", cus_id[i]);
			modelMap.put("IN_报表口径", stat_style[i]);
			modelMap.put("IN_报表周期类型", stat_prd_style[i]);
			modelMap.put("IN_报表期间", stat_prd[i]);
			modelMap.put("IN_指标编号", rate_type);
			Map<String, String> outMap = new HashMap<String, String>();
			try {
				outMap = shuffleService.fireTargetRule("SUBJECTANALY","AllFinaRateAnaly", modelMap);
			} catch (Exception e1) {
				throw new ComponentException(CMISMessage.QUERYERROR, "获取模型失败");
			}
			
			/*** 规则输出参数都是string，以','作为分隔符 ***/
			String values = "",items="";
			values = outMap.get("OUT_值");
			items = outMap.get("OUT_指标");
			values = values.substring(1, values.length()).replaceAll("%", "");
			items = items.substring(1, items.length());
			String[] value = values.split(",");
			String[] item = items.split(",");
			
			kColl_count[2*i] = new KeyedCollection();
			for(int j=0;j<value.length;j++){
				kColl_count[2*i].addDataField("item"+j, value[j]);	//将规则项的值放入kColl_count
				if(i == records_length-1){
					item_name = item;	//将规则项的名称放入item_name
				}
			}
		}
		return item_name;
	}
	
	/********* 分析类型为绝对值、百分比时，生成一列新的kcoll作为比对列 **********/
	public void delCount(KeyedCollection[] kColls,String showType) throws EMPException {
		int i = 0,j=0;
		double db_mv1,db_mv2;
		DecimalFormat trans_num = new DecimalFormat("0.00");
		for(i=0;i<kColls.length;i++){
			if(i > 2 && kColls[i] == null){
				kColls[i] = new KeyedCollection("FinaRateAnaly"+i);
				for(j=0;j < kColls[i-1].size();j++){
					db_mv1 = Double.parseDouble(((DataField)kColls[i-1].getDataElement(j)).getValue().toString());
					db_mv2 = Double.parseDouble(((DataField)kColls[i-3].getDataElement(j)).getValue().toString());
					kColls[i].addDataField(kColls[i-1].getDataElement(j).getName().toString(), 
						(showType.equals("03"))?getCount(db_mv1,db_mv2,showType):
						trans_num.format(getCount(db_mv1,db_mv2,showType)*100)+"%");
				}
			}
		}
	}
	
	/********* 分析类型为绝对值、百分比时值的计算处理 **********/
	public double getCount(double db_mv1,double db_mv2 ,String showType) {
		double db_retv = 0;		
		if (showType.equals("03")) { // 绝对值
			db_retv = db_mv1 - db_mv2;
		} else if (showType.equals("02")) { // 百分比
			db_retv = db_mv2 == 0 ?0.00:(db_mv1 - db_mv2) / db_mv2;
		}
		return db_retv;
	}
	
	/********* 分析类型为绝对值、百分比时项目名称的处理 **********/
	public String[] delYname(String[] yName) {
		String[] newName = new String[yName.length*2-1];
		for(int i=0 ; i<newName.length;i++){
			if(i<2){
				newName[i] = yName[i];
			}else if(i%2!=0){
				newName[i] = yName[(1+i)/2];
			}else if(i==2){
				newName[i] = newName[i-2] + "-" + newName[i-1]+"期比对";
			}else{
				newName[i] = newName[i-3] + "-" + newName[i-1]+"期比对";
			}
		}
		return newName;
	}
	
}