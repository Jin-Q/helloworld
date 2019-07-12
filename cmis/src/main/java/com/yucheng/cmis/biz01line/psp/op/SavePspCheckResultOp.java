package com.yucheng.cmis.biz01line.psp.op;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class SavePspCheckResultOp extends CMISOperation {
	private static final String modelId = "PspCheckItemResult";
	@Override
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			
			String taskId = (String)context.getDataValue("task_id");
			String schemeId = (String)context.getDataValue("scheme_id");
			for(int i=0;i<kColl.size();i++){
				DataElement element = (DataElement) kColl.getDataElement(i);
				if (element instanceof DataField) {
					DataField aField = (DataField) element;
					KeyedCollection kc = new KeyedCollection(modelId);
					kc.addDataField("task_id", taskId);
					kc.addDataField("scheme_id", schemeId);
					kc.addDataField("item_id", aField.getName());
					kc.addDataField("check_result", aField.getValue());
					
					if(aField.getName().indexOf("_displayname") == -1){
						Map param = new HashMap();
						param.put("task_id", taskId);
						param.put("scheme_id", schemeId);
						param.put("item_id", aField.getName());
						KeyedCollection itemResKColl = dao.queryDetail(modelId, param, connection);
						String itemHelp = (String)itemResKColl.getDataValue("item_id");
						if(itemHelp != null){
							dao.update(kc, connection);
						}else {
							dao.insert(kc, connection);
						}
					}
				}
			}
			/**modified by yezm 2015-7-29 需求编号：XD150625045 联保授信业务常规检查的担保分析需求 ,担保分析start**/
			String cusId="";
			if(context.containsKey("guar_cus_id")){
				cusId = (String)context.getDataValue("guar_cus_id");
			}else{
				cusId = (String)context.getDataValue("cus_id");
			}
			
			String 	conditionStr = " where cus_id = '"+cusId+"' and grt_type = '3' and task_id not in " +
					                 "(select task_id from psp_check_task where approve_status = '997')  " +
					                 "and task_id in (select p2.task_id from psp_check_task p1,psp_check_task p2 " +
					                 "where p1.manager_id = p2.manager_id and p1.task_id = " +
					                 "replace('"+taskId+"','"+cusId+"',''))and task_id <> replace('"+taskId+"','"+cusId+"','')";
			//System.out.println(conditionStr);
			IndexedCollection iColl = dao.queryList("PspGuarAnalyRel",conditionStr, connection);
			//System.out.println(iColl.size());
			for (int j=0;j<iColl.size();j++) {
				KeyedCollection kColl1 = (KeyedCollection) iColl.get(j);
				String taskId1 = (String) kColl1.getDataValue("task_id");
				       taskId1 += (String)kColl1.getDataValue("cus_id");
				       for(int i=0;i<kColl.size();i++){
							DataElement element = (DataElement) kColl.getDataElement(i);
							if (element instanceof DataField) {
								DataField aField = (DataField) element;
								KeyedCollection kc = new KeyedCollection(modelId);
								kc.addDataField("task_id", taskId1);
								kc.addDataField("scheme_id", schemeId);
								kc.addDataField("item_id", aField.getName());
								kc.addDataField("check_result", aField.getValue());
								
								if(aField.getName().indexOf("_displayname") == -1){
									Map param = new HashMap();
									param.put("task_id", taskId1);
									param.put("scheme_id", schemeId);
									param.put("item_id", aField.getName());
									KeyedCollection itemResKColl = dao.queryDetail(modelId, param, connection);
									String itemHelp = (String)itemResKColl.getDataValue("item_id");
									if(itemHelp != null){
										dao.update(kc, connection);
									}else {
										dao.insert(kc, connection);
										}
									}
								}
							}
				       }
			/**modified by yezm 2015-7-29 需求编号：XD150625045 联保授信业务常规检查的担保分析需求 ,担保分析end**/
			context.addDataField("flag", "success");
		} catch (Exception e) {
			throw new EMPException(e.getMessage());
		} finally {
			this.releaseConnection(context, connection);
		}
		
		return null;
	}

}
