package com.yucheng.cmis.platform.workflow.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.component.WFIComponent;
import com.yucheng.cmis.pub.CMISComponentFactory;

/**
 * <p>根据用户列表检查是否有工作委托设置</p>
 * 多个用户以;分隔
 * @author liuhw
 *
 */
public class CheckIsEntrustOp extends CMISOperation {

	private final String modelId = "WfHumanstates";
	
	@Override
	public String doExecute(Context context) throws EMPException {

		String applType = null;
		String userList = null;
		Connection connection = null;
		try {
			applType = (String) context.getDataValue(WorkFlowConstance.ATTR_APPLTYPE);
			userList = (String) context.getDataValue("userList");
			context.put("isEntrust", "false");  //默认false无
			if(userList.trim().equals("") && userList.equals("null")) {
				//忽略
			} else {
				String[] userArr = userList.split(";");
				connection = this.getConnection(context);
				WFIComponent wfiComponent = (WFIComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(WorkFlowConstance.WFI_COMPONENTID, context, connection);
				for(int i=0; i<userArr.length; i++) {
					String user = userArr[i];
					user = user.replace("U.", "");
					IndexedCollection icoll = wfiComponent.queryEntrustByUser(user, applType);
					if(icoll.size()>0) {
						context.put("isEntrust", "true");
						break;
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
