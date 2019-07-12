package com.yucheng.cmis.biz01line.iqp.op.pub.oversee.iqpappoverseeorg;


import java.sql.Connection;

import com.ecc.emp.core.Context;
import com.ecc.emp.core.EMPException;
import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.dbmodel.service.TableModelDAO;
import com.yucheng.cmis.operation.CMISOperation;
import com.yucheng.cmis.pub.PUBConstant;
/**
 * 异步查询是否存在监管机构
 * @param context context对象
 * @author ZYF
 */
public class QueryOverseeOrgOp extends CMISOperation{
	
	public String doExecute(Context context) throws EMPException {
		Connection connection = null;
		try{
			connection = this.getConnection(context);
			
			String oversee_org_id = context.getDataValue("oversee_org_id").toString();
			TableModelDAO dao = this.getTableModelDAO(context);
			
			String conditionStr = "where oversee_org_id='"+oversee_org_id+"' and approve_status in('000','993','111','992')";
			IndexedCollection iCollApp = dao.queryList("IqpAppOverseeOrg", conditionStr, connection);
			
			if( iCollApp.size()<=0 || iCollApp==null){//不存在在途的监管机构
				context.addDataField("flag", PUBConstant.SUCCESS);
			}else{
				context.addDataField("flag", PUBConstant.FAIL);
			}
		}catch (EMPException ee) {
			context.addDataField("result", ee.getMessage());
			throw ee;
		} catch(Exception e){
			context.addDataField("result", e.getMessage());
			throw new EMPException(e);
		} finally {
			if (connection != null)
				this.releaseConnection(context, connection);
		}
		return "0";
	}
}
