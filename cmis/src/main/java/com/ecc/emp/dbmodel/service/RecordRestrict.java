package com.ecc.emp.dbmodel.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.data.InvalidArgumentException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.data.ObjectNotFoundException;
import com.ecc.emp.dbmodel.EMPRestrictException;
import com.ecc.emp.dbmodel.TableModel;
import com.ecc.emp.dbmodel.TableModelField;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.ecc.emp.service.EMPService;
import com.yucheng.cmis.platform.permission.record.Config;
import com.yucheng.cmis.platform.permission.record.ConfigLoader;
import com.yucheng.cmis.platform.permission.record.TemplateInterface;
 

/**
 * <p>Title:记录级权限控制</p>
 * <p>Description: 记录级权限控制API</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: yucheng</p>
 * 
 * @version 2.0
 */

public class RecordRestrict extends EMPService {
	
	final static public String QUERY_RESTRICT = "query";
	
	final static public String DELETE_RESTRICT = "delete";
	
	final static public String UPDATE_RESTRICT = "update";
	
	public TableModelDAO dao = null;
	
	public void setTableModelDao(TableModelDAO dao) {
		this.dao = dao;
	}
	
	public String judgeQueryRestrict(String modelId, String condition, Context context, Connection con) throws EMPRestrictException{
		return this.judgeRecordRestrict(modelId, QUERY_RESTRICT, condition, context, con);
	}
	
	public String judgeDeleteRestrict(String modelId, Context context, Connection con) throws EMPRestrictException{
		return this.judgeRecordRestrict(modelId, DELETE_RESTRICT, null, context, con);
	}
	
