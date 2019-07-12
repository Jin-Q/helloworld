package com.yucheng.cmis.biz01line.cont.op.ctrrpddscntcont;

import java.sql.Connection;

import javax.sql.DataSource;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.base.CMISConstance;
import com.yucheng.cmis.biz01line.cont.component.IqpAppendTermsComponent;
import com.yucheng.cmis.biz01line.cont.pub.ContConstant;
import com.yucheng.cmis.biz01line.grt.msi.GrtServiceInterface;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISModualServiceFactory;

public class RemoveCtrRpddscntContOp extends CMISOperation {
	private final String modelId = "CtrRpddscntCont";
	private final String modelIdGrtLoan = "GrtLoanRGur";
	private final String cont_no_name = "cont_no";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		
			String cont_no_value = null;
			try {
				cont_no_value = (String)context.getDataValue(cont_no_name);
			} catch (Exception e) {}
			if(cont_no_value == null || cont_no_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cont_no_name+"] cannot be null!");

			TableModelDAO dao = this.getTableModelDAO(context);
			
			//跟新合同状态
            KeyedCollection kColl = dao.queryDetail(modelId, cont_no_value, connection);
			kColl.setDataValue("cont_status", "800");//撤销操作把合同状态改为作废
			dao.update(kColl, connection);
			
			//增加贴现处理
			String serno = (String) kColl.getDataValue("serno");
			KeyedCollection kCollForBatch = null;
			String conditionstr = " where serno='"+serno+"'";
			kCollForBatch=dao.queryFirst("IqpBatchMng", null, conditionstr, connection);
			if(kCollForBatch.getDataValue("batch_no")!=null&&!(kCollForBatch.getDataValue("batch_no")).equals("")){
				//kCollForBatch.setDataValue("cont_no", "");//清掉批次与合同关系
				//kCollForBatch.setDataValue("serno", "");//清掉批次与申请关系
				kCollForBatch.setDataValue("status", "04");//改回【作废】状态
				dao.update(kCollForBatch, connection);
			}
			
			context.addDataField("flag", "success");
	      
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
