package com.yucheng.cmis.biz01line.iqp.op.iqpchkstoretask;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.platform.organization.domains.SUser;
import com.yucheng.cmis.platform.organization.msi.OrganizationServiceInterface;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddIqpChkStoreTaskRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpChkStoreTask";
	private final String setModelId = "IqpChkStoreSet";
	private final String agrModelId = "IqpOverseeAgr";
	private final String recModelId = "IqpChkStoreTaskRecord";
	private final String gageModelId = "IqpChkStoreGageRecord";
	private final String reModelId = "IqpCargoOverseeRe";
	private final String mortModelId = "MortCargoPledge";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = "success";
		try{
			connection = this.getConnection(context);
			
			/**
			 * 逻辑稍微多了点
			 * 1、根据监管协议生成任务
			 * 2、每个任务对应核/巡库记录
			 * 3、每个任务的监管协议下，货物清单信息生成核/巡库押品记录
			 * */
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(setModelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+setModelId+"] cannot be empty!");
			
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			
			String task_set_type = (String) kColl.getDataValue("task_set_type");
			String cus_id = (String) kColl.getDataValue("cus_id");
			String conditionStr = "";
			if(task_set_type.equals("01")){//出质人
				conditionStr = " where mortgagor_id = '"+cus_id+"'and status='1'";
			}else if(task_set_type.equals("02")){//监管企业
				conditionStr = " where oversee_con_id = '"+cus_id+"'and status='1'";
			}
			
			//获取所有监管协议
			IndexedCollection agrIColl = dao.queryList(agrModelId, conditionStr, connection);
			
			if(agrIColl!=null&&agrIColl.size()>0){
				//1根据监管协议生成任务
				for(int i=0;i<agrIColl.size();i++){
					KeyedCollection agrKColl = (KeyedCollection) agrIColl.get(i);
					String oversee_agr_no = (String) agrKColl.getDataValue("oversee_agr_no");
					KeyedCollection taskKColl = new KeyedCollection();
					taskKColl.addDataField("task_set_type", task_set_type);
					taskKColl.addDataField("cus_id", cus_id);
					taskKColl.addDataField("task_request_time", kColl.getDataValue("task_request_time"));
					taskKColl.addDataField("oversee_agr_no",oversee_agr_no);
					taskKColl.addDataField("prc_status", "1");//未处理
					taskKColl.addDataField("manager_id", kColl.getDataValue("task_exe_user"));
					
					CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
					OrganizationServiceInterface service = (OrganizationServiceInterface) serviceJndi.getModualServiceById("organizationServices", "organization");
					SUser suser = service.getUserByUserId((String)kColl.getDataValue("task_exe_user"),connection);
					
					String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
					
					taskKColl.addDataField("input_id", kColl.getDataValue("input_id")+"");
					taskKColl.addDataField("input_br_id", kColl.getDataValue("input_br_id")+"");
					taskKColl.addDataField("manager_br_id", suser.getOrgid());
					taskKColl.addDataField("approve_status", "000");
					taskKColl.addDataField("input_date", context.getDataValue("OPENDAY"));
					taskKColl.addDataField("task_id", serno);
					taskKColl.setName(modelId);
					dao.insert(taskKColl, connection);
					
					//2核/巡库记录
					KeyedCollection recKColl = new KeyedCollection();
					recKColl.addDataField("task_id", serno);
					recKColl.addDataField("oversee_org_id", agrKColl.getDataValue("oversee_con_id"));
					recKColl.addDataField("cus_id", cus_id);
					recKColl.setName(recModelId);
					dao.insert(recKColl, connection);
					
					//3生成核/巡库押品记录
					String reCondition = " where agr_type = '02' and agr_no = '"+oversee_agr_no+"'";
					IndexedCollection reIColl = dao.queryList(reModelId, reCondition, connection);
					for(int j=0;j<reIColl.size();j++){
						KeyedCollection reKColl = (KeyedCollection) reIColl.get(j);
						String guaranty_no = (String) reKColl.getDataValue("guaranty_no");
						
						String mortCondition = " where guaranty_no = '"+guaranty_no+"'";
						IndexedCollection mortIColl = dao.queryList(mortModelId, mortCondition, connection);
						for(int k=0;k<mortIColl.size();k++){
							KeyedCollection mortKColl = (KeyedCollection) mortIColl.get(k);
							
							KeyedCollection gageKColl = new KeyedCollection();
							gageKColl.addDataField("task_id", serno);
							gageKColl.addDataField("catalog_no", mortKColl.getDataValue("cargo_id"));
							gageKColl.addDataField("cus_id", cus_id);
							gageKColl.addDataField("unit_price", mortKColl.getDataValue("identy_unit_price"));
							gageKColl.addDataField("qnt", mortKColl.getDataValue("qnt"));
							gageKColl.addDataField("value", mortKColl.getDataValue("identy_total"));
							gageKColl.setName(gageModelId);
							dao.insert(gageKColl, connection);
						}
					}
				}
				//更新状态
				kColl.put("is_task", "1");
				dao.update(kColl, connection);
			
			}else{
				flag = "failure";
			}
		}catch (EMPException ee) {
			flag = "error";
			throw ee;
		} catch(Exception e){
			flag = "error";
			throw new EMPException(e);
		} finally {
			context.addDataField("flag", flag);
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
