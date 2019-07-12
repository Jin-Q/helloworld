package com.yucheng.cmis.biz01line.fnc.op.fncloan;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.biz01line.fnc.detail.component.FncDetailBaseComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.CMISMessage;
import com.yucheng.cmis.pub.PUBConstant;

public class ImportFncLoanOp extends CMISOperation {
	 
	private final String modelId = "FncLoan";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		String returnMessage = "";
		try{
			connection = this.getConnection(context);
			String pk_value = (String)context.getDataValue("FncDetailBase.pk");
			String cus_id = (String)context.getDataValue("FncDetailBase.cus_id");
			String year = (String)context.getDataValue("FncDetailBase.fnc_ym");
			String beforePk = "";
			
			if(pk_value==null){
				throw new EMPException("parent primary key not found!");
			}
			//业务处理类
			FncDetailBaseComponent fncDetailBaseComponent = (FncDetailBaseComponent) CMISComponentFactory
			.getComponentFactoryInstance().getComponentInstance(PUBConstant.FNCDETAILBASE,context, connection);
			//获取上期报表明细基表的主键PK
			beforePk = fncDetailBaseComponent.queryFncDetailBasePk(cus_id, year);
			beforePk =beforePk.trim();
			//不存在上期报表
			if(beforePk.equals("")){
				returnMessage = "noHaveBefore";
			//存在的话
			}else{
				TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
				String strMessage = fncDetailBaseComponent.insertBeforeMess( beforePk, pk_value, modelId, dao);
				strMessage = strMessage.trim();
				if(strMessage.equals("beforeEmpty")){
					returnMessage = "beforeEmpty";
				}else if(strMessage.equals(CMISMessage.SUCCESS)){
					returnMessage = "success";
				}else {
					returnMessage = "fail";
				}
			}
			context.addDataField("returnMess",returnMessage);
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
