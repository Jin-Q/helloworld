package com.yucheng.cmis.biz01line.cus.op.cuscomfinabond;

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

public class AddCusComFinaBondRecordOp extends CMISOperation {
	
	private final String modelId = "CusComFinaBond";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String flag = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			TableModelDAO dao = this.getTableModelDAO(context);
			//债券编号，保存前先校验该编号是否已经存在
            String com_bond_no = (String)kColl.getDataValue("com_bond_no");
            String cus_id = (String)kColl.getDataValue("cus_id");
            String condition = " where com_bond_no='"+com_bond_no+"' and cus_id='"+cus_id+"'";
			IndexedCollection iCollFb = dao.queryList(modelId, condition, connection);
            if(iCollFb.size()>0){
            	flag = PUBConstant.EXISTS;
            }else{
            	String seqNo = CMISSequenceService4JXXD.querySequenceFromDB("CUS", "fromDate",connection, context);
            	kColl.put("seq", seqNo);
//            	String com_bond_pub_dt = (String)kColl.getDataValue("com_bond_pub_dt");//起始日
//            	String com_bond_trm = (String)kColl.getDataValue("com_bond_trm");//期限
//            	//计算到期日
//            	String endDate = LmtUtils.computeEndDate(com_bond_pub_dt, "002", com_bond_trm);
//            	kColl.setDataValue("com_bond_end_dt", endDate);
            	dao.insert(kColl, connection);
            	flag = PUBConstant.SUCCESS;
            }
		}catch (EMPException ee) {
			flag = PUBConstant.FAIL;
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		context.addDataField("flag",flag);
		return "0";
	}
}
