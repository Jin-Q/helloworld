package com.yucheng.cmis.biz01line.cus.op.cusindivfamlby;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddCusIndivFamLbyRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusIndivFamLby";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			String indiv_debt_id = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			kColl.setDataValue("indiv_debt_id", indiv_debt_id);
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
 
			dao.insert(kColl, connection);
		/*	*//**
			 * 更新家庭收支及资产负债表中负债值和净资产值
			 *//*
			//得到负债金额
			String indiv_debt_amt = (String)kColl.getDataValue("indiv_debt_amt");
			double indivDebtAmt = Double.parseDouble(indiv_debt_amt);
			
			CusIndivFamBlcComponent cusIndivFamBlcComponent = (CusIndivFamBlcComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSINDIVFAMBIC,context);
			//String strReturnMessage = cusIndivFamBlcComponent.checkExist(cusIndivFamBlc);
			*/
			flag = "新增成功";
		}catch (EMPException ee) {
			flag = "新增失败";
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		context.addDataField("flag",flag);
		return "0";
	}
}
