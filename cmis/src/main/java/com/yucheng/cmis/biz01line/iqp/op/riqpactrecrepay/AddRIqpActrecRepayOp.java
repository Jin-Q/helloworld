package com.yucheng.cmis.biz01line.iqp.op.riqpactrecrepay;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class AddRIqpActrecRepayOp extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno = "";
		try{
			connection = this.getConnection(context);
			
			String invc_no = (String) context.getDataValue("invc_no");
			String cont_no = (String) context.getDataValue("cont_no");
			String po_no = (String) context.getDataValue("po_no");  
			
			//得到前台传过来的多选记录
//			IndexedCollection icoll = (IndexedCollection)context.getDataElement("IqpCoreBailDetailList");
//			if(null==icoll && icoll.size()<1){
//				context.addDataField("flag", "error");
//				context.addDataField("message", "未获取到关联的保证金流水记录。");
//				return "0";
//			}
			
			String params = (String)context.getDataValue("params");
			if(null==params || "".equals(params)){
				context.addDataField("flag", "error");
				context.addDataField("message", "未获取到关联的保证金流水记录。");
				return "0";
			}
			
			//判断应收账款的权利金额是否关联满   防止数据脏读，特将查询已回款明细放插入回款明细前面      ***** 很重要****
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("invc_no", invc_no);
			pkMap.put("cont_no", cont_no);
			KeyedCollection kcoll = (KeyedCollection)SqlClient.queryFirst("queryRepayBailAmt", pkMap , null, connection);
			BigDecimal bail_amt_ogr = new BigDecimal("0");
			if(null!=kcoll){
				bail_amt_ogr = bail_amt_ogr.add(BigDecimalUtil.replaceNull(kcoll.getDataValue("bail_amt")));
			}
			
			BigDecimal bail_amt_total = new BigDecimal("0");  //本次关联总金额
			IndexedCollection value_icoll = new IndexedCollection();
			
			String[] params_arr = params.split(";");
			for (int i = 0; i < params_arr.length; i++) {
				String[] value_arr = params_arr[i].split(",");
				serno = value_arr[0];
				bail_amt_total = bail_amt_total.add(new BigDecimal(value_arr[1]));
				
				KeyedCollection value_kcoll = new KeyedCollection();
				value_kcoll.addDataField("tran_serno",serno);
				value_kcoll.addDataField("cont_no",cont_no);
				value_kcoll.addDataField("invc_no",invc_no);
				value_kcoll.addDataField("po_no",po_no);
				value_kcoll.addDataField("input_date", context.getDataValue("OPENDAY"));
				value_icoll.add(value_kcoll);
			}
//			for (Iterator iterator = icoll.iterator(); iterator.hasNext();) {
//				KeyedCollection kcoll = (KeyedCollection) iterator.next();
//				serno = (String)kcoll.getDataValue("serno");
//				bail_amt_total.add(BigDecimalUtil.replaceNull(kcoll.getDataValue("bail_amt")));
//				
//				KeyedCollection value_kcoll = new KeyedCollection();
//				value_kcoll.addDataField("tran_serno",serno);
//				value_kcoll.addDataField("cont_no",cont_no);
//				value_kcoll.addDataField("invc_no",invc_no);
//				value_kcoll.addDataField("po_no",po_no);
//				value_kcoll.addDataField("input_date", context.getDataValue("OPENDAY"));
//				value_icoll.add(value_kcoll);
//			}
			SqlClient.executeBatch("insertRIqpActrecRepay", null, value_icoll, null, connection);
			
			bail_amt_total = bail_amt_total.add(bail_amt_ogr);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryFirst("IqpActrecbondDetail", null, " where invc_no='" + invc_no + "' AND cont_no='"+cont_no+"'", connection);
			BigDecimal bond_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("bond_amt"));
			
			if(bail_amt_total.compareTo(bond_amt)>=0){
				context.addDataField("flag", "confirm");
				context.addDataField("msg", "应收账款回款已登记完成，是否确认？");
				return "0";
			}
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "success");
		}catch(Exception e){
			context.addDataField("flag", "error");
			context.addDataField("msg", "回款登记失败，失败原因："+e.getMessage());			
			try {
				connection.rollback();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
