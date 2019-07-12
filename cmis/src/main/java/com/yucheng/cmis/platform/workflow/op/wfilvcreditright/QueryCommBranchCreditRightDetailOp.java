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
*@description TODO 需求编号：【XD150407025】分支机构授信审批权限配置(社区支行)
*@version v1.0
*
 */
public class QueryCommBranchCreditRightDetailOp  extends CMISOperation {
	
	private final String modelId = "WfiLvCreditRight";
	private final String WfiLifeLoanRelId = "WfiOrgLifeloanRel";
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			TableModelDAO dao = this.getTableModelDAO(context);
			String cb_org_id = (String) context.getDataValue("cb_org_id");
			//查选该社区支行审批权限配置
			int size = 10;
			PageInfo pageInfo = TableModelUtil.createPageInfo(context, "one", String.valueOf(size));
			IndexedCollection CBRIColl = dao.queryList(modelId, null,"where right_type ='03' and org_id='"+cb_org_id+"' order by new_crd_amt asc", pageInfo,connection);
			//翻译社区支行名称
			KeyedCollection cbKColl = dao.queryFirst("WfiOrgCbRel", null, "where comm_branch_id='"+cb_org_id+"'", connection);
			String cb_org_name  = (String) cbKColl.getDataValue("comm_branch_name");
			for(Iterator<KeyedCollection> iterator = CBRIColl.iterator();iterator.hasNext();){
				KeyedCollection temp = (KeyedCollection)iterator.next();
				temp.addDataField("cb_org_name", cb_org_name);
			}

			CBRIColl.setName("WfiLvCreditRightList");
			this.putDataElement2Context(CBRIColl, context);
			TableModelUtil.parsePageInfo(context, pageInfo);
			KeyedCollection lifeLoanRel =new KeyedCollection("WfiOrgLifeloanRel");
			//生活贷权限配置
			if(context.containsKey("pk_id")){
				String pk_id  = (String) context.getDataValue("pk_id");
				lifeLoanRel = dao.queryDetail(WfiLifeLoanRelId, pk_id, connection);
			}else{
				lifeLoanRel = dao.queryFirst(WfiLifeLoanRelId, null, "where org_id='"+cb_org_id+"'", connection);
				lifeLoanRel.setDataValue("org_id", cb_org_id);
			}
			lifeLoanRel.addDataField("cb_org_name", cb_org_name);
			this.putDataElement2Context(lifeLoanRel, context);
			
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
