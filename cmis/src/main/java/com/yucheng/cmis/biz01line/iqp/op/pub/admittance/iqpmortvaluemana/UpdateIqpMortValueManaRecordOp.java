package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpmortvaluemana;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class UpdateIqpMortValueManaRecordOp extends CMISOperation {
	
	private final String modelId = "IqpMortValueMana";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0){
				context.addDataField("msg", "表模型IqpMortValueMana数据为空，请联系管理员！");
				context.addDataField("flag","N");
				return "0";
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			
			//价格调整
			if(kColl.containsKey("isAdj")){
				KeyedCollection kcoll_adj = new KeyedCollection("IqpMortValueAdj");
				
				kcoll_adj.addDataField("value_no", kColl.getDataValue("value_no"));
				kcoll_adj.addDataField("org_valve", kColl.getDataValue("org_value"));
				kcoll_adj.addDataField("change_valve", kColl.getDataValue("market_value"));
				kcoll_adj.addDataField("info_sour", kColl.getDataValue("info_sour"));
				kcoll_adj.addDataField("change_resn", kColl.getDataValue("change_resn"));
				kcoll_adj.addDataField("inure_date", context.getDataValue("OPENDAY"));
				kcoll_adj.addDataField("input_id", kColl.getDataValue("org_value"));
				kcoll_adj.addDataField("input_br_id", kColl.getDataValue("org_value"));
				kcoll_adj.addDataField("input_date", context.getDataValue("OPENDAY"));
				
				dao.insert(kcoll_adj, connection);
			}
		
			int count=dao.update(kColl, connection);
			if(count!=1){
				context.addDataField("msg", "更新记录数为"+count+"，请联系管理员！");
				context.addDataField("flag","N");
				return "0";
			}
		
			//根据调整后价格更新货物单价及总价
			String value_no = (String)kColl.getDataValue("value_no");//价格编号
			String market_value = (String)kColl.getDataValue("market_value");//此次商品核准价格
			SqlClient.update("updateMortCargoPledgeAmt", value_no, market_value, null, connection);
			
			context.addDataField("flag", "Y");
			context.addDataField("msg", "Y");
			context.addDataField("value_no", kColl.getDataValue("value_no"));
		}catch(Exception e){
			context.addDataField("msg", e.getMessage());
			context.addDataField("flag","N");
			try {
				connection.rollback();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
