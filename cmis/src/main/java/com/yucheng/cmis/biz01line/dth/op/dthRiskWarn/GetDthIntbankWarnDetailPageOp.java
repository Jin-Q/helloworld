package com.yucheng.cmis.biz01line.dth.op.dthRiskWarn;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class GetDthIntbankWarnDetailPageOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String value = context.getDataValue("value").toString();
			String sql = " select a.* ,(lmt_amt - lmt_cost ) as lmt_balance  "
					+ "  from (select l.cus_id , c.same_org_cnname,c.crd_grade, l.limit_type ,l.lmt_amt ,l.start_date,l.end_date,  "
					+ "  (select lmt_cost_amt from lmt_cost_last where flag = '02' and cus_id = l.cus_id) as lmt_cost  "
					+ "  from LMT_INTBANK_ACC l , cus_same_org c where l.cus_id = c.cus_id and l.cus_id = '"+ value + "') a";

			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "10");
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);
			iColl.setName("IntbankWarnDetail");
			
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)this.releaseConnection(context, connection);
		}
		return "0";
	}

}