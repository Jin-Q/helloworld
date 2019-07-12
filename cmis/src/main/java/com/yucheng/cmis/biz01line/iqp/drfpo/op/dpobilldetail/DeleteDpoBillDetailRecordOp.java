package com.yucheng.cmis.biz01line.iqp.drfpo.op.dpobilldetail;

import java.net.URLDecoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class DeleteDpoBillDetailRecordOp extends CMISOperation {

	private final String modelId = "IqpBillDetailInfo";
	

	private final String porder_no_name = "porder_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String drfpo_no = "";
		try{
			connection = this.getConnection(context);



			String porder_no_value = null;
			try {
				porder_no_value = (String)context.getDataValue(porder_no_name);
				drfpo_no = (String) context.getDataValue("drfpo_no");
				//中文转码
				drfpo_no = URLDecoder.decode(drfpo_no,"UTF-8");
				porder_no_value = URLDecoder.decode(porder_no_value,"UTF-8");
			} catch (Exception e) {}
			if(porder_no_value == null || porder_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+porder_no_name+"] cannot be null!");
				


			TableModelDAO dao = this.getTableModelDAO(context);
			Map pkMap = new HashMap();
			pkMap.put("drfpo_no",drfpo_no);
			pkMap.put("porder_no",porder_no_value);
			//级联删除关系表信息
			int cou = dao.deleteByPks("IqpCorreInfo", pkMap, connection);
			//删除票据明细信息
			int count=dao.deleteByPk(modelId, porder_no_value, connection);
			if(count!=1||cou!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}else{
				context.addDataField("flag","success");
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
