package com.yucheng.cmis.platform.workflow.demo.op;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.demo.component.WfiDemoComponent;
import com.yucheng.cmis.platform.workflow.domain.WfiDemo;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;

/**
 * 新增记录
 * @author liuhw
 *
 */
public class AddWfiDemoOp extends CMISOperation {

	private final String modelId = "WfiDemo";
	
	@Override
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try {
			KeyedCollection kcoll = (KeyedCollection) context.getDataElement(modelId);
			ComponentHelper comHelper = new ComponentHelper();
			WfiDemo wfiDemo = new WfiDemo();
			wfiDemo = (WfiDemo) comHelper.kcolTOdomain(wfiDemo, kcoll);
			connection = this.getConnection(context);
			WfiDemoComponent comp = (WfiDemoComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("wfiDemoComponent", context, connection);
			comp.addWfiDemo(wfiDemo);
		} catch (Exception e) {
//			e.printStackTrace();
//			throw new EMPException(e);
		} finally {
			if(connection != null)
				this.releaseConnection(context, connection);
		}
		context.put("msg", "1");
		return null;
	}

}
