package com.yucheng.cmis.biz01line.lmt.op.lmtappcoop;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtAppJointCoopDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtAppJointCoop";
	
	private final String serno_name = "serno";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			if(context.containsKey("updateCheck")){
				context.setDataValue("updateCheck", "true");
			}else {
				context.addDataField("updateCheck", "true");
			}
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);

			String serno_value = null;
			String app_type = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			
			//冻结解冻则查询已冻结金额
			app_type = (String)kColl.getDataValue("app_type");
			if("03".equals(app_type)||"04".equals(app_type)){
				String agr_no = (String)kColl.getDataValue("agr_no");
				KeyedCollection kCollAgr = dao.queryDetail("LmtAgrJointCoop", agr_no, connection);
				String froze_amt = (String)kCollAgr.getDataValue("froze_amt");
				if(froze_amt==null||"".equals(froze_amt)){
					froze_amt = "0";
				}
				kColl.put("al_froze_amt", froze_amt);//已冻结金额
			}
			
			/**翻译客户名称、登记人、登记机构*/
			String[] args=new String[] { "cus_id" };
			String[] modelIds=new String[]{"CusBase"};
			String[] modelForeign=new String[]{"cus_id"};
			String[] fieldName=new String[]{"cus_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id"});
			SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id" });
			
			if("2".equals(kColl.getDataValue("share_range"))){   //共享范围为支行时翻译机构
				SystemTransUtils.containCommaORG2CN("belg_org",kColl,context);
			}
			
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
