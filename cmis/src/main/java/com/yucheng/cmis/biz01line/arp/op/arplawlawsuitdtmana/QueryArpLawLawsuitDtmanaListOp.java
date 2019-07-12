package com.yucheng.cmis.biz01line.arp.op.arplawlawsuitdtmana;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.arp.op.pubAction.CheckAssetPreserveOp;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryArpLawLawsuitDtmanaListOp extends CMISOperation {

	private final String modelId = "ArpLawLawsuitDtmana";	

	public String doExecute(Context context) throws EMPException {		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}		
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);
			
			String case_no = context.getDataValue("case_no").toString();
			if(conditionStr.indexOf("WHERE") != -1){
				conditionStr = conditionStr + " and case_no = '"+case_no+"' "  + " order by pk_serno desc";
			}else{
				conditionStr = "where 1=1 " + " and case_no = '"+case_no+"' "  + " order by pk_serno desc";
			}
			
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));		
			
			List<String> list = new ArrayList<String>();
			list.add("pk_serno");
			list.add("case_no");
			list.add("cus_id");
			list.add("bill_no");
			list.add("lawsuit_sub");
			list.add("lawsuit_cap");
			list.add("lawsuit_int");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			String[] args = new String[] { "bill_no","bill_no","bill_no","bill_no","cus_id","prd_id" ,"bill_no"};
			String[] modelIds = new String[] { "AccLoan","AccLoan","AccLoan","AccLoan","CusBase","PrdBasicinfo" ,"AccLoan"};
			String[] modelForeign = new String[] { "bill_no","bill_no","bill_no","bill_no","cus_id","prdid" ,"bill_no"};
			String[] fieldName = new String[] { "prd_id","loan_amt","loan_balance","cont_no","cus_name","prdname" ,"cur_type"};
			String[] resultName = new String[] { "prd_id","loan_amt","loan_balance","cont_no","cus_name","prd_type" ,"cur_type"};
		    SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);
		    
			/*** 外币翻译begin ***/
			CheckAssetPreserveOp cmisOp = new CheckAssetPreserveOp();
			cmisOp.delIcollCurType(iColl, "cur_type", "loan_amt", context);	//借据金额
			cmisOp.delIcollCurType(iColl, "cur_type", "loan_balance", context);	//借据余额
			/*** 外币翻译end ***/
		    
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