package com.yucheng.cmis.biz01line.cus.op.cuscomaptitude;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusComAptitudeComponent;
import com.yucheng.cmis.biz01line.cus.cuscom.domain.CusComAptitude;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class AddCusComAptitudeRecordOp extends CMISOperation {
	
	private final String modelId = "CusComAptitude";
	
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
			
			ComponentHelper cHelper = new ComponentHelper();
			CusComAptitude cusComAptitude = new CusComAptitude();
			
			cusComAptitude = (CusComAptitude) cHelper.kcolTOdomain(cusComAptitude, kColl);
			
			CusComAptitudeComponent cusComAptitudeComponent = (CusComAptitudeComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSCOMAPTITUDE,context,connection);
			String strReturnMessage = cusComAptitudeComponent.checkExist(cusComAptitude);
		
			if(strReturnMessage.equals("no")){
				//不存在，可以新增
				TableModelDAO dao = this.getTableModelDAO(context);
				dao.insert(kColl, connection);
				flag = "新增成功";
			}else if(strReturnMessage.equals("yes")){
				//该资质证书编号+客户码已存在
				flag = "该客户下的此资质证书编号已存在，不能新增！";
			}
			
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
