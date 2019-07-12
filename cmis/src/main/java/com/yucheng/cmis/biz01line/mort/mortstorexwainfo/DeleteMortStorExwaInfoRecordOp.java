package com.yucheng.cmis.biz01line.mort.mortstorexwainfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.RecordRestrict;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.dao.SqlClient;

public class DeleteMortStorExwaInfoRecordOp extends CMISOperation {

	private final String modelId = "MortStorExwaInfo";
	private final String modelIdDet = "MortStorExwaDetail";

	private final String serno_name = "serno";

	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			RecordRestrict recordRestrict = this.getRecordRestrict(context);
			recordRestrict.judgeDeleteRestrict(this.modelId, context, connection);

			String serno_value = null;
			try {
				serno_value = (String)context.getDataValue(serno_name);
			} catch (Exception e) {}
			if(serno_value == null || serno_value.length() == 0)
				throw new EMPJDBCException("The value of pk["+serno_name+"] cannot be null!");
			TableModelDAO dao = this.getTableModelDAO(context);
			int count=dao.deleteByPk(modelId, serno_value, connection);
			
			/***剔除出库申请中权证信息将状态改为“在库” add by tangzf 2014.04.25********/
			String condition = " where serno='"+serno_value+"'";
			IndexedCollection iCollDet = dao.queryList(modelIdDet, condition, connection);
			for(int i=0;i<iCollDet.size();i++){
				KeyedCollection kCollParam = new KeyedCollection();
				KeyedCollection kCollDet = (KeyedCollection)iCollDet.get(i);
				kCollParam.put("warrant_no", kCollDet.getDataValue("warrant_no"));
				kCollParam.put("warrant_type",kCollDet.getDataValue("warrant_type"));
				SqlClient.update("updateMortCertiStatus", kCollParam, "3", null, connection);
			}
			
			//构建组件类
			MortCommenOwnerComponent mortStor = (MortCommenOwnerComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(MORTConstant.MORTCOMMENOWNERCOMPONENT, context, connection);
			//删除出库申请明细表中的数据
			int count1 = mortStor.deleteMortStorExwaDetailBySerno(serno_value);
			if(count!=1){
				throw new EMPException("Remove Failed! Records :"+count);
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
