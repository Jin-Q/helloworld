package com.yucheng.cmis.msi.biz01line.modualtwo.interfaces;

import java.sql.Connection;
import java.util.List;

import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;
import com.yucheng.cmis.msi.biz01line.modualtwo.doamins.BIZ;

/**
 * <p>
 * 	测试模块，模块二对外提供服务
 * 	<ul>描述：其它模块只通过该类来调用模块二的功能，不允许绕过该类直接访问该模块</ul>
 * </p>
 * 
 * 
 * @author yuhq
 * @version 3.0
 * @since 3.0
 */
@ModualService(serviceId="modualServiceTwo",modualId="modualTwo", modualName="测试模块二",
				serviceDesc="测试模块，模块二对外提供服务",
				className="com.yucheng.cmis.standard.biz01line.modualtwo.interfaces.ModualTwoServiceInterface")
public interface ModualTwoServiceInterface {

	/**
	 * 根据客户号取得该客户所有业务
	 * @param cusId　客户码
	 * @param con　数据库连接
	 * @return　客户的集合
	 */
	@MethodService(method="getBizsByCusId", desc="根据客户号取得该客户所有业务",
					inParam={
						@MethodParam(paramName="cusId",paramDesc="客户码"),
						@MethodParam(paramName="Connection",paramDesc="数据库连接"),
					})
	public List<BIZ> getBizsByCusId(String cusId, Connection con);
	
}
