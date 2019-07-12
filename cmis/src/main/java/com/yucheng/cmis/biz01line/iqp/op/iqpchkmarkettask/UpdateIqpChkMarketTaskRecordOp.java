package com.yucheng.cmis.biz01line.iqp.op.iqpchkmarkettask;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class UpdateIqpChkMarketTaskRecordOp extends CMISOperation {
	
//	private final String modelId = "IqpChkMarketTask";
	private final String adjModelId = "IqpMortValueAdj";
	private final String valModelId = "IqpMortValueMana";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection valKColl = null;
//			RecordRestrict recordRestrict = this.getRecordRestrict(context);
//			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			String pk_id = "";
			Double value = 0.00;
			if(context.containsKey("isConfirm")){
				try {
					pk_id = (String) context.getDataValue("pk_id");
				} catch (Exception e) {}
				if(pk_id == null || "".equals(pk_id)){
					context.addDataField("msg", "主键pk_id为空，请联系管理员！");
					context.addDataField("flag","N");
					return "0";
				}
				KeyedCollection kColl4update = dao.queryDetail(adjModelId, pk_id, connection);
				kColl4update.put("status", "2");//已处理
				kColl4update.put("inure_date", context.getDataValue("OPENDAY"));
				int count=dao.update(kColl4update, connection);
				if(count!=1){
					context.addDataField("msg", "更新记录数为"+count+"，请联系管理员！");
					context.addDataField("flag","N");
					return "0";
				}
				String value_no = (String) kColl4update.getDataValue("value_no");
				KeyedCollection kColl4val = dao.queryDetail(valModelId, value_no, connection);
				kColl4val.put("auth_date", context.getDataValue("OPENDAY"));
				
				//根据调整后价格更新货物单价及总价
				String market_value = (String)kColl4update.getDataValue("change_valve");//此次商品核准价格(调整后)
				if(market_value!=null&&!"".equals(market_value)&&Double.valueOf(market_value).compareTo(value)>0){
					SqlClient.update("updateMortCargoPledgeAmt", value_no, market_value, null, connection);
				}else{
					market_value = (String)kColl4update.getDataValue("org_valve");//此次商品核准价格（未调整）
					SqlClient.update("updateMortCargoPledgeAmt", value_no, market_value, null, connection);
				}
				kColl4val.put("market_value", market_value);
				dao.update(kColl4val, connection);
			}else{
				KeyedCollection kColl = null;
				String value_no = "";
				String info_sour = "";
				try {
					kColl = (KeyedCollection)context.getDataElement(adjModelId);
					valKColl = (KeyedCollection)context.getDataElement(valModelId);
					value_no = (String) kColl.getDataValue("value_no");
					info_sour = (String) kColl.getDataValue("info_sour");
					valKColl.addDataField("value_no", value_no);
					valKColl.addDataField("info_sour", info_sour);
				} catch (Exception e) {}
				if(kColl == null || kColl.size() == 0){
					context.addDataField("msg", "表模型IqpChkMarketTask数据为空，请联系管理员！");
					context.addDataField("flag","N");
					return "0";
				}
				
				//提交价格
				if(context.containsKey("isWf")){
					kColl.setDataValue("status", "2");//已处理
					kColl.setDataValue("inure_date", context.getDataValue("OPENDAY"));
					
					valKColl.setDataValue("market_value", kColl.getDataValue("change_valve"));
					valKColl.setDataValue("auth_date", context.getDataValue("OPENDAY"));
					dao.update(valKColl, connection);
					
					//根据调整后价格更新货物单价及总价
					String market_value = (String)kColl.getDataValue("change_valve");//此次商品核准价格
					SqlClient.update("updateMortCargoPledgeAmt", value_no, market_value, null, connection);
				}
				
				int count=dao.update(kColl, connection);
				if(count!=1){
					context.addDataField("msg", "更新记录数为"+count+"，请联系管理员！");
					context.addDataField("flag","N");
					return "0";
				}
			}
			context.addDataField("flag", "Y");
			context.addDataField("msg", "Y");
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
