package com.yucheng.cmis.platform.flashcharts;

import java.util.LinkedList;
import java.util.List;

import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.platform.flashcharts.domain.MultiChartCategories;
import com.yucheng.cmis.platform.flashcharts.domain.MultiChartData;
import com.yucheng.cmis.platform.flashcharts.domain.SingleChart;

/**
 * 提交外部FusionChartsFree生成图形的XML数据
 * 
 * 其它：
 * <li>Y轴不能使用中文，商业版OK
 * <li>link中的JS方法参数不能为中文
 * @author JackYu
 *
 */
public class ProviderCharts {
	
	
    private static ProviderCharts instance;
    
    private String caption;    //图标的标题
    private String subcaption; //子标题
	private String xAxisName;  //X轴标签
    private String yAxisName;  //y轴标签
    private String yAxisNameRight; //组合图形时有效，右侧Y轴标签
    private String showName;   //
    private String decimalPrecision; //指定小数的位数 
    private String formatNumberScale;// 自动格式化 ，如果 formatNumberScale 的值设置为0，那么 value=12500 就会显示成 12,500 自动添加千位分隔符 
    private String decimalSeparator;//小数分隔符 .
    private String thousandSeparator;//千位分割符 , 一般用 ，
    private String numberPrefix; //前缀
    private String numberSuffix; //后缀
    private String bgColor; //背景色
    private String link;//图表超链节
    private String outCnvBaseFontSize;//设置图表外侧的字体大小
    private String outCnvBaseFont;//设置图表外侧的字体样式 
    private String baseFont;//设置字体样式
    private String baseFontSize; //设置字体大小
    private String rotateNames;//设置X轴字体0-水平、1-垂直放置
    
    private String divlinecolor = null;
    private String divLineAlpha = null;
    private String trendStartValue = null;//刻度线值
    private String trendDisValue = null;//刻度线显示值
    private String trendColor = null;//刻度线颜色
    private boolean trendFlag = false;//是否需要刻度线
    
    private List<String> categories; //基本分类名称


    public static ProviderCharts getInstance(){
    	   if(instance == null)
    		   return new ProviderCharts();
    	   return instance;
    }
	
	/**
	 * 设置FCF的XML数据头
	 * 
	 * 适用于单系、多系图形
	 * 
	 * @return FCF的XML数据头
	 */
	protected String careateHeard(){
		
		StringBuffer stb = new StringBuffer();
		stb.append("<"+createRoot());
		if(isString(this.caption)){
			stb.append(" caption='"+this.caption+"'");
		}
		
		if(isString(this.getBaseFont())){
			stb.append(" baseFont='"+this.getBaseFont()+"' ");
		}
		
		if(isString(this.getBaseFontSize())){
			stb.append(" baseFontSize='"+this.getBaseFontSize()+"' ");
		}
		
		if(isString(this.getOutCnvBaseFont())){
			stb.append(" outCnvBaseFont='"+this.getOutCnvBaseFont()+"' ");
		}
		
		if(isString(this.getOutCnvBaseFontSize())){
			stb.append(" outCnvBaseFontSize='"+this.getOutCnvBaseFontSize()+"' ");
		}
		
		if(isString(this.getRotateNames())){
			stb.append(" rotateNames='"+this.getRotateNames()+"' ");
		}
		
		if(isString(this.subcaption)){
			stb.append(" subcaption='"+this.subcaption+"'");
		}

		if(isString(this.xAxisName)){
			stb.append(" xAxisName='"+this.xAxisName+"'");
		}
		
		if(isString(this.yAxisName)){
			stb.append(" yAxisName='"+this.yAxisName+"'");
		}
		
		if(isString(this.showName)){
			stb.append(" showName='"+this.showName+"'");
		}
		
		if(isString(this.decimalPrecision)){
			stb.append(" decimalPrecision='"+this.decimalPrecision+"'");
		}
		
		if(isString(this.formatNumberScale)){
			stb.append(" formatNumberScale='"+this.formatNumberScale+"'");
		}
		
		if(isString(this.numberPrefix)){
			stb.append(" numberPrefix='"+this.numberPrefix+"'");
		}
		
		if(isString(this.numberSuffix)){
			stb.append(" numberSuffix='"+this.numberSuffix+"'");
		}
		
		if(isString(this.bgColor)){
			stb.append(" bgColor='"+this.bgColor+"'");
		}
		
		if(isString(this.divlinecolor)){
			stb.append(" divlinecolor='"+this.divlinecolor+"'");
		}
		
		if(isString(this.divLineAlpha)){
			stb.append(" divLineAlpha='"+this.divLineAlpha+"'");
		}
		
		stb.append(">");
		
		return stb.toString();
	}
	
