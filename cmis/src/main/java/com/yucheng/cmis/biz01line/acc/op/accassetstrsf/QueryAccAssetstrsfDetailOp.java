package com.yucheng.cmis.biz01line.acc.op.accassetstrsf;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryAccAssetstrsfDetailOp  extends CMISOperation {
	
	private final String modelId = "AccAssetstrsf";
	

	private final String bill_no_name = "bill_no";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String bill_no_value = null;
			try {
				bill_no_value = (String)context.getDataValue(bill_no_name);
			} catch (Exception e) {}
			if(bill_no_value == null || bill_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+bill_no_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, bill_no_value, connection);
		    
			String[] args=new String[] {"cus_id","cont_no" ,"cont_no","repay_type"};
			String[] modelIds=new String[]{"CusBase","CtrAssetstrsfCont","CtrAssetstrsfCont","PrdRepayMode"};
			String[]modelForeign=new String[]{"cus_id","cont_no","cont_no","repay_mode_id"};
			String[] fieldName=new String[]{"cus_name","prd_id","serno","repay_mode_dec"};
			String[] resultName=new String[]{"cus_id_displayname","prd_id","fount_serno","repay_type_displayname"};
		    SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);
		    
			SInfoUtils.addSOrgName(kColl, new String[]{"fina_br_id"});  
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id"});
			this.putDataElement2Context(kColl, context);
			
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
