import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.ecc.emp.component.factory.EMPFlowComponentFactory;
import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.iqp.iqpinterface.impl.IqpRateChangeFlowImpl;
import com.yucheng.cmis.platform.workflow.domain.WfiMsgQueue;

public class TestRateModify {
	static Context context;
	static Connection connection = null;
	static DataSource dataSource = null;
	
	public static void main(String[] args) {

		EMPFlowComponentFactory factory = (EMPFlowComponentFactory) EMPFlowComponentFactory.getComponentFactory("CMISBiz");
	    try {
			context = factory.getContextNamed(factory.getRootContextName());
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		dataSource = (DataSource) context.getService(CMISConstance.ATTR_DATASOURCE);
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
		WfiMsgQueue wfiMsgQueue = new WfiMsgQueue();
		wfiMsgQueue.setPkValue("SQ00991220190323055455");
		wfiMsgQueue.setWfiStatus("997");
		IqpRateChangeFlowImpl iqpRateChangeFlowImpl = new IqpRateChangeFlowImpl();
		try {
			iqpRateChangeFlowImpl.executeAtWFAgree(wfiMsgQueue);
		} catch (EMPException e) {
			e.printStackTrace();
		}
		
	}
}
