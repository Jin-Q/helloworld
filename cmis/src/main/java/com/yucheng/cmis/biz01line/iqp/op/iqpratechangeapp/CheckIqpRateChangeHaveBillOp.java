package com.yucheng.cmis.biz01line.iqp.op.iqpratechangeapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;

public class CheckIqpRateChangeHaveBillOp  extends CMISOperation {
	
	private final String modelId = "IqpRateChangeApp";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		    KeyedCollection kColl = new KeyedCollection("IqpRateChangeApp");
			String bill_no = null;
			try {
				bill_no = (String)context.getDataValue("bill_no");
			} catch (Exception e) {}
			if(bill_no == null || "".equals(bill_no))
				throw new EMPJDBCException("The value bill_no cannot be null!");
			//查询条件：判断是否存在在途的利率变更申请
		    String condition = "where bill_no='"+bill_no+"' and approve_status in ('000','991','111','992','993')";
			//查询条件：如果审批通过的申请，继续发起利率变更，则从通过的利率变更申请中取数,
		    //取最新的调整后利率生效日期 的一笔数据,
//		    String conditionStr = "where bill_no='"+bill_no+"' and approve_status not in ('000','991','111','992','993') order by new_inure_date";

		    TableModelDAO dao = this.getTableModelDAO(context);
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			if(iColl.size()==0){
//				kColl = dao.queryFirst(modelId, null, conditionStr, connection);
//				if(kColl != null){
//                  String serno = (String)kColl.getDataValue("serno");
//                  if(serno!=null && !"".equals(serno)){
//                	  context.addDataField("flag", "have");
//                  }else{
                	  context.addDataField("flag", "success");
//                  }
//				}
			}else{
				context.addDataField("flag", "error");
			}
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
