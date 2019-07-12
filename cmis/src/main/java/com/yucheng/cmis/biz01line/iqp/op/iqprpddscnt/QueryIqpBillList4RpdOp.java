package com.yucheng.cmis.biz01line.iqp.op.iqprpddscnt;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpBillList4RpdOp extends CMISOperation {
	private final String batchModel = "IqpBatchMng";
	private final String billModel = "IqpBillDetail";
	private final String relModel = "IqpBatchBillRel";
	/**
	 * 通过批次号查询批次下面的列表信息
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			String batch_no = "";
			
			if(context.containsKey("batch_no")){
				batch_no = (String)context.getDataValue("batch_no");
			}
			IndexedCollection relIColl = null;
			
			//从批次信息中获取票据种类，并且放入context中。
			KeyedCollection batchKcoll = dao.queryAllDetail(batchModel, batch_no, connection);
			if(batch_no!=null&&!batch_no.equals("")){
				if(!context.containsKey("bill_type")){
					context.addDataField("bill_type", batchKcoll.getDataValue("bill_type"));
				}else{
					context.setDataValue("bill_type", batchKcoll.getDataValue("bill_type"));
				}
				if(!context.containsKey("rpay_amt")){
					context.addDataField("rpay_amt", batchKcoll.getDataValue("rpay_amt"));
				}else{
					context.setDataValue("rpay_amt", batchKcoll.getDataValue("rpay_amt"));
				}
				if(!context.containsKey("int_amt")){
					context.addDataField("int_amt", batchKcoll.getDataValue("int_amt"));
				}else{
					context.setDataValue("int_amt", batchKcoll.getDataValue("int_amt"));
				}
			}
			
			relIColl = dao.queryList(relModel, " where batch_no = '"+batch_no+"'", connection);
			String conditionStr = "";
			if(relIColl != null && relIColl.size() > 0){
				for(int i=0;i<relIColl.size();i++){
					KeyedCollection kcOne = (KeyedCollection)relIColl.get(i);
					conditionStr += "'"+kcOne.getDataValue("porder_no")+"',";
				}
				conditionStr = " where porder_no in (" + conditionStr.substring(0, conditionStr.length()-1) + ") ";
			}else {
				conditionStr = " where porder_no in ('')";
			}
			
			//将批次号batch_no放入context中。
			if(!context.containsKey("batch_no")){
				context.addDataField("batch_no", batch_no);
			}else{
				context.setDataValue("batch_no", batch_no);
			}
			
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
			IndexedCollection iColl = dao.queryList(billModel,null ,conditionStr,pageInfo,connection);
			iColl.setName(iColl.getName()+"List");
			String is_ebill = "";
			//添加贴现日期显示
			for(int i=0;i<iColl.size();i++){
				KeyedCollection kColl = (KeyedCollection) iColl.get(i);
				String porder_no = (String) kColl.getDataValue("porder_no");
				is_ebill = (String) kColl.getDataValue("is_ebill");
				Map<String,String> incomeMap = new HashMap<String,String>();
				incomeMap.put("batch_no",batch_no);
				incomeMap.put("porder_no",porder_no);
				KeyedCollection incomeKColl = dao.queryDetail("IqpBillIncome", incomeMap, connection);
				kColl.addDataField("fore_disc_date", incomeKColl.getDataValue("fore_disc_date"));
			}
			context.put("is_ebill", is_ebill);
			this.putDataElement2Context(iColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
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
