package com.yucheng.cmis.ops.cfg.cfgriskclass;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.TimeUtil;

public class UpdateCfgRiskClassRecordOp extends CMISOperation {
	private final String modelId = "CfgRiskClass";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = "";
		try{
			connection = this.getConnection(context);

			String ovdueDay = "";//逾期天数
			String grt100 ="";//抵押
			String grt200 ="";//质押
			String grt300 ="";//担保
			String grt400 ="";//信用
			KeyedCollection kColl = null;
			String organNo = (String) context.getDataValue("organNo");
        	String currentUserId = (String) context.getDataValue("currentUserId");
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(context.containsKey("ovdue_days")){
				ovdueDay = (String)context.getDataValue("ovdue_days");
			}
			if(context.containsKey("grt100")){
				grt100 = (String)context.getDataValue("grt100");
			}
			if(context.containsKey("grt200")){
				grt200 = (String)context.getDataValue("grt200");
			}
			if(context.containsKey("grt300")){
				grt300 = (String)context.getDataValue("grt300");
			}
			if(context.containsKey("grt400")){
				grt400 = (String)context.getDataValue("grt400");
			}
			KeyedCollection cfgRiskClassKcolldy = new KeyedCollection();
			cfgRiskClassKcolldy.put("ovdue_days",ovdueDay);
			cfgRiskClassKcolldy.put("guar_way","100");
			cfgRiskClassKcolldy.put("risk_class",grt100);
			cfgRiskClassKcolldy.put("upd_id",currentUserId);
			cfgRiskClassKcolldy.put("upd_br_id",organNo);
			cfgRiskClassKcolldy.put("upd_date",TimeUtil.getCurDate());
			cfgRiskClassKcolldy.setName("CfgRiskClass");
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.update(cfgRiskClassKcolldy, connection);
			
			KeyedCollection cfgRiskClassKcollzy = new KeyedCollection();
			cfgRiskClassKcollzy.put("ovdue_days",ovdueDay);
			cfgRiskClassKcollzy.put("guar_way","200");
			cfgRiskClassKcollzy.put("risk_class",grt200);
			cfgRiskClassKcollzy.put("upd_id",currentUserId);
			cfgRiskClassKcollzy.put("upd_br_id",organNo);
			cfgRiskClassKcollzy.put("upd_date",TimeUtil.getCurDate());
			cfgRiskClassKcollzy.setName("CfgRiskClass");
			dao.update(cfgRiskClassKcollzy, connection);
			
			KeyedCollection cfgRiskClassKcolldb = new KeyedCollection();
			cfgRiskClassKcolldb.put("ovdue_days",ovdueDay);
			cfgRiskClassKcolldb.put("guar_way","300");
			cfgRiskClassKcolldb.put("risk_class",grt300);
			cfgRiskClassKcolldb.put("upd_id",currentUserId);
			cfgRiskClassKcolldb.put("upd_br_id",organNo);
			cfgRiskClassKcolldb.put("upd_date",TimeUtil.getCurDate());
			cfgRiskClassKcolldb.setName("CfgRiskClass");
			dao.update(cfgRiskClassKcolldb, connection);
			
			KeyedCollection cfgRiskClassKcollxy = new KeyedCollection();
			cfgRiskClassKcollxy.put("ovdue_days",ovdueDay);
			cfgRiskClassKcollxy.put("guar_way","400");
			cfgRiskClassKcollxy.put("risk_class",grt400);
			cfgRiskClassKcollxy.put("upd_id",currentUserId);
			cfgRiskClassKcollxy.put("upd_br_id",organNo);
			cfgRiskClassKcollxy.put("upd_date",TimeUtil.getCurDate());
			cfgRiskClassKcollxy.setName("CfgRiskClass");
			dao.update(cfgRiskClassKcollxy, connection);
			flag = "success";
			context.put("flag", flag);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
