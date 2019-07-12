package com.yucheng.cmis.platform.flashcharts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.platform.flashcharts.domain.MultiChartCategories;
import com.yucheng.cmis.platform.flashcharts.domain.MultiChartData;
import com.yucheng.cmis.platform.flashcharts.domain.SingleChart;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;

/**
 * <p>生成FCFChar的Flash图形所需的数据
 * 
 * <li>该类只是为了方便将IndexedCollection中的数据转化为FCFChar所需的XML数据，因此Flash图形很多的
 * 属性使用默认，另有一些功能如刻度线、点击图形触发自定义的JS 缺省不提供。如果有更复杂的需求可以
 * 调用ProviderCharts来完成
 * 
 * 
 * <li>单例Flash图形对应的swf文件
 * 		<ul>FCF_Column3D.swf</ul>
 * 	 	<ul>FCF_Column2D.swf</ul>
 * 		<ul>FCF_Line.swf</ul>
 * 		<ul>FCF_Area2D.swf</ul>
 * 		<ul>FCF_Bar2D.swf</ul>
 * 		<ul>FCF_Pie2D.swf</ul>
 * 		<ul>FCF_Pie3D.swf</ul>
 * 		<ul>FCF_Doughnut2D.swf</ul>
 * </li>
 * <li>多例图形对应的swf文件
 * 		<ul>FCF_MSColumn2D.swf	</ul>
 * 		<ul>FCF_MSColumn3D.swf </ul>
 * 		<ul>FCF_MSLine.swf	 </ul>
 *  	<ul>FCF_MSBar2D.swf </ul>
 *  	<ul>FCF_MSArea2D.swf </ul>
 *  <li>组合图形对应的swf文件
 *  	<ul>FCF_MSColumn2DLineDY.swf </ul>
 *  	<ul>FCF_MSColumn3DLineDY.swf </ul>
 *  
 *  
 *  <li>接收参数IndexedCollection结构要求：二维数据结构
 *  示例：
 *  <pre>
 *  	&lt;iColl &gt;
 *  		&lt;kColl &gt;
 *  			&lt;field name="xKCollName" value="x轴值"&gt;
 *  			&lt;field name="yKCollName" value="y轴值"&gt;
 *  		&lt;/kColl &gt;
 *  		&lt;kColl &gt;
 *  			&lt;field name="xKCollName" value="x轴值"&gt;
 *  			&lt;field name="yKCollName" value="y轴值"&gt;
 *  		&lt;/kColl &gt;
 *  		...
 *  	&lt;/iColl &gt;
 *  </pre>
 *  
 * @author JackYu
 *
 */
@ModualService(serviceId="flashchartsService",modualId="flashcharts",modualName="Flash图形展示数据生成服务",
				serviceDesc="Flash图形展示数据生成服务",
				className="com.yucheng.cmis.platform.flashcharts.FCFCharHelper")
public class FCFCharHelper {

	
	/**
	 * 生成单例的Flash图形XML数据
	 * 
	 * @param iColl 生成Flash图形的数据源
	 * @param xKCollName 生成X轴数据的KeyedCollection的name
	 * @param yKCollName 生成Y轴数据的KeyedCollection的name
	 * @param title Flash图形的标题
	 * @param subTitle Flash图形的副标题
	 * @return XML数据
	 * @throws Exception
	 */
	@MethodService(method="createXMLDataSingle",desc="生成单例的Flash图形XML数据")
	public static String createXMLDataSingle(IndexedCollection iColl,String xKCollName,String yKCollName,String title,String subTitle)throws EMPException{
		ProviderCharts pcharts = new ProviderCharts();
		
		//单例
		List<SingleChart> list = new ArrayList<SingleChart>();
		String color1 = ColorHelper.getColor();
		String color2 = ColorHelper.getColor(); 
		for (Iterator iterator = iColl.iterator(); iterator.hasNext();) {
			KeyedCollection kColl = (KeyedCollection) iterator.next();
			SingleChart chart = new SingleChart();
			chart.setName((String)kColl.getDataValue(xKCollName));
			chart.setValue((String)kColl.getDataValue(yKCollName));
			chart.setColor(ColorHelper.getColor());
			//chart.setLink("JavaScript:myJS(\""+kColl.getDataValue("cus_birthday")+"\", \""+chart.getValue()+"\");"); //调用自定义JS
			
			list.add(chart);
		}
		
		pcharts.setDecimalPrecision("2");
		pcharts.setFormatNumberScale("0");
		//pcharts.setXAxisName("X轴名称");
		//pcharts.setYAxisName("Y轴名称");  
		pcharts.setShowName("1");
		pcharts.setCaption(title);
		pcharts.setSubcaption(subTitle);
		pcharts.setRotateNames("0");
		pcharts.setOutCnvBaseFont("楷体");
		pcharts.setOutCnvBaseFontSize("16");
		//pcharts.setBgColor(ColorHelper.getColor());  
		//pcharts.setDivlinecolor("c5c5c5");
		//pcharts.setDivLineAlpha("60");  
		
		
		//设置刻度线：
		//pcharts.setTrendFlag(true); 
		//pcharts.setTrendDisValue("刻度线");  
		//pcharts.setTrendStartValue("6"); 
		
		return pcharts.createXMLDataSingle(list);
	}
	
