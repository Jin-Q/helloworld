package com.yucheng.cmis.platform.flashcharts.domain;

import java.util.LinkedList;

/**
 * 多系图形X轴元素集合
 * 该集合是有序的，与多系图形数据集合一一对应
 * @author JackYu
 *
 */
public class MultiChartCategories {
	
	//X轴元素名称集合
	private LinkedList<String> nameList = null;

	/**
	 * 设置X轴元素名称集合
	 * @param name
	 */
	public void setCategoryName(String name){
		if(nameList == null) nameList = new LinkedList<String>();
		nameList.add(name);
	}
	
	/**
	 * X轴元素名称集合
	 * 是一个有序的集合，与MultiChartData中的Value集合一一对应
	 * 
	 * @return X轴元素名称集合
	 */
	public LinkedList<String> getNameList() {
		return nameList;
	}

	/**
	 * X轴元素名称集合
	 * 是一个有序的集合，与MultiChartData中的Value集合一一对应
	 * 
	 * @param nameList X轴元素名称集合
	 */
	public void setNameList(LinkedList<String> nameList) {
		this.nameList = nameList;
	}
	
	/**
	 * 返回X轴元素集合个数
	 * @return X轴元素集合个数
	 */
	public int getSize(){
		return nameList==null?0:nameList.size();
	}
	
}
