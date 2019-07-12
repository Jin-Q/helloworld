package com.yucheng.cmis.biz01line.cus.op.custmp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPConstance;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.ecc.emp.log.EMPLog;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.ComponentHelper;
import com.yucheng.cmis.pub.PUBConstant;

public class QueryCusJsonByCertOpForDt  extends CMISOperation {
	
	private  String modelId = "CusBase";
	
	private final String cert_code_name = "cert_code";
	private final String cert_type_name = "cert_type";
	
	private boolean updateCheck = false;
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			if(this.updateCheck){
				RecordRestrict recordRestrict = this.getRecordRestrict(context);
				recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
			}
			
			String cert_code_value = null;
			String cert_type_value=null;
		
			try {
				cert_code_value = (String)context.getDataValue(cert_code_name);
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "证件号码转码前： [" + cert_code_value + "]...", null);
				//cert_code_value= new String(cert_code_value.getBytes("ISO8859-1"),"UTF-8");   //证件号码需要转码  2014-07-15 唐顺岩
				EMPLog.log(EMPConstance.EMP_FLOW, EMPLog.INFO, 0, "证件号码转码前： [" + cert_code_value + "]...", null);
				cert_type_value=(String)context.getDataValue(cert_type_name);
			} catch (Exception e) {}
			if(cert_type_value==null||cert_type_value.equals("")||cert_code_value == null || cert_code_value.length() == 0)
				throw new EMPJDBCException("The value of ["+cert_code_name+","+cert_type_name+"] cannot be null!");
			
			
			KeyedCollection kColl=new KeyedCollection();
			CusBaseComponent cusBaseComponent = (CusBaseComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.CUSBASE,context,connection);
			 ComponentHelper componetHelper = new ComponentHelper();
			 
			kColl=cusBaseComponent.getCusBaseByCert1(cert_code_value,cert_type_value);
			
			IndexedCollection iColl=new IndexedCollection("cusList"); 

			iColl.addDataElement(kColl);
			this.putDataElement2Context(iColl, context);
			
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
