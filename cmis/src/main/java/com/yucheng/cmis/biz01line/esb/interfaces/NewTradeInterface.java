package com.yucheng.cmis.biz01line.esb.interfaces;

import java.sql.Connection;

import com.dc.eai.data.CompositeData;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;

public interface NewTradeInterface {

	/**
	 * 业务逻辑处理接口
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
