package com.yucheng.cmis.biz01line.lmt.op.lmtagrdetails;

import java.sql.Connection;
import java.sql.SQLException;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckLmtCanFrozenAmt extends CMISOperation {

	private final String modelId = "LmtAgrDetails";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String org_limit_code = null;//台账编号
		try{
			connection = this.getConnection(context);
			try {
				org_limit_code = (String) context.getDataValue("org_limit_code");
			} catch (Exception e) {}
			
			if(org_limit_code == null || org_limit_code.length() == 0)
				throw new EMPJDBCException("The values ["+org_limit_code+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = "where limit_code = '"+org_limit_code+"'";
			KeyedCollection kcollTemp = dao.queryFirst(modelId, null, condition, connection);
			
			double froze_amt = 0;
			double crd_amt = Double.parseDouble(kcollTemp.getDataValue("crd_amt").toString());//授信总额
			String froze_amt_str = (String) kcollTemp.getDataValue("froze_amt");
			if(froze_amt_str!=null&&!"".equals(froze_amt_str)){
				froze_amt = Double.parseDouble(kcollTemp.getDataValue("froze_amt").toString());//冻结金额
			}
			
			double can_froze_amt = crd_amt - froze_amt;//可以冻结金额
			
			context.addDataField("flag","success");
			context.addDataField("can_froze_amt",can_froze_amt);
		}catch(Exception e){
			context.addDataField("flag","failed");
			context.addDataField("can_froze_amt", 0);
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