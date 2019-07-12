package com.yucheng.cmis.biz01line.psp.op.psppropertyanaly;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryPspPropertyAnalyListForMonOp extends CMISOperation {


	private final String modelId = "PspCapUseMonitor";
	private final String reModelId = "PspRepaySrc";
	private final String analyModelId = "PspCheckAnaly";

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
			// added by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 start
			String check_freq = "";
			if(context.containsKey("check_freq")&&context.getDataValue("check_freq")!=null){
				check_freq = (String) context.getDataValue("check_freq");
			}
			// added by yangzy 2014/12/11  需求:XD141222090,公司小微贷后检查改造 end
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			//资金用途
			String conditionStr = "  where (task_id = '"+task_id+"') or (task_id in (select task_id from "
					+ "psp_check_task where cus_id = '"+cus_id+"' and approve_status = '997' ))";
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,connection);
			iColl.setName(iColl.getName()+"List");
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"});
			this.putDataElement2Context(iColl, context);
			
			//还款来源
			String reConditionStr = " where cus_id = '"+cus_id+"' ";
			IndexedCollection reIColl = dao.queryList(reModelId, null,reConditionStr,connection);
			reIColl.setName(reIColl.getName()+"List");
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(reIColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(reIColl, new String[]{"input_br_id"});
			this.putDataElement2Context(reIColl, context);
			
			//检查分析说明
			KeyedCollection analyKColl = dao.queryDetail(analyModelId, task_id, connection);
			this.putDataElement2Context(analyKColl, context);
			
			//资金监控合计数
			KeyedCollection capKColl = (KeyedCollection) SqlClient.queryFirst("queryCapInfoByCusId", cus_id, null, connection);
			context.addDataField("disb_amt", capKColl.getDataValue("disb_amt"));
			
			//获取贷款信息
			KeyedCollection accKColl = (KeyedCollection) SqlClient.queryFirst("queryAccInfoByCusId", cus_id, null, connection);
			context.addDataField("loan_amt", accKColl.getDataValue("loan_amt"));
			context.addDataField("loan_balance", accKColl.getDataValue("loan_balance"));
			
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
