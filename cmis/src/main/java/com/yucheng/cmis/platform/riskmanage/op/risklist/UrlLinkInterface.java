package com.yucheng.cmis.platform.riskmanage.op.risklist;

import com.ecc.emp.core.Context;

public interface UrlLinkInterface {
	/**
	 * 获取风险拦截连接url
	 * @param className 类名
	 * @param serno 流水号
	 * @return url连接
	 */
	public String getUrl(Context context, String className, String serno);
}