	/**
	 * 生成多系图形，这里默生生成两系Flash图形XML数据
	 * 
	 * 这里要求 firstIColl和secondIColl中的KColl的名称相同都为xKCollName和yKCollName
	 * 
	 * @param firstIColl 生成第一例Flash图形数据源
	 * @param secondIColl 生成第二例Flash图形数据源
	 * @param xKCollName firstIColl和secondIColl中生成X轴数据的KeyedCollection的name
	 * @param yKCollName firstIColl和secondIColl中生成Y轴数据的KeyedCollection的name
	 * @param title Flash图形的标题
	 * @param subTitle Flash图形的副标题
	 * @param yName1 第一例Flash图形名称
	 * @param yName2 第二例Flash图形名称
	 * @return
	 * @throws Exception
	 */
	@MethodService(method="createXMLDataMulti",desc="生成多系图形，这里默生生成两系Flash图形XML数据")
	public static String createXMLDataMulti(IndexedCollection firstIColl,IndexedCollection secondIColl,String xKCollName,String yKCollName,String title,String subTitle,String yName1,String yName2)throws EMPException{
		String color1 = ColorHelper.getColor();//第一例图形的颜色
		String color2 = ColorHelper.getColor(); //第二例图形的颜色
		
		ProviderCharts pcharts = new ProviderCharts();
		
		pcharts.setDecimalPrecision("2");
		pcharts.setFormatNumberScale("0");
		//pcharts.setXAxisName("X轴名称");
		//pcharts.setYAxisName("Y轴名称");  
		pcharts.setShowName("1");
		pcharts.setCaption(title);
		pcharts.setSubcaption(subTitle);
		//pcharts.setBgColor(ColorHelper.getColor());  
		//pcharts.setDivlinecolor("c5c5c5");
		//pcharts.setDivLineAlpha("60");  
		
		
		//设置刻度线：
		//pcharts.setTrendFlag(true); 
		//pcharts.setTrendDisValue("刻度线");  
		//pcharts.setTrendStartValue("6"); 
		
		
		//多例图形X轴数据准备
		MultiChartCategories categories = new MultiChartCategories();
		
		
		LinkedList<MultiChartData> multList = new LinkedList<MultiChartData>();
		MultiChartData c1 = new MultiChartData();
		c1.setName(yName1);
		c1.setColor(color1);
		for (Iterator iterator = firstIColl.iterator(); iterator.hasNext();) {
			KeyedCollection kColl = (KeyedCollection) iterator.next();
			//设置X轴的数据
			categories.setCategoryName((String)kColl.getDataValue(xKCollName));
			
			//第一例图形的数据准备
			SingleChart chart = new SingleChart();
			chart.setName((String)kColl.getDataValue(xKCollName));
			chart.setValue((String)kColl.getDataValue(yKCollName));
			//chart.setLink("JavaScript:myJS(\""+kColl.getDataValue("cus_birthday")+"\", \""+chart.getValue()+"\");"); //调用自定义JS
			c1.setSingleChart(chart);
		}
		
		MultiChartData c2 = new MultiChartData();
		c2.setName(yName2);
		c2.setColor(color2);
		
		for (Iterator iterator = secondIColl.iterator(); iterator.hasNext();) {
			KeyedCollection kColl = (KeyedCollection) iterator.next();
			
			//第二例图形的数据准备
			SingleChart chart = new SingleChart();
			chart.setName((String)kColl.getDataValue(xKCollName));
			chart.setValue((String)kColl.getDataValue(yKCollName));
			//chart.setLink("JavaScript:myJS(\""+kColl.getDataValue("cus_birthday")+"\", \""+chart.getValue()+"\");"); 
			c2.setSingleChart(chart);
		}
		
		multList.add(c1);
		multList.add(c2);
		
		
		return  pcharts.createXMLDataMulti(categories, multList);
	}
	
