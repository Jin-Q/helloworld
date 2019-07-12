package com.yucheng.cmis.biz01line.iqp.op.iqpguarantchangeapp;

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

public class AddIqpGuarantChangeAppRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpGuarantChangeApp";
	private final String modelIdCtr = "CtrLoanCont";
	private final String modelIdGuarant = "IqpGuarantInfo";
	private final String modelIdGrtLoan = "GrtLoanRGur";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cont_no = null;
			String bill_no = null;
			String oldserno = null;
			KeyedCollection kCollGuarant = new KeyedCollection(modelId);
			try {
				kCollGuarant =  (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kCollGuarant == null || kCollGuarant.size() == 0){
				throw new EMPJDBCException("The values modelId "+modelId+" cannot be empty!"); 
			} 
			TableModelDAO dao = this.getTableModelDAO(context);  
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context); 
			kCollGuarant.addDataField("serno", serno);
            dao.insert(kCollGuarant, connection);  
			
		    this.putDataElement2Context(kCollGuarant, context);
		    
		    /**--------------------------------复制业务担保信息start-------------------------------------------------*/
		    cont_no = (String)kCollGuarant.getDataValue("cont_no");
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
