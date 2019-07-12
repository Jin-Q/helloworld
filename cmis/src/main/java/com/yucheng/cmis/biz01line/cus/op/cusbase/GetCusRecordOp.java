package com.yucheng.cmis.biz01line.cus.op.cusbase;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

/**
 * 
 *@Classname	AddCusBaseRecordOp.java
 *@Version 1.0	
 *@Copyright 	yuchengtech
 *@Author 		liuxin
 *@Description：  用于客户开户处理
 *@Lastmodified 
 *@Author
 *
 */
public class GetCusRecordOp extends CMISOperation {
	
	/**
	 * 业务逻辑执行的具体实现方法
	 */
	public String doExecute(Context context) throws EMPException {
		
		String flagInfo = "";
		
		Connection connection = null;
		
		try{
			connection = this.getConnection(context);
			String cusId= (String)context.getDataValue("cusId");
			
			CusBaseComponent cusBaseComponent = (CusBaseComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance("CusBase", context,connection);
			CusBase cusBase = cusBaseComponent.getCusBase(cusId);
			String belgLine = cusBase.getBelgLine();
			
			String cus_id=cusBase.getCusId();
			context.addDataField("cus_id", cus_id);
			if("BL300".equals(belgLine)){
				flagInfo = "indiv";
			}else{
				flagInfo = "com";
			}
			context.addDataField("flag", flagInfo);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return flagInfo;
	}
}
