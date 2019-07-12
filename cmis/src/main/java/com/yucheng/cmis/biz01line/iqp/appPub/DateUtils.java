package com.yucheng.cmis.biz01line.iqp.appPub;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.ParseException;

import com.ecc.emp.core.EMPException;

public class DateUtils {
	
	/**
	 * 根据类型，在初始日期上加上一段时间，返回结果时间,时间类型为YYYY-MM-DD
	 * @param type 类型 年Y、月M、日D
	 * @param startDate 初始日期
	 * @param term 增加数
	 * @return 结果时间
	 */
	public static String getAddDate(String type, String startDate, int term) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(startDate);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if("Y".equals(type)){
			calendar.add(Calendar.YEAR, term);
		}else if("M".equals(type)){
			calendar.add(Calendar.MONTH, term);
		}else if("D".equals(type)){
			calendar.add(Calendar.DATE, term);
		}
		return sdf.format(calendar.getTime());
	}
	
	/**
	 * 根据类型，在初始日期上加上一段时间，返回结果时间,时间类型为YYYY-MM-DD
	 * @param type 类型 年001、月002、日003
	 * @param startDate 起始日期 
	 * @param term 期限
	 * @return 到期日期
	 * @throws Exception
	 */
	public static String getEndDate(String type, String startDate, int term) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(startDate);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if("001".equals(type)){
			calendar.add(Calendar.YEAR, term);
		}else if("002".equals(type)){
			calendar.add(Calendar.MONTH, term);
		}else if("003".equals(type)){
			calendar.add(Calendar.DATE, term);
		}
		return sdf.format(calendar.getTime());
	}
	
	/**
	 * 根据类型及传入起始日期 ，计算下一个日期
	 * 1.M 按月 下个月的01日
	 * 2.Q 按季 下个季度的01日
	 * 3.Y 按年 下一年的01月01日
	 * @param type
	 * @param startDate
	 * @return
	 * @throws Exception
	 */
	public static String getNextDate(String type,String startDate) throws Exception{
		StringBuffer nextDate = new StringBuffer();
		
		if(type.equals("M")){
			String tempDate = getAddDate("M",startDate,1);
			nextDate.append(tempDate.substring(0, 4)).append("-").append(tempDate.substring(5, 7)).append("-").append("01");
		}
		else if(type.equals("Q")){
			int y = Integer.valueOf(startDate.substring(0, 4));
			int m = Integer.valueOf(startDate.substring(5, 7));
			String tempM = "";
			if(1<= m && m <=3){
				tempM = "04";
			}else if(4<= m && m <=6){
				tempM = "07";
			}else if(7<= m && m <=9){
				tempM = "10";
			}else if(10<= m && m <=12){
				tempM = "01";
				y = y + 1;
			}
			nextDate.append(y).append("-").append(tempM).append("-").append("01");
		}
		else if(type.equals("Y")){
			String tempDate = getAddDate("Y",startDate,1);
			nextDate.append(tempDate.substring(0, 4)).append("-").append("01").append("-").append("01");
		}
		
		return nextDate.toString();
	}
	
	/**
	 * 比较一个日期是否在两个日期之间(不包含)。
	 * @param startDate 起始日期
	 * @param endDate   结束日期
	 * @param inDate   待比较日期
	 * @return 比较结果
	 * @throws Exception
	 */
	public static boolean isBetweenStartDateAndEndDate(String startDate,String endDate,String inDate) throws Exception{
		boolean flag =false;
		DateFormat df =  DateFormat.getDateInstance();
		Date sDate = df.parse(startDate);
		Date eDate = df.parse(endDate);
		Date iDate = df.parse(inDate);
		if(iDate.after(sDate) && eDate.after(iDate)){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 比较两个日期之间的大小
	 * @param date1 日期1
	 * @param date2   日期2
	 * @return 比较结果
	 * @throws Exception
	 */
	public static boolean isBigBetweenDate(String date1,String date2) throws Exception{
		boolean flag =false;
		DateFormat df =  DateFormat.getDateInstance();
		Date sDate = df.parse(date1);
		Date eDate = df.parse(date2);
		if(sDate.after(eDate)){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 期限和期限类型的转换
     * @param term_type 类型 001':'年', '002':'月', '003':'日'
	 * @return term
	 * @throws Exception
	 */
	public static int getChangeTerm(String term_type,int term) throws Exception{
		if("001".equals(term_type)){
			term = term*360;
		}else if("002".equals(term_type)){
			term = term*30;
		}
		return term;
	}
	
	
//	public static void main(String[] agrs) throws Exception{
//		
//		DateUtils d = new DateUtils();
//		String date = d.getNextDate("Y", "2013-08-26");
//		System.out.print(date);
//	}
	
	/**
	 * @Title: getSTDTermEndDate
	 * @Description: 根据起始日期跟期限算出到期日 注意：起始日期格式为yyyy-MM-dd格式
	 * @Parmaters: @param startDate 起始日期
	 * @Parmaters: @param term 期限
	 * @Parmaters: @return
	 * @Parmaters: @throws EMPException
	 * @Return: String
	 * @Throws:
	 * @Author:
	 * @CreateDate:2016-9-26 下午7:01:53
	 * @Version:1.0
	 * @ModifyLog:2016-9-26 下午7:01:53
	 */
	public static String getSTDTermEndDate(String startDate, String term) throws EMPException {
		String loanEndDate = "";
		// 校验起始日期是否合规
		if (startDate == null || "".equals(startDate) || term == null || "".equals(term)) {
			throw new EMPException("根据起始日期、期限计算到期日异常。起始日期或期限为空");
		}
		checkDate(startDate, "起始日期");
		// 校验期限字段是否合规
		DateUtils.checkSTDTerm(term, "期限");
		String term_years = term.substring(0, 2);// 年数
		String term_months = term.substring(2, 4);// 月数
		String term_days = term.substring(4, 6);// 天数
		loanEndDate = startDate;

		if (!"00".equals(term_years)) {
			loanEndDate = DateUtils.ADD_DATE(Calendar.YEAR, loanEndDate, Integer.valueOf(term_years));// 加年
		}

		if (!"00".equals(term_months)) {
			loanEndDate = DateUtils.ADD_DATE(Calendar.MONTH, loanEndDate, Integer.valueOf(term_months));// 加月
		}

		if (!"00".equals(term_days)) {
			loanEndDate = DateUtils.ADD_DATE(Calendar.DATE, loanEndDate, Integer.valueOf(term_days));// 加天
		}
		return loanEndDate;
	}
	
	/**
	 * 判断传递参数是否为日期类型、否则抛异常
	 * 
	 * @param dateStr
	 * @param sName
	 * @throws EMPException
	 *             
	 */
	public static void checkDate(String dateStr, String sName) throws EMPException {
		Pattern pattern = Pattern
				.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(dateStr);
		if (m.matches()) {
		} else {
			throw new EMPException("传入参数【" + sName + "：" + dateStr + "】不符合格式要求");
		}
	}
	
	
	/**
	 * 校验期限格式是否合规 期限格式 000300 校验规则： 1、6位数字 2、年:0-99 月0-11 日:0-30 3、不等于000000
	 * 
	 * @param term
	 * @param sName
	 * @return
	 * @throws EMPException
	 *             date:2014-12-08 上午11:44:22 author:
	 */
	public static void checkSTDTerm(String term, String sName) throws EMPException {

		if (term == null || "".equals(term)) {
			throw new EMPException("传入参数期限为空 ");
		}
		// 是否为6位字符
		if (term.length() != 6) {
			throw new EMPException("传入参数[" + sName + "：" + term + "]不合规 ");
		}
		// 是否为000000
		if ("000000".equals(term)) {
			throw new EMPException("传入参数[" + sName + "：" + term + "]不合规 ");
		}
		// 校验是否为数字
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(term);
		if (!isNum.matches()) {
			throw new EMPException("传入参数[" + sName + "：" + term + "]不合规 ");
		}
		// 年:0-99
		int term_years = Integer.valueOf(term.substring(0, 2));
		if (term_years < 0 || term_years > 99) {
			throw new EMPException("传入参数[" + sName + "：" + term + "]不合规 ");
		}
		// 月0-11
		int term_months = Integer.valueOf(term.substring(2, 4));
		if (term_months < 0 || term_months > 11) {
			throw new EMPException("传入参数[" + sName + "：" + term + "]不合规 ");
		}
		// 日:0-30
		int term_days = Integer.valueOf(term.substring(4, 6));
		if (term_days < 0 || term_days > 30) {
			throw new EMPException("传入参数[" + sName + "：" + term + "]不合规 ");
		}
	}
	
	/**
	 * @Title: ADD_DATE
	 * @Description: 日期加天
	 * @Parmaters: @param optype
	 * @Parmaters: @param date
	 * @Parmaters: @param num
	 * @Parmaters: @return
	 * @Return: String
	 * @Throws:
	 * @Author:
	 * @CreateDate:2016-9-26 下午7:07:02
	 * @Version:1.0
	 * @ModifyLog:2016-9-26 下午7:07:02
	 */
	private static String ADD_DATE(int optype, String date, int num) {
		String st_return = "";
		try {
			DateFormat daf_date = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA);
			daf_date.parse(date);
			Calendar calendar = daf_date.getCalendar();
			calendar.add(optype, num);
			String st_m = "";
			String st_d = "";
			int y = calendar.get(Calendar.YEAR);
			int m = calendar.get(Calendar.MONTH) + 1;
			int d = calendar.get(Calendar.DAY_OF_MONTH);
			if (m <= 9) {
				st_m = "0" + m;
			} else {
				st_m = "" + m;
			}
			if (d <= 9) {
				st_d = "0" + d;
			} else {
				st_d = "" + d;
			}
			st_return = y + "-" + st_m + "-" + st_d;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return st_return;
	}
	
	/*public static String getNextJxDate(String type,String startDate,String day) throws Exception{
		StringBuffer nextDate = new StringBuffer();
		
		if(type.equals("2")){
			int y = Integer.valueOf(startDate.substring(0, 4));
			int m = Integer.valueOf(startDate.substring(5, 7));
			int d = Integer.valueOf(startDate.substring(8, 10));
			String tempDate="";
			//if(d>Integer.valueOf(day)){
			//chenBQ 20190409 如果放款日与开始日为同一日期，则下一放款日顺延至下个月
			if(d>=Integer.valueOf(day)){
				tempDate = getAddDate("M",startDate,1);
			}else{
				tempDate = getAddDate("M",startDate,0);
			}
			nextDate.append(tempDate.substring(0, 4)).append("-").append(tempDate.substring(5, 7)).append("-").append(day);
		}
		else if(type.equals("1")){
			int y = Integer.valueOf(startDate.substring(0, 4));
			int m = Integer.valueOf(startDate.substring(5, 7));
			int d = Integer.valueOf(startDate.substring(8, 10));
			String tempM = "";
			if(1<= m && m <=3){
				if(m==3 && d>Integer.valueOf(day)){
					tempM = "04";
				}else{
					tempM = "03";
				}
			}else if(4<= m && m <=6){
				if(m==6 && d>Integer.valueOf(day)){
					tempM = "07";
				}else{
					tempM = "06";
				}
			}else if(7<= m && m <=9){
				if(m==9 && d>Integer.valueOf(day)){
					tempM = "10";
				}else{
					tempM = "09";
				}
			}else if(10<= m && m <=12){
				if(m==12 && d>Integer.valueOf(day)){
					tempM = "01";
					y = y + 1;
				}else{
					tempM = "12";
				}
			}
			nextDate.append(y).append("-").append(tempM).append("-").append(day);
		}
		else if(type.equals("0")){
			int y = Integer.valueOf(startDate.substring(0, 4));
			int m = Integer.valueOf(startDate.substring(5, 7));
			int d = Integer.valueOf(startDate.substring(8, 10));
			if(m==12 && d>Integer.valueOf(day)){
				String tempDate = getAddDate("Y",startDate,1);
				nextDate.append(tempDate.substring(0, 4)).append("-").append("01").append("-").append(day);
			}else{
				String tempDate = getAddDate("Y",startDate,0);
				nextDate.append(tempDate.substring(0, 4)).append("-").append("12").append("-").append(day);
			}
		}
		
		return nextDate.toString();
	}*/
	
	/**
	 * 根据结息频率及结息日 ，计算下一个结息日期
	 * 1.0 按年结息
	 * 2.1 按季结息
	 * 3.2 按月结息
	 * @param type
	 * @param startDate
	 * @return
	 * @throws Exception
	 */
	public static String getNextJxDate(String type,String startDate,String day) throws Exception{
		StringBuffer nextDate = new StringBuffer();
		//int y = Integer.valueOf(startDate.substring(0, 4));
		int m = Integer.valueOf(startDate.substring(5, 7));
		//int d = Integer.valueOf(startDate.substring(8, 10));
		//按月结息
		String tempDate="";
		if(type.equals("2")){
			tempDate = getAddDate("M",startDate,1);//月份加一个月
			nextDate.append(tempDate.substring(0, 4)).append("-").append(tempDate.substring(5, 7)).append("-").append(day);
		}
		//按季结息
		else if(type.equals("1")){
			tempDate = getAddDate("M",startDate,3);//月份加三个月
			nextDate.append(tempDate.substring(0, 4)).append("-").append(tempDate.substring(5, 7)).append("-").append(day);
		
		}
		//按年结息
		else if(type.equals("0")){
			tempDate = getAddDate("M",startDate,12);//月份加十二个月
			//如果当前月份为12月，则下一结息日为明年12月份
			if(m == 12)
				nextDate.append(tempDate.substring(0, 4)).append("-").append("12").append("-").append(day);
			else
				nextDate.append(startDate.substring(0, 4)).append("-").append("12").append("-").append(day);
		}
		
		return nextDate.toString();
	}
}
