package com.yucheng.cmis.biz01line.iqp.op.iqpinterfact;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpInterFactDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpInterFact";
	

	private final String serno_name = "serno";
	
	
	private boolean updateCheck = false;
	

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
			
			//从申请表获取保理融资类型
			KeyedCollection iqpkcoll = dao.queryDetail("IqpLoanApp", serno_value, connection);
			String prd_id = (String) iqpkcoll.getDataValue("prd_id");
			if(prd_id!=null&&"800021".equals(prd_id)){
				kColl.setDataValue("fin_type", "1");
			}
			//added by yangzy 2015/05/28 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式  begin
			String cus_id = (String) iqpkcoll.getDataValue("cus_id");
			context.put("cus_id", cus_id);
			//added by yangzy 2015/05/28 需求编号：HS141110017，保理业务改造，过滤保理签订质押合同模式  end
			this.putDataElement2Context(kColl, context);
			/**add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造  begin**/
			String[] args=new String[] { "buy_cus_id"};
			String[] modelIds=new String[]{"CusBase"};
			String[]modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};

			//详细信息翻译时调用			
		    SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName); 
		    /**add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造  end**/
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
