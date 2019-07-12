package com.yucheng.cmis.pub.util;



/**
 * 处理转换工具类
 *@Classname	  TransUtil
 *@Version       0.1
 *@Since   	      2008-9-25
 *@Copyright 	  yuchengtech
 *@Author 	      wqgang	
 *@Description：
 *@Lastmodified 2008-9-25
 *@Author        wqgang
 */
public class TransUtil {
	
	 private static final TransUtil instance = new TransUtil();
	  public TransUtil() {
	  }

	  public static final TransUtil getInstance(){
	    return instance;
	  }
	
	 /**
	   * 转换成 html 格式*
	   *
	   * @param str String
	   * @return String
	   */
	  public static  String htmlcode(String str) {
	    String result = "";
	    if (str == null || str.equalsIgnoreCase("")) {
	      return result;
	    }
	    
	    int l = str.length();
	    for (int i = 0; i < l; i++) {
	      char tmpChar = str.charAt(i);
	      switch (tmpChar) {
	        case '<':
	          result = result + "&lt;";
	          break;
	        case '>':
	          result = result + "&gt;";
	          break;
	        case 13:
	          result = result + "<br>";
	          break;
	        case 34:
	          result = result + "&quot;";
	          break;
	        case '&':
	          result = result + "&amp;";
	          break;
	        case 32:
	          result = result + "&nbsp;";
	          break;
	        case 9:
	          result = result + "    ";
	          break;
	        default:
	          result = result + String.valueOf(tmpChar);
	      }
	    }
	    //System.out.println(result);
	    return result;
	  }

	  /**
	   * isInteger
	   *
	   * @param num String
	   * @return boolean
	   */
	  public static  boolean isInteger(String num){
	    boolean bl=false;
	    try{
	      Integer.parseInt(num) ;
	      bl=true;
	    }catch(Exception ex){
	      bl=false;
	    }
	    return bl;
	  }

	  /**
	   * isLong
	   *
	   * @param num String
	   * @return boolean
	   */
	  public static  boolean isLong(String num){
	    boolean bl=false;
	    try{
	      Long.parseLong(num) ;
	      bl=true;
	    }catch(Exception ex){
	      bl=false;
	    }
	    return bl;
	  }

	  /**
	   * isDouble
	   *
	   * @param num String
	   * @return boolean
	   */
	  public static boolean isDouble(String num){
	    boolean bl=false;
	    try{
	      Double.parseDouble(num) ;
	      bl=true;
	    }catch(Exception ex){
	      bl=false;
	    }
	    return bl;
	  }

	  /**
	   * isFloat
	   *
	   * @param num String
	   * @return boolean
	   */
	  public static boolean isFloat(String num){
	    boolean bl=false;
	    try{
	      Float.parseFloat(num) ;
	      bl=true;
	    }catch(Exception ex){
	      bl=false;
	    }
	    return bl;
	  }

	  /**
	   * isBoolean
	   *
	   * @param str String
	   * @return boolean
	   */
	  public static  boolean isBoolean(String str){
	    boolean bl=false;
	    try{
	      Boolean bl1 = Boolean.valueOf(str) ;
	      bl = bl1.booleanValue() ;
	    }catch(Exception ex){
	      bl=false;
	    }
	    return bl;
	  }

	  /**
	   * isDate
	   *
	   * @param dateStr String
	   * @return boolean
	   */
	  public static  boolean isDate(String dateStr){
	    boolean bl = true;
	    if(dateStr != null ){
	      String[] date = dateStr.split("-");
	      try{
	        int yy = Integer.parseInt(date[0], 10);
	        int mm = Integer.parseInt(date[1], 10);
	        int dd = Integer.parseInt(date[2], 10);
	        if(mm > 12 || mm <= 0 || dd <= 0 || dd > 31) return false;
	        int rndd = ((yy%4==0)&&(yy%100!=0)||(yy%400 == 0))?29:28;
	        switch(mm){
	          case 4:
	          case 6:
	          case 9:
	          case 11:
	            if(dd > 30 || dd<=0)
	              bl = false;
	            break;
	          case 1:
	          case 3:
	          case 5:
	          case 7:
	          case 8:
	          case 10:
	          case 12:
	            if(dd>31 || dd<=0)
	                bl=false;
	            break;
	          case 2:
	            if(dd > rndd || dd <= 0)
	                bl=false;
	            break;
	        }
	      }catch(Exception ex){
	        bl = false;
	      }
	    }
	    return  bl;
	  }
	  
