package com.yucheng.cmis.biz01line.iqp.op.iqpaverageassetapp;

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

public class QueryIqpAverageAssetAppDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpAverageAssetApp";
	private final String modelIdAccView = "AccView";
	private final String serno_name = "serno";
	private boolean updateCheck = true;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
		    
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			String bill_no = (String)kColl.getDataValue("bill_no");
		    KeyedCollection kCollAccView = dao.queryFirst(modelIdAccView, null, "where bill_no='"+bill_no+"'", connection);
		    if(kCollAccView!=null){
		    	kColl.put("loan_amt", kCollAccView.getDataValue("bill_amt"));
		    	kColl.put("loan_balance", kCollAccView.getDataValue("bill_bal"));
		    	kColl.put("distr_date", kCollAccView.getDataValue("start_date"));
		    	kColl.put("end_date", kCollAccView.getDataValue("end_date"));
		    	kColl.put("reality_ir_y", kCollAccView.getDataValue("reality_ir_y"));
		    	kColl.put("five_class", kCollAccView.getDataValue("five_class"));
		    	kColl.put("twelve_cls_flg", kCollAccView.getDataValue("twelve_cls_flg"));
		    	kColl.put("inner_owe_int", kCollAccView.getDataValue("inner_owe_int"));
		    	kColl.put("out_owe_int", kCollAccView.getDataValue("out_owe_int"));
		    	kColl.put("manager_br_id", kCollAccView.getDataValue("manager_br_id"));
		    	kColl.put("fina_br_id", kCollAccView.getDataValue("fina_br_id"));
		    }else{
		    	throw new Exception("查询台账视图出错！");
		    }
			String[] args=new String[] {"cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[]modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","fina_br_id","input_br_id"});
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