	/**
	 * 设置FCF的XML数据头 适用于组合系图形
	 * 
	 * @return FCF的XML数据头
	 */
	protected String careateComplexHeard(){
		
		StringBuffer stb = new StringBuffer();
		stb.append("<"+createRoot());
		if(isString(this.caption)){
			stb.append(" caption='"+this.caption+"'");
		}
		
		if(isString(this.subcaption)){
			stb.append(" subcaption='"+this.subcaption+"'");
		}
		
		if(isString(this.xAxisName)){
			stb.append(" xAxisName='"+this.xAxisName+"'");
		}
		
		//组合图形的Y轴代码分左右侧  -- 左侧
		if(isString(this.yAxisName)){
			stb.append(" PYAxisName='"+this.yAxisName+"'");
		}
		
		//组合图形的Y轴代码分左右侧 -- 右侧
		if(isString(this.yAxisName)){
			stb.append(" SYAxisName='"+this.yAxisNameRight+"'");
		}
		
		if(isString(this.showName)){
			stb.append(" showName='"+this.showName+"'");
		}
		
		if(isString(this.decimalPrecision)){
			stb.append(" decimalPrecision='"+this.decimalPrecision+"'");
		}
		
		if(isString(this.formatNumberScale)){
			stb.append(" formatNumberScale='"+this.formatNumberScale+"'");
		}
		
		if(isString(this.numberPrefix)){
			stb.append(" numberPrefix='"+this.numberPrefix+"'");
		}
		
		if(isString(this.numberSuffix)){
			stb.append(" numberSuffix='"+this.numberSuffix+"'");
		}
		
		if(isString(this.bgColor)){
			stb.append(" bgColor='"+this.bgColor+"'");
		}
		
		if(isString(this.divlinecolor)){
			stb.append(" divlinecolor='"+this.divlinecolor+"'");
		}
		
		if(isString(this.divLineAlpha)){
			stb.append(" divLineAlpha='"+this.divLineAlpha+"'");
		}
		
		stb.append(">");
		
		return stb.toString();
	}
	
	/**
	 * 生成刻度线
	 * 
	 * @param value 刻度线值
	 * @param disValue 刻度线显示值
	 * @param color 刻度线颜色
	 * @return 刻度线
	 */
	private String generatTrendLine(String value,String disValue,String color){
		if(value==null || value.trim().equals("")) return "";
		if(disValue == null) disValue = "刻度线";
		if(color == null || color.trim().equals("")) color = ColorHelper.getColor();
		
		StringBuffer sb = new StringBuffer();
		sb.append("<trendlines>")
		  .append("<line startValue='"+value+"' color='"+color+"' displayValue='"+disValue+"' showOnTop ='1' />")
		  .append("</trendlines>");
		  
		return sb.toString();
	}
	
	/**
	 * 生成单个图形的FCF的XML数据
	 * @param list SingleChart集合
	 * @return FCF XML数据
	 */
	public String createXMLDataSingle(List<SingleChart> list){
		
		StringBuffer sb = new StringBuffer();
		sb.append(careateHeard());
		if(list!=null){
			for(SingleChart chart : list){
				//sb.append("<set name='"+chart.getName()+"' value='"+chart.getValue()+"' color='"+chart.getColor()+"' link='"+chart.getLink()+"' />");
				//如果名称为空，则该数据不显示
				if(isString(chart.getName()))
					sb.append("<set name='"+chart.getName()+"' ");
				else
					continue;
				
				//值
				if(isString(chart.getValue()))
					sb.append("value='"+chart.getValue()+"' ");
				else
					sb.append("value='0' ");
				
				//颜色
				if(isString(chart.getColor()))
					sb.append("color='"+chart.getColor()+"' ");
				
				//链节
				if(isString(chart.getLink()))
					sb.append("link='"+chart.getLink()+"' ");
				
				sb.append("/>");
				
			}
		}
		
		//判断是否需要显示刻度线
		if(trendFlag){
			sb.append(this.generatTrendLine(trendStartValue, trendDisValue, trendColor));
		}
		
		sb.append("</"+createFoot()+">");
		
		System.out.println("生成单个图形XML数据:\n"+sb.toString());
		return sb.toString();
	}
	
	
	/**
	 * 生成多系图形的X轴元素名称
	 * @param categories
	 * @return
	 */
	private String createCategories(MultiChartCategories categories){
		    StringBuffer sb = new StringBuffer();
		    sb.append("<categories>");
		    
		    for(String  name : categories.getNameList()){
		    	  sb.append("<category  name='");
		    	  sb.append(name);
		    	  sb.append("' />");
		    }
		    
		    
		    sb.append("</categories>");
		    
		    return sb.toString();
	}
	
