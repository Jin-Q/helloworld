package com.yucheng.cmis.biz01line.iqp.op.iqpFlow;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.util.TableModelUtil;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.lmt.msi.LmtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.workflow.util.WorkFlowUtil;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class QueryIqpFlowOp  extends CMISOperation {
	
	private final String modelId = "WfiWorklistTodo";
	private final String modelIdEnd = "WfiWorklistEnd";
	private final String modelIdLmt = "LmtAgrInfo";
	private final String modelIdCus = "CusBase";
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
			String limit_acc_no = null;
			String limit_credit_no = null;
			String cus_id = null;
			String limit_ind = null;
			String instanceId = null;
			String instanceId1 = null;
			String instanceId2 = null;
			String condition = "where pk_value=''";;
			String condition2 = "where pk_value=''";;
			String condition3 = "";
			try {
				serno_value = (String)context.getDataValue("serno");
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk serno cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			
			/**贸易融资额度申请业务，查看审批意见时只传cus_id-------------strat----------------------*/
			if((context.containsKey("cus_id") && (!context.containsKey("limit_acc_no")) && (!context.containsKey("limit_credit_no")) )){
				cus_id = (String)context.getDataValue("cus_id");
				condition3 = "where cus_id='"+cus_id+"' and agr_status ='002' ";
				IndexedCollection iColl = dao.queryList(modelIdLmt, condition3, connection);
				if(iColl.size()>0){
					KeyedCollection kColl = (KeyedCollection)iColl.get(0);
					condition = "where pk_value='"+kColl.getDataValue("serno")+"'";
				}
			/**--------------------------------end---------------------*/
			}else if((context.containsKey("cus_id") && (context.containsKey("limit_ind")) && (context.containsKey("limit_acc_no")) && (context.containsKey("limit_credit_no")) )){
				cus_id = (String)context.getDataValue("cus_id");
				limit_ind = (String)context.getDataValue("limit_ind"); 
				limit_acc_no = (String)context.getDataValue("limit_acc_no");//授信台帐编号
				limit_credit_no = (String)context.getDataValue("limit_credit_no"); //第三方授信编号
				
				KeyedCollection kColl = dao.queryDetail(modelIdCus, cus_id, connection);
				String cus_line = (String)kColl.getDataValue("belg_line");
				
				CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
				LmtServiceInterface lmtServiceInterface = (LmtServiceInterface)serviceJndi.getModualServiceById("lmtServices", "lmt");
				if("2".equals(limit_ind) || "3".equals(limit_ind)){
				  	String serno = lmtServiceInterface.getSernoForIqpAcc(cus_line, limit_acc_no, connection);
				  	condition = "where pk_value='"+serno+"'";
				}else if("4".equals(limit_ind)){
					String serno = lmtServiceInterface.getSernoForIqpCredit(limit_credit_no, connection);
					condition2 = "where pk_value='"+serno+"'";
				}else if("5".equals(limit_ind) || "6".equals(limit_ind)){
					String sernoAcc = lmtServiceInterface.getSernoForIqpAcc(cus_line, limit_acc_no, connection);
				  	condition = "where pk_value='"+sernoAcc+"'";
				  	String sernoCredit = lmtServiceInterface.getSernoForIqpCredit(limit_credit_no, connection);
					condition2 = "where pk_value='"+sernoCredit+"'";
				}	
			}
			/**业务申请和出账申请分别为两个不同的流水号*/
			String condition1 = "where pk_value='"+serno_value+"'";
			
			/**查询流程实例号
			 * 首先查询待办流程列表，如果无结果则去查询办结流程列表，如果无结果不作处理
			 * */
			instanceId = this.getWfi(condition1, dao, connection);
			instanceId1 = this.getWfi(condition, dao, connection);
			instanceId2 = this.getWfi(condition2, dao, connection);
			context.addDataField("instanceId", instanceId);
			context.addDataField("instanceId1", instanceId1);
			context.addDataField("instanceId2", instanceId2);
 			
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
	
	public String getWfi(String condition1,TableModelDAO dao,Connection connection) throws Exception{
		String instanceId = null;
		IndexedCollection icoll = dao.queryList(modelId, condition1, connection);
		if(icoll.size()>0){
			KeyedCollection kColl = (KeyedCollection)icoll.get(0);
			instanceId = (String)kColl.getDataValue("instanceid");
		}else{
			IndexedCollection icollEnd = dao.queryList(modelIdEnd, condition1, connection);
			if(icollEnd.size()>0){
				KeyedCollection kColl = (KeyedCollection)icollEnd.get(0);
				instanceId = (String)kColl.getDataValue("instanceid");
			}
		}
	   return instanceId;	
	}
}
