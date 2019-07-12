package com.yucheng.cmis.biz01line.iqp.drfpo.op.dpobilldetail;

import java.net.URLDecoder;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryDpoBillDetailListOp extends CMISOperation {
	
	private final String modelId = "IqpBillDetailInfo";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String drfpo_no="";
		try{
			connection = this.getConnection(context);
			
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}

			String conditionStr = TableModelUtil.getQueryCondition_bak(this.modelId, queryData, context, false, true, false);
			if(conditionStr.indexOf("WHERE") == -1){
				conditionStr = "where 1=1 ";
			}else{
				conditionStr = conditionStr.replaceFirst("porder_no", "a.porder_no");
			}
			
			//中文转码
			drfpo_no = (String) context.getDataValue("drfpo_no");
			drfpo_no = URLDecoder.decode(drfpo_no,"UTF-8");
			
			String sql = "select b.status,a.bill_type,a.porder_no,a.is_ebill,a.bill_isse_date,a.porder_end_date,a.porder_curr,"
					+ "a.drft_amt,b.drfpo_no from iqp_bill_detail_info a, iqp_corre_info b "+conditionStr
					+" and b.drfpo_no = '"+drfpo_no+"' and a.porder_no = b.porder_no order by porder_no";
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "10");
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);
			
			iColl.setName("IqpBillDetailInfoList");
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
