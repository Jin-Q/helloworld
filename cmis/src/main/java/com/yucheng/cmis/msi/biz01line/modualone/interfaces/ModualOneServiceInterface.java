package com.yucheng.cmis.msi.biz01line.modualone.interfaces;

import java.sql.Connection;
import java.util.List;

import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;
import com.yucheng.cmis.msi.biz01line.modualone.domains.CusCom;

/**
 * <p>
 * 	测试模块，模块一对外提供服务
 * </p>
 * 
 * 
 * @author yuhq
 * @version 3.0
 * @since 3.0
 */

@ModualService(serviceId="modualServiceOne",modualId="modualOne",modualName="测试模块一",
				serviceDesc="测试模块，模块一对外提供的所有服务都在该接口定义下",
				className="com.yucheng.cmis.pub.standard.biz01line.modualone.interfaces.ModualOneServiceInterface")
public interface ModualOneServiceInterface {

	/**
	 * 根据客户号获取客户信息
	 * @param id　客户ＩＤ
	 * @param con 数据库连接
	 * @return　CusCom　客户信息Domain{@link com.yucheng.cmis.pub.standard.biz01line.modualone.domains.CusCom}
	 */
	@MethodService(method="getCusComById", desc="根据客户号获取客户信息",
					inParam={
						@MethodParam(paramName="id",paramDesc="客户码"),
						@MethodParam(paramName="Connection",paramDesc="数据库连接"),
					},
					outParam={
						@MethodParam(paramName="CusCom",paramDesc="客户信息Domain"),
					})
	public CusCom getCusComById(String id, Connection con);
	

	/**
	 * 获取所有客户
	 * 
	 * @param con 数据库连接
	 * @return　客户集合
	 */
	@MethodService(method="getAllCusCom", desc="获取所有客户")
	public List<CusCom> getAllCusCom(Connection con);
}
