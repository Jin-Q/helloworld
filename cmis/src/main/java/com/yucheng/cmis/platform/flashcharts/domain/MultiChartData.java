package com.yucheng.cmis.platform.flashcharts.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * <li>多系/组合图形中数据对象
 * <li>&lt;set value='xx' link='' /&gt;
 * <li>数据对象的集合是有序的与X轴元素名称集合一一对应
 * 
 * 生成组合图形时 parentYAxis 属性才起作用：P对应左侧Y轴数据，S对应右侧Y轴数据
 * 
 * @author JackYu
 */
public class MultiChartData {

	private Map<String, SingleChart> chartMap = null; //数据集合
	private String name = null; //该数据集合的名称
	private String color = null; //该数据集合图形展示颜色
	//组合图形时该字段有效，P对应左侧Y轴数据，S对应右侧Y轴数据
	private String parentYAxis = null;
	
	/**
	 * 右侧Y轴
	 */
	public static String PARENT_Y_AXIS_RIGHT = "S";
	/**
	 * 右侧Y轴
	 */
	public static String PARENT_Y_AXIS_LEFT = "P";
	
	/**
	 * 设置多系图形的值
	 * @param chart SingleChart
	 */
	public void setSingleChart(SingleChart chart){
		if(chartMap == null) chartMap = new HashMap<String, SingleChart>();
		
		if(chart != null && chart.getName() !=null) {
			chartMap.put(chart.getName(), chart);
		}
	}
	
	/**
	 * 根据X轴元素名称取值
	 * @param name X轴元素名称
	 * @return SingleChart
	 */
	public SingleChart getSingleChart(String name){
		if(chartMap == null) return null;
		
		return chartMap.get(name);
	}
	
	/**
	 * 多系图形数据集合
	 * @return 多系图形数据集合
	 */
	public Map<String, SingleChart> getMultiChartMap(){
		return chartMap;
	}
	
	/**
	 * 返回多系图形数据集个数
	 * @return
	 */
	public int getSize(){
		
		return chartMap == null?0:chartMap.size();
	}

	/**
	 * 当前系图形名称
	 * @return 当前系图形名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 当前系图形名称
	 * @param name 当前系图形名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 当前系图形颜色
	 * @return 当前系图形颜色
	 */
	public String getColor() {
		return color;
	}

	/**
	 * 当前系图形颜色
	 * @param color 当前系图形颜色
	 */
	public void setColor(String color) {
		this.color = color;
	}
	
	/**
	 * 当前数据对应P-左侧Y轴 S-右侧Y轴
	 * 
	 * @return 当前数据对应P-左侧Y轴 S-右侧Y轴
	 */
	public String getParentYAxis() {
		return parentYAxis;
	}

	/**
	 * 当前数据对应P-左侧Y轴 S-右侧Y轴
	 * 
	 * @param parentYAxis 当前数据对应P-左侧Y轴 S-右侧Y轴
	 */
	public void setParentYAxis(String parentYAxis) {
		this.parentYAxis = parentYAxis;
	}
	
	
	
}
