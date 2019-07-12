package com.yucheng.cmis.biz01line.lmt.op.lmtquotaadjust;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckLmtQuotaAdjustAppRecord extends CMISOperation {

	private final String modelId = "LmtQuotaAdjustApp";
	
	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(context.containsKey("serno")){
				String serno_value = null;
				try {
					serno_value = (String)context.getDataValue(serno_name);
				} catch (Exception e) {}
				if(serno_value == null || serno_value.length() == 0)
					throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
				TableModelDAO dao = this.getTableModelDAO(context);
				String conditionStr = "where fin_serno= '"+serno_value+"' AND approve_status in ('000','111','992','993') AND STATUS = '2' order by end_date desc";
				IndexedCollection iCollTemp = dao.queryList(modelId, null, conditionStr, connection);	
				
				if(iCollTemp!=null&&iCollTemp.size()>0){
					context.put("flag", "存在【未生效】状态的调整明细，不能提交审批，请启用待生效！");
				}else{
					context.put("flag", "success");
				}
			}else{
				String fin_agr_no = null;
				try {
					fin_agr_no = (String)context.getDataValue("fin_agr_no");
				} catch (Exception e) {}
				if(fin_agr_no == null || fin_agr_no.length() == 0)
					throw new EMPJDBCException("The value of pk[fin_agr_no] cannot be null!");
				TableModelDAO dao = this.getTableModelDAO(context);
				String conditionStr = "where fin_agr_no= '"+fin_agr_no+"' AND approve_status = '111' ";
				IndexedCollection iCollTemp = dao.queryList(modelId, null, conditionStr, connection);	
				
				if(iCollTemp!=null&&iCollTemp.size()>0){
					context.put("flag", "业务审批中，不能进行新增操作！");
				}else{
					context.put("flag", "success");
				}
			}
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