	/**
	 * 生成多系图形的XML数据格式
	 * 
	 * @param categories X轴元素名称
	 * @param chartDataList 多系图形数据
	 * @return
	 */
	public String createXMLDataMulti(MultiChartCategories categories , LinkedList<MultiChartData> chartDataList){
		StringBuffer sb = new StringBuffer();
		
		//如果categories和chartData中Value个数不等，则返回空
		if(categories==null || chartDataList==null) return "";
		
		sb.append(careateHeard());
		sb.append(createCategories(categories));
		
		//拼XML数据数据
		for (MultiChartData multiChartData : chartDataList) {
			if(!isString(multiChartData.getName())) continue;
			
			sb.append("<dataset ")
			  .append("seriesName = '"+multiChartData.getName()+"' ")
			  .append("color = '"+multiChartData.getColor()+"' ")
			  .append(">");
			  
			
			//根据categories中的name来找数据集合
			for (String name : categories.getNameList()) {
				//如果该name的数据对象为空，生成空<set/>保证数据与X轴元素能正确对应
				SingleChart chart = multiChartData.getSingleChart(name);
				if(chart == null) sb.append("<set/>");
				else{
					sb.append("<set ").append("value = '"+chart.getValue()+"' ");
					if(chart.getLink()!=null)
						sb.append("link ='"+chart.getLink()+"' ");
					
					sb.append("/>");
				}
			}
			sb.append("</dataset>");
		}
		
		sb.append("</"+createFoot()+">");
		
		System.out.println("多图形XML数据:\n"+sb.toString());
		
		return sb.toString();
	}
	
	/**
	 * 生成组合图形的XML数据格式
	 * 图形个数>=2
	 * 
	 * @param categories X轴元素名称
	 * @param chartDataList 组合图形数据
	 * @return
	 */
	public String createXMLDataComplex(MultiChartCategories categories , LinkedList<MultiChartData> chartDataList){
		StringBuffer sb = new StringBuffer();
		
		//如果categories和chartData中Value个数不等，则返回空
		if(categories==null || chartDataList==null) return "";
		
		sb.append(careateComplexHeard());
		sb.append(createCategories(categories));
		
		//拼XML数据数据
		for (MultiChartData multiChartData : chartDataList) {
			if(!isString(multiChartData.getName())) continue;
			
			sb.append("<dataset ")
			  .append("seriesName = '"+multiChartData.getName()+"' ")
			  .append("color = '"+multiChartData.getColor()+"' ")
			  .append("parentYAxis='"+multiChartData.getParentYAxis()+"'")
			  .append(">");
			  
			
			//根据categories中的name来找数据集合
			for (String name : categories.getNameList()) {
				//如果该name的数据对象为空，生成空<set/>保证数据与X轴元素能正确对应
				SingleChart chart = multiChartData.getSingleChart(name);
				if(chart == null) sb.append("<set/>");
				else{
					sb.append("<set ").append("value = '"+chart.getValue()+"' ");
					if(chart.getLink()!=null)
						sb.append("link ='"+chart.getLink()+"' ");
					
					sb.append("/>");
				}
			}
			sb.append("</dataset>");
		}
		
		sb.append("</"+createFoot()+">");
		
		System.out.println("多图形XML数据:\n"+sb.toString());
		
		return sb.toString();
	}
	
