package com.yucheng.cmis.biz01line.cus.op.cusevent;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusEventListOp extends CMISOperation {

	private final String modelId = "CusEvent";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String editFlag = "";
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}
			
			editFlag = (String)context.getDataValue("EditFlag");//修改标志
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false);
//									+"order by serno desc,cus_id desc";
			if("query".equals(editFlag)){
				if(conditionStr!=null&&!"".equals(conditionStr)){
					conditionStr = conditionStr + " and status<>'0' order by serno desc,cus_id desc";
				}else{
					conditionStr = " where status<>'0' order by serno desc,cus_id desc";
				}
			}
			
			int size = 15;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("event_dt");
			list.add("event_typ");
			list.add("serno");
			list.add("event_imp_deg");
			list.add("event_bch_name");
			list.add("event_bank_flg");
			list.add("input_id");
			list.add("input_date");
			list.add("event_classify");
			list.add("status");
			list.add("cus_id");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
//			SInfoUtils.addSOrgName(iColl, new String[] { "event_bch_name" });
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
