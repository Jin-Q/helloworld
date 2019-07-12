package com.yucheng.cmis.biz01line.ccr.msi;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;

/**
 * 评级模块对外提供服务接口
 * @author 
 * @version V1.0
 */
@ModualService(modualId="ccr",modualName="评级模块",serviceId="ccrServices",serviceDesc="评级模块对外提供服务接口",
		className="com.yucheng.cmis.biz01line.ccr.msi.CcrServiceInterface")
public interface CcrServiceInterface {
	/**
	 * 根据授信申请流水号删除对应个人评级申请信息
	 * @param lmt_serno	个人授信申请流水号
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 
	 * @throws Exception
	 */
	@MethodService(method="deleteCcrAppByLmtSerno",desc="评级结束更新客户评级信息",
			inParam={
				@MethodParam(paramName="lmt_serno",paramDesc="客户码"),
				@MethodParam(paramName="context",paramDesc="上下文"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="String",paramDesc="修改结果"))
	public String deleteCcrAppByLmtSerno(String lmt_serno,Context context, Connection connection) throws Exception ;
	/**
	 * 根据授信申请流水号更新对应个人评级申请信息
	 * @param lmt_serno	个人授信申请流水号
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 
	 * @throws Exception
	 */
	@MethodService(method="updateCcrAppInfo",desc="个人授信结束后更新评级信息",
			inParam={
				@MethodParam(paramName="lmt_serno",paramDesc="授信流水号"),
				@MethodParam(paramName="status",paramDesc="流程结束标识（997--通过，998--否决）"),
				@MethodParam(paramName="context",paramDesc="上下文"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="String",paramDesc="更新结果"))
	public int updateCcrAppInfo(String lmt_serno,String status, Context context, Connection connection) throws Exception ;
	/**
	 * 根据个人评级申请流水号更新对应个人评级申请信息
	 * @param lmt_serno	个人评级申请流水号
	 * @param context	上下文
	 * @param connection 数据库连接
	 * @return 
	 * @throws Exception
	 */
	@MethodService(method="updateCcrAppInfoIndiv",desc="根据个人评级申请流水号更新对应个人评级申请信息",
			inParam={
				@MethodParam(paramName="serno",paramDesc="个人评级申请流水号"),
				@MethodParam(paramName="context",paramDesc="上下文"),
				@MethodParam(paramName="Connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="String",paramDesc="更新结果"))
	public int updateCcrAppInfoIndiv(String serno, Context context, Connection connection) throws Exception ;
	/**
	 * 根据客户码串获得其评级信息
	 * @param CusIdic 客户码串
	 * @param pageInfo 分页信息（不需要分页可传空）
	 * @param dataSource 数据源
	 * @param condition 查询条件（可自行增加过滤）
	 * @return
	 * @throws Exception
	 */
	@MethodService(method="getCcrAppInfoByCusIdStr",desc="根据客户码串获得其评级信息",
			inParam={
				@MethodParam(paramName="CusIdic",paramDesc="客户码串存在IC中，ic中的每个KC客户码需以（cus_id）命名"),
				@MethodParam(paramName="pageInfo",paramDesc="分页信息"),
				@MethodParam(paramName="dataSource",paramDesc="数据库连接"),
				@MethodParam(paramName="condition",paramDesc="查询条件")
			},
			outParam=@MethodParam(paramName="IndexedCollection",paramDesc="查询结果"))
	public IndexedCollection getCcrAppInfoByCusIdStr(IndexedCollection CusIdic,PageInfo pageInfo,DataSource dataSource,String condition) throws Exception ;
	
}