	/**
	 * 对flash图表的各种参数进行设置
	 * @param styles 参数集合
	 * @param pcharts 图形处理对象
	 * @author GC 20131223
	 */
	public void setStyles(KeyedCollection styles,ProviderCharts pcharts) throws EMPException {
		if(styles == null){
			
		}else{
			pcharts.setCaption(styles.getDataValue("Caption").toString());	//主标题设置
			pcharts.setSubcaption(styles.getDataValue("Subcaption").toString());	//副标题设置
			
			if(styles.containsKey("DecimalPrecision")){	//小数位数设置
				pcharts.setDecimalPrecision(styles.getDataValue("DecimalPrecision").toString());
			}else{
				pcharts.setDecimalPrecision("2");
			}
			
			if(styles.containsKey("FormatNumberScale")){	//百千万后缀设置
				pcharts.setFormatNumberScale(styles.getDataValue("FormatNumberScale").toString());
			}else{
				pcharts.setFormatNumberScale("0");
			}
						
			if(styles.containsKey("RotateNames")){	//设置X轴字体0-水平、1-垂直放置
				pcharts.setRotateNames(styles.getDataValue("RotateNames").toString());
			}else{
				pcharts.setRotateNames("0");
			}
			
			if(styles.containsKey("ShowName")){	//是否显示标题名称 0 表示否 1 表示显示
				pcharts.setShowName(styles.getDataValue("ShowName").toString());
			}else{
				pcharts.setShowName("1");
			}
			
			if(styles.containsKey("OutCnvBaseFont")){	//设置图表外侧的字体样式
				pcharts.setOutCnvBaseFont(styles.getDataValue("OutCnvBaseFont").toString());
			}else{
				pcharts.setOutCnvBaseFont("1");
			}
			
			if(styles.containsKey("OutCnvBaseFontSize")){	//设置图表外侧的字体大小
				pcharts.setOutCnvBaseFontSize(styles.getDataValue("OutCnvBaseFontSize").toString());
			}else{
				pcharts.setOutCnvBaseFontSize("14");
			}
			
			if(styles.containsKey("BaseFontSize")){	//设置图表中的字体样式 
				pcharts.setBaseFontSize(styles.getDataValue("BaseFontSize").toString());
			}else{
				pcharts.setBaseFontSize("12");
			}
			
		}

		//pcharts.setBgColor(ColorHelper.getColor());  
		//pcharts.setDivlinecolor("c5c5c5");
		//pcharts.setDivLineAlpha("60");  
		//pcharts.setXAxisName("X轴名称");
		//pcharts.setYAxisName("Y轴名称"); 
		//设置刻度线：
		//pcharts.setTrendFlag(true); 
		//pcharts.setTrendDisValue("刻度线");  
		//pcharts.setTrendStartValue("6"); 
	}
	
	/**
	 * FCF根起始标签
	 * @return FCF根起始标签
	 */
	protected String createRoot(){
		return "graph";
	}
	
	/**
	 * FCF根结束标签
	 * @return FCF根起始标签
	 */
	protected String createFoot(){
		
		return "graph";
	
	}
	
	/**
	 * 多图形时X轴分类名称
	 * @return 多图形时X轴分类名称
	 */
	public List<String> getCategories() {
		return categories;
	}

	/**
	 * 多图形时X轴分类名称
	 * @param categories 多图形时X轴分类名称
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	
	/**
	 * 判断是否是字符串
	 * @param value
	 * @return
	 */
	private boolean isString(String value){
		if(value == null || value.length() == 0) return false;
		
		return true;
	}

	/**
	 * 图形中的链节，可以是JS方法，也可以是超链节
	 * @return
	 */
	public String getLink() {
		return link;
	}

	/**
	 * 图形中的链节，可以是JS方法，也可以是超链节
	 * @param link  图形中的链节
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * 刻度线值
	 * @return 刻度线值
	 */
	public String getTrendStartValue() {
		return trendStartValue;
	}

	/**
	 * 刻度线值
	 * @param trendStartValue 刻度线值
	 */
	public void setTrendStartValue(String trendStartValue) {
		this.trendStartValue = trendStartValue;
	}

	/**
	 * 刻度线显示值
	 * @return 刻度线显示值
	 */
	public String getTrendDisValue() {
		return trendDisValue;
	}
	
	/**
	 * 刻度线显示值
	 * @param trendDisValue 刻度线显示值
	 */
	public void setTrendDisValue(String trendDisValue) {
		this.trendDisValue = trendDisValue;
	}

