package com.yucheng.cmis.biz01line.fnc.op.assisAnaly;

import java.sql.Connection;
import java.text.DecimalFormat;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.flashcharts.FCFCharHelper;

public class ShowFlashChartOp extends CMISOperation{
	
	/**
	 * 财报结果要在页面上同时显示flash和表格
	 * 设计思路：
	 * 一个kcoll生成一纵列表格，将分析结果存入kcoll数组，并传到页面处理
	 * flash只要按工具格式生成参数即可，将分析结果存入icoll数组并调用通用方法
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String fncType = (String)context.getDataValue("fncType"); //报表类型：资产负债表，损益表等			
			if(fncType.equals("dupont")){	//杜邦综合分析
				getDopondAnaly(context);
			}else if(fncType.equals("is")||fncType.equals("bs")){	//结构分析
				getStruAnaly(fncType,context);
			}
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

	/******************************************* 杜邦分析图表显示处理begin ************************************************/
	/***杜邦分析多记录处理***/
	public void getDopondAnaly(Context context) throws EMPException{
		String showType = (String)context.getDataValue("showType");//结果显示类型
		String selected = (String)context.getDataValue("selected"); //被选财报项
		String chartType = (String)context.getDataValue("chartType");//图形类型：线形图，柱形图等
		String display_type = (String)context.getDataValue("display_type"); //是否仅显示比较
		String[] yName = ((String)context.getDataValue("stat_prd")).split(",");	//报表期间，六位年月
		int lengths = yName.length;
		String[] result_name = ("主营业务收入,净利润,资产总计,财务费用,管理费用,营业费用,主营业务成本," +
				"其他业务利润,所得税,流动资产合计,长期投资,固定资产,无形资产,货币资金,短期投资,应收账款," +
				"存货,其他流动资产,其他长期资产,负债合计,长期资产,全部成本,资产总额,总资产周转率,主营业务利润率," +
				"总资产收益率,权益乘数,净资产收益率").split(","); //财报项的名称，杜邦分析是固定的
		selected = selected.replaceAll("text", ""); //去掉传值String里的'text'字符串
		selected = selected.substring(1, selected.length()-1); //去掉前后的","
		String[] select = selected.split(",");	//拆分成数组
		IndexedCollection[] iColls;
		int j = 0,i = 0,num=0;
		
		/*** 调用规则取报表项的值 ***/
		DupontFnaAnlyOp del = new DupontFnaAnlyOp();
		del.delDupontChart(context);
		
		/*** 将财报项的值分别放入kcoll与icoll数组 ***/
		KeyedCollection[] kColl_count = new KeyedCollection[yName.length*2];	//kcoll数组，用来生成表格
		for(j = 0 ; j < yName.length ; j++){
			kColl_count[j*2] = (KeyedCollection)context.getDataElement("DupontAnaly"+j);
		}
		if(showType.equals("02")||showType.equals("03")){
			delCount(kColl_count,showType);
			iColls = new IndexedCollection[yName.length*2-1];
			yName = delYname(yName);
		}else{
			iColls = new IndexedCollection[yName.length];
		}
		
		for(j = 0 ; j < kColl_count.length ; j++){
			if(kColl_count[j] != null){
				iColls[num] = new IndexedCollection("DupontAnaly"+num);
				for (i = 0; i < select.length; i++) {
					KeyedCollection kColl = new KeyedCollection();
					kColl.addDataField("xName", result_name[Integer.parseInt(select[i])]);
					kColl.addDataField("yName", ((DataField) kColl_count[j]
							.getDataElement(Integer.parseInt(select[i]))).getValue().toString());
					iColls[num].add(kColl);
				}
				num++;
			}			
		}
		
		/*** 完成数据处理后调用flash组件处理 ***/
		if(chartType.equals("bar")){
			chartType = "杜邦综合分析柱状图";
		}else if(chartType.equals("line")){
			chartType = "杜邦综合分析线状图";
		}
		
		String multiXML ="";		
		KeyedCollection styles = new KeyedCollection();
		styles.addDataField("Caption", chartType);
		styles.addDataField("ShowName", "1");
		styles.addDataField("FormatNumberScale", "0");
		styles.addDataField("Subcaption", "");
		if(display_type.equals("1") && lengths >1 && !showType.equals("01")){
			IndexedCollection[] iColl_Chart = new IndexedCollection[lengths-1];
			String[] yName_Chart = new String[lengths-1];
			if(!showType.equals("01")){
				for(i = 0 ; i < lengths-1; i ++){
					yName_Chart[i] = yName[2*i+2];
					iColl_Chart[i]=iColls[2*i+2];
				}
			}
			multiXML = FCFCharHelper.createXMLDataMultis(iColl_Chart,styles, yName_Chart);
		}else{
			multiXML = FCFCharHelper.createXMLDataMultis(iColls, styles, yName);
		}		
		context.addDataField("multiXML", multiXML);
		
		/*** 根据前一页面选项的不同调整不同的返回值放入context ***/
		if(showType.equals("02")||showType.equals("03")){
			j=0;
			String str = "";
			for(i = 0 ; i < yName.length ; i++){	//移除context中已有DupontAnaly名称的kcoll
				context.removeDataElement("DupontAnaly"+i);
			}
			
			for(i = 0 ; i < kColl_count.length ; i++){	//将kcoll数据放入context
				if(kColl_count[i] != null){
					kColl_count[i].setId("DupontAnaly"+j);
					this.putDataElement2Context(kColl_count[i], context);
					j++;
				}
			}
			for(i = 0 ; i < yName.length ; i++){	//表格表头名称
				str = str+","+yName[i];
			}
			context.addDataField("records", j);
			context.addDataField("yName", str);
		}		
	}
	
