package com.yucheng.cmis.platform.riskmanage.interfaces;

import java.sql.Connection;
import java.util.Map;

import com.ecc.emp.core.Context;

public interface RiskManageInterface {
	/**
	 * 风险拦截业务实现类
	 * @param tableName 表模型
	 * @param serno 业务流水号
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 返回结果的Map
	 * @throws Exception
	 */
	public Map<String,String> getResultMap(String tableName, String serno,Context context,Connection connection) throws Exception;
}
