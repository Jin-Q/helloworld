package com.yucheng.cmis.biz01line.prd.op.prdbankinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryPrdBankInfoPopListOp extends CMISOperation {


	private final String modelId = "PrdBankInfo";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String flag = "";
			String conditionStr = TableModelUtil.getQueryCondition_bak(modelId, queryData, context, false, true, false);
			if(context.containsKey("flag")&&context.getDataValue("flag")!=null&&!"".equals(context.getDataValue("flag"))){
				flag = context.getDataValue("flag").toString();
				if("add".equals(flag)){
					if(conditionStr!=null&&!"".equals(conditionStr)){
						conditionStr += " and not exists (select 1 from cus_same_org where prd_bank_info.bank_no = cus_same_org.same_org_no and cus_same_org.same_org_type is not null) ";
					}else{
						conditionStr += " where not exists (select 1 from cus_same_org where prd_bank_info.bank_no = cus_same_org.same_org_no and cus_same_org.same_org_type is not null) ";
					}
				}
			}
			/*modified by wangj 需求编号：ED150612003 ODS系统取数需求  begin*/
			String status="";
			if(context.containsKey("status")&&context.getDataValue("status")!=null&&!"".equals(context.getDataValue("status"))){
				status = context.getDataValue("status").toString();
				if(conditionStr!=null&&!"".equals(conditionStr)){
					conditionStr += "  and prd_bank_info.status='"+status+"'";
				}else{
					conditionStr += " where prd_bank_info.status='"+status+"'";
				}
			}
			/*modified by wangj 需求编号：ED150612003 ODS系统取数需求  end*/
			
//			RecordRestrict recordRestrict = this.getRecordRestrict(context);
//			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
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
