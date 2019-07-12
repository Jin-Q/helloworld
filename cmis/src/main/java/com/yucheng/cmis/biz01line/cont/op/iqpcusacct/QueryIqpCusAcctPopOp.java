package com.yucheng.cmis.biz01line.cont.op.iqpcusacct;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.PageInfo;	
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;

public class QueryIqpCusAcctPopOp extends CMISOperation {
	private final String modelId = "IqpCusAcct";

	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			String serno = null;
			String conditionStr = null;
			try {
				serno = (String)context.getDataValue("serno");
			} catch (Exception e) {
				throw new Exception("请检查业务流水号!");
			}
			//费用账户的账户信息
		    conditionStr = "where serno= '"+serno+"' and acct_attr='07' order by serno desc,cont_no desc";	
			
			int size = 10;
		
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
		
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);
		
			List<String> list = new ArrayList<String>();
			list.add("serno"); 
			list.add("pk_id");
			list.add("acct_attr");
			list.add("acct_no");
			list.add("acct_name");
			list.add("opac_org_no");
			list.add("opan_org_name");
			list.add("pay_amt");
			list.add("is_this_org_acct");
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
