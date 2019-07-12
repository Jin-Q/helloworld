package com.yucheng.cmis.biz01line.cus.cushand.extInterface;

import java.sql.Connection;
import java.util.Map;
/**
 * 客户移交接口，根据实际需求，实现扩展处理时需要 继承此接口
 * @author HuChunyan
 * 2011-3-3
 */
public interface HandoverInterface {
	/**
	 * 移交前的处理
	 * @param map 前台传递过来的参数
	 * @param conn 数据库连接
	 * @return
	 */
	
	Map<String,Object> beforAction(Map<String,Object> map,Connection conn);
/**
 * 移交后的处理
 * @param map 执行sql的变参
 * @param conn 数据库连接
 */
	void afterAction(Map<String,Object> map, Connection conn);
}
