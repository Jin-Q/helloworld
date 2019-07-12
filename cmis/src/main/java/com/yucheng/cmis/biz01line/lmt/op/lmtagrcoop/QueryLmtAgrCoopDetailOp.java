package com.yucheng.cmis.biz01line.lmt.op.lmtagrcoop;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.msi.IqpServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISModualServiceFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryLmtAgrCoopDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtAgrJointCoop";
	
	private final String agr_no_name = "agr_no";
	
	private boolean updateCheck = false;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String agr_no_value = null;
			try {
				agr_no_value = (String)context.getDataValue(agr_no_name);
			} catch (Exception e) {}
			if(agr_no_value == null || agr_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+agr_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryFirst(modelId,null, " WHERE agr_no='"+agr_no_value+"'", connection);
			
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
			
			String lmt_totl_amt = kColl.getDataValue("lmt_totl_amt").toString();
			
			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
			KeyedCollection kCollTemp = service.getAgrUsedInfoByArgNo(agr_no_value, "03", connection, context);
			String lmt_amt = kCollTemp.getDataValue("lmt_amt").toString();
			double lmt_bal_amt = Double.parseDouble(lmt_totl_amt) - Double.parseDouble(lmt_amt);
			kColl.addDataField("lmt_bal_amt", lmt_bal_amt);
			
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
