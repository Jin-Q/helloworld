package com.yucheng.cmis.biz01line.lmt.op.jointguar.lmtappdetailsjoint;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddLmtAppDetailsJointOp extends CMISOperation {
	
	//operation TableModel
//	private final String modelApp = "LmtApply";
	private final String modelId = "LmtAppDetails";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = "success";
		try{
			connection = this.getConnection(context);
			KeyedCollection kColl = null;
//			String serno = "";//申请主表流水号
//			String belgLine = "";//所属条线
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			String limit_code = CMISSequenceService4JXXD.querySequenceFromED("ED", "all", connection, context);
			kColl.setDataValue("limit_code", limit_code);
			TableModelDAO dao = this.getTableModelDAO(context);
			//插入明细前先查询申请主表是否进行了保存操作
//			serno = kColl.getDataValue("serno").toString();
//			belgLine = kColl.getDataValue("belg_line").toString();
//			String condition = " where serno='"+serno+"'";
//			//查询授信申请表
//			IndexedCollection iCollApp = null;
//			if(belgLine!=null&&"BL300".equals(belgLine)){
//				iCollApp = dao.queryList("LmtAppIndiv", condition, connection);
//			}else{
//				iCollApp = dao.queryList(modelApp, condition, connection);
//			}
//			//查询个人额度申请表
//			if(iCollApp.size()>0){
//				kColl.setDataValue("limit_code", limit_code);
//				dao.insert(kColl, connection);
//			}else{
//				kColl.setDataValue("limit_code", limit_code);
//				flag = "fail";
//			}
			
			dao.insert(kColl, connection);
			context.addDataField("flag",flag);
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
