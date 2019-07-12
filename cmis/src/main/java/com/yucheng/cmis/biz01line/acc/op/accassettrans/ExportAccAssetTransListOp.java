package com.yucheng.cmis.biz01line.acc.op.accassettrans;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class ExportAccAssetTransListOp extends CMISOperation {


	private final String modelId = "AccAssetTrans";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String cusId = "";
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
				if(queryData.containsKey("cus_id")){
					cusId = (String) queryData.getDataValue("cus_id");
				}
			} catch (Exception e) {}
			
		
			String conditionStr = TableModelUtil.getQueryCondition(this.modelId, queryData, context, false, false, false)
									+"order by bill_no desc";
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			
			String sql = "";
			if(cusId!=null&&!"".equals(cusId)){
				sql = "select * from (select a.*,b.cus_id,b.loan_amt from acc_asset_trans a,acc_loan b where a.bill_no = b.bill_no and b.cus_id = '"+cusId+"') ";
			}else{
				sql = "select * from (select a.*,b.cus_id,b.loan_amt from acc_asset_trans a,acc_loan b where a.bill_no = b.bill_no) ";
			}
			sql = sql + conditionStr;
			
			IndexedCollection iColl = TableModelUtil.buildPageData(null, this.getDataSource(context), sql);
			
			/*RecordRestrict recordRestrict = this.getRecordRestrict(context);
			conditionStr = recordRestrict.judgeQueryRestrict(this.modelId, conditionStr, context, connection);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,connection);*/
			
			iColl.setName("AccAssetTransList");
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
