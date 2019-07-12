package com.yucheng.cmis.biz01line.iqp.op.iqpbilldetail;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryIqpBillDetailDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpBillDetail";
	private final String porder_no_name = "porder_no";
	private boolean updateCheck = false;

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String porder_no = null;
			String batch_no = "";
			String biz_type = "";
			try {
				porder_no = (String)context.getDataValue(porder_no_name);
				batch_no = (String)context.getDataValue("batch_no");
			} catch (Exception e) {}
			if(porder_no == null || porder_no.length() == 0)
				throw new EMPJDBCException("The value of pk["+porder_no_name+"] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			String serno = "";
			if(context.containsKey("serno")){
				serno = (String)context.getDataValue("serno");
				IndexedCollection ic = dao.queryList("IqpBatchMng", " where serno='"+serno+"'", connection);
				if(ic != null && ic.size() > 0){
					KeyedCollection kc = (KeyedCollection)ic.get(0);
					batch_no = (String)kc.getDataValue("batch_no");
					biz_type = (String)kc.getDataValue("biz_type");
					if(batch_no == null){
						batch_no = "";
					}
				}
			}
			if(context.containsKey("batch_no")){
				if(context.getDataValue("batch_no") == ""){
					context.setDataValue("batch_no", batch_no);
				}
				KeyedCollection kColl = dao.queryDetail("IqpBatchMng", batch_no, connection);
				biz_type = (String)kColl.getDataValue("biz_type");
			}else {
				context.addDataField("batch_no", batch_no);
				KeyedCollection kColl = dao.queryDetail("IqpBatchMng", batch_no, connection);
				biz_type = (String)kColl.getDataValue("biz_type");
			}
			/**判断被剔除的票是否为存量的票据（被用过），如果不是则删除票据明细表*/
			String condition4detail = " where batch_no in (select batch_no from iqp_batch_bill_rel where porder_no='"+porder_no+"') and status = '03' ";
			IndexedCollection detailIColl = dao.queryList("IqpBatchMng", null, condition4detail, connection);
			if(detailIColl!=null&&detailIColl.size()>0){
				context.put("isexists", "1");
			}
			context.put("biz_type", biz_type);
			KeyedCollection kColl = dao.queryDetail(modelId, porder_no, connection);
			this.putDataElement2Context(kColl, context);
			
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
