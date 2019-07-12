package com.yucheng.cmis.biz01line.mort.mortguarantybaseinfo;

import java.sql.Connection;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.mort.component.MortCommenOwnerComponent;
import com.yucheng.cmis.biz01line.mort.morttool.MORTConstant;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;

public class GetAgrInfoByGuarantyNoOp extends CMISOperation {
	
	//operation TableModel
	private final String modelId = "MortGuarantyBaseInfo";
	
	/**
	 * bussiness logic operation
	 */
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		
		try{
			connection = this.getConnection(context);
			
			TableModelDAO dao = this.getTableModelDAO(context);
			
			try {
					String guaranty_no =(String) context.getDataValue("guaranty_no");
					//构建组件类
					MortCommenOwnerComponent reComponent = (MortCommenOwnerComponent) CMISComponentFactory
					.getComponentFactoryInstance().getComponentInstance(
							MORTConstant.MORTCOMMENOWNERCOMPONENT,context, connection);
					KeyedCollection kc = reComponent.queryCarOverReRecordDetail(guaranty_no);
					String agr_type = "";
					String agr_no = "";
					if(kc!=null){
						agr_type = kc.getDataValue("agr_type").toString();
						agr_no = kc.getDataValue("agr_no").toString();
					}
					context.addDataField("agr_type", agr_type);
					context.addDataField("agr_no", agr_no);
			} catch (Exception e) {}
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
