package com.yucheng.cmis.biz01line.cont.op.ctrloancont;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.appPub.DateUtils;
import com.yucheng.cmis.operation.CMISOperation;

public class CaculateContEndDateOp extends CMISOperation {
	
	private final String modelId = "CtrLoanCont";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String startDate = null;
			String term_type = null;
			String cont_term = null;
			try {
				startDate = (String)context.getDataValue("startDate");
				term_type = (String)context.getDataValue("type");
				cont_term = (String)context.getDataValue("term");
			} catch (Exception e) {
				throw new EMPJDBCException("计算合同到期日参数异常!");
			}
			String type = "";
			if("001".equals(term_type)){
				type = "Y";
			}else if("002".equals(term_type)){
				type = "M";
			}else if("003".equals(term_type)){
				type = "D";
			}	
			String endDate = DateUtils.getAddDate(type, startDate, Integer.parseInt(cont_term));
			context.addDataField("flag", "success");
			context.addDataField("endDate", endDate);

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
