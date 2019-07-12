package com.yucheng.cmis.pub;
/**
 * CMISDomain 接口  
 * @Classname com.yucheng.cmis.pub.CMISDomain.java
 * @author wqgang
 * @Since 2009-3-24 上午09:24:08 
 * @Copyright yuchengtech
 * @version 1.0
 */
public interface CMISDomain extends Cloneable  {
	/**
	 * 克隆方法
	 * @return 被克隆对象的实例
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException;
}
