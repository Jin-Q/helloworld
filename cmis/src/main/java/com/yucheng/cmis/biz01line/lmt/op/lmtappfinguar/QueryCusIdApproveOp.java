package com.yucheng.cmis.biz01line.lmt.op.lmtappfinguar;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
/**
 * 异步查询客户是否存在在途、未提交的申请信息
 * @param context context对象
 * @author ZYF
 */
public class QueryCusIdApproveOp extends CMISOperation{
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String cus_id = "";
		try{
			connection = this.getConnection(context);
			if(context.containsKey("cus_id")){
				cus_id = context.getDataValue("cus_id").toString();
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);

			//查询客户是否存在待发起、打回、重办、审批中的数据
			String condition = " WHERE APPROVE_STATUS IN('991','992','000','111') AND CUS_ID='"+cus_id+"' AND APP_TYPE <> '01' ";
			IndexedCollection iColl = dao.queryList("LmtAppFinGuar", condition, connection);
			if(iColl != null && iColl.size()>0 ){
				context.addDataField("flag", PUBConstant.SUCCESS);
			}else{
				context.addDataField("flag", PUBConstant.FAIL);
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
