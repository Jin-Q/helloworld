package com.yucheng.cmis.biz01line.iqp.op.iqpguarchangeapp;

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

public class AddIqpGuarChangeAppRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "IqpGuarChangeApp";
	private final String modelIdCtr = "CtrLoanCont";
	private final String modelIdGrtLoan = "GrtLoanRGur";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String cont_no = null;
            String oldserno = null;
			KeyedCollection kCollGuar = new KeyedCollection(modelId);
			try {
				kCollGuar = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {} 
			if(kCollGuar == null || kCollGuar.size()==0){
				throw new EMPJDBCException("The value kCollGuar "+modelId+" cannot be empty!");
			}
			TableModelDAO dao = this.getTableModelDAO(context);
			String serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all", connection, context); 
			kCollGuar.addDataField("serno", serno);  
			dao.insert(kCollGuar, connection);
			
			/**--------------------------------复制业务担保信息start-------------------------------------------------*/
		    cont_no = (String)kCollGuar.getDataValue("cont_no");
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
