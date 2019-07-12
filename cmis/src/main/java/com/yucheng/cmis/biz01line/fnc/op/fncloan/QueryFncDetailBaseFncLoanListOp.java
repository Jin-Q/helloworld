package com.yucheng.cmis.biz01line.fnc.op.fncloan;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryFncDetailBaseFncLoanListOp extends CMISOperation {
	
	private final String modelId = "FncLoan";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String pk_value = (String)context.getDataValue("FncDetailBase.pk");
			
			if(pk_value==null){
				throw new EMPException("parent primary key not found!");
			}
			
			
			String conditionStr = "where pk = '" + pk_value+"' order by seq desc,cus_id desc,fnc_ym desc";
			int size = 10;
			//����ֻ�ڵ�һ�β�ѯ�ܼ�¼��
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		IndexedCollection iColl = dao.queryList(modelId, null,conditionStr,pageInfo,connection);
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
