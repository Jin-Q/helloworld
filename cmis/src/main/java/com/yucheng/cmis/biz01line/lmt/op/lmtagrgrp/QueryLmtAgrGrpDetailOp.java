package com.yucheng.cmis.biz01line.lmt.op.lmtagrgrp;

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

public class QueryLmtAgrGrpDetailOp  extends CMISOperation {
	
	private final String modelId = "LmtAgrGrp";
	

	private final String grp_agr_no_name = "grp_agr_no";
	
	
	private boolean updateCheck = false;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String grp_agr_no_value = null;
			try {
				grp_agr_no_value = (String)context.getDataValue(grp_agr_no_name);
			} catch (Exception e) {}
			if(grp_agr_no_value == null || grp_agr_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+grp_agr_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, grp_agr_no_value, connection);
			SInfoUtils.addSOrgName(kColl, new String[] { "input_br_id","manager_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "input_id","manager_id" });
			String[] args=new String[] { "grp_no" };
			String[] modelIds=new String[]{"CusGrpInfo"};
			String[] modelForeign=new String[]{"grp_no"};
			String[] fieldName=new String[]{"grp_name"};
			//详细信息翻译时调用			
			SystemTransUtils.dealName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			
//			String crd_totl_amt = kColl.getDataValue("crd_totl_amt").toString();
			
//			CMISModualServiceFactory serviceJndi = CMISModualServiceFactory.getInstance();
//			IqpServiceInterface service = (IqpServiceInterface)serviceJndi.getModualServiceById("iqpServices", "iqp");
//			KeyedCollection kCollTemp = service.getAgrUsedInfoByArgNo(grp_agr_no_value, "01", connection, context);
//			String lmt_amt = kCollTemp.getDataValue("lmt_amt").toString();
//			double crd_bal_amt = Double.parseDouble(crd_totl_amt) - Double.parseDouble(lmt_amt);
//			kColl.addDataField("crd_bal_amt", crd_bal_amt);
			
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
