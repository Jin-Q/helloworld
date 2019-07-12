package com.yucheng.cmis.platform.permission.record;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.TableModel;

/**
 * <p>数据记录级权限控制模板接口</p>
 * @author liuming 
 */
public interface TemplateInterface {

	/**
	 * <p>检查数据记录</p>
	 * @param model   表模型配置
	 * @param opType  操作类型 query查询 (@see com.ecc.emp.dbmodel.service.RecordRestrict)
	 * @param BCHField 业务数据表中用于权限控制的字段————机构
	 * @param USRField 业务数据表中用于权限控制的字段————用户
	 * @param context 当前请求的context
	 * @param data    当前待操作的数据（对于query的为空）
	 * @param con     数据库连接
	 * @return 权限过滤SQL
	 */
	public String checkPermission(TableModel model, String opType, String BCHField, String USRField, Context context, KeyedCollection data, Connection con);	 
}
