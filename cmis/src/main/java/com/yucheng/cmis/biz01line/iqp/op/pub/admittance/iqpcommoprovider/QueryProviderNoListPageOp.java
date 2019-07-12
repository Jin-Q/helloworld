package com.yucheng.cmis.biz01line.iqp.op.pub.admittance.iqpcommoprovider;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
/**
 * 异步查询客户是否存在在途、未提交的申请信息
 * @param context context对象
 * @author ZYF
 */
public class QueryProviderNoListPageOp extends CMISOperation{
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String provider_no = "";
		try{
			connection = this.getConnection(context);
			if(context.containsKey("provider_no")){
				provider_no = context.getDataValue("provider_no").toString();
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			String condition = " WHERE provider_no='"+provider_no+"'";
			KeyedCollection kColl = dao.queryFirst("IqpCommoProvider", null, condition, connection);
			if(kColl == null || kColl.getDataValue("provider_no")== null){
				context.addDataField("flag", "success");
			}else{
				context.addDataField("flag", "failed");
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