	/********* 杜邦分析类型为绝对值、百分比时，生成一系列新的kcoll **********/
	public void delCount(KeyedCollection[] kColls,String showType) throws EMPException {
		int i = 0,j=0;
		double db_mv1,db_mv2;
		DecimalFormat trans_num = new DecimalFormat("0.00");
		for(i=0;i<kColls.length;i++){
			if(i > 2 && kColls[i] == null){
				kColls[i] = new KeyedCollection("DupontAnaly"+i);
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
	
	/********* 杜邦分析类型为绝对值、百分比时的计算 **********/
	public double getCount(double db_mv1,double db_mv2 ,String showType) {
		double db_retv = 0;		
		if (showType.equals("03")) { // 绝对值
			db_retv = db_mv1 - db_mv2;
			/*if(db_retv < 0){
				db_retv = db_retv * -1.00;
			}*/
		} else if (showType.equals("02")) { // 百分比
			db_retv = db_mv2 == 0 ?0.00:(db_mv1 - db_mv2) / db_mv2;
		}
		return db_retv;
	}
	/********* 杜邦分析类型为绝对值、百分比时的计算 **********/
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
	/******************************************* 杜邦分析图表显示处理end ************************************************/
	
	/******************************************* 结构分析图表显示处理begin ************************************************/
	public void getStruAnaly(String fncType , Context context) throws EMPException{
		
		String selected = (String)context.getDataValue("selected"); //被选财报项
		selected = selected.replaceAll("text", ""); //去掉传值String里的'text'字符串
		selected = selected.substring(1, selected.length()-1); //去掉前后的","
		String[] select = selected.split(",");	//拆分成数组
		String chartType = (String)context.getDataValue("chartType");//图形类型：线形图，柱形图等
		String showType = (String)context.getDataValue("showType");//结果包含内容
		String[] stat_prd = ((String)context.getDataValue("stat_prd")).split(",");	//报表期间，六位年月
		KeyedCollection[] kColl_count = new KeyedCollection[stat_prd.length*2];
		IndexedCollection[] iColls = new IndexedCollection[stat_prd.length*2];
		int i=0,j = 0;
		double yValue = 0.00,keyValue =0.00;
		String key_item =fncType.equals("is")?"L01000000":"Z01000001";
		
		/*** 授限于传值问题，这里只好重新取值一次 ***/
		StruFncAnaly del = new StruFncAnaly();
		del.delStruAnaly(context);
		IndexedCollection icoll_item = (IndexedCollection)context.getDataElement("FncConfDefFmt");
		context.removeDataElement("FncConfDefFmt");
		KeyedCollection kColl_item = new KeyedCollection("item");
		for(j=0;j<icoll_item.size();j++){
			KeyedCollection kcoll = (KeyedCollection)icoll_item.getElementAt(j);
			kColl_item.addDataField(kcoll.getDataValue("item_id").toString(), kcoll.getDataValue("item_id_displayname"));			
		}
		this.putDataElement2Context(kColl_item, context);
		
		/**	损益表 结构分析中是以主营业务收入为100％，计算其它财务指标的百分比。 L01000000_主营业务收入_1
        	资产负债表 结构分析中是以资产合计为100％，计算其他财务指标的百分比。 Z01000001_资产总计_56 */		
		for(j = 0 ; j < stat_prd.length ; j++){
			KeyedCollection kColl_value = (KeyedCollection)context.getDataElement("StruAnaly"+j);
			keyValue = Double.parseDouble(((DataField) kColl_value.getDataElement(key_item)).getValue().toString());
			if(!showType.equals("02")){
				kColl_count[2*j] = new KeyedCollection("StruAnaly"+2*j);
				iColls[2*j] = new IndexedCollection("StruAnaly"+2*j);
			}
			if(!showType.equals("03")){
				kColl_count[2*j+1] = new KeyedCollection("StruAnaly"+(2*j+1));
				iColls[2*j+1] = new IndexedCollection("StruAnaly"+(2*j+1));
			}			
			for (i = 0; i < select.length; i++) {
				yValue = (keyValue==0)?0:(Double.parseDouble(((DataField) kColl_value.
						getDataElement(Integer.parseInt(select[i]))).getValue().toString()))/keyValue;
				if(!showType.equals("02")){
					KeyedCollection kColl_result = new KeyedCollection();
					kColl_result.addDataField("xName", kColl_item.getDataValue(kColl_value.getDataElement(Integer.parseInt(select[i])).getName()));
					kColl_result.addDataField("yName", ""+((DataField) kColl_value.getDataElement(Integer.parseInt(select[i])))
							.getValue().toString());
					iColls[2*j].add(kColl_result);
					kColl_count[2*j].addDataField((kColl_value.getDataElement(Integer.parseInt(select[i])).getName()), 
							((DataField) kColl_value.getDataElement(Integer.parseInt(select[i]))).getValue().toString());
				}
				if(!showType.equals("03")){
					DecimalFormat trans_num = new DecimalFormat("0.00");
					KeyedCollection kColl_analy = new KeyedCollection();				
					kColl_analy.addDataField("xName", kColl_item.getDataValue(kColl_value.getDataElement(Integer.parseInt(select[i])).getName()) );
					kColl_analy.addDataField("yName", trans_num.format(100*yValue)+"%");
					iColls[2*j+1].add(kColl_analy);				
					kColl_count[2*j+1].addDataField((kColl_value.getDataElement(Integer.parseInt(select[i])).getName()), trans_num.format(100*yValue)+"%");
				}				
			}
		}
		
		/*** 生成图表前处理数据 ***/
		if(chartType.equals("bar")){
			chartType = "结构分析柱状图";
		}else if(chartType.equals("line")){
			chartType = "结构分析线状图";
		}
		String[] yName = showType.equals("01")?new String[stat_prd.length*2]:new String[stat_prd.length];
		IndexedCollection[] iColl_Chart = showType.equals("01")?new IndexedCollection[stat_prd.length*2]:new IndexedCollection[stat_prd.length];
		if(showType.equals("02")){
			for(i = 0 ; i < stat_prd.length; i ++){
				yName[i] = stat_prd[i]+"期分析";
				iColl_Chart[i]=iColls[2*i+1];
			}
		}else if(showType.equals("03")){
			for(i = 0 ; i < stat_prd.length; i ++){
				yName[i] = stat_prd[i];
				iColl_Chart[i]=iColls[2*i];
			}
		}else{
			iColl_Chart=iColls;
			for(i = 0 ; i < stat_prd.length; i ++){
				yName[2*i] = stat_prd[i];
				yName[2*i+1] = stat_prd[i]+"期分析";
			}
		}

		KeyedCollection styles = new KeyedCollection();
		styles.addDataField("Caption", chartType);
		styles.addDataField("ShowName", "1");
		styles.addDataField("FormatNumberScale", "0");
		styles.addDataField("Subcaption", "");
		String multiXML = FCFCharHelper.createXMLDataMultis(iColl_Chart, styles, yName);
		context.addDataField("multiXML", multiXML);
		
		/*** 传入页面的context处理 ***/
		if(1 == 1 ){
			j=0;
			String str = "";
			for(i = 0 ; i < stat_prd.length ; i++){
				context.removeDataElement("StruAnaly"+i);
			}
			
			for(i = 0 ; i < kColl_count.length ; i++){						
				if(kColl_count[i] != null){
					kColl_count[i].setId("StruAnaly"+j);
					this.putDataElement2Context(kColl_count[i], context);
					j++;
				}
			}
			for(i = 0 ; i < yName.length ; i++){
				str = str+","+yName[i];
			}
			context.addDataField("records", j);
			context.addDataField("yName", str);
		}
	}
	/******************************************* 结构分析图表显示处理end ************************************************/
}