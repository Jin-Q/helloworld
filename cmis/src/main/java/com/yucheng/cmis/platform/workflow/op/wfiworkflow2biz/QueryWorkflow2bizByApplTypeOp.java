package com.yucheng.cmis.platform.workflow.op.wfiworkflow2biz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.util.TableModelUtil;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryWorkflow2bizByApplTypeOp extends CMISOperation {

private final String modelId = "WfiWorkflow2biz";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String applType = (String) context.getDataValue("appl_type");
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {
				queryData = new KeyedCollection(modelId);
			}
			queryData.put("appl_type", applType);			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+"order by appl_type";
			
			int size = 10;
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("appl_type");
			list.add("wfsign");
			list.add("wfname");
			list.add("scene_scope");
			list.add("pk1");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,connection);
			iColl.setName(modelId+"List");
			//去掉重复的记录
			IndexedCollection icollResult = new IndexedCollection(modelId+"List");
			Map<String, String> map = new HashMap<String, String>();
			for(int i=0; i<iColl.size(); i++) {
				KeyedCollection kcoll = (KeyedCollection) iColl.get(i);
				String applTypeTmp = (String) kcoll.getDataValue("appl_type");
				String wfSignTmp = (String) kcoll.getDataValue("wfsign");
				if(wfSignTmp.equals(map.get(applTypeTmp))) {
					iColl.remove(kcoll);
				} else {
					map.put(applTypeTmp, wfSignTmp);
				}
			}
			
			this.putDataElement2Context(iColl, context);

			
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
