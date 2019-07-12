package com.yucheng.cmis.biz01line.psp.msi;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;
import com.yucheng.cmis.pub.annotation.MethodService.MethodType;


@ModualService(modualId="psp",modualName="贷后管理",serviceId="pspServices",serviceDesc="贷后管理模块对外提供服务接口",
		className="com.yucheng.cmis.biz01line.psp.msi.PspServiceInterface")
public interface PspServiceInterface {
	
	/**
	 * 根据押品编号获得押品重估信息
	 * @param guaranty_no 押品编号
	 * @return res_value 返回押品重估信息列表
	 */
	@MethodService(method="getPspGuarantyValueReevalList", desc="返回贷后管理模块的担保品重估信息",
			inParam={
				@MethodParam(paramName="guaranty_no",paramDesc="押品编号"),
				@MethodParam(paramName="connection",paramDesc="数据库连接"),
				@MethodParam(paramName="context",paramDesc="context")
			},
			outParam={
				@MethodParam(paramName="res_value",paramDesc="返回押品重估信息列表")
			},
			methodType=MethodType.JAVA,
			example="getPspGuarantyValueReevalList(guaranty_no,connection,context)")
	public IndexedCollection getPspGuarantyValueReevalList(String guaranty_no,Connection connection,Context context) throws EMPException;
}
