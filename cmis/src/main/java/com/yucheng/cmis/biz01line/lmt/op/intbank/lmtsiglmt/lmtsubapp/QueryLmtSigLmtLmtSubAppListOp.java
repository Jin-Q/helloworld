package com.yucheng.cmis.biz01line.lmt.op.intbank.lmtsiglmt.lmtsubapp;

import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.util.TableModelUtil;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryLmtSigLmtLmtSubAppListOp extends CMISOperation {
	
	private final String modelId = "LmtSubApp";
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno_value = (String)context.getDataValue("serno");
			
			if(serno_value==null){
				throw new EMPException("parent primary key not found!");
			}			
			String conditionStr = "where serno = '" + serno_value+"' ";
		int size = 10;
		PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		IndexedCollection iColl = dao.queryList(modelId,null ,conditionStr,pageInfo,connection);
		
		if(!(iColl.size()>0)){
		KeyedCollection kColl_dic = (KeyedCollection)context.getDataElement("dictColl");
		IndexedCollection ic = (IndexedCollection)kColl_dic.getDataElement("STD_ZB_INTBANK_LMT");
		
		String cnname = null;
		String enname =null;
		for (int i=0;i<ic.size();i++ ){
			KeyedCollection kColl_Sub = new KeyedCollection();
			kColl_Sub = (KeyedCollection)ic.get(i);
			cnname=(String)kColl_Sub.getDataValue("cnname");
			enname=(String)kColl_Sub.getDataValue("enname");
			KeyedCollection kColl = new KeyedCollection("LmtSubApp");
			kColl.addDataField("serno", serno_value);
			kColl.addDataField("variet_no", enname);
			kColl.addDataField("variet_name", cnname);
			iColl.addDataElement(kColl);
		}
	}

		iColl.setName(iColl.getName()+"List");
		this.putDataElement2Context(iColl, context);
		TableModelUtil.parsePageInfo(context, pageInfo);

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