	  /**
	   * 得到两个字符间的字符串 *
	   *
	   * @param string String
	   * @param leftFlag char
	   * @param rightFlag char
	   * @return String
	   */
	  public static  String getParam(String string,char leftFlag,char rightFlag)
	  {
	      String param="";

	      while(string.indexOf(rightFlag)<string.indexOf(leftFlag))
	          string=string.substring(string.indexOf(rightFlag)+1);

	      if(string.indexOf(leftFlag)>=0 && string.indexOf(rightFlag)>=0)
	      {
	          int pos1=string.indexOf(leftFlag);
	          int pos2=string.indexOf(rightFlag);
	          param=string.substring(pos1+1,pos2);
	      }

	      return param;
	  }


	  public  static String getParam(String string,char flag)
	  {
	      if(string == null || string.equalsIgnoreCase(""))
	        return "";

	      String param="";
	      int pos1=string.indexOf(flag);
	      int pos2 = 0;
	      if(pos1 >= 0){
	        pos2 = string.indexOf(flag,pos1+1);

	        if(pos2 > 0){
	          param=string.substring(pos1+1,pos2);
	        }
	      }
	      return param;
	  }


	  /**
	   * 用str2 替换 str与str1 间字串 *
	   *
	   * @param str String
	   * @param str1 String
	   * @param str2 String
	   * @return String
	   */
	  public static  String replace(String str, String str1, String str2)
	  {
	      for(int pos = str.indexOf(str1); pos >= 0; pos = str.indexOf(str1))
	          str = new String(new StringBuffer(str.substring(0, pos)).append(str2).append(str.substring(str1.length() + pos)));

	      return str;
	  }

	  /**
	   * 保留小数位数，四舍五入
	   *
	   * @param ys double
	   * @param xs int
	   * @return double
	   */
	  public static  double sswl(double ys,int xs){
	    double res=0;
	    double sx=java.lang.Math.pow(10,xs);
	    res=java.lang.Math.round(sx*ys)/sx;
	    return res;
	  }



	  /**
	   * bzero
	   *
	   * @param num int
	   * @param bits int
	   * @return String
	   */
	  public  static String bzero(int num, int bits) {
	    String str = String.valueOf(num);
	    String preStr = "";
	    for(int i=0;i<bits - str.length() ;i++){
	      preStr = preStr+"0";
	    }
	    str = preStr+str;
	    return str;
	  }

public static void main(String[] args) {
	System.out.println(TransUtil.numberToChacters(987654321987.231));;
}

	  public static  String numberToChacters(double n){
	          n = sswl(n,2);

	          String result = String.valueOf((long)(n*100));
	          char[] chars = result.toCharArray();
	          //String postfix2 = "";

	          char[] change = {'零','壹','贰','叁','肆','伍','陆','柒','捌','玖'};
	          char[] postfix = {'分','角','元','拾','佰','仟','万','拾','佰','仟','亿','拾','佰','仟','万','拾','佰','仟'};

	          result = "";
	          int size = chars.length - 1;
	          for(int i = size ; i > -1; --i){
	                  chars[size - i] = change[Integer.parseInt(String.valueOf(chars[size - i]))];
	                  result += String.valueOf(chars[size - i]) + String.valueOf(postfix[i]);
	          }
	          for(int i = 0; i < 3; ++i){
	                  result = result.replaceAll("零[拾佰仟]零","零");
	          }
	         
	          result = result.replaceAll("零亿","亿");
	          result = result.replaceAll("零万","万");
	          result = result.replaceAll("零元","元");
	          result = result.replaceAll("零佰","零");
	          result = result.replaceAll("零拾","零");
	          result = result.replaceAll("零仟","零");
	          result = result.replaceAll("亿万","亿");
	          result = result.replaceAll("^壹拾","拾");
	          result = result.replaceAll("零分", "");
	          result = result.replaceAll("零角$", "");
	          result = result.replaceAll("零角", "零");

	          if(result.equals("")){// must be 0
	                  result = "零元";
	          }
	          result = result.replaceAll("元$", "元整");

	          //return String.valueOf(chars);
	          return result;
	  }


}
