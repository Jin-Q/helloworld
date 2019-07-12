package com.yucheng.cmis.biz01line.iqp.op.iqpchkstoreset;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpOverseeAgrTabForChkStoreListOp extends CMISOperation {

	private final String modelId = "IqpOverseeAgr";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String task_set_type = (String) context.getDataValue("task_set_type");
			String cus_id = (String) context.getDataValue("cus_id");
			
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
			
			if(task_set_type.equals("01")){//出质人
				if(conditionStr==null||conditionStr.equals("")){
					conditionStr = " where mortgagor_id = '"+cus_id+"'and status='1'";
				}else{
					conditionStr = conditionStr + " and mortgagor_id = '"+cus_id+"'and status='1'";
				}
			}else if(task_set_type.equals("02")){//监管企业
				if(conditionStr==null||conditionStr.equals("")){
					conditionStr = " where oversee_con_id = '"+cus_id+"'and status='1'";
				}else{
					conditionStr = conditionStr + " and oversee_con_id = '"+cus_id+"'and status='1'";
				}
			}
			
			int size = 10; 
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"});
			
			String[] args=new String[] {"mortgagor_id","oversee_con_id" };
			String[] modelIds=new String[]{"CusBase","CusBase"};
			String[]modelForeign=new String[]{"cus_id","cus_id"};
			String[] fieldName=new String[]{"cus_name","cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
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
