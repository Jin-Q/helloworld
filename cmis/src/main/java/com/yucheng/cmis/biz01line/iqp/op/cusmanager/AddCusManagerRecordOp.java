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

public class AddCusManagerRecordOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "CusManager";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		String serno ="";
		String cont_no ="";
		try{
			connection = this.getConnection(context);
			IndexedCollection iColl = null;
			String pk_id="";
			try {
				 serno= (String)context.getDataValue("serno"); 
				 iColl = (IndexedCollection)context.getDataElement("CusManagerList");
			} catch (Exception e) {}
			if(iColl == null || iColl.size() == 0 || serno==null || "".equals(serno)){
				throw new EMPJDBCException("The values cannot be empty!");
			}
			if(context.containsKey("cont_no")){
		    	cont_no = (String)context.getDataValue("cont_no");
		    }
			CusManagerComponent cmisComponent = (CusManagerComponent)CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance(AppConstant.CUSMANAGERCOMPONENT, context, connection);
			/**通过操作类型判断执行的操作*/
			int result=0;
				int m =cmisComponent.insertCusManagerByIcoll(serno, iColl,cont_no);
				if(m != 1){
					result=result+1;
				}
			    if(result==0){
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