	/**
	 * 
	 * <p>生成组合系的Flash图形XML数据
	 * 
	 * <li>组合系图形可以需要>=2个，这里默认生成两个图形，firstIColl对应左侧Y轴的数据 secondIColl对应右侧Y轴数据
	 * 
	 * <li>这里要求 firstIColl和secondIColl中的KColl的名称相同都为xKCollName和yKCollName
	 * 
	 * @param firstIColl 生成第一例Flash图形数据源
	 * @param secondIColl 生成第二例Flash图形数据源
	 * @param xKCollName firstIColl和secondIColl中生成X轴数据的KeyedCollection的name
	 * @param yKCollName firstIColl和secondIColl中生成Y轴数据的KeyedCollection的name
	 * @param title Flash图形的标题
	 * @param subTitle Flash图形的副标题
	 * @param yName1 第一例Flash图形名称
	 * @param yName2 第二例Flash图形名称
	 * @return
	 * @throws Exception
	 */
	@MethodService(method="createXMLDataComplex",desc="生成组合系的Flash图形XML数据")
	public static String createXMLDataComplex(IndexedCollection firstIColl,IndexedCollection secondIColl,String xKCollName,String yKCollName,String title,String subTitle,String yName1,String yName2)throws EMPException{
		String color1 = ColorHelper.getColor();//第一例图形的颜色
		String color2 = ColorHelper.getColor(); //第二例图形的颜色
		
		ProviderCharts pcharts = new ProviderCharts();
		
		pcharts.setDecimalPrecision("2");
		pcharts.setFormatNumberScale("0");
		//pcharts.setXAxisName("X轴名称");
		//pcharts.setYAxisName("Y轴名称");  
		pcharts.setShowName("1");
		pcharts.setCaption(title);
		pcharts.setSubcaption(subTitle);
		//pcharts.setBgColor(ColorHelper.getColor());  
		//pcharts.setDivlinecolor("c5c5c5");
		//pcharts.setDivLineAlpha("60");  
		
		
		//设置刻度线：
		//pcharts.setTrendFlag(true); 
		//pcharts.setTrendDisValue("刻度线");  
		//pcharts.setTrendStartValue("6"); 
		
		
		//多例图形X轴数据准备
		MultiChartCategories categories = new MultiChartCategories();
		
		
		LinkedList<MultiChartData> multList = new LinkedList<MultiChartData>();
		MultiChartData c1 = new MultiChartData();
		c1.setName(yName1);
		c1.setColor(color1);
		for (Iterator iterator = firstIColl.iterator(); iterator.hasNext();) {
			KeyedCollection kColl = (KeyedCollection) iterator.next();
			//设置X轴的数据
			categories.setCategoryName((String)kColl.getDataValue(xKCollName));
			
			//第一例图形的数据准备
			SingleChart chart = new SingleChart();
			chart.setName((String)kColl.getDataValue(xKCollName));
			chart.setValue((String)kColl.getDataValue(yKCollName));
			//chart.setLink("JavaScript:myJS(\""+kColl.getDataValue("cus_birthday")+"\", \""+chart.getValue()+"\");"); //调用自定义JS
			c1.setSingleChart(chart);
		}
		
		MultiChartData c2 = new MultiChartData();
		c2.setName(yName2);
		c2.setColor(color2);
		
		for (Iterator iterator = secondIColl.iterator(); iterator.hasNext();) {
			KeyedCollection kColl = (KeyedCollection) iterator.next();
			
			//第二例图形的数据准备
			SingleChart chart = new SingleChart();
			chart.setName((String)kColl.getDataValue(xKCollName));
			chart.setValue((String)kColl.getDataValue(yKCollName));
			//chart.setLink("JavaScript:myJS(\""+kColl.getDataValue("cus_birthday")+"\", \""+chart.getValue()+"\");"); 
			c2.setSingleChart(chart);
		}
		
		//这里默认生成两个图形，firstIColl对应左侧Y轴的数据 secondIColl对应右侧Y轴数据
		c1.setParentYAxis(MultiChartData.PARENT_Y_AXIS_LEFT);
		c2.setParentYAxis(MultiChartData.PARENT_Y_AXIS_RIGHT);  
		
		multList.add(c1);
		multList.add(c2);
		
		
		return  pcharts.createXMLDataComplex(categories, multList);
	}
	
