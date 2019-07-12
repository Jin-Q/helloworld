package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;
import java.util.Map;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.esb.interfaces.ClientTradeInterface;

public abstract class TranClient implements ClientTradeInterface {
	/**
	 * XD系统作为客户端时，接收配置的交易，转发给不同的交易处理类
	 * @param context 上下文
	 * @param connection 数据库连接
	 * @return 反馈结果
	 * @throws EMPException
	 */
	public abstract CompositeData doExecute(Context context, Connection connection) throws EMPException ;

	/**
	 * 交易成功后处理 XD150520037_信贷系统利率调整修改优化
	 * @param context
	 * @param connection
	 * @throws EMPException
	 */
	public abstract void doSuccess(Context context, Connection connection) throws EMPException ;
	
}
