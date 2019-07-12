package com.yucheng.cmis.biz01line.iqp.op.iqpactrecpomana;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class GetIqpActrecpoManaPoNo extends CMISOperation {

	private final String modelId = "IqpActrecpoMana";

	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		KeyedCollection kColl = new KeyedCollection(modelId);
		String poNo = "";
		try {
			poNo = (String) context.getDataValue("po_no");
		} catch (Exception e) {
		}
		connection = this.getConnection(context);
		/**modified by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 begin**/
		if (poNo == null || poNo.equals("")) {
			kColl = new KeyedCollection(modelId);
			//String main_br_id = (String) context.getDataValue("organNo");
			kColl.addDataField("input_id", context.getDataValue("currentUserId"));
			kColl.addDataField("input_br_id", context.getDataValue("organNo"));
			kColl.addDataField("input_date", context.getDataValue("OPENDAY"));
			kColl.addDataField("status", "1");
			getCusName(kColl, context);
			this.putDataElement2Context(kColl, context);
		} else {
			TableModelDAO dao = this.getTableModelDAO(context);
			kColl = dao.queryFirst(modelId, null, " where po_no = '" + poNo
					+ "'", connection);
			getCusName(kColl, context);
			this.putDataElement2Context(kColl, context);
			//查询该保理池对应的回款保证金明细
			String condition = "where PO_NO = '"+poNo+"'";
			IndexedCollection BailADL = dao.queryList("IqpBailaccDetail", condition, connection);
			BailADL.setName("IqpBailaccDetailList");
			for(int i=0 ;i<BailADL.size();i++){
				getCusName(((KeyedCollection) BailADL.get(i)), context);
			}
			this.putDataElement2Context(BailADL, context);
		}
		return "0";
		/**modified by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 end**/
	}

	private void getCusName(KeyedCollection kColl, Context context)
			throws EMPException {
		String[] args = new String[] { "cus_id" };
		String[] modelIds = new String[] { "CusBase" };
		String[] modelForeign = new String[] { "cus_id" };
		String[] fieldName = new String[] { "cus_name" };
		SInfoUtils.addUSerName(kColl, new String[] { "input_id" });
		SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id" });
		SInfoUtils.addUSerName(kColl, new String[] { "manager_id" });
		SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" });
		// 详细信息翻译时调用
		SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context,
				modelIds, modelForeign, fieldName);

	}

}