	/**
	 * 生成多系图形，这里默生生成两系Flash图形XML数据
	 * @param IColls Flash图形数据源
	 * @param xKCollName firstIColl和secondIColl中生成X轴数据的KeyedCollection的name
	 * @param yKCollName firstIColl和secondIColl中生成Y轴数据的KeyedCollection的name
	 * @param title Flash图形的标题
	 * @param subTitle Flash图形的副标题
	 * @param yName Flash图形名称
	 * @return
	 * @throws Exception
	 * @author by-GC-20130830 解决财报多报表分析问题
	 */
	@MethodService(method="createXMLDataMultis",desc="生成多系图形，用于生成多记录图")
	public static String createXMLDataMultis(IndexedCollection[] IColls,KeyedCollection styles,String[] yName)throws EMPException{
		/****显示图表属性设定****/
		ProviderCharts pcharts = new ProviderCharts();		
		pcharts.setStyles(styles, pcharts);
		
		/****多例图形X轴数据准备****/
		MultiChartCategories categories = new MultiChartCategories();		
		LinkedList<MultiChartData> multList = new LinkedList<MultiChartData>();
		
		for (int i=0;i<IColls.length;i++){
			MultiChartData c = new MultiChartData();
			c.setName(yName[i]);
			c.setColor(ColorHelper.getColor());
			for (Iterator iterator = IColls[i].iterator(); iterator.hasNext();) {
				KeyedCollection kColl = (KeyedCollection) iterator.next();
				if(i == 0){
					categories.setCategoryName((String)kColl.getDataValue("xName"));//设置X轴的数据
				}
				/******图形的数据准备*******/
				SingleChart chart = new SingleChart();
				chart.setName(kColl.getDataValue("xName").toString());
				chart.setValue((kColl.getDataValue("yName")).toString().replaceAll("%", ""));
				if(kColl.containsKey("link")){	//加入link处理
					chart.setLink((String)kColl.getDataValue("link"));
				}
				c.setSingleChart(chart);
			}
			multList.add(c);
		}
		
		return  pcharts.createXMLDataMulti(categories, multList);
	}
	
	/**
	 * 生成单例的Flash图形XML数据
	 * @param iColl 生成Flash图形的数据源
	 * @param styles　表格样式控制
	 * @return XML数据
	 * @throws Exception
	 * 调整link处理功能，并增加参数设置 20121223 GC
	 */
	@MethodService(method="createSingleCharXML",desc="生成单例的Flash图形XML数据")
	public static String createSingleCharXML(IndexedCollection iColl ,KeyedCollection styles)throws EMPException{
		ProviderCharts pcharts = new ProviderCharts();		
		List<SingleChart> list = new ArrayList<SingleChart>();
		for (Iterator iterator = iColl.iterator(); iterator.hasNext();) {
			KeyedCollection kColl = (KeyedCollection) iterator.next();
			SingleChart chart = new SingleChart();
			chart.setName(kColl.getDataValue("xName").toString());
			chart.setValue(kColl.getDataValue("yName").toString().replaceAll("%", ""));
			if(kColl.containsKey("link")){	//加入link处理
				chart.setLink(kColl.getDataValue("link").toString());
			}
			chart.setColor(ColorHelper.getColor());			
			list.add(chart);
		}

		pcharts.setStyles(styles, pcharts);		
		return pcharts.createXMLDataSingle(list);
	}
			
}
