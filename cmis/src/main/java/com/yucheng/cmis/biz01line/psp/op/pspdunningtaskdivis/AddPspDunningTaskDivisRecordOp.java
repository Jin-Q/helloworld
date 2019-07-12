package com.yucheng.cmis.biz01line.psp.op.pspdunningtaskdivis;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddPspDunningTaskDivisRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "PspDunningTaskDivis";
	
	/**
	 * bussiness logic operation
	 */
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
			
			//生成流水号
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
			kColl.setDataValue("serno", serno);
			//add a record
			TableModelDAO dao = this.getTableModelDAO(context);
			dao.insert(kColl, connection);
			//借据编号
//			String acc_no = (String)kColl.getDataValue("acc_no");
//			KeyedCollection kCollAcc = dao.queryDetail("AccLoan", acc_no, connection);
			//插入催收信息表中
//			kColl.addDataField("dunning_date", context.getDataValue("OPENDAY"));//催收进入时间
//			kColl.addDataField("totl_dunning_qnt", kCollAcc.getDataValue("overdue"));//逾期期数
//			kColl.addDataField("twelve_class", kCollAcc.getDataValue("twelve_cls_flg"));//十二级分类
//			kColl.setName("PspDunningTaskInfo");
//			dao.insert(kColl, connection);
			
			context.addDataField("flag", PUBConstant.SUCCESS);
		}catch (EMPException ee) {
			context.addDataField("flag", PUBConstant.FAIL);
			throw ee;
		} catch(Exception e){
			context.addDataField("flag", PUBConstant.FAIL);
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
