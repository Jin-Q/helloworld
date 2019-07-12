package com.yucheng.cmis.biz01line.fnc.op.importreports;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;

public class GetReportImportPageOp extends CMISOperation {


	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno = null;
			
			try{
				serno = (String)context.getDataValue("serno");
			}catch (Exception e) {}
			if(serno == null || "".equals(serno)){
				EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "业务流水号为空！");
				throw new EMPException("业务流水号为空！");
			}
			KeyedCollection kColl = new KeyedCollection();
			kColl.setName("ReportInfo");
			kColl.put("serno", serno);
			this.putDataElement2Context(kColl, context);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = " where serno='"+serno+"'";
			IndexedCollection existIcoll = new IndexedCollection();
			KeyedCollection existKcoll = null;
			if((dao.queryList("IqpMeFncBs", condition, connection)).size()>0){
				//context.put("asset_debt", "资产负债表");
				existKcoll = new KeyedCollection();
				existKcoll.put("serno", serno);
				existKcoll.put("report_name", "资产负债表");
				existKcoll.put("report_type", "01");
				existIcoll.add(existKcoll);
			}
			if((dao.queryList("IqpMeFncPl", condition, connection)).size()>0){
				//context.put("profit_loss", "损益表");
				existKcoll = new KeyedCollection();
				existKcoll.put("serno", serno);
				existKcoll.put("report_name", "损益表");
				existKcoll.put("report_type", "02");
				existIcoll.add(existKcoll);
			}
			if((dao.queryList("IqpMeFncCf", condition, connection)).size()>0){
				//context.put("current_flow", "现金流量表");
				existKcoll = new KeyedCollection();
				existKcoll.put("serno", serno);
				existKcoll.put("report_name", "现金流量表");
				existKcoll.put("report_type", "03");
				existIcoll.add(existKcoll);
			}
			if((dao.queryList("IqpMeFncDi", condition, connection)).size()>0){
				//context.put("di_investigation", "抵好贷调查表");
				existKcoll = new KeyedCollection();
				existKcoll.put("serno", serno);
				existKcoll.put("report_name", "抵好贷调查表");
				existKcoll.put("report_type", "04");
				existIcoll.add(existKcoll);
			}
			if((dao.queryList("IqpMeFncPlant", condition, connection)).size()>0){
				//context.put("plant_investigation", "种植情况调查表");
				existKcoll = new KeyedCollection();
				existKcoll.put("serno", serno);
				existKcoll.put("report_name", "种植情况调查表");
				existKcoll.put("report_type", "05");
				existIcoll.add(existKcoll);
			}
			if((dao.queryList("IqpMeFncBreed", condition, connection)).size()>0){
				//context.put("breed_investigation", "养殖情况调查表");
				existKcoll = new KeyedCollection();
				existKcoll.put("serno", serno);
				existKcoll.put("report_name", "养殖情况调查表");
				existKcoll.put("report_type", "06");
				existIcoll.add(existKcoll);
			}
			existIcoll.setName("ExistReportList");
			this.putDataElement2Context(existIcoll, context);
			
		}catch (EMPException ee) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "进入报表导入页面错误！");
			throw ee;
		} catch(Exception e){
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "进入报表导入页面错误！");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
	}	
}
