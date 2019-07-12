package com.yucheng.cmis.biz01line.cus.op.goverfin.goverfinterbill;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusGoverFinTerBillListOp extends CMISOperation {


	private final String modelId = "CusGoverFinTerBill";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);			
		
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);
			String cus_id = (String) context.getDataValue("cus_id");
			
			if(conditionStr.indexOf("WHERE") != -1){				
				conditionStr = conditionStr + " and cus_id = '" + cus_id+ "'  order by serno desc";
			}else{
				conditionStr = "where 1=1 " + " and cus_id = '" + cus_id+ "'  order by serno desc";
			}
			
			int size = 10;		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("cus_id");
			list.add("bill_no");
			list.add("loan_amount");
			list.add("loan_distr_term");
			list.add("repayment_mode");
			list.add("rfn_ori");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			String[] args=new String[] { "cus_id","repayment_mode" };
			String[] modelIds=new String[]{"CusBase","PrdRepayMode"};
			String[] modelForeign=new String[]{"cus_id","repay_mode_id"};
			String[] fieldName=new String[]{"cus_name","repay_mode_dec"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
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
