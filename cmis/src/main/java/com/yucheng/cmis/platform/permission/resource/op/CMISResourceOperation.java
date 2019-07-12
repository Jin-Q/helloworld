package com.yucheng.cmis.platform.permission.resource.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.log.LogLoader;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.permission.PermissionContents;
import com.yucheng.cmis.platform.permission.resource.component.CMISResourceComponent;
import com.yucheng.cmis.platform.permission.resource.component.CMISRolePermissionComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.util.CMISJSONUtil;

public class CMISResourceOperation extends CMISOperation {

	private String op;
	//所要操作的表模型
	private final String modelId = "s_resource";
	private final String pk_name = "resourceid";
	
	@Override
	public String doExecute(Context context) throws EMPException {
		
		CMISResourceComponent service = (CMISResourceComponent)CMISComponentFactory.getComponentFactoryInstance()
								.getComponentInstance(PermissionContents.RESOURCE_COMPONENT_ID, context, null);
		
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			
			if("resourceTree".equals(op)){
				String str = service.buildResourceJSONTree(connection);
				try {
					context.addDataField(CMISConstance.ATTR_RET_JSONDATANAME, str);
				} catch (DuplicatedDataNameException e) {
					context.setDataValue(CMISConstance.ATTR_RET_JSONDATANAME, str);
				}
			}else if("resourceActTree".equals(op)){
				String str = service.buildResourceActJSONTree(connection);
				try {
					context.addDataField(CMISConstance.ATTR_RET_JSONDATANAME, str);
				} catch (DuplicatedDataNameException e) {
					context.setDataValue(CMISConstance.ATTR_RET_JSONDATANAME, str);
				}
			}
			else if("resourceDetails".equals(op)){
				//获得查询需要的主键信息
				String pk_value = null;
				try {
					pk_value = (String)context.getDataValue("resourceid");
				} catch (Exception e) {}
				if(pk_value == null || pk_value.length() == 0)
					throw new EMPJDBCException("The value of pk[resourceid] cannot be null!");
				
				//查询列表信息
				TableModelDAO dao = this.getTableModelDAO(context);
				KeyedCollection kColl = dao.queryDetail("s_resource", pk_value, connection);
				this.putDataElement2Context(kColl, context);
			}else if("editResourceSubmit".equals(op)){
				//更新一条指定的记录				
				KeyedCollection kColl = null;
				try {
					kColl = (KeyedCollection)context.getDataElement("s_resource");
				} catch (Exception e) {}
				if(kColl == null || kColl.size() == 0)
					throw new EMPJDBCException("The values to update[s_resource] cannot be empty!");
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
				
				//加载资源列表
				LogLoader.getResource(connection);
				
			}else if("addResourceSubmit".equals(op)){
				//插入资源记录
				KeyedCollection kColl = null;
				try {
					kColl = (KeyedCollection)context.getDataElement(modelId);
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
				//新增资源的缺省操作visit
				KeyedCollection kColl2=new KeyedCollection("s_resourceaction");
				kColl2.addDataField("resourceid",kColl.getDataValue("resourceid"));
				kColl2.addDataField("actid","visit");
				kColl2.addDataField("descr","菜单访问");
				kColl2.addDataField("flag","1");
				dao.insert(kColl2, connection);
				kColl2=new KeyedCollection("s_resourceaction");
				kColl2.addDataField("resourceid",kColl.getDataValue("resourceid"));
				kColl2.addDataField("actid","add");
				kColl2.addDataField("descr","新增");
				kColl2.addDataField("flag","1");
				dao.insert(kColl2, connection);
				kColl2=new KeyedCollection("s_resourceaction");
				kColl2.addDataField("resourceid",kColl.getDataValue("resourceid"));
				kColl2.addDataField("actid","update");
				kColl2.addDataField("descr","修改");
				kColl2.addDataField("flag","1");
				dao.insert(kColl2, connection);
				kColl2=new KeyedCollection("s_resourceaction");
				kColl2.addDataField("resourceid",kColl.getDataValue("resourceid"));
				kColl2.addDataField("actid","remove");
				kColl2.addDataField("descr","删除");
				kColl2.addDataField("flag","1");
				dao.insert(kColl2, connection);
				kColl2=new KeyedCollection("s_resourceaction");
				kColl2.addDataField("resourceid",kColl.getDataValue("resourceid"));
				kColl2.addDataField("actid","view");
				kColl2.addDataField("descr","查看");
				kColl2.addDataField("flag","1");
				dao.insert(kColl2, connection);
				
				//加载资源列表
				LogLoader.getResource(connection);
				
			}else if("delResource".equals(op)){//删除资源
				//获得删除需要的主键信息
				String pk_value = null;
				try {
					pk_value = (String)context.getDataValue(pk_name);
				} catch (Exception e) {}
				if(pk_value == null || pk_value.length() == 0)
					throw new EMPJDBCException("The value of pk["+pk_name+"] cannot be null!");
				service.removeResource(pk_value, connection);			
			}
			
			context.addDataField("flag", "0");
			
		} catch (EMPException e) {
			e.printStackTrace();
			throw e;
		} catch(Exception e1){
			e1.printStackTrace();
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
