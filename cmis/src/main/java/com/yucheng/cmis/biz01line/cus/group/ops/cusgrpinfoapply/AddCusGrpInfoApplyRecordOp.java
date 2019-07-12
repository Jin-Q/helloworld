package com.yucheng.cmis.biz01line.cus.group.ops.cusgrpinfoapply;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.ecc.emp.jdbc.EMPJDBCException;
import com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.CMISComponentFactory;
import com.yucheng.cmis.pub.PUBConstant;
import com.yucheng.cmis.pub.sequence.CMISSequenceService4JXXD;

public class AddCusGrpInfoApplyRecordOp extends CMISOperation {
	
	private final String modelId = "CusGrpInfoApply";
	public String doExecute(Context context) throws EMPException {
 		Connection connection = null;
		String grp_no = null;
		String serno = null;

		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			
			KeyedCollection kColl = null;
			try {
				kColl = (KeyedCollection)context.getDataElement(modelId);
			} catch (Exception e) {}
			if(kColl == null || kColl.size() == 0)throw new EMPJDBCException("The values to insert["+modelId+"] cannot be empty!");
			
			serno = CMISSequenceService4JXXD.querySequenceFromDB("SQ", "all",connection, context);			
			
			if(((String) context.getDataValue("menuId")).equals("grpCognizChg")){
				CusBaseComponent cusComponent = (CusBaseComponent)CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance("CusBase",context,connection);
				cusComponent.delSubmitRecord("grpCognizChg", serno+","+kColl.getDataValue("grp_no"));
				
				kColl.setDataValue("serno", serno);
				dao.insert(kColl, connection);
			}else{
				grp_no = CMISSequenceService4JXXD.querySequenceFromDB("CUSGRP", "fromDate",connection, context);
				kColl.setDataValue("grp_no", grp_no);
				kColl.setDataValue("serno", serno);
				dao.insert(kColl, connection);
				
				CusBaseComponent cusComponent = (CusBaseComponent)CMISComponentFactory
				.getComponentFactoryInstance().getComponentInstance("CusBase",context,connection);
				cusComponent.delSubmitRecord("addGrpCognizMember", serno);
			}			
			
			context.addDataField("serno", serno);
			context.addDataField("flag", PUBConstant.SUCCESS);
		} catch (EMPException ee) {
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