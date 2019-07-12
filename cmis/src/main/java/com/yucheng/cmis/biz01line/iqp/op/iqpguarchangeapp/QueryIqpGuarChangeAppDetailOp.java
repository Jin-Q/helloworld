package com.yucheng.cmis.biz01line.iqp.op.iqpguarchangeapp;

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

public class QueryIqpGuarChangeAppDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpGuarChangeApp";
	private final String modelIdStar = "WfiWorklistTodo";
	private final String modelIdEnd = "WfiWorklistEnd";

	private final String serno_name = "serno";
	
	
	private boolean updateCheck = true;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeQueryRestrict(this.modelId, null, context, connection);
			}
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			
			/**查询流程实例号
			 * 首先查询待办流程列表，如果无结果则去查询办结流程列表，如果无结果不作处理
			 * */
			String instanceId = "";
			String condition = "where pk_value='"+serno_value+"'";
			IndexedCollection icollWfi = dao.queryList(modelIdStar, condition, connection);
			if(icollWfi.size()>0){
				KeyedCollection kCollWfi = (KeyedCollection)icollWfi.get(0);
				instanceId = (String)kCollWfi.getDataValue("instanceid");
			}else{
				IndexedCollection icollWfiEnd = dao.queryList(modelIdEnd, condition, connection);
				if(icollWfiEnd.size()>0){
					KeyedCollection kCollWfiEnd = (KeyedCollection)icollWfiEnd.get(0);
					instanceId = (String)kCollWfiEnd.getDataValue("instanceid");
				}
			} 
			context.addDataField("instanceIdPvp", instanceId);
			
			String[] args=new String[] {"cus_id","prd_id" };
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
			String[]modelForeign=new String[]{"cus_id","prdid"}; 
			String[] fieldName=new String[]{"cus_name","prdname"};    
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
		    /** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(kColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(kColl, new String[]{"input_br_id","manager_br_id"}); 
			
			this.putDataElement2Context(kColl, context);
			
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
