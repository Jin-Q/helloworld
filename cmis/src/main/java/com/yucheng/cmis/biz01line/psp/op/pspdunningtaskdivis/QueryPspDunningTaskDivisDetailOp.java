package com.yucheng.cmis.biz01line.psp.op.pspdunningtaskdivis;

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

public class QueryPspDunningTaskDivisDetailOp  extends CMISOperation {
	
	private final String modelId = "PspDunningTaskDivis";
	
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
			
			/*String bill_no = null;
			try {
				bill_no = (String)context.getDataValue("bill_no");
			} catch (Exception e) {}
			if(bill_no == null || bill_no.length() == 0)
				throw new EMPJDBCException("The value of pk[bill_no] cannot be null!");*/
			
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			
			/*//借据编号
			String acc_no = (String)kColl.getDataValue("acc_no");
			KeyedCollection kCollView = dao.queryDetail("AccView", acc_no, connection);
			String tableModel = (String)kCollView.getDataValue("table_model");
			KeyedCollection kCollAcc = dao.queryDetail(tableModel, acc_no, connection);
			//插入催收信息表中
			kColl.addDataField("dunning_date", kColl.getDataValue("input_date"));//催收进入时间先取任务登记时间
			kColl.addDataField("totl_dunning_qnt", kCollView.getDataValue("overdue"));//逾期期数
			kColl.addDataField("twelve_class", kCollAcc.getDataValue("twelve_cls_flg"));//十二级分类
			
			kColl.put("five_class", kCollView.getDataValue("five_class"));//五级分类
			kColl.put("overdue_date", kCollView.getDataValue("overdue_date"));//逾期日期
			kColl.put("totl_dunning_qnt", kCollView.getDataValue("overdue"));//逾期期数
			
			kCollView.setName(modelId);
			String[] args=new String[] { "cus_id" ,"prd_id"};
			String[] modelIds=new String[]{"CusBase" , "PrdBasicinfo"};
			String[] modelForeign=new String[]{"cus_id","prdid"};
			String[] fieldName=new String[]{"cus_name","prdname"};*/
			//详细信息翻译时调用			
			//SystemTransUtils.dealName(kCollView, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName);
			SInfoUtils.addSOrgName(kColl, new String[] { "exe_br_id","divis_br_id","input_br_id" });
			SInfoUtils.addUSerName(kColl, new String[] { "exe_id","divis_id","input_id","cus_id" });
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
