package com.yucheng.cmis.biz01line.iqp.op.iqpextensionagr;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.component.CatalogManaComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryIqpExtensionAgrDetailOp  extends CMISOperation {
	
	private final String modelId = "IqpExtensionAgr";
	

	private final String serno_name = "serno";
	
	
	private boolean updateCheck = true;
	

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			/*** tab页传agr_no，不传serno ***/
			if(context.containsKey("agr_no")){
				KeyedCollection kcoll = new KeyedCollection();
				kcoll.addDataField("value", context.getDataValue("agr_no"));
				CatalogManaComponent cmisComponent = (CatalogManaComponent) CMISComponentFactory
						.getComponentFactoryInstance().getComponentInstance("CatalogManaComponent", context, connection);
				kcoll = cmisComponent.excuteSql("getSnByHt", kcoll);				
				if(context.containsKey("serno")){
					context.setDataValue("serno", (String)kcoll.getDataValue("result"));
				}else{
					context.addDataField("serno", (String)kcoll.getDataValue("result"));
				}
			}
		
			if(this.updateCheck){
				if(context.containsKey("restrictUsed")&&(context.getDataValue("restrictUsed").toString()).equals("false")){
					
				}else{
					RecordRestrict recordRestrict = this.getRecordRestrict(context);
					recordRestrict.judgeUpdateRestrict(this.modelId, context, connection);
				}				
			}
			
			
			String serno_value = null;			
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			

			
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");

			
		
			TableModelDAO dao = this.getTableModelDAO(context);
			KeyedCollection kColl = dao.queryDetail(modelId, serno_value, connection);
			SInfoUtils.addSOrgName(kColl, new String[] { "manager_br_id" ,"input_br_id"});
			SInfoUtils.addUSerName(kColl, new String[] { "manager_id" ,"input_id",});
			String[] args=new String[] { "cus_id","fount_cont_no","fount_bill_no"};
			String[] modelIds=new String[]{"CusBase","CtrLoanCont","AccLoan"};
			String[] modelForeign=new String[]{"cus_id","cont_no","bill_no"};
			String[] fieldName=new String[]{"cus_name","serno","prd_id"};
			String[] resultName = new String[] { "cus_id_displayname","fount_serno","prd_id"};
			SystemTransUtils.dealPointName(kColl, args, SystemTransUtils.ADD, context, modelIds, modelForeign, fieldName,resultName);
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
