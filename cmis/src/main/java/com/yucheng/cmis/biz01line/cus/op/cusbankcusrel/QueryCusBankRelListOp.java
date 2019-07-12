package com.yucheng.cmis.biz01line.cus.op.cusbankcusrel;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusBankRelListOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement("CusBankCusRel");
			} catch (Exception e) {}
			
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[] { "cus_id" };
			String[] fieldName=new String[]{"cus_name"};
			String temp=SystemTransUtils.dealQueryName(queryData, args, context, modelIds,modelForeign, fieldName);
		
			String cert_type = null;//证件类型
			String cert_code = null;//证件号码
			if(queryData!=null&&queryData.containsKey("cert_type")){
				cert_type = (String)queryData.getDataValue("cert_type");
			}
			if(queryData!=null&&queryData.containsKey("cert_code")){
				cert_code = (String)queryData.getDataValue("cert_code");
			}
			//modify by liuhongyu on 2019-04-13
			String conditionStr = TableModelUtil.getQueryCondition_bak("CusCom", queryData, context, false, false, false);
			if(conditionStr==null||"".equals(conditionStr))
				conditionStr = "where 1=1 ";
			
			if(cert_type!=null&&!"".equals(cert_type)){
				conditionStr = conditionStr + " and cert_type='" + cert_type +"'";
			}
			if(cert_code!=null&&!"".equals(cert_code)){
				conditionStr = conditionStr + " and cert_code='" + cert_code +"'";
			}
			if(temp!=null&&temp.length()>0){
				conditionStr = conditionStr + " and "+temp;
			}
			
			String sql = "select cus_id ,cus_name ,cert_type, cert_code,cus_bank_rel,bank_duty ,equi_no ,bank_equi_amt  "
					+ "  from (select cb.cus_id ,cb.cus_name ,cb.cert_type, cb.cert_code, a.cus_bank_rel,a.bank_duty ,a.equi_no ,a.bank_equi_amt  "
					+ "  from cus_com a,cus_base cb  where a.cus_id=cb.cus_id and a.is_ours_rela_cust in ('01','02')   union   "
					+ "  (select cb.cus_id ,cb.cus_name ,cb.cert_type, cb.cert_code, b.cus_bank_rel ,b.bank_duty ,b.stockhold_code ,b.com_hold_stk_amt  "
					+ "  from cus_indiv b,cus_base cb  where b.cus_id=cb.cus_id and b.IS_RELA_CUST = '1' ) )" + conditionStr;		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "10");
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);

		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}

}
