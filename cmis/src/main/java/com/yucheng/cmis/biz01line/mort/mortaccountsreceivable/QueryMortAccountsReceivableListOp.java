package com.yucheng.cmis.biz01line.mort.mortaccountsreceivable;

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
import com.yucheng.cmis.util.TableModelUtil;

public class QueryMortAccountsReceivableListOp extends CMISOperation {


	private final String modelId = "MortAccountsReceivable";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String guaranty_no="";
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, false, false);
			
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List list = new ArrayList();
			list.add("guaranty_no");
			list.add("debt_id");
			list.add("buy_cus_name");
			list.add("sel_cus_name");
			list.add("cont_no");
			list.add("invc_no");
			list.add("invc_amt");
			list.add("invc_date");
			list.add("bond_pay_date");
			list.add("status");
			IndexedCollection iColl =null;
			if(context.containsKey("guaranty_no")){//根据押品编号获得其相应的货物信息
				guaranty_no = (String) context.getDataValue("guaranty_no");
				if(conditionStr !=null && !"".equals(conditionStr)){
					conditionStr += "and guaranty_no='"+guaranty_no+"'order by debt_id desc";
				}else{
					conditionStr = "where guaranty_no='"+guaranty_no+"'order by debt_id desc";
				}
				context.setDataValue("guaranty_no", guaranty_no);
				iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			}
		//	IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
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
