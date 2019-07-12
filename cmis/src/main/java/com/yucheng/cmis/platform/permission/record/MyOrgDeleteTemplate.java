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

public class MyOrgDeleteTemplate implements TemplateInterface {
	/**
	 * <p>检查本机构是否有权限删除表中数据</p>
	 * @param modelId 表模型ID
	 * @param opType  操作类型 delete查询  (@see com.ecc.emp.dbmodel.service.RecordRestrict)
	 * @param context 当前请求的context
	 * @param data    当前待操作的数据（对于query的为空）
	 * @param con     数据库连接
	 * @return 权限过滤SQL 1=1 表示通过权限检查  1=2 表示没有通过权限检查  空字符串 表示忽略权限检查
	 */	
	public String checkPermission(TableModel model, String opType, String BCHField, String USRField,
			Context context, KeyedCollection data, Connection con) {
		String st_return = ""; /** 缺省值为忽略*/
	 	
		if(!opType.trim().equals(RecordRestrict.DELETE_RESTRICT)){
			st_return = ""; /** 缺省值为忽略*/
			return st_return;
		}
		
		if(data != null){

			   st_return = "";

			   try{  
				  String permission = (String)data.getDataValue(BCHField);
				  
				   /** 当前登录人所属机构 */
				   String cur_org = (String)context.getDataValue("orgid");
				   /** 当前登录人所属机构的法人机构 
				   String cur_arti_org = (String)context.getDataValue("ARTI_ORGANNO");
				   */					  
				  if(cur_org != null && cur_org.trim().equals(permission.trim())){
						
						EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "当前机构拥有删除的权限!");									
						st_return = "1=1"; 
			      } else {
			    	  st_return = "1=2"; 
			    	    EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "当前机构没有删除的权限!");
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
