package com.yucheng.cmis.biz01line.mort.cargo.mortcargostorage;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class DeleteMortCargoStorageRecordOp extends CMISOperation {

	private final String modelId = "MortCargoStorage";
	

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);
			String serno_value = null;
			String guaranty_no = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);//入库管理的主键
				guaranty_no = (String) context.getDataValue("guaranty_no");//获取押品编号（用来对之前操作过的货物状态信息进行还原）
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			//构建组件
			MortCommenOwnerComponent mortCom = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(
					MORTConstant.MORTCOMMENOWNERCOMPONENT, context, connection);
			mortCom.updateStatusBatch("04","01",guaranty_no,context);//将入库待记账状态的货物还原为登记状态
			Map<String,String> refFields = new HashMap<String,String>();
            refFields.put("serno", serno_value);
			mortCom.deleteByField("MortCargoReplList",refFields);
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, serno_value, connection);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
			}else{
				context.addDataField("flag","success");
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
