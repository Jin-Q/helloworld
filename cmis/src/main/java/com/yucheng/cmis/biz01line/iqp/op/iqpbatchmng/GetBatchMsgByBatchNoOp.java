package com.yucheng.cmis.biz01line.iqp.op.iqpbatchmng;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.DataElement;
import com.ecc.emp.data.DataField;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class GetBatchMsgByBatchNoOp extends CMISOperation {
	private static final String batchModel = "IqpBatchMng";
	/** 
	 * 通过批次号获取批次信息
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try {
			connection = this.getConnection(context);
			String batch_no = "";
			if(context.containsKey("batch_no")){
				batch_no = (String)context.getDataValue("batch_no");
			}
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			KeyedCollection kcBatch = dao.queryAllDetail(batchModel, batch_no, connection);
			context.addDataField("serno", "");
			context.addDataField("biz_type", "");
			context.addDataField("bill_type", "");
			context.addDataField("bill_qnt", "");
			context.addDataField("bill_total_amt", "");
			context.addDataField("fore_disc_date", "");
			context.addDataField("rate", "");
			context.addDataField("int_amt", "");
			context.addDataField("rpay_amt", "");
			context.addDataField("rebuy_date", "");
			context.addDataField("rebuy_rate", "");
			context.addDataField("rebuy_int", "");
			context.addDataField("opp_org_no", "");
			context.addDataField("opp_org_name", "");
			//context.addDataField("drft_amt", "");
			//context.addDataField("porder_addr", "");
			

			insertMsg2KColl(kcBatch, context);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.releaseConnection(context, connection);
		}
		return null;
	}

	/**
	 * 将需要转换的KColl放入上下文中
	 * @param kColl
	 * @param context
	 * @throws Exception
	 */
	public void insertMsg2KColl(KeyedCollection kColl,Context context) throws Exception{
		for(int i=0;i<kColl.size();i++){
			DataElement element = (DataElement) kColl.getDataElement(i);
			if (element instanceof DataField) {
				DataField aField = (DataField) element;
				String value = "";
				if(aField.getValue() == null){
					value = "";
				}else {
					value = aField.getValue().toString();
				}
				if(context.containsKey(aField.getName())){
					context.setDataValue(aField.getName(), value);
				}else {
					context.addDataField(aField.getName(), value);
				}
				
			}
		}
	}
}
