package com.yucheng.cmis.biz01line.iqp.op.iqpguarantchangeapp;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

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

public class QueryIqpGuarantChangeAppListOp extends CMISOperation {


	private final String modelId = "IqpGuarantChangeApp";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
		
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, false, false);
			RecordRestrict recordRestrict = this.getRecordRestrict(context); 
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
//			if(conditionStr !=null && !"".equals(conditionStr)){
//				conditionStr += " and approve_status not in('997','111') order by serno desc";
//			}else{
//				conditionStr = "where approve_status not in('997','111') order by serno desc"; 
//			}   
			int size = 15; 
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("serno"); 
			list.add("cus_id");
			list.add("assure_main");
			list.add("cont_cur_type");
			list.add("cont_amt");
			list.add("new_cont_amt");
			list.add("apply_date");
			list.add("input_id");
			list.add("input_br_id");
			list.add("approve_status");
			list.add("end_date");
			/**add by lisj 2014年11月14日 增加prd_id 字段 begin**/
			list.add("prd_id");
			/**add by lisj 2014年11月14日 增加prd_id 字段 end**/
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			String[] args=new String[] { "cus_id","bill_no","cont_no","bill_no"};
			String[] modelIds=new String[]{"CusBase","AccLoan","CtrLoanCont","AccLoan"};
			String[] modelForeign=new String[]{"cus_id","bill_no","cont_no","bill_no"};
			String[] fieldName=new String[]{"cus_name","cont_no","serno","prd_id"};
			String[] resultName = new String[] { "cus_id_displayname","cont_no","fount_serno","prd_id"};
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
		    /** 组织机构、登记机构翻译 */
			SInfoUtils.addUSerName(iColl, new String[]{"input_id"});
			SInfoUtils.addSOrgName(iColl, new String[]{"input_br_id"}); 
			
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
