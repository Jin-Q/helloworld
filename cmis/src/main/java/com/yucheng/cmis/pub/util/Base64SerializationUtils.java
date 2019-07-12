/**
 * 
 */
package com.yucheng.cmis.pub.util;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;

/**
 * 提供将对象序列化为文本的能力
 * 
 */
public class Base64SerializationUtils extends SerializationUtils {

	/**
	 * 序列化对象为Bas64编码的String
	 * 
	 * @param obj
	 *            要序列化的对象
	 * @return 序列化后的Base64编码
	 */
	public static String serializeString(Serializable obj) {
		byte[] data = serialize(obj);
		return Base64.encodeBytes(data);
	}

	/**
	 * 从Base64编码反序列化对象
	 * 
	 * @param encode
	 *            编码
	 * @return 对象
	 */
	public static Object deserializeString(String encode) {
		byte[] data;
		try {
			data = Base64.decode(encode);
			return deserialize(data);
		} catch (IOException e) {
			throw new IllegalArgumentException("错误的序列化编码 - " + encode, e);
		}
	}

}
