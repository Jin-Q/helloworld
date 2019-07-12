package com.yucheng.cmis.biz01line.iqp.op.iqpratechangeapp;

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

public class QueryIqpRateChangeAppDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpRateChangeApp";
	private final String modelIdAcc = "AccLoan";
	private final String modelIdCont = "CtrLoanCont";
	private final String modelIdContSub = "CtrLoanContSub";
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
			KeyedCollection kCollAcc = dao.queryDetail(modelIdAcc, bill_no, connection);
			String cont_no = (String)kCollAcc.getDataValue("cont_no");
			if(cont_no!=null && !"".equals(cont_no)){
			  KeyedCollection kCollCont = dao.queryDetail(modelIdCont, cont_no, connection);
			  KeyedCollection kCollContSub = dao.queryDetail(modelIdContSub, cont_no, connection);
			  kColl.addDataField("prd_id", (String)kCollCont.getDataValue("prd_id"));
			  kColl.addDataField("term_type", (String)kCollContSub.getDataValue("term_type"));
			  kColl.addDataField("cont_term", (String)kCollContSub.getDataValue("cont_term"));
			}else{
				throw new Exception("合同编号为空，查询异常!");
			}
			String[] args=new String[] {"cus_id"};
			String[] modelIds=new String[]{"CusBase"};
			String[]modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id"});
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
