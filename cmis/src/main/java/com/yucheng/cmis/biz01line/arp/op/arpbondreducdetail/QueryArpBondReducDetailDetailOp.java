package com.yucheng.cmis.biz01line.arp.op.arpbondreducdetail;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.arp.op.pubAction.CheckAssetPreserveOp;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryArpBondReducDetailDetailOp  extends CMISOperation {
	
	private final String modelId = "ArpBondReducDetail";
	private final String pk_serno_name = "pk_serno";	
	private boolean updateCheck = false;	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
		
			if(this.updateCheck){			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}			
			
			String pk_serno_value = null;
			try {
				pk_serno_value = (String)context.getDataValue(pk_serno_name);
			} catch (Exception e) {}
			if(pk_serno_value == null || pk_serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+pk_serno_name+"] cannot be null!");		
			
			KeyedCollection kColl = dao.queryDetail(modelId, pk_serno_value, connection);			
			String[] args,modelIds,modelForeign,fieldName,resultName ;
			args = new String[] { "bill_no","bill_no","bill_no","bill_no","bill_no","bill_no","bill_no","prd_id","bill_no","bill_no"};
			modelIds = new String[] { "AccLoan","AccLoan","AccLoan","AccLoan","AccLoan","AccLoan","AccLoan","PrdBasicinfo","AccLoan","AccLoan"};
			modelForeign = new String[] { "bill_no","bill_no","bill_no","bill_no","bill_no","bill_no","bill_no","prdid","bill_no","bill_no"};
			fieldName = new String[] { "prd_id","loan_amt","loan_balance","inner_owe_int","out_owe_int","distr_date","end_date","prdname","cont_no","cur_type"};
			resultName = new String[] { "prd_id","loan_amt","loan_balance","inner_owe_int","out_owe_int","distr_date","end_date","prd_type","cont_no","cur_type"};
		    SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);
		    
			/*** 外币翻译begin ***/
			CheckAssetPreserveOp cmisOp = new CheckAssetPreserveOp();
			cmisOp.delKcollCurType(kColl, "cur_type", "loan_amt", context);	//借据金额
			cmisOp.delKcollCurType(kColl, "cur_type", "loan_balance", context);	//借据余额
			cmisOp.delKcollCurType(kColl, "cur_type", "inner_owe_int", context);	//表内欠息
			cmisOp.delKcollCurType(kColl, "cur_type", "out_owe_int", context);	//表外欠息
			/*** 外币翻译end ***/
			
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