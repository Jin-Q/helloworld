package com.yucheng.cmis.biz01line.cus.op.cusgrpinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusGrpInfoModListOpforMain extends CMISOperation {


	private final String modelId = "CusGrpInfo";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{

			connection = this.getConnection(context);

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement(this.modelId);
			} catch (Exception e) {}

			int size = 15;

			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			String conditionStr = TableModelUtil.getQueryCondition_bak("CusGrpInfo", queryData, context, false, true, false);
			String sql = "select grp_no,grp_name,parent_cus_id,parent_cus_name,grp_finance_type,manager_br_id,manager_id,parent_org_code,parent_loan_card,grp_detail,grp_cus_type from " +
					"(select a.grp_no,a.grp_name,a.parent_cus_id,b.cus_name as parent_cus_name,a.manager_br_id,a.manager_id,b.cert_code as parent_org_code," +
					"b.loan_card_id as parent_loan_card,a.grp_finance_type ,grp_detail,a.grp_cus_type from cus_grp_info a,cus_base b where a.parent_cus_id=b.cus_id) " + conditionStr;
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);
            iColl.setName("CusGrpInfoList");
			
			SInfoUtils.addSOrgName(iColl, new String[]{"manager_br_id"});
            SInfoUtils.addUSerName(iColl, new String[]{"manager_id"});
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

