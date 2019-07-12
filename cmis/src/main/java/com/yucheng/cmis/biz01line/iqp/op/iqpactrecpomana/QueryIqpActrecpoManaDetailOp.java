package com.yucheng.cmis.biz01line.iqp.op.iqpactrecpomana;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpActrecpoManaDetailOp extends CMISOperation {

	private final String modelId = "IqpActrecpoMana";

	private final String po_no_name = "po_no";

	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);

			if (this.updateCheck) {

				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context,
						connection);
			}

			String po_no_value = null;
			try {
				po_no_value = (String) context.getDataValue(po_no_name);
			} catch (Exception e) {
			}
			if (po_no_value == null || po_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk[" + po_no_name
						+ "] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, po_no_value,
					connection);
			getCusName(kColl, context);
			this.putDataElement2Context(kColl, context);
			/**modified by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 begin**/
			//查询该保理池对应的回款保证金明细
			String condition = "where PO_NO = '"+po_no_value+"'";
			IndexedCollection BailADL = dao.queryList("IqpBailaccDetail", condition, connection);
			BailADL.setName("IqpBailaccDetailList");
			for(int i=0 ;i<BailADL.size();i++){
				getCusName(((KeyedCollection) BailADL.get(i)), context);
			}
			this.putDataElement2Context(BailADL, context);
			/**modified by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 end**/
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
