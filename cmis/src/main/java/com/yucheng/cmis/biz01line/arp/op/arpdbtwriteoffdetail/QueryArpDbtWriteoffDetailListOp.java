package com.yucheng.cmis.biz01line.arp.op.arpdbtwriteoffdetail;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryArpDbtWriteoffDetailListOp extends CMISOperation {


	private final String modelId = "ArpDbtWriteoffDetail";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);

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
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			String[] args = new String[] { "bill_no","prd_id" };
			String[] modelIds = new String[] { "AccLoan","PrdBasicinfo" };
			String[] modelForeign = new String[] { "bill_no","prdid" };
			String[] fieldName = new String[] { "prd_id","prdname" };
			String[] resultName = new String[] { "prd_id","prd_type" };
		    SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds,modelForeign, fieldName,resultName);
			
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