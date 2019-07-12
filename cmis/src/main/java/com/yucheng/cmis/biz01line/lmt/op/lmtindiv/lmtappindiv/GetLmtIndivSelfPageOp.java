package com.yucheng.cmis.biz01line.lmt.op.lmtindiv.lmtappindiv;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SystemTransUtils;

/**
 * 获取个人授信自动额度页面
 * @author tsy
 * @date 20130923
 */
public class GetLmtIndivSelfPageOp extends CMISOperation {

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String returnValue="";   //定义返回字符串
		try {
			connection = this.getConnection(context);
			
			String serno_value = null;
			if(context.containsKey("serno")){
				serno_value = (String)context.getDataValue("serno");
			}else{
				EMPLog.log("MESSAGE", EMPLog.ERROR, 0, "获取自动额度信息的流水号[serno]为空！");
				throw new EMPJDBCException("获取自动额度信息的流水号[serno]为空！");
			}
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail("LmtAppIndiv", serno_value, connection);
			
			if(null!=kColl.getDataValue("same_debtor_id") && !"".equals(kColl.getDataValue("same_debtor_id"))){
				/**翻译客户名称、登记人、登记机构*/
				String[] args=new String[] { "same_debtor_id" };
				String[] modelIds=new String[]{"CusBase"};
				String[] modelForeign=new String[]{"cus_id"};
				String[] fieldName=new String[]{"cus_name"};
				//详细信息翻译时调用			
				SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			}
			
			if(null!=kColl.getDataValue("repay_type_self") && !"".equals(kColl.getDataValue("repay_type_self"))){
				/**翻译客户名称、登记人、登记机构*/
				String[] args=new String[] { "repay_type_self" };
				String[] modelIds=new String[]{"PrdRepayMode"};
				String[] modelForeign=new String[]{"repay_mode_id"};
				String[] fieldName=new String[]{"repay_mode_dec"};
				//详细信息翻译时调用			
				SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			}
			
			kColl.setName("LmtAppIndiv");
			
			this.putDataElement2Context(kColl, context);
		} catch (EMPException ee) {
			throw ee;
		} catch (Exception e) {
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return returnValue;
	}
}
