package com.yucheng.cmis.biz01line.cus.op.cusindivtax;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddCusIndivTaxRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusIndivTax";
	
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
			
		/*	ComponentHelper cHelper = new ComponentHelper();
			CusIndivTax cusIndivTax = new CusIndivTax();
			cusIndivTax = (CusIndivTax) cHelper.kcolTOdomain(cusIndivTax, kColl);
			
		
			CusIndivTaxComponent cusIndivTaxComponent = (CusIndivTaxComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSINDIVTAX,context);
			String strReturnMessage = cusIndivTaxComponent.checkExist(cusIndivTax);*/

			String serno =CMISSequenceService4JXXD.querySequenceFromDB("CUS", "all", connection, context);
			kColl.put("serno", serno);
			
			//if(strReturnMessage.equals("no")){
				//不存在，可以新增
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			flag = "新增成功";
			/*}else if(strReturnMessage.equals("yes")){
				//税种+客户码已存在
				flag = "该客户下的税种已存在，不能新增！";
			}*/
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
