package com.yucheng.cmis.biz01line.psp.op.pspsitecheckcom;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
//贷后管理系统改造（常规检查）    XD141222090     modefied by zhaoxp  2015-01-22 
public class UpdatePspSitecheckComRecordOpFortc extends CMISOperation {
	

	private final String modelId = "PspSitecheckCom";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			TableModelDAO dao = this.getTableModelDAO(context);
			String pk_id = (String)kColl.getDataValue("pk_id");
			if(pk_id==null ||"".equals(pk_id)){
				dao.insert(kColl, connection);
			}else{
				int count=dao.update(kColl, connection);
				if(count!=1){
					throw new EMPException("Update Failed! Record Count: " + count);
				}
			}
			/**modified by yezm 2015-7-29 需求编号：XD150625045 联保授信业务常规检查的担保分析需求 ,现场检查start**/
			String cusId = (String)kColl.getDataValue("cus_id");
			String taskId = (String)kColl.getDataValue("task_id");
			String visitType = (String)kColl.getDataValue("visit_type");
			String 	conditionStr = " where cus_id = '"+cusId+"' and grt_type = '3' and task_id not in " +
						            "(select task_id from psp_check_task where approve_status = '997')  " +
						            "and task_id in (select p2.task_id from psp_check_task p1,psp_check_task p2 " +
						            "where p1.manager_id = p2.manager_id and p1.task_id = '"+taskId+"')and task_id <> '"+taskId+"'";
			//System.out.println(conditionStr);
			IndexedCollection iColl = dao.queryList("PspGuarAnalyRel",conditionStr, connection);
			//System.out.println(iColl.size());
			for (int i=0; i<iColl.size();i++) {
				KeyedCollection kColl1 = (KeyedCollection) iColl.get(i);
				String taskId1 = (String) kColl1.getDataValue("task_id");
				KeyedCollection pspTaskKColl = dao.queryDetail("PspCheckTask", taskId1, connection);
				String 	condition = " where task_id = '"+taskId1+"' and visit_type='"+visitType+"' and cus_id='"+cusId+"'";
				IndexedCollection siteIColl = dao.queryList("PspSitecheckCom",condition, connection);
				IndexedCollection siteGrIColl = dao.queryList("PspSitecheckComGr",condition, connection);
				String taskType = (String) pspTaskKColl.getDataValue("task_type");
				kColl.put("task_id", taskId1);
				if("03".equals(taskType)){//03 个人客户	
					KeyedCollection kc = new KeyedCollection("PspSitecheckComGr");
					kc.putAll(kColl);
					kc.put("dbr_bgry", kColl.getDataValue("bgry_sum"));
					kc.put("dbr_bgcs", kColl.getDataValue("bgcs"));
					if(siteGrIColl.size()>0){
						for(int j=0; j<siteGrIColl.size(); j++){
							KeyedCollection siteGrKColl = (KeyedCollection)siteGrIColl.get(j) ;
							String pkId = (String) siteGrKColl.getDataValue("pk_id");
							kc.put("pk_id", pkId);
							dao.update(kc, connection);
							}
					}else{
						kc.put("pk_id", "");
						dao.insert(kc, connection);
					}
				}else {
					if(siteIColl.size()>0){
						for(int j=0; j<siteIColl.size(); j++){
							KeyedCollection siteKColl = (KeyedCollection)siteIColl.get(j) ;
							String pkId = (String) siteKColl.getDataValue("pk_id");
							kColl.put("pk_id", pkId);
							dao.update(kColl, connection);
							}
					}else{
						kColl.put("pk_id", "");
						dao.insert(kColl, connection);
					}
				}
				}
			/**modified by yezm 2015-7-29 需求编号：XD150625045 联保授信业务常规检查的担保分析需求 ,现场检查end**/
			context.addDataField("flag", "success");
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
