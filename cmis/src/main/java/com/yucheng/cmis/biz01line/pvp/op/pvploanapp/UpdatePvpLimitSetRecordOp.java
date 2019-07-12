package com.yucheng.cmis.biz01line.pvp.op.pvploanapp;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class UpdatePvpLimitSetRecordOp extends CMISOperation {
	private static final String modelId = "PvpLimitSet";
	/**
	 * 设置出账当日放款额度，对于同一营业时间的额度，存在跟新，无则新增
	 * 额度表：pvp_limit_set
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String limit_amt = (String)context.getDataValue("limit_amt"); 
			if(limit_amt == null || limit_amt.trim().length() == 0){
				throw new EMPException("获取设置额度失败！");
			}
			String orgNo = (String)context.getDataValue(CMISConstance.ATTR_ORGID);
			String openDay = (String)context.getDataValue(CMISConstance.OPENDAY);
			String inputId = (String)context.getDataValue(CMISConstance.ATTR_CURRENTUSERID);
			TableModelDAO dao = this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("serno");
			list.add("open_day");
			KeyedCollection kc = dao.queryFirst(modelId, list, " where open_day = '"+openDay+"'", connection);
			String sernoHelp = (String)kc.getDataValue("serno");
			if(sernoHelp != null && sernoHelp.length() > 0){
				kc.addDataField("org_id", orgNo);
				kc.addDataField("limit_amt", limit_amt);
				kc.addDataField("input_id", inputId);
				dao.update(kc, connection);
			}else {
				String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
				KeyedCollection insertKColl = new KeyedCollection();
				insertKColl.addDataField("serno", serno);
				insertKColl.addDataField("open_day", openDay);
				insertKColl.addDataField("org_id", orgNo);
				insertKColl.addDataField("limit_amt", limit_amt);
				insertKColl.addDataField("out_limit_amt", "0");
				insertKColl.addDataField("input_id", inputId);
				insertKColl.setName(modelId);
				dao.insert(insertKColl, connection);
			}
			context.addDataField("flag", "success");
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

}
