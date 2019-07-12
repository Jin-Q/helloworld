package com.yucheng.cmis.biz01line.lmt.op.lmtappfrozeunfroze;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.util.TableModelUtil;

public class UpdateUnfrozeRecordStatusOp extends CMISOperation {


	private final String modelId = "LmtAppFrozeUnfroze";
	private final String modelIdAgr = "LmtAgrDetails";
	
	private final String serno_name = "serno";

	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno_value = null;
			String approve_status = null;
			String limit_code_value = null;
			String agr_no_value = null;
			
			Double froze_unfroze_amt = null;
			Double froze_unfroze_amt_sum = null;
			String froze_amt_hq = null;
			String froze_unfroze_amt_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
				limit_code_value = (String)context.getDataValue("limit_code");
				approve_status = (String)context.getDataValue("approve_status");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);//获得当前选中的 对象
			String opendate = (String) context.getDataValue("OPENDAY");//系统当前日期
			
			String condition = "where limit_code='"+limit_code_value+"'";
			if("000".equals(approve_status)){
				kColl.put("approve_status", "997");
				kColl.put("over_date", opendate);
				dao.update(kColl, connection);
				
				
				/*累计总的冻结额度存入台账表中的总部冻结金额字段中*/
				KeyedCollection kColl2 =  dao.queryDetail(modelIdAgr, limit_code_value, connection);
				froze_amt_hq = (String)kColl2.getDataValue("froze_amt_hq");//获取总的冻结金额
				froze_unfroze_amt_value = (String)kColl.getDataValue("froze_unfroze_amt");//获取当前冻结金额
				froze_unfroze_amt = Double.parseDouble(froze_unfroze_amt_value);//转换获取当前冻结金额
				froze_unfroze_amt_sum = Double.parseDouble(froze_amt_hq);//转换获取总的冻结金额
				if(froze_unfroze_amt_sum >= froze_unfroze_amt){
					
					froze_unfroze_amt_sum -= froze_unfroze_amt;
				}
				
				kColl2.put("froze_amt_hq", froze_unfroze_amt_sum);
				dao.update(kColl2, connection);
				
				context.put("flag", "success");
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
