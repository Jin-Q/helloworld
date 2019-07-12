package com.yucheng.cmis.biz01line.arp.op.arpbadassethandoverrcv;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.arp.op.arpbadassethandoverapp.LeadArpBadassetHandoverAppOp;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class QueryArpBadassetHandoverRcvDetailOp  extends CMISOperation {
	
	private final String modelId = "ArpBadassetHandoverRcv";
	

	private final String serno_name = "serno";
	
	
	private boolean updateCheck = true;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
		
			if(this.updateCheck){
			
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			
			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			String bill_no = kColl.getDataValue("bill_no").toString();			
			LeadArpBadassetHandoverAppOp cmisOp =new LeadArpBadassetHandoverAppOp();
			cmisOp.delBillInfo(kColl, context, connection, bill_no, dao);
			
			SInfoUtils.addSOrgName(kColl, new String[] { "fount_manager_br_id","input_br_id","rcv_org"});
			SInfoUtils.addUSerName(kColl, new String[] { "fount_manager_id","input_id","rcv_person"});
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
