package com.yucheng.cmis.biz01line.iqp.op.iqpassetissueinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class UpdateIqpAssetIssueInfoRecordOp extends CMISOperation {
	

	private final String modelId = "IqpAssetIssueInfo";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			String serno = (String)kColl.getDataValue("serno");
			TableModelDAO dao = this.getTableModelDAO(context);
			
			KeyedCollection kc = dao.queryDetail(modelId, serno, connection);
			String kSerno = (String)kc.getDataValue("serno");
			if(kSerno != null && kSerno != ""){
				dao.update(kColl, connection);
				
				//同时修改发行结果
				String resultcondition = " where serno = '"+serno+"'";
				IndexedCollection resultic = dao.queryList("IqpAssetIssueResult", resultcondition, connection);//获取结果信息
				for(int i=0;i<resultic.size();i++){
					KeyedCollection resultkc = (KeyedCollection) resultic.get(i);
					resultkc.setDataValue("act_issue_date", kColl.getDataValue("act_issue_date"));//取的是项目信息中设置的发行日期，可编辑
					resultkc.setDataValue("end_date", kColl.getDataValue("end_date"));//取的是项目信息中的发行截止日期，若没有设置值，则为空,可编辑
					resultkc.setDataValue("base_date", kColl.getDataValue("base_date"));//不可编辑，取的是项目信息中的基准日期
					dao.update(resultkc, connection);
				}
			} else {
				dao.insert(kColl, connection);
				
				//同时生成发行结果
				String prdcondition = " where serno = '"+serno+"'";
				IndexedCollection prdic = dao.queryList("IqpAssetPrdInfo", prdcondition, connection);//获取产品信息
				for(int i=0;i<prdic.size();i++){
					KeyedCollection prdkc = (KeyedCollection) prdic.get(i);
					KeyedCollection resultkc = new KeyedCollection("IqpAssetIssueResult");
					resultkc.addDataField("serno", prdkc.getDataValue("serno"));
					resultkc.addDataField("prd_id", prdkc.getDataValue("prd_id"));
					resultkc.addDataField("act_issue_date", kColl.getDataValue("act_issue_date"));//取的是项目信息中设置的发行日期，可编辑
					resultkc.addDataField("end_date", kColl.getDataValue("end_date"));//取的是项目信息中的发行截止日期，若没有设置值，则为空,可编辑
					resultkc.addDataField("base_date", kColl.getDataValue("base_date"));//不可编辑，取的是项目信息中的基准日期
					resultkc.addDataField("fore_end_date", prdkc.getDataValue("fore_end_date"));//输入项，若在产品信息设置时已经有值，则保留原值，但可编辑
					resultkc.addDataField("cont_no", kColl.getDataValue("cont_no"));//项目编号
					dao.insert(resultkc, connection);
				}
			}

			context.addDataField("flag", "success");
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
