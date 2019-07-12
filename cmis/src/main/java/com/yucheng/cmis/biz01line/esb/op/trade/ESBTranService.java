package com.yucheng.cmis.biz01line.esb.op.trade;

import java.sql.Connection;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.biz01line.esb.interfaces.NewTradeInterface;
import com.yucheng.cmis.biz01line.esb.interfaces.TradeInterface;
/**
 * 解析出结构体中交易码、交易场景，并且转发给不同的交易
 * @author Pansq
 */
public abstract class ESBTranService implements NewTradeInterface {
	/**
	 * 业务逻辑处理入口
	 * @param CD 请求报文结构体
	 * @param connection 数据库连接
	 * @return 处理结果
	 * <KColl>
	 * <field name='ret_code' value='' />
	 * <field name='ret_msg' value='' />
	 * </KColl>
	 */
	public abstract KeyedCollection doExecute(KeyedCollection kColl, Connection connection) throws Exception;
	
}