	/**
	 * 刻度线颜色
	 * @return 刻度线颜色
	 */
	public String getTrendColor() {
		return trendColor;
	}

	/**
	 * 刻度线颜色
	 * @param trendColor 刻度线颜色
	 */
	public void setTrendColor(String trendColor) {
		this.trendColor = trendColor;
	}

	/**
	 * 是否需要刻度线
	 * @return 是否需要刻度线
	 */
	public boolean isTrendFlag() {
		return trendFlag;
	}
	
	/**
	 * 是否需要刻度线
	 * @param trendFlag 是否需要刻度线
	 */
	public void setTrendFlag(boolean trendFlag) {
		this.trendFlag = trendFlag;
	}

	public String getDivlinecolor() {
		return divlinecolor;
	}

	public void setDivlinecolor(String divlinecolor) {
		this.divlinecolor = divlinecolor;
	}

	/**
	 * 刻度线显示值
	 * @return 刻度线显示值
	 */
	public String getDivLineAlpha() {
		return divLineAlpha;
	}

	/**
	 * 刻度线显示值
	 * @param divLineAlpha 刻度线显示值
	 */
	public void setDivLineAlpha(String divLineAlpha) {
		this.divLineAlpha = divLineAlpha;
	}
	
	/**
     * 主标题
     * @return 主标题
     */
	public String getCaption() {
		return caption;
	}

	/**
	 * 设置主标题
	 * @param caption 主标题
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * X轴名称
	 * @return X轴名称
	 */
	public String getXAxisName() {
		return xAxisName;
	}

	/**
	 * 设置X轴名称
	 * @param axisName X轴名称
	 */
	public void setXAxisName(String axisName) {
		xAxisName = axisName;
	}

	/**
	 * Y轴名称
	 * 注意：由于是免费版本，Y轴不能使用中文
	 * @return X轴名称
	 */
	public String getYAxisName() {
		return yAxisName;
	}

	/**
	 * 设置Y轴名称(由于是免费版本，不能使用中文)
	 * @param axisName Y轴名称
	 */
	public void setYAxisName(String axisName) {
		yAxisName = axisName;
	}

	/**
	 * 是否显示标题名称 0 表示否 1 表示显示
	 * @return 是否显示标题名称
	 */
	public String getShowName() {
		return showName;
	}

	/**
	 * 是否显示标题名称 0 表示否 1 表示显示
	 * @param showName 是否显示标题名称
	 */
	public void setShowName(String showName) {
		this.showName = showName;
	}

	/**
	 * 小数位数
	 * @return 小数位数
	 */
	public String getDecimalPrecision() {
		return decimalPrecision;
	}

	/**
	 * 指定小数的位数  ,如果设置为0那么像12.432 就变成12 ，
	 * 如果把它设置为5那么就变成 12.43200
	 * @param decimalPrecision 小数位数
	 */
	public void setDecimalPrecision(String decimalPrecision) {
		this.decimalPrecision = decimalPrecision;
	}

	/**
	 * 是否自动格式化 
	 * FCF 会自动给你的数据添加上 k（千）或M（百万），如果你不想这样，就要把 formatNumberScale 设置为0
	 * @return
	 */
	public String getFormatNumberScale() {
		return formatNumberScale;
	}
	/**
	 * 设置是否自动格式化 
	 * FCF 会自动给你的数据添加上 k（千）或M（百万），如果你不想这样，就要把 formatNumberScale 设置为0
	 * @param formatNumberScale
	 */
	public void setFormatNumberScale(String formatNumberScale) {
		this.formatNumberScale = formatNumberScale;
	}

	
	/**
	 * 数据前辍
	 * 如果设置 numberPrefix = ‘$’ 那么就会在数字的前面加上"$",像$4000这样
     * 用%A5 表示 ￥ 
	 * @return 数据前辍
	 */
	public String getNumberPrefix() {
		return numberPrefix;
	}
    /**
     * 数据前辍
     * 如果设置 numberPrefix = ‘$’ 那么就会在数字的前面加上"$",像$4000这样
     * 用%A5 表示 ￥ 
     * @param numberPrefix 数据前辍
     */
	public void setNumberPrefix(String numberPrefix) {
		this.numberPrefix = numberPrefix;
	}

	/**
	 * 副标题
	 * @return 副标题
	 */
	public String getSubcaption() {
		return subcaption;
	}

