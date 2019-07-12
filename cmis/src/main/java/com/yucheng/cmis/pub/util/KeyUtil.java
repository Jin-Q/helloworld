package com.yucheng.cmis.pub.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @docRoot
 * 用于产生各种各样的唯一的关键字段值
 * @author 李广明
 * @version 1.0
 * @date 2006-12-06
 *
 */
public class KeyUtil {
	
	/**
	 * 产生唯一的值,用作于关键字段值
	 * 考虑到并发问题,作一定的并发处理
	 * @return
	 */
	public synchronized final static String createUniqueKey() {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyyMMddHHmmssSSS",
				Locale.ENGLISH);
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return sdf.format(new Date());
	}
	
	public synchronized final static String createShortUniqueKey() {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyyMMddHHmmssSSS",
				Locale.ENGLISH);
		Date d1 = null;
		try {
			d1 = sdf.parse("20000101010101000");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date d2 = new Date();
		long beginTime = d1.getTime();
		
		long endTime = d2.getTime();
//		long betweenDays = (long) ((endTime - beginTime) / (1000 * 60));
//		System.out.println(betweenDays);
		return new Long(endTime - beginTime).toString();
	}

	/**
	 * 测试是否存在重复的
	 * @param args
	 */
	public static void main(String[] args) {
		System.err.println(KeyUtil.createUniqueKey());
		System.err.println(KeyUtil.createUniqueKey());
		System.err.println(KeyUtil.createUniqueKey());
		System.err.println(KeyUtil.createUniqueKey());
		System.err.println(KeyUtil.createUniqueKey());
		System.err.println(KeyUtil.createUniqueKey());
		System.err.println(KeyUtil.createUniqueKey());
		
		System.err.println(KeyUtil.createShortUniqueKey());
		System.err.println(KeyUtil.createShortUniqueKey());
		System.err.println(KeyUtil.createShortUniqueKey());
		System.err.println(KeyUtil.createShortUniqueKey());
		System.err.println(KeyUtil.createShortUniqueKey());

	}

}