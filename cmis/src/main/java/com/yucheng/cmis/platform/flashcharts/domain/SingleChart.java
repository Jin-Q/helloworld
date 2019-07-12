package com.yucheng.cmis.platform.flashcharts.domain;
/**
 * 单系图形数据对象
 * @author JackYu
 *
 */
public class SingleChart {

	private String name;  //名称
	private String value; //值
	private String color; //颜色
	private String link;//链结
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
}
