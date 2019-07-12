package com.yucheng.cmis.biz01line.lmt.op.lmtindusapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class GetLmtIndusApplySernoOp extends CMISOperation {

	private final String modelId = "LmtIndusApply";
	private final String key_value = "agr_no";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			KeyedCollection kColl = new KeyedCollection(modelId);
			connection = this.getConnection(context);
			
			if(((String) context.getDataValue("menuId")).equals("indus_crd_change")){
				String agr_no = "";
				String change_list_flag = "";
				try {
					agr_no = (String)context.getDataValue(key_value);
					change_list_flag = (String)context.getDataValue("change_list_flag");
				} catch (Exception e) {}
				if(agr_no == null || agr_no.length() == 0)
					throw new EMPJDBCException("The value of pk["+key_value+"] cannot be null!");

				TableModelDAO dao = this.getTableModelDAO(context);
				kColl = dao.queryDetail("LmtIndusAgr", agr_no, connection);
				KeyedCollection kColl_app = new KeyedCollection("LmtIndusApp");
				kColl_app = dao.queryDetail(modelId, (String)kColl.getDataValue("serno"), connection);
				kColl.addDataField("crd_term_type", (String)kColl_app.getDataValue("crd_term_type"));
				kColl.addDataField("crd_term", (String)kColl_app.getDataValue("crd_term"));
				kColl.addDataField("apply_type", "002");
				kColl.addDataField("change_list_flag", change_list_flag);
				kColl.removeDataElement("serno");
				kColl.removeDataElement("input_date");
				kColl.removeDataElement("input_id");
				kColl.removeDataElement("input_br_id");
				
				if(change_list_flag.equals("2")){
					kColl.removeDataElement("manager_id");
					kColl.removeDataElement("manager_br_id");
					kColl.removeDataElement("end_date");					
				}else{
					SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id"});
					SInfoUtils.addUSerName(kColl, new String[] { "manager_id"});
				}
				
				/** 翻译机构和产品 **/
				if(((String)kColl.getDataValue("shared_scope")).equals("2")){
					SystemTransUtils.containCommaORG2CN("belg_org",kColl,context);
				}
				SInfoUtils.getPrdPopName(kColl, "suit_prd", connection);
				kColl.setName("LmtIndusApply");
			}
			
			this.putDataElement2Context(kColl, context);
			
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