	public String judgeUpdateRestrict(String modelId, Context context, Connection con) throws EMPRestrictException{
		if(context != null){
			try {
				if (context.getDataValue("requestUrl") != null && ((String)context.getDataValue("requestUrl")).indexOf("ViewPage.do") >= 0){
					/** 
					 *  对于查看请求，则忽略权限检查，直接返回
					 *  注：这里要求对于凡查看性质的请求的ACTION需要以ViewPage.do结尾
					 */
					EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "由于请求以ViewPage.do结尾，认为对于查看请求，则忽略权限检查");
					return "";
				}
			} catch (ObjectNotFoundException e) {
				e.printStackTrace();
			} catch (InvalidArgumentException e) {
				e.printStackTrace();
			}
		}
		return this.judgeRecordRestrict(modelId, UPDATE_RESTRICT, null, context, con);
	}
	
	private String judgeRecordRestrict(String modelId, String opType, String condition, Context context, Connection con)
			throws EMPRestrictException {
		String restrictCondition = this.creatRestrictInstance(modelId, opType, context, con);
		if (restrictCondition != null && restrictCondition.length() != 0) {
			if ("1=2".equals(restrictCondition))
				throw new EMPRestrictException("Forbidden opperation!!");
			else if ("1=1".equals(restrictCondition))// do nothing to continue
				;
			else {
				if (condition != null && condition.length() != 0) {
					String condition2 = condition.toUpperCase();
					condition=condition.toUpperCase();
					if (condition2.indexOf("WHERE") > -1) {
						String flag = condition.substring(condition2.indexOf("WHERE"), 
								condition2.indexOf("WHERE") + 5);
						condition = condition.replaceFirst(flag, " ");
					}
					if(condition2.trim().startsWith("ORDER BY")){/// 这里只是权宜之计，应该有专门设置order by的方法 
					  //当CONDITION是ORDER BY时
						condition = " WHERE (" + restrictCondition + ") " + condition;
					} else {
					   condition = " WHERE (" + restrictCondition + ") AND " + condition;
					}
				} else {
					condition = " WHERE (" + restrictCondition+") ";
				}
			}
		}
		System.err.println(condition);
		return condition;
	}
	
	/**
	 * <p>调用记录级权限模板</p>
	 * @param modelId 表模型ID
	 * @param opType  操作类型
	 * @param context 上下文环境
	 * @param con     数据库连接
	 * @return 过滤SQL
	 * @throws EMPRestrictException
	 */
	private String creatRestrictInstance(String modelId, String opType, Context context, Connection con)
	    throws EMPRestrictException {
		
		if ( Config.permissionTemplate == null || Config.permissionTemplate.size() <= 0) {
			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.ERROR, 0, "记录级权限模板没有加载");
			ConfigLoader.loadPermissionConfig();
			//throw new EMPRestrictException("记录级权限模板没有加载");
		}
		
		String st_return = "";
		
		TableModel model = dao.modelLoader.getTableModel(modelId);
		if (model == null)
			throw new EMPRestrictException();

		KeyedCollection modelKcoll = null;
		
		try {
			modelKcoll = (KeyedCollection) context.getDataElement(modelId);
		} catch (Exception e) {
		}
		
		if(opType != null && !opType.trim().equals(QUERY_RESTRICT)){
			/**  在不是查询操作时，用主键取当前等待操作的一条记录 */
			// 尝试从context中获取pk值
			HashMap pk_values = new HashMap();	
			Iterator it = model.getModelFields().values().iterator();
			while (it.hasNext()) {
				TableModelField field = (TableModelField) it.next();
				if (!field.isPK())
					continue;
				String pk_value = null;
				try {
					if (modelKcoll == null)
						pk_value = (String) context.getDataValue(field.getId());
					else
						pk_value = (String) modelKcoll.getDataValue(field.getId());
				} catch (Exception e) {
				}
				if (pk_value == null)
					throw new EMPRestrictException();
				else
					pk_values.put(field.getId(), pk_value);
			}
			try {
				modelKcoll = dao.queryDetail(modelId, pk_values, con);
			} catch (EMPJDBCException e) {
				throw new EMPRestrictException(e);
			}
		}
			
		/** 根据表模型ID+操作类型ID+当前岗位   来定位使用哪一个记录级权限模板 */

		String _tempKey = "";
		String dutyList = "";	
		try {
			dutyList = (String)context.getDataValue("dutyList");
		} catch (ObjectNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(dutyList != null && !dutyList.trim().equals("")){
			String[] sta_duty = dutyList.split(",");
			for(int n=0; n<sta_duty.length; n++){
				String key = modelId + opType + sta_duty[n];
				if(Config.permissionTemplate.containsKey(key)){
					/**
					 * @TODO 这里需要按配置文件中的优先级来
					 */
					_tempKey = key;
					break;
				}
			}
		}
		
		if(_tempKey == null || _tempKey.trim().equals("")){
			///缺省使用  表模型ID+操作类型ID 来定位模板
			_tempKey = modelId + opType;
		}
		
		String template = Config.permissionTemplate.get(_tempKey);
		String bchField = Config.permissionBCHField.get(modelId);
		String usrField = Config.permissionUSRField.get(modelId);
		
		EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "KEY="+ _tempKey );
 
		if(template != null && !template.trim().equals("")){
			try {
				TemplateInterface permissionTemp = (TemplateInterface) Class.forName(template).newInstance();
				st_return = permissionTemp.checkPermission(model, opType, bchField, usrField, context, modelKcoll, con);				
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			EMPLog.log(EMPConstance.EMP_JDBC, EMPLog.DEBUG, 0, "当前配置记录级权限模板为空，不执行权限控制!");
		}
				
		return st_return;
	}
	/**
	private String __creatRestrictInstance(String modelId, String opType, Context context, Connection con)
			throws EMPRestrictException {
		
		WorkingMemory workingmemory = null;
		try {
			workingmemory = RuleBase.getInstance().newWorkingMemory();
			workingmemory.setTargetRuleSetName(modelId);
			workingmemory.assertObject(context);
		} catch (Exception e) {
			throw new EMPRestrictException("Record Restrict of tableModel[" + modelId + "] is not defined!");
		}
		
		HashMap fieldMap = new HashMap();
		
		List dbFields = new ArrayList();
		dbFields = workingmemory.getDBColumns(opType);
		if (dbFields != null && !dbFields.isEmpty()) {// 查询数据库表的权限字段
			TableModel model = dao.modelLoader.getTableModel(modelId);
			if (model == null)
				throw new EMPRestrictException();

			// 尝试从context中获取pk值
			HashMap pk_values = new HashMap();
			KeyedCollection modelKcoll = null;
			try {
				modelKcoll = (KeyedCollection) context.getDataElement(modelId);
			} catch (Exception e) {
			}
			Iterator it = model.getModelFields().values().iterator();
			while (it.hasNext()) {
				TableModelField field = (TableModelField) it.next();
				if (!field.isPK())
					continue;
				String pk_value = null;
				try {
					if (modelKcoll == null)
						pk_value = (String) context.getDataValue(field.getId());
					else
						pk_value = (String) modelKcoll.getDataValue(field.getId());
				} catch (Exception e) {
				}
				if (pk_value == null)
					throw new EMPRestrictException();
				else
					pk_values.put(field.getId(), pk_value);
			}
			try {
				modelKcoll = dao.queryDetail(modelId, pk_values, con);
			} catch (EMPJDBCException e) {
				throw new EMPRestrictException(e);
			}
			for (int i = 0; i < dbFields.size(); i++) {
				String fieldId = (String) dbFields.get(i);
				try {
					Object obj_value = modelKcoll.getDataValue(fieldId);
					if (obj_value != null)
						fieldMap.put(fieldId, obj_value);
				} catch (Exception e) {
				}
			}
		}
		
		workingmemory.assertObject(fieldMap);
		
		String retStr = "1=2";
		fieldMap.remove("_result_");
		
		workingmemory.setTargetRuleName(opType);
		try{
			workingmemory.fireAllRules();
		}
		catch(Exception e){
			throw new EMPRestrictException();
		}
		
		if (fieldMap.get("_result_") != null && !fieldMap.get("_result_").toString().trim().equals(""))
			retStr = (String) fieldMap.get("_result_");
		
		return retStr;
	}
	*/
}
