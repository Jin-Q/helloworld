package com.yucheng.cmis.biz01line.fnc.op.importreports;


import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteReportOp extends CMISOperation {

	/**
	 * 删除已导入报表
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			/*
			 * 流水号
			 */
			String serno = null;
			/*
			 * 报表类型
			 */
			String reportType = null;
			try {
				serno = (String)context.getDataValue("serno");
				reportType = (String)context.getDataValue("report_type");
			} catch (Exception e) {}
			if(serno == null || serno.length() == 0){
				throw new EMPJDBCException("流水号为空!");				
			}
			if(reportType == null || reportType.length() == 0){
				throw new EMPJDBCException("报表类型为空!");
			}
				
			String modelId = null;
			if("01".equals(reportType)){
				modelId = "IqpMeFncBs";
			}else if("02".equals(reportType)){
				modelId = "IqpMeFncPl";
			}else if("03".equals(reportType)){
				modelId = "IqpMeFncCf";
			}else if("04".equals(reportType)){
				modelId = "IqpMeFncDi";
			}else if("05".equals(reportType)){
				modelId = "IqpMeFncPlant";
			}else if("06".equals(reportType)){
				modelId = "IqpMeFncBreed";
			}else{
				EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "报表类型错误！");
				throw new EMPException("报表类型错误！");
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			Map<String, String> map = new HashMap<String, String>();
			map.put("serno", serno);
//			dao.deleteByField(modelId, map, connection);
//			if("04".equals(reportType)){
//				dao.deleteByField("IqpMeFncDiMtg", map, connection);
//				dao.deleteByField("IqpMeFncDiParty", map, connection);
//			}
			
			context.put("flag", "success");
		}catch (EMPException ee) {
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "删除报表失败！");
			throw ee;
		} catch(Exception e){
			EMPLog.log(this.getClass().getName(), EMPLog.ERROR, 0, "删除报表失败！");
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
