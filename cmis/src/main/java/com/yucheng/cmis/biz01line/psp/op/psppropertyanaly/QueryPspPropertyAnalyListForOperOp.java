package com.yucheng.cmis.biz01line.psp.op.psppropertyanaly;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryPspPropertyAnalyListForOperOp extends CMISOperation {


	private final String modelId = "PspFeeDetail";
	private final String taxModelId = "PspTaxDetail";
	private final String analyModelId = "PspCheckAnaly";
	private final String bankModelId = "PspBankContacc";
	private final String wageModelId = "PspWageContacc";
	private final String ioModelId = "PspIostoreDoc";
	private final String orderModelId = "PspOrderAnaly";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String cus_id = (String) context.getDataValue("cus_id");
			if(cus_id==null){
				throw new EMPException("获取不到客户编号！");
			}
			String task_id = (String) context.getDataValue("task_id");
			if(task_id==null){
				throw new EMPException("获取不到任务编号！");
			}
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			//水电费分析
			String conditionStr = " where cus_id = '"+ cus_id
					+ "' and task_id in (select a.task_id from psp_check_task a , psp_check_task b where a.cus_id = '"+ cus_id
					+ "' and b.task_id = '"+ task_id
					+ "' and a.task_create_date <= b.task_create_date ) order by regi_date desc ";
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			
			//税费分析
			String taxConditionStr = " where cus_id = '"+ cus_id
					+ "' and task_id in (select a.task_id from psp_check_task a , psp_check_task b where a.cus_id = '"+ cus_id
					+ "' and b.task_id = '"+ task_id
					+ "' and a.task_create_date <= b.task_create_date ) ";
			IndexedCollection taxIColl = dao.queryList(taxModelId, null,taxConditionStr,connection);
			taxIColl.setName(taxIColl.getName()+"List");
			this.putDataElement2Context(taxIColl, context);
			
			//银行对账单
			String bankConditionStr = " where cus_id = '"+ cus_id
					+ "' and task_id in (select a.task_id from psp_check_task a , psp_check_task b where a.cus_id = '"+ cus_id
					+ "' and b.task_id = '"+ task_id
					+ "' and a.task_create_date <= b.task_create_date ) ";
			IndexedCollection bankIColl = dao.queryList(bankModelId, null,bankConditionStr,connection);
			bankIColl.setName(bankIColl.getName()+"List");
			this.putDataElement2Context(bankIColl, context);
			
			//工资单
			String wageConditionStr = " where cus_id = '"+ cus_id
					+ "' and task_id in (select a.task_id from psp_check_task a , psp_check_task b where a.cus_id = '"+ cus_id
					+ "' and b.task_id = '"+ task_id
					+ "' and a.task_create_date <= b.task_create_date ) ";
			IndexedCollection wageIColl = dao.queryList(wageModelId, null,wageConditionStr,connection);
			wageIColl.setName(wageIColl.getName()+"List");
			this.putDataElement2Context(wageIColl, context);
			
			//出入库单据
			String ioConditionStr = " where cus_id = '"+ cus_id
					+ "' and task_id in (select a.task_id from psp_check_task a , psp_check_task b where a.cus_id = '"+ cus_id
					+ "' and b.task_id = '"+ task_id
					+ "' and a.task_create_date <= b.task_create_date ) ";
			IndexedCollection ioIColl = dao.queryList(ioModelId, null,ioConditionStr,connection);
			ioIColl.setName(ioIColl.getName()+"List");
			this.putDataElement2Context(ioIColl, context);
			
			//检查分析说明
			KeyedCollection analyKColl = dao.queryDetail(analyModelId, task_id, connection);
			this.putDataElement2Context(analyKColl, context);
			
			//订单明细
			String orderConditionStr = " where cus_id = '"+ cus_id
					+ "' and task_id in (select a.task_id from psp_check_task a , psp_check_task b where a.cus_id = '"+ cus_id
					+ "' and b.task_id = '"+ task_id
					+ "' and a.task_create_date <= b.task_create_date ) ";
			IndexedCollection oerderIColl = dao.queryList(orderModelId, null,orderConditionStr,connection);
			oerderIColl.setName(oerderIColl.getName()+"List");
			this.putDataElement2Context(oerderIColl, context);
			
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
