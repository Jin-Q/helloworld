package com.yucheng.cmis.platform.workflow.op.wfilvcreditright;

import java.sql.Connection;
import java.util.Iterator;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ecc.emp.dbmodel.PageInfo;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.util.TableModelUtil;
/**
 * 
*@author lisj
*@time 2015-3-16
*@description TODO 需求编号：【XD150407025】分支机构授信审批权限配置
*                  社区支行授信授权列表页面
*@version v1.0
*
 */
public class QueryCommBranchCreditRightListOp extends CMISOperation {

	private final String modelId = "WfiOrgLifeloanRel";
	
	public String doExecute(Context context) throws EMPException {
		
		Connection connection = null;
		try{
			connection = this.getConnection(context);
		

			KeyedCollection queryData = null;
			try {
				queryData = (KeyedCollection)context.getDataElement("CommBranchCreditRight");
			} catch (Exception e) {}
		
			String conditionStr = TableModelUtil.getQueryCondition(modelId, queryData, context, false, false, false)+" order by is_life_loan asc";
			int size = 10;
			
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			TableModelDAO dao = (TableModelDAO)this.getTableModelDAO(context);

			IndexedCollection iColl = dao.queryList(modelId, null,conditionStr, pageInfo,connection);
			if(iColl!=null && iColl.size()>0){
				for(Iterator<KeyedCollection> iterator = iColl.iterator();iterator.hasNext();){
					KeyedCollection temp = (KeyedCollection)iterator.next();
					temp.addDataField("right_type", "03");//社区权限
				}
			}
			for(Iterator<KeyedCollection> iterator = iColl.iterator();iterator.hasNext();){
				KeyedCollection temp = (KeyedCollection)iterator.next();
				KeyedCollection cbKColl = dao.queryFirst("WfiOrgCbRel", null, "where comm_branch_id='"+temp.getDataValue("org_id")+"'", connection);
				String cb_org_name  = (String) cbKColl.getDataValue("comm_branch_name");
				temp.addDataField("cb_org_name", cb_org_name);
			}
			
			iColl.setName("CommBranchCreditRightList");
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
