package com.yucheng.cmis.biz01line.iqp.op.iqpextensionapp;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.dic.CMISTreeDicService;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryGoverBillNoPop extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {			
            connection = this.getConnection(context);         
            KeyedCollection queryData = null;
            try {
                queryData = (KeyedCollection)context.getDataElement("AccLoanPop");
            } catch (Exception e) {}
            String conditionStr = TableModelUtil.getQueryCondition_bak("AccLoan", queryData, context, false, true, false);
            if(conditionStr.length()==0){
            	conditionStr = "where 1 = 1 ";
            }
            
            String cus_id=context.getDataValue("cus_id").toString();
            
            /*** 由贷款台账取借据信息 ***/
			String sql = " select a.bill_no , a.loan_amt , a.loan_balance , b.loan_type , a.cus_id,a.cont_no," 
					+ " (select cus_name from cus_base where cus_id=a.cus_id ) as cus_name ,a.distr_date as start_date , a.end_date ,"
					+ " round(months_between(to_date(a.end_date,'yyyy-mm-dd'),to_date(a.distr_date,'yyyy-mm-dd')),1) as loan_distr_term ,"
					+ " b.repay_type , b.repay_src_des , b.loan_direction "
					+ " from Acc_Loan a , Ctr_Loan_Cont_Sub b "+conditionStr+" and a.cont_no = b.cont_no"
					+ " and a.cus_id = '"+cus_id+"' ";
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", "15");
			IndexedCollection iColl = TableModelUtil.buildPageData(pageInfo, this.getDataSource(context), sql);
			
			String[] args=new String[] { "cus_id","repay_type","cus_id" };
			String[] modelIds=new String[]{"CusBase","PrdRepayMode","CusBase"};
			String[] modelForeign=new String[]{"cus_id","repay_mode_id","cus_id"};
			String[] fieldName=new String[]{"cus_name","repay_mode_dec","belg_line"};
			String[] resultName=new String[]{"cus_id_displayname","repay_type_displayname","belg_line"};
			SystemTransUtils.dealPointName(iColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
			
			/** 翻译字典项 */
			Map<String,String> map = new HashMap<String, String>();
			CMISTreeDicService service = (CMISTreeDicService) context.getService(CMISConstance.ATTR_TREEDICSERVICE);
			for(int i = 0 ;i < iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection) iColl.get(i);
				String belg_line = kColl.getDataValue("belg_line").toString();
				map.put("loan_direction", "STD_GB_4754-2011");
//				if("BL100".equals(belg_line) || "BL200".equals(belg_line)){
//					map.put("loan_type", "STD_COM_POSITIONTYPE");
//				}else if("BL300".equals(belg_line)){
//					map.put("loan_type", "STD_PER_POSITIONTYPE");
//				}
				
				kColl.setName("kColl");
				SInfoUtils.addPopName(kColl, map, service);
				this.putDataElement2Context(kColl, context);
			}
						
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