package com.yucheng.cmis.biz01line.mort.mortguarantycertiinfo;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.util.SInfoUtils;
import com.yucheng.cmis.pub.util.SystemTransUtils;

public class QueryMortGuarantyCertiInfoAddListOp extends CMISOperation {


	private final String modelId = "MortStorExwaDetail";
	

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			String serno = (String) context.getDataValue("serno");
			IndexedCollection iColl = dao.queryList(modelId,"where serno='"+serno+"'",connection);
			iColl.setName("MortGuarantyCertiInfoList");
			/*** 源表字段必须要和翻译表主键建立对应关系 ***/
			KeyedCollection TransKcoll = new KeyedCollection();
			TransKcoll.addDataField("warrant_no", "warrant_no");
			TransKcoll.addDataField("warrant_type", "warrant_type");
			
			SystemTransUtils.dealUnionTrans(iColl, context, 
					"MortGuarantyCertiInfo",TransKcoll , "warrant_no,warrant_type" , "warrant_cls", "warrant_cls");
			SystemTransUtils.dealUnionTrans(iColl, context, 
					"MortGuarantyCertiInfo",TransKcoll , "warrant_no,warrant_type" , "keep_org_no", "keep_org_no");
			SystemTransUtils.dealUnionTrans(iColl, context, 
					"MortGuarantyCertiInfo",TransKcoll , "warrant_no,warrant_type" , "warrant_name", "warrant_name");
			SystemTransUtils.dealUnionTrans(iColl, context, 
					"MortGuarantyCertiInfo",TransKcoll , "warrant_no,warrant_type" , "is_main_warrant", "is_main_warrant");
			SInfoUtils.addSOrgName(iColl, new String[] {"keep_org_no"});
			this.putDataElement2Context(iColl, context);
		}catch (EMPException ee) {
			throw ee;
		} catch(Exception e){
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return null;
		
	}

}
