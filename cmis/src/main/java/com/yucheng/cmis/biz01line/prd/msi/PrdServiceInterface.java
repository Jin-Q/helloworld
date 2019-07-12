package com.yucheng.cmis.biz01line.prd.msi;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.prd.domain.PrdBasicinfo;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;
import com.yucheng.cmis.pub.annotation.MethodService.MethodType;

/**
 * 产品配置模块对外提供服务接口
 * @author Pansq
 * @version V1.0
 */
@ModualService(modualId="prd",modualName="产品配置模块",serviceId="prdServices",serviceDesc="产品配置模块对外提供服务接口",
		className="com.yucheng.cmis.biz01line.prd.msi.PrdServiceInterface")
public interface PrdServiceInterface {
	/**
	 * 通过产品编号获得产品配置信息
	 * @param connection
	 * @return List<PrdBasicinfo>
	 * @throws Exception
	 */
	@MethodService(method="getPrdBasicinfoList",desc="通过产品编号获得产品配置信息",
			inParam={
				@MethodParam(paramName="prdid",paramDesc="产品编号"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="PrdBasicinfo",paramDesc="产品配置信息DOMAIN"))
	public PrdBasicinfo getPrdBasicinfoList(String prdid, Connection connection) throws Exception ;
	
	/**
	 * 通过产品编号、表单KeyedCollection将KeyedCollection中数据通过映射关系插入目标表中
	 * @param prdId 产品编号
	 *  @param mapType 映射种类【cont合同映射\pvp出账映射】
	 * @param kModel 原表模型封装成的KeyedCollection
	 * @param toTable 目标表
	 * @param connection 数据库连接
	 * @return Map<String,String>
	 * @throws Exception
	 */
	@MethodService(
			method="insertMsgByKModelFromPrdMap",desc="通过映射关系将源表模型中数据插入目标表中",
			inParam={
					@MethodParam(paramName="prdId",paramDesc="产品编号"),
					@MethodParam(paramName="mapType",paramDesc="映射种类[cont合同映射,pvp出账映射]"),
					@MethodParam(paramName="kModel",paramDesc="源表表模型封装成的KeyedCollection"),
					@MethodParam(paramName="toDefKColl",paramDesc="目标表默认值封装成的KeyedCollection"),
					@MethodParam(paramName="toTable",paramDesc="目标表模型名称"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="处理结果以及反馈信息封装成的KeyedCollection")
	)
	public KeyedCollection insertMsgByKModelFromPrdMap(String prdId, String mapType, KeyedCollection kModel, KeyedCollection toDefKColl, String toTable, Context context, Connection connection) throws Exception ;
	
	/**
	 * 通过产品编号获得产品的适用机构列表信息
	 * @param prdid 产品编号
	 * @return IndexedCollection 
	 * @throws Exception
	 */
	@MethodService(
			method="getPrdApplyOrgByPrdId",desc="通过产品编号获得产品的适用机构列表信息",
			inParam={
					@MethodParam(paramName="prdid",paramDesc="产品编号"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="IndexedCollection",paramDesc="适用机构组成的IndexedCollection")
	)
	public IndexedCollection getPrdApplyOrgByPrdId(String prdid,Connection connection) throws Exception ;
	
	/**
	 * 通过业务品种、币种、期限类型获取利率信息
	 * @param prdId 业务品种
	 * @param currType 币种
	 * @param termM 期限
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 利率以及反馈信息组成的KeyedCollection 
	 * @throws Exception
	 */
	@MethodService(method="getPrdRate",desc="通过业务品种、币种、期限类型获取利率信息",
			inParam={
					@MethodParam(paramName="prdId",paramDesc="产品编号"),
					@MethodParam(paramName="currType",paramDesc="币种"),
					@MethodParam(paramName="termM",paramDesc="期限"),
					@MethodParam(paramName="context",paramDesc="上下文"),
					@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="KeyedCollection",paramDesc="利率的KeyedCollection")
	)
	public KeyedCollection getPrdRate(String prdId,String currType,int termM,Context context,Connection connection) throws Exception ;
	
	/**
	 * 通过币种、利率种类查询LIBOR牌告基准利率 
	 * @param currType 币种
	 * @param irType 利率种类
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 利率以及反馈信息组成的KeyedCollection 
	 * @throws Exception
	 */
	@MethodService(method="getLiborRate",desc="通过币种、利率种类查询LIBOR牌告基准利率 ",
			inParam={
			@MethodParam(paramName="currType",paramDesc="币种"),
			@MethodParam(paramName="irType",paramDesc="利率种类"),
			@MethodParam(paramName="context",paramDesc="上下文"),
			@MethodParam(paramName="connection",paramDesc="数据库连接")
	       },
	       outParam=@MethodParam(paramName="KeyedCollection",paramDesc="KeyedCollection")
	) 
	public KeyedCollection getLiborRate(String currType,String irType,Context context,Connection connection) throws Exception ;
}
