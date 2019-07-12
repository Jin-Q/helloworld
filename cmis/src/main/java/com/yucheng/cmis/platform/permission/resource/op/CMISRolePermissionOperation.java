package com.yucheng.cmis.platform.permission.resource.op;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DuplicatedDataNameException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.permission.PermissionContents;
import com.yucheng.cmis.platform.permission.resource.component.CMISResourceComponent;
import com.yucheng.cmis.platform.permission.resource.component.CMISRolePermissionComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class CMISRolePermissionOperation extends CMISOperation {
	
	private String op;

	@Override
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			
			if("getRolePermission".equals(op)){
				String roleNo = (String)context.getDataValue("roleNo");
				String resourceId = null;
				try {
					resourceId = (String)context.getDataValue("resourceId");
				} catch (Exception e1) {}
				CMISResourceComponent service = (CMISResourceComponent)CMISComponentFactory.getComponentFactoryInstance()
							.getComponentInstance(PermissionContents.RESOURCE_COMPONENT_ID, context, null);
				String str = service.getRolePermissionJSONTree(roleNo, resourceId, connection);;
				try {
					context.addDataField(CMISConstance.ATTR_RET_JSONDATANAME, str);
				} catch (DuplicatedDataNameException e) {
					context.setDataValue(CMISConstance.ATTR_RET_JSONDATANAME, str);
				}
			}else if("updateRolePermission".equals(op)){
				String roleNo = (String)context.getDataValue("roleNo");
				String resourceId = (String)context.getDataValue("resourceId");
				IndexedCollection actList = null;
				try {
					actList = (IndexedCollection)context.getDataElement("actList");
				} catch (Exception e) {}
				
				PreparedStatement state = null;
				String sql = null;
				try{
					sql = "delete from s_roleright where roleno=? and resourceid=?";
					state = connection.prepareStatement(sql);
					state.setObject(1, roleNo);
					state.setObject(2, resourceId);
					state.executeUpdate();
					state.close();
					
					if(actList != null && actList.size() != 0){
						sql = "insert into s_roleright(roleno,resourceid,actid,state) values(?,?,?,?)";
						state = connection.prepareStatement(sql);
						for(int i=0;i<actList.size();i++){
							//state = connection.prepareStatement(sql);
							KeyedCollection kColl = (KeyedCollection)actList.get(i);
							String actId = (String)kColl.getDataValue("actId");
							
							state.setObject(1, roleNo);
							state.setObject(2, resourceId);
							state.setObject(3, actId);
							state.setInt(4, 1);
							
							state.addBatch();
						}
						state.executeBatch();
					}
					CMISResourceComponent service = (CMISResourceComponent)CMISComponentFactory.getComponentFactoryInstance()
									.getComponentInstance(PermissionContents.RESOURCE_COMPONENT_ID, context, null);
					String str = service.getRolePermissionJSONTree(roleNo, resourceId, connection);;
					try {
						context.addDataField(CMISConstance.ATTR_RET_JSONDATANAME, str);
					} catch (DuplicatedDataNameException e) {
						context.setDataValue(CMISConstance.ATTR_RET_JSONDATANAME, str);
					}
				}catch(Exception e){
					throw e;
				}finally{
					if(state != null){
						try {
							state.close();
						} catch (Exception e) {}
					}
				}
			}else if("generateUserFile".equals(this.op)){
				CMISRolePermissionComponent service = (CMISRolePermissionComponent)CMISComponentFactory.getComponentFactoryInstance()
									.getComponentInstance(PermissionContents.ROLE_PERMISSION_COMPONENT_ID, context, connection);
				String actorNo = (String)context.getDataValue("actorno");
				String[] actorno_array = actorNo.split(",");   //将多个用户通过截取为字符串数值
				for (int i = 0; i < actorno_array.length; i++) {
					String actor_str = actorno_array[i];
					if(!"".equals(actor_str)){
						service.generatePermissionFile(actor_str, connection);
					}
				}
			}
		} catch (EMPException e) {
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
