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

public class MySelfQueryTemplate implements TemplateInterface {

	/**
	 * <p>检查本人是否有权限查询表中数据</p>
	 * @param model   表模型
	 * @param opType  操作类型 query查询 delete删除 update更新  (@see com.ecc.emp.dbmodel.service.RecordRestrict)
	 * @param context 当前请求的context
	 * @param data    当前待操作的数据（对于query的为空）
	 * @param con     数据库连接
	 * @return 权限过滤SQL  SQL的WHERE之后的部份（不带WHERE）， 
	 *         当检查过程中出现任何问题而无法继续时  返回空字符串 

	 */
	public String checkPermission(TableModel model, String opType, String BCHField, String USRField,
			Context context, KeyedCollection data, Connection con) {
		
		String st_return = ""; /** 缺省值为忽略*/
	 	
		if(!opType.trim().equals(RecordRestrict.QUERY_RESTRICT)){
			st_return = ""; /** 缺省值为忽略*/
			return st_return;
		}
		
		/** （2）判断每一个权限归属域中的值是否与当前登录者相匹配 */
		try {
	
		   String currentUserId = (String)context.getDataValue("currentUserId");
		   st_return = "";

		   /** 组装过滤SQL */
		   if(USRField != null){
              st_return += USRField + "='" + currentUserId + "' " ;
		   }
  
		} catch (ObjectNotFoundException e) {
			/** 异常不作处理 */
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			/** 异常不作处理 */
			e.printStackTrace();
		}
	    if(st_return != null && st_return.trim().startsWith("OR")){
	    	st_return = st_return.substring("OR".length());
	    }

		EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "当前权限检查结果为" + st_return);
		return st_return;

	}

}
