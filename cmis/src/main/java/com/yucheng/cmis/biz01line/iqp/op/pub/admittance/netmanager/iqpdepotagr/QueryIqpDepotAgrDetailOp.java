package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.netmanager.iqpdepotagr;

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

public class QueryIqpDepotAgrDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpDepotAgr";
	

	private final String depot_agr_no_name = "depot_agr_no";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String depot_agr_no_value = null;
			try {
				//保兑仓协议编号
				depot_agr_no_value = (String)context.getDataValue(depot_agr_no_name);
			} catch (Exception e) {}
			if(depot_agr_no_value == null || depot_agr_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+depot_agr_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, depot_agr_no_value, connection);
			String[] args=new String[] { "cus_id" };
		    String[] modelIds=new String[]{"CusBase"};
		    String[] modelForeign=new String[]{"cus_id"};
		    String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
            SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			//翻译登记人、登记机构相关信息
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id"});
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
