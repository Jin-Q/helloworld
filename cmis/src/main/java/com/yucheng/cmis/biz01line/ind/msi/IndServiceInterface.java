package com.yucheng.cmis.biz01line.ind.msi;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;

/**
 * 
 * <p>
 * 	资源权限模块对外提供的服务接口
 * </P
 * 
 * @author yuhq
 * @version 1.0
 * @since 1.0
 */
@ModualService(serviceId="indServices",serviceDesc="指标管理模块对外提供的服务接口",
				modualId="ind",modualName="指标管理模块",className="com.yucheng.cmis.biz01line.ind.msi.IndServiceInterface")
public interface IndServiceInterface {

	/**
	 * <p>
	 *  获取指标组信息
	 * </p>
	 * 
	 * @param groupno 指标组编号
	 * @param con 数据库连接
	 * @throws Exception 
	 */
	@MethodService(method="getIndGroup",desc="获取指标组信息",
			inParam={
			@MethodParam(paramName="groupno",paramDesc="指标组编号"),
			@MethodParam(paramName="conn",paramDesc="数据库连接")
		    },
		    outParam=@MethodParam(paramName="kcoll",paramDesc="KeyedCollection 指标组信息"))
	public KeyedCollection getIndGroup(String groupno, Connection con) throws Exception;
	
	/**
	 * <p>
	 *  获取指标模型信息
	 * </p>
	 * 
	 * @param groupno 指标组编号
	 * @param con 数据库连接
	 * @throws Exception 
	 */
	@MethodService(method="getIndModel",desc="获取指标模型信息",
			inParam={
			@MethodParam(paramName="modelno",paramDesc="指标模型编号"),
			@MethodParam(paramName="conn",paramDesc="数据库连接")
		    },
		    outParam=@MethodParam(paramName="kcoll",paramDesc="KeyedCollection 指标模型信息"))
	public KeyedCollection getIndModel(String modelno, Connection con) throws Exception;
	
	@MethodService(method="queryGroupIndexesWithShuffle",desc="获取指标组下的指标信息",
			inParam={
			@MethodParam(paramName="map",paramDesc="map中存trans_id：交易id，group_no指标组编号，group_name指标组名称")
		    },
		    outParam=@MethodParam(paramName="list",paramDesc="ArrayList<HashMap> 组下规则交易的所有指标信息"))
	public ArrayList<HashMap> queryGroupIndexesWithShuffle(Map map) throws Exception;
}
