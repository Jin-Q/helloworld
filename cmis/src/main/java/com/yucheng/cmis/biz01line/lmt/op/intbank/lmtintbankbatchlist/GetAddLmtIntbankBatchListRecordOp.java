package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtintbankbatchlist;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class GetAddLmtIntbankBatchListRecordOp extends CMISOperation {
	

private final String modelId = "LmtIntbankBatchList";
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)
				throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			TableModelDAO dao = this.getTableModelDAO(context);
//			String cdt_lvl = kColl.getDataValue("cdt_lvl").toString();
			//系统自动生成流水号
			String serno_value =CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			//系统自动生成批量客户号
			String batch_no_value = CMISSequenceService4JXXD.querySequenceFromDB("lmtintbank", "fromDate", connection, context);
			kColl.setDataValue("batch_cus_no", batch_no_value);
			kColl.setDataValue("serno", serno_value);
			
			//同一级别的批量包不能有两个进行同时授信
//			String batch_cus_type = (String)kColl.getDataValue("batch_cus_type");
			
//			String cnname = Tools.getComCdeCnName(context, "STD_ZB_BATCH_CUS_TYPE", batch_cus_type);
			
//			IndexedCollection iColl = dao.queryList(modelId, "where status= '01' and cdt_lvl='"+cdt_lvl+"' and batch_cus_type ='"+batch_cus_type+"'", connection);
//			if(iColl.size()>0){
//				context.addDataField("flag", "fail");
//				context.addDataField("message", "已存在等级为["+cdt_lvl+"]级,批量客户类型为["+cnname+"]且状态为[登记]的批量包!");
//				context.addDataField("serno", serno_value);//用于保存数据后进行跳转
//			}else{
				dao.insert(kColl, connection);
				context.addDataField("flag", "success");
				context.addDataField("message", "");
				context.addDataField("serno", serno_value);
//			}
			
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
