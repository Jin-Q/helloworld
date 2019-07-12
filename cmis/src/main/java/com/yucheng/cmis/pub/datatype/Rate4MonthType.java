package com.yucheng.cmis.pub.datatype;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import com.ecc.emp.datatype.EMPDataType;
import com.ecc.emp.datatype.InvalidDataException;

/**
 * <p>月利率数据类型</p>
 * <p></p>
 * @author liuming
 *
 */

public class Rate4MonthType extends EMPDataType {

	/**
	 * 标度，小数位数
	 */
	int scale = 2;

	/**
	 * 整数部分是否显示分割符
	 */
	boolean showComma = true;
	
	/**
	 * 是否是百分数(缺省是百分数)
	 */
	boolean isPercent = true;

	/**
	 * 最小值
	 */
	double min = -Double.MAX_VALUE;

	/**
	 * 最大值
	 */
	double max = Double.MAX_VALUE;
	
	/**
	 * 将月利率数的显示字符串值转换成后台存放的值
	 * 后台值与前台显示值之间的转换公式为：
	 *       后台值 = 前台显示值 × 12 ÷ 10
	 * @param strValue 前台输入数据(字符串，注带‰)
	 * @param locale 本地语言设定
	 * @return 后台数据(keepStringValue=false时为rate数值，否则为字符串)
	 * @throws InvalidDataException
	 */	
	public Object convertFromString(String strValue, Locale locale)
			throws InvalidDataException {
		System.err.println("convertFromString be called ...");
		
		if (strValue == null || "".equals(strValue))
			return null;

		strValue = strValue.trim();

		BigDecimal value = null;
		try {
			DecimalFormat numberformat = (DecimalFormat)DecimalFormat.getPercentInstance(locale);

			numberformat.applyPattern(numberformat.toPattern().replaceAll("%", "\u2030"));//\u2030是‰
			 
			value = new BigDecimal(numberformat.parse(strValue).toString());
			
			value = value.multiply(new BigDecimal("1200")); // 乘以
			
			//截取多余的数字
			BigDecimal one = new BigDecimal("1");
			value = value.divide(one, scale, BigDecimal.ROUND_DOWN);
		} catch (ParseException e) {
			throw new InvalidDataException(e);
		}
		if (keepStringValue) {
			return (value.toString());
		} else
			return value;
	}

	public String getJavaTypeName() {
		return "decimal";
	}
	/**
	 * 将后台数值转换为前台显示值。
	 * 前台显示值与后台值之间的转换公式为：
	 *       前台显示值 = 后台值 ÷ 12 × 10  
	 *       前台值 = 前台显示值 ÷ 1000
	 * @param value 后台数据(keepStringValue=false时为Rate数值，否则为字符串)
	 * @param locale 本地语言设定
	 * @param 前台数据(字符串)
	 */
	public String getStringValue(Object object, Locale locale) {
		System.err.println("getStringValue be called ...");
		
		if (object == null)
			return "";
		BigDecimal value = null;
		if (this.keepStringValue) {
			value = new BigDecimal((String) object);
		} else {
			value = (BigDecimal) object;
		}
		BigDecimal one = new BigDecimal("1200");
		value = value.divide(one, scale, BigDecimal.ROUND_DOWN);

		DecimalFormat numberformat = (DecimalFormat)DecimalFormat.getPercentInstance(locale);
		
		numberformat.applyPattern(numberformat.toPattern().replaceAll("%", "\u2030"));//\u2030是‰
		
		numberformat.setGroupingUsed(this.showComma);
		numberformat.setMinimumFractionDigits(this.scale);
		numberformat.setMaximumFractionDigits(this.scale);

		return numberformat.format(value);
		
	}

	public boolean validateStringValue(String value)
			throws InvalidDataException {
		Locale locale = Locale.getDefault();
		return this.validateStringValue(value, locale);
	}

	public boolean validateStringValue(String value, Locale locale)
			throws InvalidDataException {

		if (value == null || "".equals(value))
			return true;

		value = value.trim();
		
		DecimalFormat numberformat = (DecimalFormat)DecimalFormat.getPercentInstance(locale);
		if(!this.isPercent){//如果是千分数，则更改pattern
			numberformat.applyPattern(numberformat.toPattern().replaceAll("%", "\u2030"));
		}
		
		BigDecimal tmp = null;
		try {
			tmp = new BigDecimal(numberformat.parse(value).toString());
		} catch (ParseException e) {
			throw new InvalidDataException(e);
		}

		if (tmp.doubleValue() > max || tmp.doubleValue() < min)
			throw new InvalidDataException("The value[" + value
					+ "] must be between" + getMin() + " to " + getMax() + "!");

		return true;
	}

	public boolean getIsPercent() {
		return isPercent;
	}

	public void setIsPercent(boolean isPercent) {
		this.isPercent = isPercent;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public boolean getShowComma() {
		return showComma;
	}

	public void setShowComma(boolean showComma) {
		this.showComma = showComma;
	}

}
