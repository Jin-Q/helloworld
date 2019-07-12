package com.yucheng.cmis.biz01line.iqp.op.cusmanager;

import java.sql.Connection;

import org.jfree.xml.generator.model.KeyDescription;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.appPub.AppConstant;
import com.yucheng.cmis.biz01line.iqp.component.CusManagerComponent;
import com.yucheng.cmis.biz01line.iqp.component.IqpLoanAppComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class CheckCusManagerRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusManager";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String manager_id ="";
		String serno ="";
		try{
			connection = this.getConnection(context);
			try {
				manager_id= (String)context.getDataValue("manager_id"); 
				serno= (String)context.getDataValue("serno"); 
			} catch (Exception e) {}
			if(manager_id == null || "".equals(manager_id)|| serno==null || "".equals(serno))
				throw new EMPJDBCException("The values cannot be empty!");
			
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
			String condition = "where serno='"+serno+"' and manager_id='"+manager_id+"'";  
			IndexedCollection iColl = dao.queryList(modelId, condition, connection);
			if(iColl.size()>0){
				context.addDataField("flag", "error");
			}else{
				context.addDataField("flag", "success");	
			}
			
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
