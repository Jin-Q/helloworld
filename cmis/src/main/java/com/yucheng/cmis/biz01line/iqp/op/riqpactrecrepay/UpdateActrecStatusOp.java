package com.yucheng.cmis.biz01line.iqp.op.riqpactrecrepay;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;
import com.yucheng.cmis.pub.util.BigDecimalUtil;

public class UpdateActrecStatusOp extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String invc_no = (String) context.getDataValue("invc_no");
			String cont_no = (String) context.getDataValue("cont_no");
			
			//判断应收账款的权利金额是否关联满
			Map<String,String> pkMap = new HashMap<String,String>();
			pkMap.put("invc_no", invc_no);
			pkMap.put("cont_no", cont_no);
			
			if(context.containsKey("repay")){ //如果是从回款登记主页面，先判读是否已经回款完成。
				
				KeyedCollection value_kcoll = (KeyedCollection)SqlClient.queryFirst("queryRepayBailAmt", pkMap , null, connection);
				BigDecimal bail_amt_ogr = new BigDecimal("0.0");
				if(null!=value_kcoll){
					bail_amt_ogr = bail_amt_ogr.add(BigDecimalUtil.replaceNull(value_kcoll.getDataValue("bail_amt")));
				}
				
				TableModelDAO dao = this.getTableModelDAO(context);
				KeyedCollection kColl = dao.queryFirst("IqpActrecbondDetail", null, " where invc_no='" + invc_no + "' AND cont_no='"+cont_no+"'", connection);
				BigDecimal bond_amt = BigDecimalUtil.replaceNull(kColl.getDataValue("bond_amt"));
				
				if(bail_amt_ogr.compareTo(bond_amt) < 0){
					context.addDataField("flag", "error");
					context.addDataField("msg", "修改应收账款状态失败，失败原因：应收账款权证金额金额为["+bond_amt+"]，回款金额为["+bail_amt_ogr+"],回款未完成，请确认。");
					return "0";
				}
			}
			//修改状态
			SqlClient.executeUpd("updateActrecStatus", pkMap, "7", null, connection);
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "success");
		}catch(Exception e){
			context.addDataField("flag", "error");
			context.addDataField("msg", "修改应收账款状态失败，失败原因："+e.getMessage());
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
