package com.yucheng.cmis.biz01line.iqp.op.iqpbksyndic.iqpbksyndicinfo;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;	

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.yucheng.cmis.util.TableModelUtil;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;

public class QueryIqpBksyndicIqpBksyndicInfoListOp extends CMISOperation {
	
	private final String modelId = "IqpBksyndicInfo";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno_value = (String)context.getDataValue("serno");
			
			if(serno_value==null){
				throw new EMPException("parent primary key not found!");
			}
			String conditionStr = "where serno = '" + serno_value+"' order by serno desc";
			
		int size = 10;

		PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
		TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
		List list = new ArrayList();
		list.add("prtcpt_org_no");
		list.add("prtcpt_org_name");
		list.add("prtcpt_role");
		list.add("prtcpt_amt_rate");
		list.add("prtcpt_curr");
		list.add("prtcpt_amt");
		list.add("pk1");
		IndexedCollection iColl = dao.queryList(modelId,list ,conditionStr,pageInfo,connection);
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
