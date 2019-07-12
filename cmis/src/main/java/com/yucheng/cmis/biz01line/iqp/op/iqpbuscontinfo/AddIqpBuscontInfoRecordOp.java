package com.yucheng.cmis.biz01line.iqp.op.iqpbuscontinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class AddIqpBuscontInfoRecordOp extends CMISOperation {

	private final String modelId = "IqpBuscontInfo";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection) context.getDataElement(modelId);
			} catch (Exception e) {
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			String po_no = (String) kColl.getDataValue("po_no");
			KeyedCollection Kc = dao.queryDetail("IqpActrecpoMana", po_no, connection);
			String po_type = (String) Kc.getDataValue("po_type");
			String tcont_no = kColl.getDataValue("tcont_no").toString();
			KeyedCollection kCollQuery =null;
			if(po_type.equals("1")){//应收账款池
				kCollQuery = dao.queryFirst(modelId, null, "where tcont_no='"+tcont_no+"' and type ='1'", connection);
				kColl.addDataField("type", "1");
			}else if(po_type.equals("2")){//保理池
				kCollQuery = dao.queryFirst(modelId, null, "where tcont_no='"+tcont_no+"' and type ='2'", connection);
				kColl.addDataField("type", "2");
			}
			
			if(kCollQuery==null||kCollQuery.getDataValue("tcont_no")==null){
				String main_br_id = (String) context.getDataValue("organNo");
				/*String tContNo = CMISSequenceService4JXXD.querySequenceFromSQ("TC",
						"all", main_br_id, connection, context);*/
				kColl.setDataValue("input_id", context
						.getDataValue("currentUserId"));
				kColl.setDataValue("input_br_id", context.getDataValue("organNo"));
				kColl.setDataValue("input_date", context.getDataValue("OPENDAY"));
				kColl.setDataValue("po_no", po_no);
				kColl.setDataValue("tcont_no", tcont_no);
				dao.insert(kColl, connection);
				context.addDataField("flag", "success");
			}else{
				context.addDataField("flag", "exist");
			}
			
		} catch (EMPException ee) {
			context.addDataField("flag", "fail");
			throw ee;
		} catch (Exception e) {
			context.addDataField("flag", "fail");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
