package com.yucheng.cmis.biz01line.cus.cushand.op;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusHandoverAppComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusHandoverLstComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusHandoverApp;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusHandoverLst;
import com.yucheng.cmis.biz01line.cus.cushand.component.CusHandoverCfgComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISDomain;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.CusPubConstant;
import com.yucheng.cmis.pub.PUBConstant;
/**
 * 客户移交 中点击 接收 业务处理对应 op
 * 
 * @Version bsbcmis
 * @author wuming 2012-3-26 
 * Description:
 */
public class CushandoverReceiveOp extends CMISOperation {
	private final String modelId = "CusHandoverApp";
	private final String serno_name = "serno";
	private final String status_name = "status";

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String strReturnMessage = CMISMessage.DEFEAT;
		TableModelDAO dao = this.getTableModelDAO(context);
		try {
			connection = this.getConnection(context);

			CusHandoverAppComponent cusHandoverAppComponent = (CusHandoverAppComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(
							PUBConstant.CUSHANDOVERAPP, context, connection);

			String serno_value = null;
			KeyedCollection kColl = null;

			try {
				serno_value = (String) context.getDataValue(serno_name);
			} catch (Exception e) {
			}
			if (serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk[" + serno_name
						+ "] cannot be null!");

			String status_value = null;
			try {
				status_value = (String) context.getDataValue(status_name);
			} catch (Exception e) {
			}
			if (status_value == null || status_value.length() == 0)
				throw new EMPJDBCException("The value of [" + status_name
						+ "] cannot be null!");
            /**
             * 'STD_ZB_HAND_STATUS' : {'00':'登记', '10':'提交', '20':'同意','22':'否决', '30':'成功', '40':'托管收回', '50':'拒绝'}
             */
			
			if (CusPubConstant.CUS_CREDIT_REGISTER.equals(status_value)) {
				boolean hasList = cusHandoverAppComponent
						.hasCusTrusteeList(serno_value);
				if (hasList) {
					status_value = CusPubConstant.CUS_CREDIT_SUBMIT;
					// 更新状态位
					strReturnMessage = cusHandoverAppComponent.updateStatus(
							serno_value, status_value);
				} else {
					strReturnMessage = CMISMessage.DEFEAT;
				}
				// 审批操作
			} else if (CusPubConstant.CUS_CREDIT_SUBMIT.equals(status_value)) {
				// 同意还是否决的标志
				String updateFlag = (String) context.getDataValue("updateFlag");
				if ("0".equals(updateFlag)) {
					status_value = "20";
				} else {
					status_value = "22";
				}
				try {
					kColl = (KeyedCollection) context.getDataElement(modelId);
				} catch (Exception e) {
				}
				if (kColl == null || kColl.size() == 0)
					throw new EMPJDBCException("The values to update["
							+ modelId + "] cannot be empty!");
				kColl.setDataValue("status", status_value);
				kColl.setDataValue("supervise_date", (String) context
						.getDataValue("OPENDAY"));
				int count = dao.update(kColl, connection);
				if (count != 1) {
					throw new EMPException("Update Failed! Record Count: "
							+ count);
				}

				// 同意操作
			} else if ("20".equals(status_value)) {

				status_value = "30";

				try {
					kColl = (KeyedCollection) context.getDataElement(modelId);
				} catch (Exception e) {
				}
				if (kColl == null || kColl.size() == 0)
					throw new EMPJDBCException("The values to update["
							+ modelId + "] cannot be empty!");
				kColl.setDataValue("status", status_value);
				kColl.setDataValue("receive_date", (String) context
						.getDataValue("OPENDAY"));

				String handoverMode = (String) kColl
						.getDataValue("handover_mode");
				String receiverId = (String) kColl.getDataValue("receiver_id");
				String receiverBrId = (String) kColl
						.getDataValue("receiver_br_id");
				String handoverScope = (String) kColl
						.getDataValue("handover_scope");
				String handoverId = (String) kColl.getDataValue("handover_id");
				String handoverBrId = (String) kColl
						.getDataValue("handover_br_id");
				String superviseId = (String) kColl
						.getDataValue("supervise_id");
				String superviseBrId = (String) kColl
						.getDataValue("supervise_br_id");
					
				// 取移交明细中的客户号
				CusHandoverLstComponent cushandoverlstcomponent = (CusHandoverLstComponent) CMISComponentFactory
						.getComponentFactoryInstance().getComponentInstance(
								PUBConstant.CUSHANDOVERLST, context, connection);
				List<CMISDomain> cushandoverlst = cushandoverlstcomponent
						.findCusHandoverLstListBySerno(serno_value);
				List<String> cusidlist=new ArrayList<String>();
				for (Iterator<CMISDomain> iter = cushandoverlst.iterator(); iter
						.hasNext();) {
					CusHandoverLst cus=(CusHandoverLst)iter.next();
					cusidlist.add(cus.getBusinessCode());
				}
				
				//将页面要传递的参数放到map中
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("handovermode", handoverMode);
				map.put("handoverscope",handoverScope);
				map.put("handoverid",handoverId);
				map.put("handoversorg",handoverBrId);
				map.put("superviseid",superviseId);
				map.put("supervisesorg",superviseBrId);
				map.put("receiveid",receiverId);
				map.put("receivesorg",receiverBrId);
				map.put("openDay", (String) context.getDataValue("OPENDAY"));
				map.put("cusidlist", cusidlist);

				strReturnMessage = cusHandoverAppComponent.updateStatus(
						serno_value, status_value);
				// 调用客户移交配置函数，确定执行的移交方案
				CusHandoverCfgComponent cushandovercfgcomponent = (CusHandoverCfgComponent) CMISComponentFactory.getComponentFactoryInstance().getComponentInstance(
								                                     PUBConstant.CUSHANDOVERCFG, context, connection);
				cushandovercfgcomponent.middle(map);
				
				
				// 以前的保留
				ComponentHelper cHelper = new ComponentHelper();
				cHelper = new ComponentHelper();
				CusHandoverApp cusHandoverApp = new CusHandoverApp();
				cusHandoverApp = (CusHandoverApp) cHelper.kcolTOdomain(
						cusHandoverApp, kColl);
				// 新增日志
				String strReturnInsertLog = cusHandoverAppComponent
						.doReceive(cusHandoverApp);
				if (strReturnInsertLog.equals(CMISMessage.ADDDEFEAT)) {
					return strReturnMessage;
				}
				kColl.setDataValue("status", status_value);
				kColl.setDataValue("receive_date", (String) context
						.getDataValue("OPENDAY"));
				int count = dao.update(kColl, connection);
				if (count != 1) {
					throw new EMPException("Update Failed! Record Count: "
							+ count);
				}
			}

			// 第一次提交操作是异步请求，所以添加一个返回值
			if ("10".equals(status_value)
					&& strReturnMessage.equals(CMISMessage.SUCCESS)) {
				String flag = "提交成功";
				context.addDataField("flag", flag);
			} else if ("00".equals(status_value)
					&& strReturnMessage.equals(CMISMessage.DEFEAT)) {
				String flag = "提交失败";
				context.addDataField("flag", flag);
			}

		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}

}
