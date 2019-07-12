package com.yucheng.cmis.biz01line.cont.op.ctrlimitapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class UpdateCtrLimitAppDetailOp extends CMISOperation {
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cont_no = null;
			try {
				cont_no = (String)context.getDataValue("cont_no");
			} catch (Exception e) {}
			if(cont_no == null || cont_no.length() == 0)
				throw new EMPJDBCException("The value of pk[cont_no] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail("CtrLimitCont", cont_no, connection);
			String serno = (String)kColl.getDataValue("serno");
			
			
			String[] args=new String[] {"cus_id"};
			String[] modelIds=new String[]{"CusBase",};
			String[]modelForeign=new String[]{"cus_id"}; 
			String[] fieldName=new String[]{"cus_name"};    
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id"});
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
