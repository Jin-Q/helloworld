package com.yucheng.cmis.biz01line.iqp.op.iqpbilldetail;

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

public class QueryImportIqpBillListOp extends CMISOperation {
	
	private final String billModel = "IqpBillDetail";
	/**
	 * 查询可导入的操作记录，可导入数据显示规则如下：
	 * 1.该笔汇票票据关联表中
	 * 2.该笔汇票在批次中，批次状态为失效，可选择
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(billModel);
			} catch (Exception e) {}
			String conditionStr = TableModelUtil.getQueryCondition(billModel, queryData, context, false, false, false);
			
			IndexedCollection relIColl = null;
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			if(conditionStr==null || conditionStr.length() == 0){
				conditionStr = " where porder_no not in (select distinct(porder_no) from iqp_batch_bill_rel)";
			}else {
				conditionStr += " and porder_no not in (select distinct(porder_no) from iqp_batch_bill_rel)";
			}
			
			/**
			String conditionStr = " where porder_no not in (select distinct(porder_no) from iqp_batch_bill_rel) or " +
					"porder_no in (select distinct(t1.porder_no) " +
					"from iqp_batch_bill_rel t1,iqp_batch_mng t2 where t1.batch_no=t2.batch_no and t2.status='')";
			*/
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			List list = new ArrayList();
			list.add("porder_no");
			list.add("bill_isse_date");
			list.add("porder_end_date");
			list.add("drft_amt");
			list.add("isse_name");
			list.add("pyee_name");
			list.add("aorg_name");
			list.add("aorg_no");
			list.add("aorg_type");
			list.add("status");
			IndexedCollection iColl = dao.queryList(billModel,list ,conditionStr,pageInfo,connection);
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
