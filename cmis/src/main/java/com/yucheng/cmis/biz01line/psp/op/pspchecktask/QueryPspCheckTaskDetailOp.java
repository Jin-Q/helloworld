package com.yucheng.cmis.biz01line.psp.op.pspchecktask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryPspCheckTaskDetailOp  extends CMISOperation {
	
	private final String modelId = "PspCheckTask";
	

	private final String task_id_name = "task_id";
	
	
	private boolean updateCheck = true;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String task_id_value = null;
			String task_type = null;
			String cus_id = null;
			try {
				task_id_value = (String)context.getDataValue(task_id_name);
			} catch (Exception e) {}
			if(task_id_value == null || task_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+task_id_name+"] cannot be null!");

		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, task_id_value, connection);
			this.putDataElement2Context(kColl, context);
			
			cus_id = (String)kColl.getDataValue("cus_id");
			task_type = (String)kColl.getDataValue("task_type");
			if("01".equals(task_type)||"02".equals(task_type)){
				String condition = " where cus_id='"+cus_id+"' and prd_id in('100058','100057','100061')";
				IndexedCollection iColl = dao.queryList("AccLoan", condition, connection);
				if(iColl.size()>0){
					context.put("proFlag", "Y");
				}
			}
			/**add by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能  begin**/
			if(task_type!=null && !"".equals(task_type) &&(task_type.equals("07") || task_type.equals("08"))){
				IndexedCollection temp = dao.queryList("PspBatchTaskRel", "where sub_task_id='"+task_id_value+"'", connection);
				if(temp!=null && temp.size()>0){
					context.put("batchTaskRel", "Y");
				}
			}
			/**add by lisj 2015-1-15  需求编号：【XD150108001】贷后管理系统需求（房地产、汽车、小微、个人经营性100万元以下批量功能  end**/
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"manager_id","input_id","task_divis_person","task_huser"});
			SInfoUtils.addSOrgName(kColl, new String[]{"manager_br_id","input_br_id","task_divis_org","task_horg"});

			String[] args=new String[] {"cus_id","grp_no" };
			String[] modelIds=new String[]{"CusBase","CusGrpInfo"};
			String[]modelForeign=new String[]{"cus_id","grp_no"};
			String[] fieldName=new String[]{"cus_name","grp_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
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
