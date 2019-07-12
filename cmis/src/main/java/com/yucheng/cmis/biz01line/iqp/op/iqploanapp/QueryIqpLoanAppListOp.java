package com.yucheng.cmis.biz01line.iqp.op.iqploanapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpLoanAppListOp extends CMISOperation {
	private final String modelId = "IqpLoanApp";
	public String doExecute(Context context) throws EMPException {
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		Connection connection = null;
		String conditionStr = "";
		try{
			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			String biz_type = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			/**取得业务模式*/
			if(context.containsKey("biz_type")){
				biz_type = (String)context.getDataValue("biz_type");  
			}
			
			conditionStr = TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			
//			if(conditionStr !=null && !("".equals(conditionStr)) && biz_type != null){
//				conditionStr += " and  biz_type='"+biz_type+"' order by serno desc";
//			}else if(biz_type != null){
//				conditionStr = "where biz_type='"+biz_type+"' order by serno desc"; 
//			}
			conditionStr += "order by serno desc";
			int size = 15; 
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
		    
			iColl.setName(iColl.getName()+"List");
			String[] args=new String[] {"cus_id","prd_id" };
			String[] modelIds=new String[]{"CusBase","PrdBasicinfo"};
			String[]modelForeign=new String[]{"cus_id","prdid"};
			String[] fieldName=new String[]{"cus_name","prdname"};
		    //详细信息翻译时调用			
		    SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName);
			
			
			/** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
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
