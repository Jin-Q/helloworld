package com.yucheng.cmis.biz01line.cus.op.custrusteelog;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryCusTrusteeLogDetailOp  extends CMISOperation {
	
	private final String modelId = "CusTrusteeLog";
	

	private final String serno_name = "serno";
	
	
	private boolean updateCheck = false;
	

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
			
			SInfoUtils.addSOrgName(kColl, new String[]{"consignor_br_id", "trustee_br_id", "supervise_br_id"});
			SInfoUtils.addUSerName(kColl, new String[]{"consignor_id", "trustee_id","supervise_id"});
			//明细
			int size = 5;

			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one",
					String.valueOf(size));
			List<String> list = new ArrayList<String>();
			list.add("cus_id");
			list.add("cus_name");
			list.add("serno");
			IndexedCollection iColl = dao.queryList("CusTrusteeLst", list," where serno='"+serno_value+"'", pageInfo, connection);
			iColl.setName(iColl.getName()+"List");
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			
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
