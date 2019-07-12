package com.yucheng.cmis.platform.workflow.op.wfiworkflow2biz;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.dbmodel.service.pkgenerator.UNIDGenerator;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class AddWfiWorkflow2bizRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "WfiWorkflow2biz";
	
	/**
	 * bussiness logic operation
	 */
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
			KeyedCollection kcollRet = new KeyedCollection("result");
			//根据申请类型+流程标识+配置适用范围校验配置唯一性
			String applType = (String) kColl.getDataValue("appl_type");
			String wfSign = (String) kColl.getDataValue("wfsign");
			String sceneScope = (String) kColl.getDataValue("scene_scope");
			String condition = "WHERE APPL_TYPE='"+applType+"' AND WFSIGN='"+wfSign+"' AND SCENE_SCOPE='"+sceneScope+"' ";
			IndexedCollection icoll = dao.queryList(modelId, condition, connection);
			if(icoll!=null && icoll.size()>0) {
				kcollRet.put("pk1", "");
				kcollRet.put("flag", "根据申请类型+流程标识+配置适用范围校验唯一性不通过！");
			} else {
				UNIDGenerator generator = new UNIDGenerator();
				String pk = generator.getUNID();
				kColl.put("pk1", pk);
				//add a record
				int count = dao.insert(kColl, connection);
				kcollRet.put("flag", count);
				kcollRet.put("pk1", pk);
			}
			this.putDataElement2Context(kcollRet, context);
			
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
