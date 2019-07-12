package com.yucheng.cmis.pub.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import bsh.Interpreter;

import com.ecc.emp.log.EMPLog;

/**
 * 处理信贷系统中字符串或公式工具类
 * 
 * @Classname com.yucheng.cmis.pub.util.StringUtil.java
 * @author wqgang
 * @Since 2008-9-7 上午10:16:51
 * @Copyright yuchengtech
 * @version 1.0
 */
public class StringUtil {
    private static final StringUtil instance = new StringUtil();

    public StringUtil() {
    }

    public static final StringUtil getInstance() {
        return instance;
    }

    /**
     * 本方法的主要功能是在一个字符串的每个大写字母前面加一个_ ，字符串的首字母除外 然后把整个串以小写输出 例如：cusId-->cus_id
     * 
     * @param str
     *            传入的字符串
     * @return 处理后的字符串
     */

    public static String AddUnderlineByUppercase(String str) {
        // int count=0;
        String rv = "";

        if (null == str || str.trim().length() == 0) {
            return str;
        }

        StringBuffer tmpStr = new StringBuffer("");
        for (int i = 0; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                tmpStr.append("_").append(Character.valueOf(str.charAt(i)));
                // count ++;
            } else {
                tmpStr.append(Character.valueOf(str.charAt(i)));
            }
        }

