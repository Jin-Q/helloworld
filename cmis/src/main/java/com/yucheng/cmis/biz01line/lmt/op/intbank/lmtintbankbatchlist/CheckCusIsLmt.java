package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtintbankbatchlist;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
   /**
    * 检查批量包中客户是否已授信
    * 用于批量授信查询模块
    * */
 public class CheckCusIsLmt extends CMISOperation {
	private final String modelId = "LmtSigLmt";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String CusNo  = "";
			try {
				CusNo = (String)context.getDataValue("cus_id");
			} catch (Exception e) {}
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition ="where cus_id='"+CusNo+"'";
			IndexedCollection iColl = dao.queryList(modelId, condition+"and app_cls='"+"02'", connection);
			if(iColl.size()>0)
			{
				context.addDataField("flag", "fail");						
			}else{
				context.addDataField("flag", "success");	
			}				
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
