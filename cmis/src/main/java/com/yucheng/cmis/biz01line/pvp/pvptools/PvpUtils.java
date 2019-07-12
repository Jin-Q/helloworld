package com.yucheng.cmis.biz01line.pvp.pvptools;

public class PvpUtils {
	/**
	 * 返回指定长度的格式化字符串
	 * @param len 返回长度
	 * @param sernum 序列号
	 * @return String
	 */
	public static String numFormatToSeq(int len,String sernum){
		if (len <= 0) return "";
        StringBuffer seq = new StringBuffer();
        for(int i=0;i<len;i++){
        	seq.append('0');
        }
        seq.append(sernum);
        return seq.substring(seq.length()-len,seq.length());
    }
}
