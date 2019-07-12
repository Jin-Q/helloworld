package com.yucheng.cmis.biz01line.batch.op.iqpbatchmng;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.IqpBailComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;

public class CheckIqpBatchMngIsEbillOp extends CMISOperation{

	private final String inModel = "IqpBillDetail";
	private String checked = "2";//默认追加的类型为非电票
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String batchno = "";
		try{
			connection = this.getConnection(context);
            
			try {
				batchno = (String)context.getDataValue("batch_no");
			} catch (Exception e) {}

			TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection relIColl = dao.queryList(inModel, " where porder_no in (select porder_no from iqp_batch_bill_rel where batch_no = '"+batchno+"') and is_ebill = '1' ", connection);
			if(relIColl != null && relIColl.size() > 0){
				checked = "1";
			}else {
				checked = "2";
			}
		    
		    context.put("checked", checked);
		}catch (EMPException ee) {
			context.put("checked", "2");
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return checked;
	}
}