        rv = tmpStr.toString().toLowerCase();
        if (rv.substring(0, 1).equals("_")) {
            rv = rv.substring(1);
        }
        return rv;
    }

    public static String AddUnderlineByUppercase1(String str) {
        /*
         * 创建一个数组列表，用来存储大写字母的位置
         */
        /*
         * ArrayList<Integer> al = new ArrayList<Integer>(); int count = 0;
         * for(int j=1; j<str.length(); j++){
         * if(Character.isUpperCase(str.charAt(j))){ al.add(j); count ++; }
         */
        ArrayList<Integer> al = new ArrayList<Integer>();
        int count = 0;
        for (int j = 1; j < str.length(); j++) {
            if (Character.isUpperCase(str.charAt(j))) {
                al.add(j);
                count++;
            }
            if (NewStringUtils.isNumeric((Character.toString(str.charAt(j))))) {
                al.add(j);
                count++;
            }
        }
        if (count == 0) {
            return str;
        }
        StringBuffer tmpStr = new StringBuffer("");
        /*
         * 根据大小字母的位置在其前面加上 _
         */
        for (int x = 1; x <= al.size(); x++) {

            if ((x == 1) && (x == al.size())) {
                tmpStr.append(str.substring(0, al.get(x - 1))).append("_").append(str.substring(al.get(x - 1), str.length()));
            } else if ((x == 1) && (x != al.size())) {
                tmpStr.append(str.substring(0, al.get(x - 1))).append("_");
            } else if ((x != 1) && (x == al.size())) {
                if (al.get(x - 1) == str.length()) {
                    tmpStr.append(str.substring(al.get(x - 2), al.get(x - 1)));
                } else {
                    tmpStr.append(str.substring(al.get(x - 2), al.get(x - 1))).append("_").append(str.substring(al.get(x - 1), str.length()));
                }
            } else {
                tmpStr.append(str.substring(al.get(x - 2), al.get(x - 1))).append("_");
            }
        }
        // 转换成小写返回
        return tmpStr.toString().toLowerCase();
    }

    public static String AddUnderlineByUppercase2(String str) {

        // 创建一个数组列表，用来存储大写字母的位置

        // ArrayList<Integer> al = new ArrayList<Integer>();
        // int count = 0;
        // for(int j=1; j<str.length(); j++){
        // if(Character.isUpperCase(str.charAt(j))){
        // al.add(j);
        // count ++;
        // }
        ArrayList<Integer> al = new ArrayList<Integer>();
        int count = 0;
        for (int j = 0; j < str.length(); j++) {
            if (Character.isUpperCase(str.charAt(j))) {
                al.add(j);
                count++;
            }
            // if(NewStringUtils.isNumeric((Character.toString(str.charAt(j))))){
            // al.add(j);
            // count ++;
            // }
        }
        if (count == 0) {
            return str;
        }
        StringBuffer tmpStr = new StringBuffer("");

        // 根据大小字母的位置在其前面加上 _

        for (int x = 0; x < al.size(); x++) {
            if ((x == 0) && (x == al.size() - 1)) {
                if (str.length() == 1) {
                    tmpStr.append(str);
                } else {
                    tmpStr.append(str.substring(0, al.get(x))).append("_").append(str.substring(al.get(x), str.length()));
                }

            } else if ((x == 0) && (x != al.size() - 1)) {
                tmpStr.append(str.substring(0, al.get(x))).append("_");
            } else if ((x != 0) && (x == al.size() - 1)) {
                if (al.get(x) == str.length()) {
                    tmpStr.append(str.substring(al.get(x - 1), al.get(x)));
                } else {
                    tmpStr.append(str.substring(al.get(x - 1), al.get(x))).append("_").append(str.substring(al.get(x), str.length()));
                }
            } else {
                tmpStr.append(str.substring(al.get(x - 1), al.get(x))).append("_");
            }
        }
        // 转换成小写返回
        String rv = tmpStr.toString().toLowerCase();
        if (rv.substring(0, 1).equals("_")) {
            rv = rv.substring(1);
        }
        return rv;
    }

    /**
     * 改变字符串中的某一位的字符值
     * 
     * @param source
     *            需要改变的字符串
     * @param position
     *            位置
     * @param changeChar
     *            改变后的字符
     * @return 输入 ("abc",2,'e') 输出"abe"
     */
    public static String changeCharInString(String source, int position, char changeChar) throws Exception {
        if (source == null) {
            return null;
        }
        char[] sourceList = source.toCharArray();

        if (source.length() <= position) {
            throw new Exception("输入参数不合法");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < sourceList.length; i++) {
            if (i == position) {
                sb.append(changeChar);
            } else {
                sb.append(sourceList[i]);
            }
        }

        return sb.toString();
    }

    /**
     * 加密 *
     * 
     * @param source
     *            byte[]
     * @return byte[]
     */
    public static byte[] addKey(byte[] source) {
        byte[] reByte = new byte[source.length];
        String key = "dlvanda";
        byte[] keyByte = key.getBytes();
        for (int i = 0; i < source.length; i++) {
            int j = i % keyByte.length;
            reByte[i] = (byte) (source[i] + keyByte[j]);
        }
        return reByte;
    }

    /**
     * 解密 *
     * 
     * @param source
     *            byte[]
     * @return byte[]
     */
    public static byte[] desKey(byte[] source) {
        byte[] reByte = new byte[source.length];
        String key = "dlvanda";
        byte[] keyByte = key.getBytes();
        for (int i = 0; i < source.length; i++) {
            int j = i % keyByte.length;
            reByte[i] = (byte) (source[i] - keyByte[j]);
        }
        return reByte;
    }

    /**
     * 加密 *
     * 
     * @param source
     *            String
     * @return String
     */
    // public String addKey(String source) {
    // Descrypt des = new Descrypt();
    // return des.descrypt(source,"dlvanda");
    // }

    /**
     * 返回两位数据字串 *
     * 
     * @param sz
     *            int
     * @return String
     */
    public static String bZero(int sz) {
        return (sz < 10 ? ("0" + String.valueOf(sz)) : String.valueOf(sz));
    }

    /**
     * 字符串("|")到数组 *
     * 
     * @param string
     *            String
     * @return String[]
     */
    public static String[] strToArray(String string) {
        String tmpString;
        String[] returnArray = null;
        if (string == null || string.equalsIgnoreCase("")) {
            returnArray = new String[1];
            returnArray[0] = "";
        } else {
            int j = 0, jsInt = 0;
            for (int i = 0; i < string.length(); i++) {
                if (string.charAt(i) == '|') {
                    j++;
                }
            }
            if (j == 0) {
                returnArray = new String[1];
                returnArray[0] = string;
            } else {
                returnArray = new String[j];
                j = 0;
                for (int i = 0; i < string.length(); i++) {
                    if (string.charAt(i) == '|') {
                        if (j == i) {
                            tmpString = " ";
                        } else {
                            tmpString = string.substring(j, i).trim();
                        }
                        j = i + 1;
                        returnArray[jsInt] = tmpString;
                        jsInt += 1;
                    }
                }
            }
        }
        return returnArray;
    }

    /**
     * 字符串到数组 *
     * 
     * @param string
     *            String
     * @param separator
     *            char
     * @return String[]
     */
    public static String[] strToArray(String string, char separator) {
        String[] returnArray;
        if (string == null || string.equalsIgnoreCase("")) {
            returnArray = new String[1];
            returnArray[0] = "";
        } else {
            int j = 0, lastpos = 0;
            ArrayList pos = new ArrayList();
            pos.add(String.valueOf(-1));
            for (int i = 0; i < string.length(); i++) {
                if (string.charAt(i) == separator) {
                    j++;
                    lastpos = i;
                    pos.add(String.valueOf(i));
                }
            }
            if (lastpos != string.length()) {
                j++;
                pos.add(String.valueOf(string.length()));
            }

            int[] ps = new int[pos.size()];
            for (int i = 0; i < ps.length; i++) {
                ps[i] = Integer.parseInt(String.valueOf(pos.get(i)));
            }
            returnArray = new String[ps.length - 1];
            for (int i = 0; i < returnArray.length; i++) {
                returnArray[i] = string.substring(ps[i] + 1, ps[i + 1]).trim();
            }

        }
        return returnArray;
    }

    /**
     * 数组到字串,用"|"隔开 *
     * 
     * @param strArray
     *            String[]
     * @return String
     */
    public static String arrayToStr(String strArray[]) {
        String reStr = "";
        if (strArray != null) {
            for (int i = 0; i < strArray.length; i++) {
                if (strArray[i] != null) {
                    reStr = reStr + strArray[i].trim() + "|";
                } else {
                    reStr = reStr + "|";
                }
            }
        }
        return reStr;
    }

    /**
     * 得到两个字符间的字符串 *
     * 
     * @param string
     *            String
     * @param leftFlag
     *            char
     * @param rightFlag
     *            char
     * @return String
     */
    public static String getParamString(String string, char leftFlag, char rightFlag) {
        String param = "";

        while (string.indexOf(rightFlag) < string.indexOf(leftFlag))
            string = string.substring(string.indexOf(rightFlag) + 1);

        if (string.indexOf(leftFlag) >= 0 && string.indexOf(rightFlag) >= 0) {
            int pos1 = string.indexOf(leftFlag);
            int pos2 = string.indexOf(rightFlag);
            param = string.substring(pos1 + 1, pos2);
        }

        return param;
    }

    /**
     * 得到两个相同字符间的字符串 *
     * 
     * @param string
     *            String
     * @param leftFlag
     *            char
     * @param rightFlag
     *            char
     * @return String
     */
    public static String getParam(String string, char flag) {
        if (string == null || string.equalsIgnoreCase(""))
            return "";

        String param = "";
        int pos1 = string.indexOf(flag);
        int pos2 = 0;
        if (pos1 >= 0) {
            pos2 = string.indexOf(flag, pos1 + 1);

            if (pos2 > 0) {
                param = string.substring(pos1 + 1, pos2);
            }
        }
        return param;
    }

    /**
     * 得到一个字符串中所有两个字符间的字符串 *
     * 
     * @param string
     *            String
     * @param leftFlag
     *            char
     * @param rightFlag
     *            char
     * @return List
     */
    public static List getParamList(String string, char leftFlag, char rightFlag) {
        String param = "";
        List<String> lt = new ArrayList<String>();
        while (string.indexOf(leftFlag) != -1) {
            while (string.indexOf(rightFlag) < string.indexOf(leftFlag))
                string = string.substring(string.indexOf(rightFlag) + 1);

            if (string.indexOf(leftFlag) >= 0 && string.indexOf(rightFlag) >= 0) {
                int pos1 = string.indexOf(leftFlag);
                int pos2 = string.indexOf(rightFlag);
                param = string.substring(pos1 + 1, pos2);
                string = string.substring(pos2 + 1);
                lt.add(param);
            }

        }
        return lt;
    }
    /**
     * 目标字符串编码转化
     * @param targetStr
     * 目标字符串
     * @param localcode
     * 目标字符串编码
     * @param targetCode
     * 需要转化字符串编码
     * @return
     */
    public static String getStrEncode(String targetStr, String localcode, String targetCode) {      
        try {
            byte[] bytes = targetStr.getBytes(localcode);
            targetStr = new String(bytes, targetCode);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return targetStr;

    }

    /**
     * 用str2 替换 str与str1 间字串 *
     * 
     * @param str
     *            String
     * @param str1
     *            String
     * @param str2
     *            String
     * @return String
     */
    public static String replace(String str, String str1, String str2) {
        for (int pos = str.indexOf(str1); pos >= 0; pos = str.indexOf(str1))
            str = new String(new StringBuffer(str.substring(0, pos)).append(str2).append(str.substring(str1.length() + pos)));

        return str;
    }

    /**
     * 根据算术式String求出结果
     * 
     * @param str
     *            String
     * @return String result
     */
    public static String getFormulaValue(String string) {
        Interpreter interpreter = new Interpreter();
        DecimalFormat df = new DecimalFormat("0.00");
        if (string.equals(""))
            return "0";

        String result;

//        string = replace(string, "MAX", "com.dtbridge.core.util.Function.Max");
//        string = replace(string, "MIN", "com.dtbridge.core.util.Function.Min");
//        string = replace(string, "INT", "com.dtbridge.core.util.Function.Int");
//        string = replace(string, "ADD", "com.dtbridge.core.util.Function.Add");
        
        string = replace(string, "MAX", "com.yucheng.cmis.pub.util.SInfoUtils.getMaxValue");
		string = replace(string, "MIN", "com.yucheng.cmis.pub.util.SInfoUtils.getMinValue");
		string = replace(string, "INT", "com.yucheng.cmis.pub.util.SInfoUtils.getIntValue");
		string = replace(string, "ADD", "com.yucheng.cmis.pub.util.SInfoUtils.getTotlValue");
        string = replace(string, "--", "+");
        string = replace(string, "+-", "-");

        try {
            // 在减去负值时会发生错误
            // System.out.println("公式为："+string);
            EMPLog.log("", EMPLog.INFO, 0, string);
            interpreter.eval("try { result=".concat(string) + ";}catch(ArithmeticException e){result=0;}");
            result = interpreter.get("result").toString();

            if (result.equals("Infinity") || result.equals("NaN") || result.equals("-∞") || result.equals("∞"))
                result = "0";
            else
                result = df.format(Double.parseDouble(result));
            if (result.equals("-∞") || result.equals("∞"))
                result = "0";
            // System.out.println("String:"+string+" Result:"+result);
        } catch (Exception ex) {
            // ex.printStackTrace();
            result = "0";
        }
        return result;
    }

    /**
     * 根据算术式String求出结果
     * 
     * @param str
     *            String
     * @return String result
     */
    public static String getFormulaValue4Fnc(String string) {
        Interpreter interpreter = new Interpreter();
        DecimalFormat df = new DecimalFormat("0.00");
        if (string.equals(""))
            return "0";

        String result;

//        string = replace(string, "MAX", "com.dtbridge.core.util.Function.Max");
//        string = replace(string, "MIN", "com.dtbridge.core.util.Function.Min");
//        string = replace(string, "INT", "com.dtbridge.core.util.Function.Int");
//        string = replace(string, "ADD", "com.dtbridge.core.util.Function.Add");
        
        string = replace(string, "MAX", "com.yucheng.cmis.pub.util.SInfoUtils.getMaxValue");
		string = replace(string, "MIN", "com.yucheng.cmis.pub.util.SInfoUtils.getMinValue");
		string = replace(string, "INT", "com.yucheng.cmis.pub.util.SInfoUtils.getIntValue");
		string = replace(string, "ADD", "com.yucheng.cmis.pub.util.SInfoUtils.getTotlValue");
        string = replace(string, "--", "+");
        string = replace(string, "+-", "-");

        try {
            // 在减去负值时会发生错误
            // System.out.println("公式为："+string);
            EMPLog.log("", EMPLog.INFO, 0, string);
            interpreter.eval("try { result=".concat(string) + ";}catch(ArithmeticException e){result=0;}");
            result = interpreter.get("result").toString();

            if (result.equals("Infinity") || result.equals("NaN") || result.equals("-∞") || result.equals("∞"))
//                result = "9999999999.99";
            	result = "0";
            else
                result = df.format(Double.parseDouble(result));
            if (result.equals("-∞") || result.equals("∞"))
//                result = "9999999999.99";
            	result = "0";
            // System.out.println("String:"+string+" Result:"+result);
        } catch (Exception ex) {
            // ex.printStackTrace();
//            result = "9999999999.99";
        	result = "0";
        }
        return result;
    }

    /**
     * 得到10位随机码
     * 
     * @return
     */
    public static String getRandomId() {
        String str = "";
        int[] t = new int[10];
        Random rand = new Random();
        for (int i = 0; i < t.length; i++) {
            t[i] = rand.nextInt(10);
        }
        for (int i = 0; i < t.length; i++) {
            str += t[i];
        }
        return str;
    }

    /**
     * @param stringList
     *            字符串集合
     * @return 返回SQL语句中in的部分 如: '0001','0002','0003'
     */
    public static String listToString(List<String> stringList) {
        if (stringList == null || stringList.isEmpty()) {
            return null;
        } else {
            StringBuffer sb = new StringBuffer();
            for (Iterator<String> iterator = stringList.iterator(); iterator.hasNext();) {
                String string = (String) iterator.next();
                sb.append(",'").append(string).append("'");
            }
            return sb.toString().substring(1);
        }
    }

    /**
     * @param
     * @return
     */
    public static String transConditionStr(String conditionStr, String field) {

        String str = " " + field + " ";
        String str2 = "(" + field + " ";
        int j = conditionStr.indexOf(str);
        if (j != -1) {
            conditionStr = conditionStr.replace(str + "= '", str + "like '%");
            str = str + "like '%";
            int otherStart = conditionStr.indexOf(str) + str.length();
            int oIndex = conditionStr.indexOf("'", otherStart);
            conditionStr = conditionStr.substring(0, oIndex) + "%'" + conditionStr.substring(oIndex + 1);
            // conditionStr = conditionStr.replace(conditionStr.substring(j,
            // oIndex+1),conditionStr.substring(j,oIndex)+"%'");
        }
        if (conditionStr.indexOf(str2) != -1) {
            conditionStr = conditionStr.replace(str2 + "= '", str2 + "like '%");
            str2 = str2 + "like '%";
            int otherStart = conditionStr.indexOf(str2) + str2.length();
            int oIndex = conditionStr.indexOf("'", otherStart);
            conditionStr = conditionStr.substring(0, oIndex) + "%'" + conditionStr.substring(oIndex + 1);
            // conditionStr = conditionStr.replace(conditionStr.substring(j,
            // oIndex+1),conditionStr.substring(j,oIndex)+"%'");
        }

        return conditionStr;
    }
    public static String getFomartDouble2String(double num) {
		  
		BigDecimal bigItemVal=new BigDecimal(num);
		BigDecimal bigone=new BigDecimal("1");
		String str = bigItemVal.divide(bigone,2,BigDecimal.ROUND_HALF_DOWN).toString();
		
		return str;
	  }
    /*
     * public static void main(String[] args) {
     * System.out.println(StringUtil.AddUnderlineByUppercase2("A"));
     * System.out.println(StringUtil.AddUnderlineByUppercase2("BB"));
     * System.out.println(StringUtil.AddUnderlineByUppercase2("A123"));
     * System.out.println(StringUtil.AddUnderlineByUppercase2("aaA"));
     * System.out.println(StringUtil.AddUnderlineByUppercase2("A12A3b"));
     * System.out.println(StringUtil.AddUnderlineByUppercase2("A12A3bCDDDD"));
     * System.out.println("a12asd123".toString().toLowerCase());
     * System.out.println("***"+"a".substring(0,0)); }
     */

}
