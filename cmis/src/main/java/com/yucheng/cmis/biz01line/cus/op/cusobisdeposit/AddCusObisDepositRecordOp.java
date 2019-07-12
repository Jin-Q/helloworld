package com.yucheng.cmis.biz01line.cus.op.cusobisdeposit;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cuscom.component.CusObisDepositComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;

public class AddCusObisDepositRecordOp extends CMISOperation {
	
	private final String modelId = "CusObisDeposit";
	
	/**
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		String acc_no = null;
//		String org_name = null;
//		String cus_id = null;
		try{
			
			connection = this.getConnection(context);
			
			CusObisDepositComponent cusObisDepositComponent = (CusObisDepositComponent)CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSOBISDEPOSIT, context,connection);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
				acc_no = (String)kColl.getDataValue("acc_no");
//				org_name = (String)kColl.getDataValue("org_name");
//				cus_id = (String)kColl.getDataValue("cus_id");
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			//查询数据库校验该账号是否存在，若存在则返回客户吗
			String cusIdTmp=cusObisDepositComponent.checkExist(acc_no);
			if(cusIdTmp==null||"".equals(cusIdTmp)){
				TableModelDAO dao = this.getTableModelDAO(context);
				dao.insert(kColl, connection);
				flag = "新增成功";
			}else {
				flag = "["+acc_no+"]账号在["+cusIdTmp+"]客户名下已经存在,不能重复新增！";
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
