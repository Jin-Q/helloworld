package com.yucheng.cmis.platform.permission.resource.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.CMISJSONUtil;
import com.yucheng.cmis.util.TableModelUtil;
//资源操作的相关处理类
public class CMISResourceActOperation extends CMISOperation {
	private String op;
	//所要操作的表模型
	private final String modelId = "s_resourceaction";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			if("resourceActList".equals(op)){//显示指定resource的Act列表
				//获得查询的过滤数据
				KeyedCollection queryData = null;
				try {
					queryData = (KeyedCollection)context.getDataElement(this.modelId);
				} catch (Exception e) {}
				String resourceid = (String)context.getDataValue("s_resourceaction.resourceid");
				try{
					context.setDataValue("resourceid", resourceid);
				}
				catch (Exception e) {
					context.addDataField("resourceid", resourceid);
				}
				//获得查询条件，交集、精确查询，忽略空值
				String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
				TableModelDAO dao = this.getTableModelDAO(context);
				List list = new ArrayList();
				list.add("resourceid");
				list.add("actid");
				list.add("descr");							
				IndexedCollection iColl = dao.queryList(modelId,list,conditionStr,connection);
				iColl.setName(iColl.getName()+"List");
				this.putDataElement2Context(iColl, context);
			}else if("getEditActPage".equals(op)){//修改Action
				//获得查询需要的主键信息
				Map pk_Map = new HashMap();
				String pk_value;
				try {
					pk_value = (String)context.getDataValue("resourceid");
					pk_Map.put("resourceid", pk_value);
				} catch (Exception e) {}
				try {
					pk_value = (String)context.getDataValue("actid");
					pk_Map.put("actid", pk_value);
				} catch (Exception e) {}
				if(pk_Map == null || pk_Map.size() == 0)
					throw new EMPJDBCException("The value of pk[resourceid、actid] cannot be null!");
				
				//查询列表信息
				TableModelDAO dao = this.getTableModelDAO(context);
				KeyedCollection kColl = dao.queryDetail(modelId, pk_Map, connection);
				this.putDataElement2Context(kColl, context);
			}else if("editActionSubmit".equals(op)){//修改资源操作提交
				//更新一条指定的记录				
				KeyedCollection kColl = null;
				try {
					kColl = (KeyedCollection)context.getDataElement(modelId);
					String resourceid = (String)kColl.getDataValue("resourceid");
					try{
						context.setDataValue("resourceid", resourceid);
					}
					catch (Exception e) {
						context.addDataField("resourceid", resourceid);
					}
				} catch (Exception e) {}				
				if(kColl == null || kColl.size() == 0)
					throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
				String s;
				for(int i=0;i<kColl.size();i++){
					s=(String)((DataField)kColl.getDataElement(i)).getValue();
					if(CMISJSONUtil.isInvalidate(s)){//无效的json字符串
						throw new EMPException("所输入的信息中包含了无效的json字符串");
					}
				}
				//修改指定记录
				TableModelDAO dao = this.getTableModelDAO(context);
				int count=dao.update(kColl, connection);
				if(count!=1){
					throw new EMPException("修改数据失败！操作影响了"+count+"条记录");
				}
			}else if("addActionSubmit".equals(op)){//新增资源操作提交
				KeyedCollection kColl = null;
				try {
					kColl = (KeyedCollection)context.getDataElement(modelId);
					String resourceid = (String)kColl.getDataValue("resourceid");
					try{
						context.setDataValue("resourceid", resourceid);
					}
					catch (Exception e) {
						context.addDataField("resourceid", resourceid);
					}
				} catch (Exception e) {}
				if(kColl == null || kColl.size() == 0)
					throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
				String s;
				for(int i=0;i<kColl.size();i++){
					s=(String)((DataField)kColl.getDataElement(i)).getValue();
					if(CMISJSONUtil.isInvalidate(s)){//无效的json字符串
						throw new EMPException("所输入的信息中包含了无效的json字符串");
					}
				}
				//新增一条记录
				TableModelDAO dao = this.getTableModelDAO(context);
				dao.insert(kColl, connection);
			}else if("delAction".equals(op)){//删除资源操作
				//删除一条特定的记录				
				Map pk_Map = new HashMap();
				String pk_value;
				try {
					pk_value = (String)context.getDataValue("resourceid");
					pk_Map.put("resourceid", pk_value);
				} catch (Exception e) {}
				try {
					pk_value = (String)context.getDataValue("actid");
					pk_Map.put("actid", pk_value);
				} catch (Exception e) {}
				if(pk_Map == null || pk_Map.size() == 0)
					throw new EMPJDBCException("The value of pk[resourceid、actid] cannot be null!");

				//删除指定记录
				TableModelDAO dao = this.getTableModelDAO(context);
				int count=dao.deleteAllByPks(modelId, pk_Map, connection);
				if(count!=1){
					throw new EMPException("删除数据失败！操作影响了"+count+"条记录");
				}
			}
			context.addDataField("flag", "0");
		}catch (EMPException e) {
			throw e;
		} catch(Exception e1){
			throw new EMPException(e1);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
}
