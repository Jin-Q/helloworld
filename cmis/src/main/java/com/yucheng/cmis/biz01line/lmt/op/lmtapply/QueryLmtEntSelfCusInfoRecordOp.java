package com.yucheng.cmis.biz01line.lmt.op.lmtapply;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.WorkFlowConstance;
import com.yucheng.cmis.platform.workflow.msi.WorkflowServiceInterface;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
/*单一法人企业客户主动授信改造       modefied by zhaoxp  2015-02-08*/

public class QueryLmtEntSelfCusInfoRecordOp extends CMISOperation {

	private final String modelId = "LmtEntSelfCusInfo";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
			connection = this.getConnection(context);

			String cus_id = null;
			try {
				cus_id = (String)context.getDataValue("cus_id");
			} catch (Exception e) {}

			TableModelDAO dao = this.getTableModelDAO(context);

			KeyedCollection kColl = dao.queryDetail(modelId, cus_id, connection);
			if(kColl.containsKey("cus_id")&&kColl.getDataValue("cus_id")!=null&&!"".equals(kColl.getDataValue("cus_id"))){
				context.addDataField("flag", "success");
				context.addDataField("msg", "success");
				
			}else{
				context.addDataField("flag","failed");
				context.addDataField("msg", "没有找到客户码对应的信息");
			}
			if (connection != null)
				this.releaseConnection(context, connection);
		return "0";
	}
}
