package com.yucheng.cmis.biz01line.arp.op.arpbadassethandoverrcv;

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
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryArpBadassetHandoverRcvListOp extends CMISOperation {


	private final String modelId = "ArpBadassetHandoverRcv";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+"order by serno desc";			
			int size = 10;		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);

			List<String> list = new ArrayList<String>();
			list.add("serno");
			list.add("bill_no");
			list.add("handover_resn");
			list.add("fount_manager_id");
			list.add("rcv_person");
			list.add("rcv_status");
			IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			
			String[] args = new String[] { "bill_no","bill_no","bill_no","bill_no","bill_no","cus_id","prd_id" ,"bill_no"};
			String[] modelIds = new String[] { "AccLoan","AccLoan","AccLoan","AccLoan","AccLoan","CusBase","PrdBasicinfo" ,"AccLoan"};
			String[] modelForeign = new String[] { "bill_no","bill_no","bill_no","bill_no","bill_no","cus_id","prdid" ,"bill_no"};
			String[] fieldName = new String[] { "cus_id","prd_id","loan_amt","loan_balance","five_class","cus_name","prdname" ,"cur_type"};
			String[] resultName = new String[] { "cus_id","prd_id","loan_amt","loan_balance","five_class","cus_name","prd_type" ,"cur_type"};
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			SInfoUtils.addUSerName(iColl, new String[] {"fount_manager_id","rcv_person" });
			
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