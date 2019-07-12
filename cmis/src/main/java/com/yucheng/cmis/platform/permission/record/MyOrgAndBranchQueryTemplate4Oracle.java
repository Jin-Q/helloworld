package com.yucheng.cmis.platform.permission.record;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.TableModel;
import com.ecc.emp.dbmodel.TableModelField;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.dbmodel.service.RecordRestrict;



public class MyOrgAndBranchQueryTemplate4Oracle implements TemplateInterface {

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
	public String checkPermission(TableModel model, String opType, String BCHField, String USRField,
			Context context, KeyedCollection data, Connection con) {
		String st_return = ""; /** 缺省值为忽略*/

		if(!opType.trim().equals(RecordRestrict.QUERY_RESTRICT)){
			st_return = ""; /** 缺省值为忽略*/
			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.ERROR, 0, "当前模板不是查询模板");
			return st_return;
		}

		/** 判断每一个权限归属域中的值是否与当前登录者所属机构相匹配 */
		try {

			if(BCHField != null && !BCHField.trim().equals("")){
				/** 当前登录人所属机构 */
				//String bankCde = (String)context.getDataValue("bankNo");
				String bchCde = (String)context.getDataValue("organNo");
				/** 当前登录人所属机构的法人机构 
			   String cur_arti_org = (String)context.getDataValue("bchLegalCde");
				 */

				st_return = "";

				//根据机构代码规则，获取本机构及下属sql
				//st_return = org_field + " LIKE '" + getPrefixaOfOrgNO(cur_org) + "%'";

				//根据银行代码、机构代码内存中的bchChilds
				//st_return = org_field + " IN (" + getBchChilds(bankCde, bchCde) + ")";
				/**
				 * 注：这里仅仅用于ORACLE数据库
				 */
				st_return = BCHField + " IN (SELECT ORGANNO FROM s_org sb START WITH ORGANNO='" + bchCde + "' CONNECT BY PRIOR ORGANNO = sb.SUPORGANNO)";
				
			} else {
				EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.ERROR, 0, "机构权限控制字段名为空");
			}
		} catch (ObjectNotFoundException e) {
			/** 异常不作处理 */
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			/** 异常不作处理 */
			e.printStackTrace();
		}

		EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "当前权限检查条件为：" + st_return);
		return st_return;		
	}
}
