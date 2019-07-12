package com.yucheng.cmis.biz01line.lmt.op.lmtappcoop.lmtcoopfroze;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class GetLmtCoopFrozenAddPageOp extends CMISOperation {
	
	private final String modelId = "LmtAgrJointCoop";
	private final String modelApp = "LmtAppJointCoop";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String agrNo = "";
			String appType = "";
			try {
				agrNo = (String)context.getDataValue("agr_no");//协议编号
				appType = (String)context.getDataValue("app_type");//申请类型
			} catch (Exception e) {}
			if(agrNo == null || agrNo.length() == 0)
				throw new EMPJDBCException("The value of pk[agr_no] cannot be null!");
			
			if(appType == null || appType.length() == 0)
				throw new EMPJDBCException("The value of pk[agr_no] cannot be null!");
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, agrNo, connection);
			//已冻结金额
			String al_froze_amt = (String)kColl.getDataValue("froze_amt");
			if(al_froze_amt==null||"".equals(al_froze_amt)){
				al_froze_amt = "0";
			}
			kColl.put("al_froze_amt", al_froze_amt);
			kColl.put("serno", "");
			kColl.put("approve_status", "000");
			kColl.put("app_type", appType);
			kColl.put("input_br_id",context.getDataValue("organNo"));
			kColl.put("input_id",context.getDataValue("currentUserId"));
			kColl.put("input_date",context.getDataValue("OPENDAY"));
			kColl.remove("agr_status");
			kColl.remove("froze_amt");
			kColl.setName(modelApp);
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "input_id" });
			
			if("2".equals(kColl.getDataValue("share_range"))){   //共享范围为支行时翻译机构
				SystemTransUtils.containCommaORG2CN("belg_org",kColl,context);
			}
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			this.putDataElement2Context(kColl, context);
			
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
