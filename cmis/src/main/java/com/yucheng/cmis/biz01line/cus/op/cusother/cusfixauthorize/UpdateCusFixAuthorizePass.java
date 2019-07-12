package com.yucheng.cmis.biz01line.cus.op.cusother.cusfixauthorize;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.util.MD5;

public class UpdateCusFixAuthorizePass extends CMISOperation {
	

	private final String modelId = "CusFixAuthorize";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);

			String flagInfo = "";
			String serno = "";
			KeyedCollection kColl = null;
			KeyedCollection KCollTmp = null;
			String passWord = "";
			String passWordOld = "";
			String passWordNew = "";
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			
			serno = (String)kColl.getDataValue("serno");
			TableModelDAO dao = this.getTableModelDAO(context);
			KCollTmp = dao.queryDetail(modelId, serno, connection);
			
			passWord = (String)KCollTmp.getDataValue("checkcode");
			passWordOld = (String)kColl.getDataValue("checkcode");//老密码
			passWordOld = MD5.encode(passWordOld).toUpperCase();
			passWordNew = (String)kColl.getDataValue("checkcodenew");//新密码
			if(passWord.equals(passWordOld)){
				passWordNew = MD5.encode(passWordNew).toUpperCase();
				KCollTmp.setDataValue("checkcode", passWordNew);
				int count=dao.update(KCollTmp, connection);
				if(count!=1){
					throw new EMPException("Update Failed! Record Count: " + count);
				}
				flagInfo = PUBConstant.SUCCESS;
			}else{
				flagInfo = PUBConstant.FAIL;
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
		return "0";
	}
}
