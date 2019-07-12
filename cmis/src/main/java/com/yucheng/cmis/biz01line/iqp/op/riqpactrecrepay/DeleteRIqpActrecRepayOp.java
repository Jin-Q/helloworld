package com.yucheng.cmis.biz01line.iqp.op.riqpactrecrepay;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.dao.SqlClient;

public class DeleteRIqpActrecRepayOp extends CMISOperation {
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String invc_no = (String) context.getDataValue("invc_no");
			String cont_no = (String) context.getDataValue("cont_no");
			
			//得到前台传过来的多选记录
			IndexedCollection icoll = (IndexedCollection)context.getDataElement("RIqpActrecRepayList");
			if(null==icoll && icoll.size()<1){
				context.addDataField("flag", "error");
				context.addDataField("message", "未获取到关联的保证金流水记录。");
				return "0";
			}
			
			String tran_serno = "";
			String delete_sql ="DELETE FROM R_IQP_ACTREC_REPAY WHERE TRAN_SERNO IN(";   //解除关联的SQL
			for (Iterator iterator = icoll.iterator(); iterator.hasNext();) {
				KeyedCollection kcoll = (KeyedCollection) iterator.next();
				tran_serno += "'"+(String)kcoll.getDataValue("tran_serno")+"',";
			}
			//有需要删除的流水号
			if(!"".equals(tran_serno) && tran_serno.length()>1){
				tran_serno = tran_serno.substring(0,tran_serno.length()-1);
				delete_sql += tran_serno+")";
			}else{
				context.addDataField("flag", "error");
				context.addDataField("message", "未获取到关联的保证金流水记录。");
				return "0";
			}
			//保险起见 加贸易合同编号跟权证 号
			delete_sql +=" AND INVC_NO='"+invc_no+"' AND CONT_NO='"+cont_no+"'";
			
			SqlClient.deleteBySql(delete_sql,this.getConnection(context));
			
			context.addDataField("flag", "success");
			context.addDataField("msg", "success");
		}catch(Exception e){
			context.addDataField("flag", "error");
			context.addDataField("msg", "解除应收账款与保证金流水关系失败，失败原因："+e.getMessage());
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
