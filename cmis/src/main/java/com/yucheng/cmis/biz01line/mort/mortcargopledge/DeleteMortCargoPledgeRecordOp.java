package com.yucheng.cmis.biz01line.mort.mortcargopledge;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.iqp.component.OverseeUnderstoreComponent;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeleteMortCargoPledgeRecordOp extends CMISOperation {

	private final String modelId = "MortCargoPledge";
	

	private final String cargo_id_name = "cargo_id";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		int count=0;
		try{
			connection = this.getConnection(context);
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String cargo_id_value = null;
			try {
				cargo_id_value = (String)context.getDataValue(cargo_id_name);
			} catch (Exception e) {}
			if(cargo_id_value == null || cargo_id_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+cargo_id_name+"] cannot be null!");
				

			MortCommenOwnerComponent mortCom = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(
					MORTConstant.MORTCOMMENOWNERCOMPONENT,context,connection);
			TableModelDAO dao = this.getTableModelDAO(context);
			if(context.containsKey("action")){
				String action = (String) context.getDataValue("action");
				if("zh".equals(action)){
					//count=dao.deleteByPk("MortCargoReplList", cargo_id_value, connection);
					mortCom.deleteDateByTableAndCondition("Mort_Cargo_Repl_List", " where cargo_id = '"+cargo_id_value+"' ");
					dao.deleteByPk(modelId, cargo_id_value, connection);
				}
			}else{
				 dao.deleteByPk(modelId, cargo_id_value, connection);
			}
			
			context.addDataField("flag","success");
			
			
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
