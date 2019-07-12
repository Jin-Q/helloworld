package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtsiglmt;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class UpdateLmtSigLmtRecordOp extends CMISOperation {
	 
	private final String modelId = "LmtSigLmt";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			KeyedCollection kColl_temp =null;
			String serno = "";
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to update["+modelId+"] cannot be empty!");
			 
			TableModelDAO dao = this.getTableModelDAO(context);
			//若流水号为空则是新增，否则是修改
			serno = (String)kColl.getDataValue("serno");
			if(serno==null||"".equals(serno)){
				String manager_br_id = kColl.getDataValue("manager_br_id").toString();
				//系统自动生成业务编号
				serno = CMISSequenceService4JXXD.querySequenceFromSQ("SQ", "all", manager_br_id, connection, context);
				kColl.setDataValue("serno", serno);
				dao.insert(kColl, connection);
			}else {
				dao.update(kColl, connection);
				String lmt_amt = (String)kColl.getDataValue("lmt_amt");
				IndexedCollection iColl_temp = dao.queryList("LmtSubApp", "where serno='"+serno+"'", connection);
				if(iColl_temp != null && iColl_temp.size() > 0){
					for(int i=0;i<iColl_temp.size();i++){
						kColl_temp = (KeyedCollection) iColl_temp.get(i);
						kColl_temp.setDataValue("lmt_amt", lmt_amt);
						dao.update(kColl_temp, connection);
					}
				}
			}
			context.addDataField("flag", PUBConstant.SUCCESS);
			context.addDataField("serno", serno);
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
