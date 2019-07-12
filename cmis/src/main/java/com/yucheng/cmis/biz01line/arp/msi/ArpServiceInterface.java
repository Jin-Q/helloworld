package com.yucheng.cmis.biz01line.arp.msi;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;

/**
 * 资产保全模块对外接口
 * @author GC
 */
@ModualService(modualId="arp",modualName="资产保全",serviceId="arpServices",serviceDesc="资产保全模块对外提供服务接口",
		className="com.yucheng.cmis.biz01line.arp.msi.ArpServiceInterface")
public interface ArpServiceInterface {
	
	/**
	 * 根据查询条件返回资产保全表信息iColl
	 * @param kColl (默认参数1.type 2.condition 3.tableName，其他按需扩展)
	 * @param context
	 * @param connection
	 * @return IndexedCollection
	 * @throws Exception
	 */
	@MethodService(method="getArpIcollByCondition",desc="根据查询条件返回资产保全表信息iColl",
			inParam={
			@MethodParam(paramName="kColl",paramDesc="所有条件集合kColl"),
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	},
	outParam=@MethodParam(paramName="IndexedCollection",paramDesc="符合条件的保全表信息iColl"))
	public IndexedCollection getArpIcollByCondition(KeyedCollection kColl, Context context, Connection connection) throws Exception;
	
	/**
	 * 根据查询条件返回资产保全表信息kColl
	 * @param kColl (默认参数1.type 2.condition 3.tableName，其他按需扩展)
	 * @param context
	 * @param connection
	 * @return KeyedCollection
	 * @throws Exception
	 */
	@MethodService(method="getArpIcollByCondition",desc="根据查询条件返回资产保全表信息kColl",
			inParam={
			@MethodParam(paramName="kColl",paramDesc="所有条件集合kColl"),
			@MethodParam(paramName="context",paramDesc="上下文"), 
			@MethodParam(paramName="Connection",paramDesc="数据库连接")
	},
	outParam=@MethodParam(paramName="KeyedCollection",paramDesc="符合条件的保全表信息kColl"))
	public KeyedCollection getArpKcollByCondition(KeyedCollection kColl, Context context, Connection connection) throws Exception;
	
}