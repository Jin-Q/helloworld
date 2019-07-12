package com.yucheng.cmis.platform.workflow.op.wfilvcreditright;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
/**
 * 
*@author lisj
*@time 2015-4-3
*@description TODO 需求编号：【XD150407025】分支机构授信审批权限配置 
*							   检查分/支行授信审批金额是否合规
*@version v1.0
*
 */
public class CheckWLCRSubCrdAmtOp extends CMISOperation {

private final String modelId = "WfiLvCreditRight";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			BigDecimal nCrdAmt = BigDecimalUtil.replaceNull((String) kColl.getDataValue("new_crd_amt")) ;
			BigDecimal sCrdAmt = BigDecimalUtil.replaceNull((String) kColl.getDataValue("stock_crd_amt"));
			BigDecimal snCrdAmt = BigDecimalUtil.replaceNull((String) kColl.getDataValue("sub_new_crd_amt"));
			BigDecimal ssCrdAmt = BigDecimalUtil.replaceNull((String) kColl.getDataValue("sub_stock_crd_amt"));
			//检查分/支行审批金额是否大于总行审批金额
			String msg ="";
			if(snCrdAmt.compareTo(nCrdAmt) >0){
				msg += "【分/支行新增授信审批金额】不能超过【总行新增授信审批金额】！";
			}
			if(ssCrdAmt.compareTo(sCrdAmt) >0){
				msg+="【分/支行存量授信审批金额】不能超过【总行存量授信审批金额】！";
			}
			if("".equals(msg)){
				context.put("flag", PUBConstant.SUCCESS);
			}else{
				context.put("flag", msg);
			}
		}catch (EMPException ee) {
			context.put("flag", PUBConstant.FAIL);
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
