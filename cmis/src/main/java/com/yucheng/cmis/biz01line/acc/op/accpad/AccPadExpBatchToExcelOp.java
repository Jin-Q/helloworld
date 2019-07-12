package com.yucheng.cmis.biz01line.acc.op.accpad;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 
 * 垫款台账生成excel
 *
 */
public class AccPadExpBatchToExcelOp extends CMISOperation {
	
	private final String modelId = "AccPad";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			
			conditionStr+=" order by serno desc";

			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("cont_no");
			list.add("bill_no");
			list.add("cus_id");
			list.add("pad_type");
			list.add("pad_cur_type");
			list.add("accp_status");
			list.add("pad_amt");
			list.add("pad_bal");
			list.add("five_class");
			list.add("manager_br_id");
			list.add("serno");
			
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,connection);
			iColl.setName(iColl.getName()+"List");
			/**添加客户条线字段   2014-08-07    邓亚辉*/
			String[] args=new String[] { "cus_id","cus_id","cont_no","bill_no"};
			String[] modelIds=new String[]{"CusBase","CusBase","CtrLoanCont","AccLoan"};
			String[] modelForeign=new String[]{"cus_id","cus_id","cont_no","bill_no"};
			String[] fieldName=new String[]{"cus_name","belg_line","serno","prd_id"};
			String[] resultName = new String[] { "cus_id_displayname","belg_line","fount_serno","prd_id"};
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});
			
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
