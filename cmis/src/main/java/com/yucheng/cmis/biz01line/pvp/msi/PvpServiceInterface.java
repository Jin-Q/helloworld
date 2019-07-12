package com.yucheng.cmis.biz01line.pvp.msi;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.yucheng.cmis.pub.annotation.MethodParam;
import com.yucheng.cmis.pub.annotation.MethodService;
import com.yucheng.cmis.pub.annotation.ModualService;

/**
 * 出账申请对外提供接口服务
 * @author Pansq
 * @version V1.0
 */
@ModualService(modualId="pvp",modualName="出账申请模块",serviceId="pvpServices",serviceDesc="出账申请模块对外提供服务接口",
		className="com.yucheng.cmis.biz01line.pvp.msi.PvpServiceInterface")
public interface PvpServiceInterface {
	
	/**
	 * 通过合同编号生成出账借据号
	 * @param contNo 合同编号
	 * @param connection 数据库连接
	 * @return 借据编号
	 * @throws Exception 
	 */
	@MethodService(method="getBillNoByContNo",desc="通过合同编号生成出账借据号",
			inParam={
				@MethodParam(paramName="contNo",paramDesc="合同编号"),
				@MethodParam(paramName="context",paramDesc="上下文"),
				@MethodParam(paramName="connection",paramDesc="数据库连接")
			},
			outParam=@MethodParam(paramName="String",paramDesc="借据编号"))
	public String getBillNoByContNo(String contNo, Context context, Connection connection) throws Exception ;

}
