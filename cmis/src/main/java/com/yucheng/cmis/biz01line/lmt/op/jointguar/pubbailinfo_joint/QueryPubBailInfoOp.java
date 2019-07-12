package com.yucheng.cmis.biz01line.lmt.op.jointguar.pubbailinfo_joint;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpBailComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPubBailInfoOp extends CMISOperation {

	private final String modelId = "PubBailInfo";

	public String doExecute(Context context) throws EMPException {

		Connection connection = null;
		// String cus_id = null;
		String serno = null;
		try {
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection) context
						.getDataElement(this.modelId);
			} catch (Exception e) {
			}
			String conditionStr = TableModelUtil.getQueryCondition(
					this.modelId, queryData, context, false, false, false);

			// try {
			// cus_id = (String) context.getDataValue("cus_id");
			// context.setDataValue("cus_id", cus_id);
			// } catch (Exception e) {
			// }
//			if (context.containsKey("conType")) {
//				if ("".equals(conditionStr)) {
//					conditionStr += "where bail_status!=0";
//				} else {
//					conditionStr += "and bail_status!=0";
//				}
//			}
			String condition = "where 1=1";
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			condition = recordRestrict.judgeQueryRestrict(this.modelId, condition, context, connection);

			int size = 15;

			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one",
					String.valueOf(size));

			// 构建组件类
			IqpBailComponent iqpBail = (IqpBailComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(
							AppConstant.IQPBAILCOMPONENT, context, connection);
			
			IndexedCollection iColl =iqpBail.queryPubBailInfoList(conditionStr,condition,pageInfo, this.getDataSource(context));
			iColl.setName(modelId+"List");

		

			String[] args = new String[] { "cus_id" };
			String[] modelIds = new String[] { "CusBase" };
			String[] modelForeign = new String[] { "cus_id" };
			String[] fieldName = new String[] { "cus_name" };
			// 详细信息翻译时调用
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD,
					context, modelIds, modelForeign, fieldName);
			// 开户机构翻译
			SInfoUtils.addSOrgName(iColl, new String[] { "open_org" });
			this.putDataElement2Context(iColl, context);

			TableModelUtil.parsePageInfo(context, pageInfo);

		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
