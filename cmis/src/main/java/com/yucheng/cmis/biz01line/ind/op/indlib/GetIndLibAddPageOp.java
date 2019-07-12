package com.yucheng.cmis.biz01line.ind.op.indlib;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class GetIndLibAddPageOp extends CMISOperation {
	private static final Logger logger = Logger.getLogger(GetIndLibAddPageOp.class);
	
	public String doExecute(Context context) throws EMPException {
		String modelno="";
		KeyedCollection kcoll=new KeyedCollection("IndLib");
		Connection connection = null;
		try{ 
			connection = this.getConnection(context);
			 
			 //从context中取出sequenceService 
            //调用生成方法
		    modelno = CMISSequenceService4JXXD.querySequenceFromDB("I", "fromDate", 
	        		connection, context); 
		    kcoll.addDataField("index_no", modelno);
		    context.addDataElement(kcoll);
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
			throw new EMPException(ex); 
		}finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "";
	}

}
