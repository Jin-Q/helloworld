package com.yucheng.cmis.platform.permission.record;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.TableModel;
import com.ecc.emp.dbmodel.TableModelField;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.dbmodel.service.RecordRestrict;

public class MyOrgQueryTemplate implements TemplateInterface {
	/**
	 * <p>检查本机我是否有权限查询表中数据</p>
	 * @param model 表模型
	 * @param opType  操作类型 query查询  (@see com.ecc.emp.dbmodel.service.RecordRestrict)
	 * @param context 当前请求的context
	 * @param data    当前待操作的数据（对于query的为空）
	 * @param con     数据库连接
	 * @return 权限过滤SQL  SQL的WHERE之后的部份（不带WHERE）， 
	 *         当检查过程中出现任何问题而无法继续时  返回空字符串 
	 */
	public String checkPermission(TableModel model, String opType, String BCHField, String USRField,
			Context context, KeyedCollection data, Connection con) {
		String bchCde = "";
		try {
			bchCde = (String)context.getDataValue("organNo");
		} catch (ObjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String st_return =  BCHField +"='" + bchCde + "'";
		return st_return;
	}

}
