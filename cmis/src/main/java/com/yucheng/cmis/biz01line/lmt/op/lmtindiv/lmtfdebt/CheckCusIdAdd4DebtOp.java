package com.yucheng.cmis.biz01line.lmt.op.lmtindiv.lmtfdebt;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 异步查询客户是否存在申请信息
 * @param context context对象
 * @author ZYF
 */
public class CheckCusIdAdd4DebtOp extends CMISOperation{
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String cus_id = "";
		String serno = "";
		try{
			connection = this.getConnection(context);
			if(context.containsKey("cus_id")){
				cus_id = context.getDataValue("cus_id").toString();
				serno = context.getDataValue("serno").toString();
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kc = dao.queryFirst("LmtFdebt",null, "where cus_id='"+cus_id+"' and serno ='"+serno+"'", connection);
			System.out.println(kc != null);
			System.out.println(kc.getDataValue("cus_id"));
			if(kc != null && (kc.getDataValue("cus_id"))!= null){
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
