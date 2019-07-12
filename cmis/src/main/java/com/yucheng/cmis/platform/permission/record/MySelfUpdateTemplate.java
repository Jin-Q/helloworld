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

public class MySelfUpdateTemplate implements TemplateInterface {

	/**
	 * <p>检查本人是否有权限更新表中数据</p>
	 * <p>检查当前待操作数据中用于描述<b>权限归属用户</b>的字段的值，是否与当前登录人相匹配，匹配则检查通过</p>
	 * @param model   表模型
	 * @param opType  操作类型 update查询  (@see com.ecc.emp.dbmodel.service.RecordRestrict)
	 * @param context 当前请求的context
	 * @param data    当前待操作的数据（对于query的为空）
	 * @param con     数据库连接
	 * @return 权限过滤SQL：1=1 表示通过权限检查  1=2 表示没有通过权限检查  空字符串 表示忽略权限检查
	 */	
	public String checkPermission(TableModel model, String opType, String BCHField, String USRField,
			Context context, KeyedCollection data, Connection con) {

		String st_return = ""; /** 缺省值为忽略*/
		if(!opType.trim().equals(RecordRestrict.UPDATE_RESTRICT)){
			st_return = ""; /** 缺省值为忽略*/
			return st_return;
		} 	
		
		if(data != null){
			
 
			/**  判断每一个权限归属域中的值是否与当前登录者相匹配 */
			try {
 
			   String currentUserId = (String)context.getDataValue("currentUserId");
			   st_return = "1=2";
			   
			   try{  
				  String permission = (String)data.getDataValue(USRField);
				  if(currentUserId != null && currentUserId.trim().equals(permission.trim())){
						
						EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "当前用户拥有修改的权限!");									
						st_return = "1=1"; 
			      } else {

			    	    EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "当前用户没有修改的权限!");
			      }	     
			   } catch (ObjectNotFoundException e) {
				  /** 异常不作处理 */
				  e.printStackTrace();
			   } catch (InvalidArgumentException e) {
				  /** 异常不作处理 */
			      e.printStackTrace();
			   }

			} catch (ObjectNotFoundException e) {
				/** 异常不作处理 */
				e.printStackTrace();
			} catch (InvalidArgumentException e) {
				/** 异常不作处理 */
				e.printStackTrace();
			}
		} else {
			
			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "当前数据为空，忽略权限控制!");
			/** 当前数据为空，返回空忽略权限控制 */
			st_return = "";
		}
		EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "当前权限检查结果为" + st_return);
		return st_return;
	}

}
