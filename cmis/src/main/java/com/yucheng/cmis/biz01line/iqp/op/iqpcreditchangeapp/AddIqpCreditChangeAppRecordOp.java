package com.yucheng.cmis.biz01line.iqp.op.iqpcreditchangeapp;

import java.math.BigDecimal;
import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;
import com.yucheng.cmis.pub.util.BigDecimalUtil;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class AddIqpCreditChangeAppRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpCreditChangeApp";
	private final String modelIdGrtLoan = "GrtLoanRGur";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cont_no = null;
			KeyedCollection kCollCredit = new KeyedCollection(modelId); 
			try {
				kCollCredit = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kCollCredit == null || kCollCredit.size() == 0){
				throw new EMPJDBCException("The values modelId "+modelId+" cannot be empty!"); 
			} 
			TableModelDAO dao = this.getTableModelDAO(context); 
			/**往信用证申请表中插入serno*/
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context); 
			kCollCredit.addDataField("serno", serno);  
			dao.insert(kCollCredit, connection); 
			
		    this.putDataElement2Context(kCollCredit, context);
			
		    /**--------------------------------复制业务担保信息start-------------------------------------------------*/
		    cont_no = (String)kCollCredit.getDataValue("cont_no");
		    String conditionStr = "where cont_no='"+cont_no+"'";
		    IndexedCollection iCollRel = dao.queryList(modelIdGrtLoan, conditionStr, connection);
		    
		    for(int i=0;i<iCollRel.size();i++){
		    	KeyedCollection kColl = (KeyedCollection)iCollRel.get(i); 
		    	//只有正常状态的担保合同才复制 
		    	if("1".equals(kColl.getDataValue("corre_rel"))){
		    		String pk_id = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context);
		    		kColl.put("pk_id", pk_id);
		    		kColl.put("serno", serno);
		    		kColl.put("cont_no", "");
		    		kColl.put("corre_rel", "4");
		    		dao.insert(kColl, connection);  
		    	}
		    }
		    /**--------------------------------end-------------------------------------------------*/
		    context.addDataField("flag", "success");
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
