package com.yucheng.cmis.biz01line.arp.op.arpbondreducdetail;

import java.sql.Connection;

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

public class QueryArpBondReducDetailListOp extends CMISOperation {

	private final String modelId = "ArpBondReducDetail";

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
			String serno = context.getDataValue("serno").toString();
			if(conditionStr.indexOf("WHERE") != -1){				
				conditionStr = conditionStr + " and serno = '"+serno+"' "  + " order by pk_serno desc";
			}else{
				conditionStr = "where 1=1 " + " and serno = '"+serno+"' "  + " order by pk_serno desc";
			}
			
			int size = 10;		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));			
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			
			String[] args = new String[] { "bill_no","bill_no","bill_no","bill_no","prd_id" ,"bill_no","bill_no","bill_no"};
			String[] modelIds = new String[] { "AccLoan","AccLoan","AccLoan","AccLoan","PrdBasicinfo" ,"AccLoan","AccLoan","AccLoan"};
			String[] modelForeign = new String[] { "bill_no","bill_no","bill_no","bill_no","prdid" ,"bill_no","bill_no","bill_no"};
			String[] fieldName = new String[] { "prd_id","loan_amt","loan_balance","cont_no","prdname" ,"out_owe_int","inner_owe_int","cur_type"};
			String[] resultName = new String[] { "prd_id","loan_amt","loan_balance","cont_no","prd_type" ,"out_owe_int","inner_owe_int","cur_type"};
		    SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);
		    
			/*** 外币翻译begin ***/
			CheckAssetPreserveOp cmisOp = new CheckAssetPreserveOp();
			cmisOp.delIcollCurType(iColl, "cur_type", "loan_amt", context);	//借据金额
			cmisOp.delIcollCurType(iColl, "cur_type", "loan_balance", context);	//借据余额
			cmisOp.delIcollCurType(iColl, "cur_type", "inner_owe_int", context);	//表内欠息
			cmisOp.delIcollCurType(iColl, "cur_type", "out_owe_int", context);	//表外欠息
			/*** 外币翻译end ***/
			
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