	/**
	 * 设置副标题
	 * @param subcaption 副标题
	 */
	public void setSubcaption(String subcaption) {
		this.subcaption = subcaption;
	}

	/**
	 * 设置后辍
	 * 
	 * 如果设置 numberSuffix ='25%' 那么获得的结果就是像 43% , 66%
     * 这里的 25% 代表 %
	 * @return 后辍
	 */
	public String getNumberSuffix() {
		return numberSuffix;
	}
    /**
     * 设置后辍
     * 
     * 如果设置 numberSuffix ='25%' 那么获得的结果就是像 43% , 66%
     * 这里的 25% 代表 %
     * @param numberSuffix 后辍
     */
	public void setNumberSuffix(String numberSuffix) {
		this.numberSuffix = numberSuffix;
	}

	/**
	 * 小数分隔符 
	 * @return 小数分隔符
	 */
	public String getDecimalSeparator() {
		return decimalSeparator;
	}

	/**
	 * 设置小数分隔符
	 * @param decimalSeparator 小数分隔符
	 */
	public void setDecimalSeparator(String decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}

	/**
	 * 千位分割符
	 * @return 千位分割符
	 */
	public String getThousandSeparator() {
		return thousandSeparator;
	}

	/**
	 * 设置千位分割符
	 * @param thousandSeparator 千位分割符
	 */
	public void setThousandSeparator(String thousandSeparator) {
		this.thousandSeparator = thousandSeparator;
	}

	/**
	 * 画布的背景颜色
	 * @return 画布的背景颜色
	 */
	public String getBgColor() {
		return bgColor;
	}

    /**
     * 设置画布的背景颜色
     * @param bgColor
     */
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * 组合图形时有效，右侧Y轴标签
	 * @return 组合图形时有效，右侧Y轴标签
	 */
	public String getyAxisNameRight() {
		return yAxisNameRight;
	}
	
	/**
	 * 组合图形时有效，右侧Y轴标签
	 * @param yAxisNameRight 组合图形时有效，右侧Y轴标签
	 */
	public void setyAxisNameRight(String yAxisNameRight) {
		this.yAxisNameRight = yAxisNameRight;
	}

	/**
	 * 设置图表外侧的字体大小
	 * @return 设置图表外侧的字体大小
	 */
	public String getOutCnvBaseFontSize() {
		return outCnvBaseFontSize;
	}
	
	/**
	 * 设置图表外侧的字体大小
	 * @param outCnvBaseFontSize 设置图表外侧的字体大小
	 */
	public void setOutCnvBaseFontSize(String outCnvBaseFontSize) {
		this.outCnvBaseFontSize = outCnvBaseFontSize;
	}

	/**
	 * 设置图表外侧的字体样式
	 * @return 设置图表外侧的字体样式
	 */
	public String getOutCnvBaseFont() {
		return outCnvBaseFont;
	}
	
	/**
	 * 设置图表外侧的字体样式
	 * @param outCnvBaseFont 设置图表外侧的字体样式
	 */
	public void setOutCnvBaseFont(String outCnvBaseFont) {
		this.outCnvBaseFont = outCnvBaseFont;
	}
	
	/**
	 * 设置字体样式
	 * @return 设置字体样式
	 */
	public String getBaseFont() {
		return baseFont;
	}
	
	/**
	 * 设置字体样式
	 * @param baseFont 设置字体样式
	 */
	public void setBaseFont(String baseFont) {
		this.baseFont = baseFont;
	}
	
	/**
	 * 设置字体大小
	 * @return  设置字体大小
	 */
	public String getBaseFontSize() {
		return baseFontSize;
	}

	/**
	 * 设置字体大小
	 * @param baseFontSize 设置字体大小
	 */
	public void setBaseFontSize(String baseFontSize) {
		this.baseFontSize = baseFontSize;
	}

	/**
	 * 设置X轴字体0-水平、1-垂直放置
	 * @return 设置X轴字体0-水平、1-垂直放置
	 */
	public String getRotateNames() {
		return rotateNames;
	}

	/**
	 * 设置X轴字体0-水平、1-垂直放置
	 * @param rotateNames 设置X轴字体0-水平、1-垂直放置
	 */
	public void setRotateNames(String rotateNames) {
		this.rotateNames = rotateNames;
	}
}
