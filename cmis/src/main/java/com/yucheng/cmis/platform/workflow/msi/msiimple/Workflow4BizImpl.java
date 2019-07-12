package com.yucheng.cmis.platform.workflow.msi.msiimple;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.echain.log.WfLog;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.platform.workflow.domain.WfiBizVarRecordVO;
import com.yucheng.cmis.platform.workflow.exception.WFIException;
import com.yucheng.cmis.platform.workflow.msi.Workflow4BIZIface;
import com.yucheng.cmis.pub.CMISModualService;
import com.yucheng.cmis.pub.dao.SqlClient;


public class Workflow4BizImpl extends CMISModualService implements Workflow4BIZIface {

	public String getModifiedBizVarWithString(String instanceId, String varKey, Connection connection) throws WFIException {
		String retVal = null;
		try {
			String sqlId = "queryWfiBizVarByKey";
			KeyedCollection param = new KeyedCollection();		
			param.addDataField("instanceId", instanceId);
			param.addDataField("var_key", varKey);
			WfiBizVarRecordVO varVo = (WfiBizVarRecordVO) SqlClient.queryFirst(sqlId, param, null, connection);
			if(varVo==null || varVo.getInstanceid()==null) {
				sqlId = "queryWfiBizVarByKeyHis";
				varVo = (WfiBizVarRecordVO) SqlClient.queryFirst(sqlId, param, null, connection);
			}
			if(varVo != null) {
				String type = varVo.getVarType();
				if(type != null){
					if(!type.trim().toLowerCase().equals("string")){
						throw new WFIException("业务要素的值类型不为String");
					}
				}
				retVal = varVo.getVarValue();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WFIException(e);
		}
		return retVal;
	}

	public Double getModifiedBizVarWithDouble(String instanceId, String varKey, Connection connection) throws WFIException {
		Double retVal = null;
		try {
			String sqlId = "queryWfiBizVarByKey";
			KeyedCollection param = new KeyedCollection();		
			param.addDataField("instanceId", instanceId);
			param.addDataField("var_key", varKey);
			WfiBizVarRecordVO varVo = (WfiBizVarRecordVO) SqlClient.queryFirst(sqlId, param, null, connection);
			if(varVo==null || varVo.getInstanceid()==null) {
				sqlId = "queryWfiBizVarByKeyHis";
				varVo = (WfiBizVarRecordVO) SqlClient.queryFirst(sqlId, param, null, connection);
			}
			if(varVo != null) {
				String type = varVo.getVarType();
				if(type != null){
					if(!type.trim().toLowerCase().equals("double")){
						throw new WFIException("业务要素的值类型不为double");
					}
				}
				if(varVo.getVarValue() != null) {
					retVal = Double.valueOf(varVo.getVarValue());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WFIException(e);
		}
		return retVal;
		
	}

	public Integer getModifiedBizVarWithInteger(String instanceId, String varKey, Connection connection) throws WFIException {
		Integer retVal = null;
		try {
			String sqlId = "queryWfiBizVarByKey";
			KeyedCollection param = new KeyedCollection();		
			param.addDataField("instanceId", instanceId);
			param.addDataField("var_key", varKey);
			WfiBizVarRecordVO varVo = (WfiBizVarRecordVO) SqlClient.queryFirst(sqlId, param, null, connection);
			if(varVo==null || varVo.getInstanceid()==null) {
				sqlId = "queryWfiBizVarByKeyHis";
				varVo = (WfiBizVarRecordVO) SqlClient.queryFirst(sqlId, param, null, connection);
			}
			if(varVo != null) {
				String type = varVo.getVarType();
				if(type != null){
					if(!type.trim().toLowerCase().equals("int")){
						throw new WFIException("业务要素的值类型不为int");
					}
				}
				if(varVo.getVarValue() != null) {
					retVal = Integer.valueOf(varVo.getVarValue());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WFIException(e);
		}
		return retVal;
	}

	public Map getAllModifiedBizVar(String instanceId, Connection connection) throws WFIException {
		Map retMap = new HashMap();
		String sqlId = "selectWfiVarKey";
		try {
			Collection col = SqlClient.queryList(sqlId, instanceId, connection);
			if(col==null || col.size()<1) {
				String sqlIdHis = "selectWfiVarKeyHis";
				col = SqlClient.queryList(sqlIdHis, instanceId, connection);
			}
			if(col!=null && col.size()>0) {
				for(Object obj : col) {
					KeyedCollection kcoll = (KeyedCollection) obj;
					String varKey = (String) kcoll.getDataValue("var_key");
					String varType = (String) kcoll.getDataValue("var_type");
					Object varValue = null;
					if("string".equals(varType)) {
						varValue = this.getModifiedBizVarWithString(instanceId, varKey, connection);
					} else if("int".equals(varType)) {
						varValue = this.getModifiedBizVarWithInteger(instanceId, varKey, connection);
					} else if("double".equals(varType)) {
						varValue = this.getModifiedBizVarWithDouble(instanceId, varKey, connection);
					}
					retMap.put(varKey, varValue);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WFIException(e);
		}
		return retMap;
	}
	
	public Map getAllModifiedBizVarBySerno(String serno, String modelId, Connection connection) throws WFIException {
		Map retMap = new HashMap();
		KeyedCollection WJInfo = null;
		try {
			String sqlId = "getMainWfiJoinByBiz";
			KeyedCollection condi = new KeyedCollection();
			condi.put("table_name", modelId);
			condi.put("pk_value", serno);
			WJInfo = (KeyedCollection)SqlClient.queryFirst(sqlId, condi, null, connection);
		} catch (Exception e) {
			WfLog.log(WfLog.ERROR, "根据业务表模型ID["+modelId+"]、业务流水号（主键值）["+serno+"]查询信贷流程中间表信息出错。异常信息："+e.getMessage(), e);
			throw new WFIException(e);
		}
		if(WJInfo!=null&&WJInfo.size()>0){
			try {
				String instanceId = (String) WJInfo.getDataValue("instanceid");
				String sqlId = "selectWfiVarKey";
				Collection col = SqlClient.queryList(sqlId, instanceId, connection);
				if(col==null || col.size()<1) {
					String sqlIdHis = "selectWfiVarKeyHis";
					col = SqlClient.queryList(sqlIdHis, instanceId, connection);
				}
				if(col!=null && col.size()>0) {
					for(Object obj : col) {
						KeyedCollection kcoll = (KeyedCollection) obj;
						String varKey = (String) kcoll.getDataValue("var_key");
						String varType = (String) kcoll.getDataValue("var_type");
						Object varValue = null;
						if("string".equals(varType)) {
							varValue = this.getModifiedBizVarWithString(instanceId, varKey, connection);
						} else if("int".equals(varType)) {
							varValue = this.getModifiedBizVarWithInteger(instanceId, varKey, connection);
						} else if("double".equals(varType)) {
							varValue = this.getModifiedBizVarWithDouble(instanceId, varKey, connection);
						}
						retMap.put(varKey, varValue);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new WFIException(e);
			}
		}
		return retMap;
	}

}
